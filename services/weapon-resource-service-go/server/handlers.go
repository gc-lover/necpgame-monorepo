// Package server Issue: #1595
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/weapon-resource-service-go/pkg/api"
)

const DBTimeout = 50 * time.Millisecond

// Handlers implements api.Handler interface (ogen typed handlers!)
type Handlers struct {
	service *Service
}

// NewHandlers creates new handlers
func NewHandlers(service *Service) *Handlers {
	return &Handlers{service: service}
}

// APIV1WeaponsResourcesWeaponIdGet - TYPED response!
func (h *Handlers) APIV1WeaponsResourcesWeaponIdGet(ctx context.Context, params api.APIV1WeaponsResourcesWeaponIdGetParams) (api.APIV1WeaponsResourcesWeaponIdGetRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	resources, err := h.service.GetWeaponResources(params.WeaponId.String())
	if err != nil {
		return &api.APIV1WeaponsResourcesWeaponIdGetInternalServerError{
			Error:   "InternalServerError",
			Message: err.Error(),
		}, nil
	}

	return resources, nil
}

// APIV1WeaponsResourcesWeaponIdConsumePost - TYPED response!
func (h *Handlers) APIV1WeaponsResourcesWeaponIdConsumePost(ctx context.Context, _ *api.ConsumeResourceRequest, params api.APIV1WeaponsResourcesWeaponIdConsumePostParams) (api.APIV1WeaponsResourcesWeaponIdConsumePostRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.ConsumeResource(params.WeaponId.String())
	if err != nil {
		return &api.APIV1WeaponsResourcesWeaponIdConsumePostInternalServerError{
			Error:   "InternalServerError",
			Message: err.Error(),
		}, nil
	}

	return result, nil
}

// APIV1WeaponsResourcesWeaponIdCooldownPost - TYPED response!
func (h *Handlers) APIV1WeaponsResourcesWeaponIdCooldownPost(ctx context.Context, _ *api.ApplyCooldownRequest, params api.APIV1WeaponsResourcesWeaponIdCooldownPostParams) (api.APIV1WeaponsResourcesWeaponIdCooldownPostRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.ApplyCooldown(params.WeaponId.String())
	if err != nil {
		return &api.APIV1WeaponsResourcesWeaponIdCooldownPostInternalServerError{
			Error:   "InternalServerError",
			Message: err.Error(),
		}, nil
	}

	return result, nil
}

// APIV1WeaponsResourcesWeaponIdReloadPost - TYPED response!
func (h *Handlers) APIV1WeaponsResourcesWeaponIdReloadPost(ctx context.Context, _ *api.ReloadWeaponRequest, params api.APIV1WeaponsResourcesWeaponIdReloadPostParams) (api.APIV1WeaponsResourcesWeaponIdReloadPostRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.ReloadWeapon(params.WeaponId.String())
	if err != nil {
		return &api.APIV1WeaponsResourcesWeaponIdReloadPostInternalServerError{
			Error:   "InternalServerError",
			Message: err.Error(),
		}, nil
	}

	return result, nil
}

// APIV1WeaponsResourcesWeaponIdStatusGet - TYPED response!
func (h *Handlers) APIV1WeaponsResourcesWeaponIdStatusGet(ctx context.Context, params api.APIV1WeaponsResourcesWeaponIdStatusGetParams) (api.APIV1WeaponsResourcesWeaponIdStatusGetRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	status, err := h.service.GetWeaponStatus(params.WeaponId.String())
	if err != nil {
		return &api.APIV1WeaponsResourcesWeaponIdStatusGetInternalServerError{
			Error:   "InternalServerError",
			Message: err.Error(),
		}, nil
	}

	return status, nil
}
