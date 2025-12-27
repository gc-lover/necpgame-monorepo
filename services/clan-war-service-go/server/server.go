package server

import (
	"context"
	"net/http"
	"sync"
	"time"

	"github.com/go-chi/jwtauth/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	clanwarservice "github.com/gc-lover/necpgame-monorepo/services/clan-war-service-go/pkg/api"
)

// Server implements the clanwarservice.Handler
type Server struct {
	logger        *zap.Logger
	db            *pgxpool.Pool
	tokenAuth     *jwtauth.JWTAuth
	healthPool    *sync.Pool // Memory pool for health checks
}

// NewServer creates a new server instance
func NewServer(db *pgxpool.Pool, logger *zap.Logger, tokenAuth *jwtauth.JWTAuth) *Server {
	return &Server{
		db:        db,
		logger:    logger,
		tokenAuth: tokenAuth,
		healthPool: &sync.Pool{
			New: func() interface{} {
				return &clanwarservice.HealthResponse{}
			},
		},
	}
}

// CreateRouter creates the HTTP router with all endpoints
func (s *Server) CreateRouter() http.Handler {
	h := clanwarservice.NewServer(s)
	return h
}

// GetHealth implements clanwarservice.Handler
func (s *Server) GetHealth(ctx context.Context) (clanwarservice.GetHealthRes, error) {
	// Get health response from pool
	healthResp := s.healthPool.Get().(*clanwarservice.HealthResponse)
	defer s.healthPool.Put(healthResp)

	// Reset the struct
	*healthResp = clanwarservice.HealthResponse{}

	// Fill with current health data
	healthResp.Status = "healthy"
	healthResp.Timestamp = time.Now()
	healthResp.Version = "1.0.0"
	healthResp.Uptime = 0 // TODO: Implement uptime tracking

	s.logger.Info("Health check requested",
		zap.String("status", healthResp.Status),
		zap.Time("timestamp", healthResp.Timestamp))

	return &clanwarservice.GetHealthOK{
		Data: *healthResp,
	}, nil
}

// Issue: #1846
