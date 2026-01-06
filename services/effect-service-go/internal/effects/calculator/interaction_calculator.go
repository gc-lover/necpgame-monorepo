// Issue: #143577551
// Package calculator implements elemental interaction calculations
package calculator

import (
	"sort"

	"necpgame/services/effect-service-go/internal/effects/types"
)

// Issue: #143577551
// ElementalInteractionCalculator handles complex elemental effect interactions
type ElementalInteractionCalculator struct {
	// Predefined interaction matrix
	interactionMatrix map[string]map[string]types.Interaction
}

// Issue: #143577551
// NewElementalInteractionCalculator creates a new interaction calculator
func NewElementalInteractionCalculator() *ElementalInteractionCalculator {
	calc := &ElementalInteractionCalculator{
		interactionMatrix: make(map[string]map[string]types.Interaction),
	}
	calc.initializeInteractionMatrix()
	return calc
}

// Issue: #143577551
// CalculateInteractions calculates all interactions between active effects
func (c *ElementalInteractionCalculator) CalculateInteractions(effects []types.ActiveEffect, environment types.EnvironmentContext) []types.Interaction {
	interactions := []types.Interaction{}

	// Group effects by elemental type
	elementCounts := make(map[string]int)
	elementEffects := make(map[string][]types.ActiveEffect)

	for _, effect := range effects {
		elementCounts[effect.EffectID]++
		elementEffects[effect.EffectID] = append(elementEffects[effect.EffectID], effect)
	}

	// Check for pair-wise interactions
	elementalTypes := make([]string, 0, len(elementCounts))
	for elementType := range elementCounts {
		elementalTypes = append(elementalTypes, elementType)
	}

	// Sort for consistent processing
	sort.Strings(elementalTypes)

	// Check all pairs for interactions
	for i := 0; i < len(elementalTypes); i++ {
		for j := i + 1; j < len(elementalTypes); j++ {
			element1 := elementalTypes[i]
			element2 := elementalTypes[j]

			if interaction, exists := c.interactionMatrix[element1][element2]; exists {
				// Apply environmental modifiers to interaction
				modifiedInteraction := c.applyEnvironmentalModifiers(interaction, environment)
				interactions = append(interactions, modifiedInteraction)
			}
		}
	}

	// Check for triple interactions (rare but possible)
	if len(elementalTypes) >= 3 {
		interactions = append(interactions, c.calculateTripleInteractions(elementalTypes, environment)...)
	}

	return interactions
}

// Issue: #143577551
// GetSynergyMultiplier returns synergy multiplier between two elements
func (c *ElementalInteractionCalculator) GetSynergyMultiplier(primary, secondary types.ElementalType) float64 {
	if interaction, exists := c.interactionMatrix[string(primary)][string(secondary)]; exists {
		if interaction.Type == types.InteractionTypeSynergy {
			return interaction.Multiplier
		}
	}
	return 1.0
}

// Issue: #143577551
// GetConflictMultiplier returns conflict multiplier between two elements
func (c *ElementalInteractionCalculator) GetConflictMultiplier(primary, secondary types.ElementalType) float64 {
	if interaction, exists := c.interactionMatrix[string(primary)][string(secondary)]; exists {
		if interaction.Type == types.InteractionTypeConflict {
			return interaction.Multiplier
		}
	}
	return 1.0
}

// Issue: #143577551
// initializeInteractionMatrix sets up the base interaction rules
func (c *ElementalInteractionCalculator) initializeInteractionMatrix() {
	// Initialize nested maps
	elements := []string{"fire", "ice", "electric", "acid", "poison", "void"}
	for _, elem1 := range elements {
		c.interactionMatrix[elem1] = make(map[string]types.Interaction)
		for _, elem2 := range elements {
			if elem1 != elem2 {
				c.interactionMatrix[elem1][elem2] = c.getBaseInteraction(elem1, elem2)
			}
		}
	}
}

