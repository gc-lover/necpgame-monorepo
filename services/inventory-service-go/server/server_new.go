// Issue: #1591 - Server creation and configuration

package server

import (
	"net/http"

	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/inventory-service-go/oas"
)

// CreateRouter creates and configures the HTTP router with all handlers
func (s *Server) CreateRouter() http.Handler {
	// Create ogen server with all handlers
	ogenSrv, err := oas.NewServer(s, s)
	if err != nil {
		s.logger.Fatal("Failed to create ogen server", zap.Error(err))
	}

	return ogenSrv
}

// Issue: #1591

