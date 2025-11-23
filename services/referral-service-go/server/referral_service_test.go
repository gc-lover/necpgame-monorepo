package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/referral-service-go/models"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

type mockReferralRepository struct {
	mock.Mock
}

func (m *mockReferralRepository) GetReferralCode(ctx context.Context, playerID uuid.UUID) (*models.ReferralCode, error) {
	args := m.Called(ctx, playerID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.ReferralCode), args.Error(1)
}

func (m *mockReferralRepository) CreateReferralCode(ctx context.Context, code *models.ReferralCode) error {
	args := m.Called(ctx, code)
	return args.Error(0)
}

func (m *mockReferralRepository) ValidateReferralCode(ctx context.Context, code string) (*models.ReferralCode, error) {
	args := m.Called(ctx, code)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.ReferralCode), args.Error(1)
}

func (m *mockReferralRepository) CreateReferral(ctx context.Context, referral *models.Referral) error {
	args := m.Called(ctx, referral)
	return args.Error(0)
}

func (m *mockReferralRepository) GetReferral(ctx context.Context, id uuid.UUID) (*models.Referral, error) {
	args := m.Called(ctx, id)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Referral), args.Error(1)
}

func (m *mockReferralRepository) GetReferralsByPlayer(ctx context.Context, playerID uuid.UUID, status *models.ReferralStatus, limit, offset int) ([]models.Referral, int, error) {
	args := m.Called(ctx, playerID, status, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Get(1).(int), args.Error(2)
	}
	return args.Get(0).([]models.Referral), args.Get(1).(int), args.Error(2)
}

func (m *mockReferralRepository) UpdateReferral(ctx context.Context, referral *models.Referral) error {
	args := m.Called(ctx, referral)
	return args.Error(0)
}

func (m *mockReferralRepository) GetMilestones(ctx context.Context, playerID uuid.UUID) ([]models.ReferralMilestone, error) {
	args := m.Called(ctx, playerID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.ReferralMilestone), args.Error(1)
}

func (m *mockReferralRepository) CreateMilestone(ctx context.Context, milestone *models.ReferralMilestone) error {
	args := m.Called(ctx, milestone)
	return args.Error(0)
}

func (m *mockReferralRepository) UpdateMilestone(ctx context.Context, milestone *models.ReferralMilestone) error {
	args := m.Called(ctx, milestone)
	return args.Error(0)
}

func (m *mockReferralRepository) CreateReward(ctx context.Context, reward *models.ReferralReward) error {
	args := m.Called(ctx, reward)
	return args.Error(0)
}

func (m *mockReferralRepository) GetRewardHistory(ctx context.Context, playerID uuid.UUID, rewardType *models.ReferralRewardType, limit, offset int) ([]models.ReferralReward, int, error) {
	args := m.Called(ctx, playerID, rewardType, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Get(1).(int), args.Error(2)
	}
	return args.Get(0).([]models.ReferralReward), args.Get(1).(int), args.Error(2)
}

func (m *mockReferralRepository) GetReferralStats(ctx context.Context, playerID uuid.UUID) (*models.ReferralStats, error) {
	args := m.Called(ctx, playerID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.ReferralStats), args.Error(1)
}

func (m *mockReferralRepository) GetPublicReferralStats(ctx context.Context, code string) (*models.ReferralStats, error) {
	args := m.Called(ctx, code)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.ReferralStats), args.Error(1)
}

func (m *mockReferralRepository) GetLeaderboard(ctx context.Context, leaderboardType models.ReferralLeaderboardType, limit, offset int) ([]models.ReferralLeaderboardEntry, int, error) {
	args := m.Called(ctx, leaderboardType, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Get(1).(int), args.Error(2)
	}
	return args.Get(0).([]models.ReferralLeaderboardEntry), args.Get(1).(int), args.Error(2)
}

func (m *mockReferralRepository) GetLeaderboardPosition(ctx context.Context, playerID uuid.UUID, leaderboardType models.ReferralLeaderboardType) (*models.ReferralLeaderboardEntry, int, error) {
	args := m.Called(ctx, playerID, leaderboardType)
	if args.Get(0) == nil {
		return nil, args.Get(1).(int), args.Error(2)
	}
	return args.Get(0).(*models.ReferralLeaderboardEntry), args.Get(1).(int), args.Error(2)
}

