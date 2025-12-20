// Package server Issue: #2224 - Effects handlers for world events service
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/world-events-service-go/models"
	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"go.uber.org/zap"
)

// GetWorldEventEffectsHandler handles GET /api/v1/world-events/{eventID}/effects
func (s *WorldEventsService) GetWorldEventEffectsHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	eventIDStr := chi.URLParam(r, "eventID")
	eventID, err := uuid.Parse(eventIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid event ID")
		return
	}

	effects, err := s.repo.GetWorldEventEffects(ctx, eventID)
	if err != nil {
		s.logger.Error("Failed to get world event effects", zap.Error(err), zap.String("event_id", eventID.String()))
		s.respondError(w, http.StatusInternalServerError, "Failed to get world event effects")
		return
	}

	response := map[string]interface{}{
		"effects":  effects,
		"event_id": eventID,
		"count":    len(effects),
	}

	s.respondJSON(w, http.StatusOK, response)
}

// AddWorldEventEffectHandler handles POST /api/v1/world-events/{eventID}/effects
func (s *WorldEventsService) AddWorldEventEffectHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	eventIDStr := chi.URLParam(r, "eventID")
	eventID, err := uuid.Parse(eventIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid event ID")
		return
	}

	var req models.CreateEventEffectRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// Validate required fields
	if req.TargetSystem == "" || req.EffectType == "" || req.Parameters == nil {
		s.respondError(w, http.StatusBadRequest, "Missing required fields: target_system, effect_type, parameters")
		return
	}

	effect := &models.EventEffect{
		ID:           uuid.New(),
		EventID:      eventID,
		TargetSystem: req.TargetSystem,
		EffectType:   req.EffectType,
		Parameters:   req.Parameters,
		StartTime:    req.StartTime,
		EndTime:      req.EndTime,
		IsActive:     true,
		CreatedAt:    time.Now(),
	}

	if err := s.repo.CreateWorldEventEffect(ctx, effect); err != nil {
		s.logger.Error("Failed to create world event effect", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to create world event effect")
		return
	}

	s.respondJSON(w, http.StatusCreated, effect)
}

// UpdateWorldEventEffectHandler handles PUT /api/v1/world-events/{eventID}/effects/{effectID}
func (s *WorldEventsService) UpdateWorldEventEffectHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	eventIDStr := chi.URLParam(r, "eventID")
	effectIDStr := chi.URLParam(r, "effectID")

	eventID, err := uuid.Parse(eventIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid event ID")
		return
	}

	effectID, err := uuid.Parse(effectIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid effect ID")
		return
	}

	var req models.UpdateEventEffectRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	effect, err := s.repo.GetWorldEventEffect(ctx, effectID)
	if err != nil {
		if err == sql.ErrNoRows {
			s.respondError(w, http.StatusNotFound, "Event effect not found")
			return
		}
		s.logger.Error("Failed to get world event effect", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to update world event effect")
		return
	}

	// Verify effect belongs to the event
	if effect.EventID != eventID {
		s.respondError(w, http.StatusBadRequest, "Effect does not belong to the specified event")
		return
	}

	// Update fields
	if req.Parameters != nil {
		effect.Parameters = req.Parameters
	}
	if req.StartTime.IsZero() == false {
		effect.StartTime = req.StartTime
	}
	if req.EndTime.IsZero() == false {
		effect.EndTime = req.EndTime
	}
	if req.IsActive != nil {
		effect.IsActive = *req.IsActive
	}

	if err := s.repo.UpdateWorldEventEffect(ctx, effect); err != nil {
		s.logger.Error("Failed to update world event effect", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to update world event effect")
		return
	}

	s.respondJSON(w, http.StatusOK, effect)
}

// DeleteWorldEventEffectHandler handles DELETE /api/v1/world-events/{eventID}/effects/{effectID}
func (s *WorldEventsService) DeleteWorldEventEffectHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	eventIDStr := chi.URLParam(r, "eventID")
	effectIDStr := chi.URLParam(r, "effectID")

	eventID, err := uuid.Parse(eventIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid event ID")
		return
	}

	effectID, err := uuid.Parse(effectIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid effect ID")
		return
	}

	effect, err := s.repo.GetWorldEventEffect(ctx, effectID)
	if err != nil {
		if err == sql.ErrNoRows {
			s.respondError(w, http.StatusNotFound, "Event effect not found")
			return
		}
		s.logger.Error("Failed to get world event effect for deletion", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to delete world event effect")
		return
	}

	// Verify effect belongs to the event
	if effect.EventID != eventID {
		s.respondError(w, http.StatusBadRequest, "Effect does not belong to the specified event")
		return
	}

	if err := s.repo.DeleteWorldEventEffect(ctx, effectID); err != nil {
		s.logger.Error("Failed to delete world event effect", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to delete world event effect")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"message": "World event effect deleted successfully"})
}
