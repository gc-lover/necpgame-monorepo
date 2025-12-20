// Package server Issue: #1595 - ogen handlers (TYPED responses)
package server

import (
	"context"
	"errors"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/weapon-progression-service-go/pkg/api"
)

const (
	DBTimeout = 50 * time.Millisecond
)

var (
	ErrNotFound = errors.New("not found")
)

type Handlers struct {
	service *Service
}

func NewHandlers(service *Service) *Handlers {
	return &Handlers{service: service}
}

func (h *Handlers) APIV1WeaponsProgressionWeaponIdGet(ctx context.Context, params api.APIV1WeaponsProgressionWeaponIdGetParams) (api.APIV1WeaponsProgressionWeaponIdGetRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetWeaponProgression(params.WeaponId)
	if err != nil {
		if err == ErrNotFound {
			return &api.APIV1WeaponsProgressionWeaponIdGetNotFound{}, nil
		}
		return &api.APIV1WeaponsProgressionWeaponIdGetInternalServerError{}, err
	}

	return result, nil
}

func (h *Handlers) APIV1WeaponsProgressionWeaponIdPost(ctx context.Context, _ *api.UpgradeWeaponRequest, params api.APIV1WeaponsProgressionWeaponIdPostParams) (api.APIV1WeaponsProgressionWeaponIdPostRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.UpgradeWeapon(params.WeaponId)
	if err != nil {
		if err == ErrNotFound {
			return &api.APIV1WeaponsProgressionWeaponIdPostNotFound{}, nil
		}
		return &api.APIV1WeaponsProgressionWeaponIdPostInternalServerError{}, err
	}

	return result, nil
}

func (h *Handlers) APIV1WeaponsMasteryGet(ctx context.Context, _ api.APIV1WeaponsMasteryGetParams) (api.APIV1WeaponsMasteryGetRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetAllWeaponMasteries()
	if err != nil {
		return &api.Error{Error: "INTERNAL_SERVER_ERROR", Message: err.Error()}, err
	}

	return result, nil
}

func (h *Handlers) APIV1WeaponsMasteryWeaponTypeGet(ctx context.Context, params api.APIV1WeaponsMasteryWeaponTypeGetParams) (api.APIV1WeaponsMasteryWeaponTypeGetRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetWeaponMasteryByType(params.PlayerId, params.WeaponType)
	if err != nil {
		if err == ErrNotFound {
			return &api.APIV1WeaponsMasteryWeaponTypeGetNotFound{}, nil
		}
		return &api.APIV1WeaponsMasteryWeaponTypeGetInternalServerError{}, err
	}

	return result, nil
}

func (h *Handlers) APIV1WeaponsPerksGet(ctx context.Context, _ api.APIV1WeaponsPerksGetParams) (api.APIV1WeaponsPerksGetRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetWeaponPerks()
	if err != nil {
		return &api.Error{Error: "INTERNAL_SERVER_ERROR", Message: err.Error()}, err
	}

	return result, nil
}

func (h *Handlers) APIV1WeaponsPerksPerkIdUnlockPost(ctx context.Context, _ *api.UnlockPerkRequest, _ api.APIV1WeaponsPerksPerkIdUnlockPostParams) (api.APIV1WeaponsPerksPerkIdUnlockPostRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.UnlockPerk()
	if err != nil {
		if err == ErrNotFound {
			return &api.APIV1WeaponsPerksPerkIdUnlockPostNotFound{}, nil
		}
		return &api.APIV1WeaponsPerksPerkIdUnlockPostInternalServerError{}, err
	}

	return result, nil
}