func (m *mockReferralRepository) CreateEvent(ctx context.Context, event *models.ReferralEvent) error {
	args := m.Called(ctx, event)
	return args.Error(0)
}

func (m *mockReferralRepository) GetEvents(ctx context.Context, playerID uuid.UUID, eventType *models.ReferralEventType, limit, offset int) ([]models.ReferralEvent, int, error) {
	args := m.Called(ctx, playerID, eventType, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Get(1).(int), args.Error(2)
	}
	return args.Get(0).([]models.ReferralEvent), args.Get(1).(int), args.Error(2)
}

type mockEventBus struct {
	mock.Mock
}

func (m *mockEventBus) PublishEvent(ctx context.Context, eventType string, payload map[string]interface{}) error {
	args := m.Called(ctx, eventType, payload)
	return args.Error(0)
}

func TestReferralService_GetReferralCode(t *testing.T) {
	repo := new(mockReferralRepository)
	eventBus := new(mockEventBus)
	logger := GetLogger()

	service := NewReferralService(repo, logger, eventBus)

	playerID := uuid.New()
	code := &models.ReferralCode{
		ID:        uuid.New(),
		PlayerID:  playerID,
		Code:      "TEST1234",
		IsActive:  true,
		CreatedAt: time.Now(),
	}

	repo.On("GetReferralCode", mock.Anything, playerID).Return(code, nil)

	ctx := context.Background()
	result, err := service.GetReferralCode(ctx, playerID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, code.Code, result.Code)
	repo.AssertExpectations(t)
}

func TestReferralService_GenerateReferralCode(t *testing.T) {
	repo := new(mockReferralRepository)
	eventBus := new(mockEventBus)
	logger := GetLogger()

	service := NewReferralService(repo, logger, eventBus)

	playerID := uuid.New()

	repo.On("GetReferralCode", mock.Anything, playerID).Return(nil, nil)
	repo.On("CreateReferralCode", mock.Anything, mock.AnythingOfType("*models.ReferralCode")).Return(nil)
	repo.On("CreateEvent", mock.Anything, mock.AnythingOfType("*models.ReferralEvent")).Return(nil)
	eventBus.On("PublishEvent", mock.Anything, string(models.EventTypeCodeGenerated), mock.Anything).Return(nil)

	ctx := context.Background()
	result, err := service.GenerateReferralCode(ctx, playerID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, playerID, result.PlayerID)
	assert.NotEmpty(t, result.Code)
	repo.AssertExpectations(t)
	eventBus.AssertExpectations(t)
}

func TestReferralService_RegisterWithCode(t *testing.T) {
	repo := new(mockReferralRepository)
	eventBus := new(mockEventBus)
	logger := GetLogger()

	service := NewReferralService(repo, logger, eventBus)

	referrerID := uuid.New()
	refereeID := uuid.New()
	codeID := uuid.New()
	code := &models.ReferralCode{
		ID:       codeID,
		PlayerID: referrerID,
		Code:     "TEST1234",
		IsActive: true,
	}

	repo.On("ValidateReferralCode", mock.Anything, "TEST1234").Return(code, nil)
	repo.On("GetReferralsByPlayer", mock.Anything, referrerID, (*models.ReferralStatus)(nil), 100, 0).Return([]models.Referral{}, 0, nil)
	repo.On("CreateReferral", mock.Anything, mock.AnythingOfType("*models.Referral")).Return(nil)
	repo.On("CreateEvent", mock.Anything, mock.AnythingOfType("*models.ReferralEvent")).Return(nil)
	eventBus.On("PublishEvent", mock.Anything, string(models.EventTypeRegistered), mock.Anything).Return(nil)

	ctx := context.Background()
	referral, err := service.RegisterWithCode(ctx, refereeID, "TEST1234")

	assert.NoError(t, err)
	assert.NotNil(t, referral)
	assert.Equal(t, referrerID, referral.ReferrerID)
	assert.Equal(t, refereeID, referral.RefereeID)
	repo.AssertExpectations(t)
	eventBus.AssertExpectations(t)
}

