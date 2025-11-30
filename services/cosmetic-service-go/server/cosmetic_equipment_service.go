package server

import (
	"context"
	"fmt"

	"github.com/necpgame/cosmetic-service-go/models"
)

type CosmeticEquipmentService struct {
	equipmentRepo *CosmeticEquipmentRepository
	inventoryRepo *CosmeticInventoryRepository
}

func NewCosmeticEquipmentService(equipmentRepo *CosmeticEquipmentRepository, inventoryRepo *CosmeticInventoryRepository) *CosmeticEquipmentService {
	return &CosmeticEquipmentService{
		equipmentRepo: equipmentRepo,
		inventoryRepo: inventoryRepo,
	}
}

func (s *CosmeticEquipmentService) EquipCosmetic(ctx context.Context, cosmeticID string, req *models.EquipCosmeticRequest) (*models.EquippedCosmetics, error) {
	id, err := parseUUID(cosmeticID)
	if err != nil {
		return nil, fmt.Errorf("invalid cosmetic ID: %w", err)
	}

	owned, err := s.inventoryRepo.CheckOwnership(ctx, req.PlayerID, id)
	if err != nil {
		return nil, fmt.Errorf("failed to check ownership: %w", err)
	}

	if owned == nil {
		return nil, fmt.Errorf("player does not own cosmetic: %s", cosmeticID)
	}

	err = s.equipmentRepo.EquipCosmetic(ctx, req.PlayerID, id, req.Slot)
	if err != nil {
		return nil, fmt.Errorf("failed to equip cosmetic: %w", err)
	}

	equipped, err := s.equipmentRepo.GetEquippedCosmetics(ctx, req.PlayerID)
	if err != nil {
		return nil, fmt.Errorf("failed to get equipped cosmetics: %w", err)
	}

	return equipped, nil
}

func (s *CosmeticEquipmentService) UnequipCosmetic(ctx context.Context, cosmeticID string, req *models.UnequipCosmeticRequest) error {
	err := s.equipmentRepo.UnequipCosmetic(ctx, req.PlayerID, req.Slot)
	if err != nil {
		return fmt.Errorf("failed to unequip cosmetic: %w", err)
	}

	return nil
}

func (s *CosmeticEquipmentService) GetEquippedCosmetics(ctx context.Context, playerID string) (*models.EquippedCosmetics, error) {
	id, err := parseUUID(playerID)
	if err != nil {
		return nil, fmt.Errorf("invalid player ID: %w", err)
	}

	equipped, err := s.equipmentRepo.GetEquippedCosmetics(ctx, id)
	if err != nil {
		return nil, fmt.Errorf("failed to get equipped cosmetics: %w", err)
	}

	return equipped, nil
}

