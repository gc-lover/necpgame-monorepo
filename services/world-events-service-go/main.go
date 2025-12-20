// Issue: #2224
package main

import (
	"context"
	"database/sql"
	"fmt"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #2224 - profiling endpoints
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/world-events-service-go/server"
	"go.uber.org/zap"
)

func main() {
	// Initialize structured logger
	logger, err := zap.NewProduction()
	if err != nil {
		panic("failed to initialize logger: " + err.Error())
	}
	defer logger.Sync()

	logger.Info("World Events Service (Go) starting...")

	// Load configuration
	config, err := loadConfig()
	if err != nil {
		logger.Fatal("Failed to load configuration", zap.Error(err))
	}

	// Connect to database
	db, err := connectDatabase(config.DatabaseURL, logger)
	if err != nil {
		logger.Fatal("Failed to connect to database", zap.Error(err))
	}
	defer db.Close()

	// Create server
	srv := server.NewWorldEventsServer(logger, db, config.JWTSecret)

	// OPTIMIZATION: Issue #2224 - Start pprof server for profiling
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6555")
		logger.Info("pprof server starting", zap.String("addr", pprofAddr))
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			logger.Error("pprof server failed", zap.Error(err))
		}
	}()

	// Start server
	go func() {
		logger.Info("HTTP server listening", zap.String("addr", ":8086"))
		if err := srv.Start(":8086"); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Failed to start HTTP server", zap.Error(err))
		}
	}()

	// Wait for interrupt signal
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("Shutting down server...")

	// Graceful shutdown
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		logger.Error("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Server exited")
}

// Config holds application configuration
type Config struct {
	DatabaseURL string
	JWTSecret   string
}

// loadConfig loads configuration from environment variables
func loadConfig() (*Config, error) {
	databaseURL := os.Getenv("DATABASE_URL")
	if databaseURL == "" {
		databaseURL = "postgres://necpgame:necpgame@localhost:5432/necpgame?sslmode=disable"
	}

	jwtSecret := os.Getenv("JWT_SECRET")
	if jwtSecret == "" {
		return nil, fmt.Errorf("JWT_SECRET environment variable is required")
	}

	return &Config{
		DatabaseURL: databaseURL,
		JWTSecret:   jwtSecret,
	}, nil
}

// connectDatabase establishes connection to PostgreSQL with optimized settings
func connectDatabase(databaseURL string, logger *zap.Logger) (*sql.DB, error) {
	logger.Info("Connecting to database")

	db, err := sql.Open("pgx", databaseURL)
	if err != nil {
		return nil, fmt.Errorf("failed to open database connection: %w", err)
	}

	// MMOFPS OPTIMIZATION: Configure connection pool for high-performance world events
	db.SetMaxOpenConns(50)                 // Higher limit for world events processing
	db.SetMaxIdleConns(15)                 // More idle connections for event coordination
	db.SetConnMaxLifetime(5 * time.Minute) // Shorter lifetime for better resource management

	// Test connection with timeout
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	if err := db.PingContext(ctx); err != nil {
		db.Close()
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	logger.Info("Database connection established")
	return db, nil
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
