package server

import (
	"context"
	"fmt"

	"github.com/necpgame/cosmetic-service-go/models"
)

type CosmeticCatalogService struct {
	catalogRepo *CosmeticCatalogRepository
}

func NewCosmeticCatalogService(catalogRepo *CosmeticCatalogRepository) *CosmeticCatalogService {
	return &CosmeticCatalogService{
		catalogRepo: catalogRepo,
	}
}

func (s *CosmeticCatalogService) GetCatalog(ctx context.Context, category, rarity *string, limit, offset int) (*models.CosmeticCatalogResponse, error) {
	if limit <= 0 {
		limit = 50
	}
	if limit > 100 {
		limit = 100
	}
	if offset < 0 {
		offset = 0
	}

	items, total, err := s.catalogRepo.GetCatalog(ctx, category, rarity, limit, offset)
	if err != nil {
		return nil, fmt.Errorf("failed to get catalog: %w", err)
	}

	return &models.CosmeticCatalogResponse{
		Items:  items,
		Total:  total,
		Limit:  limit,
		Offset: offset,
	}, nil
}

func (s *CosmeticCatalogService) GetCosmeticByID(ctx context.Context, cosmeticID string) (*models.CosmeticItem, error) {
	id, err := parseUUID(cosmeticID)
	if err != nil {
		return nil, fmt.Errorf("invalid cosmetic ID: %w", err)
	}

	item, err := s.catalogRepo.GetCosmeticByID(ctx, id)
	if err != nil {
		return nil, fmt.Errorf("failed to get cosmetic: %w", err)
	}

	if item == nil {
		return nil, fmt.Errorf("cosmetic not found: %s", cosmeticID)
	}

	return item, nil
}

func (s *CosmeticCatalogService) GetCategories(ctx context.Context) (*models.CosmeticCategoriesResponse, error) {
	categories, err := s.catalogRepo.GetCategories(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to get categories: %w", err)
	}

	return &models.CosmeticCategoriesResponse{
		Categories: categories,
	}, nil
}

