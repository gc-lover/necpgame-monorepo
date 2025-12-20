// Package server Issue: #140876112
package server

import (
	"context"
	"database/sql"
	"time"

	"go.uber.org/zap"
)

// RomanceCoreService реализует алгоритмы романтики
type RomanceCoreService struct {
	db     *sql.DB
	logger *zap.Logger
}

// RomanceCalculationContext представляет контекст для расчета романтики
type RomanceCalculationContext struct {
	PlayerID     string                 `json:"player_id"`
	TargetID     string                 `json:"target_id"`
	RomanceType  string                 `json:"romance_type"`
	Location     string                 `json:"location"`
	TimeOfDay    string                 `json:"time_of_day"`
	Season       string                 `json:"season"`
	Weather      string                 `json:"weather"`
	PlayerTraits map[string]interface{} `json:"player_traits"`
	TargetTraits map[string]interface{} `json:"target_traits"`
	Relationship RelationshipState      `json:"relationship"`
	CultureMatch float64                `json:"culture_match"`
}

// RelationshipState представляет состояние отношений
type RelationshipState struct {
	Stage             string `json:"stage"`
	Score             int    `json:"score"`
	ChemistryScore    int    `json:"chemistry_score"`
	TrustScore        int    `json:"trust_score"`
	PhysicalIntimacy  int    `json:"physical_intimacy"`
	EmotionalIntimacy int    `json:"emotional_intimacy"`
	Health            int    `json:"health"`
	IsRomantic        bool   `json:"is_romantic"`
}

// RomanceEvent представляет романтическое событие
type RomanceEvent struct {
	ID             string                 `json:"id"`
	Name           string                 `json:"name"`
	Type           string                 `json:"type"`
	Priority       string                 `json:"priority"`
	RequiredStage  string                 `json:"required_stage"`
	MinChemistry   int                    `json:"min_chemistry"`
	MinTrust       int                    `json:"min_trust"`
	LocationReq    []string               `json:"location_req"`
	TimeReq        []string               `json:"time_req"`
	CultureFactors map[string]interface{} `json:"culture_factors"`
	BaseWeight     int                    `json:"base_weight"`
	DramaFactor    float64                `json:"drama_factor"`
	ConflictFactor float64                `json:"conflict_factor"`
}

// ScoredEvent представляет событие с рассчитанным баллом
type ScoredEvent struct {
	Event        RomanceEvent `json:"event"`
	FinalScore   float64      `json:"final_score"`
	Weight       float64      `json:"weight"`
	Chemistry    float64      `json:"chemistry"`
	CultureBonus float64      `json:"culture_bonus"`
	RandomBonus  float64      `json:"random_bonus"`
}

// NewRomanceCoreService создает новый сервис романтики

// CalculateFinalEventScore рассчитывает итоговый балл события (Algorithm 1)
func (s *RomanceCoreService) CalculateFinalEventScore(ctx context.Context, event RomanceEvent, romanceCtx RomanceCalculationContext) (float64, error) {
	// Context timeout для предотвращения зависаний
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	s.logger.Info("Calculating final event score",
		zap.String("event_id", event.ID),
		zap.String("player_id", romanceCtx.PlayerID),
		zap.String("target_id", romanceCtx.TargetID))

	// Базовый вес события
	score := float64(event.BaseWeight)

	// Бонусы за локацию
	score += s.calculateLocationBonus(event, romanceCtx)

	// Бонусы за время и сезон
	score += s.calculateTimingBonus(event, romanceCtx)

	// Бонусы за состояние отношений
	score += s.calculateRelationshipBonus(event, romanceCtx)

	// Бонусы за химию
	score += s.calculateChemistryBonus(event, romanceCtx)

	// Бонусы за предпочтения игрока
	score += s.calculatePreferenceBonus()

	// Штрафы за драму и конфликт
	score += s.calculateDramaPenalty(event, romanceCtx)

	// Ограничиваем диапазон 0-100
	if score < 0 {
		score = 0
	} else if score > 100 {
		score = 100
	}

	s.logger.Info("Final event score calculated",
		zap.String("event_id", event.ID),
		zap.Float64("score", score))

	return score, nil
}

