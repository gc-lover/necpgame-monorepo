package server

import (
	"net/http"

	"github.com/necpgame/maintenance-service-go/pkg/api"
)

func (h *MaintenanceHandlers) GetMaintenanceStatus(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	status, err := h.service.GetMaintenanceStatus(ctx)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get maintenance status")
		h.respondError(w, http.StatusInternalServerError, "failed to get maintenance status")
		return
	}

	h.respondJSON(w, http.StatusOK, status)
}

func (h *MaintenanceHandlers) UpdateMaintenanceStatus(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	var req api.UpdateMaintenanceStatusRequest

	if err := h.decodeRequest(r, &req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	status, err := h.service.UpdateMaintenanceStatus(ctx, req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to update maintenance status")
		h.respondError(w, http.StatusInternalServerError, "failed to update maintenance status")
		return
	}

	h.respondJSON(w, http.StatusOK, status)
}

