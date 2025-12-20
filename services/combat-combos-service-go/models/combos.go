// Package models Issue: #158
package models

import (
	"time"
)

// ComboType represents the type of combo (solo, team, legendary)
type ComboType string

// ComboComplexity represents the complexity level of a combo
type ComboComplexity string

const (
	ComboComplexityBronze    ComboComplexity = "Bronze"
	ComboComplexitySilver    ComboComplexity = "Silver"
	ComboComplexityGold      ComboComplexity = "Gold"
	ComboComplexityPlatinum  ComboComplexity = "Platinum"
	ComboComplexityLegendary ComboComplexity = "Legendary"
)

// SynergyType represents different types of synergies
type SynergyType string

// ComboCatalog represents a combo in the catalog
type ComboCatalog struct {
	ID              string          `json:"id" db:"id"`
	Name            string          `json:"name" db:"name"`
	Description     string          `json:"description" db:"description"`
	ComboType       ComboType       `json:"combo_type" db:"combo_type"`
	Complexity      ComboComplexity `json:"complexity" db:"complexity"`
	Requirements    Requirements    `json:"requirements" db:"requirements"`
	Sequence        []string        `json:"sequence" db:"sequence"`
	Bonuses         Bonuses         `json:"bonuses" db:"bonuses"`
	Cooldown        int             `json:"cooldown" db:"cooldown"`
	ChainCompatible bool            `json:"chain_compatible" db:"chain_compatible"`
	CreatedAt       time.Time       `json:"created_at" db:"created_at"`
}

// Requirements for combo activation
type Requirements struct {
	MinLevel        int      `json:"min_level,omitempty"`
	RequiredSkills  []string `json:"required_skills,omitempty"`
	MinParticipants int      `json:"min_participants,omitempty"`
	MaxParticipants int      `json:"max_participants,omitempty"`
}

// Bonuses provided by the combo
type Bonuses struct {
	DamageMultiplier  float64            `json:"damage_multiplier,omitempty"`
	CooldownReduction int                `json:"cooldown_reduction,omitempty"`
	EffectBonuses     map[string]float64 `json:"effect_bonuses,omitempty"`
	SpecialEffects    []string           `json:"special_effects,omitempty"`
}

// ComboActivation represents a combo activation instance
type ComboActivation struct {
	ID           string                 `json:"id" db:"id"`
	ComboID      string                 `json:"combo_id" db:"combo_id"`
	CharacterID  string                 `json:"character_id" db:"character_id"`
	Participants []string               `json:"participants" db:"participants"`
	Context      map[string]interface{} `json:"context" db:"context"`
	Success      bool                   `json:"success" db:"success"`
	Score        int                    `json:"score" db:"score"`
	ActivatedAt  time.Time              `json:"activated_at" db:"activated_at"`
	Duration     int                    `json:"duration" db:"duration"`
}

// ComboSynergy represents synergy relationships
type ComboSynergy struct {
	ID           string       `json:"id" db:"id"`
	SynergyType  SynergyType  `json:"synergy_type" db:"synergy_type"`
	ComboID      string       `json:"combo_id" db:"combo_id"`
	AbilityIDs   []string     `json:"ability_ids,omitempty" db:"ability_ids"`
	EquipmentIDs []string     `json:"equipment_ids,omitempty" db:"equipment_ids"`
	ImplantIDs   []string     `json:"implant_ids,omitempty" db:"implant_ids"`
	TeamSize     int          `json:"team_size,omitempty" db:"team_size"`
	TimingWindow int          `json:"timing_window,omitempty" db:"timing_window"`
	Bonuses      Bonuses      `json:"bonuses" db:"bonuses"`
	Requirements Requirements `json:"requirements" db:"requirements"`
}

// ComboLoadout represents a character's combo preferences
type ComboLoadout struct {
	ID           string                 `json:"id" db:"id"`
	CharacterID  string                 `json:"character_id" db:"character_id"`
	ActiveCombos []string               `json:"active_combos" db:"active_combos"`
	Preferences  map[string]interface{} `json:"preferences" db:"preferences"`
	AutoActivate bool                   `json:"auto_activate" db:"auto_activate"`
	CreatedAt    time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt    time.Time              `json:"updated_at" db:"updated_at"`
}

