// Issue: #140875766
package server

import (
	"context"
	"database/sql"
	"fmt"
	"math"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"
)

// SandevistanService содержит бизнес-логику Sandevistan системы
type SandevistanService struct {
	repo     *SandevistanRepository
	logger   *zap.Logger
	calculator *SandevistanCalculator
}

// SandevistanState представляет текущее состояние Sandevistan для игрока
type SandevistanState struct {
	UserID             string    `json:"user_id"`
	IsActive           bool      `json:"is_active"`
	ActivationTime     *time.Time `json:"activation_time"`
	RemainingTime      float64   `json:"remaining_time"`
	TimeDilation       float64   `json:"time_dilation"`
	CooldownRemaining  float64   `json:"cooldown_remaining"`
	CyberpsychosisLevel float64  `json:"cyberpsychosis_level"`
	HeatLevel          float64   `json:"heat_level"`
	LastActivation     *time.Time `json:"last_activation"`
}

// SandevistanStats представляет статистику Sandevistan для игрока
type SandevistanStats struct {
	UserID                  string  `json:"user_id"`
	Level                   int     `json:"level"`
	MaxDuration             float64 `json:"max_duration"`
	CooldownTime            float64 `json:"cooldown_time"`
	TimeDilationBase        float64 `json:"time_dilation_base"`
	CyberpsychosisResistance float64 `json:"cyberpsychosis_resistance"`
	HeatDissipationRate     float64 `json:"heat_dissipation_rate"`
	TotalActivations        int     `json:"total_activations"`
	TotalDuration           float64 `json:"total_duration"`
	BestStreak              int     `json:"best_streak"`
	AverageDuration         float64 `json:"average_duration"`
}

// SandevistanCalculator содержит логику расчетов для Sandevistan
type SandevistanCalculator struct {
	baseDuration      float64 // базовая длительность (секунды)
	baseCooldown      float64 // базовое время cooldown (секунды)
	baseTimeDilation  float64 // базовый коэффициент замедления
	maxLevel          int     // максимальный уровень прокачки
	cyberpsychosisRate float64 // скорость накопления киберпсихоза
	heatBuildupRate   float64 // скорость нагрева
	heatDissipationRate float64 // скорость охлаждения
}

// NewSandevistanCalculator создает новый калькулятор с базовыми параметрами
func NewSandevistanCalculator() *SandevistanCalculator {
	return &SandevistanCalculator{
		baseDuration:       8.0,   // 8 секунд базовой длительности
		baseCooldown:       30.0,  // 30 секунд базового cooldown
		baseTimeDilation:   0.1,   // замедление в 10 раз (90% замедления)
		maxLevel:           10,    // максимальный уровень прокачки
		cyberpsychosisRate: 0.05,  // +5% киберпсихоза за секунду
		heatBuildupRate:    0.1,   // +10% нагрева за секунду
		heatDissipationRate: 0.02, // -2% охлаждения за секунду неактивности
	}
}

// NewSandevistanService создает новый сервис Sandevistan
func NewSandevistanService(db *sql.DB, logger *zap.Logger) *SandevistanService {
	return &SandevistanService{
		repo:       NewSandevistanRepository(db, logger),
		logger:     logger,
		calculator: NewSandevistanCalculator(),
	}
}

// ActivateSandevistan активирует Sandevistan для игрока
func (s *SandevistanService) ActivateSandevistan(ctx context.Context, userID string, durationOverride *float64) (*SandevistanState, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	s.logger.Info("Activating Sandevistan", zap.String("user_id", userID))

	// Получаем текущее состояние
	state, err := s.repo.GetSandevistanState(ctx, userID)
	if err != nil {
		return nil, fmt.Errorf("failed to get state: %w", err)
	}

	// Проверяем cooldown
	if state.CooldownRemaining > 0 {
		return nil, &CooldownError{
			CooldownRemaining: state.CooldownRemaining,
			Message:          "Sandevistan is on cooldown",
		}
	}

	// Получаем статистику для расчетов
	stats, err := s.repo.GetSandevistanStats(ctx, userID)
	if err != nil {
		return nil, fmt.Errorf("failed to get stats: %w", err)
	}

	// Рассчитываем параметры активации
	duration := s.calculator.calculateDuration(stats.Level, durationOverride)
	timeDilation := s.calculator.calculateTimeDilation(stats.Level)
	activationTime := time.Now()

	// Создаем новое состояние
	newState := &SandevistanState{
		UserID:             userID,
		IsActive:           true,
		ActivationTime:     &activationTime,
		RemainingTime:      duration,
		TimeDilation:       timeDilation,
		CooldownRemaining:  0,
		CyberpsychosisLevel: s.calculator.calculateCyberpsychosisIncrease(state.CyberpsychosisLevel, stats.CyberpsychosisResistance),
		HeatLevel:          s.calculator.calculateHeatIncrease(state.HeatLevel),
		LastActivation:     &activationTime,
	}

	// Сохраняем состояние
	if err := s.repo.UpdateSandevistanState(ctx, newState); err != nil {
		return nil, fmt.Errorf("failed to update state: %w", err)
	}

	// Обновляем статистику
	if err := s.repo.IncrementActivationCount(ctx, userID, duration); err != nil {
		s.logger.Warn("Failed to update activation stats", zap.Error(err))
	}

	s.logger.Info("Sandevistan activated successfully",
		zap.String("user_id", userID),
		zap.Float64("duration", duration),
		zap.Float64("time_dilation", timeDilation))

	return newState, nil
}

