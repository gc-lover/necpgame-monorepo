// Issue: #140890235 - Unit tests for matchmaking-go handlers
package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"

	api "github.com/gc-lover/necpgame-monorepo/services/matchmaking-go/pkg/api"
)

// MockService is a mock implementation of ServiceInterface for testing
type MockService struct {
	mock.Mock
}

func (m *MockService) EnterQueue(ctx context.Context, playerID uuid.UUID, req *api.EnterQueueRequest) (*api.QueueResponse, error) {
	args := m.Called(ctx, playerID, req)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*api.QueueResponse), args.Error(1)
}

func (m *MockService) GetQueueStatus(ctx context.Context, queueID uuid.UUID) (*api.QueueStatusResponse, error) {
	args := m.Called(ctx, queueID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*api.QueueStatusResponse), args.Error(1)
}

func (m *MockService) LeaveQueue(ctx context.Context, queueID uuid.UUID) (*api.LeaveQueueResponse, error) {
	args := m.Called(ctx, queueID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*api.LeaveQueueResponse), args.Error(1)
}

func (m *MockService) GetPlayerRating(ctx context.Context, playerID uuid.UUID) (*api.PlayerRatingResponse, error) {
	args := m.Called(ctx, playerID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*api.PlayerRatingResponse), args.Error(1)
}

func (m *MockService) GetLeaderboard(ctx context.Context, params api.GetLeaderboardParams) (*api.LeaderboardResponse, error) {
	args := m.Called(ctx, params)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*api.LeaderboardResponse), args.Error(1)
}

func (m *MockService) AcceptMatch(ctx context.Context, matchID uuid.UUID) error {
	args := m.Called(ctx, matchID)
	return args.Error(0)
}

func (m *MockService) DeclineMatch(ctx context.Context, matchID uuid.UUID) error {
	args := m.Called(ctx, matchID)
	return args.Error(0)
}

// TestEnterQueue tests EnterQueue handler
func TestEnterQueue(t *testing.T) {
	tests := []struct {
		name           string
		playerID       uuid.UUID
		req            *api.EnterQueueRequest
		mockResponse   *api.QueueResponse
		mockError      error
		expectedStatus string
	}{
		{
			name:     "success",
			playerID: uuid.New(),
			req: &api.EnterQueueRequest{
				ActivityType: api.EnterQueueRequestActivityTypePvp5v5,
			},
			mockResponse: &api.QueueResponse{
				QueueId:           uuid.New(),
				EstimatedWaitTime: 30,
				CurrentQueueSize:  5,
			},
			mockError:      nil,
			expectedStatus: "success",
		},
		{
			name:     "unauthorized - no player_id in context",
			playerID: uuid.Nil,
			req: &api.EnterQueueRequest{
				ActivityType: api.EnterQueueRequestActivityTypePvp5v5,
			},
			mockResponse:   nil,
			mockError:      nil,
			expectedStatus: "unauthorized",
		},
		{
			name:     "already in queue",
			playerID: uuid.New(),
			req: &api.EnterQueueRequest{
				ActivityType: api.EnterQueueRequestActivityTypePvp5v5,
			},
			mockResponse:   nil,
			mockError:      ErrAlreadyInQueue,
			expectedStatus: "conflict",
		},
		{
			name:     "internal server error",
			playerID: uuid.New(),
			req: &api.EnterQueueRequest{
				ActivityType: api.EnterQueueRequestActivityTypePvp5v5,
			},
			mockResponse:   nil,
			mockError:      assert.AnError,
			expectedStatus: "error",
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			mockService := new(MockService)
			handlers := &Handlers{service: mockService}

			ctx := context.Background()
			if tt.playerID != uuid.Nil {
				ctx = context.WithValue(ctx, "player_id", tt.playerID)
			}

			if tt.playerID != uuid.Nil {
				// Use mock.MatchedBy for context to handle context.WithTimeout
				mockService.On("EnterQueue", mock.MatchedBy(func(ctx context.Context) bool {
					// Check that context has player_id value
					val := ctx.Value("player_id")
					return val != nil && val.(uuid.UUID) == tt.playerID
				}), tt.playerID, tt.req).Return(tt.mockResponse, tt.mockError)
			}

			res, err := handlers.EnterQueue(ctx, tt.req)

			switch tt.expectedStatus {
			case "success":
				assert.NoError(t, err)
				assert.IsType(t, &api.QueueResponse{}, res)
				queueRes := res.(*api.QueueResponse)
				assert.Equal(t, tt.mockResponse.QueueId, queueRes.QueueId)
			case "unauthorized":
				assert.NoError(t, err)
				assert.IsType(t, &api.EnterQueueUnauthorized{}, res)
			case "conflict":
				assert.NoError(t, err)
				assert.IsType(t, &api.EnterQueueConflict{}, res)
			case "error":
				assert.Error(t, err)
				assert.IsType(t, &api.EnterQueueInternalServerError{}, res)
			}

			mockService.AssertExpectations(t)
		})
	}
}

