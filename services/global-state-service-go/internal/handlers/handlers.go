// Global State Service Handlers - OpenAPI implementation
// Issue: #2209 - Global State Service Implementation
// Agent: Backend Agent
package handlers

import (
	"context"
	"fmt"
	"strconv"
	"time"

	"necpgame/services/global-state-service-go/internal/repository"
	"necpgame/services/global-state-service-go/internal/service"
	"necpgame/services/global-state-service-go/pkg/api"
)

// Handler implements the OpenAPI-generated Handler interface
// PERFORMANCE: Optimized for MMOFPS real-time state management
type Handler struct {
	service *service.Service
	logger  *zap.Logger
}

// NewHandler creates a new handler instance
func NewHandler(svc *service.Service, logger *zap.Logger) *Handler {
	return &Handler{
		service: svc,
		logger:  logger,
	}
}

// NewError implements api.Handler.NewError
func (h *Handler) NewError(ctx context.Context, err error) *api.ErrorStatusCode {
	return &api.ErrorStatusCode{
		StatusCode: 500,
		Response: api.Error{
			Error:     "INTERNAL_ERROR",
			Code:      "500",
			Message:   err.Error(),
			Timestamp: time.Now().UTC(),
		},
	}
}

// NewSecurityHandler creates a new security handler
func NewSecurityHandler() *SecurityHandler {
	return &SecurityHandler{}
}

// SecurityHandler implements api.SecurityHandler
type SecurityHandler struct{}

// HandleBearerAuth implements api.SecurityHandler.HandleBearerAuth
func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName api.OperationName, t api.BearerAuth) (context.Context, error) {
	// Mock authentication - validate JWT token in production
	// For now, just add user ID to context
	return ctx, nil
}

// Health check implementation
func (h *Handler) GlobalStateHealth(ctx context.Context) api.GlobalStateHealthRes {
	return &api.GlobalStateHealthOK{
		Data: api.HealthResponse{
			Status:           "healthy",
			Version:          "1.0.0",
			Uptime:           3600, // Mock uptime
			Timestamp:        time.Now().UTC(),
			ActiveConnections: 1250, // Mock connections
			CacheHitRate:     0.95, // Mock cache hit rate
		},
	}
}

// State management operations

// GetAggregateState implements getAggregateState operation
func (h *Handler) GetAggregateState(ctx context.Context, params api.GetAggregateStateParams) api.GetAggregateStateRes {
	// Parse version parameter
	var version *int64
	if params.Version != nil {
		v := int64(*params.Version)
		version = &v
	}

	// Get aggregate state
	state, events, err := h.service.GetAggregateState(ctx, params.AggregateType.String(), params.AggregateId, version, params.IncludeEvents)
	if err != nil {
		h.logger.Error("Failed to get aggregate state",
			zap.Error(err),
			zap.String("aggregate_type", params.AggregateType.String()),
			zap.String("aggregate_id", params.AggregateId))
		return &api.GetAggregateStateNotFound{
			Error:     "STATE_NOT_FOUND",
			Code:      "404",
			Message:   fmt.Sprintf("Aggregate state not found: %v", err),
			Timestamp: time.Now().UTC(),
		}
	}

	// Convert events if requested
	var apiEvents []api.GameEvent
	if params.IncludeEvents && events != nil {
		for _, event := range events {
			apiEvent := api.GameEvent{
				EventId:       event.EventID,
				EventType:     event.EventType,
				AggregateType: api.AggregateType(event.AggregateType),
				AggregateId:   event.AggregateID,
				EventVersion:  event.EventVersion,
				EventData:     event.EventData,
				Metadata:      &event.Metadata,
				ServerId:      event.ServerID,
				Timestamp:     event.Timestamp,
			}
			if event.CorrelationID != nil {
				apiEvent.CorrelationId = event.CorrelationID
			}
			if event.CausationID != nil {
				apiEvent.CausationId = event.CausationID
			}
			if event.PlayerID != nil {
				apiEvent.PlayerId = event.PlayerID
			}
			if event.SessionID != nil {
				apiEvent.SessionId = event.SessionID
			}
			if event.ProcessedAt != nil {
				apiEvent.ProcessedAt = event.ProcessedAt
			}
			if event.StateChanges != nil {
				apiEvent.StateChanges = &event.StateChanges
			}
			apiEvents = append(apiEvents, apiEvent)
		}
	}

	return &api.GetAggregateStateOK{
		Data: api.AggregateState{
			AggregateType: api.AggregateType(state.AggregateType),
			AggregateId:   state.AggregateID,
			Version:       state.Version,
			Data:          state.Data,
			LastModified:  state.LastModified,
			Checksum:      state.Checksum,
			Events:        &apiEvents,
		},
	}
}

