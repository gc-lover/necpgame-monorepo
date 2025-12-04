// Issue: #150, #1607 - Client Service ogen handlers
package server

import (
	"context"
	"sync"
	"time"

	"github.com/google/uuid"
	api "github.com/necpgame/client-service-go/pkg/api"
)

// Context timeout constants
const (
	DBTimeout = 50 * time.Millisecond
)

// Handlers implements api.Handler interface (ogen typed handlers)
// Issue: #1607 - Memory pooling for hot path structs (Level 2 optimization)
type Handlers struct {
	service WeaponEffectsServiceInterface

	// Memory pooling for hot path structs (zero allocations target!)
	visualEffectResponsePool sync.Pool
	audioEffectResponsePool  sync.Pool
	effectInfoPool           sync.Pool
}

// NewHandlers creates new handlers
func NewHandlers(service WeaponEffectsServiceInterface) *Handlers {
	h := &Handlers{service: service}

	// Initialize memory pools (zero allocations target!)
	h.visualEffectResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.VisualEffectResponse{}
		},
	}
	h.audioEffectResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.AudioEffectResponse{}
		},
	}
	h.effectInfoPool = sync.Pool{
		New: func() interface{} {
			return &api.EffectInfo{}
		},
	}

	return h
}

// TriggerVisualEffect - TYPED response!
func (h *Handlers) TriggerVisualEffect(ctx context.Context, req *api.TriggerVisualEffectRequest) (api.TriggerVisualEffectRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Convert request to service format
	effectType := string(req.EffectType)
	mechanicType := req.MechanicType
	position := map[string]float64{
		"x": float64(req.Position.X),
		"y": float64(req.Position.Y),
		"z": float64(req.Position.Z),
	}
	var targetID *uuid.UUID
	if req.TargetID.Set {
		id := uuid.UUID(req.TargetID.Value)
		targetID = &id
	}
	effectData := make(map[string]interface{})
	if req.EffectData.Set {
		// Convert jx.Raw to map
		for k, v := range req.EffectData.Value {
			effectData[k] = v
		}
	}

	// Service call
	effectID, err := h.service.TriggerVisualEffect(ctx, effectType, mechanicType, position, targetID, effectData)
	if err != nil {
		return &api.TriggerVisualEffectInternalServerError{}, err
	}

	// Issue: #1607 - Use memory pooling
	result := h.visualEffectResponsePool.Get().(*api.VisualEffectResponse)
	// Note: Not returning to pool - struct is returned to caller

	result.EffectID = api.NewOptUUID(effectID)
	return result, nil
}

// TriggerAudioEffect - TYPED response!
func (h *Handlers) TriggerAudioEffect(ctx context.Context, req *api.TriggerAudioEffectRequest) (api.TriggerAudioEffectRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Convert request to service format
	effectType := string(req.EffectType)
	mechanicType := req.MechanicType
	soundID := req.SoundID
	position := map[string]float64{
		"x": float64(req.Position.X),
		"y": float64(req.Position.Y),
		"z": float64(req.Position.Z),
	}
	var volume, pitch *float64
	if req.Volume.Set {
		v := float64(req.Volume.Value)
		volume = &v
	}
	if req.Pitch.Set {
		p := float64(req.Pitch.Value)
		pitch = &p
	}

	// Service call
	effectID, err := h.service.TriggerAudioEffect(ctx, effectType, mechanicType, soundID, position, volume, pitch)
	if err != nil {
		return &api.TriggerAudioEffectInternalServerError{}, err
	}

	// Issue: #1607 - Use memory pooling
	result := h.audioEffectResponsePool.Get().(*api.AudioEffectResponse)
	// Note: Not returning to pool - struct is returned to caller

	result.EffectID = api.NewOptUUID(effectID)
	return result, nil
}

// GetEffect - TYPED response!
func (h *Handlers) GetEffect(ctx context.Context, params api.GetEffectParams) (api.GetEffectRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	effectID := uuid.UUID(params.EffectId)

	// Service call
	effect, err := h.service.GetEffect(ctx, effectID)
	if err != nil {
		return &api.GetEffectInternalServerError{}, err
	}

	if effect == nil {
		return &api.GetEffectNotFound{}, nil
	}

	// Issue: #1607 - Use memory pooling
	effectInfo := h.effectInfoPool.Get().(*api.EffectInfo)
	// Note: Not returning to pool - struct is returned to caller

	effectInfo.ID = api.NewOptUUID(effectID)
	if effectType, ok := effect["effect_type"].(string); ok {
		effectInfo.EffectType = api.NewOptString(effectType)
	}
	if mechanicType, ok := effect["mechanic_type"].(string); ok {
		effectInfo.MechanicType = api.NewOptString(mechanicType)
	}

	return effectInfo, nil
}

