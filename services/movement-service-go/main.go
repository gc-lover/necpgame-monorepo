// Issue: #1584
package main

import (
	"context"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling endpoints
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/movement-service-go/server"
	"github.com/prometheus/client_golang/prometheus/promhttp"
)

func main() {
	logger := server.GetLogger()
	logger.Info("Movement Service (Go) starting...")

	addr := getEnv("ADDR", "0.0.0.0:8086")
	metricsAddr := getEnv("METRICS_ADDR", ":9091")

	dbURL := getEnv("DATABASE_URL", "postgresql://necpgame:necpgame@localhost:5432/necpgame?sslmode=disable")
	redisURL := getEnv("REDIS_URL", "redis://localhost:6379/1")
	gatewayURL := getEnv("GATEWAY_URL", "ws://localhost:18080/client")
	updateInterval := getEnv("UPDATE_INTERVAL", "5s")

	updateDuration, err := time.ParseDuration(updateInterval)
	if err != nil {
		logger.WithError(err).Warn("Invalid UPDATE_INTERVAL, using default 5s")
		updateDuration = 5 * time.Second
	}

	movementService, err := server.NewMovementService(dbURL, redisURL, gatewayURL, updateDuration)
	if err != nil {
		logger.WithError(err).Fatal("Failed to initialize movement service")
	}

	httpServer := server.NewHTTPServer(addr, movementService)

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
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6783")
		logger.WithField("addr", pprofAddr).Info("pprof server starting")
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

	// Issue: #1585 - Runtime Goroutine Monitoring
	monitor := server.NewGoroutineMonitor(700) // Max 700 goroutines for movement
	go monitor.Start()
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
		movementService.Shutdown()
	}()

	go func() {
		if err := movementService.StartGatewayConnection(ctx); err != nil {
			logger.WithError(err).Error("Gateway connection error")
		}
	}()

	go func() {
		if err := movementService.StartPositionSaver(ctx); err != nil {
			logger.WithError(err).Error("Position saver error")
		}
	}()

	logger.WithField("addr", addr).Info("HTTP server starting")
	if err := httpServer.Start(); err != nil && err != http.ErrServerClosed {
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
