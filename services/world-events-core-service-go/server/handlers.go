// Package server Issue: #44 - ogen handlers (TYPED responses)
package server

import (
	"context"
	"encoding/json"
	"errors"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/world-events-core-service-go/pkg/api"
	"github.com/go-faster/jx"
	"github.com/google/uuid"
	"go.uber.org/zap"
)

const (
	DBTimeout = 50 * time.Millisecond
)

var (
	ErrNotFound = errors.New("not found")
)

type Handlers struct {
	service Service
	logger  *zap.Logger
}

func NewHandlers(service *service, logger *zap.Logger) *Handlers {
	return &Handlers{service: service, logger: logger}
}

func (h *Handlers) CreateWorldEvent(ctx context.Context, req *api.CreateWorldEventRequest) (api.CreateWorldEventRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	var startTime, endTime *time.Time
	if req.StartTime.Set {
		startTime = &req.StartTime.Value
	}
	if req.EndTime.Set {
		endTime = &req.EndTime.Value
	}

	// Convert effects to map[string]interface{}
	effects := make(map[string]interface{})
	for _, effect := range req.Effects {
		if effect.EffectType.Set && effect.Parameters.Set {
			var params map[string]interface{}
			// Convert jx.Raw map to JSON bytes
			paramsJSON, _ := json.Marshal(effect.Parameters.Value)
			if err := json.Unmarshal(paramsJSON, &params); err == nil {
				effects[effect.EffectType.Value] = params
			}
		}
	}

	event, err := h.service.CreateEvent(
		ctx, req.Title, req.Description, string(req.Type), string(req.Scale), string(req.Frequency),
		startTime, endTime, effects, nil, nil,
	)
	if err != nil {
		h.logger.Error("Failed to create event", zap.Error(err))
		return &api.CreateWorldEventInternalServerError{
			Error:   "INTERNAL_SERVER_ERROR",
			Message: err.Error(),
		}, nil
	}

	return h.toAPIWorldEvent(event), nil
}

func (h *Handlers) GetWorldEvent(ctx context.Context, params api.GetWorldEventParams) (api.GetWorldEventRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	event, err := h.service.GetEvent(ctx, params.ID)
	if err != nil {
		h.logger.Error("Failed to get event", zap.Error(err))
		return &api.GetWorldEventInternalServerError{
			Error:   "INTERNAL_SERVER_ERROR",
			Message: err.Error(),
		}, nil
	}

	if event == nil {
		return &api.GetWorldEventNotFound{
			Error:   "NOT_FOUND",
			Message: "Event not found",
		}, nil
	}

	return h.toAPIWorldEvent(event), nil
}

func (h *Handlers) UpdateWorldEvent(ctx context.Context, req *api.UpdateWorldEventRequest, params api.UpdateWorldEventParams) (api.UpdateWorldEventRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	var name, description, eventType, scale, frequency, status *string
	if req.Title.Set {
		name = &req.Title.Value
	}
	if req.Description.Set {
		description = &req.Description.Value
	}
	if req.Type.Set {
		s := string(req.Type.Value)
		eventType = &s
	}
	if req.Scale.Set {
		s := string(req.Scale.Value)
		scale = &s
	}
	if req.Frequency.Set {
		s := string(req.Frequency.Value)
		frequency = &s
	}
	// Note: UpdateWorldEventRequest doesn't have Status field

	var startTime, endTime *time.Time
	if req.StartTime.Set {
		startTime = &req.StartTime.Value
	}
	if req.EndTime.Set {
		endTime = &req.EndTime.Value
	}

	// Convert effects (UpdateWorldEventRequestEffectsItem is empty struct, skip for now)
	var effects map[string]interface{}
	// TODO: Parse effects from UpdateWorldEventRequestEffectsItem when schema is updated

	event, err := h.service.UpdateEvent(
		ctx, params.ID, name, description, eventType, scale, frequency, status,
		startTime, endTime, effects, nil, nil,
	)
	if err != nil {
		h.logger.Error("Failed to update event", zap.Error(err))
		if err == ErrNotFound {
			return &api.UpdateWorldEventNotFound{
				Error:   "NOT_FOUND",
				Message: "Event not found",
			}, nil
		}
		return &api.UpdateWorldEventInternalServerError{
			Error:   "INTERNAL_SERVER_ERROR",
			Message: err.Error(),
		}, nil
	}

	return h.toAPIWorldEvent(event), nil
}

func (h *Handlers) DeleteWorldEvent(ctx context.Context, params api.DeleteWorldEventParams) (api.DeleteWorldEventRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	err := h.service.DeleteEvent(ctx, params.ID)
	if err != nil {
		h.logger.Error("Failed to delete event", zap.Error(err))
		if err == ErrNotFound {
			return &api.DeleteWorldEventNotFound{
				Error:   "NOT_FOUND",
				Message: "Event not found",
			}, nil
		}
		return &api.DeleteWorldEventInternalServerError{
			Error:   "INTERNAL_SERVER_ERROR",
			Message: err.Error(),
		}, nil
	}

	return &api.DeleteWorldEventNoContent{}, nil
}