// DeactivateSandevistan деактивирует Sandevistan для игрока
func (s *SandevistanService) DeactivateSandevistan(ctx context.Context, userID string) (*SandevistanState, error) {
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	s.logger.Info("Deactivating Sandevistan", zap.String("user_id", userID))

	// Получаем текущее состояние
	state, err := s.repo.GetSandevistanState(ctx, userID)
	if err != nil {
		return nil, fmt.Errorf("failed to get state: %w", err)
	}

	if !state.IsActive {
		return nil, fmt.Errorf("Sandevistan is not active")
	}

	// Получаем статистику для расчетов cooldown
	stats, err := s.repo.GetSandevistanStats(ctx, userID)
	if err != nil {
		return nil, fmt.Errorf("failed to get stats: %w", err)
	}

	// Рассчитываем cooldown
	cooldown := s.calculator.calculateCooldown(stats.Level)

	// Создаем новое состояние
	newState := &SandevistanState{
		UserID:             userID,
		IsActive:           false,
		ActivationTime:     nil,
		RemainingTime:      0,
		TimeDilation:       1.0, // нормальная скорость
		CooldownRemaining:  cooldown,
		CyberpsychosisLevel: state.CyberpsychosisLevel, // сохраняем уровень
		HeatLevel:          state.HeatLevel,           // сохраняем уровень
		LastActivation:     state.LastActivation,
	}

	// Сохраняем состояние
	if err := s.repo.UpdateSandevistanState(ctx, newState); err != nil {
		return nil, fmt.Errorf("failed to update state: %w", err)
	}

	s.logger.Info("Sandevistan deactivated successfully",
		zap.String("user_id", userID),
		zap.Float64("cooldown", cooldown))

	return newState, nil
}

// GetSandevistanState получает текущее состояние Sandevistan
func (s *SandevistanService) GetSandevistanState(ctx context.Context, userID string) (*SandevistanState, error) {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	state, err := s.repo.GetSandevistanState(ctx, userID)
	if err != nil {
		return nil, fmt.Errorf("failed to get state: %w", err)
	}

	// Обновляем оставшееся время если активно
	if state.IsActive && state.ActivationTime != nil {
		elapsed := time.Since(*state.ActivationTime).Seconds()
		if elapsed >= state.RemainingTime {
			// Время вышло, деактивируем
			state.IsActive = false
			state.RemainingTime = 0
			state.TimeDilation = 1.0
			// Автоматически обновляем состояние в БД
			s.repo.UpdateSandevistanState(ctx, state)
		} else {
			state.RemainingTime -= elapsed
		}
	}

	// Обновляем cooldown
	if state.CooldownRemaining > 0 {
		if state.LastActivation != nil {
			elapsed := time.Since(*state.LastActivation).Seconds()
			stats, _ := s.repo.GetSandevistanStats(ctx, userID)
			cooldown := s.calculator.calculateCooldown(stats.Level)
			remaining := cooldown - elapsed
			if remaining <= 0 {
				state.CooldownRemaining = 0
			} else {
				state.CooldownRemaining = remaining
			}
		}
	}

	// Обновляем уровни heat и cyberpsychosis
	if !state.IsActive {
		state.HeatLevel = s.calculator.calculateHeatDissipation(state.HeatLevel)
	}

	return state, nil
}

