// Package service содержит алгоритмы decay и recovery для системы репутации
// Issue: #2174 - Reputation Decay & Recovery mechanics
package service

import (
	"fmt"
	"math"
	"sync"
	"time"

	"necpgame/services/reputation-service-go/internal/models"
)

// ReputationEngine реализует алгоритмы decay и recovery
type ReputationEngine struct {
	mu            sync.RWMutex
	registry      *models.ReputationRegistry
	randomSeed    int64
	lastProcessed time.Time
}

// DecayResult представляет результат применения decay
type DecayResult struct {
	PlayerID       string  `json:"player_id"`
	ReputationType string  `json:"reputation_type"`
	TargetID       string  `json:"target_id"`
	OldValue       float64 `json:"old_value"`
	NewValue       float64 `json:"new_value"`
	DecayApplied   float64 `json:"decay_applied"`
	Reason         string  `json:"reason"`
}

// RecoveryResult представляет результат применения recovery
type RecoveryResult struct {
	PlayerID         string  `json:"player_id"`
	RecoveryAmount   float64 `json:"recovery_amount"`
	NewReputation    float64 `json:"new_reputation"`
	RecoveryType     string  `json:"recovery_type"`
	BonusMultiplier  float64 `json:"bonus_multiplier"`
	CooldownSeconds  int     `json:"cooldown_seconds"`
}

// NewReputationEngine создает новый движок репутации
func NewReputationEngine(registry *models.ReputationRegistry) *ReputationEngine {
	return &ReputationEngine{
		registry:      registry,
		randomSeed:    time.Now().UnixNano(),
		lastProcessed: time.Now(),
	}
}

// ApplyDecay применяет decay к репутации игрока
func (re *ReputationEngine) ApplyDecay(playerID, reputationType, targetID string) (*DecayResult, error) {
	re.mu.Lock()
	defer re.mu.Unlock()

	// Получить текущую репутацию
	rep, exists := re.registry.GetPlayerReputation(playerID, reputationType, targetID)
	if !exists {
		return nil, &ReputationError{Type: "NOT_FOUND", Message: "Reputation not found"}
	}

	// Проверить, можно ли применять decay
	if !rep.IsDecayEnabled {
		return nil, &ReputationError{Type: "DECAY_DISABLED", Message: "Decay is disabled for this reputation"}
	}

	// Проверить время последнего decay
	now := time.Now()
	if now.Unix() < rep.NextDecayUnix {
		return nil, &ReputationError{Type: "TOO_EARLY", Message: "Decay cooldown not expired"}
	}

	// Получить правило decay
	rule, exists := re.registry.GetDecayRule(reputationType)
	if !exists {
		rule = re.getDefaultDecayRule(reputationType)
	}

	// Рассчитать величину decay
	decayAmount := re.calculateDecayAmount(rep, rule)

	// Проверить границы
	oldValue := rep.CurrentValue
	newValue := math.Max(rep.CurrentValue-decayAmount, rule.MinReputation)

	// Если репутация уже на минимуме, не применять decay
	if newValue >= oldValue {
		return &DecayResult{
			PlayerID:       playerID,
			ReputationType: reputationType,
			TargetID:       targetID,
			OldValue:       oldValue,
			NewValue:       newValue,
			DecayApplied:   0,
			Reason:         "Already at minimum reputation",
		}, nil
	}

	// Применить decay
	rep.CurrentValue = newValue
	rep.LastDecayUnix = now.Unix()
	rep.NextDecayUnix = now.Unix() + int64(rule.DecayIntervalHours*3600)
	rep.UpdatedAt = now

	// Записать изменение
	change := &models.ReputationChange{
		ID:             generateUUID(),
		PlayerID:       playerID,
		ReputationType: reputationType,
		TargetID:       targetID,
		OldValue:       oldValue,
		NewValue:       newValue,
		ChangeAmount:   -decayAmount,
		Reason:         "Decay applied",
		ExecutedAt:     now,
		ExecutedAtUnix: now.Unix(),
		DecayApplied:   1,
	}

	re.registry.RecordReputationChange(change)

	return &DecayResult{
		PlayerID:       playerID,
		ReputationType: reputationType,
		TargetID:       targetID,
		OldValue:       oldValue,
		NewValue:       newValue,
		DecayApplied:   decayAmount,
		Reason:         "Decay applied successfully",
	}, nil
}

