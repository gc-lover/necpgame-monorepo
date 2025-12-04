// Issue: #1602
package server

import (
	"context"

	"github.com/ogen-go/ogen/ogenerrors"
	"github.com/necpgame/admin-service-go/pkg/api"
)

type SecurityHandler struct {
	jwtValidator *JwtValidator
	authEnabled  bool
}

func NewSecurityHandler(jwtValidator *JwtValidator, authEnabled bool) *SecurityHandler {
	return &SecurityHandler{
		jwtValidator: jwtValidator,
		authEnabled:  authEnabled,
	}
}

func (h *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName api.OperationName, t api.BearerAuth) (context.Context, error) {
	if !h.authEnabled || h.jwtValidator == nil {
		return ctx, nil
	}

	authHeader := "Bearer " + t.Token
	claims, err := h.jwtValidator.Verify(ctx, authHeader)
	if err != nil {
		return ctx, err
	}

	ctx = context.WithValue(ctx, "claims", claims)
	ctx = context.WithValue(ctx, "user_id", claims.Subject)
	ctx = context.WithValue(ctx, "username", claims.PreferredUsername)

	// Check admin role
	hasAdminRole := false
	for _, role := range claims.RealmAccess.Roles {
		if role == "admin" || role == "moderator" {
			hasAdminRole = true
			break
		}
	}

	if !hasAdminRole {
		return ctx, ogenerrors.ErrSecurityRequirementIsNotSatisfied
	}

	return ctx, nil
}

