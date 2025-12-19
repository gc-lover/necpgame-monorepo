// SQL queries use prepared statements with placeholders (, , ?) for safety
// Issue: #42 - economy-player-market ogen typed handlers with business logic
package server

import (
	"context"
	"errors"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/economy-player-market-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
)

// PurchaseMarketItem implements api.Handler
func (h *MarketHandlersOgen) PurchaseMarketItem(ctx context.Context, req *api.PurchaseRequest) (api.PurchaseMarketItemRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Get character ID from JWT auth context
	userIDValue := ctx.Value("user_id")
	if userIDValue == nil {
		return &api.PurchaseMarketItemUnauthorized{}, nil
	}

	buyerID, err := uuid.Parse(userIDValue.(string))
	if err != nil {
		return &api.PurchaseMarketItemBadRequest{
			Error:   "INVALID_USER_ID",
			Message: "Invalid user ID format",
		}, nil
	}

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

	// Get character ID from JWT auth context
	userIDValue := ctx.Value("user_id")
	if userIDValue == nil {
		return &api.GetPurchaseHistoryUnauthorized{}, nil
	}

	buyerID, err := uuid.Parse(userIDValue.(string))
	if err != nil {
		return &api.GetPurchaseHistoryUnauthorized{}, nil
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

	// Get character ID from JWT auth context
	userIDValue := ctx.Value("user_id")
	if userIDValue == nil {
		return &api.GetSalesHistoryUnauthorized{}, nil
	}

	sellerID, err := uuid.Parse(userIDValue.(string))
	if err != nil {
		return &api.GetSalesHistoryUnauthorized{}, nil
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


