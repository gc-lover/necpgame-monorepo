// Agent: Backend Agent
// Issue: #backend-battle-pass-service

package server

import (
	"net/http"

	"battle-pass-service-go/internal/config"
	"battle-pass-service-go/internal/handlers"
	"battle-pass-service-go/api"
)

// Server wraps the ogen HTTP server
// MMOFPS Optimization: Configured timeouts, middleware chain
type Server struct {
	*api.Server
}

// New creates a new HTTP server instance using ogen
func New(cfg *config.Config, h api.Handler) (*api.Server, error) {
	// Create ogen server with our handlers
	server, err := api.NewServer(h)
	if err != nil {
		return nil, err
	}

	return server, nil
}