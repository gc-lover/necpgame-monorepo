package server

import (
	"context"
	"encoding/json"
	"net/http"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/inventory-service-go/models"
	"github.com/necpgame/inventory-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type InventoryServiceInterface interface {
	GetInventory(ctx context.Context, characterID uuid.UUID) (*models.InventoryResponse, error)
	AddItem(ctx context.Context, characterID uuid.UUID, req *models.AddItemRequest) error
	RemoveItem(ctx context.Context, characterID uuid.UUID, itemID uuid.UUID) error
	EquipItem(ctx context.Context, characterID uuid.UUID, req *models.EquipItemRequest) error
	UnequipItem(ctx context.Context, characterID uuid.UUID, itemID uuid.UUID) error
}

type InventoryHandlers struct {
	service InventoryServiceInterface
	logger  *logrus.Logger
}

func NewInventoryHandlers(service InventoryServiceInterface) *InventoryHandlers {
	return &InventoryHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *InventoryHandlers) GetInventory(w http.ResponseWriter, r *http.Request, characterId api.CharacterIdCamel) {
	ctx := r.Context()
	characterID := uuid.UUID(characterId)

	response, err := h.service.GetInventory(ctx, characterID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get inventory")
		h.respondError(w, http.StatusInternalServerError, "failed to get inventory")
		return
	}

	apiResponse := toAPIInventoryResponse(response)
	h.respondJSON(w, http.StatusOK, apiResponse)
}

func (h *InventoryHandlers) AddItem(w http.ResponseWriter, r *http.Request, characterId api.CharacterIdCamel) {
	ctx := r.Context()
	characterID := uuid.UUID(characterId)

	var req api.AddItemRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	modelReq := &models.AddItemRequest{
		ItemID:     req.ItemId,
		StackCount: req.StackCount,
	}

	err := h.service.AddItem(ctx, characterID, modelReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to add item")
		h.respondError(w, http.StatusInternalServerError, "failed to add item")
		return
	}

	successResponse := api.SuccessResponse{
		Status: stringPtr("success"),
	}
	h.respondJSON(w, http.StatusOK, successResponse)
}

func (h *InventoryHandlers) RemoveItem(w http.ResponseWriter, r *http.Request, characterId api.CharacterIdCamel, itemId api.ItemIdCamel) {
	ctx := r.Context()
	characterID := uuid.UUID(characterId)
	itemID := uuid.UUID(itemId)

	err := h.service.RemoveItem(ctx, characterID, itemID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to remove item")
		h.respondError(w, http.StatusInternalServerError, "failed to remove item")
		return
	}

	successResponse := api.SuccessResponse{
		Status: stringPtr("success"),
	}
	h.respondJSON(w, http.StatusOK, successResponse)
}

func (h *InventoryHandlers) EquipItem(w http.ResponseWriter, r *http.Request, characterId api.CharacterIdCamel) {
	ctx := r.Context()
	characterID := uuid.UUID(characterId)

	var req api.EquipItemRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	modelReq := &models.EquipItemRequest{
		ItemID:    req.ItemId,
		EquipSlot: req.EquipSlot,
	}

	err := h.service.EquipItem(ctx, characterID, modelReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to equip item")
		h.respondError(w, http.StatusInternalServerError, "failed to equip item")
		return
	}

	successResponse := api.SuccessResponse{
		Status: stringPtr("success"),
	}
	h.respondJSON(w, http.StatusOK, successResponse)
}

func (h *InventoryHandlers) UnequipItem(w http.ResponseWriter, r *http.Request, characterId api.CharacterIdCamel, itemId api.ItemIdCamel) {
	ctx := r.Context()
	characterID := uuid.UUID(characterId)
	itemID := uuid.UUID(itemId)

	err := h.service.UnequipItem(ctx, characterID, itemID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to unequip item")
		h.respondError(w, http.StatusInternalServerError, "failed to unequip item")
		return
	}

	successResponse := api.SuccessResponse{
		Status: stringPtr("success"),
	}
	h.respondJSON(w, http.StatusOK, successResponse)
}

func (h *InventoryHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *InventoryHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := api.Error{
		Error:   http.StatusText(status),
		Message: message,
	}
	h.respondJSON(w, status, errorResponse)
}

func toAPIInventoryResponse(response *models.InventoryResponse) *api.InventoryResponse {
	if response == nil {
		return nil
	}

	apiInventory := toAPIInventory(&response.Inventory)
	apiItems := make([]api.InventoryItem, len(response.Items))
	for i, item := range response.Items {
		apiItems[i] = toAPIInventoryItem(item)
	}

	return &api.InventoryResponse{
		Inventory: apiInventory,
		Items:     &apiItems,
	}
}

func toAPIInventory(inventory *models.Inventory) *api.Inventory {
	if inventory == nil {
		return nil
	}

	apiID := openapi_types.UUID(inventory.ID)
	apiCharID := openapi_types.UUID(inventory.CharacterID)

	weight32 := float32(inventory.Weight)
	maxWeight32 := float32(inventory.MaxWeight)
	
	return &api.Inventory{
		Id:          &apiID,
		CharacterId: &apiCharID,
		Capacity:    intPtr(inventory.Capacity),
		UsedSlots:   intPtr(inventory.UsedSlots),
		Weight:      &weight32,
		MaxWeight:   &maxWeight32,
		CreatedAt:   timePtr(inventory.CreatedAt),
		UpdatedAt:   timePtr(inventory.UpdatedAt),
	}
}

func toAPIInventoryItem(item models.InventoryItem) api.InventoryItem {
	apiID := openapi_types.UUID(item.ID)
	apiInventoryID := openapi_types.UUID(item.InventoryID)

	result := api.InventoryItem{
		Id:           &apiID,
		InventoryId:  &apiInventoryID,
		ItemId:       stringPtr(item.ItemID),
		SlotIndex:    intPtr(item.SlotIndex),
		StackCount:   intPtr(item.StackCount),
		MaxStackSize: intPtr(item.MaxStackSize),
		IsEquipped:   boolPtr(item.IsEquipped),
		CreatedAt:    timePtr(item.CreatedAt),
		UpdatedAt:    timePtr(item.UpdatedAt),
	}

	if item.EquipSlot != "" {
		result.EquipSlot = stringPtr(item.EquipSlot)
	}

	if item.Metadata != nil {
		result.Metadata = &item.Metadata
	}

	return result
}

func stringPtr(s string) *string {
	return &s
}

func boolPtr(b bool) *bool {
	return &b
}

func intPtr(i int) *int {
	return &i
}


func timePtr(t time.Time) *time.Time {
	return &t
}

