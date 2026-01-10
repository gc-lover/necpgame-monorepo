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

// RewardHandler handles reward-related HTTP requests
type RewardHandler struct {
	rewardService *services.RewardService
	logger        *zap.Logger
}

// NewRewardHandler creates a new RewardHandler
func NewRewardHandler(rewardService *services.RewardService, logger *zap.Logger) *RewardHandler {
	return &RewardHandler{
		rewardService: rewardService,
		logger:        logger,
	}
}

// ClaimReward handles POST /rewards/claim
func (h *RewardHandler) ClaimReward(w http.ResponseWriter, r *http.Request) {
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
	var request models.ClaimRequest
	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		h.logger.Error("Failed to decode claim request", zap.Error(err))
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	// Validate request
	if request.Level <= 0 {
		http.Error(w, "Level must be positive", http.StatusBadRequest)
		return
	}

	if request.Tier != "free" && request.Tier != "premium" {
		http.Error(w, "Tier must be 'free' or 'premium'", http.StatusBadRequest)
		return
	}

	result, err := h.rewardService.ClaimReward(playerID, request)
	if err != nil {
		h.logger.Error("Failed to claim reward",
			zap.String("playerID", playerID), zap.Int("level", request.Level), zap.String("tier", request.Tier), zap.Error(err))

		switch {
		case strings.Contains(err.Error(), "level too low"):
			http.Error(w, "Insufficient level", http.StatusForbidden)
		case strings.Contains(err.Error(), "premium pass required"):
			http.Error(w, "Premium pass required", http.StatusForbidden)
		case strings.Contains(err.Error(), "already claimed"):
			http.Error(w, "Reward already claimed", http.StatusConflict)
		default:
			http.Error(w, "Internal server error", http.StatusInternalServerError)
		}
		return
	}

	h.respondJSON(w, http.StatusOK, result)
}

// GetAvailableRewards handles GET /rewards/available
func (h *RewardHandler) GetAvailableRewards(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodGet {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	// Extract player ID from query parameters
	playerID := r.URL.Query().Get("playerId")
	if playerID == "" {
		http.Error(w, "playerId query parameter required", http.StatusBadRequest)
		return
	}

	// Get season ID from query parameters
	seasonID := r.URL.Query().Get("seasonId")
	if seasonID == "" {
		// Get current season if not specified
		currentSeason, err := h.rewardService.GetCurrentSeason()
		if err != nil {
			h.logger.Error("Failed to get current season", zap.Error(err))
			http.Error(w, "Internal server error", http.StatusInternalServerError)
			return
		}
		seasonID = currentSeason.ID
	}

	rewards, err := h.rewardService.GetAvailableRewards(playerID, seasonID)
	if err != nil {
		h.logger.Error("Failed to get available rewards",
			zap.String("playerID", playerID), zap.String("seasonID", seasonID), zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	response := map[string]interface{}{
		"rewards":    rewards,
		"totalCount": len(rewards),
	}

	h.respondJSON(w, http.StatusOK, response)
}

// CreateReward handles POST /rewards
func (h *RewardHandler) CreateReward(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	var reward models.Reward
	if err := json.NewDecoder(r.Body).Decode(&reward); err != nil {
		h.logger.Error("Failed to decode reward", zap.Error(err))
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	err := h.rewardService.CreateReward(&reward)
	if err != nil {
		h.logger.Error("Failed to create reward", zap.Error(err))
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	h.respondJSON(w, http.StatusCreated, reward)
}

// ListRewards handles GET /rewards
func (h *RewardHandler) ListRewards(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodGet {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	// Parse query parameters
	limit := 50
	offset := 0
	rewardType := r.URL.Query().Get("type")

	if limitParam := r.URL.Query().Get("limit"); limitParam != "" {
		if l, err := strconv.Atoi(limitParam); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}

	if offsetParam := r.URL.Query().Get("offset"); offsetParam != "" {
		if o, err := strconv.Atoi(offsetParam); err == nil && o >= 0 {
			offset = o
		}
	}

	rewards, err := h.rewardService.ListRewards(limit, offset, rewardType)
	if err != nil {
		h.logger.Error("Failed to list rewards", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	response := map[string]interface{}{
		"rewards": rewards,
		"limit":   limit,
		"offset":  offset,
	}

	h.respondJSON(w, http.StatusOK, response)
}

// GetClaimHistory handles GET /rewards/history
func (h *RewardHandler) GetClaimHistory(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodGet {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	// Extract player ID from query parameters
	playerID := r.URL.Query().Get("playerId")
	if playerID == "" {
		http.Error(w, "playerId query parameter required", http.StatusBadRequest)
		return
	}

	// Get season ID from query parameters
	seasonID := r.URL.Query().Get("seasonId")

	// Parse pagination parameters
	limit := 50
	offset := 0

	if limitParam := r.URL.Query().Get("limit"); limitParam != "" {
		if l, err := strconv.Atoi(limitParam); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}

	if offsetParam := r.URL.Query().Get("offset"); offsetParam != "" {
		if o, err := strconv.Atoi(offsetParam); err == nil && o >= 0 {
			offset = o
		}
	}

	history, err := h.rewardService.GetClaimHistory(playerID, seasonID, limit, offset)
	if err != nil {
		h.logger.Error("Failed to get claim history", zap.String("playerID", playerID), zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	response := map[string]interface{}{
		"history": history,
		"limit":   limit,
		"offset":  offset,
	}

	h.respondJSON(w, http.StatusOK, response)
}

// respondJSON sends a JSON response
func (h *RewardHandler) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)

	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.Error("Failed to encode JSON response", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
	}
}