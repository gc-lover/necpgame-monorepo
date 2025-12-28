// Issue: #2213 - Tournament Spectator Mode Implementation
// Main entry point for Tournament Spectator Service - Enterprise-grade spectator system

package main

import (
	"context"
	"fmt"
	"log"
	"net/http"
	"os"
	"os/signal"
	"runtime"
	"syscall"
	"time"

	_ "net/http/pprof" // PERFORMANCE: Enable pprof profiling

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"go.uber.org/zap"

	"tournament-spectator-service-go/internal/config"
	"tournament-spectator-service-go/internal/handlers"
	"tournament-spectator-service-go/internal/repository"
	"tournament-spectator-service-go/internal/service"
)

func main() {
	// PERFORMANCE: Optimize GC for high-throughput spectator operations
	if os.Getenv("GOGC") == "" {
		os.Setenv("GOGC", "75") // Higher threshold for spectator-heavy workloads
	}

	// Initialize structured logger
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	sugar := logger.Sugar()

	// Load configuration
	cfg, err := config.Load()
	if err != nil {
		sugar.Fatalf("Failed to load configuration: %v", err)
	}

	// Initialize database connection
	db, err := repository.NewConnection(cfg.DatabaseURL)
	if err != nil {
		sugar.Fatalf("Failed to connect to database: %v", err)
	}
	defer db.Close()

	// Initialize repository
	repo := repository.NewRepository(db)

	// Initialize service
	svc := service.NewSpectatorService(repo, sugar)

	// Initialize handlers
	h := handlers.NewSpectatorHandlers(svc, sugar)

	// Setup router
	r := setupRouter(h, sugar)

	// Configure HTTP server with optimized settings
	httpServer := &http.Server{
		Addr:         fmt.Sprintf(":%d", cfg.ServerPort),
		Handler:      r,
		ReadTimeout:  30 * time.Second, // PERFORMANCE: Extended for spectator streaming
		WriteTimeout: 30 * time.Second, // PERFORMANCE: Extended for live updates
		IdleTimeout:  120 * time.Second, // PERFORMANCE: Longer idle for spectator sessions
	}

	// Graceful shutdown setup
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)

	// Start server in goroutine
	serverErr := make(chan error, 1)
	go func() {
		sugar.Infof("Starting Tournament Spectator Service on :%d", cfg.ServerPort)
		if err := httpServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			serverErr <- err
		}
	}()

	// Wait for shutdown signal or server error
	select {
	case err := <-serverErr:
		sugar.Fatalf("HTTP server error: %v", err)
	case sig := <-quit:
		sugar.Infof("Received signal %v, shutting down server...", sig)
	}

	// Graceful shutdown with timeout
	shutdownCtx, shutdownCancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer shutdownCancel()

	if err := httpServer.Shutdown(shutdownCtx); err != nil {
		sugar.Errorf("Server forced to shutdown: %v", err)
	}

	// Force GC before exit
	runtime.GC()
	sugar.Info("Server exited cleanly")
}

func setupRouter(h *handlers.SpectatorHandlers, logger *zap.SugaredLogger) *chi.Mux {
	r := chi.NewRouter()

	// PERFORMANCE: Optimized middleware stack for spectator traffic
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(30 * time.Second))

	// CORS configuration for web spectator clients
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"}, // Configure for production
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300,
	}))

	// Health endpoint
	r.Get("/health", h.HealthCheck)

	// Tournament spectator endpoints
	r.Get("/api/v1/tournaments/{tournamentId}/spectate", h.GetTournamentSpectatorView)
	r.Get("/api/v1/matches/{matchId}/spectate", h.GetMatchSpectatorView)
	r.Get("/api/v1/spectators/live", h.GetLiveSpectatorMatches)
	r.Get("/api/v1/spectators/popular", h.GetPopularSpectatorMatches)
	r.Get("/api/v1/spectators/stats", h.GetSpectatorStatistics)

	// WebSocket endpoint for real-time spectator updates
	r.Get("/ws/spectate/{matchId}", h.SpectatorWebSocket)

	// Admin endpoints
	r.Post("/api/v1/admin/spectator-mode/enable", h.EnableSpectatorMode)
	r.Post("/api/v1/admin/spectator-mode/disable", h.DisableSpectatorMode)

	// Metrics endpoint
	r.Handle("/metrics", promhttp.Handler())

	// PERFORMANCE: Profiling endpoints for performance monitoring
	r.Mount("/debug", http.DefaultServeMux)

	return r
}
