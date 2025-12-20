// Package server Issue: #1595
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-ai-service-go/pkg/api"
)

const DBTimeout = 50 * time.Millisecond

// Handlers Issue: #1588 - Resilience patterns (Load Shedding, Circuit Breaker)
type Handlers struct {
	service     *Service
	loadShedder *LoadShedder
}

func NewHandlers(service *Service) *Handlers {
	// Issue: #1588 - Resilience patterns for hot path service
	loadShedder := NewLoadShedder(500) // Max 500 concurrent requests

	return &Handlers{
		service:     service,
		loadShedder: loadShedder,
	}
}

func (h *Handlers) GetAIProfile(ctx context.Context, _ api.GetAIProfileParams) (api.GetAIProfileRes, error) {
	// Issue: #1588 - Load shedding (prevent overload)
	if !h.loadShedder.Allow() {
		err := api.GetAIProfileInternalServerError(api.Error{
			Error:   "ServiceUnavailable",
			Message: "service overloaded, please try again later",
			Code:    api.NewOptNilString("503"),
		})
		return &err, nil
	}
	defer h.loadShedder.Done()

	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetAIProfile()
	if err != nil {
		if err == ErrNotFound {
			return &api.GetAIProfileNotFound{}, nil
		}
		return &api.GetAIProfileInternalServerError{}, err
	}

	return result, nil
}

func (h *Handlers) GetAIProfileTelemetry(ctx context.Context, params api.GetAIProfileTelemetryParams) (api.GetAIProfileTelemetryRes, error) {
	// Issue: #1588 - Load shedding (prevent overload)
	if !h.loadShedder.Allow() {
		err := api.GetAIProfileTelemetryInternalServerError(api.Error{
			Error:   "ServiceUnavailable",
			Message: "service overloaded, please try again later",
			Code:    api.NewOptNilString("503"),
		})
		return &err, nil
	}
	defer h.loadShedder.Done()

	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetAIProfileTelemetry()
	if err != nil {
		if err == ErrNotFound {
			return &api.GetAIProfileTelemetryNotFound{}, nil
		}
		return &api.GetAIProfileTelemetryInternalServerError{}, err
	}

	return result, nil
}

func (h *Handlers) ListAIProfiles(ctx context.Context, params api.ListAIProfilesParams) (api.ListAIProfilesRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.ListAIProfiles()
	if err != nil {
		return &api.ListAIProfilesInternalServerError{}, err
	}

	return result, nil
}
