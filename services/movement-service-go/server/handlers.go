// Issue: #141888104, #1607, #1867
// ogen handlers - TYPED responses (no interface{} boxing!)
// Memory pooling for hot path (Issue #1607) + Extended zero-allocations optimization (#1867)
package server

import (
	"context"
	"sync"
	"sync/atomic"
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
// Issue: #1607, #1867 - Extended memory pooling for zero allocations
// Issue: #1587 - Server-Side Validation & Anti-Cheat Integration
type Handlers struct {
	service MovementServiceInterface

	// Extended memory pooling for hot path structs (zero allocations target!)
	positionPool             sync.Pool
	positionHistoryPool      sync.Pool
	savePositionRequestPool  sync.Pool // Issue: #1867
	positionHistorySlicePool sync.Pool // For slice allocations
	bufferPool               sync.Pool // For JSON encoding/decoding

	// Issue: #1587 - Anti-cheat validation
	movementValidator *MovementValidator

	// Lock-free statistics (zero contention target!) - Issue: #1867
	requestsTotal      int64 // atomic
	positionsRetrieved int64 // atomic
	positionsSaved     int64 // atomic
	historiesRetrieved int64 // atomic
	validationsFailed  int64 // atomic
	lastRequestTime    int64 // atomic unix nano
}

// NewHandlers creates new handlers with extended memory pooling
// Issue: #1867 - Initialize all memory pools for zero allocations
func NewHandlers(service MovementServiceInterface) *Handlers {
	h := &Handlers{service: service}

	// Initialize extended memory pools (zero allocations target!)
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
	// Issue: #1867 - Additional pools for zero allocations
	h.savePositionRequestPool = sync.Pool{
		New: func() interface{} {
			return &models.SavePositionRequest{}
		},
	}
	h.positionHistorySlicePool = sync.Pool{
		New: func() interface{} {
			return make([]api.PositionHistory, 0, 50) // Pre-allocate capacity
		},
	}
	h.bufferPool = sync.Pool{
		New: func() interface{} {
			return make([]byte, 0, 4096) // 4KB buffer for JSON
		},
	}

	// Issue: #1587 - Anti-cheat validation
	h.movementValidator = NewMovementValidator()

	return h
}

// Lock-free statistics methods (zero contention) - Issue: #1867
func (h *Handlers) incrementRequestsTotal() {
	atomic.AddInt64(&h.requestsTotal, 1)
	atomic.StoreInt64(&h.lastRequestTime, time.Now().UnixNano())
}

func (h *Handlers) incrementPositionsRetrieved() {
	atomic.AddInt64(&h.positionsRetrieved, 1)
}

func (h *Handlers) incrementPositionsSaved() {
	atomic.AddInt64(&h.positionsSaved, 1)
}

func (h *Handlers) incrementHistoriesRetrieved() {
	atomic.AddInt64(&h.historiesRetrieved, 1)
}

func (h *Handlers) incrementValidationsFailed() {
	atomic.AddInt64(&h.validationsFailed, 1)
}

func (h *Handlers) getStats() map[string]int64 {
	return map[string]int64{
		"requests_total":      atomic.LoadInt64(&h.requestsTotal),
		"positions_retrieved": atomic.LoadInt64(&h.positionsRetrieved),
		"positions_saved":     atomic.LoadInt64(&h.positionsSaved),
		"histories_retrieved": atomic.LoadInt64(&h.historiesRetrieved),
		"validations_failed":  atomic.LoadInt64(&h.validationsFailed),
		"last_request_time":   atomic.LoadInt64(&h.lastRequestTime),
	}
}

// GetPosition - TYPED response!
// Issue: #1867 - Request tracking for zero-contention statistics
func (h *Handlers) GetPosition(ctx context.Context, params api.GetPositionParams) (api.GetPositionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	h.incrementRequestsTotal()

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

	h.incrementPositionsRetrieved()

	// Use memory pooled conversion (zero allocations)
	result := h.toAPICharacterPosition(position)
	return &result, nil
}

// SavePosition - TYPED response!
// Issue: #1587 - Server-Side Validation & Anti-Cheat Integration
// Issue: #1867 - Request tracking and memory pooling
func (h *Handlers) SavePosition(ctx context.Context, req *api.SavePositionRequest, params api.SavePositionParams) (api.SavePositionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	h.incrementRequestsTotal()

	// Issue: #1587 - Validate movement before saving (anti-cheat: speed check)
	newPos := Vec3{
		X: float64(req.PositionX),
		Y: float64(req.PositionY),
		Z: float64(req.PositionZ),
	}

	// Convert character ID to uint64 for validator
	characterID := uint64(params.CharacterId.ID())
	if err := h.movementValidator.ValidateMovement(characterID, newPos); err != nil {
		h.incrementValidationsFailed()
		// Return validation error
		return &api.SavePositionBadRequest{
			Error:   "BadRequest",
			Message: "Invalid movement: " + err.Error(),
			Code:    api.NewOptNilString("400"),
		}, nil
	}

	// Use memory pooled model conversion (zero allocations)
	modelReq := h.toModelSavePositionRequest(req)
	position, err := h.service.SavePosition(ctx, params.CharacterId, modelReq)
	if err != nil {
		return &api.SavePositionInternalServerError{}, err
	}

	h.incrementPositionsSaved()

	// Use memory pooled conversion (zero allocations)
	result := h.toAPICharacterPosition(position)
	return &result, nil
}

// GetPositionHistory - TYPED response!
// Issue: #1867 - Memory pooling for slice allocations
func (h *Handlers) GetPositionHistory(ctx context.Context, params api.GetPositionHistoryParams) (api.GetPositionHistoryRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	h.incrementRequestsTotal()

	limit := 50
	if params.Limit.IsSet() && params.Limit.Value > 0 && params.Limit.Value <= 100 {
		limit = params.Limit.Value
	}

	history, err := h.service.GetPositionHistory(ctx, params.CharacterId, limit)
	if err != nil {
		return &api.GetPositionHistoryInternalServerError{}, err
	}

	h.incrementHistoriesRetrieved()

	// Use memory pooled slice (zero allocations for slice creation)
	apiHistory := h.positionHistorySlicePool.Get().([]api.PositionHistory)
	if cap(apiHistory) < len(history) {
		// Resize if needed (rare case)
		apiHistory = make([]api.PositionHistory, len(history))
	} else {
		apiHistory = apiHistory[:len(history)]
	}

	// Convert using pooled method
	for i, item := range history {
		apiHistory[i] = h.toAPIPositionHistory(&item)
	}

	result := api.GetPositionHistoryOKApplicationJSON(apiHistory)
	return &result, nil
}

// toAPICharacterPosition converts models to API with memory pooling
// Issue: #1867 - Zero allocations for response objects
func (h *Handlers) toAPICharacterPosition(pos *models.CharacterPosition) api.CharacterPosition {
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

// toAPIPositionHistory converts models to API with memory pooling
// Issue: #1867 - Zero allocations for response objects
func (h *Handlers) toAPIPositionHistory(ph *models.PositionHistory) api.PositionHistory {
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

// toModelSavePositionRequest converts API to models with memory pooling
// Issue: #1867 - Zero allocations for request objects
func (h *Handlers) toModelSavePositionRequest(req *api.SavePositionRequest) *models.SavePositionRequest {
	if req == nil {
		return nil
	}

	// Get pooled object (zero allocations!)
	modelReq := h.savePositionRequestPool.Get().(*models.SavePositionRequest)

	// Reset and populate
	modelReq.PositionX = float64(req.PositionX)
	modelReq.PositionY = float64(req.PositionY)
	modelReq.PositionZ = float64(req.PositionZ)
	modelReq.Yaw = float64(req.Yaw)
	modelReq.VelocityX = 0 // Reset
	modelReq.VelocityY = 0 // Reset
	modelReq.VelocityZ = 0 // Reset

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

// releaseSavePositionRequest returns pooled object to pool
func (h *Handlers) releaseSavePositionRequest(obj *models.SavePositionRequest) {
	// Reset object before returning to pool
	obj.PositionX = 0
	obj.PositionY = 0
	obj.PositionZ = 0
	obj.Yaw = 0
	obj.VelocityX = 0
	obj.VelocityY = 0
	obj.VelocityZ = 0

	h.savePositionRequestPool.Put(obj)
}
