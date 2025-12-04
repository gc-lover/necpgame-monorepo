// Issue: #1595
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-ai-service-go/pkg/api"
)

const DBTimeout = 50 * time.Millisecond

type Handlers struct {
	service *Service
}

func NewHandlers(service *Service) *Handlers {
	return &Handlers{service: service}
}

func (h *Handlers) GetAIProfile(ctx context.Context, params api.GetAIProfileParams) (api.GetAIProfileRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetAIProfile(ctx, params.ProfileID.String())
	if err != nil {
		if err == ErrNotFound {
			return &api.GetAIProfileNotFound{}, nil
		}
		return &api.GetAIProfileInternalServerError{}, err
	}

	return result, nil
}

func (h *Handlers) GetAIProfileTelemetry(ctx context.Context, params api.GetAIProfileTelemetryParams) (api.GetAIProfileTelemetryRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetAIProfileTelemetry(ctx, params.ProfileID.String())
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

	result, err := h.service.ListAIProfiles(ctx, params)
	if err != nil {
		return &api.ListAIProfilesInternalServerError{}, err
	}

	return result, nil
}

