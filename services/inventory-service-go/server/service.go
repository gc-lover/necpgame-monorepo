// Issue: #135
package server

import (
	"context"
	"errors"

	"github.com/gc-lover/necpgame/services/inventory-service-go/pkg/api"
)

type Service interface {
	GetInventory(ctx context.Context, playerID string) (*api.InventoryResponse, error)
	AddItem(ctx context.Context, playerID string, req *api.AddItemRequest) (*api.InventoryItemResponse, error)
	GetItem(ctx context.Context, playerID, itemID string) (*api.InventoryItemResponse, error)
	UpdateItem(ctx context.Context, playerID, itemID string, req *api.UpdateItemRequest) (*api.InventoryItemResponse, error)
	RemoveItem(ctx context.Context, playerID, itemID string) error
	MoveItem(ctx context.Context, playerID, itemID string, req *api.MoveItemRequest) error
	GetEquipment(ctx context.Context, playerID string) (*api.EquipmentResponse, error)
	EquipItem(ctx context.Context, playerID, itemID string, req *api.EquipItemRequest) (*api.EquipmentResponse, error)
	UnequipItem(ctx context.Context, playerID, itemID string) (*api.EquipmentResponse, error)
	GetVaults(ctx context.Context, playerID string) (*api.VaultsListResponse, error)
	GetVault(ctx context.Context, vaultID string) (*api.VaultResponse, error)
	StoreItem(ctx context.Context, vaultID string, req *api.StoreItemRequest) error
	RetrieveItem(ctx context.Context, vaultID, itemID string) error
}

type InventoryService struct {
	repository Repository
}

func NewInventoryService(repository Repository) Service {
	return &InventoryService{repository: repository}
}

func (s *InventoryService) GetInventory(ctx context.Context, playerID string) (*api.InventoryResponse, error) {
	return nil, errors.New("not implemented")
}

func (s *InventoryService) AddItem(ctx context.Context, playerID string, req *api.AddItemRequest) (*api.InventoryItemResponse, error) {
	return nil, errors.New("not implemented")
}

func (s *InventoryService) GetItem(ctx context.Context, playerID, itemID string) (*api.InventoryItemResponse, error) {
	return nil, errors.New("not implemented")
}

func (s *InventoryService) UpdateItem(ctx context.Context, playerID, itemID string, req *api.UpdateItemRequest) (*api.InventoryItemResponse, error) {
	return nil, errors.New("not implemented")
}

func (s *InventoryService) RemoveItem(ctx context.Context, playerID, itemID string) error {
	return nil
}

func (s *InventoryService) MoveItem(ctx context.Context, playerID, itemID string, req *api.MoveItemRequest) error {
	return nil
}

func (s *InventoryService) GetEquipment(ctx context.Context, playerID string) (*api.EquipmentResponse, error) {
	return nil, errors.New("not implemented")
}

func (s *InventoryService) EquipItem(ctx context.Context, playerID, itemID string, req *api.EquipItemRequest) (*api.EquipmentResponse, error) {
	return nil, errors.New("not implemented")
}

func (s *InventoryService) UnequipItem(ctx context.Context, playerID, itemID string) (*api.EquipmentResponse, error) {
	return nil, errors.New("not implemented")
}

func (s *InventoryService) GetVaults(ctx context.Context, playerID string) (*api.VaultsListResponse, error) {
	return &api.VaultsListResponse{Vaults: &[]api.VaultResponse{}}, nil
}

func (s *InventoryService) GetVault(ctx context.Context, vaultID string) (*api.VaultResponse, error) {
	return nil, errors.New("not implemented")
}

func (s *InventoryService) StoreItem(ctx context.Context, vaultID string, req *api.StoreItemRequest) error {
	return nil
}

func (s *InventoryService) RetrieveItem(ctx context.Context, vaultID, itemID string) error {
	return nil
}

