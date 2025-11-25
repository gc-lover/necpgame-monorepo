package server

import (
	"net/http"

	"github.com/necpgame/maintenance-service-go/pkg/api"
)

func (h *MaintenanceHandlers) AdminCreateMaintenanceWindow(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	var req api.CreateMaintenanceWindowRequest

	if err := h.decodeRequest(r, &req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	window, err := h.service.CreateMaintenanceWindow(ctx, req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create maintenance window (admin)")
		h.respondError(w, http.StatusInternalServerError, "failed to create maintenance window")
		return
	}

	h.respondJSON(w, http.StatusCreated, window)
}

func (h *MaintenanceHandlers) AdminStartMaintenance(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	var req api.AdminStartMaintenanceRequest

	if err := h.decodeRequest(r, &req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	window, err := h.service.AdminStartMaintenance(ctx, req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to start maintenance (admin)")
		h.respondError(w, http.StatusInternalServerError, "failed to start maintenance")
		return
	}

	h.respondJSON(w, http.StatusOK, window)
}

func (h *MaintenanceHandlers) AdminStopMaintenance(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	var req api.AdminStopMaintenanceRequest

	if err := h.decodeRequest(r, &req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if err := h.service.AdminStopMaintenance(ctx, req); err != nil {
		h.logger.WithError(err).Error("Failed to stop maintenance (admin)")
		h.respondError(w, http.StatusInternalServerError, "failed to stop maintenance")
		return
	}

	response := map[string]string{"status": "stopped"}
	h.respondJSON(w, http.StatusOK, response)
}

