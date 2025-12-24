// Issue: #backend-achievement_system
// PERFORMANCE: HTTP handlers optimized for MMOFPS workloads

package server

import (
	"context"
	"encoding/json"
	"net/http"
	"time"

	"achievement-service-go/pkg/api"
	"go.uber.org/zap"
)

// AchievementHandler handles HTTP requests for achievements
// PERFORMANCE: Reusable handler with dependency injection
type AchievementHandler struct {
	service   *AchievementServiceLogic
	logger    *zap.Logger
}

// NewAchievementHandler creates a new handler instance
// PERFORMANCE: Pre-allocates resources
func NewAchievementHandler(svc *AchievementServiceLogic) *AchievementHandler {
	handler := &AchievementHandler{
		service: svc,
	}

	// PERFORMANCE: Initialize structured logger
	if l, err := zap.NewProduction(); err == nil {
		handler.logger = l
	} else {
		handler.logger = zap.NewNop()
	}

	return handler
}

// AchievementGetAchievements handles GET /api/v1/achievement/achievements
// PERFORMANCE: Optimized for high-throughput achievement listing
func (h *AchievementHandler) AchievementGetAchievements(ctx context.Context, params api.AchievementGetAchievementsParams) (api.AchievementGetAchievementsRes, error) {
	playerID := params.PlayerID
	if playerID == "" {
		return &api.AchievementGetAchievementsBadRequest{
			Error:   "player_id is required",
			Status:  400,
			Service: "achievement-service",
		}, nil
	}

	limit := 50  // default
	offset := 0 // default

	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 100 {
		limit = *params.Limit
	}

	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	achievements, err := h.service.GetAchievements(ctx, playerID, limit, offset)
	if err != nil {
		h.logger.Error("Failed to get achievements", zap.Error(err))
		return &api.AchievementGetAchievementsInternalServerError{
			Error:   "Failed to retrieve achievements",
			Status:  500,
			Service: "achievement-service",
		}, nil
	}

	// Convert to API response format
	apiAchievements := make([]api.Achievement, 0, len(achievements))
	for _, achievement := range achievements {
		apiAchievement := api.Achievement{
			Id:          api.NewOptString(achievement.ID),
			Name:        api.NewOptString(achievement.Name),
			Description: api.NewOptString(achievement.Description),
			Rarity:      api.NewOptString(achievement.Rarity),
			Points:      api.NewOptInt32(achievement.Points),
			IsUnlocked:  api.NewOptBool(achievement.IsUnlocked),
		}

		if achievement.IconURL != "" {
			apiAchievement.IconUrl = api.NewOptString(achievement.IconURL)
		}

		if achievement.UnlockedAt != nil {
			apiAchievement.UnlockedAt = api.NewOptDateTime(*achievement.UnlockedAt)
		}

		apiAchievement.CreatedAt = api.NewOptDateTime(achievement.CreatedAt)
		apiAchievement.UpdatedAt = api.NewOptDateTime(achievement.UpdatedAt)

		apiAchievements = append(apiAchievements, apiAchievement)
	}

	return &api.AchievementListResponse{
		Achievements: apiAchievements,
		Count:        api.NewOptInt(len(apiAchievements)),
	}, nil
}

