package handlers

import (
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"go.uber.org/zap"
)

// CombatAbilitiesHandler handles HTTP requests for combat abilities
type CombatAbilitiesHandler struct {
	service *Service
	logger  *zap.Logger
}

// NewCombatAbilitiesHandler creates a new handler instance
func NewCombatAbilitiesHandler(service *Service, logger *zap.Logger) *CombatAbilitiesHandler {
	return &CombatAbilitiesHandler{
		service: service,
		logger:  logger,
	}
}

// HealthCheck handles service health check
func (h *CombatAbilitiesHandler) HealthCheck(w http.ResponseWriter, r *http.Request) {
	h.respondJSON(w, http.StatusOK, map[string]interface{}{
		"status":    "healthy",
		"service":   "combat-abilities-service-go",
		"timestamp": time.Now(),
		"version":   "1.0.0",
	})
}

// ListAbilities handles GET /combat/abilities - list available abilities
func (h *CombatAbilitiesHandler) ListAbilities(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := contextWithTimeout(r.Context(), 50*time.Millisecond)
	defer cancel()

	// Parse query parameters
	characterIDStr := r.URL.Query().Get("character_id")
	if characterIDStr == "" {
		h.respondError(w, http.StatusBadRequest, "character_id is required")
		return
	}

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid character_id format")
		return
	}

	limitStr := r.URL.Query().Get("limit")
	limit := 20 // default
	if limitStr != "" {
		if parsedLimit, err := strconv.Atoi(limitStr); err == nil && parsedLimit > 0 && parsedLimit <= 100 {
			limit = parsedLimit
		}
	}

	offsetStr := r.URL.Query().Get("offset")
	offset := 0 // default
	if offsetStr != "" {
		if parsedOffset, err := strconv.Atoi(offsetStr); err == nil && parsedOffset >= 0 {
			offset = parsedOffset
		}
	}

	// Get abilities
	abilities, err := h.service.GetCharacterAbilities(ctx, characterID)
	if err != nil {
		h.logger.Error("Failed to get character abilities", zap.Error(err), zap.String("character_id", characterID.String()))
		h.respondError(w, http.StatusInternalServerError, "failed to retrieve abilities")
		return
	}

	// Apply pagination
	start := offset
	end := start + limit
	if start > len(abilities) {
		start = len(abilities)
	}
	if end > len(abilities) {
		end = len(abilities)
	}
	paginatedAbilities := abilities[start:end]

	response := map[string]interface{}{
		"abilities": paginatedAbilities,
		"pagination": map[string]interface{}{
			"offset":   offset,
			"limit":    limit,
			"has_more": end < len(abilities),
		},
		"total_count": len(abilities),
	}

	h.respondJSON(w, http.StatusOK, response)
}

// ActivateAbility handles POST /combat/abilities - activate an ability
func (h *CombatAbilitiesHandler) ActivateAbility(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := contextWithTimeout(r.Context(), 25*time.Millisecond)
	defer cancel()

	var req ActivateAbilityRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	// Basic validation
	if req.AbilityID == uuid.Nil || req.CharacterID == uuid.Nil {
		h.respondError(w, http.StatusBadRequest, "ability_id and character_id are required")
		return
	}

	// Activate ability
	response, err := h.service.ActivateAbility(ctx, &req)
	if err != nil {
		h.logger.Error("Failed to activate ability", zap.Error(err),
			zap.String("ability_id", req.AbilityID.String()),
			zap.String("character_id", req.CharacterID.String()))
		h.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	h.logger.Info("Ability activated successfully",
		zap.String("activation_id", response.ActivationID.String()),
		zap.String("ability_id", response.AbilityID.String()),
		zap.String("character_id", response.CharacterID.String()))

	h.respondJSON(w, http.StatusOK, response)
}

// GetAbilityCooldown handles GET /combat/abilities/{ability_id}/cooldown
func (h *CombatAbilitiesHandler) GetAbilityCooldown(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := contextWithTimeout(r.Context(), 25*time.Millisecond)
	defer cancel()

	abilityIDStr := chi.URLParam(r, "ability_id")
	abilityID, err := uuid.Parse(abilityIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid ability_id format")
		return
	}

	characterIDStr := r.URL.Query().Get("character_id")
	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid character_id format")
		return
	}

	response, err := h.service.GetAbilityCooldown(ctx, characterID, abilityID)
	if err != nil {
		h.logger.Error("Failed to get ability cooldown", zap.Error(err),
			zap.String("ability_id", abilityID.String()),
			zap.String("character_id", characterID.String()))
		h.respondError(w, http.StatusInternalServerError, "failed to retrieve cooldown")
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

// GetAbilitySynergies handles GET /combat/abilities/{ability_id}/synergies
func (h *CombatAbilitiesHandler) GetAbilitySynergies(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := contextWithTimeout(r.Context(), 25*time.Millisecond)
	defer cancel()

	abilityIDStr := chi.URLParam(r, "ability_id")
	abilityID, err := uuid.Parse(abilityIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid ability_id format")
		return
	}

	response, err := h.service.GetAbilitySynergies(ctx, abilityID)
	if err != nil {
		h.logger.Error("Failed to get ability synergies", zap.Error(err),
			zap.String("ability_id", abilityID.String()))
		h.respondError(w, http.StatusInternalServerError, "failed to retrieve synergies")
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

// ValidateAbilityActivation handles POST /combat/abilities/validate
func (h *CombatAbilitiesHandler) ValidateAbilityActivation(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := contextWithTimeout(r.Context(), 25*time.Millisecond)
	defer cancel()

	var req ValidateAbilityRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	// Basic validation
	if req.AbilityID == uuid.Nil || req.CharacterID == uuid.Nil {
		h.respondError(w, http.StatusBadRequest, "ability_id and character_id are required")
		return
	}

	response, err := h.service.ValidateAbilityActivation(ctx, &req)
	if err != nil {
		h.logger.Error("Failed to validate ability activation", zap.Error(err),
			zap.String("ability_id", req.AbilityID.String()),
			zap.String("character_id", req.CharacterID.String()))
		h.respondError(w, http.StatusInternalServerError, "validation failed")
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

// Helper methods

func (h *CombatAbilitiesHandler) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *CombatAbilitiesHandler) respondError(w http.ResponseWriter, status int, message string) {
	h.respondJSON(w, status, map[string]interface{}{
		"error": map[string]interface{}{
			"code":    http.StatusText(status),
			"message": message,
		},
	})
}

func contextWithTimeout(ctx context.Context, timeout time.Duration) (context.Context, context.CancelFunc) {
	return context.WithTimeout(ctx, timeout)
}
