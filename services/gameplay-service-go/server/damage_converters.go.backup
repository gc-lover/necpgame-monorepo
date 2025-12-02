// Issue: #142109884
package server

import (
	"github.com/necpgame/gameplay-service-go/pkg/damageapi"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

func convertDamageCalculationResultToAPI(result *DamageCalculationResult) *damageapi.DamageCalculationResult {
	attackerID := openapi_types.UUID(result.AttackerID)
	targetID := openapi_types.UUID(result.TargetID)
	damageType := damageapi.DamageCalculationResultDamageType(result.DamageType)

	modifiers := make([]struct {
		Name  *string  `json:"name,omitempty"`
		Value *float32 `json:"value,omitempty"`
	}, len(result.ModifiersApplied))

	for i, mod := range result.ModifiersApplied {
		name := mod.Name
		value := mod.Value
		modifiers[i] = struct {
			Name  *string  `json:"name,omitempty"`
			Value *float32 `json:"value,omitempty"`
		}{
			Name:  &name,
			Value: &value,
		}
	}

	return &damageapi.DamageCalculationResult{
		AttackerId:       &attackerID,
		TargetId:         &targetID,
		BaseDamage:       &result.BaseDamage,
		FinalDamage:      &result.FinalDamage,
		DamageType:       &damageType,
		ModifiersApplied: &modifiers,
		WasCritical:      &result.WasCritical,
		WasBlocked:       &result.WasBlocked,
		DamageReduction:  &result.DamageReduction,
	}
}

func convertApplyEffectsResultToAPI(effects []CombatEffect) *damageapi.ApplyEffectsJSON200Response {
	apiEffects := make([]damageapi.CombatEffect, len(effects))

	for i, effect := range effects {
		id := openapi_types.UUID(effect.ID)
		effectType := damageapi.CombatEffectEffectType(effect.EffectType)

		apiEffects[i] = damageapi.CombatEffect{
			Id:             &id,
			EffectType:     &effectType,
			EffectName:     &effect.EffectName,
			Duration:       &effect.Duration,
			RemainingTurns: &effect.RemainingTurns,
			Value:          &effect.Value,
			AppliedAt:      &effect.AppliedAt,
		}
	}

	return &damageapi.ApplyEffectsJSON200Response{
		Effects: &apiEffects,
	}
}






