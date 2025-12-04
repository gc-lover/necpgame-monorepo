// Issue: #1595 - ogen handlers (TYPED responses)
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/gameplay-weapon-special-mechanics-service-go/pkg/api"
)

const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
)

type Handlers struct {
	service *Service
}

func NewHandlers(service *Service) *Handlers {
	return &Handlers{service: service}
}

func (h *Handlers) ApplySpecialMechanics(ctx context.Context, req *api.ApplySpecialMechanicsRequest) (api.ApplySpecialMechanicsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.ApplySpecialMechanics(ctx, req)
	if err != nil {
		return &api.ApplySpecialMechanicsInternalServerError{}, err
	}

	return result, nil
}

func (h *Handlers) CalculateChainDamage(ctx context.Context, req *api.CalculateChainDamageRequest) (api.CalculateChainDamageRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.CalculateChainDamage(ctx, req)
	if err != nil {
		return &api.CalculateChainDamageInternalServerError{}, err
	}

	return result, nil
}

func (h *Handlers) CreatePersistentEffect(ctx context.Context, req *api.CreatePersistentEffectRequest) (api.CreatePersistentEffectRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.CreatePersistentEffect(ctx, req)
	if err != nil {
		return &api.CreatePersistentEffectInternalServerError{}, err
	}

	return result, nil
}

func (h *Handlers) DestroyEnvironment(ctx context.Context, req *api.DestroyEnvironmentRequest) (api.DestroyEnvironmentRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.DestroyEnvironment(ctx, req)
	if err != nil {
		return &api.DestroyEnvironmentInternalServerError{}, err
	}

	return result, nil
}

func (h *Handlers) GetPersistentEffects(ctx context.Context, params api.GetPersistentEffectsParams) (api.GetPersistentEffectsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetPersistentEffects(ctx, params.TargetId)
	if err != nil {
		return &api.GetPersistentEffectsInternalServerError{}, err
	}

	return result, nil
}

func (h *Handlers) GetWeaponSpecialMechanics(ctx context.Context, params api.GetWeaponSpecialMechanicsParams) (api.GetWeaponSpecialMechanicsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetWeaponSpecialMechanics(ctx, params.WeaponId)
	if err != nil {
		return &api.GetWeaponSpecialMechanicsInternalServerError{}, err
	}

	return result, nil
}

