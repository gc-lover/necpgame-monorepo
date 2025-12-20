// Package server Issue: #141889181
package server

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
)

type Matchmaker struct {
	config *MatchmakerConfig
	client *redis.Client
}

func NewMatchmaker(config *MatchmakerConfig) *Matchmaker {
	logger := GetLogger()
	opt, err := redis.ParseURL(config.RedisUrl)
	if err != nil {
		logger.WithError(err).Fatal("Invalid Redis URL")
	}

	client := redis.NewClient(opt)

	logger.WithFields(map[string]interface{}{
		"redis_url": config.RedisUrl,
		"mode":      config.Mode,
		"team_size": config.TeamSize,
	}).Info("Matchmaker initialized")

	return &Matchmaker{
		config: config,
		client: client,
	}
}

func (m *Matchmaker) Close() error {
	return m.client.Close()
}

func (m *Matchmaker) LoopOnce(ctx context.Context) {
	startTime := time.Now()
	defer func() {
		duration := time.Since(startTime).Seconds()
		RecordLoopDuration(duration)
	}()

	tickets := m.popTickets(ctx, m.config.TeamSize)
	if len(tickets) == m.config.TeamSize {
		m.allocate(ctx, tickets)
		RecordMatch()
		RecordTicket("matched")
	} else if len(tickets) > 0 {
		for _, ticket := range tickets {
			m.client.LPush(ctx, redisKeys.queue(m.config.Mode), ticket)
		}
		RecordTicket("pushed_back")
	}

	queueSize, err := m.client.LLen(ctx, redisKeys.queue(m.config.Mode)).Result()
	if err == nil {
		RecordQueueSize(m.config.Mode, int(queueSize))
	}
}

func (m *Matchmaker) popTickets(ctx context.Context, count int) []string {
	var tickets []string
	for i := 0; i < count; i++ {
		result := m.client.RPop(ctx, redisKeys.queue(m.config.Mode))
		if result.Err() == redis.Nil {
			break
		}
		if result.Err() != nil {
			logger := GetLogger()
			logger.WithError(result.Err()).Error("Error popping ticket")
			RecordError("pop_ticket")
			break
		}
		tickets = append(tickets, result.Val())
		RecordTicket("popped")
	}
	return tickets
}

func (m *Matchmaker) allocate(ctx context.Context, tickets []string) {
	instanceId := uuid.New().String()
	logger := GetLogger()

	playersJson, err := json.Marshal(tickets)
	if err != nil {
		logger.WithError(err).Error("Failed to marshal tickets JSON")
		RecordError("marshal_tickets")
		return
	}

	payload := map[string]interface{}{
		"instance": instanceId,
		"mode":     m.config.Mode,
		"players":  string(playersJson),
	}

	payloadJson, err := json.Marshal(payload)
	if err != nil {
		logger.WithError(err).Error("Failed to marshal payload JSON")
		RecordError("marshal_payload")
		return
	}

	args := redis.XAddArgs{
		Stream: redisKeys.allocations(),
		MaxLen: 1000,
		Values: map[string]interface{}{
			"ts":   time.Now().UnixMilli(),
			"data": string(payloadJson),
		},
	}

	if err := m.client.XAdd(ctx, &args).Err(); err != nil {
		logger.WithError(err).Error("Error allocating match")
		RecordError("allocate_match")
	} else {
		logger.WithFields(map[string]interface{}{
			"instance_id": instanceId,
			"mode":        m.config.Mode,
			"players":     len(tickets),
		}).Info("Allocated match")
	}
}

type redisKeysType struct{}

var redisKeys = redisKeysType{}

func (r redisKeysType) queue(mode string) string {
	return fmt.Sprintf("mm:queue:%s", mode)
}

func (r redisKeysType) ticket(id string) string {
	return fmt.Sprintf("mm:ticket:%s", id)
}

func (r redisKeysType) allocations() string {
	return "mm:allocations"
}
