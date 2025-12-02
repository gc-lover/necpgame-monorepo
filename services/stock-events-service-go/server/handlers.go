// Handlers for stock-events-service - implements api.ServerInterface
package server

import (
	"net/http"
	"github.com/sirupsen/logrus"
)

// StockHandlers implements api.ServerInterface
type StockHandlers struct {
	logger *logrus.Logger
}

// NewStockHandlers creates new handlers
func NewStockHandlers(logger *logrus.Logger) *StockHandlers {
	return &StockHandlers{logger: logger}
}

// HealthCheck implements GET /health
func (h *StockHandlers) HealthCheck(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"ok"}`))
}
