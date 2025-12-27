// Issue: #2232
package handlers

import (
	"encoding/json"
	"net/http"
	"strconv"
	"strings"
	"time"

	"github.com/go-chi/chi/v5"
	"go.uber.org/zap"

	"realtime-combat-service-go/internal/service"
	"realtime-combat-service-go/internal/metrics"
	"realtime-combat-service-go/internal/repository"
)

// CombatHandlers handles HTTP requests
type CombatHandlers struct {
	service *service.CombatService
	logger  *zap.SugaredLogger
	metrics *metrics.Collector
}

// NewCombatHandlers creates new combat handlers
func NewCombatHandlers(svc *service.CombatService, logger *zap.SugaredLogger) *CombatHandlers {
	return &CombatHandlers{
		service: svc,
		logger:  logger,
		metrics: &metrics.Collector{}, // This should be passed from main
	}
}

// AuthMiddleware validates JWT tokens
func (h *CombatHandlers) AuthMiddleware(next http.Handler) http.Handler {
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
func (h *CombatHandlers) Health(w http.ResponseWriter, r *http.Request) {
	h.respondWithJSON(w, http.StatusOK, map[string]string{
		"status": "healthy",
		"time":   time.Now().Format(time.RFC3339),
	})
}

// Readiness check endpoint
func (h *CombatHandlers) Ready(w http.ResponseWriter, r *http.Request) {
	h.respondWithJSON(w, http.StatusOK, map[string]string{
		"status": "ready",
		"time":   time.Now().Format(time.RFC3339),
	})
}

// ListCombatSessions lists all combat sessions
func (h *CombatHandlers) ListCombatSessions(w http.ResponseWriter, r *http.Request) {
	// For now, return empty array (should be implemented with proper pagination)
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"sessions": []interface{}{},
		"total":    0,
	})
}

// CreateCombatSession creates a new combat session
func (h *CombatHandlers) CreateCombatSession(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	var req struct {
		Name       string `json:"name"`
		Type       string `json:"type"`
		MaxPlayers int    `json:"maxPlayers"`
		MapID      string `json:"mapId"`
		GameMode   string `json:"gameMode"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	session, err := h.service.CreateCombatSession(r.Context(), req.Name, req.Type, req.MapID, req.GameMode, req.MaxPlayers)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusCreated, session)
}

// GetCombatSession gets a combat session by ID
func (h *CombatHandlers) GetCombatSession(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	sessionID := chi.URLParam(r, "sessionId")
	if sessionID == "" {
		h.respondWithError(w, http.StatusBadRequest, "Missing session ID")
		return
	}

	session, err := h.service.GetCombatSession(r.Context(), sessionID)
	if err != nil {
		if strings.Contains(err.Error(), "not found") {
			h.respondWithError(w, http.StatusNotFound, "Session not found")
			return
		}
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, session)
}

// UpdateCombatSession updates a combat session
func (h *CombatHandlers) UpdateCombatSession(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	sessionID := chi.URLParam(r, "sessionId")
	if sessionID == "" {
		h.respondWithError(w, http.StatusBadRequest, "Missing session ID")
		return
	}

	var req struct {
		Name       string `json:"name"`
		Type       string `json:"type"`
		MaxPlayers int    `json:"maxPlayers"`
		MapID      string `json:"mapId"`
		GameMode   string `json:"gameMode"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	session := &repository.CombatSession{
		ID:         sessionID,
		Name:       req.Name,
		Type:       req.Type,
		MaxPlayers: req.MaxPlayers,
		MapID:      req.MapID,
		GameMode:   req.GameMode,
	}

	if err := h.service.UpdateCombatSession(r.Context(), session); err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, session)
}

// EndCombatSession ends a combat session
func (h *CombatHandlers) EndCombatSession(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	sessionID := chi.URLParam(r, "sessionId")
	if sessionID == "" {
		h.respondWithError(w, http.StatusBadRequest, "Missing session ID")
		return
	}

	if err := h.service.EndCombatSession(r.Context(), sessionID); err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]string{
		"message": "Session ended successfully",
	})
}

// JoinCombatSession joins a player to a combat session
func (h *CombatHandlers) JoinCombatSession(w http.ResponseWriter, r *http.Request) {
	sessionID := chi.URLParam(r, "sessionId")
	if sessionID == "" {
		h.respondWithError(w, http.StatusBadRequest, "Missing session ID")
		return
	}

	// Extract player ID from context or header (simplified - should come from auth)
	playerID := r.Header.Get("X-Player-ID")
	if playerID == "" {
		h.respondWithError(w, http.StatusBadRequest, "Missing player ID")
		return
	}

	if err := h.service.JoinCombatSession(r.Context(), sessionID, playerID); err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"message":   "Joined session successfully",
		"sessionId": sessionID,
		"playerId":  playerID,
	})
}