// Issue: #143577551
// getBaseInteraction returns the base interaction between two elements
func (c *ElementalInteractionCalculator) getBaseInteraction(elem1, elem2 string) types.Interaction {
	switch {
	// Fire + Ice = Steam explosion
	case (elem1 == "fire" && elem2 == "ice") || (elem1 == "ice" && elem2 == "fire"):
		return types.Interaction{
			Type:           types.InteractionTypeTransform,
			PrimaryEffect:  elem1,
			SecondaryEffect: elem2,
			Multiplier:      2.0,
			Description:     "Паровой взрыв: огонь + лед создают мощный взрыв пара",
			SpecialEffects:  []string{"steam_explosion", "area_damage", "knockback"},
		}

	// Fire + Poison = Toxic combustion
	case (elem1 == "fire" && elem2 == "poison") || (elem1 == "poison" && elem2 == "fire"):
		return types.Interaction{
			Type:           types.InteractionTypeAmplify,
			PrimaryEffect:  elem1,
			SecondaryEffect: elem2,
			Multiplier:      1.5,
			Description:     "Токсичное горение: огонь усиливает распространение яда",
			SpecialEffects:  []string{"toxic_cloud", "area_poison", "burning_toxin"},
		}

	// Fire + Electric = Plasma
	case (elem1 == "fire" && elem2 == "electric") || (elem1 == "electric" && elem2 == "fire"):
		return types.Interaction{
			Type:           types.InteractionTypeSynergy,
			PrimaryEffect:  elem1,
			SecondaryEffect: elem2,
			Multiplier:      1.8,
			Description:     "Плазменный эффект: огонь + электричество создают плазму",
			SpecialEffects:  []string{"plasma_damage", "ion_burst", "chain_reaction"},
		}

	// Ice + Electric = Conductive ice
	case (elem1 == "ice" && elem2 == "electric") || (elem1 == "electric" && elem2 == "ice"):
		return types.Interaction{
			Type:           types.InteractionTypeSynergy,
			PrimaryEffect:  elem1,
			SecondaryEffect: elem2,
			Multiplier:      1.6,
			Description:     "Проводящий лед: лед создает проводящие поверхности для электричества",
			SpecialEffects:  []string{"conductive_surface", "chain_lightning", "shatter_chain"},
		}

	// Ice + Acid = Corrosive freeze
	case (elem1 == "ice" && elem2 == "acid") || (elem1 == "acid" && elem2 == "ice"):
		return types.Interaction{
			Type:           types.InteractionTypeAmplify,
			PrimaryEffect:  elem1,
			SecondaryEffect: elem2,
			Multiplier:      1.4,
			Description:     "Коррозионный мороз: лед замедляет, кислота разъедает",
			SpecialEffects:  []string{"corrosive_freeze", "armor_melt", "slow_corrosion"},
		}

	// Poison + Electric = Neurotoxin
	case (elem1 == "poison" && elem2 == "electric") || (elem1 == "electric" && elem2 == "poison"):
		return types.Interaction{
			Type:           types.InteractionTypeSynergy,
			PrimaryEffect:  elem1,
			SecondaryEffect: elem2,
			Multiplier:      1.7,
			Description:     "Нейротоксин: электричество усиливает нервно-паралитический яд",
			SpecialEffects:  []string{"neurotoxin", "paralysis_risk", "nerve_damage"},
		}

	// Poison + Acid = Chemical amplification
	case (elem1 == "poison" && elem2 == "acid") || (elem1 == "acid" && elem2 == "poison"):
		return types.Interaction{
			Type:           types.InteractionTypeAmplify,
			PrimaryEffect:  elem1,
			SecondaryEffect: elem2,
			Multiplier:      1.6,
			Description:     "Химическое усиление: кислота + яд создают мощные токсины",
			SpecialEffects:  []string{"chemical_burn", "corrosive_poison", "acid_toxin"},
		}

	// Acid + Electric = Conductive corrosion
	case (elem1 == "acid" && elem2 == "electric") || (elem1 == "electric" && elem2 == "acid"):
		return types.Interaction{
			Type:           types.InteractionTypeSynergy,
			PrimaryEffect:  elem1,
			SecondaryEffect: elem2,
			Multiplier:      1.5,
			Description:     "Проводящая коррозия: кислота становится проводником электричества",
			SpecialEffects:  []string{"conductive_acid", "electrolyte_damage", "corrosive_shock"},
		}

	// Void + Any element = Absorption
	case elem1 == "void" || elem2 == "void":
		voidElement := elem1
		otherElement := elem2
		if elem2 == "void" {
			voidElement = elem2
			otherElement = elem1
		}
		return types.Interaction{
			Type:           types.InteractionTypeNeutralize,
			PrimaryEffect:  voidElement,
			SecondaryEffect: otherElement,
			Multiplier:      0.3,
			Description:     "Поглощение стихии: пустота поглощает и ослабляет другие элементы",
			SpecialEffects:  []string{"element_absorption", "void_amplification", "resistance_drain"},
		}

	default:
		// Neutral interaction
		return types.Interaction{
			Type:           types.InteractionTypeNeutralize,
			PrimaryEffect:  elem1,
			SecondaryEffect: elem2,
			Multiplier:      0.9,
			Description:     "Нейтральное взаимодействие: элементы слегка ослабляют друг друга",
			SpecialEffects:  []string{},
		}
	}
}

