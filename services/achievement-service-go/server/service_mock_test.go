// Issue: #391
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/achievement-service-go/pkg/api"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

// MockRepository is a mock implementation of RepositoryInterface
type MockRepository struct {
	mock.Mock
}

func (m *MockRepository) Close() error {
	args := m.Called()
	return args.Error(0)
}

// Test Service with mock repository
func TestService_WithMockRepository(t *testing.T) {
	mockRepo := new(MockRepository)
	service := NewService(mockRepo)

	t.Run("Service creation with mock", func(t *testing.T) {
		assert.NotNil(t, service)
		assert.Equal(t, mockRepo, service.repo)
	})

	t.Run("ClaimAchievementReward with mock", func(t *testing.T) {
		ctx := context.Background()
		// No repository calls in current implementation
		result, err := service.ClaimAchievementReward(ctx, uuid.New().String(), uuid.New().String())

		assert.NoError(t, err)
		assert.NotNil(t, result)
		assert.IsType(t, &api.ClaimAchievementRewardOK{}, result)

		// Verify no unexpected calls were made
		mockRepo.AssertExpectations(t)
	})

	t.Run("GetAchievementDetails with mock", func(t *testing.T) {
		ctx := context.Background()
		// No repository calls in current implementation
		result, err := service.GetAchievementDetails(ctx, uuid.New().String())

		assert.NoError(t, err)
		assert.NotNil(t, result)
		assert.IsType(t, &api.AchievementDetails{}, result)

		// Verify no unexpected calls were made
		mockRepo.AssertExpectations(t)
	})

	t.Run("GetAchievements with mock", func(t *testing.T) {
		ctx := context.Background()
		params := api.GetAchievementsParams{}

		// No repository calls in current implementation
		result, err := service.GetAchievements(ctx, params)

		assert.NoError(t, err)
		assert.NotNil(t, result)
		assert.IsType(t, &api.GetAchievementsOK{}, result)

		// Verify no unexpected calls were made
		mockRepo.AssertExpectations(t)
	})

	t.Run("GetPlayerProgress with mock", func(t *testing.T) {
		ctx := context.Background()
		params := api.GetPlayerProgressParams{}

		// No repository calls in current implementation
		result, err := service.GetPlayerProgress(ctx, uuid.New().String(), params)

		assert.NoError(t, err)
		assert.NotNil(t, result)
		assert.IsType(t, &api.GetPlayerProgressOK{}, result)

		// Verify no unexpected calls were made
		mockRepo.AssertExpectations(t)
	})

	t.Run("GetPlayerTitles with mock", func(t *testing.T) {
		ctx := context.Background()
		// No repository calls in current implementation
		result, err := service.GetPlayerTitles(ctx, uuid.New().String())

		assert.NoError(t, err)
		assert.NotNil(t, result)
		assert.IsType(t, &api.GetPlayerTitlesOK{}, result)

		// Verify no unexpected calls were made
		mockRepo.AssertExpectations(t)
	})

	t.Run("SetActiveTitle with mock", func(t *testing.T) {
		ctx := context.Background()
		req := &api.SetActiveTitleReq{
			TitleID: uuid.New(),
		}

		// No repository calls in current implementation
		result, err := service.SetActiveTitle(ctx, uuid.New().String(), req)

		assert.NoError(t, err)
		assert.NotNil(t, result)
		assert.IsType(t, &api.PlayerTitle{}, result)

		// Verify no unexpected calls were made
		mockRepo.AssertExpectations(t)
	})
}

// Test error scenarios with mock
func TestService_ErrorScenarios(t *testing.T) {
	mockRepo := new(MockRepository)
	service := NewService(mockRepo)

	t.Run("Repository close error", func(t *testing.T) {
		ctx := context.Background()
		mockRepo.On("Close").Return(assert.AnError).Once()
		
		err := service.repo.Close()
		assert.Error(t, err)
		assert.Equal(t, assert.AnError, err)
		
		mockRepo.AssertExpectations(t)
	})

	t.Run("Repository close success", func(t *testing.T) {
		mockRepo.On("Close").Return(nil).Once()
		
		err := service.repo.Close()
		assert.NoError(t, err)
		
		mockRepo.AssertExpectations(t)
	})
}

// Benchmark with mock repository
func BenchmarkService_WithMock(b *testing.B) {
	mockRepo := new(MockRepository)
	service := NewService(mockRepo)

	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		ctx := context.Background()
		_, err := service.ClaimAchievementReward(ctx, uuid.New().String(), uuid.New().String())
		if err != nil {
			b.Fatal(err)
		}
	}
}
