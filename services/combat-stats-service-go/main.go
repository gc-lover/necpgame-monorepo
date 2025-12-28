// Issue: #2250
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

	"combat-stats-service-go/internal/config"
	"combat-stats-service-go/internal/handlers"
	"combat-stats-service-go/internal/service"
	"combat-stats-service-go/internal/repository"
	"combat-stats-service-go/internal/metrics"

	// Import enhanced error handling and logging
	errorhandling "github.com/your-org/necpgame/scripts/core/error-handling"
)

func main() {
	// Optimize GC for high-throughput stats processing
	if os.Getenv("GOGC") == "" {
		os.Setenv("GOGC", "75") // Higher threshold for stats workloads
	}

	// Initialize enhanced structured logger
	loggerConfig := &errorhandling.LoggerConfig{
		ServiceName: "combat-stats-service",
		Level:       zap.InfoLevel,
		Development: os.Getenv("ENV") == "development",
		AddCaller:   true,
	}

	logger, err := errorhandling.NewLogger(loggerConfig)
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

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
	repo := repository.NewCombatStatsRepository(db, redisClient, sugar)

	// Initialize service layer
	combatStatsService := service.NewCombatStatsService(repo, metricsCollector, sugar)

	// Initialize handlers
	combatStatsHandlers := handlers.NewCombatStatsHandlers(combatStatsService, sugar)

	// Setup HTTP server
	r := setupRouter(combatStatsHandlers, metricsCollector, sugar)

	srv := &http.Server{
		Addr:         fmt.Sprintf(":%d", cfg.Port),
		Handler:      r,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	// Start server in goroutine
	go func() {
		sugar.Infof("Starting Combat Stats Service on port %d (GOGC=%s)", cfg.Port, os.Getenv("GOGC"))
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

func setupRouter(handlers *handlers.CombatStatsHandlers, metrics *metrics.Collector, logger *zap.SugaredLogger) *chi.Mux {
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
	r.Route("/api/v1/combat-stats", func(r chi.Router) {
		r.Use(handlers.AuthMiddleware)

		// Combat statistics
		r.Get("/player/{playerId}", handlers.GetPlayerStats)
		r.Get("/weapon/{weaponId}", handlers.GetWeaponStats)
		r.Get("/match/{matchId}", handlers.GetMatchStats)
		r.Post("/events", handlers.RecordCombatEvent)

		// Leaderboards
		r.Get("/leaderboards/kills", handlers.GetKillLeaderboard)
		r.Get("/leaderboards/score", handlers.GetScoreLeaderboard)
		r.Get("/leaderboards/weapon/{weaponId}", handlers.GetWeaponLeaderboard)

		// Analytics
		r.Get("/analytics/damage", handlers.GetDamageAnalytics)
		r.Get("/analytics/kill-death", handlers.GetKillDeathAnalytics)
		r.Get("/analytics/playtime", handlers.GetPlaytimeAnalytics)

		// Performance metrics
		r.Get("/performance/player/{playerId}", handlers.GetPlayerPerformance)
		r.Get("/performance/weapon/{weaponId}", handlers.GetWeaponPerformance)
		r.Get("/performance/match/{matchId}", handlers.GetMatchPerformance)
	})

	return r
}