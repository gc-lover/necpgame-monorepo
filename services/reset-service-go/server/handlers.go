// Package server Issue: #1595, #1607 - ogen handlers (TYPED responses)
package server

import (
	"context"
	"encoding/json"
	"errors"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/reset-service-go/models"
	"github.com/gc-lover/necpgame-monorepo/services/reset-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

const (
	DBTimeout = 50 * time.Millisecond
)

var (
	_ = errors.New("not found")
)

// Handlers Issue: #1607 - Memory pooling for hot path structs (Level 2 optimization)
type Handlers struct {
	service ResetServiceInterface
	logger  *logrus.Logger

	// Memory pooling for hot path structs (zero allocations target!)
	resetStatsPool        sync.Pool
	resetListResponsePool sync.Pool
	successResponsePool   sync.Pool
}

func NewHandlers(service ResetServiceInterface) *Handlers {
	h := &Handlers{
		service: service,
		logger:  GetLogger(),
	}

	// Initialize memory pools (zero allocations target!)
	h.resetStatsPool = sync.Pool{
		New: func() interface{} {
			return &api.ResetStats{}
		},
	}
	h.resetListResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.ResetListResponse{}
		},
	}
	h.successResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.SuccessResponse{}
		},
	}

	return h
}

func (h *Handlers) GetResetStats(ctx context.Context) (api.GetResetStatsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	stats, err := h.service.GetResetStats(ctx)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get reset stats")
		return &api.Error{
			Error:   "INTERNAL_SERVER_ERROR",
			Message: "failed to get reset stats",
		}, nil
	}

	// Issue: #1607 - Use memory pooling
	result := h.resetStatsPool.Get().(*api.ResetStats)
	// Note: Not returning to pool - struct is returned to caller

	result.LastDailyReset = toOptNilDateTime(stats.LastDailyReset)
	result.LastWeeklyReset = toOptNilDateTime(stats.LastWeeklyReset)
	result.NextDailyReset = api.NewOptDateTime(stats.NextDailyReset)
	result.NextWeeklyReset = api.NewOptDateTime(stats.NextWeeklyReset)

	return result, nil
}

func (h *Handlers) GetResetHistory(ctx context.Context, params api.GetResetHistoryParams) (api.GetResetHistoryRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	var resetType *models.ResetType
	if params.Type.IsSet() {
		rt := models.ResetType(params.Type.Value)
		resetType = &rt
	}

	limit := 50
	if params.Limit.IsSet() && params.Limit.Value > 0 {
		if params.Limit.Value > 100 {
			limit = 100
		} else {
			limit = params.Limit.Value
		}
	}

	offset := 0
	if params.Offset.IsSet() && params.Offset.Value >= 0 {
		offset = params.Offset.Value
	}

	response, err := h.service.GetResetHistory(ctx, resetType, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get reset history")
		return &api.Error{
			Error:   "INTERNAL_SERVER_ERROR",
			Message: "failed to get reset history",
		}, nil
	}

	records := make([]api.ResetRecord, 0, len(response.Resets))
	for _, r := range response.Resets {
		records = append(records, toAPIResetRecord(r))
	}

	hasMore := offset+len(response.Resets) < response.Total

	// Issue: #1607 - Use memory pooling
	result := h.resetListResponsePool.Get().(*api.ResetListResponse)
	// Note: Not returning to pool - struct is returned to caller

	result.Items = records
	result.Total = response.Total
	result.Limit = api.NewOptInt(limit)
	result.Offset = api.NewOptInt(offset)
	result.HasMore = api.NewOptBool(hasMore)

	return result, nil
}

func (h *Handlers) TriggerReset(ctx context.Context, req *api.TriggerResetRequest) (api.TriggerResetRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	resetType := models.ResetType(req.Type)
	if resetType != models.ResetTypeDaily && resetType != models.ResetTypeWeekly {
		return &api.TriggerResetBadRequest{
			Error:   "BAD_REQUEST",
			Message: "invalid reset type",
		}, nil
	}

	err := h.service.TriggerReset(ctx, resetType)
	if err != nil {
		h.logger.WithError(err).Error("Failed to trigger reset")
		return &api.TriggerResetInternalServerError{
			Error:   "INTERNAL_SERVER_ERROR",
			Message: "failed to trigger reset",
		}, nil
	}

	// Issue: #1607 - Use memory pooling
	result := h.successResponsePool.Get().(*api.SuccessResponse)
	// Note: Not returning to pool - struct is returned to caller

	result.Status = api.NewOptString("success")

	return result, nil
}

func toAPIResetRecord(r models.ResetRecord) api.ResetRecord {
	record := api.ResetRecord{
		ID:        api.NewOptUUID(r.ID),
		Type:      api.NewOptResetType(api.ResetType(r.Type)),
		Status:    api.NewOptResetStatus(api.ResetStatus(r.Status)),
		StartedAt: api.NewOptDateTime(r.StartedAt),
	}

	if r.CompletedAt != nil {
		record.CompletedAt = api.NewOptNilDateTime(*r.CompletedAt)
	}

	if r.Error != nil {
		record.Error = api.NewOptNilString(*r.Error)
	}

	if r.Metadata != nil {
		metadata := make(api.ResetRecordMetadata)
		for k, v := range r.Metadata {
			if vBytes, err := json.Marshal(v); err == nil {
				metadata[k] = vBytes
			}
		}
		record.Metadata = api.NewOptNilResetRecordMetadata(metadata)
	}

	return record
}

func toOptNilDateTime(t *time.Time) api.OptNilDateTime {
	if t == nil {
		return api.OptNilDateTime{}
	}
	return api.NewOptNilDateTime(*t)
}
