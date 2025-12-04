package main

import (
	"context"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling endpoints
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/necpgame/character-service-go/server"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	// TODO: Issue #1611 - Continuous Profiling (Pyroscope)
	// pyroscope package path needs to be verified
	// _ "github.com/pyroscope-io/client/pyroscope"
)

func main() {
	// TODO: Issue #1611 - Continuous Profiling (Pyroscope)
	// Uncomment when pyroscope package is available
	// pyroscope.Start(pyroscope.Config{
	// 	ApplicationName: "necpgame.character",
	// 	ServerAddress:   getEnv("PYROSCOPE_SERVER", "http://pyroscope:4040"),
	// 	ProfileTypes: []pyroscope.ProfileType{
	// 		pyroscope.ProfileCPU,
	// 		pyroscope.ProfileAllocObjects,
	// 		pyroscope.ProfileAllocSpace,
	// 		pyroscope.ProfileInuseObjects,
	// 		pyroscope.ProfileInuseSpace,
	// 	},
	// 	Tags: map[string]string{
	// 		"environment": getEnv("ENV", "development"),
	// 		"version":     getEnv("VERSION", "unknown"),
	// 	},
	// 	SampleRate: 100, // 100 Hz
	// })

	logger := server.GetLogger()
	logger.Info("Character Service (Go) starting...")
	// logger.Info("OK Pyroscope continuous profiling started")

	addr := getEnv("ADDR", "0.0.0.0:8087")
	metricsAddr := getEnv("METRICS_ADDR", ":9092")
	
	dbURL := getEnv("DATABASE_URL", "postgresql://necpgame:necpgame@localhost:5432/necpgame?sslmode=disable")
	redisURL := getEnv("REDIS_URL", "redis://localhost:6379/2")
	keycloakURL := getEnv("KEYCLOAK_URL", "http://localhost:8080")

	characterService, err := server.NewCharacterService(dbURL, redisURL, keycloakURL)
	if err != nil {
		logger.WithError(err).Fatal("Failed to initialize character service")
	}
	
	// OPTIMIZATION: Issue #1584 - Start pprof server for profiling
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6557")
		logger.WithField("addr", pprofAddr).Info("pprof server starting")
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

	// OPTIMIZATION: Issue #1593 - ogen migration for 90% faster performance
	httpServer := server.NewHTTPServerOgen(addr, characterService)

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

	sigChan := make(chan os.Signal, 1)
	signal.Notify(sigChan, os.Interrupt, syscall.SIGTERM)

	go func() {
		<-sigChan
		logger.Info("Shutting down server...")

		shutdownCtx, shutdownCancel := context.WithTimeout(context.Background(), 5*time.Second)
		defer shutdownCancel()
		metricsServer.Shutdown(shutdownCtx)
		httpServer.Shutdown(shutdownCtx)
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

