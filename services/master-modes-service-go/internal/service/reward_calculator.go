package service

import (
	"context"
	"math"
	"time"

	"github.com/go-faster/errors"
	"github.com/google/uuid"
	"go.opentelemetry.io/otel/attribute"
	"go.uber.org/zap"
)

// RewardCalculator рассчитывает награды за прохождение мастер-режимов
type RewardCalculator struct {
	service *Service
	logger  *zap.Logger
}

// RewardBreakdown представляет разбивку награды
type RewardBreakdown struct {
	BaseReward        int64                  `json:"base_reward"`
	TimeBonus         int64                  `json:"time_bonus"`
	DifficultyBonus   int64                  `json:"difficulty_bonus"`
	PerformanceBonus  int64                  `json:"performance_bonus"`
	AchievementBonus  int64                  `json:"achievement_bonus"`
	TotalReward       int64                  `json:"total_reward"`
	RewardMultipliers map[string]float64     `json:"reward_multipliers"`
	Bonuses           map[string]int64        `json:"bonuses"`
	CurrencyBreakdown map[string]int64        `json:"currency_breakdown"`
}

// NewRewardCalculator создает новый калькулятор наград
func NewRewardCalculator(svc *Service, logger *zap.Logger) *RewardCalculator {
	return &RewardCalculator{
		service: svc,
		logger:  logger,
	}
}

// CalculateRewards рассчитывает награды за завершение сессии
func (rc *RewardCalculator) CalculateRewards(ctx context.Context, sessionID uuid.UUID) (*RewardBreakdown, error) {
	ctx, span := rc.service.GetTracer().Start(ctx, "RewardCalculator.CalculateRewards")
	defer span.End()

	span.SetAttributes(attribute.String("session.id", sessionID.String()))

	// Получаем статистику сессии
	sessionStats, err := rc.service.GetAnalyticsCollector().GetSessionStats(ctx, sessionID)
	if err != nil {
		return nil, errors.Wrap(err, "failed to get session stats")
	}

	// Получаем информацию о режиме сложности
	mode, err := rc.service.GetDifficultyManager().GetDifficultyMode(ctx, sessionStats.ModeID)
	if err != nil {
		return nil, errors.Wrap(err, "failed to get difficulty mode")
	}

	breakdown := &RewardBreakdown{
		RewardMultipliers: make(map[string]float64),
		Bonuses:           make(map[string]int64),
		CurrencyBreakdown: make(map[string]int64),
	}

	// Базовая награда за режим
	breakdown.BaseReward = rc.calculateBaseReward(mode)

	// Бонус за время прохождения
	breakdown.TimeBonus = rc.calculateTimeBonus(sessionStats, mode)

	// Бонус за сложность
	breakdown.DifficultyBonus = rc.calculateDifficultyBonus(mode)

	// Бонус за производительность
	breakdown.PerformanceBonus = rc.calculatePerformanceBonus(sessionStats)

	// Бонус за достижения
	breakdown.AchievementBonus = rc.calculateAchievementBonus(ctx, sessionID, sessionStats.PlayerID)

	// Общая награда
	breakdown.TotalReward = breakdown.BaseReward +
		breakdown.TimeBonus +
		breakdown.DifficultyBonus +
		breakdown.PerformanceBonus +
		breakdown.AchievementBonus

	// Множители наград
	breakdown.RewardMultipliers = rc.calculateRewardMultipliers(sessionStats, mode)

	// Применяем множители
	totalMultiplier := 1.0
	for _, multiplier := range breakdown.RewardMultipliers {
		totalMultiplier *= multiplier
	}
	breakdown.TotalReward = int64(float64(breakdown.TotalReward) * totalMultiplier)

	// Разбивка по валютам
	breakdown.CurrencyBreakdown = rc.calculateCurrencyBreakdown(breakdown.TotalReward)

	rc.logger.Info("Rewards calculated",
		zap.String("session_id", sessionID.String()),
		zap.Int64("total_reward", breakdown.TotalReward),
		zap.Float64("multiplier", totalMultiplier))

	return breakdown, nil
}

