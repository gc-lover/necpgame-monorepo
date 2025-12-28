package models

import (
	"time"

	"github.com/google/uuid"
)

type AbilityType string

const (
	AbilityTypeOffensive AbilityType = "OFFENSIVE"
	AbilityTypeDefensive AbilityType = "DEFENSIVE"
	AbilityTypeUtility   AbilityType = "UTILITY"
	AbilityTypeMobility  AbilityType = "MOBILITY"
)

type DamageType string

const (
	DamageTypePhysical DamageType = "PHYSICAL"
	DamageTypeEnergy   DamageType = "ENERGY"
	DamageTypeChemical DamageType = "CHEMICAL"
	DamageTypeThermal  DamageType = "THERMAL"
)

type AbilitySlot string

const (
	AbilitySlotQ AbilitySlot = "Q" // Tactical
	AbilitySlotE AbilitySlot = "E" // Signature
	AbilitySlotR AbilitySlot = "R" // Ultimate
	AbilitySlotP AbilitySlot = "P" // Passive
	AbilitySlotH AbilitySlot = "H" // Hacking
)

type Ability struct {
	ID               uuid.UUID         `json:"id"`
	Name             string            `json:"name"`
	Description      string            `json:"description"`
	Type             AbilityType       `json:"type"`
	DamageType       DamageType        `json:"damage_type"`
	Slot             AbilitySlot       `json:"slot"`
	CooldownMs       int               `json:"cooldown_ms"`
	ResourceCost     ResourceCost      `json:"resource_cost"`
	Range            float64           `json:"range"`
	AreaOfEffect     float64           `json:"area_of_effect"`
	LevelRequirement int               `json:"level_requirement"`
	Damage           int               `json:"damage"`
	Healing          int               `json:"healing"`
	StatusEffects    []StatusEffect    `json:"status_effects"`
	Synergies        []Synergy         `json:"synergies"`
	Metadata         map[string]string `json:"metadata"`
	CreatedAt        time.Time         `json:"created_at"`
	UpdatedAt        time.Time         `json:"updated_at"`
}

type ResourceCost struct {
	Health   int `json:"health"`
	Stamina  int `json:"stamina"`
	Mana     int `json:"mana"`
	Energy   int `json:"energy"`
}

type StatusEffect struct {
	Type     string  `json:"type"`
	Duration int     `json:"duration_ms"`
	Strength float64 `json:"strength"`
}

type Synergy struct {
	PartnerAbilityID uuid.UUID `json:"partner_ability_id"`
	Type             string    `json:"type"`
	BonusMultiplier  float64   `json:"bonus_multiplier"`
	Condition        string    `json:"condition"`
}

// ADVANCED COMBAT SYSTEM ENHANCEMENTS
// Issue: #2219 - Combat System Enhancement - Advanced Combos & Synergies

// ComboChain represents a sequence of abilities that create combo effects
type ComboChain struct {
	ID          uuid.UUID       `json:"id"`
	Name        string          `json:"name"`
	Description string          `json:"description"`
	Abilities   []ComboStep     `json:"abilities"`     // Sequence of abilities
	MaxTimeGap  int             `json:"max_time_gap"`  // Max time between steps (ms)
	BonusEffects []ComboEffect  `json:"bonus_effects"` // Effects when combo completes
	Difficulty  string          `json:"difficulty"`    // "easy", "medium", "hard", "expert"
	UnlockLevel int             `json:"unlock_level"`
	IsActive    bool            `json:"is_active"`
	UsageCount  int             `json:"usage_count"`
	SuccessRate float64         `json:"success_rate"`
}

// ComboStep represents a single step in a combo chain
type ComboStep struct {
	AbilityID    uuid.UUID              `json:"ability_id"`
	Order        int                    `json:"order"`         // Position in chain
	TimeWindow   int                    `json:"time_window"`   // Time to execute this step (ms)
	PositionReq  ComboPositionReq       `json:"position_req"`  // Position requirements
	StateReq     ComboStateReq          `json:"state_req"`     // State requirements
	VisualCue    string                 `json:"visual_cue"`    // UI visual indicator
	AudioCue     string                 `json:"audio_cue"`     // Audio feedback
}

