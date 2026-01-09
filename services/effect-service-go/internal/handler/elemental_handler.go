// Issue: #143577551
// Package handler implements the API handlers for elemental effects service
package handler

import (
	"context"
	"fmt"
	"strconv"
	"time"

	"github.com/necpgame/necpgame/services/effect-service-go/internal/effects/manager"
	"github.com/necpgame/necpgame/services/effect-service-go/internal/effects/types"
)

// Issue: #143577551
// ElementalEffectHandler implements the API handlers using EffectManager
type ElementalEffectHandler struct {
	effectManager *manager.ElementalEffectManager
}

// Issue: #143577551
// NewElementalEffectHandler creates a new handler with effect manager
func NewElementalEffectHandler() *ElementalEffectHandler {
	return &ElementalEffectHandler{
		effectManager: manager.NewElementalEffectManager(),
	}
}

// Issue: #143577551
// ApplyEffect applies an elemental effect to a target
func (h *ElementalEffectHandler) ApplyEffect(ctx context.Context, req EffectApplicationRequest) (EffectApplicationResponse, error) {
	// Convert API request to internal format
	internalReq := types.EffectApplicationRequest{
		WeaponID:  req.WeaponID,
		TargetID:  req.TargetID,
		AttackerID: req.AttackerID,
		EffectID:  req.EffectID,
		Intensity: req.Intensity,
		Duration:  req.Duration,
		Metadata:  req.Metadata,
		Environment: types.EnvironmentContext{
			Temperature:   req.Environment.Temperature,
			Humidity:      req.Environment.Humidity,
			Pressure:      req.Environment.Pressure,
			Radiation:     req.Environment.Radiation,
			ElectricField: req.Environment.ElectricField,
			MagneticField: req.Environment.MagneticField,
			WeatherType:   req.Environment.WeatherType,
			TerrainType:   req.Environment.TerrainType,
			TimeOfDay:     req.Environment.TimeOfDay,
		},
	}

	// Apply the effect
	response, err := h.effectManager.ApplyEffect(ctx, internalReq)
	if err != nil {
		return EffectApplicationResponse{}, err
	}

	// Convert back to API response format
	apiResponse := EffectApplicationResponse{
		Success:      response.Success,
		ActiveEffectID: response.ActiveEffectID,
		DamageDealt:  response.DamageDealt,
		StatusEffect: response.StatusEffect,
		Error:        response.Error,
	}

	// Convert interactions
	for _, interaction := range response.Interactions {
		apiResponse.Interactions = append(apiResponse.Interactions, Interaction{
			Type:           InteractionType(interaction.Type),
			PrimaryEffect:  interaction.PrimaryEffect,
			SecondaryEffect: interaction.SecondaryEffect,
			Multiplier:      interaction.Multiplier,
			Description:     interaction.Description,
			SpecialEffects:  interaction.SpecialEffects,
		})
	}

	return apiResponse, nil
}

// Issue: #143577551
// GetActiveEffects returns active effects for a character
func (h *ElementalEffectHandler) GetActiveEffects(ctx context.Context, characterID string) ([]ActiveEffect, error) {
	activeEffects := h.effectManager.GetActiveEffects(characterID)

	var apiEffects []ActiveEffect
	for _, effect := range activeEffects {
		apiEffect := ActiveEffect{
			ID:        effect.ID,
			EffectID:  effect.EffectID,
			TargetID:  effect.TargetID,
			SourceID:  effect.SourceID,
			AppliedAt: effect.AppliedAt,
			ExpiresAt: effect.ExpiresAt,
			Duration:  effect.Duration,
			Stacks:    effect.Stacks,
			MaxStacks: effect.MaxStacks,
			DamageDealt: effect.DamageDealt,
			TicksApplied: effect.TicksApplied,
			Metadata:  effect.Metadata,
			IsExpired: effect.IsExpired,
		}
		apiEffects = append(apiEffects, apiEffect)
	}

	return apiEffects, nil
}

