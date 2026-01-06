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
// AcidEffectProcessor handles acid elemental effects processing
type AcidEffectProcessor struct{}

// Issue: #143577551
// GetElementType returns the elemental type this processor handles
func (p *AcidEffectProcessor) GetElementType() types.ElementalType {
	return types.ElementalTypeAcid
}

// Issue: #143577551
// ProcessEffect processes acid effect application
func (p *AcidEffectProcessor) ProcessEffect(ctx types.EffectApplicationRequest) (types.EffectApplicationResponse, error) {
	response := types.EffectApplicationResponse{
		Success: true,
	}

	// Acid damage builds up and degrades armor/materials
	baseDamage := int(float64(35) * ctx.Intensity)

	// Environmental modifiers
	envMod := p.calculateEnvironmentalModifier(ctx.Environment)
	baseDamage = int(float64(baseDamage) * envMod)

	// Create active effect
	activeEffectID := fmt.Sprintf("acid-%s-%d", ctx.TargetID, time.Now().UnixNano())

	response.ActiveEffectID = activeEffectID
	response.DamageDealt = baseDamage
	response.StatusEffect = "corrode"

	response.Interactions = p.GetInteractions([]types.ActiveEffect{})

	return response, nil
}

// Issue: #143577551
// CalculateDamage calculates acid damage with all modifiers
func (p *AcidEffectProcessor) CalculateDamage(req types.DamageCalculationRequest) types.DamageCalculationResponse {
	response := types.DamageCalculationResponse{}

	baseDamage := req.BaseDamage

	// Acid elemental modifier - good against armor
	elementalMod := 1.1

	// Environmental modifier
	envMod := p.calculateEnvironmentalModifier(req.Environment)

	// Resistance penalty
	resistanceMod := 1.0 - req.TargetResist

	// Calculate total damage
	elementalDamage := int(float64(baseDamage) * elementalMod * envMod * resistanceMod)
	totalDamage := baseDamage + elementalDamage

	// Acid DoT damage (corrosion over time)
	dotDamage := int(float64(totalDamage) * 0.4) // 40% as DoT
	totalDamage += dotDamage

	response.TotalDamage = totalDamage
	response.ElementalDamage = elementalDamage
	response.DotDamage = dotDamage

	if elementalDamage > 0 {
		response.StatusEffects = []string{"corrode", "armor_degradation"}
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
// GetInteractions returns acid-specific elemental interactions
func (p *AcidEffectProcessor) GetInteractions(targetEffects []types.ActiveEffect) []types.Interaction {
	interactions := []types.Interaction{}

	for _, effect := range targetEffects {
		switch effect.EffectID {
		case "ice":
			// Acid vs Ice: corrosive freeze (handled by ice processor)
			interactions = append(interactions, types.Interaction{
				Type:           types.InteractionTypeAmplify,
				PrimaryEffect:  "acid",
				SecondaryEffect: "ice",
				Multiplier:      1.4,
				Description:     "Коррозионный мороз: кислота + лед = усиленная коррозия",
				SpecialEffects:  []string{"corrosive_freeze", "armor_melt"},
			})
		case "poison":
			// Acid vs Poison: chemical amplification (handled by poison processor)
			interactions = append(interactions, types.Interaction{
				Type:           types.InteractionTypeAmplify,
				PrimaryEffect:  "acid",
				SecondaryEffect: "poison",
				Multiplier:      1.6,
				Description:     "Химическое усиление: кислота + яд = мощный токсин",
				SpecialEffects:  []string{"chemical_burn", "corrosive_poison"},
			})
		case "electric":
			// Acid vs Electric: conductive corrosion
			interactions = append(interactions, types.Interaction{
				Type:           types.InteractionTypeSynergy,
				PrimaryEffect:  "acid",
				SecondaryEffect: "electric",
				Multiplier:      1.5,
				Description:     "Проводящая коррозия: кислота + электричество = электролит",
				SpecialEffects:  []string{"conductive_acid", "electrolyte_damage"},
			})
		}
	}

	return interactions
}

// Issue: #143577551
// calculateEnvironmentalModifier calculates environmental impact on acid effects
func (p *AcidEffectProcessor) calculateEnvironmentalModifier(env types.EnvironmentContext) float64 {
	modifier := 1.0

	// Temperature affects acid reaction speed
	if env.Temperature > 40 {
		modifier *= 1.4 // High heat accelerates acid reactions
	} else if env.Temperature < 0 {
		modifier *= 0.6 // Freezing slows acid reactions
	}

	// Humidity affects acid concentration
	if env.Humidity > 70 {
		modifier *= 0.8 // High humidity dilutes acid
	} else if env.Humidity < 30 {
		modifier *= 1.2 // Low humidity concentrates acid
	}

	// pH level would affect acid strength (simplified)
	if env.TerrainType == "industrial" {
		modifier *= 1.1 // Industrial areas may have more acidic conditions
	}

	// Weather effects
	switch env.WeatherType {
	case "acid_rain":
		modifier *= 1.5 // Acid rain enhances acid effects
	case "rain":
		modifier *= 0.7 // Normal rain dilutes acid
	case "fog":
		modifier *= 1.1 // Fog can help maintain acid concentration
	}

	return math.Max(0.1, modifier)
}

