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
	BaseDamageMultiplier     float64               `json:"base_damage_multiplier" db:"base_damage_multiplier"`
	CriticalHitMultiplier    float64               `json:"critical_hit_multiplier" db:"critical_hit_multiplier"`
	ArmorReductionFactor     float64               `json:"armor_reduction_factor" db:"armor_reduction_factor"`
	EnvironmentalModifiers   EnvironmentalModifiers `json:"environmental_modifiers,omitempty" db:"environmental_modifiers"`
}

//go:align 64
type EnvironmentalModifiers struct {
	WeatherDamageModifier float64 `json:"weather_damage_modifier,omitempty" db:"weather_damage_modifier"`
	TimeOfDayModifier     float64 `json:"time_of_day_modifier,omitempty" db:"time_of_day_modifier"`
}

//go:align 64
type CombatMechanics struct {
	TurnBasedEnabled    bool                  `json:"turn_based_enabled" db:"turn_based_enabled"`
	ActionPointsSystem  ActionPointsSystem    `json:"action_points_system" db:"action_points_system"`
	InterruptionRules   InterruptionRules     `json:"interruption_rules" db:"interruption_rules"`
	CooldownSystem      CooldownSystem        `json:"cooldown_system" db:"cooldown_system"`
	TargetingRules      TargetingRules        `json:"targeting_rules" db:"targeting_rules"`
}

//go:align 64
type ActionPointsSystem struct {
	MaxActionPoints int `json:"max_action_points" db:"max_action_points"`
	PointsPerTurn   int `json:"points_per_turn" db:"points_per_turn"`
}

//go:align 64
type InterruptionRules struct {
	InterruptionEnabled bool    `json:"interruption_enabled" db:"interruption_enabled"`
	InterruptionChance  float64 `json:"interruption_chance" db:"interruption_chance"`
}

//go:align 64
type CooldownSystem struct {
	GlobalCooldownMs int `json:"global_cooldown_ms" db:"global_cooldown_ms"`
	CooldownReduction float64 `json:"cooldown_reduction" db:"cooldown_reduction"`
}

//go:align 64
type TargetingRules struct {
	MaxTargets       int  `json:"max_targets" db:"max_targets"`
	AreaOfEffect     bool `json:"area_of_effect" db:"area_of_effect"`
	LineOfSight       bool `json:"line_of_sight" db:"line_of_sight"`
}

//go:align 64
type BalanceParameters struct {
	DifficultyScaling DifficultyScaling `json:"difficulty_scaling" db:"difficulty_scaling"`
	ClassBalance      ClassBalance      `json:"class_balance" db:"class_balance"`
	LevelScaling      LevelScaling      `json:"level_scaling" db:"level_scaling"`
}

//go:align 64
type DifficultyScaling struct {
	EasyMultiplier   float64 `json:"easy_multiplier" db:"easy_multiplier"`
	NormalMultiplier float64 `json:"normal_multiplier" db:"normal_multiplier"`
	HardMultiplier   float64 `json:"hard_multiplier" db:"hard_multiplier"`
}

//go:align 64
type ClassBalance struct {
	DPSBalance     float64 `json:"dps_balance" db:"dps_balance"`
	TankBalance    float64 `json:"tank_balance" db:"tank_balance"`
	HealerBalance  float64 `json:"healer_balance" db:"healer_balance"`
	SupportBalance float64 `json:"support_balance" db:"support_balance"`
}

//go:align 64
type LevelScaling struct {
	MinLevel int     `json:"min_level" db:"min_level"`
	MaxLevel int     `json:"max_level" db:"max_level"`
	ScalingFactor float64 `json:"scaling_factor" db:"scaling_factor"`
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