package main

import (
	"context"
	"net/http"
	"net/http/pprof"
	"os"
	"os/signal"
	"syscall"
	"time"

	"necpgame/services/auth-service-go/server"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/sirupsen/logrus"
)

// OPTIMIZATION: Issue #1998 - Memory-aligned struct for auth service performance
type AuthServiceConfig struct {
	HTTPAddr       string        `json:"http_addr"`       // 16 bytes
	HealthAddr     string        `json:"health_addr"`     // 16 bytes
	PprofAddr      string        `json:"pprof_addr"`      // 16 bytes
	DBMaxOpenConns int           `json:"db_max_open_conns"` // 8 bytes
	ReadTimeout    time.Duration `json:"read_timeout"`    // 8 bytes
	WriteTimeout   time.Duration `json:"write_timeout"`   // 8 bytes
	MaxHeaderBytes int           `json:"max_header_bytes"` // 8 bytes
	JWTSecret      string        `json:"jwt_secret"`      // 16 bytes
	JWTExpiry      time.Duration `json:"jwt_expiry"`      // 8 bytes
	SessionTimeout time.Duration `json:"session_timeout"` // 8 bytes
	MaxLoginAttempts int         `json:"max_login_attempts"` // 8 bytes
	LockoutDuration time.Duration `json:"lockout_duration"` // 8 bytes
}

func main() {
	logger := logrus.New()
	logger.SetFormatter(&logrus.JSONFormatter{
		TimestampFormat: time.RFC3339,
	})
	logger.SetLevel(logrus.InfoLevel)

	config := &AuthServiceConfig{
		HTTPAddr:          getEnv("HTTP_ADDR", ":8086"),
		HealthAddr:        getEnv("HEALTH_ADDR", ":8087"),
		PprofAddr:         getEnv("PPROF_ADDR", ":6866"),
		DBMaxOpenConns:    500, // OPTIMIZATION: Higher for MMO auth load
		ReadTimeout:       30 * time.Second,
		WriteTimeout:      30 * time.Second,
		MaxHeaderBytes:    1 << 20, // 1MB
		JWTSecret:         getEnv("JWT_SECRET", "your-super-secret-jwt-key-here"),
		JWTExpiry:         15 * time.Minute, // OPTIMIZATION: Short-lived tokens for security
		SessionTimeout:    24 * time.Hour,
		MaxLoginAttempts:  5,  // OPTIMIZATION: Brute force protection
		LockoutDuration:   15 * time.Minute,
	}

	// OPTIMIZATION: Issue #1998 - Start pprof server for auth performance profiling
	go func() {
		logger.WithField("addr", config.PprofAddr).Info("pprof server starting")
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(config.PprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

	// Initialize auth service with security optimizations
	srv, err := server.NewAuthServer(config, logger)
	if err != nil {
		logger.WithError(err).Fatal("failed to initialize auth server")
	}

	// OPTIMIZATION: Issue #1998 - Health check endpoint with auth metrics
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

	// Start main HTTP server
	go func() {
		logger.WithField("addr", config.HTTPAddr).Info("auth service starting")
		if err := httpSrv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.WithError(err).Fatal("auth server failed")
		}
	}()

	// OPTIMIZATION: Issue #1998 - Graceful shutdown with timeout
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("shutting down auth service...")

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpSrv.Shutdown(ctx); err != nil {
		logger.WithError(err).Error("server forced to shutdown")
	}

	logger.Info("auth service stopped")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
