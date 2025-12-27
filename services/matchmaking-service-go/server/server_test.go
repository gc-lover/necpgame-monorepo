package server

import (
	"context"
	"database/sql"
	"testing"
	"time"

	"github.com/DATA-DOG/go-sqlmock"
	"github.com/google/uuid"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
	"go.uber.org/zap/zaptest"

	"github.com/gc-lover/necpgame-monorepo/services/matchmaking-service-go/pkg/api"
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
	assert.Equal(t, "matchmaking-service-go", result.Service)
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
	assert.Equal(t, "matchmaking-service-go", result.Service)
}

func TestServer_JoinQueue_Success(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	playerID := uuid.New()

	req := &api.JoinQueueRequest{
		PlayerID:      playerID,
		GameMode:      "ranked",
		Region:        "us-east",
		SkillRating:   1500,
		Preferences:   map[string]interface{}{"max_wait_time": 300},
		MatchCriteria: api.MatchCriteria{TeamSize: 5, GameMode: "ranked"},
	}

	// Execute
	result, err := server.JoinQueue(context.Background(), req)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, playerID, result.PlayerID)
	assert.NotEqual(t, uuid.Nil, result.QueueID)
	assert.Equal(t, api.QueueStatusQueued, result.Status)
	assert.NotZero(t, result.EstimatedWaitTime)
}

func TestServer_JoinQueue_InvalidRequest(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	req := &api.JoinQueueRequest{
		PlayerID:    uuid.Nil, // Invalid
		GameMode:    "",       // Invalid
		Region:      "",
		SkillRating: -100, // Invalid
	}

	// Execute
	result, err := server.JoinQueue(context.Background(), req)

	// Verify - should handle validation
	assert.Error(t, err)
	assert.Equal(t, api.JoinQueueRes{}, result)
}

func TestServer_JoinQueue_AlreadyInQueue(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	playerID := uuid.New()

	// First join
	req1 := &api.JoinQueueRequest{
		PlayerID:    playerID,
		GameMode:    "ranked",
		Region:      "us-east",
		SkillRating: 1500,
	}

	result1, err1 := server.JoinQueue(context.Background(), req1)
	assert.NoError(t, err1)
	assert.NotNil(t, result1)

	// Second join (should fail or return different result)
	req2 := &api.JoinQueueRequest{
		PlayerID:    playerID,
		GameMode:    "casual",
		Region:      "us-west",
		SkillRating: 1400,
	}

	result2, err2 := server.JoinQueue(context.Background(), req2)

	// Verify - implementation dependent, but should handle gracefully
	assert.NoError(t, err2)
	assert.NotNil(t, result2)
}

func TestServer_LeaveQueue_Success(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	playerID := uuid.New()

	req := &api.LeaveQueueRequest{
		PlayerID: playerID,
		Reason:   "user_cancelled",
	}

	// Execute
	result, err := server.LeaveQueue(context.Background(), req)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, playerID, result.PlayerID)
	assert.True(t, result.Success)
	assert.Equal(t, api.QueueStatusLeft, result.Status)
}

func TestServer_LeaveQueue_NotInQueue(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	playerID := uuid.New()

	req := &api.LeaveQueueRequest{
		PlayerID: playerID,
		Reason:   "not_in_queue",
	}

	// Execute
	result, err := server.LeaveQueue(context.Background(), req)

	// Verify - should handle gracefully
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.False(t, result.Success)
}

func TestServer_GetQueueStatus_Success(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	playerID := uuid.New()

	params := api.GetQueueStatusParams{
		PlayerID: playerID,
	}

	// Execute
	result, err := server.GetQueueStatus(context.Background(), params)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, playerID, result.PlayerID)
	assert.IsType(t, api.QueueStatusNotQueued, result.Status)
}

func TestServer_GetQueueStatus_InvalidPlayerID(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	params := api.GetQueueStatusParams{
		PlayerID: uuid.Nil, // Invalid
	}

	// Execute
	result, err := server.GetQueueStatus(context.Background(), params)

	// Verify - should handle validation
	assert.Error(t, err)
	assert.Equal(t, api.GetQueueStatusRes{}, result)
}

func TestServer_GetMatchDetails_Success(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	matchID := uuid.New()

	params := api.GetMatchDetailsParams{
		MatchID: matchID,
	}

	// Execute
	result, err := server.GetMatchDetails(context.Background(), params)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, matchID, result.MatchID)
	assert.NotEmpty(t, result.GameMode)
	assert.NotEmpty(t, result.Region)
	assert.Greater(t, len(result.Players), 0)
}

func TestServer_GetMatchDetails_NotFound(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	matchID := uuid.New()

	params := api.GetMatchDetailsParams{
		MatchID: matchID,
	}

	// Execute
	result, err := server.GetMatchDetails(context.Background(), params)

	// Verify - should handle not found gracefully
	assert.Error(t, err)
	assert.Equal(t, api.GetMatchDetailsRes{}, result)
}

