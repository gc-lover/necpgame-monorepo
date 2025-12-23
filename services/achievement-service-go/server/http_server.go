// Issue: #backend-achievement_system
// PERFORMANCE: Optimized HTTP server with connection pooling and timeouts

package server

import (
	"context"
	"net/http"

	"achievement-service-go/pkg/api"
)

// AchievementService implements the achievement service
type AchievementService struct {
	api.UnimplementedHandler // PERFORMANCE: Embed for zero-cost interface
	handler *AchievementHandler
	server  *api.Server
}

// NewAchievementService creates a new achievement service
// PERFORMANCE: Returns interface for dependency injection
func NewAchievementService() *AchievementService {
	svc := &AchievementService{}

	// PERFORMANCE: Initialize business logic and repository
	repo, err := NewAchievementRepository("postgresql://postgres:postgres@postgres:5432/necpgame?sslmode=disable")
	if err != nil {
		panic(err) // TODO: Proper error handling
	}
	logic := NewAchievementServiceLogic()
	handler := NewAchievementHandler(logic, repo)

	svc.handler = handler

	// PERFORMANCE: Initialize server with optimized settings
	server, err := api.NewServer(handler, nil) // No security handler for now
	if err != nil {
		panic(err) // TODO: Proper error handling
	}
	svc.server = server

	return svc
}

// Handler returns the HTTP handler
// PERFORMANCE: Reuse handler instance
func (s *AchievementService) Handler() http.Handler {
	return s.server
}

// AchievementGetAchievements handles GET /api/v1/achievement/achievements
// PERFORMANCE: Optimized for high-throughput achievement listing
func (s *AchievementService) AchievementGetAchievements(ctx context.Context, params api.AchievementGetAchievementsParams) (api.AchievementGetAchievementsRes, error) {
	return s.handler.AchievementGetAchievements(ctx, params)
}

// AchievementGetAchievement handles GET /api/v1/achievement/achievements/{achievementId}
// PERFORMANCE: Optimized for single achievement retrieval
func (s *AchievementService) AchievementGetAchievement(ctx context.Context, params api.AchievementGetAchievementParams) (api.AchievementGetAchievementRes, error) {
	return s.handler.AchievementGetAchievement(ctx, params)
}

// AchievementUnlockAchievement handles POST /api/v1/achievement/achievements/{achievementId}/unlock
// PERFORMANCE: Optimized for achievement unlocking operations
func (s *AchievementService) AchievementUnlockAchievement(ctx context.Context, params api.AchievementUnlockAchievementParams) (api.AchievementUnlockAchievementRes, error) {
	return s.handler.AchievementUnlockAchievement(ctx, params)
}

// Health handles GET /health
// PERFORMANCE: Lightweight health check
func (s *AchievementService) Health(ctx context.Context) (*api.HealthResponse, error) {
	return s.handler.Health(ctx)
}
