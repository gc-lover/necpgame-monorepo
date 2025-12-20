package main

import (
	"context"
	"net/http"
	"os"
	"os/signal"
	"runtime/debug"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/circuit-breaker-service-go/server"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/sirupsen/logrus"
)

func main() {
	logger := logrus.New()
	logger.SetFormatter(&logrus.JSONFormatter{
		TimestampFormat: time.RFC3339,
	})
	logger.SetLevel(logrus.InfoLevel)

	config := &server.CircuitBreakerServiceConfig{
		HTTPAddr:                getEnv("HTTP_ADDR", ":8090"),
		RedisAddr:               getEnv("REDIS_ADDR", "localhost:6379"),
		PprofAddr:               getEnv("PPROF_ADDR", ":6866"),
		HealthAddr:              getEnv("HEALTH_ADDR", ":8091"),
		ReadTimeout:             30 * time.Second,
		WriteTimeout:            30 * time.Second,
		MaxHeaderBytes:          1 << 20, // 1MB
		MaxConnections:          10000,   // OPTIMIZATION: Support 10k+ concurrent connections
		MetricsInterval:         10 * time.Second,
		StateSyncInterval:       5 * time.Second,
		DefaultFailureThreshold: 5,
		DefaultTimeout:          5 * time.Second,
		CleanupInterval:         10 * time.Minute,
	}

	// OPTIMIZATION: Issue #2156 - Start pprof server for circuit breaker performance profiling
	go func() {
		logger.WithField("addr", config.PprofAddr).Info("pprof server starting")
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(config.PprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

	// Initialize circuit breaker service with performance optimizations
	srv, err := server.NewCircuitBreakerServer(config, logger)
	if err != nil {
		logger.WithError(err).Fatal("failed to initialize circuit breaker server")
	}

	// OPTIMIZATION: Issue #2202 - GC tuning for circuit breaker service performance
	// Set GOGC to 50 for optimal balance between memory usage and GC overhead
	oldGC := os.Getenv("GOGC")
	if oldGC == "" {
		debug.SetGCPercent(50) // 50% instead of default 100%
		logger.Info("OK GC tuning applied: GOGC=50 for optimal circuit breaker performance")
	}

	// OPTIMIZATION: Issue #2156 - Health check endpoint with circuit breaker metrics
	healthMux := http.NewServeMux()
	healthMux.HandleFunc("/health", srv.HealthCheck)
	healthMux.Handle("/metrics", promhttp.Handler())

	go func() {
		logger.WithField("addr", config.HealthAddr).Info("health/metrics server starting")
		if err := http.ListenAndServe(config.HealthAddr, healthMux); err != nil {
			logger.WithError(err).Error("health server failed")
		}
	}()

	// Start HTTP server
	httpSrv := &http.Server{
		Addr:           config.HTTPAddr,
		Handler:        srv.Router(),
		ReadTimeout:    config.ReadTimeout,
		WriteTimeout:   config.WriteTimeout,
		MaxHeaderBytes: config.MaxHeaderBytes,
	}

	// Start HTTP server
	go func() {
		logger.WithField("addr", config.HTTPAddr).Info("circuit breaker service starting")
		if err := httpSrv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.WithError(err).Fatal("circuit breaker server failed")
		}
	}()

	// OPTIMIZATION: Issue #2156 - Graceful shutdown with timeout
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("shutting down circuit breaker service...")

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpSrv.Shutdown(ctx); err != nil {
		logger.WithError(err).Error("server forced to shutdown")
	}

	logger.Info("circuit breaker service stopped")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
