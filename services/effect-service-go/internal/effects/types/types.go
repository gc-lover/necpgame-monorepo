// Issue: #143577551
// Package types defines core data structures for weapon elemental effects system
package types

import (
	"time"
)

// Issue: #143577551
// ElementalType represents different elemental damage types
type ElementalType string

const (
	ElementalTypeFire     ElementalType = "fire"
	ElementalTypeIce      ElementalType = "ice"
	ElementalTypeElectric ElementalType = "electric"
	ElementalTypeAcid     ElementalType = "acid"
	ElementalTypePoison   ElementalType = "poison"
	ElementalTypeVoid     ElementalType = "void"
)

// Issue: #143577551
// ElementalEffect represents a single elemental effect instance
type ElementalEffect struct {
	ID               string        `json:"id" db:"id"`
	EffectID         string        `json:"effect_id" db:"effect_id"`
	Name             string        `json:"name" db:"name"`
	Description      string        `json:"description" db:"description"`
	ElementType      ElementalType `json:"element_type" db:"element_type"`
	BaseDamageMod    float64       `json:"base_damage_modifier" db:"base_damage_modifier"`
	DamageOverTime   bool          `json:"damage_over_time" db:"damage_over_time"`
	DotDamage        int           `json:"dot_damage" db:"dot_damage"`
	DotDuration      float64       `json:"dot_duration" db:"dot_duration"`
	DotTicks         int           `json:"dot_ticks" db:"dot_ticks"`
	StatusEffect     string        `json:"status_effect" db:"status_effect"`
	ResistancePen    float64       `json:"resistance_penalty" db:"resistance_penalty"`
	VisualEffect     string        `json:"visual_effect" db:"visual_effect"`
	SoundEffect      string        `json:"sound_effect" db:"sound_effect"`
	IsActive         bool          `json:"is_active" db:"is_active"`
	CreatedAt        time.Time     `json:"created_at" db:"created_at"`
	UpdatedAt        time.Time     `json:"updated_at" db:"updated_at"`
}

// Issue: #143577551
// ActiveEffect represents an active effect applied to a target
type ActiveEffect struct {
	ID            string                 `json:"id"`
	EffectID      string                 `json:"effect_id"`
	TargetID      string                 `json:"target_id"`
	SourceID      string                 `json:"source_id"`
	AppliedAt     time.Time              `json:"applied_at"`
	ExpiresAt     *time.Time             `json:"expires_at,omitempty"`
	Duration      float64                `json:"duration"`
	Stacks        int                    `json:"stacks"`
	MaxStacks     int                    `json:"max_stacks"`
	DamageDealt   int                    `json:"damage_dealt"`
	TicksApplied  int                    `json:"ticks_applied"`
	Metadata      map[string]interface{} `json:"metadata"`
	IsExpired     bool                   `json:"is_expired"`
}

// Issue: #143577551
// EffectApplicationRequest represents a request to apply an elemental effect
type EffectApplicationRequest struct {
	WeaponID     string                 `json:"weapon_id"`
	TargetID     string                 `json:"target_id"`
	AttackerID   string                 `json:"attacker_id"`
	EffectID     string                 `json:"effect_id"`
	Intensity    float64                `json:"intensity"`
	Duration     float64                `json:"duration"`
	Metadata     map[string]interface{} `json:"metadata"`
	Environment  EnvironmentContext     `json:"environment"`
}

// Issue: #143577551
// EffectApplicationResponse represents the result of applying an effect
type EffectApplicationResponse struct {
	Success         bool             `json:"success"`
	ActiveEffectID  string           `json:"active_effect_id,omitempty"`
	DamageDealt     int              `json:"damage_dealt"`
	StatusEffect    string           `json:"status_effect,omitempty"`
	Interactions    []Interaction    `json:"interactions,omitempty"`
	Error           string           `json:"error,omitempty"`
}

// Issue: #143577551
// Interaction represents an interaction between elemental effects
type Interaction struct {
	Type           InteractionType `json:"type"`
	PrimaryEffect  string          `json:"primary_effect"`
	SecondaryEffect string         `json:"secondary_effect"`
	Multiplier      float64         `json:"multiplier"`
	Description     string          `json:"description"`
	SpecialEffects  []string        `json:"special_effects,omitempty"`
}

