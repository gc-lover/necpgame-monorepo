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
	"github.com/go-chi/cors"
	"go.uber.org/zap"

	"necpgame/services/webrtc-signaling-service-go/internal/config"
	"necpgame/services/webrtc-signaling-service-go/internal/handlers"
	"necpgame/services/webrtc-signaling-service-go/internal/repository"
	"necpgame/services/webrtc-signaling-service-go/internal/service"
)

func main() {
	// Initialize structured logger
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

	// Initialize database connection
	db, err := repository.NewPostgresConnection(cfg.DatabaseURL)
	if err != nil {
		logger.Fatal("Failed to connect to database", zap.Error(err))
	}
	defer db.Close()

	// Initialize Redis connection
	redisClient, err := repository.NewRedisConnection(cfg.RedisURL)
	if err != nil {
		logger.Fatal("Failed to connect to Redis", zap.Error(err))
	}
	defer redisClient.Close()

	// Initialize repository layer
	repo := repository.NewRepository(db, redisClient, logger)

	// Initialize guild service client
	guildClient := service.NewGuildClient(cfg.GuildServiceURL, logger)

	// Initialize service layer
	svc := service.NewService(repo, guildClient, logger)

	// Initialize handlers
	h := handlers.NewHandlers(svc, logger)

	// Setup HTTP server
	r := setupRouter(h)

	srv := &http.Server{
		Addr:         cfg.ServerAddr,
		Handler:      r,
		ReadTimeout:  30 * time.Second,
		WriteTimeout: 30 * time.Second,
		IdleTimeout:  120 * time.Second,
	}

	// Start server in goroutine
	go func() {
		logger.Info("Starting WebRTC Signaling Service",
			zap.String("addr", cfg.ServerAddr),
			zap.String("version", "1.0.0"))
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
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		logger.Fatal("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Server exited")
}

func setupRouter(h *handlers.Handlers) *chi.Mux {
	r := chi.NewRouter()

	// Middleware stack
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(60 * time.Second))

	// CORS middleware for WebRTC clients
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"}, // Configure for production
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: true,
		MaxAge:           300,
	}))

	// Health check endpoints
	r.Get("/health", h.HealthCheck)
	r.Post("/health/batch", h.BatchHealthCheck)
	r.Get("/health/ws", h.HealthWebSocket)

	// Metrics endpoint
	r.Handle("/metrics", h.MetricsHandler())

	// API routes
	r.Route("/api/v1", func(r chi.Router) {
		// Voice channels management
		r.Get("/voice-channels", h.ListVoiceChannels)
		r.Post("/voice-channels", h.CreateVoiceChannel)
		r.Get("/voice-channels/{channel_id}", h.GetVoiceChannel)
		r.Put("/voice-channels/{channel_id}", h.UpdateVoiceChannel)
		r.Delete("/voice-channels/{channel_id}", h.DeleteVoiceChannel)

		// Voice channel operations
		r.Post("/voice-channels/{channel_id}/join", h.JoinVoiceChannel)
		r.Post("/voice-channels/{channel_id}/signal", h.ExchangeSignalingMessage)
		r.Post("/voice-channels/{channel_id}/leave", h.LeaveVoiceChannel)

		// Guild voice channels management
		r.Get("/guilds/{guild_id}/voice-channels", h.ListGuildVoiceChannels)
		r.Post("/guilds/{guild_id}/voice-channels", h.CreateGuildVoiceChannel)
		r.Put("/guilds/{guild_id}/voice-channels/{channel_id}", h.UpdateGuildVoiceChannel)
		r.Post("/guilds/{guild_id}/voice-channels/{channel_id}/join", h.JoinGuildVoiceChannel)

		// Voice quality monitoring
		r.Post("/voice-quality/{channel_id}/report", h.ReportVoiceQuality)
	})

	return r
}

// PERFORMANCE: Main function optimized for fast startup
// Graceful shutdown ensures all connections are properly closed
// Structured logging provides comprehensive observability