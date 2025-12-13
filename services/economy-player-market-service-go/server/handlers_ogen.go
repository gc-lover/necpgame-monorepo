// Issue: #42 - economy-player-market ogen typed handlers with business logic
package server

import (
	"context"
	"errors"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/economy-player-market-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
)

const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
)

type MarketHandlersOgen struct {
	db         *pgxpool.Pool
	repository *PlayerMarketRepository
}

func NewMarketHandlersOgen(db *pgxpool.Pool) *MarketHandlersOgen {
	return &MarketHandlersOgen{
		db:         db,
		repository: NewPlayerMarketRepository(db),
	}
}

// CreateMarketListing implements api.Handler
func (h *MarketHandlersOgen) CreateMarketListing(ctx context.Context, req *api.CreateListingRequest) (api.CreateMarketListingRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Validate input
	if req.CharacterID == uuid.Nil {
		return &api.CreateMarketListingBadRequest{
			Error:   "INVALID_CHARACTER_ID",
			Message: "Character ID is required",
		}, nil
	}

	if req.ItemID == uuid.Nil {
		return &api.CreateMarketListingBadRequest{
			Error:   "INVALID_ITEM_ID",
			Message: "Item ID is required",
		}, nil
	}

	if req.Price <= 0 || req.Price > 999999999 {
		return &api.CreateMarketListingBadRequest{
			Error:   "INVALID_PRICE",
			Message: "Price must be between 0.01 and 999,999,999",
		}, nil
	}

	// Check active listings limit (20 per seller)
	var activeCount int
	err := h.db.QueryRow(ctx, `
		SELECT COUNT(*) FROM economy.market_listings
		WHERE seller_id = $1 AND status = 'active'`, req.CharacterID).Scan(&activeCount)

	if err != nil {
		return &api.CreateMarketListingInternalServerError{
			Error:   "DATABASE_ERROR",
			Message: "Failed to check active listings",
		}, nil
	}

	if activeCount >= 20 {
		return &api.CreateMarketListingBadRequest{
			Error:   "LISTING_LIMIT_EXCEEDED",
			Message: "Maximum 20 active listings per seller",
		}, nil
	}

	// Create listing
	listingID, err := h.repository.CreateListing(ctx, req.CharacterID, req.ItemID, float64(req.Price), &req.CityID)
	if err != nil {
		return &api.CreateMarketListingInternalServerError{
			Error:   "DATABASE_ERROR",
			Message: "Failed to create listing",
		}, nil
	}

	// Return created listing
	return &api.MarketListing{
		ID:         listingID,
		SellerID:   req.CharacterID,
		ItemID:     req.ItemID,
		CityID:     api.NewOptUUID(req.CityID),
		Price:      req.Price,
		Status:     api.MarketListingStatusActive,
		CreatedAt:  time.Now(),
		ExpiresAt:  api.NewOptDateTime(time.Now().AddDate(0, 0, 7)),
		Commission: api.NewOptFloat32(req.Price * 0.01), // 1% commission
	}, nil
}

