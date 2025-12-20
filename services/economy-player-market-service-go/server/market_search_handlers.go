// Package server Issue: #42 - economy-player-market ogen typed handlers with business logic
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/economy-player-market-service-go/pkg/api"
	"github.com/google/uuid"
)

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
		filters["min_level"] = params.MinLevel.Value
	}
	if params.MaxLevel.IsSet() {
		filters["max_level"] = params.MaxLevel.Value
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
		limit = params.Limit.Value
	}
	if params.Offset.IsSet() && params.Offset.Value >= 0 {
		offset = params.Offset.Value
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
