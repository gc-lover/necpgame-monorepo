// Issue: #1574
package server

import (
	"context"

	"github.com/gc-lover/necpgame-monorepo/services/weapon-resource-service-go/pkg/api"
)

// Service contains business logic for weapon resources
type Service struct {
	repo *Repository
}

// NewService creates service with DI
func NewService(repo *Repository) *Service {
	return &Service{repo: repo}
}

// GetWeaponResources gets all resources for weapon
func (s *Service) GetWeaponResources(ctx context.Context, weaponID string) (*api.WeaponResources, error) {
	resources, err := s.repo.GetWeaponResources(ctx, weaponID)
	if err != nil {
		return nil, err
	}
	return resources, nil
}

// UpdateAmmo updates ammo count for weapon
func (s *Service) UpdateAmmo(ctx context.Context, weaponID string, req api.UpdateAmmoJSONRequestBody) (*api.WeaponResources, error) {
	// Business logic: validate ammo count, update
	resources, err := s.repo.UpdateAmmo(ctx, weaponID, req)
	if err != nil {
		return nil, err
	}
	return resources, nil
}

// UpdateHeat updates heat level for weapon
func (s *Service) UpdateHeat(ctx context.Context, weaponID string, req api.UpdateHeatJSONRequestBody) (*api.WeaponResources, error) {
	resources, err := s.repo.UpdateHeat(ctx, weaponID, req)
	if err != nil {
		return nil, err
	}
	return resources, nil
}

// UpdateEnergy updates energy level for weapon
func (s *Service) UpdateEnergy(ctx context.Context, weaponID string, req api.UpdateEnergyJSONRequestBody) (*api.WeaponResources, error) {
	resources, err := s.repo.UpdateEnergy(ctx, weaponID, req)
	if err != nil {
		return nil, err
	}
	return resources, nil
}

// UpdateCooldown updates cooldown status for weapon
func (s *Service) UpdateCooldown(ctx context.Context, weaponID string, req api.UpdateCooldownJSONRequestBody) (*api.WeaponResources, error) {
	resources, err := s.repo.UpdateCooldown(ctx, weaponID, req)
	if err != nil {
		return nil, err
	}
	return resources, nil
}






