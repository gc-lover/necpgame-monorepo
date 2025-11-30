// Issue: #142109884
package server

import (
	"context"
	"errors"
	"math"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
)

type DamageModifiers struct {
	IsCritical   bool
	WeakSpot     bool
	RangeModifier float32
}

type EffectRequest struct {
	EffectType string
	EffectName string
	Duration   int
	Value      int
}

type DamageCalculationResult struct {
	AttackerID       uuid.UUID
	TargetID         uuid.UUID
	BaseDamage       int
	FinalDamage      int
	DamageType       string
	ModifiersApplied []ModifierApplied
	WasCritical      bool
	WasBlocked       bool
	DamageReduction  int
}

type ModifierApplied struct {
	Name  string
	Value float32
}

type CombatEffect struct {
	ID            uuid.UUID
	EffectType    string
	EffectName    string
	Duration      int
	RemainingTurns int
	Value         int
	AppliedAt     string
}

type DamageRepositoryInterface interface {
	CalculateDamage(ctx context.Context, attackerID uuid.UUID, targetID uuid.UUID, baseDamage int, damageType string, modifiers *DamageModifiers) (*DamageCalculationResult, error)
	ApplyEffects(ctx context.Context, targetID uuid.UUID, effects []EffectRequest) ([]CombatEffect, error)
	RemoveEffect(ctx context.Context, effectID uuid.UUID) error
	ExtendEffect(ctx context.Context, effectID uuid.UUID, additionalTurns int) error
}

type DamageRepository struct {
	db *pgxpool.Pool
}

func NewDamageRepository(db *pgxpool.Pool) *DamageRepository {
	return &DamageRepository{
		db: db,
	}
}

func (r *DamageRepository) CalculateDamage(ctx context.Context, attackerID uuid.UUID, targetID uuid.UUID, baseDamage int, damageType string, modifiers *DamageModifiers) (*DamageCalculationResult, error) {
	result := &DamageCalculationResult{
		AttackerID:       attackerID,
		TargetID:         targetID,
		BaseDamage:       baseDamage,
		FinalDamage:      baseDamage,
		DamageType:       damageType,
		ModifiersApplied: []ModifierApplied{},
		WasCritical:      false,
		WasBlocked:       false,
		DamageReduction:  0,
	}

	var attackerStrength, attackerAgility, attackerIntelligence float32
	var targetArmor, targetResistance float32

	err := r.db.QueryRow(ctx, `
		SELECT 
			COALESCE(strength, 0)::float,
			COALESCE(agility, 0)::float,
			COALESCE(intelligence, 0)::float
		FROM character_stats
		WHERE character_id = $1
	`, attackerID).Scan(&attackerStrength, &attackerAgility, &attackerIntelligence)

	if err != nil {
		attackerStrength = 0
		attackerAgility = 0
		attackerIntelligence = 0
	}

	err = r.db.QueryRow(ctx, `
		SELECT 
			COALESCE(armor, 0)::float,
			COALESCE(resistance, 0)::float
		FROM character_stats
		WHERE character_id = $1
	`, targetID).Scan(&targetArmor, &targetResistance)

	if err != nil {
		targetArmor = 0
		targetResistance = 0
	}

	damage := float32(baseDamage)
	damage += attackerStrength * 0.1
	damage += attackerAgility * 0.05
	damage += attackerIntelligence * 0.05

	if modifiers != nil {
		if modifiers.IsCritical {
			damage *= 2.0
			result.WasCritical = true
			result.ModifiersApplied = append(result.ModifiersApplied, ModifierApplied{
				Name:  "critical_multiplier",
				Value: 2.0,
			})
		}

		if modifiers.WeakSpot {
			damage *= 1.5
			result.ModifiersApplied = append(result.ModifiersApplied, ModifierApplied{
				Name:  "weak_spot_bonus",
				Value: 1.5,
			})
		}

		damage *= modifiers.RangeModifier
		if modifiers.RangeModifier != 1.0 {
			result.ModifiersApplied = append(result.ModifiersApplied, ModifierApplied{
				Name:  "range_modifier",
				Value: modifiers.RangeModifier,
			})
		}
	}

	reduction := targetArmor * 0.25 + targetResistance * 0.15
	damage -= reduction

	if damage < 0 {
		damage = 0
	}

	result.FinalDamage = int(math.Round(float64(damage)))
	result.DamageReduction = int(math.Round(float64(reduction)))

	if reduction > 0 {
		result.ModifiersApplied = append(result.ModifiersApplied, ModifierApplied{
			Name:  "armor_reduction",
			Value: -reduction,
		})
	}

	return result, nil
}

func (r *DamageRepository) ApplyEffects(ctx context.Context, targetID uuid.UUID, effects []EffectRequest) ([]CombatEffect, error) {
	var appliedEffects []CombatEffect

	for _, effect := range effects {
		effectID := uuid.New()
		_, err := r.db.Exec(ctx, `
			INSERT INTO combat_effects (id, target_id, effect_type, effect_name, duration, remaining_turns, value)
			VALUES ($1, $2, $3, $4, $5, $5, $6)
		`, effectID, targetID, effect.EffectType, effect.EffectName, effect.Duration, effect.Value)

		if err != nil {
			continue
		}

		appliedEffects = append(appliedEffects, CombatEffect{
			ID:             effectID,
			EffectType:     effect.EffectType,
			EffectName:     effect.EffectName,
			Duration:       effect.Duration,
			RemainingTurns: effect.Duration,
			Value:          effect.Value,
		})
	}

	return appliedEffects, nil
}

func (r *DamageRepository) RemoveEffect(ctx context.Context, effectID uuid.UUID) error {
	result, err := r.db.Exec(ctx, `
		DELETE FROM combat_effects WHERE id = $1
	`, effectID)

	if err != nil {
		return err
	}

	if result.RowsAffected() == 0 {
		return errors.New("effect not found")
	}

	return nil
}

func (r *DamageRepository) ExtendEffect(ctx context.Context, effectID uuid.UUID, additionalTurns int) error {
	result, err := r.db.Exec(ctx, `
		UPDATE combat_effects 
		SET remaining_turns = remaining_turns + $1, duration = duration + $1
		WHERE id = $2
	`, additionalTurns, effectID)

	if err != nil {
		return err
	}

	if result.RowsAffected() == 0 {
		return errors.New("effect not found")
	}

	return nil
}



