package main

import (
	"context"
	"fmt"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/ogen-go/ogen/middleware"
	"go.uber.org/zap"
)

func main() {
	// Initialize structured logging
	logger, err := zap.NewProduction()
	if err != nil {
		panic(fmt.Sprintf("failed to create logger: %v", err))
	}
	defer logger.Sync()

	logger.Info("Starting relationship-service-go")

	// Create handler
	handler := NewReputationHandler(logger)

	// Create server
	srv, err := api.NewServer(handler,
		api.WithMiddleware(middleware.Logger(logger)),
		api.WithMiddleware(middleware.Recovery()),
	)
	if err != nil {
		logger.Fatal("Failed to create server", zap.Error(err))
	}

	// HTTP server configuration
	httpSrv := &http.Server{
		Addr:         ":8080",
		Handler:      srv,
		ReadTimeout:  30 * time.Second,
		WriteTimeout: 30 * time.Second,
		IdleTimeout:  120 * time.Second,
	}

	// Start server in goroutine
	go func() {
		logger.Info("Server starting on :8080")
		if err := httpSrv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Server failed to start", zap.Error(err))
		}
	}()

	// Wait for interrupt signal to gracefully shutdown the server
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("Shutting down server...")

	// Graceful shutdown
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpSrv.Shutdown(ctx); err != nil {
		logger.Error("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Server stopped")
}