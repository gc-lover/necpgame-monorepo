package service

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/go-faster/errors"
	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"go.opentelemetry.io/otel/attribute"
	"go.uber.org/zap"
)

// DifficultyModeManager управляет режимами сложности с кешированием и оптимизациями
type DifficultyModeManager struct {
	service     *Service
	logger      *zap.Logger
	cacheTTL    time.Duration
	cachePrefix string
}

// NewDifficultyModeManager создает новый менеджер режимов сложности
func NewDifficultyModeManager(svc *Service, logger *zap.Logger) *DifficultyModeManager {
	return &DifficultyModeManager{
		service:     svc,
		logger:      logger,
		cacheTTL:    30 * time.Minute, // Кеш на 30 минут для MMOFPS
		cachePrefix: "difficulty_mode:",
	}
}

// GetDifficultyMode получает режим сложности по ID с кешированием
func (dm *DifficultyModeManager) GetDifficultyMode(ctx context.Context, modeID uuid.UUID) (*DifficultyMode, error) {
	ctx, span := dm.service.GetTracer().Start(ctx, "DifficultyModeManager.GetDifficultyMode")
	defer span.End()

	span.SetAttributes(attribute.String("mode.id", modeID.String()))

	// Сначала пытаемся получить из кеша
	mode, err := dm.getFromCache(ctx, modeID)
	if err == nil && mode != nil {
		dm.logger.Debug("Difficulty mode retrieved from cache",
			zap.String("mode_id", modeID.String()))
		return mode, nil
	}

	// Если не в кеше, получаем из БД
	mode, err = dm.getFromDB(ctx, modeID)
	if err != nil {
		return nil, errors.Wrap(err, "failed to get difficulty mode from database")
	}

	// Сохраняем в кеш для будущих запросов
	if err := dm.saveToCache(ctx, mode); err != nil {
		dm.logger.Warn("Failed to save difficulty mode to cache",
			zap.Error(err), zap.String("mode_id", modeID.String()))
		// Не возвращаем ошибку, так как основная операция успешна
	}

	return mode, nil
}

// GetAllDifficultyModes получает все режимы сложности с пагинацией
func (dm *DifficultyModeManager) GetAllDifficultyModes(ctx context.Context, limit, offset int) ([]*DifficultyMode, error) {
	ctx, span := dm.service.GetTracer().Start(ctx, "DifficultyModeManager.GetAllDifficultyModes")
	defer span.End()

	span.SetAttributes(
		attribute.Int("limit", limit),
		attribute.Int("offset", offset),
	)

	modes, err := dm.getAllFromDB(ctx, limit, offset)
	if err != nil {
		return nil, errors.Wrap(err, "failed to get difficulty modes from database")
	}

	dm.logger.Debug("Retrieved difficulty modes",
		zap.Int("count", len(modes)),
		zap.Int("limit", limit),
		zap.Int("offset", offset))

	return modes, nil
}

// GetModeRequirements получает требования для режима сложности
func (dm *DifficultyModeManager) GetModeRequirements(ctx context.Context, modeID uuid.UUID) (*DifficultyRequirements, error) {
	ctx, span := dm.service.GetTracer().Start(ctx, "DifficultyModeManager.GetModeRequirements")
	defer span.End()

	span.SetAttributes(attribute.String("mode.id", modeID.String()))

	// Проверяем кеш требований
	cacheKey := fmt.Sprintf("%srequirements:%s", dm.cachePrefix, modeID.String())
	cachedReqs, err := dm.service.GetRedis().Get(ctx, cacheKey).Result()
	if err == nil {
		var reqs DifficultyRequirements
		if err := json.Unmarshal([]byte(cachedReqs), &reqs); err == nil {
			return &reqs, nil
		}
	}

	// Получаем требования из БД
	reqs, err := dm.getRequirementsFromDB(ctx, modeID)
	if err != nil {
		return nil, errors.Wrap(err, "failed to get mode requirements from database")
	}

	// Кешируем требования
	reqsJSON, err := json.Marshal(reqs)
	if err == nil {
		dm.service.GetRedis().Set(ctx, cacheKey, reqsJSON, dm.cacheTTL)
	}

	return reqs, nil
}

