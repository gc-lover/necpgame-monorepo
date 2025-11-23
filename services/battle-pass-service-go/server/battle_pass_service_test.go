package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/battle-pass-service-go/models"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

type mockBattlePassRepository struct {
	mock.Mock
}

func (m *mockBattlePassRepository) CreateSeason(ctx context.Context, season *models.BattlePassSeason) error {
	args := m.Called(ctx, season)
	return args.Error(0)
}

func (m *mockBattlePassRepository) GetSeasonByID(ctx context.Context, id uuid.UUID) (*models.BattlePassSeason, error) {
	args := m.Called(ctx, id)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.BattlePassSeason), args.Error(1)
}

func (m *mockBattlePassRepository) GetCurrentSeason(ctx context.Context) (*models.BattlePassSeason, error) {
	args := m.Called(ctx)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.BattlePassSeason), args.Error(1)
}

func (m *mockBattlePassRepository) CreateReward(ctx context.Context, reward *models.BattlePassReward) error {
	args := m.Called(ctx, reward)
	return args.Error(0)
}

func (m *mockBattlePassRepository) GetRewardsBySeason(ctx context.Context, seasonID uuid.UUID) ([]models.BattlePassReward, error) {
	args := m.Called(ctx, seasonID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.BattlePassReward), args.Error(1)
}

func (m *mockBattlePassRepository) GetRewardByID(ctx context.Context, id uuid.UUID) (*models.BattlePassReward, error) {
	args := m.Called(ctx, id)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.BattlePassReward), args.Error(1)
}

func (m *mockBattlePassRepository) GetProgress(ctx context.Context, characterID, seasonID uuid.UUID) (*models.PlayerBattlePassProgress, error) {
	args := m.Called(ctx, characterID, seasonID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.PlayerBattlePassProgress), args.Error(1)
}

func (m *mockBattlePassRepository) CreateProgress(ctx context.Context, progress *models.PlayerBattlePassProgress) error {
	args := m.Called(ctx, progress)
	return args.Error(0)
}

func (m *mockBattlePassRepository) UpdateProgress(ctx context.Context, progress *models.PlayerBattlePassProgress) error {
	args := m.Called(ctx, progress)
	return args.Error(0)
}

func (m *mockBattlePassRepository) GetClaimedRewards(ctx context.Context, characterID, seasonID uuid.UUID) ([]uuid.UUID, error) {
	args := m.Called(ctx, characterID, seasonID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]uuid.UUID), args.Error(1)
}

func (m *mockBattlePassRepository) ClaimReward(ctx context.Context, characterID, rewardID uuid.UUID) error {
	args := m.Called(ctx, characterID, rewardID)
	return args.Error(0)
}

func (m *mockBattlePassRepository) CreateWeeklyChallenge(ctx context.Context, challenge *models.WeeklyChallenge) error {
	args := m.Called(ctx, challenge)
	return args.Error(0)
}

func (m *mockBattlePassRepository) GetWeeklyChallenges(ctx context.Context, seasonID uuid.UUID, weekNumber *int) ([]models.WeeklyChallenge, error) {
	args := m.Called(ctx, seasonID, weekNumber)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.WeeklyChallenge), args.Error(1)
}

func (m *mockBattlePassRepository) GetChallengeProgress(ctx context.Context, characterID, challengeID uuid.UUID) (*models.PlayerChallengeProgress, error) {
	args := m.Called(ctx, characterID, challengeID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.PlayerChallengeProgress), args.Error(1)
}

func (m *mockBattlePassRepository) CreateChallengeProgress(ctx context.Context, progress *models.PlayerChallengeProgress) error {
	args := m.Called(ctx, progress)
	return args.Error(0)
}

func (m *mockBattlePassRepository) UpdateChallengeProgress(ctx context.Context, progress *models.PlayerChallengeProgress) error {
	args := m.Called(ctx, progress)
	return args.Error(0)
}

func (m *mockBattlePassRepository) GetLevelRequirements(ctx context.Context, level int) (*models.LevelRequirements, error) {
	args := m.Called(ctx, level)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.LevelRequirements), args.Error(1)
}

type mockEventBus struct {
	mock.Mock
}

func (m *mockEventBus) PublishEvent(ctx context.Context, eventType string, payload map[string]interface{}) error {
	args := m.Called(ctx, eventType, payload)
	return args.Error(0)
}

func TestBattlePassService_GetCurrentSeason(t *testing.T) {
	repo := new(mockBattlePassRepository)
	eventBus := new(mockEventBus)
	logger := GetLogger()

	service := &BattlePassService{
		repo:     repo,
		cache:    nil,
		logger:   logger,
		eventBus: eventBus,
	}

	seasonID := uuid.New()
	season := &models.BattlePassSeason{
		ID:        seasonID,
		Name:      "Season 1",
		StartDate: time.Now().Add(-24 * time.Hour),
		EndDate:   time.Now().Add(30 * 24 * time.Hour),
		MaxLevel:  100,
		IsActive:  true,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	repo.On("GetCurrentSeason", mock.Anything).Return(season, nil)

	ctx := context.Background()
	result, err := service.GetCurrentSeason(ctx)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, seasonID, result.ID)
	assert.Equal(t, "Season 1", result.Name)
	repo.AssertExpectations(t)
}

