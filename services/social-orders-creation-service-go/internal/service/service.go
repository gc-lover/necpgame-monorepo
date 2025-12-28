// Issue: #140894825 - Social Orders Service: ogen handlers implementation
// PERFORMANCE: Social orders service interface optimized for business logic operations

package service

import (
	"context"

	"github.com/google/uuid"

	"social-orders-creation-service-go/internal/models"
)

// SocialOrdersService defines the business logic operations for social orders
type SocialOrdersService interface {
	// CreateOrderDraft creates an order draft with validation and suggestions
	CreateOrderDraft(ctx context.Context, clientID uuid.UUID, req models.CreateOrderDraftRequest) (*models.OrderDraftResponse, error)

	// ValidateOrderParameters validates order parameters
	ValidateOrderParameters(ctx context.Context, clientID uuid.UUID, req models.ValidateOrderRequest) (*models.OrderValidationResponse, error)

	// OptimizeOrderParameters provides optimization suggestions
	OptimizeOrderParameters(ctx context.Context, clientID uuid.UUID, req models.OptimizeOrderRequest) (*models.OrderOptimizationResponse, error)

	// SuggestContractors suggests suitable contractors for an order
	SuggestContractors(ctx context.Context, req models.SuggestContractorsRequest, maxSuggestions int) (*models.SuggestedContractorsResponse, error)

	// CreateOrderWithValidation creates an order with full validation and optimization
	CreateOrderWithValidation(ctx context.Context, clientID uuid.UUID, req models.CreateOrderWithValidationRequest) (*models.OrderCreatedResponse, error)
}
