package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/achievement-service-go/models"
	"github.com/stretchr/testify/assert"
)

type MockAchievementRepository struct{}

func (m *MockAchievementRepository) GetAchievement(ctx context.Context, characterID, achievementID uuid.UUID) (*models.PlayerAchievement, error) {
	return &models.PlayerAchievement{
		ID:            achievementID,
		CharacterID:   characterID,
		AchievementID: achievementID,
		Progress:      50,
		IsUnlocked:    false,
		UnlockedAt:    nil,
		CreatedAt:     time.Now(),
	}, nil
}

func (m *MockAchievementRepository) ListAchievements(ctx context.Context, characterID uuid.UUID, limit, offset int) ([]models.PlayerAchievement, error) {
	return []models.PlayerAchievement{}, nil
}

func (m *MockAchievementRepository) UpdateProgress(ctx context.Context, characterID, achievementID uuid.UUID, progress int) error {
	return nil
}

func (m *MockAchievementRepository) UnlockAchievement(ctx context.Context, characterID, achievementID uuid.UUID) error {
	return nil
}

func (m *MockAchievementRepository) GetStatistics(ctx context.Context, characterID uuid.UUID) (*models.AchievementStatistics, error) {
	return &models.AchievementStatistics{
		TotalAchievements:    100,
		UnlockedAchievements: 25,
		TotalPoints:          1000,
		EarnedPoints:         250,
	}, nil
}

func (m *MockAchievementRepository) Close() error {
	return nil
}

func TestNewAchievementService(t *testing.T) {
	repo := &MockAchievementRepository{}
	service := NewAchievementService(repo)
	assert.NotNil(t, service)
}

func TestGetAchievement(t *testing.T) {
	repo := &MockAchievementRepository{}
	service := NewAchievementService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	achievementID := uuid.New()
	
	achievement, err := service.GetAchievement(ctx, characterID, achievementID)
	assert.NoError(t, err)
	assert.NotNil(t, achievement)
	assert.Equal(t, characterID, achievement.CharacterID)
}

func TestListAchievements(t *testing.T) {
	repo := &MockAchievementRepository{}
	service := NewAchievementService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	
	achievements, err := service.ListAchievements(ctx, characterID, 50, 0)
	assert.NoError(t, err)
	assert.NotNil(t, achievements)
}

func TestUpdateProgress(t *testing.T) {
	repo := &MockAchievementRepository{}
	service := NewAchievementService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	achievementID := uuid.New()
	newProgress := 75
	
	err := service.UpdateProgress(ctx, characterID, achievementID, newProgress)
	assert.NoError(t, err)
}

func TestUnlockAchievement(t *testing.T) {
	repo := &MockAchievementRepository{}
	service := NewAchievementService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	achievementID := uuid.New()
	
	err := service.UnlockAchievement(ctx, characterID, achievementID)
	assert.NoError(t, err)
}

func TestGetStatistics(t *testing.T) {
	repo := &MockAchievementRepository{}
	service := NewAchievementService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	
	stats, err := service.GetStatistics(ctx, characterID)
	assert.NoError(t, err)
	assert.NotNil(t, stats)
	assert.Equal(t, 100, stats.TotalAchievements)
	assert.Equal(t, 25, stats.UnlockedAchievements)
}

func TestAchievementServiceNilRepository(t *testing.T) {
	defer func() {
		if r := recover(); r == nil {
			t.Errorf("Expected panic with nil repository")
		}
	}()
	NewAchievementService(nil)
}

func TestAchievementProgress(t *testing.T) {
	repo := &MockAchievementRepository{}
	service := NewAchievementService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	achievementID := uuid.New()
	
	achievement, err := service.GetAchievement(ctx, characterID, achievementID)
	assert.NoError(t, err)
	assert.Equal(t, 50, achievement.Progress)
	assert.False(t, achievement.IsUnlocked)
}

