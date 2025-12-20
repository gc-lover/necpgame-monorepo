// Package server Issue: #2224
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

// WorldEventsService handles business logic for world events
type WorldEventsService struct {
	db     *sql.DB
	logger *zap.Logger
	repo   *WorldEventsRepository
}

// NewWorldEventsService creates a new world events service
func NewWorldEventsService(db *sql.DB, logger *zap.Logger) *WorldEventsService {
	return &WorldEventsService{
		db:     db,
		logger: logger,
		repo:   NewWorldEventsRepository(db, logger),
	}
}

// applyEventEffects applies effects of an active world event
func (s *WorldEventsService) applyEventEffects(ctx context.Context, event *models.WorldEvent) {
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
		if errors.Is(err, sql.ErrNoRows) {
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
		if errors.Is(err, sql.ErrNoRows) {
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
		if errors.Is(err, sql.ErrNoRows) {
			s.respondError(w, http.StatusNotFound, "World event not found")
			return
		}
		s.logger.Error("Failed to get world event for deletion", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to delete world event")
		return
	}

	// Only allow deletion of PLANNED events
	if event.Status != models.EventStatusPlanned {
		s.respondError(w, http.StatusBadRequest, "Can only delete events with PLANNED status")
		return
	}

	if err := s.repo.DeleteWorldEvent(ctx, eventID); err != nil {
		s.logger.Error("Failed to delete world event", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to delete world event")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"message": "World event deleted successfully"})
}

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

	// Check if event can be announced
	if event.Status != models.EventStatusPlanned {
		s.respondError(w, http.StatusBadRequest, "Event must be in PLANNED status to be announced")
		return
	}

	// Update status to ANNOUNCED
	event.Status = models.EventStatusAnnounced
	event.UpdatedAt = time.Now()

	if err := s.repo.UpdateWorldEvent(ctx, event); err != nil {
		s.logger.Error("Failed to update world event status to announced", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to announce world event")
		return
	}

	s.respondJSON(w, http.StatusOK, event)
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

	// Check if event can be activated
	if event.Status != models.EventStatusAnnounced && event.Status != models.EventStatusPlanned {
		s.respondError(w, http.StatusBadRequest, "Event must be in PLANNED or ANNOUNCED status to be activated")
		return
	}

	// Set start time if not set
	if event.StartTime == nil {
		now := time.Now()
		event.StartTime = &now
	}

	// Calculate end time if duration is set
	if event.Duration != nil {
		endTime := event.StartTime.Add(time.Duration(*event.Duration) * time.Second)
		event.EndTime = &endTime
	}

	// Update status to ACTIVE
	event.Status = models.EventStatusActive
	event.UpdatedAt = time.Now()

	// Apply effects if any exist
	if len(event.Effects) > 0 {
		if s.applyEventEffects(ctx, event); err != nil {
			s.logger.Error("Failed to apply event effects", zap.Error(err))
			// Continue with activation even if effects fail
		}
	}

	if err := s.repo.UpdateWorldEvent(ctx, event); err != nil {
		s.logger.Error("Failed to update world event status to active", zap.Error(err))
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

	// Check if event can be deactivated
	if event.Status != models.EventStatusActive {
		s.respondError(w, http.StatusBadRequest, "Event must be in ACTIVE status to be deactivated")
		return
	}

	// Rollback effects
	if len(event.Effects) > 0 {
		if err := s.rollbackEventEffects(event); err != nil {
			s.logger.Error("Failed to rollback event effects", zap.Error(err))
			// Continue with deactivation even if rollback fails
		}
	}

	// Update status to COOLDOWN
	event.Status = models.EventStatusCooldown
	event.UpdatedAt = time.Now()

	if err := s.repo.UpdateWorldEvent(ctx, event); err != nil {
		s.logger.Error("Failed to update world event status to cooldown", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to deactivate world event")
		return
	}

	s.respondJSON(w, http.StatusOK, event)
}

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
	}

	s.respondJSON(w, http.StatusOK, response)
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

// applyEventEffects applies effects of an active world event
func (s *WorldEventsService) applyEventEffects(event *models.WorldEvent) error {
	s.logger.Info("Applying event effects", zap.String("event_id", event.ID.String()))

	// TODO: Implement effect application logic for different systems
	// This would involve calling other services (economy, social, gameplay)
	// via Event Bus or direct API calls

	return nil
}

// rollbackEventEffects rolls back effects when event is deactivated
func (s *WorldEventsService) rollbackEventEffects(event *models.WorldEvent) error {
	s.logger.Info("Rolling back event effects", zap.String("event_id", event.ID.String()))

	// TODO: Implement effect rollback logic

	return nil
}

// HealthCheckHandler handles health check requests
func (s *WorldEventsService) HealthCheckHandler(w http.ResponseWriter) {
	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"status":    "ok",
		"service":   "world-events-service",
		"timestamp": time.Now().UTC().Format(time.RFC3339),
		"version":   "1.0.0",
	})
}

// ReadinessCheckHandler handles readiness check requests
func (s *WorldEventsService) ReadinessCheckHandler(w http.ResponseWriter, r *http.Request) {
	// Check database connectivity
	ctx, cancel := context.WithTimeout(r.Context(), 3*time.Second)
	defer cancel()

	if err := s.db.PingContext(ctx); err != nil {
		s.logger.Error("Database connectivity check failed", zap.Error(err))
		s.respondError(w, http.StatusServiceUnavailable, "Database not ready")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"status":    "ready",
		"service":   "world-events-service",
		"timestamp": time.Now().UTC().Format(time.RFC3339),
	})
}

// MetricsHandler handles metrics requests
func (s *WorldEventsService) MetricsHandler(w http.ResponseWriter) {
	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"service": "world-events-service",
		"metrics": map[string]interface{}{
			"uptime":         "unknown", // TODO: Add actual metrics
			"requests_total": 0,
		},
	})
}

// HealthCheckHandler handles health check requests
func (s *WorldEventsService) HealthCheckHandler(w http.ResponseWriter) {
	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"status":    "ok",
		"service":   "world-events-service",
		"timestamp": time.Now().UTC().Format(time.RFC3339),
		"version":   "1.0.0",
	})
}

// ReadinessCheckHandler handles readiness check requests
func (s *WorldEventsService) ReadinessCheckHandler(w http.ResponseWriter, r *http.Request) {
	// Check database connectivity
	ctx, cancel := context.WithTimeout(r.Context(), 3*time.Second)
	defer cancel()

	if err := s.db.PingContext(ctx); err != nil {
		s.logger.Error("Database connectivity check failed", zap.Error(err))
		s.respondError(w, http.StatusServiceUnavailable, "Database not ready")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"status":    "ready",
		"service":   "world-events-service",
		"timestamp": time.Now().UTC().Format(time.RFC3339),
	})
}

// MetricsHandler handles metrics requests
func (s *WorldEventsService) MetricsHandler(w http.ResponseWriter) {
	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"service": "world-events-service",
		"metrics": map[string]interface{}{
			"uptime":         "unknown", // TODO: Add actual metrics
			"requests_total": 0,
		},
	})
}

// respondJSON sends a JSON response
func (s *WorldEventsService) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

// respondError sends an error response
func (s *WorldEventsService) respondError(w http.ResponseWriter, status int, message string) {
	s.respondJSON(w, status, map[string]interface{}{
		"error": message,
		"code":  status,
	})
}
