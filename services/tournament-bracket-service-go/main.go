// Issue: #2210
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

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"go.uber.org/zap"

	"tournament-bracket-service-go/internal/config"
	"tournament-bracket-service-go/internal/handlers"
	"tournament-bracket-service-go/internal/service"
	"tournament-bracket-service-go/internal/repository"
	"tournament-bracket-service-go/internal/metrics"
)

func main() {
	// Optimize GC for tournament bracket processing
	if os.Getenv("GOGC") == "" {
		os.Setenv("GOGC", "100") // Higher threshold for tournament workloads
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

	// Initialize Redis connection
	redisClient, err := repository.NewRedisClient(cfg.RedisURL)
	if err != nil {
		sugar.Fatalf("Failed to connect to Redis: %v", err)
	}
	defer redisClient.Close()

	// Initialize metrics
	metricsCollector := metrics.NewCollector()

	// Initialize repository layer
	repo := repository.NewTournamentRepository(db, redisClient, sugar)

	// Initialize service layer
	tournamentService := service.NewTournamentService(repo, metricsCollector, sugar)

	// Initialize handlers
	tournamentHandlers := handlers.NewTournamentHandlers(tournamentService, sugar)

	// Setup HTTP server
	r := setupRouter(tournamentHandlers, metricsCollector, sugar)

	srv := &http.Server{
		Addr:         fmt.Sprintf(":%d", cfg.Port),
		Handler:      r,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	// Start server in goroutine
	go func() {
		sugar.Infof("Starting Tournament Bracket Service on port %d (GOGC=%s)", cfg.Port, os.Getenv("GOGC"))
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			sugar.Fatalf("Server failed to start: %v", err)
		}
	}()

	// Setup graceful shutdown
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	sugar.Info("Shutting down server...")

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		sugar.Errorf("Server forced to shutdown: %v", err)
	}

	runtime.GC()
	sugar.Info("Server exited gracefully")
}

func setupRouter(handlers *handlers.TournamentHandlers, metrics *metrics.Collector, logger *zap.SugaredLogger) *chi.Mux {
	r := chi.NewRouter()

	// Middleware
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(30 * time.Second))

	// CORS
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"},
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300,
	}))

	// Health and metrics
	r.Get("/health", handlers.Health)
	r.Get("/ready", handlers.Ready)
	r.Handle("/metrics", promhttp.Handler())

	// API routes
	r.Route("/api/v1/tournament", func(r chi.Router) {
		r.Use(handlers.AuthMiddleware)

		// Tournaments
		r.Get("/tournaments", handlers.GetTournaments)
		r.Get("/tournaments/{tournamentId}", handlers.GetTournament)
		r.Post("/tournaments", handlers.CreateTournament)
		r.Put("/tournaments/{tournamentId}", handlers.UpdateTournament)
		r.Post("/tournaments/{tournamentId}/register", handlers.RegisterForTournament)
		r.Post("/tournaments/{tournamentId}/unregister", handlers.UnregisterFromTournament)

		// Brackets
		r.Get("/tournaments/{tournamentId}/brackets", handlers.GetTournamentBrackets)
		r.Get("/brackets/{bracketId}", handlers.GetBracket)
		r.Get("/brackets/{bracketId}/matches", handlers.GetBracketMatches)

		// Matches
		r.Get("/matches/{matchId}", handlers.GetMatch)
		r.Put("/matches/{matchId}/result", handlers.UpdateMatchResult)
		r.Get("/matches/{matchId}/spectators", handlers.GetMatchSpectators)
		r.Post("/matches/{matchId}/spectate", handlers.JoinSpectator)

		// Participants
		r.Get("/tournaments/{tournamentId}/participants", handlers.GetTournamentParticipants)
		r.Get("/participants/{participantId}", handlers.GetParticipant)

		// Results
		r.Get("/tournaments/{tournamentId}/results", handlers.GetTournamentResults)
		r.Get("/tournaments/{tournamentId}/leaderboard", handlers.GetTournamentLeaderboard)

		// Live updates
		r.Get("/live/tournaments", handlers.GetLiveTournaments)
		r.Get("/live/matches", handlers.GetLiveMatches)
		r.Get("/live/results/{tournamentId}", handlers.GetLiveResults)

		// Spectator mode - NEW
		r.Post("/matches/{match_id}/spectator/join", handlers.JoinSpectatorMode)
		r.Post("/spectators/{spectator_id}/leave", handlers.LeaveSpectatorMode)
		r.Put("/spectators/{spectator_id}/view", handlers.UpdateSpectatorView)
		r.Get("/matches/{match_id}/spectators", handlers.GetMatchSpectators)
		r.Get("/tournaments/{tournament_id}/spectator-stats", handlers.GetSpectatorStats)
	})

	return r
}
