package service

import (
	"context"
	"time"

	"github.com/go-faster/errors"
	"github.com/google/uuid"
	"go.opentelemetry.io/otel/attribute"
	"go.uber.org/zap"
)

// AchievementTracker отслеживает достижения игроков в мастер-режимах
type AchievementTracker struct {
	service *Service
	logger  *zap.Logger
}

// Achievement представляет достижение в мастер-режиме
type Achievement struct {
	ID          string    `json:"id"`
	Name        string    `json:"name"`
	Description string    `json:"description"`
	Category    string    `json:"category"` // combat, survival, speed, perfection
	Rarity      string    `json:"rarity"`   // common, rare, epic, legendary
	Points      int       `json:"points"`
	Icon        string    `json:"icon"`
	UnlockedAt  time.Time `json:"unlocked_at"`
}

// NewAchievementTracker создает новый трекер достижений
func NewAchievementTracker(svc *Service, logger *zap.Logger) *AchievementTracker {
	return &AchievementTracker{
		service: svc,
		logger:  logger,
	}
}

// TrackAchievement разблокирует достижение для игрока
func (at *AchievementTracker) TrackAchievement(ctx context.Context, playerID uuid.UUID, achievementID string, sessionID uuid.UUID) error {
	ctx, span := at.service.GetTracer().Start(ctx, "AchievementTracker.TrackAchievement")
	defer span.End()

	span.SetAttributes(
		attribute.String("player.id", playerID.String()),
		attribute.String("achievement.id", achievementID),
		attribute.String("session.id", sessionID.String()),
	)

	// Проверяем, не разблокировано ли уже достижение
	unlocked, err := at.isAchievementUnlocked(ctx, playerID, achievementID)
	if err != nil {
		return errors.Wrap(err, "failed to check achievement status")
	}

	if unlocked {
		at.logger.Debug("Achievement already unlocked",
			zap.String("player_id", playerID.String()),
			zap.String("achievement_id", achievementID))
		return nil
	}

	// Получаем информацию о достижении
	achievement, err := at.getAchievementInfo(ctx, achievementID)
	if err != nil {
		return errors.Wrap(err, "failed to get achievement info")
	}

	// Записываем достижение
	if err := at.unlockAchievement(ctx, playerID, achievement, sessionID); err != nil {
		return errors.Wrap(err, "failed to unlock achievement")
	}

	// Записываем в аналитику
	if err := at.service.GetAnalyticsCollector().RecordAchievement(ctx, sessionID, achievementID); err != nil {
		at.logger.Warn("Failed to record achievement in analytics", zap.Error(err))
	}

	at.logger.Info("Achievement unlocked",
		zap.String("player_id", playerID.String()),
		zap.String("achievement_id", achievementID),
		zap.String("achievement_name", achievement.Name),
		zap.Int("points", achievement.Points))

	return nil
}

// CheckCombatAchievements проверяет боевые достижения
func (at *AchievementTracker) CheckCombatAchievements(ctx context.Context, sessionID uuid.UUID, stats map[string]interface{}) error {
	ctx, span := at.service.GetTracer().Start(ctx, "AchievementTracker.CheckCombatAchievements")
	defer span.End()

	span.SetAttributes(attribute.String("session.id", sessionID.String()))

	// Получаем статистику сессии
	sessionStats, err := at.service.GetAnalyticsCollector().GetSessionStats(ctx, sessionID)
	if err != nil {
		return errors.Wrap(err, "failed to get session stats")
	}

	playerID := sessionStats.PlayerID

	// Проверяем различные боевые достижения
	achievements := []string{}

	// No Deaths Run
	if sessionStats.Deaths == 0 && sessionStats.Status == "completed" {
		achievements = append(achievements, "no_deaths_master")
	}

	// Perfect Accuracy (если есть статистика точности)
	if accuracy, ok := stats["accuracy"].(float64); ok && accuracy >= 95.0 {
		achievements = append(achievements, "perfect_accuracy")
	}

	// Speed Demon (быстрое прохождение)
	if sessionStats.Duration < 600*time.Second && sessionStats.Status == "completed" { // менее 10 минут
		achievements = append(achievements, "speed_demon")
	}

	// Разблокируем найденные достижения
	for _, achievementID := range achievements {
		if err := at.TrackAchievement(ctx, playerID, achievementID, sessionID); err != nil {
			at.logger.Warn("Failed to track achievement",
				zap.String("achievement_id", achievementID),
				zap.Error(err))
		}
	}

	at.logger.Debug("Combat achievements checked",
		zap.String("session_id", sessionID.String()),
		zap.Int("achievements_found", len(achievements)))

	return nil
}

// CheckSurvivalAchievements проверяет достижения выживания
func (at *AchievementTracker) CheckSurvivalAchievements(ctx context.Context, sessionID uuid.UUID) error {
	ctx, span := at.service.GetTracer().Start(ctx, "AchievementTracker.CheckSurvivalAchievements")
	defer span.End()

	span.SetAttributes(attribute.String("session.id", sessionID.String()))

	sessionStats, err := at.service.GetAnalyticsCollector().GetSessionStats(ctx, sessionID)
	if err != nil {
		return errors.Wrap(err, "failed to get session stats")
	}

	playerID := sessionStats.PlayerID
	achievements := []string{}

	// Last Stand (выжил с 1 HP)
	if sessionStats.Status == "completed" {
		// В реальной реализации здесь будет проверка минимального HP
		achievements = append(achievements, "last_stand")
	}

	// Resourceful (минимальное использование чекпоинтов)
	if sessionStats.CheckpointsUsed <= 1 && sessionStats.Status == "completed" {
		achievements = append(achievements, "resourceful")
	}

	// Immortal (максимум респавнов, но прошел)
	if sessionStats.RespawnsUsed >= 2 && sessionStats.Status == "completed" {
		achievements = append(achievements, "immortal")
	}

	// Разблокируем достижения
	for _, achievementID := range achievements {
		if err := at.TrackAchievement(ctx, playerID, achievementID, sessionID); err != nil {
			at.logger.Warn("Failed to track achievement",
				zap.String("achievement_id", achievementID),
				zap.Error(err))
		}
	}

	return nil
}

