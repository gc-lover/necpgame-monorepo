package server

import (
	"time"
)

// OPTIMIZATION: Issue #1936 - Struct field alignment (large → small)
type CombatSession struct {
	CombatID     string                 `json:"combat_id"`     // 16 bytes
	Status       string                 `json:"status"`        // 16 bytes
	CombatType   string                 `json:"combat_type"`   // 16 bytes
	Participants []*CombatParticipant   `json:"participants"`  // 24 bytes (slice)
	CurrentTurn  string                 `json:"current_turn"`  // 16 bytes
	Events       []*CombatEvent         `json:"events"`        // 24 bytes (slice)
	StartTime    time.Time              `json:"start_time"`    // 24 bytes
	TimeLimit    time.Duration          `json:"time_limit"`    // 8 bytes
	Location     string                 `json:"location"`      // 16 bytes
}

// OPTIMIZATION: Issue #1936 - Struct field alignment (large → small)
type CombatParticipant struct {
	CharacterID   string          `json:"character_id"`   // 16 bytes
	Health        int             `json:"health"`         // 8 bytes
	MaxHealth     int             `json:"max_health"`     // 8 bytes
	Position      *Vector3        `json:"position"`       // 8 bytes (pointer)
	StatusEffects []*StatusEffect `json:"status_effects"` // 24 bytes (slice)
	IsAlive       bool            `json:"is_alive"`       // 1 byte
	Team          string          `json:"team"`           // 16 bytes
}

// OPTIMIZATION: Issue #1936 - Memory-aligned Vector3
type Vector3 struct {
	X float64 `json:"x"` // 8 bytes
	Y float64 `json:"y"` // 8 bytes
	Z float64 `json:"z"` // 8 bytes
}

// OPTIMIZATION: Issue #1936 - Struct field alignment (large → small)
type StatusEffect struct {
	EffectID   string                 `json:"effect_id"`   // 16 bytes
	EffectType string                 `json:"effect_type"` // 16 bytes
	Name       string                 `json:"name"`        // 16 bytes
	Description string                 `json:"description"` // 16 bytes
	Duration   int                    `json:"duration"`    // 8 bytes (seconds, -1 for permanent)
	Stacks     int                    `json:"stacks"`      // 8 bytes
	Parameters map[string]interface{} `json:"parameters"`  // 8 bytes (map)
	AppliedAt  time.Time              `json:"applied_at"`  // 24 bytes
}

// OPTIMIZATION: Issue #1936 - Struct field alignment (large → small)
type CombatEvent struct {
	EventID    string                 `json:"event_id"`    // 16 bytes
	EventType  string                 `json:"event_type"`  // 16 bytes
	Timestamp  int64                  `json:"timestamp"`  // 8 bytes
	Data       map[string]interface{} `json:"data"`       // 8 bytes (map)
}

// OPTIMIZATION: Issue #1936 - Struct field alignment (large → small)
type DamageResult struct {
	TotalDamage    int     `json:"total_damage"`    // 8 bytes
	DamageType     string  `json:"damage_type"`     // 16 bytes
	CriticalHit    bool    `json:"critical_hit"`    // 1 byte
	Blocked        bool    `json:"blocked"`         // 1 byte
	Mitigated      int     `json:"mitigated"`       // 8 bytes
	CriticalMultiplier float64 `json:"critical_multiplier"` // 8 bytes
}

// Request structs
type InitiateCombatRequest struct {
	AttackerID string `json:"attacker_id"`
	DefenderID string `json:"defender_id"`
	Location   string `json:"location"`
	CombatType string `json:"combat_type"`
}

type CombatActionRequest struct {
	ActionType string                 `json:"action_type"`
	ActionID   string                 `json:"action_id"`
	TargetID   string                 `json:"target_id"`
	Parameters map[string]interface{} `json:"parameters,omitempty"`
}

type ApplyStatusEffectRequest struct {
	EffectID   string                 `json:"effect_id"`
	SourceID   string                 `json:"source_id"`
	Parameters map[string]interface{} `json:"parameters,omitempty"`
}

type DamageCalculationRequest struct {
	AttackerID string             `json:"attacker_id"`
	DefenderID string             `json:"defender_id"`
	AttackType string             `json:"attack_type"`
	BaseDamage int                `json:"base_damage"`
	Modifiers  []*DamageModifier  `json:"modifiers"`
}

type DamageModifier struct {
	Type   string      `json:"type"`
	Value  interface{} `json:"value"`
	Source string      `json:"source"`
}

// Response structs
type InitiateCombatResponse struct {
	CombatID     string               `json:"combat_id"`
	Status       string               `json:"status"`
	Participants []*CombatParticipant `json:"participants"`
}

type CombatStatusResponse struct {
	CombatID      string            `json:"combat_id"`
	Status        string            `json:"status"`
	Participants  []*CombatParticipant `json:"participants"`
	CurrentTurn   string            `json:"current_turn"`
	TimeRemaining int               `json:"time_remaining"`
	Events        []*CombatEvent    `json:"events"`
}

type CombatActionResponse struct {
	ActionID string        `json:"action_id"`
	Success  bool          `json:"success"`
	Damage   *DamageResult `json:"damage,omitempty"`
	StatusEffects []*StatusEffect `json:"status_effects,omitempty"`
	Cooldown int           `json:"cooldown"`
}

type EndCombatResponse struct {
	CombatID         string       `json:"combat_id"`
	Winner           string       `json:"winner,omitempty"`
	Duration         int          `json:"duration"`
	ExperienceGained int          `json:"experience_gained"`
	Loot             []*LootItem  `json:"loot,omitempty"`
}

type LootItem struct {
	ItemID   string `json:"item_id"`
	Quantity int    `json:"quantity"`
	Rarity   string `json:"rarity"`
}

type DamageCalculationResponse struct {
	FinalDamage        int               `json:"final_damage"`
	DamageType         string            `json:"damage_type"`
	CriticalMultiplier float64           `json:"critical_multiplier"`
	ArmorReduction     int               `json:"armor_reduction"`
	ModifiersApplied   []*DamageModifier `json:"modifiers_applied"`
}

type StatusEffectsResponse struct {
	CharacterID string          `json:"character_id"`
	Effects     []*StatusEffect `json:"effects"`
}

type ApplyStatusEffectResponse struct {
	EffectID string `json:"effect_id"`
	Applied  bool   `json:"applied"`
	Duration int    `json:"duration"`
	Message  string `json:"message"`
}
