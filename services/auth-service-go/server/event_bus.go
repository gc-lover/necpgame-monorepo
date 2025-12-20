// Package server Issue: #136
package server

import (
	"context"
	"encoding/json"

	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"
)

// EventBus интерфейс для публикации событий
type EventBus interface {
	PublishEvent(ctx context.Context, eventType string, payload map[string]interface{}) error
}

// RedisEventBus реализация EventBus с Redis
type RedisEventBus struct {
	client *redis.Client
	logger *zap.Logger
}

// NewRedisEventBus создает новый Redis event bus

// PublishEvent публикует событие в Redis канал
func (b *RedisEventBus) PublishEvent(ctx context.Context, eventType string, payload map[string]interface{}) error {
	eventData, err := json.Marshal(payload)
	if err != nil {
		b.logger.Error("Failed to marshal event payload", zap.Error(err))
		return err
	}

	channel := "events:" + eventType
	err = b.client.Publish(ctx, channel, eventData).Err()
	if err != nil {
		b.logger.Error("Failed to publish event", zap.String("eventType", eventType), zap.Error(err))
		return err
	}

	b.logger.Info("Event published successfully", zap.String("eventType", eventType))
	return nil
}

// AuthEventType типы событий аутентификации
type AuthEventType string