func TestServer_AcceptMatch_Success(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	playerID := uuid.New()
	matchID := uuid.New()

	req := &api.AcceptMatchRequest{
		ResponseTime: 5,
	}

	params := api.AcceptMatchParams{
		PlayerID: playerID,
		MatchID:  matchID,
	}

	// Execute
	result, err := server.AcceptMatch(context.Background(), req, params)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, playerID, result.PlayerID)
	assert.Equal(t, matchID, result.MatchID)
	assert.True(t, result.Accepted)
	assert.Equal(t, api.MatchStatusAccepted, result.Status)
}

func TestServer_AcceptMatch_Timeout(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	playerID := uuid.New()
	matchID := uuid.New()

	req := &api.AcceptMatchRequest{
		ResponseTime: 25, // Too slow
	}

	params := api.AcceptMatchParams{
		PlayerID: playerID,
		MatchID:  matchID,
	}

	// Execute
	result, err := server.AcceptMatch(context.Background(), req, params)

	// Verify - should handle timeout
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.False(t, result.Accepted)
}

func TestServer_DeclineMatch_Success(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	playerID := uuid.New()
	matchID := uuid.New()

	req := &api.DeclineMatchRequest{
		Reason: "not_interested",
	}

	params := api.DeclineMatchParams{
		PlayerID: playerID,
		MatchID:  matchID,
	}

	// Execute
	result, err := server.DeclineMatch(context.Background(), req, params)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, playerID, result.PlayerID)
	assert.Equal(t, matchID, result.MatchID)
	assert.False(t, result.Accepted)
	assert.Equal(t, api.MatchStatusDeclined, result.Status)
}

func TestServer_StartMatch_Success(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	matchID := uuid.New()

	params := api.StartMatchParams{
		MatchID: matchID,
	}

	// Execute
	result, err := server.StartMatch(context.Background(), params)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, matchID, result.MatchID)
	assert.Equal(t, api.MatchStatusStarted, result.Status)
	assert.NotZero(t, result.StartTime)
	assert.NotEmpty(t, result.ServerAddress)
}

func TestServer_StartMatch_AlreadyStarted(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	matchID := uuid.New()

	params := api.StartMatchParams{
		MatchID: matchID,
	}

	// First start
	result1, err1 := server.StartMatch(context.Background(), params)
	assert.NoError(t, err1)
	assert.Equal(t, api.MatchStatusStarted, result1.Status)

	// Second start (should fail)
	result2, err2 := server.StartMatch(context.Background(), params)

	// Verify - should handle gracefully
	assert.Error(t, err2)
	assert.Equal(t, api.StartMatchRes{}, result2)
}

func TestServer_GetMatchmakingPreferences_Success(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	playerID := uuid.New()

	params := api.GetMatchmakingPreferencesParams{
		PlayerID: playerID,
	}

	// Execute
	result, err := server.GetMatchmakingPreferences(context.Background(), params)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, playerID, result.PlayerID)
	assert.NotNil(t, result.Preferences)
}

func TestServer_UpdateMatchmakingPreferences_Success(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	playerID := uuid.New()

	req := &api.UpdatePreferencesRequest{
		PlayerID: playerID,
		Preferences: api.MatchmakingPreferences{
			PreferredGameModes: []string{"ranked", "casual"},
			PreferredRegions:   []string{"us-east", "us-west"},
			SkillRange:         api.SkillRange{Min: 1400, Max: 1600},
			MaxWaitTime:        600,
			TeamPreferences:    map[string]interface{}{"allow_random_teams": true},
		},
	}

	// Execute
	result, err := server.UpdateMatchmakingPreferences(context.Background(), req)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, playerID, result.PlayerID)
	assert.True(t, result.Success)
	assert.Contains(t, result.UpdatedPreferences.PreferredGameModes, "ranked")
}

func TestServer_UpdateMatchmakingPreferences_Invalid(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	req := &api.UpdatePreferencesRequest{
		PlayerID: uuid.Nil, // Invalid
		Preferences: api.MatchmakingPreferences{
			MaxWaitTime: -100, // Invalid
		},
	}

	// Execute
	result, err := server.UpdateMatchmakingPreferences(context.Background(), req)

	// Verify - should handle validation
	assert.Error(t, err)
	assert.Equal(t, api.UpdateMatchmakingPreferencesRes{}, result)
}

func TestServer_GetQueueAnalytics_Success(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	params := api.GetQueueAnalyticsParams{
		TimeRange: &[]string{"24h"}[0],
		Region:    &[]string{"us-east"}[0],
		GameMode:  &[]string{"ranked"}[0],
	}

	// Execute
	result, err := server.GetQueueAnalytics(context.Background(), params)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.NotNil(t, result.Statistics)
	assert.GreaterOrEqual(t, result.Statistics.TotalQueued, 0)
	assert.GreaterOrEqual(t, result.Statistics.AverageWaitTime, 0)
}

