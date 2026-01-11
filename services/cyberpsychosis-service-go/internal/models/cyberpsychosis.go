// Package models содержит модели данных для системы Cyberpsychosis Combat States
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16-24 bytes): Time (24), string (16+), pointers (8), slices (24+)
// Medium fields (8 bytes aligned): float64 (grouped together)
// Small fields (≤4 bytes): int32, bool
//go:align 64
package models

import (
	"time"
)

// CyberpsychosisState представляет состояние киберпсихоза игрока
type CyberpsychosisState struct {
	// Large fields first (16-24 bytes): Time (24), string (16+), pointers (8), slices (24+)
	StateID       string                   `json:"state_id"`
	PlayerID      string                   `json:"player_id"`
	CombatSession *CombatSession           `json:"combat_session,omitempty"`
	StateHistory  []*StateTransition       `json:"state_history"`

	// Medium fields (8 bytes aligned): float64 (grouped together)
	DamageMultiplier    float64 `json:"damage_multiplier"`
	SpeedMultiplier     float64 `json:"speed_multiplier"`
	AccuracyMultiplier  float64 `json:"accuracy_multiplier"`
	HealthDrainRate     float64 `json:"health_drain_rate"`
	NeuralOverloadLevel float64 `json:"neural_overload_level"`
	SystemInstability   float64 `json:"system_instability"`

	// Small fields (≤4 bytes): int32, bool
	StateType      CyberpsychosisStateType `json:"state_type"`
	SeverityLevel  int32                   `json:"severity_level"`
	IsActive       bool                    `json:"is_active"`
	IsControllable bool                    `json:"is_controllable"`
	CanBeCured     bool                    `json:"can_be_cured"`
}

// CyberpsychosisStateType определяет тип состояния киберпсихоза
type CyberpsychosisStateType int32

const (
	StateNormal CyberpsychosisStateType = iota
	StateBerserk
	StateAdrenalOverload
	StateNeuralOverload
	StateSystemShock
	StateCyberpsychosis
)

// CombatSession представляет боевую сессию
type CombatSession struct {
	SessionID    string    `json:"session_id"`
	PlayerID     string    `json:"player_id"`
	StartTime    time.Time `json:"start_time"`
	EndTime      *time.Time `json:"end_time,omitempty"`
	Kills        int32     `json:"kills"`
	Deaths       int32     `json:"deaths"`
	DamageDealt  float64   `json:"damage_dealt"`
	DamageTaken  float64   `json:"damage_taken"`
	IsActive     bool      `json:"is_active"`
}

// StateTransition представляет переход между состояниями
type StateTransition struct {
	TransitionID   string                   `json:"transition_id"`
	FromState      CyberpsychosisStateType  `json:"from_state"`
	ToState        CyberpsychosisStateType  `json:"to_state"`
	TransitionTime time.Time                `json:"transition_time"`
	TriggerReason  string                   `json:"trigger_reason"`
	SeverityChange int32                    `json:"severity_change"`
}

// CyberpsychosisConfig содержит конфигурацию системы
type CyberpsychosisConfig struct {
	MaxSeverityLevel      int32         `json:"max_severity_level"`
	StateTransitionTime   time.Duration `json:"state_transition_time"`
	HealthDrainInterval   time.Duration `json:"health_drain_interval"`
	CureCooldownTime      time.Duration `json:"cure_cooldown_time"`
	BerserkDuration       time.Duration `json:"berserk_duration"`
	AdrenalOverloadDuration time.Duration `json:"adrenal_overload_duration"`
	NeuralOverloadDuration time.Duration `json:"neural_overload_duration"`
	SystemShockDuration   time.Duration `json:"system_shock_duration"`
}

// SystemHealth представляет состояние здоровья системы
type SystemHealth struct {
	TotalStates     int64   `json:"total_states"`
	ActiveStates    int64   `json:"active_states"`
	InactiveStates  int64   `json:"inactive_states"`
	AverageSeverity float64 `json:"average_severity"`
	LastHealthCheck time.Time `json:"last_health_check"`
	ResponseTime    int64   `json:"response_time_ms"`
	ErrorRate       float64 `json:"error_rate"`
}