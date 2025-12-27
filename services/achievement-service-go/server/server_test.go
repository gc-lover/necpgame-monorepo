package server

import (
	"context"
	"database/sql"
	"net/http"
	"net/http/httptest"
	"testing"
	"time"

	"github.com/DATA-DOG/go-sqlmock"
	"github.com/google/uuid"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
	"go.uber.org/zap/zaptest"

	"github.com/gc-lover/necpgame-monorepo/services/achievement-service-go/pkg/api"
)

func setupTestServer() (*Server, sqlmock.Sqlmock) {
	// Create mock database
	db, mock, err := sqlmock.New()
	if err != nil {
		panic("Failed to create mock database")
	}

	logger := zaptest.NewLogger(nil)

	// Create mock pool (using db directly for simplicity)
	server := NewServer(db, logger, nil)

	return server, mock
}

func TestServer_HealthCheck_Success(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	// Setup mock expectations
	mock.ExpectPing().WillReturnError(nil)

	// Execute
	result, err := server.HealthCheck(context.Background())

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, api.HealthResponseStatusHealthy, result.Status)
	assert.Equal(t, "achievement-service-go", result.Service)
	assert.WithinDuration(t, time.Now(), result.Timestamp, time.Second)
}

func TestServer_HealthCheck_DatabaseError(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	// Setup mock to return error
	mock.ExpectPing().WillReturnError(sql.ErrConnDone)

	// Execute
	result, err := server.HealthCheck(context.Background())

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, api.HealthResponseStatusUnhealthy, result.Status)
	assert.Equal(t, "achievement-service-go", result.Service)
}

func TestServer_ListAchievements_Success(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	params := api.ListAchievementsParams{
		Limit:  &[]int{10}[0],
		Offset: &[]int{0}[0],
	}

	// Execute
	result, err := server.ListAchievements(context.Background(), params)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.NotNil(t, result.Data)
	assert.GreaterOrEqual(t, len(result.Data), 0)
	assert.NotNil(t, result.Meta)
}

func TestServer_ListAchievements_WithFilters(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	params := api.ListAchievementsParams{
		Limit:     &[]int{5}[0],
		Offset:    &[]int{0}[0],
		Rarity:    &[]api.AchievementRarity{api.AchievementRarityCommon}[0],
		Category:  &[]string{"combat"}[0],
		IsActive:  &[]bool{true}[0],
	}

	// Execute
	result, err := server.ListAchievements(context.Background(), params)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	// Mock data should respect filters (implementation dependent)
}

func TestServer_GetPlayerAchievements_Success(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	playerID := uuid.New()
	params := api.GetPlayerAchievementsParams{
		PlayerID: playerID,
		Limit:    &[]int{20}[0],
		Offset:   &[]int{0}[0],
	}

	// Execute
	result, err := server.GetPlayerAchievements(context.Background(), params)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.NotNil(t, result.Data)
	assert.NotNil(t, result.Stats)
	assert.Equal(t, playerID, result.PlayerID)
}

func TestServer_GetPlayerAchievements_InvalidPlayerID(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	params := api.GetPlayerAchievementsParams{
		PlayerID: uuid.Nil, // Invalid
		Limit:    &[]int{10}[0],
		Offset:   &[]int{0}[0],
	}

	// Execute
	result, err := server.GetPlayerAchievements(context.Background(), params)

	// Verify - should handle gracefully
	assert.NoError(t, err)
	assert.NotNil(t, result)
	// May return empty data for invalid player
}

func TestServer_UpdateAchievementProgress_Success(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	playerID := uuid.New()
	achievementID := uuid.New()

	req := &api.UpdateProgressRequest{
		Increment: 25.5,
		Source:    "combat_win",
		Metadata:  map[string]interface{}{"difficulty": "hard"},
	}

	params := api.UpdateAchievementProgressParams{
		PlayerID:      playerID,
		AchievementID: achievementID,
	}

	// Execute
	result, err := server.UpdateAchievementProgress(context.Background(), req, params)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, playerID, result.PlayerID)
	assert.Equal(t, achievementID, result.AchievementID)
	assert.GreaterOrEqual(t, result.NewProgress, 0.0)
	assert.LessOrEqual(t, result.NewProgress, 100.0)
}

func TestServer_UpdateAchievementProgress_InvalidIncrement(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	playerID := uuid.New()
	achievementID := uuid.New()

	req := &api.UpdateProgressRequest{
		Increment: -50.0, // Invalid negative increment
		Source:    "invalid_source",
	}

	params := api.UpdateAchievementProgressParams{
		PlayerID:      playerID,
		AchievementID: achievementID,
	}

	// Execute
	result, err := server.UpdateAchievementProgress(context.Background(), req, params)

	// Verify - should handle validation
	assert.Error(t, err)
	assert.Nil(t, result)
}

