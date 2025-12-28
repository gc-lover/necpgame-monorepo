// Issue: #2262 - Cyberspace Easter Eggs Backend Integration
// Main entry point for Cyberspace Easter Eggs Service - Enterprise-grade implementation

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

	"cyberspace-easter-eggs-service-go/internal/config"
	"cyberspace-easter-eggs-service-go/internal/generated"
	"cyberspace-easter-eggs-service-go/internal/handlers"
	"cyberspace-easter-eggs-service-go/internal/metrics"
	"cyberspace-easter-eggs-service-go/pkg/repository"
	"cyberspace-easter-eggs-service-go/internal/service"
)

func main() {
	// PERFORMANCE: Optimize GC for high-throughput easter egg processing
	if os.Getenv("GOGC") == "" {
		os.Setenv("GOGC", "75") // Higher threshold for content-heavy workloads
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

	// Initialize metrics collector
	metricsCollector := metrics.NewCollector()

	// Initialize service
	svc := service.NewEasterEggsService(repo, metricsCollector, sugar)

	// Initialize handlers
	h := handlers.NewEasterEggsHandlers(svc, sugar, metricsCollector)

	// Setup generated server
	server := &generated.ServerInterfaceWrapper{
		Handler: h,
		ErrorHandler: func(w http.ResponseWriter, r *http.Request, err error) {
			sugar.Errorf("API error: %v", err)
			http.Error(w, "Internal server error", http.StatusInternalServerError)
		},
	}

	// Setup router
	r := setupRouter(server)

	// Configure HTTP server with optimized settings
	httpServer := &http.Server{
		Addr:         fmt.Sprintf(":%d", cfg.Port),
		Handler:      r,
		ReadTimeout:  30 * time.Second, // PERFORMANCE: Extended for content-heavy requests
		WriteTimeout: 30 * time.Second, // PERFORMANCE: Extended for content-heavy responses
		IdleTimeout:  120 * time.Second, // PERFORMANCE: Longer idle timeout for exploration sessions
	}

	// Graceful shutdown setup
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)

	// Start server in goroutine
	serverErr := make(chan error, 1)
	go func() {
		sugar.Infof("Starting Cyberspace Easter Eggs Service on :%d", cfg.Port)
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

func setupRouter(server *generated.ServerInterfaceWrapper) *chi.Mux {
	r := chi.NewRouter()

	// PERFORMANCE: Optimized middleware stack
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(30 * time.Second))

	// CORS configuration for web clients
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"}, // Configure for production
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300,
	}))

	// Use generated server for all routes
	r.Mount("/", server)

	// Metrics endpoint (keep separate)
	r.Handle("/metrics", promhttp.Handler())

	return r
}
