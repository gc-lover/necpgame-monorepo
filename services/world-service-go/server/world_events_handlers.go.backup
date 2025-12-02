package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/world-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type WorldEventsHandlers struct {
	service WorldEventsServiceInterface
	logger  *logrus.Logger
}

func NewWorldEventsHandlers(service WorldEventsServiceInterface) *WorldEventsHandlers {
	return &WorldEventsHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *WorldEventsHandlers) ListWorldEvents(w http.ResponseWriter, r *http.Request, params api.ListWorldEventsParams) {
	limit := 20
	if params.Limit != nil {
		limit = *params.Limit
	}
	offset := 0
	if params.Offset != nil {
		offset = *params.Offset
	}

	events, err := h.service.ListWorldEvents(r.Context(), params.Status, params.Type, params.Scale, params.Frequency, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to list world events")
		h.respondError(w, http.StatusInternalServerError, "failed to list world events")
		return
	}

	response := api.WorldEventsListResponse{
		Events: events,
	}
	h.respondJSON(w, http.StatusOK, response)
}

func (h *WorldEventsHandlers) CreateWorldEvent(w http.ResponseWriter, r *http.Request) {
	var req api.CreateWorldEventJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	event, err := h.service.CreateWorldEvent(r.Context(), &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create world event")
		h.respondError(w, http.StatusInternalServerError, "failed to create world event")
		return
	}

	h.respondJSON(w, http.StatusCreated, event)
}

func (h *WorldEventsHandlers) GetActiveWorldEvents(w http.ResponseWriter, r *http.Request) {
	events, err := h.service.GetActiveWorldEvents(r.Context())
	if err != nil {
		h.logger.WithError(err).Error("Failed to get active world events")
		h.respondError(w, http.StatusInternalServerError, "failed to get active world events")
		return
	}

	response := api.WorldEventsListResponse{
		Events: events,
	}
	h.respondJSON(w, http.StatusOK, response)
}

func (h *WorldEventsHandlers) GetWorldEventsByFrequency(w http.ResponseWriter, r *http.Request, frequency api.WorldEventFrequency) {
	events, err := h.service.GetWorldEventsByFrequency(r.Context(), frequency)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get world events by frequency")
		h.respondError(w, http.StatusInternalServerError, "failed to get world events by frequency")
		return
	}

	response := api.WorldEventsListResponse{
		Events: events,
	}
	h.respondJSON(w, http.StatusOK, response)
}

func (h *WorldEventsHandlers) GetWorldEventsByScale(w http.ResponseWriter, r *http.Request, scale api.WorldEventScale) {
	events, err := h.service.GetWorldEventsByScale(r.Context(), scale)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get world events by scale")
		h.respondError(w, http.StatusInternalServerError, "failed to get world events by scale")
		return
	}

	response := api.WorldEventsListResponse{
		Events: events,
	}
	h.respondJSON(w, http.StatusOK, response)
}

func (h *WorldEventsHandlers) GetWorldEventsByType(w http.ResponseWriter, r *http.Request, pType api.WorldEventType) {
	events, err := h.service.GetWorldEventsByType(r.Context(), pType)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get world events by type")
		h.respondError(w, http.StatusInternalServerError, "failed to get world events by type")
		return
	}

	response := api.WorldEventsListResponse{
		Events: events,
	}
	h.respondJSON(w, http.StatusOK, response)
}

func (h *WorldEventsHandlers) GetPlannedWorldEvents(w http.ResponseWriter, r *http.Request) {
	events, err := h.service.GetPlannedWorldEvents(r.Context())
	if err != nil {
		h.logger.WithError(err).Error("Failed to get planned world events")
		h.respondError(w, http.StatusInternalServerError, "failed to get planned world events")
		return
	}

	response := api.WorldEventsListResponse{
		Events: events,
	}
	h.respondJSON(w, http.StatusOK, response)
}

