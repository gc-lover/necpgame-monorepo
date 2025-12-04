// Issue: #1591 - Repository adapter for InventoryRepository
package server

import (
	"context"

	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/inventory-service-go/models"
)

// RepositoryAdapter adapts InventoryRepository to Repository interface
type RepositoryAdapter struct {
	repo *InventoryRepository
}

// NewRepositoryAdapter creates adapter
func NewRepositoryAdapter(repo *InventoryRepository) Repository {
	return &RepositoryAdapter{repo: repo}
}

// GetInventory implements Repository interface
func (a *RepositoryAdapter) GetInventory(ctx context.Context, characterID uuid.UUID) (*models.InventoryResponse, error) {
	// Get inventory
	inv, err := a.repo.GetInventoryByCharacterID(ctx, characterID)
	if err != nil {
		return nil, err
	}
	if inv == nil {
		// Create if not exists
		inv, err = a.repo.CreateInventory(ctx, characterID, 50, 100.0)
		if err != nil {
			return nil, err
		}
	}

	// Get items
	items, err := a.repo.GetInventoryItems(ctx, inv.ID)
	if err != nil {
		return nil, err
	}

	return &models.InventoryResponse{
		Inventory: *inv,
		Items:     items,
	}, nil
}

// AddItem implements Repository interface
func (a *RepositoryAdapter) AddItem(ctx context.Context, characterID uuid.UUID, req *models.AddItemRequest) error {
	// Get inventory
	inv, err := a.repo.GetInventoryByCharacterID(ctx, characterID)
	if err != nil {
		return err
	}
	if inv == nil {
		// Create if not exists
		inv, err = a.repo.CreateInventory(ctx, characterID, 50, 100.0)
		if err != nil {
			return err
		}
	}

	// Create item
	item := &models.InventoryItem{
		InventoryID: inv.ID,
		ItemID:      req.ItemID,
		StackCount:  req.StackCount,
	}

	return a.repo.AddItem(ctx, item)
}

// RemoveItem implements Repository interface
func (a *RepositoryAdapter) RemoveItem(ctx context.Context, characterID, itemID uuid.UUID) error {
	return a.repo.RemoveItem(ctx, itemID)
}

// UpdateItem implements Repository interface
func (a *RepositoryAdapter) UpdateItem(ctx context.Context, characterID, itemID uuid.UUID, updateFn func() error) error {
	// Execute update function
	return updateFn()
}