// calculateLocationBonus рассчитывает бонус за локацию
func (s *RomanceCoreService) calculateLocationBonus(event RomanceEvent, context RomanceContext) float64 {
	bonus := 0.0

	// Проверяем соответствие локации
	for _, reqLoc := range event.LocationReq {
		if reqLoc == context.Location {
			bonus += 15.0
			break
		}
		// Частичное совпадение (регион)
		if len(reqLoc) > 0 && len(context.Location) > 0 && reqLoc[0] == context.Location[0] {
			bonus += 5.0
		}
	}

	return bonus
}

// calculateTimingBonus рассчитывает бонус за тайминг
func (s *RomanceCoreService) calculateTimingBonus(event RomanceEvent, context RomanceContext) float64 {
	bonus := 0.0

	// Проверяем время суток
	for _, reqTime := range event.TimeReq {
		if reqTime == context.TimeOfDay {
			bonus += 10.0
			break
		}
	}

	// Сезонные бонусы
	switch context.Season {
	case "spring":
		bonus += 5.0 // Весна - время романтики
	case "summer":
		bonus += 3.0 // Лето - активные отношения
	case "autumn":
		bonus += 2.0 // Осень - стабильность
	case "winter":
		bonus += 4.0 // Зима - близость
	}

	// Погодные условия
	switch context.Weather {
	case "sunny":
		bonus += 3.0
	case "rainy":
		bonus += 8.0 // Романтика в дождь
	case "snowy":
		bonus += 6.0 // Зимняя романтика
	}

	return bonus
}

// calculateRelationshipBonus рассчитывает бонус за состояние отношений
func (s *RomanceCoreService) calculateRelationshipBonus(event RomanceEvent, context RomanceContext) float64 {
	bonus := 0.0

	// Бонус за соответствие стадии
	if event.RequiredStage == context.Relationship.Stage {
		bonus += 20.0
	} else {
		// Штраф за несоответствие
		bonus -= 10.0
	}

	// Бонус за общий score отношений
	bonus += float64(context.Relationship.Score) * 0.5

	// Бонус за доверие
	bonus += float64(context.Relationship.TrustScore) * 0.3

	// Бонус за эмоциональную близость
	bonus += float64(context.Relationship.EmotionalIntimacy) * 0.4

	return bonus
}

// calculateChemistryBonus рассчитывает бонус за химию
func (s *RomanceCoreService) calculateChemistryBonus(event RomanceEvent, context RomanceContext) float64 {
	bonus := 0.0

	chemistry := float64(context.Relationship.ChemistryScore)

	// Минимальная химия для события
	if chemistry < float64(event.MinChemistry) {
		return -50.0 // Блокирующий штраф
	}

	// Бонус за превышение минимальной химии
	bonus += (chemistry - float64(event.MinChemistry)) * 0.5

	// Дополнительный бонус за высокую химию
	if chemistry > 80 {
		bonus += 15.0
	}

	return bonus
}

// calculatePreferenceBonus рассчитывает бонус за предпочтения игрока
func (s *RomanceCoreService) calculatePreferenceBonus() float64 {
	bonus := 0.0

	// Здесь должна быть логика анализа предпочтений игрока
	// Пока возвращаем небольшой базовый бонус
	bonus += 5.0

	return bonus
}

// calculateDramaPenalty рассчитывает штрафы за драму и конфликт
func (s *RomanceCoreService) calculateDramaPenalty(event RomanceEvent, context RomanceContext) float64 {
	penalty := 0.0

	// Штраф за низкое здоровье отношений
	if context.Relationship.Health < 50 {
		penalty -= 20.0
	}

	// Штраф за драматические события
	penalty -= event.DramaFactor * 10.0

	// Штраф за конфликтные события
	penalty -= event.ConflictFactor * 15.0

	return penalty
}
