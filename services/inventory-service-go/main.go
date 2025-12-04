// Issue: #1581 - ogen migration + full optimizations, #1584 - pprof profiling
package main

import (
	"context"
	"log"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling endpoints
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/go-redis/redis/v8"
	"github.com/gc-lover/necpgame-monorepo/services/inventory-service-go/server"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/grafana/pyroscope-go" // Issue: #1611 - Continuous Profiling
)

func main() {
	// Issue: #1611 - Continuous Profiling (Pyroscope)
	pyroscope.Start(pyroscope.Config{
		ApplicationName: "necpgame.inventory",
		ServerAddress:   getEnv("PYROSCOPE_SERVER", "http://pyroscope:4040"),
		ProfileTypes: []pyroscope.ProfileType{
			pyroscope.ProfileCPU,
			pyroscope.ProfileAllocObjects,
			pyroscope.ProfileAllocSpace,
			pyroscope.ProfileInuseObjects,
			pyroscope.ProfileInuseSpace,
		},
		Tags: map[string]string{
			"environment": getEnv("ENV", "development"),
			"version":     getEnv("VERSION", "unknown"),
		},
		UploadRate: 10 * time.Second, // 10 seconds
	})

	logger := server.GetLogger()
	logger.Info("Inventory Service (Go) starting...")
	logger.Info("OK Pyroscope continuous profiling started")

	addr := getEnv("ADDR", "0.0.0.0:8085")
	metricsAddr := getEnv("METRICS_ADDR", ":9090")
	
	dbURL := getEnv("DATABASE_URL", "postgresql://necpgame:necpgame@localhost:5432/necpgame?sslmode=disable")
	redisAddr := getEnv("REDIS_ADDR", "localhost:6379")

	// Database connection with pgxpool
	config, err := pgxpool.ParseConfig(dbURL)
	if err != nil {
		log.Fatalf("Failed to parse database URL: %v", err)
	}
	// CRITICAL: Configure DB pool (hot path - 10k RPS)
	config.MaxConns = 50
	config.MinConns = 10
	config.MaxConnLifetime = 5 * time.Minute
	config.MaxConnIdleTime = 10 * time.Minute
	
	db, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		log.Fatalf("Failed to connect to database: %v", err)
	}
	defer db.Close()

	// Redis client for caching
	redisClient := redis.NewClient(&redis.Options{
		Addr: redisAddr,
	})

	// Create optimized service with 3-tier caching
	repo := server.NewInventoryRepository(db)
	repoAdapter := server.NewRepositoryAdapter(repo)
	optimizedService := server.NewOptimizedInventoryService(redisClient, repoAdapter)

	// Create ogen server
	httpServer := server.NewHTTPServerOgen(addr, optimizedService)

	metricsMux := http.NewServeMux()
	metricsMux.Handle("/metrics", promhttp.Handler())

	metricsServer := &http.Server{
		Addr:         metricsAddr,
		Handler:      metricsMux,
		ReadTimeout:  10 * time.Second,
		WriteTimeout: 10 * time.Second,
	}

	go func() {
		logger.WithField("addr", metricsAddr).Info("Metrics server starting")
		if err := metricsServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.WithError(err).Fatal("Metrics server failed")
		}
	}()

	// OPTIMIZATION: Issue #1584 - Start pprof server for profiling
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6819")
		logger.WithField("addr", pprofAddr).Info("pprof server starting")
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

	// Issue: #1585 - Runtime Goroutine Monitoring (CRITICAL - 10k+ RPS!)
	monitor := server.NewGoroutineMonitor(1200) // Max 1200 goroutines for inventory (high concurrency)
	go monitor.Start()
	defer monitor.Stop()
	logger.Info("OK Goroutine monitor started")

	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()

	sigChan := make(chan os.Signal, 1)
	signal.Notify(sigChan, os.Interrupt, syscall.SIGTERM)

	go func() {
		<-sigChan
		logger.Info("Shutting down server...")
		cancel()

		shutdownCtx, shutdownCancel := context.WithTimeout(context.Background(), 5*time.Second)
		defer shutdownCancel()
		metricsServer.Shutdown(shutdownCtx)
		httpServer.Shutdown(shutdownCtx)
	}()

	logger.WithField("addr", addr).Info("HTTP server starting")
	if err := httpServer.Start(ctx); err != nil && err != http.ErrServerClosed {
		logger.WithError(err).Fatal("Server error")
	}

	logger.Info("Server stopped")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
