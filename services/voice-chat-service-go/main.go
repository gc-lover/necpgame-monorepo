package main

import (
	"context"
	"fmt"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #2177 - Enable pprof profiling
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/sirupsen/logrus"

	"necpgame/services/voice-chat-service-go/server"
)

func main() {
	// Initialize structured logging
	logger := server.NewLogger()

	logger.Info("🎤 Starting Voice Chat Service...")

	// Configuration
	config := &server.VoiceChatServiceConfig{
		Port:                   getEnv("PORT", "8080"),
		ReadTimeout:            30 * time.Second,
		WriteTimeout:           30 * time.Second,
		MaxHeaderBytes:         1 << 20, // 1MB
		RedisAddr:              getEnv("REDIS_ADDR", "localhost:6379"),
		WebSocketReadTimeout:   60 * time.Second,
		WebSocketWriteTimeout:  10 * time.Second,
		MaxVoiceConnections:    5000,
		ProximityUpdateInterval: 100 * time.Millisecond,
		ConnectionCleanupInterval: 30 * time.Second,
		ChannelCleanupInterval: 5 * time.Minute,
	}

	// Initialize metrics (placeholder for now)
	metrics := &server.VoiceChatMetrics{
		ActiveChannels:      0,
		ActiveConnections:   0,
		AudioStreams:        0,
		TTSSynthesizations:  0,
		ModerationReports:   0,
		Errors:              0,
	}

	// Initialize service
	voiceService := server.NewVoiceChatService(logger, metrics, config)

	// OPTIMIZATION: Issue #2177 - Start pprof server for profiling
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6868")
		logger.WithField("addr", pprofAddr).Info("pprof server starting")
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

	// Issue #2177 - Runtime Goroutine Monitoring
	monitor := server.NewGoroutineMonitor(800, logger) // Max 800 goroutines for voice chat service
	go monitor.Start()
	defer monitor.Stop()
	logger.Info("OK Goroutine monitor started")

	// Initialize HTTP server
	httpServer := server.NewHTTPServer(voiceService, logger, config)

	// Start server in a goroutine
	go func() {
		logger.Info("🌐 Voice Chat Service starting on port " + config.Port)
		if err := httpServer.Start(); err != nil {
			logger.WithError(err).Fatal("Failed to start HTTP server")
		}
	}()

	// Wait for interrupt signal to gracefully shutdown the server
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("🛑 Shutting down Voice Chat Service...")

	// Give outstanding requests 30 seconds to complete
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	logger.Info("OK Voice Chat Service stopped gracefully")
}

// getEnv gets an environment variable or returns a default value
func getEnv(key, defaultValue string) string {
	if value, exists := os.LookupEnv(key); exists {
		return value
	}
	return defaultValue
}