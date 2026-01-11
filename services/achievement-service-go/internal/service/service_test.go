//go:build unit

package service

import (
	"context"
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"

	"necpgame/services/achievement-service-go/internal/models"
	"necpgame/services/achievement-service-go/internal/repository"
)

// Mock repository for testing
type mockAchievementRepository struct {
	mock.Mock
}

func (m *mockAchievementRepository) Create(ctx context.Context, achievement *models.Achievement) error {
	args := m.Called(ctx, achievement)
	return args.Error(0)
}

func (m *mockAchievementRepository) GetByID(ctx context.Context, id string) (*models.Achievement, error) {
	args := m.Called(ctx, id)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Achievement), args.Error(1)
}

func (m *mockAchievementRepository) Update(ctx context.Context, achievement *models.Achievement) error {
	args := m.Called(ctx, achievement)
	return args.Error(0)
}

func (m *mockAchievementRepository) Delete(ctx context.Context, id string) error {
	args := m.Called(ctx, id)
	return args.Error(0)
}

func (m *mockAchievementRepository) List(ctx context.Context, limit, offset int) ([]*models.Achievement, error) {
	args := m.Called(ctx, limit, offset)
	return args.Get(0).([]*models.Achievement), args.Error(1)
}

func TestAchievementService_CreateAchievement(t *testing.T) {
	// Setup
	mockRepo := &mockAchievementRepository{}
	service := NewAchievementService(mockRepo)

	ctx := context.Background()
	achievement := &models.Achievement{
		Name:        "Test Achievement",
		Description: "Test Description",
		Type:        models.AchievementTypeProgress,
		CreatedAt:   time.Now(),
	}

	// Mock expectations
	mockRepo.On("Create", ctx, achievement).Return(nil)

	// Execute
	err := service.CreateAchievement(ctx, achievement)

	// Assert
	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestAchievementService_GetAchievement(t *testing.T) {
	// Setup
	mockRepo := &mockAchievementRepository{}
	service := NewAchievementService(mockRepo)

	ctx := context.Background()
	achievementID := "test-achievement-123"
	expectedAchievement := &models.Achievement{
		ID:          achievementID,
		Name:        "Test Achievement",
		Description: "Test Description",
		Type:        models.AchievementTypeProgress,
	}

	// Mock expectations
	mockRepo.On("GetByID", ctx, achievementID).Return(expectedAchievement, nil)

	// Execute
	achievement, err := service.GetAchievement(ctx, achievementID)

	// Assert
	assert.NoError(t, err)
	assert.NotNil(t, achievement)
	assert.Equal(t, achievementID, achievement.ID)
	assert.Equal(t, "Test Achievement", achievement.Name)
	mockRepo.AssertExpectations(t)
}

func TestAchievementService_GetAchievement_NotFound(t *testing.T) {
	// Setup
	mockRepo := &mockAchievementRepository{}
	service := NewAchievementService(mockRepo)

	ctx := context.Background()
	achievementID := "non-existent-id"

	// Mock expectations - return nil achievement and error
	mockRepo.On("GetByID", ctx, achievementID).Return((*models.Achievement)(nil), models.ErrAchievementNotFound)

	// Execute
	achievement, err := service.GetAchievement(ctx, achievementID)

	// Assert
	assert.Error(t, err)
	assert.Nil(t, achievement)
	assert.Equal(t, models.ErrAchievementNotFound, err)
	mockRepo.AssertExpectations(t)
}

func TestAchievementService_UpdateAchievement(t *testing.T) {
	// Setup
	mockRepo := &mockAchievementRepository{}
	service := NewAchievementService(mockRepo)

	ctx := context.Background()
	achievement := &models.Achievement{
		ID:          "test-achievement-123",
		Name:        "Updated Achievement",
		Description: "Updated Description",
		Type:        models.AchievementTypeProgress,
		UpdatedAt:   time.Now(),
	}

	// Mock expectations
	mockRepo.On("Update", ctx, achievement).Return(nil)

	// Execute
	err := service.UpdateAchievement(ctx, achievement)

	// Assert
	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestAchievementService_ListAchievements(t *testing.T) {
	// Setup
	mockRepo := &mockAchievementRepository{}
	service := NewAchievementService(mockRepo)

	ctx := context.Background()
	limit := 10
	offset := 0
	expectedAchievements := []*models.Achievement{
		{
			ID:          "achievement-1",
			Name:        "Achievement 1",
			Description: "Description 1",
			Type:        models.AchievementTypeProgress,
		},
		{
			ID:          "achievement-2",
			Name:        "Achievement 2",
			Description: "Description 2",
			Type:        models.AchievementTypeMilestone,
		},
	}

	// Mock expectations
	mockRepo.On("List", ctx, limit, offset).Return(expectedAchievements, nil)

	// Execute
	achievements, err := service.ListAchievements(ctx, limit, offset)

	// Assert
	assert.NoError(t, err)
	assert.Len(t, achievements, 2)
	assert.Equal(t, "achievement-1", achievements[0].ID)
	assert.Equal(t, "achievement-2", achievements[1].ID)
	mockRepo.AssertExpectations(t)
}

// Benchmark tests for performance validation
func BenchmarkAchievementService_GetAchievement(b *testing.B) {
	// Setup
	mockRepo := &mockAchievementRepository{}
	service := NewAchievementService(mockRepo)

	ctx := context.Background()
	achievementID := "test-achievement-123"
	achievement := &models.Achievement{
		ID:          achievementID,
		Name:        "Test Achievement",
		Description: "Test Description",
		Type:        models.AchievementTypeProgress,
	}

	// Mock expectations - will be called b.N times
	mockRepo.On("GetByID", ctx, achievementID).Return(achievement, nil)

	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		_, _ = service.GetAchievement(ctx, achievementID)
	}
}