func (h *Handlers) ListWorldEvents(ctx context.Context, params api.ListWorldEventsParams) (api.ListWorldEventsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	filter := EventFilter{
		Limit:  20,
		Offset: 0,
	}

	if params.Status.Set {
		s := string(params.Status.Value)
		filter.Status = &s
	}
	if params.Type.Set {
		s := string(params.Type.Value)
		filter.Type = &s
	}
	if params.Scale.Set {
		s := string(params.Scale.Value)
		filter.Scale = &s
	}
	if params.Frequency.Set {
		s := string(params.Frequency.Value)
		filter.Frequency = &s
	}
	if params.Limit.Set {
		filter.Limit = params.Limit.Value
	}
	if params.Offset.Set {
		filter.Offset = params.Offset.Value
	}

	events, total, err := h.service.ListEvents(ctx, filter)
	if err != nil {
		h.logger.Error("Failed to list events", zap.Error(err))
		return &api.ListWorldEventsInternalServerError{
			Error:   "INTERNAL_SERVER_ERROR",
			Message: err.Error(),
		}, nil
	}

	apiEvents := make([]api.WorldEvent, len(events))
	for i, event := range events {
		apiEvents[i] = *h.toAPIWorldEvent(event)
	}

	return &api.WorldEventsListResponse{
		Events: apiEvents,
		Total:  total,
	}, nil
}

func (h *Handlers) GetActiveWorldEvents(ctx context.Context) (api.GetActiveWorldEventsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	events, err := h.service.GetActiveEvents(ctx)
	if err != nil {
		h.logger.Error("Failed to get active events", zap.Error(err))
		return &api.Error{
			Error:   "INTERNAL_SERVER_ERROR",
			Message: err.Error(),
		}, nil
	}

	apiEvents := make([]api.WorldEvent, len(events))
	for i, event := range events {
		apiEvents[i] = *h.toAPIWorldEvent(event)
	}

	return &api.WorldEventsListResponse{
		Events: apiEvents,
		Total:  len(apiEvents),
	}, nil
}

func (h *Handlers) GetPlannedWorldEvents(ctx context.Context) (api.GetPlannedWorldEventsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	events, err := h.service.GetPlannedEvents(ctx)
	if err != nil {
		h.logger.Error("Failed to get planned events", zap.Error(err))
		return &api.Error{
			Error:   "INTERNAL_SERVER_ERROR",
			Message: err.Error(),
		}, nil
	}

	apiEvents := make([]api.WorldEvent, len(events))
	for i, event := range events {
		apiEvents[i] = *h.toAPIWorldEvent(event)
	}

	return &api.WorldEventsListResponse{
		Events: apiEvents,
		Total:  len(apiEvents),
	}, nil
}

func (h *Handlers) ActivateWorldEvent(ctx context.Context, params api.ActivateWorldEventParams) (api.ActivateWorldEventRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Get activatedBy from context/auth
	event, err := h.service.GetEvent(ctx, params.ID)
	if err != nil {
		h.logger.Error("Failed to get event for activation", zap.Error(err))
		return &api.ActivateWorldEventNotFound{
			Error:   "NOT_FOUND",
			Message: "Event not found",
		}, nil
	}

	err = h.service.ActivateEvent(ctx, params.ID, "system")
	if err != nil {
		h.logger.Error("Failed to activate event", zap.Error(err))
		return &api.ActivateWorldEventInternalServerError{
			Error:   "INTERNAL_SERVER_ERROR",
			Message: err.Error(),
		}, nil
	}

	// Return updated event
	return h.toAPIWorldEvent(event), nil
}

func (h *Handlers) DeactivateWorldEvent(ctx context.Context, params api.DeactivateWorldEventParams) (api.DeactivateWorldEventRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	event, err := h.service.GetEvent(ctx, params.ID)
	if err != nil {
		h.logger.Error("Failed to get event for deactivation", zap.Error(err))
		return &api.DeactivateWorldEventNotFound{
			Error:   "NOT_FOUND",
			Message: "Event not found",
		}, nil
	}

	err = h.service.DeactivateEvent(ctx, params.ID)
	if err != nil {
		h.logger.Error("Failed to deactivate event", zap.Error(err))
		return &api.DeactivateWorldEventInternalServerError{
			Error:   "INTERNAL_SERVER_ERROR",
			Message: err.Error(),
		}, nil
	}

	// Return updated event
	return h.toAPIWorldEvent(event), nil
}

