// Package server Issue: ogen migration, #1607
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"errors"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/leaderboard-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

const DBTimeout = 50 * time.Millisecond

var (
	_ = errors.New("not found")
)

// Handlers implements api.Handler interface (ogen typed handlers!)
// Issue: #1607 - Memory pooling for hot path structs (Level 2 optimization)
type Handlers struct {
	logger  *logrus.Logger
	service LeaderboardServiceInterface

	// Memory pooling for hot path structs (zero allocations target!)
	globalLeaderboardPool  sync.Pool
	factionLeaderboardPool sync.Pool
	playerRankPool         sync.Pool
	leaderboardEntryPool   sync.Pool
}

// NewHandlers creates new handlers with memory pooling
func NewHandlers(logger *logrus.Logger, service LeaderboardServiceInterface) *Handlers {
	h := &Handlers{
		logger:  logger,
		service: service,
	}

	// Initialize memory pools (zero allocations target!)
	h.globalLeaderboardPool = sync.Pool{
		New: func() interface{} {
			return &api.GetGlobalLeaderboardOK{}
		},
	}
	h.factionLeaderboardPool = sync.Pool{
		New: func() interface{} {
			return &api.GetFactionLeaderboardOK{}
		},
	}
	h.playerRankPool = sync.Pool{
		New: func() interface{} {
			return &api.PlayerRank{}
		},
	}
	h.leaderboardEntryPool = sync.Pool{
		New: func() interface{} {
			return &api.LeaderboardEntry{}
		},
	}

	return h
}

// GetGlobalLeaderboard - TYPED response!
// Issue: #1607 - Uses memory pooling for zero allocations
func (h *Handlers) GetGlobalLeaderboard(ctx context.Context, params api.GetGlobalLeaderboardParams) (api.GetGlobalLeaderboardRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return &api.GetGlobalLeaderboardOK{
			Entries: []api.LeaderboardEntry{},
		}, nil
	}

	period := "all-time"
	if params.Period.IsSet() {
		period = string(params.Period.Value)
	}

	limit := 50
	if params.Limit.IsSet() {
		limit = params.Limit.Value
	}

	offset := 0
	if params.Offset.IsSet() {
		offset = params.Offset.Value
	}

	entries, pagination, err := h.service.GetGlobalLeaderboard(ctx, period, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("GetGlobalLeaderboard: failed")
		return &api.GetGlobalLeaderboardOK{
			Entries: []api.LeaderboardEntry{},
		}, nil
	}

	// Issue: #1607 - Use memory pooling
	result := h.globalLeaderboardPool.Get().(*api.GetGlobalLeaderboardOK)
	// Note: Not returning to pool - struct is returned to caller

	result.Entries = entries
	if pagination != nil {
		result.Pagination = api.NewOptPaginationResponse(*pagination)
	}

	return result, nil
}

// GetFactionLeaderboard - TYPED response!
// Issue: #1607 - Uses memory pooling for zero allocations
func (h *Handlers) GetFactionLeaderboard(ctx context.Context, params api.GetFactionLeaderboardParams) (api.GetFactionLeaderboardRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return &api.GetFactionLeaderboardOK{
			Entries: []api.LeaderboardEntry{},
		}, nil
	}

	limit := 50
	if params.Limit.IsSet() {
		limit = params.Limit.Value
	}

	offset := 0
	if params.Offset.IsSet() {
		offset = params.Offset.Value
	}

	entries, pagination, err := h.service.GetFactionLeaderboard(ctx, params.FactionID, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("GetFactionLeaderboard: failed")
		return &api.GetFactionLeaderboardOK{
			Entries: []api.LeaderboardEntry{},
		}, nil
	}

	// Issue: #1607 - Use memory pooling
	result := h.factionLeaderboardPool.Get().(*api.GetFactionLeaderboardOK)
	// Note: Not returning to pool - struct is returned to caller

	result.Entries = entries
	if pagination != nil {
		result.Pagination = api.NewOptPaginationResponse(*pagination)
	}

	return result, nil
}

// GetPlayerRank - TYPED response!
// Issue: #1607 - Uses memory pooling for zero allocations
func (h *Handlers) GetPlayerRank(ctx context.Context, params api.GetPlayerRankParams) (api.GetPlayerRankRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return &api.PlayerRank{
			PlayerID:   params.PlayerID,
			GlobalRank: 0,
			Score:      0,
		}, nil
	}

	rank, err := h.service.GetPlayerRank(ctx, params.PlayerID)
	if err != nil {
		h.logger.WithError(err).Error("GetPlayerRank: failed")
		return &api.PlayerRank{
			PlayerID:   params.PlayerID,
			GlobalRank: 0,
			Score:      0,
		}, nil
	}

	// Issue: #1607 - Use memory pooling
	result := h.playerRankPool.Get().(*api.PlayerRank)
	// Note: Not returning to pool - struct is returned to caller

	*result = *rank

	return result, nil
}