func TestBattlePassService_AwardXP_LevelUp(t *testing.T) {
	repo := new(mockBattlePassRepository)
	eventBus := new(mockEventBus)
	logger := GetLogger()

	service := &BattlePassService{
		repo:     repo,
		cache:    nil,
		logger:   logger,
		eventBus: eventBus,
	}

	seasonID := uuid.New()
	characterID := uuid.New()
	season := &models.BattlePassSeason{
		ID:        seasonID,
		Name:      "Season 1",
		StartDate: time.Now().Add(-24 * time.Hour),
		EndDate:   time.Now().Add(30 * 24 * time.Hour),
		MaxLevel:  100,
		IsActive:  true,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	progress := &models.PlayerBattlePassProgress{
		ID:            uuid.New(),
		CharacterID:  characterID,
		SeasonID:     seasonID,
		Level:        1,
		XP:           0,
		XPToNextLevel: 1000,
		HasPremium:   false,
		CreatedAt:    time.Now(),
		UpdatedAt:    time.Now(),
	}

	levelReqs3 := &models.LevelRequirements{
		Level:        3,
		XPRequired:   1500,
		CumulativeXP: 2250,
	}

	repo.On("GetCurrentSeason", mock.Anything).Return(season, nil)
	repo.On("GetProgress", mock.Anything, characterID, seasonID).Return(progress, nil)
	repo.On("GetLevelRequirements", mock.Anything, 3).Return(levelReqs3, nil)
	repo.On("UpdateProgress", mock.Anything, mock.AnythingOfType("*models.PlayerBattlePassProgress")).Return(nil)
	eventBus.On("PublishEvent", mock.Anything, "battle-pass:level-up", mock.Anything).Return(nil)

	ctx := context.Background()
	result, err := service.AwardXP(ctx, characterID, 1500, models.XPSourceQuest)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, 2, result.Level)
	assert.Equal(t, 500, result.XP)
	assert.Equal(t, 1500, result.XPToNextLevel)
	repo.AssertExpectations(t)
	eventBus.AssertExpectations(t)
}

