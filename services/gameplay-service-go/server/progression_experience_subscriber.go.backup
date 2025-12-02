package server

import (
	"context"
	"encoding/json"
	"strconv"
	"strings"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type ProgressionExperienceSubscriber struct {
	progressionService *ProgressionService
	logger             *logrus.Logger
	pubsub             *redis.PubSub
	ctx                context.Context
	cancel             context.CancelFunc
}

func NewProgressionExperienceSubscriber(progressionService *ProgressionService) *ProgressionExperienceSubscriber {
	ctx, cancel := context.WithCancel(context.Background())
	return &ProgressionExperienceSubscriber{
		progressionService: progressionService,
		logger:             GetLogger(),
		ctx:                ctx,
		cancel:             cancel,
	}
}

func (pes *ProgressionExperienceSubscriber) Start() error {
	pes.logger.Info("Starting progression experience subscriber")

	channels := []string{
		"events:combat:enemy-killed:*",
		"events:quest:completed:*",
		"events:skill:used:*",
	}

	redisClient := pes.progressionService.cache
	pes.pubsub = redisClient.PSubscribe(pes.ctx, channels...)

	go pes.listen()
	return nil
}

func (pes *ProgressionExperienceSubscriber) Stop() error {
	pes.logger.Info("Stopping progression experience subscriber")
	pes.cancel()
	if pes.pubsub != nil {
		return pes.pubsub.Close()
	}
	return nil
}

func (pes *ProgressionExperienceSubscriber) listen() {
	ch := pes.pubsub.Channel()

	for {
		select {
		case <-pes.ctx.Done():
			pes.logger.Info("Progression experience subscriber context cancelled")
			return
		case msg := <-ch:
			if msg == nil {
				continue
			}

			pes.handleExperienceEvent(msg.Channel, []byte(msg.Payload))
		}
	}
}

func (pes *ProgressionExperienceSubscriber) handleExperienceEvent(channel string, data []byte) {
	var eventData map[string]interface{}
	if err := json.Unmarshal(data, &eventData); err != nil {
		pes.logger.WithError(err).Error("Failed to unmarshal experience event data")
		return
	}

	pes.logger.WithFields(logrus.Fields{
		"channel": channel,
	}).Debug("Received experience event for progression")

	characterIDStr, ok := eventData["character_id"].(string)
	if !ok {
		pes.logger.WithField("channel", channel).Warn("Event missing character_id")
		return
	}

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		pes.logger.WithError(err).WithField("character_id", characterIDStr).Error("Invalid character_id in event")
		return
	}

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	var experienceAmount int64
	var source string

	if strings.Contains(channel, "combat:enemy-killed") {
		experienceAmount = pes.getExperienceFromCombat(eventData)
		source = "combat"
	} else if strings.Contains(channel, "quest:completed") {
		experienceAmount = pes.getExperienceFromQuest(eventData)
		source = "quest"
	} else if strings.Contains(channel, "skill:used") {
		experienceAmount = pes.getExperienceFromSkill(eventData)
		source = "skill"
	}

	if experienceAmount > 0 {
		if err := pes.progressionService.AddExperience(ctx, characterID, experienceAmount, source); err != nil {
			pes.logger.WithError(err).WithFields(logrus.Fields{
				"character_id": characterID,
				"source":       source,
				"amount":       experienceAmount,
			}).Error("Failed to add experience from event")
		} else {
			pes.logger.WithFields(logrus.Fields{
				"character_id": characterID,
				"source":       source,
				"amount":       experienceAmount,
			}).Debug("Added experience from event")
		}
	}
}

func (pes *ProgressionExperienceSubscriber) getExperienceFromCombat(eventData map[string]interface{}) int64 {
	if exp, ok := eventData["experience"].(float64); ok {
		return int64(exp)
	}
	if exp, ok := eventData["experience"].(int64); ok {
		return exp
	}
	if exp, ok := eventData["experience"].(int); ok {
		return int64(exp)
	}
	if expStr, ok := eventData["experience"].(string); ok {
		if exp, err := strconv.ParseInt(expStr, 10, 64); err == nil {
			return exp
		}
	}

	enemyLevel := 1
	if level, ok := eventData["enemy_level"].(float64); ok {
		enemyLevel = int(level)
	} else if level, ok := eventData["enemy_level"].(int); ok {
		enemyLevel = level
	}

	return int64(enemyLevel * 10)
}

func (pes *ProgressionExperienceSubscriber) getExperienceFromQuest(eventData map[string]interface{}) int64 {
	if exp, ok := eventData["experience"].(float64); ok {
		return int64(exp)
	}
	if exp, ok := eventData["experience"].(int64); ok {
		return exp
	}
	if exp, ok := eventData["experience"].(int); ok {
		return int64(exp)
	}
	if expStr, ok := eventData["experience"].(string); ok {
		if exp, err := strconv.ParseInt(expStr, 10, 64); err == nil {
			return exp
		}
	}

	questLevel := 1
	if level, ok := eventData["quest_level"].(float64); ok {
		questLevel = int(level)
	} else if level, ok := eventData["quest_level"].(int); ok {
		questLevel = level
	}

	return int64(questLevel * 50)
}

func (pes *ProgressionExperienceSubscriber) getExperienceFromSkill(eventData map[string]interface{}) int64 {
	if exp, ok := eventData["experience"].(float64); ok {
		return int64(exp)
	}
	if exp, ok := eventData["experience"].(int64); ok {
		return exp
	}
	if exp, ok := eventData["experience"].(int); ok {
		return int64(exp)
	}
	if expStr, ok := eventData["experience"].(string); ok {
		if exp, err := strconv.ParseInt(expStr, 10, 64); err == nil {
			return exp
		}
	}

	return 5
}

