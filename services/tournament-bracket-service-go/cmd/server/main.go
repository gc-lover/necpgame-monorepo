// Tournament Bracket Service - Enterprise-grade tournament management
// Issue: #2210
// Agent: Backend Agent
package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"go.uber.org/zap"

	"necpgame/services/tournament-bracket-service-go/internal/config"
	"necpgame/services/tournament-bracket-service-go/internal/handlers"
	"necpgame/services/tournament-bracket-service-go/internal/repository"
	"necpgame/services/tournament-bracket-service-go/internal/service"
)

// Service represents the tournament bracket service
type Service struct {
	config   *config.Config
	logger   *zap.Logger
	repo     *repository.Repository
	svc      *service.Service
	handlers *handlers.Handlers
	server   *http.Server
}

// NewService creates a new tournament bracket service instance
func NewService() (*Service, error) {
	// Load configuration
	cfg, err := config.Load()
	if err != nil {
		return nil, err
	}

	// Initialize logger
	logger, err := zap.NewProduction()
	if err != nil {
		return nil, err
	}

	// Initialize repository
	repo, err := repository.NewRepository(cfg.DatabaseURL)
	if err != nil {
		return nil, err
	}

	// Initialize service
	svc := service.NewService(repo, logger)

	// Initialize handlers
	h := handlers.NewHandlers(svc, logger)

	// Create HTTP server
	mux := http.NewServeMux()

	// Health endpoints
	mux.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"status":"healthy","timestamp":"` + time.Now().UTC().Format(time.RFC3339) + `"}`))
	})

	mux.HandleFunc("/ready", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"status":"ready","timestamp":"` + time.Now().UTC().Format(time.RFC3339) + `"}`))
	})

	// API routes
	mux.HandleFunc("/api/v1/brackets", h.HandleBrackets)
	mux.HandleFunc("/api/v1/brackets/", h.HandleBracketByID)
	mux.HandleFunc("/api/v1/brackets/", h.HandleBracketOperations)
	mux.HandleFunc("/api/v1/rounds", h.HandleRounds)
	mux.HandleFunc("/api/v1/rounds/", h.HandleRoundByID)
	mux.HandleFunc("/api/v1/matches", h.HandleMatches)
	mux.HandleFunc("/api/v1/matches/", h.HandleMatchByID)
	mux.HandleFunc("/api/v1/matches/", h.HandleMatchOperations)
	mux.HandleFunc("/api/v1/participants", h.HandleParticipants)
	mux.HandleFunc("/api/v1/participants/", h.HandleParticipantByID)

	// WebSocket for real-time updates
	mux.HandleFunc("/ws/brackets/", h.HandleBracketWebSocket)

	server := &http.Server{
		Addr:         cfg.ServerAddr,
		Handler:      mux,
		ReadTimeout:  30 * time.Second,
		WriteTimeout: 30 * time.Second,
	}

	return &Service{
		config:   cfg,
		logger:   logger,
		repo:     repo,
		svc:      svc,
		handlers: h,
		server:   server,
	}, nil
}

// Start begins the service operation
func (s *Service) Start(ctx context.Context) error {
	s.logger.Info("Starting Tournament Bracket Service",
		zap.String("version", "1.0.0"),
		zap.String("addr", s.config.ServerAddr))

	// Start HTTP server in background
	go func() {
		s.logger.Info("HTTP server starting", zap.String("addr", s.server.Addr))
		if err := s.server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			s.logger.Fatal("HTTP server failed", zap.Error(err))
		}
	}()

	return nil
}

// Stop gracefully shuts down the service
func (s *Service) Stop(ctx context.Context) error {
	s.logger.Info("Stopping Tournament Bracket Service")

	// Shutdown HTTP server
	shutdownCtx, cancel := context.WithTimeout(ctx, 30*time.Second)
	defer cancel()

	if err := s.server.Shutdown(shutdownCtx); err != nil {
		s.logger.Error("HTTP server shutdown failed", zap.Error(err))
	}

	// Close repository connection
	if err := s.repo.Close(); err != nil {
		s.logger.Error("Repository close failed", zap.Error(err))
	}

	s.logger.Info("Tournament Bracket Service stopped")
	return nil
}

func main() {
	// Create service
	service, err := NewService()
	if err != nil {
		log.Fatal("Failed to create service", err)
	}
	defer service.logger.Sync()

	// Setup signal handling for graceful shutdown
	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()

	sigChan := make(chan os.Signal, 1)
	signal.Notify(sigChan, syscall.SIGINT, syscall.SIGTERM)

	// Start service
	if err := service.Start(ctx); err != nil {
		service.logger.Fatal("Failed to start service", zap.Error(err))
	}

	// Wait for shutdown signal
	<-sigChan
	service.logger.Info("Shutdown signal received")

	// Graceful shutdown
	shutdownCtx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := service.Stop(shutdownCtx); err != nil {
		service.logger.Error("Service shutdown failed", zap.Error(err))
		os.Exit(1)
	}
}