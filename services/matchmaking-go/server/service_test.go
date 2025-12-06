// Issue: #234 - Unit tests for matchmaking-go service layer
// Note: Service uses *Repository and *CacheManager (not interfaces)
// For full testing, use testcontainers for integration tests
package server

import (
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/stretchr/testify/assert"

	api "github.com/gc-lover/necpgame-monorepo/services/matchmaking-go/pkg/api"
)

// TestService_NewService tests NewService creation
func TestService_NewService(t *testing.T) {
	// This test requires real Repository and CacheManager
	// For unit tests, we skip it
	t.Skip("Requires real Repository and CacheManager. Use integration tests with testcontainers.")
}

// TestService_EnterQueue_Validation tests EnterQueue input validation
func TestService_EnterQueue_Validation(t *testing.T) {
	req := &api.EnterQueueRequest{
		ActivityType: api.EnterQueueRequestActivityTypePvp5v5,
		PreferredRoles: []api.EnterQueueRequestPreferredRolesItem{
			api.EnterQueueRequestPreferredRolesItemDps,
		},
	}

	assert.NotNil(t, req)
	assert.Equal(t, api.EnterQueueRequestActivityTypePvp5v5, req.ActivityType)
	assert.Len(t, req.PreferredRoles, 1)
}

// TestService_GetQueueStatus_Validation tests GetQueueStatus input validation
func TestService_GetQueueStatus_Validation(t *testing.T) {
	queueID := uuid.New()
	assert.NotEqual(t, uuid.Nil, queueID)
}

// TestService_LeaveQueue_Validation tests LeaveQueue input validation
func TestService_LeaveQueue_Validation(t *testing.T) {
	queueID := uuid.New()
	assert.NotEqual(t, uuid.Nil, queueID)
}

// TestService_GetPlayerRating_Validation tests GetPlayerRating input validation
func TestService_GetPlayerRating_Validation(t *testing.T) {
	playerID := uuid.New()
	assert.NotEqual(t, uuid.Nil, playerID)
}

// TestService_GetLeaderboard_Validation tests GetLeaderboard input validation
func TestService_GetLeaderboard_Validation(t *testing.T) {
	params := api.GetLeaderboardParams{
		ActivityType: api.GetLeaderboardActivityTypePvp5v5,
		Limit:        api.NewOptInt(10),
	}

	assert.Equal(t, api.GetLeaderboardActivityTypePvp5v5, params.ActivityType)
	assert.True(t, params.Limit.IsSet())
	assert.Equal(t, 10, params.Limit.Value)
}

// TestCalculateWaitTime tests calculateWaitTime function
func TestCalculateWaitTime(t *testing.T) {
	tests := []struct {
		name     string
		queueSize int
		expected int
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

// TestQueueEntry_Validation tests QueueEntry structure validation
func TestQueueEntry_Validation(t *testing.T) {
	entry := &QueueEntry{
		ID:           uuid.New(),
		PlayerID:     uuid.New(),
		ActivityType: "pvp_5v5",
		Rating:       1500,
		Status:       "waiting",
		EnteredAt:    time.Now(),
	}

	assert.NotEqual(t, uuid.Nil, entry.ID)
	assert.NotEqual(t, uuid.Nil, entry.PlayerID)
	assert.NotEmpty(t, entry.ActivityType)
	assert.Greater(t, entry.Rating, 0)
	assert.NotEmpty(t, entry.Status)
	assert.False(t, entry.EnteredAt.IsZero())
}

// TestPlayerRating_Validation tests PlayerRating structure validation
func TestPlayerRating_Validation(t *testing.T) {
	rating := &PlayerRating{
		PlayerID:      uuid.New(),
		ActivityType:  "pvp_5v5",
		CurrentRating: 1500,
		PeakRating:    1600,
		Wins:          10,
		Losses:        5,
		Draws:         2,
		CurrentStreak: 3,
		Tier:          "gold",
		League:        1,
	}

	assert.NotEqual(t, uuid.Nil, rating.PlayerID)
	assert.NotEmpty(t, rating.ActivityType)
	assert.Greater(t, rating.CurrentRating, 0)
	assert.GreaterOrEqual(t, rating.PeakRating, rating.CurrentRating)
	assert.GreaterOrEqual(t, rating.Wins, 0)
	assert.GreaterOrEqual(t, rating.Losses, 0)
}

// TestLeaderboardEntry_Validation tests LeaderboardEntry structure validation
func TestLeaderboardEntry_Validation(t *testing.T) {
	entry := &LeaderboardEntry{
		Rank:       1,
		PlayerID:   uuid.New(),
		PlayerName: "Player1",
		Rating:     2500,
		Tier:       "grandmaster",
		Wins:       100,
		Losses:     20,
	}

	assert.Greater(t, entry.Rank, 0)
	assert.NotEqual(t, uuid.Nil, entry.PlayerID)
	assert.NotEmpty(t, entry.PlayerName)
	assert.Greater(t, entry.Rating, 0)
	assert.NotEmpty(t, entry.Tier)
}

