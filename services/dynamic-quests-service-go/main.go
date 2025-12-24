// Dynamic Quests Service - Backend Implementation
// Issue: #2244
// Agent: Backend

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
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"go.uber.org/zap"

	"necpgame/services/dynamic-quests-service-go/internal/config"
	"necpgame/services/dynamic-quests-service-go/internal/handlers"
	"necpgame/services/dynamic-quests-service-go/internal/repository"
	"necpgame/services/dynamic-quests-service-go/internal/service"
	"necpgame/services/dynamic-quests-service-go/pkg/database"
)

func main() {
	// Initialize structured logging
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	sugar := logger.Sugar()
	sugar.Info("Starting Dynamic Quests Service v1.0.0")

	// Load configuration
	cfg, err := config.Load()
	if err != nil {
		sugar.Fatalf("Failed to load configuration: %v", err)
	}

	// Initialize database connection with connection pooling
	db, err := database.NewConnection(cfg.Database)
	if err != nil {
		sugar.Fatalf("Failed to connect to database: %v", err)
	}
	defer db.Close()

	// Initialize repository layer
	repo := repository.NewRepository(db, sugar)

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
	r.Get("/debug/pprof/*", middleware.Profiler())

	// API routes with versioning
	r.Route("/api/v1", func(r chi.Router) {
		r.Route("/quests", func(r chi.Router) {
			r.Get("/", h.ListQuests)                    // GET /api/v1/quests
			r.Post("/", h.CreateQuest)                   // POST /api/v1/quests
			r.Get("/{questId}", h.GetQuest)              // GET /api/v1/quests/{id}
			r.Put("/{questId}", h.UpdateQuest)           // PUT /api/v1/quests/{id}
			r.Delete("/{questId}", h.DeleteQuest)        // DELETE /api/v1/quests/{id}
		})

		r.Route("/quests/{questId}", func(r chi.Router) {
			r.Post("/start", h.StartQuest)               // POST /api/v1/quests/{id}/start
			r.Get("/state", h.GetQuestState)             // GET /api/v1/quests/{id}/state
			r.Post("/choices", h.MakeChoice)             // POST /api/v1/quests/{id}/choices
			r.Post("/complete", h.CompleteQuest)         // POST /api/v1/quests/{id}/complete
		})

		r.Route("/players/{playerId}", func(r chi.Router) {
			r.Get("/quests", h.GetPlayerQuests)          // GET /api/v1/players/{id}/quests
			r.Get("/reputation", h.GetPlayerReputation)   // GET /api/v1/players/{id}/reputation
		})

		r.Route("/admin", func(r chi.Router) {
			r.Post("/import", h.ImportQuests)            // POST /api/v1/admin/import
			r.Post("/reset", h.ResetPlayerProgress)      // POST /api/v1/admin/reset
		})
	})

	// Create HTTP server with optimized settings
	srv := &http.Server{
		Addr:         fmt.Sprintf(":%d", cfg.Server.Port),
		Handler:      r,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	// Start server in goroutine
	go func() {
		sugar.Infof("Server starting on port %d", cfg.Server.Port)
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