// calculateBaseReward рассчитывает базовую награду за режим
func (rc *RewardCalculator) calculateBaseReward(mode *DifficultyMode) int64 {
	// Базовые награды по уровням сложности
	baseRewards := map[DifficultyLevel]int64{
		DifficultyEasy:      1000,
		DifficultyNormal:    2500,
		DifficultyHard:      5000,
		DifficultyMaster:    10000,
		DifficultyChallenge: 25000,
	}

	reward, exists := baseRewards[mode.Level]
	if !exists {
		reward = 5000 // default
	}

	return reward
}

// calculateTimeBonus рассчитывает бонус за время прохождения
func (rc *RewardCalculator) calculateTimeBonus(stats *DifficultySessionStats, mode *DifficultyMode) int64 {
	if stats.Status != "completed" {
		return 0
	}

	// Целевое время для режима (в секундах)
	targetTimes := map[DifficultyLevel]float64{
		DifficultyEasy:      1800,  // 30 мин
		DifficultyNormal:    2400,  // 40 мин
		DifficultyHard:      3600,  // 60 мин
		DifficultyMaster:    1800,  // 30 мин
		DifficultyChallenge: 1200,  // 20 мин
	}

	targetTime, exists := targetTimes[mode.Level]
	if !exists {
		targetTime = 2400 // default 40 мин
	}

	actualTime := stats.Duration.Seconds()

	// Максимальный бонус за время
	maxTimeBonus := int64(5000)

	if actualTime <= targetTime {
		// Полный бонус за прохождение в целевое время
		return maxTimeBonus
	}

	// Линейное снижение бонуса
	timeRatio := targetTime / actualTime
	if timeRatio < 0.5 {
		return 0 // Нет бонуса если время в 2 раза больше целевого
	}

	return int64(float64(maxTimeBonus) * timeRatio)
}

// calculateDifficultyBonus рассчитывает бонус за сложность
func (rc *RewardCalculator) calculateDifficultyBonus(mode *DifficultyMode) int64 {
	// Дополнительный бонус за модификаторы сложности
	bonus := int64(0)

	// Бонус за HP модификатор (чем выше, тем больше бонус)
	if mode.HpModifier > 1.0 {
		hpBonus := int64((mode.HpModifier - 1.0) * 2000)
		bonus += hpBonus
	}

	// Бонус за damage модификатор
	if mode.DamageModifier > 1.0 {
		damageBonus := int64((mode.DamageModifier - 1.0) * 1500)
		bonus += damageBonus
	}

	// Бонус за специальные механики
	specialBonus := int64(len(mode.SpecialMechanics) * 1000)
	bonus += specialBonus

	return bonus
}

// calculatePerformanceBonus рассчитывает бонус за производительность
func (rc *RewardCalculator) calculatePerformanceBonus(stats *DifficultySessionStats) int64 {
	bonus := int64(0)

	// Бонус за отсутствие смертей
	if stats.Deaths == 0 {
		bonus += 3000
	} else if stats.Deaths <= 2 {
		bonus += 1500
	}

	// Бонус за минимальное использование чекпоинтов
	if stats.CheckpointsUsed <= 1 {
		bonus += 2000
	} else if stats.CheckpointsUsed <= 3 {
		bonus += 1000
	}

	// Бонус за минимальное использование респавнов
	if stats.RespawnsUsed <= 1 {
		bonus += 2000
	} else if stats.RespawnsUsed <= 3 {
		bonus += 1000
	}

	// Штраф за низкий счет
	if stats.Score < 50000 {
		bonus -= 1000
	}

	return bonus
}

// calculateAchievementBonus рассчитывает бонус за достижения
func (rc *RewardCalculator) calculateAchievementBonus(ctx context.Context, sessionID, playerID uuid.UUID) int64 {
	// Получаем достижения игрока
	achievements, err := rc.service.GetAchievementTracker().GetPlayerAchievements(ctx, playerID)
	if err != nil {
		rc.logger.Warn("Failed to get achievements for reward calculation", zap.Error(err))
		return 0
	}

	bonus := int64(0)
	for _, achievement := range achievements {
		// Проверяем, было ли достижение разблокировано в этой сессии
		// В реальной реализации здесь будет проверка времени разблокировки
		if achievement.UnlockedAt.After(time.Now().Add(-24 * time.Hour)) {
			bonus += int64(achievement.Points * 10) // 10x множитель для очков достижений
		}
	}

	return bonus
}

