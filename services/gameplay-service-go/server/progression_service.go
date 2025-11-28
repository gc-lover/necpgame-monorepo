package server

import (
	"context"
	"encoding/json"
	"fmt"
	"math"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/gameplay-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type ProgressionRepositoryInterface interface {
	GetProgression(ctx context.Context, characterID uuid.UUID) (*models.CharacterProgression, error)
	CreateProgression(ctx context.Context, progression *models.CharacterProgression) error
	UpdateProgression(ctx context.Context, progression *models.CharacterProgression) error
	GetSkillExperience(ctx context.Context, characterID uuid.UUID, skillID string) (*models.SkillExperience, error)
	CreateSkillExperience(ctx context.Context, skillExp *models.SkillExperience) error
	UpdateSkillExperience(ctx context.Context, skillExp *models.SkillExperience) error
	ListSkillExperience(ctx context.Context, characterID uuid.UUID, limit, offset int) ([]models.SkillExperience, error)
	CountSkillExperience(ctx context.Context, characterID uuid.UUID) (int, error)
}

type ProgressionService struct {
	repo   ProgressionRepositoryInterface
	db     *pgxpool.Pool
	cache  *redis.Client
	logger *logrus.Logger
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

func NewProgressionService(dbURL, redisURL string) (*ProgressionService, error) {
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		return nil, err
	}

	redisOpts, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, err
	}

	redisClient := redis.NewClient(redisOpts)

	repo := NewProgressionRepository(dbPool)
	eventBus := NewRedisEventBus(redisClient)

	return &ProgressionService{
		repo:     repo,
		db:       dbPool,
		cache:    redisClient,
		logger:   GetLogger(),
		eventBus: eventBus,
	}, nil
}

func (s *ProgressionService) GetDBPool() *pgxpool.Pool {
	return s.db
}

func (s *ProgressionService) GetProgression(ctx context.Context, characterID uuid.UUID) (*models.CharacterProgression, error) {
	cacheKey := fmt.Sprintf("progression:%s", characterID.String())

	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil {
		var progression models.CharacterProgression
		if err := json.Unmarshal([]byte(cached), &progression); err == nil {
			return &progression, nil
		} else {
			s.logger.WithError(err).Error("Failed to unmarshal cached progression JSON")
		}
	}

	progression, err := s.repo.GetProgression(ctx, characterID)
	if err != nil {
		return nil, err
	}

	if progression == nil {
		progression = &models.CharacterProgression{
			CharacterID:      characterID,
			Level:            1,
			Experience:       0,
			ExperienceToNext: s.calculateExperienceForLevel(2),
			AttributePoints:  0,
			SkillPoints:      0,
			Attributes:       make(map[string]int),
			UpdatedAt:        time.Now(),
		}
		err = s.repo.CreateProgression(ctx, progression)
		if err != nil {
			return nil, err
		}
	}

	data, err := json.Marshal(progression)
	if err != nil {
		s.logger.WithError(err).Error("Failed to marshal progression JSON")
		return progression, nil
	}
	s.cache.Set(ctx, cacheKey, data, 5*time.Minute)

	return progression, nil
}

func (s *ProgressionService) AddExperience(ctx context.Context, characterID uuid.UUID, amount int64, source string) error {
	progression, err := s.GetProgression(ctx, characterID)
	if err != nil {
		return err
	}

	oldLevel := progression.Level
	progression.Experience += amount

	for progression.Experience >= progression.ExperienceToNext {
		progression.Level++
		progression.ExperienceToNext = s.calculateExperienceForLevel(progression.Level + 1)
		progression.AttributePoints += 2
		progression.SkillPoints += 1

		if progression.Level > oldLevel {
			err = s.publishLevelUpEvent(ctx, characterID, progression.Level)
			if err != nil {
				s.logger.WithError(err).Error("Failed to publish level up event")
			}
			RecordLevelUp()
		}
	}

	err = s.repo.UpdateProgression(ctx, progression)
	if err != nil {
		return err
	}

	cacheKey := fmt.Sprintf("progression:%s", characterID.String())
	s.cache.Del(ctx, cacheKey)

	RecordExperienceAdded(source, float64(amount))

	return nil
}

func (s *ProgressionService) AddSkillExperience(ctx context.Context, characterID uuid.UUID, skillID string, amount int64) error {
	skillExp, err := s.repo.GetSkillExperience(ctx, characterID, skillID)
	if err != nil {
		return err
	}

	if skillExp == nil {
		skillExp = &models.SkillExperience{
			CharacterID: characterID,
			SkillID:     skillID,
			Level:       1,
			Experience:  0,
			UpdatedAt:   time.Now(),
		}
	}

	skillExp.Experience += amount
	skillExp.Level = s.calculateSkillLevel(skillExp.Experience)

	if skillExp.ID == uuid.Nil {
		err = s.repo.CreateSkillExperience(ctx, skillExp)
	} else {
		err = s.repo.UpdateSkillExperience(ctx, skillExp)
	}

	if err != nil {
		return err
	}

	err = s.publishSkillLeveledEvent(ctx, characterID, skillID, skillExp.Level)
	if err != nil {
		s.logger.WithError(err).Error("Failed to publish skill leveled event")
	}

	return nil
}

