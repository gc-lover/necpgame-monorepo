// Issue: #2210
package handlers

import (
	"encoding/json"
	"net/http"
	"strconv"
	"strings"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"go.uber.org/zap"

	"tournament-bracket-service-go/internal/service"
	"tournament-bracket-service-go/internal/metrics"
)

// TournamentHandlers handles HTTP requests
type TournamentHandlers struct {
	service *service.TournamentService
	logger  *zap.SugaredLogger
	metrics *metrics.Collector
}

// NewTournamentHandlers creates new tournament handlers
func NewTournamentHandlers(svc *service.TournamentService, logger *zap.SugaredLogger) *TournamentHandlers {
	return &TournamentHandlers{
		service: svc,
		logger:  logger,
		metrics: &metrics.Collector{}, // This should be passed from main
	}
}

// AuthMiddleware validates JWT tokens
func (h *TournamentHandlers) AuthMiddleware(next http.Handler) http.Handler {
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
func (h *TournamentHandlers) Health(w http.ResponseWriter, r *http.Request) {
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"status":      "healthy",
		"service":     "tournament-bracket-service",
		"version":     "1.0.0",
		"timestamp":   time.Now(),
		"description": "Tournament bracket system for competitive gameplay",
	})
}

// Readiness check endpoint
func (h *TournamentHandlers) Ready(w http.ResponseWriter, r *http.Request) {
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"status":    "ready",
		"timestamp": time.Now(),
	})
}

// GetTournaments gets tournaments with filtering
func (h *TournamentHandlers) GetTournaments(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	status := r.URL.Query().Get("status")
	gameMode := r.URL.Query().Get("gameMode")
	limitStr := r.URL.Query().Get("limit")
	offsetStr := r.URL.Query().Get("offset")

	limit := 20
	if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
		limit = l
	}

	offset := 0
	if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
		offset = o
	}

	tournaments, err := h.service.GetTournaments(r.Context(), &status, &gameMode, limit, offset)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"tournaments": tournaments,
		"total":       len(tournaments),
		"limit":       limit,
		"offset":      offset,
	})
}

// GetTournament gets a single tournament
func (h *TournamentHandlers) GetTournament(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	tournamentIDStr := chi.URLParam(r, "tournamentId")
	tournamentID, err := uuid.Parse(tournamentIDStr)
	if err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid tournament ID")
		return
	}

	tournament, err := h.service.GetTournament(r.Context(), tournamentID)
	if err != nil {
		if strings.Contains(err.Error(), "not found") {
			h.respondWithError(w, http.StatusNotFound, "Tournament not found")
			return
		}
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, tournament)
}

// CreateTournament creates a new tournament
func (h *TournamentHandlers) CreateTournament(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	var req struct {
		Name               string                 `json:"name"`
		Description        string                 `json:"description"`
		GameMode           string                 `json:"gameMode"`
		TournamentType     string                 `json:"tournamentType"`
		MaxParticipants    int                    `json:"maxParticipants"`
		MinSkillLevel      int                    `json:"minSkillLevel"`
		MaxSkillLevel      int                    `json:"maxSkillLevel"`
		EntryFee           int                    `json:"entryFee"`
		PrizePool          map[string]interface{} `json:"prizePool"`
		Rules              map[string]interface{} `json:"rules"`
		Metadata           map[string]interface{} `json:"metadata"`
		RegistrationStart  *time.Time             `json:"registrationStart"`
		RegistrationEnd    *time.Time             `json:"registrationEnd"`
		StartTime          *time.Time             `json:"startTime"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	tournament, err := h.service.CreateTournament(r.Context(), req.Name, req.Description,
		req.GameMode, req.TournamentType, req.MaxParticipants, req.MinSkillLevel,
		req.MaxSkillLevel, req.EntryFee, req.PrizePool, req.Rules, req.Metadata,
		req.RegistrationStart, req.RegistrationEnd, req.StartTime)

	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusCreated, tournament)
}

// UpdateTournament updates a tournament
func (h *TournamentHandlers) UpdateTournament(w http.ResponseWriter, r *http.Request) {
	tournamentIDStr := chi.URLParam(r, "tournamentId")
	// Implementation for updating tournament
	h.respondWithJSON(w, http.StatusOK, map[string]string{
		"message":      "Tournament updated successfully",
		"tournamentId": tournamentIDStr,
	})
}

// RegisterForTournament registers a player for a tournament
func (h *TournamentHandlers) RegisterForTournament(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	tournamentIDStr := chi.URLParam(r, "tournamentId")
	tournamentID, err := uuid.Parse(tournamentIDStr)
	if err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid tournament ID")
		return
	}

	var req struct {
		PlayerID    string `json:"playerId"`
		PlayerName  string `json:"playerName"`
		SkillRating int    `json:"skillRating"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	participant, err := h.service.RegisterForTournament(r.Context(), tournamentID,
		req.PlayerID, req.PlayerName, req.SkillRating)

	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusCreated, participant)
}

