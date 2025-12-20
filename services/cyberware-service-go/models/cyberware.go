// Package models contains data models for the Cyberware Service
package models

import (
	"time"
)

// ImplantType represents the type of cyberware implant
type ImplantType string

// ImplantRarity represents implant rarity levels
type ImplantRarity string

// ImplantCategory represents specific implant categories
type ImplantCategory string

// ImplantCatalogItem represents an item in the implant catalog
type ImplantCatalogItem struct {
	ID            string                 `json:"id" db:"id"`
	Name          string                 `json:"name" db:"name"`
	Type          ImplantType            `json:"type" db:"type"`
	Category      ImplantCategory        `json:"category" db:"category"`
	Rarity        ImplantRarity          `json:"rarity" db:"rarity"`
	Description   string                 `json:"description" db:"description"`
	Effects       map[string]interface{} `json:"effects" db:"effects"`
	EnergyCost    int                    `json:"energy_cost" db:"energy_cost"`
	HumanityCost  int                    `json:"humanity_cost" db:"humanity_cost"`
	SlotType      string                 `json:"slot_type" db:"slot_type"`
	Compatibility map[string]interface{} `json:"compatibility" db:"compatibility"`
	MaxLevel      int                    `json:"max_level" db:"max_level"`
	BasePrice     float64                `json:"base_price" db:"base_price"`
	CreatedAt     time.Time              `json:"created_at" db:"created_at"`
}

// CharacterImplant represents an implant installed on a character
type CharacterImplant struct {
	ID           string                 `json:"id" db:"id"`
	CharacterID  string                 `json:"character_id" db:"character_id"`
	ImplantID    string                 `json:"implant_id" db:"implant_id"`
	Name         string                 `json:"name" db:"name"`
	Type         ImplantType            `json:"type" db:"type"`
	Category     ImplantCategory        `json:"category" db:"category"`
	CurrentLevel int                    `json:"current_level" db:"upgrade_level"`
	MaxLevel     int                    `json:"max_level" db:"max_level"`
	Slot         string                 `json:"slot" db:"slot"`
	IsActive     bool                   `json:"is_active" db:"is_active"`
	InstalledAt  *time.Time             `json:"installed_at" db:"installed_at"`
	Effects      map[string]interface{} `json:"effects" db:"effects"`
}

// ImplantAcquisition represents the history of implant acquisitions
type ImplantAcquisition struct {
	ID              string                 `json:"id" db:"id"`
	CharacterID     string                 `json:"character_id" db:"character_id"`
	ImplantID       string                 `json:"implant_id" db:"implant_id"`
	AcquisitionType string                 `json:"acquisition_type" db:"acquisition_type"`
	Cost            map[string]interface{} `json:"cost" db:"cost"`
	AcquiredAt      time.Time              `json:"acquired_at" db:"acquired_at"`
}

// ImplantLimitsState represents the current limits state for a character
type ImplantLimitsState struct {
	ID                string                 `json:"id" db:"id"`
	CharacterID       string                 `json:"character_id" db:"character_id"`
	TotalEnergyUsed   int                    `json:"total_energy_used" db:"total_energy_used"`
	MaxEnergy         int                    `json:"max_energy" db:"max_energy"`
	TotalHumanityLost int                    `json:"total_humanity_lost" db:"total_humanity_lost"`
	MaxHumanity       int                    `json:"max_humanity" db:"max_humanity"`
	SlotsUsed         map[string]interface{} `json:"slots_used" db:"slots_used"`
	LastUpdate        time.Time              `json:"last_update" db:"last_update"`
}

// CyberpsychosisState represents the cyberpsychosis state for a character
type CyberpsychosisState struct {
	ID             string                 `json:"id" db:"id"`
	CharacterID    string                 `json:"character_id" db:"character_id"`
	CurrentLevel   int                    `json:"current_level" db:"current_level"`
	ThresholdLevel int                    `json:"threshold_level" db:"threshold_level"`
	EffectsActive  []CyberpsychosisEffect `json:"effects_active" db:"effects_active"`
	LastUpdate     time.Time              `json:"last_update" db:"last_update"`
}

// CyberpsychosisEffect represents an active cyberpsychosis effect
type CyberpsychosisEffect struct {
	EffectType  string `json:"effect_type"`
	Severity    string `json:"severity"` // mild, moderate, severe, critical
	Description string `json:"description"`
}

// ImplantSynergy represents an active implant synergy
type ImplantSynergy struct {
	ID             string                 `json:"id" db:"id"`
	CharacterID    string                 `json:"character_id" db:"character_id"`
	SynergyID      string                 `json:"synergy_id" db:"synergy_id"`
	Name           string                 `json:"name"`
	Description    string                 `json:"description"`
	ActiveImplants []string               `json:"active_implants" db:"active_implants"`
	BonusEffects   map[string]interface{} `json:"bonus_effects" db:"bonus_effects"`
	ActivatedAt    time.Time              `json:"activated_at" db:"activated_at"`
}

// ImplantVisuals represents visual customization for implants
type ImplantVisuals struct {
	ImplantID       string  `json:"implant_id"`
	GlowColor       string  `json:"glow_color,omitempty"`
	ParticleEffects bool    `json:"particle_effects,omitempty"`
	AnimationSpeed  float64 `json:"animation_speed,omitempty"`
	CustomTexture   string  `json:"custom_texture,omitempty"`
}

// AcquisitionType represents how an implant was acquired
type AcquisitionType string

// PaymentMethod represents payment methods for implants
type PaymentMethod string

const (
	PaymentCredits     PaymentMethod = "credits"
	PaymentMaterials   PaymentMethod = "materials"
	PaymentQuestReward PaymentMethod = "quest_reward"
)

// RiskAssessment represents cyberpsychosis risk levels
type RiskAssessment string

// Request/Response models for API

// AcquireImplantRequest represents a request to acquire an implant
type AcquireImplantRequest struct {
	ImplantID     string        `json:"implant_id" validate:"required,uuid"`
	PaymentMethod PaymentMethod `json:"payment_method" validate:"required"`
}

// CompatibilityCheckRequest represents a compatibility check request
type CompatibilityCheckRequest struct {
	ImplantIDs []string `json:"implant_ids" validate:"required,min=1,dive,uuid"`
	TargetSlot string   `json:"target_slot" validate:"required"`
}

// UpdateVisualsRequest represents a request to update implant visuals
type UpdateVisualsRequest struct {
	ImplantID string                 `json:"implant_id" validate:"required,uuid"`
	Visuals   map[string]interface{} `json:"visuals" validate:"required"`
}

// CompatibilityConflict represents a compatibility conflict
type CompatibilityConflict struct {
	ImplantA string `json:"implant_a"`
	ImplantB string `json:"implant_b"`
	Reason   string `json:"reason"`
}

// EnergyLimit represents energy usage limits
type EnergyLimit struct {
	Used      int `json:"used"`
	Max       int `json:"max"`
	Available int `json:"available"`
}

// HumanityLimit represents humanity loss limits
type HumanityLimit struct {
	Lost      int `json:"lost"`
	Max       int `json:"max"`
	Remaining int `json:"remaining"`
}

// SlotLimit represents slot usage limits
type SlotLimit struct {
	Used      int `json:"used"`
	Max       int `json:"max"`
	Available int `json:"available"`
}
