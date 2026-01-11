package server

import (
	"context"
	"log/slog"
	"net/http"
	"time"

	"github.com/labstack/echo/v4"
)

// HTTPServer wraps Echo server with graceful shutdown
type HTTPServer struct {
	server  *echo.Echo
	handler *AssetCacheHandler
}

// NewHTTPServer creates a new HTTP server
func NewHTTPServer(service *AssetCacheService, middleware []echo.MiddlewareFunc) *HTTPServer {
	e := echo.New()
	e.HideBanner = true
	e.HidePort = true

	// Apply middleware
	for _, mw := range middleware {
		e.Use(mw)
	}

	// Create handler
	handler := NewAssetCacheHandler(service)

	// Register routes
	v1 := e.Group("/api/v1")

	// Asset operations
	assets := v1.Group("/assets")
	assets.GET("/:id", handler.GetAsset)
	assets.PUT("/:id", handler.PutAsset)
	assets.DELETE("/:id", handler.DeleteAsset)

	// Cache management
	cache := v1.Group("/cache")
	cache.GET("/stats", handler.GetCacheStats)
	cache.POST("/preload", handler.PreloadAssets)
	cache.POST("/cleanup", handler.CleanupExpiredAssets)

	// Health check
	e.GET("/health", handler.HealthCheck)

	return &HTTPServer{
		server:  e,
		handler: handler,
	}
}

// Start starts the HTTP server
func (s *HTTPServer) Start(addr string) error {
	slog.Info("Starting Asset Cache HTTP server", "addr", addr)
	return s.server.Start(addr)
}

// Shutdown gracefully shuts down the HTTP server
func (s *HTTPServer) Shutdown(ctx context.Context) error {
	slog.Info("Shutting down Asset Cache HTTP server")
	return s.server.Shutdown(ctx)
}

// GetEchoServer returns the underlying Echo server (for testing)
func (s *HTTPServer) GetEchoServer() *echo.Echo {
	return s.server
}