// ComboPositionReq represents position requirements for combo step
type ComboPositionReq struct {
	DistanceToTarget *FloatRange `json:"distance_to_target,omitempty"`
	AngleToTarget    *FloatRange `json:"angle_to_target,omitempty"`
	MovementSpeed    *FloatRange `json:"movement_speed,omitempty"`
	TerrainType      []string    `json:"terrain_type,omitempty"` // "ground", "air", "wall", "ceiling"
}

// ComboStateReq represents state requirements for combo step
type ComboStateReq struct {
	HealthPercent    *FloatRange         `json:"health_percent,omitempty"`
	EnergyPercent    *FloatRange         `json:"energy_percent,omitempty"`
	StatusEffects    []string            `json:"status_effects,omitempty"`    // Required status effects
	ForbiddenEffects []string            `json:"forbidden_effects,omitempty"` // Forbidden status effects
	WeaponType       []string            `json:"weapon_type,omitempty"`       // Required weapon types
	AbilityCooldowns map[string]bool      `json:"ability_cooldowns,omitempty"` // true = must be off cooldown
}

// FloatRange represents a range of float values
type FloatRange struct {
	Min float64 `json:"min"`
	Max float64 `json:"max"`
}

// ComboEffect represents bonus effects from completed combo
type ComboEffect struct {
	Type        string                 `json:"type"`        // "damage", "healing", "status", "resource", "special"
	Value       interface{}            `json:"value"`       // Effect value
	Target      string                 `json:"target"`      // "self", "target", "area", "allies"
	Duration    int                    `json:"duration"`    // Effect duration (ms)
	Conditions  []string               `json:"conditions"`  // Activation conditions
	Scaling     ComboScaling           `json:"scaling"`     // How effect scales
}

// ComboScaling represents how combo effects scale
type ComboScaling struct {
	LevelMultiplier    float64 `json:"level_multiplier"`    // Scales with character level
	ComboLengthBonus   float64 `json:"combo_length_bonus"`  // Bonus per additional combo step
	TimeBonus          float64 `json:"time_bonus"`          // Bonus for faster execution
	DifficultyBonus    float64 `json:"difficulty_bonus"`    // Bonus for harder combos
	MasteryBonus       float64 `json:"mastery_bonus"`       // Bonus for combo mastery
}

// DynamicCombo represents a player-learned combo pattern
type DynamicCombo struct {
	ID              uuid.UUID         `json:"id"`
	PlayerID        uuid.UUID         `json:"player_id"`
	Name            string            `json:"name"`
	Description     string            `json:"description"`
	Steps           []ComboStep       `json:"steps"`
	DiscoveredAt    time.Time         `json:"discovered_at"`
	LastUsedAt      time.Time         `json:"last_used_at"`
	UsageCount      int               `json:"usage_count"`
	SuccessCount    int               `json:"success_count"`
	AverageTime     int               `json:"average_time"`    // Average completion time (ms)
	BestTime        int               `json:"best_time"`       // Best completion time (ms)
	MasteryLevel    int               `json:"mastery_level"`   // 1-10 mastery level
	IsOptimized     bool              `json:"is_optimized"`    // AI-optimized combo
	OptimizationScore float64         `json:"optimization_score"`
	NeuralPattern   string            `json:"neural_pattern"`  // Neural activation pattern
}

