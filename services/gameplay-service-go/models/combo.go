// Package models SQL queries use prepared statements with placeholders ($1, $2, ?) for safety
package models

import (
	"time"

	"github.com/google/uuid"
)

type ComboLoadout struct {
	ID           uuid.UUID           `json:"id"`
	CharacterID  uuid.UUID           `json:"character_id"`
	ActiveCombos []uuid.UUID         `json:"active_combos"`
	Preferences  *LoadoutPreferences `json:"preferences,omitempty"`
	CreatedAt    time.Time           `json:"created_at"`
	UpdatedAt    time.Time           `json:"updated_at"`
}

type LoadoutPreferences struct {
	AutoActivate  bool        `json:"auto_activate"`
	PriorityOrder []uuid.UUID `json:"priority_order"`
}

type UpdateLoadoutRequest struct {
	CharacterID  uuid.UUID           `json:"character_id"`
	ActiveCombos []uuid.UUID         `json:"active_combos,omitempty"`
	Preferences  *LoadoutPreferences `json:"preferences,omitempty"`
}

type ComboScore struct {
	ActivationID        uuid.UUID `json:"activation_id"`
	ExecutionDifficulty int       `json:"execution_difficulty"`
	DamageOutput        int       `json:"damage_output"`
	VisualImpact        int       `json:"visual_impact"`
	TeamCoordination    *int      `json:"team_coordination,omitempty"`
	TotalScore          int       `json:"total_score"`
	Category            string    `json:"category"`
	Timestamp           time.Time `json:"timestamp"`
}

type SubmitScoreRequest struct {
	ActivationID        uuid.UUID `json:"activation_id"`
	ExecutionDifficulty int       `json:"execution_difficulty"`
	DamageOutput        int       `json:"damage_output"`
	VisualImpact        int       `json:"visual_impact"`
	TeamCoordination    *int      `json:"team_coordination,omitempty"`
}

type ScoreSubmissionResponse struct {
	Success bool          `json:"success"`
	Score   ComboScore    `json:"score"`
	Rewards *ScoreRewards `json:"rewards,omitempty"`
}

type ScoreRewards struct {
	Experience int         `json:"experience,omitempty"`
	Currency   int         `json:"currency,omitempty"`
	Items      []uuid.UUID `json:"items,omitempty"`
}

type ComboAnalytics struct {
	ComboID           uuid.UUID      `json:"combo_id"`
	TotalActivations  int            `json:"total_activations"`
	SuccessRate       float32        `json:"success_rate"`
	AverageScore      float32        `json:"average_score"`
	AverageCategory   string         `json:"average_category"`
	MostUsedSynergies []SynergyUsage `json:"most_used_synergies"`
	ChainComboCount   int            `json:"chain_combo_count"`
}

type SynergyUsage struct {
	SynergyID   uuid.UUID `json:"synergy_id"`
	SynergyType string    `json:"synergy_type"`
	UsageCount  int       `json:"usage_count"`
}

type ComboActivation struct {
	ID          uuid.UUID `json:"id"`
	ComboID     uuid.UUID `json:"combo_id"`
	CharacterID uuid.UUID `json:"character_id"`
	ActivatedAt time.Time `json:"activated_at"`
}

type AnalyticsResponse struct {
	Analytics   []ComboAnalytics `json:"analytics"`
	PeriodStart time.Time        `json:"period_start"`
	PeriodEnd   time.Time        `json:"period_end"`
}
