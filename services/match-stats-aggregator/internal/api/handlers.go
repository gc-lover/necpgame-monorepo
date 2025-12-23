// Issue: #2214
// REST API Handlers for Match Statistics
// High-performance HTTP handlers optimized for MMOFPS workloads

package api

import (
	"encoding/json"
	"net/http"
	"strconv"
	"strings"
	"time"

	"github.com/gc-lover/necpgame/services/match-stats-aggregator/internal/aggregator"
	"github.com/gc-lover/necpgame/services/match-stats-aggregator/internal/cache"
	"github.com/gc-lover/necpgame/services/match-stats-aggregator/pkg/models"
	"go.uber.org/zap"
)

// StatisticsHandler handles HTTP requests for match statistics
// Optimized for low latency and high throughput
type StatisticsHandler struct {
	aggregator *aggregator.StatisticsAggregator
	cache      *cache.RedisCache
	logger     *zap.Logger
}

// NewStatisticsHandler creates a new statistics handler
func NewStatisticsHandler(agg *aggregator.StatisticsAggregator, cache *cache.RedisCache, logger *zap.Logger) *StatisticsHandler {
	return &StatisticsHandler{
		aggregator: agg,
		cache:      cache,
		logger:     logger,
	}
}

// GetMatchStatistics handles GET /api/v1/match-stats/matches/{matchID}
// Returns current statistics for a specific match
func (sh *StatisticsHandler) GetMatchStatistics(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() {
		sh.logger.Debug("Handled match statistics request",
			zap.Duration("duration", time.Since(start)))
	}()

	// Extract match ID from URL path
	matchID := extractMatchID(r.URL.Path)
	if matchID == "" {
		sh.writeError(w, http.StatusBadRequest, "invalid match ID")
		return
	}

	// Try cache first for performance
	ctx := r.Context()
	if sh.cache != nil {
		if stats, err := sh.cache.GetMatchStatistics(ctx, matchID); err == nil {
			sh.writeJSON(w, http.StatusOK, stats)
			return
		} else if err != cache.ErrCacheMiss {
			sh.logger.Warn("Cache error", zap.Error(err))
			// Continue to aggregator
		}
	}

	// Get from aggregator
	stats, err := sh.aggregator.GetMatchStatistics(matchID)
	if err != nil {
		if err == aggregator.ErrMatchNotFound {
			sh.writeError(w, http.StatusNotFound, "match not found")
		} else {
			sh.logger.Error("Failed to get match statistics", zap.Error(err))
			sh.writeError(w, http.StatusInternalServerError, "internal server error")
		}
		return
	}

	// Cache the result for future requests
	if sh.cache != nil {
		go func() {
			if err := sh.cache.StoreMatchStatistics(ctx, stats); err != nil {
				sh.logger.Warn("Failed to cache match statistics", zap.Error(err))
			}
		}()
	}

	sh.writeJSON(w, http.StatusOK, stats)
}

// GetActiveMatches handles GET /api/v1/match-stats/matches/active
// Returns statistics for all currently active matches
func (sh *StatisticsHandler) GetActiveMatches(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() {
		sh.logger.Debug("Handled active matches request",
			zap.Duration("duration", time.Since(start)))
	}()

	matches := sh.aggregator.GetAllActiveMatches()

	response := map[string]interface{}{
		"matches": matches,
		"count":   len(matches),
		"timestamp": time.Now(),
	}

	sh.writeJSON(w, http.StatusOK, response)
}

