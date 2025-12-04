package main

import (
	"context"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/necpgame/character-engram-historical-service-go/server"
	"github.com/prometheus/client_golang/prometheus/promhttp"
)

func main() {
	logger := server.GetLogger()
	logger.Info("Character Engram Historical Service (Go) starting...")

	addr := getEnv("ADDR", "0.0.0.0:8087")
	metricsAddr := getEnv("METRICS_ADDR", ":9097")

	httpServer := server.NewHTTPServer(addr)

	// OPTIMIZATION: Issue #1584 - Start pprof server for profiling
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6101")
		logger.WithField("addr", pprofAddr).Info("pprof server starting")
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

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

	go func() {
		logger.WithField("addr", addr).Info("HTTP server starting")
		if err := httpServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.WithError(err).Fatal("Could not start HTTP server")
		}
	}()

	stop := make(chan os.Signal, 1)
	signal.Notify(stop, os.Interrupt, syscall.SIGTERM)

	<-stop

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

	logger.Info("Character Engram Historical Service (Go) stopped.")
}

func getEnv(key, defaultValue string) string {
	if value, exists := os.LookupEnv(key); exists {
		return value
	}
	return defaultValue
}




















