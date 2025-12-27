package service

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"services/achievement-system-service-go/internal/repository"
	"services/achievement-system-service-go/pkg/models"
)

// Service handles business logic for the Achievement System
type Service struct {
	repo   *repository.Repository
	logger *zap.Logger
}

// NewService creates a new service instance
func NewService(repo *repository.Repository, logger *zap.Logger) *Service {
	return &Service{
		repo:   repo,
		logger: logger,
	}
}

// Achievement management

// CreateAchievement creates a new achievement
func (s *Service) CreateAchievement(ctx context.Context, req *models.Achievement) (*models.Achievement, error) {
	if req.ID == uuid.Nil {
		req.ID = uuid.New()
	}

	if err := s.validateAchievement(req); err != nil {
		return nil, fmt.Errorf("invalid achievement: %w", err)
	}

	if err := s.repo.CreateAchievement(ctx, req); err != nil {
		s.logger.Error("Failed to create achievement", zap.Error(err), zap.String("achievement_id", req.ID.String()))
		return nil, err
	}

	s.logger.Info("Achievement created", zap.String("achievement_id", req.ID.String()), zap.String("name", req.Name))
	return req, nil
}

// GetAchievement retrieves an achievement by ID
func (s *Service) GetAchievement(ctx context.Context, id uuid.UUID) (*models.Achievement, error) {
	achievement, err := s.repo.GetAchievement(ctx, id)
	if err != nil {
		s.logger.Error("Failed to get achievement", zap.Error(err), zap.String("achievement_id", id.String()))
		return nil, err
	}
	return achievement, nil
}

// ListAchievements retrieves all achievements with pagination
func (s *Service) ListAchievements(ctx context.Context, limit, offset int) ([]*models.Achievement, error) {
	if limit <= 0 || limit > 100 {
		limit = 50
	}
	if offset < 0 {
		offset = 0
	}

	achievements, err := s.repo.ListAchievements(ctx, limit, offset)
	if err != nil {
		s.logger.Error("Failed to list achievements", zap.Error(err))
		return nil, err
	}
	return achievements, nil
}

// UpdateAchievement updates an existing achievement
func (s *Service) UpdateAchievement(ctx context.Context, req *models.Achievement) (*models.Achievement, error) {
	if err := s.validateAchievement(req); err != nil {
		return nil, fmt.Errorf("invalid achievement: %w", err)
	}

	if err := s.repo.UpdateAchievement(ctx, req); err != nil {
		s.logger.Error("Failed to update achievement", zap.Error(err), zap.String("achievement_id", req.ID.String()))
		return nil, err
	}

	s.logger.Info("Achievement updated", zap.String("achievement_id", req.ID.String()))
	return req, nil
}

// DeleteAchievement soft deletes an achievement
func (s *Service) DeleteAchievement(ctx context.Context, id uuid.UUID) error {
	if err := s.repo.DeleteAchievement(ctx, id); err != nil {
		s.logger.Error("Failed to delete achievement", zap.Error(err), zap.String("achievement_id", id.String()))
		return err
	}

	s.logger.Info("Achievement deleted", zap.String("achievement_id", id.String()))
	return nil
}

// Player achievement operations

// GetPlayerAchievements retrieves all achievements for a player
func (s *Service) GetPlayerAchievements(ctx context.Context, playerID uuid.UUID) ([]*models.PlayerAchievement, error) {
	achievements, err := s.repo.GetPlayerAchievements(ctx, playerID)
	if err != nil {
		s.logger.Error("Failed to get player achievements", zap.Error(err), zap.String("player_id", playerID.String()))
		return nil, err
	}
	return achievements, nil
}

// UnlockAchievement unlocks an achievement for a player
func (s *Service) UnlockAchievement(ctx context.Context, playerID, achievementID uuid.UUID) (*models.PlayerAchievement, error) {
	// Check if achievement exists and is active
	achievement, err := s.repo.GetAchievement(ctx, achievementID)
	if err != nil {
		return nil, fmt.Errorf("achievement not found: %w", err)
	}

	// Check if player already has this achievement
	playerAchievements, err := s.repo.GetPlayerAchievements(ctx, playerID)
	if err != nil {
		return nil, err
	}

	for _, pa := range playerAchievements {
		if pa.AchievementID == achievementID {
			return nil, fmt.Errorf("achievement already unlocked")
		}
	}

	// Create player achievement
	playerAchievement := &models.PlayerAchievement{
		ID:            uuid.New(),
		PlayerID:      playerID,
		AchievementID: achievementID,
		UnlockedAt:    time.Now(),
		PointsEarned:  achievement.Points,
		Rewards:       s.generateRewards(achievement),
	}

	if err := s.repo.UnlockAchievement(ctx, playerAchievement); err != nil {
		s.logger.Error("Failed to unlock achievement", zap.Error(err),
			zap.String("player_id", playerID.String()), zap.String("achievement_id", achievementID.String()))
		return nil, err
	}

	s.logger.Info("Achievement unlocked",
		zap.String("player_id", playerID.String()),
		zap.String("achievement_id", achievementID.String()),
		zap.String("achievement_name", achievement.Name))

	return playerAchievement, nil
}

