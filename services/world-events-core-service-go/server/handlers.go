// Issue: #44
package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/world-events-core-service-go/pkg/api"
)

// Handlers - структура handlers для реализации api.ServerInterface
type Handlers struct {
	service Service
	logger  *zap.Logger
}

// NewHandlers создает новый Handlers
func NewHandlers(service Service, logger *zap.Logger) *Handlers {
	return &Handlers{
		service: service,
		logger:  logger,
	}
}

// ListWorldEvents - GET /world/events
func (h *Handlers) ListWorldEvents(w http.ResponseWriter, r *http.Request, params api.ListWorldEventsParams) {
	filter := EventFilter{
		Limit:  20,
		Offset: 0,
	}

	if params.Status != nil {
		status := string(*params.Status)
		filter.Status = &status
	}
	if params.Type != nil {
		eventType := string(*params.Type)
		filter.Type = &eventType
	}
	if params.Scale != nil {
		scale := string(*params.Scale)
		filter.Scale = &scale
	}
	if params.Frequency != nil {
		frequency := string(*params.Frequency)
		filter.Frequency = &frequency
	}
	if params.Limit != nil {
		filter.Limit = *params.Limit
	}
	if params.Offset != nil {
		filter.Offset = *params.Offset
	}

	events, total, err := h.service.ListEvents(r.Context(), filter)
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to list events", h.logger)
		return
	}

	// Convert to API response
	apiEvents := make([]api.WorldEvent, len(events))
	for i, event := range events {
		apiEvents[i] = h.toAPIWorldEvent(event)
	}

	response := api.WorldEventsListResponse{
		Events: apiEvents,
		Total:  total,
	}

	respondJSON(w, http.StatusOK, response)
}

// CreateWorldEvent - POST /world/events
func (h *Handlers) CreateWorldEvent(w http.ResponseWriter, r *http.Request) {
	var req api.CreateWorldEventRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body", h.logger)
		return
	}

	var startTime, endTime *time.Time
	if req.StartTime != nil {
		t := time.Time(*req.StartTime)
		startTime = &t
	}
	if req.EndTime != nil {
		t := time.Time(*req.EndTime)
		endTime = &t
	}

	event, err := h.service.CreateEvent(
		r.Context(),
		req.Name,
		req.Description,
		string(req.Type),
		string(req.Scale),
		string(req.Frequency),
		startTime,
		endTime,
		req.Effects,
		req.Triggers,
		req.Constraints,
	)
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to create event", h.logger)
		return
	}

	respondJSON(w, http.StatusCreated, h.toAPIWorldEvent(event))
}

// GetWorldEvent - GET /world/events/{id}
func (h *Handlers) GetWorldEvent(w http.ResponseWriter, r *http.Request, eventId api.EventIdParam) {
	id, err := uuid.Parse(string(eventId))
	if err != nil {
		respondError(w, http.StatusBadRequest, "Invalid event ID", h.logger)
		return
	}

	event, err := h.service.GetEvent(r.Context(), id)
	if err != nil {
		respondError(w, http.StatusNotFound, "Event not found", h.logger)
		return
	}

	respondJSON(w, http.StatusOK, h.toAPIWorldEvent(event))
}

// UpdateWorldEvent - PUT /world/events/{id}
func (h *Handlers) UpdateWorldEvent(w http.ResponseWriter, r *http.Request, eventId api.EventIdParam) {
	id, err := uuid.Parse(string(eventId))
	if err != nil {
		respondError(w, http.StatusBadRequest, "Invalid event ID", h.logger)
		return
	}

	var req api.UpdateWorldEventRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body", h.logger)
		return
	}

	var startTime, endTime *time.Time
	if req.StartTime != nil {
		t := time.Time(*req.StartTime)
		startTime = &t
	}
	if req.EndTime != nil {
		t := time.Time(*req.EndTime)
		endTime = &t
	}

	var name, description, eventType, scale, frequency, status *string
	if req.Name != nil {
		name = req.Name
	}
	if req.Description != nil {
		description = req.Description
	}
	if req.Type != nil {
		t := string(*req.Type)
		eventType = &t
	}
	if req.Scale != nil {
		s := string(*req.Scale)
		scale = &s
	}
	if req.Frequency != nil {
		f := string(*req.Frequency)
		frequency = &f
	}
	if req.Status != nil {
		st := string(*req.Status)
		status = &st
	}

	event, err := h.service.UpdateEvent(
		r.Context(), id, name, description, eventType, scale, frequency, status,
		startTime, endTime, req.Effects, req.Triggers, req.Constraints,
	)
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to update event", h.logger)
		return
	}

	respondJSON(w, http.StatusOK, h.toAPIWorldEvent(event))
}

