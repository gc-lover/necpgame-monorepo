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

