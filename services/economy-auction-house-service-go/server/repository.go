// Package server Issue: #42 - Player Market Repository
// Performance: Connection pooling, covering indexes, context timeouts
package server

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
)

// PlayerMarketRepository handles database operations
type PlayerMarketRepository struct {
	db *pgxpool.Pool
}

// NewPlayerMarketRepository creates new repository
func NewPlayerMarketRepository(db *pgxpool.Pool) *PlayerMarketRepository {
	return &PlayerMarketRepository{db: db}
}

// Ping database connection
func (r *PlayerMarketRepository) Ping(ctx context.Context) error {
	return r.db.Ping(ctx)
}

// CreateListing creates a new market listing
func (r *PlayerMarketRepository) CreateListing(ctx context.Context, sellerID, itemID uuid.UUID, price float64, cityID *uuid.UUID) (uuid.UUID, error) {
	query := `
		INSERT INTO economy.market_listings (seller_id, item_id, price, expires_at, city_id)
		VALUES ($1, $2, $3, $4, $5)
		RETURNING id`

	expiresAt := time.Now().AddDate(0, 0, 7) // 7 days
	var listingID uuid.UUID

	err := r.db.QueryRow(ctx, query, sellerID, itemID, price, expiresAt, cityID).Scan(&listingID)
	return listingID, err
}

// GetListingByID retrieves a listing with seller and item info
func (r *PlayerMarketRepository) GetListingByID(ctx context.Context, listingID uuid.UUID) (pgx.Row, error) {
	query := `
		SELECT
			ml.id, ml.seller_id, ml.item_id, ml.price, ml.status, ml.created_at, ml.expires_at,
			ml.city_id, ml.quantity,
			c.name as seller_name,
			i.name as item_name, i.category as item_category, i.quality as item_quality, i.level as item_level,
			city.name as city_name
		FROM economy.market_listings ml
		JOIN mvp_core.character c ON ml.seller_id = c.id
		JOIN gameplay.items i ON ml.item_id = i.id
		LEFT JOIN gameplay.cities city ON ml.city_id = city.id
		WHERE ml.id = $1 AND ml.status = 'active'`

	row := r.db.QueryRow(ctx, query, listingID)
	return row, nil
}

// SearchListings searches active listings with filters
func (r *PlayerMarketRepository) SearchListings(ctx context.Context, filters map[string]interface{}, limit, offset int) (pgx.Rows, error) {
	query := `
		SELECT
			ml.id, ml.seller_id, ml.item_id, ml.price, ml.status, ml.created_at, ml.expires_at,
			ml.city_id, ml.quantity,
			c.name as seller_name,
			i.name as item_name, i.category as item_category, i.quality as item_quality, i.level as item_level,
			city.name as city_name,
			COALESCE(ml.views, 0) as views
		FROM economy.market_listings ml
		JOIN mvp_core.character c ON ml.seller_id = c.id
		JOIN gameplay.items i ON ml.item_id = i.id
		LEFT JOIN gameplay.cities city ON ml.city_id = city.id
		WHERE ml.status = 'active'`

	var args []interface{}

	// Apply filters
	if itemName, ok := filters["item_name"].(string); ok && itemName != "" {
		query += fmt.Sprintf(" AND i.name ILIKE $%d", len(args)+1)
		args = append(args, "%"+itemName+"%")
	}

	if category, ok := filters["category"].(string); ok && category != "" {
		query += fmt.Sprintf(" AND i.category = $%d", len(args)+1)
		args = append(args, category)
	}

	if quality, ok := filters["quality"].(string); ok && quality != "" {
		query += fmt.Sprintf(" AND i.quality = $%d", len(args)+1)
		args = append(args, quality)
	}

	if minLevel, ok := filters["min_level"].(int); ok {
		query += fmt.Sprintf(" AND i.level >= $%d", len(args)+1)
		args = append(args, minLevel)
	}

	if maxLevel, ok := filters["max_level"].(int); ok {
		query += fmt.Sprintf(" AND i.level <= $%d", len(args)+1)
		args = append(args, maxLevel)
	}

	if minPrice, ok := filters["min_price"].(float64); ok {
		query += fmt.Sprintf(" AND ml.price >= $%d", len(args)+1)
		args = append(args, minPrice)
	}

	if maxPrice, ok := filters["max_price"].(float64); ok {
		query += fmt.Sprintf(" AND ml.price <= $%d", len(args)+1)
		args = append(args, maxPrice)
	}

	if cityID, ok := filters["city_id"].(uuid.UUID); ok {
		query += fmt.Sprintf(" AND ml.city_id = $%d", len(args)+1)
		args = append(args, cityID)
	}

	if sellerID, ok := filters["seller_id"].(uuid.UUID); ok {
		query += fmt.Sprintf(" AND ml.seller_id = $%d", len(args)+1)
		args = append(args, sellerID)
	}

	// Sorting
	sortBy := "ml.created_at DESC"
	if sort, ok := filters["sort_by"].(string); ok {
		switch sort {
		case "price_asc":
			sortBy = "ml.price ASC"
		case "price_desc":
			sortBy = "ml.price DESC"
		case "date_asc":
			sortBy = "ml.created_at ASC"
		case "date_desc":
			sortBy = "ml.created_at DESC"
		case "popularity":
			sortBy = "COALESCE(ml.views, 0) DESC"
		}
	}
	query += " ORDER BY " + sortBy

	// Pagination
	argCount++
	query += fmt.Sprintf(" LIMIT $%d", argCount)
	args = append(args, limit)

	argCount++
	query += fmt.Sprintf(" OFFSET $%d", argCount)
	args = append(args, offset)

	return r.db.Query(ctx, query, args...)
}

