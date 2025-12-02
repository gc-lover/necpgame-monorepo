// Handlers for leaderboard-service - implements api.ServerInterface
package server

import (
	"encoding/json"
	"net/http"

	"github.com/necpgame/leaderboard-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
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

// Helper functions
func respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

// GetGlobalLeaderboard implements GET /api/v1/leaderboard/global
func (h *ServiceHandlers) GetGlobalLeaderboard(w http.ResponseWriter, r *http.Request, params api.GetGlobalLeaderboardParams) {
	// TODO: Implement logic
	respondJSON(w, http.StatusOK, map[string]interface{}{
		"entries":    []interface{}{},
		"pagination": map[string]int{"total": 0, "limit": 100, "offset": 0},
	})
}

// GetFactionLeaderboard implements GET /api/v1/leaderboard/faction/{faction_id}
func (h *ServiceHandlers) GetFactionLeaderboard(w http.ResponseWriter, r *http.Request, factionId openapi_types.UUID, params api.GetFactionLeaderboardParams) {
	// TODO: Implement logic
	respondJSON(w, http.StatusOK, map[string]interface{}{
		"entries":    []interface{}{},
		"pagination": map[string]int{"total": 0, "limit": 100, "offset": 0},
	})
}

// GetPlayerRank implements GET /api/v1/leaderboard/player/{player_id}/rank
func (h *ServiceHandlers) GetPlayerRank(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID) {
	// TODO: Implement logic
	respondJSON(w, http.StatusOK, map[string]interface{}{
		"player_id":    playerId.String(),
		"global_rank":  0,
		"faction_rank": 0,
		"score":        0,
		"percentile":   0.0,
	})
}
