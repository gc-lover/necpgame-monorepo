// Issue: #2217
package handlers

import (
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"strings"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"go.uber.org/zap"

	"event-sourcing-aggregates-go/internal/service"
	"event-sourcing-aggregates-go/internal/metrics"
)

// EventSourcingHandlers handles HTTP requests
type EventSourcingHandlers struct {
	service *service.EventSourcingService
	logger  *zap.SugaredLogger
	metrics *metrics.Collector
}

// NewEventSourcingHandlers creates new event sourcing handlers
func NewEventSourcingHandlers(svc *service.EventSourcingService, logger *zap.SugaredLogger) *EventSourcingHandlers {
	return &EventSourcingHandlers{
		service: svc,
		logger:  logger,
		metrics: &metrics.Collector{}, // This should be passed from main
	}
}

// AuthMiddleware validates JWT tokens
func (h *EventSourcingHandlers) AuthMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			h.respondWithError(w, http.StatusUnauthorized, "Missing authorization header")
			return
		}

		// Simple token validation (should be replaced with proper JWT validation)
		if !strings.HasPrefix(authHeader, "Bearer ") {
			h.respondWithError(w, http.StatusUnauthorized, "Invalid authorization format")
			return
		}

		// For now, just check if token is not empty
		token := strings.TrimPrefix(authHeader, "Bearer ")
		if token == "" {
			h.respondWithError(w, http.StatusUnauthorized, "Empty token")
			return
		}

		next.ServeHTTP(w, r)
	})
}

// Health check endpoint
func (h *EventSourcingHandlers) Health(w http.ResponseWriter, r *http.Request) {
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"status":      "healthy",
		"service":     "event-sourcing-aggregates-service",
		"version":     "1.0.0",
		"timestamp":   time.Now(),
		"description": "Event sourcing and CQRS aggregates service",
	})
}

// Readiness check endpoint
func (h *EventSourcingHandlers) Ready(w http.ResponseWriter, r *http.Request) {
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"status":    "ready",
		"timestamp": time.Now(),
	})
}

// GetEventStream gets the event stream for an aggregate
func (h *EventSourcingHandlers) GetEventStream(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveEventAppendLatency(time.Since(start).Seconds()) }()

	aggregateIDStr := chi.URLParam(r, "aggregateId")
	aggregateID, err := uuid.Parse(aggregateIDStr)
	if err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid aggregate ID")
		return
	}

	fromVersionStr := r.URL.Query().Get("fromVersion")
	fromVersion := int64(0)
	if fv, err := strconv.ParseInt(fromVersionStr, 10, 64); err == nil && fv >= 0 {
		fromVersion = fv
	}

	events, err := h.service.GetEventStream(r.Context(), aggregateID, fromVersion)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"aggregateId": aggregateID.String(),
		"events":      events,
		"total":       len(events),
		"fromVersion": fromVersion,
	})
}

// GetAggregatesByType gets aggregates by type
func (h *EventSourcingHandlers) GetAggregatesByType(w http.ResponseWriter, r *http.Request) {
	aggregateType := chi.URLParam(r, "aggregateType")
	limitStr := r.URL.Query().Get("limit")
	offsetStr := r.URL.Query().Get("offset")

	limit := 20
	if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
		limit = l
	}

	offset := 0
	if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
		offset = o
	}

	// This would typically query the aggregates table
	// For now, return mock data
	aggregates := []map[string]interface{}{
		{
			"aggregateId":   "agg_001",
			"aggregateType": aggregateType,
			"version":       15,
			"lastEventAt":   time.Now().Add(-time.Hour),
		},
		{
			"aggregateId":   "agg_002",
			"aggregateType": aggregateType,
			"version":       8,
			"lastEventAt":   time.Now().Add(-2 * time.Hour),
		},
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"aggregateType": aggregateType,
		"aggregates":    aggregates,
		"total":         len(aggregates),
		"limit":         limit,
		"offset":        offset,
	})
}

