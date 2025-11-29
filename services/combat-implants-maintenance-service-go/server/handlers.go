package server

import (
	"encoding/json"
	"net/http"

	"github.com/necpgame/combat-implants-maintenance-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type MaintenanceHandlers struct {
	logger *logrus.Logger
}

func NewMaintenanceHandlers() *MaintenanceHandlers {
	return &MaintenanceHandlers{
		logger: GetLogger(),
	}
}

func (h *MaintenanceHandlers) ModifyImplant(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	var req api.ModifyRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode ModifyImplant request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"implant_id":      req.ImplantId,
		"modification_id": req.ModificationId,
	}).Info("ModifyImplant request")

	success := true
	response := api.ModifyResult{
		Success: &success,
		AppliedModifications: &[]struct {
			Description    *string             `json:"description,omitempty"`
			ModificationId *openapi_types.UUID `json:"modification_id,omitempty"`
			Name           *string             `json:"name,omitempty"`
		}{},
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *MaintenanceHandlers) RepairImplant(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	var req api.RepairRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode RepairImplant request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"implant_id": req.ImplantId,
		"repair_type": req.RepairType,
	}).Info("RepairImplant request")

	success := true
	durability := float32(100.0)
	response := api.RepairResult{
		Success:   &success,
		Durability: &durability,
		Cost: &struct {
			Amount   *int    `json:"amount,omitempty"`
			Currency *string `json:"currency,omitempty"`
		}{},
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *MaintenanceHandlers) UpgradeImplant(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	var req api.UpgradeRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode UpgradeImplant request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	componentsCount := 0
	if req.Components != nil {
		componentsCount = len(*req.Components)
	}
	h.logger.WithFields(logrus.Fields{
		"implant_id": req.ImplantId,
		"components": componentsCount,
	}).Info("UpgradeImplant request")

	success := true
	newLevel := 1
	response := api.UpgradeResult{
		Success:  &success,
		NewLevel: &newLevel,
		NewStats: &map[string]interface{}{},
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *MaintenanceHandlers) GetVisuals(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	h.logger.Info("GetVisuals request")

	visibilityMode := api.VisualsSettingsVisibilityModeFull
	response := api.VisualsSettings{
		VisibilityMode: &visibilityMode,
		ColorScheme:    new(string),
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *MaintenanceHandlers) CustomizeVisuals(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	var req api.CustomizeVisualsRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode CustomizeVisuals request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"implant_id":      req.ImplantId,
		"visibility_mode": req.VisibilityMode,
		"color_scheme":    req.ColorScheme,
	}).Info("CustomizeVisuals request")

	success := true
	response := api.CustomizeVisualsResult{
		Success: &success,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *MaintenanceHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *MaintenanceHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := api.Error{
		Error:   http.StatusText(status),
		Message: message,
	}
	h.respondJSON(w, status, errorResponse)
}

