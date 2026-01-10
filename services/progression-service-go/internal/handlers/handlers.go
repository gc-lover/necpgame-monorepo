package handlers

import (
	"context"
	"fmt"
	"log"
	"os"
	"time"

	"necpgame/services/progression-service-go/internal/service"
	api "necpgame/services/progression-service-go"
)

// ProgressionHandlers implements the generated Handler interface
type ProgressionHandlers struct {
	progressionSvc *service.ProgressionService
}

// NewProgressionHandlers creates a new instance of ProgressionHandlers
func NewProgressionHandlers(svc *service.ProgressionService) *ProgressionHandlers {
	return &ProgressionHandlers{
		progressionSvc: svc,
	}
}

// ProgressionSecurityHandler implements the SecurityHandler interface
type ProgressionSecurityHandler struct {
	jwtService *service.JWTService
}

// NewProgressionSecurityHandler creates a new security handler
func NewProgressionSecurityHandler() *ProgressionSecurityHandler {
	jwtSecret := os.Getenv("JWT_SECRET")
	if jwtSecret == "" {
		jwtSecret = "default-jwt-secret-change-in-production"
	}
	return &ProgressionSecurityHandler{
		jwtService: service.NewJWTService(jwtSecret),
	}
}

// HandleBearerAuth implements JWT Bearer token authentication
func (s *ProgressionSecurityHandler) HandleBearerAuth(ctx context.Context, operationName api.OperationName, t api.BearerAuth) (context.Context, error) {
	if t.Token == "" {
		return ctx, fmt.Errorf("missing bearer token")
	}

	// Validate JWT token
	claims, err := s.jwtService.ValidateToken(t.Token)
	if err != nil {
		return ctx, fmt.Errorf("invalid JWT token: %w", err)
	}

	// Check if token is expired
	if claims.ExpiresAt != nil && claims.ExpiresAt.Before(time.Now()) {
		return ctx, fmt.Errorf("token expired")
	}

	// Extract user ID and add to context for use in handlers
	userID, err := s.jwtService.ExtractUserID(t.Token)
	if err != nil {
		return ctx, fmt.Errorf("failed to extract user ID from token: %w", err)
	}

	// Add user information to context
	ctx = context.WithValue(ctx, "user_id", userID.String())
	ctx = context.WithValue(ctx, "username", claims.Username)

	return ctx, nil
}

// HealthCheck implements health check endpoint
func (h *ProgressionHandlers) HealthCheck(ctx context.Context) (*api.HealthCheckOK, error) {
	log.Println("Health check requested")

	response := &api.HealthCheckOK{
		Status:    "healthy",
		Service:   "endgame-progression-service",
		Timestamp: time.Now(),
	}

	return response, nil
}

// ReadinessCheck implements readiness check endpoint
func (h *ProgressionHandlers) ReadinessCheck(ctx context.Context) (*api.ReadinessCheckOK, error) {
	log.Println("Readiness check requested")

	response := &api.ReadinessCheckOK{
		Status: "ready",
	}

	return response, nil
}

// PARAGON LEVELS ENDPOINTS

// GetParagonLevels implements paragon levels retrieval
func (h *ProgressionHandlers) GetParagonLevels(ctx context.Context, params api.GetParagonLevelsParams) (api.GetParagonLevelsRes, error) {
	characterID := params.CharacterID.String()

	log.Printf("Getting paragon levels for character: %s", characterID)

	levels, err := h.progressionSvc.GetParagonLevels(ctx, characterID)
	if err != nil {
		log.Printf("Failed to get paragon levels for %s: %v", characterID, err)
		return &api.GetParagonLevelsInternalServerError{
			Code:    "500",
			Message: fmt.Sprintf("Failed to get paragon levels: %v", err),
		}, nil
	}

	// Convert to ogen types
	pointsDistributed := api.PointsDistributed{}
	if levels.PointsDistributed != nil {
		pointsDistributed = api.PointsDistributed{
			Strength:     levels.PointsDistributed.Strength,
			Agility:      levels.PointsDistributed.Agility,
			Intelligence: levels.PointsDistributed.Intelligence,
			Vitality:     levels.PointsDistributed.Vitality,
			Luck:         levels.PointsDistributed.Luck,
		}
	}

	response := &api.ParagonLevels{
		CurrentLevel:      levels.CurrentLevel,
		TotalXp:           levels.TotalXp,
		AvailablePoints:   levels.AvailablePoints,
		PointsDistributed: pointsDistributed,
		XpToNextLevel:     levels.XpToNextLevel,
		XpProgress:        levels.XpProgress,
		LastUpdated:       levels.LastUpdated,
	}

	return response, nil
}