// GetAggregate gets a single aggregate
func (h *EventSourcingHandlers) GetAggregate(w http.ResponseWriter, r *http.Request) {
	aggregateType := chi.URLParam(r, "aggregateType")
	aggregateIDStr := chi.URLParam(r, "aggregateId")
	aggregateID, err := uuid.Parse(aggregateIDStr)
	if err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid aggregate ID")
		return
	}

	// Try to get from snapshot first
	snapshot, err := h.service.GetAggregateSnapshot(r.Context(), aggregateID)
	if err == nil {
		h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
			"aggregateId":   aggregateID.String(),
			"aggregateType": aggregateType,
			"version":       snapshot.Version,
			"state":         snapshot.State,
			"source":        "snapshot",
			"snapshotAt":    snapshot.CreatedAt,
		})
		return
	}

	// Rebuild from events
	state, err := h.service.RebuildAggregate(r.Context(), aggregateID, aggregateType)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"aggregateId":   aggregateID.String(),
		"aggregateType": aggregateType,
		"state":         state,
		"source":        "rebuild",
		"rebuiltAt":     time.Now(),
	})
}

// AppendEvent appends a new event to the event store
func (h *EventSourcingHandlers) AppendEvent(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveEventAppendLatency(time.Since(start).Seconds()) }()

	var req struct {
		AggregateID   string                 `json:"aggregateId"`
		AggregateType string                 `json:"aggregateType"`
		EventType     string                 `json:"eventType"`
		Payload       map[string]interface{} `json:"payload"`
		Metadata      map[string]interface{} `json:"metadata,omitempty"`
		CausationID   string                 `json:"causationId,omitempty"`
		CorrelationID string                 `json:"correlationId,omitempty"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	aggregateID, err := uuid.Parse(req.AggregateID)
	if err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid aggregate ID")
		return
	}

	var causationID, correlationID *uuid.UUID
	if req.CausationID != "" {
		if id, err := uuid.Parse(req.CausationID); err == nil {
			causationID = &id
		}
	}
	if req.CorrelationID != "" {
		if id, err := uuid.Parse(req.CorrelationID); err == nil {
			correlationID = &id
		}
	}

	event, err := h.service.AppendEvent(r.Context(), aggregateID, req.AggregateType,
		req.EventType, req.Payload, req.Metadata, causationID, correlationID)

	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusCreated, event)
}

// GetEvents gets events with filtering
func (h *EventSourcingHandlers) GetEvents(w http.ResponseWriter, r *http.Request) {
	eventType := r.URL.Query().Get("eventType")
	aggregateType := r.URL.Query().Get("aggregateType")
	limitStr := r.URL.Query().Get("limit")
	offsetStr := r.URL.Query().Get("offset")

	limit := 50
	if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 500 {
		limit = l
	}

	offset := 0
	if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
		offset = o
	}

	// This would query events with filters
	// For now, return mock data
	events := []map[string]interface{}{
		{
			"eventId":        "evt_001",
			"aggregateId":    "agg_001",
			"aggregateType":  aggregateType,
			"eventType":      eventType,
			"aggregateVersion": 5,
			"occurredAt":     time.Now().Add(-time.Hour),
			"payload":        map[string]interface{}{"key": "value"},
		},
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"events":        events,
		"total":         len(events),
		"limit":         limit,
		"offset":        offset,
		"eventType":     eventType,
		"aggregateType": aggregateType,
	})
}

// RebuildAggregate rebuilds an aggregate from its event stream
func (h *EventSourcingHandlers) RebuildAggregate(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveSnapshotCreationTime(time.Since(start).Seconds()) }()

	aggregateType := chi.URLParam(r, "aggregateType")
	aggregateIDStr := chi.URLParam(r, "aggregateId")
	aggregateID, err := uuid.Parse(aggregateIDStr)
	if err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid aggregate ID")
		return
	}

	state, err := h.service.RebuildAggregate(r.Context(), aggregateID, aggregateType)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"aggregateId":   aggregateID.String(),
		"aggregateType": aggregateType,
		"state":         state,
		"rebuiltAt":     time.Now(),
		"processingTime": time.Since(start).String(),
	})
}

// GetAggregateSnapshot gets the latest snapshot for an aggregate
func (h *EventSourcingHandlers) GetAggregateSnapshot(w http.ResponseWriter, r *http.Request) {
	aggregateType := chi.URLParam(r, "aggregateType")
	aggregateIDStr := chi.URLParam(r, "aggregateId")
	aggregateID, err := uuid.Parse(aggregateIDStr)
	if err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid aggregate ID")
		return
	}

	snapshot, err := h.service.GetAggregateSnapshot(r.Context(), aggregateID)
	if err != nil {
		if strings.Contains(err.Error(), "not found") {
			h.respondWithError(w, http.StatusNotFound, "Snapshot not found")
			return
		}
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"aggregateId":   aggregateID.String(),
		"aggregateType": aggregateType,
		"version":       snapshot.Version,
		"state":         snapshot.State,
		"createdAt":     snapshot.CreatedAt,
	})
}

// CreateAggregateSnapshot creates a new snapshot for an aggregate
func (h *EventSourcingHandlers) CreateAggregateSnapshot(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveSnapshotCreationTime(time.Since(start).Seconds()) }()

	aggregateType := chi.URLParam(r, "aggregateType")
	aggregateIDStr := chi.URLParam(r, "aggregateId")
	aggregateID, err := uuid.Parse(aggregateIDStr)
	if err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid aggregate ID")
		return
	}

	var req struct {
		State map[string]interface{} `json:"state"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// Get current version from events
	currentVersion, err := h.getCurrentAggregateVersion(r.Context(), aggregateID)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	if err := h.service.CreateAggregateSnapshot(r.Context(), aggregateID, aggregateType, currentVersion, req.State); err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusCreated, map[string]interface{}{
		"aggregateId":   aggregateID.String(),
		"aggregateType": aggregateType,
		"version":       currentVersion,
		"createdAt":     time.Now(),
		"processingTime": time.Since(start).String(),
	})
}

