package server

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/reset-service-go/models"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/redis/go-redis/v9"
	"github.com/robfig/cron/v3"
	"github.com/sirupsen/logrus"
)

type ResetRepositoryInterface interface {
	Create(ctx context.Context, record *models.ResetRecord) error
	Update(ctx context.Context, record *models.ResetRecord) error
	GetLastReset(ctx context.Context, resetType models.ResetType) (*models.ResetRecord, error)
	List(ctx context.Context, resetType *models.ResetType, limit, offset int) ([]models.ResetRecord, error)
	Count(ctx context.Context, resetType *models.ResetType) (int, error)
}

type ResetService struct {
	repo     ResetRepositoryInterface
	cache    *redis.Client
	cron     *cron.Cron
	logger   *logrus.Logger
	eventBus EventBus
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

func NewResetService(dbURL, redisURL string) (*ResetService, error) {
	// Issue: #1605 - DB Connection Pool configuration
	config, err := pgxpool.ParseConfig(dbURL)
	if err != nil {
		return nil, err
	}
	config.MaxConns = 50
	config.MinConns = 10
	config.MaxConnLifetime = 5 * time.Minute
	config.MaxConnIdleTime = 1 * time.Minute
	
	dbPool, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		return nil, err
	}

	redisOpts, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, err
	}

	redisClient := redis.NewClient(redisOpts)

	repo := NewResetRepository(dbPool)
	eventBus := NewRedisEventBus(redisClient)

	cronScheduler := cron.New(cron.WithSeconds())

	service := &ResetService{
		repo:     repo,
		cache:    redisClient,
		cron:     cronScheduler,
		logger:   GetLogger(),
		eventBus: eventBus,
	}

	cronScheduler.AddFunc("0 0 * * *", func() {
		ctx := context.Background()
		if err := service.ExecuteDailyReset(ctx); err != nil {
			service.logger.WithError(err).Error("Failed to execute daily reset")
		}
	})

	cronScheduler.AddFunc("0 0 * * MON", func() {
		ctx := context.Background()
		if err := service.ExecuteWeeklyReset(ctx); err != nil {
			service.logger.WithError(err).Error("Failed to execute weekly reset")
		}
	})

	return service, nil
}

func (s *ResetService) Start() {
	s.cron.Start()
	s.logger.Info("Reset service cron scheduler started")
}

func (s *ResetService) Stop() {
	ctx := s.cron.Stop()
	<-ctx.Done()
	s.logger.Info("Reset service cron scheduler stopped")
}

func (s *ResetService) ExecuteDailyReset(ctx context.Context) error {
	startTime := time.Now()
	s.logger.Info("Starting daily reset")

	record := &models.ResetRecord{
		ID:        uuid.New(),
		Type:      models.ResetTypeDaily,
		Status:    models.ResetStatusRunning,
		StartedAt: startTime,
		Metadata:  make(map[string]interface{}),
	}

	err := s.repo.Create(ctx, record)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create reset record")
		return err
	}

	RecordReset(string(record.Type), string(record.Status))

	defer func() {
		duration := time.Since(startTime).Seconds()
		RecordResetDuration(string(record.Type), duration)
	}()

	metadata := make(map[string]interface{})
	metadata["execution_time"] = startTime.Format(time.RFC3339)

	err = s.publishDailyResetEvent(ctx, metadata)
	if err != nil {
		s.logger.WithError(err).Error("Failed to publish daily reset event")
		record.Status = models.ResetStatusFailed
		errorMsg := err.Error()
		record.Error = &errorMsg
	} else {
		now := time.Now()
		record.Status = models.ResetStatusCompleted
		record.CompletedAt = &now
		metadata["completed_at"] = now.Format(time.RFC3339)
		record.Metadata = metadata
	}

	err = s.repo.Update(ctx, record)
	if err != nil {
		s.logger.WithError(err).Error("Failed to update reset record")
		return err
	}

	RecordReset(string(record.Type), string(record.Status))
	s.logger.WithFields(logrus.Fields{
		"reset_id": record.ID,
		"duration": time.Since(startTime).Seconds(),
	}).Info("Daily reset completed")

	return nil
}

func (s *ResetService) ExecuteWeeklyReset(ctx context.Context) error {
	startTime := time.Now()
	s.logger.Info("Starting weekly reset")

	record := &models.ResetRecord{
		ID:        uuid.New(),
		Type:      models.ResetTypeWeekly,
		Status:    models.ResetStatusRunning,
		StartedAt: startTime,
		Metadata:  make(map[string]interface{}),
	}

	err := s.repo.Create(ctx, record)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create reset record")
		return err
	}

	RecordReset(string(record.Type), string(record.Status))

	defer func() {
		duration := time.Since(startTime).Seconds()
		RecordResetDuration(string(record.Type), duration)
	}()

	metadata := make(map[string]interface{})
	metadata["execution_time"] = startTime.Format(time.RFC3339)

	err = s.publishWeeklyResetEvent(ctx, metadata)
	if err != nil {
		s.logger.WithError(err).Error("Failed to publish weekly reset event")
		record.Status = models.ResetStatusFailed
		errorMsg := err.Error()
		record.Error = &errorMsg
	} else {
		now := time.Now()
		record.Status = models.ResetStatusCompleted
		record.CompletedAt = &now
		metadata["completed_at"] = now.Format(time.RFC3339)
		record.Metadata = metadata
	}

	err = s.repo.Update(ctx, record)
	if err != nil {
		s.logger.WithError(err).Error("Failed to update reset record")
		return err
	}

	RecordReset(string(record.Type), string(record.Status))
	s.logger.WithFields(logrus.Fields{
		"reset_id": record.ID,
		"duration": time.Since(startTime).Seconds(),
	}).Info("Weekly reset completed")

	return nil
}