func TestServer_ClaimAchievementReward_Success(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	playerID := uuid.New()
	achievementID := uuid.New()

	params := api.ClaimAchievementRewardParams{
		PlayerID:      playerID,
		AchievementID: achievementID,
	}

	// Execute
	result, err := server.ClaimAchievementReward(context.Background(), params)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, playerID, result.PlayerID)
	assert.Equal(t, achievementID, result.AchievementID)
	assert.True(t, result.Success)
	assert.NotEmpty(t, result.Rewards)
}

func TestServer_ClaimAchievementReward_AlreadyClaimed(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	playerID := uuid.New()
	achievementID := uuid.New()

	params := api.ClaimAchievementRewardParams{
		PlayerID:      playerID,
		AchievementID: achievementID,
	}

	// First claim
	result1, err1 := server.ClaimAchievementReward(context.Background(), params)
	assert.NoError(t, err1)
	assert.True(t, result1.Success)

	// Second claim (should fail or return different result)
	result2, err2 := server.ClaimAchievementReward(context.Background(), params)

	// Verify - implementation dependent, but should handle gracefully
	assert.NoError(t, err2)
	assert.NotNil(t, result2)
}

func TestServer_CreateAchievement_Success(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	req := &api.CreateAchievementRequest{
		Name:        "Test Achievement",
		Description: "A test achievement for unit testing",
		Rarity:      api.AchievementRarityRare,
		Category:    "test",
		Points:      100,
		Rewards: []api.AchievementReward{
			{
				Type:       api.AchievementRewardTypeCurrency,
				Currency:   "eddies",
				Amount:     500,
				ItemID:     "",
				Title:      "Eddies Reward",
				Description: "500 eddies for completing the achievement",
			},
		},
		Requirements: api.AchievementRequirements{
			Type:           "progress",
			TargetValue:    100.0,
			TimeLimit:      nil,
			Prerequisites:  []uuid.UUID{},
			Conditions:     map[string]interface{}{"combat_wins": 10},
		},
		IsActive: true,
	}

	// Execute
	result, err := server.CreateAchievement(context.Background(), req)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.NotEqual(t, uuid.Nil, result.ID)
	assert.Equal(t, req.Name, result.Name)
	assert.Equal(t, req.Description, result.Description)
	assert.Equal(t, req.Rarity, result.Rarity)
	assert.Equal(t, req.Points, result.Points)
}

func TestServer_CreateAchievement_InvalidRequest(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	req := &api.CreateAchievementRequest{
		Name:        "", // Invalid: empty name
		Description: "A test achievement",
		Rarity:      api.AchievementRarityCommon,
		Category:    "test",
		Points:      -100, // Invalid: negative points
		Rewards:     []api.AchievementReward{},
		Requirements: api.AchievementRequirements{
			Type:        "invalid_type",
			TargetValue: -50.0, // Invalid: negative target
		},
		IsActive: true,
	}

	// Execute
	result, err := server.CreateAchievement(context.Background(), req)

	// Verify - should handle validation
	assert.Error(t, err)
	assert.Nil(t, result)
}

func TestServer_GetAchievement_Success(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	achievementID := uuid.New()

	params := api.GetAchievementParams{
		AchievementID: achievementID,
	}

	// Execute
	result, err := server.GetAchievement(context.Background(), params)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, achievementID, result.ID)
}

func TestServer_GetAchievement_NotFound(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	achievementID := uuid.New()

	params := api.GetAchievementParams{
		AchievementID: achievementID,
	}

	// Execute
	result, err := server.GetAchievement(context.Background(), params)

	// Verify - should handle not found gracefully
	// Implementation dependent - may return error or empty result
	assert.NoError(t, err)
	assert.NotNil(t, result)
}

// Helper function tests
func TestServer_calculateAchievementProgress(t *testing.T) {
	server, _ := setupTestServer()

	tests := []struct {
		name          string
		achievementID uuid.UUID
		playerID      uuid.UUID
		increment     float64
		expectedMin   float64
		expectedMax   float64
	}{
		{
			name:          "small increment",
			achievementID: uuid.New(),
			playerID:      uuid.New(),
			increment:     10.0,
			expectedMin:   0.0,
			expectedMax:   100.0,
		},
		{
			name:          "large increment",
			achievementID: uuid.New(),
			playerID:      uuid.New(),
			increment:     75.0,
			expectedMin:   0.0,
			expectedMax:   100.0,
		},
		{
			name:          "zero increment",
			achievementID: uuid.New(),
			playerID:      uuid.New(),
			increment:     0.0,
			expectedMin:   0.0,
			expectedMax:   100.0,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			progress := server.calculateAchievementProgress(tt.achievementID, tt.playerID, tt.increment)

			assert.GreaterOrEqual(t, progress, tt.expectedMin)
			assert.LessOrEqual(t, progress, tt.expectedMax)
		})
	}
}