// calculateDecayAmount рассчитывает величину decay
func (re *ReputationEngine) calculateDecayAmount(rep *models.PlayerReputation, rule *models.DecayRule) float64 {
	// Базовый decay
	baseDecay := rep.BaseValue * rule.BaseDecayRate * (float64(rule.DecayIntervalHours) / 24.0)

	// Модификатор активности (активные игроки теряют репутацию медленнее)
	activityModifier := 1.0
	if re.isPlayerActive(rep.PlayerID) {
		activityModifier = rule.ActivityMultiplier // Обычно < 1.0
	} else {
		activityModifier = 2.0 // Неактивные теряют быстрее
	}

	// Модификатор фракции
	factionModifier := re.getFactionModifier(rep)

	// Decay rate из репутации игрока
	playerDecayRate := rep.DecayRate
	if playerDecayRate <= 0 {
		playerDecayRate = rule.BaseDecayRate
	}

	// Итоговый decay
	totalDecay := baseDecay * activityModifier * factionModifier * playerDecayRate

	// Ограничить максимальный decay за раз
	maxDecay := re.registry.Config.MaxDecayPerDay * (float64(rule.DecayIntervalHours) / 24.0)
	totalDecay = math.Min(totalDecay, maxDecay)

	// Не применять decay если репутация уже низкая
	if rep.CurrentValue <= rule.MinReputation+10 {
		totalDecay *= 0.1 // Минимальный decay для очень низкой репутации
	}

	return math.Max(totalDecay, 0.1) // Минимум 0.1 decay
}

// getFactionModifier возвращает модификатор фракции
func (re *ReputationEngine) getFactionModifier(rep *models.PlayerReputation) float64 {
	if rep.ReputationType != "faction" || rep.TargetID == "" {
		return 1.0 // Нет модификатора для не-фракционной репутации
	}

	// Получить репутацию с фракцией
	factionRep, exists := re.registry.GetPlayerReputation(rep.PlayerID, "faction", rep.TargetID)
	if !exists {
		return re.registry.Config.FactionModifierNeutral
	}

	// На основе текущей репутации с фракцией
	switch {
	case factionRep.CurrentValue > 200:
		return re.registry.Config.FactionModifierAlly // Союзники decay медленнее
	case factionRep.CurrentValue < -200:
		return re.registry.Config.FactionModifierEnemy // Враги decay быстрее
	default:
		return re.registry.Config.FactionModifierNeutral
	}
}

// isPlayerActive проверяет активность игрока (упрощенная версия)
func (re *ReputationEngine) isPlayerActive(playerID string) bool {
	// В реальной реализации это проверялось бы по последней активности
	// Для демо возвращаем true
	return true
}

// getDefaultDecayRule возвращает правило decay по умолчанию
func (re *ReputationEngine) getDefaultDecayRule(reputationType string) *models.DecayRule {
	return &models.DecayRule{
		ReputationType:     reputationType,
		BaseDecayRate:      re.registry.Config.DefaultDecayRate,
		DecayIntervalHours: re.registry.Config.DecayIntervalHours,
		MinReputation:      re.registry.Config.MinReputation,
		MaxReputation:      re.registry.Config.MaxReputation,
		ActivityMultiplier: re.registry.Config.ActivityMultiplierActive,
		FactionModifier:    re.registry.Config.FactionModifierNeutral,
		IsActive:          true,
		Priority:          1,
		Version:           1,
	}
}