// DistributeParagonPoints implements paragon points distribution
func (h *ProgressionHandlers) DistributeParagonPoints(ctx context.Context, req *api.DistributeParagonPointsReq) (api.DistributeParagonPointsRes, error) {
	characterID := req.CharacterID.String()

	log.Printf("Distributing paragon points for character: %s", characterID)

	distribution := &service.PointsDistribution{
		Strength:     req.Distribution.Strength,
		Agility:      req.Distribution.Agility,
		Intelligence: req.Distribution.Intelligence,
		Vitality:     req.Distribution.Vitality,
		Luck:         req.Distribution.Luck,
	}

	err := h.progressionSvc.DistributeParagonPoints(ctx, characterID, distribution)
	if err != nil {
		log.Printf("Failed to distribute paragon points for %s: %v", characterID, err)
		return &api.DistributeParagonPointsInternalServerError{
			Code:    "400",
			Message: fmt.Sprintf("Failed to distribute paragon points: %v", err),
		}, nil
	}

	response := &api.SuccessResponse{
		Message: "Paragon points distributed successfully",
	}

	return response, nil
}

// GetParagonStats implements paragon statistics retrieval
func (h *ProgressionHandlers) GetParagonStats(ctx context.Context, params api.GetParagonStatsParams) (api.GetParagonStatsRes, error) {
	characterID := params.CharacterID.String()

	log.Printf("Getting paragon stats for character: %s", characterID)

	stats, err := h.progressionSvc.GetParagonStats(ctx, characterID)
	if err != nil {
		log.Printf("Failed to get paragon stats for %s: %v", characterID, err)
		return &api.Error{
			Code:    "500",
			Message: fmt.Sprintf("Failed to get paragon stats: %v", err),
		}, nil
	}

	response := &api.ParagonStats{
		TotalCharactersWithParagon: stats.TotalCharactersWithParagon,
		AverageParagonLevel:        stats.AverageParagonLevel,
		HighestParagonLevel:        stats.HighestParagonLevel,
	}

	return response, nil
}

// PRESTIGE SYSTEM ENDPOINTS

// GetPrestigeInfo implements prestige information retrieval
func (h *ProgressionHandlers) GetPrestigeInfo(ctx context.Context, params api.GetPrestigeInfoParams) (api.GetPrestigeInfoRes, error) {
	characterID := params.CharacterID.String()

	log.Printf("Getting prestige info for character: %s", characterID)

	info, err := h.progressionSvc.GetPrestigeInfo(ctx, characterID)
	if err != nil {
		log.Printf("Failed to get prestige info for %s: %v", characterID, err)
		return &api.Error{
			Code:    "500",
			Message: fmt.Sprintf("Failed to get prestige info: %v", err),
		}, nil
	}

	response := &api.PrestigeInfo{
		CurrentLevel:        info.CurrentLevel,
		TotalResets:         info.TotalResets,
		BonusMultiplier:     info.BonusMultiplier,
		NextLevelXpRequired: info.NextLevelXpRequired,
		LastReset:           info.LastReset,
	}

	return response, nil
}

// ResetPrestige implements prestige reset
func (h *ProgressionHandlers) ResetPrestige(ctx context.Context, req *api.ResetPrestigeReq) (api.ResetPrestigeRes, error) {
	characterID := req.CharacterID.String()

	log.Printf("Resetting prestige for character: %s", characterID)

	err := h.progressionSvc.ResetPrestige(ctx, characterID)
	if err != nil {
		log.Printf("Failed to reset prestige for %s: %v", characterID, err)
		return &api.ResetPrestigeInternalServerError{
			Code:    "400",
			Message: fmt.Sprintf("Failed to reset prestige: %v", err),
		}, nil
	}

	response := &api.SuccessResponse{
		Message: "Prestige reset successfully completed",
	}

	return response, nil
}