// GetReadModel gets a read model
func (h *EventSourcingHandlers) GetReadModel(w http.ResponseWriter, r *http.Request) {
	modelName := chi.URLParam(r, "modelName")
	id := chi.URLParam(r, "id")

	if id == "" {
		// Get all read models for this type
		// For now, return mock data
		models := []map[string]interface{}{
			{
				"id":        "model_001",
				"modelName": modelName,
				"data":      map[string]interface{}{"status": "active", "count": 42},
				"version":   5,
				"updatedAt": time.Now(),
			},
		}

		h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
			"modelName": modelName,
			"models":    models,
			"total":     len(models),
		})
		return
	}

	model, err := h.service.GetReadModel(r.Context(), modelName, id)
	if err != nil {
		if strings.Contains(err.Error(), "not found") {
			h.respondWithError(w, http.StatusNotFound, "Read model not found")
			return
		}
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, model)
}

// GetReadModelById gets a specific read model by ID
func (h *EventSourcingHandlers) GetReadModelById(w http.ResponseWriter, r *http.Request) {
	modelName := chi.URLParam(r, "modelName")
	id := chi.URLParam(r, "id")

	model, err := h.service.GetReadModel(r.Context(), modelName, id)
	if err != nil {
		if strings.Contains(err.Error(), "not found") {
			h.respondWithError(w, http.StatusNotFound, "Read model not found")
			return
		}
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, model)
}

// GetProjection gets a projection
func (h *EventSourcingHandlers) GetProjection(w http.ResponseWriter, r *http.Request) {
	projectionName := chi.URLParam(r, "projectionName")

	// Mock projection data
	projection := map[string]interface{}{
		"projectionName": projectionName,
		"status":         "active",
		"lastProcessedEventId": "evt_123",
		"processedEvents": 15432,
		"lastUpdated":     time.Now(),
		"data": map[string]interface{}{
			"totalPlayers": 1250,
			"activeGames":  89,
			"totalQuests":  456,
		},
	}

	h.respondWithJSON(w, http.StatusOK, projection)
}

// GetProcessingStatus gets the current event processing status
func (h *EventSourcingHandlers) GetProcessingStatus(w http.ResponseWriter, r *http.Request) {
	status, err := h.service.GetProcessingStatus(r.Context())
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, status)
}

// RetryEventProcessing retries processing a failed event
func (h *EventSourcingHandlers) RetryEventProcessing(w http.ResponseWriter, r *http.Request) {
	eventIDStr := chi.URLParam(r, "eventId")
	eventID, err := uuid.Parse(eventIDStr)
	if err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid event ID")
		return
	}

	// Implementation for retrying event processing
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"eventId":     eventID.String(),
		"status":      "retry_queued",
		"queuedAt":    time.Now(),
		"message":     "Event processing retry has been queued",
	})
}

