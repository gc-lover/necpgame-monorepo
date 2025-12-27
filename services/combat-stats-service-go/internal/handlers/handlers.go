// Issue: #2250
package handlers

import (
	"encoding/json"
	"net/http"
	"strconv"
	"strings"
	"time"

	"github.com/go-chi/chi/v5"
	"go.uber.org/zap"

	"combat-stats-service-go/internal/service"
	"combat-stats-service-go/internal/metrics"
)

// CombatStatsHandlers handles HTTP requests
type CombatStatsHandlers struct {
	service *service.CombatStatsService
	logger  *zap.SugaredLogger
	metrics *metrics.Collector
}

// NewCombatStatsHandlers creates new combat stats handlers
func NewCombatStatsHandlers(svc *service.CombatStatsService, logger *zap.SugaredLogger) *CombatStatsHandlers {
	return &CombatStatsHandlers{
		service: svc,
		logger:  logger,
		metrics: &metrics.Collector{}, // This should be passed from main
	}
}

// AuthMiddleware validates JWT tokens
func (h *CombatStatsHandlers) AuthMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			h.respondWithError(w, http.StatusUnauthorized, "Missing authorization header")
			return
		}

		// Simple token validation (should be replaced with proper JWT validation)
		if !strings.HasPrefix(authHeader, "Bearer ") {
			h.respondWithError(w, http.StatusUnauthorized, "Invalid authorization format")
			return
		}

		// For now, just check if token is not empty
		token := strings.TrimPrefix(authHeader, "Bearer ")
		if token == "" {
			h.respondWithError(w, http.StatusUnauthorized, "Empty token")
			return
		}

		next.ServeHTTP(w, r)
	})
}

// Health check endpoint
func (h *CombatStatsHandlers) Health(w http.ResponseWriter, r *http.Request) {
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"status":      "healthy",
		"service":     "combat-stats-service",
		"version":     "1.0.0",
		"timestamp":   time.Now(),
		"description": "Real-time combat statistics aggregation service",
	})
}

// Readiness check endpoint
func (h *CombatStatsHandlers) Ready(w http.ResponseWriter, r *http.Request) {
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"status":    "ready",
		"timestamp": time.Now(),
	})
}

// GetPlayerStats gets player combat statistics
func (h *CombatStatsHandlers) GetPlayerStats(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	playerID := chi.URLParam(r, "playerId")
	if playerID == "" {
		h.respondWithError(w, http.StatusBadRequest, "Missing player ID")
		return
	}

	stats, err := h.service.GetPlayerStats(r.Context(), playerID)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, stats)
}

// GetWeaponStats gets weapon-specific statistics
func (h *CombatStatsHandlers) GetWeaponStats(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	weaponID := chi.URLParam(r, "weaponId")
	if weaponID == "" {
		h.respondWithError(w, http.StatusBadRequest, "Missing weapon ID")
		return
	}

	stats, err := h.service.GetWeaponStats(r.Context(), weaponID)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, stats)
}

// GetMatchStats gets statistics for a specific match
func (h *CombatStatsHandlers) GetMatchStats(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	matchID := chi.URLParam(r, "matchId")
	if matchID == "" {
		h.respondWithError(w, http.StatusBadRequest, "Missing match ID")
		return
	}

	stats, err := h.service.GetMatchStats(r.Context(), matchID)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"matchId": matchID,
		"stats":   stats,
		"count":   len(stats),
	})
}

// RecordCombatEvent records a combat event
func (h *CombatStatsHandlers) RecordCombatEvent(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	var event struct {
		EventID   string                 `json:"eventId"`
		EventType string                 `json:"eventType"`
		PlayerID  string                 `json:"playerId"`
		TargetID  string                 `json:"targetId,omitempty"`
		WeaponID  string                 `json:"weaponId,omitempty"`
		Damage    int                    `json:"damage,omitempty"`
		MatchID   string                 `json:"matchId,omitempty"`
		Position  map[string]float64     `json:"position,omitempty"`
		Metadata  map[string]interface{} `json:"metadata,omitempty"`
	}

	if err := json.NewDecoder(r.Body).Decode(&event); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	combatEvent := &service.CombatEvent{
		EventID:   event.EventID,
		EventType: event.EventType,
		PlayerID:  event.PlayerID,
		TargetID:  event.TargetID,
		WeaponID:  event.WeaponID,
		Damage:    event.Damage,
		Timestamp: time.Now(),
		MatchID:   event.MatchID,
		Position:  event.Position,
		Metadata:  event.Metadata,
	}

	if err := h.service.RecordCombatEvent(r.Context(), combatEvent); err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusAccepted, map[string]interface{}{
		"eventId":   event.EventID,
		"status":    "recorded",
		"timestamp": time.Now(),
	})
}

