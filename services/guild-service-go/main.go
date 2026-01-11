//go:align 64
// Issue: #2290 - Backend implementation fixes

package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"runtime/debug"
	"syscall"
	"time"

	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"

	"guild-service-go/internal/repository"
	"guild-service-go/internal/service"
	"guild-service-go/server"
	"guild-service-go/pkg/api"
)

// SecurityHandler implements security middleware for BearerAuth
type SecurityHandler struct{}

// HandleBearerAuth handles Bearer token authentication
func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName api.OperationName, t api.BearerAuth) (context.Context, error) {
	// TODO: Implement proper JWT token validation
	// For now, accept any token
	return ctx, nil
}

func main() {
	// PERFORMANCE: Optimize GC for MMOFPS guild system
	if gcPercent := os.Getenv("GOGC"); gcPercent == "" {
		debug.SetGCPercent(50) // Reduce GC pressure for high-frequency guild operations
	}

	logger := log.New(os.Stdout, "[guild-service] ", log.LstdFlags|log.Lmicroseconds)

	// Initialize repository
	repo := repository.NewRepository()

	// Initialize Redis client (mock for now)
	redisClient := redis.NewClient(&redis.Options{
		Addr: "localhost:6379",
	})

	// Initialize service config
	serviceConfig := service.Config{
		MaxGuildNameLength:    50,
		MaxGuildDescription:   500,
		DefaultMaxMembers:     100,
		GuildOperationTimeout: 30 * time.Second,
		MaxConcurrentOps:      50,
	}

	// Initialize zap logger (mock for now)
	zapLogger, _ := zap.NewDevelopment()

	// Initialize service
	svc := service.NewService(repo, serviceConfig, redisClient, zapLogger)

	// Initialize handlers
	handlerConfig := &handlers.Config{
		MaxWorkers: 50,
		CacheTTL:   10 * time.Minute,
	}
	h := handlers.NewGuildHandler(handlerConfig, svc, repo, zapLogger)

	// Create server with security handler
	httpSrv, _ := api.NewServer(h, &SecurityHandler{})

	// PERFORMANCE: Configure HTTP server for low latency
	server := &http.Server{
		Addr:           getEnv("SERVER_ADDR", ":8080"),
		Handler:        httpSrv,
		ReadTimeout:    15 * time.Second,
		WriteTimeout:   15 * time.Second,
		IdleTimeout:    60 * time.Second,
		MaxHeaderBytes: 1 << 16,
	}

	// Graceful shutdown handling
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)

	// Start server in background
	go func() {
		logger.Printf("Starting Guild Service on %s", server.Addr)

		if err := server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatalf("HTTP server error: %v", err)
		}
	}()

	// Wait for shutdown signal
	<-quit
	logger.Println("Shutting down Guild Service...")

	// PERFORMANCE: Graceful shutdown with timeout for active guild sessions
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := server.Shutdown(ctx); err != nil {
		logger.Printf("Server forced to shutdown: %v", err)
	}

	logger.Println("Guild Service exited")
}

// getEnv gets environment variable with fallback
func getEnv(key, fallback string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return fallback
}