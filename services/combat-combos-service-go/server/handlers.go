// Issue: #158
package server

import (
	"encoding/json"
	"net/http"

	"github.com/gc-lover/necpgame-monorepo/services/combat-combos-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

// Handlers implements api.ServerInterface
type Handlers struct {
	service *Service
}

// NewHandlers creates new handlers with dependency injection
func NewHandlers(service *Service) *Handlers {
	return &Handlers{service: service}
}

// GetComboCatalog returns combo catalog with filtering
func (h *Handlers) GetComboCatalog(w http.ResponseWriter, r *http.Request, params api.GetComboCatalogParams) {
	catalog, err := h.service.GetComboCatalog(r.Context(), params)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, catalog)
}

// GetComboDetails returns detailed combo information
func (h *Handlers) GetComboDetails(w http.ResponseWriter, r *http.Request, comboId openapi_types.UUID) {
	details, err := h.service.GetComboDetails(r.Context(), comboId.String())
	if err != nil {
		if err == ErrNotFound {
			respondError(w, http.StatusNotFound, "Combo not found")
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, details)
}

// ActivateCombo activates combo for character
func (h *Handlers) ActivateCombo(w http.ResponseWriter, r *http.Request) {
	var req api.ActivateComboRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	response, err := h.service.ActivateCombo(r.Context(), &req)
	if err != nil {
		if err == ErrRequirementsNotMet {
			respondError(w, http.StatusBadRequest, "Combo requirements not met")
			return
		}
		if err == ErrNotFound {
			respondError(w, http.StatusNotFound, "Combo not found")
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, response)
}

// ApplySynergy applies synergy to activated combo
func (h *Handlers) ApplySynergy(w http.ResponseWriter, r *http.Request) {
	var req api.ApplySynergyRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	response, err := h.service.ApplySynergy(r.Context(), &req)
	if err != nil {
		if err == ErrNotFound {
			respondError(w, http.StatusNotFound, "Activation or synergy not found")
			return
		}
		if err == ErrSynergyUnavailable {
			respondError(w, http.StatusBadRequest, "Synergy unavailable")
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, response)
}

// GetComboLoadout returns character's combo loadout
func (h *Handlers) GetComboLoadout(w http.ResponseWriter, r *http.Request, params api.GetComboLoadoutParams) {
	loadout, err := h.service.GetComboLoadout(r.Context(), params.CharacterId.String())
	if err != nil {
		if err == ErrNotFound {
			respondError(w, http.StatusNotFound, "Loadout not found")
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, loadout)
}

// UpdateComboLoadout updates character's combo loadout
func (h *Handlers) UpdateComboLoadout(w http.ResponseWriter, r *http.Request) {
	var req api.UpdateLoadoutRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	loadout, err := h.service.UpdateComboLoadout(r.Context(), &req)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, loadout)
}

// SubmitComboScore submits combo scoring results
func (h *Handlers) SubmitComboScore(w http.ResponseWriter, r *http.Request) {
	var req api.SubmitScoreRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	response, err := h.service.SubmitComboScore(r.Context(), &req)
	if err != nil {
		if err == ErrNotFound {
			respondError(w, http.StatusNotFound, "Activation not found")
			return
		}
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, response)
}

// GetComboAnalytics returns combo effectiveness analytics
func (h *Handlers) GetComboAnalytics(w http.ResponseWriter, r *http.Request, params api.GetComboAnalyticsParams) {
	analytics, err := h.service.GetComboAnalytics(r.Context(), params)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	respondJSON(w, http.StatusOK, analytics)
}

// Helper functions

func respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func respondError(w http.ResponseWriter, status int, message string) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(map[string]string{
		"error":   http.StatusText(status),
		"message": message,
	})
}