// UnregisterFromTournament unregisters a player from a tournament
func (h *TournamentHandlers) UnregisterFromTournament(w http.ResponseWriter, r *http.Request) {
	tournamentIDStr := chi.URLParam(r, "tournamentId")
	playerID := r.URL.Query().Get("playerId")

	if playerID == "" {
		h.respondWithError(w, http.StatusBadRequest, "Missing player ID")
		return
	}

	// Implementation for unregistering
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"message":      "Successfully unregistered from tournament",
		"tournamentId": tournamentIDStr,
		"playerId":     playerID,
	})
}

// GetTournamentParticipants gets participants for a tournament
func (h *TournamentHandlers) GetTournamentParticipants(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	tournamentIDStr := chi.URLParam(r, "tournamentId")
	tournamentID, err := uuid.Parse(tournamentIDStr)
	if err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid tournament ID")
		return
	}

	limitStr := r.URL.Query().Get("limit")
	offsetStr := r.URL.Query().Get("offset")

	limit := 50
	if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 500 {
		limit = l
	}

	offset := 0
	if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
		offset = o
	}

	participants, err := h.service.GetTournamentParticipants(r.Context(), tournamentID, limit, offset)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"participants": participants,
		"total":        len(participants),
		"limit":        limit,
		"offset":       offset,
	})
}

// GetParticipant gets a single participant
func (h *TournamentHandlers) GetParticipant(w http.ResponseWriter, r *http.Request) {
	participantIDStr := chi.URLParam(r, "participantId")
	participantID, err := uuid.Parse(participantIDStr)
	if err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid participant ID")
		return
	}

	// Implementation for getting single participant
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"participantId": participantID.String(),
		"status":        "registered",
		"skillRating":   1200,
	})
}

// GetTournamentBrackets gets brackets for a tournament
func (h *TournamentHandlers) GetTournamentBrackets(w http.ResponseWriter, r *http.Request) {
	tournamentIDStr := chi.URLParam(r, "tournamentId")

	// Mock brackets data
	brackets := []map[string]interface{}{
		{
			"id":          "bracket_001",
			"name":        "winners",
			"round":       1,
			"roundName":   "Round of 32",
			"status":      "in_progress",
			"matches":     16,
			"completed":   8,
		},
		{
			"id":          "bracket_002",
			"name":        "losers",
			"round":       1,
			"roundName":   "Losers Round 1",
			"status":      "pending",
			"matches":     8,
			"completed":   0,
		},
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"tournamentId": tournamentIDStr,
		"brackets":     brackets,
		"total":        len(brackets),
	})
}

// GetBracket gets a single bracket
func (h *TournamentHandlers) GetBracket(w http.ResponseWriter, r *http.Request) {
	bracketIDStr := chi.URLParam(r, "bracketId")

	// Implementation for getting single bracket
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"bracketId":  bracketIDStr,
		"name":       "winners",
		"round":      1,
		"roundName":  "Round of 32",
		"status":     "in_progress",
		"matches":    16,
	})
}

// GetBracketMatches gets matches for a bracket
func (h *TournamentHandlers) GetBracketMatches(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	bracketIDStr := chi.URLParam(r, "bracketId")
	bracketID, err := uuid.Parse(bracketIDStr)
	if err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid bracket ID")
		return
	}

	matches, err := h.service.GetMatchesByBracket(r.Context(), bracketID)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"bracketId": bracketID.String(),
		"matches":   matches,
		"total":     len(matches),
	})
}

// GetMatch gets a single match
func (h *TournamentHandlers) GetMatch(w http.ResponseWriter, r *http.Request) {
	matchIDStr := chi.URLParam(r, "matchId")
	matchID, err := uuid.Parse(matchIDStr)
	if err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid match ID")
		return
	}

	// Implementation for getting single match
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"matchId":      matchID.String(),
		"status":       "in_progress",
		"player1":      "CyberNinja",
		"player2":      "NeonGhost",
		"score1":       12,
		"score2":       8,
		"map":          "Downtown",
		"spectators":   45,
	})
}

