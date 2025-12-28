package server

import (
	"context"
	"net/http"

	"github.com/go-chi/jwtauth/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	clanwarservice "github.com/gc-lover/necpgame-monorepo/services/clan-war-service-go/pkg/api"
)

// Server implements the clanwarservice.Handler
type Server struct {
	logger    *zap.Logger
	db        *pgxpool.Pool
	tokenAuth *jwtauth.JWTAuth
}

// SecurityHandlerImpl implements clanwarservice.SecurityHandler
type SecurityHandlerImpl struct{}

// HandleApiKeyAuth handles API key authentication
func (s *SecurityHandlerImpl) HandleApiKeyAuth(ctx context.Context, operationName clanwarservice.OperationName, t clanwarservice.ApiKeyAuth) (context.Context, error) {
	// TODO: Implement API key validation
	return ctx, nil
}

// HandleBearerAuth handles JWT bearer authentication
func (s *SecurityHandlerImpl) HandleBearerAuth(ctx context.Context, operationName clanwarservice.OperationName, t clanwarservice.BearerAuth) (context.Context, error) {
	// TODO: Implement JWT validation
	return ctx, nil
}

// NewServer creates a new server instance
func NewServer(db *pgxpool.Pool, logger *zap.Logger, tokenAuth *jwtauth.JWTAuth) *Server {
	return &Server{
		db:        db,
		logger:    logger,
		tokenAuth: tokenAuth,
	}
}

// CreateRouter creates the HTTP router with all endpoints
func (s *Server) CreateRouter() http.Handler {
	securityHandler := &SecurityHandlerImpl{}
	h, err := clanwarservice.NewServer(s, securityHandler)
	if err != nil {
		s.logger.Fatal("Failed to create OpenAPI server", zap.Error(err))
	}
	return h
}

// GetHealth implements clanwarservice.Handler
func (s *Server) GetHealth(ctx context.Context) error {
	s.logger.Info("Health check requested")
	return nil
}

// Issue: #1846
