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
	handler *FeatureFlagHandler
}

// NewHTTPServer creates a new HTTP server
func NewHTTPServer(service *FeatureFlagService, middleware []echo.MiddlewareFunc) *HTTPServer {
	e := echo.New()
	e.HideBanner = true
	e.HidePort = true

	// Apply middleware
	for _, mw := range middleware {
		e.Use(mw)
	}

	// Create handler
	handler := NewFeatureFlagHandler(service)

	// Register routes
	v1 := e.Group("/api/v1")

	// Feature flag operations
	flags := v1.Group("/flags")
	flags.POST("/:flagName/evaluate", handler.EvaluateFeature)
	flags.POST("", handler.CreateFeatureFlag)
	flags.GET("/:flagName", handler.GetFeatureFlag)
	flags.PUT("/:flagName", handler.UpdateFeatureFlag)
	flags.GET("", handler.ListFeatureFlags)

	// Bulk evaluation
	flags.POST("/evaluate/bulk", handler.BulkEvaluate)

	// Experiment operations
	experiments := v1.Group("/experiments")
	experiments.POST("", handler.CreateExperiment)
	experiments.POST("/:experimentId/start", handler.StartExperiment)
	experiments.GET("/:experimentId/results", handler.GetExperimentResults)

	// Service statistics
	v1.GET("/stats", handler.GetServiceStats)

	// Health check
	e.GET("/health", handler.HealthCheck)

	return &HTTPServer{
		server:  e,
		handler: handler,
	}
}

// Start starts the HTTP server
func (s *HTTPServer) Start(addr string) error {
	slog.Info("Starting Feature Flag HTTP server", "addr", addr)
	return s.server.Start(addr)
}

// Shutdown gracefully shuts down the HTTP server
func (s *HTTPServer) Shutdown(ctx context.Context) error {
	slog.Info("Shutting down Feature Flag HTTP server")
	return s.server.Shutdown(ctx)
}

// GetEchoServer returns the underlying Echo server (for testing)
func (s *HTTPServer) GetEchoServer() *echo.Echo {
	return s.server
}