func TestReferralService_GetMilestones(t *testing.T) {
	repo := new(mockReferralRepository)
	eventBus := new(mockEventBus)
	logger := GetLogger()

	service := NewReferralService(repo, logger, eventBus)

	playerID := uuid.New()
	milestones := []models.ReferralMilestone{
		{
			ID:            uuid.New(),
			PlayerID:      playerID,
			MilestoneType: models.Milestone5,
			MilestoneValue: 5,
			AchievedAt:    time.Now(),
			RewardClaimed: false,
		},
	}

	stats := &models.ReferralStats{
		PlayerID:        playerID,
		CurrentMilestone: nil,
	}

	repo.On("GetMilestones", mock.Anything, playerID).Return(milestones, nil)
	repo.On("GetReferralStats", mock.Anything, playerID).Return(stats, nil)

	ctx := context.Background()
	result, currentMilestone, err := service.GetMilestones(ctx, playerID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, 1, len(result))
	assert.Nil(t, currentMilestone)
	repo.AssertExpectations(t)
}

func TestReferralService_ClaimMilestoneReward(t *testing.T) {
	repo := new(mockReferralRepository)
	eventBus := new(mockEventBus)
	logger := GetLogger()

	service := NewReferralService(repo, logger, eventBus)

	playerID := uuid.New()
	milestoneID := uuid.New()
	milestones := []models.ReferralMilestone{
		{
			ID:            milestoneID,
			PlayerID:      playerID,
			MilestoneType: models.Milestone5,
			MilestoneValue: 5,
			AchievedAt:    time.Now(),
			RewardClaimed: false,
		},
	}

	repo.On("GetMilestones", mock.Anything, playerID).Return(milestones, nil)
	repo.On("UpdateMilestone", mock.Anything, mock.AnythingOfType("*models.ReferralMilestone")).Return(nil)
	eventBus.On("PublishEvent", mock.Anything, string(models.EventTypeMilestoneAchieved), mock.Anything).Return(nil)

	ctx := context.Background()
	result, err := service.ClaimMilestoneReward(ctx, playerID, milestoneID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.True(t, result.RewardClaimed)
	repo.AssertExpectations(t)
	eventBus.AssertExpectations(t)
}

func TestReferralService_DistributeRewards(t *testing.T) {
	repo := new(mockReferralRepository)
	eventBus := new(mockEventBus)
	logger := GetLogger()

	service := NewReferralService(repo, logger, eventBus)

	referralID := uuid.New()
	referrerID := uuid.New()
	refereeID := uuid.New()
	referral := &models.Referral{
		ID:                referralID,
		ReferrerID:        referrerID,
		RefereeID:         refereeID,
		Status:            models.ReferralStatusActive,
		WelcomeBonusGiven: false,
		ReferrerBonusGiven: false,
	}

	repo.On("GetReferral", mock.Anything, referralID).Return(referral, nil)
	repo.On("CreateReward", mock.Anything, mock.AnythingOfType("*models.ReferralReward")).Return(nil)
	repo.On("UpdateReferral", mock.Anything, mock.AnythingOfType("*models.Referral")).Return(nil)
	repo.On("CreateEvent", mock.Anything, mock.AnythingOfType("*models.ReferralEvent")).Return(nil)
	eventBus.On("PublishEvent", mock.Anything, string(models.EventTypeRewardDistributed), mock.Anything).Return(nil)

	ctx := context.Background()
	err := service.DistributeRewards(ctx, referralID, models.RewardTypeWelcomeBonus)

	assert.NoError(t, err)
	repo.AssertExpectations(t)
	eventBus.AssertExpectations(t)
}

func TestReferralService_GetReferralStats(t *testing.T) {
	repo := new(mockReferralRepository)
	eventBus := new(mockEventBus)
	logger := GetLogger()

	service := NewReferralService(repo, logger, eventBus)

	playerID := uuid.New()
	stats := &models.ReferralStats{
		PlayerID:        playerID,
		TotalReferrals:  10,
		ActiveReferrals: 5,
		Level10Referrals: 3,
		TotalRewards:    5000,
		LastUpdated:     time.Now(),
	}

	repo.On("GetReferralStats", mock.Anything, playerID).Return(stats, nil)

	ctx := context.Background()
	result, err := service.GetReferralStats(ctx, playerID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, playerID, result.PlayerID)
	assert.Equal(t, 10, result.TotalReferrals)
	repo.AssertExpectations(t)
}

