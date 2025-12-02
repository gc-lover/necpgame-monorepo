// Issue: #1574
package server

import (
	"encoding/json"
	"net/http"

	"github.com/gc-lover/necpgame-monorepo/services/weapon-resource-service-go/pkg/api"
)

// Handlers implements api.ServerInterface
type Handlers struct {
	service *Service
}

// NewHandlers creates handlers with DI
func NewHandlers(service *Service) *Handlers {
	return &Handlers{service: service}
}

// GetWeaponResources implements GET /api/v1/weapons/{weapon_id}/resources
func (h *Handlers) GetWeaponResources(w http.ResponseWriter, r *http.Request, weaponId string) {
	resources, err := h.service.GetWeaponResources(r.Context(), weaponId)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, resources)
}

// UpdateAmmo implements PUT /api/v1/weapons/{weapon_id}/ammo
func (h *Handlers) UpdateAmmo(w http.ResponseWriter, r *http.Request, weaponId string) {
	var req api.UpdateAmmoJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	result, err := h.service.UpdateAmmo(r.Context(), weaponId, req)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, result)
}

// UpdateHeat implements PUT /api/v1/weapons/{weapon_id}/heat
func (h *Handlers) UpdateHeat(w http.ResponseWriter, r *http.Request, weaponId string) {
	var req api.UpdateHeatJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	result, err := h.service.UpdateHeat(r.Context(), weaponId, req)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, result)
}

// UpdateEnergy implements PUT /api/v1/weapons/{weapon_id}/energy
func (h *Handlers) UpdateEnergy(w http.ResponseWriter, r *http.Request, weaponId string) {
	var req api.UpdateEnergyJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	result, err := h.service.UpdateEnergy(r.Context(), weaponId, req)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, result)
}

// UpdateCooldown implements PUT /api/v1/weapons/{weapon_id}/cooldown
func (h *Handlers) UpdateCooldown(w http.ResponseWriter, r *http.Request, weaponId string) {
	var req api.UpdateCooldownJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	result, err := h.service.UpdateCooldown(r.Context(), weaponId, req)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, result)
}

// Helper functions
func respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func respondError(w http.ResponseWriter, status int, message string) {
	respondJSON(w, status, api.Error{
		Code:    int32(status),
		Message: message,
	})
}

