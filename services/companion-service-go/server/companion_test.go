package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/companion-service-go/models"
	"github.com/stretchr/testify/assert"
)

type MockCompanionRepository struct{}

func (m *MockCompanionRepository) GetCompanion(ctx context.Context, characterID, companionID uuid.UUID) (*models.Companion, error) {
	return &models.Companion{
		ID:          companionID,
		OwnerID:     characterID,
		Name:        "Test Companion",
		Type:        models.CompanionTypeDrone,
		Level:       5,
		Experience:  1000,
		IsActive:    false,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}, nil
}

func (m *MockCompanionRepository) ListCompanions(ctx context.Context, characterID uuid.UUID, limit, offset int) ([]models.Companion, error) {
	return []models.Companion{}, nil
}

func (m *MockCompanionRepository) CreateCompanion(ctx context.Context, companion *models.Companion) error {
	return nil
}

func (m *MockCompanionRepository) UpdateCompanion(ctx context.Context, companion *models.Companion) error {
	return nil
}

func (m *MockCompanionRepository) DeleteCompanion(ctx context.Context, characterID, companionID uuid.UUID) error {
	return nil
}

func (m *MockCompanionRepository) ActivateCompanion(ctx context.Context, characterID, companionID uuid.UUID) error {
	return nil
}

func (m *MockCompanionRepository) DeactivateCompanion(ctx context.Context, characterID, companionID uuid.UUID) error {
	return nil
}

func (m *MockCompanionRepository) AddExperience(ctx context.Context, companionID uuid.UUID, amount int) error {
	return nil
}

func (m *MockCompanionRepository) LevelUp(ctx context.Context, companionID uuid.UUID, newLevel int) error {
	return nil
}

func (m *MockCompanionRepository) GetSkills(ctx context.Context, companionID uuid.UUID) ([]models.CompanionSkill, error) {
	return []models.CompanionSkill{}, nil
}

func (m *MockCompanionRepository) UnlockSkill(ctx context.Context, companionID, skillID uuid.UUID) error {
	return nil
}

func (m *MockCompanionRepository) Close() error {
	return nil
}

func TestNewCompanionService(t *testing.T) {
	repo := &MockCompanionRepository{}
	service := NewCompanionService(repo)
	assert.NotNil(t, service)
}

func TestGetCompanion(t *testing.T) {
	repo := &MockCompanionRepository{}
	service := NewCompanionService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	companionID := uuid.New()
	
	companion, err := service.GetCompanion(ctx, characterID, companionID)
	assert.NoError(t, err)
	assert.NotNil(t, companion)
	assert.Equal(t, characterID, companion.OwnerID)
	assert.Equal(t, models.CompanionTypeDrone, companion.Type)
}

func TestListCompanions(t *testing.T) {
	repo := &MockCompanionRepository{}
	service := NewCompanionService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	
	companions, err := service.ListCompanions(ctx, characterID, 50, 0)
	assert.NoError(t, err)
	assert.NotNil(t, companions)
}

func TestCreateCompanion(t *testing.T) {
	repo := &MockCompanionRepository{}
	service := NewCompanionService(repo)
	
	ctx := context.Background()
	req := &models.CreateCompanionRequest{
		OwnerID: uuid.New(),
		Name:    "New Companion",
		Type:    models.CompanionTypeBot,
	}
	
	companion, err := service.CreateCompanion(ctx, req)
	assert.NoError(t, err)
	assert.NotNil(t, companion)
	assert.Equal(t, req.Name, companion.Name)
	assert.Equal(t, req.Type, companion.Type)
}

func TestUpdateCompanion(t *testing.T) {
	repo := &MockCompanionRepository{}
	service := NewCompanionService(repo)
	
	ctx := context.Background()
	companionID := uuid.New()
	req := &models.UpdateCompanionRequest{
		Name: "Updated Companion",
	}
	
	companion, err := service.UpdateCompanion(ctx, companionID, req)
	assert.NoError(t, err)
	assert.NotNil(t, companion)
}

func TestDeleteCompanion(t *testing.T) {
	repo := &MockCompanionRepository{}
	service := NewCompanionService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	companionID := uuid.New()
	
	err := service.DeleteCompanion(ctx, characterID, companionID)
	assert.NoError(t, err)
}

func TestActivateCompanion(t *testing.T) {
	repo := &MockCompanionRepository{}
	service := NewCompanionService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	companionID := uuid.New()
	
	err := service.ActivateCompanion(ctx, characterID, companionID)
	assert.NoError(t, err)
}

func TestDeactivateCompanion(t *testing.T) {
	repo := &MockCompanionRepository{}
	service := NewCompanionService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	companionID := uuid.New()
	
	err := service.DeactivateCompanion(ctx, characterID, companionID)
	assert.NoError(t, err)
}

func TestAddExperience(t *testing.T) {
	repo := &MockCompanionRepository{}
	service := NewCompanionService(repo)
	
	ctx := context.Background()
	companionID := uuid.New()
	amount := 500
	
	companion, err := service.AddExperience(ctx, companionID, amount)
	assert.NoError(t, err)
	assert.NotNil(t, companion)
}

func TestLevelUp(t *testing.T) {
	repo := &MockCompanionRepository{}
	service := NewCompanionService(repo)
	
	ctx := context.Background()
	companionID := uuid.New()
	
	companion, err := service.LevelUp(ctx, companionID)
	assert.NoError(t, err)
	assert.NotNil(t, companion)
}

func TestGetSkills(t *testing.T) {
	repo := &MockCompanionRepository{}
	service := NewCompanionService(repo)
	
	ctx := context.Background()
	companionID := uuid.New()
	
	skills, err := service.GetSkills(ctx, companionID)
	assert.NoError(t, err)
	assert.NotNil(t, skills)
}

func TestUnlockSkill(t *testing.T) {
	repo := &MockCompanionRepository{}
	service := NewCompanionService(repo)
	
	ctx := context.Background()
	companionID := uuid.New()
	skillID := uuid.New()
	
	err := service.UnlockSkill(ctx, companionID, skillID)
	assert.NoError(t, err)
}

func TestCompanionServiceNilRepository(t *testing.T) {
	defer func() {
		if r := recover(); r == nil {
			t.Errorf("Expected panic with nil repository")
		}
	}()
	NewCompanionService(nil)
}

func TestCompanionLifecycle(t *testing.T) {
	repo := &MockCompanionRepository{}
	service := NewCompanionService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	
	createReq := &models.CreateCompanionRequest{
		OwnerID: characterID,
		Name:    "My Companion",
		Type:    models.CompanionTypeDrone,
	}
	
	companion, err := service.CreateCompanion(ctx, createReq)
	assert.NoError(t, err)
	assert.NotNil(t, companion)
	
	err = service.ActivateCompanion(ctx, characterID, companion.ID)
	assert.NoError(t, err)
	
	_, err = service.AddExperience(ctx, companion.ID, 1000)
	assert.NoError(t, err)
	
	_, err = service.LevelUp(ctx, companion.ID)
	assert.NoError(t, err)
	
	err = service.DeactivateCompanion(ctx, characterID, companion.ID)
	assert.NoError(t, err)
}

