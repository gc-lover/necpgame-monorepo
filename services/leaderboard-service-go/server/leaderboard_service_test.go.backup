package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
	"github.com/necpgame/leaderboard-service-go/models"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

type mockLeaderboardRepository struct {
	mock.Mock
}

func (m *mockLeaderboardRepository) GetGlobalLeaderboard(ctx context.Context, metric models.LeaderboardMetric, limit, offset int) ([]models.LeaderboardEntry, int, error) {
	args := m.Called(ctx, metric, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Get(1).(int), args.Error(2)
	}
	return args.Get(0).([]models.LeaderboardEntry), args.Get(1).(int), args.Error(2)
}

func (m *mockLeaderboardRepository) GetSeasonalLeaderboard(ctx context.Context, seasonID string, metric models.LeaderboardMetric, limit, offset int) ([]models.LeaderboardEntry, int, error) {
	args := m.Called(ctx, seasonID, metric, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Get(1).(int), args.Error(2)
	}
	return args.Get(0).([]models.LeaderboardEntry), args.Get(1).(int), args.Error(2)
}

func (m *mockLeaderboardRepository) GetClassLeaderboard(ctx context.Context, classID uuid.UUID, metric models.LeaderboardMetric, limit, offset int) ([]models.LeaderboardEntry, int, error) {
	args := m.Called(ctx, classID, metric, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Get(1).(int), args.Error(2)
	}
	return args.Get(0).([]models.LeaderboardEntry), args.Get(1).(int), args.Error(2)
}

func (m *mockLeaderboardRepository) GetFriendsLeaderboard(ctx context.Context, characterID uuid.UUID, metric models.LeaderboardMetric, limit int) ([]models.LeaderboardEntry, error) {
	args := m.Called(ctx, characterID, metric, limit)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.LeaderboardEntry), args.Error(1)
}

func (m *mockLeaderboardRepository) GetGuildLeaderboard(ctx context.Context, guildID uuid.UUID, metric models.LeaderboardMetric, limit, offset int) ([]models.LeaderboardEntry, int, error) {
	args := m.Called(ctx, guildID, metric, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Get(1).(int), args.Error(2)
	}
	return args.Get(0).([]models.LeaderboardEntry), args.Get(1).(int), args.Error(2)
}

func (m *mockLeaderboardRepository) GetPlayerRank(ctx context.Context, characterID uuid.UUID, metric models.LeaderboardMetric, scope models.LeaderboardScope, seasonID *string) (*models.PlayerRank, error) {
	args := m.Called(ctx, characterID, metric, scope, seasonID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.PlayerRank), args.Error(1)
}

func (m *mockLeaderboardRepository) GetRankNeighbors(ctx context.Context, characterID uuid.UUID, metric models.LeaderboardMetric, scope models.LeaderboardScope, rangeSize int, seasonID *string) ([]models.LeaderboardEntry, error) {
	args := m.Called(ctx, characterID, metric, scope, rangeSize, seasonID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.LeaderboardEntry), args.Error(1)
}

func (m *mockLeaderboardRepository) GetLeaderboards(ctx context.Context, leaderboardType *models.LeaderboardType, limit, offset int) ([]models.Leaderboard, int, error) {
	args := m.Called(ctx, leaderboardType, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Get(1).(int), args.Error(2)
	}
	return args.Get(0).([]models.Leaderboard), args.Get(1).(int), args.Error(2)
}

func (m *mockLeaderboardRepository) GetLeaderboard(ctx context.Context, leaderboardID uuid.UUID) (*models.Leaderboard, error) {
	args := m.Called(ctx, leaderboardID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Leaderboard), args.Error(1)
}

func (m *mockLeaderboardRepository) GetLeaderboardTop(ctx context.Context, leaderboardID uuid.UUID, limit, offset int) ([]models.LeaderboardEntry, int, error) {
	args := m.Called(ctx, leaderboardID, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Get(1).(int), args.Error(2)
	}
	return args.Get(0).([]models.LeaderboardEntry), args.Get(1).(int), args.Error(2)
}

