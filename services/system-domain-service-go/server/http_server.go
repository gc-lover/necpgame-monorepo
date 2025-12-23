// Issue: #backend-system_domain

package server

import (
	"context"
	"net/http"

	"system-domain-service-go/pkg/api"
)

// SecurityHandler implements the security interface for Bearer token authentication
type SecurityHandler struct{}

func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName api.OperationName, t api.BearerAuth) (context.Context, error) {
	// For health check endpoint, allow anonymous access
	if operationName == api.SystemDomainHealthCheckOperation {
		return ctx, nil
	}
	// TODO: Implement proper JWT token validation for other endpoints
	return ctx, nil
}

type SystemdomainService struct {
	api *api.Server
}

func NewSystemdomainService() *SystemdomainService {
	handler := NewHandler()
	securityHandler := &SecurityHandler{}

	server, err := api.NewServer(handler, securityHandler)
	if err != nil {
		panic("Failed to create API server: " + err.Error())
	}

	return &SystemdomainService{
		api: server,
	}
}

func (s *SystemdomainService) Handler() http.Handler {
	return s.api
}
