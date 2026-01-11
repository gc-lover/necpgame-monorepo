//go:align 64
package models

import (
	"time"

	"github.com/google/uuid"
)

//go:align 64
type CombatSystemRules struct {
	ID               uuid.UUID         `json:"id" db:"id"`
	Version          int               `json:"version" db:"version"`
	DamageRules      DamageRules       `json:"damage_rules" db:"damage_rules"`
	CombatMechanics  CombatMechanics   `json:"combat_mechanics" db:"combat_mechanics"`
	BalanceParameters BalanceParameters `json:"balance_parameters" db:"balance_parameters"`
	CreatedAt        time.Time         `json:"created_at" db:"created_at"`
	UpdatedAt        time.Time         `json:"updated_at" db:"updated_at"`
	CreatedBy        string            `json:"created_by,omitempty" db:"created_by"`
}

//go:align 64
type DamageRules struct {
	BaseDamageMultiplier     float32               `json:"base_damage_multiplier" db:"base_damage_multiplier"`
	CriticalHitMultiplier    float32               `json:"critical_hit_multiplier" db:"critical_hit_multiplier"`
	ArmorReductionFactor     float32               `json:"armor_reduction_factor" db:"armor_reduction_factor"`
	EnvironmentalModifiers   EnvironmentalModifiers `json:"environmental_modifiers,omitempty" db:"environmental_modifiers"`
}

//go:align 64
type EnvironmentalModifiers struct {
	WeatherDamageModifier float32 `json:"weather_damage_modifier,omitempty" db:"weather_damage_modifier"`
	TimeOfDayModifier     float32 `json:"time_of_day_modifier,omitempty" db:"time_of_day_modifier"`
}

//go:align 64
type CombatMechanics struct {
	TurnBasedEnabled   bool              `json:"turn_based_enabled" db:"turn_based_enabled"`
	ActionPointsSystem ActionPointsSystem `json:"action_points_system" db:"action_points_system"`
	InterruptionRules  InterruptionRules `json:"interruption_rules" db:"interruption_rules"`
}

//go:align 64
type ActionPointsSystem struct {
	MaxActionPoints int `json:"max_action_points" db:"max_action_points"`
	PointsPerTurn   int `json:"points_per_turn" db:"points_per_turn"`
}

//go:align 64
type InterruptionRules struct {
	AllowInterruptions   bool `json:"allow_interruptions" db:"allow_interruptions"`
	InterruptionPenalty  int  `json:"interruption_penalty" db:"interruption_penalty"`
}

//go:align 64
type BalanceParameters struct {
	DifficultyScaling DifficultyScaling `json:"difficulty_scaling" db:"difficulty_scaling"`
	PlayerAdvantages  PlayerAdvantages  `json:"player_advantages" db:"player_advantages"`
	NPCMModifiers     NPCMModifiers     `json:"npc_modifiers" db:"npc_modifiers"`
}

//go:align 64
type DifficultyScaling struct {
	ScalingFactor           float32 `json:"scaling_factor" db:"scaling_factor"`
	LevelDifferenceModifier float32 `json:"level_difference_modifier" db:"level_difference_modifier"`
}

//go:align 64
type PlayerAdvantages struct {
	FirstStrikeBonus    float32 `json:"first_strike_bonus" db:"first_strike_bonus"`
	PositionalAdvantage float32 `json:"positional_advantage" db:"positional_advantage"`
}

//go:align 64
type NPCMModifiers struct {
	EliteMultiplier float32 `json:"elite_multiplier" db:"elite_multiplier"`
	BossMultiplier  float32 `json:"boss_multiplier" db:"boss_multiplier"`
}

//go:align 64
type DamageCalculationRequest struct {
	AttackerID     uuid.UUID         `json:"attacker_id" db:"attacker_id"`
	DefenderID     uuid.UUID         `json:"defender_id" db:"defender_id"`
	AbilityID      uuid.UUID         `json:"ability_id" db:"ability_id"`
	BaseDamage     int               `json:"base_damage" db:"base_damage"`
	DamageType     string            `json:"damage_type" db:"damage_type"`
	AttackerStats  CharacterStats    `json:"attacker_stats" db:"attacker_stats"`
	DefenderStats  CharacterStats    `json:"defender_stats" db:"defender_stats"`
	EnvironmentalFactors EnvironmentalFactors `json:"environmental_factors,omitempty" db:"environmental_factors"`
}

//go:align 64
type CharacterStats struct {
	Level         int     `json:"level" db:"level"`
	Strength      int     `json:"strength" db:"strength"`
	Agility       int     `json:"agility" db:"agility"`
	Intelligence  int     `json:"intelligence" db:"intelligence"`
	Armor         int     `json:"armor" db:"armor"`
	CriticalChance float64 `json:"critical_chance" db:"critical_chance"`
}