// GetPlayerStatistics handles GET /api/v1/match-stats/matches/{matchID}/players/{playerID}
// Returns statistics for a specific player in a match
func (sh *StatisticsHandler) GetPlayerStatistics(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() {
		sh.logger.Debug("Handled player statistics request",
			zap.Duration("duration", time.Since(start)))
	}()

	matchID, playerID := extractMatchAndPlayerID(r.URL.Path)
	if matchID == "" || playerID == "" {
		sh.writeError(w, http.StatusBadRequest, "invalid match ID or player ID")
		return
	}

	// Try cache first
	ctx := r.Context()
	if sh.cache != nil {
		if stats, err := sh.cache.GetPlayerStatistics(ctx, matchID, playerID); err == nil {
			sh.writeJSON(w, http.StatusOK, stats)
			return
		}
	}

	// Get from aggregator
	matchStats, err := sh.aggregator.GetMatchStatistics(matchID)
	if err != nil {
		if err == aggregator.ErrMatchNotFound {
			sh.writeError(w, http.StatusNotFound, "match not found")
		} else {
			sh.writeError(w, http.StatusInternalServerError, "internal server error")
		}
		return
	}

	// Find player in match
	for _, playerStats := range matchStats.PlayerStats {
		if playerStats.PlayerID == playerID {
			// Cache individual player stats
			if sh.cache != nil {
				go func() {
					if err := sh.cache.StorePlayerStatistics(ctx, matchID, playerID, &playerStats); err != nil {
						sh.logger.Warn("Failed to cache player statistics", zap.Error(err))
					}
				}()
			}

			sh.writeJSON(w, http.StatusOK, playerStats)
			return
		}
	}

	sh.writeError(w, http.StatusNotFound, "player not found in match")
}

// GetLeaderboard handles GET /api/v1/match-stats/matches/{matchID}/leaderboard/{metric}
// Returns top players for a specific metric
func (sh *StatisticsHandler) GetLeaderboard(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() {
		sh.logger.Debug("Handled leaderboard request",
			zap.Duration("duration", time.Since(start)))
	}()

	matchID, metric := extractMatchAndMetric(r.URL.Path)
	if matchID == "" || metric == "" {
		sh.writeError(w, http.StatusBadRequest, "invalid match ID or metric")
		return
	}

	// Parse limit from query parameter
	limitStr := r.URL.Query().Get("limit")
	limit := 10 // default
	if limitStr != "" {
		if parsed, err := strconv.Atoi(limitStr); err == nil && parsed > 0 && parsed <= 100 {
			limit = parsed
		}
	}

	// Get from cache if available
	ctx := r.Context()
	if sh.cache != nil {
		if players, err := sh.cache.GetTopPlayers(ctx, matchID, metric, limit); err == nil && len(players) > 0 {
			response := map[string]interface{}{
				"match_id":  matchID,
				"metric":    metric,
				"limit":     limit,
				"players":   players,
				"count":     len(players),
				"timestamp": time.Now(),
			}
			sh.writeJSON(w, http.StatusOK, response)
			return
		}
	}

	// Fallback to aggregator calculation
	matchStats, err := sh.aggregator.GetMatchStatistics(matchID)
	if err != nil {
		if err == aggregator.ErrMatchNotFound {
			sh.writeError(w, http.StatusNotFound, "match not found")
		} else {
			sh.writeError(w, http.StatusInternalServerError, "internal server error")
		}
		return
	}

	// Calculate leaderboard (simple implementation)
	players := sh.calculateLeaderboard(matchStats.PlayerStats, metric, limit)

	response := map[string]interface{}{
		"match_id":  matchID,
		"metric":    metric,
		"limit":     limit,
		"players":   players,
		"count":     len(players),
		"timestamp": time.Now(),
	}

	sh.writeJSON(w, http.StatusOK, response)
}

// GetSystemStats handles GET /api/v1/match-stats/system/stats
// Returns system-wide statistics and health information
func (sh *StatisticsHandler) GetSystemStats(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() {
		sh.logger.Debug("Handled system stats request",
			zap.Duration("duration", time.Since(start)))
	}()

	aggStats := sh.aggregator.GetStats()

	response := map[string]interface{}{
		"aggregator": aggStats,
		"timestamp":  time.Now(),
		"version":    "1.0.0",
		"status":     "healthy",
	}

	// Add cache health if available
	if sh.cache != nil {
		ctx, cancel := context.WithTimeout(r.Context(), 1*time.Second)
		cacheHealthy := sh.cache.Health(ctx) == nil
		cancel()

		response["cache"] = map[string]interface{}{
			"healthy": cacheHealthy,
		}
	}

	sh.writeJSON(w, http.StatusOK, response)
}

