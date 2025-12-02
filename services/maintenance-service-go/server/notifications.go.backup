package server

import (
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/maintenance-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

func (h *MaintenanceHandlers) SendMaintenanceNotification(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	var req api.SendMaintenanceNotificationRequest

	if err := h.decodeRequest(r, &req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	recipientsCount, err := h.service.SendMaintenanceNotification(ctx, req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to send maintenance notification")
		h.respondError(w, http.StatusInternalServerError, "failed to send maintenance notification")
		return
	}

	response := map[string]interface{}{
		"status":           "sent",
		"recipients_count": recipientsCount,
	}
	h.respondJSON(w, http.StatusOK, response)
}

func (h *MaintenanceHandlers) GetMaintenanceNotifications(w http.ResponseWriter, r *http.Request, windowID openapi_types.UUID) {
	ctx := r.Context()

	notifications, err := h.service.GetMaintenanceNotifications(ctx, uuid.UUID(windowID))
	if err != nil {
		h.logger.WithError(err).Error("Failed to get maintenance notifications")
		h.respondError(w, http.StatusNotFound, "notifications not found")
		return
	}

	h.respondJSON(w, http.StatusOK, notifications)
}

func (h *MaintenanceHandlers) GetMaintenanceEvents(w http.ResponseWriter, r *http.Request, windowID openapi_types.UUID, params api.GetMaintenanceEventsParams) {
	ctx := r.Context()

	limit := 20
	if params.Limit != nil {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil {
		offset = *params.Offset
	}

	events, err := h.service.GetMaintenanceEvents(ctx, uuid.UUID(windowID), limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get maintenance events")
		h.respondError(w, http.StatusNotFound, "events not found")
		return
	}

	h.respondJSON(w, http.StatusOK, events)
}

