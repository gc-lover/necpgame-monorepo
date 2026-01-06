package main

import (
	"context"
	"flag"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"go.uber.org/zap"

	api "necpgame/services/world-event-service-go/api"
	"necpgame/services/world-event-service-go/internal/repository"
	"necpgame/services/world-event-service-go/internal/service"
)

func main() {
	logger, _ := zap.NewProduction()
	defer logger.Sync()

	addr := flag.String("addr", ":8080", "listen address")
	dsn := flag.String("dsn", os.Getenv("DATABASE_URL"), "database DSN")
	flag.Parse()

	ctx, cancel := signal.NotifyContext(context.Background(), syscall.SIGINT, syscall.SIGTERM)
	defer cancel()

	var repo *repository.Repository
	if *dsn != "" {
		var err error
		repo, err = repository.NewRepository(ctx, logger, *dsn)
		if err != nil {
			logger.Fatal("Failed to initialize repository", zap.Error(err))
		}
		defer repo.Close()
	}

	// Initialize service handler
	h := service.NewHandler(logger, repo)

	// Initialize security handler (mock for now)
	sec := service.NewSecurityHandler()

	// Create server
	srv, err := api.NewServer(h, sec)
	if err != nil {
		logger.Fatal("Failed to create server", zap.Error(err))
	}

	httpSrv := &http.Server{
		Addr:    *addr,
		Handler: srv,
		// PERFORMANCE: Basic timeouts
		ReadTimeout:  10 * time.Second,
		WriteTimeout: 10 * time.Second,
	}

	// Graceful shutdown
	go func() {
		<-ctx.Done()
		logger.Info("Shutting down server...")
		shutdownCtx, shutdownCancel := context.WithTimeout(context.Background(), 10*time.Second)
		defer shutdownCancel()

		if err := httpSrv.Shutdown(shutdownCtx); err != nil {
			logger.Error("Failed to shutdown server", zap.Error(err))
		}
	}()

	logger.Info("Starting world-event-service", zap.String("addr", *addr))
	if err := httpSrv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
		logger.Fatal("Server failed", zap.Error(err))
	}
}
