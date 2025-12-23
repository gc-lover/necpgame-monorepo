// Issue: #2236
package server

import (
	"context"
	"database/sql"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/trading-core-service-go/pkg/api"
)

// TradingRepository handles database operations for trading
type TradingRepository struct {
	db    *sql.DB
	redis *RedisClient
}

// NewTradingRepository creates a new trading repository
func NewTradingRepository(db *sql.DB, redis *RedisClient) *TradingRepository {
	return &TradingRepository{
		db:    db,
		redis: redis,
	}
}

// CreateListing creates a new trade listing in the database
func (r *TradingRepository) CreateListing(ctx context.Context, req *api.CreateListingRequest) (*api.TradeListing, error) {
	// BACKEND NOTE: Prepared statement for security and performance
	query := `
		INSERT INTO gameplay.trade_listings (
			id, seller_id, item_id, item_type, quantity, price_per_unit,
			currency_type, created_at, expires_at, is_active
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)
		RETURNING id, seller_id, item_id, item_type, quantity, price_per_unit,
			currency_type, created_at, expires_at, is_active`

	// Generate UUID for listing
	listingID := generateUUID()
	expiresAt := time.Now().Add(time.Duration(req.DurationHours) * time.Hour)

	// Extract item type (assuming it's passed in request body or derived)
	itemType := "weapon" // Default - should be passed in request

	// Extract currency type from optional field
	currencyType := "eurodollars" // Default
	if req.CurrencyType.IsSet() {
		currencyType = string(req.CurrencyType.Value)
	}

	var listing api.TradeListing
	err := r.db.QueryRowContext(ctx, query,
		listingID,
		getUserIDFromContext(ctx), // Extract from context
		req.ItemID.String(), // Convert UUID to string
		itemType,
		req.Quantity,
		req.PricePerUnit,
		currencyType,
		time.Now().Unix(),
		expiresAt.Unix(),
		true, // is_active
	).Scan(
		&listing.ID,
		&listing.SellerID,
		&listing.ItemID,
		&listing.ItemType,
		&listing.Quantity,
		&listing.PricePerUnit,
		&listing.CurrencyType,
		&listing.CreatedAt,
		&listing.ExpiresAt,
		&listing.IsActive,
	)

	if err != nil {
		return nil, fmt.Errorf("failed to create listing: %w", err)
	}

	return &listing, nil
}

