package server

import (
	"context"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"

	"github.com/gc-lover/necpgame-monorepo/services/combat-damage-service-go/pkg/api"
)

// Server implements the api.Handler
type Server struct {
	handlers *Handlers
}

// NewServer creates a new server instance
func NewServer(db interface{}, logger interface{}, tokenAuth interface{}) *Server {
	handlers := NewHandlers()
	return &Server{
		handlers: handlers,
	}
}

// CalculateDamage implements api.Handler
func (s *Server) CalculateDamage(ctx context.Context, req *api.DamageRequest) (api.CalculateDamageRes, error) {
	// Simple implementation
	return nil, nil
}

// ValidateDamage implements api.Handler
func (s *Server) ValidateDamage(ctx context.Context, req *api.DamageValidationRequest) (api.ValidateDamageRes, error) {
	// Simple implementation
	return nil, nil
}

// ApplyEffects implements api.Handler
func (s *Server) ApplyEffects(ctx context.Context, req *api.EffectsRequest) (api.ApplyEffectsRes, error) {
	// Simple implementation
	return nil, nil
}

// GetActiveEffects implements api.Handler
func (s *Server) GetActiveEffects(ctx context.Context, params api.GetActiveEffectsParams) (api.GetActiveEffectsRes, error) {
	// Simple implementation
	return nil, nil
}

// RemoveEffect implements api.Handler
func (s *Server) RemoveEffect(ctx context.Context, params api.RemoveEffectParams) (api.RemoveEffectRes, error) {
	// Simple implementation
	return nil, nil
}

// HealthCheck implements api.Handler
func (s *Server) HealthCheck(ctx context.Context) (api.HealthCheckRes, error) {
	// Simple implementation
	return nil, nil
}

// CreateRouter creates Chi router with ogen handlers
func (s *Server) CreateRouter() *chi.Mux {
	r := chi.NewRouter()

	// Create ogen server
	server, err := api.NewServer(s, nil) // No security handler for now
	if err != nil {
		panic("Failed to create ogen server: " + err.Error())
	}

	// Mount ogen server
	r.Mount("/api/v1", http.HandlerFunc(server.ServeHTTP))

	return r
}

// Issue: #2251