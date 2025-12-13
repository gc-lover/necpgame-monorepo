// Combat Combos Loadouts Service Business Logic
// Issue: #141890005

package server

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
)

// Service handles business logic for combo loadouts
type Service struct {
	repo *Repository
}

// NewService creates a new service instance
func NewService(repo *Repository) *Service {
	return &Service{repo: repo}
}

// GetComboLoadout retrieves a character's combo loadout with validation
func (s *Service) GetComboLoadout(ctx context.Context, characterID uuid.UUID) (*ComboLoadout, error) {
	if characterID == uuid.Nil {
		return nil, fmt.Errorf("invalid character ID")
	}

	loadout, err := s.repo.GetComboLoadout(ctx, characterID)
	if err != nil {
		return nil, fmt.Errorf("failed to get combo loadout: %w", err)
	}

	// Validate loadout data
	if err := s.validateLoadout(loadout); err != nil {
		return nil, fmt.Errorf("invalid loadout data: %w", err)
	}

	return loadout, nil
}

// UpdateComboLoadout updates a character's combo loadout with validation
func (s *Service) UpdateComboLoadout(ctx context.Context, req *UpdateLoadoutRequest) (*ComboLoadout, error) {
	// Validate request
	if err := s.validateUpdateRequest(req); err != nil {
		return nil, fmt.Errorf("invalid update request: %w", err)
	}

	// Update loadout
	loadout, err := s.repo.UpdateComboLoadout(ctx, req)
	if err != nil {
		return nil, fmt.Errorf("failed to update combo loadout: %w", err)
	}

	// Validate updated loadout
	if err := s.validateLoadout(loadout); err != nil {
		return nil, fmt.Errorf("updated loadout is invalid: %w", err)
	}

	return loadout, nil
}

// DeleteComboLoadout removes a character's combo loadout
func (s *Service) DeleteComboLoadout(ctx context.Context, characterID uuid.UUID) error {
	if characterID == uuid.Nil {
		return fmt.Errorf("invalid character ID")
	}

	return s.repo.DeleteComboLoadout(ctx, characterID)
}

// ListComboLoadouts retrieves paginated list of combo loadouts (admin function)
func (s *Service) ListComboLoadouts(ctx context.Context, limit, offset int) ([]ComboLoadout, int, error) {
	// Validate pagination parameters
	if limit <= 0 || limit > 100 {
		limit = 50 // default
	}
	if offset < 0 {
		offset = 0
	}

	loadouts, total, err := s.repo.ListComboLoadouts(ctx, limit, offset)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to list combo loadouts: %w", err)
	}

	// Validate each loadout
	for i, loadout := range loadouts {
		if err := s.validateLoadout(&loadout); err != nil {
			return nil, 0, fmt.Errorf("invalid loadout at index %d: %w", i, err)
		}
	}

	return loadouts, total, nil
}

// validateLoadout validates combo loadout data
func (s *Service) validateLoadout(loadout *ComboLoadout) error {
	if loadout == nil {
		return fmt.Errorf("loadout is nil")
	}

	if loadout.ID == uuid.Nil {
		return fmt.Errorf("invalid loadout ID")
	}

	if loadout.CharacterID == uuid.Nil {
		return fmt.Errorf("invalid character ID")
	}

	// Validate active combos
	maxCombos := 10 // configurable limit
	if len(loadout.ActiveCombos) > maxCombos {
		return fmt.Errorf("too many active combos: %d (max %d)", len(loadout.ActiveCombos), maxCombos)
	}

	// Check for duplicate combo IDs
	seen := make(map[uuid.UUID]bool)
	for _, comboID := range loadout.ActiveCombos {
		if comboID == uuid.Nil {
			return fmt.Errorf("invalid combo ID in active combos")
		}
		if seen[comboID] {
			return fmt.Errorf("duplicate combo ID: %s", comboID)
		}
		seen[comboID] = true
	}

	// Validate preferences
	if err := s.validatePreferences(&loadout.Preferences); err != nil {
		return fmt.Errorf("invalid preferences: %w", err)
	}

	// Validate timestamps
	now := time.Now()
	if loadout.CreatedAt.After(now) {
		return fmt.Errorf("created_at is in the future")
	}
	if loadout.UpdatedAt.After(now) {
		return fmt.Errorf("updated_at is in the future")
	}
	if loadout.UpdatedAt.Before(loadout.CreatedAt) {
		return fmt.Errorf("updated_at is before created_at")
	}

	return nil
}

// validateUpdateRequest validates update request data
func (s *Service) validateUpdateRequest(req *UpdateLoadoutRequest) error {
	if req == nil {
		return fmt.Errorf("request is nil")
	}

	if req.CharacterID == uuid.Nil {
		return fmt.Errorf("invalid character ID")
	}

	// Validate active combos
	maxCombos := 10
	if len(req.ActiveCombos) > maxCombos {
		return fmt.Errorf("too many active combos: %d (max %d)", len(req.ActiveCombos), maxCombos)
	}

	// Check for duplicate combo IDs
	seen := make(map[uuid.UUID]bool)
	for _, comboID := range req.ActiveCombos {
		if comboID == uuid.Nil {
			return fmt.Errorf("invalid combo ID in active combos")
		}
		if seen[comboID] {
			return fmt.Errorf("duplicate combo ID: %s", comboID)
		}
		seen[comboID] = true
	}

	// Validate preferences
	if err := s.validatePreferences(&req.Preferences); err != nil {
		return fmt.Errorf("invalid preferences: %w", err)
	}

	return nil
}

// validatePreferences validates loadout preferences
func (s *Service) validatePreferences(prefs *ComboLoadoutPreferences) error {
	if prefs == nil {
		return fmt.Errorf("preferences is nil")
	}

	maxPriorityCombos := 20 // should match active combos limit
	if len(prefs.PriorityOrder) > maxPriorityCombos {
		return fmt.Errorf("too many priority combos: %d (max %d)", len(prefs.PriorityOrder), maxPriorityCombos)
	}

	// Check that all priority combos are valid UUIDs
	for _, comboID := range prefs.PriorityOrder {
		if comboID == uuid.Nil {
			return fmt.Errorf("invalid combo ID in priority order")
		}
	}

	// Validate max active combos
	if prefs.MaxActiveCombos < 0 || prefs.MaxActiveCombos > 20 {
		return fmt.Errorf("invalid max active combos: %d (must be 0-20)", prefs.MaxActiveCombos)
	}

	return nil
}
