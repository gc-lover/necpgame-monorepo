// Issue: #44
package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/google/uuid"
	openapi_types "github.com/oapi-codegen/runtime/types"
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

	// Convert effects to map
	effects := make(map[string]interface{})
	if req.Effects != nil {
		effects["effects"] = req.Effects
	}
	
	event, err := h.service.CreateEvent(
		r.Context(),
		req.Title,
		req.Description,
		string(req.Type),
		string(req.Scale),
		string(req.Frequency),
		startTime,
		endTime,
		effects,
		nil, // triggers - not in current schema
		nil, // constraints - not in current schema
	)
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to create event", h.logger)
		return
	}

	respondJSON(w, http.StatusCreated, h.toAPIWorldEvent(event))
}

// GetWorldEvent - GET /world/events/{id}
func (h *Handlers) GetWorldEvent(w http.ResponseWriter, r *http.Request, id openapi_types.UUID) {
	eventUUID, err := uuid.Parse(id.String())
	if err != nil {
		respondError(w, http.StatusBadRequest, "Invalid event ID", h.logger)
		return
	}

	event, err := h.service.GetEvent(r.Context(), eventUUID)
	if err != nil {
		respondError(w, http.StatusNotFound, "Event not found", h.logger)
		return
	}

	respondJSON(w, http.StatusOK, h.toAPIWorldEvent(event))
}

// UpdateWorldEvent - PUT /world/events/{id}
func (h *Handlers) UpdateWorldEvent(w http.ResponseWriter, r *http.Request, id openapi_types.UUID) {
	eventUUID, err := uuid.Parse(id.String())
	if err != nil {
		respondError(w, http.StatusBadRequest, "Invalid event ID", h.logger)
		return
	}

	var req api.UpdateWorldEventRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request body", h.logger)
		return
	}

	// Convert optional types to service layer format
	var eventType, scale, frequency *string
	if req.Frequency != nil {
		f := string(*req.Frequency)
		frequency = &f
	}
	if req.Scale != nil {
		s := string(*req.Scale)
		scale = &s
	}

	// Simplified update (use actual fields from UpdateWorldEventRequest)
	event, err := h.service.UpdateEvent(
		r.Context(), eventUUID, nil, req.Description, eventType, scale, frequency, nil,
		req.StartTime, req.EndTime, nil, nil, nil,
	)
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to update event", h.logger)
		return
	}

	respondJSON(w, http.StatusOK, h.toAPIWorldEvent(event))
}

// DeleteWorldEvent - DELETE /world/events/{id}
func (h *Handlers) DeleteWorldEvent(w http.ResponseWriter, r *http.Request, id openapi_types.UUID) {
	eventUUID, err := uuid.Parse(id.String())
	if err != nil {
		respondError(w, http.StatusBadRequest, "Invalid event ID", h.logger)
		return
	}

	err = h.service.DeleteEvent(r.Context(), eventUUID)
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to delete event", h.logger)
		return
	}

	w.WriteHeader(http.StatusNoContent)
}

// ActivateWorldEvent - POST /world/events/{id}/activate
func (h *Handlers) ActivateWorldEvent(w http.ResponseWriter, r *http.Request, id openapi_types.UUID) {
	eventUUID, err := uuid.Parse(id.String())
	if err != nil {
		respondError(w, http.StatusBadRequest, "Invalid event ID", h.logger)
		return
	}

	// Get user ID from context (set by auth middleware)
	userID := "system" // TODO: get from JWT claims

	err = h.service.ActivateEvent(r.Context(), eventUUID, userID)
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to activate event", h.logger)
		return
	}

	w.WriteHeader(http.StatusNoContent)
}

// DeactivateWorldEvent - POST /world/events/{id}/deactivate
func (h *Handlers) DeactivateWorldEvent(w http.ResponseWriter, r *http.Request, id openapi_types.UUID) {
	eventUUID, err := uuid.Parse(id.String())
	if err != nil {
		respondError(w, http.StatusBadRequest, "Invalid event ID", h.logger)
		return
	}

	err = h.service.DeactivateEvent(r.Context(), eventUUID)
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to deactivate event", h.logger)
		return
	}

	w.WriteHeader(http.StatusNoContent)
}

// AnnounceWorldEvent - POST /world/events/{id}/announce
func (h *Handlers) AnnounceWorldEvent(w http.ResponseWriter, r *http.Request, id openapi_types.UUID) {
	eventUUID, err := uuid.Parse(id.String())
	if err != nil {
		respondError(w, http.StatusBadRequest, "Invalid event ID", h.logger)
		return
	}

	userID := "system" // TODO: get from JWT claims

	// Simplified announcement - no request body for now
	message := "Event announced"
	err = h.service.AnnounceEvent(r.Context(), eventUUID, userID, message, nil)
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

// GetWorldEventsByFrequency - GET /world/events/by-frequency/{frequency}
func (h *Handlers) GetWorldEventsByFrequency(w http.ResponseWriter, r *http.Request, frequency api.WorldEventFrequency) {
	events, err := h.service.GetActiveEvents(r.Context())
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to get events", h.logger)
		return
	}

	// Filter by frequency
	filtered := []api.WorldEvent{}
	for _, event := range events {
		if event.Frequency == string(frequency) {
			filtered = append(filtered, h.toAPIWorldEvent(event))
		}
	}

	respondJSON(w, http.StatusOK, filtered)
}

// GetWorldEventsByScale - GET /world/events/by-scale/{scale}
func (h *Handlers) GetWorldEventsByScale(w http.ResponseWriter, r *http.Request, scale api.WorldEventScale) {
	events, err := h.service.GetActiveEvents(r.Context())
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to get events", h.logger)
		return
	}

	// Filter by scale
	filtered := []api.WorldEvent{}
	for _, event := range events {
		if event.Scale == string(scale) {
			filtered = append(filtered, h.toAPIWorldEvent(event))
		}
	}

	respondJSON(w, http.StatusOK, filtered)
}

// GetWorldEventsByType - GET /world/events/by-type/{type}
func (h *Handlers) GetWorldEventsByType(w http.ResponseWriter, r *http.Request, pType api.WorldEventType) {
	events, err := h.service.GetActiveEvents(r.Context())
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to get events", h.logger)
		return
	}

	// Filter by type
	filtered := []api.WorldEvent{}
	for _, event := range events {
		if event.Type == string(pType) {
			filtered = append(filtered, h.toAPIWorldEvent(event))
		}
	}

	respondJSON(w, http.StatusOK, filtered)
}

// Helper: Convert internal WorldEvent to API WorldEvent
func (h *Handlers) toAPIWorldEvent(event *WorldEvent) api.WorldEvent {
	// Convert UUID
	idUUID := openapi_types.UUID{}
	_ = idUUID.UnmarshalText([]byte(event.ID.String()))
	
	return api.WorldEvent{
		Id:          idUUID,
		Title:       event.Name,
		Description: event.Description,
		Type:        api.WorldEventType(event.Type),
		Scale:       api.WorldEventScale(event.Scale),
		Frequency:   api.WorldEventFrequency(event.Frequency),
		Status:      api.WorldEventStatus(event.Status),
		StartTime:   event.StartTime,
		EndTime:     event.EndTime,
		Effects:     nil, // TODO: Convert []byte to []EventEffect
		CreatedAt:   event.CreatedAt,
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

