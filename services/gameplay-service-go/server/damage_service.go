// Issue: #142109884
package server

import (
	"context"
	"errors"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/gameplay-service-go/pkg/damageapi"
	"github.com/sirupsen/logrus"
)

type DamageServiceInterface interface {
	CalculateDamage(ctx context.Context, attackerID uuid.UUID, targetID uuid.UUID, baseDamage int, damageType string, modifiers *DamageModifiers) (*damageapi.DamageCalculationResult, error)
	ApplyEffects(ctx context.Context, targetID uuid.UUID, effects []EffectRequest) (*damageapi.ApplyEffectsJSON200Response, error)
	RemoveEffect(ctx context.Context, effectID uuid.UUID) error
	ExtendEffect(ctx context.Context, effectID uuid.UUID, additionalTurns int) error
}

type DamageService struct {
	repo   DamageRepositoryInterface
	logger *logrus.Logger
}

func NewDamageService(db *pgxpool.Pool) *DamageService {
	return &DamageService{
		repo:   NewDamageRepository(db),
		logger: GetLogger(),
	}
}

func (s *DamageService) CalculateDamage(ctx context.Context, attackerID uuid.UUID, targetID uuid.UUID, baseDamage int, damageType string, modifiers *DamageModifiers) (*damageapi.DamageCalculationResult, error) {
	result, err := s.repo.CalculateDamage(ctx, attackerID, targetID, baseDamage, damageType, modifiers)
	if err != nil {
		return nil, err
	}

	return convertDamageCalculationResultToAPI(result), nil
}

func (s *DamageService) ApplyEffects(ctx context.Context, targetID uuid.UUID, effects []EffectRequest) (*damageapi.ApplyEffectsJSON200Response, error) {
	result, err := s.repo.ApplyEffects(ctx, targetID, effects)
	if err != nil {
		return nil, err
	}

	return convertApplyEffectsResultToAPI(result), nil
}

func (s *DamageService) RemoveEffect(ctx context.Context, effectID uuid.UUID) error {
	return s.repo.RemoveEffect(ctx, effectID)
}

func (s *DamageService) ExtendEffect(ctx context.Context, effectID uuid.UUID, additionalTurns int) error {
	return s.repo.ExtendEffect(ctx, effectID, additionalTurns)
}






