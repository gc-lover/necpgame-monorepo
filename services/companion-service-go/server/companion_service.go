package server

import (
	"context"
	"encoding/json"
	"fmt"
	"math"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/companion-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type CompanionRepositoryInterface interface {
	GetCompanionType(ctx context.Context, companionTypeID string) (*models.CompanionType, error)
	ListCompanionTypes(ctx context.Context, category *models.CompanionCategory, limit, offset int) ([]models.CompanionType, error)
	CountCompanionTypes(ctx context.Context, category *models.CompanionCategory) (int, error)
	CreatePlayerCompanion(ctx context.Context, companion *models.PlayerCompanion) error
	GetPlayerCompanion(ctx context.Context, companionID uuid.UUID) (*models.PlayerCompanion, error)
	GetActiveCompanion(ctx context.Context, characterID uuid.UUID) (*models.PlayerCompanion, error)
	UpdatePlayerCompanion(ctx context.Context, companion *models.PlayerCompanion) error
	ListPlayerCompanions(ctx context.Context, characterID uuid.UUID, status *models.CompanionStatus, limit, offset int) ([]models.PlayerCompanion, error)
	CountPlayerCompanions(ctx context.Context, characterID uuid.UUID, status *models.CompanionStatus) (int, error)
	GetCompanionAbility(ctx context.Context, playerCompanionID uuid.UUID, abilityID string) (*models.CompanionAbility, error)
	CreateCompanionAbility(ctx context.Context, ability *models.CompanionAbility) error
	UpdateCompanionAbility(ctx context.Context, ability *models.CompanionAbility) error
	ListCompanionAbilities(ctx context.Context, playerCompanionID uuid.UUID) ([]models.CompanionAbility, error)
}

type CompanionService struct {
	repo     CompanionRepositoryInterface
	cache    *redis.Client
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

func NewCompanionService(dbURL, redisURL string) (*CompanionService, error) {
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		return nil, err
	}

	redisOpts, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, err
	}

	redisClient := redis.NewClient(redisOpts)

	repo := NewCompanionRepository(dbPool)
	eventBus := NewRedisEventBus(redisClient)

	return &CompanionService{
		repo:     repo,
		cache:    redisClient,
		logger:   GetLogger(),
		eventBus: eventBus,
	}, nil
}

func (s *CompanionService) GetCompanionType(ctx context.Context, companionTypeID string) (*models.CompanionType, error) {
	return s.repo.GetCompanionType(ctx, companionTypeID)
}

func (s *CompanionService) ListCompanionTypes(ctx context.Context, category *models.CompanionCategory, limit, offset int) (*models.CompanionTypeListResponse, error) {
	types, err := s.repo.ListCompanionTypes(ctx, category, limit, offset)
	if err != nil {
		return nil, err
	}

	total, err := s.repo.CountCompanionTypes(ctx, category)
	if err != nil {
		return nil, err
	}

	return &models.CompanionTypeListResponse{
		Types: types,
		Total: total,
	}, nil
}

func (s *CompanionService) PurchaseCompanion(ctx context.Context, characterID uuid.UUID, companionTypeID string) (*models.PlayerCompanion, error) {
	companionType, err := s.repo.GetCompanionType(ctx, companionTypeID)
	if err != nil {
		return nil, err
	}

	if companionType == nil {
		return nil, fmt.Errorf("companion type not found")
	}

	existing, err := s.repo.GetPlayerCompanion(ctx, uuid.Nil)
	if err != nil {
		return nil, err
	}

	companions, err := s.repo.ListPlayerCompanions(ctx, characterID, nil, 100, 0)
	if err != nil {
		return nil, err
	}

	for _, c := range companions {
		if c.CompanionTypeID == companionTypeID {
			return nil, fmt.Errorf("companion already owned")
		}
	}

	baseStats := make(map[string]interface{})
	if companionType.Stats != nil {
		for k, v := range companionType.Stats {
			baseStats[k] = v
		}
	}

	companion := &models.PlayerCompanion{
		CharacterID:     characterID,
		CompanionTypeID: companionTypeID,
		Level:           1,
		Experience:      0,
		Status:          models.CompanionStatusOwned,
		Equipment:       make(map[string]interface{}),
		Stats:           baseStats,
	}

	err = s.repo.CreatePlayerCompanion(ctx, companion)
	if err != nil {
		return nil, err
	}

	_ = existing

	err = s.publishCompanionPurchasedEvent(ctx, characterID, companionTypeID, companion.ID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to publish companion purchased event")
	}

	RecordCompanionOperation("purchased")

	return companion, nil
}

func (s *CompanionService) GetPlayerCompanion(ctx context.Context, companionID uuid.UUID) (*models.PlayerCompanion, error) {
	return s.repo.GetPlayerCompanion(ctx, companionID)
}

