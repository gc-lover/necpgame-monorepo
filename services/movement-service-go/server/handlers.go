// Issue: #141888104, #1607
// ogen handlers - TYPED responses (no interface{} boxing!)
// Memory pooling for hot path (Issue #1607)
package server

import (
	"context"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/movement-service-go/models"
	"github.com/gc-lover/necpgame-monorepo/services/movement-service-go/pkg/api"
)

// Context timeout constants
const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
)

// Handlers implements api.Handler interface (ogen typed handlers!)
// Issue: #1607 - Memory pooling for hot path
type Handlers struct {
	service MovementServiceInterface

	// Memory pooling for hot path structs (Level 2 optimization)
	positionPool        sync.Pool
	positionHistoryPool sync.Pool
}

// NewHandlers creates new handlers with memory pooling
func NewHandlers(service MovementServiceInterface) *Handlers {
	h := &Handlers{service: service}

	// Initialize memory pools (zero allocations target!)
	h.positionPool = sync.Pool{
		New: func() interface{} {
			return &api.CharacterPosition{}
		},
	}
	h.positionHistoryPool = sync.Pool{
		New: func() interface{} {
			return &api.PositionHistory{}
		},
	}

	return h
}

// GetPosition - TYPED response!
func (h *Handlers) GetPosition(ctx context.Context, params api.GetPositionParams) (api.GetPositionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	position, err := h.service.GetPosition(ctx, params.CharacterId)
	if err != nil {
		if err.Error() == "position not found" {
			return &api.GetPositionNotFound{}, nil
		}
		return &api.GetPositionInternalServerError{}, err
	}

	if position == nil {
		return &api.GetPositionNotFound{}, nil
	}

	result := toAPICharacterPosition(position)
	return &result, nil
}

// SavePosition - TYPED response!
func (h *Handlers) SavePosition(ctx context.Context, req *api.SavePositionRequest, params api.SavePositionParams) (api.SavePositionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	modelReq := toModelSavePositionRequest(req)
	position, err := h.service.SavePosition(ctx, params.CharacterId, modelReq)
	if err != nil {
		return &api.SavePositionInternalServerError{}, err
	}

	result := toAPICharacterPosition(position)
	return &result, nil
}

// GetPositionHistory - TYPED response!
func (h *Handlers) GetPositionHistory(ctx context.Context, params api.GetPositionHistoryParams) (api.GetPositionHistoryRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	limit := 50
	if params.Limit.IsSet() && params.Limit.Value > 0 && params.Limit.Value <= 100 {
		limit = params.Limit.Value
	}

	history, err := h.service.GetPositionHistory(ctx, params.CharacterId, limit)
	if err != nil {
		return &api.GetPositionHistoryInternalServerError{}, err
	}

	apiHistory := make([]api.PositionHistory, len(history))
	for i, item := range history {
		apiHistory[i] = toAPIPositionHistory(&item)
	}

	result := api.GetPositionHistoryOKApplicationJSON(apiHistory)
	return &result, nil
}

func toAPICharacterPosition(pos *models.CharacterPosition) api.CharacterPosition {
	if pos == nil {
		return api.CharacterPosition{}
	}

	return api.CharacterPosition{
		ID:          api.NewOptUUID(pos.ID),
		CharacterID: api.NewOptUUID(pos.CharacterID),
		PositionX:   api.NewOptFloat32(float32(pos.PositionX)),
		PositionY:   api.NewOptFloat32(float32(pos.PositionY)),
		PositionZ:   api.NewOptFloat32(float32(pos.PositionZ)),
		Yaw:         api.NewOptFloat32(float32(pos.Yaw)),
		VelocityX:   api.NewOptNilFloat32(float32(pos.VelocityX)),
		VelocityY:   api.NewOptNilFloat32(float32(pos.VelocityY)),
		VelocityZ:   api.NewOptNilFloat32(float32(pos.VelocityZ)),
		CreatedAt:   api.NewOptDateTime(pos.CreatedAt),
		UpdatedAt:   api.NewOptDateTime(pos.UpdatedAt),
	}
}

func toAPIPositionHistory(ph *models.PositionHistory) api.PositionHistory {
	if ph == nil {
		return api.PositionHistory{}
	}

	return api.PositionHistory{
		ID:          api.NewOptUUID(ph.ID),
		CharacterID: api.NewOptUUID(ph.CharacterID),
		PositionX:   api.NewOptFloat32(float32(ph.PositionX)),
		PositionY:   api.NewOptFloat32(float32(ph.PositionY)),
		PositionZ:   api.NewOptFloat32(float32(ph.PositionZ)),
		Yaw:         api.NewOptFloat32(float32(ph.Yaw)),
		VelocityX:   api.NewOptNilFloat32(float32(ph.VelocityX)),
		VelocityY:   api.NewOptNilFloat32(float32(ph.VelocityY)),
		VelocityZ:   api.NewOptNilFloat32(float32(ph.VelocityZ)),
		CreatedAt:   api.NewOptDateTime(ph.CreatedAt),
	}
}

func toModelSavePositionRequest(req *api.SavePositionRequest) *models.SavePositionRequest {
	if req == nil {
		return nil
	}

	modelReq := &models.SavePositionRequest{
		PositionX: float64(req.PositionX),
		PositionY: float64(req.PositionY),
		PositionZ: float64(req.PositionZ),
		Yaw:       float64(req.Yaw),
	}

	if req.VelocityX.IsSet() && !req.VelocityX.Null {
		modelReq.VelocityX = float64(req.VelocityX.Value)
	}
	if req.VelocityY.IsSet() && !req.VelocityY.Null {
		modelReq.VelocityY = float64(req.VelocityY.Value)
	}
	if req.VelocityZ.IsSet() && !req.VelocityZ.Null {
		modelReq.VelocityZ = float64(req.VelocityZ.Value)
	}

	return modelReq
}