func (s *ResetService) publishDailyResetEvent(ctx context.Context, metadata map[string]interface{}) error {
	payload := map[string]interface{}{
		"type":      "daily",
		"timestamp": time.Now().Format(time.RFC3339),
		"metadata":  metadata,
	}

	return s.eventBus.PublishEvent(ctx, "reset.daily.completed", payload)
}

func (s *ResetService) publishWeeklyResetEvent(ctx context.Context, metadata map[string]interface{}) error {
	payload := map[string]interface{}{
		"type":      "weekly",
		"timestamp": time.Now().Format(time.RFC3339),
		"metadata":  metadata,
	}

	return s.eventBus.PublishEvent(ctx, "reset.weekly.completed", payload)
}

func (s *ResetService) TriggerReset(ctx context.Context, resetType models.ResetType) error {
	switch resetType {
	case models.ResetTypeDaily:
		return s.ExecuteDailyReset(ctx)
	case models.ResetTypeWeekly:
		return s.ExecuteWeeklyReset(ctx)
	default:
		return fmt.Errorf("unknown reset type: %s", resetType)
	}
}

func (s *ResetService) GetResetStats(ctx context.Context) (*models.ResetStats, error) {
	lastDaily, err := s.repo.GetLastReset(ctx, models.ResetTypeDaily)
	if err != nil {
		return nil, err
	}

	lastWeekly, err := s.repo.GetLastReset(ctx, models.ResetTypeWeekly)
	if err != nil {
		return nil, err
	}

	now := time.Now()
	nextDaily := getNextDailyReset(now, lastDaily)
	nextWeekly := getNextWeeklyReset(now, lastWeekly)

	stats := &models.ResetStats{
		NextDailyReset:  nextDaily,
		NextWeeklyReset: nextWeekly,
	}

	if lastDaily != nil && lastDaily.CompletedAt != nil {
		stats.LastDailyReset = lastDaily.CompletedAt
	}
	if lastWeekly != nil && lastWeekly.CompletedAt != nil {
		stats.LastWeeklyReset = lastWeekly.CompletedAt
	}

	return stats, nil
}

func (s *ResetService) GetResetHistory(ctx context.Context, resetType *models.ResetType, limit, offset int) (*models.ResetListResponse, error) {
	var modelResetType *models.ResetType
	if resetType != nil {
		modelResetType = resetType
	}

	records, err := s.repo.List(ctx, modelResetType, limit, offset)
	if err != nil {
		return nil, err
	}

	total, err := s.repo.Count(ctx, modelResetType)
	if err != nil {
		return nil, err
	}

	response := &models.ResetListResponse{
		Resets: records,
		Total:  total,
	}

	return response, nil
}

func getNextDailyReset(now time.Time, lastReset *models.ResetRecord) time.Time {
	next := time.Date(now.Year(), now.Month(), now.Day(), 0, 0, 0, 0, now.Location())
	next = next.Add(24 * time.Hour)

	if lastReset != nil && lastReset.CompletedAt != nil {
		lastResetTime := *lastReset.CompletedAt
		lastResetDay := time.Date(lastResetTime.Year(), lastResetTime.Month(), lastResetTime.Day(), 0, 0, 0, 0, lastResetTime.Location())

		if lastResetDay.Equal(time.Date(now.Year(), now.Month(), now.Day(), 0, 0, 0, 0, now.Location())) {
			next = next.Add(24 * time.Hour)
		}
	}

	return next
}

func getNextWeeklyReset(now time.Time, lastReset *models.ResetRecord) time.Time {
	weekday := now.Weekday()
	daysUntilMonday := (8 - int(weekday)) % 7
	if daysUntilMonday == 0 {
		daysUntilMonday = 7
	}

	next := time.Date(now.Year(), now.Month(), now.Day(), 0, 0, 0, 0, now.Location())
	next = next.Add(time.Duration(daysUntilMonday) * 24 * time.Hour)

	if lastReset != nil && lastReset.CompletedAt != nil {
		lastResetTime := *lastReset.CompletedAt
		lastResetWeek := getWeekStart(lastResetTime)
		currentWeek := getWeekStart(now)

		if lastResetWeek.Equal(currentWeek) {
			next = next.Add(7 * 24 * time.Hour)
		}
	}

	return next
}

func getWeekStart(t time.Time) time.Time {
	weekday := t.Weekday()
	daysFromMonday := (int(weekday) + 6) % 7
	return time.Date(t.Year(), t.Month(), t.Day()-daysFromMonday, 0, 0, 0, 0, t.Location())
}