// AdvancedSynergy represents complex multi-ability synergies
type AdvancedSynergy struct {
	ID                uuid.UUID             `json:"id"`
	Name              string                `json:"name"`
	Description       string                `json:"description"`
	AbilitySet        []uuid.UUID           `json:"ability_set"`       // Abilities involved
	SynergyType       string                `json:"synergy_type"`      // "elemental", "temporal", "spatial", "neural"
	ActivationReq     SynergyRequirement    `json:"activation_req"`
	Effects           []AdvancedSynergyEffect `json:"effects"`
	CooldownMs        int                   `json:"cooldown_ms"`
	EnergyCost        int                   `json:"energy_cost"`
	DurationMs        int                   `json:"duration_ms"`
	IsActive          bool                  `json:"is_active"`
	ActivationCount   int                   `json:"activation_count"`
	SuccessRate       float64               `json:"success_rate"`
	LastActivatedAt   *time.Time            `json:"last_activated_at"`
}

// SynergyRequirement represents requirements for synergy activation
type SynergyRequirement struct {
	AbilitySequence  []uuid.UUID `json:"ability_sequence"`  // Required ability order
	TimeWindow       int         `json:"time_window"`       // Max time between abilities (ms)
	PositionReq      ComboPositionReq `json:"position_req"`      // Position requirements
	StateReq         ComboStateReq     `json:"state_req"`         // State requirements
	EnvironmentalReq []string    `json:"environmental_req"` // Weather, time of day, etc.
	RarityReq        string      `json:"rarity_req"`        // Required ability rarities
}

// AdvancedSynergyEffect represents complex synergy effects
type AdvancedSynergyEffect struct {
	Type          string                 `json:"type"`           // "chain_reaction", "field_effect", "time_dilation", "neural_boost"
	PrimaryEffect ComboEffect            `json:"primary_effect"`
	ChainEffects  []ChainEffect          `json:"chain_effects"`  // Effects that trigger other effects
	Scaling       AdvancedScaling        `json:"scaling"`
	VisualEffects []VisualEffect         `json:"visual_effects"`
	AudioEffects  []AudioEffect          `json:"audio_effects"`
}

// ChainEffect represents effects that can trigger other effects
type ChainEffect struct {
	TriggerCondition string      `json:"trigger_condition"` // "on_damage", "on_kill", "on_time", "on_position"
	Effect           ComboEffect `json:"effect"`
	ChainProbability float64     `json:"chain_probability"` // Probability of triggering
	MaxChains        int         `json:"max_chains"`         // Maximum chain length
}

// AdvancedScaling represents complex scaling mechanics
type AdvancedScaling struct {
	BaseScaling     ComboScaling       `json:"base_scaling"`
	ExponentialBonus map[string]float64 `json:"exponential_bonus"` // Bonus that grows exponentially
	DiminishingBonus map[string]float64 `json:"diminishing_bonus"` // Bonus that diminishes
	ThresholdBonuses []ThresholdBonus   `json:"threshold_bonuses"` // Bonuses at thresholds
	SynergyStacking  bool              `json:"synergy_stacking"`  // Can stack with other synergies
}

// ThresholdBonus represents bonuses that activate at thresholds
type ThresholdBonus struct {
	Threshold   float64     `json:"threshold"`   // Value threshold
	Metric      string      `json:"metric"`      // What to measure (damage, time, etc.)
	BonusEffect ComboEffect `json:"bonus_effect"`
	IsActive    bool        `json:"is_active"`
}

// VisualEffect represents visual feedback for synergies
type VisualEffect struct {
	Type        string                 `json:"type"`        // "particle", "screen_effect", "model_change", "trail"
	AssetPath   string                 `json:"asset_path"`
	Parameters  map[string]interface{} `json:"parameters"`
	Duration    int                    `json:"duration"`
	Intensity   float64                `json:"intensity"`
}

// AudioEffect represents audio feedback for synergies
type AudioEffect struct {
	Type       string  `json:"type"`       // "sound", "music", "voiceover"
	AudioPath  string  `json:"audio_path"`
	Volume     float64 `json:"volume"`
	Pitch      float64 `json:"pitch"`
	IsLooping  bool    `json:"is_looping"`
	FadeInTime int     `json:"fade_in_time"`
	FadeOutTime int    `json:"fade_out_time"`
}