// CreateDifficultyMode создает новый режим сложности
func (dm *DifficultyModeManager) CreateDifficultyMode(ctx context.Context, mode *DifficultyMode) error {
	ctx, span := dm.service.GetTracer().Start(ctx, "DifficultyModeManager.CreateDifficultyMode")
	defer span.End()

	span.SetAttributes(
		attribute.String("mode.name", mode.Name),
		attribute.String("mode.level", string(mode.Level)),
	)

	// Валидация режима
	if err := dm.validateDifficultyMode(mode); err != nil {
		return errors.Wrap(err, "difficulty mode validation failed")
	}

	// Сохраняем в БД
	if err := dm.saveToDB(ctx, mode); err != nil {
		return errors.Wrap(err, "failed to save difficulty mode to database")
	}

	// Сохраняем в кеш
	if err := dm.saveToCache(ctx, mode); err != nil {
		dm.logger.Warn("Failed to save new difficulty mode to cache",
			zap.Error(err), zap.String("mode_id", mode.ID.String()))
	}

	dm.logger.Info("Difficulty mode created",
		zap.String("mode_id", mode.ID.String()),
		zap.String("mode_name", mode.Name),
		zap.String("mode_level", string(mode.Level)))

	return nil
}

// UpdateDifficultyMode обновляет режим сложности
func (dm *DifficultyModeManager) UpdateDifficultyMode(ctx context.Context, mode *DifficultyMode) error {
	ctx, span := dm.service.GetTracer().Start(ctx, "DifficultyModeManager.UpdateDifficultyMode")
	defer span.End()

	span.SetAttributes(attribute.String("mode.id", mode.ID.String()))

	// Валидация режима
	if err := dm.validateDifficultyMode(mode); err != nil {
		return errors.Wrap(err, "difficulty mode validation failed")
	}

	// Обновляем в БД
	mode.UpdatedAt = time.Now()
	if err := dm.updateInDB(ctx, mode); err != nil {
		return errors.Wrap(err, "failed to update difficulty mode in database")
	}

	// Инвалидируем кеш
	if err := dm.invalidateCache(ctx, mode.ID); err != nil {
		dm.logger.Warn("Failed to invalidate difficulty mode cache",
			zap.Error(err), zap.String("mode_id", mode.ID.String()))
	}

	dm.logger.Info("Difficulty mode updated",
		zap.String("mode_id", mode.ID.String()),
		zap.String("mode_name", mode.Name))

	return nil
}

// GetContentDifficultyModes получает доступные режимы для контента
func (dm *DifficultyModeManager) GetContentDifficultyModes(ctx context.Context, contentID uuid.UUID) ([]*ContentDifficultyMode, error) {
	ctx, span := dm.service.GetTracer().Start(ctx, "DifficultyModeManager.GetContentDifficultyModes")
	defer span.End()

	span.SetAttributes(attribute.String("content.id", contentID.String()))

	modes, err := dm.getContentModesFromDB(ctx, contentID)
	if err != nil {
		return nil, errors.Wrap(err, "failed to get content difficulty modes from database")
	}

	dm.logger.Debug("Retrieved content difficulty modes",
		zap.String("content_id", contentID.String()),
		zap.Int("modes_count", len(modes)))

	return modes, nil
}

// Вспомогательные методы для работы с кешем

func (dm *DifficultyModeManager) getFromCache(ctx context.Context, modeID uuid.UUID) (*DifficultyMode, error) {
	cacheKey := fmt.Sprintf("%s%s", dm.cachePrefix, modeID.String())
	cachedMode, err := dm.service.GetRedis().Get(ctx, cacheKey).Result()
	if err == redis.Nil {
		return nil, nil // Не найдено в кеше
	}
	if err != nil {
		return nil, errors.Wrap(err, "failed to get from cache")
	}

	var mode DifficultyMode
	if err := json.Unmarshal([]byte(cachedMode), &mode); err != nil {
		return nil, errors.Wrap(err, "failed to unmarshal cached mode")
	}

	return &mode, nil
}

func (dm *DifficultyModeManager) saveToCache(ctx context.Context, mode *DifficultyMode) error {
	cacheKey := fmt.Sprintf("%s%s", dm.cachePrefix, mode.ID.String())
	modeJSON, err := json.Marshal(mode)
	if err != nil {
		return errors.Wrap(err, "failed to marshal mode for cache")
	}

	return dm.service.GetRedis().Set(ctx, cacheKey, modeJSON, dm.cacheTTL).Err()
}

