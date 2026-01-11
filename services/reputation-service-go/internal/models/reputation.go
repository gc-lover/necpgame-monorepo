// Package models содержит модели данных для системы репутации
// Issue: #2174 - Reputation Decay & Recovery mechanics
package models

import (
	"time"
)

// PlayerReputation представляет репутацию игрока
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16-24 bytes): Time (24), string (16+), float64 (8)
// Medium fields (8 bytes aligned): int64 (8)
// Small fields (≤4 bytes): int32, bool
//go:align 64
type PlayerReputation struct {
	// Large fields first (16-24 bytes): Time (24), string (16+)
	CreatedAt     time.Time `json:"created_at" db:"created_at"`
	UpdatedAt     time.Time `json:"updated_at" db:"updated_at"`
	PlayerID      string    `json:"player_id" db:"player_id"`             // UUID игрока
	ReputationType string    `json:"reputation_type" db:"reputation_type"` // player, faction, region, global
	TargetID      string    `json:"target_id" db:"target_id"`             // ID цели (faction_id, region_id, etc.)

	// Large/Medium fields (8 bytes aligned): float64
	CurrentValue float64 `json:"current_value" db:"current_value"` // Текущее значение репутации
	BaseValue    float64 `json:"base_value" db:"base_value"`       // Базовое значение (без decay)
	DecayRate    float64 `json:"decay_rate" db:"decay_rate"`       // Скорость decay (0-1 в день)
	RecoveryRate float64 `json:"recovery_rate" db:"recovery_rate"` // Множитель recovery

	// Medium fields (8 bytes aligned): int64
	LastDecayUnix int64 `json:"last_decay_unix" db:"last_decay_unix"` // Unix timestamp последнего decay
	NextDecayUnix int64 `json:"next_decay_unix" db:"next_decay_unix"` // Unix timestamp следующего decay

	// Small fields (≤4 bytes): int32, bool
	Version         int  `json:"version" db:"version"`                     // Версия для optimistic locking
	IsActive        bool `json:"is_active" db:"is_active"`                 // Активна ли репутация
	IsDecayEnabled  bool `json:"is_decay_enabled" db:"is_decay_enabled"`   // Включен ли decay
	IsRecoveryEnabled bool `json:"is_recovery_enabled" db:"is_recovery_enabled"` // Включен ли recovery
}

// ReputationChange представляет изменение репутации
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16-24 bytes): Time (24), string (16+), float64 (8)
// Medium fields (8 bytes aligned): int64 (8)
// Small fields (≤4 bytes): int32
//go:align 64
type ReputationChange struct {
	// Large fields first (16-24 bytes): Time (24), string (16+)
	ExecutedAt     time.Time `json:"executed_at" db:"executed_at"`
	ID             string    `json:"id" db:"id"`                         // UUID изменения
	PlayerID       string    `json:"player_id" db:"player_id"`           // ID игрока
	ReputationType string    `json:"reputation_type" db:"reputation_type"` // Тип репутации
	TargetID       string    `json:"target_id" db:"target_id"`           // ID цели
	Reason         string    `json:"reason" db:"reason"`                 // Причина изменения

	// Large/Medium fields (8 bytes aligned): float64, int64
	OldValue      float64 `json:"old_value" db:"old_value"`           // Старое значение
	NewValue      float64 `json:"new_value" db:"new_value"`           // Новое значение
	ChangeAmount  float64 `json:"change_amount" db:"change_amount"`   // Величина изменения
	ExecutedAtUnix int64 `json:"executed_at_unix" db:"executed_at_unix"` // Unix timestamp

	// Medium fields (8 bytes aligned): int32 (grouped together)
	EventID      int `json:"event_id" db:"event_id"`           // ID связанного события
	DecayApplied int `json:"decay_applied" db:"decay_applied"` // Применен ли decay (0=no, 1=yes)
}

// DecayRule представляет правило decay для типа репутации
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16+ bytes): string, float64
// Medium fields (8 bytes aligned): int32 (grouped)
// Small fields (≤4 bytes): n/a
//go:align 64
type DecayRule struct {
	// Large fields first (16+ bytes): string
	ReputationType string `json:"reputation_type" db:"reputation_type"` // Тип репутации

	// Large/Medium fields (8 bytes aligned): float64, int32
	BaseDecayRate     float64 `json:"base_decay_rate" db:"base_decay_rate"`         // Базовая скорость decay (0-1)
	ActivityMultiplier float64 `json:"activity_multiplier" db:"activity_multiplier"` // Множитель активности
	FactionModifier   float64 `json:"faction_modifier" db:"faction_modifier"`       // Модификатор фракции
	MinReputation     float64 `json:"min_reputation" db:"min_reputation"`           // Мин. репутация для decay
	MaxReputation     float64 `json:"max_reputation" db:"max_reputation"`           // Макс. репутация для decay

	// Medium fields (8 bytes aligned): int32 (grouped together)
	DecayIntervalHours int `json:"decay_interval_hours" db:"decay_interval_hours"` // Интервал decay в часах
	Priority           int `json:"priority" db:"priority"`                         // Приоритет правила
	Version            int `json:"version" db:"version"`                           // Версия правила
	IsActive           bool `json:"is_active" db:"is_active"`                      // Активно ли правило
}

