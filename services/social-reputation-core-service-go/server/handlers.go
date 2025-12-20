// Package server Issue: #1595
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"errors"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/social-reputation-core-service-go/pkg/api"
)

const (
	DBTimeout = 50 * time.Millisecond
)

var (
	ErrNotFound = errors.New("not found")
)

// Handlers implements api.Handler interface (ogen typed handlers!)
type Handlers struct {
	service *Service
}

// NewHandlers creates new handlers
func NewHandlers(service *Service) *Handlers {
	return &Handlers{service: service}
}

// GetReputation - TYPED response!
func (h *Handlers) GetReputation(ctx context.Context, params api.GetReputationParams) (api.GetReputationRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetReputation(params.CharacterId)
	if err != nil {
		if err == ErrNotFound {
			return &api.GetReputationNotFound{}, nil
		}
		return &api.GetReputationInternalServerError{}, err
	}

	return result, nil
}

// GetFactionReputation - TYPED response!
func (h *Handlers) GetFactionReputation(ctx context.Context, params api.GetFactionReputationParams) (api.GetFactionReputationRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetFactionReputation(params.CharacterId, params.FactionId)
	if err != nil {
		if err == ErrNotFound {
			return &api.GetFactionReputationNotFound{}, nil
		}
		return &api.GetFactionReputationInternalServerError{}, err
	}

	return result, nil
}

// GetFactionRelations - TYPED response!
func (h *Handlers) GetFactionRelations(ctx context.Context, params api.GetFactionRelationsParams) (api.GetFactionRelationsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetFactionRelations(params.CharacterId)
	if err != nil {
		if err == ErrNotFound {
			return &api.GetFactionRelationsNotFound{}, nil
		}
		return &api.GetFactionRelationsInternalServerError{}, err
	}

	return result, nil
}

// GetReputationTier - TYPED response!
func (h *Handlers) GetReputationTier(ctx context.Context, params api.GetReputationTierParams) (api.GetReputationTierRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetReputationTier(params.CharacterId)
	if err != nil {
		if err == ErrNotFound {
			return &api.GetReputationTierNotFound{}, nil
		}
		return &api.GetReputationTierInternalServerError{}, err
	}

	return result, nil
}

// GetReputationEffects - TYPED response!
func (h *Handlers) GetReputationEffects(ctx context.Context, params api.GetReputationEffectsParams) (api.GetReputationEffectsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetReputationEffects(params.CharacterId)
	if err != nil {
		if err == ErrNotFound {
			return &api.GetReputationEffectsNotFound{}, nil
		}
		return &api.GetReputationEffectsInternalServerError{}, err
	}

	return result, nil
}

// ChangeReputation - TYPED response!
func (h *Handlers) ChangeReputation(ctx context.Context, req *api.ChangeReputationRequest, params api.ChangeReputationParams) (api.ChangeReputationRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.ChangeReputation(params.CharacterId, req)
	if err != nil {
		if err == ErrNotFound {
			return &api.ChangeReputationBadRequest{}, nil
		}
		return &api.ChangeReputationInternalServerError{}, err
	}

	return result, nil
}
