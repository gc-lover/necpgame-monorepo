// Package algorithms содержит алгоритмы восстановления репутации
// Issue: #2174 - Reputation Decay & Recovery mechanics
package algorithms

import (
	"math"
	"time"

	"github.com/go-faster/errors"

	"github.com/gc-lover/necp-game/services/reputation-decay-recovery-service-go/internal/models"
)

// RecoveryCalculator рассчитывает восстановление репутации
type RecoveryCalculator struct {
	config *models.RecoveryConfig
}

// NewRecoveryCalculator создает новый калькулятор восстановления
func NewRecoveryCalculator(config *models.RecoveryConfig) *RecoveryCalculator {
	return &RecoveryCalculator{
		config: config,
	}
}

// CalculateRecoveryProgress рассчитывает прогресс восстановления
func (rc *RecoveryCalculator) CalculateRecoveryProgress(recovery *models.ReputationRecovery, now time.Time) (float64, error) {
	if recovery.Status != models.StatusActive {
		return recovery.Progress, nil
	}

	timeElapsed := now.Sub(recovery.StartTime)
	totalDuration := recovery.EstimatedEnd.Sub(recovery.StartTime)

	if totalDuration <= 0 {
		return 1.0, nil // Мгновенное завершение
	}

	progress := timeElapsed.Seconds() / totalDuration.Seconds()

	// Ограничиваем прогресс
	if progress > 1.0 {
		progress = 1.0
	}
	if progress < 0.0 {
		progress = 0.0
	}

	return progress, nil
}

// CalculateRecoveryValue рассчитывает текущую репутацию на основе прогресса
func (rc *RecoveryCalculator) CalculateRecoveryValue(recovery *models.ReputationRecovery, progress float64) float64 {
	switch recovery.Method {
	case models.MethodTimeBased:
		return rc.calculateTimeBasedRecovery(recovery, progress)
	case models.MethodPaymentBased:
		return rc.calculatePaymentBasedRecovery(recovery, progress)
	case models.MethodQuestBased:
		return rc.calculateQuestBasedRecovery(recovery, progress)
	case models.MethodActionBased:
		return rc.calculateActionBasedRecovery(recovery, progress)
	case models.MethodHybrid:
		return rc.calculateHybridRecovery(recovery, progress)
	default:
		return recovery.StartValue
	}
}

// calculateTimeBasedRecovery - линейное восстановление со временем
func (rc *RecoveryCalculator) calculateTimeBasedRecovery(recovery *models.ReputationRecovery, progress float64) float64 {
	delta := recovery.TargetValue - recovery.StartValue
	return recovery.StartValue + (delta * progress)
}

// calculatePaymentBasedRecovery - экспоненциальное восстановление (быстрее в начале)
func (rc *RecoveryCalculator) calculatePaymentBasedRecovery(recovery *models.ReputationRecovery, progress float64) float64 {
	delta := recovery.TargetValue - recovery.StartValue
	// Экспоненциальная кривая для премиум восстановления
	exponentialProgress := 1.0 - math.Exp(-progress*3.0)
	return recovery.StartValue + (delta * exponentialProgress)
}

// calculateQuestBasedRecovery - ступенчатое восстановление через задания
func (rc *RecoveryCalculator) calculateQuestBasedRecovery(recovery *models.ReputationRecovery, progress float64) float64 {
	delta := recovery.TargetValue - recovery.StartValue

	// Ступенчатое восстановление (25%, 50%, 75%, 100%)
	steps := []float64{0.25, 0.50, 0.75, 1.0}
	currentStep := 0

	for _, step := range steps {
		if progress >= step {
			currentStep++
		} else {
			break
		}
	}

	stepProgress := float64(currentStep) / float64(len(steps))
	return recovery.StartValue + (delta * stepProgress)
}

// calculateActionBasedRecovery - нелинейное восстановление через действия
func (rc *RecoveryCalculator) calculateActionBasedRecovery(recovery *models.ReputationRecovery, progress float64) float64 {
	delta := recovery.TargetValue - recovery.StartValue

	// Логарифмическая кривая - быстрое начальное восстановление, затем замедление
	if progress <= 0 {
		return recovery.StartValue
	}

	logProgress := math.Log(1.0+progress*9.0) / math.Log(10.0) // log10(1+9*progress)
	return recovery.StartValue + (delta * logProgress)
}

// calculateHybridRecovery - комбинированное восстановление
func (rc *RecoveryCalculator) calculateHybridRecovery(recovery *models.ReputationRecovery, progress float64) float64 {
	// 70% времени + 30% действий
	timeProgress := progress * 0.7
	actionProgress := rc.calculateActionBasedRecovery(recovery, progress) * 0.3 / (recovery.TargetValue - recovery.StartValue)

	totalProgress := timeProgress + actionProgress
	delta := recovery.TargetValue - recovery.StartValue

	return recovery.StartValue + (delta * totalProgress)
}

// EstimateRecoveryDuration оценивает длительность восстановления
func (rc *RecoveryCalculator) EstimateRecoveryDuration(startValue, targetValue float64) time.Duration {
	if rc.config == nil {
		return rc.config.MinDuration
	}

	delta := math.Abs(targetValue - startValue)
	baseDuration := time.Duration(delta*float64(rc.config.MinDuration)/100) * time.Hour

	// Применяем множитель
	duration := time.Duration(float64(baseDuration) * rc.config.TimeMultiplier)

	// Ограничиваем диапазон
	if duration < rc.config.MinDuration {
		duration = rc.config.MinDuration
	}
	if duration > rc.config.MaxDuration {
		duration = rc.config.MaxDuration
	}

	return duration
}

// CalculateRecoveryCost рассчитывает стоимость восстановления
func (rc *RecoveryCalculator) CalculateRecoveryCost(startValue, targetValue float64) models.RecoveryCost {
	delta := math.Abs(targetValue - startValue)

	baseCost := delta * 10.0 // 10 единиц валюты за единицу репутации

	cost := models.RecoveryCost{
		CurrencyType: "eddies", // Основная валюта игры
		Amount:       baseCost * rc.config.CostMultiplier,
	}

	return cost
}

// ValidateRecoveryRequest проверяет корректность запроса на восстановление
func (rc *RecoveryCalculator) ValidateRecoveryRequest(recovery *models.ReputationRecovery) error {
	if recovery.StartValue >= recovery.TargetValue {
		return errors.New("start value must be less than target value")
	}

	if recovery.Method == "" {
		return errors.New("recovery method is required")
	}

	duration := rc.EstimateRecoveryDuration(recovery.StartValue, recovery.TargetValue)
	if duration < rc.config.MinDuration || duration > rc.config.MaxDuration {
		return errors.New("estimated duration is outside allowed range")
	}

	return nil
}