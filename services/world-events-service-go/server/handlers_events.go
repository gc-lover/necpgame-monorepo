// Package server Issue: #2224 - Event CRUD handlers for world events service
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/world-events-service-go/models"
	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"go.uber.org/zap"
)

// ListWorldEventsHandler handles GET /api/v1/world-events
func (s *WorldEventsService) ListWorldEventsHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	// Parse and validate query parameters
	limit := 20
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err != nil {
			s.respondError(w, http.StatusBadRequest, "Invalid limit parameter")
			return
		} else if l <= 0 || l > 100 {
			s.respondError(w, http.StatusBadRequest, "Limit must be between 1 and 100")
			return
		}
		limit = l
	}

	offset := 0
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err != nil {
			s.respondError(w, http.StatusBadRequest, "Invalid offset parameter")
			return
		} else if o < 0 {
			s.respondError(w, http.StatusBadRequest, "Offset must be non-negative")
			return
		}
		offset = o
	}

	var status *models.WorldEventStatus
	if statusStr := r.URL.Query().Get("status"); statusStr != "" {
		s := models.WorldEventStatus(statusStr)
		status = &s
	}

	var eventType *models.WorldEventType
	if typeStr := r.URL.Query().Get("type"); typeStr != "" {
		t := models.WorldEventType(typeStr)
		eventType = &t
	}

	var scale *models.WorldEventScale
	if scaleStr := r.URL.Query().Get("scale"); scaleStr != "" {
		sc := models.WorldEventScale(scaleStr)
		scale = &sc
	}

	var frequency *models.WorldEventFrequency
	if freqStr := r.URL.Query().Get("frequency"); freqStr != "" {
		f := models.WorldEventFrequency(freqStr)
		frequency = &f
	}

	events, total, err := s.repo.ListWorldEvents(ctx, status, eventType, scale, frequency, limit, offset)
	if err != nil {
		s.logger.Error("Failed to list world events", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to list world events")
		return
	}

	response := map[string]interface{}{
		"events": events,
		"total":  total,
		"limit":  limit,
		"offset": offset,
	}

	s.respondJSON(w, http.StatusOK, response)
}

// CreateWorldEventHandler handles POST /api/v1/world-events
func (s *WorldEventsService) CreateWorldEventHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	var req models.CreateWorldEventRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// Validate required fields
	if req.Title == "" || req.Type == "" || req.Scale == "" || req.Frequency == "" {
		s.respondError(w, http.StatusBadRequest, "Missing required fields: title, type, scale, frequency")
		return
	}

	event := &models.WorldEvent{
		ID:               uuid.New(),
		Title:            req.Title,
		Description:      req.Description,
		Type:             req.Type,
		Scale:            req.Scale,
		Frequency:        req.Frequency,
		Status:           models.EventStatusPlanned,
		StartTime:        req.StartTime,
		Duration:         req.Duration,
		TargetRegions:    req.TargetRegions,
		TargetFactions:   req.TargetFactions,
		Prerequisites:    req.Prerequisites,
		CooldownDuration: req.CooldownDuration,
		MaxConcurrent:    req.MaxConcurrent,
		CreatedAt:        time.Now(),
		UpdatedAt:        time.Now(),
		Version:          1,
	}

	if err := s.repo.CreateWorldEvent(ctx, event); err != nil {
		s.logger.Error("Failed to create world event", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to create world event")
		return
	}

	s.respondJSON(w, http.StatusCreated, event)
}

// GetWorldEventHandler handles GET /api/v1/world-events/{eventID}
func (s *WorldEventsService) GetWorldEventHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	eventIDStr := chi.URLParam(r, "eventID")
	eventID, err := uuid.Parse(eventIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid event ID")
		return
	}

	event, err := s.repo.GetWorldEvent(ctx, eventID)
	if err != nil {
		if err == sql.ErrNoRows {
			s.respondError(w, http.StatusNotFound, "World event not found")
			return
		}
		s.logger.Error("Failed to get world event", zap.Error(err), zap.String("event_id", eventID.String()))
		s.respondError(w, http.StatusInternalServerError, "Failed to get world event")
		return
	}

	s.respondJSON(w, http.StatusOK, event)
}

// UpdateWorldEventHandler handles PUT /api/v1/world-events/{eventID}
func (s *WorldEventsService) UpdateWorldEventHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	eventIDStr := chi.URLParam(r, "eventID")
	eventID, err := uuid.Parse(eventIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid event ID")
		return
	}

	var req models.UpdateWorldEventRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	event, err := s.repo.GetWorldEvent(ctx, eventID)
	if err != nil {
		if err == sql.ErrNoRows {
			s.respondError(w, http.StatusNotFound, "World event not found")
			return
		}
		s.logger.Error("Failed to get world event for update", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to update world event")
		return
	}

	// Update fields
	if req.Title != "" {
		event.Title = req.Title
	}
	if req.Description != "" {
		event.Description = req.Description
	}
	if req.StartTime != nil {
		event.StartTime = req.StartTime
	}
	if req.Duration != nil {
		event.Duration = req.Duration
	}
	if req.TargetRegions != nil {
		event.TargetRegions = req.TargetRegions
	}
	if req.CooldownDuration != nil {
		event.CooldownDuration = req.CooldownDuration
	}
	if req.MaxConcurrent != nil {
		event.MaxConcurrent = req.MaxConcurrent
	}

	event.UpdatedAt = time.Now()
	event.Version++

	if err := s.repo.UpdateWorldEvent(ctx, event); err != nil {
		s.logger.Error("Failed to update world event", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to update world event")
		return
	}

	s.respondJSON(w, http.StatusOK, event)
}

// DeleteWorldEventHandler handles DELETE /api/v1/world-events/{eventID}
func (s *WorldEventsService) DeleteWorldEventHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	eventIDStr := chi.URLParam(r, "eventID")
	eventID, err := uuid.Parse(eventIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid event ID")
		return
	}

	event, err := s.repo.GetWorldEvent(ctx, eventID)
	if err != nil {
		if err == sql.ErrNoRows {
			s.respondError(w, http.StatusNotFound, "World event not found")
			return
		}
		s.logger.Error("Failed to get world event for deletion", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to delete world event")
		return
	}

	if event.Status == models.EventStatusActive {
		s.respondError(w, http.StatusConflict, "Cannot delete active world event")
		return
	}

	if err := s.repo.DeleteWorldEvent(ctx, eventID); err != nil {
		s.logger.Error("Failed to delete world event", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to delete world event")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"message": "World event deleted successfully"})
}