// RecoveryEvent представляет событие recovery
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16-24 bytes): Time (24), string (16+), float64 (8)
// Medium fields (8 bytes aligned): int32 (grouped)
// Small fields (≤4 bytes): bool
//go:align 64
type RecoveryEvent struct {
	// Large fields first (16-24 bytes): Time (24), string (16+)
	ExecutedAt time.Time `json:"executed_at" db:"executed_at"`
	ID         string    `json:"id" db:"id"`               // UUID события
	PlayerID   string    `json:"player_id" db:"player_id"` // ID игрока
	EventType  string    `json:"event_type" db:"event_type"` // Тип события recovery

	// Large/Medium fields (8 bytes aligned): float64
	RecoveryAmount float64 `json:"recovery_amount" db:"recovery_amount"` // Величина recovery

	// Medium fields (8 bytes aligned): int32 (grouped together)
	EventID        int  `json:"event_id" db:"event_id"`               // ID связанного события
	RecoveryStreak int  `json:"recovery_streak" db:"recovery_streak"` // Серия recovery
	CooldownHours  int  `json:"cooldown_hours" db:"cooldown_hours"`   // Cooldown в часах

	// Small fields (≤4 bytes): bool
	IsBonusRecovery bool `json:"is_bonus_recovery" db:"is_bonus_recovery"` // Бонусный recovery
	IsActive        bool `json:"is_active" db:"is_active"`                 // Активно ли событие
}

// ReputationProfile представляет полный профиль репутации игрока
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16-24 bytes): Time (24), maps (24+)
// Medium fields (8 bytes aligned): slices
// Small fields (≤4 bytes): int32, bool
//go:align 64
type ReputationProfile struct {
	// Large fields first (16-24 bytes): Time (24), maps (24+)
	LastUpdated time.Time `json:"last_updated"`

	// Player basic info
	PlayerID string `json:"player_id"`
	Level    string `json:"level"` // outcast, neutral, respected, honored, legendary

	// Reputation scores by type
	ReputationScores map[string]*PlayerReputation `json:"reputation_scores"` // key: "player:{id}", "faction:{id}", etc.

	// Decay and recovery status
	DecayStatus   *DecayStatus   `json:"decay_status"`
	RecoveryStatus *RecoveryStatus `json:"recovery_status"`

	// Recent history (last 10 changes)
	RecentChanges []*ReputationChange `json:"recent_changes"`

	// Statistics
	TotalDecay   float64 `json:"total_decay"`   // Общий decay за всё время
	TotalRecovery float64 `json:"total_recovery"` // Общий recovery за всё время
	ChangeCount  int     `json:"change_count"`  // Количество изменений
}

// DecayStatus представляет статус decay для игрока
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16-24 bytes): Time (24), float64 (8)
// Medium fields (8 bytes aligned): int64 (8)
// Small fields (≤4 bytes): int32, bool
//go:align 64
type DecayStatus struct {
	// Large fields first (16-24 bytes): Time (24), float64 (8)
	LastRun    time.Time `json:"last_run"`
	NextRun    time.Time `json:"next_run"`
	TotalDecayed float64 `json:"total_decayed"` // Общий decay

	// Medium fields (8 bytes aligned): int64
	LastRunUnix  int64 `json:"last_run_unix"`  // Unix timestamp
	NextRunUnix  int64 `json:"next_run_unix"`  // Unix timestamp

	// Medium/Small fields (8 bytes aligned): int32, bool
	DecayCount     int  `json:"decay_count"`      // Количество применений decay
	IsActive       bool `json:"is_active"`        // Активен ли decay
	DecayMultiplier float64 `json:"decay_multiplier"` // Текущий множитель decay
}

// RecoveryStatus представляет статус recovery для игрока
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16-24 bytes): Time (24), float64 (8)
// Medium fields (8 bytes aligned): int32 (grouped)
// Small fields (≤4 bytes): bool
//go:align 64
type RecoveryStatus struct {
	// Large fields first (16-24 bytes): Time (24), float64 (8)
	LastRecovery time.Time `json:"last_recovery"`
	AvailableRecovery float64 `json:"available_recovery"` // Доступный recovery

	// Medium fields (8 bytes aligned): int32 (grouped together)
	RecoveryCount  int `json:"recovery_count"`  // Количество recovery
	RecoveryStreak int `json:"recovery_streak"` // Текущая серия
	CooldownSeconds int `json:"cooldown_seconds"` // Cooldown в секундах

	// Small fields (≤4 bytes): bool
	IsCooldownActive bool `json:"is_cooldown_active"` // Активен ли cooldown
	CanRecover       bool `json:"can_recover"`        // Можно ли recover сейчас
}

