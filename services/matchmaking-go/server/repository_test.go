package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func TestRepository_InsertAndGetQueueEntry(t *testing.T) {
	repo, cleanup := newTestRepository(t)
	defer cleanup()

	truncateMatchmakingTables(t, repo.db)

	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	entry := &QueueEntry{
		ID:           uuid.New(),
		PlayerID:     uuid.New(),
		ActivityType: "pvp_5v5",
		Rating:       1500,
		Status:       "waiting",
		EnteredAt:    time.Now().UTC(),
	}

	err := repo.InsertQueueEntry(ctx, entry)
	require.NoError(t, err)

	stored, err := repo.GetQueueEntry(ctx, entry.ID)
	require.NoError(t, err)

	assert.Equal(t, entry.ID, stored.ID)
	assert.Equal(t, entry.PlayerID, stored.PlayerID)
	assert.Equal(t, entry.ActivityType, stored.ActivityType)
	assert.Equal(t, entry.Status, stored.Status)

	err = repo.UpdateQueueStatus(ctx, entry.ID, "matched")
	require.NoError(t, err)

	updated, err := repo.GetQueueEntry(ctx, entry.ID)
	require.NoError(t, err)
	assert.Equal(t, "matched", updated.Status)
}

func TestRepository_GetPlayerRating_DefaultAndExisting(t *testing.T) {
	repo, cleanup := newTestRepository(t)
	defer cleanup()

	truncateMatchmakingTables(t, repo.db)

	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	playerID := uuid.New()

	rating, err := repo.GetPlayerRating(ctx, playerID, "pvp_5v5")
	require.NoError(t, err)
	assert.Equal(t, 1500, rating)

	_, err = repo.db.ExecContext(ctx, `
		INSERT INTO player_ratings (player_id, activity_type, current_rating, peak_rating, wins, losses, draws, current_streak, tier, league, season_id)
		VALUES ($1, $2, 1800, 1900, 10, 2, 0, 5, 'diamond', 2, 'current')
	`, playerID, "pvp_5v5")
	require.NoError(t, err)

	rating, err = repo.GetPlayerRating(ctx, playerID, "pvp_5v5")
	require.NoError(t, err)
	assert.Equal(t, 1800, rating)
}

func TestRepository_GetPlayerRatingsAndLeaderboard(t *testing.T) {
	repo, cleanup := newTestRepository(t)
	defer cleanup()

	truncateMatchmakingTables(t, repo.db)

	ctx, cancel := context.WithTimeout(context.Background(), 15*time.Second)
	defer cancel()

	playerA := uuid.New()
	playerB := uuid.New()

	_, err := repo.db.ExecContext(ctx, `
		INSERT INTO players (id, username) VALUES ($1, 'Alpha'), ($2, 'Bravo');
	`, playerA, playerB)
	require.NoError(t, err)

	_, err = repo.db.ExecContext(ctx, `
		INSERT INTO player_ratings (player_id, activity_type, current_rating, peak_rating, wins, losses, draws, current_streak, tier, league, season_id)
		VALUES 
			($1, 'pvp_5v5', 2000, 2100, 20, 5, 0, 4, 'diamond', 3, 'current'),
			($2, 'pvp_5v5', 1700, 1750, 12, 8, 1, 2, 'gold', 2, 'current');
	`, playerA, playerB)
	require.NoError(t, err)

	ratings, err := repo.GetPlayerRatings(ctx, playerA)
	require.NoError(t, err)
	require.Len(t, ratings, 1)
	assert.Equal(t, 2000, ratings[0].CurrentRating)
	assert.Equal(t, "pvp_5v5", ratings[0].ActivityType)

	leaderboard, err := repo.GetLeaderboard(ctx, "pvp_5v5", "current", 10)
	require.NoError(t, err)
	require.Len(t, leaderboard, 2)
	assert.Equal(t, playerA, leaderboard[0].PlayerID)
	assert.Equal(t, "Alpha", leaderboard[0].PlayerName)
	assert.Equal(t, 2000, leaderboard[0].Rating)
	assert.Equal(t, playerB, leaderboard[1].PlayerID)
}

func TestRepository_BatchInsertQueueEntries(t *testing.T) {
	repo, cleanup := newTestRepository(t)
	defer cleanup()

	truncateMatchmakingTables(t, repo.db)

	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	now := time.Now().UTC()
	entries := []*QueueEntry{
		{
			ID:           uuid.New(),
			PlayerID:     uuid.New(),
			ActivityType: "pvp_5v5",
			Rating:       1500,
			Status:       "waiting",
			EnteredAt:    now,
		},
		{
			ID:           uuid.New(),
			PlayerID:     uuid.New(),
			ActivityType: "pvp_5v5",
			Rating:       1520,
			Status:       "waiting",
			EnteredAt:    now.Add(time.Second),
		},
	}

	err := repo.BatchInsertQueueEntries(ctx, entries)
	require.NoError(t, err)

	var count int
	err = repo.db.QueryRowContext(ctx, `SELECT COUNT(*) FROM matchmaking_queues`).Scan(&count)
	require.NoError(t, err)
	assert.Equal(t, 2, count)
}
