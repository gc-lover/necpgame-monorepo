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
			Error: "DATABASE_ERROR",
			Message: "Failed to verify ownership",
		}, nil
	}
	if !isOwner {
		return &api.CancelMarketListingForbidden{
			Error: "FORBIDDEN",
			Message: "You can only cancel your own listings",
		}, nil
	}

	// Cancel listing
	err = h.repository.CancelListing(ctx, params.ListingID)
	if err != nil {
		return &api.CancelMarketListingInternalServerError{
			Error: "DATABASE_ERROR",
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
				Error: "LISTING_NOT_FOUND",
				Message: "Listing not found or inactive",
			}, nil
		}
		return &api.PurchaseMarketItemInternalServerError{
			Error: "DATABASE_ERROR",
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
			Error: "DATABASE_ERROR",
			Message: "Failed to parse listing data",
		}, nil
	}

	// Check if buyer is not the seller
	if buyerID == sellerID {
		return &api.PurchaseMarketItemBadRequest{
			Error: "CANNOT_BUY_OWN_ITEM",
			Message: "You cannot buy your own items",
		}, nil
	}

	// Check if buyer has enough money (simplified - assume always has)
	// In real implementation, check wallet balance

	// Execute purchase
	purchaseID, err := h.repository.PurchaseListing(ctx, req.ListingID, buyerID)
	if err != nil {
		return &api.PurchaseMarketItemInternalServerError{
			Error: "DATABASE_ERROR",
			Message: "Failed to complete purchase",
		}, nil
	}

	return &api.PurchaseResult{
		PurchaseID: purchaseID,
		Success:    true,
		ItemID:     api.NewOptUUID(itemID),
		PricePaid:  api.NewOptFloat32(float32(price)),
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
			Error: "DATABASE_ERROR",
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
			Error: "DATABASE_ERROR",
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
				Error: "SELLER_NOT_FOUND",
				Message: "Seller profile not found",
			}, nil
		}
		return &api.GetSellerProfileInternalServerError{
			Error: "DATABASE_ERROR",
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
			Error: "DATABASE_ERROR",
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
			Error: "INVALID_RATING",
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
			Error: "DATABASE_ERROR",
			Message: "Failed to verify purchase",
		}, nil
	}

	if count == 0 {
		return &api.CreateSellerReviewBadRequest{
			Error: "INVALID_PURCHASE",
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
			Error: "DATABASE_ERROR",
			Message: "Failed to check existing review",
		}, nil
	}

	if count > 0 {
		return &api.CreateSellerReviewBadRequest{
			Error: "REVIEW_ALREADY_EXISTS",
			Message: "You have already reviewed this purchase",
		}, nil
	}

	// Create review
	reviewID, err := h.repository.CreateSellerReview(ctx, req.PurchaseID, buyerID, req.SellerID, req.Rating, &req.Comment.Value)
	if err != nil {
		return &api.CreateSellerReviewInternalServerError{
			Error: "DATABASE_ERROR",
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
