// Issue: #1574
package server

import (
	"encoding/json"
	"net/http"

	"github.com/gc-lover/necpgame-monorepo/services/weapon-progression-service-go/pkg/api"
)

// Handlers implements api.ServerInterface
type Handlers struct {
	service *Service
}

// NewHandlers creates handlers with DI
func NewHandlers(service *Service) *Handlers {
	return &Handlers{service: service}
}

// GetWeaponUpgrades implements GET /api/v1/weapons/{weapon_id}/upgrades
func (h *Handlers) GetWeaponUpgrades(w http.ResponseWriter, r *http.Request, weaponId string, params api.GetWeaponUpgradesParams) {
	upgrades, err := h.service.GetWeaponUpgrades(r.Context(), weaponId, params)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, upgrades)
}

// ApplyUpgrade implements POST /api/v1/weapons/{weapon_id}/upgrades
func (h *Handlers) ApplyUpgrade(w http.ResponseWriter, r *http.Request, weaponId string) {
	var req api.ApplyUpgradeJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	result, err := h.service.ApplyUpgrade(r.Context(), weaponId, req)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, result)
}

// GetWeaponPerks implements GET /api/v1/weapons/{weapon_id}/perks
func (h *Handlers) GetWeaponPerks(w http.ResponseWriter, r *http.Request, weaponId string, params api.GetWeaponPerksParams) {
	perks, err := h.service.GetWeaponPerks(r.Context(), weaponId, params)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, perks)
}

// UnlockPerk implements POST /api/v1/weapons/{weapon_id}/perks
func (h *Handlers) UnlockPerk(w http.ResponseWriter, r *http.Request, weaponId string) {
	var req api.UnlockPerkJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	result, err := h.service.UnlockPerk(r.Context(), weaponId, req)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, result)
}

// GetWeaponMastery implements GET /api/v1/weapons/{weapon_id}/mastery
func (h *Handlers) GetWeaponMastery(w http.ResponseWriter, r *http.Request, weaponId string) {
	mastery, err := h.service.GetWeaponMastery(r.Context(), weaponId)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, mastery)
}

// UpdateMastery implements PUT /api/v1/weapons/{weapon_id}/mastery
func (h *Handlers) UpdateMastery(w http.ResponseWriter, r *http.Request, weaponId string) {
	var req api.UpdateMasteryJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	result, err := h.service.UpdateMastery(r.Context(), weaponId, req)
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

