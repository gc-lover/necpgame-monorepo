// Issue: #44
package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/world-events-analytics-service-go/pkg/api"
)

type Handlers struct {
	service Service
	logger  *zap.Logger
}

func NewHandlers(service Service, logger *zap.Logger) *Handlers {
	return &Handlers{service: service, logger: logger}
}

func (h *Handlers) GetWorldEventMetrics(w http.ResponseWriter, r *http.Request, id openapi_types.UUID) {
	eventID, err := uuid.Parse(id.String())
	if err != nil {
		respondError(w, http.StatusBadRequest, "Invalid event ID", h.logger)
		return
	}

	metrics, err := h.service.GetEventMetrics(r.Context(), eventID)
	if err != nil || metrics == nil {
		respondError(w, http.StatusNotFound, "Metrics not found", h.logger)
		return
	}

	// Convert to API format
	playerCount := metrics.ParticipantCount
	engagementRate := float32(metrics.PlayerEngagement)
	economicImpact := float32(0.0)
	socialImpact := float32(0.0)

	response := api.WorldEventMetrics{
		EventId:        openapi_types.UUID(metrics.EventID),
		PlayerCount:    &playerCount,
		EngagementRate: &engagementRate,
		EconomicImpact: &economicImpact,
		SocialImpact:   &socialImpact,
		Uptime:         "0h",
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) GetWorldEventEngagement(w http.ResponseWriter, r *http.Request, id openapi_types.UUID) {
	// Simplified engagement metrics
	response := map[string]interface{}{
		"event_id":         id.String(),
		"engagement_rate":  0.0,
		"active_players":   0,
		"completion_rate":  0.0,
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) GetWorldEventImpact(w http.ResponseWriter, r *http.Request, id openapi_types.UUID) {
	// Simplified impact metrics
	response := map[string]interface{}{
		"event_id":        id.String(),
		"economic_impact": 0.0,
		"social_impact":   0.0,
		"gameplay_impact": 0.0,
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) GetWorldEventAlerts(w http.ResponseWriter, r *http.Request) {
	// Simplified alerts - empty list
	response := []interface{}{}
	respondJSON(w, http.StatusOK, response)
}

func respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func respondError(w http.ResponseWriter, status int, message string, logger *zap.Logger) {
	logger.Error("Request error", zap.String("message", message))
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(map[string]string{"error": message})
}