// GetKillLeaderboard gets kill leaderboard
func (h *CombatStatsHandlers) GetKillLeaderboard(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	limitStr := r.URL.Query().Get("limit")
	limit := 10
	if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
		limit = l
	}

	leaderboard, err := h.service.GetKillLeaderboard(r.Context(), limit)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"leaderboard": leaderboard,
		"type":        "kills",
		"limit":       limit,
		"count":       len(leaderboard),
	})
}

// GetScoreLeaderboard gets score leaderboard
func (h *CombatStatsHandlers) GetScoreLeaderboard(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	limitStr := r.URL.Query().Get("limit")
	limit := 10
	if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
		limit = l
	}

	leaderboard, err := h.service.GetScoreLeaderboard(r.Context(), limit)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"leaderboard": leaderboard,
		"type":        "score",
		"limit":       limit,
		"count":       len(leaderboard),
	})
}

// GetWeaponLeaderboard gets weapon leaderboard
func (h *CombatStatsHandlers) GetWeaponLeaderboard(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	weaponID := chi.URLParam(r, "weaponId")
	limitStr := r.URL.Query().Get("limit")
	limit := 10
	if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
		limit = l
	}

	leaderboard, err := h.service.GetWeaponLeaderboard(r.Context(), weaponID, limit)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"leaderboard": leaderboard,
		"weaponType":  weaponID,
		"limit":       limit,
		"count":       len(leaderboard),
	})
}

// GetDamageAnalytics gets damage analytics
func (h *CombatStatsHandlers) GetDamageAnalytics(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	hoursStr := r.URL.Query().Get("hours")
	hours := 24
	if h, err := strconv.Atoi(hoursStr); err == nil && h > 0 && h <= 168 { // max 1 week
		hours = h
	}

	analytics, err := h.service.GetDamageAnalytics(r.Context(), hours)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, analytics)
}

// GetKillDeathAnalytics gets K/D analytics
func (h *CombatStatsHandlers) GetKillDeathAnalytics(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	hoursStr := r.URL.Query().Get("hours")
	hours := 24
	if h, err := strconv.Atoi(hoursStr); err == nil && h > 0 && h <= 168 {
		hours = h
	}

	analytics, err := h.service.GetKillDeathAnalytics(r.Context(), hours)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, analytics)
}

// GetPlaytimeAnalytics gets playtime analytics
func (h *CombatStatsHandlers) GetPlaytimeAnalytics(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	hoursStr := r.URL.Query().Get("hours")
	hours := 24
	if h, err := strconv.Atoi(hoursStr); err == nil && h > 0 && h <= 168 {
		hours = h
	}

	analytics, err := h.service.GetPlaytimeAnalytics(r.Context(), hours)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, analytics)
}

// GetPlayerPerformance gets detailed player performance metrics
func (h *CombatStatsHandlers) GetPlayerPerformance(w http.ResponseWriter, r *http.Request) {
	playerID := chi.URLParam(r, "playerId")
	// Implementation for detailed player performance
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"playerId":    playerID,
		"performance": map[string]interface{}{
			"skillRating":     1850,
			"consistency":     0.85,
			"improvementRate": 0.12,
			"peakPerformance": time.Now().Add(-time.Hour),
		},
	})
}

// GetWeaponPerformance gets weapon performance analytics
func (h *CombatStatsHandlers) GetWeaponPerformance(w http.ResponseWriter, r *http.Request) {
	weaponID := chi.URLParam(r, "weaponId")
	// Implementation for weapon performance analytics
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"weaponId":    weaponID,
		"performance": map[string]interface{}{
			"popularity":     0.75,
			"effectiveness":  0.82,
			"usageTrend":     "increasing",
			"metaRelevance":  "high",
		},
	})
}

// GetMatchPerformance gets match performance analytics
func (h *CombatStatsHandlers) GetMatchPerformance(w http.ResponseWriter, r *http.Request) {
	matchID := chi.URLParam(r, "matchId")
	// Implementation for match performance analytics
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"matchId":     matchID,
		"performance": map[string]interface{}{
			"intensity":       8.5,
			"balance":         0.92,
			"completionRate":  0.95,
			"engagementScore": 4.2,
		},
	})
}

// Helper functions
func (h *CombatStatsHandlers) respondWithJSON(w http.ResponseWriter, status int, payload interface{}) {
	response, _ := json.Marshal(payload)
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	w.Write(response)
}

func (h *CombatStatsHandlers) respondWithError(w http.ResponseWriter, status int, message string) {
	h.respondWithJSON(w, status, map[string]string{"error": message})
}