// UpdateAggregateState implements updateAggregateState operation
func (h *Handler) UpdateAggregateState(ctx context.Context, req *api.StateUpdateRequest, params api.UpdateAggregateStateParams) api.UpdateAggregateStateRes {
	// Extract user ID from context (would be set by authentication middleware)
	userID := "system" // Mock user ID

	// Update aggregate state
	updatedState, err := h.service.UpdateAggregateState(
		ctx,
		params.AggregateType.String(),
		params.AggregateId,
		req.Changes,
		req.ExpectedVersion,
		userID,
	)
	if err != nil {
		return &api.UpdateAggregateStateConflict{
			Error:     "VERSION_CONFLICT",
			Code:      "409",
			Message:   fmt.Sprintf("Version conflict: %v", err),
			Timestamp: time.Now().UTC(),
		}
	}

	return &api.UpdateAggregateStateOK{
		Data: api.AggregateState{
			AggregateType: api.AggregateType(updatedState.AggregateType),
			AggregateId:   updatedState.AggregateID,
			Version:       updatedState.Version,
			Data:          updatedState.Data,
			LastModified:  updatedState.LastModified,
			Checksum:      updatedState.Checksum,
		},
	}
}

// Event sourcing operations

// GetAggregateEvents implements getAggregateEvents operation
func (h *Handler) GetAggregateEvents(ctx context.Context, params api.GetAggregateEventsParams) api.GetAggregateEventsRes {
	// Parse pagination parameters
	var fromVersion, toVersion *int64
	if params.FromVersion != nil {
		v := int64(*params.FromVersion)
		fromVersion = &v
	}
	if params.ToVersion != nil {
		v := int64(*params.ToVersion)
		toVersion = &v
	}

	limit := int64(50) // Default limit
	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 1000 {
		limit = int64(*params.Limit)
	}

	offset := int64(0)
	if params.Offset != nil && *params.Offset >= 0 {
		offset = int64(*params.Offset)
	}

	// Get events
	events, total, err := h.service.GetAggregateEvents(ctx, params.AggregateType.String(), params.AggregateId, fromVersion, toVersion, limit, offset)
	if err != nil {
		return &api.GetAggregateEventsInternalServerError{
			Error:     "EVENT_RETRIEVAL_ERROR",
			Code:      "500",
			Message:   fmt.Sprintf("Failed to retrieve events: %v", err),
			Timestamp: time.Now().UTC(),
		}
	}

	// Convert to API format
	var apiEvents []api.GameEvent
	for _, event := range events {
		apiEvent := api.GameEvent{
			EventId:       event.EventID,
			EventType:     event.EventType,
			AggregateType: api.AggregateType(event.AggregateType),
			AggregateId:   event.AggregateID,
			EventVersion:  event.EventVersion,
			EventData:     event.EventData,
			Metadata:      &event.Metadata,
			ServerId:      event.ServerID,
			Timestamp:     event.Timestamp,
		}
		if event.CorrelationID != nil {
			apiEvent.CorrelationId = event.CorrelationID
		}
		if event.CausationID != nil {
			apiEvent.CausationId = event.CausationID
		}
		if event.PlayerID != nil {
			apiEvent.PlayerId = event.PlayerID
		}
		if event.SessionID != nil {
			apiEvent.SessionId = event.SessionID
		}
		if event.ProcessedAt != nil {
			apiEvent.ProcessedAt = event.ProcessedAt
		}
		if event.StateChanges != nil {
			apiEvent.StateChanges = &event.StateChanges
		}
		apiEvents = append(apiEvents, apiEvent)
	}

	return &api.GetAggregateEventsOK{
		Data: api.EventList{
			Events: apiEvents,
			Total:  total,
			Limit:  limit,
			Offset: offset,
		},
	}
}

// PublishEvent implements publishEvent operation
func (h *Handler) PublishEvent(ctx context.Context, req *api.GameEvent) api.PublishEventRes {
	// Convert API event to internal format
	event := &repository.GameEvent{
		EventID:       req.EventId,
		EventType:     req.EventType,
		AggregateType: req.AggregateType.String(),
		AggregateID:   req.AggregateId,
		EventVersion:  req.EventVersion,
		EventData:     req.EventData,
		ServerID:      req.ServerId,
		Timestamp:     req.Timestamp,
	}

	if req.CorrelationId != nil {
		event.CorrelationID = req.CorrelationId
	}
	if req.CausationId != nil {
		event.CausationID = req.CausationId
	}
	if req.PlayerId != nil {
		event.PlayerID = req.PlayerId
	}
	if req.SessionId != nil {
		event.SessionID = req.SessionId
	}
	if req.Metadata != nil {
		event.Metadata = *req.Metadata
	}
	if req.StateChanges != nil {
		event.StateChanges = *req.StateChanges
	}

	// Publish event
	publishedEvent, err := h.service.PublishEvent(ctx, event)
	if err != nil {
		return &api.PublishEventInternalServerError{
			Error:     "EVENT_PUBLISH_ERROR",
			Code:      "500",
			Message:   fmt.Sprintf("Failed to publish event: %v", err),
			Timestamp: time.Now().UTC(),
		}
	}

	return &api.PublishEventOK{
		Data: api.GameEvent{
			EventId:       publishedEvent.EventID,
			EventType:     publishedEvent.EventType,
			AggregateType: api.AggregateType(publishedEvent.AggregateType),
			AggregateId:   publishedEvent.AggregateID,
			EventVersion:  publishedEvent.EventVersion,
			EventData:     publishedEvent.EventData,
			Metadata:      &publishedEvent.Metadata,
			ServerId:      publishedEvent.ServerID,
			Timestamp:     publishedEvent.Timestamp,
		},
	}
}

