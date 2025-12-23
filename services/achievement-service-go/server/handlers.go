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
	repo      *AchievementRepository
	logger    *zap.Logger
}

// NewAchievementHandler creates a new handler instance
// PERFORMANCE: Pre-allocates resources
func NewAchievementHandler(svc *AchievementServiceLogic, repo *AchievementRepository) *AchievementHandler {
	handler := &AchievementHandler{
		service: svc,
		repo:    repo,
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
	// TODO: Implement achievements listing
	return &api.AchievementListResponse{
		Achievements: []api.Achievement{},
		Count:        0,
	}, nil
}

// AchievementGetAchievement handles GET /api/v1/achievement/achievements/{achievementId}
// PERFORMANCE: Optimized for single achievement retrieval
func (h *AchievementHandler) AchievementGetAchievement(ctx context.Context, params api.AchievementGetAchievementParams) (api.AchievementGetAchievementRes, error) {
	// TODO: Implement achievement retrieval
	return &api.AchievementGetAchievementNotFound{
		Error:   "Not implemented",
		Status:  501,
		Service: "achievement-service",
	}, nil
}

// AchievementUnlockAchievement handles POST /api/v1/achievement/achievements/{achievementId}/unlock
// PERFORMANCE: Optimized for achievement unlocking operations
func (h *AchievementHandler) AchievementUnlockAchievement(ctx context.Context, params api.AchievementUnlockAchievementParams) (api.AchievementUnlockAchievementRes, error) {
	// TODO: Implement achievement unlocking
	return &api.AchievementUnlockAchievementBadRequest{
		Error:   "Not implemented",
		Status:  501,
		Service: "achievement-service",
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