func (h *WorldEventsHandlers) DeleteWorldEvent(w http.ResponseWriter, r *http.Request, id openapi_types.UUID) {
	eventID := uuid.UUID(id)

	err := h.service.DeleteWorldEvent(r.Context(), eventID)
	if err != nil {
		if err.Error() == "world event not found" {
			h.respondError(w, http.StatusNotFound, "world event not found")
			return
		}
		h.logger.WithError(err).Error("Failed to delete world event")
		h.respondError(w, http.StatusInternalServerError, "failed to delete world event")
		return
	}

	h.respondJSON(w, http.StatusNoContent, nil)
}

func (h *WorldEventsHandlers) GetWorldEvent(w http.ResponseWriter, r *http.Request, id openapi_types.UUID) {
	eventID := uuid.UUID(id)

	event, err := h.service.GetWorldEvent(r.Context(), eventID)
	if err != nil {
		if err.Error() == "world event not found" {
			h.respondError(w, http.StatusNotFound, "world event not found")
			return
		}
		h.logger.WithError(err).Error("Failed to get world event")
		h.respondError(w, http.StatusInternalServerError, "failed to get world event")
		return
	}

	h.respondJSON(w, http.StatusOK, event)
}

func (h *WorldEventsHandlers) UpdateWorldEvent(w http.ResponseWriter, r *http.Request, id openapi_types.UUID) {
	eventID := uuid.UUID(id)

	var req api.UpdateWorldEventJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	event, err := h.service.UpdateWorldEvent(r.Context(), eventID, &req)
	if err != nil {
		if err.Error() == "world event not found" {
			h.respondError(w, http.StatusNotFound, "world event not found")
			return
		}
		h.logger.WithError(err).Error("Failed to update world event")
		h.respondError(w, http.StatusInternalServerError, "failed to update world event")
		return
	}

	h.respondJSON(w, http.StatusOK, event)
}

func (h *WorldEventsHandlers) ActivateWorldEvent(w http.ResponseWriter, r *http.Request, id openapi_types.UUID) {
	eventID := uuid.UUID(id)

	event, err := h.service.ActivateWorldEvent(r.Context(), eventID)
	if err != nil {
		if err.Error() == "world event not found" {
			h.respondError(w, http.StatusNotFound, "world event not found")
			return
		}
		h.logger.WithError(err).Error("Failed to activate world event")
		h.respondError(w, http.StatusInternalServerError, "failed to activate world event")
		return
	}

	h.respondJSON(w, http.StatusOK, event)
}

func (h *WorldEventsHandlers) AnnounceWorldEvent(w http.ResponseWriter, r *http.Request, id openapi_types.UUID) {
	eventID := uuid.UUID(id)

	event, err := h.service.AnnounceWorldEvent(r.Context(), eventID)
	if err != nil {
		if err.Error() == "world event not found" {
			h.respondError(w, http.StatusNotFound, "world event not found")
			return
		}
		h.logger.WithError(err).Error("Failed to announce world event")
		h.respondError(w, http.StatusInternalServerError, "failed to announce world event")
		return
	}

	h.respondJSON(w, http.StatusOK, event)
}

func (h *WorldEventsHandlers) DeactivateWorldEvent(w http.ResponseWriter, r *http.Request, id openapi_types.UUID) {
	eventID := uuid.UUID(id)

	event, err := h.service.DeactivateWorldEvent(r.Context(), eventID)
	if err != nil {
		if err.Error() == "world event not found" {
			h.respondError(w, http.StatusNotFound, "world event not found")
			return
		}
		h.logger.WithError(err).Error("Failed to deactivate world event")
		h.respondError(w, http.StatusInternalServerError, "failed to deactivate world event")
		return
	}

	h.respondJSON(w, http.StatusOK, event)
}

func (h *WorldEventsHandlers) GetWorldEventAlerts(w http.ResponseWriter, r *http.Request) {
	alerts, err := h.service.GetWorldEventAlerts(r.Context())
	if err != nil {
		h.logger.WithError(err).Error("Failed to get world event alerts")
		h.respondError(w, http.StatusInternalServerError, "failed to get world event alerts")
		return
	}

	h.respondJSON(w, http.StatusOK, alerts)
}

