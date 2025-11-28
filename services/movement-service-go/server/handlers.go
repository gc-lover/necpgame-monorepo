package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/movement-service-go/models"
	"github.com/necpgame/movement-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

type Handlers struct {
	service *MovementService
}

func NewHandlers(service *MovementService) *Handlers {
	return &Handlers{
		service: service,
	}
}

func toAPICharacterPosition(pos *models.CharacterPosition) api.CharacterPosition {
	charID := openapi_types.UUID(pos.CharacterID)
	id := openapi_types.UUID(pos.ID)
	posX := float32(pos.PositionX)
	posY := float32(pos.PositionY)
	posZ := float32(pos.PositionZ)
	
	return api.CharacterPosition{
		CharacterId: &charID,
		Id:          &id,
		PositionX:   &posX,
		PositionY:   &posY,
		PositionZ:   &posZ,
		CreatedAt:   &pos.CreatedAt,
	}
}

func toAPIPositionHistory(pos models.PositionHistory) api.PositionHistory {
	charID := openapi_types.UUID(pos.CharacterID)
	id := openapi_types.UUID(pos.ID)
	posX := float32(pos.PositionX)
	posY := float32(pos.PositionY)
	posZ := float32(pos.PositionZ)
	
	return api.PositionHistory{
		CharacterId: &charID,
		Id:          &id,
		PositionX:   &posX,
		PositionY:   &posY,
		PositionZ:   &posZ,
		CreatedAt:   &pos.CreatedAt,
	}
}

func (h *Handlers) GetPositionHistory(w http.ResponseWriter, r *http.Request, characterId openapi_types.UUID, params api.GetPositionHistoryParams) {
	limit := 100
	if params.Limit != nil {
		limit = *params.Limit
	}

	charUUID, err := uuid.Parse(characterId.String())
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		json.NewEncoder(w).Encode(api.Error{Message: "Invalid character ID"})
		return
	}

	positions, err := h.service.GetPositionHistory(r.Context(), charUUID, limit)
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		json.NewEncoder(w).Encode(api.Error{Message: err.Error()})
		return
	}

	apiPositions := make([]api.PositionHistory, 0, len(positions))
	for _, pos := range positions {
		apiPositions = append(apiPositions, toAPIPositionHistory(pos))
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(apiPositions)
}

func (h *Handlers) GetPosition(w http.ResponseWriter, r *http.Request, characterId openapi_types.UUID) {
	charUUID, err := uuid.Parse(characterId.String())
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		json.NewEncoder(w).Encode(api.Error{Message: "Invalid character ID"})
		return
	}

	position, err := h.service.GetPosition(r.Context(), charUUID)
	if err != nil {
		w.WriteHeader(http.StatusNotFound)
		json.NewEncoder(w).Encode(api.Error{Message: "Position not found"})
		return
	}

	apiPosition := toAPICharacterPosition(position)

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(apiPosition)
}

func (h *Handlers) SavePosition(w http.ResponseWriter, r *http.Request, characterId openapi_types.UUID) {
	charUUID, err := uuid.Parse(characterId.String())
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		json.NewEncoder(w).Encode(api.Error{Message: "Invalid character ID"})
		return
	}

	var req api.SavePositionJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		w.WriteHeader(http.StatusBadRequest)
		json.NewEncoder(w).Encode(api.Error{Message: "Invalid request body"})
		return
	}

	modelReq := &models.SavePositionRequest{
		PositionX: float64(req.PositionX),
		PositionY: float64(req.PositionY),
		PositionZ: float64(req.PositionZ),
		Yaw:       0,
	}

	position, err := h.service.SavePosition(r.Context(), charUUID, modelReq)
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		json.NewEncoder(w).Encode(api.Error{Message: err.Error()})
		return
	}

	apiPosition := toAPICharacterPosition(position)

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(apiPosition)
}
