// Issue: #1591 - ogen typed handlers (HOT PATH - 10k RPS)
// OPTIMIZATION: Typed responses, no interface{} boxing, ogen optimized JSON marshaling
package server

import (
	"context"
	"time"

	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/inventory-service-go/models"
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
	service *InventoryService
	logger  *logrus.Logger
}

// NewInventoryHandlersOgen creates ogen handlers
func NewInventoryHandlersOgen(service *InventoryService) *InventoryHandlersOgen {
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

	characterID := uuid.UUID(params.PlayerID)

	response, err := h.service.GetInventory(ctx, characterID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get inventory")
		return nil, err
	}

	// Convert to ogen API type
	apiResponse := toOgenInventoryResponse(response)
	return apiResponse, nil
}

// AddItem - TYPED ogen response
func (h *InventoryHandlersOgen) AddItem(ctx context.Context, req *api.AddItemRequest, params api.AddItemParams) (api.AddItemRes, error) {
	// CRITICAL: Context timeout
	ctx, cancel := context.WithTimeout(ctx, DBTimeout) // 50ms DB operation
	defer cancel()

	characterID := uuid.UUID(params.PlayerID)

	// Convert ogen types to models
	quantity := 1
	if req.Quantity.Set {
		quantity = req.Quantity.Value
	}

	modelReq := &models.AddItemRequest{
		ItemID:     req.ItemID.String(),
		StackCount: quantity,
	}

	err := h.service.AddItem(ctx, characterID, modelReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to add item")
		return &api.AddItemInternalServerError{}, err
	}

	return &api.SuccessResponse{
		Status: api.NewOptString("success"),
	}, nil
}

// RemoveItem - TYPED ogen response
func (h *InventoryHandlersOgen) RemoveItem(ctx context.Context, params api.RemoveItemParams) (api.RemoveItemRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	characterID := uuid.UUID(params.PlayerID)
	itemID := uuid.UUID(params.ItemID)

	err := h.service.RemoveItem(ctx, characterID, itemID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to remove item")
		return &api.RemoveItemInternalServerError{}, err
	}

	return &api.SuccessResponse{
		Status: api.NewOptString("success"),
	}, nil
}

// EquipItem - TYPED ogen response
func (h *InventoryHandlersOgen) EquipItem(ctx context.Context, req *api.EquipItemRequest, params api.EquipItemParams) (*api.EquipmentResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	characterID := uuid.UUID(params.PlayerID)
	itemID := uuid.UUID(params.ItemID)
	
	modelReq := &models.EquipItemRequest{
		ItemID:    itemID.String(),
		EquipSlot: string(req.EquipmentSlot),
	}

	err := h.service.EquipItem(ctx, characterID, modelReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to equip item")
		return nil, err
	}

	return &api.EquipmentResponse{
		Equipment: api.OptEquipmentResponseEquipment{},
	}, nil
}

// UnequipItem - TYPED ogen response
func (h *InventoryHandlersOgen) UnequipItem(ctx context.Context, params api.UnequipItemParams) (*api.EquipmentResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	characterID := uuid.UUID(params.PlayerID)
	itemID := uuid.UUID(params.ItemID)

	err := h.service.UnequipItem(ctx, characterID, itemID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to unequip item")
		return nil, err
	}

	return &api.EquipmentResponse{
		Items: []api.EquippedItem{},
	}, nil
}

// GetEquipment - TYPED ogen response
func (h *InventoryHandlersOgen) GetEquipment(ctx context.Context, params api.GetEquipmentParams) (*api.EquipmentResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, CacheTimeout) // Should be cached
	defer cancel()

	// TODO: Implement GetEquipment in service
	return &api.EquipmentResponse{
		Items: []api.EquippedItem{},
	}, nil
}

// UpdateItem - TYPED ogen response
func (h *InventoryHandlersOgen) UpdateItem(ctx context.Context, req *api.UpdateItemRequest, params api.UpdateItemParams) (*api.InventoryItemResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement UpdateItem in service
	return &api.InventoryItemResponse{}, nil
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
		Vaults: []api.Vault{},
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

// Converter helpers

func toOgenInventoryResponse(resp *models.InventoryResponse) *api.InventoryResponse {
	// Convert models.InventoryResponse to api.InventoryResponse
	items := make([]api.InventoryItemResponse, 0, len(resp.Items))
	for _, item := range resp.Items {
		items = append(items, api.InventoryItemResponse{
			ID:           item.ID,
			InventoryID:  item.InventoryID,
			ItemID:       item.ItemID,
			SlotIndex:    item.SlotIndex,
			StackCount:   item.StackCount,
			MaxStackSize: item.MaxStackSize,
			IsEquipped:   item.IsEquipped,
			EquipSlot:    api.NewOptString(item.EquipSlot),
		})
	}

	return &api.InventoryResponse{
		ID:            resp.Inventory.ID,
		PlayerID:      resp.Inventory.CharacterID,
		Items:         items,
		MaxWeight:     resp.Inventory.MaxWeight,
		CurrentWeight: resp.Inventory.Weight,
		MaxSlots:      int32(resp.Inventory.Capacity),
		UsedSlots:     int32(resp.Inventory.UsedSlots),
	}
}


