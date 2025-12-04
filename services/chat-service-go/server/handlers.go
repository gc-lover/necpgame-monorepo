// Issue: #1595
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/chat-service-go/pkg/api"
)

// Context timeout constants
const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
)

// Handlers implements api.Handler interface (ogen typed handlers!)
type Handlers struct {
	service *Service
}

// NewHandlers creates new handlers
func NewHandlers(service *Service) *Handlers {
	return &Handlers{service: service}
}

// ApplyEffects - TYPED response!
func (h *Handlers) ApplyEffects(ctx context.Context, req *api.ApplyEffectsRequest) (api.ApplyEffectsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.ApplyEffects(ctx, req)
	if err != nil {
		return &api.ApplyEffectsInternalServerError{}, err
	}

	// Return TYPED response (ogen will marshal directly!)
	return result, nil
}

// CalculateDamage - TYPED response!
func (h *Handlers) CalculateDamage(ctx context.Context, req *api.CalculateDamageRequest) (api.CalculateDamageRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.CalculateDamage(ctx, req)
	if err != nil {
		return &api.CalculateDamageInternalServerError{}, err
	}

	return result, nil
}

// DefendInCombat - TYPED response!
func (h *Handlers) DefendInCombat(ctx context.Context, req *api.DefendRequest, params api.DefendInCombatParams) (api.DefendInCombatRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.DefendInCombat(ctx, params.SessionId.String(), req)
	if err != nil {
		if err == ErrNotFound {
			return &api.DefendInCombatNotFound{}, nil
		}
		return &api.DefendInCombatInternalServerError{}, err
	}

	return result, nil
}

// ProcessAttack - TYPED response!
func (h *Handlers) ProcessAttack(ctx context.Context, req *api.AttackRequest, params api.ProcessAttackParams) (api.ProcessAttackRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.ProcessAttack(ctx, params.SessionId.String(), req)
	if err != nil {
		if err == ErrNotFound {
			return &api.ProcessAttackNotFound{}, nil
		}
		return &api.ProcessAttackInternalServerError{}, err
	}

	return result, nil
}

// UseCombatAbility - TYPED response!
func (h *Handlers) UseCombatAbility(ctx context.Context, req *api.UseAbilityRequest, params api.UseCombatAbilityParams) (api.UseCombatAbilityRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.UseCombatAbility(ctx, params.SessionId.String(), req)
	if err != nil {
		if err == ErrNotFound {
			return &api.UseCombatAbilityNotFound{}, nil
		}
		return &api.UseCombatAbilityInternalServerError{}, err
	}

	return result, nil
}

// UseCombatItem - TYPED response!
func (h *Handlers) UseCombatItem(ctx context.Context, req *api.UseItemRequest, params api.UseCombatItemParams) (api.UseCombatItemRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.UseCombatItem(ctx, params.SessionId.String(), req)
	if err != nil {
		if err == ErrNotFound {
			return &api.UseCombatItemNotFound{}, nil
		}
		return &api.UseCombatItemInternalServerError{}, err
	}

	return result, nil
}

