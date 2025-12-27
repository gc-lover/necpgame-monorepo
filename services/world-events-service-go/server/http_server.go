// World Events HTTP Server - Enterprise-grade server setup
// Issue: #2224
// PERFORMANCE: Optimized HTTP server for world event management

package server

import (
	"context"
	"database/sql"
	"fmt"
	"net/http"

	"github.com/go-redis/redis/v8"
	"github.com/golang-jwt/jwt/v4"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/world-events-service-go/pkg/api"
)

// Server wraps the HTTP server with handlers
type Server struct {
	handler *api.Server
}

// NewServer creates a new HTTP server instance
// PERFORMANCE: Dependency injection for database and cache
func NewServer(db *sql.DB, redisClient *redis.Client) *Server {
	// Create handler with PERFORMANCE optimizations and dependencies
	h := NewHandler(db, redisClient)

	// Create ogen server with middleware
	handler, err := api.NewServer(h, nil) // TODO: Add security handler if needed
	if err != nil {
		panic(err) // TODO: Proper error handling
	}

	return &Server{
		handler: handler,
	}
}

// Handler returns the HTTP handler
func (s *Server) Handler() http.Handler {
	return s.handler
}