// ApplyRecovery применяет recovery к репутации игрока
func (re *ReputationEngine) ApplyRecovery(playerID, recoveryType string, context map[string]interface{}) (*RecoveryResult, error) {
	re.mu.Lock()
	defer re.mu.Unlock()

	// Проверить cooldown
	if !re.canApplyRecovery(playerID) {
		cooldown := re.getRecoveryCooldown(playerID)
		return nil, &ReputationError{
			Type:    "COOLDOWN_ACTIVE",
			Message: "Recovery cooldown active",
			Data:    map[string]interface{}{"cooldown_seconds": cooldown},
		}
	}

	// Рассчитать величину recovery
	recoveryAmount := re.calculateRecoveryAmount(playerID, recoveryType, context)

	// Найти репутации для recovery (обычно самые низкие)
	reputationsToRecover := re.findReputationsNeedingRecovery(playerID)

	if len(reputationsToRecover) == 0 {
		return &RecoveryResult{
			PlayerID:        playerID,
			RecoveryAmount:  0,
			NewReputation:   0,
			RecoveryType:    recoveryType,
			BonusMultiplier: 1.0,
			CooldownSeconds: re.registry.Config.RecoveryCooldownHours * 3600,
		}, nil
	}

	// Распределить recovery по репутациям
	totalRecovered := 0.0
	now := time.Now()

	for _, rep := range reputationsToRecover {
		if recoveryAmount <= 0 {
			break
		}

		// Рассчитать сколько можно восстановить для этой репутации
		maxRecoverable := rep.BaseValue - rep.CurrentValue
		if maxRecoverable <= 0 {
			continue // Уже на максимуме
		}

		actualRecovery := math.Min(recoveryAmount, maxRecoverable)
		actualRecovery = math.Min(actualRecovery, re.registry.Config.MaxRecoveryPerDay)

		oldValue := rep.CurrentValue
		newValue := rep.CurrentValue + actualRecovery

		// Обновить репутацию
		rep.CurrentValue = newValue
		rep.UpdatedAt = now

		// Записать изменение
		change := &models.ReputationChange{
			ID:             generateUUID(),
			PlayerID:       playerID,
			ReputationType: rep.ReputationType,
			TargetID:       rep.TargetID,
			OldValue:       oldValue,
			NewValue:       newValue,
			ChangeAmount:   actualRecovery,
			Reason:         "Recovery applied: " + recoveryType,
			ExecutedAt:     now,
			ExecutedAtUnix: now.Unix(),
			DecayApplied:   0,
		}

		re.registry.RecordReputationChange(change)

		recoveryAmount -= actualRecovery
		totalRecovered += actualRecovery
	}

	// Обновить статус recovery
	re.updateRecoveryStatus(playerID, recoveryType, totalRecovered)

	return &RecoveryResult{
		PlayerID:        playerID,
		RecoveryAmount:  totalRecovered,
		NewReputation:   0, // Рассчитывается отдельно если нужно
		RecoveryType:    recoveryType,
		BonusMultiplier: re.calculateBonusMultiplier(playerID, recoveryType),
		CooldownSeconds: re.registry.Config.RecoveryCooldownHours * 3600,
	}, nil
}

// calculateRecoveryAmount рассчитывает величину recovery
func (re *ReputationEngine) calculateRecoveryAmount(playerID, recoveryType string, context map[string]interface{}) float64 {
	baseRecovery := 25.0 // Базовое значение recovery

	// Модификатор типа recovery
	typeModifier := 1.0
	switch recoveryType {
	case "time_based":
		typeModifier = 0.5
	case "action_based":
		typeModifier = 1.0
	case "event_based":
		typeModifier = 1.5
	case "bonus":
		typeModifier = 2.0
	}

	// Бонус за активность
	activityBonus := 1.0
	if re.isPlayerActive(playerID) {
		activityBonus = 1.2
	}

	// Контекстные модификаторы
	contextModifier := 1.0
	if context != nil {
		if difficulty, ok := context["difficulty"].(float64); ok {
			contextModifier *= difficulty // Более сложные действия дают больше recovery
		}
		if streak, ok := context["streak"].(float64); ok {
			contextModifier *= (1 + streak*0.1) // Серия увеличивает recovery
		}
	}

	totalRecovery := baseRecovery * typeModifier * activityBonus * contextModifier

	// Ограничить максимум
	return math.Min(totalRecovery, re.registry.Config.MaxRecoveryPerDay)
}