// UpdateAchievementProgress updates progress towards an achievement
func (s *Service) UpdateAchievementProgress(ctx context.Context, playerID, achievementID uuid.UUID, progress int) error {
	// Get or create progress record
	existingProgress, err := s.repo.GetPlayerAchievementProgress(ctx, playerID, achievementID)
	if err != nil && err.Error() != "progress not found" {
		return err
	}

	var progressRecord *models.AchievementProgress
	if existingProgress != nil {
		progressRecord = existingProgress
		progressRecord.Progress = progress
	} else {
		progressRecord = &models.AchievementProgress{
			ID:            uuid.New(),
			PlayerID:      playerID,
			AchievementID: achievementID,
			Progress:      progress,
			MaxProgress:   100, // Default, could be configurable per achievement
			IsCompleted:   false,
		}
	}

	// Check if achievement is completed
	if progress >= progressRecord.MaxProgress && !progressRecord.IsCompleted {
		progressRecord.IsCompleted = true
		progressRecord.CompletedAt = &time.Time{}
		*progressRecord.CompletedAt = time.Now()

		// Auto-unlock achievement if completed
		go func() {
			ctx := context.Background()
			_, err := s.UnlockAchievement(ctx, playerID, achievementID)
			if err != nil {
				s.logger.Error("Failed to auto-unlock achievement", zap.Error(err),
					zap.String("player_id", playerID.String()), zap.String("achievement_id", achievementID.String()))
			}
		}()
	}

	if err := s.repo.UpdateAchievementProgress(ctx, progressRecord); err != nil {
		s.logger.Error("Failed to update achievement progress", zap.Error(err),
			zap.String("player_id", playerID.String()), zap.String("achievement_id", achievementID.String()))
		return err
	}

	s.logger.Debug("Achievement progress updated",
		zap.String("player_id", playerID.String()),
		zap.String("achievement_id", achievementID.String()),
		zap.Int("progress", progress))

	return nil
}

// GetPlayerProfile retrieves a player's achievement profile
func (s *Service) GetPlayerProfile(ctx context.Context, playerID uuid.UUID) (*models.PlayerAchievementProfile, error) {
	profile, err := s.repo.GetPlayerProfile(ctx, playerID)
	if err != nil {
		s.logger.Error("Failed to get player profile", zap.Error(err), zap.String("player_id", playerID.String()))
		return nil, err
	}
	return profile, nil
}

// ProcessAchievementEvent processes events that can trigger achievement progress
func (s *Service) ProcessAchievementEvent(ctx context.Context, event *models.AchievementEvent) error {
	// Record the event
	if err := s.repo.RecordAchievementEvent(ctx, event); err != nil {
		s.logger.Error("Failed to record achievement event", zap.Error(err), zap.String("event_type", event.Type))
		return err
	}

	// Process event based on type
	switch event.Type {
	case "combat_win":
		return s.processCombatWinEvent(ctx, event)
	case "quest_complete":
		return s.processQuestCompleteEvent(ctx, event)
	case "level_up":
		return s.processLevelUpEvent(ctx, event)
	case "item_collect":
		return s.processItemCollectEvent(ctx, event)
	default:
		s.logger.Warn("Unknown achievement event type", zap.String("event_type", event.Type))
	}

	return nil
}

// Event processing methods

func (s *Service) processCombatWinEvent(ctx context.Context, event *models.AchievementEvent) error {
	playerID := event.PlayerID

	// Increment "Combat Wins" achievement progress
	winsAchievementID := uuid.MustParse("550e8400-e29b-41d4-a716-446655440001") // Would be configurable
	return s.UpdateAchievementProgress(ctx, playerID, winsAchievementID, 1)
}

func (s *Service) processQuestCompleteEvent(ctx context.Context, event *models.AchievementEvent) error {
	playerID := event.PlayerID

	// Increment "Quests Completed" achievement progress
	questsAchievementID := uuid.MustParse("550e8400-e29b-41d4-a716-446655440002") // Would be configurable
	return s.UpdateAchievementProgress(ctx, playerID, questsAchievementID, 1)
}

