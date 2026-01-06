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
// ElectricEffectProcessor handles electric elemental effects processing
type ElectricEffectProcessor struct{}

// Issue: #143577551
// GetElementType returns the elemental type this processor handles
func (p *ElectricEffectProcessor) GetElementType() types.ElementalType {
	return types.ElementalTypeElectric
}

// Issue: #143577551
// ProcessEffect processes electric effect application
func (p *ElectricEffectProcessor) ProcessEffect(ctx types.EffectApplicationRequest) (types.EffectApplicationResponse, error) {
	response := types.EffectApplicationResponse{
		Success: true,
	}

	// Electric damage can chain between targets
	baseDamage := int(float64(45) * ctx.Intensity)

	// Environmental modifiers
	envMod := p.calculateEnvironmentalModifier(ctx.Environment)
	baseDamage = int(float64(baseDamage) * envMod)

	// Create active effect
	activeEffectID := fmt.Sprintf("electric-%s-%d", ctx.TargetID, time.Now().UnixNano())

	response.ActiveEffectID = activeEffectID
	response.DamageDealt = baseDamage
	response.StatusEffect = "shock"

	response.Interactions = p.GetInteractions([]types.ActiveEffect{})

	return response, nil
}

// Issue: #143577551
// CalculateDamage calculates electric damage with all modifiers
func (p *ElectricEffectProcessor) CalculateDamage(req types.DamageCalculationRequest) types.DamageCalculationResponse {
	response := types.DamageCalculationResponse{}

	baseDamage := req.BaseDamage

	// Electric elemental modifier - can chain and stun
	elementalMod := 1.0

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
		statusEffects = []string{"shock"}
		// Electric can chain to nearby targets
		if req.ElementType == types.ElementalTypeElectric {
			statusEffects = append(statusEffects, "chain_damage")
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
// GetInteractions returns electric-specific elemental interactions
func (p *ElectricEffectProcessor) GetInteractions(targetEffects []types.ActiveEffect) []types.Interaction {
	interactions := []types.Interaction{}

	for _, effect := range targetEffects {
		switch effect.EffectID {
		case "fire":
			// Electric vs Fire: plasma effect (handled by fire processor)
			interactions = append(interactions, types.Interaction{
				Type:           types.InteractionTypeSynergy,
				PrimaryEffect:  "electric",
				SecondaryEffect: "fire",
				Multiplier:      1.8,
				Description:     "Плазменный эффект: электричество + огонь = плазма",
				SpecialEffects:  []string{"plasma_damage", "ion_burst"},
			})
		case "ice":
			// Electric vs Ice: conductive ice (handled by ice processor)
			interactions = append(interactions, types.Interaction{
				Type:           types.InteractionTypeSynergy,
				PrimaryEffect:  "electric",
				SecondaryEffect: "ice",
				Multiplier:      1.6,
				Description:     "Проводящий лед: электричество + лед = цепная молния",
				SpecialEffects:  []string{"conductive_surface", "chain_lightning"},
			})
		case "poison":
			// Electric vs Poison: neurotoxin enhancement (handled by poison processor)
			interactions = append(interactions, types.Interaction{
				Type:           types.InteractionTypeSynergy,
				PrimaryEffect:  "electric",
				SecondaryEffect: "poison",
				Multiplier:      1.7,
				Description:     "Нейротоксин: электричество + яд = нервное повреждение",
				SpecialEffects:  []string{"neurotoxin", "paralysis"},
			})
		case "acid":
			// Electric vs Acid: conductive corrosion (handled by acid processor)
			interactions = append(interactions, types.Interaction{
				Type:           types.InteractionTypeSynergy,
				PrimaryEffect:  "electric",
				SecondaryEffect: "acid",
				Multiplier:      1.5,
				Description:     "Проводящая коррозия: электричество + кислота = электролит",
				SpecialEffects:  []string{"conductive_acid", "electrolyte_burst"},
			})
		}
	}

	return interactions
}

// Issue: #143577551
// calculateEnvironmentalModifier calculates environmental impact on electric effects
func (p *ElectricEffectProcessor) calculateEnvironmentalModifier(env types.EnvironmentContext) float64 {
	modifier := 1.0

	// Humidity greatly affects electric conductivity
	if env.Humidity > 80 {
		modifier *= 1.8 // High humidity creates conductive surfaces
	} else if env.Humidity < 20 {
		modifier *= 0.7 // Dry air reduces conductivity
	}

	// Electric field strength
	if env.ElectricField > 100 {
		modifier *= 1.5 // Strong electric fields amplify effects
	} else if env.ElectricField > 50 {
		modifier *= 1.2 // Moderate electric fields help
	}

	// Weather effects
	switch env.WeatherType {
	case "storm":
		modifier *= 2.0 // Storms massively amplify electric effects
	case "rain":
		modifier *= 1.6 // Rain creates conductive surfaces
	case "fog":
		modifier *= 1.3 // Fog can carry electric charge
	case "snow":
		modifier *= 1.4 // Snow can create insulating yet conductive conditions
	}

	// Terrain type affects grounding
	switch env.TerrainType {
	case "urban":
		modifier *= 1.2 // Urban areas have more conductive materials
	case "industrial":
		modifier *= 1.3 // Industrial areas often have high electric fields
	case "desert":
		modifier *= 0.8 // Sandy deserts can insulate
	}

	return math.Max(0.1, modifier)
}

