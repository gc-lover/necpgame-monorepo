// Issue: #140875381
package main

import (
	"context"
	"fmt"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/joho/godotenv"
	"go.uber.org/zap"

	"services/world-cities-service-go/internal/config"
	"services/world-cities-service-go/internal/database"
	"services/world-cities-service-go/internal/server"
	"services/world-cities-service-go/internal/service"
)

// BACKEND NOTE: World Cities Service - Enterprise-grade microservice for geographical city management
// Issue: #140875381
// Performance: Optimized for MMORPG scale with spatial queries and JSONB operations
// Architecture: Clean architecture with repository, service, and handler layers
// Security: JWT authentication with proper RBAC
// Monitoring: Structured logging with Zap, health checks, metrics

func main() {
	// Load environment variables
	if err := godotenv.Load(); err != nil {
		log.Printf("Warning: .env file not found: %v", err)
	}

	// Initialize structured logger
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	// Load configuration
	cfg, err := config.Load()
	if err != nil {
		logger.Fatal("Failed to load configuration", zap.Error(err))
	}

	// Initialize database connection
	dbConfig := &database.DatabaseConfig{
		Host:     cfg.Database.Host,
		Port:     fmt.Sprintf("%d", cfg.Database.Port),
		User:     cfg.Database.User,
		Password: cfg.Database.Password,
		Database: cfg.Database.DBName,
		SSLMode:  cfg.Database.SSLMode,
	}
	db, err := database.NewDatabaseConnection(dbConfig)
	if err != nil {
		logger.Fatal("Failed to connect to database", zap.Error(err))
	}
	defer db.Close()

	// Run database migrations
	if err := database.RunMigrations(db, logger); err != nil {
		logger.Fatal("Failed to run migrations", zap.Error(err))
	}

	// Initialize database layer
	database := database.NewDatabase(db, logger)

	// Initialize HTTP server
	srv := server.NewServer(database, logger)

	// Start server in goroutine
	go func() {
		logger.Info("Starting World Cities Service",
			zap.String("address", cfg.Server.Address),
			zap.Int("port", cfg.Server.Port))

		if err := srv.Start(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Failed to start server", zap.Error(err))
		}
	}()

	// Wait for interrupt signal to gracefully shutdown the server
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("Shutting down server...")

	// Give outstanding requests 30 seconds to complete
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		logger.Error("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Server exited")
}