func (s *Service) processLevelUpEvent(ctx context.Context, event *models.AchievementEvent) error {
	playerID := event.PlayerID
	newLevel := int(event.Data["level"].(float64))

	// Check level-based achievements
	levelAchievements := map[int]uuid.UUID{
		10:  uuid.MustParse("550e8400-e29b-41d4-a716-446655440003"),
		25:  uuid.MustParse("550e8400-e29b-41d4-a716-446655440004"),
		50:  uuid.MustParse("550e8400-e29b-41d4-a716-446655440005"),
		100: uuid.MustParse("550e8400-e29b-41d4-a716-446655440006"),
	}

	if achievementID, exists := levelAchievements[newLevel]; exists {
		_, err := s.UnlockAchievement(ctx, playerID, achievementID)
		return err
	}

	return nil
}

func (s *Service) processItemCollectEvent(ctx context.Context, event *models.AchievementEvent) error {
	playerID := event.PlayerID
	itemType := event.Data["item_type"].(string)

	// Different achievements for different item types
	var achievementID uuid.UUID
	switch itemType {
	case "rare_weapon":
		achievementID = uuid.MustParse("550e8400-e29b-41d4-a716-446655440007")
	case "legendary_armor":
		achievementID = uuid.MustParse("550e8400-e29b-41d4-a716-446655440008")
	case "cyberware":
		achievementID = uuid.MustParse("550e8400-e29b-41d4-a716-446655440009")
	}

	if achievementID != uuid.Nil {
		return s.UpdateAchievementProgress(ctx, playerID, achievementID, 1)
	}

	return nil
}

// Helper methods

func (s *Service) validateAchievement(achievement *models.Achievement) error {
	if achievement.Name == "" {
		return fmt.Errorf("achievement name is required")
	}
	if len(achievement.Name) > 100 {
		return fmt.Errorf("achievement name too long")
	}
	if achievement.Description == "" {
		return fmt.Errorf("achievement description is required")
	}
	if achievement.Points < 0 {
		return fmt.Errorf("achievement points cannot be negative")
	}
	if achievement.Rarity == "" {
		achievement.Rarity = "common"
	}

	validRarities := []string{"common", "uncommon", "rare", "epic", "legendary"}
	valid := false
	for _, r := range validRarities {
		if achievement.Rarity == r {
			valid = true
			break
		}
	}
	if !valid {
		return fmt.Errorf("invalid rarity: %s", achievement.Rarity)
	}

	return nil
}

func (s *Service) generateRewards(achievement *models.Achievement) []models.Reward {
	var rewards []models.Reward

	// Base rewards based on achievement rarity
	switch achievement.Rarity {
	case "common":
		rewards = append(rewards, models.Reward{Type: "currency", ID: "credits", Amount: 100})
	case "uncommon":
		rewards = append(rewards, models.Reward{Type: "currency", ID: "credits", Amount: 250})
	case "rare":
		rewards = append(rewards, models.Reward{Type: "currency", ID: "credits", Amount: 500})
		rewards = append(rewards, models.Reward{Type: "item", ID: "rare_cosmetic", Amount: 1})
	case "epic":
		rewards = append(rewards, models.Reward{Type: "currency", ID: "credits", Amount: 1000})
		rewards = append(rewards, models.Reward{Type: "title", ID: "elite", Amount: 1})
	case "legendary":
		rewards = append(rewards, models.Reward{Type: "currency", ID: "credits", Amount: 2500})
		rewards = append(rewards, models.Reward{Type: "skin", ID: "legendary", Amount: 1})
	}

	// Add points as currency reward
	rewards = append(rewards, models.Reward{Type: "currency", ID: "achievement_points", Amount: achievement.Points})

	return rewards
}

// Bulk operations for efficiency

// BulkUpdateProgress updates progress for multiple achievements at once
func (s *Service) BulkUpdateProgress(ctx context.Context, updates []struct {
	PlayerID      uuid.UUID
	AchievementID uuid.UUID
	Progress      int
}) error {
	for _, update := range updates {
		if err := s.UpdateAchievementProgress(ctx, update.PlayerID, update.AchievementID, update.Progress); err != nil {
			s.logger.Error("Failed bulk progress update", zap.Error(err),
				zap.String("player_id", update.PlayerID.String()),
				zap.String("achievement_id", update.AchievementID.String()))
			// Continue with other updates even if one fails
		}
	}
	return nil
}

// ImportAchievements imports achievements from YAML data
func (s *Service) ImportAchievements(ctx context.Context, achievements []*models.Achievement) error {
	for _, achievement := range achievements {
		if _, err := s.CreateAchievement(ctx, achievement); err != nil {
			s.logger.Error("Failed to import achievement", zap.Error(err), zap.String("name", achievement.Name))
			// Continue importing other achievements
		}
	}
	return nil
}
