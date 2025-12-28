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

	"services/mail-system-service-go/internal/config"
	"services/mail-system-service-go/internal/handlers"
	"services/mail-system-service-go/internal/repository"
	"services/mail-system-service-go/internal/service"
)

func main() {
	// Initialize logger
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
		Addr:         fmt.Sprintf(":%d", cfg.Port),
		Handler:      r,
		ReadTimeout:  30 * time.Second,
		WriteTimeout: 30 * time.Second,
		IdleTimeout:  120 * time.Second,
	}

	// Start server in a goroutine
	go func() {
		logger.Info("Starting mail system service", zap.Int("port", cfg.Port))
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Failed to start server", zap.Error(err))
		}
	}()

	// Wait for interrupt signal to gracefully shutdown the server
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("Shutting down server...")

	// Give outstanding requests 30 seconds to complete
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
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

		// Mail management routes
		r.Route("/mail", func(r chi.Router) {
			r.Get("/", h.GetMailbox)
			r.Post("/", h.SendMail)

			r.Route("/{mailID}", func(r chi.Router) {
				r.Get("/", h.GetMail)
				r.Delete("/", h.DeleteMail)
				r.Post("/read", h.MarkAsRead)
				r.Post("/archive", h.ArchiveMail)
				r.Post("/moderation/report", h.ReportMail)
			})

			// Bulk operations
			r.Post("/bulk", h.SendBulkMail)

			// Attachments
			r.Get("/attachments/{attachmentID}", h.DownloadAttachment)

			// Analytics
			r.Get("/analytics", h.GetMailAnalytics)

			// System announcements
			r.Post("/system/announcement", h.SendSystemAnnouncement)
		})
	})

	return r
}

