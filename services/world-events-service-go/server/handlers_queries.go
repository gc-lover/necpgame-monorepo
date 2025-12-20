// Package server Issue: #2224 - Query handlers for world events service
package server

import (
	"context"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/world-events-service-go/models"
	"github.com/go-chi/chi/v5"
	"go.uber.org/zap"
)

// GetActiveWorldEventsHandler handles GET /api/v1/world-events/active
func (s *WorldEventsService) GetActiveWorldEventsHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	events, err := s.repo.GetActiveWorldEvents(ctx)
	if err != nil {
		s.logger.Error("Failed to get active world events", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to get active world events")
		return
	}

	response := map[string]interface{}{
		"events": events,
		"count":  len(events),
	}

	s.respondJSON(w, http.StatusOK, response)
}

// GetPlannedWorldEventsHandler handles GET /api/v1/world-events/planned
func (s *WorldEventsService) GetPlannedWorldEventsHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	events, err := s.repo.GetPlannedWorldEvents(ctx)
	if err != nil {
		s.logger.Error("Failed to get planned world events", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to get planned world events")
		return
	}

	response := map[string]interface{}{
		"events": events,
		"count":  len(events),
	}

	s.respondJSON(w, http.StatusOK, response)
}

// GetWorldEventsByScaleHandler handles GET /api/v1/world-events/scale/{scale}
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

// GetWorldEventsByTypeHandler handles GET /api/v1/world-events/type/{type}
func (s *WorldEventsService) GetWorldEventsByTypeHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	typeStr := chi.URLParam(r, "type")
	eventType := models.WorldEventType(typeStr)

	// Validate type
	validTypes := []models.WorldEventType{
		models.EventTypeWar, models.EventTypeFestival, models.EventTypeDisaster,
		models.EventTypeQuest, models.EventTypeEconomic, models.EventTypePolitical,
	}
	valid := false
	for _, t := range validTypes {
		if eventType == t {
			valid = true
			break
		}
	}
	if !valid {
		s.respondError(w, http.StatusBadRequest, "Invalid type parameter")
		return
	}

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

// GetWorldEventsByFrequencyHandler handles GET /api/v1/world-events/frequency/{frequency}
func (s *WorldEventsService) GetWorldEventsByFrequencyHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	freqStr := chi.URLParam(r, "frequency")
	frequency := models.WorldEventFrequency(freqStr)

	// Validate frequency
	validFrequencies := []models.WorldEventFrequency{
		models.EventFrequencyOneTime, models.EventFrequencyDaily,
		models.EventFrequencyWeekly, models.EventFrequencyMonthly,
	}
	valid := false
	for _, f := range validFrequencies {
		if frequency == f {
			valid = true
			break
		}
	}
	if !valid {
		s.respondError(w, http.StatusBadRequest, "Invalid frequency parameter")
		return
	}

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