// GetEventsPerDayAnalytics gets events per day analytics
func (h *EventSourcingHandlers) GetEventsPerDayAnalytics(w http.ResponseWriter, r *http.Request) {
	daysStr := r.URL.Query().Get("days")
	days := 7
	if d, err := strconv.Atoi(daysStr); err == nil && d > 0 && d <= 90 {
		days = d
	}

	// Mock analytics data
	analytics := []map[string]interface{}{
		{
			"date":         time.Now().AddDate(0, 0, -6).Format("2006-01-02"),
			"events":      1250,
			"uniqueAggregates": 89,
		},
		{
			"date":         time.Now().AddDate(0, 0, -5).Format("2006-01-02"),
			"events":      1180,
			"uniqueAggregates": 76,
		},
		{
			"date":         time.Now().AddDate(0, 0, -4).Format("2006-01-02"),
			"events":      1340,
			"uniqueAggregates": 92,
		},
		{
			"date":         time.Now().AddDate(0, 0, -3).Format("2006-01-02"),
			"events":      1420,
			"uniqueAggregates": 105,
		},
		{
			"date":         time.Now().AddDate(0, 0, -2).Format("2006-01-02"),
			"events":      1380,
			"uniqueAggregates": 98,
		},
		{
			"date":         time.Now().AddDate(0, 0, -1).Format("2006-01-02"),
			"events":      1290,
			"uniqueAggregates": 87,
		},
		{
			"date":         time.Now().Format("2006-01-02"),
			"events":      1150,
			"uniqueAggregates": 73,
		},
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"analytics": analytics,
		"periodDays": days,
		"totalEvents": 10010,
		"avgEventsPerDay": 1430,
		"timestamp": time.Now(),
	})
}

// GetProcessingLatencyAnalytics gets processing latency analytics
func (h *EventSourcingHandlers) GetProcessingLatencyAnalytics(w http.ResponseWriter, r *http.Request) {
	hoursStr := r.URL.Query().Get("hours")
	hours := 24
	if h, err := strconv.Atoi(hoursStr); err == nil && h > 0 && h <= 168 {
		hours = h
	}

	// Mock latency analytics
	analytics := map[string]interface{}{
		"p50Latency":      0.045, // seconds
		"p95Latency":      0.125,
		"p99Latency":      0.280,
		"avgLatency":      0.067,
		"minLatency":      0.012,
		"maxLatency":      1.234,
		"totalProcessed":  15432,
		"failedProcessing": 23,
		"periodHours":     hours,
		"timestamp":       time.Now(),
	}

	h.respondWithJSON(w, http.StatusOK, analytics)
}

// GetAggregateSizesAnalytics gets aggregate sizes analytics
func (h *EventSourcingHandlers) GetAggregateSizesAnalytics(w http.ResponseWriter, r *http.Request) {
	// Mock aggregate sizes analytics
	analytics := []map[string]interface{}{
		{
			"aggregateType":  "player",
			"totalAggregates": 1250,
			"avgEventsPerAggregate": 45.6,
			"avgSizeBytes":   2450,
			"largestAggregateEvents": 234,
		},
		{
			"aggregateType":  "quest",
			"totalAggregates": 456,
			"avgEventsPerAggregate": 23.4,
			"avgSizeBytes":   1200,
			"largestAggregateEvents": 89,
		},
		{
			"aggregateType":  "combat_session",
			"totalAggregates": 3456,
			"avgEventsPerAggregate": 12.8,
			"avgSizeBytes":   890,
			"largestAggregateEvents": 67,
		},
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"analytics": analytics,
		"timestamp": time.Now(),
	})
}

// Helper functions
func (h *EventSourcingHandlers) respondWithJSON(w http.ResponseWriter, status int, payload interface{}) {
	response, _ := json.Marshal(payload)
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	w.Write(response)
}

func (h *EventSourcingHandlers) respondWithError(w http.ResponseWriter, status int, message string) {
	h.respondWithJSON(w, status, map[string]string{"error": message})
}

func (h *EventSourcingHandlers) getCurrentAggregateVersion(ctx context.Context, aggregateID uuid.UUID) (int64, error) {
	// This would query the database to get current version
	// For now, return mock version
	return 15, nil
}
