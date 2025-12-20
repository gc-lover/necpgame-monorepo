// Package server Issue: #2224 - Filter handlers for world events service
// Split from service.go to reduce file size (was 859 lines)
package server

import (
	"context"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/world-events-service-go/models"
	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"go.uber.org/zap"
)

// GetActiveWorldEventsHandler handles GET /api/v1/world-events/active
func (s *WorldEventsService) GetActiveWorldEventsHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	status := models.EventStatusActive
	events, _, err := s.repo.ListWorldEvents(ctx, &status, nil, nil, nil, 100, 0)
	if err != nil {
		s.logger.Error("Failed to get active world events", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to get active world events")
		return
	}

	response := map[string]interface{}{
		"events": events,
		"count":  len(events),
		"status": "active",
	}

	s.respondJSON(w, http.StatusOK, response)
}

// GetPlannedWorldEventsHandler handles GET /api/v1/world-events/planned
func (s *WorldEventsService) GetPlannedWorldEventsHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	status := models.EventStatusPlanned
	events, _, err := s.repo.ListWorldEvents(ctx, &status, nil, nil, nil, 100, 0)
	if err != nil {
		s.logger.Error("Failed to get planned world events", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to get planned world events")
		return
	}

	response := map[string]interface{}{
		"events": events,
		"count":  len(events),
		"status": "planned",
	}

	s.respondJSON(w, http.StatusOK, response)
}

// GetWorldEventsByScaleHandler handles GET /api/v1/world-events/by-scale/{scale}
func (s *WorldEventsService) GetWorldEventsByScaleHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	scaleStr := chi.URLParam(r, "scale")
	scale := models.WorldEventScale(scaleStr)

	// Validate scale
	validScales := []models.WorldEventScale{
		models.EventScaleGlobal, models.EventScaleRegional,
		models.EventScaleCity, models.EventScaleLocal,
	}
	valid := false
	for _, s := range validScales {
		if scale == s {
			valid = true
			break
		}
	}
	if !valid {
		s.respondError(w, http.StatusBadRequest, "Invalid scale parameter")
		return
	}

	events, _, err := s.repo.ListWorldEvents(ctx, nil, nil, &scale, nil, 50, 0)
	if err != nil {
		s.logger.Error("Failed to get world events by scale", zap.Error(err), zap.String("scale", string(scale)))
		s.respondError(w, http.StatusInternalServerError, "Failed to get world events by scale")
		return
	}

	response := map[string]interface{}{
		"events": events,
		"scale":  scale,
		"count":  len(events),
	}

	s.respondJSON(w, http.StatusOK, response)
}

// GetWorldEventsByTypeHandler handles GET /api/v1/world-events/by-type/{type}
func (s *WorldEventsService) GetWorldEventsByTypeHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	typeStr := chi.URLParam(r, "type")
	eventType := models.WorldEventType(typeStr)

	events, _, err := s.repo.ListWorldEvents(ctx, nil, &eventType, nil, nil, 50, 0)
	if err != nil {
		s.logger.Error("Failed to get world events by type", zap.Error(err), zap.String("type", string(eventType)))
		s.respondError(w, http.StatusInternalServerError, "Failed to get world events by type")
		return
	}

	response := map[string]interface{}{
		"events": events,
		"type":   eventType,
		"count":  len(events),
	}

	s.respondJSON(w, http.StatusOK, response)
}

// GetWorldEventsByFrequencyHandler handles GET /api/v1/world-events/by-frequency/{frequency}
func (s *WorldEventsService) GetWorldEventsByFrequencyHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	freqStr := chi.URLParam(r, "frequency")
	frequency := models.WorldEventFrequency(freqStr)

	events, _, err := s.repo.ListWorldEvents(ctx, nil, nil, nil, &frequency, 50, 0)
	if err != nil {
		s.logger.Error("Failed to get world events by frequency", zap.Error(err), zap.String("frequency", string(frequency)))
		s.respondError(w, http.StatusInternalServerError, "Failed to get world events by frequency")
		return
	}

	response := map[string]interface{}{
		"events":    events,
		"frequency": frequency,
		"count":     len(events),
	}

	s.respondJSON(w, http.StatusOK, response)
}

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
