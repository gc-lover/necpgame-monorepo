// Package server Issue: #1595
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"math"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/projectile-core-service-go/pkg/api"
)

const DBTimeout = 50 * time.Millisecond

// Handlers implements api.Handler interface (ogen typed handlers!)
// Issue: #1588 - Resilience patterns (Load Shedding, Circuit Breaker)
// Issue: #1587 - Server-Side Validation & Anti-Cheat Integration
type Handlers struct {
	service             *ProjectileService
	loadShedder         *LoadShedder
	projectileValidator *ProjectileValidator
}

// NewHandlers creates new handlers
func NewHandlers(service *ProjectileService) *Handlers {
	// Issue: #1588 - Resilience patterns for hot path service (2k+ RPS)
	loadShedder := NewLoadShedder(1000) // Max 1000 concurrent requests

	// Issue: #1587 - Anti-cheat validation
	projectileValidator := NewProjectileValidator()

	return &Handlers{
		service:             service,
		loadShedder:         loadShedder,
		projectileValidator: projectileValidator,
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
// Issue: #1587 - Server-Side Validation & Anti-Cheat Integration
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

	// Issue: #1587 - Validate projectile spawn (anti-cheat: rate, velocity, trajectory)
	playerID := req.WeaponID // Use weapon_id as player identifier (TODO: get actual player_id from context)

	// Extract position from Origin
	position := Vec3{
		X: req.Origin.X,
		Y: req.Origin.Y,
		Z: req.Origin.Z,
	}

	// Extract velocity from Direction and Velocity
	velocityValue := float32(100.0) // Default velocity
	if req.Velocity.IsSet() {
		velocityValue = req.Velocity.Value
	}

	// Calculate velocity vector from direction and magnitude
	directionMagnitude := math.Sqrt(float64(req.Direction.X*req.Direction.X + req.Direction.Y*req.Direction.Y + req.Direction.Z*req.Direction.Z))
	if directionMagnitude > 0 {
		normalizedX := req.Direction.X / float32(directionMagnitude)
		normalizedY := req.Direction.Y / float32(directionMagnitude)
		normalizedZ := req.Direction.Z / float32(directionMagnitude)

		velocity := Vec3{
			X: normalizedX * velocityValue,
			Y: normalizedY * velocityValue,
			Z: normalizedZ * velocityValue,
		}

		// Calculate max range from velocity (simplified: horizontal projection)
		maxRange := velocityValue * velocityValue / (2 * 9.8) // gravity = 9.8 m/s^2

		if err := h.projectileValidator.ValidateProjectileSpawn(playerID, velocity, maxRange); err != nil {
			// Return validation error
			return &api.SpawnProjectileBadRequest{
				Error:   "BadRequest",
				Message: "Invalid projectile: " + err.Error(),
				Code:    api.NewOptNilString("400"),
			}, nil
		}
	}

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
