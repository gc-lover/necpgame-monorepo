package server

import (
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/maintenance-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

func (h *MaintenanceHandlers) CreateMaintenanceWindow(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	var req api.CreateMaintenanceWindowRequest

	if err := h.decodeRequest(r, &req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	window, err := h.service.CreateMaintenanceWindow(ctx, req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create maintenance window")
		h.respondError(w, http.StatusInternalServerError, "failed to create maintenance window")
		return
	}

	h.respondJSON(w, http.StatusCreated, window)
}

func (h *MaintenanceHandlers) ListMaintenanceWindows(w http.ResponseWriter, r *http.Request, params api.ListMaintenanceWindowsParams) {
	ctx := r.Context()

	var maintenanceType *string
	if params.MaintenanceType != nil {
		mt := string(*params.MaintenanceType)
		maintenanceType = &mt
	}

	var status *string
	if params.Status != nil {
		s := string(*params.Status)
		status = &s
	}

	limit := 20
	if params.Limit != nil {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil {
		offset = *params.Offset
	}

	response, err := h.service.ListMaintenanceWindows(ctx, maintenanceType, status, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to list maintenance windows")
		h.respondError(w, http.StatusInternalServerError, "failed to list maintenance windows")
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *MaintenanceHandlers) GetMaintenanceWindow(w http.ResponseWriter, r *http.Request, windowID openapi_types.UUID) {
	ctx := r.Context()

	window, err := h.service.GetMaintenanceWindow(ctx, uuid.UUID(windowID))
	if err != nil {
		h.logger.WithError(err).Error("Failed to get maintenance window")
		h.respondError(w, http.StatusNotFound, "maintenance window not found")
		return
	}

	h.respondJSON(w, http.StatusOK, window)
}

func (h *MaintenanceHandlers) UpdateMaintenanceWindow(w http.ResponseWriter, r *http.Request, windowID openapi_types.UUID) {
	ctx := r.Context()
	var req api.UpdateMaintenanceWindowRequest

	if err := h.decodeRequest(r, &req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	window, err := h.service.UpdateMaintenanceWindow(ctx, uuid.UUID(windowID), req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to update maintenance window")
		h.respondError(w, http.StatusInternalServerError, "failed to update maintenance window")
		return
	}

	h.respondJSON(w, http.StatusOK, window)
}

func (h *MaintenanceHandlers) CancelMaintenanceWindow(w http.ResponseWriter, r *http.Request, windowID openapi_types.UUID) {
	ctx := r.Context()

	if err := h.service.CancelMaintenanceWindow(ctx, uuid.UUID(windowID)); err != nil {
		h.logger.WithError(err).Error("Failed to cancel maintenance window")
		h.respondError(w, http.StatusInternalServerError, "failed to cancel maintenance window")
		return
	}

	response := map[string]string{"status": "cancelled"}
	h.respondJSON(w, http.StatusOK, response)
}

func (h *MaintenanceHandlers) ScheduleMaintenance(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	var req api.ScheduleMaintenanceRequest

	if err := h.decodeRequest(r, &req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	window, err := h.service.ScheduleMaintenance(ctx, req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to schedule maintenance")
		h.respondError(w, http.StatusInternalServerError, "failed to schedule maintenance")
		return
	}

	h.respondJSON(w, http.StatusCreated, window)
}

func (h *MaintenanceHandlers) GetNextMaintenance(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	window, err := h.service.GetNextMaintenance(ctx)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get next maintenance")
		h.respondError(w, http.StatusNotFound, "no scheduled maintenance found")
		return
	}

	h.respondJSON(w, http.StatusOK, window)
}
