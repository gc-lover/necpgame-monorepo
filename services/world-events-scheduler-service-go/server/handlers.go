// Issue: #44 - ogen handlers (TYPED responses)
package server

import (
	"context"
	"errors"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/world-events-scheduler-service-go/pkg/api"
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

func NewHandlers(service Service, logger *zap.Logger) *Handlers {
	return &Handlers{service: service, logger: logger}
}

func (h *Handlers) ScheduleWorldEvent(ctx context.Context, req *api.ScheduleWorldEventRequest) (api.ScheduleWorldEventRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	eventID := req.EventID
	triggerType := string(req.TriggerType)
	cronPattern := ""
	if req.TriggerParameters.Set {
		// TODO: Parse TriggerParameters JSON
	}

	scheduled, err := h.service.ScheduleEvent(
		ctx, eventID, req.ScheduledTime, cronPattern, triggerType,
	)
	if err != nil {
		h.logger.Error("Failed to schedule", zap.Error(err))
		return &api.ScheduleWorldEventInternalServerError{
			Error:   "INTERNAL_SERVER_ERROR",
			Message: err.Error(),
		}, nil
	}

	result := &api.EventSchedule{
		ID:            scheduled.ID,
		EventID:       scheduled.EventID,
		ScheduledTime: scheduled.ScheduledAt,
		TriggerType:   api.EventTriggerType(scheduled.TriggerType),
		Status:        api.EventScheduleStatusSCHEDULED,
		CreatedAt:     scheduled.CreatedAt,
	}

	return result, nil
}

func (h *Handlers) GetScheduledWorldEvents(ctx context.Context) (api.GetScheduledWorldEventsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	events, err := h.service.GetScheduledEvents(ctx)
	if err != nil {
		h.logger.Error("Failed to get schedules", zap.Error(err))
		return &api.Error{
			Error:   "INTERNAL_SERVER_ERROR",
			Message: err.Error(),
		}, nil
	}

	apiEvents := make([]api.EventSchedule, len(events))
	for i, event := range events {
		apiEvents[i] = api.EventSchedule{
			ID:            event.ID,
			EventID:       event.EventID,
			ScheduledTime: event.ScheduledAt,
			TriggerType:   api.EventTriggerType(event.TriggerType),
			Status:        api.EventScheduleStatusSCHEDULED,
			CreatedAt:     event.CreatedAt,
		}
	}

	result := &api.EventSchedulesListResponse{
		Schedules: apiEvents,
		Total:     len(apiEvents),
	}

	return result, nil
}

func (h *Handlers) TriggerScheduledWorldEvent(ctx context.Context, params api.TriggerScheduledWorldEventParams) (api.TriggerScheduledWorldEventRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	eventID := params.ID
	if err := h.service.TriggerEvent(ctx, eventID, "manual", "manual trigger"); err != nil {
		h.logger.Error("Failed to trigger", zap.Error(err))
		if err == ErrNotFound {
			return &api.TriggerScheduledWorldEventNotFound{}, nil
		}
		return &api.TriggerScheduledWorldEventInternalServerError{
			Error:   "INTERNAL_SERVER_ERROR",
			Message: err.Error(),
		}, nil
	}

	return &api.WorldEvent{
		ID: eventID,
	}, nil
}

func (h *Handlers) GetWorldEventsCalendar(ctx context.Context, params api.GetWorldEventsCalendarParams) (api.GetWorldEventsCalendarRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	events, err := h.service.GetScheduledEvents(ctx)
	if err != nil {
		h.logger.Error("Failed to get calendar", zap.Error(err))
		return &api.GetWorldEventsCalendarInternalServerError{
			Error:   "INTERNAL_SERVER_ERROR",
			Message: err.Error(),
		}, nil
	}

	var filteredEvents []*ScheduledEvent
	if params.StartDate.Set || params.EndDate.Set {
		for _, event := range events {
			include := true
			if params.StartDate.Set {
				startDate := params.StartDate.Value
				if event.ScheduledAt.Before(startDate) {
					include = false
				}
			}
			if params.EndDate.Set && include {
				endDate := params.EndDate.Value
				if event.ScheduledAt.After(endDate) {
					include = false
				}
			}
			if include {
				filteredEvents = append(filteredEvents, event)
			}
		}
	} else {
		filteredEvents = events
	}

	apiEvents := make([]api.EventSchedule, len(filteredEvents))
	for i, event := range filteredEvents {
		apiEvents[i] = api.EventSchedule{
			ID:            event.ID,
			EventID:       event.EventID,
			ScheduledTime: event.ScheduledAt,
			TriggerType:   api.EventTriggerType(event.TriggerType),
			Status:        api.EventScheduleStatusSCHEDULED,
			CreatedAt:     event.CreatedAt,
		}
	}

	result := &api.EventSchedulesListResponse{
		Schedules: apiEvents,
		Total:     len(apiEvents),
	}

	return result, nil
}
