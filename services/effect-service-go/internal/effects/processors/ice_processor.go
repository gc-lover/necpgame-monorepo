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
// IceEffectProcessor handles ice elemental effects processing
type IceEffectProcessor struct{}

// Issue: #143577551
// GetElementType returns the elemental type this processor handles
func (p *IceEffectProcessor) GetElementType() types.ElementalType {
	return types.ElementalTypeIce
}

// Issue: #143577551
// ProcessEffect processes ice effect application
func (p *IceEffectProcessor) ProcessEffect(ctx types.EffectApplicationRequest) (types.EffectApplicationResponse, error) {
	response := types.EffectApplicationResponse{
		Success: true,
	}

	// Calculate ice damage based on intensity
	baseDamage := int(float64(40) * ctx.Intensity)

	// Environmental modifiers
	envMod := p.calculateEnvironmentalModifier(ctx.Environment)
	baseDamage = int(float64(baseDamage) * envMod)

	// Create active effect
	activeEffectID := fmt.Sprintf("ice-%s-%d", ctx.TargetID, time.Now().UnixNano())

	response.ActiveEffectID = activeEffectID
	response.DamageDealt = baseDamage

	// Apply freeze or slow effects based on intensity
	if ctx.Intensity > 0.8 {
		response.StatusEffect = "freeze"
	} else if ctx.Intensity > 0.4 {
		response.StatusEffect = "slow"
	}

	response.Interactions = p.GetInteractions([]types.ActiveEffect{})

	return response, nil
}

// Issue: #143577551
// CalculateDamage calculates ice damage with all modifiers
func (p *IceEffectProcessor) CalculateDamage(req types.DamageCalculationRequest) types.DamageCalculationResponse {
	response := types.DamageCalculationResponse{}

	baseDamage := req.BaseDamage

	// Ice elemental modifier
	elementalMod := 0.9 // Ice typically does less direct damage but has control effects

	// Environmental modifier
	envMod := p.calculateEnvironmentalModifier(req.Environment)

	// Resistance penalty
	resistanceMod := 1.0 - req.TargetResist

	// Calculate total damage
	elementalDamage := int(float64(baseDamage) * elementalMod * envMod * resistanceMod)
	totalDamage := baseDamage + elementalDamage

	response.TotalDamage = totalDamage
	response.ElementalDamage = elementalDamage

	statusEffects := []string{}
	if elementalDamage > 0 {
		if req.ElementType == types.ElementalTypeIce {
			statusEffects = []string{"slow", "brittle"}
		}
	}

	response.StatusEffects = statusEffects

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
// GetInteractions returns ice-specific elemental interactions
func (p *IceEffectProcessor) GetInteractions(targetEffects []types.ActiveEffect) []types.Interaction {
	interactions := []types.Interaction{}

	for _, effect := range targetEffects {
		switch effect.EffectID {
		case "fire":
			// Ice vs Fire: steam explosion (handled by fire processor)
			interactions = append(interactions, types.Interaction{
				Type:           types.InteractionTypeTransform,
				PrimaryEffect:  "ice",
				SecondaryEffect: "fire",
				Multiplier:      2.0,
				Description:     "Паровой взрыв: лед + огонь = мощный взрыв",
				SpecialEffects:  []string{"steam_explosion", "area_damage"},
			})
		case "electric":
			// Ice vs Electric: conductive ice (increases electric damage)
			interactions = append(interactions, types.Interaction{
				Type:           types.InteractionTypeSynergy,
				PrimaryEffect:  "ice",
				SecondaryEffect: "electric",
				Multiplier:      1.6,
				Description:     "Проводящий лед: лед усиливает электричество",
				SpecialEffects:  []string{"conductive_surface", "chain_damage"},
			})
		case "acid":
			// Ice vs Acid: corrosive freeze
			interactions = append(interactions, types.Interaction{
				Type:           types.InteractionTypeAmplify,
				PrimaryEffect:  "ice",
				SecondaryEffect: "acid",
				Multiplier:      1.4,
				Description:     "Коррозионный мороз: лед усиливает кислоту",
				SpecialEffects:  []string{"corrosive_freeze", "armor_degradation"},
			})
		}
	}

	return interactions
}

// Issue: #143577551
// calculateEnvironmentalModifier calculates environmental impact on ice effects
func (p *IceEffectProcessor) calculateEnvironmentalModifier(env types.EnvironmentContext) float64 {
	modifier := 1.0

	// Temperature greatly affects ice effectiveness
	if env.Temperature < -20 {
		modifier *= 2.0 // Very cold intensifies ice
	} else if env.Temperature > 20 {
		modifier *= 0.4 // Warm weather melts ice
	} else if env.Temperature > 0 {
		modifier *= 0.7 // Above freezing weakens ice
	}

	// Humidity affects ice formation
	if env.Humidity > 80 {
		modifier *= 1.2 // High humidity helps ice formation
	}

	// Weather effects
	switch env.WeatherType {
	case "snow":
		modifier *= 1.5 // Snow intensifies ice effects
	case "rain":
		modifier *= 0.8 // Rain can melt ice
	case "fog":
		modifier *= 1.1 // Fog helps maintain ice
	}

	return math.Max(0.1, modifier)
}
