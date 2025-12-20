package main

import (
	"context"
	"net/http"
	"net/http/pprof"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/voice-chat-service-go/server"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/sirupsen/logrus"
)

// OPTIMIZATION: Issue #2030 - Memory-aligned struct for voice chat service performance
type VoiceChatServiceConfig struct {
	HTTPAddr       string        `json:"http_addr"`       // 16 bytes
	WebSocketAddr  string        `json:"ws_addr"`         // 16 bytes
	HealthAddr     string        `json:"health_addr"`     // 16 bytes
	PprofAddr      string        `json:"pprof_addr"`      // 16 bytes
	DBMaxOpenConns int           `json:"db_max_open_conns"` // 8 bytes
	ReadTimeout    time.Duration `json:"read_timeout"`    // 8 bytes
	WriteTimeout   time.Duration `json:"write_timeout"`   // 8 bytes
	MaxHeaderBytes int           `json:"max_header_bytes"` // 8 bytes
	MaxVoiceConnections int      `json:"max_voice_connections"` // 8 bytes
	AudioBufferSize    int       `json:"audio_buffer_size"`     // 8 bytes
	HeartbeatInterval  time.Duration `json:"heartbeat_interval"` // 8 bytes
	ProximityRadius    float64   `json:"proximity_radius"`   // 8 bytes
	MaxChannelSize     int       `json:"max_channel_size"`    // 8 bytes
}

func main() {
	logger := logrus.New()
	logger.SetFormatter(&logrus.JSONFormatter{
		TimestampFormat: time.RFC3339,
	})
	logger.SetLevel(logrus.InfoLevel)

	config := &VoiceChatServiceConfig{
		HTTPAddr:            getEnv("HTTP_ADDR", ":8088"),
		WebSocketAddr:       getEnv("WS_ADDR", ":8089"),
		HealthAddr:          getEnv("HEALTH_ADDR", ":8090"),
		PprofAddr:           getEnv("PPROF_ADDR", ":6868"),
		DBMaxOpenConns:      300, // OPTIMIZATION: Lower for voice chat service
		ReadTimeout:         30 * time.Second,
		WriteTimeout:        30 * time.Second,
		MaxHeaderBytes:      1 << 20, // 1MB
		MaxVoiceConnections: 5000,    // OPTIMIZATION: Support 5000+ concurrent voice connections
		AudioBufferSize:     4096,    // OPTIMIZATION: Audio buffer for low latency
		HeartbeatInterval:   5 * time.Second, // OPTIMIZATION: Frequent heartbeats for voice
		ProximityRadius:     25.0,    // OPTIMIZATION: Default proximity audio radius
		MaxChannelSize:      50,      // OPTIMIZATION: Max participants per channel
	}

	// OPTIMIZATION: Issue #2030 - Start pprof server for voice performance profiling
	go func() {
		logger.WithField("addr", config.PprofAddr).Info("pprof server starting")
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(config.PprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

	// Initialize voice chat service with performance optimizations
	srv, err := server.NewVoiceChatServer(config, logger)
	if err != nil {
		logger.WithError(err).Fatal("failed to initialize voice chat server")
	}

	// OPTIMIZATION: Issue #2030 - Health check endpoint with voice metrics
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

	// Start WebSocket server for voice streaming
	go func() {
		logger.WithField("addr", config.WebSocketAddr).Info("WebSocket voice server starting")
		wsSrv := &http.Server{
			Addr:    config.WebSocketAddr,
			Handler: srv.WebSocketRouter(),
		}
		if err := wsSrv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.WithError(err).Fatal("WebSocket voice server failed")
		}
	}()

	// Start main HTTP server
	go func() {
		logger.WithField("addr", config.HTTPAddr).Info("voice chat service starting")
		if err := httpSrv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.WithError(err).Fatal("voice chat server failed")
		}
	}()

	// OPTIMIZATION: Issue #2030 - Graceful shutdown with timeout
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("shutting down voice chat service...")

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpSrv.Shutdown(ctx); err != nil {
		logger.WithError(err).Error("server forced to shutdown")
	}

	logger.Info("voice chat service stopped")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}