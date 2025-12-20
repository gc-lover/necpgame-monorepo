package main

import (
	"context"
	"net/http"
	"net/http/pprof"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-service-go/server"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/sirupsen/logrus"
)

// OPTIMIZATION: Issue #1936 - Memory-aligned struct for performance
type CombatServiceConfig struct {
	HTTPAddr       string        `json:"http_addr"`       // 16 bytes
	HealthAddr     string        `json:"health_addr"`     // 16 bytes
	PprofAddr      string        `json:"pprof_addr"`      // 16 bytes
	DBMaxOpenConns int           `json:"db_max_open_conns"` // 8 bytes
	ReadTimeout    time.Duration `json:"read_timeout"`    // 8 bytes
	WriteTimeout   time.Duration `json:"write_timeout"`   // 8 bytes
	MaxHeaderBytes int           `json:"max_header_bytes"` // 8 bytes
}

func main() {
	logger := logrus.New()
	logger.SetFormatter(&logrus.JSONFormatter{
		TimestampFormat: time.RFC3339,
	})
	logger.SetLevel(logrus.InfoLevel)

	config := &CombatServiceConfig{
		HTTPAddr:       getEnv("HTTP_ADDR", ":8080"),
		HealthAddr:     getEnv("HEALTH_ADDR", ":8081"),
		PprofAddr:      getEnv("PPROF_ADDR", ":6862"),
		DBMaxOpenConns: 50, // OPTIMIZATION: Higher connection pool for MMO load
		ReadTimeout:    15 * time.Second,
		WriteTimeout:   15 * time.Second,
		MaxHeaderBytes: 1 << 20, // 1MB
	}

	// OPTIMIZATION: Issue #1936 - Start pprof server for profiling
	go func() {
		logger.WithField("addr", config.PprofAddr).Info("pprof server starting")
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(config.PprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

	// OPTIMIZATION: Issue #1585 - Runtime Goroutine Monitoring
	monitor := server.NewGoroutineMonitor(200, logger) // Max 200 goroutines for combat service
	go monitor.Start()
	defer monitor.Stop()
	logger.Info("OK Goroutine monitor started")

	// Initialize service with performance optimizations
	srv, err := server.NewCombatServer(config, logger)
	if err != nil {
		logger.WithError(err).Fatal("failed to initialize combat server")
	}

	// OPTIMIZATION: Issue #1935 - Health check endpoint with metrics
	healthMux := http.NewServeMux()
	healthMux.HandleFunc("/health", srv.HealthCheck)
	healthMux.Handle("/metrics", promhttp.Handler())

	go func() {
		logger.WithField("addr", config.HealthAddr).Info("health/metrics server starting")
		if err := http.ListenAndServe(config.HealthAddr, healthMux); err != nil {
			logger.WithError(err).Error("health server failed")
		}
	}()

	// Start main HTTP server
	httpSrv := &http.Server{
		Addr:           config.HTTPAddr,
		Handler:        srv.Router(),
		ReadTimeout:    config.ReadTimeout,
		WriteTimeout:   config.WriteTimeout,
		MaxHeaderBytes: config.MaxHeaderBytes,
	}

	// Graceful shutdown
	go func() {
		logger.WithField("addr", config.HTTPAddr).Info("combat service starting")
		if err := httpSrv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.WithError(err).Fatal("combat server failed")
		}
	}()

	// OPTIMIZATION: Issue #1935 - Graceful shutdown with timeout
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("shutting down combat service...")

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpSrv.Shutdown(ctx); err != nil {
		logger.WithError(err).Error("server forced to shutdown")
	}

	logger.Info("combat service stopped")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