// Issue: #143577551
// applyEnvironmentalModifiers applies environmental factors to interactions
func (c *ElementalInteractionCalculator) applyEnvironmentalModifiers(interaction types.Interaction, env types.EnvironmentContext) types.Interaction {
	modified := interaction

	// Environmental modifiers based on interaction type
	switch interaction.Type {
	case types.InteractionTypeTransform:
		// Transformations are affected by extreme conditions
		if env.Temperature > 50 || env.Temperature < -30 {
			modified.Multiplier *= 1.3 // Extreme temperatures enhance transformations
		}
		if env.WeatherType == "storm" {
			modified.Multiplier *= 1.2 // Storms enhance transformative effects
		}

	case types.InteractionTypeSynergy:
		// Synergies benefit from conductive environments
		if env.Humidity > 70 {
			modified.Multiplier *= 1.1 // Humidity helps synergies
		}
		if env.ElectricField > 50 {
			modified.Multiplier *= 1.2 // Electric fields enhance synergies
		}

	case types.InteractionTypeAmplify:
		// Amplifications benefit from chemical catalysts
		if env.Temperature > 30 {
			modified.Multiplier *= 1.1 // Heat accelerates chemical reactions
		}
		if env.Radiation > 25 {
			modified.Multiplier *= 1.15 // Radiation enhances chemical effects
		}

	case types.InteractionTypeNeutralize:
		// Void absorption is stronger in dark, empty places
		if env.TimeOfDay == "night" && (env.TerrainType == "desert" || env.TerrainType == "urban") {
			modified.Multiplier *= 0.7 // Stronger void absorption in desolate areas
		}
	}

	return modified
}

// Issue: #143577551
// calculateTripleInteractions handles rare triple element interactions
func (c *ElementalInteractionCalculator) calculateTripleInteractions(elements []string, env types.EnvironmentContext) []types.Interaction {
	interactions := []types.Interaction{}

	// Fire + Ice + Electric = Plasma storm
	if containsAll(elements, []string{"fire", "ice", "electric"}) {
		interactions = append(interactions, types.Interaction{
			Type:           types.InteractionTypeTransform,
			PrimaryEffect:  "fire+ice+electric",
			SecondaryEffect: "combined",
			Multiplier:      2.5,
			Description:     "Плазменная буря: огонь + лед + электричество создают разрушительную плазменную бурю",
			SpecialEffects:  []string{"plasma_storm", "massive_area_damage", "chain_reactions", "environmental_hazard"},
		})
	}

	// Poison + Acid + Electric = Chemical weapon
	if containsAll(elements, []string{"poison", "acid", "electric"}) {
		interactions = append(interactions, types.Interaction{
			Type:           types.InteractionTypeAmplify,
			PrimaryEffect:  "poison+acid+electric",
			SecondaryEffect: "combined",
			Multiplier:      2.2,
			Description:     "Химическое оружие: яд + кислота + электричество создают нервно-паралитический газ",
			SpecialEffects:  []string{"chemical_weapon", "nerve_gas", "area_contamination", "long_term_effects"},
		})
	}

	return interactions
}

// Issue: #143577551
// containsAll checks if slice contains all specified elements
func containsAll(slice, elements []string) bool {
	elementMap := make(map[string]bool)
	for _, item := range slice {
		elementMap[item] = true
	}

	for _, element := range elements {
		if !elementMap[element] {
			return false
		}
	}
	return true
}
