package server

import (
	"context"
	"encoding/json"
	"strings"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type GuildProgressionSubscriber struct {
	guildRepo GuildRepositoryInterface
	eventBus  EventBus
	cache     *redis.Client
	logger    *logrus.Logger
	pubsub    *redis.PubSub
	ctx       context.Context
	cancel    context.CancelFunc
}

func NewGuildProgressionSubscriber(guildRepo GuildRepositoryInterface, eventBus EventBus, cache *redis.Client) *GuildProgressionSubscriber {
	ctx, cancel := context.WithCancel(context.Background())
	return &GuildProgressionSubscriber{
		guildRepo: guildRepo,
		eventBus:  eventBus,
		cache:     cache,
		logger:    GetLogger(),
		ctx:       ctx,
		cancel:    cancel,
	}
}

func (gps *GuildProgressionSubscriber) Start() error {
	gps.logger.Info("Starting guild progression subscriber")

	channels := []string{
		"events:character:level-up:*",
		"events:territory:captured:*",
	}

	gps.pubsub = gps.cache.PSubscribe(gps.ctx, channels...)

	go gps.listen()
	return nil
}

func (gps *GuildProgressionSubscriber) Stop() error {
	gps.logger.Info("Stopping guild progression subscriber")
	gps.cancel()
	if gps.pubsub != nil {
		return gps.pubsub.Close()
	}
	return nil
}

func (gps *GuildProgressionSubscriber) listen() {
	ch := gps.pubsub.Channel()

	for {
		select {
		case <-gps.ctx.Done():
			gps.logger.Info("Guild progression subscriber context cancelled")
			return
		case msg := <-ch:
			if msg == nil {
				continue
			}

			gps.handleProgressionEvent(msg.Channel, []byte(msg.Payload))
		}
	}
}

func (gps *GuildProgressionSubscriber) handleProgressionEvent(channel string, data []byte) {
	var eventData map[string]interface{}
	if err := json.Unmarshal(data, &eventData); err != nil {
		gps.logger.WithError(err).Error("Failed to unmarshal progression event data")
		return
	}

	gps.logger.WithFields(logrus.Fields{
		"channel": channel,
	}).Debug("Received progression event for guild")

	characterIDStr, ok := eventData["character_id"].(string)
	if !ok {
		gps.logger.WithField("channel", channel).Warn("Event missing character_id")
		return
	}

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		gps.logger.WithError(err).WithField("character_id", characterIDStr).Error("Invalid character_id in event")
		return
	}

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	guild, err := gps.findGuildByCharacter(ctx, characterID)
	if err != nil || guild == nil {
		return
	}

	experienceGain := 0
	if strings.Contains(channel, "level-up") {
		experienceGain = 10
	} else if strings.Contains(channel, "territory:captured") {
		experienceGain = 50
	}

	if experienceGain > 0 {
		gps.addGuildExperience(ctx, guild.ID, experienceGain)
	}
}

func (gps *GuildProgressionSubscriber) findGuildByCharacter(ctx context.Context, characterID uuid.UUID) (*models.Guild, error) {
	guilds, err := gps.guildRepo.List(ctx, 100, 0)
	if err != nil {
		return nil, err
	}

	for _, guild := range guilds {
		member, err := gps.guildRepo.GetMember(ctx, guild.ID, characterID)
		if err == nil && member != nil && member.Status == models.GuildMemberStatusActive {
			return &guild, nil
		}
	}

	return nil, nil
}

func (gps *GuildProgressionSubscriber) addGuildExperience(ctx context.Context, guildID uuid.UUID, experience int) {
	guild, err := gps.guildRepo.GetByID(ctx, guildID)
	if err != nil || guild == nil {
		return
	}

	oldLevel := guild.Level
	newExperience := guild.Experience + experience

	requiredExp := gps.calculateRequiredExperience(guild.Level + 1)
	newLevel := guild.Level
	if newExperience >= requiredExp {
		newLevel = guild.Level + 1
		newExperience = newExperience - requiredExp
	}

	err = gps.guildRepo.UpdateLevel(ctx, guildID, newLevel, newExperience)
	if err != nil {
		gps.logger.WithError(err).WithField("guild_id", guildID).Error("Failed to update guild level")
		return
	}

	if newLevel > oldLevel {
		if gps.eventBus != nil {
			payload := map[string]interface{}{
				"guild_id":    guildID.String(),
				"old_level":   oldLevel,
				"new_level":   newLevel,
				"experience":  newExperience,
				"timestamp":   time.Now().Format(time.RFC3339),
			}
			gps.eventBus.PublishEvent(ctx, "guild:leveled-up", payload)
		}
		gps.logger.WithFields(logrus.Fields{
			"guild_id":  guildID,
			"old_level": oldLevel,
			"new_level": newLevel,
		}).Info("Guild leveled up")
	}
}

func (gps *GuildProgressionSubscriber) calculateRequiredExperience(level int) int {
	return level * 1000
}

