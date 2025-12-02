// Handlers for companion-service-go - implements api.ServerInterface
package server

import (
    "net/http"
    "github.com/sirupsen/logrus"
)

// ServiceHandlers implements api.ServerInterface
type ServiceHandlers struct {
    logger *logrus.Logger
}

// NewServiceHandlers creates new handlers
func NewServiceHandlers(logger *logrus.Logger) *ServiceHandlers {
    return &ServiceHandlers{logger: logger}
}

// HealthCheck implements GET /health
func (h *ServiceHandlers) HealthCheck(w http.ResponseWriter, r *http.Request) {
    w.Header().Set("Content-Type", "application/json")
    w.WriteHeader(http.StatusOK)
    w.Write([]byte(`{"status":"ok"}`))
}
