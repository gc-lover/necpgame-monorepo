package server

import (
	"context"
	"encoding/json"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/leaderboard-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type LeaderboardService interface {
	GetGlobalLeaderboard(ctx context.Context, metric models.LeaderboardMetric, limit, offset int) (*models.LeaderboardResponse, error)
	GetSeasonalLeaderboard(ctx context.Context, seasonID string, metric models.LeaderboardMetric, limit, offset int) (*models.LeaderboardResponse, error)
	GetClassLeaderboard(ctx context.Context, classID uuid.UUID, metric models.LeaderboardMetric, limit, offset int) (*models.LeaderboardResponse, error)
	GetFriendsLeaderboard(ctx context.Context, characterID uuid.UUID, metric models.LeaderboardMetric, limit int) (*models.LeaderboardResponse, error)
	GetGuildLeaderboard(ctx context.Context, guildID uuid.UUID, metric models.LeaderboardMetric, limit, offset int) (*models.LeaderboardResponse, error)
	
	GetPlayerRank(ctx context.Context, characterID uuid.UUID, metric models.LeaderboardMetric, scope models.LeaderboardScope, seasonID *string) (*models.PlayerRank, error)
	GetRankNeighbors(ctx context.Context, characterID uuid.UUID, metric models.LeaderboardMetric, scope models.LeaderboardScope, rangeSize int, seasonID *string) ([]models.LeaderboardEntry, error)
	
	GetLeaderboards(ctx context.Context, leaderboardType *models.LeaderboardType, limit, offset int) (*models.LeaderboardListResponse, error)
	GetLeaderboard(ctx context.Context, leaderboardID uuid.UUID) (*models.LeaderboardDetails, error)
	GetLeaderboardTop(ctx context.Context, leaderboardID uuid.UUID, limit, offset int) (*models.LeaderboardResponse, error)
	GetLeaderboardPlayerRank(ctx context.Context, leaderboardID, playerID uuid.UUID) (*models.PlayerRank, error)
	GetLeaderboardRankAround(ctx context.Context, leaderboardID, playerID uuid.UUID, rangeSize int) ([]models.LeaderboardEntry, error)
	
	UpdateScore(ctx context.Context, characterID uuid.UUID, metric models.LeaderboardMetric, score int64) error
}

type EventBus interface {
	PublishEvent(ctx context.Context, eventType string, payload map[string]interface{}) error
}

type RedisEventBus struct {
	client *redis.Client
	logger *logrus.Logger
}

func NewRedisEventBus(redisClient *redis.Client) *RedisEventBus {
	return &RedisEventBus{
		client: redisClient,
		logger: GetLogger(),
	}
}

func (b *RedisEventBus) PublishEvent(ctx context.Context, eventType string, payload map[string]interface{}) error {
	eventData, err := json.Marshal(payload)
	if err != nil {
		return err
	}

	channel := "events:" + eventType
	return b.client.Publish(ctx, channel, eventData).Err()
}

type leaderboardService struct {
	repo     LeaderboardRepository
	logger   *logrus.Logger
	eventBus EventBus
}

func NewLeaderboardService(repo LeaderboardRepository, logger *logrus.Logger, eventBus EventBus) LeaderboardService {
	return &leaderboardService{
		repo:     repo,
		logger:   logger,
		eventBus: eventBus,
	}
}

func (s *leaderboardService) GetGlobalLeaderboard(ctx context.Context, metric models.LeaderboardMetric, limit, offset int) (*models.LeaderboardResponse, error) {
	entries, total, err := s.repo.GetGlobalLeaderboard(ctx, metric, limit, offset)
	if err != nil {
		return nil, err
	}
	
	RecordLeaderboardQuery(string(models.ScopeGlobal), string(metric))
	
	return &models.LeaderboardResponse{
		Metric: metric,
		Scope:  models.ScopeGlobal,
		Entries: entries,
		Total:   total,
		Limit:   limit,
		Offset:  offset,
	}, nil
}

func (s *leaderboardService) GetSeasonalLeaderboard(ctx context.Context, seasonID string, metric models.LeaderboardMetric, limit, offset int) (*models.LeaderboardResponse, error) {
	entries, total, err := s.repo.GetSeasonalLeaderboard(ctx, seasonID, metric, limit, offset)
	if err != nil {
		return nil, err
	}
	
	RecordLeaderboardQuery(string(models.ScopeSeasonal), string(metric))
	
	return &models.LeaderboardResponse{
		Metric:   metric,
		Scope:    models.ScopeSeasonal,
		SeasonID: &seasonID,
		Entries:  entries,
		Total:    total,
		Limit:    limit,
		Offset:   offset,
	}, nil
}

func (s *leaderboardService) GetClassLeaderboard(ctx context.Context, classID uuid.UUID, metric models.LeaderboardMetric, limit, offset int) (*models.LeaderboardResponse, error) {
	entries, total, err := s.repo.GetClassLeaderboard(ctx, classID, metric, limit, offset)
	if err != nil {
		return nil, err
	}
	
	RecordLeaderboardQuery(string(models.ScopeClass), string(metric))
	
	return &models.LeaderboardResponse{
		Metric:  metric,
		Scope:   models.ScopeClass,
		Entries: entries,
		Total:   total,
		Limit:   limit,
		Offset:  offset,
	}, nil
}

func (s *leaderboardService) GetFriendsLeaderboard(ctx context.Context, characterID uuid.UUID, metric models.LeaderboardMetric, limit int) (*models.LeaderboardResponse, error) {
	entries, err := s.repo.GetFriendsLeaderboard(ctx, characterID, metric, limit)
	if err != nil {
		return nil, err
	}
	
	RecordLeaderboardQuery(string(models.ScopeFriends), string(metric))
	
	return &models.LeaderboardResponse{
		Metric:  metric,
		Scope:   models.ScopeFriends,
		Entries: entries,
		Total:   len(entries),
		Limit:   limit,
		Offset:  0,
	}, nil
}

func (s *leaderboardService) GetGuildLeaderboard(ctx context.Context, guildID uuid.UUID, metric models.LeaderboardMetric, limit, offset int) (*models.LeaderboardResponse, error) {
	entries, total, err := s.repo.GetGuildLeaderboard(ctx, guildID, metric, limit, offset)
	if err != nil {
		return nil, err
	}
	
	RecordLeaderboardQuery(string(models.ScopeGuild), string(metric))
	
	return &models.LeaderboardResponse{
		Metric:  metric,
		Scope:   models.ScopeGuild,
		Entries: entries,
		Total:   total,
		Limit:   limit,
		Offset:  offset,
	}, nil
}

func (s *leaderboardService) GetPlayerRank(ctx context.Context, characterID uuid.UUID, metric models.LeaderboardMetric, scope models.LeaderboardScope, seasonID *string) (*models.PlayerRank, error) {
	return s.repo.GetPlayerRank(ctx, characterID, metric, scope, seasonID)
}

func (s *leaderboardService) GetRankNeighbors(ctx context.Context, characterID uuid.UUID, metric models.LeaderboardMetric, scope models.LeaderboardScope, rangeSize int, seasonID *string) ([]models.LeaderboardEntry, error) {
	return s.repo.GetRankNeighbors(ctx, characterID, metric, scope, rangeSize, seasonID)
}

func (s *leaderboardService) GetLeaderboards(ctx context.Context, leaderboardType *models.LeaderboardType, limit, offset int) (*models.LeaderboardListResponse, error) {
	leaderboards, total, err := s.repo.GetLeaderboards(ctx, leaderboardType, limit, offset)
	if err != nil {
		return nil, err
	}
	
	return &models.LeaderboardListResponse{
		Leaderboards: leaderboards,
		Total:        total,
		Limit:        limit,
		Offset:       offset,
	}, nil
}

func (s *leaderboardService) GetLeaderboard(ctx context.Context, leaderboardID uuid.UUID) (*models.LeaderboardDetails, error) {
	leaderboard, err := s.repo.GetLeaderboard(ctx, leaderboardID)
	if err != nil {
		return nil, err
	}
	if leaderboard == nil {
		return nil, nil
	}
	
	entries, total, err := s.repo.GetLeaderboardTop(ctx, leaderboardID, 1, 0)
	if err != nil {
		return nil, err
	}
	
	var lastUpdated time.Time
	if len(entries) > 0 {
		lastUpdated = entries[0].LastUpdated
	}
	
	return &models.LeaderboardDetails{
		Leaderboard:  *leaderboard,
		TotalPlayers: total,
		LastUpdated:  lastUpdated,
	}, nil
}

func (s *leaderboardService) GetLeaderboardTop(ctx context.Context, leaderboardID uuid.UUID, limit, offset int) (*models.LeaderboardResponse, error) {
	leaderboard, err := s.repo.GetLeaderboard(ctx, leaderboardID)
	if err != nil {
		return nil, err
	}
	if leaderboard == nil {
		return nil, nil
	}
	
	entries, total, err := s.repo.GetLeaderboardTop(ctx, leaderboardID, limit, offset)
	if err != nil {
		return nil, err
	}
	
	var seasonID *string
	if leaderboard.SeasonID != nil {
		seasonIDStr := leaderboard.SeasonID.String()
		seasonID = &seasonIDStr
	}
	
	return &models.LeaderboardResponse{
		Metric:   leaderboard.Metric,
		Scope:    models.LeaderboardScope(leaderboard.Type),
		SeasonID: seasonID,
		Entries:  entries,
		Total:    total,
		Limit:    limit,
		Offset:   offset,
	}, nil
}

func (s *leaderboardService) GetLeaderboardPlayerRank(ctx context.Context, leaderboardID, playerID uuid.UUID) (*models.PlayerRank, error) {
	return s.repo.GetLeaderboardPlayerRank(ctx, leaderboardID, playerID)
}

func (s *leaderboardService) GetLeaderboardRankAround(ctx context.Context, leaderboardID, playerID uuid.UUID, rangeSize int) ([]models.LeaderboardEntry, error) {
	return s.repo.GetLeaderboardRankAround(ctx, leaderboardID, playerID, rangeSize)
}

func (s *leaderboardService) UpdateScore(ctx context.Context, characterID uuid.UUID, metric models.LeaderboardMetric, score int64) error {
	if err := s.repo.UpdateScore(ctx, characterID, metric, score); err != nil {
		return err
	}
	
	RecordScoreUpdate(string(metric))
	
	if s.eventBus != nil {
		payload := map[string]interface{}{
			"character_id": characterID.String(),
			"metric":       string(metric),
			"score":        score,
		}
		s.eventBus.PublishEvent(ctx, "leaderboard:score-updated", payload)
	}
	
	return nil
}

