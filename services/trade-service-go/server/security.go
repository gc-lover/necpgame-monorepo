// Issue: #131
package server

import (
	"context"
	"net/http"

	"github.com/gc-lover/necpgame-monorepo/services/trade-service-go/pkg/api"
)

type SecurityHandler struct{}

func (h *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName string, t api.BearerAuth) (context.Context, error) {
	// Extract token from header
	req := ctx.Value("http_request").(*http.Request)
	token := req.Header.Get("Authorization")
	if token == "" {
		return ctx, nil
	}

	// Set user_id in context (stub)
	ctx = context.WithValue(ctx, "user_id", "user-id-from-token")
	return ctx, nil
}

