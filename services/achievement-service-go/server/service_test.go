// Unit tests for AchievementServiceLogic
// Issue: #391
// PERFORMANCE: Tests run without external dependencies

package server

import (
	"context"
	"errors"
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
	"go.uber.org/zap/zaptest"
)

// MockAchievementRepository is a mock implementation for testing
type MockAchievementRepository struct {
	mock.Mock
}

func (m *MockAchievementRepository) GetAchievements(ctx context.Context, playerID string, limit, offset int) ([]*Achievement, error) {
	args := m.Called(ctx, playerID, limit, offset)
	return args.Get(0).([]*Achievement), args.Error(1)
}

func (m *MockAchievementRepository) GetAchievement(ctx context.Context, achievementID, playerID string) (*Achievement, error) {
	args := m.Called(ctx, achievementID, playerID)
	return args.Get(0).(*Achievement), args.Error(1)
}

func (m *MockAchievementRepository) UnlockAchievement(ctx context.Context, playerID, achievementID string) error {
	args := m.Called(ctx, playerID, achievementID)
	return args.Error(0)
}

func TestAchievementServiceLogic_GetAchievements(t *testing.T) {
	tests := []struct {
		name           string
		playerID       string
		limit          int
		offset         int
		mockSetup      func(*MockAchievementRepository)
		expectedError  bool
		expectedCount  int
	}{
		{
			name:     "successful retrieval",
			playerID: "player-123",
			limit:    10,
			offset:   0,
			mockSetup: func(mock *MockAchievementRepository) {
				mockAchievements := []*Achievement{
					{
						ID:          "ach-1",
						Name:        "First Achievement",
						Description: "Test achievement",
						Rarity:      "common",
						Points:      10,
						IsUnlocked:  true,
						CreatedAt:   time.Now(),
						UpdatedAt:   time.Now(),
					},
					{
						ID:          "ach-2",
						Name:        "Second Achievement",
						Description: "Another test achievement",
						Rarity:      "rare",
						Points:      25,
						IsUnlocked:  false,
						CreatedAt:   time.Now(),
						UpdatedAt:   time.Now(),
					},
				}
				mock.On("GetAchievements", mock.Anything, "player-123", 10, 0).Return(mockAchievements, nil)
			},
			expectedError: false,
			expectedCount: 2,
		},
		{
			name:     "empty player ID",
			playerID: "",
			limit:    10,
			offset:   0,
			mockSetup: func(mock *MockAchievementRepository) {
				// No setup needed
			},
			expectedError:  true,
			expectedCount:  0,
		},
		{
			name:     "repository error",
			playerID: "player-123",
			limit:    10,
			offset:   0,
			mockSetup: func(mock *MockAchievementRepository) {
				mock.On("GetAchievements", mock.Anything, "player-123", 10, 0).Return([]*Achievement{}, errors.New("database error"))
			},
			expectedError: true,
			expectedCount: 0,
		},
		{
			name:     "default limits",
			playerID: "player-123",
			limit:    0, // Should use default 50
			offset:   -1, // Should use default 0
			mockSetup: func(mock *MockAchievementRepository) {
				mock.On("GetAchievements", mock.Anything, "player-123", 50, 0).Return([]*Achievement{}, nil)
			},
			expectedError: false,
			expectedCount: 0,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			// Setup
			mockRepo := new(MockAchievementRepository)
			tt.mockSetup(mockRepo)

			service := &AchievementServiceLogic{
				repo:   mockRepo,
				logger: zaptest.NewLogger(t),
			}

			// Execute
			result, err := service.GetAchievements(context.Background(), tt.playerID, tt.limit, tt.offset)

			// Assert
			if tt.expectedError {
				assert.Error(t, err)
				assert.Nil(t, result)
			} else {
				assert.NoError(t, err)
				assert.NotNil(t, result)
				assert.Len(t, result, tt.expectedCount)
			}

			mockRepo.AssertExpectations(t)
		})
	}
}