// GetMarketListingById implements api.Handler
func (h *MarketHandlersOgen) GetMarketListingById(ctx context.Context, params api.GetMarketListingByIdParams) (api.GetMarketListingByIdRes, error) {
	ctx, cancel := context.WithTimeout(ctx, CacheTimeout)
	defer cancel()

	row, err := h.repository.GetListingByID(ctx, params.ListingID)
	if err != nil {
		if errors.Is(err, pgx.ErrNoRows) {
			return &api.GetMarketListingByIdNotFound{
				Error:   "LISTING_NOT_FOUND",
				Message: "Listing not found or inactive",
			}, nil
		}
		return &api.GetMarketListingByIdInternalServerError{
			Error:   "DATABASE_ERROR",
			Message: "Failed to retrieve listing",
		}, nil
	}

	// Parse row data
	var listing api.MarketListing
	var cityID *uuid.UUID
	var cityNameStr *string
	var expiresAt *time.Time
	var views *int32

	err = row.Scan(
		&listing.ID, &listing.SellerID, &listing.ItemID, &listing.Price,
		&listing.Status, &listing.CreatedAt, &expiresAt,
		&cityID,
		&listing.SellerName, &listing.ItemName, &listing.ItemCategory,
		&listing.ItemQuality, &listing.ItemLevel, &cityNameStr,
		&views,
	)

	if err != nil {
		return &api.GetMarketListingByIdInternalServerError{
			Error:   "DATABASE_ERROR",
			Message: "Failed to parse listing data",
		}, nil
	}

	// Set optional fields
	if cityID != nil {
		listing.CityID = api.NewOptUUID(*cityID)
	}
	if cityNameStr != nil {
		listing.CityName = api.NewOptString(*cityNameStr)
	}
	if expiresAt != nil {
		listing.ExpiresAt = api.NewOptDateTime(*expiresAt)
	}
	if views != nil {
		listing.Views = api.NewOptInt32(*views)
	}
	listing.Commission = api.NewOptFloat32(listing.Price * 0.01)

	// Increment view counter
	go h.repository.IncrementListingViews(context.Background(), params.ListingID)

	return &listing, nil
}

// SearchMarketListings implements api.Handler
func (h *MarketHandlersOgen) SearchMarketListings(ctx context.Context, params api.SearchMarketListingsParams) (api.SearchMarketListingsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Build filters map
	filters := make(map[string]interface{})

	if params.ItemName.IsSet() && params.ItemName.Value != "" {
		filters["item_name"] = params.ItemName.Value
	}
	if params.Category.IsSet() && params.Category.Value != "" {
		filters["category"] = params.Category.Value
	}
	if params.Quality.IsSet() && params.Quality.Value != "" {
		filters["quality"] = params.Quality.Value
	}
	if params.MinLevel.IsSet() {
		filters["min_level"] = int(params.MinLevel.Value)
	}
	if params.MaxLevel.IsSet() {
		filters["max_level"] = int(params.MaxLevel.Value)
	}
	if params.MinPrice.IsSet() {
		filters["min_price"] = params.MinPrice.Value
	}
	if params.MaxPrice.IsSet() {
		filters["max_price"] = params.MaxPrice.Value
	}
	if params.CityID.IsSet() {
		filters["city_id"] = params.CityID.Value
	}
	if params.SellerID.IsSet() {
		filters["seller_id"] = params.SellerID.Value
	}
	if params.SortBy.IsSet() {
		filters["sort_by"] = params.SortBy.Value
	}

	// Set pagination defaults
	limit := 50
	offset := 0
	if params.Limit.IsSet() && params.Limit.Value > 0 && params.Limit.Value <= 100 {
		limit = int(params.Limit.Value)
	}
	if params.Offset.IsSet() && params.Offset.Value >= 0 {
		offset = int(params.Offset.Value)
	}

	// Get total count
	total, err := h.repository.CountListings(ctx, filters)
	if err != nil {
		return &api.SearchMarketListingsInternalServerError{
			Error:   "DATABASE_ERROR",
			Message: "Failed to count listings",
		}, nil
	}

	// Get listings
	rows, err := h.repository.SearchListings(ctx, filters, limit, offset)
	if err != nil {
		return &api.SearchMarketListingsInternalServerError{
			Error:   "DATABASE_ERROR",
			Message: "Failed to search listings",
		}, nil
	}
	defer rows.Close()

	var listings []api.MarketListing
	for rows.Next() {
		var listing api.MarketListing
		var cityID *uuid.UUID
		var cityNameStr *string
		var expiresAt *time.Time
		var views *int32

		err := rows.Scan(
			&listing.ID, &listing.SellerID, &listing.ItemID, &listing.Price,
			&listing.Status, &listing.CreatedAt, &expiresAt,
			&cityID,
			&listing.SellerName, &listing.ItemName, &listing.ItemCategory,
			&listing.ItemQuality, &listing.ItemLevel, &cityNameStr,
			&views,
		)
		if err != nil {
			continue // Skip malformed rows
		}

		// Set optional fields
		if cityID != nil {
			listing.CityID = api.NewOptUUID(*cityID)
		}
		if cityNameStr != nil {
			listing.CityName = api.NewOptString(*cityNameStr)
		}
		if expiresAt != nil {
			listing.ExpiresAt = api.NewOptDateTime(*expiresAt)
		}
		if views != nil {
			listing.Views = api.NewOptInt32(*views)
		}
		listing.Commission = api.NewOptFloat32(listing.Price * 0.01)

		listings = append(listings, listing)
	}

	return &api.SearchMarketListingsOK{
		Listings: listings,
		Total:    api.NewOptInt(total),
	}, nil
}

