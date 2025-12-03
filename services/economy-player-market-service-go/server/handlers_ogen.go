// Issue: #1594 - economy-player-market ogen typed handlers
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/economy-player-market-service-go/pkg/api"
	"github.com/google/uuid"
)

const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
)

type MarketHandlersOgen struct{}

func NewMarketHandlersOgen() *MarketHandlersOgen {
	return &MarketHandlersOgen{}
}

// CreateMarketListing implements api.Handler
func (h *MarketHandlersOgen) CreateMarketListing(ctx context.Context, req *api.CreateListingRequest) (api.CreateMarketListingRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// MarketListing implements createMarketListingRes
	return &api.MarketListing{
		ID:        uuid.New(),
		SellerID:  req.CharacterID,
		ItemID:    req.ItemID,
		Price:     req.Price,
		Status:    api.MarketListingStatusActive,
		CreatedAt: time.Now(),
	}, nil
}

// GetMarketListingById implements api.Handler
func (h *MarketHandlersOgen) GetMarketListingById(ctx context.Context, params api.GetMarketListingByIdParams) (api.GetMarketListingByIdRes, error) {
	ctx, cancel := context.WithTimeout(ctx, CacheTimeout)
	defer cancel()

	// MarketListing implements getMarketListingByIdRes
	return &api.MarketListing{
		ID:        params.ListingID,
		SellerID:  uuid.New(),
		ItemID:    uuid.New(),
		Price:     100.0,
		Status:    api.MarketListingStatusActive,
		CreatedAt: time.Now(),
	}, nil
}

// SearchMarketListings implements api.Handler
func (h *MarketHandlersOgen) SearchMarketListings(ctx context.Context, params api.SearchMarketListingsParams) (api.SearchMarketListingsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// SearchMarketListingsOK implements searchMarketListingsRes
	return &api.SearchMarketListingsOK{
		Listings: []api.MarketListing{},
		Total:    api.NewOptInt(0),
	}, nil
}

// GetMyMarketListings implements api.Handler
func (h *MarketHandlersOgen) GetMyMarketListings(ctx context.Context, params api.GetMyMarketListingsParams) (api.GetMyMarketListingsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// GetMyMarketListingsOK implements getMyMarketListingsRes
	return &api.GetMyMarketListingsOK{
		Listings: []api.MarketListing{},
		Total:    api.NewOptInt(0),
	}, nil
}

// UpdateMarketListing implements api.Handler
func (h *MarketHandlersOgen) UpdateMarketListing(ctx context.Context, req *api.UpdateListingRequest, params api.UpdateMarketListingParams) (api.UpdateMarketListingRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// MarketListing implements updateMarketListingRes
	return &api.MarketListing{
		ID:        params.ListingID,
		SellerID:  uuid.New(),
		ItemID:    uuid.New(),
		Price:     req.Price.Value,
		Status:    api.MarketListingStatusActive,
		CreatedAt: time.Now(),
	}, nil
}

// CancelMarketListing implements api.Handler
func (h *MarketHandlersOgen) CancelMarketListing(ctx context.Context, params api.CancelMarketListingParams) (api.CancelMarketListingRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// StatusResponse implements cancelMarketListingRes
	return &api.StatusResponse{
		Status: api.NewOptString("cancelled"),
	}, nil
}

// PurchaseMarketItem implements api.Handler
func (h *MarketHandlersOgen) PurchaseMarketItem(ctx context.Context, req *api.PurchaseRequest) (api.PurchaseMarketItemRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// PurchaseResult implements purchaseMarketItemRes
	return &api.PurchaseResult{
		PurchaseID: uuid.New(),
		Success:    true,
		ItemID:     api.NewOptUUID(req.ListingID),
		PricePaid:  api.NewOptFloat32(100.0),
	}, nil
}

// GetPurchaseHistory implements api.Handler
func (h *MarketHandlersOgen) GetPurchaseHistory(ctx context.Context, params api.GetPurchaseHistoryParams) (api.GetPurchaseHistoryRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// GetPurchaseHistoryOK implements getPurchaseHistoryRes
	return &api.GetPurchaseHistoryOK{
		Purchases: []api.PurchaseHistory{},
		Total:     api.NewOptInt(0),
	}, nil
}

// GetSalesHistory implements api.Handler
func (h *MarketHandlersOgen) GetSalesHistory(ctx context.Context, params api.GetSalesHistoryParams) (api.GetSalesHistoryRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// GetSalesHistoryOK implements getSalesHistoryRes
	return &api.GetSalesHistoryOK{
		Sales: []api.SaleHistory{},
		Total: api.NewOptInt(0),
	}, nil
}

// GetSellerProfile implements api.Handler
func (h *MarketHandlersOgen) GetSellerProfile(ctx context.Context, params api.GetSellerProfileParams) (api.GetSellerProfileRes, error) {
	ctx, cancel := context.WithTimeout(ctx, CacheTimeout)
	defer cancel()

	// SellerProfile implements getSellerProfileRes
	return &api.SellerProfile{
		SellerID:   params.SellerID,
		TotalSales: 0,
		Rating:     5.0,
	}, nil
}

// CreateSellerReview implements api.Handler
func (h *MarketHandlersOgen) CreateSellerReview(ctx context.Context, req *api.CreateReviewRequest) (api.CreateSellerReviewRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// SellerReview implements createSellerReviewRes
	return &api.SellerReview{
		ID:         uuid.New(),
		PurchaseID: req.PurchaseID,
		SellerID:   req.SellerID,
		BuyerID:    uuid.New(), // TODO: from auth context
		CreatedAt:  time.Now(),
		Rating:     req.Rating,
	}, nil
}