// GetPlayerAchievements получает достижения игрока
func (at *AchievementTracker) GetPlayerAchievements(ctx context.Context, playerID uuid.UUID) ([]Achievement, error) {
	ctx, span := at.service.GetTracer().Start(ctx, "AchievementTracker.GetPlayerAchievements")
	defer span.End()

	span.SetAttributes(attribute.String("player.id", playerID.String()))

	// В реальной реализации здесь будет запрос к БД
	// Для демонстрации возвращаем mock данные

	achievements := []Achievement{
		{
			ID:          "no_deaths_master",
			Name:        "Безупречный Мастер",
			Description: "Пройти мастер-режим без единой смерти",
			Category:    "survival",
			Rarity:      "epic",
			Points:      500,
			Icon:        "no_deaths_icon",
			UnlockedAt:  time.Now().Add(-24 * time.Hour),
		},
		{
			ID:          "speed_demon",
			Name:        "Демон Скорости",
			Description: "Пройти режим менее чем за 10 минут",
			Category:    "speed",
			Rarity:      "rare",
			Points:      300,
			Icon:        "speed_icon",
			UnlockedAt:  time.Now().Add(-48 * time.Hour),
		},
	}

	at.logger.Debug("Retrieved player achievements",
		zap.String("player_id", playerID.String()),
		zap.Int("count", len(achievements)))

	return achievements, nil
}

// GetAchievementProgress получает прогресс к достижению
func (at *AchievementTracker) GetAchievementProgress(ctx context.Context, playerID uuid.UUID, achievementID string) (map[string]interface{}, error) {
	ctx, span := at.service.GetTracer().Start(ctx, "AchievementTracker.GetAchievementProgress")
	defer span.End()

	span.SetAttributes(
		attribute.String("player.id", playerID.String()),
		attribute.String("achievement.id", achievementID),
	)

	// В реальной реализации здесь будет расчет прогресса
	// Для демонстрации возвращаем mock данные

	progress := map[string]interface{}{
		"achievement_id": achievementID,
		"current_value":  75,
		"target_value":   100,
		"progress":       0.75,
		"is_completed":   false,
		"description":    "Прогресс к достижению",
	}

	return progress, nil
}

// isAchievementUnlocked проверяет, разблокировано ли достижение
func (at *AchievementTracker) isAchievementUnlocked(ctx context.Context, playerID uuid.UUID, achievementID string) (bool, error) {
	achievements, err := at.GetPlayerAchievements(ctx, playerID)
	if err != nil {
		return false, err
	}

	for _, achievement := range achievements {
		if achievement.ID == achievementID {
			return true, nil
		}
	}

	return false, nil
}

// getAchievementInfo получает информацию о достижении
func (at *AchievementTracker) getAchievementInfo(ctx context.Context, achievementID string) (*Achievement, error) {
	// В реальной реализации здесь будет запрос к БД или кэш
	// Для демонстрации возвращаем mock данные

	achievementMap := map[string]*Achievement{
		"no_deaths_master": {
			ID:          "no_deaths_master",
			Name:        "Безупречный Мастер",
			Description: "Пройти мастер-режим без единой смерти",
			Category:    "survival",
			Rarity:      "epic",
			Points:      500,
			Icon:        "no_deaths_icon",
		},
		"perfect_accuracy": {
			ID:          "perfect_accuracy",
			Name:        "Идеальная Точность",
			Description: "Достичь 95% точности стрельбы",
			Category:    "combat",
			Rarity:      "legendary",
			Points:      1000,
			Icon:        "accuracy_icon",
		},
		"speed_demon": {
			ID:          "speed_demon",
			Name:        "Демон Скорости",
			Description: "Пройти режим менее чем за 10 минут",
			Category:    "speed",
			Rarity:      "rare",
			Points:      300,
			Icon:        "speed_icon",
		},
		"last_stand": {
			ID:          "last_stand",
			Name:        "Последний Рубеж",
			Description: "Выжить с минимальным количеством здоровья",
			Category:    "survival",
			Rarity:      "rare",
			Points:      250,
			Icon:        "survival_icon",
		},
		"resourceful": {
			ID:          "resourceful",
			Name:        "Находчивый",
			Description: "Пройти с минимальным использованием чекпоинтов",
			Category:    "survival",
			Rarity:      "uncommon",
			Points:      150,
			Icon:        "resourceful_icon",
		},
		"immortal": {
			ID:          "immortal",
			Name:        "Бессмертный",
			Description: "Пройти несмотря на максимум смертей",
			Category:    "combat",
			Rarity:      "epic",
			Points:      400,
			Icon:        "immortal_icon",
		},
	}

	achievement, exists := achievementMap[achievementID]
	if !exists {
		return nil, errors.Errorf("achievement not found: %s", achievementID)
	}

	return achievement, nil
}

// unlockAchievement записывает достижение в БД
func (at *AchievementTracker) unlockAchievement(ctx context.Context, playerID uuid.UUID, achievement *Achievement, sessionID uuid.UUID) error {
	achievement.UnlockedAt = time.Now()

	// В реальной реализации здесь будет INSERT в БД
	at.logger.Debug("Achievement unlocked in database",
		zap.String("player_id", playerID.String()),
		zap.String("achievement_id", achievement.ID))

	return nil
}