func (h *WorldEventsHandlers) GetWorldEventEngagement(w http.ResponseWriter, r *http.Request, id openapi_types.UUID) {
	eventID := uuid.UUID(id)

	engagement, err := h.service.GetWorldEventEngagement(r.Context(), eventID)
	if err != nil {
		if err.Error() == "world event not found" {
			h.respondError(w, http.StatusNotFound, "world event not found")
			return
		}
		h.logger.WithError(err).Error("Failed to get world event engagement")
		h.respondError(w, http.StatusInternalServerError, "failed to get world event engagement")
		return
	}

	h.respondJSON(w, http.StatusOK, engagement)
}

func (h *WorldEventsHandlers) GetWorldEventImpact(w http.ResponseWriter, r *http.Request, id openapi_types.UUID) {
	eventID := uuid.UUID(id)

	impact, err := h.service.GetWorldEventImpact(r.Context(), eventID)
	if err != nil {
		if err.Error() == "world event not found" {
			h.respondError(w, http.StatusNotFound, "world event not found")
			return
		}
		h.logger.WithError(err).Error("Failed to get world event impact")
		h.respondError(w, http.StatusInternalServerError, "failed to get world event impact")
		return
	}

	h.respondJSON(w, http.StatusOK, impact)
}

func (h *WorldEventsHandlers) GetWorldEventMetrics(w http.ResponseWriter, r *http.Request, id openapi_types.UUID) {
	eventID := uuid.UUID(id)

	metrics, err := h.service.GetWorldEventMetrics(r.Context(), eventID)
	if err != nil {
		if err.Error() == "world event not found" {
			h.respondError(w, http.StatusNotFound, "world event not found")
			return
		}
		h.logger.WithError(err).Error("Failed to get world event metrics")
		h.respondError(w, http.StatusInternalServerError, "failed to get world event metrics")
		return
	}

	h.respondJSON(w, http.StatusOK, metrics)
}

func (h *WorldEventsHandlers) GetWorldEventsCalendar(w http.ResponseWriter, r *http.Request, params api.GetWorldEventsCalendarParams) {
	calendar, err := h.service.GetWorldEventsCalendar(r.Context(), &params)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get world events calendar")
		h.respondError(w, http.StatusInternalServerError, "failed to get world events calendar")
		return
	}

	h.respondJSON(w, http.StatusOK, calendar)
}

func (h *WorldEventsHandlers) ScheduleWorldEvent(w http.ResponseWriter, r *http.Request) {
	var req api.ScheduleWorldEventJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	scheduled, err := h.service.ScheduleWorldEvent(r.Context(), &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to schedule world event")
		h.respondError(w, http.StatusInternalServerError, "failed to schedule world event")
		return
	}

	h.respondJSON(w, http.StatusCreated, scheduled)
}

func (h *WorldEventsHandlers) GetScheduledWorldEvents(w http.ResponseWriter, r *http.Request) {
	events, err := h.service.GetScheduledWorldEvents(r.Context())
	if err != nil {
		h.logger.WithError(err).Error("Failed to get scheduled world events")
		h.respondError(w, http.StatusInternalServerError, "failed to get scheduled world events")
		return
	}

	response := api.WorldEventsListResponse{
		Events: events,
	}
	h.respondJSON(w, http.StatusOK, response)
}

func (h *WorldEventsHandlers) TriggerScheduledWorldEvent(w http.ResponseWriter, r *http.Request, id openapi_types.UUID) {
	eventID := uuid.UUID(id)

	event, err := h.service.TriggerScheduledWorldEvent(r.Context(), eventID)
	if err != nil {
		if err.Error() == "scheduled world event not found" {
			h.respondError(w, http.StatusNotFound, "scheduled world event not found")
			return
		}
		h.logger.WithError(err).Error("Failed to trigger scheduled world event")
		h.respondError(w, http.StatusInternalServerError, "failed to trigger scheduled world event")
		return
	}

	h.respondJSON(w, http.StatusOK, event)
}

func (h *WorldEventsHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if data != nil {
		if err := json.NewEncoder(w).Encode(data); err != nil {
			h.logger.WithError(err).Error("Failed to write JSON response")
		}
	}
}

func (h *WorldEventsHandlers) respondError(w http.ResponseWriter, status int, message string) {
	h.respondJSON(w, status, map[string]string{"error": message})
}

