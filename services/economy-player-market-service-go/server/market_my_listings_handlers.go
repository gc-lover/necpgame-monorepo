// Package server Issue: #42 - economy-player-market ogen typed handlers with business logic
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/economy-player-market-service-go/pkg/api"
	"github.com/google/uuid"
)

// GetMyMarketListings implements api.Handler
func (h *MarketHandlersOgen) GetMyMarketListings(ctx context.Context, params api.GetMyMarketListingsParams) (api.GetMyMarketListingsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Get character ID from JWT auth context
	userIDValue := ctx.Value("user_id")
	if userIDValue == nil {
		return &api.GetMyMarketListingsUnauthorized{}, nil
	}

	characterID, err := uuid.Parse(userIDValue.(string))
	if err != nil {
		return &api.GetMyMarketListingsUnauthorized{}, nil
	}

	var statusFilter *string
	if params.Status.IsSet() {
		statusStr := string(params.Status.Value)
		statusFilter = &statusStr
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

	// Get character ID from JWT auth context
	userIDValue := ctx.Value("user_id")
	if userIDValue == nil {
		return &api.UpdateMarketListingUnauthorized{}, nil
	}

	characterID, err := uuid.Parse(userIDValue.(string))
	if err != nil {
		return &api.UpdateMarketListingBadRequest{
			Error:   "INVALID_USER_ID",
			Message: "Invalid user ID format",
		}, nil
	}

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

	// Get character ID from JWT auth context
	userIDValue := ctx.Value("user_id")
	if userIDValue == nil {
		return &api.CancelMarketListingUnauthorized{}, nil
	}

	characterID, err := uuid.Parse(userIDValue.(string))
	if err != nil {
		return &api.CancelMarketListingUnauthorized{}, nil
	}

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
