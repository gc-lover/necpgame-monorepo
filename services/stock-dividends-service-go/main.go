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

	"github.com/gc-lover/necpgame/services/stock-dividends-service-go/api"
	"github.com/gc-lover/necpgame/services/stock-dividends-service-go/internal/config"
	"github.com/gc-lover/necpgame/services/stock-dividends-service-go/internal/database"
	"github.com/gc-lover/necpgame/services/stock-dividends-service-go/internal/handlers"
	"github.com/gc-lover/necpgame/services/stock-dividends-service-go/internal/repository/postgres"
	"github.com/gc-lover/necpgame/services/stock-dividends-service-go/internal/server"
	"github.com/gc-lover/necpgame/services/stock-dividends-service-go/internal/service"

	"go.uber.org/zap"
)

func main() {
	// Initialize logger
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

	// Initialize database
	db, err := database.NewConnection(cfg.Database)
	if err != nil {
		logger.Fatal("Failed to connect to database", zap.Error(err))
	}
	defer db.Close()

	// Initialize repository
	repo := postgres.NewPostgresRepository(db, logger)

	// Initialize service
	dividendsSvc := service.NewDividendsService(repo, logger, cfg)

	// Initialize handlers
	httpHandlers := handlers.NewDividendsHandlers(dividendsSvc, logger)

	// Initialize authentication middleware
	authMiddleware := server.NewAuthMiddleware(logger, cfg.JWT.Secret)
	securityHandler := server.NewSecurityAdapter(authMiddleware)

	// Initialize HTTP server with security handler
	apiServer, err := api.NewServer(httpHandlers, securityHandler)
	if err != nil {
		logger.Fatal("Failed to create API server", zap.Error(err))
	}

	// Create HTTP server
	srv := &http.Server{
		Addr:    fmt.Sprintf(":%d", cfg.Server.Port),
		Handler: apiServer,
	}

	// Start server in goroutine
	go func() {
		logger.Info("Starting HTTP server", zap.String("address", srv.Addr))
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Failed to start HTTP server", zap.Error(err))
		}
	}()

	logger.Info("Stock dividends service initialized successfully",
		zap.Int("port", cfg.Server.Port),
		zap.String("database", cfg.Database.DBName))

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