package server

import (
	"context"
	"net/http"

	"github.com/go-chi/jwtauth/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/achievement-service-go/pkg/api"
)

// Server wraps the ogen-generated server with our handlers
type Server struct {
	db        *pgxpool.Pool
	logger    *zap.Logger
	tokenAuth *jwtauth.JWTAuth
	handlers  *Handlers
	ogenSrv   *api.Server
}

// NewServer creates a new server instance with ogen integration
func NewServer(db *pgxpool.Pool, logger *zap.Logger, tokenAuth *jwtauth.JWTAuth, cfg *Config) *Server {
	handlers := NewHandlers(db, logger, cfg)

	// Create ogen server with security handler
	secHandler := &SecurityHandler{tokenAuth: tokenAuth}
	ogenSrv, err := api.NewServer(handlers, secHandler)
	if err != nil {
		logger.Fatal("Failed to create ogen server", zap.Error(err))
	}

	return &Server{
		db:        db,
		logger:    logger,
		tokenAuth: tokenAuth,
		handlers:  handlers,
		ogenSrv:   ogenSrv,
	}
}

// CreateRouter creates the HTTP router with ogen handlers
func (s *Server) CreateRouter() http.Handler {
	return s.ogenSrv
}

// SecurityHandler implements JWT authentication for ogen
type SecurityHandler struct {
	tokenAuth *jwtauth.JWTAuth
}

// HandleBearerAuth implements api.SecurityHandler
func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName string, t api.BearerAuth) (context.Context, error) {
	// TODO: Implement JWT token validation
	// For now, allow all requests
	return ctx, nil
}

// Issue: #2253