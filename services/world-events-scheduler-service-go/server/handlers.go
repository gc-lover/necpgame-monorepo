// Issue: #44
package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/world-events-scheduler-service-go/pkg/api"
)

type Handlers struct {
	service Service
	logger  *zap.Logger
}

func NewHandlers(service Service, logger *zap.Logger) *Handlers {
	return &Handlers{service: service, logger: logger}
}

func (h *Handlers) ScheduleWorldEvent(w http.ResponseWriter, r *http.Request) {
	var req api.ScheduleWorldEventRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request", h.logger)
		return
	}

	eventID, err := uuid.Parse(req.EventId.String())
	if err != nil {
		respondError(w, http.StatusBadRequest, "Invalid event ID", h.logger)
		return
	}

	triggerType := string(req.TriggerType)
	cronPattern := ""
	if req.TriggerParameters != nil {
		if cp, ok := (*req.TriggerParameters)["cron_pattern"].(string); ok {
			cronPattern = cp
		}
	}

	scheduled, err := h.service.ScheduleEvent(
		r.Context(), eventID, req.ScheduledTime, cronPattern, triggerType,
	)
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to schedule", h.logger)
		return
	}

	respondJSON(w, http.StatusCreated, api.EventSchedule{
		Id:            openapi_types.UUID(scheduled.ID),
		EventId:       openapi_types.UUID(scheduled.EventID),
		ScheduledTime: scheduled.ScheduledAt,
		TriggerType:   api.EventTriggerType(scheduled.TriggerType),
		Status:        api.EventScheduleStatus("SCHEDULED"),
		CreatedAt:     scheduled.CreatedAt,
	})
}

func (h *Handlers) GetScheduledWorldEvents(w http.ResponseWriter, r *http.Request) {
	events, err := h.service.GetScheduledEvents(r.Context())
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to get schedules", h.logger)
		return
	}

	apiEvents := make([]api.EventSchedule, len(events))
	for i, event := range events {
		apiEvents[i] = api.EventSchedule{
			Id:            openapi_types.UUID(event.ID),
			EventId:       openapi_types.UUID(event.EventID),
			ScheduledTime: event.ScheduledAt,
			TriggerType:   api.EventTriggerType(event.TriggerType),
			Status:        api.EventScheduleStatus("SCHEDULED"),
			CreatedAt:     event.CreatedAt,
		}
	}

	response := api.EventSchedulesListResponse{
		Schedules: apiEvents,
		Total:     len(apiEvents),
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) TriggerScheduledWorldEvent(w http.ResponseWriter, r *http.Request, id openapi_types.UUID) {
	eventID, err := uuid.Parse(id.String())
	if err != nil {
		respondError(w, http.StatusBadRequest, "Invalid event ID", h.logger)
		return
	}

	if err := h.service.TriggerEvent(r.Context(), eventID, "manual", "manual trigger"); err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to trigger", h.logger)
		return
	}

	w.WriteHeader(http.StatusNoContent)
}

func (h *Handlers) GetWorldEventsCalendar(w http.ResponseWriter, r *http.Request, params api.GetWorldEventsCalendarParams) {
	events, err := h.service.GetScheduledEvents(r.Context())
	if err != nil {
		respondError(w, http.StatusInternalServerError, "Failed to get calendar", h.logger)
		return
	}

	// Filter by date range if provided
	var filteredEvents []*ScheduledEvent
	if params.StartDate != nil || params.EndDate != nil {
		for _, event := range events {
			include := true
			if params.StartDate != nil {
				startDate := params.StartDate.Time
				if event.ScheduledAt.Before(startDate) {
					include = false
				}
			}
			if params.EndDate != nil && include {
				endDate := params.EndDate.Time
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

	// Convert to calendar response
	apiEvents := make([]api.EventSchedule, len(filteredEvents))
	for i, event := range filteredEvents {
		apiEvents[i] = api.EventSchedule{
			Id:            openapi_types.UUID(event.ID),
			EventId:       openapi_types.UUID(event.EventID),
			ScheduledTime: event.ScheduledAt,
			TriggerType:   api.EventTriggerType(event.TriggerType),
			Status:        api.EventScheduleStatus("SCHEDULED"),
			CreatedAt:     event.CreatedAt,
		}
	}

	response := api.EventSchedulesListResponse{
		Schedules: apiEvents,
		Total:     len(apiEvents),
	}

	respondJSON(w, http.StatusOK, response)
}

func respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func respondError(w http.ResponseWriter, status int, message string, logger *zap.Logger) {
	logger.Error("Request error", zap.String("message", message))
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(map[string]string{"error": message})
}

