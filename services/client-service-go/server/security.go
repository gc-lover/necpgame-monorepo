// Issue: #150 - Security Handler for ogen
package server

import (
	"context"

	api "github.com/necpgame/client-service-go/pkg/api"
)

// SecurityHandler implements ogen security handler
type SecurityHandler struct{}

// NewSecurityHandler creates new security handler
func NewSecurityHandler() *SecurityHandler {
	return &SecurityHandler{}
}

// HandleBearerAuth handles Bearer token authentication
func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName api.OperationName, t api.BearerAuth) (context.Context, error) {
	// TODO: Implement JWT validation if needed
	// For now, accept all requests
	return ctx, nil
}

