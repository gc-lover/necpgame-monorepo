package handlers

import (
	"encoding/json"
	"net/http"
	"strconv"
	"strings"

	"go.uber.org/zap"

	"battle-pass-service-go/internal/models"
	"battle-pass-service-go/internal/services"
)

// ProgressHandler handles progress-related HTTP requests
type ProgressHandler struct {
	progressService *services.ProgressService
	logger          *zap.Logger
}

// NewProgressHandler creates a new ProgressHandler
func NewProgressHandler(progressService *services.ProgressService, logger *zap.Logger) *ProgressHandler {
	return &ProgressHandler{
		progressService: progressService,
		logger:          logger,
	}
}

// GetProgress handles GET /progress/{playerId}
func (h *ProgressHandler) GetProgress(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodGet {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	// Extract player ID from URL path
	pathParts := strings.Split(strings.Trim(r.URL.Path, "/"), "/")
	if len(pathParts) < 2 {
		http.Error(w, "Invalid URL path", http.StatusBadRequest)
		return
	}

	playerID := pathParts[1] // progress/{playerId}

	// Get season ID from query parameters
	seasonID := r.URL.Query().Get("seasonId")
	if seasonID == "" {
		// Get current season if not specified
		currentSeason, err := h.progressService.GetCurrentSeason()
		if err != nil {
			h.logger.Error("Failed to get current season", zap.Error(err))
			http.Error(w, "Internal server error", http.StatusInternalServerError)
			return
		}
		seasonID = currentSeason.ID
	}

	progress, err := h.progressService.GetPlayerProgress(playerID, seasonID)
	if err != nil {
		h.logger.Error("Failed to get player progress",
			zap.String("playerID", playerID), zap.String("seasonID", seasonID), zap.Error(err))
		if strings.Contains(err.Error(), "not found") {
			http.Error(w, "Progress not found", http.StatusNotFound)
		} else {
			http.Error(w, "Internal server error", http.StatusInternalServerError)
		}
		return
	}

	h.respondJSON(w, http.StatusOK, progress)
}

// GrantXP handles POST /progress/xp
func (h *ProgressHandler) GrantXP(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	// Extract player ID from query parameters
	playerID := r.URL.Query().Get("playerId")
	if playerID == "" {
		http.Error(w, "playerId query parameter required", http.StatusBadRequest)
		return
	}

	// Parse request body
	var grant models.XPGrant
	if err := json.NewDecoder(r.Body).Decode(&grant); err != nil {
		h.logger.Error("Failed to decode XP grant request", zap.Error(err))
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	// Validate request
	if grant.Amount <= 0 {
		http.Error(w, "XP amount must be positive", http.StatusBadRequest)
		return
	}

	if grant.Reason == "" {
		http.Error(w, "XP grant reason required", http.StatusBadRequest)
		return
	}

	result, err := h.progressService.GrantXP(playerID, grant)
	if err != nil {
		h.logger.Error("Failed to grant XP",
			zap.String("playerID", playerID), zap.Int("amount", grant.Amount), zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	h.respondJSON(w, http.StatusOK, result)
}

// PurchasePremiumPass handles POST /progress/premium
func (h *ProgressHandler) PurchasePremiumPass(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	// Extract player ID from query parameters
	playerID := r.URL.Query().Get("playerId")
	if playerID == "" {
		http.Error(w, "playerId query parameter required", http.StatusBadRequest)
		return
	}

	// Parse request body
	var request struct {
		Price    int    `json:"price"`
		Currency string `json:"currency"`
	}
	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		h.logger.Error("Failed to decode premium purchase request", zap.Error(err))
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	// Get current season
	currentSeason, err := h.progressService.GetCurrentSeason()
	if err != nil {
		h.logger.Error("Failed to get current season", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	err = h.progressService.PurchasePremiumPass(playerID, currentSeason.ID, request.Price, request.Currency)
	if err != nil {
		h.logger.Error("Failed to purchase premium pass",
			zap.String("playerID", playerID), zap.Error(err))
		if strings.Contains(err.Error(), "already has premium pass") {
			http.Error(w, "Already has premium pass", http.StatusConflict)
		} else {
			http.Error(w, "Internal server error", http.StatusInternalServerError)
		}
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"message": "Premium pass purchased successfully"})
}

// GetLeaderboard handles GET /progress/leaderboard
func (h *ProgressHandler) GetLeaderboard(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodGet {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	// Get season ID from query parameters
	seasonID := r.URL.Query().Get("seasonId")
	if seasonID == "" {
		// Get current season if not specified
		currentSeason, err := h.progressService.GetCurrentSeason()
		if err != nil {
			h.logger.Error("Failed to get current season", zap.Error(err))
			http.Error(w, "Internal server error", http.StatusInternalServerError)
			return
		}
		seasonID = currentSeason.ID
	}

	// Get limit from query parameters
	limit := 50
	if limitParam := r.URL.Query().Get("limit"); limitParam != "" {
		if l, err := strconv.Atoi(limitParam); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}

	leaderboard, err := h.progressService.GetLeaderboard(seasonID, limit)
	if err != nil {
		h.logger.Error("Failed to get leaderboard", zap.String("seasonID", seasonID), zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]interface{}{
		"seasonId":    seasonID,
		"leaderboard": leaderboard,
	})
}

// respondJSON sends a JSON response
func (h *ProgressHandler) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)

	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.Error("Failed to encode JSON response", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
	}
}