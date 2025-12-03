// Issue: #1592 - Character Service ogen Migration (1.5k RPS)
// Handlers for character-service-go - implements api.Handler (ogen)
package server

import (
	"context"
	"time"

	"github.com/necpgame/character-service-go/models"
	"github.com/necpgame/character-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

const (
	DBTimeout    = 50 * time.Millisecond  // Hot path: 1.5k RPS
	CacheTimeout = 10 * time.Millisecond  // Redis
)

// CharacterHandlersOgen implements api.Handler (ogen)
type CharacterHandlersOgen struct {
	service *CharacterService
	logger  *logrus.Logger
}

// NewCharacterHandlersOgen creates new ogen handlers
func NewCharacterHandlersOgen(service *CharacterService) *CharacterHandlersOgen {
	return &CharacterHandlersOgen{
		service: service,
		logger:  GetLogger(),
	}
}

// CreateCharacterV2 implements createCharacterV2 operation
func (h *CharacterHandlersOgen) CreateCharacterV2(ctx context.Context, req *api.CreateCharacterV2Request) (api.CreateCharacterV2Res, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	h.logger.WithField("name", req.Name).Info("CreateCharacterV2 called")

	// Convert ogen request to models.CreateCharacterRequest
	createReq := &models.CreateCharacterRequest{
		Name:   req.Name,
		// Map Class string to ClassID (TODO: lookup in DB)
		// Map Origin string (TODO: implement)
	}

	character, err := h.service.CreateCharacter(ctx, createReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create character")
		return &api.CreateCharacterV2InternalServerError{}, err
	}

	// Convert models.Character to api.CharacterFull
	response := convertCharacterToOgen(character)
	return response, nil
}

// GetCharacterV2 implements getCharacterV2 operation
// Hot path: 1.5k RPS - requires caching
func (h *CharacterHandlersOgen) GetCharacterV2(ctx context.Context, params api.GetCharacterV2Params) (api.GetCharacterV2Res, error) {
	ctx, cancel := context.WithTimeout(ctx, CacheTimeout)
	defer cancel()

	h.logger.WithField("character_id", params.CharacterID).Debug("GetCharacterV2 called")

	// params.CharacterID is already uuid.UUID
	character, err := h.service.GetCharacter(ctx, params.CharacterID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get character")
		return &api.GetCharacterV2NotFound{}, err
	}

	response := convertCharacterToOgen(character)
	return response, nil
}

// GetCurrentCharacter implements getCurrentCharacter operation
func (h *CharacterHandlersOgen) GetCurrentCharacter(ctx context.Context, params api.GetCurrentCharacterParams) (api.GetCurrentCharacterRes, error) {
	ctx, cancel := context.WithTimeout(ctx, CacheTimeout)
	defer cancel()

	h.logger.Debug("GetCurrentCharacter called")

	// TODO: Extract user_id from JWT context
	// For now, return not found
	return &api.GetCurrentCharacterNotFound{}, nil
}

// DeleteCharacterV2 implements deleteCharacterV2 operation
func (h *CharacterHandlersOgen) DeleteCharacterV2(ctx context.Context, req *api.DeleteCharacterRequest, params api.DeleteCharacterV2Params) (api.DeleteCharacterV2Res, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

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
func (h *CharacterHandlersOgen) ValidateCharacterName(ctx context.Context, params api.ValidateCharacterNameParams) (api.ValidateCharacterNameRes, error) {
	ctx, cancel := context.WithTimeout(ctx, CacheTimeout)
	defer cancel()

	h.logger.WithField("name", params.Name).Debug("ValidateCharacterName called")

	// TODO: Implement name validation
	// - Check length (3-20 chars)
	// - Check allowed characters
	// - Check uniqueness in DB
	// - Cache results (5 min TTL)

	response := &api.ValidateNameResponse{
		Available: api.NewOptBool(true),
		Message:   api.NewOptNilString("Name is available"),
	}
	return response, nil
}

// Helper: Convert models.Character to api.CharacterFull
func convertCharacterToOgen(char *models.Character) *api.CharacterFull {
	return &api.CharacterFull{
		ID:        api.NewOptUUID(char.ID),
		PlayerID:  api.NewOptUUID(char.AccountID),
		Name:      api.NewOptString(char.Name),
		Level:     api.NewOptInt(char.Level),
		// TODO: Map other fields (Class, Origin, CreatedAt, etc.)
	}
}
