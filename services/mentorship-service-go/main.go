package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/mentorship-service-go/pkg/api"
	"github.com/gc-lover/necpgame-monorepo/services/mentorship-service-go/server"
	"github.com/kelseyhightower/envconfig"
	"go.uber.org/zap"
)

// Config holds all configuration for the mentorship service
type Config struct {
	Port         string        `envconfig:"PORT" default:"8081"`
	DatabaseURL  string        `envconfig:"DATABASE_URL" required:"true"`
	LogLevel     string        `envconfig:"LOG_LEVEL" default:"info"`
	ReadTimeout  time.Duration `envconfig:"READ_TIMEOUT" default:"15s"`
	WriteTimeout time.Duration `envconfig:"WRITE_TIMEOUT" default:"15s"`
	IdleTimeout  time.Duration `envconfig:"IDLE_TIMEOUT" default:"60s"`

	// Performance tuning for mentorship operations
	MaxDBConnections    int           `envconfig:"MAX_DB_CONNECTIONS" default:"200"`
	MinDBConnections    int           `envconfig:"MIN_DB_CONNECTIONS" default:"20"`
	DBConnMaxLifetime   time.Duration `envconfig:"DB_CONN_MAX_LIFETIME" default:"30m"`
	DBConnMaxIdleTime   time.Duration `envconfig:"DB_CONN_MAX_IDLE_TIME" default:"10m"`
}

func main() {
	// Load configuration
	var cfg Config
	if err := envconfig.Process("", &cfg); err != nil {
		log.Fatal("Failed to load config:", err)
	}

	// Initialize logger with configurable level
	config := zap.NewProductionConfig()
	if cfg.LogLevel == "debug" {
		config.Level = zap.NewAtomicLevelAt(zap.DebugLevel)
	}
	logger, err := config.Build()
	if err != nil {
		log.Fatalf("failed to create logger: %v", err)
	}
	defer func() {
		if syncErr := logger.Sync(); syncErr != nil {
			log.Printf("failed to sync logger: %v", syncErr)
		}
	}()

	logger.Info("Starting Mentorship Service",
		zap.String("port", cfg.Port),
		zap.String("database_url", cfg.DatabaseURL[:20]+"..."), // Log partial URL for security
		zap.Int("max_db_connections", cfg.MaxDBConnections))

	handler := server.NewHandlerWithConfig(logger, server.Config{
		DatabaseURL:       cfg.DatabaseURL,
		MaxDBConnections:  cfg.MaxDBConnections,
		MinDBConnections:  cfg.MinDBConnections,
		DBConnMaxLifetime: cfg.DBConnMaxLifetime,
		DBConnMaxIdleTime: cfg.DBConnMaxIdleTime,
	})

	srv, err := api.NewServer(handler, handler)
	if err != nil {
		logger.Fatal("failed to create server", zap.Error(err))
	}

	httpServer := &http.Server{
		Addr:         ":" + cfg.Port,
		Handler:      srv,
		ReadTimeout:  cfg.ReadTimeout,
		WriteTimeout: cfg.WriteTimeout,
		IdleTimeout:  cfg.IdleTimeout,
	}

	go func() {
		logger.Info("Mentorship Service listening", zap.String("addr", httpServer.Addr))
		if err := httpServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Server failed to start", zap.Error(err))
		}
	}()

	logger.Info("Mentorship Service started successfully",
		zap.String("port", cfg.Port),
		zap.Duration("read_timeout", cfg.ReadTimeout),
		zap.Duration("write_timeout", cfg.WriteTimeout))

	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	logger.Info("Shutting down Mentorship Service...")

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpServer.Shutdown(ctx); err != nil {
		logger.Fatal("Server forced to shutdown", zap.Error(err))
	}
	logger.Info("Server exited gracefully.")
}




