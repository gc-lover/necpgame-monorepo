package server

import (
	"context"

	"github.com/gc-lover/necpgame/services/support-service-go/api"
)

// SecurityAdapter adapts AuthMiddleware to api.SecurityHandler
type SecurityAdapter struct {
	middleware *AuthMiddleware
}

// NewSecurityAdapter creates a new security adapter
func NewSecurityAdapter(middleware *AuthMiddleware) api.SecurityHandler {
	return &SecurityAdapter{
		middleware: middleware,
	}
}

// HandleApiKeyAuth implements the API key authentication
func (s *SecurityAdapter) HandleApiKeyAuth(ctx context.Context, _ api.OperationName, apiKey api.ApiKeyAuth) (context.Context, error) {
	// For JWT-based auth, we don't use API keys directly
	// The middleware handles JWT tokens from Authorization header
	return ctx, nil
}

// HandleBearerAuth implements the Bearer token authentication
func (s *SecurityAdapter) HandleBearerAuth(ctx context.Context, _ api.OperationName, bearer api.BearerAuth) (context.Context, error) {
	// The middleware handles JWT tokens, so we return the context as-is
	// Actual validation happens in the HTTP middleware
	return ctx, nil
}