// GetListings retrieves trade listings with filtering and pagination
func (r *TradingRepository) GetListings(ctx context.Context, params api.GetTradeListingsParams) (*api.ListingsResponse, error) {
	// BACKEND NOTE: Optimized query with proper indexing
	baseQuery := `
		SELECT id, seller_id, item_id, item_type, seller_name, currency_type,
			   item_modifiers, price_per_unit, created_at, expires_at,
			   quantity, is_active, is_featured, allow_offers
		FROM gameplay.trade_listings
		WHERE is_active = true
		AND expires_at > $1`

	args := []interface{}{time.Now().Unix()}
	argCount := 1

	// Add filters
	if params.ItemType.IsSet() {
		argCount++
		baseQuery += fmt.Sprintf(" AND item_type = $%d", argCount)
		args = append(args, params.ItemType.Value)
	}

	if params.MinPrice.IsSet() {
		argCount++
		baseQuery += fmt.Sprintf(" AND price_per_unit >= $%d", argCount)
		args = append(args, params.MinPrice.Value)
	}

	if params.MaxPrice.IsSet() {
		argCount++
		baseQuery += fmt.Sprintf(" AND price_per_unit <= $%d", argCount)
		args = append(args, params.MaxPrice.Value)
	}

	// Add ordering and pagination
	baseQuery += " ORDER BY created_at DESC"

	limit := int32(20) // default
	if params.Limit.IsSet() && int32(params.Limit.Value) <= 100 {
		limit = int32(params.Limit.Value)
	}
	argCount++
	baseQuery += fmt.Sprintf(" LIMIT $%d", argCount)
	args = append(args, limit)

	offset := int32(0) // default
	if params.Offset.IsSet() {
		offset = int32(params.Offset.Value)
	}
	argCount++
	baseQuery += fmt.Sprintf(" OFFSET $%d", argCount)
	args = append(args, offset)

	// Execute query
	rows, err := r.db.QueryContext(ctx, baseQuery, args...)
	if err != nil {
		return nil, fmt.Errorf("failed to query listings: %w", err)
	}
	defer rows.Close()

	var listings []api.TradeListing
	for rows.Next() {
		var listing api.TradeListing
		err := rows.Scan(
			&listing.ID,
			&listing.SellerID,
			&listing.ItemID,
			&listing.ItemType,
			&listing.SellerName,
			&listing.CurrencyType,
			&listing.ItemModifiers,
			&listing.PricePerUnit,
			&listing.CreatedAt,
			&listing.ExpiresAt,
			&listing.Quantity,
			&listing.IsActive,
			&listing.IsFeatured,
			&listing.AllowOffers,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan listing: %w", err)
		}
		listings = append(listings, listing)
	}

	// Get total count for pagination
	countQuery := "SELECT COUNT(*) FROM gameplay.trade_listings WHERE is_active = true AND expires_at > $1"
	var totalCount int64
	err = r.db.QueryRowContext(ctx, countQuery, time.Now().Unix()).Scan(&totalCount)
	if err != nil {
		return nil, fmt.Errorf("failed to get total count: %w", err)
	}

	// Calculate pagination info
	hasNext := int64(offset)+int64(limit) < totalCount
	hasPrev := offset > 0
	currentPage := (offset / limit) + 1
	totalPages := int32((totalCount + int64(limit) - 1) / int64(limit))

	// Create OptInt32 for ItemsPerPage
	var itemsPerPage api.OptInt32
	itemsPerPage.SetTo(limit)

	response := &api.ListingsResponse{
		Listings: listings,
		TotalCount: totalCount,
		PageInfo: api.ListingsResponsePageInfo{
			HasNextPage:   hasNext,
			HasPreviousPage: hasPrev,
			CurrentPage:   currentPage,
			TotalPages:    totalPages,
			ItemsPerPage:  itemsPerPage,
		},
	}

	return response, nil
}