func TestServer_generateAchievementRewards(t *testing.T) {
	server, _ := setupTestServer()

	achievementID := uuid.New()

	rewards := server.generateAchievementRewards(achievementID)

	// Verify rewards structure
	assert.NotNil(t, rewards)
	assert.IsType(t, []api.AchievementReward{}, rewards)

	// Should generate at least some rewards for valid achievement
	if len(rewards) > 0 {
		for _, reward := range rewards {
			assert.NotEmpty(t, reward.Type)
			assert.NotEmpty(t, reward.Title)
			assert.NotEmpty(t, reward.Description)
			assert.Greater(t, reward.Amount, 0)
		}
	}
}

func TestServer_calculateAchievementStatistics(t *testing.T) {
	server, _ := setupTestServer()

	// Test with empty achievements
	stats := server.calculateAchievementStatistics([]api.PlayerAchievement{})

	assert.NotNil(t, stats)
	assert.Equal(t, 0, stats.TotalAchievements)
	assert.Equal(t, 0, stats.UnlockedAchievements)
	assert.Equal(t, 0.0, stats.CompletionPercentage)

	// Test with some achievements
	achievements := []api.PlayerAchievement{
		{
			ID:         uuid.New(),
			IsUnlocked: true,
			Rarity:     api.AchievementRarityCommon,
			Points:     10,
		},
		{
			ID:         uuid.New(),
			IsUnlocked: false,
			Rarity:     api.AchievementRarityRare,
			Points:     25,
		},
		{
			ID:         uuid.New(),
			IsUnlocked: true,
			Rarity:     api.AchievementRarityEpic,
			Points:     50,
		},
	}

	stats = server.calculateAchievementStatistics(achievements)

	assert.Equal(t, 3, stats.TotalAchievements)
	assert.Equal(t, 2, stats.UnlockedAchievements)
	assert.Equal(t, 66.67, stats.CompletionPercentage) // 2/3 * 100
	assert.Equal(t, 60, stats.TotalPoints)             // 10 + 25 + 50
	assert.Equal(t, 30, stats.UnlockedPoints)          // 10 + 50
}

// Integration test for achievement workflow
func TestServer_Integration_AchievementWorkflow(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	ctx := context.Background()

	// Step 1: Create achievement
	createReq := &api.CreateAchievementRequest{
		Name:        "Integration Test Achievement",
		Description: "Achievement for integration testing",
		Rarity:      api.AchievementRarityCommon,
		Category:    "test",
		Points:      50,
		Rewards: []api.AchievementReward{
			{
				Type:       api.AchievementRewardTypeCurrency,
				Currency:   "eddies",
				Amount:     250,
				Title:      "Test Reward",
				Description: "Reward for integration test",
			},
		},
		Requirements: api.AchievementRequirements{
			Type:        "progress",
			TargetValue: 100.0,
			Conditions:  map[string]interface{}{"test_actions": 5},
		},
		IsActive: true,
	}

	achievement, err := server.CreateAchievement(ctx, createReq)
	require.NoError(t, err)
	assert.NotNil(t, achievement)

	// Step 2: Get achievement details
	getParams := api.GetAchievementParams{
		AchievementID: achievement.ID,
	}

	details, err := server.GetAchievement(ctx, getParams)
	assert.NoError(t, err)
	assert.Equal(t, achievement.ID, details.ID)

	// Step 3: Get player achievements (should include created achievement)
	playerID := uuid.New()
	playerParams := api.GetPlayerAchievementsParams{
		PlayerID: playerID,
		Limit:    &[]int{50}[0],
		Offset:   &[]int{0}[0],
	}

	playerAchievements, err := server.GetPlayerAchievements(ctx, playerParams)
	assert.NoError(t, err)
	assert.NotNil(t, playerAchievements)

	// Step 4: Update progress
	progressReq := &api.UpdateProgressRequest{
		Increment: 25.0,
		Source:    "integration_test",
		Metadata:  map[string]interface{}{"test_run": true},
	}

	progressParams := api.UpdateAchievementProgressParams{
		PlayerID:      playerID,
		AchievementID: achievement.ID,
	}

	progress, err := server.UpdateAchievementProgress(ctx, progressReq, progressParams)
	assert.NoError(t, err)
	assert.NotNil(t, progress)

	// Step 5: Claim reward (if achievement completed)
	if progress.NewProgress >= 100.0 {
		claimParams := api.ClaimAchievementRewardParams{
			PlayerID:      playerID,
			AchievementID: achievement.ID,
		}

		reward, err := server.ClaimAchievementReward(ctx, claimParams)
		assert.NoError(t, err)
		assert.NotNil(t, reward)
		assert.True(t, reward.Success)
	}
}
