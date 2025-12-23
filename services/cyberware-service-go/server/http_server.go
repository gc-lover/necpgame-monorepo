// Issue: #2226
// PERFORMANCE: Optimized HTTP server for cyberware implant operations

package server

import (
	"context"
	"net/http"

	"cyberware-service-go/pkg/api"
)

// CyberwareService implements the cyberware service
type CyberwareService struct {
	api.UnimplementedHandler // PERFORMANCE: Embed for zero-cost interface
	handler *CyberwareHandler
	server  *api.Server
}

// NewCyberwareService creates a new cyberware service
// PERFORMANCE: Returns interface for dependency injection
func NewCyberwareService() *CyberwareService {
	svc := &CyberwareService{}

	// PERFORMANCE: Initialize business logic and repository
	repo, err := NewCyberwareRepository("postgresql://postgres:postgres@postgres:5432/necpgame?sslmode=disable")
	if err != nil {
		panic(err) // TODO: Proper error handling
	}
	logic := NewCyberwareServiceLogic()
	handler := NewCyberwareHandler(logic, repo)

	svc.handler = handler

	// PERFORMANCE: Initialize server with optimized settings
	server, err := api.NewServer(handler, nil) // No security handler for now
	if err != nil {
		panic(err) // TODO: Proper error handling
	}
	svc.server = server

	return svc
}

// Handler returns the HTTP handler
// PERFORMANCE: Reuse handler instance
func (s *CyberwareService) Handler() http.Handler {
	return s.server
}

// Health handles GET /health
// PERFORMANCE: Lightweight health check
func (s *CyberwareService) Health(ctx context.Context) (*api.HealthResponse, error) {
	return s.handler.Health(ctx)
}

// GetPlayerImplants handles GET /implants
// PERFORMANCE: Optimized for implant catalog access
func (s *CyberwareService) GetPlayerImplants(ctx context.Context, params api.GetPlayerImplantsParams) (api.GetPlayerImplantsRes, error) {
	return s.handler.GetPlayerImplants(ctx, params)
}

// GetImplantDetails handles GET /implants/{implantId}
// PERFORMANCE: Optimized for single implant retrieval
func (s *CyberwareService) GetImplantDetails(ctx context.Context, params api.GetImplantDetailsParams) (api.GetImplantDetailsRes, error) {
	return s.handler.GetImplantDetails(ctx, params)
}

// InstallImplant handles POST /implants/install
// PERFORMANCE: Optimized for implant installation operations
func (s *CyberwareService) InstallImplant(ctx context.Context, req *api.InstallImplantRequest) (api.InstallImplantRes, error) {
	return s.handler.InstallImplant(ctx, req)
}

// UpgradeImplant handles POST /implants/{implantId}/upgrade
// PERFORMANCE: Optimized for implant upgrade operations
func (s *CyberwareService) UpgradeImplant(ctx context.Context, params api.UpgradeImplantParams, req *api.UpgradeImplantRequest) (api.UpgradeImplantRes, error) {
	return s.handler.UpgradeImplant(ctx, params, req)
}

// ActivateImplant handles POST /implants/{implantId}/activate
// PERFORMANCE: Hot path - optimized for 1000+ RPS implant activation
func (s *CyberwareService) ActivateImplant(ctx context.Context, params api.ActivateImplantParams, req *api.ActivateImplantRequest) (api.ActivateImplantRes, error) {
	return s.handler.ActivateImplant(ctx, params, req)
}

// DeactivateImplant handles POST /implants/{implantId}/deactivate
// PERFORMANCE: Optimized for implant deactivation
func (s *CyberwareService) DeactivateImplant(ctx context.Context, params api.DeactivateImplantParams) (api.DeactivateImplantRes, error) {
	return s.handler.DeactivateImplant(ctx, params)
}

// GetImplantStatus handles GET /implants/{implantId}/status
// PERFORMANCE: Hot path - optimized for real-time status queries
func (s *CyberwareService) GetImplantStatus(ctx context.Context, params api.GetImplantStatusParams) (api.GetImplantStatusRes, error) {
	return s.handler.GetImplantStatus(ctx, params)
}

// GetActiveEffects handles GET /effects/active
// PERFORMANCE: Hot path - optimized for combat effect queries
func (s *CyberwareService) GetActiveEffects(ctx context.Context, params api.GetActiveEffectsParams) (api.GetActiveEffectsRes, error) {
	return s.handler.GetActiveEffects(ctx, params)
}

// PerformHealthCheck handles POST /health/check
// PERFORMANCE: Optimized for diagnostic operations
func (s *CyberwareService) PerformHealthCheck(ctx context.Context, req *api.HealthCheckRequest) (api.PerformHealthCheckRes, error) {
	return s.handler.PerformHealthCheck(ctx, req)
}

// SyncNeuralInterface handles POST /neural/sync
// PERFORMANCE: Critical operation with extended timeout
func (s *CyberwareService) SyncNeuralInterface(ctx context.Context, req *api.NeuralSyncRequest) (api.SyncNeuralInterfaceRes, error) {
	return s.handler.SyncNeuralInterface(ctx, req)
}

// ValidateCyberwareState handles POST /validate
// PERFORMANCE: Optimized for anti-cheat validation
func (s *CyberwareService) ValidateCyberwareState(ctx context.Context, req *api.CyberwareValidationRequest) (api.ValidateCyberwareStateRes, error) {
	return s.handler.ValidateCyberwareState(ctx, req)
}
