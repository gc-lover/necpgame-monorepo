// Issue: #1595
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/projectile-core-service-go/pkg/api"
)

const DBTimeout = 50 * time.Millisecond

// Handlers implements api.Handler interface (ogen typed handlers!)
// Issue: #1588 - Resilience patterns (Load Shedding, Circuit Breaker)
type Handlers struct {
	service     *ProjectileService
	loadShedder *LoadShedder
}

// NewHandlers creates new handlers
func NewHandlers(service *ProjectileService) *Handlers {
	// Issue: #1588 - Resilience patterns for hot path service (2k+ RPS)
	loadShedder := NewLoadShedder(1000) // Max 1000 concurrent requests
	
	return &Handlers{
		service:     service,
		loadShedder: loadShedder,
	}
}

// GetProjectileForms - TYPED response!
func (h *Handlers) GetProjectileForms(ctx context.Context, params api.GetProjectileFormsParams) (api.GetProjectileFormsRes, error) {
	// Issue: #1588 - Load shedding (prevent overload)
	if !h.loadShedder.Allow() {
		err := api.GetProjectileFormsInternalServerError(api.Error{
			Error:   "ServiceUnavailable",
			Message: "service overloaded, please try again later",
			Code:    api.NewOptNilString("503"),
		})
		return &err, nil
	}
	defer h.loadShedder.Done()

	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	forms, err := h.service.GetForms(ctx, params)
	if err != nil {
		return &api.GetProjectileFormsInternalServerError{}, err
	}

	return forms, nil
}

// GetProjectileForm - TYPED response!
func (h *Handlers) GetProjectileForm(ctx context.Context, params api.GetProjectileFormParams) (api.GetProjectileFormRes, error) {
	// Issue: #1588 - Load shedding (prevent overload)
	if !h.loadShedder.Allow() {
		err := api.GetProjectileFormInternalServerError(api.Error{
			Error:   "ServiceUnavailable",
			Message: "service overloaded, please try again later",
			Code:    api.NewOptNilString("503"),
		})
		return &err, nil
	}
	defer h.loadShedder.Done()

	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	form, err := h.service.GetForm(ctx, params.FormID)
	if err != nil {
		return &api.GetProjectileFormNotFound{}, nil
	}

	return form, nil
}

// SpawnProjectile - TYPED response!
func (h *Handlers) SpawnProjectile(ctx context.Context, req *api.SpawnProjectileRequest) (api.SpawnProjectileRes, error) {
	// Issue: #1588 - Load shedding (prevent overload)
	if !h.loadShedder.Allow() {
		err := api.SpawnProjectileInternalServerError(api.Error{
			Error:   "ServiceUnavailable",
			Message: "service overloaded, please try again later",
			Code:    api.NewOptNilString("503"),
		})
		return &err, nil
	}
	defer h.loadShedder.Done()

	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	resp, err := h.service.SpawnProjectile(ctx, req)
	if err != nil {
		return &api.SpawnProjectileInternalServerError{}, err
	}

	return resp, nil
}

// ValidateCompatibility - TYPED response!
func (h *Handlers) ValidateCompatibility(ctx context.Context, req *api.ValidateCompatibilityRequest) (api.ValidateCompatibilityRes, error) {
	// Issue: #1588 - Load shedding (prevent overload)
	if !h.loadShedder.Allow() {
		err := api.ValidateCompatibilityInternalServerError(api.Error{
			Error:   "ServiceUnavailable",
			Message: "service overloaded, please try again later",
			Code:    api.NewOptNilString("503"),
		})
		return &err, nil
	}
	defer h.loadShedder.Done()

	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	resp, err := h.service.ValidateCompatibility(ctx, req)
	if err != nil {
		return &api.ValidateCompatibilityInternalServerError{}, err
	}

	return resp, nil
}

// GetCompatibilityMatrix - TYPED response!
func (h *Handlers) GetCompatibilityMatrix(ctx context.Context) (api.GetCompatibilityMatrixRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	matrix, err := h.service.GetCompatibilityMatrix(ctx)
	if err != nil {
		return &api.Error{
			Error:   "InternalServerError",
			Message: err.Error(),
		}, nil
	}

	return matrix, nil
}