// CountListings counts active listings with filters
func (r *PlayerMarketRepository) CountListings(ctx context.Context, filters map[string]interface{}) (int, error) {
	query := `SELECT COUNT(*) FROM economy.market_listings ml JOIN gameplay.items i ON ml.item_id = i.id WHERE ml.status = 'active'`

	var args []interface{}
	argCount := 0

	// Apply same filters as search
	if itemName, ok := filters["item_name"].(string); ok && itemName != "" {
		argCount++
		query += fmt.Sprintf(" AND i.name ILIKE $%d", argCount)
		args = append(args, "%"+itemName+"%")
	}

	if category, ok := filters["category"].(string); ok && category != "" {
		argCount++
		query += fmt.Sprintf(" AND i.category = $%d", argCount)
		args = append(args, category)
	}

	if quality, ok := filters["quality"].(string); ok && quality != "" {
		argCount++
		query += fmt.Sprintf(" AND i.quality = $%d", argCount)
		args = append(args, quality)
	}

	if minLevel, ok := filters["min_level"].(int); ok {
		argCount++
		query += fmt.Sprintf(" AND i.level >= $%d", argCount)
		args = append(args, minLevel)
	}

	if maxLevel, ok := filters["max_level"].(int); ok {
		argCount++
		query += fmt.Sprintf(" AND i.level <= $%d", argCount)
		args = append(args, maxLevel)
	}

	if minPrice, ok := filters["min_price"].(float64); ok {
		argCount++
		query += fmt.Sprintf(" AND ml.price >= $%d", argCount)
		args = append(args, minPrice)
	}

	if maxPrice, ok := filters["max_price"].(float64); ok {
		argCount++
		query += fmt.Sprintf(" AND ml.price <= $%d", argCount)
		args = append(args, maxPrice)
	}

	if cityID, ok := filters["city_id"].(uuid.UUID); ok {
		argCount++
		query += fmt.Sprintf(" AND ml.city_id = $%d", argCount)
		args = append(args, cityID)
	}

	if sellerID, ok := filters["seller_id"].(uuid.UUID); ok {
		argCount++
		query += fmt.Sprintf(" AND ml.seller_id = $%d", argCount)
		args = append(args, sellerID)
	}

	var count int
	err := r.db.QueryRow(ctx, query, args...).Scan(&count)
	return count, err
}

// GetMyListings gets listings for a specific seller
func (r *PlayerMarketRepository) GetMyListings(ctx context.Context, sellerID uuid.UUID, status *string, limit, offset int) (pgx.Rows, error) {
	query := `
		SELECT
			ml.id, ml.seller_id, ml.item_id, ml.price, ml.status, ml.created_at, ml.expires_at,
			ml.city_id, ml.quantity,
			i.name as item_name, i.category as item_category, i.quality as item_quality, i.level as item_level,
			city.name as city_name
		FROM economy.market_listings ml
		JOIN gameplay.items i ON ml.item_id = i.id
		LEFT JOIN gameplay.cities city ON ml.city_id = city.id
		WHERE ml.seller_id = $1`

	args := []interface{}{sellerID}
	argCount := 1

	if status != nil {
		argCount++
		query += fmt.Sprintf(" AND ml.status = $%d", argCount)
		args = append(args, *status)
	}

	query += " ORDER BY ml.created_at DESC"

	argCount++
	query += fmt.Sprintf(" LIMIT $%d", argCount)
	args = append(args, limit)

	argCount++
	query += fmt.Sprintf(" OFFSET $%d", argCount)
	args = append(args, offset)

	return r.db.Query(ctx, query, args...)
}