func (s *CompanionService) GetCompanionDetail(ctx context.Context, companionID uuid.UUID) (*models.CompanionDetailResponse, error) {
	companion, err := s.repo.GetPlayerCompanion(ctx, companionID)
	if err != nil {
		return nil, err
	}

	if companion == nil {
		return nil, fmt.Errorf("companion not found")
	}

	companionType, err := s.repo.GetCompanionType(ctx, companion.CompanionTypeID)
	if err != nil {
		return nil, err
	}

	abilities, err := s.repo.ListCompanionAbilities(ctx, companionID)
	if err != nil {
		return nil, err
	}

	return &models.CompanionDetailResponse{
		Companion: companion,
		Type:      companionType,
		Abilities: abilities,
	}, nil
}

func (s *CompanionService) SummonCompanion(ctx context.Context, characterID uuid.UUID, companionID uuid.UUID) error {
	companion, err := s.repo.GetPlayerCompanion(ctx, companionID)
	if err != nil {
		return err
	}

	if companion == nil {
		return fmt.Errorf("companion not found")
	}

	if companion.CharacterID != characterID {
		return fmt.Errorf("companion does not belong to character")
	}

	if companion.Status == models.CompanionStatusSummoned {
		return fmt.Errorf("companion already summoned")
	}

	active, err := s.repo.GetActiveCompanion(ctx, characterID)
	if err != nil {
		return err
	}

	if active != nil && active.ID != companionID {
		active.Status = models.CompanionStatusDismissed
		active.SummonedAt = nil
		err = s.repo.UpdatePlayerCompanion(ctx, active)
		if err != nil {
			s.logger.WithError(err).Error("Failed to dismiss active companion")
		}
	}

	now := time.Now()
	companion.Status = models.CompanionStatusSummoned
	companion.SummonedAt = &now

	err = s.repo.UpdatePlayerCompanion(ctx, companion)
	if err != nil {
		return err
	}

	err = s.publishCompanionSummonedEvent(ctx, characterID, companionID, companion.CompanionTypeID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to publish companion summoned event")
	}

	RecordCompanionOperation("summoned")

	return nil
}

func (s *CompanionService) DismissCompanion(ctx context.Context, characterID uuid.UUID, companionID uuid.UUID) error {
	companion, err := s.repo.GetPlayerCompanion(ctx, companionID)
	if err != nil {
		return err
	}

	if companion == nil {
		return fmt.Errorf("companion not found")
	}

	if companion.CharacterID != characterID {
		return fmt.Errorf("companion does not belong to character")
	}

	if companion.Status != models.CompanionStatusSummoned {
		return fmt.Errorf("companion is not summoned")
	}

	companion.Status = models.CompanionStatusDismissed
	companion.SummonedAt = nil

	err = s.repo.UpdatePlayerCompanion(ctx, companion)
	if err != nil {
		return err
	}

	err = s.publishCompanionDismissedEvent(ctx, characterID, companionID, companion.CompanionTypeID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to publish companion dismissed event")
	}

	RecordCompanionOperation("dismissed")

	return nil
}

func (s *CompanionService) RenameCompanion(ctx context.Context, characterID uuid.UUID, companionID uuid.UUID, customName string) error {
	companion, err := s.repo.GetPlayerCompanion(ctx, companionID)
	if err != nil {
		return err
	}

	if companion == nil {
		return fmt.Errorf("companion not found")
	}

	if companion.CharacterID != characterID {
		return fmt.Errorf("companion does not belong to character")
	}

	companion.CustomName = &customName

	err = s.repo.UpdatePlayerCompanion(ctx, companion)
	if err != nil {
		return err
	}

	RecordCompanionOperation("renamed")

	return nil
}

func (s *CompanionService) AddExperience(ctx context.Context, characterID uuid.UUID, companionID uuid.UUID, amount int64, source string) error {
	companion, err := s.repo.GetPlayerCompanion(ctx, companionID)
	if err != nil {
		return err
	}

	if companion == nil {
		return fmt.Errorf("companion not found")
	}

	if companion.CharacterID != characterID {
		return fmt.Errorf("companion does not belong to character")
	}

	oldLevel := companion.Level
	companion.Experience += amount

	maxLevel := 50
	for companion.Level < maxLevel {
		expForNext := s.calculateExperienceForLevel(companion.Level + 1)
		if companion.Experience < expForNext {
			break
		}
		companion.Level++
	}

	if companion.Level > oldLevel {
		s.updateCompanionStats(companion)
		err = s.publishCompanionLevelUpEvent(ctx, characterID, companionID, companion.Level)
		if err != nil {
			s.logger.WithError(err).Error("Failed to publish companion level up event")
		}
	}

	err = s.repo.UpdatePlayerCompanion(ctx, companion)
	if err != nil {
		return err
	}

	RecordExperienceAdded(source, float64(amount))

	return nil
}

