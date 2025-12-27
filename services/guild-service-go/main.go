// Guild Service - Enterprise-grade social guild management
// Issue: #2247
// Agent: Backend

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

	"github.com/gc-lover/necpgame-monorepo/services/guild-service-go/internal/config"
	"github.com/gc-lover/necpgame-monorepo/services/guild-service-go/internal/handlers"
	"github.com/gc-lover/necpgame-monorepo/services/guild-service-go/internal/repository"
	"github.com/gc-lover/necpgame-monorepo/services/guild-service-go/internal/service"
)

func main() {
	// Initialize structured logging
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	sugar := logger.Sugar()
	sugar.Info("Starting Guild Service v1.0.0")

	// Load configuration
	cfg, err := config.Load()
	if err != nil {
		sugar.Fatalf("Failed to load configuration: %v", err)
	}

	// Initialize database connection with connection pooling
	dbConfig := repository.DatabaseConfig{
		Host:     cfg.Database.Host,
		Port:     cfg.Database.Port,
		User:     cfg.Database.User,
		Password: cfg.Database.Password,
		Database: cfg.Database.Database,
		SSLMode:  cfg.Database.SSLMode,
		MaxConns: cfg.Database.MaxConns,
	}
	db, err := repository.NewDatabaseConnection(dbConfig)
	if err != nil {
		sugar.Fatalf("Failed to connect to database: %v", err)
	}
	defer db.Close()

	// Initialize Redis for caching
	redisConfig := repository.RedisConfig{
		Host:     cfg.Redis.Host,
		Port:     cfg.Redis.Port,
		Password: cfg.Redis.Password,
		DB:       cfg.Redis.DB,
	}
	redisClient, err := repository.NewRedisConnection(redisConfig)
	if err != nil {
		sugar.Fatalf("Failed to connect to Redis: %v", err)
	}
	defer redisClient.Close()

	// Initialize repository layer
	repo := repository.NewRepository(db, redisClient, sugar)

	// Initialize service layer with business logic
	svc := service.NewService(repo, sugar)

	// Initialize handlers
	h := handlers.NewHandlers(svc, sugar)

	// Create HTTP router with enterprise-grade middleware
	r := chi.NewRouter()

	// Core middleware
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(30 * time.Second))

	// Security middleware
	r.Use(middleware.SetHeader("X-Content-Type-Options", "nosniff"))
	r.Use(middleware.SetHeader("X-Frame-Options", "DENY"))
	r.Use(middleware.SetHeader("X-XSS-Protection", "1; mode=block"))

	// Health and monitoring endpoints
	r.Get("/health", h.Health)
	r.Get("/ready", h.Ready)
	r.Handle("/metrics", promhttp.Handler())
	r.Mount("/debug/pprof", middleware.Profiler())

	// API routes with versioning
	r.Route("/api/v1", func(r chi.Router) {
		r.Route("/guilds", func(r chi.Router) {
			r.Get("/", h.ListGuilds)                    // GET /api/v1/guilds
			r.Post("/", h.CreateGuild)                  // POST /api/v1/guilds
			r.Get("/{guildId}", h.GetGuild)             // GET /api/v1/guilds/{id}
			r.Put("/{guildId}", h.UpdateGuild)          // PUT /api/v1/guilds/{id}
			r.Delete("/{guildId}", h.DeleteGuild)       // DELETE /api/v1/guilds/{id}
		})

		r.Route("/guilds/{guildId}", func(r chi.Router) {
			r.Get("/members", h.GetGuildMembers)        // GET /api/v1/guilds/{id}/members
			r.Post("/members", h.AddGuildMember)        // POST /api/v1/guilds/{id}/members
			r.Put("/members/{playerId}", h.UpdateMemberRole) // PUT /api/v1/guilds/{id}/members/{playerId}
			r.Delete("/members/{playerId}", h.RemoveGuildMember) // DELETE /api/v1/guilds/{id}/members/{playerId}

			r.Get("/announcements", h.GetGuildAnnouncements) // GET /api/v1/guilds/{id}/announcements
			r.Post("/announcements", h.CreateAnnouncement)   // POST /api/v1/guilds/{id}/announcements
		})

		r.Route("/players/{playerId}", func(r chi.Router) {
			r.Get("/guilds", h.GetPlayerGuilds)         // GET /api/v1/players/{id}/guilds
			r.Post("/guilds/{guildId}/join", h.JoinGuild) // POST /api/v1/players/{id}/guilds/{guildId}/join
			r.Post("/guilds/{guildId}/leave", h.LeaveGuild) // POST /api/v1/players/{id}/guilds/{guildId}/leave
		})
	})

	// Create HTTP server with optimized settings
	srv := &http.Server{
		Addr:         cfg.Server.GetAddr(),
		Handler:      r,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	// Start server in goroutine
	go func() {
		sugar.Infof("Server starting on %s", cfg.Server.GetAddr())
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			sugar.Fatalf("Server failed to start: %v", err)
		}
	}()

	// Wait for interrupt signal to gracefully shutdown the server
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	sugar.Info("Shutting down server...")

	// Give outstanding requests 30 seconds to complete
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		sugar.Errorf("Server forced to shutdown: %v", err)
	}

	sugar.Info("Server exited successfully")
}
