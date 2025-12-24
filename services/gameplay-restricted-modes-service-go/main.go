// Issue: #1499
package main

import (
	"context"
	"crypto/tls"
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
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/redis/go-redis/v9"
	"github.com/prometheus/client_golang/prometheus/promhttp"

	"gameplay-restricted-modes-service-go/internal/auth"
	"gameplay-restricted-modes-service-go/internal/config"
	"gameplay-restricted-modes-service-go/internal/database"
	"gameplay-restricted-modes-service-go/internal/handlers"
	"gameplay-restricted-modes-service-go/internal/middleware/security"
	"gameplay-restricted-modes-service-go/internal/monitoring"
	"gameplay-restricted-modes-service-go/pkg/api"
)

func main() {
	// Load configuration
	cfg, err := config.Load()
	if err != nil {
		log.Fatalf("Failed to load configuration: %v", err)
	}

	// Initialize structured logging
	logger := monitoring.NewLogger(cfg.Logging.Level)
	logger.Info().Str("version", "1.0.0").Str("environment", cfg.Environment).Msg("Starting Gameplay Restricted Modes Service")

	// Initialize database connections
	ctx := context.Background()

	// PostgreSQL connection with connection pooling (50 max connections for gameplay operations)
	dbConfig, err := pgxpool.ParseConfig(cfg.Database.URL)
	if err != nil {
		logger.Fatal().Err(err).Msg("Failed to parse database config")
	}
	dbConfig.MaxConns = 50 // Enterprise-grade connection pooling
	dbConfig.MinConns = 5  // Maintain minimum connections
	dbConfig.MaxConnLifetime = time.Hour
	dbConfig.MaxConnIdleTime = 30 * time.Minute

	dbPool, err := pgxpool.NewWithConfig(ctx, dbConfig)
	if err != nil {
		logger.Fatal("Failed to create database pool", "error", err)
	}
	defer dbPool.Close()

	// Test database connection
	if err := dbPool.Ping(ctx); err != nil {
		logger.Fatal().Err(err).Msg("Failed to ping database")
	}
	logger.Info().Msg("Database connection established")

	// Redis connection for session management and caching
	rdb := redis.NewClient(&redis.Options{
		Addr:     cfg.Redis.Addr,
		Password: cfg.Redis.Password,
		DB:       cfg.Redis.DB,
	})
	defer rdb.Close()

	// Test Redis connection
	if err := rdb.Ping(ctx).Err(); err != nil {
		logger.Fatal().Err(err).Msg("Failed to connect to Redis")
	}
	logger.Info().Msg("Redis connection established")

	// Initialize database layer
	dbLayer := database.New(dbPool, rdb, logger)

	// Initialize authentication service
	authService := auth.NewService(dbLayer, cfg.JWT, logger)

	// Initialize handlers
	gameplayHandlers := handlers.NewRestrictedModesHandlers(authService, dbLayer, logger)

	// Create Chi router with enterprise-grade middleware
	r := chi.NewRouter()

	// Global middleware stack
	r.Use(middleware.RequestID)
	r.Use(monitoring.NewStructuredLogger(logger))
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(30 * time.Second)) // Enterprise-grade timeout

	// Rate limiting - 1000 requests per minute per IP
	rateLimiter := middleware.Throttle(1000) // 1000 req/min
	r.Use(rateLimiter)

	// CORS configuration for web clients
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   cfg.CORS.AllowedOrigins,
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: true,
		MaxAge:           300,
	}))

	// Security middleware
	r.Use(security.HTTPSRedirect)
	r.Use(security.SecurityHeaders)
	r.Use(security.CSRFProtection)

	// Health check endpoints (no auth required)
	r.Get("/health", gameplayHandlers.HealthCheck)
	r.Get("/ready", gameplayHandlers.ReadinessCheck)

	// Metrics endpoint (basic auth for security)
	r.With(security.BasicAuth("metrics", cfg.Metrics.Username, cfg.Metrics.Password)).
		Get("/metrics", promhttp.Handler().ServeHTTP)

	// Create OpenAPI server
	oasServer, err := api.NewServer(gameplayHandlers, nil)
	if err != nil {
		logger.Fatal().Err(err).Msg("Failed to create OpenAPI server")
	}

	// Mount OpenAPI routes
	r.Mount("/", oasServer)

	// Start profiling server on port 6555 (configurable)
	if cfg.Profiling.Enabled {
		go func() {
			logger.Info("Starting profiling server", "port", cfg.Profiling.Port)
			if err := http.ListenAndServe(fmt.Sprintf(":%d", cfg.Profiling.Port), nil); err != nil {
				logger.Error("Profiling server failed", "error", err)
			}
		}()
	}

	// Start HTTP server with graceful shutdown
	srv := &http.Server{
		Addr:         fmt.Sprintf(":%d", cfg.Server.Port),
		Handler:      r,
		ReadTimeout:  30 * time.Second,
		WriteTimeout: 30 * time.Second,
		IdleTimeout:  120 * time.Second,
		TLSConfig: &tls.Config{
			MinVersion:               tls.VersionTLS12,
			CurvePreferences:         []tls.CurveID{tls.CurveP521, tls.CurveP384, tls.CurveP256},
			PreferServerCipherSuites: true,
			CipherSuites: []uint16{
				tls.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384,
				tls.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305,
				tls.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384,
			},
		},
	}

	// Start server in goroutine
	go func() {
		logger.Info().Int("port", cfg.Server.Port).Str("env", cfg.Environment).Msg("Starting HTTP server")
		var err error
		if cfg.Server.TLS.Enabled {
			err = srv.ListenAndServeTLS(cfg.Server.TLS.CertFile, cfg.Server.TLS.KeyFile)
		} else {
			err = srv.ListenAndServe()
		}
		if err != nil && err != http.ErrServerClosed {
			logger.Fatal().Err(err).Msg("Server failed to start")
		}
	}()

	// Wait for interrupt signal to gracefully shutdown
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	logger.Info().Msg("Shutting down server...")

	// Graceful shutdown with timeout
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		logger.Error().Err(err).Msg("Server forced to shutdown")
	}

	logger.Info().Msg("Server exited")
}

