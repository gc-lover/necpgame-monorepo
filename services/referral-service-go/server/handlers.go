// Handlers for referral-service - implements api.ServerInterface
package server

import (
	"encoding/json"
	"net/http"

	"github.com/necpgame/referral-service-go/pkg/api"
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

// GetReferralCode implements GET /growth/referral/code
func (h *ServiceHandlers) GetReferralCode(w http.ResponseWriter, r *http.Request, params api.GetReferralCodeParams) {
	// TODO: Implement logic
	respondJSON(w, http.StatusOK, map[string]interface{}{
		"code": "REF123",
		"id":   "00000000-0000-0000-0000-000000000000",
	})
}
