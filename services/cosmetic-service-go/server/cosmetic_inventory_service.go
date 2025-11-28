package server

import (
	"context"
	"fmt"

	"github.com/google/uuid"
	"github.com/necpgame/cosmetic-service-go/models"
)

type CosmeticInventoryService struct {
	inventoryRepo *CosmeticInventoryRepository
}

func NewCosmeticInventoryService(inventoryRepo *CosmeticInventoryRepository) *CosmeticInventoryService {
	return &CosmeticInventoryService{
		inventoryRepo: inventoryRepo,
	}
}

func (s *CosmeticInventoryService) GetInventory(ctx context.Context, playerID string, category, rarity *string, limit, offset int) (*models.CosmeticInventoryResponse, error) {
	id, err := parseUUID(playerID)
	if err != nil {
		return nil, fmt.Errorf("invalid player ID: %w", err)
	}

	if limit <= 0 {
		limit = 50
	}
	if limit > 100 {
		limit = 100
	}
	if offset < 0 {
		offset = 0
	}

	cosmetics, total, err := s.inventoryRepo.GetInventory(ctx, id, category, rarity, limit, offset)
	if err != nil {
		return nil, fmt.Errorf("failed to get inventory: %w", err)
	}

	return &models.CosmeticInventoryResponse{
		PlayerID:  id,
		Cosmetics: cosmetics,
		Total:     total,
		Limit:     limit,
		Offset:    offset,
	}, nil
}

func (s *CosmeticInventoryService) CheckOwnership(ctx context.Context, playerID, cosmeticID string) (*models.OwnershipStatusResponse, error) {
	playerUUID, err := parseUUID(playerID)
	if err != nil {
		return nil, fmt.Errorf("invalid player ID: %w", err)
	}

	cosmeticUUID, err := parseUUID(cosmeticID)
	if err != nil {
		return nil, fmt.Errorf("invalid cosmetic ID: %w", err)
	}

	playerCosmetic, err := s.inventoryRepo.CheckOwnership(ctx, playerUUID, cosmeticUUID)
	if err != nil {
		return nil, fmt.Errorf("failed to check ownership: %w", err)
	}

	owned := playerCosmetic != nil

	return &models.OwnershipStatusResponse{
		PlayerID:       playerUUID,
		CosmeticID:     cosmeticUUID,
		Owned:          owned,
		PlayerCosmetic: playerCosmetic,
	}, nil
}

func (s *CosmeticInventoryService) GetCosmeticsByRarity(ctx context.Context, rarity string, category *string, limit, offset int) (*models.CosmeticCatalogResponse, error) {
	if limit <= 0 {
		limit = 50
	}
	if limit > 100 {
		limit = 100
	}
	if offset < 0 {
		offset = 0
	}

	items, total, err := s.inventoryRepo.GetCosmeticsByRarity(ctx, rarity, category, limit, offset)
	if err != nil {
		return nil, fmt.Errorf("failed to get cosmetics by rarity: %w", err)
	}

	return &models.CosmeticCatalogResponse{
		Items:  items,
		Total:  total,
		Limit:  limit,
		Offset: offset,
	}, nil
}

func (s *CosmeticInventoryService) GetEvents(ctx context.Context, playerID string, eventType *string, limit, offset int) (*models.CosmeticEventsResponse, error) {
	id, err := parseUUID(playerID)
	if err != nil {
		return nil, fmt.Errorf("invalid player ID: %w", err)
	}

	if limit <= 0 {
		limit = 50
	}
	if limit > 100 {
		limit = 100
	}
	if offset < 0 {
		offset = 0
	}

	events, total, err := s.inventoryRepo.GetEvents(ctx, id, eventType, limit, offset)
	if err != nil {
		return nil, fmt.Errorf("failed to get events: %w", err)
	}

	return &models.CosmeticEventsResponse{
		Events: events,
		Total:  total,
		Limit:  limit,
		Offset: offset,
	}, nil
}

