// Package server Issue: #1604, #1607
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/loot-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

const (
	DBTimeout = 50 * time.Millisecond
)

// Handlers implements api.Handler interface (ogen typed handlers!)
type Handlers struct {
	logger  *logrus.Logger
	service LootServiceInterface

	lootHistoryPool    sync.Pool // Issue: #1607 - Memory pooling
	worldDropsListPool sync.Pool // Issue: #1607 - Memory pooling
}

// NewHandlers creates new handlers
func NewHandlers(logger *logrus.Logger, service LootServiceInterface) *Handlers {
	h := &Handlers{
		logger:  logger,
		service: service,
	}
	// Issue: #1607 - Initialize memory pools for hot path responses
	h.lootHistoryPool = sync.Pool{
		New: func() interface{} {
			return &api.LootHistoryResponse{}
		},
	}
	h.worldDropsListPool = sync.Pool{
		New: func() interface{} {
			return &api.WorldDropsListResponse{}
		},
	}
	return h
}

// DistributeLoot - TYPED response!
func (h *Handlers) DistributeLoot(ctx context.Context, req *api.DistributeLootRequest) (api.DistributeLootRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Basic validation and logging
	if req == nil {
		h.logger.Error("DistributeLoot: nil request")
		return &api.DistributeLootResponse{}, nil
	}

	if len(req.Items) == 0 {
		h.logger.Warn("DistributeLoot: empty items")
	}

	if len(req.PlayerIds) == 0 {
		h.logger.Warn("DistributeLoot: empty player_ids")
	}

	if h.service == nil {
		return &api.DistributeLootResponse{}, nil
	}

	response, err := h.service.DistributeLoot(ctx, req)
	if err != nil {
		h.logger.WithError(err).Error("DistributeLoot: failed")
		return &api.DistributeLootResponse{}, nil
	}

	return response, nil
}

// GenerateLoot - TYPED response!
func (h *Handlers) GenerateLoot(ctx context.Context, req *api.GenerateLootRequest) (api.GenerateLootRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Basic validation and logging
	if req == nil {
		h.logger.Error("GenerateLoot: nil request")
		return &api.GenerateLootResponse{}, nil
	}

	if req.SourceID == "" {
		h.logger.Warn("GenerateLoot: empty source_id")
	}

	if req.PlayerID == uuid.Nil {
		h.logger.Warn("GenerateLoot: empty player_id")
	}

	if h.service == nil {
		return &api.GenerateLootResponse{}, nil
	}

	response, err := h.service.GenerateLoot(ctx, req)
	if err != nil {
		h.logger.WithError(err).Error("GenerateLoot: failed")
		return &api.GenerateLootResponse{}, nil
	}

	return response, nil
}

// GetPlayerLootHistory - TYPED response!
func (h *Handlers) GetPlayerLootHistory(ctx context.Context, params api.GetPlayerLootHistoryParams) (*api.LootHistoryResponse, error) {
	_, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Issue: #1607 - Memory pooling for hot path
	resp := h.lootHistoryPool.Get().(*api.LootHistoryResponse)
	defer func() {
		// Reset before returning to pool
		*resp = api.LootHistoryResponse{}
		h.lootHistoryPool.Put(resp)
	}()

	if h.service == nil {
		resp.History = []api.LootHistoryEntry{}
		result := &api.LootHistoryResponse{
			History: resp.History,
			Total:   resp.Total,
		}
		return result, nil
	}

	limit := 50
	if params.Limit.IsSet() {
		limit = params.Limit.Value
	}
	offset := 0 // Offset not in params, using default

	history, total, err := h.service.GetPlayerLootHistory(ctx, params.PlayerID, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("GetPlayerLootHistory: failed")
		resp.History = []api.LootHistoryEntry{}
		result := &api.LootHistoryResponse{
			History: resp.History,
			Total:   resp.Total,
		}
		return result, nil
	}

	resp.History = history
	resp.Total = api.NewOptInt(total)

	// Create copy to return (pooled struct stays in pool)
	result := &api.LootHistoryResponse{
		History: resp.History,
		Total:   resp.Total,
	}
	return result, nil
}

