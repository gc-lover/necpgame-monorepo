// Issue: #2220 - Server creation and configuration

package server

import (
	"net/http"

	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/matchmaking-service-go/pkg/api"
)

// CreateRouter creates and configures the HTTP router with all handlers
func (s *Server) CreateRouter() http.Handler {
	// Create ogen server with all handlers
	ogenSrv, err := api.NewServer(s, s)
	if err != nil {
		s.logger.Fatal("Failed to create ogen server", zap.Error(err))
	}

	return ogenSrv
}

// Issue: #2220
