package service

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"

	api "necpgame/services/world-event-service-go/api"
	"necpgame/services/world-event-service-go/internal/repository"
)

// HealthCheck implements healthCheck operation.
func (h *Handler) HealthCheck(ctx context.Context) (api.HealthCheckRes, error) {
	dbHealthy := true
	if err := h.repo.HealthCheck(ctx); err != nil {
		dbHealthy = false
	}

	status := api.HealthCheckOKStatusHealthy
	if !dbHealthy {
		status = api.HealthCheckOKStatusUnhealthy
	}

	return &api.HealthCheckOK{
		Status:    status,
		Timestamp: time.Now(),
		Version:   api.NewOptString("1.0.0"),
		Uptime:    api.NewOptInt(0),
	}, nil
}

// ListEvents implements listEvents operation.
func (h *Handler) ListEvents(ctx context.Context, params api.ListEventsParams) (api.ListEventsRes, error) {
	events, err := h.repo.GetActiveEvents(ctx)
	if err != nil {
		return &api.Error{
			Code:    "500",
			Message: fmt.Sprintf("Failed to list events: %v", err),
		}, nil
	}

	var apiEvents []api.WorldEvent
	for _, event := range events {
		apiEvents = append(apiEvents, api.WorldEvent{
			ID:                  event.ID,
			Name:                event.Name,
			Type:                api.WorldEventType(event.Type),
			Region:              event.Region,
			Status:              api.WorldEventStatus(event.Status),
			StartTime:           event.StartTime,
			Difficulty:          api.WorldEventDifficulty(event.Difficulty),
			CurrentParticipants: api.NewOptInt(event.CurrentParticipants),
			CreatedAt:           api.NewOptDateTime(event.CreatedAt),
			UpdatedAt:           api.NewOptDateTime(event.UpdatedAt),
		})
	}

	return &api.ListEventsOK{
		Events:     apiEvents,
		Pagination: api.PaginationMeta{Total: len(apiEvents), Offset: 0, Limit: 50},
	}, nil
}

// CreateEvent implements createEvent operation.
func (h *Handler) CreateEvent(ctx context.Context, req *api.CreateEventRequest) (api.CreateEventRes, error) {
	event := &repository.WorldEvent{
		EventID:             uuid.New().String(),
		Name:                req.Name,
		Type:                string(req.Type),
		Region:              req.Region,
		Status:              "announced",
		StartTime:           req.StartTime,
		CurrentParticipants: 0,
		Difficulty:          string(req.Difficulty),
	}

	if req.Description.Set {
		event.Description = &req.Description.Value
	}
	if req.EndTime.Set {
		event.EndTime = &req.EndTime.Value
	}
	if req.MaxParticipants.Set {
		event.MaxParticipants = &req.MaxParticipants.Value
	}

	created, err := h.repo.CreateWorldEvent(ctx, event)
	if err != nil {
		return &api.CreateEventBadRequest{
			Code:    "400",
			Message: fmt.Sprintf("Failed to create event: %v", err),
		}, nil
	}

	return &api.WorldEvent{
		ID:                  created.ID,
		Name:                created.Name,
		Type:                api.WorldEventType(created.Type),
		Region:              created.Region,
		Status:              api.WorldEventStatus(created.Status),
		StartTime:           created.StartTime,
		Difficulty:          api.WorldEventDifficulty(created.Difficulty),
		CurrentParticipants: api.NewOptInt(created.CurrentParticipants),
		CreatedAt:           api.NewOptDateTime(created.CreatedAt),
		UpdatedAt:           api.NewOptDateTime(created.UpdatedAt),
	}, nil
}

// GetEvent implements getEvent operation.
func (h *Handler) GetEvent(ctx context.Context, params api.GetEventParams) (api.GetEventRes, error) {
	event, err := h.repo.GetWorldEvent(ctx, params.EventId)
	if err != nil {
		return &api.Error{
			Code:    "404",
			Message: fmt.Sprintf("Event not found: %s", params.EventId.String()),
		}, nil
	}

	return &api.WorldEventDetail{
		ID:                  event.ID,
		Name:                event.Name,
		Type:                api.WorldEventDetailType(event.Type),
		Region:              event.Region,
		Status:              api.WorldEventDetailStatus(event.Status),
		StartTime:           event.StartTime,
		Difficulty:          api.WorldEventDetailDifficulty(event.Difficulty),
		CurrentParticipants: api.NewOptInt(event.CurrentParticipants),
		CreatedAt:           api.NewOptDateTime(event.CreatedAt),
		UpdatedAt:           api.NewOptDateTime(event.UpdatedAt),
	}, nil
}

// UpdateEvent implements updateEvent operation.
func (h *Handler) UpdateEvent(ctx context.Context, req *api.UpdateEventRequest, params api.UpdateEventParams) (api.UpdateEventRes, error) {
	updates := make(map[string]interface{})

	if req.Name.Set {
		updates["name"] = req.Name.Value
	}
	if req.Description.Set {
		updates["description"] = &req.Description.Value
	}
	if req.Status.Set {
		updates["status"] = string(req.Status.Value)
	}
	if req.EndTime.Set {
		updates["end_time"] = &req.EndTime.Value
	}
	if req.MaxParticipants.Set {
		updates["max_participants"] = &req.MaxParticipants.Value
	}

	updated, err := h.repo.UpdateWorldEvent(ctx, params.EventId, updates)
	if err != nil {
		return &api.UpdateEventNotFound{
			Code:    "404",
			Message: fmt.Sprintf("Event not found: %s", params.EventId.String()),
		}, nil
	}

	return &api.WorldEvent{
		ID:                  updated.ID,
		Name:                updated.Name,
		Type:                api.WorldEventType(updated.Type),
		Region:              updated.Region,
		Status:              api.WorldEventStatus(updated.Status),
		StartTime:           updated.StartTime,
		Difficulty:          api.WorldEventDifficulty(updated.Difficulty),
		CurrentParticipants: api.NewOptInt(updated.CurrentParticipants),
		CreatedAt:           api.NewOptDateTime(updated.CreatedAt),
		UpdatedAt:           api.NewOptDateTime(updated.UpdatedAt),
	}, nil
}

// CancelEvent implements cancelEvent operation.
func (h *Handler) CancelEvent(ctx context.Context, params api.CancelEventParams) (api.CancelEventRes, error) {
	updates := map[string]interface{}{
		"status":   "cancelled",
		"end_time": time.Now(),
	}

	_, err := h.repo.UpdateWorldEvent(ctx, params.EventId, updates)
	if err != nil {
		return &api.CancelEventNotFound{
			Code:    "404",
			Message: fmt.Sprintf("Event not found: %s", params.EventId.String()),
		}, nil
	}

	return &api.CancelEventOK{
		Message: api.NewOptString("Event cancelled successfully"),
	}, nil
}

// TODO: Implement advanced participation features when API compatibility is resolved
