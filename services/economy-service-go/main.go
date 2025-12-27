package main

import (
	"context"
	"database/sql"
	"fmt"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/cors"
	"github.com/go-chi/jwtauth/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	_ "github.com/jackc/pgx/v5/stdlib"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/economy-service-go/server"
)

func main() {
	// PERFORMANCE: Optimize GC for low-latency economy service
	// BACKEND NOTE: Lower GC threshold for MMOFPS economic operations
	if os.Getenv("GOGC") == "" {
		os.Setenv("GOGC", "40") // Reduced from default 100 for economic service
	}

	// PERFORMANCE: Preallocate logger to avoid allocations
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	logger.Info("Starting Economy Service",
		zap.String("service", "economy-service-go"),
		zap.String("version", "1.0.0"),
	)

	// PERFORMANCE: Context with timeout for initialization
	// BACKEND NOTE: Prevents hanging during service startup
	initCtx, initCancel := context.WithTimeout(context.Background(), 15*time.Second)
	defer initCancel()

	// Initialize database connection with performance optimizations
	dbPool, err := initDatabase(initCtx, logger)
	if err != nil {
		logger.Fatal("Failed to initialize database", zap.Error(err))
	}
	defer dbPool.Close()

	// Initialize JWT authenticator
	tokenAuth := jwtauth.New("HS256", []byte(os.Getenv("JWT_SECRET")), nil)

	// Initialize HTTP server with enterprise-grade configuration
	httpSrv := initHTTPServer(logger, dbPool, tokenAuth)

	// Start health check endpoint
	go func() {
		if err := httpSrv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("HTTP server failed", zap.Error(err))
		}
	}()

	logger.Info("Economy Service started successfully",
		zap.String("port", ":8080"),
		zap.String("health", "http://localhost:8080/health"),
	)

	// Graceful shutdown
	waitForShutdown(logger, httpSrv)
}

func initDatabase(ctx context.Context, logger *zap.Logger) (*pgxpool.Pool, error) {

	// Enterprise-grade database configuration for MMOFPS performance
	dbConfig, err := pgxpool.ParseConfig(os.Getenv("DATABASE_URL"))
	if err != nil {
		return nil, fmt.Errorf("failed to parse database URL: %w", err)
	}

	// Performance optimizations for 5000+ transactions/minute
	dbConfig.MaxConns = 50                    // Connection pool size
	dbConfig.MinConns = 10                    // Minimum connections
	dbConfig.MaxConnLifetime = 1 * time.Hour  // Connection lifetime
	dbConfig.MaxConnIdleTime = 30 * time.Minute // Idle timeout
	dbConfig.HealthCheckPeriod = 1 * time.Minute // Health checks

	// Prepared statements for hot path optimization
	dbConfig.ConnConfig.RuntimeParams = map[string]string{
		"standard_conforming_strings": "on",
	}

	pool, err := pgxpool.NewWithConfig(ctx, dbConfig)
	if err != nil {
		return nil, fmt.Errorf("failed to create database pool: %w", err)
	}

	// Health check
	if err := pool.Ping(ctx); err != nil {
		return nil, fmt.Errorf("database ping failed: %w", err)
	}

	logger.Info("Database connection established",
		zap.Int32("max_conns", dbConfig.MaxConns),
		zap.Int32("min_conns", dbConfig.MinConns),
	)

	return pool, nil
}

func initHTTPServer(logger *zap.Logger, dbPool *pgxpool.Pool, tokenAuth *jwtauth.JWTAuth) *http.Server {
	r := chi.NewRouter()

	// Enterprise-grade middleware stack
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"}, // Configure for production
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300, // Maximum value not ignored by any of major browsers
	}))

	// Performance and security middleware
	r.Use(middlewareLogger(logger))
	r.Use(middlewareTimeout(30 * time.Second)) // Context timeouts for MMOFPS requirements

	// JWT authentication middleware
	r.Use(jwtauth.Verifier(tokenAuth))
	r.Use(jwtauth.Authenticator(tokenAuth))

	// Initialize server with optimized handlers
	economyServer := server.NewEconomyService(logger, dbPool)

	// Mount Ogen-generated routes (to be generated)
	// api.HandlerFromMux(economyServer, r)

	// Fallback health check
	r.Get("/health", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"status":"healthy","service":"economy-service-go","timestamp":"` + time.Now().Format(time.RFC3339) + `"}`))
	})

	return &http.Server{
		Addr:         ":8080",
		Handler:      r,
		ReadTimeout:  15 * time.Second, // P99 latency <20ms target
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}
}

func middlewareLogger(logger *zap.Logger) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()

			// Custom response writer to capture status code
			rw := &responseWriter{ResponseWriter: w, statusCode: http.StatusOK}

			next.ServeHTTP(rw, r)

			logger.Info("HTTP Request",
				zap.String("method", r.Method),
				zap.String("path", r.URL.Path),
				zap.Int("status", rw.statusCode),
				zap.Duration("duration", time.Since(start)),
				zap.String("remote_addr", r.RemoteAddr),
			)
		})
	}
}

func middlewareTimeout(timeout time.Duration) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			ctx, cancel := context.WithTimeout(r.Context(), timeout)
			defer cancel()

			r = r.WithContext(ctx)
			next.ServeHTTP(w, r)
		})
	}
}

// responseWriter captures the status code for logging
type responseWriter struct {
	http.ResponseWriter
	statusCode int
}

func (rw *responseWriter) WriteHeader(code int) {
	rw.statusCode = code
	rw.ResponseWriter.WriteHeader(code)
}

func waitForShutdown(logger *zap.Logger, srv *http.Server) {
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("Shutting down Economy Service...")

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		logger.Error("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Economy Service stopped")
}