func (dm *DifficultyModeManager) invalidateCache(ctx context.Context, modeID uuid.UUID) error {
	cacheKey := fmt.Sprintf("%s%s", dm.cachePrefix, modeID.String())
	return dm.service.GetRedis().Del(ctx, cacheKey).Err()
}

// Методы для работы с БД (заглушки для интеграции)

func (dm *DifficultyModeManager) getFromDB(ctx context.Context, modeID uuid.UUID) (*DifficultyMode, error) {
	// В реальной реализации здесь будет SQL запрос к PostgreSQL
	// Пока возвращаем тестовые данные

	switch modeID.String() {
	case "550e8400-e29b-41d4-a716-446655440000": // Master Mode
		return &DifficultyMode{
			ID:                  modeID,
			Name:                "Master Mode",
			Level:               DifficultyMaster,
			Description:         "Extreme challenge for experienced players",
			HpModifier:          2.0,
			DamageModifier:      1.5,
			TimeLimitMultiplier: 1.0,
			RespawnLimit:        3,
			CheckpointLimit:     2,
			RewardModifier:      2.0,
			Permadeath:          false,
			SpecialMechanics:    []string{"enhanced_ai"},
			CreatedAt:           time.Now().Add(-24 * time.Hour),
			UpdatedAt:           time.Now(),
		}, nil

	case "550e8400-e29b-41d4-a716-446655440001": // Grandmaster Mode
		return &DifficultyMode{
			ID:                  modeID,
			Name:                "Grandmaster Mode",
			Level:               DifficultyGrandmaster,
			Description:         "Nightmare fuel for elite players",
			HpModifier:          3.0,
			DamageModifier:      2.0,
			TimeLimitMultiplier: 0.75,
			RespawnLimit:        1,
			CheckpointLimit:     1,
			RewardModifier:      3.0,
			Permadeath:          false,
			SpecialMechanics:    []string{"enhanced_ai", "resource_scarcity"},
			CreatedAt:           time.Now().Add(-48 * time.Hour),
			UpdatedAt:           time.Now(),
		}, nil

	case "550e8400-e29b-41d4-a716-446655440002": // Legendary Mode
		return &DifficultyMode{
			ID:                  modeID,
			Name:                "Legendary Mode",
			Level:               DifficultyLegendary,
			Description:         "Impossible challenge for legends",
			HpModifier:          4.0,
			DamageModifier:      2.5,
			TimeLimitMultiplier: 0.5,
			RespawnLimit:        0,
			CheckpointLimit:     0,
			RewardModifier:      5.0,
			Permadeath:          true,
			SpecialMechanics:    []string{"enhanced_ai", "resource_scarcity", "time_dilation"},
			CreatedAt:           time.Now().Add(-72 * time.Hour),
			UpdatedAt:           time.Now(),
		}, nil
	}

	return nil, fmt.Errorf("difficulty mode not found: %s", modeID.String())
}