// GetMyMarketListings implements api.Handler
func (h *MarketHandlersOgen) GetMyMarketListings(ctx context.Context, params api.GetMyMarketListingsParams) (api.GetMyMarketListingsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Get character ID from auth context (assuming it's passed via middleware)
	// For now, using a placeholder - in real implementation this would come from JWT
	characterID := uuid.New() // TODO: get from auth context

	var statusFilter *string
	if params.Status.IsSet() {
		statusStr := string(params.Status.Value)
		statusFilter = &statusStr
	}

	// Set pagination defaults
	limit := 50
	offset := 0
	if params.Limit.IsSet() && params.Limit.Value > 0 && params.Limit.Value <= 100 {
		limit = int(params.Limit.Value)
	}
	if params.Offset.IsSet() && params.Offset.Value >= 0 {
		offset = int(params.Offset.Value)
	}

	// Get total count
	total, err := h.repository.CountMyListings(ctx, characterID, statusFilter)
	if err != nil {
		return &api.GetMyMarketListingsInternalServerError{
			Error:   "DATABASE_ERROR",
			Message: "Failed to count listings",
		}, nil
	}

	// Get listings
	rows, err := h.repository.GetMyListings(ctx, characterID, statusFilter, limit, offset)
	if err != nil {
		return &api.GetMyMarketListingsInternalServerError{
			Error:   "DATABASE_ERROR",
			Message: "Failed to get listings",
		}, nil
	}
	defer rows.Close()

	var listings []api.MarketListing
	for rows.Next() {
		var listing api.MarketListing
		var cityID *uuid.UUID
		var cityNameStr *string
		var expiresAt *time.Time

		err := rows.Scan(
			&listing.ID, &listing.SellerID, &listing.ItemID, &listing.Price,
			&listing.Status, &listing.CreatedAt, &expiresAt,
			&cityID,
			&listing.ItemName, &listing.ItemCategory,
			&listing.ItemQuality, &listing.ItemLevel, &cityNameStr,
		)
		if err != nil {
			continue
		}

		// Set optional fields
		if cityID != nil {
			listing.CityID = api.NewOptUUID(*cityID)
		}
		if cityNameStr != nil {
			listing.CityName = api.NewOptString(*cityNameStr)
		}
		if expiresAt != nil {
			listing.ExpiresAt = api.NewOptDateTime(*expiresAt)
		}
		listing.Commission = api.NewOptFloat32(listing.Price * 0.01)

		listings = append(listings, listing)
	}

	return &api.GetMyMarketListingsOK{
		Listings: listings,
		Total:    api.NewOptInt(total),
	}, nil
}

// UpdateMarketListing implements api.Handler
func (h *MarketHandlersOgen) UpdateMarketListing(ctx context.Context, req *api.UpdateListingRequest, params api.UpdateMarketListingParams) (api.UpdateMarketListingRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Get character ID from auth context
	characterID := uuid.New() // TODO: get from auth context

	// Check ownership
	isOwner, err := h.repository.CheckListingOwnership(ctx, params.ListingID, characterID)
	if err != nil {
		return &api.UpdateMarketListingInternalServerError{
			Error:   "DATABASE_ERROR",
			Message: "Failed to verify ownership",
		}, nil
	}
	if !isOwner {
		return &api.UpdateMarketListingForbidden{
			Error:   "FORBIDDEN",
			Message: "You can only update your own listings",
		}, nil
	}

	// Validate price if provided
	var price *float64
	if req.Price.IsSet() {
		if float64(req.Price.Value) <= 0 || float64(req.Price.Value) > 999999999 {
			return &api.UpdateMarketListingBadRequest{
				Error:   "INVALID_PRICE",
				Message: "Price must be between 0.01 and 999,999,999",
			}, nil
		}
		priceVal := float64(req.Price.Value)
		price = &priceVal
	}

	// Update listing
	err = h.repository.UpdateListing(ctx, params.ListingID, price, nil) // Description update not implemented yet
	if err != nil {
		return &api.UpdateMarketListingInternalServerError{
			Error:   "DATABASE_ERROR",
			Message: "Failed to update listing",
		}, nil
	}

	// Return updated listing
	return &api.MarketListing{
		ID:        params.ListingID,
		SellerID:  characterID,
		Status:    api.MarketListingStatusActive,
		CreatedAt: time.Now(), // This should be fetched from DB
	}, nil
}

// CancelMarketListing implements api.Handler
func (h *MarketHandlersOgen) CancelMarketListing(ctx context.Context, params api.CancelMarketListingParams) (api.CancelMarketListingRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Get character ID from auth context
	characterID := uuid.New() // TODO: get from auth context

	// Check ownership
	isOwner, err := h.repository.CheckListingOwnership(ctx, params.ListingID, characterID)
	if err != nil {
		return &api.CancelMarketListingInternalServerError{
			Error:   "DATABASE_ERROR",
			Message: "Failed to verify ownership",
		}, nil
	}
	if !isOwner {
		return &api.CancelMarketListingForbidden{
			Error:   "FORBIDDEN",
			Message: "You can only cancel your own listings",
		}, nil
	}

	// Cancel listing
	err = h.repository.CancelListing(ctx, params.ListingID)
	if err != nil {
		return &api.CancelMarketListingInternalServerError{
			Error:   "DATABASE_ERROR",
			Message: "Failed to cancel listing",
		}, nil
	}

	return &api.StatusResponse{
		Status: api.NewOptString("cancelled"),
	}, nil
}

// PurchaseMarketItem implements api.Handler
func (h *MarketHandlersOgen) PurchaseMarketItem(ctx context.Context, req *api.PurchaseRequest) (api.PurchaseMarketItemRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Get character ID from auth context
	buyerID := uuid.New() // TODO: get from auth context

	// Check if listing exists and is active
	row, err := h.repository.GetListingByID(ctx, req.ListingID)
	if err != nil {
		if errors.Is(err, pgx.ErrNoRows) {
			return &api.PurchaseMarketItemNotFound{
				Error:   "LISTING_NOT_FOUND",
				Message: "Listing not found or inactive",
			}, nil
		}
		return &api.PurchaseMarketItemInternalServerError{
			Error:   "DATABASE_ERROR",
			Message: "Failed to check listing",
		}, nil
	}

	// Parse listing data to get price and seller
	var listingID, sellerID, itemID uuid.UUID
	var price float64
	var status string

	err = row.Scan(&listingID, &sellerID, &itemID, &price, &status)
	if err != nil {
		return &api.PurchaseMarketItemInternalServerError{
			Error:   "DATABASE_ERROR",
			Message: "Failed to parse listing data",
		}, nil
	}

	// Check if buyer is not the seller
	if buyerID == sellerID {
		return &api.PurchaseMarketItemBadRequest{
			Error:   "CANNOT_BUY_OWN_ITEM",
			Message: "You cannot buy your own items",
		}, nil
	}

	// Check if buyer has enough money (simplified - assume always has)
	// In real implementation, check wallet balance

	// Execute purchase
	purchaseID, err := h.repository.PurchaseListing(ctx, req.ListingID, buyerID)
	if err != nil {
		return &api.PurchaseMarketItemInternalServerError{
			Error:   "DATABASE_ERROR",
			Message: "Failed to complete purchase",
		}, nil
	}

	return &api.PurchaseResult{
		PurchaseID:  purchaseID,
		Success:     true,
		ItemID:      api.NewOptUUID(itemID),
		PricePaid:   api.NewOptFloat32(float32(price)),
		PurchasedAt: api.NewOptDateTime(time.Now()),
	}, nil
}

// GetPurchaseHistory implements api.Handler
func (h *MarketHandlersOgen) GetPurchaseHistory(ctx context.Context, params api.GetPurchaseHistoryParams) (api.GetPurchaseHistoryRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Get character ID from auth context
	buyerID := uuid.New() // TODO: get from auth context

	// Set pagination defaults
	limit := 50
	offset := 0
	if params.Limit.IsSet() && params.Limit.Value > 0 && params.Limit.Value <= 100 {
		limit = int(params.Limit.Value)
	}
	if params.Offset.IsSet() && params.Offset.Value >= 0 {
		offset = int(params.Offset.Value)
	}

	// Get purchase history
	rows, err := h.repository.GetPurchaseHistory(ctx, buyerID, limit, offset)
	if err != nil {
		return &api.GetPurchaseHistoryInternalServerError{
			Error:   "DATABASE_ERROR",
			Message: "Failed to get purchase history",
		}, nil
	}
	defer rows.Close()

	var purchases []api.PurchaseHistory
	for rows.Next() {
		var purchase api.PurchaseHistory
		err := rows.Scan(
			&purchase.PurchaseID, &purchase.ListingID, &purchase.ItemID,
			&purchase.PricePaid, &purchase.PurchasedAt,
			&purchase.SellerID, &purchase.SellerName, &purchase.ItemName,
		)
		if err != nil {
			continue
		}
		purchases = append(purchases, purchase)
	}

	return &api.GetPurchaseHistoryOK{
		Purchases: purchases,
		Total:     api.NewOptInt(len(purchases)), // Simplified - should count all records
	}, nil
}

// GetSalesHistory implements api.Handler
func (h *MarketHandlersOgen) GetSalesHistory(ctx context.Context, params api.GetSalesHistoryParams) (api.GetSalesHistoryRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Get character ID from auth context
	sellerID := uuid.New() // TODO: get from auth context

	// Set pagination defaults
	limit := 50
	offset := 0
	if params.Limit.IsSet() && params.Limit.Value > 0 && params.Limit.Value <= 100 {
		limit = int(params.Limit.Value)
	}
	if params.Offset.IsSet() && params.Offset.Value >= 0 {
		offset = int(params.Offset.Value)
	}

	// Get sales history
	rows, err := h.repository.GetSalesHistory(ctx, sellerID, limit, offset)
	if err != nil {
		return &api.GetSalesHistoryInternalServerError{
			Error:   "DATABASE_ERROR",
			Message: "Failed to get sales history",
		}, nil
	}
	defer rows.Close()

	var sales []api.SaleHistory
	for rows.Next() {
		var sale api.SaleHistory
		err := rows.Scan(
			&sale.SaleID, &sale.ListingID, &sale.ItemID,
			&sale.PriceReceived, &sale.SoldAt, &sale.Commission,
			&sale.BuyerID, &sale.BuyerName, &sale.ItemName,
		)
		if err != nil {
			continue
		}
		sales = append(sales, sale)
	}

	return &api.GetSalesHistoryOK{
		Sales: sales,
		Total: api.NewOptInt(len(sales)), // Simplified - should count all records
	}, nil
}

// GetSellerProfile implements api.Handler
func (h *MarketHandlersOgen) GetSellerProfile(ctx context.Context, params api.GetSellerProfileParams) (api.GetSellerProfileRes, error) {
	ctx, cancel := context.WithTimeout(ctx, CacheTimeout)
	defer cancel()

	row, err := h.repository.GetSellerProfile(ctx, params.SellerID)
	if err != nil {
		if errors.Is(err, pgx.ErrNoRows) {
			return &api.GetSellerProfileNotFound{
				Error:   "SELLER_NOT_FOUND",
				Message: "Seller profile not found",
			}, nil
		}
		return &api.GetSellerProfileInternalServerError{
			Error:   "DATABASE_ERROR",
			Message: "Failed to get seller profile",
		}, nil
	}

	var profile api.SellerProfile
	var joinedAt *time.Time

	err = row.Scan(
		&profile.SellerID, &profile.SellerName, &joinedAt,
		&profile.TotalSales, &profile.PositiveReviews,
		&profile.NegativeReviews, &profile.Rating, &profile.TotalRevenue,
	)
	if err != nil {
		return &api.GetSellerProfileInternalServerError{
			Error:   "DATABASE_ERROR",
			Message: "Failed to parse seller profile",
		}, nil
	}

	if joinedAt != nil {
		profile.JoinedAt = api.NewOptDateTime(*joinedAt)
	}

	return &profile, nil
}

// CreateSellerReview implements api.Handler
func (h *MarketHandlersOgen) CreateSellerReview(ctx context.Context, req *api.CreateReviewRequest) (api.CreateSellerReviewRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Get buyer ID from auth context
	buyerID := uuid.New() // TODO: get from auth context

	// Validate rating
	if req.Rating < 1 || req.Rating > 5 {
		return &api.CreateSellerReviewBadRequest{
			Error:   "INVALID_RATING",
			Message: "Rating must be between 1 and 5",
		}, nil
	}

	// Check if buyer actually purchased from this seller
	var count int
	err := h.db.QueryRow(ctx, `
		SELECT COUNT(*) FROM economy.market_trade_history
		WHERE id = $1 AND buyer_id = $2 AND seller_id = $3`,
		req.PurchaseID, buyerID, req.SellerID).Scan(&count)

	if err != nil {
		return &api.CreateSellerReviewInternalServerError{
			Error:   "DATABASE_ERROR",
			Message: "Failed to verify purchase",
		}, nil
	}

	if count == 0 {
		return &api.CreateSellerReviewBadRequest{
			Error:   "INVALID_PURCHASE",
			Message: "You can only review items you actually purchased",
		}, nil
	}

	// Check if review already exists
	err = h.db.QueryRow(ctx, `
		SELECT COUNT(*) FROM economy.seller_reviews
		WHERE trade_id = $1 AND buyer_id = $2`,
		req.PurchaseID, buyerID).Scan(&count)

	if err != nil {
		return &api.CreateSellerReviewInternalServerError{
			Error:   "DATABASE_ERROR",
			Message: "Failed to check existing review",
		}, nil
	}

	if count > 0 {
		return &api.CreateSellerReviewBadRequest{
			Error:   "REVIEW_ALREADY_EXISTS",
			Message: "You have already reviewed this purchase",
		}, nil
	}

	// Create review
	reviewID, err := h.repository.CreateSellerReview(ctx, req.PurchaseID, buyerID, req.SellerID, req.Rating, &req.Comment.Value)
	if err != nil {
		return &api.CreateSellerReviewInternalServerError{
			Error:   "DATABASE_ERROR",
			Message: "Failed to create review",
		}, nil
	}

	return &api.SellerReview{
		ID:         reviewID,
		PurchaseID: req.PurchaseID,
		SellerID:   req.SellerID,
		BuyerID:    buyerID,
		CreatedAt:  time.Now(),
		Rating:     req.Rating,
		Comment:    req.Comment,
	}, nil
}