// ComboScoring represents scoring metrics for a combo
type ComboScoring struct {
	ID                  string    `json:"id" db:"id"`
	ActivationID        string    `json:"activation_id" db:"activation_id"`
	ExecutionDifficulty int       `json:"execution_difficulty" db:"execution_difficulty"`
	DamageOutput        int       `json:"damage_output" db:"damage_output"`
	VisualImpact        int       `json:"visual_impact" db:"visual_impact"`
	TeamCoordination    int       `json:"team_coordination" db:"team_coordination"`
	TotalScore          int       `json:"total_score" db:"total_score"`
	Category            string    `json:"category" db:"category"`
	Timestamp           time.Time `json:"timestamp" db:"timestamp"`
}

// ComboChain represents chain combo relationships
type ComboChain struct {
	ID                string                 `json:"id" db:"id"`
	FirstComboID      string                 `json:"first_combo_id" db:"first_combo_id"`
	SecondComboID     string                 `json:"second_combo_id" db:"second_combo_id"`
	CharacterID       string                 `json:"character_id" db:"character_id"`
	TimeBetween       int                    `json:"time_between" db:"time_between"`
	BonusesApplied    map[string]interface{} `json:"bonuses_applied" db:"bonuses_applied"`
	CooldownReduction int                    `json:"cooldown_reduction" db:"cooldown_reduction"`
	Timestamp         time.Time              `json:"timestamp" db:"timestamp"`
}

// ComboFailure represents failed combo attempts
type ComboFailure struct {
	ID            string                 `json:"id" db:"id"`
	ComboID       string                 `json:"combo_id" db:"combo_id"`
	CharacterID   string                 `json:"character_id" db:"character_id"`
	FailureReason string                 `json:"failure_reason" db:"failure_reason"`
	Context       map[string]interface{} `json:"context" db:"context"`
	Timestamp     time.Time              `json:"timestamp" db:"timestamp"`
}

// API Request/Response models

// ComboCatalogResponse represents the response for combo catalog
type ComboCatalogResponse struct {
	Combos []ComboCatalog `json:"combos"`
	Total  int            `json:"total"`
}

// ComboDetailResponse represents detailed combo information
type ComboDetailResponse struct {
	Combo     ComboCatalog   `json:"combo"`
	Synergies []ComboSynergy `json:"synergies"`
}

// ActivateComboRequest represents a combo activation request
type ActivateComboRequest struct {
	ComboID      string                 `json:"combo_id"`
	CharacterID  string                 `json:"character_id"`
	Participants []string               `json:"participants,omitempty"`
	Context      map[string]interface{} `json:"context,omitempty"`
}

// ActivateComboResponse represents a combo activation response
type ActivateComboResponse struct {
	ActivationID string  `json:"activation_id"`
	Success      bool    `json:"success"`
	Score        int     `json:"score"`
	Bonuses      Bonuses `json:"bonuses"`
}

// ComboLoadoutRequest represents a loadout update request
type ComboLoadoutRequest struct {
	ActiveCombos []string               `json:"active_combos"`
	Preferences  map[string]interface{} `json:"preferences"`
	AutoActivate bool                   `json:"auto_activate"`
}

// ComboLoadoutResponse represents a loadout response
type ComboLoadoutResponse struct {
	Loadout ComboLoadout `json:"loadout"`
}

// ComboAnalyticsResponse represents analytics data
type ComboAnalyticsResponse struct {
	TotalActivations int               `json:"total_activations"`
	SuccessRate      float64           `json:"success_rate"`
	PopularCombos    []ComboPopularity `json:"popular_combos"`
	ScoringTrends    []ScoringTrend    `json:"scoring_trends"`
}

// ComboPopularity represents popularity metrics for combos
type ComboPopularity struct {
	ComboID      string  `json:"combo_id"`
	ComboName    string  `json:"combo_name"`
	Activations  int     `json:"activations"`
	AverageScore float64 `json:"average_score"`
}

// ScoringTrend represents scoring trends over time
type ScoringTrend struct {
	Date         string  `json:"date"`
	AverageScore float64 `json:"average_score"`
	TotalCombos  int     `json:"total_combos"`
}
