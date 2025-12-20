// Package server SQL queries use prepared statements with placeholders (, , ?) for safety
// Issue: #1595
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-extended-mechanics-service-go/pkg/api"
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

// ActivateCombatImplant - TYPED response!
func (h *Handlers) ActivateCombatImplant(ctx context.Context, req *api.CombatImplantActivationRequest) (api.ActivateCombatImplantRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.ActivateCombatImplant()
	if err != nil {
		return &api.ActivateCombatImplantInternalServerError{}, err
	}

	return result, nil
}

// AdvancedAim - TYPED response!
func (h *Handlers) AdvancedAim(ctx context.Context, _ *api.AdvancedAimRequest) (api.AdvancedAimRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.AdvancedAim()
	if err != nil {
		return &api.AdvancedAimInternalServerError{}, err
	}

	return result, nil
}

// ControlRecoil - TYPED response!
func (h *Handlers) ControlRecoil(ctx context.Context, _ *api.RecoilControlRequest) (api.ControlRecoilRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.ControlRecoil()
	if err != nil {
		return &api.ControlRecoilInternalServerError{}, err
	}

	return result, nil
}

// CreateOrUpdateCombatLoadout - TYPED response!
func (h *Handlers) CreateOrUpdateCombatLoadout(ctx context.Context, req *api.CombatLoadoutCreate) (api.CreateOrUpdateCombatLoadoutRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.CreateOrUpdateCombatLoadout(req)
	if err != nil {
		return &api.CreateOrUpdateCombatLoadoutInternalServerError{}, err
	}

	// Return as OK response (200)
	return &api.CreateOrUpdateCombatLoadoutOK{
		LoadoutID:   result.LoadoutID,
		CharacterID: result.CharacterID,
		Name:        result.Name,
		Weapons:     result.Weapons,
		Abilities:   result.Abilities,
		Implants:    result.Implants,
		Equipment:   result.Equipment,
		IsActive:    result.IsActive,
	}, nil
}

// EquipCombatLoadout - TYPED response!
func (h *Handlers) EquipCombatLoadout(ctx context.Context, _ *api.CombatLoadoutEquipRequest) (api.EquipCombatLoadoutRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.EquipCombatLoadout()
	if err != nil {
		if err == ErrNotFound {
			return &api.EquipCombatLoadoutNotFound{}, nil
		}
		return &api.EquipCombatLoadoutInternalServerError{}, err
	}

	return result, nil
}

// ExecuteCombatHacking - TYPED response!
func (h *Handlers) ExecuteCombatHacking(ctx context.Context, _ *api.CombatHackingRequest) (api.ExecuteCombatHackingRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.ExecuteCombatHacking()
	if err != nil {
		return &api.ExecuteCombatHackingInternalServerError{}, err
	}

	return result, nil
}

// GetCombatHackingNetworks - TYPED response!
func (h *Handlers) GetCombatHackingNetworks(ctx context.Context, _ api.GetCombatHackingNetworksParams) (api.GetCombatHackingNetworksRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetCombatHackingNetworks()
	if err != nil {
		if err == ErrNotFound {
			return &api.GetCombatHackingNetworksNotFound{}, nil
		}
		return &api.GetCombatHackingNetworksInternalServerError{}, err
	}

	return result, nil
}

// GetCombatImplantEffects - TYPED response!
func (h *Handlers) GetCombatImplantEffects(ctx context.Context, _ api.GetCombatImplantEffectsParams) (api.GetCombatImplantEffectsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetCombatImplantEffects()
	if err != nil {
		if err == ErrNotFound {
			return &api.GetCombatImplantEffectsNotFound{}, nil
		}
		return &api.GetCombatImplantEffectsInternalServerError{}, err
	}

	return result, nil
}

// GetCombatLoadouts - TYPED response!
func (h *Handlers) GetCombatLoadouts(ctx context.Context, _ api.GetCombatLoadoutsParams) (api.GetCombatLoadoutsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetCombatLoadouts()
	if err != nil {
		if err == ErrNotFound {
			return &api.GetCombatLoadoutsNotFound{}, nil
		}
		return &api.GetCombatLoadoutsInternalServerError{}, err
	}

	return result, nil
}

// GetCombatMechanicsStatus - TYPED response!
func (h *Handlers) GetCombatMechanicsStatus(ctx context.Context, params api.GetCombatMechanicsStatusParams) (api.GetCombatMechanicsStatusRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetCombatMechanicsStatus(params)
	if err != nil {
		if err == ErrNotFound {
			return &api.GetCombatMechanicsStatusNotFound{}, nil
		}
		return &api.GetCombatMechanicsStatusInternalServerError{}, err
	}

	return result, nil
}
