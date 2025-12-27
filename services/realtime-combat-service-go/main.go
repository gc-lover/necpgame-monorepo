// Issue: #2232
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

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"go.uber.org/zap"

	"realtime-combat-service-go/internal/config"
	"realtime-combat-service-go/internal/handlers"
	"realtime-combat-service-go/internal/service"
	"realtime-combat-service-go/internal/repository"
	"realtime-combat-service-go/internal/metrics"
)

func main() {
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
	repo := repository.NewCombatRepository(db, redisClient, sugar)

	// Initialize service layer
	combatService := service.NewCombatService(repo, metricsCollector, sugar)

	// Initialize handlers
	combatHandlers := handlers.NewCombatHandlers(combatService, sugar)

	// Setup HTTP server
	r := setupRouter(combatHandlers, metricsCollector, sugar)

	srv := &http.Server{
		Addr:         fmt.Sprintf(":%d", cfg.Port),
		Handler:      r,
		ReadTimeout:  30 * time.Second,
		WriteTimeout: 30 * time.Second,
		IdleTimeout:  120 * time.Second,
	}

	// Start server in goroutine
	go func() {
		sugar.Infof("Starting Real-time Combat Service on port %d", cfg.Port)
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

	sugar.Info("Server exited")
}

func setupRouter(handlers *handlers.CombatHandlers, metrics *metrics.Collector, logger *zap.SugaredLogger) *chi.Mux {
	r := chi.NewRouter()

	// Middleware
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(60 * time.Second))

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
	r.Route("/api/v1/combat", func(r chi.Router) {
		r.Use(handlers.AuthMiddleware)

		// Combat sessions management
		r.Get("/sessions", handlers.ListCombatSessions)
		r.Post("/sessions", handlers.CreateCombatSession)
		r.Get("/sessions/{sessionId}", handlers.GetCombatSession)
		r.Put("/sessions/{sessionId}", handlers.UpdateCombatSession)
		r.Delete("/sessions/{sessionId}", handlers.EndCombatSession)

		// Session participation
		r.Post("/sessions/{sessionId}/join", handlers.JoinCombatSession)
		r.Post("/sessions/{sessionId}/leave", handlers.LeaveCombatSession)

		// Real-time combat actions
		r.Post("/sessions/{sessionId}/damage", handlers.ApplyDamage)
		r.Post("/sessions/{sessionId}/actions", handlers.ExecuteAction)
		r.Post("/sessions/{sessionId}/spectate", handlers.StartSpectating)

		// Position and state sync
		r.Get("/sessions/{sessionId}/state", handlers.GetCombatState)
		r.Post("/sessions/{sessionId}/position", handlers.UpdatePosition)
		r.Get("/sessions/{sessionId}/replay", handlers.GetCombatReplay)

		// Combat statistics
		r.Get("/sessions/{sessionId}/stats", handlers.GetCombatStats)
		r.Get("/players/{playerId}/stats", handlers.GetPlayerCombatStats)
	})

	return r
}