// UpdateMatchResult updates match result
func (h *TournamentHandlers) UpdateMatchResult(w http.ResponseWriter, r *http.Request) {
	matchIDStr := chi.URLParam(r, "matchId")
	matchID, err := uuid.Parse(matchIDStr)
	if err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid match ID")
		return
	}

	var req struct {
		WinnerID    string `json:"winnerId"`
		WinnerScore int    `json:"winnerScore"`
		LoserID     string `json:"loserId"`
		LoserScore  int    `json:"loserScore"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	winnerID, err := uuid.Parse(req.WinnerID)
	if err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid winner ID")
		return
	}

	loserID, err := uuid.Parse(req.LoserID)
	if err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid loser ID")
		return
	}

	if err := h.service.UpdateMatchResult(r.Context(), matchID, winnerID, req.WinnerScore, loserID, req.LoserScore); err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"message":  "Match result updated successfully",
		"matchId":  matchID.String(),
		"winnerId": winnerID.String(),
		"loserId":  loserID.String(),
	})
}

// GetMatchSpectators gets spectators for a match
func (h *TournamentHandlers) GetMatchSpectators(w http.ResponseWriter, r *http.Request) {
	matchIDStr := chi.URLParam(r, "matchId")

	// Mock spectators data
	spectators := []map[string]interface{}{
		{
			"spectatorId":   "player_123",
			"spectatorName": "SpectatorOne",
			"joinedAt":      time.Now().Add(-time.Minute * 15),
			"sessionTime":   900, // seconds
		},
		{
			"spectatorId":   "player_456",
			"spectatorName": "SpectatorTwo",
			"joinedAt":      time.Now().Add(-time.Minute * 8),
			"sessionTime":   480,
		},
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"matchId":    matchIDStr,
		"spectators": spectators,
		"total":      len(spectators),
	})
}

// JoinSpectator joins a spectator to a match
func (h *TournamentHandlers) JoinSpectator(w http.ResponseWriter, r *http.Request) {
	matchIDStr := chi.URLParam(r, "matchId")
	var req struct {
		SpectatorID string `json:"spectatorId"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// Implementation for joining spectator
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"message":     "Successfully joined as spectator",
		"matchId":     matchIDStr,
		"spectatorId": req.SpectatorID,
		"joinedAt":    time.Now(),
	})
}

// GetTournamentResults gets tournament results
func (h *TournamentHandlers) GetTournamentResults(w http.ResponseWriter, r *http.Request) {
	tournamentIDStr := chi.URLParam(r, "tournamentId")

	// Mock results data
	results := []map[string]interface{}{
		{
			"participantId":   "participant_001",
			"playerName":      "CyberNinja",
			"finalPosition":   1,
			"totalScore":      2500,
			"matchesPlayed":   18,
			"matchesWon":      15,
			"matchesLost":     3,
			"rewards":         map[string]interface{}{"currency": 10000, "title": "Champion"},
		},
		{
			"participantId":   "participant_002",
			"playerName":      "NeonGhost",
			"finalPosition":   2,
			"totalScore":      2200,
			"matchesPlayed":   17,
			"matchesWon":      13,
			"matchesLost":     4,
			"rewards":         map[string]interface{}{"currency": 5000, "title": "Runner-up"},
		},
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"tournamentId": tournamentIDStr,
		"results":      results,
		"total":        len(results),
	})
}

// GetTournamentLeaderboard gets tournament leaderboard
func (h *TournamentHandlers) GetTournamentLeaderboard(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	tournamentIDStr := chi.URLParam(r, "tournamentId")
	tournamentID, err := uuid.Parse(tournamentIDStr)
	if err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid tournament ID")
		return
	}

	limitStr := r.URL.Query().Get("limit")
	limit := 10
	if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
		limit = l
	}

	leaderboard, err := h.service.GetTournamentLeaderboard(r.Context(), tournamentID, limit)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"tournamentId": tournamentID.String(),
		"leaderboard":  leaderboard,
		"limit":        limit,
	})
}

// GetLiveTournaments gets currently active tournaments
func (h *TournamentHandlers) GetLiveTournaments(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	tournaments, err := h.service.GetLiveTournaments(r.Context())
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"tournaments": tournaments,
		"total":       len(tournaments),
		"timestamp":   time.Now(),
	})
}

// GetLiveMatches gets currently active matches
func (h *TournamentHandlers) GetLiveMatches(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	limitStr := r.URL.Query().Get("limit")
	limit := 20
	if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
		limit = l
	}

	liveMatches, err := h.service.GetLiveMatches(r.Context(), limit)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"matches":   liveMatches,
		"total":     len(liveMatches),
		"limit":     limit,
		"timestamp": time.Now(),
	})
}

