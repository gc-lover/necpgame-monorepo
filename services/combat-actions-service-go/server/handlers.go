// Issue: #1595
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-actions-service-go/pkg/api"
)

// Context timeout constants
const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
)

// Handlers implements api.Handler interface (ogen typed handlers!)
// Issue: #1587 - Server-Side Validation & Anti-Cheat Integration
// Issue: #1588 - Resilience patterns (Load Shedding, Circuit Breaker)
type Handlers struct {
	service *Service
	// Issue: #1587 - Anti-cheat validation
	actionValidator *ActionValidator
	anomalyDetector *AnomalyDetector
	// Issue: #1588 - Resilience patterns
	loadShedder *LoadShedder
}

// NewHandlers creates new handlers
func NewHandlers(service *Service) *Handlers {
	// Issue: #1588 - Resilience patterns for hot path service (1.5k+ RPS)
	loadShedder := NewLoadShedder(750) // Max 750 concurrent requests
	
	return &Handlers{
		service: service,
		// Issue: #1587 - Anti-cheat validation
		actionValidator: NewActionValidator(),
		anomalyDetector: NewAnomalyDetector(),
		loadShedder:     loadShedder,
	}
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
	// Issue: #1588 - Load shedding (prevent overload)
	if !h.loadShedder.Allow() {
		err := api.DefendInCombatInternalServerError(api.Error{
			Error:   "ServiceUnavailable",
			Message: "service overloaded, please try again later",
			Code:    api.NewOptNilString("503"),
		})
		return &err, nil
	}
	defer h.loadShedder.Done()

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
// Issue: #1587 - Server-Side Validation & Anti-Cheat Integration
func (h *Handlers) ProcessAttack(ctx context.Context, req *api.AttackRequest, params api.ProcessAttackParams) (api.ProcessAttackRes, error) {
	// Issue: #1588 - Load shedding (prevent overload)
	if !h.loadShedder.Allow() {
		err := api.ProcessAttackInternalServerError(api.Error{
			Error:   "ServiceUnavailable",
			Message: "service overloaded, please try again later",
			Code:    api.NewOptNilString("503"),
		})
		return &err, nil
	}
	defer h.loadShedder.Done()

	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Issue: #1587 - Validate attack before processing (anti-cheat)
	playerID := req.AttackerID.String()
	attack := &AttackAction{
		From:       Vec3{X: 0, Y: 0, Z: 0}, // TODO: Get from request or session
		To:         Vec3{X: 0, Y: 0, Z: 0}, // TODO: Get from request or session
		Distance:   0,                       // TODO: Calculate from From/To
		AttackType: "melee",                // TODO: Get from request
	}

	if err := h.actionValidator.ValidateAttack(playerID, attack); err != nil {
		// Return validation error
		return &api.ProcessAttackBadRequest{
			Error:   "BadRequest",
			Message: "Invalid attack: " + err.Error(),
			Code:    api.NewOptNilString("400"),
		}, nil
	}

	result, err := h.service.ProcessAttack(ctx, params.SessionId.String(), req)
	if err != nil {
		if err == ErrNotFound {
			return &api.ProcessAttackNotFound{}, nil
		}
		return &api.ProcessAttackInternalServerError{}, err
	}

	// Issue: #1587 - Record attack for anomaly detection
	h.anomalyDetector.RecordAttack(playerID, false, 0) // TODO: Get critical and reaction time from result

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