// AchievementGetAchievement handles GET /api/v1/achievement/achievements/{achievementId}
// PERFORMANCE: Optimized for single achievement retrieval
func (h *AchievementHandler) AchievementGetAchievement(ctx context.Context, params api.AchievementGetAchievementParams) (api.AchievementGetAchievementRes, error) {
	achievementID := params.AchievementId

	if achievementID == "" {
		return &api.AchievementGetAchievementBadRequest{
			Error:   "achievement_id is required",
			Status:  400,
			Service: "achievement-service",
		}, nil
	}

	// TODO: Extract player_id from JWT token or context
	// For now, use a mock player_id - this needs to be implemented properly
	playerID := "mock-player-id" // This should come from authentication

	achievement, err := h.service.GetAchievement(ctx, achievementID, playerID)
	if err != nil {
		h.logger.Error("Failed to get achievement", zap.Error(err))
		return &api.AchievementGetAchievementInternalServerError{
			Error:   "Failed to retrieve achievement",
			Status:  500,
			Service: "achievement-service",
		}, nil
	}

	if achievement == nil {
		return &api.AchievementGetAchievementNotFound{
			Error:   "Achievement not found",
			Status:  404,
			Service: "achievement-service",
		}, nil
	}

	// Convert to API response format
	apiAchievement := api.Achievement{
		Id:          api.NewOptString(achievement.ID),
		Name:        api.NewOptString(achievement.Name),
		Description: api.NewOptString(achievement.Description),
		Rarity:      api.NewOptString(achievement.Rarity),
		Points:      api.NewOptInt32(achievement.Points),
		IsUnlocked:  api.NewOptBool(achievement.IsUnlocked),
	}

	if achievement.IconURL != "" {
		apiAchievement.IconUrl = api.NewOptString(achievement.IconURL)
	}

	if achievement.UnlockedAt != nil {
		apiAchievement.UnlockedAt = api.NewOptDateTime(*achievement.UnlockedAt)
	}

	apiAchievement.CreatedAt = api.NewOptDateTime(achievement.CreatedAt)
	apiAchievement.UpdatedAt = api.NewOptDateTime(achievement.UpdatedAt)

	return &api.AchievementGetAchievementOK{
		Achievement: apiAchievement,
	}, nil
}

// AchievementUnlockAchievement handles POST /api/v1/achievement/achievements/{achievementId}/unlock
// PERFORMANCE: Optimized for achievement unlocking operations
func (h *AchievementHandler) AchievementUnlockAchievement(ctx context.Context, params api.AchievementUnlockAchievementParams) (api.AchievementUnlockAchievementRes, error) {
	achievementID := params.AchievementId

	if achievementID == "" {
		return &api.AchievementUnlockAchievementBadRequest{
			Error:   "achievement_id is required",
			Status:  400,
			Service: "achievement-service",
		}, nil
	}

	// TODO: Extract player_id from JWT token or context
	// For now, use a mock player_id - this needs to be implemented properly
	playerID := "mock-player-id" // This should come from authentication

	err := h.service.UnlockAchievement(ctx, playerID, achievementID)
	if err != nil {
		h.logger.Error("Failed to unlock achievement", zap.Error(err))
		return &api.AchievementUnlockAchievementInternalServerError{
			Error:   "Failed to unlock achievement",
			Status:  500,
			Service: "achievement-service",
		}, nil
	}

	return &api.AchievementUnlockAchievementOK{
		Message: api.NewOptString("Achievement unlocked successfully"),
	}, nil
}

// Health handles GET /health
// PERFORMANCE: Lightweight health check with database ping
func (h *AchievementHandler) Health(ctx context.Context) (*api.HealthResponse, error) {
	// PERFORMANCE: Quick health check
	ctx, cancel := context.WithTimeout(ctx, 1*time.Second)
	defer cancel()

	// Test database connectivity
	if err := h.repo.db.Ping(ctx); err != nil {
		h.logger.Error("Health check failed", zap.Error(err))
		return nil, err
	}

	return &api.HealthResponse{
		Status:    api.HealthResponseStatusHealthy,
		Service:   "achievement-service",
		Timestamp: time.Now(),
		Version:   "1.0.0",
	}, nil
}

// writeError writes a JSON error response
// PERFORMANCE: Reusable error response method
func (h *AchievementHandler) writeError(w http.ResponseWriter, statusCode int, message string) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(statusCode)

	response := map[string]interface{}{
		"error":   message,
		"status":  statusCode,
		"service": "achievement-service",
	}

	encoder := json.NewEncoder(w)
	encoder.Encode(response)
}
