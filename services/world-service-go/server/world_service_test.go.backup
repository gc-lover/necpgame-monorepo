package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/world-service-go/models"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

type mockWorldRepository struct {
	mock.Mock
}

func (m *mockWorldRepository) CreateResetExecution(ctx context.Context, execution *models.ResetExecution) error {
	args := m.Called(ctx, execution)
	return args.Error(0)
}

func (m *mockWorldRepository) GetResetExecution(ctx context.Context, id uuid.UUID) (*models.ResetExecution, error) {
	args := m.Called(ctx, id)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.ResetExecution), args.Error(1)
}

func (m *mockWorldRepository) UpdateResetExecution(ctx context.Context, execution *models.ResetExecution) error {
	args := m.Called(ctx, execution)
	return args.Error(0)
}

func (m *mockWorldRepository) GetLastReset(ctx context.Context, resetType models.ResetType) (*time.Time, error) {
	args := m.Called(ctx, resetType)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*time.Time), args.Error(1)
}

func (m *mockWorldRepository) GetResetSchedule(ctx context.Context) (*models.ResetSchedule, error) {
	args := m.Called(ctx)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.ResetSchedule), args.Error(1)
}

func (m *mockWorldRepository) UpdateResetSchedule(ctx context.Context, schedule *models.ResetSchedule) error {
	args := m.Called(ctx, schedule)
	return args.Error(0)
}

func (m *mockWorldRepository) GetQuestPool(ctx context.Context, poolType models.QuestPoolType, playerLevel *int) ([]models.QuestPoolEntry, error) {
	args := m.Called(ctx, poolType, playerLevel)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.QuestPoolEntry), args.Error(1)
}

func (m *mockWorldRepository) AssignQuest(ctx context.Context, playerID, questID uuid.UUID, poolType models.QuestPoolType) error {
	args := m.Called(ctx, playerID, questID, poolType)
	return args.Error(0)
}

func (m *mockWorldRepository) GetPlayerQuests(ctx context.Context, playerID uuid.UUID, poolType *models.QuestPoolType) ([]models.PlayerQuest, error) {
	args := m.Called(ctx, playerID, poolType)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.PlayerQuest), args.Error(1)
}

func (m *mockWorldRepository) GetPlayerLoginRewards(ctx context.Context, playerID uuid.UUID) (*models.PlayerLoginRewards, error) {
	args := m.Called(ctx, playerID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.PlayerLoginRewards), args.Error(1)
}

func (m *mockWorldRepository) ClaimLoginReward(ctx context.Context, playerID uuid.UUID, rewardType models.LoginRewardType, dayNumber int) error {
	args := m.Called(ctx, playerID, rewardType, dayNumber)
	return args.Error(0)
}

func (m *mockWorldRepository) GetLoginStreak(ctx context.Context, playerID uuid.UUID) (*models.LoginStreak, error) {
	args := m.Called(ctx, playerID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.LoginStreak), args.Error(1)
}

func (m *mockWorldRepository) UpdateLoginStreak(ctx context.Context, streak *models.LoginStreak) error {
	args := m.Called(ctx, streak)
	return args.Error(0)
}

func (m *mockWorldRepository) CreateResetEvent(ctx context.Context, event *models.ResetEvent) error {
	args := m.Called(ctx, event)
	return args.Error(0)
}

func (m *mockWorldRepository) GetResetEvents(ctx context.Context, resetType *models.ResetType, limit, offset int) ([]models.ResetEvent, int, error) {
	args := m.Called(ctx, resetType, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Get(1).(int), args.Error(2)
	}
	return args.Get(0).([]models.ResetEvent), args.Get(1).(int), args.Error(2)
}

func (m *mockWorldRepository) GetTravelEvent(ctx context.Context, id uuid.UUID) (*models.TravelEvent, error) {
	args := m.Called(ctx, id)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.TravelEvent), args.Error(1)
}

func (m *mockWorldRepository) GetTravelEventsByEpoch(ctx context.Context, epochID string) ([]models.TravelEvent, error) {
	args := m.Called(ctx, epochID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.TravelEvent), args.Error(1)
}

func (m *mockWorldRepository) GetAvailableTravelEvents(ctx context.Context, zoneID uuid.UUID, epochID *string) ([]models.TravelEvent, error) {
	args := m.Called(ctx, zoneID, epochID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.TravelEvent), args.Error(1)
}

func (m *mockWorldRepository) CreateTravelEventInstance(ctx context.Context, instance *models.TravelEventInstance) error {
	args := m.Called(ctx, instance)
	return args.Error(0)
}

func (m *mockWorldRepository) GetTravelEventInstance(ctx context.Context, id uuid.UUID) (*models.TravelEventInstance, error) {
	args := m.Called(ctx, id)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.TravelEventInstance), args.Error(1)
}

