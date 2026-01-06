// Issue: #143577551
// Package processors implements specific elemental effect processors
package processors

import (
	"fmt"
	"math"
	"time"

	"necpgame/services/effect-service-go/internal/effects/types"
)

// Issue: #143577551
// PoisonEffectProcessor handles poison elemental effects processing
type PoisonEffectProcessor struct{}

// Issue: #143577551
// GetElementType returns the elemental type this processor handles
func (p *PoisonEffectProcessor) GetElementType() types.ElementalType {
	return types.ElementalTypePoison
}

// Issue: #143577551
// ProcessEffect processes poison effect application
func (p *PoisonEffectProcessor) ProcessEffect(ctx types.EffectApplicationRequest) (types.EffectApplicationResponse, error) {
	response := types.EffectApplicationResponse{
		Success: true,
	}

	// Poison builds up over time, initial damage is lower
	baseDamage := int(float64(20) * ctx.Intensity)

	// Environmental modifiers
	envMod := p.calculateEnvironmentalModifier(ctx.Environment)
	baseDamage = int(float64(baseDamage) * envMod)

	// Create active effect
	activeEffectID := fmt.Sprintf("poison-%s-%d", ctx.TargetID, time.Now().UnixNano())

	response.ActiveEffectID = activeEffectID
	response.DamageDealt = baseDamage
	response.StatusEffect = "poison"

	response.Interactions = p.GetInteractions([]types.ActiveEffect{})

	return response, nil
}

// Issue: #143577551
// CalculateDamage calculates poison damage with all modifiers
func (p *PoisonEffectProcessor) CalculateDamage(req types.DamageCalculationRequest) types.DamageCalculationResponse {
	response := types.DamageCalculationResponse{}

	baseDamage := req.BaseDamage

	// Poison elemental modifier - focuses on DoT rather than direct damage
	elementalMod := 0.6

	// Environmental modifier
	envMod := p.calculateEnvironmentalModifier(req.Environment)

	// Resistance penalty
	resistanceMod := 1.0 - req.TargetResist

	// Calculate total damage
	elementalDamage := int(float64(baseDamage) * elementalMod * envMod * resistanceMod)
	totalDamage := baseDamage + elementalDamage

	// Poison DoT damage (main damage type for poison)
	dotDamage := int(float64(totalDamage) * 0.8) // 80% as DoT over time
	totalDamage += dotDamage

	response.TotalDamage = totalDamage
	response.ElementalDamage = elementalDamage
	response.DotDamage = dotDamage

	if elementalDamage > 0 {
		response.StatusEffects = []string{"poison", "regen_reduction"}
	}

	response.Breakdown = types.DamageBreakdown{
		BaseDamage:        baseDamage,
		ElementalModifier: elementalMod,
		ResistancePenalty: req.TargetResist,
		EnvironmentalMod:  envMod,
		InteractionMods:   []types.InteractionMod{},
	}

	return response
}

// Issue: #143577551
// GetInteractions returns poison-specific elemental interactions
func (p *PoisonEffectProcessor) GetInteractions(targetEffects []types.ActiveEffect) []types.Interaction {
	interactions := []types.Interaction{}

	for _, effect := range targetEffects {
		switch effect.EffectID {
		case "fire":
			// Poison vs Fire: toxic combustion (handled by fire processor)
			interactions = append(interactions, types.Interaction{
				Type:           types.InteractionTypeAmplify,
				PrimaryEffect:  "poison",
				SecondaryEffect: "fire",
				Multiplier:      1.5,
				Description:     "Токсичное горение: яд + огонь = токсичный дым",
				SpecialEffects:  []string{"toxic_combustion", "area_poison"},
			})
		case "electric":
			// Poison vs Electric: neurotoxin enhancement
			interactions = append(interactions, types.Interaction{
				Type:           types.InteractionTypeSynergy,
				PrimaryEffect:  "poison",
				SecondaryEffect: "electric",
				Multiplier:      1.7,
				Description:     "Нейротоксин: яд + электричество = нервное повреждение",
				SpecialEffects:  []string{"neurotoxin", "paralysis_risk"},
			})
		case "acid":
			// Poison vs Acid: chemical amplification
			interactions = append(interactions, types.Interaction{
				Type:           types.InteractionTypeAmplify,
				PrimaryEffect:  "poison",
				SecondaryEffect: "acid",
				Multiplier:      1.6,
				Description:     "Химическое усиление: яд + кислота = мощный токсин",
				SpecialEffects:  []string{"chemical_burn", "corrosive_poison"},
			})
		}
	}

	return interactions
}

// Issue: #143577551
// calculateEnvironmentalModifier calculates environmental impact on poison effects
func (p *PoisonEffectProcessor) calculateEnvironmentalModifier(env types.EnvironmentContext) float64 {
	modifier := 1.0

	// Temperature affects poison potency
	if env.Temperature > 35 {
		modifier *= 1.3 // Heat accelerates chemical reactions
	} else if env.Temperature < 5 {
		modifier *= 0.8 // Cold slows chemical reactions
	}

	// Humidity affects airborne toxins
	if env.Humidity > 60 {
		modifier *= 1.2 // High humidity helps toxin dispersal
	} else if env.Humidity < 20 {
		modifier *= 0.9 // Low humidity dries out toxins
	}

	// Radiation can enhance or weaken toxins
	if env.Radiation > 50 {
		modifier *= 1.4 // High radiation mutates/enhances toxins
	} else if env.Radiation > 20 {
		modifier *= 1.1 // Moderate radiation slightly enhances toxins
	}

	// Weather effects
	switch env.WeatherType {
	case "fog":
		modifier *= 1.3 // Fog helps conceal and spread toxins
	case "wind":
		modifier *= 1.2 // Wind can spread toxins
	case "rain":
		modifier *= 0.7 // Rain dilutes toxins
	}

	return math.Max(0.1, modifier)
}