// findReputationsNeedingRecovery находит репутации, которые нуждаются в recovery
func (re *ReputationEngine) findReputationsNeedingRecovery(playerID string) []*models.PlayerReputation {
	playerReps, exists := re.registry.PlayerReputations[playerID]
	if !exists {
		return nil
	}

	var needingRecovery []*models.PlayerReputation

	for _, rep := range playerReps {
		if rep.IsRecoveryEnabled && rep.CurrentValue < rep.BaseValue {
			needingRecovery = append(needingRecovery, rep)
		}
	}

	// Сортировать по степени нужды (самые низкие сначала)
	for i := 0; i < len(needingRecovery)-1; i++ {
		for j := i + 1; j < len(needingRecovery); j++ {
			diffI := needingRecovery[i].BaseValue - needingRecovery[i].CurrentValue
			diffJ := needingRecovery[j].BaseValue - needingRecovery[j].CurrentValue
			if diffJ > diffI {
				needingRecovery[i], needingRecovery[j] = needingRecovery[j], needingRecovery[i]
			}
		}
	}

	return needingRecovery
}

// canApplyRecovery проверяет, можно ли применить recovery
func (re *ReputationEngine) canApplyRecovery(playerID string) bool {
	// В реальной реализации проверяется по статусу recovery игрока
	// Для демо всегда разрешаем
	return true
}

// getRecoveryCooldown возвращает cooldown для recovery
func (re *ReputationEngine) getRecoveryCooldown(playerID string) int {
	return re.registry.Config.RecoveryCooldownHours * 3600
}

// updateRecoveryStatus обновляет статус recovery игрока
func (re *ReputationEngine) updateRecoveryStatus(playerID, recoveryType string, amount float64) {
	// В реальной реализации обновляется статус recovery игрока
	// Для демо просто логируем
}

// calculateBonusMultiplier рассчитывает бонусный множитель recovery
func (re *ReputationEngine) calculateBonusMultiplier(playerID, recoveryType string) float64 {
	// Базовый множитель
	multiplier := 1.0

	// Бонус за тип recovery
	switch recoveryType {
	case "bonus":
		multiplier = 1.5
	case "event_based":
		multiplier = 1.3
	}

	// Бонус за активность
	if re.isPlayerActive(playerID) {
		multiplier *= 1.1
	}

	return multiplier
}

// BatchApplyDecay применяет decay ко всем игрокам (для фоновой обработки)
func (re *ReputationEngine) BatchApplyDecay() ([]*DecayResult, error) {
	re.mu.Lock()
	defer re.mu.Unlock()

	var results []*DecayResult
	now := time.Now()

	// Проходим по всем игрокам
	for playerID, playerReps := range re.registry.PlayerReputations {
		for _, rep := range playerReps {
			// Проверить, нужно ли применять decay
			if !rep.IsDecayEnabled || now.Unix() < rep.NextDecayUnix {
				continue
			}

			// Применить decay
			result, err := re.ApplyDecay(playerID, rep.ReputationType, rep.TargetID)
			if err != nil {
				// Логировать ошибку, но продолжать
				continue
			}

			results = append(results, result)
		}
	}

	return results, nil
}

// ReputationError представляет ошибку репутации
type ReputationError struct {
	Type    string                 `json:"type"`
	Message string                 `json:"message"`
	Data    map[string]interface{} `json:"data,omitempty"`
}

func (e *ReputationError) Error() string {
	return e.Message
}

// generateUUID генерирует простой UUID (в реальной реализации использовать crypto/rand)
func generateUUID() string {
	// Для демо используем timestamp
	return fmt.Sprintf("rep-%d", time.Now().UnixNano())
}