// Issue: #142
package server

import (
	"encoding/json"
	"net/http"

	"github.com/gc-lover/necpgame/services/loot-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

type Handlers struct {
	service Service
}

func NewHandlers(service Service) *Handlers {
	return &Handlers{service: service}
}

func (h *Handlers) GenerateLoot(w http.ResponseWriter, r *http.Request) {
	var req api.GenerateLootRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request")
		return
	}
	response, err := h.service.GenerateLoot(r.Context(), &req)
	if err != nil {
		respondError(w, http.StatusBadRequest, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) DistributeLoot(w http.ResponseWriter, r *http.Request) {
	var req api.DistributeLootRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request")
		return
	}
	response, err := h.service.DistributeLoot(r.Context(), &req)
	if err != nil {
		respondError(w, http.StatusBadRequest, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) GetWorldDrops(w http.ResponseWriter, r *http.Request, locationId openapi_types.UUID) {
	response, err := h.service.GetWorldDrops(r.Context(), locationId.String())
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) PickupWorldDrop(w http.ResponseWriter, r *http.Request, dropId openapi_types.UUID) {
	response, err := h.service.PickupWorldDrop(r.Context(), dropId.String())
	if err != nil {
		respondError(w, http.StatusForbidden, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) GetRollStatus(w http.ResponseWriter, r *http.Request, rollId openapi_types.UUID) {
	response, err := h.service.GetRollStatus(r.Context(), rollId.String())
	if err != nil {
		respondError(w, http.StatusNotFound, "Roll not found")
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) RollForItem(w http.ResponseWriter, r *http.Request, rollId openapi_types.UUID) {
	var req api.RollRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request")
		return
	}
	response, err := h.service.RollForItem(r.Context(), rollId.String(), &req)
	if err != nil {
		respondError(w, http.StatusBadRequest, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) PassRoll(w http.ResponseWriter, r *http.Request, rollId openapi_types.UUID) {
	err := h.service.PassRoll(r.Context(), rollId.String())
	if err != nil {
		respondError(w, http.StatusNotFound, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, map[string]string{"status": "passed"})
}

func (h *Handlers) GetPlayerLootHistory(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID, params api.GetPlayerLootHistoryParams) {
	response, err := h.service.GetPlayerLootHistory(r.Context(), playerId.String(), params)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func respondJSON(w http.ResponseWriter, code int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(code)
	json.NewEncoder(w).Encode(data)
}

func respondError(w http.ResponseWriter, code int, message string) {
	respondJSON(w, code, map[string]string{"error": message})
}

