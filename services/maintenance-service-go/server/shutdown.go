package server

import (
	"net/http"

	"github.com/necpgame/maintenance-service-go/pkg/api"
)

func (h *MaintenanceHandlers) StartGracefulShutdown(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	var req api.StartGracefulShutdownRequest

	if err := h.decodeRequest(r, &req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	status, err := h.service.StartGracefulShutdown(ctx, req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to start graceful shutdown")
		h.respondError(w, http.StatusInternalServerError, "failed to start graceful shutdown")
		return
	}

	h.respondJSON(w, http.StatusOK, status)
}

func (h *MaintenanceHandlers) GetGracefulShutdownStatus(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	status, err := h.service.GetGracefulShutdownStatus(ctx)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get graceful shutdown status")
		h.respondError(w, http.StatusInternalServerError, "failed to get graceful shutdown status")
		return
	}

	h.respondJSON(w, http.StatusOK, status)
}