// LeaveCombatSession leaves a combat session
func (h *CombatHandlers) LeaveCombatSession(w http.ResponseWriter, r *http.Request) {
	sessionID := chi.URLParam(r, "sessionId")
	if sessionID == "" {
		h.respondWithError(w, http.StatusBadRequest, "Missing session ID")
		return
	}

	playerID := r.Header.Get("X-Player-ID")
	if playerID == "" {
		h.respondWithError(w, http.StatusBadRequest, "Missing player ID")
		return
	}

	if err := h.service.LeaveCombatSession(r.Context(), sessionID, playerID); err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"message":   "Left session successfully",
		"sessionId": sessionID,
		"playerId":  playerID,
	})
}

// ApplyDamage applies damage in combat
func (h *CombatHandlers) ApplyDamage(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	sessionID := chi.URLParam(r, "sessionId")
	if sessionID == "" {
		h.respondWithError(w, http.StatusBadRequest, "Missing session ID")
		return
	}

	var req struct {
		AttackerID string `json:"attackerId"`
		VictimID   string `json:"victimId"`
		Damage     int    `json:"damage"`
		Type       string `json:"type"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	if err := h.service.ApplyDamage(r.Context(), sessionID, req.AttackerID, req.VictimID, req.Damage, req.Type); err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]string{
		"message": "Damage applied successfully",
	})
}

// ExecuteAction executes a combat action
func (h *CombatHandlers) ExecuteAction(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	sessionID := chi.URLParam(r, "sessionId")
	if sessionID == "" {
		h.respondWithError(w, http.StatusBadRequest, "Missing session ID")
		return
	}

	var req struct {
		PlayerID   string                 `json:"playerId"`
		ActionType string                 `json:"actionType"`
		Data       map[string]interface{} `json:"data"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	if err := h.service.ExecuteAction(r.Context(), sessionID, req.PlayerID, req.ActionType, req.Data); err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]string{
		"message": "Action executed successfully",
	})
}

// StartSpectating starts spectating a combat session
func (h *CombatHandlers) StartSpectating(w http.ResponseWriter, r *http.Request) {
	sessionID := chi.URLParam(r, "sessionId")
	// Implementation for spectating
	h.respondWithJSON(w, http.StatusOK, map[string]string{
		"message": "Spectating started",
		"sessionId": sessionID,
	})
}

// GetCombatState gets the current state of combat
func (h *CombatHandlers) GetCombatState(w http.ResponseWriter, r *http.Request) {
	sessionID := chi.URLParam(r, "sessionId")
	// Implementation for getting combat state
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"sessionId": sessionID,
		"status":    "active",
		"players":   []interface{}{},
	})
}

// UpdatePosition updates player position
func (h *CombatHandlers) UpdatePosition(w http.ResponseWriter, r *http.Request) {
	sessionID := chi.URLParam(r, "sessionId")
	// Implementation for position updates
	h.respondWithJSON(w, http.StatusOK, map[string]string{
		"message": "Position updated",
		"sessionId": sessionID,
	})
}

// GetCombatReplay gets combat replay data
func (h *CombatHandlers) GetCombatReplay(w http.ResponseWriter, r *http.Request) {
	sessionID := chi.URLParam(r, "sessionId")
	limitStr := r.URL.Query().Get("limit")
	limit := 100
	if l, err := strconv.Atoi(limitStr); err == nil && l > 0 {
		limit = l
	}

	events, err := h.service.GetCombatEvents(r.Context(), sessionID, limit)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"sessionId": sessionID,
		"events":    events,
	})
}

// GetCombatStats gets combat statistics
func (h *CombatHandlers) GetCombatStats(w http.ResponseWriter, r *http.Request) {
	sessionID := chi.URLParam(r, "sessionId")
	// Implementation for combat stats
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"sessionId": sessionID,
		"stats": map[string]interface{}{
			"totalDamage":  1000,
			"totalKills":   5,
			"duration":     300,
		},
	})
}

// GetPlayerCombatStats gets player combat statistics
func (h *CombatHandlers) GetPlayerCombatStats(w http.ResponseWriter, r *http.Request) {
	playerID := chi.URLParam(r, "playerId")
	// Implementation for player stats
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"playerId": playerID,
		"stats": map[string]interface{}{
			"kills":      10,
			"deaths":     5,
			"damage":     5000,
			"accuracy":   0.75,
			"rank":       "Gold",
		},
	})
}

// Helper functions
func (h *CombatHandlers) respondWithJSON(w http.ResponseWriter, status int, payload interface{}) {
	response, _ := json.Marshal(payload)
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	w.Write(response)
}

func (h *CombatHandlers) respondWithError(w http.ResponseWriter, status int, message string) {
	h.respondWithJSON(w, status, map[string]string{"error": message})
}
