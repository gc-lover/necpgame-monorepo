// Package server Issue: #2224 - Lifecycle handlers for world events service
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

// AnnounceWorldEventHandler handles POST /api/v1/world-events/{eventID}/announce
func (s *WorldEventsService) AnnounceWorldEventHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	eventIDStr := chi.URLParam(r, "eventID")
	eventID, err := uuid.Parse(eventIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid event ID")
		return
	}

	var req models.CreateEventAnnouncementRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// Validate required fields
	if req.Title == "" {
		s.respondError(w, http.StatusBadRequest, "Title is required")
		return
	}
	if req.Message == "" {
		s.respondError(w, http.StatusBadRequest, "Message is required")
		return
	}
	if req.Type == "" {
		s.respondError(w, http.StatusBadRequest, "Type is required")
		return
	}
	if req.TargetAudience == "" {
		s.respondError(w, http.StatusBadRequest, "Target audience is required")
		return
	}
	if req.Priority == "" {
		s.respondError(w, http.StatusBadRequest, "Priority is required")
		return
	}

	event, err := s.repo.GetWorldEvent(ctx, eventID)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			s.respondError(w, http.StatusNotFound, "World event not found")
			return
		}
		s.logger.Error("Failed to get world event for announcement", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to announce world event")
		return
	}

	if event.Status != models.EventStatusPlanned {
		s.respondError(w, http.StatusConflict, "Can only announce planned events")
		return
	}

	announcement := &models.EventAnnouncement{
		ID:             uuid.New(),
		EventID:        eventID,
		Title:          req.Title,
		Message:        req.Message,
		Type:           req.Type,
		TargetAudience: req.TargetAudience,
		Priority:       req.Priority,
		ExpiresAt:      req.ExpiresAt,
		CreatedAt:      time.Now(),
	}

	if err := s.repo.CreateEventAnnouncement(ctx, announcement); err != nil {
		s.logger.Error("Failed to create event announcement", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to announce world event")
		return
	}

	// Update event status to announced
	event.Status = models.EventStatusAnnounced
	event.UpdatedAt = time.Now()
	if err := s.repo.UpdateWorldEvent(ctx, event); err != nil {
		s.logger.Error("Failed to update event status to announced", zap.Error(err))
		// Don't fail the request, just log the error
	}

	s.respondJSON(w, http.StatusCreated, announcement)
}

// ActivateWorldEventHandler handles POST /api/v1/world-events/{eventID}/activate
func (s *WorldEventsService) ActivateWorldEventHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 15*time.Second)
	defer cancel()

	eventIDStr := chi.URLParam(r, "eventID")
	eventID, err := uuid.Parse(eventIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid event ID")
		return
	}

	event, err := s.repo.GetWorldEvent(ctx, eventID)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			s.respondError(w, http.StatusNotFound, "World event not found")
			return
		}
		s.logger.Error("Failed to get world event for activation", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to activate world event")
		return
	}

	if event.Status != models.EventStatusAnnounced && event.Status != models.EventStatusPlanned {
		s.respondError(w, http.StatusConflict, "Can only activate announced or planned events")
		return
	}

	// Check concurrency limits
	if event.MaxConcurrent != nil {
		activeCount, err := s.repo.CountActiveEventsByType(ctx, event.Type)
		if err != nil {
			s.logger.Error("Failed to check active events count", zap.Error(err))
			s.respondError(w, http.StatusInternalServerError, "Failed to activate world event")
			return
		}
		if activeCount >= *event.MaxConcurrent {
			s.respondError(w, http.StatusConflict, "Maximum concurrent events of this type reached")
			return
		}
	}

	// Activate the event
	event.Status = models.EventStatusActive
	event.StartTime = &time.Now()
	event.UpdatedAt = time.Now()

	if err := s.repo.UpdateWorldEvent(ctx, event); err != nil {
		s.logger.Error("Failed to update event status to active", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to activate world event")
		return
	}

	// Apply event effects
	if s.applyEventEffects(ctx, event); err != nil {
		s.logger.Error("Failed to apply event effects", zap.Error(err))
		// Rollback activation
		event.Status = models.EventStatusAnnounced
		event.StartTime = nil
		s.repo.UpdateWorldEvent(ctx, event)
		s.respondError(w, http.StatusInternalServerError, "Failed to activate world event")
		return
	}

	s.respondJSON(w, http.StatusOK, event)
}

// DeactivateWorldEventHandler handles POST /api/v1/world-events/{eventID}/deactivate
func (s *WorldEventsService) DeactivateWorldEventHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 15*time.Second)
	defer cancel()

	eventIDStr := chi.URLParam(r, "eventID")
	eventID, err := uuid.Parse(eventIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid event ID")
		return
	}

	event, err := s.repo.GetWorldEvent(ctx, eventID)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			s.respondError(w, http.StatusNotFound, "World event not found")
			return
		}
		s.logger.Error("Failed to get world event for deactivation", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to deactivate world event")
		return
	}

	if event.Status != models.EventStatusActive {
		s.respondError(w, http.StatusConflict, "Can only deactivate active events")
		return
	}

	// Rollback event effects
	if err := s.rollbackEventEffects(event); err != nil {
		s.logger.Error("Failed to rollback event effects", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to deactivate world event")
		return
	}

	// Deactivate the event
	event.Status = models.EventStatusCooldown
	event.EndTime = &time.Now()
	event.UpdatedAt = time.Now()

	if err := s.repo.UpdateWorldEvent(ctx, event); err != nil {
		s.logger.Error("Failed to update event status to cooldown", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to deactivate world event")
		return
	}

	s.respondJSON(w, http.StatusOK, event)
}

// CreateEventAnnouncementHandler handles POST /api/v1/world-events/{eventID}/announcements
func (s *WorldEventsService) CreateEventAnnouncementHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	eventIDStr := chi.URLParam(r, "eventID")
	eventID, err := uuid.Parse(eventIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid event ID")
		return
	}

	var req models.CreateAnnouncementRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// Validate required fields
	if req.Title == "" || req.Message == "" || req.Type == "" || req.Priority == "" {
		s.respondError(w, http.StatusBadRequest, "Missing required fields: title, message, type, priority")
		return
	}

	announcement := &models.EventAnnouncement{
		ID:             uuid.New(),
		EventID:        eventID,
		Title:          req.Title,
		Message:        req.Message,
		Type:           req.Type,
		TargetAudience: req.TargetAudience,
		Priority:       req.Priority,
		ExpiresAt:      req.ExpiresAt,
		CreatedAt:      time.Now(),
	}

	if err := s.repo.CreateEventAnnouncement(ctx, announcement); err != nil {
		s.logger.Error("Failed to create event announcement", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to create event announcement")
		return
	}

	s.respondJSON(w, http.StatusCreated, announcement)
}