// TestGetQueueStatus tests GetQueueStatus handler
func TestGetQueueStatus(t *testing.T) {
	queueID := uuid.New()
	statusResponse := &api.QueueStatusResponse{
		QueueId:     queueID,
		Status:      api.QueueStatusResponseStatusWaiting,
		TimeInQueue: 10,
		RatingRange: []int32{1500, 1600},
	}

	mockService := new(MockService)
	handlers := &Handlers{service: mockService}

	ctx := context.Background()
	params := api.GetQueueStatusParams{
		QueueId: queueID,
	}

	mockService.On("GetQueueStatus", mock.Anything, queueID).Return(statusResponse, nil)

	res, err := handlers.GetQueueStatus(ctx, params)

	assert.NoError(t, err)
	assert.IsType(t, &api.QueueStatusResponse{}, res)
	statusRes := res.(*api.QueueStatusResponse)
	assert.Equal(t, queueID, statusRes.QueueId)
	assert.Equal(t, api.QueueStatusResponseStatusWaiting, statusRes.Status)

	mockService.AssertExpectations(t)
}

// TestGetQueueStatus_NotFound tests GetQueueStatus when queue not found
func TestGetQueueStatus_NotFound(t *testing.T) {
	queueID := uuid.New()

	mockService := new(MockService)
	handlers := &Handlers{service: mockService}

	ctx := context.Background()
	params := api.GetQueueStatusParams{
		QueueId: queueID,
	}

	mockService.On("GetQueueStatus", mock.Anything, queueID).Return(nil, ErrNotFound)

	res, err := handlers.GetQueueStatus(ctx, params)

	assert.NoError(t, err)
	assert.IsType(t, &api.Error{}, res)
	errorRes := res.(*api.Error)
	assert.Equal(t, "Queue not found", errorRes.Message)

	mockService.AssertExpectations(t)
}

// TestLeaveQueue tests LeaveQueue handler
func TestLeaveQueue(t *testing.T) {
	queueID := uuid.New()
	leaveResponse := &api.LeaveQueueResponse{
		Status:          api.LeaveQueueResponseStatusCancelled,
		WaitTimeSeconds: 30,
	}

	mockService := new(MockService)
	handlers := &Handlers{service: mockService}

	ctx := context.Background()
	params := api.LeaveQueueParams{
		QueueId: queueID,
	}

	mockService.On("LeaveQueue", mock.Anything, queueID).Return(leaveResponse, nil)

	res, err := handlers.LeaveQueue(ctx, params)

	assert.NoError(t, err)
	assert.IsType(t, &api.LeaveQueueResponse{}, res)
	leaveRes := res.(*api.LeaveQueueResponse)
	assert.Equal(t, api.LeaveQueueResponseStatusCancelled, leaveRes.Status)

	mockService.AssertExpectations(t)
}

// TestGetPlayerRating tests GetPlayerRating handler
func TestGetPlayerRating(t *testing.T) {
	playerID := uuid.New()
	ratingResponse := &api.PlayerRatingResponse{
		PlayerId: playerID,
		Ratings: []api.ActivityRating{
			{
				ActivityType: "pvp_5v5",
				Tier:         api.ActivityRatingTierGold,
				CurrentRating: 1500,
			},
		},
	}

	mockService := new(MockService)
	handlers := &Handlers{service: mockService}

	ctx := context.Background()
	params := api.GetPlayerRatingParams{
		PlayerID: playerID,
	}

	mockService.On("GetPlayerRating", mock.Anything, playerID).Return(ratingResponse, nil)

	res, err := handlers.GetPlayerRating(ctx, params)

	assert.NoError(t, err)
	assert.IsType(t, &api.PlayerRatingResponse{}, res)
	ratingRes := res.(*api.PlayerRatingResponse)
	assert.Equal(t, playerID, ratingRes.PlayerId)
	assert.Len(t, ratingRes.Ratings, 1)
	assert.Equal(t, int32(1500), ratingRes.Ratings[0].CurrentRating)

	mockService.AssertExpectations(t)
}

// TestGetLeaderboard tests GetLeaderboard handler
func TestGetLeaderboard(t *testing.T) {
	leaderboardResponse := &api.LeaderboardResponse{
		ActivityType: "pvp_5v5",
		SeasonId:     "season1",
		Leaderboard: []api.LeaderboardEntry{
			{
				Rank:       1,
				PlayerId:   uuid.New(),
				PlayerName: "Player1",
				Rating:     2000,
				Tier:       api.LeaderboardEntryTierDiamond,
				Wins:       api.NewOptInt32(100),
				Losses:     api.NewOptInt32(20),
			},
		},
	}

	mockService := new(MockService)
	handlers := &Handlers{service: mockService}

	ctx := context.Background()
	params := api.GetLeaderboardParams{
		ActivityType: api.GetLeaderboardActivityTypePvp5v5,
		Limit:        api.NewOptInt(10),
	}

	mockService.On("GetLeaderboard", mock.Anything, params).Return(leaderboardResponse, nil)

	res, err := handlers.GetLeaderboard(ctx, params)

	assert.NoError(t, err)
	assert.NotNil(t, res)
	leaderboardRes := res
	assert.Len(t, leaderboardRes.Leaderboard, 1)

	mockService.AssertExpectations(t)
}

