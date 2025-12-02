package server

import (
	"context"
	"fmt"

	"github.com/necpgame/cosmetic-service-go/models"
)

type CosmeticPurchaseService struct {
	purchaseRepo *CosmeticPurchaseRepository
	catalogRepo  *CosmeticCatalogRepository
}

func NewCosmeticPurchaseService(purchaseRepo *CosmeticPurchaseRepository, catalogRepo *CosmeticCatalogRepository) *CosmeticPurchaseService {
	return &CosmeticPurchaseService{
		purchaseRepo: purchaseRepo,
		catalogRepo:  catalogRepo,
	}
}

func (s *CosmeticPurchaseService) PurchaseCosmetic(ctx context.Context, req *models.PurchaseCosmeticRequest) (*models.PlayerCosmetic, error) {
	cosmeticItem, err := s.catalogRepo.GetCosmeticByID(ctx, req.CosmeticID)
	if err != nil {
		return nil, fmt.Errorf("failed to get cosmetic item: %w", err)
	}

	if cosmeticItem == nil {
		return nil, fmt.Errorf("cosmetic item not found: %s", req.CosmeticID)
	}

	if !cosmeticItem.IsActive {
		return nil, fmt.Errorf("cosmetic item is not active: %s", req.CosmeticID)
	}

	playerCosmetic, err := s.purchaseRepo.CreatePlayerCosmetic(ctx, req.PlayerID, req.CosmeticID, "purchase")
	if err != nil {
		return nil, fmt.Errorf("failed to create player cosmetic: %w", err)
	}

	err = s.purchaseRepo.CreatePurchaseRecord(ctx, req.PlayerID, req.CosmeticID, cosmeticItem.Cost, cosmeticItem.CurrencyType)
	if err != nil {
		return nil, fmt.Errorf("failed to create purchase record: %w", err)
	}

	return playerCosmetic, nil
}

func (s *CosmeticPurchaseService) GetPurchaseHistory(ctx context.Context, playerID string, limit, offset int) (*models.PurchaseHistoryResponse, error) {
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

	purchases, total, err := s.purchaseRepo.GetPurchaseHistory(ctx, id, limit, offset)
	if err != nil {
		return nil, fmt.Errorf("failed to get purchase history: %w", err)
	}

	return &models.PurchaseHistoryResponse{
		Purchases: purchases,
		Total:     total,
		Limit:     limit,
		Offset:    offset,
	}, nil
}

