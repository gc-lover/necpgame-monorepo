package server

import (
	"context"
	"fmt"

	"github.com/necpgame/cosmetic-service-go/models"
)

type CosmeticShopService struct {
	shopRepo *CosmeticShopRepository
}

func NewCosmeticShopService(shopRepo *CosmeticShopRepository) *CosmeticShopService {
	return &CosmeticShopService{
		shopRepo: shopRepo,
	}
}

func (s *CosmeticShopService) GetDailyShop(ctx context.Context) (*models.DailyShopResponse, error) {
	dailyShop, err := s.shopRepo.GetDailyShop(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to get daily shop: %w", err)
	}

	if dailyShop == nil {
		return &models.DailyShopResponse{
			Items: []models.CosmeticItem{},
		}, nil
	}

	return &models.DailyShopResponse{
		RotationID:     dailyShop.RotationID,
		RotationDate:   dailyShop.RotationDate,
		Items:          dailyShop.Items,
		NextRotationAt: dailyShop.NextRotationAt,
	}, nil
}

func (s *CosmeticShopService) GetShopHistory(ctx context.Context, limit, offset int) (*models.ShopHistoryResponse, error) {
	if limit <= 0 {
		limit = 50
	}
	if limit > 100 {
		limit = 100
	}
	if offset < 0 {
		offset = 0
	}

	rotations, total, err := s.shopRepo.GetShopHistory(ctx, limit, offset)
	if err != nil {
		return nil, fmt.Errorf("failed to get shop history: %w", err)
	}

	return &models.ShopHistoryResponse{
		Rotations: rotations,
		Total:     total,
		Limit:     limit,
		Offset:    offset,
	}, nil
}