func (s *ProgressionService) AllocateAttributePoint(ctx context.Context, characterID uuid.UUID, attribute string) error {
	progression, err := s.GetProgression(ctx, characterID)
	if err != nil {
		return err
	}

	if progression.AttributePoints < 1 {
		return fmt.Errorf("not enough attribute points")
	}

	if progression.Attributes == nil {
		progression.Attributes = make(map[string]int)
	}

	currentValue := progression.Attributes[attribute]
	if currentValue >= 25 {
		return fmt.Errorf("attribute %s is at maximum", attribute)
	}

	progression.Attributes[attribute] = currentValue + 1
	progression.AttributePoints--

	err = s.repo.UpdateProgression(ctx, progression)
	if err != nil {
		return err
	}

	cacheKey := fmt.Sprintf("progression:%s", characterID.String())
	s.cache.Del(ctx, cacheKey)

	err = s.publishAttributeIncreasedEvent(ctx, characterID, attribute, progression.Attributes[attribute])
	if err != nil {
		s.logger.WithError(err).Error("Failed to publish attribute increased event")
	}

	return nil
}

func (s *ProgressionService) AllocateSkillPoint(ctx context.Context, characterID uuid.UUID, skillID string) error {
	progression, err := s.GetProgression(ctx, characterID)
	if err != nil {
		return err
	}

	if progression.SkillPoints < 1 {
		return fmt.Errorf("not enough skill points")
	}

	skillExp, err := s.repo.GetSkillExperience(ctx, characterID, skillID)
	if err != nil {
		return err
	}

	if skillExp == nil {
		skillExp = &models.SkillExperience{
			CharacterID: characterID,
			SkillID:     skillID,
			Level:       1,
			Experience:  0,
			UpdatedAt:   time.Now(),
		}
	}

	skillExp.Level++
	progression.SkillPoints--

	err = s.repo.UpdateProgression(ctx, progression)
	if err != nil {
		return err
	}

	if skillExp.ID == uuid.Nil {
		err = s.repo.CreateSkillExperience(ctx, skillExp)
	} else {
		err = s.repo.UpdateSkillExperience(ctx, skillExp)
	}

	if err != nil {
		return err
	}

	cacheKey := fmt.Sprintf("progression:%s", characterID.String())
	s.cache.Del(ctx, cacheKey)

	err = s.publishSkillLeveledEvent(ctx, characterID, skillID, skillExp.Level)
	if err != nil {
		s.logger.WithError(err).Error("Failed to publish skill leveled event")
	}

	return nil
}

func (s *ProgressionService) GetSkillProgression(ctx context.Context, characterID uuid.UUID, limit, offset int) (*models.SkillProgressionResponse, error) {
	skills, err := s.repo.ListSkillExperience(ctx, characterID, limit, offset)
	if err != nil {
		return nil, err
	}

	total, err := s.repo.CountSkillExperience(ctx, characterID)
	if err != nil {
		return nil, err
	}

	return &models.SkillProgressionResponse{
		Skills: skills,
		Total:  total,
	}, nil
}

func (s *ProgressionService) calculateExperienceForLevel(level int) int64 {
	baseExp := 100.0
	multiplier := 1.5
	return int64(baseExp * math.Pow(float64(level-1), multiplier))
}

func (s *ProgressionService) calculateSkillLevel(experience int64) int {
	baseExp := 50.0
	multiplier := 1.3
	level := 1

	for {
		requiredExp := int64(baseExp * math.Pow(float64(level), multiplier))
		if experience < requiredExp {
			break
		}
		level++
	}

	return level
}

func (s *ProgressionService) publishLevelUpEvent(ctx context.Context, characterID uuid.UUID, level int) error {
	payload := map[string]interface{}{
		"character_id": characterID.String(),
		"level":        level,
		"timestamp":     time.Now().Format(time.RFC3339),
	}

	return s.eventBus.PublishEvent(ctx, "character:level-up", payload)
}

func (s *ProgressionService) publishSkillLeveledEvent(ctx context.Context, characterID uuid.UUID, skillID string, level int) error {
	payload := map[string]interface{}{
		"character_id": characterID.String(),
		"skill_id":     skillID,
		"level":        level,
		"timestamp":    time.Now().Format(time.RFC3339),
	}

	return s.eventBus.PublishEvent(ctx, "character:skill-leveled", payload)
}

func (s *ProgressionService) publishAttributeIncreasedEvent(ctx context.Context, characterID uuid.UUID, attribute string, value int) error {
	payload := map[string]interface{}{
		"character_id": characterID.String(),
		"attribute":    attribute,
		"value":        value,
		"timestamp":    time.Now().Format(time.RFC3339),
	}

	return s.eventBus.PublishEvent(ctx, "character:attribute-increased", payload)
}

