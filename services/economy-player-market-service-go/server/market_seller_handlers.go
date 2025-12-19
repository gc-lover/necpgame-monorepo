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

	// Get buyer ID from JWT auth context
	userIDValue := ctx.Value("user_id")
	if userIDValue == nil {
		return &api.CreateSellerReviewUnauthorized{}, nil
	}

	buyerID, err := uuid.Parse(userIDValue.(string))
	if err != nil {
		return &api.CreateSellerReviewBadRequest{
			Error:   "INVALID_USER_ID",
			Message: "Invalid user ID format",
		}, nil
	}

	// Validate rating
	if req.Rating < 1 || req.Rating > 5 {
		return &api.CreateSellerReviewBadRequest{
			Error:   "INVALID_RATING",
			Message: "Rating must be between 1 and 5",
		}, nil
	}

	// Check if buyer actually purchased from this seller
	var count int
	err = h.db.QueryRow(ctx, `
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