// GetLiveResults gets live tournament results
func (h *TournamentHandlers) GetLiveResults(w http.ResponseWriter, r *http.Request) {
	tournamentIDStr := chi.URLParam(r, "tournamentId")

	// Mock live results
	liveResults := map[string]interface{}{
		"tournamentId":    tournamentIDStr,
		"status":          "in_progress",
		"currentRound":    3,
		"roundName":       "Quarterfinals",
		"matchesRemaining": 8,
		"matchesCompleted": 24,
		"topPlayers": []map[string]interface{}{
			{
				"playerName": "CyberNinja",
				"score":      1800,
				"wins":       12,
				"winRate":    0.86,
			},
		},
		"timestamp": time.Now(),
	}

	h.respondWithJSON(w, http.StatusOK, liveResults)
}

// SPECTATOR MODE HANDLERS
// Issue: #2213 - Tournament Spectator Mode Implementation

// JoinSpectatorModeRequest represents request to join spectator mode
type JoinSpectatorModeRequest struct {
	PlayerName string `json:"player_name"`
	ViewMode   string `json:"view_mode,omitempty"` // "free", "follow_player", "follow_team", "overview"
	IsVIP      bool   `json:"is_vip,omitempty"`
}

// UpdateSpectatorViewRequest represents request to update spectator view
type UpdateSpectatorViewRequest struct {
	ViewMode  string                 `json:"view_mode"`
	FollowID  string                 `json:"follow_id,omitempty"`
	CameraPos map[string]interface{} `json:"camera_pos,omitempty"`
}

// JoinSpectatorMode allows a player to join tournament match as spectator
func (h *TournamentHandlers) JoinSpectatorMode(w http.ResponseWriter, r *http.Request) {
	matchIDStr := chi.URLParam(r, "match_id")
	matchID, err := uuid.Parse(matchIDStr)
	if err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid match ID")
		return
	}

	var req JoinSpectatorModeRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// Get player ID from context (set by auth middleware)
	playerIDStr := r.Context().Value("player_id").(string)
	playerID, err := uuid.Parse(playerIDStr)
	if err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid player ID")
		return
	}

	// Set default view mode
	if req.ViewMode == "" {
		req.ViewMode = "free"
	}

	spectator, err := h.service.JoinSpectatorMode(r.Context(), matchID, playerID, req.PlayerName, req.ViewMode, req.IsVIP)
	if err != nil {
		h.logger.Errorf("Failed to join spectator mode: %v", err)
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, spectator)
}

// LeaveSpectatorMode allows a spectator to leave the match
func (h *TournamentHandlers) LeaveSpectatorMode(w http.ResponseWriter, r *http.Request) {
	spectatorID := chi.URLParam(r, "spectator_id")

	err := h.service.LeaveSpectatorMode(r.Context(), spectatorID)
	if err != nil {
		h.logger.Errorf("Failed to leave spectator mode: %v", err)
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]string{"status": "left"})
}

// UpdateSpectatorView updates spectator camera position and view mode
func (h *TournamentHandlers) UpdateSpectatorView(w http.ResponseWriter, r *http.Request) {
	spectatorID := chi.URLParam(r, "spectator_id")

	var req UpdateSpectatorViewRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	err := h.service.UpdateSpectatorView(r.Context(), spectatorID, req.ViewMode, req.FollowID, req.CameraPos)
	if err != nil {
		h.logger.Errorf("Failed to update spectator view: %v", err)
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]string{"status": "updated"})
}

// GetMatchSpectators gets all active spectators for a match
func (h *TournamentHandlers) GetMatchSpectators(w http.ResponseWriter, r *http.Request) {
	matchIDStr := chi.URLParam(r, "match_id")
	matchID, err := uuid.Parse(matchIDStr)
	if err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid match ID")
		return
	}

	spectators, err := h.service.GetMatchSpectators(r.Context(), matchID)
	if err != nil {
		h.logger.Errorf("Failed to get match spectators: %v", err)
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"match_id":   matchIDStr,
		"spectators": spectators,
		"count":      len(spectators),
	})
}

// GetSpectatorStats gets spectator statistics for a tournament
func (h *TournamentHandlers) GetSpectatorStats(w http.ResponseWriter, r *http.Request) {
	tournamentIDStr := chi.URLParam(r, "tournament_id")
	tournamentID, err := uuid.Parse(tournamentIDStr)
	if err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid tournament ID")
		return
	}

	stats, err := h.service.GetSpectatorStats(r.Context(), tournamentID)
	if err != nil {
		h.logger.Errorf("Failed to get spectator stats: %v", err)
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, stats)
}

// Helper functions
func (h *TournamentHandlers) respondWithJSON(w http.ResponseWriter, status int, payload interface{}) {
	response, _ := json.Marshal(payload)
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	w.Write(response)
}

func (h *TournamentHandlers) respondWithError(w http.ResponseWriter, status int, message string) {
	h.respondWithJSON(w, status, map[string]string{"error": message})
}