// DeleteWorldEvent - DELETE /world/events/{id}
func (h *Handlers) DeleteWorldEvent(w http.ResponseWriter, r *http.Request, eventId api.EventIdParam) {
	id, err := uuid.Parse(string(eventId))
	if err != nil {
		respondError(w, http.StatusBadRequest, "Invalid event ID", h.logger)
		return
	}

	err = h.service.DeleteEvent(r.Context(), id)
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to delete event", h.logger)
		return
	}

	w.WriteHeader(http.StatusNoContent)
}

// ActivateWorldEvent - POST /world/events/{id}/activate
func (h *Handlers) ActivateWorldEvent(w http.ResponseWriter, r *http.Request, eventId api.EventIdParam) {
	id, err := uuid.Parse(string(eventId))
	if err != nil {
		respondError(w, http.StatusBadRequest, "Invalid event ID", h.logger)
		return
	}

	// Get user ID from context (set by auth middleware)
	userID := "system" // TODO: get from JWT claims

	err = h.service.ActivateEvent(r.Context(), id, userID)
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to activate event", h.logger)
		return
	}

	w.WriteHeader(http.StatusNoContent)
}

// DeactivateWorldEvent - POST /world/events/{id}/deactivate
func (h *Handlers) DeactivateWorldEvent(w http.ResponseWriter, r *http.Request, eventId api.EventIdParam) {
	id, err := uuid.Parse(string(eventId))
	if err != nil {
		respondError(w, http.StatusBadRequest, "Invalid event ID", h.logger)
		return
	}

	err = h.service.DeactivateEvent(r.Context(), id)
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to deactivate event", h.logger)
		return
	}

	w.WriteHeader(http.StatusNoContent)
}

// AnnounceWorldEvent - POST /world/events/{id}/announce
func (h *Handlers) AnnounceWorldEvent(w http.ResponseWriter, r *http.Request, eventId api.EventIdParam) {
	id, err := uuid.Parse(string(eventId))
	if err != nil {
		respondError(w, http.StatusBadRequest, "Invalid event ID", h.logger)
		return
	}

	var req api.AnnounceWorldEventRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body", h.logger)
		return
	}

	userID := "system" // TODO: get from JWT claims

	err = h.service.AnnounceEvent(r.Context(), id, userID, req.Message, req.Channels)
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to announce event", h.logger)
		return
	}

	w.WriteHeader(http.StatusNoContent)
}

// GetActiveWorldEvents - GET /world/events/active
func (h *Handlers) GetActiveWorldEvents(w http.ResponseWriter, r *http.Request) {
	events, err := h.service.GetActiveEvents(r.Context())
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to get active events", h.logger)
		return
	}

	apiEvents := make([]api.WorldEvent, len(events))
	for i, event := range events {
		apiEvents[i] = h.toAPIWorldEvent(event)
	}

	respondJSON(w, http.StatusOK, apiEvents)
}

// GetPlannedWorldEvents - GET /world/events/planned
func (h *Handlers) GetPlannedWorldEvents(w http.ResponseWriter, r *http.Request) {
	events, err := h.service.GetPlannedEvents(r.Context())
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to get planned events", h.logger)
		return
	}

	apiEvents := make([]api.WorldEvent, len(events))
	for i, event := range events {
		apiEvents[i] = h.toAPIWorldEvent(event)
	}

	respondJSON(w, http.StatusOK, apiEvents)
}

// Helper: Convert internal WorldEvent to API WorldEvent
func (h *Handlers) toAPIWorldEvent(event *WorldEvent) api.WorldEvent {
	var effects, triggers, constraints map[string]interface{}

	if len(event.Effects) > 0 {
		json.Unmarshal(event.Effects, &effects)
	}
	if len(event.Triggers) > 0 {
		json.Unmarshal(event.Triggers, &triggers)
	}
	if len(event.Constraints) > 0 {
		json.Unmarshal(event.Constraints, &constraints)
	}

	return api.WorldEvent{
		Id:          event.ID.String(),
		Name:        event.Name,
		Description: event.Description,
		Type:        api.WorldEventType(event.Type),
		Scale:       api.WorldEventScale(event.Scale),
		Frequency:   api.WorldEventFrequency(event.Frequency),
		Status:      api.WorldEventStatus(event.Status),
		StartTime:   event.StartTime,
		EndTime:     event.EndTime,
		Effects:     effects,
		Triggers:    triggers,
		Constraints: constraints,
		CreatedAt:   event.CreatedAt,
		UpdatedAt:   event.UpdatedAt,
	}
}

// Helper functions for JSON response
func respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}
}

func respondError(w http.ResponseWriter, status int, message string, logger *zap.Logger) {
	logger.Error("Request error", zap.String("message", message), zap.Int("status", status))
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(map[string]string{"error": message})
}

