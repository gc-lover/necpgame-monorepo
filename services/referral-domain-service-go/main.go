// Issue: Referral Domain Service Implementation
// PERFORMANCE: Optimized for referral system with high-throughput code validation
// BACKEND: Enterprise-grade referral domain service for NECPGAME

package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"go.uber.org/zap"

	"services/referral-domain-service-go/internal/config"
	"services/referral-domain-service-go/internal/handlers"
	"services/referral-domain-service-go/internal/repository"
	"services/referral-domain-service-go/internal/service"
)

func main() {
	// Initialize structured logger
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	// Load configuration with MMOFPS optimizations
	cfg, err := config.Load()
	if err != nil {
		logger.Fatal("Failed to load configuration", zap.Error(err))
	}

	// Initialize database connection with MMOFPS optimizations
	db, err := repository.NewDBConnection(cfg.DatabaseURL, cfg)
	if err != nil {
		logger.Fatal("Failed to connect to database", zap.Error(err))
	}
	defer db.Close()

	// Initialize Redis client with MMOFPS optimizations
	redisClient, err := repository.NewRedisClient(cfg.RedisURL, cfg)
	if err != nil {
		logger.Fatal("Failed to connect to Redis", zap.Error(err))
	}
	defer redisClient.Close()

	// Initialize repository layer
	repo := repository.NewRepository(db, redisClient, logger)

	// Initialize service layer
	svc := service.NewService(repo, logger)

	// Initialize handlers with MMOFPS optimizations
	h := handlers.NewHandler(svc, logger, cfg)

	// Setup HTTP server with MMOFPS optimizations
	r := setupRouter(h, cfg)

	srv := &http.Server{
		Addr:         cfg.ServerAddr,
		Handler:      r,
		ReadTimeout:  cfg.RequestTimeout,
		WriteTimeout: cfg.RequestTimeout,
		IdleTimeout:  120 * time.Second,
	}

	// Start server in goroutine
	go func() {
		logger.Info("Starting Referral Domain Service",
			zap.String("addr", cfg.ServerAddr))
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Failed to start server", zap.Error(err))
		}
	}()

	// Wait for interrupt signal to gracefully shutdown
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	logger.Info("Shutting down server...")

	// Graceful shutdown with timeout
	ctx, cancel := context.WithTimeout(context.Background(), cfg.ShutdownTimeout)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		logger.Fatal("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Server exited")
}

func setupRouter(h *handlers.Handler, cfg *config.Config) *chi.Mux {
	r := chi.NewRouter()

	// Middleware with MMOFPS optimizations
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(cfg.RequestTimeout))

	// Health check endpoint
	r.Get("/health", h.HealthCheck)

	// Metrics endpoint
	r.Handle("/metrics", promhttp.Handler())

	// API routes
	r.Route("/api/v1", func(r chi.Router) {
		r.Use(h.AuthMiddleware)

		// Referral codes routes
		r.Route("/referral-codes", func(r chi.Router) {
			r.Get("/", h.GetReferralCodes)
			r.Post("/", h.CreateReferralCode)
			r.Get("/{codeID}", h.GetReferralCode)
			r.Put("/{codeID}", h.UpdateReferralCode)
			r.Delete("/{codeID}", h.DeleteReferralCode)
			r.Post("/{codeID}/validate", h.ValidateReferralCode)
		})

		// Referral registration routes
		r.Route("/referral-registration", func(r chi.Router) {
			r.Get("/", h.GetReferralRegistrations)
			r.Post("/", h.CreateReferralRegistration)
			r.Get("/{registrationID}", h.GetReferralRegistration)
			r.Put("/{registrationID}", h.UpdateReferralRegistration)
		})

		// Referral milestones routes
		r.Route("/referral-milestones", func(r chi.Router) {
			r.Get("/", h.GetReferralMilestones)
			r.Post("/", h.CreateReferralMilestone)
			r.Get("/{milestoneID}", h.GetReferralMilestone)
			r.Put("/{milestoneID}", h.UpdateReferralMilestone)
		})

		// Referral rewards routes
		r.Route("/referral-rewards", func(r chi.Router) {
			r.Get("/", h.GetReferralRewards)
			r.Post("/", h.ClaimReferralReward)
			r.Get("/{rewardID}", h.GetReferralReward)
		})

		// Referral statistics routes
		r.Route("/referral-statistics", func(r chi.Router) {
			r.Get("/", h.GetReferralStatistics)
			r.Get("/leaderboard", h.GetReferralLeaderboard)
		})
	})

	return r
}
