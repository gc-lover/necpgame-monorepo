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

	"go.uber.org/zap"

	"necpgame/services/tournament-service-go/internal/config"
	"necpgame/services/tournament-service-go/internal/database"
	"necpgame/services/tournament-service-go/internal/handlers"
	tournamentredis "necpgame/services/tournament-service-go/internal/redis"
	"necpgame/services/tournament-service-go/internal/service"
	api "necpgame/services/tournament-service-go/pkg/api"
)

func main() {
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to create logger: %v", err)
	}
	defer logger.Sync()

	fmt.Println("Tournament Service Starting...")

	// Load configuration
	cfg, err := config.Load()
	if err != nil {
		logger.Fatal("Failed to load configuration", zap.Error(err))
	}

	if err := cfg.Validate(); err != nil {
		logger.Fatal("Invalid configuration", zap.Error(err))
	}

	// Initialize database manager with connection pooling
	dbManager, err := database.NewManager(&cfg.Database, logger)
	if err != nil {
		logger.Fatal("Failed to initialize database", zap.Error(err))
	}
	defer dbManager.Close()

	// Initialize Redis manager with optimized connection pooling
	redisManager, err := tournamentredis.NewManager(&cfg.Redis, logger)
	if err != nil {
		logger.Fatal("Failed to initialize Redis", zap.Error(err))
	}
	defer redisManager.Close()

	// Initialize tournament manager
	tm := service.NewTournamentManager(logger)

	// Initialize service with enterprise-grade components
	tournamentSvc := service.NewService(dbManager.GetDB(), redisManager.Client(), tm, logger)

	// Create HTTP handlers with ogen-generated interfaces
	httpHandlers := handlers.NewHandler(tournamentSvc, logger)

	// Create ogen server
	server, err := api.NewServer(httpHandlers, nil)
	if err != nil {
		logger.Fatal("Failed to create server", zap.Error(err))
	}

	// Configure HTTP server with enterprise-grade timeouts
	httpServer := &http.Server{
		Addr:         ":" + cfg.Server.Port,
		Handler:      server,
		ReadTimeout:  cfg.Server.ReadTimeout,
		WriteTimeout: cfg.Server.WriteTimeout,
		IdleTimeout:  cfg.Server.IdleTimeout,
	}

	// Start server in goroutine
	go func() {
		fmt.Printf("Tournament Service listening on port %s\n", cfg.Server.Port)
		if err := httpServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Server failed to start", zap.Error(err))
		}
	}()

	// Wait for interrupt signal to gracefully shutdown the server
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	fmt.Println("Shutting down Tournament Service...")

	// Graceful shutdown with timeout
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpServer.Shutdown(ctx); err != nil {
		logger.Fatal("Server forced to shutdown", zap.Error(err))
	}

	fmt.Println("Tournament Service stopped")
}