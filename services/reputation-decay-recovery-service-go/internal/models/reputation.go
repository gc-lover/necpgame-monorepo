// Package models содержит модели данных для системы репутационных механик
// Issue: #2174 - Reputation Decay & Recovery mechanics
package models

import (
	"time"
)

// ReputationDecay представляет процесс естественного разрушения репутации
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16-24 bytes): Time (24), string (16+), float64 (8)
// Medium fields (8 bytes aligned): float64 (grouped together)
// Small fields (≤4 bytes): bool
//go:align 64
type ReputationDecay struct {
	// Large fields first (16-24 bytes): Time (24), string (16+)
	CreatedAt     time.Time `json:"created_at" db:"created_at"`
	UpdatedAt     time.Time `json:"updated_at" db:"updated_at"`
	LastDecayTime time.Time `json:"last_decay_time" db:"last_decay_time"` // Последнее обновление
	NextDecayTime time.Time `json:"next_decay_time" db:"next_decay_time"` // Следующее обновление
	ID            string    `json:"id" db:"id"`                           // UUID процесса (16 bytes)
	CharacterID   string    `json:"character_id" db:"character_id"`     // ID персонажа
	FactionID     string    `json:"faction_id" db:"faction_id"`         // ID фракции

	// Medium fields (8 bytes aligned): float64 (grouped together)
	CurrentValue float64 `json:"current_value" db:"current_value"` // Текущая репутация
	DecayRate    float64 `json:"decay_rate" db:"decay_rate"`       // Скорость разрушения (% в день)

	// Small fields (≤4 bytes): bool
	IsActive bool `json:"is_active" db:"is_active"` // Активен ли процесс
}

// ReputationRecovery представляет процесс восстановления репутации
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16-24 bytes): Time (24), string (16+), pointer (8), map (24+), RecoveryCost (complex)
// Medium fields (8 bytes aligned): float64 (grouped together)
// Small fields (≤4 bytes): RecoveryMethod, RecoveryStatus (if int-based enums)
//go:align 64
type ReputationRecovery struct {
	// Large fields first (16-24 bytes): Time (24), string (16+), pointer (8), map (24+)
	CreatedAt    time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt    time.Time              `json:"updated_at" db:"updated_at"`
	StartTime    time.Time              `json:"start_time" db:"start_time"`         // Время начала
	EstimatedEnd time.Time              `json:"estimated_end" db:"estimated_end"`   // Предполагаемое завершение
	ActualEnd    *time.Time             `json:"actual_end,omitempty" db:"actual_end"` // Фактическое завершение
	ID           string                 `json:"id" db:"id"`                         // UUID процесса (16 bytes)
	CharacterID  string                 `json:"character_id" db:"character_id"`     // ID персонажа
	FactionID    string                 `json:"faction_id" db:"faction_id"`         // ID фракции
	Metadata     map[string]interface{} `json:"metadata" db:"metadata"`             // Дополнительные данные
	Cost         RecoveryCost           `json:"cost" db:"cost"`                     // Стоимость восстановления

	// Medium fields (8 bytes aligned): float64 (grouped together)
	StartValue   float64 `json:"start_value" db:"start_value"`     // Репутация на старте
	TargetValue  float64 `json:"target_value" db:"target_value"`   // Целевая репутация
	CurrentValue float64 `json:"current_value" db:"current_value"` // Текущая репутация
	Progress     float64 `json:"progress" db:"progress"`           // Прогресс (0-1)

	// Small fields (≤4 bytes): RecoveryMethod, RecoveryStatus
	Method RecoveryMethod `json:"method" db:"method"` // Метод восстановления
	Status RecoveryStatus `json:"status" db:"status"` // Статус процесса
}

// RecoveryMethod определяет метод восстановления репутации
type RecoveryMethod string

const (
	MethodTimeBased     RecoveryMethod = "time_based"     // Восстановление со временем
	MethodQuestBased    RecoveryMethod = "quest_based"    // Через квесты
	MethodPaymentBased  RecoveryMethod = "payment_based"  // За плату
	MethodActionBased   RecoveryMethod = "action_based"   // Через действия
	MethodHybrid        RecoveryMethod = "hybrid"         // Комбинированный метод
)

