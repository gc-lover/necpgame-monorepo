// Issue: #1595
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"time"

	"github.com/necpgame/combat-damage-service-go/pkg/api"
)

const DBTimeout = 50 * time.Millisecond

// Handlers implements api.Handler interface (ogen typed handlers!)
type Handlers struct{}

// NewHandlers creates new handlers
func NewHandlers() *Handlers {
	return &Handlers{}
}

// CalculateDamage - TYPED response!
func (h *Handlers) CalculateDamage(ctx context.Context, req *api.DamageCalculationRequest) (api.CalculateDamageRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	finalDamage := req.BaseDamage
	wasCritical := false

	// Handle modifiers using ogen optional types
	if req.Modifiers.IsSet() {
		mods := req.Modifiers.Value

		// Check critical hit
		if mods.IsCritical.IsSet() && mods.IsCritical.Value {
			finalDamage = finalDamage * 2
			wasCritical = true
		}

		// Check weak spot
		if mods.WeakSpot.IsSet() && mods.WeakSpot.Value {
			finalDamage = int(float32(finalDamage) * 1.5)
		}

		// Check range modifier
		if mods.RangeModifier.IsSet() {
			finalDamage = int(float32(finalDamage) * mods.RangeModifier.Value)
		}
	}

	damageType := api.DamageCalculationResultDamageType(req.DamageType)

	result := &api.DamageCalculationResult{
		AttackerID:      api.NewOptUUID(req.AttackerID),
		TargetID:        api.NewOptUUID(req.TargetID),
		BaseDamage:      api.NewOptInt(req.BaseDamage),
		FinalDamage:     api.NewOptInt(finalDamage),
		DamageType:      api.NewOptDamageCalculationResultDamageType(damageType),
		WasCritical:     api.NewOptBool(wasCritical),
		WasBlocked:      api.NewOptBool(false),
		DamageReduction: api.NewOptInt(0),
		ModifiersApplied: []api.DamageCalculationResultModifiersAppliedItem{},
	}

	return result, nil
}

// ApplyEffects - TYPED response!
func (h *Handlers) ApplyEffects(ctx context.Context, req *api.ApplyEffectsRequest) (api.ApplyEffectsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	effects := make([]api.CombatEffect, 0, len(req.Effects))
	for _, effectReq := range req.Effects {
		effectType := api.CombatEffectEffectType(effectReq.EffectType)
		effect := api.CombatEffect{
			EffectName:     api.NewOptString(effectReq.EffectName),
			EffectType:     api.NewOptCombatEffectEffectType(effectType),
			Value:          api.NewOptInt(effectReq.Value),
			Duration:       api.NewOptInt(effectReq.Duration),
			RemainingTurns: api.NewOptInt(effectReq.Duration),
		}
		effects = append(effects, effect)
	}

	result := &api.ApplyEffectsOK{
		Effects: effects,
	}

	return result, nil
}

// RemoveEffect - TYPED response!
func (h *Handlers) RemoveEffect(ctx context.Context, params api.RemoveEffectParams) (api.RemoveEffectRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement business logic
	result := &api.RemoveEffectOK{
		Status: api.NewOptString("removed"),
	}

	return result, nil
}

// ExtendEffect - TYPED response!
func (h *Handlers) ExtendEffect(ctx context.Context, req *api.ExtendEffectReq, params api.ExtendEffectParams) (api.ExtendEffectRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement business logic
	result := &api.ExtendEffectOK{
		Status: api.NewOptString("extended"),
	}

	return result, nil
}
