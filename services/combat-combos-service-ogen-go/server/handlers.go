// Package server Issue: #1578
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-combos-service-ogen-go/pkg/api"
)

const (
	DBTimeout = 50 * time.Millisecond
)

// Handlers implements api.Handler interface (ogen typed handlers!)
type Handlers struct {
	service *Service
}

// NewHandlers creates new handlers
func NewHandlers(service *Service) *Handlers {
	return &Handlers{service: service}
}

// GetComboCatalog returns combo catalog - TYPED response!
func (h *Handlers) GetComboCatalog(ctx context.Context, params api.GetComboCatalogParams) (api.GetComboCatalogRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	catalog, err := h.service.GetComboCatalog()
	if err != nil {
		return &api.GetComboCatalogInternalServerError{}, err
	}

	// Return TYPED response (ogen will marshal directly!)
	return catalog, nil
}

// GetComboDetails - TYPED response!
func (h *Handlers) GetComboDetails(ctx context.Context, params api.GetComboDetailsParams) (api.GetComboDetailsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	details, err := h.service.GetComboDetails()
	if err != nil {
		if err == ErrNotFound {
			return &api.GetComboDetailsNotFound{}, nil
		}
		return &api.GetComboDetailsInternalServerError{}, err
	}

	return details, nil
}

// ActivateCombo - TYPED response!
func (h *Handlers) ActivateCombo(ctx context.Context, req *api.ActivateComboRequest) (api.ActivateComboRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	response, err := h.service.ActivateCombo(req)
	if err != nil {
		if err == ErrRequirementsNotMet {
			return &api.ActivateComboBadRequest{}, nil
		}
		if err == ErrNotFound {
			return &api.ActivateComboNotFound{}, nil
		}
		return &api.ActivateComboInternalServerError{}, err
	}

	return response, nil
}

// ApplySynergy - TYPED response!
func (h *Handlers) ApplySynergy(ctx context.Context, req *api.ApplySynergyRequest) (api.ApplySynergyRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	response, err := h.service.ApplySynergy(req)
	if err != nil {
		if err == ErrNotFound {
			return &api.ApplySynergyNotFound{}, nil
		}
		if err == ErrSynergyUnavailable {
			return &api.ApplySynergyBadRequest{}, nil
		}
		return &api.ApplySynergyInternalServerError{}, err
	}

	return response, nil
}

// GetComboLoadout - TYPED response!
func (h *Handlers) GetComboLoadout(ctx context.Context, params api.GetComboLoadoutParams) (api.GetComboLoadoutRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	loadout, err := h.service.GetComboLoadout(params.CharacterID.String())
	if err != nil {
		if err == ErrNotFound {
			return &api.GetComboLoadoutNotFound{}, nil
		}
		return &api.GetComboLoadoutInternalServerError{}, err
	}

	return loadout, nil
}

// UpdateComboLoadout - TYPED response!
func (h *Handlers) UpdateComboLoadout(ctx context.Context, req *api.UpdateLoadoutRequest) (api.UpdateComboLoadoutRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	loadout, err := h.service.UpdateComboLoadout(req)
	if err != nil {
		return &api.UpdateComboLoadoutInternalServerError{}, err
	}

	return loadout, nil
}

// SubmitComboScore - TYPED response!
func (h *Handlers) SubmitComboScore(ctx context.Context, req *api.SubmitScoreRequest) (api.SubmitComboScoreRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	response, err := h.service.SubmitComboScore(req)
	if err != nil {
		if err == ErrNotFound {
			return &api.SubmitComboScoreNotFound{}, nil
		}
		return &api.SubmitComboScoreInternalServerError{}, err
	}

	return response, nil
}

// GetComboAnalytics - TYPED response!
func (h *Handlers) GetComboAnalytics(ctx context.Context, params api.GetComboAnalyticsParams) (api.GetComboAnalyticsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	analytics, err := h.service.GetComboAnalytics(params)
	if err != nil {
		return &api.GetComboAnalyticsInternalServerError{}, err
	}

	return analytics, nil
}
