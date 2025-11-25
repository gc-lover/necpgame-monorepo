package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/gameplay-service-go/models"
	"github.com/stretchr/testify/assert"
)

type MockProgressionRepository struct{}

func (m *MockProgressionRepository) GetProgression(ctx context.Context, characterID uuid.UUID) (*models.CharacterProgression, error) {
	return &models.CharacterProgression{
		CharacterID: characterID,
		Level:       10,
		Experience:  5000,
		NextLevelXP: 10000,
		SkillPoints: 5,
		UpdatedAt:   time.Now(),
	}, nil
}

func (m *MockProgressionRepository) AddExperience(ctx context.Context, characterID uuid.UUID, amount int) error {
	return nil
}

func (m *MockProgressionRepository) LevelUp(ctx context.Context, characterID uuid.UUID, newLevel int) error {
	return nil
}

func (m *MockProgressionRepository) GetSkills(ctx context.Context, characterID uuid.UUID) ([]models.Skill, error) {
	return []models.Skill{}, nil
}

func (m *MockProgressionRepository) UnlockSkill(ctx context.Context, characterID, skillID uuid.UUID) error {
	return nil
}

func (m *MockProgressionRepository) UpgradeSkill(ctx context.Context, characterID, skillID uuid.UUID, level int) error {
	return nil
}

func (m *MockProgressionRepository) GetStatistics(ctx context.Context, characterID uuid.UUID) (*models.ProgressionStats, error) {
	return &models.ProgressionStats{
		TotalPlayTime:    3600,
		QuestsCompleted:  25,
		AchievementsEarned: 10,
		SkillsUnlocked:   8,
	}, nil
}

func (m *MockProgressionRepository) Close() error {
	return nil
}

func TestNewProgressionService(t *testing.T) {
	repo := &MockProgressionRepository{}
	service := NewProgressionService(repo)
	assert.NotNil(t, service)
}

func TestGetProgression(t *testing.T) {
	repo := &MockProgressionRepository{}
	service := NewProgressionService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	
	progression, err := service.GetProgression(ctx, characterID)
	assert.NoError(t, err)
	assert.NotNil(t, progression)
	assert.Equal(t, characterID, progression.CharacterID)
	assert.Equal(t, 10, progression.Level)
}

func TestAddExperience(t *testing.T) {
	repo := &MockProgressionRepository{}
	service := NewProgressionService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	amount := 1000
	
	progression, err := service.AddExperience(ctx, characterID, amount)
	assert.NoError(t, err)
	assert.NotNil(t, progression)
}

func TestLevelUp(t *testing.T) {
	repo := &MockProgressionRepository{}
	service := NewProgressionService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	
	progression, err := service.LevelUp(ctx, characterID)
	assert.NoError(t, err)
	assert.NotNil(t, progression)
}

func TestGetSkills(t *testing.T) {
	repo := &MockProgressionRepository{}
	service := NewProgressionService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	
	skills, err := service.GetSkills(ctx, characterID)
	assert.NoError(t, err)
	assert.NotNil(t, skills)
}

func TestUnlockSkill(t *testing.T) {
	repo := &MockProgressionRepository{}
	service := NewProgressionService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	skillID := uuid.New()
	
	err := service.UnlockSkill(ctx, characterID, skillID)
	assert.NoError(t, err)
}

func TestUpgradeSkill(t *testing.T) {
	repo := &MockProgressionRepository{}
	service := NewProgressionService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	skillID := uuid.New()
	newLevel := 2
	
	err := service.UpgradeSkill(ctx, characterID, skillID, newLevel)
	assert.NoError(t, err)
}

func TestGetStatistics(t *testing.T) {
	repo := &MockProgressionRepository{}
	service := NewProgressionService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	
	stats, err := service.GetStatistics(ctx, characterID)
	assert.NoError(t, err)
	assert.NotNil(t, stats)
	assert.Equal(t, 25, stats.QuestsCompleted)
	assert.Equal(t, 10, stats.AchievementsEarned)
}

func TestProgressionServiceNilRepository(t *testing.T) {
	defer func() {
		if r := recover(); r == nil {
			t.Errorf("Expected panic with nil repository")
		}
	}()
	NewProgressionService(nil)
}

func TestExperienceToLevelUp(t *testing.T) {
	repo := &MockProgressionRepository{}
	service := NewProgressionService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	
	progression, err := service.GetProgression(ctx, characterID)
	assert.NoError(t, err)
	assert.Equal(t, 5000, progression.Experience)
	assert.Equal(t, 10000, progression.NextLevelXP)
	
	xpNeeded := progression.NextLevelXP - progression.Experience
	assert.Equal(t, 5000, xpNeeded)
}

func TestSkillProgression(t *testing.T) {
	repo := &MockProgressionRepository{}
	service := NewProgressionService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	skillID := uuid.New()
	
	err := service.UnlockSkill(ctx, characterID, skillID)
	assert.NoError(t, err)
	
	err = service.UpgradeSkill(ctx, characterID, skillID, 2)
	assert.NoError(t, err)
	
	err = service.UpgradeSkill(ctx, characterID, skillID, 3)
	assert.NoError(t, err)
}