// Issue: #143577551
// CalculateDamage calculates elemental damage
func (h *ElementalEffectHandler) CalculateDamage(ctx context.Context, req DamageCalculationRequest) (DamageCalculationResponse, error) {
	// Convert API request to internal format
	internalReq := types.DamageCalculationRequest{
		BaseDamage: req.BaseDamage,
		ElementType: types.ElementalType(req.ElementType),
		TargetResist: req.TargetResistance,
		Environment: types.EnvironmentContext{
			Temperature:   req.Environment.Temperature,
			Humidity:      req.Environment.Humidity,
			Pressure:      req.Environment.Pressure,
			Radiation:     req.Environment.Radiation,
			ElectricField: req.Environment.ElectricField,
			MagneticField: req.Environment.MagneticField,
			WeatherType:   req.Environment.WeatherType,
			TerrainType:   req.Environment.TerrainType,
			TimeOfDay:     req.Environment.TimeOfDay,
		},
		ActiveEffects: convertActiveEffects(req.ActiveEffects),
	}

	// Calculate damage
	response := h.effectManager.CalculateDamage(internalReq)

	// Convert back to API response
	apiResponse := DamageCalculationResponse{
		TotalDamage:    response.TotalDamage,
		ElementalDamage: response.ElementalDamage,
		DotDamage:      response.DotDamage,
		StatusEffects:  response.StatusEffects,
		Breakdown: DamageBreakdown{
			BaseDamage:        response.Breakdown.BaseDamage,
			ElementalModifier: response.Breakdown.ElementalModifier,
			ResistancePenalty: response.Breakdown.ResistancePenalty,
			EnvironmentalMod:  response.Breakdown.EnvironmentalMod,
		},
	}

	// Convert interaction mods
	for _, mod := range response.Breakdown.InteractionMods {
		apiResponse.Breakdown.InteractionMods = append(apiResponse.Breakdown.InteractionMods, InteractionMod{
			Effect:      mod.Effect,
			Type:        mod.Type,
			Multiplier:  mod.Multiplier,
			Description: mod.Description,
		})
	}

	return apiResponse, nil
}

// Issue: #143577551
// PreviewInteraction calculates interaction preview
func (h *ElementalEffectHandler) PreviewInteraction(ctx context.Context, effects []string) ([]Interaction, error) {
	// Convert effect IDs to ActiveEffect format for preview
	var activeEffects []types.ActiveEffect
	for i, effectID := range effects {
		activeEffects = append(activeEffects, types.ActiveEffect{
			ID:       fmt.Sprintf("preview-%d", i),
			EffectID: effectID,
			TargetID: "preview-target",
		})
	}

	// Calculate interactions
	calculator := h.effectManager.GetInteractionCalculator()
	interactions := calculator.CalculateInteractions(activeEffects, types.EnvironmentContext{})

	// Convert to API format
	var apiInteractions []Interaction
	for _, interaction := range interactions {
		apiInteractions = append(apiInteractions, Interaction{
			Type:           InteractionType(interaction.Type),
			PrimaryEffect:  interaction.PrimaryEffect,
			SecondaryEffect: interaction.SecondaryEffect,
			Multiplier:      interaction.Multiplier,
			Description:     interaction.Description,
			SpecialEffects:  interaction.SpecialEffects,
		})
	}

	return apiInteractions, nil
}

// Issue: #143577551
// CleanupExpiredEffects performs maintenance cleanup
func (h *ElementalEffectHandler) CleanupExpiredEffects(ctx context.Context) (int, error) {
	cleaned := h.effectManager.CleanupExpiredEffects()
	return cleaned, nil
}

// Issue: #143577551
// GetEffectManager returns the underlying effect manager
func (h *ElementalEffectHandler) GetEffectManager() *manager.ElementalEffectManager {
	return h.effectManager
}

// Issue: #143577551
// convertActiveEffects converts API active effects to internal format
func convertActiveEffects(apiEffects []ActiveEffect) []types.ActiveEffect {
	var internalEffects []types.ActiveEffect
	for _, apiEffect := range apiEffects {
		internalEffect := types.ActiveEffect{
			ID:            apiEffect.ID,
			EffectID:      apiEffect.EffectID,
			TargetID:      apiEffect.TargetID,
			SourceID:      apiEffect.SourceID,
			AppliedAt:     apiEffect.AppliedAt,
			ExpiresAt:     apiEffect.ExpiresAt,
			Duration:      apiEffect.Duration,
			Stacks:        apiEffect.Stacks,
			MaxStacks:     apiEffect.MaxStacks,
			DamageDealt:   apiEffect.DamageDealt,
			TicksApplied:  apiEffect.TicksApplied,
			Metadata:      apiEffect.Metadata,
			IsExpired:     apiEffect.IsExpired,
		}
		internalEffects = append(internalEffects, internalEffect)
	}
	return internalEffects
}