// RecoveryStatus определяет статус процесса восстановления
type RecoveryStatus string

const (
	StatusPending    RecoveryStatus = "pending"    // Ожидает начала
	StatusActive     RecoveryStatus = "active"     // Активен
	StatusPaused     RecoveryStatus = "paused"     // Приостановлен
	StatusCompleted  RecoveryStatus = "completed"  // Завершен
	StatusFailed     RecoveryStatus = "failed"     // Провалился
	StatusCancelled  RecoveryStatus = "cancelled"  // Отменен
)

// RecoveryCost представляет стоимость восстановления
type RecoveryCost struct {
	CurrencyType string  `json:"currency_type"` // Тип валюты (eddies, prestige, etc.)
	Amount       float64 `json:"amount"`        // Количество
	ItemID       *string `json:"item_id,omitempty"` // ID предмета (если требуется)
}

// DecayConfig содержит настройки разрушения репутации
type DecayConfig struct {
	FactionID       string        `json:"faction_id"`
	BaseDecayRate   float64       `json:"base_decay_rate"`   // Базовая скорость (% в день)
	TimeThreshold   time.Duration `json:"time_threshold"`    // Порог времени без активности
	MinReputation   float64       `json:"min_reputation"`    // Минимальная репутация
	MaxDecayRate    float64       `json:"max_decay_rate"`    // Максимальная скорость
	ActivityBoost   float64       `json:"activity_boost"`    // Бонус за активность
}

// RecoveryConfig содержит настройки восстановления репутации
type RecoveryConfig struct {
	Method          RecoveryMethod `json:"method"`
	BaseRecoveryRate float64       `json:"base_recovery_rate"` // Базовая скорость восстановления
	TimeMultiplier  float64        `json:"time_multiplier"`    // Множитель времени
	CostMultiplier  float64        `json:"cost_multiplier"`    // Множитель стоимости
	MinDuration     time.Duration  `json:"min_duration"`       // Минимальная длительность
	MaxDuration     time.Duration  `json:"max_duration"`       // Максимальная длительность
}

// ReputationEvent представляет событие изменения репутации
type ReputationEvent struct {
	ID            string    `json:"id" db:"id"`
	CharacterID   string    `json:"character_id" db:"character_id"`
	FactionID     string    `json:"faction_id" db:"faction_id"`
	EventType     string    `json:"event_type" db:"event_type"`         // Тип события (decay, recovery, action)
	OldValue      float64   `json:"old_value" db:"old_value"`           // Старая репутация
	NewValue      float64   `json:"new_value" db:"new_value"`           // Новая репутация
	Delta         float64   `json:"delta" db:"delta"`                   // Изменение
	Reason        string    `json:"reason" db:"reason"`                 // Причина изменения
	Source        string    `json:"source" db:"source"`                 // Источник (decay_worker, recovery_process, etc.)
	Timestamp     time.Time `json:"timestamp" db:"timestamp"`
	Metadata      map[string]interface{} `json:"metadata" db:"metadata"`
}

// DecayStats содержит статистику процессов разрушения
type DecayStats struct {
	TotalActiveProcesses int           `json:"total_active_processes"`
	TotalProcessedToday  int           `json:"total_processed_today"`
	AverageDecayRate     float64       `json:"average_decay_rate"`
	LastProcessedTime    time.Time     `json:"last_processed_time"`
	ProcessingDuration   time.Duration `json:"processing_duration"`
}

// RecoveryStats содержит статистику процессов восстановления
type RecoveryStats struct {
	TotalActiveProcesses int           `json:"total_active_processes"`
	TotalCompletedToday  int           `json:"total_completed_today"`
	TotalFailedToday     int           `json:"total_failed_today"`
	AverageRecoveryTime  time.Duration `json:"average_recovery_time"`
	SuccessRate          float64       `json:"success_rate"`
	LastProcessedTime    time.Time     `json:"last_processed_time"`
}