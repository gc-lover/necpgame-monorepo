// Issue: Social Domain Service Implementation
// PERFORMANCE: Optimized for social interactions with real-time messaging and caching
// BACKEND: Enterprise-grade social domain service for NECPGAME

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

	"services/social-domain-service-go/internal/config"
	"services/social-domain-service-go/internal/handlers"
	"services/social-domain-service-go/internal/repository"
	"services/social-domain-service-go/internal/service"
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
		logger.Info("Starting Social Domain Service",
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

		// Chat routes
		r.Route("/chat", func(r chi.Router) {
			r.Get("/channels", h.GetChatChannels)
			r.Post("/channels", h.CreateChatChannel)
			r.Get("/channels/{channelID}/messages", h.GetChannelMessages)
			r.Post("/channels/{channelID}/messages", h.SendMessage)
		})

		// Guild routes
		r.Route("/guilds", func(r chi.Router) {
			r.Get("/", h.GetGuilds)
			r.Post("/", h.CreateGuild)
			r.Get("/{guildID}", h.GetGuild)
			r.Put("/{guildID}", h.UpdateGuild)
			r.Post("/{guildID}/join", h.JoinGuild)
			r.Post("/{guildID}/leave", h.LeaveGuild)
		})

		// Party routes
		r.Route("/parties", func(r chi.Router) {
			r.Get("/", h.GetParties)
			r.Post("/", h.CreateParty)
			r.Get("/{partyID}", h.GetParty)
			r.Post("/{partyID}/join", h.JoinParty)
			r.Post("/{partyID}/leave", h.LeaveParty)
		})

		// Relationships routes
		r.Route("/relationships", func(r chi.Router) {
			r.Get("/", h.GetRelationships)
			r.Post("/", h.CreateRelationship)
			r.Get("/{relationshipID}", h.GetRelationship)
			r.Put("/{relationshipID}", h.UpdateRelationship)
		})

		// Orders routes
		r.Route("/orders", func(r chi.Router) {
			r.Get("/", h.GetOrders)
			r.Post("/", h.CreateOrder)
			r.Get("/{orderID}", h.GetOrder)
			r.Put("/{orderID}", h.UpdateOrder)
			r.Post("/{orderID}/accept", h.AcceptOrder)
		})

		// Mentorship routes
		r.Route("/mentorship", func(r chi.Router) {
			r.Get("/mentors", h.GetMentors)
			r.Post("/proposals", h.CreateMentorshipProposal)
			r.Get("/proposals", h.GetMentorshipProposals)
			r.Post("/proposals/{proposalID}/accept", h.AcceptMentorshipProposal)
		})

		// Reputation routes
		r.Route("/reputation", func(r chi.Router) {
			r.Get("/", h.GetPlayerReputation)
			r.Get("/leaderboard", h.GetReputationLeaderboard)
			r.Get("/benefits", h.GetReputationBenefits)
		})

		// Dynamic Relationships routes
		r.Route("/social", func(r chi.Router) {
			// Relationships
			r.Get("/relationships", h.GetRelationships)
			r.Post("/relationships", h.UpdateRelationship)
			r.Get("/relationships/{entity_id}/events", h.GetRelationshipEvents)

			// Reputation Network
			r.Get("/reputation/{entity_id}", h.GetReputation)
			r.Post("/reputation/events", h.RecordReputationEvent)

			// Social Network
			r.Get("/network/{player_id}/influence", h.CalculateSocialInfluence)
		})

		// Notifications routes
		r.Route("/notifications", func(r chi.Router) {
			r.Get("/", h.GetNotifications)
			r.Post("/{notificationID}/read", h.MarkNotificationRead)
			r.Put("/preferences", h.UpdateNotificationPreferences)
		})
	})

	return r
}
