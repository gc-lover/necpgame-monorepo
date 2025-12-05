// Issue: #44
package server

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"github.com/segmentio/kafka-go"
	"go.uber.org/zap"
)

// Service - интерфейс бизнес-логики
type Service interface {
	// Events CRUD
	CreateEvent(ctx context.Context, name, description, eventType, scale, frequency string, startTime, endTime *time.Time, effects, triggers, constraints map[string]interface{}) (*WorldEvent, error)
	GetEvent(ctx context.Context, id uuid.UUID) (*WorldEvent, error)
	UpdateEvent(ctx context.Context, id uuid.UUID, name, description, eventType, scale, frequency, status *string, startTime, endTime *time.Time, effects, triggers, constraints map[string]interface{}) (*WorldEvent, error)
	DeleteEvent(ctx context.Context, id uuid.UUID) error
	ListEvents(ctx context.Context, filter EventFilter) ([]*WorldEvent, int, error)
	
	// Event state
	GetActiveEvents(ctx context.Context) ([]*WorldEvent, error)
	GetPlannedEvents(ctx context.Context) ([]*WorldEvent, error)
	
	// Event actions
	ActivateEvent(ctx context.Context, id uuid.UUID, activatedBy string) error
	DeactivateEvent(ctx context.Context, id uuid.UUID) error
	AnnounceEvent(ctx context.Context, id uuid.UUID, announcedBy, message string, channels []string) error
}

type service struct {
	repo        Repository
	redis       *redis.Client
	kafkaWriter *kafka.Writer
	logger      *zap.Logger
}

// NewService создает новый сервис
func NewService(repo Repository, redis *redis.Client, kafkaWriter *kafka.Writer, logger *zap.Logger) Service {
	return &service{
		repo:        repo,
		redis:       redis,
		kafkaWriter: kafkaWriter,
		logger:      logger,
	}
}

