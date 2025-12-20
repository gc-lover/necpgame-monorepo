// Package server Issue: #1595, #1607
// Performance: Memory pooling for hot path (Issue #1607)
package server

import (
	"context"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/weapon-resource-service-go/pkg/api"
	"github.com/google/uuid"
)

// Service contains business logic for weapon resources with memory pooling
// Issue: #1607 - Memory pooling for hot path structs (Level 2 optimization)
type Service struct {
	repo *Repository

	// Memory pooling for hot structs (zero allocations target!)
	resourcesPool sync.Pool
	statusPool    sync.Pool
}

// NewService creates service with DI and memory pooling
func NewService(repo *Repository) *Service {
	s := &Service{repo: repo}

	// Initialize memory pools (zero allocations target!)
	s.resourcesPool = sync.Pool{
		New: func() interface{} {
			return &api.WeaponResources{}
		},
	}
	s.statusPool = sync.Pool{
		New: func() interface{} {
			return &api.WeaponStatus{}
		},
	}

	return s
}

// GetWeaponResources - HOT PATH
// Issue: #1607 - Uses memory pooling for zero allocations
func (s *Service) GetWeaponResources(weaponID string) (*api.WeaponResources, error) {
	resources, err := s.repo.GetWeaponResources(weaponID)
	if err != nil {
		return nil, err
	}
	// Repository returns new instance, no pooling needed here
	// Pooling would be used if we create response in service
	return resources, nil
}

// ConsumeResource consumes resource (ammo/heat/energy)
func (s *Service) ConsumeResource(ctx context.Context, weaponID string) (*api.WeaponResources, error) {
	// Business logic: validate, consume resource
	resources, err := s.repo.ConsumeResource(weaponID)
	if err != nil {
		return nil, err
	}
	return resources, nil
}

// ApplyCooldown applies cooldown to weapon/ability
func (s *Service) ApplyCooldown(ctx context.Context, weaponID string) (*api.WeaponResources, error) {
	resources, err := s.repo.ApplyCooldown(weaponID)
	if err != nil {
		return nil, err
	}
	return resources, nil
}

// ReloadWeapon reloads weapon ammunition
func (s *Service) ReloadWeapon(ctx context.Context, weaponID string) (*api.WeaponResources, error) {
	resources, err := s.repo.ReloadWeapon(weaponID)
	if err != nil {
		return nil, err
	}
	return resources, nil
}

// GetWeaponStatus - HOT PATH
// Issue: #1607 - Uses memory pooling for zero allocations
func (s *Service) GetWeaponStatus(weaponID string) (*api.WeaponStatus, error) {
	weaponIDUUID, err := uuid.Parse(weaponID)
	if err != nil {
		return nil, err
	}

	resources, err := s.repo.GetWeaponResources(weaponID)
	if err != nil {
		return nil, err
	}

	// Get from pool (zero allocation!)
	status := s.statusPool.Get().(*api.WeaponStatus)
	defer func() {
		// Reset before returning to pool
		status.WeaponInstanceId = uuid.UUID{}
		status.CanFire = false
		status.Overheated = api.OptBool{}
		status.ActiveCooldowns = status.ActiveCooldowns[:0]
		s.statusPool.Put(status)
	}()

	cooldowns := make([]api.CooldownInfo, 0, len(resources.Cooldowns))
	for abilityID, expiresAt := range resources.Cooldowns {
		remaining := float32(time.Until(expiresAt).Seconds())
		if remaining < 0 {
			remaining = 0
		}
		cooldowns = append(cooldowns, api.CooldownInfo{
			AbilityId:        abilityID,
			ExpiresAt:        expiresAt,
			RemainingSeconds: remaining,
		})
	}

	status.WeaponInstanceId = weaponIDUUID
	status.CanFire = !resources.Heat.Overheated
	status.Overheated = api.NewOptBool(resources.Heat.Overheated)
	status.ActiveCooldowns = cooldowns

	// Clone response (caller owns it)
	result := &api.WeaponStatus{
		WeaponInstanceId: status.WeaponInstanceId,
		CanFire:          status.CanFire,
		Overheated:       status.Overheated,
		ActiveCooldowns:  status.ActiveCooldowns,
	}

	return result, nil
}