func TestAchievementServiceLogic_GetAchievement(t *testing.T) {
	tests := []struct {
		name            string
		achievementID   string
		playerID        string
		mockSetup       func(*MockAchievementRepository)
		expectedError   bool
		expectedNil     bool
	}{
		{
			name:          "successful retrieval",
			achievementID: "ach-123",
			playerID:      "player-456",
			mockSetup: func(mock *MockAchievementRepository) {
				mockAchievement := &Achievement{
					ID:          "ach-123",
					Name:        "Test Achievement",
					Description: "A test achievement",
					Rarity:      "common",
					Points:      10,
					IsUnlocked:  true,
					CreatedAt:   time.Now(),
					UpdatedAt:   time.Now(),
				}
				mock.On("GetAchievement", mock.Anything, "ach-123", "player-456").Return(mockAchievement, nil)
			},
			expectedError: false,
			expectedNil:   false,
		},
		{
			name:          "achievement not found",
			achievementID: "ach-999",
			playerID:      "player-456",
			mockSetup: func(mock *MockAchievementRepository) {
				mock.On("GetAchievement", mock.Anything, "ach-999", "player-456").Return((*Achievement)(nil), nil)
			},
			expectedError: false,
			expectedNil:   true,
		},
		{
			name:          "empty achievement ID",
			achievementID: "",
			playerID:      "player-456",
			mockSetup: func(mock *MockAchievementRepository) {
				// No setup needed
			},
			expectedError: true,
			expectedNil:   true,
		},
		{
			name:          "empty player ID",
			achievementID: "ach-123",
			playerID:      "",
			mockSetup: func(mock *MockAchievementRepository) {
				// No setup needed
			},
			expectedError: true,
			expectedNil:   true,
		},
		{
			name:          "repository error",
			achievementID: "ach-123",
			playerID:      "player-456",
			mockSetup: func(mock *MockAchievementRepository) {
				mock.On("GetAchievement", mock.Anything, "ach-123", "player-456").Return((*Achievement)(nil), errors.New("database error"))
			},
			expectedError: true,
			expectedNil:   true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			// Setup
			mockRepo := new(MockAchievementRepository)
			tt.mockSetup(mockRepo)

			service := &AchievementServiceLogic{
				repo:   mockRepo,
				logger: zaptest.NewLogger(t),
			}

			// Execute
			result, err := service.GetAchievement(context.Background(), tt.achievementID, tt.playerID)

			// Assert
			if tt.expectedError {
				assert.Error(t, err)
				if tt.expectedNil {
					assert.Nil(t, result)
				}
			} else {
				assert.NoError(t, err)
				if tt.expectedNil {
					assert.Nil(t, result)
				} else {
					assert.NotNil(t, result)
					assert.Equal(t, tt.achievementID, result.ID)
				}
			}

			mockRepo.AssertExpectations(t)
		})
	}
}

func TestAchievementServiceLogic_UnlockAchievement(t *testing.T) {
	tests := []struct {
		name          string
		playerID      string
		achievementID string
		mockSetup     func(*MockAchievementRepository)
		expectedError bool
	}{
		{
			name:          "successful unlock",
			playerID:      "player-123",
			achievementID: "ach-456",
			mockSetup: func(mock *MockAchievementRepository) {
				mock.On("UnlockAchievement", mock.Anything, "player-123", "ach-456").Return(nil)
			},
			expectedError: false,
		},
		{
			name:          "already unlocked",
			playerID:      "player-123",
			achievementID: "ach-456",
			mockSetup: func(mock *MockAchievementRepository) {
				mock.On("UnlockAchievement", mock.Anything, "player-123", "ach-456").Return(nil)
			},
			expectedError: false,
		},
		{
			name:          "empty player ID",
			playerID:      "",
			achievementID: "ach-456",
			mockSetup: func(mock *MockAchievementRepository) {
				// No setup needed
			},
			expectedError: true,
		},
		{
			name:          "empty achievement ID",
			playerID:      "player-123",
			achievementID: "",
			mockSetup: func(mock *MockAchievementRepository) {
				// No setup needed
			},
			expectedError: true,
		},
		{
			name:          "repository error",
			playerID:      "player-123",
			achievementID: "ach-456",
			mockSetup: func(mock *MockAchievementRepository) {
				mock.On("UnlockAchievement", mock.Anything, "player-123", "ach-456").Return(errors.New("database error"))
			},
			expectedError: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			// Setup
			mockRepo := new(MockAchievementRepository)
			tt.mockSetup(mockRepo)

			service := &AchievementServiceLogic{
				repo:   mockRepo,
				logger: zaptest.NewLogger(t),
			}

			// Execute
			err := service.UnlockAchievement(context.Background(), tt.playerID, tt.achievementID)

			// Assert
			if tt.expectedError {
				assert.Error(t, err)
			} else {
				assert.NoError(t, err)
			}

			mockRepo.AssertExpectations(t)
		})
	}
}

// Benchmark tests for performance validation
func BenchmarkAchievementServiceLogic_GetAchievements(b *testing.B) {
	mockRepo := new(MockAchievementRepository)
	mockAchievements := make([]*Achievement, 50)
	for i := 0; i < 50; i++ {
		mockAchievements[i] = &Achievement{
			ID:          uuid.New().String(),
			Name:        "Benchmark Achievement",
			Description: "Performance test achievement",
			Rarity:      "common",
			Points:      10,
			IsUnlocked:  true,
			CreatedAt:   time.Now(),
			UpdatedAt:   time.Now(),
		}
	}
	mockRepo.On("GetAchievements", mock.Anything, "player-123", 50, 0).Return(mockAchievements, nil)

	service := &AchievementServiceLogic{
		repo:   mockRepo,
		logger: zaptest.NewLogger(b),
	}

	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		_, err := service.GetAchievements(context.Background(), "player-123", 50, 0)
		if err != nil {
			b.Fatal(err)
		}
	}
}

func BenchmarkAchievementServiceLogic_UnlockAchievement(b *testing.B) {
	mockRepo := new(MockAchievementRepository)
	mockRepo.On("UnlockAchievement", mock.Anything, "player-123", "ach-456").Return(nil)

	service := &AchievementServiceLogic{
		repo:   mockRepo,
		logger: zaptest.NewLogger(b),
	}

	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		err := service.UnlockAchievement(context.Background(), "player-123", "ach-456")
		if err != nil {
			b.Fatal(err)
		}
	}
}