// GetRollStatus - TYPED response!
func (h *Handlers) GetRollStatus(ctx context.Context, params api.GetRollStatusParams) (api.GetRollStatusRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Basic validation and logging
	if params.RollID == uuid.Nil {
		h.logger.Warn("GetRollStatus: empty roll_id")
	}

	if h.service == nil {
		return &api.RollStatusResponse{}, nil
	}

	response, err := h.service.GetRollStatus(ctx, params.RollID)
	if err != nil {
		h.logger.WithError(err).Error("GetRollStatus: failed")
		return &api.RollStatusResponse{}, nil
	}

	return response, nil
}

// GetWorldDrops - TYPED response!
func (h *Handlers) GetWorldDrops(ctx context.Context, _ api.GetWorldDropsParams) (*api.WorldDropsListResponse, error) {
	_, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Issue: #1607 - Memory pooling for hot path
	resp := h.worldDropsListPool.Get().(*api.WorldDropsListResponse)
	defer func() {
		// Reset before returning to pool
		*resp = api.WorldDropsListResponse{}
		h.worldDropsListPool.Put(resp)
	}()

	if h.service == nil {
		resp.Drops = []api.WorldDrop{}
		result := &api.WorldDropsListResponse{
			Drops: resp.Drops,
		}
		return result, nil
	}

	limit := 50 // Default limit
	offset := 0 // Default offset

	drops, err := h.service.GetWorldDrops(ctx, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("GetWorldDrops: failed")
		resp.Drops = []api.WorldDrop{}
		result := &api.WorldDropsListResponse{
			Drops: resp.Drops,
		}
		return result, nil
	}

	resp.Drops = drops

	// Create copy to return (pooled struct stays in pool)
	result := &api.WorldDropsListResponse{
		Drops: resp.Drops,
	}
	return result, nil
}

// PassRoll - TYPED response!
func (h *Handlers) PassRoll(ctx context.Context, params api.PassRollParams) (api.PassRollRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Basic validation and logging
	if params.RollID == uuid.Nil {
		h.logger.Warn("PassRoll: empty roll_id")
	}

	if h.service == nil {
		return &api.SuccessResponse{
			Status: api.NewOptString("success"),
		}, nil
	}

	err := h.service.PassRoll(ctx, params.RollID)
	if err != nil {
		h.logger.WithError(err).Error("PassRoll: failed")
		return &api.SuccessResponse{
			Status: api.NewOptString("error"),
		}, nil
	}

	return &api.SuccessResponse{
		Status: api.NewOptString("success"),
	}, nil
}

// PickupWorldDrop - TYPED response!
func (h *Handlers) PickupWorldDrop(ctx context.Context, params api.PickupWorldDropParams) (api.PickupWorldDropRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Basic validation and logging
	if params.DropID == uuid.Nil {
		h.logger.Warn("PickupWorldDrop: empty drop_id")
	}

	if h.service == nil {
		return &api.PickupDropResponse{}, nil
	}

	response, err := h.service.PickupWorldDrop(ctx, params.DropID)
	if err != nil {
		h.logger.WithError(err).Error("PickupWorldDrop: failed")
		return &api.PickupDropResponse{}, nil
	}

	return response, nil
}

// RollForItem - TYPED response!
func (h *Handlers) RollForItem(ctx context.Context, req *api.RollRequest, _ api.RollForItemParams) (api.RollForItemRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Basic validation and logging
	if req == nil {
		h.logger.Error("RollForItem: nil request")
		return &api.RollResponse{}, nil
	}

	if req.ItemID == uuid.Nil {
		h.logger.Warn("RollForItem: empty item_id in request")
	}

	if h.service == nil {
		return &api.RollResponse{}, nil
	}

	response, err := h.service.RollForItem(ctx, req)
	if err != nil {
		h.logger.WithError(err).Error("RollForItem: failed")
		return &api.RollResponse{}, nil
	}

	return response, nil
}