// Utility methods

// calculateLeaderboard creates a leaderboard from player statistics
func (sh *StatisticsHandler) calculateLeaderboard(players []models.PlayerMatchStats, metric string, limit int) []models.PlayerMatchStats {
	if len(players) == 0 {
		return []models.PlayerMatchStats{}
	}

	// Sort players by metric (simple bubble sort for demo)
	sorted := make([]models.PlayerMatchStats, len(players))
	copy(sorted, players)

	for i := 0; i < len(sorted)-1; i++ {
		for j := 0; j < len(sorted)-i-1; j++ {
			if sh.comparePlayers(sorted[j], sorted[j+1], metric) < 0 {
				sorted[j], sorted[j+1] = sorted[j+1], sorted[j]
			}
		}
	}

	// Return top N
	if len(sorted) > limit {
		return sorted[:limit]
	}
	return sorted
}

// comparePlayers compares two players by a specific metric
func (sh *StatisticsHandler) comparePlayers(a, b models.PlayerMatchStats, metric string) int {
	var aVal, bVal float64

	switch metric {
	case "kills":
		aVal, bVal = float64(a.Kills), float64(b.Kills)
	case "deaths":
		aVal, bVal = float64(a.Deaths), float64(b.Deaths)
	case "kd_ratio":
		aVal, bVal = a.KDRatio, b.KDRatio
	case "damage":
		aVal, bVal = float64(a.DamageDealt), float64(b.DamageDealt)
	case "accuracy":
		aVal, bVal = a.Accuracy, b.Accuracy
	case "score":
		aVal, bVal = float64(a.Score), float64(b.Score)
	default:
		aVal, bVal = float64(a.Kills), float64(b.Kills) // default to kills
	}

	if aVal > bVal {
		return 1
	} else if aVal < bVal {
		return -1
	}
	return 0
}

// writeJSON writes a JSON response
func (sh *StatisticsHandler) writeJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)

	if err := json.NewEncoder(w).Encode(data); err != nil {
		sh.logger.Error("Failed to encode JSON response", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
	}
}

// writeError writes an error response
func (sh *StatisticsHandler) writeError(w http.ResponseWriter, status int, message string) {
	response := map[string]interface{}{
		"error":   message,
		"status":  status,
		"timestamp": time.Now(),
	}
	sh.writeJSON(w, status, response)
}

// URL parsing utilities
func extractMatchID(path string) string {
	// Expected format: /api/v1/match-stats/matches/{matchID}
	parts := strings.Split(strings.Trim(path, "/"), "/")
	if len(parts) >= 5 && parts[0] == "api" && parts[1] == "v1" && parts[2] == "match-stats" && parts[3] == "matches" {
		return parts[4]
	}
	return ""
}

func extractMatchAndPlayerID(path string) (string, string) {
	// Expected format: /api/v1/match-stats/matches/{matchID}/players/{playerID}
	parts := strings.Split(strings.Trim(path, "/"), "/")
	if len(parts) >= 7 && parts[0] == "api" && parts[1] == "v1" && parts[2] == "match-stats" &&
		parts[3] == "matches" && parts[5] == "players" {
		return parts[4], parts[6]
	}
	return "", ""
}

func extractMatchAndMetric(path string) (string, string) {
	// Expected format: /api/v1/match-stats/matches/{matchID}/leaderboard/{metric}
	parts := strings.Split(strings.Trim(path, "/"), "/")
	if len(parts) >= 7 && parts[0] == "api" && parts[1] == "v1" && parts[2] == "match-stats" &&
		parts[3] == "matches" && parts[5] == "leaderboard" {
		return parts[4], parts[6]
	}
	return "", ""
}