func (m *mockLeaderboardRepository) GetLeaderboardPlayerRank(ctx context.Context, leaderboardID, playerID uuid.UUID) (*models.PlayerRank, error) {
	args := m.Called(ctx, leaderboardID, playerID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.PlayerRank), args.Error(1)
}

func (m *mockLeaderboardRepository) GetLeaderboardRankAround(ctx context.Context, leaderboardID, playerID uuid.UUID, rangeSize int) ([]models.LeaderboardEntry, error) {
	args := m.Called(ctx, leaderboardID, playerID, rangeSize)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.LeaderboardEntry), args.Error(1)
}

func (m *mockLeaderboardRepository) UpdateScore(ctx context.Context, characterID uuid.UUID, metric models.LeaderboardMetric, score int64) error {
	args := m.Called(ctx, characterID, metric, score)
	return args.Error(0)
}

func (m *mockLeaderboardRepository) GetCharacterScore(ctx context.Context, characterID uuid.UUID, metric models.LeaderboardMetric) (*models.LeaderboardScore, error) {
	args := m.Called(ctx, characterID, metric)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.LeaderboardScore), args.Error(1)
}

type mockEventBus struct {
	mock.Mock
}

func (m *mockEventBus) PublishEvent(ctx context.Context, eventType string, payload map[string]interface{}) error {
	args := m.Called(ctx, eventType, payload)
	return args.Error(0)
}

func TestLeaderboardService_GetGlobalLeaderboard(t *testing.T) {
	repo := new(mockLeaderboardRepository)
	eventBus := new(mockEventBus)
	logger := GetLogger()

	service := NewLeaderboardService(repo, logger, eventBus)

	entries := []models.LeaderboardEntry{
		{
			Rank:        1,
			CharacterID: uuid.New(),
			Score:       10000,
			Metric:      models.MetricOverallPower,
		},
	}

	repo.On("GetGlobalLeaderboard", mock.Anything, models.MetricOverallPower, 100, 0).Return(entries, 1, nil)

	ctx := context.Background()
	result, err := service.GetGlobalLeaderboard(ctx, models.MetricOverallPower, 100, 0)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, models.ScopeGlobal, result.Scope)
	assert.Equal(t, 1, len(result.Entries))
	repo.AssertExpectations(t)
}

func TestLeaderboardService_GetPlayerRank(t *testing.T) {
	repo := new(mockLeaderboardRepository)
	eventBus := new(mockEventBus)
	logger := GetLogger()

	service := NewLeaderboardService(repo, logger, eventBus)

	characterID := uuid.New()
	rank := &models.PlayerRank{
		CharacterID:  characterID,
		Rank:         10,
		Score:        5000,
		Metric:       models.MetricOverallPower,
		Scope:        models.ScopeGlobal,
		TotalPlayers: 1000,
	}

	repo.On("GetPlayerRank", mock.Anything, characterID, models.MetricOverallPower, models.ScopeGlobal, (*string)(nil)).Return(rank, nil)

	ctx := context.Background()
	result, err := service.GetPlayerRank(ctx, characterID, models.MetricOverallPower, models.ScopeGlobal, nil)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, characterID, result.CharacterID)
	assert.Equal(t, 10, result.Rank)
	repo.AssertExpectations(t)
}

func TestLeaderboardService_UpdateScore(t *testing.T) {
	repo := new(mockLeaderboardRepository)
	eventBus := new(mockEventBus)
	logger := GetLogger()

	service := NewLeaderboardService(repo, logger, eventBus)

	characterID := uuid.New()
	metric := models.MetricOverallPower
	score := int64(10000)

	repo.On("UpdateScore", mock.Anything, characterID, metric, score).Return(nil)
	eventBus.On("PublishEvent", mock.Anything, "leaderboard:score-updated", mock.Anything).Return(nil)

	ctx := context.Background()
	err := service.UpdateScore(ctx, characterID, metric, score)

	assert.NoError(t, err)
	repo.AssertExpectations(t)
	eventBus.AssertExpectations(t)
}