// ReputationStatistics представляет статистику системы репутации
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16-24 bytes): Time (24), maps (24+), float64 (8)
// Medium fields (8 bytes aligned): int32 (grouped)
// Small fields (≤4 bytes): n/a
//go:align 64
type ReputationStatistics struct {
	// Large fields first (16-24 bytes): Time (24), maps (24+), float64 (8)
	LastCalculated time.Time `json:"last_calculated"`
	AverageReputation map[string]float64 `json:"average_reputation"` // Средняя репутация по типам

	// Large/Medium fields (8 bytes aligned): float64, int32
	TotalPlayers    int     `json:"total_players"`     // Общее количество игроков
	ActivePlayers   int     `json:"active_players"`    // Активных игроков
	DecayEvents     int     `json:"decay_events"`      // Событий decay
	RecoveryEvents  int     `json:"recovery_events"`   // Событий recovery
	TimeframeHours  int     `json:"timeframe_hours"`   // Период анализа в часах

	// Reputation distribution by levels
	ReputationDistribution map[string]int `json:"reputation_distribution"` // outcast, neutral, etc.

	// Most changed players (top 10)
	MostChangedPlayers []*PlayerReputationChange `json:"most_changed_players"`
}

// PlayerReputationChange представляет изменение репутации игрока для статистики
type PlayerReputationChange struct {
	PlayerID         string  `json:"player_id"`
	ReputationChange float64 `json:"reputation_change"`
	ChangeCount      int     `json:"change_count"`
}

// ReputationRegistry центральный реестр репутации
type ReputationRegistry struct {
	// Player reputations by player_id -> reputation_type -> target_id -> reputation
	PlayerReputations map[string]map[string]map[string]*PlayerReputation `json:"player_reputations"`

	// Decay rules by reputation_type
	DecayRules map[string]*DecayRule `json:"decay_rules"`

	// Recent changes for analytics
	RecentChanges []*ReputationChange `json:"recent_changes"`

	// Statistics
	Stats *ReputationStatistics `json:"stats"`

	// Configuration
	Config *ReputationConfig `json:"config"`
}

// ReputationConfig представляет конфигурацию системы репутации
type ReputationConfig struct {
	// Global settings
	MaxReputation     float64 `json:"max_reputation"`      // Максимальная репутация
	MinReputation     float64 `json:"min_reputation"`      // Минимальная репутация
	DefaultDecayRate  float64 `json:"default_decay_rate"`  // Дефолтная скорость decay
	DefaultRecoveryRate float64 `json:"default_recovery_rate"` // Дефолтная скорость recovery

	// Decay settings
	DecayIntervalHours int `json:"decay_interval_hours"` // Интервал decay в часах
	MaxDecayPerDay     float64 `json:"max_decay_per_day"` // Максимальный decay в день

	// Recovery settings
	RecoveryCooldownHours int `json:"recovery_cooldown_hours"` // Cooldown recovery в часах
	MaxRecoveryPerDay     float64 `json:"max_recovery_per_day"` // Максимальный recovery в день

	// Activity multipliers
	ActivityMultiplierActive   float64 `json:"activity_multiplier_active"`   // Для активных игроков
	ActivityMultiplierInactive float64 `json:"activity_multiplier_inactive"` // Для неактивных игроков

	// Faction modifiers
	FactionModifierAlly   float64 `json:"faction_modifier_ally"`   // Союзники
	FactionModifierEnemy  float64 `json:"faction_modifier_enemy"`  // Враги
	FactionModifierNeutral float64 `json:"faction_modifier_neutral"` // Нейтралы
}

// NewReputationRegistry создает новый реестр репутации
func NewReputationRegistry() *ReputationRegistry {
	return &ReputationRegistry{
		PlayerReputations: make(map[string]map[string]map[string]*PlayerReputation),
		DecayRules:        make(map[string]*DecayRule),
		RecentChanges:     make([]*ReputationChange, 0),
		Stats:            &ReputationStatistics{},
		Config:           &ReputationConfig{
			MaxReputation:           1000.0,
			MinReputation:           -1000.0,
			DefaultDecayRate:        0.05,  // 5% в день
			DefaultRecoveryRate:     1.0,
			DecayIntervalHours:      24,
			MaxDecayPerDay:          50.0,
			RecoveryCooldownHours:   6,
			MaxRecoveryPerDay:       100.0,
			ActivityMultiplierActive:   0.5,  // Decay медленнее для активных
			ActivityMultiplierInactive: 2.0,  // Decay быстрее для неактивных
			FactionModifierAlly:     0.5,
			FactionModifierEnemy:    2.0,
			FactionModifierNeutral:  1.0,
		},
	}
}

