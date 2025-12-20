// Package server Issue: #1592 - Character Service ogen Migration
// Security handler for ogen (JWT authentication with Keycloak)
package server

import (
	"context"

	"github.com/necpgame/character-service-go/pkg/api"
)

// SecurityHandlerOgen implements ogen.SecurityHandler
type SecurityHandlerOgen struct {
	keycloakURL string
}

// NewSecurityHandlerOgen creates new security handler
func NewSecurityHandlerOgen(keycloakURL string) *SecurityHandlerOgen {
	return &SecurityHandlerOgen{
		keycloakURL: keycloakURL,
	}
}

// HandleBearerAuth implements BearerAuth security scheme
func (s *SecurityHandlerOgen) HandleBearerAuth(ctx context.Context, _ string, _ api.BearerAuth) (context.Context, error) {
	// TODO: Implement JWT validation with Keycloak
	// - Parse JWT token
	// - Validate with Keycloak public keys
	// - Check expiration
	// - Extract user ID, roles
	// - Store in context

	// For now, pass through (mock)
	return ctx, nil
}