// TestAcceptMatch tests AcceptMatch handler
func TestAcceptMatch(t *testing.T) {
	matchID := uuid.New()

	mockService := new(MockService)
	handlers := &Handlers{service: mockService}

	ctx := context.Background()
	params := api.AcceptMatchParams{
		MatchId: matchID,
	}

	mockService.On("AcceptMatch", mock.Anything, matchID).Return(nil)

	res, err := handlers.AcceptMatch(ctx, params)

	assert.NoError(t, err)
	assert.NotNil(t, res)
	successRes, ok := res.(*api.SuccessResponse)
	assert.True(t, ok)
	assert.True(t, successRes.Status.Set)
	assert.Equal(t, "accepted", successRes.Status.Value)

	mockService.AssertExpectations(t)
}

// TestAcceptMatch_NotFound tests AcceptMatch when match not found
func TestAcceptMatch_NotFound(t *testing.T) {
	matchID := uuid.New()

	mockService := new(MockService)
	handlers := &Handlers{service: mockService}

	ctx := context.Background()
	params := api.AcceptMatchParams{
		MatchId: matchID,
	}

	mockService.On("AcceptMatch", mock.Anything, matchID).Return(ErrNotFound)

	res, err := handlers.AcceptMatch(ctx, params)

	assert.NoError(t, err)
	assert.IsType(t, &api.AcceptMatchNotFound{}, res)
	notFoundRes := res.(*api.AcceptMatchNotFound)
	assert.Equal(t, "NOT_FOUND", notFoundRes.Error)
	assert.Equal(t, "Match not found", notFoundRes.Message)

	mockService.AssertExpectations(t)
}

// TestAcceptMatch_Cancelled tests AcceptMatch when match cancelled
func TestAcceptMatch_Cancelled(t *testing.T) {
	matchID := uuid.New()

	mockService := new(MockService)
	handlers := &Handlers{service: mockService}

	ctx := context.Background()
	params := api.AcceptMatchParams{
		MatchId: matchID,
	}

	mockService.On("AcceptMatch", mock.Anything, matchID).Return(ErrMatchCancelled)

	res, err := handlers.AcceptMatch(ctx, params)

	assert.NoError(t, err)
	assert.IsType(t, &api.AcceptMatchConflict{}, res)
	conflictRes := res.(*api.AcceptMatchConflict)
	assert.Equal(t, "CONFLICT", conflictRes.Error)
	assert.Equal(t, "Match already cancelled or started", conflictRes.Message)

	mockService.AssertExpectations(t)
}

// TestDeclineMatch tests DeclineMatch handler
func TestDeclineMatch(t *testing.T) {
	matchID := uuid.New()

	mockService := new(MockService)
	handlers := &Handlers{service: mockService}

	ctx := context.Background()
	params := api.DeclineMatchParams{
		MatchId: matchID,
	}

	mockService.On("DeclineMatch", mock.Anything, matchID).Return(nil)

	res, err := handlers.DeclineMatch(ctx, params)

	assert.NoError(t, err)
	assert.NotNil(t, res)
	successRes := res
	assert.True(t, successRes.Status.Set)
	assert.Equal(t, "declined", successRes.Status.Value)

	mockService.AssertExpectations(t)
}

// TestContextTimeout tests that handlers respect context timeouts
func TestContextTimeout(t *testing.T) {
	mockService := new(MockService)
	handlers := &Handlers{service: mockService}

	// Create context with very short timeout
	ctx, cancel := context.WithTimeout(context.Background(), 1*time.Nanosecond)
	defer cancel()

	// Wait for timeout
	time.Sleep(10 * time.Millisecond)

	playerID := uuid.New()
	ctx = context.WithValue(ctx, "player_id", playerID)
	req := &api.EnterQueueRequest{
		ActivityType: api.EnterQueueRequestActivityTypePvp5v5,
	}

	// Service may be called, but context will be expired
	// Mock should return context.DeadlineExceeded error
	mockService.On("EnterQueue", mock.MatchedBy(func(ctx context.Context) bool {
		val := ctx.Value("player_id")
		return val != nil && val.(uuid.UUID) == playerID
	}), playerID, req).Return(nil, context.DeadlineExceeded)

	res, err := handlers.EnterQueue(ctx, req)

	// Should return error due to timeout
	assert.Error(t, err)
	assert.IsType(t, &api.EnterQueueInternalServerError{}, res)

	mockService.AssertExpectations(t)
}

