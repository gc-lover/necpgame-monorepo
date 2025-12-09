package server

import (
	"context"
	"fmt"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"

	api "github.com/gc-lover/necpgame-monorepo/services/matchmaking-go/pkg/api"
)

func TestService_EnterQueue_PersistsAndCaches(t *testing.T) {
	repo, repoCleanup := newTestRepository(t)
	defer repoCleanup()
	cache, cacheCleanup := newTestCache(t)
	defer cacheCleanup()
	truncateMatchmakingTables(t, repo.db)

	service := NewService(repo, cache)

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	playerID := uuid.New()
	req := &api.EnterQueueRequest{
		ActivityType: api.EnterQueueRequestActivityTypePvp5v5,
	}

	resp, err := service.EnterQueue(ctx, playerID, req)
	require.NoError(t, err)
	require.NotNil(t, resp)
	assert.NotEqual(t, uuid.Nil, resp.QueueId)

	entry, err := repo.GetQueueEntry(ctx, resp.QueueId)
	require.NoError(t, err)
	assert.Equal(t, "waiting", entry.Status)
	assert.Equal(t, playerID, entry.PlayerID)

	inQueue, err := cache.IsPlayerInQueue(ctx, playerID)
	require.NoError(t, err)
	assert.True(t, inQueue)
}

func TestService_GetQueueStatus_CacheAndDbFallback(t *testing.T) {
	repo, repoCleanup := newTestRepository(t)
	defer repoCleanup()
	cache, cacheCleanup := newTestCache(t)
	defer cacheCleanup()
	truncateMatchmakingTables(t, repo.db)

	service := NewService(repo, cache)

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	queueID := uuid.New()
	cached := &api.QueueStatusResponse{
		QueueId:     queueID,
		Status:      api.QueueStatusResponseStatusWaiting,
		TimeInQueue: 5,
		RatingRange: []int32{1400, 1600},
	}
	err := cache.CacheQueueStatus(ctx, queueID, cached, 30*time.Second)
	require.NoError(t, err)

	res, err := service.GetQueueStatus(ctx, queueID)
	require.NoError(t, err)
	assert.Equal(t, cached.QueueId, res.QueueId)
	assert.Equal(t, cached.Status, res.Status)

	// Cache miss -> fallback to DB
	_, err = cache.client.Del(ctx, fmt.Sprintf("queue:status:%s", queueID)).Result()
	require.NoError(t, err)

	entry := &QueueEntry{
		ID:           queueID,
		PlayerID:     uuid.New(),
		ActivityType: "pvp_5v5",
		Rating:       1550,
		Status:       "waiting",
		EnteredAt:    time.Now().Add(-15 * time.Second).UTC(),
	}
	err = repo.InsertQueueEntry(ctx, entry)
	require.NoError(t, err)

	res, err = service.GetQueueStatus(ctx, queueID)
	require.NoError(t, err)
	assert.Equal(t, queueID, res.QueueId)
	assert.Equal(t, api.QueueStatusResponseStatus("waiting"), res.Status)
	assert.True(t, res.TimeInQueue >= 0)
}

func TestService_LeaveQueue_RemovesFromMatcher(t *testing.T) {
	repo, repoCleanup := newTestRepository(t)
	defer repoCleanup()
	cache, cacheCleanup := newTestCache(t)
	defer cacheCleanup()
	truncateMatchmakingTables(t, repo.db)

	service := NewService(repo, cache)

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	entry := &QueueEntry{
		ID:           uuid.New(),
		PlayerID:     uuid.New(),
		ActivityType: "pvp_5v5",
		Rating:       1520,
		Status:       "waiting",
		EnteredAt:    time.Now().Add(-20 * time.Second).UTC(),
	}
	require.NoError(t, repo.InsertQueueEntry(ctx, entry))
	service.matcher.AddToQueue(entry)

	resp, err := service.LeaveQueue(ctx, entry.ID)
	require.NoError(t, err)
	assert.Equal(t, api.LeaveQueueResponseStatusCancelled, resp.Status)

	updated, err := repo.GetQueueEntry(ctx, entry.ID)
	require.NoError(t, err)
	assert.Equal(t, "cancelled", updated.Status)

	size := service.matcher.GetQueueSize(entry.ActivityType, entry.Rating)
	assert.Equal(t, 0, size)
}

func TestService_GetPlayerRating_ReturnsData(t *testing.T) {
	repo, repoCleanup := newTestRepository(t)
	defer repoCleanup()
	cache, cacheCleanup := newTestCache(t)
	defer cacheCleanup()
	truncateMatchmakingTables(t, repo.db)

	service := NewService(repo, cache)

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	playerID := uuid.New()
	_, err := repo.db.ExecContext(ctx, `
		INSERT INTO player_ratings (player_id, activity_type, current_rating, peak_rating, wins, losses, draws, current_streak, tier, league, season_id)
		VALUES ($1, 'pvp_5v5', 1750, 1800, 15, 3, 0, 4, 'platinum', 2, 'current')
	`, playerID)
	require.NoError(t, err)

	res, err := service.GetPlayerRating(ctx, playerID)
	require.NoError(t, err)
	require.Len(t, res.Ratings, 1)
	assert.Equal(t, int32(1750), res.Ratings[0].CurrentRating)
	assert.Equal(t, "pvp_5v5", res.Ratings[0].ActivityType)
}

func TestCalculateWaitTime(t *testing.T) {
	tests := []struct {
		name      string
		queueSize int
		expected  int
	}{
		{"small queue", 5, 30},
		{"medium queue", 25, 60},
		{"large queue", 75, 120},
		{"very large queue", 150, 180},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			result := calculateWaitTime(tt.queueSize)
			assert.Equal(t, tt.expected, result)
		})
	}
}
