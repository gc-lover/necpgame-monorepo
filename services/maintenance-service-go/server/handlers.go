package server

import (
	"encoding/json"
	"net/http"

	"github.com/necpgame/maintenance-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type MaintenanceHandlers struct {
	service MaintenanceService
	logger  *logrus.Logger
}

func NewMaintenanceHandlers(service MaintenanceService, logger *logrus.Logger) *MaintenanceHandlers {
	return &MaintenanceHandlers{
		service: service,
		logger:  logger,
	}
}

func (h *MaintenanceHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if data != nil {
		if err := json.NewEncoder(w).Encode(data); err != nil {
			h.logger.WithError(err).Error("Failed to encode JSON response")
		}
	}
}

func (h *MaintenanceHandlers) respondError(w http.ResponseWriter, status int, message string) {
	statusText := http.StatusText(status)
	errorResponse := api.Error{
		Error:   &statusText,
		Message: &message,
	}
	h.respondJSON(w, status, errorResponse)
}

func (h *MaintenanceHandlers) decodeRequest(r *http.Request, v interface{}) error {
	if err := json.NewDecoder(r.Body).Decode(v); err != nil {
		h.logger.WithError(err).Error("Failed to decode request body")
		return err
	}
	return nil
}