func (s *service) CreateEvent(ctx context.Context, name, description, eventType, scale, frequency string, startTime, endTime *time.Time, effects, triggers, constraints map[string]interface{}) (*WorldEvent, error) {
	event := &WorldEvent{
		ID:          uuid.New(),
		Name:        name,
		Description: description,
		Type:        eventType,
		Scale:       scale,
		Frequency:   frequency,
		Status:      "planned",
		StartTime:   startTime,
		EndTime:     endTime,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	// Marshal effects, triggers, constraints to JSON
	if effects != nil {
		effectsJSON, err := json.Marshal(effects)
		if err != nil {
			return nil, err
		}
		event.Effects = effectsJSON
	}

	if triggers != nil {
		triggersJSON, err := json.Marshal(triggers)
		if err != nil {
			return nil, err
		}
		event.Triggers = triggersJSON
	}

	if constraints != nil {
		constraintsJSON, err := json.Marshal(constraints)
		if err != nil {
			return nil, err
		}
		event.Constraints = constraintsJSON
	}

	err := s.repo.CreateEvent(ctx, event)
	if err != nil {
		return nil, err
	}

	// Publish Kafka event
	s.publishKafkaEvent(ctx, "world.event.created", event)

	// Invalidate cache
	s.invalidateCache(ctx, "world:events:list")

	return event, nil
}

func (s *service) GetEvent(ctx context.Context, id uuid.UUID) (*WorldEvent, error) {
	// Try cache first
	cacheKey := fmt.Sprintf("world:event:%s", id.String())
	cached, err := s.redis.Get(ctx, cacheKey).Result()
	if err == nil {
		var event WorldEvent
		if err := json.Unmarshal([]byte(cached), &event); err == nil {
			return &event, nil
		}
	}

	// Get from DB
	event, err := s.repo.GetEventByID(ctx, id)
	if err != nil {
		return nil, err
	}

	if event == nil {
		return nil, fmt.Errorf("event not found")
	}

	// Cache for 5 minutes
	eventJSON, _ := json.Marshal(event)
	s.redis.Set(ctx, cacheKey, eventJSON, 5*time.Minute)

	return event, nil
}

func (s *service) UpdateEvent(ctx context.Context, id uuid.UUID, name, description, eventType, scale, frequency, status *string, startTime, endTime *time.Time, effects, triggers, constraints map[string]interface{}) (*WorldEvent, error) {
	event, err := s.repo.GetEventByID(ctx, id)
	if err != nil {
		return nil, err
	}
	if event == nil {
		return nil, fmt.Errorf("event not found")
	}

	// Update fields
	if name != nil {
		event.Name = *name
	}
	if description != nil {
		event.Description = *description
	}
	if eventType != nil {
		event.Type = *eventType
	}
	if scale != nil {
		event.Scale = *scale
	}
	if frequency != nil {
		event.Frequency = *frequency
	}
	if status != nil {
		event.Status = *status
	}
	if startTime != nil {
		event.StartTime = startTime
	}
	if endTime != nil {
		event.EndTime = endTime
	}

	// Update effects, triggers, constraints
	if effects != nil {
		effectsJSON, err := json.Marshal(effects)
		if err != nil {
			return nil, err
		}
		event.Effects = effectsJSON
	}
	if triggers != nil {
		triggersJSON, err := json.Marshal(triggers)
		if err != nil {
			return nil, err
		}
		event.Triggers = triggersJSON
	}
	if constraints != nil {
		constraintsJSON, err := json.Marshal(constraints)
		if err != nil {
			return nil, err
		}
		event.Constraints = constraintsJSON
	}

	err = s.repo.UpdateEvent(ctx, event)
	if err != nil {
		return nil, err
	}

	// Publish Kafka event
	s.publishKafkaEvent(ctx, "world.event.updated", event)

	// Invalidate cache
	s.invalidateCache(ctx, fmt.Sprintf("world:event:%s", id.String()))
	s.invalidateCache(ctx, "world:events:list")

	return event, nil
}

func (s *service) DeleteEvent(ctx context.Context, id uuid.UUID) error {
	err := s.repo.DeleteEvent(ctx, id)
	if err != nil {
		return err
	}

	// Publish Kafka event
	s.publishKafkaEvent(ctx, "world.event.deleted", map[string]interface{}{"event_id": id.String()})

	// Invalidate cache
	s.invalidateCache(ctx, fmt.Sprintf("world:event:%s", id.String()))
	s.invalidateCache(ctx, "world:events:list")

	return nil
}

func (s *service) ListEvents(ctx context.Context, filter EventFilter) ([]*WorldEvent, int, error) {
	return s.repo.ListEvents(ctx, filter)
}

func (s *service) GetActiveEvents(ctx context.Context) ([]*WorldEvent, error) {
	// Try cache first
	cacheKey := "world:events:active"
	cached, err := s.redis.Get(ctx, cacheKey).Result()
	if err == nil {
		var events []*WorldEvent
		if err := json.Unmarshal([]byte(cached), &events); err == nil {
			return events, nil
		}
	}

	// Get from DB
	events, err := s.repo.GetActiveEvents(ctx)
	if err != nil {
		return nil, err
	}

	// Cache for 1 minute
	eventsJSON, _ := json.Marshal(events)
	s.redis.Set(ctx, cacheKey, eventsJSON, 1*time.Minute)

	return events, nil
}

func (s *service) GetPlannedEvents(ctx context.Context) ([]*WorldEvent, error) {
	return s.repo.GetPlannedEvents(ctx)
}

func (s *service) ActivateEvent(ctx context.Context, id uuid.UUID, activatedBy string) error {
	event, err := s.repo.GetEventByID(ctx, id)
	if err != nil {
		return err
	}
	if event == nil {
		return fmt.Errorf("event not found")
	}

	if event.Status != "planned" {
		return fmt.Errorf("event is not in planned state")
	}

	// Update status
	event.Status = "active"
	now := time.Now()
	event.StartTime = &now
	err = s.repo.UpdateEvent(ctx, event)
	if err != nil {
		return err
	}

	// Record activation
	activation := &EventActivation{
		EventID:     id,
		ActivatedAt: now,
		ActivatedBy: activatedBy,
		Reason:      "manual activation",
	}
	err = s.repo.RecordActivation(ctx, activation)
	if err != nil {
		s.logger.Error("Failed to record activation", zap.Error(err))
		// Don't fail the activation if recording fails
	}

	// Publish Kafka event
	s.publishKafkaEvent(ctx, "world.event.activated", event)

	// Invalidate cache
	s.invalidateCache(ctx, fmt.Sprintf("world:event:%s", id.String()))
	s.invalidateCache(ctx, "world:events:active")

	return nil
}

func (s *service) DeactivateEvent(ctx context.Context, id uuid.UUID) error {
	event, err := s.repo.GetEventByID(ctx, id)
	if err != nil {
		return err
	}
	if event == nil {
		return fmt.Errorf("event not found")
	}

	if event.Status != "active" {
		return fmt.Errorf("event is not active")
	}

	// Update status
	event.Status = "completed"
	now := time.Now()
	event.EndTime = &now
	err = s.repo.UpdateEvent(ctx, event)
	if err != nil {
		return err
	}

	// Publish Kafka event
	s.publishKafkaEvent(ctx, "world.event.deactivated", event)

	// Invalidate cache
	s.invalidateCache(ctx, fmt.Sprintf("world:event:%s", id.String()))
	s.invalidateCache(ctx, "world:events:active")

	return nil
}

func (s *service) AnnounceEvent(ctx context.Context, id uuid.UUID, announcedBy, message string, channels []string) error {
	event, err := s.repo.GetEventByID(ctx, id)
	if err != nil {
		return err
	}
	if event == nil {
		return fmt.Errorf("event not found")
	}

	// Record announcement
	announcement := &EventAnnouncement{
		EventID:      id,
		AnnouncedAt:  time.Now(),
		AnnouncedBy:  announcedBy,
		Message:      message,
		Channels:     channels,
	}
	err = s.repo.RecordAnnouncement(ctx, announcement)
	if err != nil {
		s.logger.Error("Failed to record announcement", zap.Error(err))
		// Don't fail the announcement if recording fails
	}

	// Publish Kafka event
	s.publishKafkaEvent(ctx, "world.event.announced", map[string]interface{}{
		"event_id":     id.String(),
		"message":      message,
		"channels":     channels,
		"announced_by": announcedBy,
	})

	return nil
}

// Helper methods

func (s *service) publishKafkaEvent(ctx context.Context, eventType string, data interface{}) {
	payload, err := json.Marshal(map[string]interface{}{
		"event_type": eventType,
		"timestamp":  time.Now().Unix(),
		"data":       data,
	})
	if err != nil {
		s.logger.Error("Failed to marshal Kafka event", zap.Error(err))
		return
	}

	err = s.kafkaWriter.WriteMessages(ctx, kafka.Message{
		Key:   []byte(eventType),
		Value: payload,
	})
	if err != nil {
		s.logger.Error("Failed to publish Kafka event", zap.Error(err))
	}
}

func (s *service) invalidateCache(ctx context.Context, key string) {
	err := s.redis.Del(ctx, key).Err()
	if err != nil {
		s.logger.Error("Failed to invalidate cache", zap.String("key", key), zap.Error(err))
	}
}