func TestBattlePassService_PurchasePremium(t *testing.T) {
	repo := new(mockBattlePassRepository)
	eventBus := new(mockEventBus)
	logger := GetLogger()

	service := &BattlePassService{
		repo:     repo,
		cache:    nil,
		logger:   logger,
		eventBus: eventBus,
	}

	seasonID := uuid.New()
	characterID := uuid.New()
	season := &models.BattlePassSeason{
		ID:        seasonID,
		Name:      "Season 1",
		StartDate: time.Now().Add(-24 * time.Hour),
		EndDate:   time.Now().Add(30 * 24 * time.Hour),
		MaxLevel:  100,
		IsActive:  true,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	progress := &models.PlayerBattlePassProgress{
		ID:            uuid.New(),
		CharacterID:  characterID,
		SeasonID:     seasonID,
		Level:        1,
		XP:           0,
		XPToNextLevel: 1000,
		HasPremium:   false,
		CreatedAt:    time.Now(),
		UpdatedAt:    time.Now(),
	}

	repo.On("GetCurrentSeason", mock.Anything).Return(season, nil)
	repo.On("GetProgress", mock.Anything, characterID, seasonID).Return(progress, nil)
	repo.On("UpdateProgress", mock.Anything, mock.AnythingOfType("*models.PlayerBattlePassProgress")).Return(nil)
	eventBus.On("PublishEvent", mock.Anything, "battle-pass:premium-purchased", mock.Anything).Return(nil)

	ctx := context.Background()
	result, err := service.PurchasePremium(ctx, characterID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.True(t, result.HasPremium)
	assert.NotNil(t, result.PremiumPurchasedAt)
	repo.AssertExpectations(t)
	eventBus.AssertExpectations(t)
}

func TestBattlePassService_ClaimReward_Success(t *testing.T) {
	repo := new(mockBattlePassRepository)
	eventBus := new(mockEventBus)
	logger := GetLogger()

	service := &BattlePassService{
		repo:     repo,
		cache:    nil,
		logger:   logger,
		eventBus: eventBus,
	}

	seasonID := uuid.New()
	characterID := uuid.New()
	rewardID := uuid.New()

	reward := &models.BattlePassReward{
		ID:         rewardID,
		SeasonID:   seasonID,
		Level:      5,
		Track:      models.TrackFree,
		RewardType: models.RewardTypeCurrency,
		RewardData: map[string]interface{}{"amount": 100},
		CreatedAt:  time.Now(),
		UpdatedAt:  time.Now(),
	}

	progress := &models.PlayerBattlePassProgress{
		ID:            uuid.New(),
		CharacterID:  characterID,
		SeasonID:     seasonID,
		Level:        10,
		XP:           0,
		XPToNextLevel: 1000,
		HasPremium:   false,
		CreatedAt:    time.Now(),
		UpdatedAt:    time.Now(),
	}

	repo.On("GetRewardByID", mock.Anything, rewardID).Return(reward, nil)
	repo.On("GetProgress", mock.Anything, characterID, seasonID).Return(progress, nil)
	repo.On("GetClaimedRewards", mock.Anything, characterID, seasonID).Return([]uuid.UUID{}, nil)
	repo.On("ClaimReward", mock.Anything, characterID, rewardID).Return(nil)
	eventBus.On("PublishEvent", mock.Anything, "battle-pass:reward-claimed", mock.Anything).Return(nil)

	ctx := context.Background()
	err := service.ClaimReward(ctx, characterID, rewardID)

	assert.NoError(t, err)
	repo.AssertExpectations(t)
	eventBus.AssertExpectations(t)
}

func TestBattlePassService_ClaimReward_InsufficientLevel(t *testing.T) {
	repo := new(mockBattlePassRepository)
	eventBus := new(mockEventBus)
	logger := GetLogger()

	service := &BattlePassService{
		repo:     repo,
		cache:    nil,
		logger:   logger,
		eventBus: eventBus,
	}

	seasonID := uuid.New()
	characterID := uuid.New()
	rewardID := uuid.New()

	reward := &models.BattlePassReward{
		ID:         rewardID,
		SeasonID:   seasonID,
		Level:      10,
		Track:      models.TrackFree,
		RewardType: models.RewardTypeCurrency,
		RewardData: map[string]interface{}{"amount": 100},
		CreatedAt:  time.Now(),
		UpdatedAt:  time.Now(),
	}

	progress := &models.PlayerBattlePassProgress{
		ID:            uuid.New(),
		CharacterID:  characterID,
		SeasonID:     seasonID,
		Level:        5,
		XP:           0,
		XPToNextLevel: 1000,
		HasPremium:   false,
		CreatedAt:    time.Now(),
		UpdatedAt:    time.Now(),
	}

	repo.On("GetRewardByID", mock.Anything, rewardID).Return(reward, nil)
	repo.On("GetProgress", mock.Anything, characterID, seasonID).Return(progress, nil)

	ctx := context.Background()
	err := service.ClaimReward(ctx, characterID, rewardID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "insufficient level")
	repo.AssertExpectations(t)
}

func TestBattlePassService_GetLevelRequirements(t *testing.T) {
	repo := new(mockBattlePassRepository)
	eventBus := new(mockEventBus)
	logger := GetLogger()

	service := &BattlePassService{
		repo:     repo,
		cache:    nil,
		logger:   logger,
		eventBus: eventBus,
	}

	levelReqs := &models.LevelRequirements{
		Level:        5,
		XPRequired:   2000,
		CumulativeXP: 5000,
	}

	repo.On("GetLevelRequirements", mock.Anything, 5).Return(levelReqs, nil)

	ctx := context.Background()
	result, err := service.GetLevelRequirements(ctx, 5)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, 5, result.Level)
	assert.Equal(t, 2000, result.XPRequired)
	assert.Equal(t, 5000, result.CumulativeXP)
	repo.AssertExpectations(t)
}

func TestBattlePassService_GetProgress_CreatesNew(t *testing.T) {
	repo := new(mockBattlePassRepository)
	eventBus := new(mockEventBus)
	logger := GetLogger()

	service := &BattlePassService{
		repo:     repo,
		cache:    nil,
		logger:   logger,
		eventBus: eventBus,
	}

	seasonID := uuid.New()
	characterID := uuid.New()

	season := &models.BattlePassSeason{
		ID:        seasonID,
		Name:      "Season 1",
		StartDate: time.Now().Add(-24 * time.Hour),
		EndDate:   time.Now().Add(30 * 24 * time.Hour),
		MaxLevel:  100,
		IsActive:  true,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	levelReqs := &models.LevelRequirements{
		Level:        1,
		XPRequired:   1000,
		CumulativeXP: 0,
	}

	repo.On("GetProgress", mock.Anything, characterID, seasonID).Return(nil, nil)
	repo.On("GetSeasonByID", mock.Anything, seasonID).Return(season, nil)
	repo.On("GetLevelRequirements", mock.Anything, 1).Return(levelReqs, nil)
	repo.On("CreateProgress", mock.Anything, mock.AnythingOfType("*models.PlayerBattlePassProgress")).Return(nil)

	ctx := context.Background()
	result, err := service.GetProgress(ctx, characterID, seasonID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, characterID, result.CharacterID)
	assert.Equal(t, seasonID, result.SeasonID)
	assert.Equal(t, 1, result.Level)
	assert.Equal(t, 0, result.XP)
	repo.AssertExpectations(t)
}

