// Issue: #1442
package server

import (
	"encoding/json"
	"net/http"

	"github.com/gc-lover/necpgame-monorepo/services/faction-core-service-go/pkg/api"
)

type Handlers struct {
	service *Service
}

func NewHandlers(service *Service) *Handlers {
	return &Handlers{service: service}
}

// CreateFaction implements POST /api/v1/factions/create
func (h *Handlers) CreateFaction(w http.ResponseWriter, r *http.Request) {
	var req api.CreateFactionRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	faction, err := h.service.CreateFaction(r.Context(), req)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusCreated, faction)
}

// GetFaction implements GET /api/v1/factions/{factionId}
func (h *Handlers) GetFaction(w http.ResponseWriter, r *http.Request, factionId string) {
	faction, err := h.service.GetFaction(r.Context(), factionId)
	if err != nil {
		if err == ErrNotFound {
			respondError(w, http.StatusNotFound, "Faction not found")
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, faction)
}

// UpdateFaction implements PUT /api/v1/factions/{factionId}
func (h *Handlers) UpdateFaction(w http.ResponseWriter, r *http.Request, factionId string) {
	var req api.UpdateFactionRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	faction, err := h.service.UpdateFaction(r.Context(), factionId, req)
	if err != nil {
		if err == ErrNotFound {
			respondError(w, http.StatusNotFound, "Faction not found")
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, faction)
}

// DeleteFaction implements DELETE /api/v1/factions/{factionId}
func (h *Handlers) DeleteFaction(w http.ResponseWriter, r *http.Request, factionId string) {
	if err := h.service.DeleteFaction(r.Context(), factionId); err != nil {
		if err == ErrNotFound {
			respondError(w, http.StatusNotFound, "Faction not found")
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	w.WriteHeader(http.StatusNoContent)
}

// ListFactions implements GET /api/v1/factions/list
func (h *Handlers) ListFactions(w http.ResponseWriter, r *http.Request, params api.ListFactionsParams) {
	factions, pagination, err := h.service.ListFactions(r.Context(), params)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	response := map[string]interface{}{
		"factions":   factions,
		"pagination": pagination,
	}

	respondJSON(w, http.StatusOK, response)
}

// UpdateHierarchy implements POST /api/v1/factions/{factionId}/hierarchy/update
func (h *Handlers) UpdateHierarchy(w http.ResponseWriter, r *http.Request, factionId string) {
	var req api.UpdateHierarchyRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	hierarchy, err := h.service.UpdateHierarchy(r.Context(), factionId, req)
	if err != nil {
		if err == ErrNotFound {
			respondError(w, http.StatusNotFound, "Faction not found")
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, hierarchy)
}

// GetHierarchy implements GET /api/v1/factions/{factionId}/hierarchy
func (h *Handlers) GetHierarchy(w http.ResponseWriter, r *http.Request, factionId string) {
	hierarchy, err := h.service.GetHierarchy(r.Context(), factionId)
	if err != nil {
		if err == ErrNotFound {
			respondError(w, http.StatusNotFound, "Faction not found")
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, hierarchy)
}

// Helper functions
func respondJSON(w http.ResponseWriter, code int, payload interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(code)
	json.NewEncoder(w).Encode(payload)
}

func respondError(w http.ResponseWriter, code int, message string) {
	respondJSON(w, code, map[string]string{"error": message})
}



