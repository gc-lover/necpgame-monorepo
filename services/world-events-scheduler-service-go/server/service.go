// Issue: #44
package server

import (
	"context"
	"encoding/json"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"github.com/robfig/cron/v3"
	"github.com/segmentio/kafka-go"
	"go.uber.org/zap"
)

type Service interface {
	ScheduleEvent(ctx context.Context, eventID uuid.UUID, scheduledAt time.Time, cronPattern, triggerType string) (*ScheduledEvent, error)
	GetScheduledEvents(ctx context.Context) ([]*ScheduledEvent, error)
	TriggerEvent(ctx context.Context, eventID uuid.UUID, triggeredBy, reason string) error
	DeleteSchedule(ctx context.Context, id uuid.UUID) error
}

type service struct {
	repo        Repository
	redis       *redis.Client
	kafkaWriter *kafka.Writer
	cron        *cron.Cron
	logger      *zap.Logger
}

func NewService(repo Repository, redis *redis.Client, kafka *kafka.Writer, cronScheduler *cron.Cron, logger *zap.Logger) Service {
	return &service{
		repo:        repo,
		redis:       redis,
		kafkaWriter: kafka,
		cron:        cronScheduler,
		logger:      logger,
	}
}

func (s *service) ScheduleEvent(ctx context.Context, eventID uuid.UUID, scheduledAt time.Time, cronPattern, triggerType string) (*ScheduledEvent, error) {
	scheduled := &ScheduledEvent{
		ID:          uuid.New(),
		EventID:     eventID,
		ScheduledAt: scheduledAt,
		CronPattern: cronPattern,
		TriggerType: triggerType,
		Enabled:     true,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	if err := s.repo.CreateScheduledEvent(ctx, scheduled); err != nil {
		return nil, err
	}

	// Add to cron if pattern provided
	if cronPattern != "" {
		s.cron.AddFunc(cronPattern, func() {
			s.TriggerEvent(context.Background(), eventID, "cron", "scheduled trigger")
		})
	}

	// Publish to Kafka
	s.publishKafka(ctx, "world.event.scheduled", scheduled)

	return scheduled, nil
}

func (s *service) GetScheduledEvents(ctx context.Context) ([]*ScheduledEvent, error) {
	return s.repo.GetScheduledEvents(ctx)
}

func (s *service) TriggerEvent(ctx context.Context, eventID uuid.UUID, triggeredBy, reason string) error {
	payload := map[string]interface{}{
		"event_id":     eventID.String(),
		"triggered_by": triggeredBy,
		"reason":       reason,
		"timestamp":    time.Now().Unix(),
	}

	s.publishKafka(ctx, "world.event.triggered", payload)
	return nil
}

func (s *service) DeleteSchedule(ctx context.Context, id uuid.UUID) error {
	return s.repo.DeleteScheduledEvent(ctx, id)
}

func (s *service) publishKafka(ctx context.Context, eventType string, data interface{}) {
	payload, _ := json.Marshal(map[string]interface{}{
		"event_type": eventType,
		"timestamp":  time.Now().Unix(),
		"data":       data,
	})

	s.kafkaWriter.WriteMessages(ctx, kafka.Message{
		Key:   []byte(eventType),
		Value: payload,
	})
}























