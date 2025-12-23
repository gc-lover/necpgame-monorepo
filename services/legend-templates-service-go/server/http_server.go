// HTTP Server configuration for Legend Templates Service
// Issue: #2241
// PERFORMANCE: Optimized for high-throughput legend generation

package server

import (
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/legend-templates-service-go/pkg/api"
)

// NewHandler creates a new handler with performance optimizations
func NewHandler() *Handler {
	return &Handler{
		// PERFORMANCE: Initialize with pre-allocated pools
	}
}

// Ensure Handler implements the required interfaces
var _ api.Handler = (*Handler)(nil)
var _ api.SecurityHandler = (*Handler)(nil)

// SetupHTTPServer creates optimized HTTP server
func SetupHTTPServer(handler *Handler) *http.Server {
	server, err := api.NewServer(handler, handler)
	if err != nil {
		panic(err) // TODO: Proper error handling
	}

	return &http.Server{
		Addr:         ":8080",
		Handler:      server,
		ReadTimeout:  15 * time.Second, // PERFORMANCE: Strict timeouts
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}
}