//go:align 64
type EnvironmentalFactors struct {
	Weather     string  `json:"weather,omitempty" db:"weather"`
	TimeOfDay   string  `json:"time_of_day,omitempty" db:"time_of_day"`
	TerrainType string  `json:"terrain_type,omitempty" db:"terrain_type"`
	Modifier    float64 `json:"modifier,omitempty" db:"modifier"`
}

//go:align 64
type DamageCalculationResponse struct {
	FinalDamage     int               `json:"final_damage" db:"final_damage"`
	CriticalHit     bool              `json:"critical_hit" db:"critical_hit"`
	DamageType      string            `json:"damage_type" db:"damage_type"`
	Modifiers       []DamageModifier  `json:"modifiers" db:"modifiers"`
	CalculationLog  []string          `json:"calculation_log,omitempty" db:"calculation_log"`
}

//go:align 64
type DamageModifier struct {
	Type        string  `json:"type" db:"type"`
	Value       float64 `json:"value" db:"value"`
	Description string  `json:"description,omitempty" db:"description"`
}

//go:align 64
type CombatBalanceConfig struct {
	ID                    uuid.UUID         `json:"id" db:"id"`
	Version               int               `json:"version" db:"version"`
	GlobalMultipliers    GlobalMultipliers `json:"global_multipliers" db:"global_multipliers"`
	CharacterBalance      CharacterBalance  `json:"character_balance" db:"character_balance"`
	EnvironmentalBalance  EnvironmentalBalance `json:"environmental_balance" db:"environmental_balance"`
	CreatedAt             time.Time         `json:"created_at" db:"created_at"`
	UpdatedAt             time.Time         `json:"updated_at" db:"updated_at"`
}

//go:align 64
type GlobalMultipliers struct {
	DamageMultiplier  float64 `json:"damage_multiplier" db:"damage_multiplier"`
	HealingMultiplier float64 `json:"healing_multiplier" db:"healing_multiplier"`
	CooldownMultiplier float64 `json:"cooldown_multiplier" db:"cooldown_multiplier"`
}

//go:align 64
type CharacterBalance struct {
	LevelScalingEnabled bool              `json:"level_scaling_enabled" db:"level_scaling_enabled"`
	ClassMultipliers   map[string]float64 `json:"class_multipliers" db:"class_multipliers"`
	StatWeights         map[string]float64 `json:"stat_weights" db:"stat_weights"`
}

//go:align 64
type EnvironmentalBalance struct {
	WeatherEffects   map[string]float64 `json:"weather_effects" db:"weather_effects"`
	TerrainModifiers map[string]float64 `json:"terrain_modifiers" db:"terrain_modifiers"`
	TimeOfDayEffects map[string]float64 `json:"time_of_day_effects" db:"time_of_day_effects"`
}

//go:align 64
type AbilityConfiguration struct {
	ID                uuid.UUID         `json:"id" db:"id"`
	Name              string            `json:"name" db:"name"`
	Type              string            `json:"type" db:"type"`
	Description       string            `json:"description,omitempty" db:"description"`
	Damage            int               `json:"damage,omitempty" db:"damage"`
	CooldownMs        int               `json:"cooldown_ms" db:"cooldown_ms"`
	ManaCost          int               `json:"mana_cost,omitempty" db:"mana_cost"`
	Range             int               `json:"range,omitempty" db:"range"`
	CastTimeMs        int               `json:"cast_time_ms,omitempty" db:"cast_time_ms"`
	BalanceNotes      string            `json:"balance_notes,omitempty" db:"balance_notes"`
	StatRequirements  map[string]int    `json:"stat_requirements" db:"stat_requirements"`
	Effects           []AbilityEffect   `json:"effects" db:"effects"`
	CreatedAt         time.Time         `json:"created_at" db:"created_at"`
	UpdatedAt         time.Time         `json:"updated_at" db:"updated_at"`
}

//go:align 64
type AbilityEffect struct {
	Type        string                 `json:"type" db:"type"`
	DurationMs  int                    `json:"duration_ms,omitempty" db:"duration_ms"`
	Value       interface{}            `json:"value" db:"value"`
	Target      string                 `json:"target" db:"target"`
}

//go:align 64
type SystemHealth struct {
	TotalCombatCalculations int64 `json:"total_combat_calculations"`
	ActiveCombatSessions    int64 `json:"active_combat_sessions"`
	TotalAbilities          int64 `json:"total_abilities"`
	ActiveBalanceConfigs    int64 `json:"active_balance_configs"`
}