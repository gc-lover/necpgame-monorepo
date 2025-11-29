package models

import (
	"time"

	"github.com/google/uuid"
)

type AffixCategory string

const (
	AffixCategoryCombat       AffixCategory = "combat"
	AffixCategoryEnvironmental AffixCategory = "environmental"
	AffixCategoryDebuff       AffixCategory = "debuff"
	AffixCategoryDefensive    AffixCategory = "defensive"
)

type Affix struct {
	ID                uuid.UUID              `json:"id"`
	Name              string                 `json:"name"`
	Category          AffixCategory          `json:"category"`
	Description       string                 `json:"description"`
	Mechanics         map[string]interface{} `json:"mechanics,omitempty"`
	VisualEffects     map[string]interface{} `json:"visual_effects,omitempty"`
	RewardModifier    float64                `json:"reward_modifier"`
	DifficultyModifier float64               `json:"difficulty_modifier"`
	CreatedAt         time.Time              `json:"created_at"`
}

type AffixSummary struct {
	ID                uuid.UUID     `json:"id"`
	Name              string        `json:"name"`
	Category          AffixCategory `json:"category"`
	Description       string        `json:"description"`
	RewardModifier    float64       `json:"reward_modifier"`
	DifficultyModifier float64      `json:"difficulty_modifier"`
}

type ActiveAffixesResponse struct {
	WeekStart      time.Time       `json:"week_start"`
	WeekEnd        time.Time       `json:"week_end"`
	ActiveAffixes  []AffixSummary  `json:"active_affixes"`
	SeasonalAffix  *AffixSummary   `json:"seasonal_affix,omitempty"`
}

type InstanceAffixesResponse struct {
	InstanceID            uuid.UUID     `json:"instance_id"`
	AppliedAt             time.Time     `json:"applied_at"`
	Affixes               []AffixSummary `json:"affixes"`
	TotalRewardModifier   float64       `json:"total_reward_modifier"`
	TotalDifficultyModifier float64    `json:"total_difficulty_modifier"`
}

type AffixRotation struct {
	ID            uuid.UUID     `json:"id"`
	WeekStart     time.Time     `json:"week_start"`
	WeekEnd       time.Time     `json:"week_end"`
	ActiveAffixes []AffixSummary `json:"active_affixes"`
	SeasonalAffix *AffixSummary  `json:"seasonal_affix,omitempty"`
	CreatedAt     time.Time     `json:"created_at"`
}

type AffixRotationHistoryResponse struct {
	Items  []AffixRotation `json:"items"`
	Total  int             `json:"total"`
	Limit  int             `json:"limit"`
	Offset int             `json:"offset"`
}

type TriggerRotationRequest struct {
	Force         bool        `json:"force"`
	CustomAffixes []uuid.UUID `json:"custom_affixes,omitempty"`
}

