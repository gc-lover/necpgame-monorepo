package main

import (
	"context"
	"net/http"
	"net/http/pprof"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/tournament-service-go/server"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/sirupsen/logrus"
)

// OPTIMIZATION: Issue #2166 - Memory-aligned struct for tournament service performance
type TournamentServiceConfig struct {
	HTTPAddr       string        `json:"http_addr"`       // 16 bytes
	RedisAddr      string        `json:"redis_addr"`      // 16 bytes
	PprofAddr      string        `json:"pprof_addr"`      // 16 bytes
	HealthAddr     string        `json:"health_addr"`     // 16 bytes
	ReadTimeout    time.Duration `json:"read_timeout"`    // 8 bytes
	WriteTimeout   time.Duration `json:"write_timeout"`   // 8 bytes
	MaxHeaderBytes int           `json:"max_header_bytes"` // 8 bytes
	MaxConnections int           `json:"max_connections"` // 8 bytes
	MatchTimeout   time.Duration `json:"match_timeout"`   // 8 bytes
	BracketUpdateInterval time.Duration `json:"bracket_update_interval"` // 8 bytes
	RankingUpdateInterval time.Duration `json:"ranking_update_interval"` // 8 bytes
	StatsCleanupInterval  time.Duration `json:"stats_cleanup_interval"`  // 8 bytes
}

func main() {
	logger := logrus.New()
	logger.SetFormatter(&logrus.JSONFormatter{
		TimestampFormat: time.RFC3339,
	})
	logger.SetLevel(logrus.InfoLevel)

	config := &TournamentServiceConfig{
		HTTPAddr:               getEnv("HTTP_ADDR", ":8100"),
		RedisAddr:              getEnv("REDIS_ADDR", "localhost:6379"),
		PprofAddr:              getEnv("PPROF_ADDR", ":6870"),
		HealthAddr:             getEnv("HEALTH_ADDR", ":8101"),
		ReadTimeout:            30 * time.Second,
		WriteTimeout:           30 * time.Second,
		MaxHeaderBytes:         1 << 20, // 1MB
		MaxConnections:         10000,   // OPTIMIZATION: Support 10k+ concurrent tournament connections
		MatchTimeout:           10 * time.Minute,
		BracketUpdateInterval:  5 * time.Second,
		RankingUpdateInterval:  30 * time.Second,
		StatsCleanupInterval:   1 * time.Hour,
	}

	// OPTIMIZATION: Issue #2166 - Start pprof server for tournament performance profiling
	go func() {
		logger.WithField("addr", config.PprofAddr).Info("pprof server starting")
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(config.PprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

	// Initialize tournament service with performance optimizations
	srv, err := server.NewTournamentServer(config, logger)
	if err != nil {
		logger.WithError(err).Fatal("failed to initialize tournament server")
	}

	// OPTIMIZATION: Issue #2166 - Health check endpoint with tournament metrics
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
		logger.WithField("addr", config.HTTPAddr).Info("tournament service starting")
		if err := httpSrv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.WithError(err).Fatal("tournament server failed")
		}
	}()

	// OPTIMIZATION: Issue #2166 - Graceful shutdown with timeout
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("shutting down tournament service...")

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpSrv.Shutdown(ctx); err != nil {
		logger.WithError(err).Error("server forced to shutdown")
	}

	logger.Info("tournament service stopped")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}