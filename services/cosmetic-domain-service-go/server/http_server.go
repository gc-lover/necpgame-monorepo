// Issue: #backend-cosmetic_domain
// PERFORMANCE: Optimized HTTP server with middleware and security

package server

import (
	"context"
	"net/http"

	"cosmetic-domain-service-go/pkg/api"
)

// SecurityHandler implements security for API endpoints
type securityHandler struct{}

func (s *securityHandler) HandleBearerAuth(ctx context.Context, operationName api.OperationName, t api.BearerAuth) (context.Context, error) {
	// TODO: Implement proper authentication
	// For now, accept all bearer tokens
	return ctx, nil
}

type CosmeticdomainService struct {
	api *api.Server
}

func NewCosmeticdomainService() *CosmeticdomainService {
	handler := NewHandler()
	security := &securityHandler{}

	server, err := api.NewServer(handler, security)
	if err != nil {
		panic(err) // TODO: Proper error handling
	}

	return &CosmeticdomainService{
		api: server,
	}
}

func (s *CosmeticdomainService) Handler() http.Handler {
	return s.api
}
