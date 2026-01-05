// Issue: #143577551
// Package manager provides the main elemental effects management system
package manager

import (
	"context"
	"fmt"
	"sync"
	"time"

	"github.com/necpgame/necpgame/services/effect-service-go/internal/effects/calculator"
	"github.com/necpgame/necpgame/services/effect-service-go/internal/effects/processors"
	"github.com/necpgame/necpgame/services/effect-service-go/internal/effects/types"
)

// Issue: #143577551
// ElementalEffectManager manages all elemental effects and their processors
type ElementalEffectManager struct {
	processors          map[types.ElementalType]types.EffectProcessor
	interactionCalculator *calculator.ElementalInteractionCalculator
	activeEffects       map[string]*types.ActiveEffect
	mu                  sync.RWMutex
}

// Issue: #143577551
// NewElementalEffectManager creates a new effect manager with all processors
func NewElementalEffectManager() *ElementalEffectManager {
	manager := &ElementalEffectManager{
		processors:          make(map[types.ElementalType]types.EffectProcessor),
		interactionCalculator: calculator.NewElementalInteractionCalculator(),
		activeEffects:       make(map[string]*types.ActiveEffect),
	}

	// Register all effect processors
	manager.registerProcessors()

	return manager
}

// Issue: #143577551
// ApplyEffect applies an elemental effect to a target
func (m *ElementalEffectManager) ApplyEffect(ctx context.Context, req types.EffectApplicationRequest) (types.EffectApplicationResponse, error) {
	// Get the appropriate processor
	elementType := types.ElementalType(req.EffectID)
	processor, exists := m.processors[elementType]
	if !exists {
		return types.EffectApplicationResponse{
			Success: false,
			Error:   fmt.Sprintf("Unknown elemental type: %s", req.EffectID),
		}, nil
	}

	// Process the effect application
	response, err := processor.ProcessEffect(req)
	if err != nil {
		return types.EffectApplicationResponse{
			Success: false,
			Error:   err.Error(),
		}, err
	}

	// Create and store active effect if successful
	if response.Success && response.ActiveEffectID != "" {
		activeEffect := &types.ActiveEffect{
			ID:        response.ActiveEffectID,
			EffectID:  req.EffectID,
			TargetID:  req.TargetID,
			SourceID:  req.AttackerID,
			AppliedAt: time.Now(),
			Duration:  req.Duration,
			Stacks:    1,
			MaxStacks: 3, // Default max stacks
			Metadata:  req.Metadata,
			IsExpired: false,
		}

		// Set expiration if duration is specified
		if req.Duration > 0 {
			expiresAt := time.Now().Add(time.Duration(req.Duration) * time.Second)
			activeEffect.ExpiresAt = &expiresAt
		}

		m.mu.Lock()
		m.activeEffects[response.ActiveEffectID] = activeEffect
		m.mu.Unlock()
	}

	// Calculate interactions with existing effects
	if response.Success {
		targetEffects := m.getActiveEffectsForTarget(req.TargetID)
		interactions := m.interactionCalculator.CalculateInteractions(targetEffects, req.Environment)
		response.Interactions = interactions
	}

	return response, nil
}

// Issue: #143577551
// CalculateDamage calculates damage for an elemental effect
func (m *ElementalEffectManager) CalculateDamage(req types.DamageCalculationRequest) types.DamageCalculationResponse {
	elementType := req.ElementType
	processor, exists := m.processors[elementType]
	if !exists {
		return types.DamageCalculationResponse{}
	}

	response := processor.CalculateDamage(req)

	// Apply interaction modifiers
	activeEffects := req.ActiveEffects
	if len(activeEffects) > 0 {
		interactions := m.interactionCalculator.CalculateInteractions(activeEffects, req.Environment)

		for _, interaction := range interactions {
			// Apply interaction modifiers to damage breakdown
			response.Breakdown.InteractionMods = append(response.Breakdown.InteractionMods, types.InteractionMod{
				Effect:     interaction.PrimaryEffect + "+" + interaction.SecondaryEffect,
				Type:       string(interaction.Type),
				Multiplier: interaction.Multiplier,
				Description: interaction.Description,
			})

			// Apply the multiplier to total damage
			response.TotalDamage = int(float64(response.TotalDamage) * interaction.Multiplier)
		}
	}

	return response
}

// Issue: #143577551
// GetActiveEffects returns all active effects for a target
func (m *ElementalEffectManager) GetActiveEffects(targetID string) []types.ActiveEffect {
	m.mu.RLock()
	defer m.mu.RUnlock()

	var activeEffects []types.ActiveEffect
	for _, effect := range m.activeEffects {
		if effect.TargetID == targetID && !effect.IsExpired {
			activeEffects = append(activeEffects, *effect)
		}
	}

	return activeEffects
}

// Issue: #143577551
// ExpireEffect marks an effect as expired
func (m *ElementalEffectManager) ExpireEffect(effectID string) error {
	m.mu.Lock()
	defer m.mu.Unlock()

	effect, exists := m.activeEffects[effectID]
	if !exists {
		return fmt.Errorf("effect not found: %s", effectID)
	}

	effect.IsExpired = true
	return nil
}

// Issue: #143577551
// CleanupExpiredEffects removes expired effects from memory
func (m *ElementalEffectManager) CleanupExpiredEffects() int {
	m.mu.Lock()
	defer m.mu.Unlock()

	cleaned := 0
	now := time.Now()

	for id, effect := range m.activeEffects {
		if effect.IsExpired {
			delete(m.activeEffects, id)
			cleaned++
		} else if effect.ExpiresAt != nil && now.After(*effect.ExpiresAt) {
			effect.IsExpired = true
			delete(m.activeEffects, id)
			cleaned++
		}
	}

	return cleaned
}

// Issue: #143577551
// GetProcessor returns the processor for a specific elemental type
func (m *ElementalEffectManager) GetProcessor(elementType types.ElementalType) (types.EffectProcessor, bool) {
	processor, exists := m.processors[elementType]
	return processor, exists
}

// Issue: #143577551
// GetInteractionCalculator returns the interaction calculator
func (m *ElementalEffectManager) GetInteractionCalculator() *calculator.ElementalInteractionCalculator {
	return m.interactionCalculator
}

// Issue: #143577551
// registerProcessors registers all available effect processors
func (m *ElementalEffectManager) registerProcessors() {
	m.processors[types.ElementalTypeFire] = &processors.FireEffectProcessor{}
	m.processors[types.ElementalTypeIce] = &processors.IceEffectProcessor{}
	m.processors[types.ElementalTypeElectric] = &processors.ElectricEffectProcessor{}
	m.processors[types.ElementalTypeAcid] = &processors.AcidEffectProcessor{}
	m.processors[types.ElementalTypePoison] = &processors.PoisonEffectProcessor{}
	m.processors[types.ElementalTypeVoid] = &processors.VoidEffectProcessor{}
}

// Issue: #143577551
// getActiveEffectsForTarget returns active effects for a specific target
func (m *ElementalEffectManager) getActiveEffectsForTarget(targetID string) []types.ActiveEffect {
	m.mu.RLock()
	defer m.mu.RUnlock()

	var effects []types.ActiveEffect
	for _, effect := range m.activeEffects {
		if effect.TargetID == targetID && !effect.IsExpired {
			effects = append(effects, *effect)
		}
	}

	return effects
}
