// Issue: #1581 - ogen migration + full optimizations, #1584 - pprof profiling
package main

import (
	"context"
	"database/sql"
	"log"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling endpoints
	"os"
	"os/signal"
	"syscall"
	"time"

	_ "github.com/lib/pq"
	"github.com/go-redis/redis/v8"
	"github.com/gc-lover/necpgame-monorepo/services/inventory-service-go/server"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/pyroscope-io/client/pyroscope" // Issue: #1611 - Continuous Profiling
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
		SampleRate: 100, // 100 Hz
	})

	logger := server.GetLogger()
	logger.Info("Inventory Service (Go) starting...")
	logger.Info("OK Pyroscope continuous profiling started")

	addr := getEnv("ADDR", "0.0.0.0:8085")
	metricsAddr := getEnv("METRICS_ADDR", ":9090")
	
	dbURL := getEnv("DATABASE_URL", "postgresql://necpgame:necpgame@localhost:5432/necpgame?sslmode=disable")
	redisAddr := getEnv("REDIS_ADDR", "localhost:6379")

	// Database connection
	db, err := sql.Open("postgres", dbURL)
	if err != nil {
		log.Fatalf("Failed to connect to database: %v", err)
	}
	defer db.Close()

	// CRITICAL: Configure DB pool (hot path - 10k RPS)
	db.SetMaxOpenConns(50)
	db.SetMaxIdleConns(50)
	db.SetConnMaxLifetime(5 * time.Minute)
	db.SetConnMaxIdleTime(10 * time.Minute)

	// Redis client for caching
	redisClient := redis.NewClient(&redis.Options{
		Addr: redisAddr,
	})

	// Create optimized service with 3-tier caching
	repo := server.NewRepository(db)
	optimizedService := server.NewOptimizedInventoryService(redisClient, repo)

	// Create ogen server
	httpServer := server.NewOgenHTTPServer(addr, optimizedService)
	if err != nil {
		log.Fatalf("Failed to create server: %v", err)
	}

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
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6071")
		logger.WithField("addr", pprofAddr).Info("pprof server starting")
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

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