func (m *mockWorldRepository) UpdateTravelEventInstance(ctx context.Context, instance *models.TravelEventInstance) error {
	args := m.Called(ctx, instance)
	return args.Error(0)
}

func (m *mockWorldRepository) GetCharacterTravelEventCooldowns(ctx context.Context, characterID uuid.UUID) ([]models.TravelEventCooldown, error) {
	args := m.Called(ctx, characterID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.TravelEventCooldown), args.Error(1)
}

type mockEventBus struct {
	mock.Mock
}

func (m *mockEventBus) PublishEvent(ctx context.Context, eventType string, payload map[string]interface{}) error {
	args := m.Called(ctx, eventType, payload)
	return args.Error(0)
}

func TestWorldService_ExecuteDailyReset(t *testing.T) {
	repo := new(mockWorldRepository)
	eventBus := new(mockEventBus)
	logger := GetLogger()

	service := NewWorldService(repo, logger, eventBus)

	repo.On("CreateResetExecution", mock.Anything, mock.AnythingOfType("*models.ResetExecution")).Return(nil)
	repo.On("UpdateResetExecution", mock.Anything, mock.AnythingOfType("*models.ResetExecution")).Return(nil).Maybe()
	repo.On("CreateResetEvent", mock.Anything, mock.AnythingOfType("*models.ResetEvent")).Return(nil).Maybe()
	eventBus.On("PublishEvent", mock.Anything, "world:reset:completed", mock.Anything).Return(nil).Maybe()

	ctx := context.Background()
	execution, err := service.ExecuteDailyReset(ctx)

	assert.NoError(t, err)
	assert.NotNil(t, execution)
	assert.Equal(t, models.ResetTypeDaily, execution.ResetType)
	assert.Equal(t, models.ResetStatusInProgress, execution.Status)
	
	time.Sleep(200 * time.Millisecond)
}

func TestWorldService_GetQuestPool(t *testing.T) {
	repo := new(mockWorldRepository)
	eventBus := new(mockEventBus)
	logger := GetLogger()

	service := NewWorldService(repo, logger, eventBus)

	entries := []models.QuestPoolEntry{
		{
			QuestID:  uuid.New(),
			Weight:   10,
			MinLevel: 1,
			IsActive: true,
		},
	}

	repo.On("GetQuestPool", mock.Anything, models.QuestPoolTypeDaily, (*int)(nil)).Return(entries, nil)

	ctx := context.Background()
	pool, err := service.GetQuestPool(ctx, models.QuestPoolTypeDaily, nil)

	assert.NoError(t, err)
	assert.NotNil(t, pool)
	assert.Equal(t, models.QuestPoolTypeDaily, pool.PoolType)
	assert.Equal(t, 1, pool.Total)
	repo.AssertExpectations(t)
}

func TestWorldService_AssignQuestToPlayer(t *testing.T) {
	repo := new(mockWorldRepository)
	eventBus := new(mockEventBus)
	logger := GetLogger()

	service := NewWorldService(repo, logger, eventBus)

	playerID := uuid.New()
	questID := uuid.New()
	poolType := models.QuestPoolTypeDaily

	repo.On("AssignQuest", mock.Anything, playerID, questID, poolType).Return(nil)
	repo.On("GetPlayerQuests", mock.Anything, playerID, &poolType).Return([]models.PlayerQuest{
		{
			ID:         uuid.New(),
			PlayerID:   playerID,
			QuestID:    questID,
			PoolType:   poolType,
			AssignedAt: time.Now(),
		},
	}, nil)
	eventBus.On("PublishEvent", mock.Anything, "world:quest:assigned", mock.Anything).Return(nil)

	ctx := context.Background()
	quest, err := service.AssignQuestToPlayer(ctx, playerID, questID, poolType)

	assert.NoError(t, err)
	assert.NotNil(t, quest)
	assert.Equal(t, questID, quest.QuestID)
	repo.AssertExpectations(t)
	eventBus.AssertExpectations(t)
}

func TestWorldService_GetPlayerLoginRewards(t *testing.T) {
	repo := new(mockWorldRepository)
	eventBus := new(mockEventBus)
	logger := GetLogger()

	service := NewWorldService(repo, logger, eventBus)

	playerID := uuid.New()
	rewards := &models.PlayerLoginRewards{
		PlayerID:        playerID,
		AvailableRewards: []models.LoginReward{},
		ClaimedRewards:   []models.LoginReward{},
		StreakDays:      5,
	}

	repo.On("GetPlayerLoginRewards", mock.Anything, playerID).Return(rewards, nil)

	ctx := context.Background()
	result, err := service.GetPlayerLoginRewards(ctx, playerID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, playerID, result.PlayerID)
	repo.AssertExpectations(t)
}

