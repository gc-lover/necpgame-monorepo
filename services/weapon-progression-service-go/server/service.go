// Issue: #1574
package server

import (
	"context"

	"github.com/gc-lover/necpgame-monorepo/services/weapon-progression-service-go/pkg/api"
)

// Service contains business logic for weapon progression
type Service struct {
	repo *Repository
}

// NewService creates service with DI
func NewService(repo *Repository) *Service {
	return &Service{repo: repo}
}

// GetWeaponUpgrades gets available upgrades for weapon
func (s *Service) GetWeaponUpgrades(ctx context.Context, weaponID string, params api.GetWeaponUpgradesParams) (*api.WeaponUpgradesResponse, error) {
	// Business logic: get upgrades from repository
	upgrades, err := s.repo.GetWeaponUpgrades(ctx, weaponID, params)
	if err != nil {
		return nil, err
	}
	return upgrades, nil
}

// ApplyUpgrade applies upgrade to weapon
func (s *Service) ApplyUpgrade(ctx context.Context, weaponID string, req api.ApplyUpgradeJSONRequestBody) (*api.WeaponUpgrade, error) {
	// Business logic: validate, apply upgrade, update database
	upgrade, err := s.repo.ApplyUpgrade(ctx, weaponID, req)
	if err != nil {
		return nil, err
	}
	return upgrade, nil
}

// GetWeaponPerks gets perks for weapon
func (s *Service) GetWeaponPerks(ctx context.Context, weaponID string, params api.GetWeaponPerksParams) (*api.WeaponPerksResponse, error) {
	perks, err := s.repo.GetWeaponPerks(ctx, weaponID, params)
	if err != nil {
		return nil, err
	}
	return perks, nil
}

// UnlockPerk unlocks perk for weapon
func (s *Service) UnlockPerk(ctx context.Context, weaponID string, req api.UnlockPerkJSONRequestBody) (*api.WeaponPerk, error) {
	perk, err := s.repo.UnlockPerk(ctx, weaponID, req)
	if err != nil {
		return nil, err
	}
	return perk, nil
}

// GetWeaponMastery gets mastery for weapon
func (s *Service) GetWeaponMastery(ctx context.Context, weaponID string) (*api.WeaponMastery, error) {
	mastery, err := s.repo.GetWeaponMastery(ctx, weaponID)
	if err != nil {
		return nil, err
	}
	return mastery, nil
}

// UpdateMastery updates mastery for weapon
func (s *Service) UpdateMastery(ctx context.Context, weaponID string, req api.UpdateMasteryJSONRequestBody) (*api.WeaponMastery, error) {
	mastery, err := s.repo.UpdateMastery(ctx, weaponID, req)
	if err != nil {
		return nil, err
	}
	return mastery, nil
}




