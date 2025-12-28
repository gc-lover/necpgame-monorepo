// Issue: #1495 - Gameplay Affixes Service implementation
// PERFORMANCE: Affix handlers with optimized request processing and error handling

package handlers

import (
	"encoding/json"
	"net/http"
	"strconv"
	"strings"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"go.uber.org/zap"

	"gameplay-affixes-service-go/internal/models"
	"gameplay-affixes-service-go/internal/service"
)

// AffixHandlers handles affix-related HTTP requests
type AffixHandlers struct {
	affixService service.AffixService
	logger       *zap.Logger
}

// NewAffixHandlers creates new affix handlers instance
func NewAffixHandlers(affixService service.AffixService, logger *zap.Logger) *AffixHandlers {
	return &AffixHandlers{
		affixService: affixService,
		logger:       logger,
	}
}

// GetActiveAffixes handles GET /affixes/active
// Returns the currently active affixes for this week
func (h *AffixHandlers) GetActiveAffixes(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	activeAffixes, err := h.affixService.GetActiveAffixes(ctx)
	if err != nil {
		h.logger.Error("Failed to get active affixes", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get active affixes")
		return
	}

	h.respondJSON(w, http.StatusOK, activeAffixes)
}

// GetAffix handles GET /affixes/{id}
// Returns detailed information about a specific affix
func (h *AffixHandlers) GetAffix(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	vars := mux.Vars(r)

	affixID, err := uuid.Parse(vars["id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid affix ID")
		return
	}

	affix, err := h.affixService.GetAffix(ctx, affixID)
	if err != nil {
		h.logger.Error("Failed to get affix", zap.Error(err), zap.String("affix_id", affixID.String()))
		h.respondError(w, http.StatusNotFound, "Affix not found")
		return
	}

	h.respondJSON(w, http.StatusOK, affix)
}

// GetInstanceAffixes handles GET /instances/{instance_id}/affixes
// Returns affixes applied to a specific instance
func (h *AffixHandlers) GetInstanceAffixes(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	vars := mux.Vars(r)

	instanceID, err := uuid.Parse(vars["instance_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid instance ID")
		return
	}

	instanceAffixes, err := h.affixService.GetInstanceAffixes(ctx, instanceID)
	if err != nil {
		h.logger.Error("Failed to get instance affixes", zap.Error(err), zap.String("instance_id", instanceID.String()))
		h.respondError(w, http.StatusNotFound, "Instance affixes not found")
		return
	}

	h.respondJSON(w, http.StatusOK, instanceAffixes)
}

// GenerateInstanceAffixes handles POST /instances/{instance_id}/affixes/generate
// Generates and applies random affixes to an instance
func (h *AffixHandlers) GenerateInstanceAffixes(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	vars := mux.Vars(r)

	instanceID, err := uuid.Parse(vars["instance_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid instance ID")
		return
	}

	instanceAffixes, err := h.affixService.GenerateInstanceAffixes(ctx, instanceID)
	if err != nil {
		h.logger.Error("Failed to generate instance affixes", zap.Error(err), zap.String("instance_id", instanceID.String()))
		h.respondError(w, http.StatusInternalServerError, "Failed to generate instance affixes")
		return
	}

	h.respondJSON(w, http.StatusCreated, instanceAffixes)
}

// GetAffixRotationHistory handles GET /affixes/rotation/history
// Returns the history of affix rotations
func (h *AffixHandlers) GetAffixRotationHistory(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	// Parse query parameters
	weeksBackStr := r.URL.Query().Get("weeks_back")
	limitStr := r.URL.Query().Get("limit")
	offsetStr := r.URL.Query().Get("offset")

	weeksBack := 4 // default
	if weeksBackStr != "" {
		if parsed, err := strconv.Atoi(weeksBackStr); err == nil && parsed > 0 && parsed <= 52 {
			weeksBack = parsed
		}
	}

	limit := 20 // default
	if limitStr != "" {
		if parsed, err := strconv.Atoi(limitStr); err == nil && parsed > 0 && parsed <= 100 {
			limit = parsed
		}
	}

	offset := 0 // default
	if offsetStr != "" {
		if parsed, err := strconv.Atoi(offsetStr); err == nil && parsed >= 0 {
			offset = parsed
		}
	}

	history, err := h.affixService.GetAffixRotationHistory(ctx, weeksBack, limit, offset)
	if err != nil {
		h.logger.Error("Failed to get affix rotation history", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get affix rotation history")
		return
	}

	response := map[string]interface{}{
		"items":  history,
		"total":  len(history),
		"limit":  limit,
		"offset": offset,
	}

	h.respondJSON(w, http.StatusOK, response)
}

// TriggerAffixRotation handles POST /affixes/rotation/trigger
// Manually triggers affix rotation (admin only)
func (h *AffixHandlers) TriggerAffixRotation(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	var request models.TriggerRotationRequest
	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// Validate custom affixes count
	if len(request.CustomAffixes) > 0 {
		if len(request.CustomAffixes) < 8 || len(request.CustomAffixes) > 10 {
			h.respondError(w, http.StatusBadRequest, "Custom affixes must be between 8 and 10")
			return
		}
	}

	err := h.affixService.RotateAffixes(ctx, request.CustomAffixes, request.Force)
	if err != nil {
		h.logger.Error("Failed to trigger affix rotation", zap.Error(err))
		if strings.Contains(err.Error(), "rotation already exists") {
			h.respondError(w, http.StatusConflict, err.Error())
			return
		}
		h.respondError(w, http.StatusInternalServerError, "Failed to trigger affix rotation")
		return
	}

	response := map[string]string{
		"message": "Affix rotation completed successfully",
	}

	h.respondJSON(w, http.StatusOK, response)
}

// ListAffixes handles GET /affixes
// Returns a paginated list of all affixes
func (h *AffixHandlers) ListAffixes(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	// Parse query parameters
	limitStr := r.URL.Query().Get("limit")
	offsetStr := r.URL.Query().Get("offset")

	limit := 20 // default
	if limitStr != "" {
		if parsed, err := strconv.Atoi(limitStr); err == nil && parsed > 0 && parsed <= 100 {
			limit = parsed
		}
	}

	offset := 0 // default
	if offsetStr != "" {
		if parsed, err := strconv.Atoi(offsetStr); err == nil && parsed >= 0 {
			offset = parsed
		}
	}

	affixes, err := h.affixService.ListAffixes(ctx, limit, offset)
	if err != nil {
		h.logger.Error("Failed to list affixes", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to list affixes")
		return
	}

	response := map[string]interface{}{
		"items":  affixes,
		"total":  len(affixes),
		"limit":  limit,
		"offset": offset,
	}

	h.respondJSON(w, http.StatusOK, response)
}

// CreateAffix handles POST /affixes
// Creates a new affix (admin only)
func (h *AffixHandlers) CreateAffix(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	var affix models.Affix
	if err := json.NewDecoder(r.Body).Decode(&affix); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	if err := h.affixService.CreateAffix(ctx, &affix); err != nil {
		h.logger.Error("Failed to create affix", zap.Error(err))
		h.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	h.respondJSON(w, http.StatusCreated, affix)
}

// UpdateAffix handles PUT /affixes/{id}
// Updates an existing affix (admin only)
func (h *AffixHandlers) UpdateAffix(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	vars := mux.Vars(r)

	affixID, err := uuid.Parse(vars["id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid affix ID")
		return
	}

	var affix models.Affix
	if err := json.NewDecoder(r.Body).Decode(&affix); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	affix.ID = affixID

	if err := h.affixService.UpdateAffix(ctx, &affix); err != nil {
		h.logger.Error("Failed to update affix", zap.Error(err), zap.String("affix_id", affixID.String()))
		h.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	h.respondJSON(w, http.StatusOK, affix)
}

// DeleteAffix handles DELETE /affixes/{id}
// Deletes an affix (admin only)
func (h *AffixHandlers) DeleteAffix(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	vars := mux.Vars(r)

	affixID, err := uuid.Parse(vars["id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid affix ID")
		return
	}

	if err := h.affixService.DeleteAffix(ctx, affixID); err != nil {
		h.logger.Error("Failed to delete affix", zap.Error(err), zap.String("affix_id", affixID.String()))
		h.respondError(w, http.StatusInternalServerError, "Failed to delete affix")
		return
	}

	w.WriteHeader(http.StatusNoContent)
}

// respondJSON sends a JSON response
func (h *AffixHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)

	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.Error("Failed to encode JSON response", zap.Error(err))
	}
}

// respondError sends an error response
func (h *AffixHandlers) respondError(w http.ResponseWriter, status int, message string) {
	response := map[string]string{
		"error":   http.StatusText(status),
		"message": message,
	}

	h.respondJSON(w, status, response)
}
