// Issue: #142109955
package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/gameplay-service-go/pkg/implantsmaintenanceapi"
	"github.com/sirupsen/logrus"
)

type ImplantsMaintenanceHandlers struct {
	service ImplantsMaintenanceServiceInterface
	logger  *logrus.Logger
}

func NewImplantsMaintenanceHandlers(service ImplantsMaintenanceServiceInterface) *ImplantsMaintenanceHandlers {
	return &ImplantsMaintenanceHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *ImplantsMaintenanceHandlers) RepairImplant(w http.ResponseWriter, r *http.Request) {
	var req implantsmaintenanceapi.RepairImplantJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	implantID := uuid.UUID(req.ImplantId)
	if implantID == uuid.Nil {
		h.respondError(w, http.StatusBadRequest, "implant_id is required")
		return
	}

	result, err := h.service.RepairImplant(r.Context(), implantID, string(req.RepairType))
	if err != nil {
		h.logger.WithError(err).Error("Failed to repair implant")
		h.respondError(w, http.StatusInternalServerError, "failed to repair implant")
		return
	}

	h.respondJSON(w, http.StatusOK, result)
}

func (h *ImplantsMaintenanceHandlers) UpgradeImplant(w http.ResponseWriter, r *http.Request) {
	var req implantsmaintenanceapi.UpgradeImplantJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	implantID := uuid.UUID(req.ImplantId)
	if implantID == uuid.Nil {
		h.respondError(w, http.StatusBadRequest, "implant_id is required")
		return
	}

	var components []Component
	if req.Components != nil {
		components = make([]Component, len(*req.Components))
		for i, comp := range *req.Components {
			if comp.ComponentId != nil && comp.Quantity != nil {
				components[i] = Component{
					ComponentID: uuid.UUID(*comp.ComponentId),
					Quantity:    *comp.Quantity,
				}
			}
		}
	}

	result, err := h.service.UpgradeImplant(r.Context(), implantID, components)
	if err != nil {
		h.logger.WithError(err).Error("Failed to upgrade implant")
		h.respondError(w, http.StatusInternalServerError, "failed to upgrade implant")
		return
	}

	h.respondJSON(w, http.StatusOK, result)
}

func (h *ImplantsMaintenanceHandlers) ModifyImplant(w http.ResponseWriter, r *http.Request) {
	var req implantsmaintenanceapi.ModifyImplantJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	implantID := uuid.UUID(req.ImplantId)
	if implantID == uuid.Nil {
		h.respondError(w, http.StatusBadRequest, "implant_id is required")
		return
	}

	modificationID := uuid.UUID(req.ModificationId)
	if modificationID == uuid.Nil {
		h.respondError(w, http.StatusBadRequest, "modification_id is required")
		return
	}

	result, err := h.service.ModifyImplant(r.Context(), implantID, modificationID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to modify implant")
		h.respondError(w, http.StatusInternalServerError, "failed to modify implant")
		return
	}

	h.respondJSON(w, http.StatusOK, result)
}

func (h *ImplantsMaintenanceHandlers) GetVisuals(w http.ResponseWriter, r *http.Request) {
	settings, err := h.service.GetVisuals(r.Context())
	if err != nil {
		h.logger.WithError(err).Error("Failed to get visuals")
		h.respondError(w, http.StatusInternalServerError, "failed to get visuals")
		return
	}

	h.respondJSON(w, http.StatusOK, settings)
}

func (h *ImplantsMaintenanceHandlers) CustomizeVisuals(w http.ResponseWriter, r *http.Request) {
	var req implantsmaintenanceapi.CustomizeVisualsJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	implantID := uuid.UUID(req.ImplantId)
	if implantID == uuid.Nil {
		h.respondError(w, http.StatusBadRequest, "implant_id is required")
		return
	}

	var visibilityMode *string
	if req.VisibilityMode != nil {
		mode := string(*req.VisibilityMode)
		visibilityMode = &mode
	}

	result, err := h.service.CustomizeVisuals(r.Context(), implantID, visibilityMode, req.ColorScheme)
	if err != nil {
		h.logger.WithError(err).Error("Failed to customize visuals")
		h.respondError(w, http.StatusInternalServerError, "failed to customize visuals")
		return
	}

	h.respondJSON(w, http.StatusOK, result)
}

func (h *ImplantsMaintenanceHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *ImplantsMaintenanceHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := implantsmaintenanceapi.Error{
		Error:   http.StatusText(status),
		Message: message,
	}
	h.respondJSON(w, status, errorResponse)
}

