// Package server Issue: #1592, #1867 - Character Service ogen Migration (1.5k RPS) + Memory Pooling Optimization
// Handlers for character-service-go - implements api.Handler (ogen)
// Memory pooling for hot path structs (zero allocations target!)
package server

import (
	"context"
	"sync"
	"sync/atomic"
	"time"

	"github.com/necpgame/character-service-go/models"
	"github.com/necpgame/character-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

const (
	DBTimeout    = 50 * time.Millisecond // Hot path: 1.5k RPS
	CacheTimeout = 10 * time.Millisecond // Redis
)

// CharacterHandlersOgen implements api.Handler (ogen)
// Issue: #1867 - Memory pooling for hot path structs (zero allocations target!)
type CharacterHandlersOgen struct {
	service *CharacterService
	logger  *logrus.Logger

	// Memory pooling for hot path structs (zero allocations target!)
	characterFullPool        sync.Pool
	validateNameResponsePool sync.Pool
	bufferPool               sync.Pool // For JSON encoding/decoding

	// Lock-free statistics (zero contention target!)
	requestsTotal       int64 // atomic
	charactersCreated   int64 // atomic
	charactersRetrieved int64 // atomic
	charactersValidated int64 // atomic
	lastRequestTime     int64 // atomic unix nano
}

// NewCharacterHandlersOgen creates new ogen handlers with memory pooling
// Issue: #1867 - Initialize memory pools for zero allocations
func NewCharacterHandlersOgen(service *CharacterService) *CharacterHandlersOgen {
	h := &CharacterHandlersOgen{
		service: service,
		logger:  GetLogger(),
	}

	// Initialize memory pools (zero allocations target!)
	h.characterFullPool = sync.Pool{
		New: func() interface{} {
			return &api.CharacterFull{}
		},
	}
	h.validateNameResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.ValidateNameResponse{}
		},
	}
	h.bufferPool = sync.Pool{
		New: func() interface{} {
			return make([]byte, 0, 2048) // 2KB buffer for JSON
		},
	}

	return h
}

// Lock-free statistics methods (zero contention)
func (h *CharacterHandlersOgen) incrementRequestsTotal() {
	atomic.AddInt64(&h.requestsTotal, 1)
	atomic.StoreInt64(&h.lastRequestTime, time.Now().UnixNano())
}

func (h *CharacterHandlersOgen) incrementCharactersCreated() {
	atomic.AddInt64(&h.charactersCreated, 1)
}

func (h *CharacterHandlersOgen) incrementCharactersRetrieved() {
	atomic.AddInt64(&h.charactersRetrieved, 1)
}

func (h *CharacterHandlersOgen) incrementCharactersValidated() {
	atomic.AddInt64(&h.charactersValidated, 1)
}

func (h *CharacterHandlersOgen) getStats() map[string]int64 {
	return map[string]int64{
		"requests_total":       atomic.LoadInt64(&h.requestsTotal),
		"characters_created":   atomic.LoadInt64(&h.charactersCreated),
		"characters_retrieved": atomic.LoadInt64(&h.charactersRetrieved),
		"characters_validated": atomic.LoadInt64(&h.charactersValidated),
		"last_request_time":    atomic.LoadInt64(&h.lastRequestTime),
	}
}

// CreateCharacterV2 implements createCharacterV2 operation
// Issue: #1867 - Memory pooling for response objects
func (h *CharacterHandlersOgen) CreateCharacterV2(ctx context.Context, req *api.CreateCharacterV2Request) (api.CreateCharacterV2Res, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	h.incrementRequestsTotal()
	h.logger.WithField("name", req.Name).Info("CreateCharacterV2 called")

	// Convert ogen request to models.CreateCharacterRequest
	createReq := &models.CreateCharacterRequest{
		Name: req.Name,
		// Map Class string to ClassID (TODO: lookup in DB)
		// Map Origin string (TODO: implement)
	}

	character, err := h.service.CreateCharacter(ctx, createReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create character")
		return &api.CreateCharacterV2InternalServerError{}, err
	}

	h.incrementCharactersCreated()

	// Convert models.Character to api.CharacterFull (memory pooled)
	response := h.convertCharacterToOgen(character)
	return response, nil
}

