// Issue: #143577551
// Package processors implements specific elemental effect processors
package processors

import (
	"fmt"
	"math"
	"time"

	"github.com/necpgame/necpgame/services/effect-service-go/internal/effects/types"
)

// Issue: #143577551
// VoidEffectProcessor handles void elemental effects processing
type VoidEffectProcessor struct{}

// Issue: #143577551
// GetElementType returns the elemental type this processor handles
func (p *VoidEffectProcessor) GetElementType() types.ElementalType {
	return types.ElementalTypeVoid
}

// Issue: #143577551
// ProcessEffect processes void effect application
func (p *VoidEffectProcessor) ProcessEffect(ctx types.EffectApplicationRequest) (types.EffectApplicationResponse, error) {
	response := types.EffectApplicationResponse{
		Success: true,
	}

	// Void damage drains life force and ignores some resistances
	baseDamage := int(float64(55) * ctx.Intensity)

	// Environmental modifiers
	envMod := p.calculateEnvironmentalModifier(ctx.Environment)
	baseDamage = int(float64(baseDamage) * envMod)

	// Create active effect
	activeEffectID := fmt.Sprintf("void-%s-%d", ctx.TargetID, time.Now().UnixNano())

	response.ActiveEffectID = activeEffectID
	response.DamageDealt = baseDamage
	response.StatusEffect = "drain"

	response.Interactions = p.GetInteractions([]types.ActiveEffect{})

	return response, nil
}

// Issue: #143577551
// CalculateDamage calculates void damage with all modifiers
func (p *VoidEffectProcessor) CalculateDamage(req types.DamageCalculationRequest) types.DamageCalculationResponse {
	response := types.DamageCalculationResponse{}

	baseDamage := req.BaseDamage

	// Void elemental modifier - ignores some resistances
	elementalMod := 1.3

	// Environmental modifier
	envMod := p.calculateEnvironmentalModifier(req.Environment)

	// Void partially ignores resistance
	resistanceMod := 1.0 - (req.TargetResist * 0.5) // Only 50% effective against void

	// Calculate total damage
	elementalDamage := int(float64(baseDamage) * elementalMod * envMod * resistanceMod)
	totalDamage := baseDamage + elementalDamage

	response.TotalDamage = totalDamage
	response.ElementalDamage = elementalDamage

	if elementalDamage > 0 {
		response.StatusEffects = []string{"drain", "resistance_reduction"}
	}

	response.Breakdown = types.DamageBreakdown{
		BaseDamage:        baseDamage,
		ElementalModifier: elementalMod,
		ResistancePenalty: req.TargetResist * 0.5, // Shows partial resistance ignore
		EnvironmentalMod:  envMod,
		InteractionMods:   []types.InteractionMod{},
	}

	return response
}

// Issue: #143577551
// GetInteractions returns void-specific elemental interactions
func (p *VoidEffectProcessor) GetInteractions(targetEffects []types.ActiveEffect) []types.Interaction {
	interactions := []types.Interaction{}

	for _, effect := range targetEffects {
		switch effect.EffectID {
		case "fire", "ice", "electric", "acid", "poison":
			// Void consumes other elements
			interactions = append(interactions, types.Interaction{
				Type:           types.InteractionTypeNeutralize,
				PrimaryEffect:  "void",
				SecondaryEffect: effect.EffectID,
				Multiplier:      0.3, // Reduces other elemental effects
				Description:     "Поглощение стихии: пустота поглощает другие элементы",
				SpecialEffects:  []string{"element_absorption", "void_amplification"},
			})
		}
	}

	return interactions
}

// Issue: #143577551
// calculateEnvironmentalModifier calculates environmental impact on void effects
func (p *VoidEffectProcessor) calculateEnvironmentalModifier(env types.EnvironmentContext) float64 {
	modifier := 1.0

	// Void is affected by "nothingness" - empty, dark places enhance it
	if env.TimeOfDay == "night" {
		modifier *= 1.3 // Darkness enhances void effects
	}

	if env.WeatherType == "clear" && env.TimeOfDay == "night" {
		modifier *= 1.2 // Clear night skies enhance void
	}

	// Radiation can enhance void effects
	if env.Radiation > 30 {
		modifier *= 1.4 // High radiation strengthens void
	}

	// Void is stronger in enclosed or abandoned spaces
	switch env.TerrainType {
	case "urban":
		if env.TimeOfDay == "night" {
			modifier *= 1.2 // Urban decay at night enhances void
		}
	case "desert":
		modifier *= 1.1 // Empty deserts enhance void
	case "industrial":
		if env.Radiation > 20 {
			modifier *= 1.3 // Radioactive industrial areas greatly enhance void
		}
	}

	return math.Max(0.1, modifier)
}