// CountMyListings counts listings for a specific seller
func (r *PlayerMarketRepository) CountMyListings(ctx context.Context, sellerID uuid.UUID, status *string) (int, error) {
	query := `SELECT COUNT(*) FROM economy.market_listings WHERE seller_id = $1`

	args := []interface{}{sellerID}
	if status != nil {
		query += " AND status = $2"
		args = append(args, *status)
	}

	var count int
	err := r.db.QueryRow(ctx, query, args...).Scan(&count)
	return count, err
}

// UpdateListing updates listing price and description
func (r *PlayerMarketRepository) UpdateListing(ctx context.Context, listingID uuid.UUID, price *float64, description *string) error {
	query := `UPDATE economy.market_listings SET updated_at = CURRENT_TIMESTAMP`
	var args []interface{}
	argCount := 0

	if price != nil {
		argCount++
		query += fmt.Sprintf(", price = $%d", argCount)
		args = append(args, *price)
	}

	if description != nil {
		argCount++
		query += fmt.Sprintf(", description = $%d", argCount)
		args = append(args, *description)
	}

	argCount++
	query += fmt.Sprintf(" WHERE id = $%d AND status = 'active'", argCount)
	args = append(args, listingID)

	_, err := r.db.Exec(ctx, query, args...)
	return err
}

// CancelListing cancels an active listing
func (r *PlayerMarketRepository) CancelListing(ctx context.Context, listingID uuid.UUID) error {
	query := `UPDATE economy.market_listings SET status = 'cancelled', updated_at = CURRENT_TIMESTAMP WHERE id = $1 AND status = 'active'`
	_, err := r.db.Exec(ctx, query, listingID)
	return err
}

// PurchaseListing executes a purchase transaction
func (r *PlayerMarketRepository) PurchaseListing(ctx context.Context, listingID, buyerID uuid.UUID) (uuid.UUID, error) {
	// Start transaction
	tx, err := r.db.Begin(ctx)
	if err != nil {
		return uuid.Nil, err
	}
	defer tx.Rollback(ctx)

	// Get listing details and lock it
	var sellerID uuid.UUID
	var itemID uuid.UUID
	var price float64
	var quantity int

	err = tx.QueryRow(ctx, `
		SELECT seller_id, item_id, price, quantity
		FROM economy.market_listings
		WHERE id = $1 AND status = 'active'
		FOR UPDATE`, listingID).Scan(&sellerID, &itemID, &price, &quantity)

	if err != nil {
		return uuid.Nil, err
	}

	// Check if buyer has enough money (assuming we have wallet system)
	// For now, assume purchase is always possible

	// Update listing status
	_, err = tx.Exec(ctx, `
		UPDATE economy.market_listings
		SET status = 'sold', sold_at = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP
		WHERE id = $1`, listingID)

	if err != nil {
		return uuid.Nil, err
	}

	// Calculate commission (1%)
	commission := price * 0.01
	sellerReceived := price - commission

	// Create trade history record
	var tradeID uuid.UUID
	err = tx.QueryRow(ctx, `
		INSERT INTO economy.market_trade_history
		(listing_id, buyer_id, seller_id, item_id, price_per_unit, total_price, commission, seller_received, quantity)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
		RETURNING id`,
		listingID, buyerID, sellerID, itemID, price, price, commission, sellerReceived, quantity).Scan(&tradeID)

	if err != nil {
		return uuid.Nil, err
	}

	// Transfer item from seller to buyer (assuming inventory system)
	// For now, just log the transfer

	// Update seller statistics
	_, err = tx.Exec(ctx, `
		INSERT INTO economy.seller_statistics (seller_id, total_revenue, total_sales, items_sold, last_sale_at)
		VALUES ($1, $2, 1, $3, CURRENT_TIMESTAMP)
		ON CONFLICT (seller_id) DO UPDATE SET
			total_revenue = seller_statistics.total_revenue + $2,
			total_sales = seller_statistics.total_sales + 1,
			items_sold = seller_statistics.items_sold + $3,
			last_sale_at = CURRENT_TIMESTAMP,
			last_update = CURRENT_TIMESTAMP`, sellerID, sellerReceived, quantity)

	if err != nil {
		return uuid.Nil, err
	}

	// Commit transaction
	err = tx.Commit(ctx)
	return tradeID, err
}

