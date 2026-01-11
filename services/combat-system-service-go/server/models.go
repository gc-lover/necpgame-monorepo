//go:align 64
// Issue: #2293

package server

import (
	"fmt"
	"time"
)

// DamageCalculation contains damage calculation state
// PERFORMANCE: Struct aligned for memory efficiency (float64 first for SIMD)
type DamageCalculation struct {
	// Core damage values
	BaseDamage          float64
	ModifiedDamage      float64
	AbilityDamage       float64
	EnvironmentalDamage float64
	BalanceAdjustedDamage float64
	FinalDamage         float64

	// Multipliers and modifiers
	CriticalMultiplier  float64
	ArmorReduction      float64
	DamageModifier      float64

	// Combat context
	AttackerID          string
	DefenderID          string
	AbilityID           string
	CombatSessionID     string
	WeatherCondition    string
	LocationType        string

	// Timing
	Timestamp           time.Time
	CalculationID       string

	// Advanced mechanics
	AbilitySynergies    []AbilitySynergy
	ComboBonuses        []ComboBonus
	ResourceCosts       map[string]float64

	// Performance tracking
	ProcessingTime      time.Duration

	// Padding for alignment
	_pad [64]byte
}

// AbilitySynergy represents ability combination effects
type AbilitySynergy struct {
	PrimaryAbility   string
	SecondaryAbility string
	SynergyMultiplier float64
	SynergyType       string
}

// ComboBonus represents combo system bonuses
type ComboBonus struct {
	ComboCount     int
	BonusMultiplier float64
	BonusType       string
}

// Reset clears damage calculation state for reuse
func (dc *DamageCalculation) Reset() {
	dc.BaseDamage = 0
	dc.ModifiedDamage = 0
	dc.AbilityDamage = 0
	dc.EnvironmentalDamage = 0
	dc.BalanceAdjustedDamage = 0
	dc.FinalDamage = 0
	dc.CriticalMultiplier = 1.0
	dc.ArmorReduction = 0
	dc.DamageModifier = 1.0
	dc.AbilityID = ""
	dc.CombatSessionID = ""
	dc.WeatherCondition = ""
	dc.LocationType = ""
	dc.AbilitySynergies = dc.AbilitySynergies[:0]
	dc.ComboBonuses = dc.ComboBonuses[:0]
	if dc.ResourceCosts != nil {
		for k := range dc.ResourceCosts {
			delete(dc.ResourceCosts, k)
		}
	}
	dc.ProcessingTime = 0
}

// CombatEvent represents combat event for analytics
type CombatEvent struct {
	EventID       string    `json:"event_id"`
	EventType     string    `json:"event_type"`
	PlayerID      string    `json:"player_id"`
	TargetID      string    `json:"target_id"`
	AbilityID     string    `json:"ability_id"`
	DamageDealt   float64   `json:"damage_dealt"`
	DamageTaken   float64   `json:"damage_taken"`
	IsCritical    bool      `json:"is_critical"`
	IsKill        bool      `json:"is_kill"`
	CombatSession string    `json:"combat_session"`
	Timestamp     time.Time `json:"timestamp"`
	Location      string    `json:"location"`
}

// PlayerCombatStats represents player combat statistics
type PlayerCombatStats struct {
	PlayerID        string    `json:"player_id"`
	TotalDamageDealt float64   `json:"total_damage_dealt"`
	TotalDamageTaken float64   `json:"total_damage_taken"`
	Kills           int       `json:"kills"`
	Deaths          int       `json:"deaths"`
	Assists         int       `json:"assists"`
	CombatRating    float64   `json:"combat_rating"`
	LastCombatTime  time.Time `json:"last_combat_time"`
}

// AbilityCooldown represents ability cooldown state
type AbilityCooldown struct {
	AbilityID     string    `json:"ability_id"`
	PlayerID      string    `json:"player_id"`
	RemainingMs   int       `json:"remaining_ms"`
	TotalCooldown int       `json:"total_cooldown"`
	LastUsed      time.Time `json:"last_used"`
}

// Error definitions
var (
	ErrVersionConflict = fmt.Errorf("version conflict")
	ErrNotFound        = fmt.Errorf("not found")
	ErrTimeout         = fmt.Errorf("operation timeout")
	ErrInvalidInput    = fmt.Errorf("invalid input")
)