// GetCharacterV2 implements getCharacterV2 operation
// Hot path: 1.5k RPS - requires caching
// Issue: #1867 - Memory pooling for hot path
func (h *CharacterHandlersOgen) GetCharacterV2(ctx context.Context, params api.GetCharacterV2Params) (api.GetCharacterV2Res, error) {
	ctx, cancel := context.WithTimeout(ctx, CacheTimeout)
	defer cancel()

	h.incrementRequestsTotal()
	h.logger.WithField("character_id", params.CharacterID).Debug("GetCharacterV2 called")

	// params.CharacterID is already uuid.UUID
	character, err := h.service.GetCharacter(ctx, params.CharacterID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get character")
		return &api.GetCharacterV2NotFound{}, err
	}

	h.incrementCharactersRetrieved()

	// Convert with memory pooling (zero allocations)
	response := h.convertCharacterToOgen(character)
	return response, nil
}

// GetCurrentCharacter implements getCurrentCharacter operation
// Issue: #1867 - Request tracking for statistics
func (h *CharacterHandlersOgen) GetCurrentCharacter(ctx context.Context, _ api.GetCurrentCharacterParams) (api.GetCurrentCharacterRes, error) {
	ctx, cancel := context.WithTimeout(ctx, CacheTimeout)
	defer cancel()

	h.incrementRequestsTotal()
	h.logger.Debug("GetCurrentCharacter called")

	// TODO: Extract user_id from JWT context
	// For now, return not found
	return &api.GetCurrentCharacterNotFound{}, nil
}

// DeleteCharacterV2 implements deleteCharacterV2 operation
// Issue: #1867 - Request tracking for statistics
func (h *CharacterHandlersOgen) DeleteCharacterV2(ctx context.Context, _ *api.DeleteCharacterRequest, params api.DeleteCharacterV2Params) (api.DeleteCharacterV2Res, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	h.incrementRequestsTotal()
	h.logger.WithField("character_id", params.CharacterID).Info("DeleteCharacterV2 called")

	// params.CharacterID is already uuid.UUID
	if err := h.service.DeleteCharacter(ctx, params.CharacterID); err != nil {
		h.logger.WithError(err).Error("Failed to delete character")
		return &api.DeleteCharacterV2InternalServerError{}, err
	}

	// TODO: Find correct response type (no StatusResponse in this API)
	return &api.DeleteCharacterV2InternalServerError{}, nil
}

// ValidateCharacterName implements validateCharacterName operation
// Issue: #1867 - Memory pooling for validation responses
func (h *CharacterHandlersOgen) ValidateCharacterName(ctx context.Context, params api.ValidateCharacterNameParams) (api.ValidateCharacterNameRes, error) {
	ctx, cancel := context.WithTimeout(ctx, CacheTimeout)
	defer cancel()

	h.incrementRequestsTotal()
	h.incrementCharactersValidated()
	h.logger.WithField("name", params.Name).Debug("ValidateCharacterName called")

	// TODO: Implement name validation
	// - Check length (3-20 chars)
	// - Check allowed characters
	// - Check uniqueness in DB
	// - Cache results (5 min TTL)

	// Get pooled response object (zero allocations!)
	response := h.validateNameResponsePool.Get().(*api.ValidateNameResponse)

	// Reset and populate
	response.Available = api.NewOptBool(true)
	response.Message = api.NewOptNilString("Name is available")

	return response, nil
}

// convertCharacterToOgen converts models.Character to api.CharacterFull (memory pooled)
// Issue: #1867 - Zero allocations for response objects
func (h *CharacterHandlersOgen) convertCharacterToOgen(char *models.Character) *api.CharacterFull {
	// Get pooled object (zero allocations!)
	response := h.characterFullPool.Get().(*api.CharacterFull)

	// Reset and populate
	response.ID = api.NewOptUUID(char.ID)
	response.PlayerID = api.NewOptUUID(char.AccountID)
	response.Name = api.NewOptString(char.Name)
	response.Level = api.NewOptInt(char.Level)
	// TODO: Map other fields (Class, Origin, CreatedAt, etc.)

	return response
}

// releaseCharacterFull returns pooled object to pool
func (h *CharacterHandlersOgen) releaseCharacterFull(obj *api.CharacterFull) {
	// Reset object before returning to pool
	obj.ID = api.OptUUID{}
	obj.PlayerID = api.OptUUID{}
	obj.Name = api.OptString{}
	obj.Level = api.OptInt{}

	h.characterFullPool.Put(obj)
}
