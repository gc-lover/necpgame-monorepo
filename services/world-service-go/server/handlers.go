// Handlers for world-service - implements api.ServerInterface
package server

import (
	"encoding/json"
	"net/http"

	"github.com/necpgame/world-service-go/pkg/api"
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

// Helper
func respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

// ListContinents implements GET /world/continents
func (h *ServiceHandlers) ListContinents(w http.ResponseWriter, r *http.Request, params api.ListContinentsParams) {
	// TODO: Implement logic
	respondJSON(w, http.StatusOK, []interface{}{})
}

// CreateContinent implements POST /world/continents
func (h *ServiceHandlers) CreateContinent(w http.ResponseWriter, r *http.Request) {
	// TODO: Implement logic
	respondJSON(w, http.StatusCreated, map[string]interface{}{"id": "00000000-0000-0000-0000-000000000000"})
}