// GetSandevistanStats получает статистику Sandevistan
func (s *SandevistanService) GetSandevistanStats(ctx context.Context, userID string) (*SandevistanStats, error) {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	stats, err := s.repo.GetSandevistanStats(ctx, userID)
	if err != nil {
		return nil, fmt.Errorf("failed to get stats: %w", err)
	}

	return stats, nil
}

// UpgradeSandevistan улучшает Sandevistan
func (s *SandevistanService) UpgradeSandevistan(ctx context.Context, userID, upgradeType string, level int) (*SandevistanStats, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	s.logger.Info("Upgrading Sandevistan",
		zap.String("user_id", userID),
		zap.String("upgrade_type", upgradeType),
		zap.Int("level", level))

	if level < 1 || level > s.calculator.maxLevel {
		return nil, fmt.Errorf("invalid level: must be between 1 and %d", s.calculator.maxLevel)
	}

	// Получаем текущую статистику
	stats, err := s.repo.GetSandevistanStats(ctx, userID)
	if err != nil {
		return nil, fmt.Errorf("failed to get stats: %w", err)
	}

	// Проверяем, что уровень повышается
	if level <= stats.Level {
		return nil, fmt.Errorf("new level must be higher than current level %d", stats.Level)
	}

	// Рассчитываем стоимость улучшения (примерная формула)
	cost := s.calculator.calculateUpgradeCost(upgradeType, level)
	// В реальности здесь нужно проверить баланс игрока

	// Обновляем статистику
	switch upgradeType {
	case "duration":
		stats.MaxDuration = s.calculator.calculateDuration(level, nil)
	case "cooldown":
		stats.CooldownTime = s.calculator.calculateCooldown(level)
	case "cyberpsychosis_resistance":
		stats.CyberpsychosisResistance = s.calculator.calculateResistance(level)
	case "heat_dissipation":
		stats.HeatDissipationRate = s.calculator.calculateDissipationRate(level)
	default:
		return nil, fmt.Errorf("invalid upgrade type: %s", upgradeType)
	}

	stats.Level = level

	// Сохраняем обновленную статистику
	if err := s.repo.UpdateSandevistanStats(ctx, stats); err != nil {
		return nil, fmt.Errorf("failed to update stats: %w", err)
	}

	s.logger.Info("Sandevistan upgraded successfully",
		zap.String("user_id", userID),
		zap.String("upgrade_type", upgradeType),
		zap.Int("new_level", level))

	return stats, nil
}

// Методы калькулятора

func (c *SandevistanCalculator) calculateDuration(level int, override *float64) float64 {
	if override != nil {
		return math.Min(*override, c.baseDuration*float64(level+1))
	}
	return c.baseDuration * (1.0 + float64(level)*0.2) // +20% за уровень
}

func (c *SandevistanCalculator) calculateCooldown(level int) float64 {
	return c.baseCooldown * (1.0 - float64(level)*0.1) // -10% за уровень
}

func (c *SandevistanCalculator) calculateTimeDilation(level int) float64 {
	return c.baseTimeDilation * (1.0 + float64(level)*0.05) // +5% замедления за уровень
}

func (c *SandevistanCalculator) calculateCyberpsychosisIncrease(currentLevel, resistance float64) float64 {
	increase := c.cyberpsychosisRate * (1.0 - resistance)
	return math.Min(1.0, currentLevel+increase) // максимум 100%
}

func (c *SandevistanCalculator) calculateHeatIncrease(currentLevel float64) float64 {
	return math.Min(1.0, currentLevel+c.heatBuildupRate) // максимум 100%
}

func (c *SandevistanCalculator) calculateHeatDissipation(currentLevel float64) float64 {
	return math.Max(0.0, currentLevel-c.heatDissipationRate)
}

func (c *SandevistanCalculator) calculateResistance(level int) float64 {
	return float64(level) / float64(c.maxLevel) // 0-1 сопротивление
}

func (c *SandevistanCalculator) calculateDissipationRate(level int) float64 {
	return c.heatDissipationRate * (1.0 + float64(level)*0.5) // +50% за уровень
}

func (c *SandevistanCalculator) calculateUpgradeCost(upgradeType string, level int) int {
	baseCost := 1000
	return baseCost * level * level // квадратичная стоимость
}

// CooldownError представляет ошибку cooldown
type CooldownError struct {
	CooldownRemaining float64
	Message          string
}

func (e *CooldownError) Error() string {
	return e.Message
}