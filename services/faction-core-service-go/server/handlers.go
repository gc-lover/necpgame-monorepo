// Issue: #1442, #1604
package server

import (
	"context"
	"encoding/json"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/faction-core-service-go/pkg/api"
)

// Context timeout constants
const (
	DBTimeout = 50 * time.Millisecond
)

type Handlers struct {
	service *Service
}

func NewHandlers(service *Service) *Handlers {
	return &Handlers{service: service}
}

// CreateFaction implements POST /api/v1/factions/create
func (h *Handlers) CreateFaction(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	var req api.CreateFactionRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	faction, err := h.service.CreateFaction(ctx, req)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusCreated, faction)
}

// GetFaction implements GET /api/v1/factions/{factionId}
func (h *Handlers) GetFaction(w http.ResponseWriter, r *http.Request, factionId string) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	faction, err := h.service.GetFaction(ctx, factionId)
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
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	var req api.UpdateFactionRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	faction, err := h.service.UpdateFaction(ctx, factionId, req)
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
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	if err := h.service.DeleteFaction(ctx, factionId); err != nil {
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
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	factions, pagination, err := h.service.ListFactions(ctx, params)
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
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	var req api.UpdateHierarchyRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	hierarchy, err := h.service.UpdateHierarchy(ctx, factionId, req)
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
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()

	hierarchy, err := h.service.GetHierarchy(ctx, factionId)
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