func (s *CompanionService) UseAbility(ctx context.Context, characterID uuid.UUID, companionID uuid.UUID, abilityID string) error {
	companion, err := s.repo.GetPlayerCompanion(ctx, companionID)
	if err != nil {
		return err
	}

	if companion == nil {
		return fmt.Errorf("companion not found")
	}

	if companion.CharacterID != characterID {
		return fmt.Errorf("companion does not belong to character")
	}

	if companion.Status != models.CompanionStatusSummoned {
		return fmt.Errorf("companion is not summoned")
	}

	ability, err := s.repo.GetCompanionAbility(ctx, companionID, abilityID)
	if err != nil {
		return err
	}

	if ability != nil && ability.CooldownUntil != nil {
		if time.Now().Before(*ability.CooldownUntil) {
			return fmt.Errorf("ability is on cooldown")
		}
	}

	now := time.Now()
	cooldownUntil := now.Add(30 * time.Second)

	if ability == nil {
		ability = &models.CompanionAbility{
			PlayerCompanionID: companionID,
			AbilityID:         abilityID,
			IsActive:          true,
			CooldownUntil:     &cooldownUntil,
			LastUsedAt:        &now,
		}
		err = s.repo.CreateCompanionAbility(ctx, ability)
	} else {
		ability.CooldownUntil = &cooldownUntil
		ability.LastUsedAt = &now
		err = s.repo.UpdateCompanionAbility(ctx, ability)
	}

	if err != nil {
		return err
	}

	err = s.publishCompanionAbilityUsedEvent(ctx, characterID, companionID, abilityID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to publish companion ability used event")
	}

	RecordCompanionOperation("ability_used")

	return nil
}

func (s *CompanionService) ListPlayerCompanions(ctx context.Context, characterID uuid.UUID, status *models.CompanionStatus, limit, offset int) (*models.PlayerCompanionListResponse, error) {
	companions, err := s.repo.ListPlayerCompanions(ctx, characterID, status, limit, offset)
	if err != nil {
		return nil, err
	}

	total, err := s.repo.CountPlayerCompanions(ctx, characterID, status)
	if err != nil {
		return nil, err
	}

	return &models.PlayerCompanionListResponse{
		Companions: companions,
		Total:      total,
	}, nil
}

func (s *CompanionService) calculateExperienceForLevel(level int) int64 {
	baseExp := 100.0
	multiplier := 1.4
	return int64(baseExp * math.Pow(float64(level-1), multiplier))
}

func (s *CompanionService) updateCompanionStats(companion *models.PlayerCompanion) {
	if companion.Stats == nil {
		companion.Stats = make(map[string]interface{})
	}

	if health, ok := companion.Stats["health"].(float64); ok {
		companion.Stats["health"] = health * (1.0 + float64(companion.Level-1)*0.05)
	}

	if damage, ok := companion.Stats["damage"].(float64); ok {
		companion.Stats["damage"] = damage * (1.0 + float64(companion.Level-1)*0.03)
	}
}

func (s *CompanionService) publishCompanionPurchasedEvent(ctx context.Context, characterID uuid.UUID, companionTypeID string, companionID uuid.UUID) error {
	payload := map[string]interface{}{
		"character_id":     characterID.String(),
		"companion_type_id": companionTypeID,
		"companion_id":     companionID.String(),
		"timestamp":        time.Now().Format(time.RFC3339),
	}

	return s.eventBus.PublishEvent(ctx, "companion:purchased", payload)
}

func (s *CompanionService) publishCompanionSummonedEvent(ctx context.Context, characterID uuid.UUID, companionID uuid.UUID, companionTypeID string) error {
	payload := map[string]interface{}{
		"character_id":     characterID.String(),
		"companion_id":     companionID.String(),
		"companion_type_id": companionTypeID,
		"timestamp":        time.Now().Format(time.RFC3339),
	}

	return s.eventBus.PublishEvent(ctx, "companion:summoned", payload)
}

func (s *CompanionService) publishCompanionDismissedEvent(ctx context.Context, characterID uuid.UUID, companionID uuid.UUID, companionTypeID string) error {
	payload := map[string]interface{}{
		"character_id":     characterID.String(),
		"companion_id":     companionID.String(),
		"companion_type_id": companionTypeID,
		"timestamp":        time.Now().Format(time.RFC3339),
	}

	return s.eventBus.PublishEvent(ctx, "companion:dismissed", payload)
}

func (s *CompanionService) publishCompanionLevelUpEvent(ctx context.Context, characterID uuid.UUID, companionID uuid.UUID, level int) error {
	payload := map[string]interface{}{
		"character_id": characterID.String(),
		"companion_id": companionID.String(),
		"level":        level,
		"timestamp":   time.Now().Format(time.RFC3339),
	}

	return s.eventBus.PublishEvent(ctx, "companion:level-up", payload)
}

func (s *CompanionService) publishCompanionAbilityUsedEvent(ctx context.Context, characterID uuid.UUID, companionID uuid.UUID, abilityID string) error {
	payload := map[string]interface{}{
		"character_id": characterID.String(),
		"companion_id": companionID.String(),
		"ability_id":   abilityID,
		"timestamp":    time.Now().Format(time.RFC3339),
	}

	return s.eventBus.PublishEvent(ctx, "companion:ability-used", payload)
}