// Issue: #143577551
// InteractionType defines types of elemental interactions
type InteractionType string

const (
	InteractionTypeSynergy     InteractionType = "synergy"      // усиление эффектов
	InteractionTypeConflict    InteractionType = "conflict"     // ослабление эффектов
	InteractionTypeNeutralize  InteractionType = "neutralize"   // нейтрализация
	InteractionTypeAmplify     InteractionType = "amplify"      // усиление
	InteractionTypeTransform   InteractionType = "transform"    // трансформация
)

// Issue: #143577551
// EnvironmentContext represents environmental factors affecting effects
type EnvironmentContext struct {
	Temperature    float64 `json:"temperature"`    // -50 to +50 Celsius
	Humidity       float64 `json:"humidity"`       // 0 to 100 percent
	Pressure       float64 `json:"pressure"`       // atmospheric pressure
	Radiation      float64 `json:"radiation"`      // radiation level
	ElectricField  float64 `json:"electric_field"` // electric field strength
	MagneticField  float64 `json:"magnetic_field"` // magnetic field strength
	WeatherType    string  `json:"weather_type"`   // rain, snow, storm, etc.
	TerrainType    string  `json:"terrain_type"`   // urban, desert, forest, etc.
	TimeOfDay      string  `json:"time_of_day"`    // day, night, dawn, dusk
}

// Issue: #143577551
// DamageCalculationRequest represents a request to calculate elemental damage
type DamageCalculationRequest struct {
	BaseDamage     int              `json:"base_damage"`
	ElementType    ElementalType    `json:"element_type"`
	TargetResist   float64          `json:"target_resistance"`
	Environment    EnvironmentContext `json:"environment"`
	ActiveEffects  []ActiveEffect   `json:"active_effects"`
	WeaponMods     []WeaponModifier `json:"weapon_modifiers"`
}

// Issue: #143577551
// DamageCalculationResponse represents calculated damage result
type DamageCalculationResponse struct {
	TotalDamage    int                    `json:"total_damage"`
	ElementalDamage int                   `json:"elemental_damage"`
	DotDamage      int                    `json:"dot_damage"`
	StatusEffects  []string               `json:"status_effects"`
	Breakdown      DamageBreakdown        `json:"breakdown"`
}

// Issue: #143577551
// DamageBreakdown provides detailed damage calculation breakdown
type DamageBreakdown struct {
	BaseDamage         int               `json:"base_damage"`
	ElementalModifier  float64           `json:"elemental_modifier"`
	ResistancePenalty  float64           `json:"resistance_penalty"`
	EnvironmentalMod   float64           `json:"environmental_modifier"`
	InteractionMods    []InteractionMod  `json:"interaction_modifiers"`
}

// Issue: #143577551
// InteractionMod represents a single interaction modifier
type InteractionMod struct {
	Effect       string  `json:"effect"`
	Type         string  `json:"type"`
	Multiplier   float64 `json:"multiplier"`
	Description  string  `json:"description"`
}

// Issue: #143577551
// WeaponModifier represents weapon-specific modifiers
type WeaponModifier struct {
	Type       string  `json:"type"`
	Value      float64 `json:"value"`
	IsPercent  bool    `json:"is_percent"`
	Description string `json:"description"`
}

// Issue: #143577551
// EffectProcessor interface defines the contract for effect processors
type EffectProcessor interface {
	GetElementType() ElementalType
	ProcessEffect(ctx EffectApplicationRequest) (EffectApplicationResponse, error)
	CalculateDamage(req DamageCalculationRequest) DamageCalculationResponse
	GetInteractions(targetEffects []ActiveEffect) []Interaction
}

// Issue: #143577551
// InteractionCalculator interface defines the contract for interaction calculations
type InteractionCalculator interface {
	CalculateInteractions(effects []ActiveEffect, environment EnvironmentContext) []Interaction
	GetSynergyMultiplier(primary, secondary ElementalType) float64
	GetConflictMultiplier(primary, secondary ElementalType) float64
}
