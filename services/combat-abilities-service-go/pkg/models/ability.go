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
