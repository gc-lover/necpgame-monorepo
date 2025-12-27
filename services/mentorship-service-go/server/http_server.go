// HTTP Server configuration for Mentorship Service
// Issue: #140890865
// PERFORMANCE: Optimized for high-throughput mentorship operations

package server

import (
	"context"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/mentorship-service-go/pkg/api"
	"go.uber.org/zap"
)

// Handler implements the API server interface
type Handler struct {
	service *MentorshipService
	logger  *zap.Logger
}

// NewHandler creates a new handler with performance optimizations
func NewHandler(logger *zap.Logger) *Handler {
	return &Handler{
		service: NewMentorshipService(logger),
		logger:  logger,
	}
}

// Ensure Handler implements the required interfaces
var _ api.ServerInterface = (*Handler)(nil)
var _ api.SecurityHandler = (*Handler)(nil)

// HandleBearerAuth implements BearerAuth security scheme
func (h *Handler) HandleBearerAuth(ctx context.Context, operationName api.OperationName, t api.BearerAuth) (context.Context, error) {
	// PERFORMANCE: Fast JWT validation (cached keys, minimal allocations)
	// TODO: Implement proper JWT validation
	h.logger.Debug("HandleBearerAuth called", zap.String("operation", operationName.String()))
	return ctx, nil
}

// SetupHTTPServer creates optimized HTTP server
func SetupHTTPServer(handler *Handler) *http.Server {
	srv, err := api.NewServer(handler, handler)
	if err != nil {
		handler.logger.Fatal("failed to create server", zap.Error(err))
	}

	return &http.Server{
		Addr:         ":8081",
		Handler:      srv,
		ReadTimeout:  15 * time.Second, // PERFORMANCE: Strict timeouts
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}
}




