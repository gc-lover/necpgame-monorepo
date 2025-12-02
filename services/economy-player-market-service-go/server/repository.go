// Issue: #42
package server

import (
	"context"
	"errors"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/economy-player-market-service-go/pkg/api"
	"github.com/jackc/pgx/v5/pgxpool"
)

type PlayerMarketRepository struct {
	db *pgxpool.Pool
}

func NewPlayerMarketRepository(db *pgxpool.Pool) *PlayerMarketRepository {
	return &PlayerMarketRepository{db: db}
}

// ListListings получает список объявлений из БД
func (r *PlayerMarketRepository) ListListings(ctx context.Context, params api.ListListingsParams) ([]api.MarketListing, error) {
	query := `
		SELECT listing_id, seller_id, item_id, quantity, price, currency,
		       status, created_at, expires_at
		FROM player_market_listings
		WHERE status = 'active'
		ORDER BY created_at DESC
		LIMIT 50
	`

	rows, err := r.db.Query(ctx, query)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var listings []api.MarketListing
	for rows.Next() {
		var listing api.MarketListing
		err := rows.Scan(
			&listing.ListingId,
			&listing.SellerId,
			&listing.ItemId,
			&listing.Quantity,
			&listing.Price,
			&listing.Currency,
			&listing.Status,
			&listing.CreatedAt,
			&listing.ExpiresAt,
		)
		if err != nil {
			return nil, err
		}
		listings = append(listings, listing)
	}

	return listings, nil
}

// CreateListing создает новое объявление
func (r *PlayerMarketRepository) CreateListing(ctx context.Context, req *api.CreateListingRequest) (*api.MarketListing, error) {
	query := `
		INSERT INTO player_market_listings 
		(listing_id, seller_id, item_id, quantity, price, currency, status, created_at, expires_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
		RETURNING listing_id, seller_id, item_id, quantity, price, currency, status, created_at, expires_at
	`

	listing := &api.MarketListing{
		ListingId: generateID(),
		SellerId:  req.SellerId,
		ItemId:    req.ItemId,
		Quantity:  req.Quantity,
		Price:     req.Price,
		Currency:  req.Currency,
		Status:    "active",
		CreatedAt: time.Now(),
		ExpiresAt: time.Now().Add(24 * time.Hour),
	}

	err := r.db.QueryRow(ctx, query,
		listing.ListingId,
		listing.SellerId,
		listing.ItemId,
		listing.Quantity,
		listing.Price,
		listing.Currency,
		listing.Status,
		listing.CreatedAt,
		listing.ExpiresAt,
	).Scan(
		&listing.ListingId,
		&listing.SellerId,
		&listing.ItemId,
		&listing.Quantity,
		&listing.Price,
		&listing.Currency,
		&listing.Status,
		&listing.CreatedAt,
		&listing.ExpiresAt,
	)

	if err != nil {
		return nil, err
	}

	return listing, nil
}

// GetListing получает объявление по ID
func (r *PlayerMarketRepository) GetListing(ctx context.Context, listingID string) (*api.MarketListing, error) {
	query := `
		SELECT listing_id, seller_id, item_id, quantity, price, currency,
		       status, created_at, expires_at
		FROM player_market_listings
		WHERE listing_id = $1
	`

	var listing api.MarketListing
	err := r.db.QueryRow(ctx, query, listingID).Scan(
		&listing.ListingId,
		&listing.SellerId,
		&listing.ItemId,
		&listing.Quantity,
		&listing.Price,
		&listing.Currency,
		&listing.Status,
		&listing.CreatedAt,
		&listing.ExpiresAt,
	)

	if err != nil {
		return nil, err
	}

	return &listing, nil
}

// PurchaseListing покупает товар
func (r *PlayerMarketRepository) PurchaseListing(ctx context.Context, listingID string, req *api.PurchaseListingRequest) (*api.MarketTransaction, error) {
	// TODO: Implement full transaction logic with inventory and economy service
	tx, err := r.db.Begin(ctx)
	if err != nil {
		return nil, err
	}
	defer tx.Rollback(ctx)

	// 1. Update listing status
	updateQuery := `
		UPDATE player_market_listings
		SET status = 'sold'
		WHERE listing_id = $1 AND status = 'active'
		RETURNING seller_id, item_id, quantity, price, currency
	`

	var sellerId, itemId, currency string
	var quantity, price int
	err = tx.QueryRow(ctx, updateQuery, listingID).Scan(&sellerId, &itemId, &quantity, &price, &currency)
	if err != nil {
		return nil, errors.New("listing not available")
	}

	// 2. Create transaction record
	insertQuery := `
		INSERT INTO player_market_transactions
		(transaction_id, listing_id, buyer_id, seller_id, item_id, quantity, price, currency, created_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
		RETURNING transaction_id, listing_id, buyer_id, seller_id, item_id, quantity, price, currency, created_at
	`

	transaction := &api.MarketTransaction{
		TransactionId: generateID(),
		ListingId:     listingID,
		BuyerId:       req.BuyerId,
		SellerId:      sellerId,
		ItemId:        itemId,
		Quantity:      quantity,
		Price:         price,
		Currency:      currency,
		CreatedAt:     time.Now(),
	}

	err = tx.QueryRow(ctx, insertQuery,
		transaction.TransactionId,
		transaction.ListingId,
		transaction.BuyerId,
		transaction.SellerId,
		transaction.ItemId,
		transaction.Quantity,
		transaction.Price,
		transaction.Currency,
		transaction.CreatedAt,
	).Scan(
		&transaction.TransactionId,
		&transaction.ListingId,
		&transaction.BuyerId,
		&transaction.SellerId,
		&transaction.ItemId,
		&transaction.Quantity,
		&transaction.Price,
		&transaction.Currency,
		&transaction.CreatedAt,
	)

	if err != nil {
		return nil, err
	}

	if err := tx.Commit(ctx); err != nil {
		return nil, err
	}

	return transaction, nil
}

func generateID() string {
	return time.Now().Format("20060102150405")
}