// calculateRewardMultipliers рассчитывает множители наград
func (rc *RewardCalculator) calculateRewardMultipliers(stats *DifficultySessionStats, mode *DifficultyMode) map[string]float64 {
	multipliers := make(map[string]float64)

	// Множитель за уровень сложности
	switch mode.Level {
	case DifficultyEasy:
		multipliers["difficulty"] = 0.8
	case DifficultyNormal:
		multipliers["difficulty"] = 1.0
	case DifficultyHard:
		multipliers["difficulty"] = 1.3
	case DifficultyMaster:
		multipliers["difficulty"] = 1.8
	case DifficultyChallenge:
		multipliers["difficulty"] = 2.5
	}

	// Множитель за производительность
	performanceMultiplier := 1.0
	if stats.Deaths == 0 {
		performanceMultiplier += 0.5 // +50% за отсутствие смертей
	}
	if stats.CheckpointsUsed <= 1 {
		performanceMultiplier += 0.3 // +30% за минимальное использование чекпоинтов
	}
	multipliers["performance"] = performanceMultiplier

	// Множитель за время
	if stats.Duration < 1800*time.Second && stats.Status == "completed" {
		multipliers["speed"] = 1.2 // +20% за быстрое прохождение
	}

	// Множитель за сезонные события (заглушка)
	multipliers["seasonal"] = 1.0

	return multipliers
}

// calculateCurrencyBreakdown разбивает награду по валютам
func (rc *RewardCalculator) calculateCurrencyBreakdown(totalReward int64) map[string]int64 {
	breakdown := make(map[string]int64)

	// Основная валюта (80%)
	breakdown["credits"] = int64(float64(totalReward) * 0.8)

	// Премиум валюта (15%)
	breakdown["premium_credits"] = int64(float64(totalReward) * 0.15)

	// Редкая валюта (5%)
	breakdown["rare_currency"] = int64(float64(totalReward) * 0.05)

	return breakdown
}

// ApplyRewards применяет рассчитанные награды игроку
func (rc *RewardCalculator) ApplyRewards(ctx context.Context, playerID uuid.UUID, breakdown *RewardBreakdown) error {
	ctx, span := rc.service.GetTracer().Start(ctx, "RewardCalculator.ApplyRewards")
	defer span.End()

	span.SetAttributes(
		attribute.String("player.id", playerID.String()),
		attribute.Int64("total_reward", breakdown.TotalReward),
	)

	// В реальной реализации здесь будет:
	// 1. Обновление баланса игрока в БД
	// 2. Отправка уведомления игроку
	// 3. Логирование транзакции

	rc.logger.Info("Rewards applied to player",
		zap.String("player_id", playerID.String()),
		zap.Any("currency_breakdown", breakdown.CurrencyBreakdown))

	return nil
}

// ValidateRewardCalculation проверяет корректность расчета наград
func (rc *RewardCalculator) ValidateRewardCalculation(ctx context.Context, sessionID uuid.UUID, expectedTotal int64) error {
	ctx, span := rc.service.GetTracer().Start(ctx, "RewardCalculator.ValidateRewardCalculation")
	defer span.End()

	breakdown, err := rc.CalculateRewards(ctx, sessionID)
	if err != nil {
		return errors.Wrap(err, "failed to calculate rewards for validation")
	}

	if breakdown.TotalReward != expectedTotal {
		return errors.Errorf("reward calculation mismatch: calculated %d, expected %d",
			breakdown.TotalReward, expectedTotal)
	}

	rc.logger.Debug("Reward calculation validated",
		zap.String("session_id", sessionID.String()),
		zap.Int64("total_reward", breakdown.TotalReward))

	return nil
}