func (dm *DifficultyModeManager) getAllFromDB(ctx context.Context, limit, offset int) ([]*DifficultyMode, error) {
	// В реальной реализации здесь будет SQL запрос с пагинацией
	// Пока возвращаем тестовые данные
	return []*DifficultyMode{
		{
			ID:                  uuid.MustParse("550e8400-e29b-41d4-a716-446655440000"),
			Name:                "Master Mode",
			Level:               DifficultyMaster,
			Description:         "Extreme challenge for experienced players",
			HpModifier:          2.0,
			DamageModifier:      1.5,
			TimeLimitMultiplier: 1.0,
			RespawnLimit:        3,
			CheckpointLimit:     2,
			RewardModifier:      2.0,
			Permadeath:          false,
			SpecialMechanics:    []string{"enhanced_ai"},
			CreatedAt:           time.Now().Add(-24 * time.Hour),
			UpdatedAt:           time.Now(),
		},
		{
			ID:                  uuid.MustParse("550e8400-e29b-41d4-a716-446655440001"),
			Name:                "Grandmaster Mode",
			Level:               DifficultyGrandmaster,
			Description:         "Nightmare fuel for elite players",
			HpModifier:          3.0,
			DamageModifier:      2.0,
			TimeLimitMultiplier: 0.75,
			RespawnLimit:        1,
			CheckpointLimit:     1,
			RewardModifier:      3.0,
			Permadeath:          false,
			SpecialMechanics:    []string{"enhanced_ai", "resource_scarcity"},
			CreatedAt:           time.Now().Add(-48 * time.Hour),
			UpdatedAt:           time.Now(),
		},
		{
			ID:                  uuid.MustParse("550e8400-e29b-41d4-a716-446655440002"),
			Name:                "Legendary Mode",
			Level:               DifficultyLegendary,
			Description:         "Impossible challenge for legends",
			HpModifier:          4.0,
			DamageModifier:      2.5,
			TimeLimitMultiplier: 0.5,
			RespawnLimit:        0,
			CheckpointLimit:     0,
			RewardModifier:      5.0,
			Permadeath:          true,
			SpecialMechanics:    []string{"enhanced_ai", "resource_scarcity", "time_dilation"},
			CreatedAt:           time.Now().Add(-72 * time.Hour),
			UpdatedAt:           time.Now(),
		},
	}, nil
}

func (dm *DifficultyModeManager) getRequirementsFromDB(ctx context.Context, modeID uuid.UUID) (*DifficultyRequirements, error) {
	// В реальной реализации здесь будет SQL запрос
	// Пока возвращаем тестовые требования
	return &DifficultyRequirements{
		MinLevel:          25,
		MinSkillRating:    1500,
		CompletedMissions: []uuid.UUID{}, // Пустой список для упрощения
		ReputationLevel:   "skilled",
		PrerequisiteModes: []uuid.UUID{},
	}, nil
}

func (dm *DifficultyModeManager) saveToDB(ctx context.Context, mode *DifficultyMode) error {
	// В реальной реализации здесь будет INSERT запрос
	dm.logger.Info("Saving difficulty mode to database", zap.String("mode_id", mode.ID.String()))
	return nil
}

func (dm *DifficultyModeManager) updateInDB(ctx context.Context, mode *DifficultyMode) error {
	// В реальной реализации здесь будет UPDATE запрос
	dm.logger.Info("Updating difficulty mode in database", zap.String("mode_id", mode.ID.String()))
	return nil
}

func (dm *DifficultyModeManager) getContentModesFromDB(ctx context.Context, contentID uuid.UUID) ([]*ContentDifficultyMode, error) {
	// В реальной реализации здесь будет JOIN запрос
	// Пока возвращаем тестовые данные
	return []*ContentDifficultyMode{
		{
			ContentID: contentID,
			ModeID:    uuid.MustParse("550e8400-e29b-41d4-a716-446655440000"),
			Requirements: DifficultyRequirements{
				MinLevel:        25,
				MinSkillRating:  1500,
				ReputationLevel: "skilled",
			},
			IsEnabled: true,
		},
		{
			ContentID: contentID,
			ModeID:    uuid.MustParse("550e8400-e29b-41d4-a716-446655440001"),
			Requirements: DifficultyRequirements{
				MinLevel:        35,
				MinSkillRating:  2000,
				ReputationLevel: "veteran",
			},
			IsEnabled: true,
		},
	}, nil
}

// validateDifficultyMode валидирует режим сложности
func (dm *DifficultyModeManager) validateDifficultyMode(mode *DifficultyMode) error {
	if mode.Name == "" {
		return fmt.Errorf("mode name cannot be empty")
	}
	if mode.HpModifier < 1.0 {
		return fmt.Errorf("HP modifier must be >= 1.0")
	}
	if mode.DamageModifier < 1.0 {
		return fmt.Errorf("damage modifier must be >= 1.0")
	}
	if mode.TimeLimitMultiplier <= 0.0 {
		return fmt.Errorf("time limit multiplier must be > 0.0")
	}
	if mode.RespawnLimit < 0 {
		return fmt.Errorf("respawn limit cannot be negative")
	}
	if mode.CheckpointLimit < 0 {
		return fmt.Errorf("checkpoint limit cannot be negative")
	}
	if mode.RewardModifier < 1.0 {
		return fmt.Errorf("reward modifier must be >= 1.0")
	}
	return nil
}