// GetPrestigeBonuses implements prestige bonuses retrieval
func (h *ProgressionHandlers) GetPrestigeBonuses(ctx context.Context, params api.GetPrestigeBonusesParams) (api.GetPrestigeBonusesRes, error) {
	characterID := params.CharacterID.String()

	log.Printf("Getting prestige bonuses for character: %s", characterID)

	bonuses, err := h.progressionSvc.GetPrestigeBonuses(ctx, characterID)
	if err != nil {
		log.Printf("Failed to get prestige bonuses for %s: %v", characterID, err)
		return &api.Error{
			Code:    "500",
			Message: fmt.Sprintf("Failed to get prestige bonuses: %v", err),
		}, nil
	}

	response := &api.PrestigeBonuses{
		Bonuses: bonuses.Bonuses,
	}

	return response, nil
}

// MASTERY SYSTEM ENDPOINTS

// GetMasteryLevels implements mastery levels retrieval
func (h *ProgressionHandlers) GetMasteryLevels(ctx context.Context, params api.GetMasteryLevelsParams) (api.GetMasteryLevelsRes, error) {
	characterID := params.CharacterID.String()

	log.Printf("Getting mastery levels for character: %s", characterID)

	levels, err := h.progressionSvc.GetMasteryLevels(ctx, characterID)
	if err != nil {
		log.Printf("Failed to get mastery levels for %s: %v", characterID, err)
		return &api.Error{
			Code:    "500",
			Message: fmt.Sprintf("Failed to get mastery levels: %v", err),
		}, nil
	}

	response := &api.MasteryLevels{
		Levels:  levels.Levels,
		Rewards: levels.Rewards,
	}

	return response, nil
}

// GetMasteryProgress implements mastery progress retrieval
func (h *ProgressionHandlers) GetMasteryProgress(ctx context.Context, params api.GetMasteryProgressParams) (api.GetMasteryProgressRes, error) {
	characterID := params.CharacterID.String()
	masteryType := string(params.MasteryType)

	log.Printf("Getting mastery progress for character: %s, type: %s", characterID, masteryType)

	progress, err := h.progressionSvc.GetMasteryProgress(ctx, characterID, masteryType)
	if err != nil {
		log.Printf("Failed to get mastery progress for %s/%s: %v", characterID, masteryType, err)
		return &api.GetMasteryProgressInternalServerError{
			Code:    "500",
			Message: fmt.Sprintf("Failed to get mastery progress: %v", err),
		}, nil
	}

	response := &api.MasteryProgress{
		MasteryType:     api.MasteryProgressMasteryType(progress.MasteryType),
		CurrentLevel:    progress.CurrentLevel,
		CurrentXp:       progress.CurrentXp,
		XpToNextLevel:   progress.XpToNextLevel,
		ProgressPercent: progress.ProgressPercent,
		TotalXpEarned:   progress.TotalXpEarned,
	}

	return response, nil
}

// GetMasteryRewards implements mastery rewards retrieval
func (h *ProgressionHandlers) GetMasteryRewards(ctx context.Context, params api.GetMasteryRewardsParams) (api.GetMasteryRewardsRes, error) {
	characterID := params.CharacterID.String()
	masteryType := string(params.MasteryType)

	log.Printf("Getting mastery rewards for character: %s, type: %s", characterID, masteryType)

	rewards, err := h.progressionSvc.GetMasteryRewards(ctx, characterID, masteryType)
	if err != nil {
		log.Printf("Failed to get mastery rewards for %s/%s: %v", characterID, masteryType, err)
		return &api.GetMasteryRewardsInternalServerError{
			Code:    "500",
			Message: fmt.Sprintf("Failed to get mastery rewards: %v", err),
		}, nil
	}

	response := &api.MasteryRewards{
		MasteryType: api.MasteryRewardsMasteryType(rewards.MasteryType),
		Rewards:     rewards.Rewards,
	}

	return response, nil
}