func (h *Handlers) AnnounceWorldEvent(ctx context.Context, params api.AnnounceWorldEventParams) (api.AnnounceWorldEventRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Parse request body for message and channels
	event, err := h.service.GetEvent(ctx, params.ID)
	if err != nil {
		h.logger.Error("Failed to get event for announcement", zap.Error(err))
		return &api.AnnounceWorldEventNotFound{
			Error:   "NOT_FOUND",
			Message: "Event not found",
		}, nil
	}

	err = h.service.AnnounceEvent(ctx, params.ID, "system", "Event announced", []string{"global"})
	if err != nil {
		h.logger.Error("Failed to announce event", zap.Error(err))
		return &api.AnnounceWorldEventInternalServerError{
			Error:   "INTERNAL_SERVER_ERROR",
			Message: err.Error(),
		}, nil
	}

	// Return event
	return h.toAPIWorldEvent(event), nil
}

func (h *Handlers) GetWorldEventsByType(ctx context.Context, params api.GetWorldEventsByTypeParams) (api.GetWorldEventsByTypeRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	filter := EventFilter{
		Type:   &[]string{string(params.Type)}[0],
		Limit:  100,
		Offset: 0,
	}

	events, total, err := h.service.ListEvents(ctx, filter)
	if err != nil {
		h.logger.Error("Failed to get events by type", zap.Error(err))
		return &api.GetWorldEventsByTypeInternalServerError{
			Error:   "INTERNAL_SERVER_ERROR",
			Message: err.Error(),
		}, nil
	}

	apiEvents := make([]api.WorldEvent, len(events))
	for i, event := range events {
		apiEvents[i] = *h.toAPIWorldEvent(event)
	}

	return &api.WorldEventsListResponse{
		Events: apiEvents,
		Total:  total,
	}, nil
}

func (h *Handlers) GetWorldEventsByScale(ctx context.Context, params api.GetWorldEventsByScaleParams) (api.GetWorldEventsByScaleRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	filter := EventFilter{
		Scale:  &[]string{string(params.Scale)}[0],
		Limit:  100,
		Offset: 0,
	}

	events, total, err := h.service.ListEvents(ctx, filter)
	if err != nil {
		h.logger.Error("Failed to get events by scale", zap.Error(err))
		return &api.GetWorldEventsByScaleInternalServerError{
			Error:   "INTERNAL_SERVER_ERROR",
			Message: err.Error(),
		}, nil
	}

	apiEvents := make([]api.WorldEvent, len(events))
	for i, event := range events {
		apiEvents[i] = *h.toAPIWorldEvent(event)
	}

	return &api.WorldEventsListResponse{
		Events: apiEvents,
		Total:  total,
	}, nil
}

func (h *Handlers) GetWorldEventsByFrequency(ctx context.Context, params api.GetWorldEventsByFrequencyParams) (api.GetWorldEventsByFrequencyRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	filter := EventFilter{
		Frequency: &[]string{string(params.Frequency)}[0],
		Limit:     100,
		Offset:    0,
	}

	events, total, err := h.service.ListEvents(ctx, filter)
	if err != nil {
		h.logger.Error("Failed to get events by frequency", zap.Error(err))
		return &api.GetWorldEventsByFrequencyInternalServerError{
			Error:   "INTERNAL_SERVER_ERROR",
			Message: err.Error(),
		}, nil
	}

	apiEvents := make([]api.WorldEvent, len(events))
	for i, event := range events {
		apiEvents[i] = *h.toAPIWorldEvent(event)
	}

	return &api.WorldEventsListResponse{
		Events: apiEvents,
		Total:  total,
	}, nil
}

// Helper: convert internal WorldEvent to API WorldEvent
func (h *Handlers) toAPIWorldEvent(event *WorldEvent) *api.WorldEvent {
	apiEvent := &api.WorldEvent{
		ID:          event.ID,
		Title:       event.Name,
		Description: event.Description,
		Type:        api.WorldEventType(event.Type),
		Scale:       api.WorldEventScale(event.Scale),
		Frequency:   api.WorldEventFrequency(event.Frequency),
		Status:      api.WorldEventStatus(event.Status),
		CreatedAt:   event.CreatedAt,
		UpdatedAt:   event.UpdatedAt,
	}

	if event.StartTime != nil {
		apiEvent.StartTime = api.NewOptNilDateTime(*event.StartTime)
	}
	if event.EndTime != nil {
		apiEvent.EndTime = api.NewOptNilDateTime(*event.EndTime)
	}

	// Parse effects JSON
	if len(event.Effects) > 0 {
		var effects map[string]interface{}
		if err := json.Unmarshal(event.Effects, &effects); err == nil {
			// Convert to API format (EventEffect)
			apiEffects := make([]api.EventEffect, 0, len(effects))
			for typ, params := range effects {
				paramsJSON, _ := json.Marshal(params)
				// Parse jx.Raw from JSON
				var rawParams map[string]jx.Raw
				json.Unmarshal(paramsJSON, &rawParams)
				apiEffects = append(apiEffects, api.EventEffect{
					ID:         uuid.New(),
					EventID:    event.ID,
					EffectType: typ,
					Parameters: api.NewOptEventEffectParameters(
						rawParams,
					),
				})
			}
			apiEvent.Effects = apiEffects
		}
	}

	return apiEvent
}

// Era-based functionality integrated into existing handlers
// Era configurations are now available via service methods
