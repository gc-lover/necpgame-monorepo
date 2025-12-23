// Issue: #2236
package server

import (
	"context"
	"fmt"
	"log"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/trading-core-service-go/pkg/api"
)

// TradingCoreHandler implements the generated ServerInterface
type TradingCoreHandler struct {
	service *TradingCoreService
}

// NewTradingCoreHandler creates a new handler instance
func NewTradingCoreHandler(service *TradingCoreService) *TradingCoreHandler {
	return &TradingCoreHandler{
		service: service,
	}
}

// TradingCoreHealthCheck implements health check endpoint
func (h *TradingCoreHandler) TradingCoreHealthCheck(ctx context.Context) (api.TradingCoreHealthCheckRes, error) {
	return h.service.HealthCheck(ctx)
}

// CreateTradeListing implements POST /api/v1/economy-domain/trading/listings
func (h *TradingCoreHandler) CreateTradeListing(ctx context.Context, req *api.CreateListingRequest) (api.CreateTradeListingRes, error) {
	// BACKEND NOTE: Critical path operation with anti-cheat validation
	start := time.Now()
	defer func() {
		h.service.metrics.RecordDuration("create_listing", time.Since(start))
	}()

	// Validate request with context timeout
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	// Anti-cheat validation: check item ownership
	itemIDStr := req.ItemID.String() // Convert UUID to string
	if err := h.validateItemOwnership(ctx, itemIDStr, getUserIDFromContext(ctx)); err != nil {
		h.service.metrics.RecordError("create_listing", "ownership_validation_failed")
		return &api.CreateTradeListingForbidden{
			Code:    "OWNERSHIP_VALIDATION_FAILED",
			Message: "Item ownership validation failed",
		}, nil
	}

	// Price validation
	if err := h.validatePrice(ctx, req); err != nil {
		h.service.metrics.RecordError("create_listing", "price_validation_failed")
		return &api.CreateTradeListingBadRequest{
			Code:    "PRICE_VALIDATION_FAILED",
			Message: "Price validation failed",
		}, nil
	}

	// Create listing in database with transaction
	listing, err := h.service.repo.CreateListing(ctx, req)
	if err != nil {
		h.service.metrics.RecordError("create_listing", "database_error")
		return &api.CreateTradeListingBadRequest{
			Code:    "DATABASE_ERROR",
			Message: "Failed to create listing",
		}, nil
	}

	h.service.metrics.RecordSuccess("create_listing")
	return listing, nil
}

// GetTradeListings implements GET /api/v1/economy-domain/trading/listings
func (h *TradingCoreHandler) GetTradeListings(ctx context.Context, params api.GetTradeListingsParams) (api.GetTradeListingsRes, error) {
	// BACKEND NOTE: Hot path endpoint (1000+ RPS) with Redis caching
	start := time.Now()
	defer func() {
		h.service.metrics.RecordDuration("get_listings", time.Since(start))
	}()

	// Context timeout for hot path
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	// Try cache first
	cacheKey := h.buildListingsCacheKey(params)
	if cached, err := h.service.redis.GetListingsCache(ctx, cacheKey); err == nil && cached != nil {
		h.service.metrics.RecordCacheHit("get_listings")
		return cached, nil
	}

	// Fetch from database
	response, err := h.service.repo.GetListings(ctx, params)
	if err != nil {
		h.service.metrics.RecordError("get_listings", "database_error")
		return &api.Error{
			Code:    "DATABASE_ERROR",
			Message: "Failed to fetch listings",
		}, nil
	}

	// Cache result for 5 minutes
	h.service.redis.SetListingsCache(ctx, cacheKey, response, 5*time.Minute)

	h.service.metrics.RecordSuccess("get_listings")
	return response, nil
}

