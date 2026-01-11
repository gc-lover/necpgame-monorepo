package server

import (
	"context"

	"github.com/gc-lover/necpgame/services/stock-dividends-service-go/api"
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

// HandleBearerAuth implements the Bearer token authentication
func (s *SecurityAdapter) HandleBearerAuth(ctx context.Context, _ api.OperationName, bearer api.BearerAuth) (context.Context, error) {
	// The middleware handles JWT tokens, so we return the context as-is
	// Actual validation happens in the HTTP middleware
	return ctx, nil
}