// Issue: #141888104
package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/movement-service-go/models"
	"github.com/necpgame/movement-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type MovementHandlers struct {
	service MovementServiceInterface
	logger  *logrus.Logger
}

func NewMovementHandlers(service MovementServiceInterface) *MovementHandlers {
	return &MovementHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *MovementHandlers) GetPosition(w http.ResponseWriter, r *http.Request, characterId openapi_types.UUID) {
	ctx := r.Context()
	characterID := uuid.UUID(characterId)

	position, err := h.service.GetPosition(ctx, characterID)
	if err != nil {
		if err.Error() == "position not found" {
			h.respondError(w, http.StatusNotFound, "position not found")
			return
		}
		h.logger.WithError(err).Error("Failed to get position")
		h.respondError(w, http.StatusInternalServerError, "failed to get position")
		return
	}

	if position == nil {
		h.respondError(w, http.StatusNotFound, "position not found")
		return
	}

	apiPosition := toAPICharacterPosition(position)
	h.respondJSON(w, http.StatusOK, apiPosition)
}

func (h *MovementHandlers) SavePosition(w http.ResponseWriter, r *http.Request, characterId openapi_types.UUID) {
	ctx := r.Context()
	characterID := uuid.UUID(characterId)

	var req api.SavePositionRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	modelReq := toModelSavePositionRequest(&req)

	position, err := h.service.SavePosition(ctx, characterID, modelReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to save position")
		h.respondError(w, http.StatusInternalServerError, "failed to save position")
		return
	}

	apiPosition := toAPICharacterPosition(position)
	h.respondJSON(w, http.StatusOK, apiPosition)
}

func (h *MovementHandlers) GetPositionHistory(w http.ResponseWriter, r *http.Request, characterId openapi_types.UUID, params api.GetPositionHistoryParams) {
	ctx := r.Context()
	characterID := uuid.UUID(characterId)

	limit := 50
	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 100 {
		limit = *params.Limit
	}

	history, err := h.service.GetPositionHistory(ctx, characterID, limit)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get position history")
		h.respondError(w, http.StatusInternalServerError, "failed to get position history")
		return
	}

	apiHistory := make([]api.PositionHistory, len(history))
	for i, item := range history {
		apiHistory[i] = toAPIPositionHistory(&item)
	}

	h.respondJSON(w, http.StatusOK, apiHistory)
}

func (h *MovementHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *MovementHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := api.Error{
		Error:   http.StatusText(status),
		Message: message,
	}
	h.respondJSON(w, status, errorResponse)
}

func toAPICharacterPosition(pos *models.CharacterPosition) api.CharacterPosition {
	if pos == nil {
		return api.CharacterPosition{}
	}

	apiID := openapi_types.UUID(pos.ID)
	apiCharID := openapi_types.UUID(pos.CharacterID)

	posX := float32(pos.PositionX)
	posY := float32(pos.PositionY)
	posZ := float32(pos.PositionZ)
	yaw := float32(pos.Yaw)
	velX := float32(pos.VelocityX)
	velY := float32(pos.VelocityY)
	velZ := float32(pos.VelocityZ)

	return api.CharacterPosition{
		Id:          &apiID,
		CharacterId: &apiCharID,
		PositionX:   &posX,
		PositionY:   &posY,
		PositionZ:   &posZ,
		Yaw:         &yaw,
		VelocityX:   &velX,
		VelocityY:   &velY,
		VelocityZ:   &velZ,
		CreatedAt:   &pos.CreatedAt,
		UpdatedAt:   &pos.UpdatedAt,
	}
}

func toAPIPositionHistory(ph *models.PositionHistory) api.PositionHistory {
	if ph == nil {
		return api.PositionHistory{}
	}

	apiID := openapi_types.UUID(ph.ID)
	apiCharID := openapi_types.UUID(ph.CharacterID)

	posX := float32(ph.PositionX)
	posY := float32(ph.PositionY)
	posZ := float32(ph.PositionZ)
	yaw := float32(ph.Yaw)
	velX := float32(ph.VelocityX)
	velY := float32(ph.VelocityY)
	velZ := float32(ph.VelocityZ)

	return api.PositionHistory{
		Id:          &apiID,
		CharacterId: &apiCharID,
		PositionX:   &posX,
		PositionY:   &posY,
		PositionZ:   &posZ,
		Yaw:         &yaw,
		VelocityX:   &velX,
		VelocityY:   &velY,
		VelocityZ:   &velZ,
		CreatedAt:   &ph.CreatedAt,
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

	if req.VelocityX != nil {
		modelReq.VelocityX = float64(*req.VelocityX)
	}
	if req.VelocityY != nil {
		modelReq.VelocityY = float64(*req.VelocityY)
	}
	if req.VelocityZ != nil {
		modelReq.VelocityZ = float64(*req.VelocityZ)
	}

	return modelReq
}
