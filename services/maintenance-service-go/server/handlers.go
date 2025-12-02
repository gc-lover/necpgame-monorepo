// Handlers for maintenance-service - implements api.ServerInterface
package server

import (
	"net/http"

	"github.com/sirupsen/logrus"
)

// MaintenanceHandlers implements api.ServerInterface
type MaintenanceHandlers struct {
	logger *logrus.Logger
}

// NewMaintenanceHandlers creates new handlers
func NewMaintenanceHandlers(logger *logrus.Logger) *MaintenanceHandlers {
	return &MaintenanceHandlers{
		logger: logger,
	}
}

// HealthCheck implements GET /health
func (h *MaintenanceHandlers) HealthCheck(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"ok"}`))
}
