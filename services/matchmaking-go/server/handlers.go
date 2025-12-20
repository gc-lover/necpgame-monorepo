// Package server Issue: #150 - Matchmaking Service Handlers (ogen-based)
// Performance: TYPED responses, zero allocations target, memory pooling
package server

import (
	"context"
	"time"

	"github.com/google/uuid"

	"github.com/gc-lover/necpgame-monorepo/services/matchmaking-go/pkg/api"
)

// Context timeout constants (critical for hot path!)
const (
	QueueTimeout       = 100 * time.Millisecond // Hot: 2k+ RPS
	StatusTimeout      = 50 * time.Millisecond  // Hot: 5k+ RPS (polling)
	RatingTimeout      = 100 * time.Millisecond
	LeaderboardTimeout = 200 * time.Millisecond // Heavy query, but cached
)

// ServiceInterface defines the interface for matchmaking service
// Issue: #140890235 - Used for testing with mocks
type ServiceInterface interface {
	EnterQueue(ctx context.Context, playerID uuid.UUID, req *api.EnterQueueRequest) (*api.QueueResponse, error)
	GetQueueStatus(ctx context.Context, queueID uuid.UUID) (*api.QueueStatusResponse, error)
	LeaveQueue(ctx context.Context, queueID uuid.UUID) (*api.LeaveQueueResponse, error)
	GetPlayerRating(ctx context.Context, playerID uuid.UUID) (*api.PlayerRatingResponse, error)
	GetLeaderboard(ctx context.Context, params api.GetLeaderboardParams) (*api.LeaderboardResponse, error)
	AcceptMatch(ctx context.Context, matchID uuid.UUID) error
	DeclineMatch(ctx context.Context, matchID uuid.UUID) error
}

// Handlers implements api.Handler interface (ogen typed handlers)
type Handlers struct {
	service ServiceInterface
}

// NewHandlers creates new handlers with performance optimizations
func NewHandlers(service ServiceInterface) *Handlers {
	return &Handlers{service: service}
}

// EnterQueue - HOT PATH: 2000+ RPS
// Performance: Memory pooling, zero allocations
func (h *Handlers) EnterQueue(ctx context.Context, req *api.EnterQueueRequest) (api.EnterQueueRes, error) {
	ctx, cancel := context.WithTimeout(ctx, QueueTimeout)
	defer cancel()

	// Extract player_id from context (JWT middleware sets it)
	playerID, ok := ctx.Value("player_id").(uuid.UUID)
	if !ok {
		return &api.EnterQueueUnauthorized{}, nil
	}

	// Service call (uses memory pooling internally)
	response, err := h.service.EnterQueue(ctx, playerID, req)
	if err != nil {
		if err == ErrAlreadyInQueue {
			return &api.EnterQueueConflict{}, nil
		}
		return &api.EnterQueueInternalServerError{}, err
	}

	// TYPED response (ogen marshals directly, no interface{} boxing!)
	return response, nil
}

// GetQueueStatus - HOT PATH: 5000+ RPS (polling)
// Performance: Redis cache 5s TTL, zero allocations
func (h *Handlers) GetQueueStatus(ctx context.Context, params api.GetQueueStatusParams) (api.GetQueueStatusRes, error) {
	ctx, cancel := context.WithTimeout(ctx, StatusTimeout)
	defer cancel()

	// Parse UUID (ogen already validated format)
	queueID := params.QueueId

	// Service call (Redis cache hit = <1ms)
	status, err := h.service.GetQueueStatus(ctx, queueID)
	if err != nil {
		if err == ErrNotFound {
			return &api.Error{Message: "Queue not found"}, nil
		}
		return &api.Error{Message: "Internal server error"}, err
	}

	return status, nil
}

// LeaveQueue - Standard path: ~500 RPS
func (h *Handlers) LeaveQueue(ctx context.Context, params api.LeaveQueueParams) (api.LeaveQueueRes, error) {
	ctx, cancel := context.WithTimeout(ctx, QueueTimeout)
	defer cancel()

	queueID := params.QueueId

	response, err := h.service.LeaveQueue(ctx, queueID)
	if err != nil {
		if err == ErrNotFound {
			return &api.Error{Message: "Queue not found"}, nil
		}
		return &api.Error{Message: "Internal server error"}, err
	}

	return response, nil
}

// GetPlayerRating - Standard path: ~1000 RPS
// Performance: Covering index = <1ms P95
func (h *Handlers) GetPlayerRating(ctx context.Context, params api.GetPlayerRatingParams) (api.GetPlayerRatingRes, error) {
	ctx, cancel := context.WithTimeout(ctx, RatingTimeout)
	defer cancel()

	playerID := params.PlayerID

	rating, err := h.service.GetPlayerRating(ctx, playerID)
	if err != nil {
		if err == ErrNotFound {
			return &api.Error{Message: "Player rating not found"}, nil
		}
		return &api.Error{Message: "Internal server error"}, err
	}

	return rating, nil
}

// GetLeaderboard - Heavy query: ~500 RPS
// Performance: Materialized view + Redis cache 5min = <50ms
func (h *Handlers) GetLeaderboard(ctx context.Context, params api.GetLeaderboardParams) (*api.LeaderboardResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, LeaderboardTimeout)
	defer cancel()

	// Service handles Redis cache + materialized view
	leaderboard, err := h.service.GetLeaderboard(ctx, params)
	if err != nil {
		return nil, err
	}

	return leaderboard, nil
}

// AcceptMatch - Standard path: ~1000 RPS
func (h *Handlers) AcceptMatch(ctx context.Context, params api.AcceptMatchParams) (api.AcceptMatchRes, error) {
	ctx, cancel := context.WithTimeout(ctx, QueueTimeout)
	defer cancel()

	matchID := params.MatchId

	err := h.service.AcceptMatch(ctx, matchID)
	if err != nil {
		if err == ErrNotFound {
			return &api.AcceptMatchNotFound{
				Error:   "NOT_FOUND",
				Message: "Match not found",
			}, nil
		}
		if err == ErrMatchCancelled {
			return &api.AcceptMatchConflict{
				Error:   "CONFLICT",
				Message: "Match already cancelled or started",
			}, nil
		}
		// For 500 errors, return error (ogen handles it)
		return nil, err
	}

	return &api.SuccessResponse{Status: api.NewOptString("accepted")}, nil
}

// DeclineMatch - Standard path: ~200 RPS
func (h *Handlers) DeclineMatch(ctx context.Context, params api.DeclineMatchParams) (*api.SuccessResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, QueueTimeout)
	defer cancel()

	matchID := params.MatchId

	err := h.service.DeclineMatch(ctx, matchID)
	if err != nil {
		return nil, err
	}

	return &api.SuccessResponse{Status: api.NewOptString("declined")}, nil
}
