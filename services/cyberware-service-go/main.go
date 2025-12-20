// Package main implements the Cyberware Service for Advanced Cyberware Integration System
package main

import (
	"context"
	"database/sql"
	"fmt"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	_ "net/http/pprof" // Import for pprof endpoints

	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/cyberware-service-go/server"
)

func main() {
	// Initialize structured logger
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	// Load configuration
	config, err := loadConfig()
	if err != nil {
		logger.Fatal("Failed to load configuration", zap.Error(err))
	}

	// Connect to PostgreSQL database
	db, err := connectDatabase(config.DatabaseURL)
	if err != nil {
		logger.Fatal("Failed to connect to database", zap.Error(err))
	}
	defer db.Close()

	// Start pprof server for profiling
	go func() {
		logger.Info("Starting pprof server", zap.String("port", "6556"))
		if err := http.ListenAndServe(":6556", nil); err != nil {
			logger.Error("pprof server failed", zap.Error(err))
		}
	}()

	// Create HTTP server
	httpServer, err := server.NewCyberwareServer(db, logger)
	if err != nil {
		logger.Fatal("Failed to create HTTP server", zap.Error(err))
	}

	// Start server in goroutine
	go func() {
		logger.Info("Starting Cyberware Service", zap.String("port", config.ServerPort))
		if err := httpServer.Start(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Server failed to start", zap.Error(err))
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

	if err := httpServer.Shutdown(ctx); err != nil {
		logger.Fatal("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Server exited")
}

// Config holds application configuration
type Config struct {
	DatabaseURL string
	JWTSecret   string
	ServerPort  string
	RedisURL    string
}

// loadConfig loads configuration from environment variables
func loadConfig() (*Config, error) {
	databaseURL := os.Getenv("DATABASE_URL")
	if databaseURL == "" {
		return nil, fmt.Errorf("DATABASE_URL environment variable is required")
	}

	jwtSecret := os.Getenv("JWT_SECRET")
	if jwtSecret == "" {
		return nil, fmt.Errorf("JWT_SECRET environment variable is required")
	}

	serverPort := os.Getenv("SERVER_PORT")
	if serverPort == "" {
		serverPort = "8086" // Default port for cyberware service
	}

	redisURL := os.Getenv("REDIS_URL")
	if redisURL == "" {
		redisURL = "redis://localhost:6379" // Default Redis URL
	}

	return &Config{
		DatabaseURL: databaseURL,
		JWTSecret:   jwtSecret,
		ServerPort:  serverPort,
		RedisURL:    redisURL,
	}, nil
}

// connectDatabase establishes connection to PostgreSQL
func connectDatabase(databaseURL string) (*sql.DB, error) {
	db, err := sql.Open("postgres", databaseURL)
	if err != nil {
		return nil, fmt.Errorf("failed to open database: %w", err)
	}

	// Configure connection pool for MMOFPS performance
	db.SetMaxOpenConns(50) // High concurrent load support
	db.SetMaxIdleConns(10) // Reasonable idle connections
	db.SetConnMaxLifetime(5 * time.Minute)

	// Test connection with timeout
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	if err := db.PingContext(ctx); err != nil {
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	return db, nil
}
