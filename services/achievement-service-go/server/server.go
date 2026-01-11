// Agent: Backend Agent
// Issue: #backend-achievement-service-1

package server

import (
	"context"

	"achievement-service-go/internal/config"
	"achievement-service-go/api"
)

// New creates a new HTTP server instance using ogen
func New(cfg *config.Config, h api.Handler) (*api.Server, error) {
	// Create security handler (for now, allow all)
	sec := &SecurityHandler{}

	// Create ogen server with our handlers
	ogenServer, err := api.NewServer(h, sec)
	if err != nil {
		return nil, err
	}

	return ogenServer, nil
}

// SecurityHandler implements basic security handling
type SecurityHandler struct{}

// HandleBearerAuth implements BearerAuth security
func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName api.OperationName, t api.BearerAuth) (context.Context, error) {
	// For now, accept all bearer tokens
	// In production, this would validate JWT tokens
	return ctx, nil
}

// HandleAdminAuth implements AdminAuth security
func (s *SecurityHandler) HandleAdminAuth(ctx context.Context, operationName api.OperationName, t api.AdminAuth) (context.Context, error) {
	// For now, accept all admin tokens
	// In production, this would validate admin JWT tokens with specific claims
	return ctx, nil
}