// Issue: #143577551
// EffectApplicationRequest represents API request for effect application
type EffectApplicationRequest struct {
	WeaponID   string                 `json:"weapon_id"`
	TargetID   string                 `json:"target_id"`
	AttackerID string                 `json:"attacker_id"`
	EffectID   string                 `json:"effect_id"`
	Intensity  float64                `json:"intensity"`
	Duration   float64                `json:"duration"`
	Metadata   map[string]interface{} `json:"metadata"`
	Environment EnvironmentContext    `json:"environment"`
}

// Issue: #143577551
// EffectApplicationResponse represents API response for effect application
type EffectApplicationResponse struct {
	Success       bool         `json:"success"`
	ActiveEffectID string       `json:"active_effect_id,omitempty"`
	DamageDealt   int          `json:"damage_dealt"`
	StatusEffect  string       `json:"status_effect,omitempty"`
	Interactions  []Interaction `json:"interactions,omitempty"`
	Error         string       `json:"error,omitempty"`
}

// Issue: #143577551
// ActiveEffect represents an active effect in API format
type ActiveEffect struct {
	ID           string                 `json:"id"`
	EffectID     string                 `json:"effect_id"`
	TargetID     string                 `json:"target_id"`
	SourceID     string                 `json:"source_id"`
	AppliedAt    time.Time              `json:"applied_at"`
	ExpiresAt    *time.Time             `json:"expires_at,omitempty"`
	Duration     float64                `json:"duration"`
	Stacks       int                    `json:"stacks"`
	MaxStacks    int                    `json:"max_stacks"`
	DamageDealt  int                    `json:"damage_dealt"`
	TicksApplied int                    `json:"ticks_applied"`
	Metadata     map[string]interface{} `json:"metadata"`
	IsExpired    bool                   `json:"is_expired"`
}

// Issue: #143577551
// DamageCalculationRequest represents API request for damage calculation
type DamageCalculationRequest struct {
	BaseDamage       int                `json:"base_damage"`
	ElementType      string             `json:"element_type"`
	TargetResistance float64            `json:"target_resistance"`
	Environment      EnvironmentContext `json:"environment"`
	ActiveEffects    []ActiveEffect     `json:"active_effects"`
}

// Issue: #143577551
// DamageCalculationResponse represents API response for damage calculation
type DamageCalculationResponse struct {
	TotalDamage     int              `json:"total_damage"`
	ElementalDamage int              `json:"elemental_damage"`
	DotDamage       int              `json:"dot_damage"`
	StatusEffects   []string         `json:"status_effects"`
	Breakdown       DamageBreakdown  `json:"breakdown"`
}

// Issue: #143577551
// DamageBreakdown provides detailed damage calculation breakdown
type DamageBreakdown struct {
	BaseDamage        int             `json:"base_damage"`
	ElementalModifier float64         `json:"elemental_modifier"`
	ResistancePenalty float64         `json:"resistance_penalty"`
	EnvironmentalMod  float64         `json:"environmental_modifier"`
	InteractionMods   []InteractionMod `json:"interaction_modifiers"`
}

// Issue: #143577551
// InteractionMod represents a single interaction modifier
type InteractionMod struct {
	Effect      string  `json:"effect"`
	Type        string  `json:"type"`
	Multiplier  float64 `json:"multiplier"`
	Description string  `json:"description"`
}

// Issue: #143577551
// Interaction represents an interaction between elemental effects
type Interaction struct {
	Type           InteractionType `json:"type"`
	PrimaryEffect  string          `json:"primary_effect"`
	SecondaryEffect string         `json:"secondary_effect"`
	Multiplier      float64         `json:"multiplier"`
	Description     string          `json:"description"`
	SpecialEffects  []string        `json:"special_effects,omitempty"`
}

// Issue: #143577551
// InteractionType defines types of elemental interactions
type InteractionType string

const (
	InteractionTypeSynergy    InteractionType = "synergy"
	InteractionTypeConflict   InteractionType = "conflict"
	InteractionTypeNeutralize InteractionType = "neutralize"
	InteractionTypeAmplify    InteractionType = "amplify"
	InteractionTypeTransform  InteractionType = "transform"
)

// Issue: #143577551
// EnvironmentContext represents environmental factors
type EnvironmentContext struct {
	Temperature   float64 `json:"temperature"`
	Humidity      float64 `json:"humidity"`
	Pressure      float64 `json:"pressure"`
	Radiation     float64 `json:"radiation"`
	ElectricField float64 `json:"electric_field"`
	MagneticField float64 `json:"magnetic_field"`
	WeatherType   string  `json:"weather_type"`
	TerrainType   string  `json:"terrain_type"`
	TimeOfDay     string  `json:"time_of_day"`
}






