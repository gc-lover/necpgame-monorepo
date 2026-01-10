package handlers

import (
	"encoding/json"
	"net/http"
	"strings"

	"go.uber.org/zap"

	"battle-pass-service-go/internal/services"
)

// AnalyticsHandler handles analytics-related HTTP requests
type AnalyticsHandler struct {
	analyticsService *services.AnalyticsService
	logger           *zap.Logger
}

// NewAnalyticsHandler creates a new AnalyticsHandler
func NewAnalyticsHandler(analyticsService *services.AnalyticsService, logger *zap.Logger) *AnalyticsHandler {
	return &AnalyticsHandler{
		analyticsService: analyticsService,
		logger:           logger,
	}
}

// GetPlayerStatistics handles GET /statistics/player/{playerId}
func (h *AnalyticsHandler) GetPlayerStatistics(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodGet {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	// Extract player ID from URL path
	pathParts := strings.Split(strings.Trim(r.URL.Path, "/"), "/")
	if len(pathParts) < 3 {
		http.Error(w, "Invalid URL path", http.StatusBadRequest)
		return
	}

	playerID := pathParts[2] // statistics/player/{playerId}

	stats, err := h.analyticsService.GetPlayerStatistics(playerID)
	if err != nil {
		h.logger.Error("Failed to get player statistics",
			zap.String("playerID", playerID), zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	h.respondJSON(w, http.StatusOK, stats)
}

// GetGlobalStatistics handles GET /statistics/global
func (h *AnalyticsHandler) GetGlobalStatistics(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodGet {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	stats, err := h.analyticsService.GetGlobalStats()
	if err != nil {
		h.logger.Error("Failed to get global statistics", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	h.respondJSON(w, http.StatusOK, stats)
}

// respondJSON sends a JSON response
func (h *AnalyticsHandler) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)

	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.Error("Failed to encode JSON response", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
	}
}