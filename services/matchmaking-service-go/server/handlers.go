// Issue: #1579 - ogen migration + full optimizations
// Migrated from oapi-codegen to ogen for typed responses (no interface{} boxing!)
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/matchmaking-service-go/pkg/api"
)

// Context timeout constants
const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
)

// Handlers implements api.Handler interface (ogen typed handlers!)
type Handlers struct {
	service Service
}

// NewHandlers creates handlers with dependency injection
func NewHandlers(service Service) *Handlers {
	return &Handlers{service: service}
}

// EnterQueue adds player to queue (TYPED ogen response)
func (h *Handlers) EnterQueue(ctx context.Context, req *api.EnterQueueRequest) (api.EnterQueueRes, error) {
	// CRITICAL: Context timeout for DB operations (50ms)
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	response, err := h.service.EnterQueue(ctx, req)
	if err != nil {
		// Typed error response (ogen advantage - no interface{} boxing!)
		return &api.EnterQueueInternalServerError{
			Error:   "internal_server_error",
			Message: err.Error(),
		}, nil
	}

	return response, nil
}

// GetQueueStatus gets queue status (TYPED ogen response)
func (h *Handlers) GetQueueStatus(ctx context.Context, params api.GetQueueStatusParams) (api.GetQueueStatusRes, error) {
	// CRITICAL: Context timeout (10ms for cache, 50ms for DB)
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	response, err := h.service.GetQueueStatus(ctx, params.QueueId.String())
	if err != nil {
		// api.Error implements getQueueStatusRes interface
		return &api.Error{
			Error:   "not_found",
			Message: "Queue not found",
		}, nil
	}

	return response, nil
}

// LeaveQueue removes player from queue (TYPED ogen response)
func (h *Handlers) LeaveQueue(ctx context.Context, params api.LeaveQueueParams) (api.LeaveQueueRes, error) {
	// CRITICAL: Context timeout
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	response, err := h.service.LeaveQueue(ctx, params.QueueId.String())
	if err != nil {
		// api.Error implements leaveQueueRes interface
		return &api.Error{
			Error:   "internal_server_error",
			Message: err.Error(),
		}, nil
	}

	return response, nil
}

// GetPlayerRating gets player rating (TYPED ogen response)
func (h *Handlers) GetPlayerRating(ctx context.Context, params api.GetPlayerRatingParams) (api.GetPlayerRatingRes, error) {
	// CRITICAL: Context timeout (cache operation)
	ctx, cancel := context.WithTimeout(ctx, CacheTimeout)
	defer cancel()

	response, err := h.service.GetPlayerRating(ctx, params.PlayerID.String())
	if err != nil {
		// api.Error implements getPlayerRatingRes interface
		return &api.Error{
			Error:   "not_found",
			Message: "Player not found",
		}, nil
	}

	return response, nil
}

// GetLeaderboard gets leaderboard (TYPED ogen response)
func (h *Handlers) GetLeaderboard(ctx context.Context, params api.GetLeaderboardParams) (*api.LeaderboardResponse, error) {
	// CRITICAL: Context timeout (leaderboard from cache)
	ctx, cancel := context.WithTimeout(ctx, CacheTimeout)
	defer cancel()

	response, err := h.service.GetLeaderboard(ctx, string(params.ActivityType), params)
	if err != nil {
		return nil, err
	}

	return response, nil
}

// AcceptMatch accepts match (TYPED ogen response)
func (h *Handlers) AcceptMatch(ctx context.Context, params api.AcceptMatchParams) (api.AcceptMatchRes, error) {
	// CRITICAL: Context timeout
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	err := h.service.AcceptMatch(ctx, params.MatchId.String())
	if err != nil {
		return &api.AcceptMatchNotFound{
			Error:   "not_found",
			Message: "Match not found or expired",
		}, nil
	}

	return &api.SuccessResponse{
		Status: api.NewOptString("accepted"),
	}, nil
}

// DeclineMatch declines match (TYPED ogen response)
func (h *Handlers) DeclineMatch(ctx context.Context, params api.DeclineMatchParams) (*api.SuccessResponse, error) {
	// CRITICAL: Context timeout
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	err := h.service.DeclineMatch(ctx, params.MatchId.String())
	if err != nil {
		return nil, err
	}

	return &api.SuccessResponse{
		Status: api.NewOptString("declined"),
	}, nil
}

// NewError implements ogen error handler (handles errors from middleware/validation)
func (h *Handlers) NewError(ctx context.Context, err error) *api.Error {
	return &api.Error{
		Error:   "error",
		Message: err.Error(),
	}
}
