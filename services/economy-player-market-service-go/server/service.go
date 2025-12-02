// Issue: #42
package server

import (
	"context"

	"github.com/gc-lover/necpgame-monorepo/services/economy-player-market-service-go/pkg/api"
)

type PlayerMarketService struct {
	repo *PlayerMarketRepository
}

func NewPlayerMarketService(repo *PlayerMarketRepository) *PlayerMarketService {
	return &PlayerMarketService{repo: repo}
}

// ListListings возвращает список объявлений
func (s *PlayerMarketService) ListListings(ctx context.Context, params api.ListListingsParams) ([]api.MarketListing, error) {
	return s.repo.ListListings(ctx, params)
}

// CreateListing создает новое объявление
func (s *PlayerMarketService) CreateListing(ctx context.Context, req *api.CreateListingRequest) (*api.MarketListing, error) {
	return s.repo.CreateListing(ctx, req)
}

// GetListing возвращает объявление по ID
func (s *PlayerMarketService) GetListing(ctx context.Context, listingID string) (*api.MarketListing, error) {
	return s.repo.GetListing(ctx, listingID)
}

// PurchaseListing покупает товар
func (s *PlayerMarketService) PurchaseListing(ctx context.Context, listingID string, req *api.PurchaseListingRequest) (*api.MarketTransaction, error) {
	// TODO: Implement transaction logic
	// 1. Check listing availability
	// 2. Check buyer balance
	// 3. Transfer items
	// 4. Create transaction record
	return s.repo.PurchaseListing(ctx, listingID, req)
}

