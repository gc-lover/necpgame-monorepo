// Package models contains request/response models for Cyberware Service API
package models

import (
	"time"
)

// InstallImplantRequest represents a request to install an implant
type InstallImplantRequest struct {
	ImplantID string `json:"implant_id" validate:"required"`
	Slot      int    `json:"slot" validate:"required,min=1,max=10"`
}

// UpgradeImplantRequest represents a request to upgrade an implant
type UpgradeImplantRequest struct {
	ImplantID string `json:"implant_id" validate:"required"`
}

// ActivateImplantRequest represents a request to activate/deactivate an implant
type ActivateImplantRequest struct {
	ImplantID string `json:"implant_id" validate:"required"`
	Active    bool   `json:"active"`
}

// ImplantCatalogResponse represents the response for implant catalog
type ImplantCatalogResponse struct {
	ImplantID      string             `json:"implant_id"`
	Name           string             `json:"name"`
	Description    string             `json:"description"`
	Type           ImplantType        `json:"type"`
	Category       ImplantCategory    `json:"category"`
	Rarity         ImplantRarity      `json:"rarity"`
	Stats          map[string]float64 `json:"stats"`
	Cyberpsychosis float64            `json:"cyberpsychosis"`
	Cost           int                `json:"cost"`
	Requirements   map[string]int     `json:"requirements,omitempty"`
	UnlockLevel    int                `json:"unlock_level,omitempty"`
}

// PlayerImplantsResponse represents the response for player's implants
type PlayerImplantsResponse struct {
	UserID         string                 `json:"user_id"`
	Implants       []*PlayerImplant       `json:"implants"`
	Cyberpsychosis float64                `json:"cyberpsychosis"`
	MaxSlots       int                    `json:"max_slots"`
	UsedSlots      int                    `json:"used_slots"`
	Effects        map[string]interface{} `json:"effects"`
}

// PlayerImplant represents an implant installed by a player
type PlayerImplant struct {
	ImplantID      string             `json:"implant_id"`
	Name           string             `json:"name"`
	Type           ImplantType        `json:"type"`
	Category       ImplantCategory    `json:"category"`
	Level          int                `json:"level"`
	Active         bool               `json:"active"`
	Slot           int                `json:"slot"`
	Stats          map[string]float64 `json:"stats"`
	Cyberpsychosis float64            `json:"cyberpsychosis"`
	InstalledAt    time.Time          `json:"installed_at"`
	LastUsedAt     *time.Time         `json:"last_used_at,omitempty"`
}

// ImplantStatsResponse represents implant usage statistics
type ImplantStatsResponse struct {
	TotalImplants  int                     `json:"total_implants"`
	ActiveImplants int                     `json:"active_implants"`
	TypeBreakdown  map[ImplantType]int     `json:"type_breakdown"`
	CategoryStats  map[ImplantCategory]int `json:"category_stats"`
	Popularity     map[string]int          `json:"popularity"`
	AverageLevel   float64                 `json:"average_level"`
	Cyberpsychosis CyberpsychosisStats     `json:"cyberpsychosis"`
	SynergyBonuses map[string]SynergyBonus `json:"synergy_bonuses"`
}

// CyberpsychosisStats represents cyberpsychosis statistics
type CyberpsychosisStats struct {
	Average float64 `json:"average"`
	Min     float64 `json:"min"`
	Max     float64 `json:"max"`
	Count   int     `json:"count"`
}

// SynergyBonus represents a synergy bonus between implants
type SynergyBonus struct {
	BonusType   string   `json:"bonus_type"`
	Value       float64  `json:"value"`
	Implants    []string `json:"implants"`
	Description string   `json:"description"`
}

// UpgradeCostResponse represents the cost to upgrade an implant
type UpgradeCostResponse struct {
	ImplantID              string             `json:"implant_id"`
	CurrentLevel           int                `json:"current_level"`
	NextLevel              int                `json:"next_level"`
	Cost                   map[string]int     `json:"cost"`
	CyberpsychosisIncrease float64            `json:"cyberpsychosis_increase"`
	StatImprovements       map[string]float64 `json:"stat_improvements"`
}

// ErrorResponse represents an error response
type ErrorResponse struct {
	Error   string `json:"error"`
	Message string `json:"message,omitempty"`
	Code    int    `json:"code,omitempty"`
}

// SuccessResponse represents a success response
type SuccessResponse struct {
	Success bool        `json:"success"`
	Message string      `json:"message,omitempty"`
	Data    interface{} `json:"data,omitempty"`
}
