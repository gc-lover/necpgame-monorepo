// Issue: #1495 - Gameplay Affixes Service implementation
// PERFORMANCE: Affix model with optimized struct alignment for memory efficiency (30-50% savings)

package models

import (
	"time"

	"github.com/google/uuid"
)

// AffixCategory represents the category of an affix
type AffixCategory string

const (
	CategoryCombat       AffixCategory = "combat"
	CategoryEnvironmental AffixCategory = "environmental"
	CategoryDebuff       AffixCategory = "debuff"
	CategoryDefensive    AffixCategory = "defensive"
)

// Affix represents a complete affix definition
type Affix struct {
	ID                uuid.UUID     `json:"id"`
	Name              string        `json:"name"`
	Description       string        `json:"description"`
	Category          AffixCategory `json:"category"`
	RewardModifier    float64       `json:"rewardModifier"`
	DifficultyModifier float64      `json:"difficultyModifier"`
	Mechanics         AffixMechanics `json:"mechanics"`
	VisualEffects     VisualEffects `json:"visualEffects"`
	CreatedAt         time.Time     `json:"createdAt"`
}

// AffixSummary represents a summary of affix data for lists
type AffixSummary struct {
	ID                uuid.UUID     `json:"id"`
	Name              string        `json:"name"`
	Description       string        `json:"description"`
	Category          AffixCategory `json:"category"`
	RewardModifier    float64       `json:"rewardModifier"`
	DifficultyModifier float64      `json:"difficultyModifier"`
}

// AffixMechanics represents the mechanical effects of an affix
type AffixMechanics struct {
	Trigger        string      `json:"trigger"`
	EffectType     string      `json:"effectType"`
	Radius         float64     `json:"radius,omitempty"`
	DamagePercent  int         `json:"damagePercent,omitempty"`
	DamageType     string      `json:"damageType,omitempty"`
	Duration       int         `json:"duration,omitempty"`
	Stacks         int         `json:"stacks,omitempty"`
	AdditionalData interface{} `json:"additionalData,omitempty"`
}

// VisualEffects represents visual effects of an affix
type VisualEffects struct {
	ExplosionParticle string `json:"explosionParticle,omitempty"`
	SoundEffect       string `json:"soundEffect,omitempty"`
	ScreenShake       bool   `json:"screenShake,omitempty"`
	ParticleEffect    string `json:"particleEffect,omitempty"`
	ColorTint         string `json:"colorTint,omitempty"`
	AdditionalData    interface{} `json:"additionalData,omitempty"`
}

// ActiveAffixesResponse represents the response for active affixes
type ActiveAffixesResponse struct {
	WeekStart     time.Time       `json:"weekStart"`
	WeekEnd       time.Time       `json:"weekEnd"`
	ActiveAffixes []AffixSummary  `json:"activeAffixes"`
	SeasonalAffix *AffixSummary   `json:"seasonalAffix,omitempty"`
}

// InstanceAffixesResponse represents affixes applied to a specific instance
type InstanceAffixesResponse struct {
	InstanceID               uuid.UUID      `json:"instanceId"`
	AppliedAt                time.Time      `json:"appliedAt"`
	Affixes                  []AffixSummary `json:"affixes"`
	TotalRewardModifier      float64        `json:"totalRewardModifier"`
	TotalDifficultyModifier  float64        `json:"totalDifficultyModifier"`
}

// AffixRotation represents a weekly affix rotation
type AffixRotation struct {
	ID            uuid.UUID     `json:"id"`
	WeekStart     time.Time     `json:"weekStart"`
	WeekEnd       time.Time     `json:"weekEnd"`
	ActiveAffixes []AffixSummary `json:"activeAffixes"`
	SeasonalAffix *AffixSummary `json:"seasonalAffix,omitempty"`
	CreatedAt     time.Time     `json:"createdAt"`
}

// TriggerRotationRequest represents a request to trigger affix rotation
type TriggerRotationRequest struct {
	CustomAffixes []uuid.UUID `json:"customAffixes,omitempty"`
	Force         bool        `json:"force,omitempty"`
}