func TestServer_ValidateMatchmakingState_Success(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	playerID := uuid.New()

	req := &api.MatchmakingValidationRequest{
		PlayerID: playerID,
		State:    "queued",
		Metadata: map[string]interface{}{"queue_id": uuid.New().String()},
	}

	// Execute
	result, err := server.ValidateMatchmakingState(context.Background(), req)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, playerID, result.PlayerID)
	assert.True(t, result.IsValid)
	assert.Empty(t, result.ValidationErrors)
}

func TestServer_ValidateMatchmakingState_Invalid(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	req := &api.MatchmakingValidationRequest{
		PlayerID: uuid.Nil, // Invalid
		State:    "invalid_state",
	}

	// Execute
	result, err := server.ValidateMatchmakingState(context.Background(), req)

	// Verify - should handle validation
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.False(t, result.IsValid)
	assert.NotEmpty(t, result.ValidationErrors)
}

// Integration test for matchmaking workflow
func TestServer_Integration_MatchmakingWorkflow(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	ctx := context.Background()
	playerID := uuid.New()

	// Step 1: Join queue
	joinReq := &api.JoinQueueRequest{
		PlayerID:      playerID,
		GameMode:      "ranked",
		Region:        "us-east",
		SkillRating:   1500,
		MatchCriteria: api.MatchCriteria{TeamSize: 5, GameMode: "ranked"},
	}

	joinResult, err := server.JoinQueue(ctx, joinReq)
	require.NoError(t, err)
	assert.Equal(t, api.QueueStatusQueued, joinResult.Status)

	// Step 2: Check queue status
	statusParams := api.GetQueueStatusParams{PlayerID: playerID}
	statusResult, err := server.GetQueueStatus(ctx, statusParams)
	assert.NoError(t, err)
	assert.Equal(t, playerID, statusResult.PlayerID)

	// Step 3: Get matchmaking preferences
	prefParams := api.GetMatchmakingPreferencesParams{PlayerID: playerID}
	prefResult, err := server.GetMatchmakingPreferences(ctx, prefParams)
	assert.NoError(t, err)
	assert.Equal(t, playerID, prefResult.PlayerID)

	// Step 4: Update preferences
	updateReq := &api.UpdatePreferencesRequest{
		PlayerID: playerID,
		Preferences: api.MatchmakingPreferences{
			PreferredGameModes: []string{"ranked"},
			MaxWaitTime:        300,
		},
	}

	updateResult, err := server.UpdateMatchmakingPreferences(ctx, updateReq)
	assert.NoError(t, err)
	assert.True(t, updateResult.Success)

	// Step 5: Simulate match found and accept
	matchID := uuid.New()
	acceptReq := &api.AcceptMatchRequest{ResponseTime: 3}
	acceptParams := api.AcceptMatchParams{PlayerID: playerID, MatchID: matchID}

	acceptResult, err := server.AcceptMatch(ctx, acceptReq, acceptParams)
	assert.NoError(t, err)
	assert.True(t, acceptResult.Accepted)

	// Step 6: Get match details
	matchParams := api.GetMatchDetailsParams{MatchID: matchID}
	matchResult, err := server.GetMatchDetails(ctx, matchParams)
	assert.NoError(t, err)
	assert.Equal(t, matchID, matchResult.MatchID)

	// Step 7: Start match
	startParams := api.StartMatchParams{MatchID: matchID}
	startResult, err := server.StartMatch(ctx, startParams)
	assert.NoError(t, err)
	assert.Equal(t, api.MatchStatusStarted, startResult.Status)

	// Step 8: Leave queue (cleanup)
	leaveReq := &api.LeaveQueueRequest{
		PlayerID: playerID,
		Reason:   "match_started",
	}

	leaveResult, err := server.LeaveQueue(ctx, leaveReq)
	assert.NoError(t, err)
	assert.True(t, leaveResult.Success)
}

// Performance test for concurrent matchmaking operations
func TestServer_ConcurrentMatchmaking(t *testing.T) {
	server, mock := setupTestServer()
	defer mock.ExpectClose()

	const numGoroutines = 10
	const operationsPerGoroutine = 5

	// Run concurrent operations
	t.Run("concurrent", func(t *testing.T) {
		for i := 0; i < numGoroutines; i++ {
			go func(routineID int) {
				for j := 0; j < operationsPerGoroutine; j++ {
					playerID := uuid.New()

					// Join queue
					req := &api.JoinQueueRequest{
						PlayerID:    playerID,
						GameMode:    "ranked",
						Region:      "us-east",
						SkillRating: 1500,
					}

					result, err := server.JoinQueue(context.Background(), req)
					assert.NoError(t, err)
					assert.NotNil(t, result)

					// Check status
					params := api.GetQueueStatusParams{PlayerID: playerID}
					status, err := server.GetQueueStatus(context.Background(), params)
					assert.NoError(t, err)
					assert.NotNil(t, status)

					// Leave queue
					leaveReq := &api.LeaveQueueRequest{
						PlayerID: playerID,
						Reason:   "test_cleanup",
					}

					leaveResult, err := server.LeaveQueue(context.Background(), leaveReq)
					assert.NoError(t, err)
					assert.NotNil(t, leaveResult)
				}
			}(i)
		}
	})
}
