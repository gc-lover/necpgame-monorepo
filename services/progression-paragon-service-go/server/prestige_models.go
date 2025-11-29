package server

import (
	"time"

	"github.com/google/uuid"
)

type PrestigeInfo struct {
	CharacterID            uuid.UUID              `json:"character_id"`
	PrestigeLevel          int                    `json:"prestige_level"`
	ResetCount             int                    `json:"reset_count"`
	BonusesApplied         map[string]float64     `json:"bonuses_applied"`
	NextPrestigeRequirements *PrestigeRequirements `json:"next_prestige_requirements,omitempty"`
	LastResetAt            *time.Time             `json:"last_reset_at,omitempty"`
	UpdatedAt              time.Time              `json:"updated_at"`
}

type PrestigeRequirements struct {
	MinLevel         int      `json:"min_level"`
	MinParagonLevel int      `json:"min_paragon_level"`
	CompletedContent []string `json:"completed_content"`
}

type ResetPrestigeRequest struct {
	Confirm bool `json:"confirm"`
}

type PrestigeBonuses struct {
	CharacterID       uuid.UUID           `json:"character_id"`
	PrestigeLevel     int                 `json:"prestige_level"`
	AvailableBonuses  []PrestigeBonusItem `json:"available_bonuses"`
	MaxPrestigeLevel  int                 `json:"max_prestige_level"`
}

type PrestigeBonusItem struct {
	Type        string  `json:"type"`
	Value       float64 `json:"value"`
	Description string  `json:"description"`
}