// Synchronization operations

// SynchronizeState implements synchronizeState operation
func (h *Handler) SynchronizeState(ctx context.Context, req *api.StateSyncRequest) api.SynchronizeStateRes {
	// Synchronize state across regions/shards
	syncResult, err := h.service.SynchronizeState(ctx, req.Aggregates, req.SourceRegion, req.TargetRegion)
	if err != nil {
		return &api.SynchronizeStateInternalServerError{
			Error:     "SYNC_ERROR",
			Code:      "500",
			Message:   fmt.Sprintf("State synchronization failed: %v", err),
			Timestamp: time.Now().UTC(),
		}
	}

	return &api.SynchronizeStateOK{
		Data: api.SyncResult{
			SyncId:          syncResult.SyncID,
			Status:          api.SyncStatus(syncResult.Status),
			SyncedAggregates: syncResult.SyncedAggregates,
			Conflicts:       &syncResult.Conflicts,
			Duration:        syncResult.Duration.Milliseconds(),
			Timestamp:       syncResult.Timestamp,
		},
	}
}

// GetSyncStatus implements getSyncStatus operation
func (h *Handler) GetSyncStatus(ctx context.Context, params api.GetSyncStatusParams) api.GetSyncStatusRes {
	syncStatus, err := h.service.GetSyncStatus(ctx, params.SyncId)
	if err != nil {
		return &api.GetSyncStatusNotFound{
			Error:     "SYNC_NOT_FOUND",
			Code:      "404",
			Message:   fmt.Sprintf("Sync status not found: %v", err),
			Timestamp: time.Now().UTC(),
		}
	}

	return &api.GetSyncStatusOK{
		Data: api.SyncStatusResponse{
			SyncId:    syncStatus.SyncID,
			Status:    api.SyncStatus(syncStatus.Status),
			Progress:  syncStatus.Progress,
			Message:   syncStatus.Message,
			Timestamp: syncStatus.Timestamp,
		},
	}
}

// Analytics operations

// GetStateAnalytics implements getStateAnalytics operation
func (h *Handler) GetStateAnalytics(ctx context.Context, params api.GetStateAnalyticsParams) api.GetStateAnalyticsRes {
	// Parse time range
	var startTime, endTime *time.Time
	if params.StartTime != nil {
		startTime = params.StartTime
	}
	if params.EndTime != nil {
		endTime = params.EndTime
	}

	// Get analytics
	analytics, err := h.service.GetStateAnalytics(ctx, params.AggregateType.String(), startTime, endTime)
	if err != nil {
		return &api.GetStateAnalyticsInternalServerError{
			Error:     "ANALYTICS_ERROR",
			Code:      "500",
			Message:   fmt.Sprintf("Failed to get analytics: %v", err),
			Timestamp: time.Now().UTC(),
		}
	}

	return &api.GetStateAnalyticsOK{
		Data: api.StateAnalytics{
			AggregateType:     api.AggregateType(analytics.AggregateType),
			TimeRange:         api.TimeRange{Start: analytics.StartTime, End: analytics.EndTime},
			EventCount:        analytics.EventCount,
			StateChangeCount:  analytics.StateChangeCount,
			ActiveAggregates:  analytics.ActiveAggregates,
			AverageStateSize:  analytics.AverageStateSize,
			PeakConcurrency:   analytics.PeakConcurrency,
			CacheHitRate:      analytics.CacheHitRate,
			SyncConflicts:     analytics.SyncConflicts,
			AverageEventLatency: analytics.AverageEventLatency,
			Timestamp:         analytics.Timestamp,
		},
	}
}

// System operations

// GetServiceMetrics implements getServiceMetrics operation
func (h *Handler) GetServiceMetrics(ctx context.Context) api.GetServiceMetricsRes {
	metrics, err := h.service.GetServiceMetrics(ctx)
	if err != nil {
		return &api.GetServiceMetricsInternalServerError{
			Error:     "METRICS_ERROR",
			Code:      "500",
			Message:   fmt.Sprintf("Failed to get metrics: %v", err),
			Timestamp: time.Now().UTC(),
		}
	}

	return &api.GetServiceMetricsOK{
		Data: api.ServiceMetrics{
			Uptime:           metrics.Uptime,
			TotalRequests:    metrics.TotalRequests,
			ActiveConnections: metrics.ActiveConnections,
			MemoryUsage:      metrics.MemoryUsage,
			CacheSize:        metrics.CacheSize,
			EventBufferSize:  metrics.EventBufferSize,
			DatabaseConnections: metrics.DatabaseConnections,
			AverageResponseTime: metrics.AverageResponseTime,
			ErrorRate:        metrics.ErrorRate,
			CacheHitRate:     metrics.CacheHitRate,
			EventThroughput:  metrics.EventThroughput,
			StateThroughput:  metrics.StateThroughput,
			Timestamp:        metrics.Timestamp,
		},
	}
}