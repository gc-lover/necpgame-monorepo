// Issue: #1560

package server

import (
	"encoding/json"
	"net/http"

	"github.com/gc-lover/necpgame-monorepo/services/projectile-core-service-go/pkg/api"
)

// ProjectileHandlers implements api.ServerInterface
type ProjectileHandlers struct {
	service *ProjectileService
}

// NewProjectileHandlers creates new handlers
func NewProjectileHandlers(service *ProjectileService) *ProjectileHandlers {
	return &ProjectileHandlers{
		service: service,
	}
}

// GetProjectileForms implements GET /api/v1/projectile/forms
func (h *ProjectileHandlers) GetProjectileForms(w http.ResponseWriter, r *http.Request, params api.GetProjectileFormsParams) {
	forms, err := h.service.GetForms(r.Context(), params)
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to get forms", err)
		return
	}

	respondJSON(w, http.StatusOK, forms)
}

// GetProjectileForm implements GET /api/v1/projectile/forms/{form_id}
func (h *ProjectileHandlers) GetProjectileForm(w http.ResponseWriter, r *http.Request, formId string) {
	form, err := h.service.GetForm(r.Context(), formId)
	if err != nil {
		respondError(w, http.StatusNotFound, "Form not found", err)
		return
	}

	respondJSON(w, http.StatusOK, form)
}

// SpawnProjectile implements POST /api/v1/projectile/spawn
func (h *ProjectileHandlers) SpawnProjectile(w http.ResponseWriter, r *http.Request) {
	var req api.SpawnProjectileRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body", err)
		return
	}

	resp, err := h.service.SpawnProjectile(r.Context(), &req)
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to spawn projectile", err)
		return
	}

	respondJSON(w, http.StatusCreated, resp)
}

// ValidateCompatibility implements POST /api/v1/projectile/validate-compatibility
func (h *ProjectileHandlers) ValidateCompatibility(w http.ResponseWriter, r *http.Request) {
	var req api.ValidateCompatibilityRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body", err)
		return
	}

	resp, err := h.service.ValidateCompatibility(r.Context(), &req)
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to validate", err)
		return
	}

	respondJSON(w, http.StatusOK, resp)
}

// GetCompatibilityMatrix implements GET /api/v1/projectile/compatibility-matrix
func (h *ProjectileHandlers) GetCompatibilityMatrix(w http.ResponseWriter, r *http.Request) {
	matrix, err := h.service.GetCompatibilityMatrix(r.Context())
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to get matrix", err)
		return
	}

	respondJSON(w, http.StatusOK, matrix)
}

// Helper functions

func respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func respondError(w http.ResponseWriter, status int, message string, err error) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(map[string]interface{}{
		"error":   message,
		"details": err.Error(),
	})
}








