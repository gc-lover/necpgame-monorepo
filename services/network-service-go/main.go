package main

import (
	"context"
	"net/http"
	"net/http/pprof"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/network-service-go/server"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/sirupsen/logrus"
)

// OPTIMIZATION: Issue #1978 - Memory-aligned struct for network service performance
type NetworkServiceConfig struct {
	HTTPAddr       string        `json:"http_addr"`       // 16 bytes
	WSAddr         string        `json:"ws_addr"`         // 16 bytes
	HealthAddr     string        `json:"health_addr"`     // 16 bytes
	PprofAddr      string        `json:"pprof_addr"`      // 16 bytes
	DBMaxOpenConns int           `json:"db_max_open_conns"` // 8 bytes
	ReadTimeout    time.Duration `json:"read_timeout"`    // 8 bytes
	WriteTimeout   time.Duration `json:"write_timeout"`   // 8 bytes
	MaxHeaderBytes int           `json:"max_header_bytes"` // 8 bytes
	MaxConnections int           `json:"max_connections"`  // 8 bytes
	HeartbeatInterval time.Duration `json:"heartbeat_interval"` // 8 bytes
}

func main() {
	logger := logrus.New()
	logger.SetFormatter(&logrus.JSONFormatter{
		TimestampFormat: time.RFC3339,
	})
	logger.SetLevel(logrus.InfoLevel)

	config := &NetworkServiceConfig{
		HTTPAddr:          getEnv("HTTP_ADDR", ":8084"),
		WSAddr:            getEnv("WS_ADDR", ":8085"),
		HealthAddr:        getEnv("HEALTH_ADDR", ":8086"),
		PprofAddr:         getEnv("PPROF_ADDR", ":6865"),
		DBMaxOpenConns:    200, // OPTIMIZATION: Higher for MMO messaging load
		ReadTimeout:       30 * time.Second,
		WriteTimeout:      30 * time.Second,
		MaxHeaderBytes:    1 << 20, // 1MB
		MaxConnections:    5000,    // OPTIMIZATION: Support 5000+ concurrent WebSocket connections
		HeartbeatInterval: 30 * time.Second,
	}

	// OPTIMIZATION: Issue #1978 - Start pprof server for network performance profiling
	go func() {
		logger.WithField("addr", config.PprofAddr).Info("pprof server starting")
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(config.PprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

	// Initialize network service with performance optimizations
	srv, err := server.NewNetworkServer(config, logger)
	if err != nil {
		logger.WithError(err).Fatal("failed to initialize network server")
	}

	// OPTIMIZATION: Issue #1972 - Health check endpoint with network metrics
	healthMux := http.NewServeMux()
	healthMux.HandleFunc("/health", srv.HealthCheck)
	healthMux.Handle("/metrics", promhttp.Handler())

	go func() {
		logger.WithField("addr", config.HealthAddr).Info("health/metrics server starting")
		if err := http.ListenAndServe(config.HealthAddr, healthMux); err != nil {
			logger.WithError(err).Error("health server failed")
		}
	}()

	// Start main HTTP/WebSocket server
	httpSrv := &http.Server{
		Addr:           config.HTTPAddr,
		Handler:        srv.Router(),
		ReadTimeout:    config.ReadTimeout,
		WriteTimeout:   config.WriteTimeout,
		MaxHeaderBytes: config.MaxHeaderBytes,
	}

	// Start WebSocket server
	go func() {
		logger.WithField("addr", config.WSAddr).Info("WebSocket server starting")
		wsSrv := &http.Server{
			Addr:    config.WSAddr,
			Handler: srv.WebSocketRouter(),
		}
		if err := wsSrv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.WithError(err).Fatal("WebSocket server failed")
		}
	}()

	// Start main HTTP server
	go func() {
		logger.WithField("addr", config.HTTPAddr).Info("network service starting")
		if err := httpSrv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.WithError(err).Fatal("network server failed")
		}
	}()

	// OPTIMIZATION: Issue #1972 - Graceful shutdown with timeout
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("shutting down network service...")

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpSrv.Shutdown(ctx); err != nil {
		logger.WithError(err).Error("server forced to shutdown")
	}

	logger.Info("network service stopped")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