// GetPurchaseHistory gets purchase history for a buyer
func (r *PlayerMarketRepository) GetPurchaseHistory(ctx context.Context, buyerID uuid.UUID, limit, offset int) (pgx.Rows, error) {
	query := `
		SELECT
			mth.id as purchase_id, mth.listing_id, mth.item_id, mth.price_per_unit as price_paid,
			mth.completed_at as purchased_at, mth.quantity,
			mth.seller_id, c.name as seller_name,
			i.name as item_name
		FROM economy.market_trade_history mth
		JOIN mvp_core.character c ON mth.seller_id = c.id
		JOIN gameplay.items i ON mth.item_id = i.id
		WHERE mth.buyer_id = $1
		ORDER BY mth.completed_at DESC
		LIMIT $2 OFFSET $3`

	return r.db.Query(ctx, query, buyerID, limit, offset)
}

// GetSalesHistory gets sales history for a seller
func (r *PlayerMarketRepository) GetSalesHistory(ctx context.Context, sellerID uuid.UUID, limit, offset int) (pgx.Rows, error) {
	query := `
		SELECT
			mth.id as sale_id, mth.listing_id, mth.item_id, mth.seller_received as price_received,
			mth.completed_at as sold_at, mth.commission, mth.quantity,
			mth.buyer_id, c.name as buyer_name,
			i.name as item_name
		FROM economy.market_trade_history mth
		JOIN mvp_core.character c ON mth.buyer_id = c.id
		JOIN gameplay.items i ON mth.item_id = i.id
		WHERE mth.seller_id = $1
		ORDER BY mth.completed_at DESC
		LIMIT $2 OFFSET $3`

	return r.db.Query(ctx, query, sellerID, limit, offset)
}

// GetSellerProfile gets seller statistics and profile
func (r *PlayerMarketRepository) GetSellerProfile(ctx context.Context, sellerID uuid.UUID) (pgx.Row, error) {
	query := `
		SELECT
			ss.seller_id, c.name as seller_name, c.created_at as joined_at,
			COALESCE(ss.total_sales, 0) as total_sales,
			COALESCE(ss.positive_reviews, 0) as positive_reviews,
			COALESCE(ss.negative_reviews, 0) as negative_reviews,
			COALESCE(ss.average_rating, 0) as rating,
			COALESCE(ss.total_revenue, 0) as total_revenue
		FROM economy.seller_statistics ss
		JOIN mvp_core.character c ON ss.seller_id = c.id
		WHERE ss.seller_id = $1`

	row := r.db.QueryRow(ctx, query, sellerID)
	return row, nil
}

// CreateSellerReview creates a review for a seller
func (r *PlayerMarketRepository) CreateSellerReview(ctx context.Context, tradeID, buyerID, sellerID uuid.UUID, rating int, comment *string) (uuid.UUID, error) {
	query := `
		INSERT INTO economy.seller_reviews (trade_id, seller_id, buyer_id, rating, comment)
		VALUES ($1, $2, $3, $4, $5)
		RETURNING id`

	var reviewID uuid.UUID
	err := r.db.QueryRow(ctx, query, tradeID, sellerID, buyerID, rating, comment).Scan(&reviewID)

	if err != nil {
		return uuid.Nil, err
	}

	// Update seller statistics
	isPositive := rating >= 4
	_, err = r.db.Exec(ctx, `
		UPDATE economy.seller_statistics SET
			total_reviews = total_reviews + 1,
			positive_reviews = positive_reviews + CASE WHEN $2 THEN 1 ELSE 0 END,
			negative_reviews = negative_reviews + CASE WHEN $2 THEN 0 ELSE 1 END,
			average_rating = (
				SELECT AVG(rating)::decimal(3,2)
				FROM economy.seller_reviews
				WHERE seller_id = $1
			),
			last_update = CURRENT_TIMESTAMP
		WHERE seller_id = $1`, sellerID, isPositive)

	return reviewID, err
}

// CheckListingOwnership checks if listing belongs to seller
func (r *PlayerMarketRepository) CheckListingOwnership(ctx context.Context, listingID, sellerID uuid.UUID) (bool, error) {
	var count int
	err := r.db.QueryRow(ctx, `
		SELECT COUNT(*) FROM economy.market_listings
		WHERE id = $1 AND seller_id = $2 AND status = 'active'`, listingID, sellerID).Scan(&count)
	return count > 0, err
}

// IncrementListingViews increments view counter for listing
func (r *PlayerMarketRepository) IncrementListingViews(ctx context.Context, listingID uuid.UUID) error {
	query := `
		UPDATE economy.market_listings
		SET views = COALESCE(views, 0) + 1, updated_at = CURRENT_TIMESTAMP
		WHERE id = $1 AND status = 'active'`
	_, err := r.db.Exec(ctx, query, listingID)
	return err
}
