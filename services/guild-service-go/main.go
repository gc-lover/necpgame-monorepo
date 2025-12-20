// Issue: #1943
package main

import (
	"context"
	"fmt"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #1584
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/guild-service-go/server"
	"github.com/prometheus/client_golang/prometheus/promhttp"
)

func main() {
	fmt.Println("Starting Guild Service...")
	logger := server.GetLogger()
	logger.Info("Guild Service (Go) starting...")

	addr := getEnv("ADDR", "0.0.0.0:8084")
	metricsAddr := getEnv("METRICS_ADDR", ":9184")
	fmt.Printf("Server address: %s, Metrics address: %s\n", addr, metricsAddr)

	// Issue: #1943 - Initialize guild service with optimizations
	guildService, err := server.NewGuildService()
	if err != nil {
		logger.WithError(err).Fatal("Failed to initialize guild service")
	}

	httpServer := server.NewHTTPServer(addr, guildService)
	fmt.Println("OK HTTP server created successfully")
	logger.Info("OK HTTP server created successfully")

	// OPTIMIZATION: Issue #1584 - Start pprof server for profiling
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6862")
		logger.WithField("addr", pprofAddr).Info("pprof server starting")
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

	// Issue: #1585 - Runtime Goroutine Monitoring
	monitor := server.NewGoroutineMonitor(150, logger) // Max 150 goroutines for guild service
	go monitor.Start()
	defer monitor.Stop()
	logger.Info("OK Goroutine monitor started")

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
			logger.WithError(err).Fatal("Could not start metrics server")
		}
	}()

	// Graceful shutdown handling
	c := make(chan os.Signal, 1)
	signal.Notify(c, os.Interrupt, syscall.SIGTERM)

	logger.WithField("addr", addr).Info("HTTP server starting")
	fmt.Println("Starting HTTP server...")

	go func() {
		if err := httpServer.Start(); err != nil && err != http.ErrServerClosed {
			logger.WithError(err).Fatal("Could not start HTTP server")
			fmt.Printf("Could not start HTTP server: %v\n", err)
		}
	}()

	<-c
	logger.Info("Shutting down servers...")

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	if err := httpServer.Shutdown(ctx); err != nil {
		logger.WithError(err).Error("HTTP server shutdown failed")
	} else {
		logger.Info("HTTP server gracefully stopped")
	}

	if err := metricsServer.Shutdown(ctx); err != nil {
		logger.WithError(err).Error("Metrics server shutdown failed")
	} else {
		logger.Info("Metrics server gracefully stopped")
	}

	logger.Info("Guild Service (Go) stopped.")
}

func getEnv(key, defaultValue string) string {
	if value, exists := os.LookupEnv(key); exists {
		return value
	}
	return defaultValue
}
