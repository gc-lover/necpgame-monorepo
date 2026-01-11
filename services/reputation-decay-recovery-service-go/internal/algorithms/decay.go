// Package algorithms содержит алгоритмы для репутационных механик
// Issue: #2174 - Reputation Decay & Recovery mechanics
// PERFORMANCE: Оптимизированные алгоритмы для MMOFPS с минимальными аллокациями
package algorithms

import (
	"math"
	"time"

	"github.com/go-faster/errors"

	"github.com/gc-lover/necp-game/services/reputation-decay-recovery-service-go/internal/models"
)

// DecayCalculator рассчитывает разрушение репутации
type DecayCalculator struct {
	config *models.DecayConfig
}

// NewDecayCalculator создает новый калькулятор разрушения
func NewDecayCalculator(config *models.DecayConfig) *DecayCalculator {
	return &DecayCalculator{
		config: config,
	}
}

// CalculateDecay рассчитывает изменение репутации за период времени
func (dc *DecayCalculator) CalculateDecay(currentValue float64, lastActivity time.Time, now time.Time) (float64, error) {
	if dc.config == nil {
		return 0, errors.New("decay config is required")
	}

	timeSinceActivity := now.Sub(lastActivity)

	// Если прошло меньше порогового времени, не разрушаем
	if timeSinceActivity < dc.config.TimeThreshold {
		return 0, nil
	}

	// Расчет базового разрушения
	daysSinceActivity := timeSinceActivity.Hours() / 24.0
	baseDecay := currentValue * (dc.config.BaseDecayRate / 100.0) * daysSinceActivity

	// Применяем нелинейный фактор (медленнее разрушается при низкой репутации)
	nonlinearFactor := dc.calculateNonlinearFactor(currentValue)

	// Применяем фактор активности (меньше разрушается при недавней активности)
	activityFactor := dc.calculateActivityFactor(timeSinceActivity)

	totalDecay := baseDecay * nonlinearFactor * activityFactor

	// Ограничиваем максимальное разрушение за раз
	maxDecay := currentValue * (dc.config.MaxDecayRate / 100.0)
	if totalDecay > maxDecay {
		totalDecay = maxDecay
	}

	// Не позволяем репутации опуститься ниже минимума
	if currentValue-totalDecay < dc.config.MinReputation {
		totalDecay = currentValue - dc.config.MinReputation
		if totalDecay < 0 {
			totalDecay = 0
		}
	}

	return -totalDecay, nil // Возвращаем отрицательное значение (разрушение)
}

// calculateNonlinearFactor рассчитывает нелинейный фактор
// При низкой репутации разрушение замедляется (труднее упасть ниже)
func (dc *DecayCalculator) calculateNonlinearFactor(currentValue float64) float64 {
	if currentValue >= 0 {
		return 1.0
	}

	// Для отрицательной репутации фактор уменьшается экспоненциально
	absValue := math.Abs(currentValue)
	factor := math.Exp(-absValue / 100.0) // Мягкая кривая

	return math.Max(factor, 0.1) // Минимум 10% от базового разрушения
}

// calculateActivityFactor рассчитывает фактор недавней активности
func (dc *DecayCalculator) calculateActivityFactor(timeSinceActivity time.Duration) float64 {
	days := timeSinceActivity.Hours() / 24.0

	// Фактор уменьшается со временем
	// Недавняя активность сильно замедляет разрушение
	factor := 1.0 / (1.0 + days*0.1) // Гиперболическая кривая

	return math.Max(factor, 0.01) // Минимум 1% от базового разрушения
}

// ShouldProcessDecay проверяет, нужно ли обрабатывать разрушение для данного процесса
func (dc *DecayCalculator) ShouldProcessDecay(decay *models.ReputationDecay, now time.Time) bool {
	return decay.IsActive && now.After(decay.NextDecayTime)
}

// UpdateDecayProcess обновляет процесс разрушения после применения
func (dc *DecayCalculator) UpdateDecayProcess(decay *models.ReputationDecay, delta float64, now time.Time) {
	decay.CurrentValue += delta
	decay.LastDecayTime = now

	// Следующее обновление через 1 час для MMOFPS отзывчивости
	decay.NextDecayTime = now.Add(1 * time.Hour)
	decay.UpdatedAt = now

	// Деактивируем если достигли минимума
	if decay.CurrentValue <= dc.config.MinReputation {
		decay.IsActive = false
	}
}