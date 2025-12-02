// Issue: #39
package server

import (
	"encoding/json"
	"net/http"

	"github.com/necpgame/combat-sandevistan-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

type SandevistanHandlers struct {
	service SandevistanService
	logger  *logrus.Logger
}

func NewSandevistanHandlers() *SandevistanHandlers {
	repo := NewInMemoryRepository()
	logger := GetLogger()
	service := NewSandevistanService(repo, logger)

	return &SandevistanHandlers{
		service: service,
		logger:  logger,
	}
}

func (h *SandevistanHandlers) ActivateSandevistan(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID) {
	ctx := r.Context()

	playerUUID, err := uuid.Parse(playerId.String())
	if err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid player ID")
		return
	}

	activation, err := h.service.Activate(ctx, playerUUID)
	if err != nil {
		if err.Error() == "sandevistan already active" {
			respondError(w, http.StatusConflict, err, err.Error())
			return
		}
		respondError(w, http.StatusInternalServerError, err, "Failed to activate Sandevistan")
		return
	}

	respondJSON(w, http.StatusOK, activation)
}

func (h *SandevistanHandlers) DeactivateSandevistan(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID) {
	ctx := r.Context()

	playerUUID, err := uuid.Parse(playerId.String())
	if err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid player ID")
		return
	}

	if err := h.service.Deactivate(ctx, playerUUID); err != nil {
		if err.Error() == "sandevistan not active" {
			respondError(w, http.StatusNotFound, err, err.Error())
			return
		}
		respondError(w, http.StatusInternalServerError, err, "Failed to deactivate Sandevistan")
		return
	}

	status := "deactivated"
	response := api.StatusResponse{
		Status: &status,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *SandevistanHandlers) GetSandevistanStatus(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID) {
	ctx := r.Context()

	playerUUID, err := uuid.Parse(playerId.String())
	if err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid player ID")
		return
	}

	status, err := h.service.GetStatus(ctx, playerUUID)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err, "Failed to get Sandevistan status")
		return
	}

	respondJSON(w, http.StatusOK, status)
}

func (h *SandevistanHandlers) UseActionBudget(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID) {
	ctx := r.Context()

	playerUUID, err := uuid.Parse(playerId.String())
	if err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid player ID")
		return
	}

	var req api.UseActionBudgetJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid request body")
		return
	}

	result, err := h.service.UseActionBudget(ctx, playerUUID, req.Actions)
	if err != nil {
		if err.Error() == "too many actions in batch" || err.Error() == "insufficient action budget" {
			respondError(w, http.StatusBadRequest, err, err.Error())
			return
		}
		if err.Error() == "sandevistan not in active phase" {
			respondError(w, http.StatusConflict, err, err.Error())
			return
		}
		respondError(w, http.StatusInternalServerError, err, "Failed to use action budget")
		return
	}

	respondJSON(w, http.StatusOK, result)
}

func (h *SandevistanHandlers) SetTemporalMarks(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID) {
	ctx := r.Context()

	playerUUID, err := uuid.Parse(playerId.String())
	if err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid player ID")
		return
	}

	var req api.SetTemporalMarksJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid request body")
		return
	}

	targetIDs := make([]uuid.UUID, len(req.TargetIds))
	for i, id := range req.TargetIds {
		targetUUID, err := uuid.Parse(id.String())
		if err != nil {
			respondError(w, http.StatusBadRequest, err, "Invalid target ID")
			return
		}
		targetIDs[i] = targetUUID
	}

	if err := h.service.SetTemporalMarks(ctx, playerUUID, targetIDs); err != nil {
		if err.Error() == "too many temporal marks" || err.Error() == "sandevistan not active" {
			respondError(w, http.StatusBadRequest, err, err.Error())
			return
		}
		respondError(w, http.StatusInternalServerError, err, "Failed to set temporal marks")
		return
	}

	status := "marks_set"
	response := api.StatusResponse{
		Status: &status,
	}

	respondJSON(w, http.StatusCreated, response)
}

func (h *SandevistanHandlers) GetTemporalMarks(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID) {
	ctx := r.Context()

	playerUUID, err := uuid.Parse(playerId.String())
	if err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid player ID")
		return
	}

	marks, err := h.service.GetTemporalMarks(ctx, playerUUID)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err, "Failed to get temporal marks")
		return
	}

	response := map[string]interface{}{
		"marks": marks,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *SandevistanHandlers) ApplyCoolingCartridge(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID) {
	ctx := r.Context()

	playerUUID, err := uuid.Parse(playerId.String())
	if err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid player ID")
		return
	}

	var req api.ApplyCoolingCartridgeJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid request body")
		return
	}

	cartridgeUUID, err := uuid.Parse(req.CartridgeId.String())
	if err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid cartridge ID")
		return
	}

	result, err := h.service.ApplyCooling(ctx, playerUUID, cartridgeUUID)
	if err != nil {
		if err.Error() == "no active sandevistan activation" {
			respondError(w, http.StatusNotFound, err, err.Error())
			return
		}
		respondError(w, http.StatusInternalServerError, err, "Failed to apply cooling")
		return
	}

	respondJSON(w, http.StatusOK, result)
}

func (h *SandevistanHandlers) GetHeatStatus(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID) {
	ctx := r.Context()

	playerUUID, err := uuid.Parse(playerId.String())
	if err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid player ID")
		return
	}

	status, err := h.service.GetHeatStatus(ctx, playerUUID)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err, "Failed to get heat status")
		return
	}

	respondJSON(w, http.StatusOK, status)
}

func (h *SandevistanHandlers) ApplyCounterplay(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID) {
	ctx := r.Context()

	playerUUID, err := uuid.Parse(playerId.String())
	if err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid player ID")
		return
	}

	var req api.ApplyCounterplayJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid request body")
		return
	}

	sourcePlayerUUID, err := uuid.Parse(req.SourcePlayerId.String())
	if err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid source player ID")
		return
	}

	result, err := h.service.ApplyCounterplay(ctx, playerUUID, string(req.EffectType), sourcePlayerUUID)
	if err != nil {
		if err.Error() == "sandevistan not active" {
			respondError(w, http.StatusNotFound, err, err.Error())
			return
		}
		respondError(w, http.StatusInternalServerError, err, "Failed to apply counterplay")
		return
	}

	respondJSON(w, http.StatusOK, result)
}