// ExecuteTrade implements POST /api/v1/economy-domain/trading/listings/{listing_id}/execute
func (h *TradingCoreHandler) ExecuteTrade(ctx context.Context, req *api.ExecuteTradeRequest, params api.ExecuteTradeParams) (api.ExecuteTradeRes, error) {
	// BACKEND NOTE: Critical hot path (P99 <5ms required) with atomic transactions
	start := time.Now()
	defer func() {
		h.service.metrics.RecordDuration("execute_trade", time.Since(start))
	}()

	// Strict timeout for critical path
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	listingID := params.ListingID.String() // Convert UUID to string

	// Anti-cheat validation
	if err := h.validateTradeRequest(ctx, listingID, req); err != nil {
		h.service.metrics.RecordError("execute_trade", "validation_failed")
		return err, nil
	}

	// Execute trade in database transaction
	result, err := h.service.repo.ExecuteTrade(ctx, listingID, req)
	if err != nil {
		h.service.metrics.RecordError("execute_trade", "transaction_failed")
		return &api.ExecuteTradeBadRequest{
			Code:    "TRADE_EXECUTION_FAILED",
			Message: "Trade execution failed",
		}, nil
	}

	// Update cache invalidation
	h.invalidateListingCache(ctx, listingID)

	h.service.metrics.RecordSuccess("execute_trade")
	return result, nil
}

// Helper methods

func (h *TradingCoreHandler) validateItemOwnership(ctx context.Context, itemID, userID string) error {
	// BACKEND NOTE: Anti-cheat validation - verify item ownership
	owned, err := h.service.repo.CheckItemOwnership(ctx, itemID, userID)
	if err != nil {
		return fmt.Errorf("ownership check failed: %w", err)
	}
	if !owned {
		return fmt.Errorf("item not owned by user")
	}
	return nil
}

func (h *TradingCoreHandler) validatePrice(ctx context.Context, req *api.CreateListingRequest) error {
	// BACKEND NOTE: Price validation with floor/ceiling checks
	if req.PricePerUnit < 1 {
		return fmt.Errorf("price too low")
	}
	if req.PricePerUnit > 1000000 { // 1M eurodollars max
		return fmt.Errorf("price too high")
	}
	return nil
}

func (h *TradingCoreHandler) validateTradeRequest(ctx context.Context, listingID string, req *api.ExecuteTradeRequest) api.ExecuteTradeRes {
	// BACKEND NOTE: Comprehensive trade validation
	if req.Quantity < 1 {
		return &api.ExecuteTradeBadRequest{
			Code:    "INVALID_QUANTITY",
			Message: "Quantity must be positive",
		}
	}

	// Check listing exists and is active
	listing, err := h.service.repo.GetListingByID(ctx, listingID)
	if err != nil {
		return &api.ExecuteTradeNotFound{
			Code:    "LISTING_NOT_FOUND",
			Message: "Listing not found",
		}
	}

	if !listing.IsActive {
		return &api.ExecuteTradeConflict{
			Code:    "LISTING_INACTIVE",
			Message: "Listing is no longer active",
		}
	}

	return nil
}

func (h *TradingCoreHandler) buildListingsCacheKey(params api.GetTradeListingsParams) string {
	key := "listings"
	if params.ItemType.IsSet() {
		key += fmt.Sprintf(":type_%s", params.ItemType.Value)
	}
	if params.MinPrice.IsSet() {
		key += fmt.Sprintf(":min_%d", params.MinPrice.Value)
	}
	if params.MaxPrice.IsSet() {
		key += fmt.Sprintf(":max_%d", params.MaxPrice.Value)
	}
	if params.Limit.IsSet() {
		key += fmt.Sprintf(":limit_%d", params.Limit.Value)
	}
	if params.Offset.IsSet() {
		key += fmt.Sprintf(":offset_%d", params.Offset.Value)
	}
	return key
}

func (h *TradingCoreHandler) invalidateListingCache(ctx context.Context, listingID string) {
	// BACKEND NOTE: Cache invalidation after trade execution
	keys := []string{
		fmt.Sprintf("listing:%s", listingID),
		"listings:*", // Invalidate all listings cache
	}

	for _, key := range keys {
		if err := h.service.redis.Delete(ctx, key); err != nil {
			log.Printf("Cache invalidation failed for key %s: %v", key, err)
		}
	}
}

func getUserIDFromContext(ctx context.Context) string {
	// Extract user ID from JWT token in context
	// Implementation depends on authentication middleware
	return "user-123" // Placeholder
}
