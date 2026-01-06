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
// FireEffectProcessor handles fire elemental effects processing
type FireEffectProcessor struct{}

// Issue: #143577551
// GetElementType returns the elemental type this processor handles
func (p *FireEffectProcessor) GetElementType() types.ElementalType {
	return types.ElementalTypeFire
}

// Issue: #143577551
// ProcessEffect processes fire effect application
func (p *FireEffectProcessor) ProcessEffect(ctx types.EffectApplicationRequest) (types.EffectApplicationResponse, error) {
	response := types.EffectApplicationResponse{
		Success: true,
	}

	// Calculate fire damage based on intensity and environment
	baseDamage := int(float64(50) * ctx.Intensity) // Base fire damage

	// Environmental modifiers
	envMod := p.calculateEnvironmentalModifier(ctx.Environment)
	baseDamage = int(float64(baseDamage) * envMod)

	// Create active effect
	activeEffectID := fmt.Sprintf("fire-%s-%d", ctx.TargetID, time.Now().UnixNano())

	response.ActiveEffectID = activeEffectID
	response.DamageDealt = baseDamage

	// Apply burn status effect if intensity is high enough
	if ctx.Intensity > 0.7 {
		response.StatusEffect = "burn"
	}

	// Check for fire-specific interactions
	response.Interactions = p.GetInteractions([]types.ActiveEffect{}) // Empty for now, will be filled by calculator

	return response, nil
}

// Issue: #143577551
// CalculateDamage calculates fire damage with all modifiers
func (p *FireEffectProcessor) CalculateDamage(req types.DamageCalculationRequest) types.DamageCalculationResponse {
	response := types.DamageCalculationResponse{}

	baseDamage := req.BaseDamage

	// Fire elemental modifier (typically 1.2x for fire)
	elementalMod := 1.2

	// Environmental modifier
	envMod := p.calculateEnvironmentalModifier(req.Environment)

	// Resistance penalty
	resistanceMod := 1.0 - req.TargetResist

	// Calculate total damage
	elementalDamage := int(float64(baseDamage) * elementalMod * envMod * resistanceMod)
	totalDamage := baseDamage + elementalDamage

	// Fire DoT damage if applicable
	dotDamage := 0
	if req.ElementType == types.ElementalTypeFire {
		// Calculate burn DoT based on intensity
		dotDamage = int(float64(totalDamage) * 0.3) // 30% as DoT
		totalDamage += dotDamage
	}

	response.TotalDamage = totalDamage
	response.ElementalDamage = elementalDamage
	response.DotDamage = dotDamage

	if elementalDamage > 0 {
		response.StatusEffects = []string{"burn"}
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
// GetInteractions returns fire-specific elemental interactions
func (p *FireEffectProcessor) GetInteractions(targetEffects []types.ActiveEffect) []types.Interaction {
	interactions := []types.Interaction{}

	for _, effect := range targetEffects {
		switch effect.EffectID {
		case "ice":
			// Fire vs Ice: steam explosion
			interactions = append(interactions, types.Interaction{
				Type:           types.InteractionTypeTransform,
				PrimaryEffect:  "fire",
				SecondaryEffect: "ice",
				Multiplier:      2.0,
				Description:     "Паровой взрыв: огонь + лед = мощный взрыв",
				SpecialEffects:  []string{"steam_explosion", "area_damage"},
			})
		case "poison":
			// Fire vs Poison: toxic combustion
			interactions = append(interactions, types.Interaction{
				Type:           types.InteractionTypeAmplify,
				PrimaryEffect:  "fire",
				SecondaryEffect: "poison",
				Multiplier:      1.5,
				Description:     "Токсичное горение: огонь усиливает яд",
				SpecialEffects:  []string{"toxic_cloud"},
			})
		case "electric":
			// Fire vs Electric: plasma effect
			interactions = append(interactions, types.Interaction{
				Type:           types.InteractionTypeSynergy,
				PrimaryEffect:  "fire",
				SecondaryEffect: "electric",
				Multiplier:      1.8,
				Description:     "Плазменный эффект: огонь + электричество",
				SpecialEffects:  []string{"plasma_damage", "chain_lightning"},
			})
		}
	}

	return interactions
}

// Issue: #143577551
// calculateEnvironmentalModifier calculates environmental impact on fire effects
func (p *FireEffectProcessor) calculateEnvironmentalModifier(env types.EnvironmentContext) float64 {
	modifier := 1.0

	// Temperature affects fire intensity
	if env.Temperature > 30 {
		modifier *= 1.3 // Hotter weather intensifies fire
	} else if env.Temperature < -10 {
		modifier *= 0.7 // Cold weather weakens fire
	}

	// Humidity reduces fire effectiveness
	if env.Humidity > 70 {
		modifier *= 0.6 // High humidity extinguishes fire
	} else if env.Humidity < 30 {
		modifier *= 1.2 // Low humidity intensifies fire
	}

	// Wind can spread fire (simplified)
	if env.WeatherType == "windy" {
		modifier *= 1.1
	}

	// Rain extinguishes fire
	if env.WeatherType == "rain" || env.WeatherType == "storm" {
		modifier *= 0.3
	}

	return math.Max(0.1, modifier) // Minimum 10% effectiveness
}
