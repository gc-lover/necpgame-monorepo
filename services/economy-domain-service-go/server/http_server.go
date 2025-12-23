// Issue: #backend-economy_domain

package server

import (
	"context"
	"net/http"

	"economy-domain-service-go/pkg/api"
)

// SecurityHandler implements the security interface for Bearer token authentication
type SecurityHandler struct{}

func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName api.OperationName, t api.BearerAuth) (context.Context, error) {
	// For health check endpoint, allow anonymous access
	if operationName == api.GetEconomyHealthOperation {
		return ctx, nil
	}
	// TODO: Implement proper JWT token validation for other endpoints
	return ctx, nil
}

type EconomydomainService struct {
	api *api.Server
}

func NewEconomydomainService() *EconomydomainService {
	handler := NewHandler()
	securityHandler := &SecurityHandler{}

	server, err := api.NewServer(handler, securityHandler)
	if err != nil {
		panic("Failed to create API server: " + err.Error())
	}

	return &EconomydomainService{
		api: server,
	}
}

func (s *EconomydomainService) Handler() http.Handler {
	return s.api
}