// ExecuteTrade executes a trade transaction
func (r *TradingRepository) ExecuteTrade(ctx context.Context, listingID string, req *api.ExecuteTradeRequest) (*api.TradeResult, error) {
	// BACKEND NOTE: Atomic transaction for trade execution (CRITICAL)
	tx, err := r.db.BeginTx(ctx, nil)
	if err != nil {
		return nil, fmt.Errorf("failed to begin transaction: %w", err)
	}
	defer tx.Rollback()

	// Lock the listing for update
	var listing api.TradeListing
	lockQuery := `
		SELECT id, seller_id, item_id, quantity, price_per_unit, currency_type, is_active
		FROM gameplay.trade_listings
		WHERE id = $1 AND is_active = true
		FOR UPDATE`

	err = tx.QueryRowContext(ctx, lockQuery, listingID).Scan(
		&listing.ID, &listing.SellerID, &listing.ItemID,
		&listing.Quantity, &listing.PricePerUnit, &listing.CurrencyType, &listing.IsActive)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("listing not found or inactive")
		}
		return nil, fmt.Errorf("failed to lock listing: %w", err)
	}

	// Validate trade
	if req.Quantity > listing.Quantity {
		return nil, fmt.Errorf("insufficient quantity available")
	}

	totalPrice := int64(req.Quantity) * listing.PricePerUnit

	// Execute trade operations within transaction
	tradeID := generateUUID()

	// 1. Update listing quantity or deactivate
	if req.Quantity == listing.Quantity {
		// Deactivate listing
		_, err = tx.ExecContext(ctx, "UPDATE gameplay.trade_listings SET is_active = false WHERE id = $1", listingID)
	} else {
		// Reduce quantity
		_, err = tx.ExecContext(ctx, "UPDATE gameplay.trade_listings SET quantity = quantity - $1 WHERE id = $2", req.Quantity, listingID)
	}
	if err != nil {
		return nil, fmt.Errorf("failed to update listing: %w", err)
	}

	// 2. Transfer items (simplified - would need inventory system integration)
	// This would typically involve calling inventory service

	// 3. Transfer currency
	// This would typically involve calling economy service

	// 4. Record trade
	tradeQuery := `
		INSERT INTO gameplay.trade_history (
			id, listing_id, seller_id, buyer_id, item_id, quantity,
			total_price, currency_type, executed_at, success
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)`

	_, err = tx.ExecContext(ctx, tradeQuery,
		tradeID, listingID, listing.SellerID, req.BuyerID,
		listing.ItemID, req.Quantity, totalPrice, listing.CurrencyType,
		time.Now().Unix(), true)
	if err != nil {
		return nil, fmt.Errorf("failed to record trade: %w", err)
	}

	// Commit transaction
	if err = tx.Commit(); err != nil {
		return nil, fmt.Errorf("failed to commit transaction: %w", err)
	}

	// Parse UUIDs
	tradeUUID, _ := uuid.Parse(tradeID)
	buyerUUID, _ := uuid.Parse(req.BuyerID.String())
	listingUUID, _ := uuid.Parse(listingID)

	// Create optional UUID for ListingID
	var optListingID api.OptUUID
	optListingID.SetTo(listingUUID)

	// Convert currency type
	var currencyType api.TradeResultCurrencyType
	switch listing.CurrencyType {
	case "eurodollars":
		currencyType = api.TradeResultCurrencyTypeEurodollars
	case "faction_credits":
		currencyType = api.TradeResultCurrencyTypeFactionCredits
	case "premium_tokens":
		currencyType = api.TradeResultCurrencyTypePremiumTokens
	default:
		currencyType = api.TradeResultCurrencyTypeEurodollars
	}

	// Return result
	result := &api.TradeResult{
		TradeID:     tradeUUID,
		SellerID:    listing.SellerID,
		BuyerID:     buyerUUID,
		ItemID:      listing.ItemID,
		ListingID:   optListingID,
		CurrencyType: currencyType,
		TotalPrice:  totalPrice,
		ExecutedAt:  time.Now().Unix(),
		Quantity:    req.Quantity,
		Success:     true,
	}

	return result, nil
}

// GetListingByID retrieves a single listing by ID
func (r *TradingRepository) GetListingByID(ctx context.Context, listingID string) (*api.TradeListing, error) {
	query := `
		SELECT id, seller_id, item_id, item_type, quantity, price_per_unit,
			   currency_type, created_at, expires_at, is_active
		FROM gameplay.trade_listings
		WHERE id = $1`

	var listing api.TradeListing
	err := r.db.QueryRowContext(ctx, query, listingID).Scan(
		&listing.ID, &listing.SellerID, &listing.ItemID, &listing.ItemType,
		&listing.Quantity, &listing.PricePerUnit, &listing.CurrencyType,
		&listing.CreatedAt, &listing.ExpiresAt, &listing.IsActive)

	if err != nil {
		return nil, fmt.Errorf("failed to get listing: %w", err)
	}

	return &listing, nil
}

// CheckItemOwnership verifies if user owns the item
func (r *TradingRepository) CheckItemOwnership(ctx context.Context, itemID, userID string) (bool, error) {
	// BACKEND NOTE: Anti-cheat validation query
	query := "SELECT COUNT(*) FROM inventory.player_items WHERE item_id = $1 AND player_id = $2 AND quantity > 0"
	var count int
	err := r.db.QueryRowContext(ctx, query, itemID, userID).Scan(&count)
	if err != nil {
		return false, fmt.Errorf("failed to check ownership: %w", err)
	}
	return count > 0, nil
}

// Helper functions

func generateUUID() string {
	// Simple UUID generation - in production use proper UUID library
	return fmt.Sprintf("listing-%d", time.Now().UnixNano())
}