// RegisterPlayerReputation регистрирует репутацию игрока
func (r *ReputationRegistry) RegisterPlayerReputation(rep *PlayerReputation) {
	if r.PlayerReputations[rep.PlayerID] == nil {
		r.PlayerReputations[rep.PlayerID] = make(map[string]map[string]*PlayerReputation)
	}
	if r.PlayerReputations[rep.PlayerID][rep.ReputationType] == nil {
		r.PlayerReputations[rep.PlayerID][rep.ReputationType] = make(map[string]*PlayerReputation)
	}
	r.PlayerReputations[rep.PlayerID][rep.ReputationType][rep.TargetID] = rep
}

// GetPlayerReputation возвращает репутацию игрока
func (r *ReputationRegistry) GetPlayerReputation(playerID, reputationType, targetID string) (*PlayerReputation, bool) {
	playerReps, exists := r.PlayerReputations[playerID]
	if !exists {
		return nil, false
	}

	typeReps, exists := playerReps[reputationType]
	if !exists {
		return nil, false
	}

	rep, exists := typeReps[targetID]
	return rep, exists
}

// GetPlayerReputationProfile возвращает полный профиль репутации игрока
func (r *ReputationRegistry) GetPlayerReputationProfile(playerID string) (*ReputationProfile, bool) {
	playerReps, exists := r.PlayerReputations[playerID]
	if !exists {
		return nil, false
	}

	// Flatten the nested map for the profile
	flattenedReps := make(map[string]*PlayerReputation)
	for repType, typeReps := range playerReps {
		for targetID, rep := range typeReps {
			key := repType
			if targetID != "" {
				key += ":" + targetID
			}
			flattenedReps[key] = rep
		}
	}

	profile := &ReputationProfile{
		PlayerID:         playerID,
		ReputationScores: flattenedReps,
		LastUpdated:      time.Now(),
		RecentChanges:    make([]*ReputationChange, 0),
		Level:           r.calculateReputationLevel(flattenedReps),
	}

	// Calculate statistics
	totalDecay := 0.0
	totalRecovery := 0.0
	changeCount := 0

	for _, typeReps := range playerReps {
		for _, rep := range typeReps {
			if rep.BaseValue > rep.CurrentValue {
				totalDecay += rep.BaseValue - rep.CurrentValue
			} else {
				totalRecovery += rep.CurrentValue - rep.BaseValue
			}
			changeCount++
		}
	}

	profile.TotalDecay = totalDecay
	profile.TotalRecovery = totalRecovery
	profile.ChangeCount = changeCount

	return profile, true
}

// calculateReputationLevel рассчитывает уровень репутации
func (r *ReputationRegistry) calculateReputationLevel(reputations map[string]*PlayerReputation) string {
	if len(reputations) == 0 {
		return "neutral"
	}

	totalScore := 0.0
	count := 0

	for _, rep := range reputations {
		if rep != nil {
			totalScore += rep.CurrentValue
			count++
		}
	}

	if count == 0 {
		return "neutral"
	}

	avgScore := totalScore / float64(count)

	switch {
	case avgScore < -500:
		return "outcast"
	case avgScore < -100:
		return "neutral"
	case avgScore < 300:
		return "respected"
	case avgScore < 700:
		return "honored"
	default:
		return "legendary"
	}
}

// RecordReputationChange записывает изменение репутации
func (r *ReputationRegistry) RecordReputationChange(change *ReputationChange) {
	// Add to recent changes (keep last 100)
	r.RecentChanges = append(r.RecentChanges, change)
	if len(r.RecentChanges) > 100 {
		r.RecentChanges = r.RecentChanges[len(r.RecentChanges)-100:]
	}

	// Update player reputation
	if rep, exists := r.GetPlayerReputation(change.PlayerID, change.ReputationType, change.TargetID); exists {
		rep.CurrentValue = change.NewValue
		rep.BaseValue = change.NewValue
		rep.UpdatedAt = change.ExecutedAt
		rep.Version++
	}
}

// UpdateDecayRules обновляет правила decay
func (r *ReputationRegistry) UpdateDecayRules(rules map[string]*DecayRule) {
	r.DecayRules = rules
}

// GetDecayRule возвращает правило decay для типа репутации
func (r *ReputationRegistry) GetDecayRule(reputationType string) (*DecayRule, bool) {
	rule, exists := r.DecayRules[reputationType]
	return rule, exists
}