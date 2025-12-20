// Package server Issue: #140876112
package server

import (
	"context"
	"math"
	"time"

	"go.uber.org/zap"
)

// CalculateChemistry рассчитывает химию между персонажами (Algorithm 3)
func (s *RomanceCoreService) CalculateChemistry(ctx context.Context, playerTraits, targetTraits map[string]interface{}) (int, error) {
	// Context timeout для предотвращения зависаний
	ctx, cancel := context.WithTimeout(ctx, 1*time.Second)
	defer cancel()

	s.logger.Info("Calculating chemistry between characters")

	chemistry := 0.0

	// Совместимость по Big Five личностным чертам
	chemistry += s.calculateBigFiveCompatibility(playerTraits, targetTraits) * 0.4

	// Совместимость по общим интересам
	chemistry += s.calculateInterestCompatibility(playerTraits, targetTraits) * 0.3

	// Физическое притяжение
	chemistry += s.calculatePhysicalAttraction(playerTraits, targetTraits) * 0.2

	// Культурная совместимость
	chemistry += s.calculateCulturalCompatibility(playerTraits, targetTraits) * 0.1

	// Нормализация до 0-100
	if chemistry < 0 {
		chemistry = 0
	} else if chemistry > 100 {
		chemistry = 100
	}

	s.logger.Info("Chemistry calculated",
		zap.Float64("chemistry_score", chemistry))

	return int(math.Round(chemistry)), nil
}

// calculateBigFiveCompatibility рассчитывает совместимость по Big Five
func (s *RomanceCoreService) calculateBigFiveCompatibility(player, target map[string]interface{}) float64 {
	compatibility := 0.0

	traits := []string{"openness", "conscientiousness", "extraversion", "agreeableness", "neuroticism"}

	for _, trait := range traits {
		playerVal, playerOk := player[trait].(float64)
		targetVal, targetOk := target[trait].(float64)

		if playerOk && targetOk {
			// Идеальная совместимость при разнице < 0.3
			diff := math.Abs(playerVal - targetVal)
			if diff < 0.3 {
				compatibility += 20.0
			} else if diff < 0.6 {
				compatibility += 10.0
			}
		}
	}

	return compatibility
}

// calculateInterestCompatibility рассчитывает совместимость по интересам
func (s *RomanceCoreService) calculateInterestCompatibility(player, target map[string]interface{}) float64 {
	compatibility := 0.0

	playerInterests, playerOk := player["interests"].([]interface{})
	targetInterests, targetOk := target["interests"].([]interface{})

	if playerOk && targetOk {
		common := 0
		total := len(playerInterests) + len(targetInterests)

		for _, pInt := range playerInterests {
			for _, tInt := range targetInterests {
				if pInt == tInt {
					common++
					break
				}
			}
		}

		if total > 0 {
			compatibility = (float64(common*2) / float64(total)) * 100.0
		}
	}

	return compatibility
}

// calculatePhysicalAttraction рассчитывает физическое притяжение
func (s *RomanceCoreService) calculatePhysicalAttraction(player, target map[string]interface{}) float64 {
	// Упрощенная логика физического притяжения
	playerAge, playerOk := player["age"].(float64)
	targetAge, targetOk := target["age"].(float64)

	if playerOk && targetOk {
		ageDiff := math.Abs(playerAge - targetAge)
		if ageDiff < 5 {
			return 80.0
		} else if ageDiff < 10 {
			return 60.0
		} else if ageDiff < 20 {
			return 40.0
		}
	}

	return 50.0 // Среднее значение по умолчанию
}

// calculateCulturalCompatibility рассчитывает культурную совместимость
func (s *RomanceCoreService) calculateCulturalCompatibility(player, target map[string]interface{}) float64 {
	playerCulture, playerOk := player["culture"].(string)
	targetCulture, targetOk := target["culture"].(string)

	if playerOk && targetOk {
		if playerCulture == targetCulture {
			return 100.0
		}
		// Упрощенная логика для разных культур
		return 70.0
	}

	return 50.0
}
