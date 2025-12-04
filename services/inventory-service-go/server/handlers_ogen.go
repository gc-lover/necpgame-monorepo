// Issue: #1591 - ogen typed handlers (HOT PATH - 10k RPS)
// OPTIMIZATION: Typed responses, no interface{} boxing, ogen optimized JSON marshaling
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/inventory-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

// Context timeout constants (OPTIMIZATION: Issue #1581, #1591)
const (
	DBTimeout    = 50 * time.Millisecond // DB queries
	CacheTimeout = 10 * time.Millisecond // Cache queries
)

// InventoryHandlersOgen implements api.Handler interface (ogen typed handlers!)
type InventoryHandlersOgen struct {
	service InventoryServiceInterface
	logger  *logrus.Logger
}

// NewInventoryHandlersOgen creates ogen handlers
func NewInventoryHandlersOgen(service InventoryServiceInterface) *InventoryHandlersOgen {
	return &InventoryHandlersOgen{
		service: service,
		logger:  GetLogger(),
	}
}

// GetInventory - TYPED ogen response (no interface{}, no manual JSON!)
// HOT PATH: 10k+ RPS expected
func (h *InventoryHandlersOgen) GetInventory(ctx context.Context, params api.GetInventoryParams) (*api.InventoryResponse, error) {
	// CRITICAL: Context timeout (hot path!)
	ctx, cancel := context.WithTimeout(ctx, CacheTimeout) // 10ms - should hit cache
	defer cancel()

	playerID := params.PlayerID.String()

	response, err := h.service.GetInventory(ctx, playerID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get inventory")
		return nil, err
	}

	// Response is already *api.InventoryResponse
	return response, nil
}

// AddItem - TYPED ogen response
func (h *InventoryHandlersOgen) AddItem(ctx context.Context, req *api.AddItemRequest, params api.AddItemParams) (api.AddItemRes, error) {
	// CRITICAL: Context timeout
	ctx, cancel := context.WithTimeout(ctx, DBTimeout) // 50ms DB operation
	defer cancel()

	playerID := params.PlayerID.String()

	// Convert ogen types to API request
	quantity := 1
	if req.Quantity.Set {
		quantity = req.Quantity.Value
	}

	apiReq := &api.AddItemRequest{
		ItemID:   req.ItemID,
		Quantity: api.NewOptInt(quantity),
	}

	response, err := h.service.AddItem(ctx, playerID, apiReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to add item")
		return &api.Error{
			Error:   "INTERNAL_SERVER_ERROR",
			Message: err.Error(),
		}, nil
	}

	// Return the added item response
	return response, nil
}

// RemoveItem - TYPED ogen response
func (h *InventoryHandlersOgen) RemoveItem(ctx context.Context, params api.RemoveItemParams) (api.RemoveItemRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	playerID := params.PlayerID.String()
	itemID := params.ItemID.String()

	err := h.service.RemoveItem(ctx, playerID, itemID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to remove item")
		return &api.Error{
			Error:   "INTERNAL_SERVER_ERROR",
			Message: err.Error(),
		}, nil
	}

	return &api.SuccessResponse{
		Status: api.NewOptString("success"),
	}, nil
}

// EquipItem - TYPED ogen response
func (h *InventoryHandlersOgen) EquipItem(ctx context.Context, req *api.EquipItemRequest, params api.EquipItemParams) (api.EquipItemRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	playerID := params.PlayerID.String()
	itemID := params.ItemID.String()

	response, err := h.service.EquipItem(ctx, playerID, itemID, req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to equip item")
		return &api.Error{
			Error:   "INTERNAL_SERVER_ERROR",
			Message: err.Error(),
		}, nil
	}

	if response == nil {
		// Return empty response if service returns nil
		return &api.EquipmentResponse{
			PlayerID:  api.NewOptUUID(params.PlayerID),
			Equipment: api.OptEquipmentResponseEquipment{},
		}, nil
	}

	return response, nil
}

// UnequipItem - TYPED ogen response
func (h *InventoryHandlersOgen) UnequipItem(ctx context.Context, params api.UnequipItemParams) (*api.EquipmentResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	playerID := params.PlayerID.String()
	itemID := params.ItemID.String()

	response, err := h.service.UnequipItem(ctx, playerID, itemID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to unequip item")
		return nil, err
	}

	if response == nil {
		// Return empty response if service returns nil
		return &api.EquipmentResponse{
			PlayerID:  api.NewOptUUID(params.PlayerID),
			Equipment: api.OptEquipmentResponseEquipment{},
		}, nil
	}

	return response, nil
}

// GetEquipment - TYPED ogen response
func (h *InventoryHandlersOgen) GetEquipment(ctx context.Context, params api.GetEquipmentParams) (*api.EquipmentResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, CacheTimeout) // Should be cached
	defer cancel()

	playerID := params.PlayerID.String()

	response, err := h.service.GetEquipment(ctx, playerID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get equipment")
		return nil, err
	}

	return response, nil
}

// UpdateItem - TYPED ogen response
func (h *InventoryHandlersOgen) UpdateItem(ctx context.Context, req *api.UpdateItemRequest, params api.UpdateItemParams) (*api.InventoryItemResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	playerID := params.PlayerID.String()
	itemID := params.ItemID.String()

	response, err := h.service.UpdateItem(ctx, playerID, itemID, req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to update item")
		return nil, err
	}

	return response, nil
}

// GetItem - TYPED ogen response
func (h *InventoryHandlersOgen) GetItem(ctx context.Context, params api.GetItemParams) (api.GetItemRes, error) {
	ctx, cancel := context.WithTimeout(ctx, CacheTimeout)
	defer cancel()

	// TODO: Implement GetItem in service
	return &api.InventoryItemResponse{}, nil
}

// MoveItem - TYPED ogen response
func (h *InventoryHandlersOgen) MoveItem(ctx context.Context, req *api.MoveItemRequest, params api.MoveItemParams) (api.MoveItemRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement MoveItem in service
	return &api.SuccessResponse{
		Status: api.NewOptString("success"),
	}, nil
}

// GetVaults - TYPED ogen response
func (h *InventoryHandlersOgen) GetVaults(ctx context.Context, params api.GetVaultsParams) (*api.VaultsListResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, CacheTimeout)
	defer cancel()

	// TODO: Implement GetVaults in service
	return &api.VaultsListResponse{
		Vaults: []api.VaultResponse{},
	}, nil
}

// GetVault - TYPED ogen response
func (h *InventoryHandlersOgen) GetVault(ctx context.Context, params api.GetVaultParams) (api.GetVaultRes, error) {
	ctx, cancel := context.WithTimeout(ctx, CacheTimeout)
	defer cancel()

	// TODO: Implement GetVault in service
	return &api.VaultResponse{}, nil
}

// RetrieveItem - TYPED ogen response
func (h *InventoryHandlersOgen) RetrieveItem(ctx context.Context, params api.RetrieveItemParams) (api.RetrieveItemRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement RetrieveItem in service
	return &api.SuccessResponse{
		Status: api.NewOptString("success"),
	}, nil
}

// StoreItem - TYPED ogen response
func (h *InventoryHandlersOgen) StoreItem(ctx context.Context, req *api.StoreItemRequest, params api.StoreItemParams) (api.StoreItemRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement StoreItem in service
	return &api.SuccessResponse{
		Status: api.NewOptString("success"),
	}, nil
}

// Converter helpers removed - service returns *api.InventoryResponse directly


