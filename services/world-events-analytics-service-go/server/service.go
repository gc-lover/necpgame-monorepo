// Issue: #44
package server

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"
)

type Service interface {
	RecordMetrics(ctx context.Context, eventID uuid.UUID, participantCount int, completionRate float64, avgDuration, totalRewards int64, engagement float64) error
	GetEventMetrics(ctx context.Context, eventID uuid.UUID) (*EventMetrics, error)
	GetEventAnalytics(ctx context.Context, eventID uuid.UUID) (*EventAnalytics, error)
}

type service struct {
	repo   Repository
	redis  *redis.Client
	logger *zap.Logger
}

func NewService(repo Repository, redis *redis.Client, logger *zap.Logger) Service {
	return &service{repo: repo, redis: redis, logger: logger}
}

func (s *service) RecordMetrics(ctx context.Context, eventID uuid.UUID, participantCount int, completionRate float64, avgDuration, totalRewards int64, engagement float64) error {
	metrics := &EventMetrics{
		EventID:          eventID,
		ParticipantCount: participantCount,
		CompletionRate:   completionRate,
		AverageDuration:  avgDuration,
		TotalRewards:     totalRewards,
		PlayerEngagement: engagement,
		RecordedAt:       time.Now(),
	}

	if err := s.repo.RecordEventMetrics(ctx, metrics); err != nil {
		return err
	}

	// Invalidate cache
	s.invalidateCache(ctx, fmt.Sprintf("analytics:event:%s", eventID.String()))

	return nil
}

func (s *service) GetEventMetrics(ctx context.Context, eventID uuid.UUID) (*EventMetrics, error) {
	// Try cache
	cacheKey := fmt.Sprintf("analytics:event:%s:metrics", eventID.String())
	cached, err := s.redis.Get(ctx, cacheKey).Result()
	if err == nil {
		var metrics EventMetrics
		if err := json.Unmarshal([]byte(cached), &metrics); err == nil {
			return &metrics, nil
		}
	}

	metrics, err := s.repo.GetEventMetrics(ctx, eventID)
	if err != nil {
		return nil, err
	}

	// Cache for 5 minutes
	if metrics != nil {
		metricsJSON, _ := json.Marshal(metrics)
		s.redis.Set(ctx, cacheKey, metricsJSON, 5*time.Minute)
	}

	return metrics, nil
}

func (s *service) GetEventAnalytics(ctx context.Context, eventID uuid.UUID) (*EventAnalytics, error) {
	return s.repo.GetEventAnalytics(ctx, eventID)
}

func (s *service) invalidateCache(ctx context.Context, key string) {
	s.redis.Del(ctx, key)
}









