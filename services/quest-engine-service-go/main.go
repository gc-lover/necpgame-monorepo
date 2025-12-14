// Issue: #176
package main

import (
	"context"
	"log"
	"os"
	"os/signal"
	"strconv"
	"syscall"
	"time"

	"github.com/NECPGAME/quest-engine-service-go/server"
	"go.uber.org/zap"
)

func main() {
	// Initialize structured logging
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	sugar := logger.Sugar()

	// Create service instance with performance optimizations
	svc, err := server.NewService(server.Config{
		HTTPPort:         getEnvInt("HTTP_PORT", 8300),
		WebSocketPort:    getEnvInt("WS_PORT", 8301),
		DatabaseURL:      getEnv("DATABASE_URL", "postgres://user:pass@localhost:5432/necp_game"),
		KafkaBrokers:     getEnv("KAFKA_BROKERS", "localhost:9092"),
		RedisURL:         getEnv("REDIS_URL", "redis://localhost:6379"),
		PrometheusPort:   getEnvInt("PROMETHEUS_PORT", 9092),
		Environment:      getEnv("ENVIRONMENT", "development"),
		MaxConnections:   getEnvInt("MAX_CONNECTIONS", 10000),
		WorkerPoolSize:   getEnvInt("WORKER_POOL_SIZE", 100),
		BufferSize:       getEnvInt("BUFFER_SIZE", 8192),
		ContextTimeout:   time.Duration(getEnvInt("CONTEXT_TIMEOUT_SEC", 30)) * time.Second,
		ReadTimeout:      time.Duration(getEnvInt("READ_TIMEOUT_SEC", 10)) * time.Second,
		WriteTimeout:     time.Duration(getEnvInt("WRITE_TIMEOUT_SEC", 10)) * time.Second,
		MaxRequestSize:   getEnvInt("MAX_REQUEST_SIZE_MB", 1) * 1024 * 1024,
		RateLimitRPM:     getEnvInt("RATE_LIMIT_RPM", 1000),
		EnableMetrics:    getEnvBool("ENABLE_METRICS", true),
		EnableTracing:    getEnvBool("ENABLE_TRACING", false),
		EnableDebugLogs:  getEnvBool("ENABLE_DEBUG_LOGS", false),
	})
	if err != nil {
		sugar.Fatalf("Failed to create service: %v", err)
	}

	// Initialize service components
	ctx := context.Background()
	if err := svc.Initialize(ctx); err != nil {
		sugar.Fatalf("Failed to initialize service: %v", err)
	}

	// Start HTTP server
	httpServer := svc.HTTPServer()
	go func() {
		sugar.Infof("Starting HTTP server on port %d", svc.Config().HTTPPort)
		if err := httpServer.ListenAndServe(); err != nil {
			sugar.Errorf("HTTP server error: %v", err)
		}
	}()

	// Start WebSocket server
	wsServer := svc.WebSocketServer()
	go func() {
		sugar.Infof("Starting WebSocket server on port %d", svc.Config().WebSocketPort)
		if err := wsServer.ListenAndServe(); err != nil {
			sugar.Errorf("WebSocket server error: %v", err)
		}
	}()

	// Start metrics server
	if svc.Config().EnableMetrics {
		metricsServer := svc.MetricsServer()
		go func() {
			sugar.Infof("Starting metrics server on port %d", svc.Config().PrometheusPort)
			if err := metricsServer.ListenAndServe(); err != nil {
				sugar.Errorf("Metrics server error: %v", err)
			}
		}()
	}

	// Wait for interrupt signal to gracefully shutdown the server
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	sugar.Info("Shutting down servers...")

	// Graceful shutdown with timeout
	shutdownCtx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := svc.Shutdown(shutdownCtx); err != nil {
		sugar.Errorf("Server forced to shutdown: %v", err)
	}

	sugar.Info("Servers exited")
}

// Helper functions for environment variables
func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

func getEnvInt(key string, defaultValue int) int {
	if value := os.Getenv(key); value != "" {
		if intValue, err := strconv.Atoi(value); err == nil {
			return intValue
		}
	}
	return defaultValue
}

func getEnvBool(key string, defaultValue bool) bool {
	if value := os.Getenv(key); value != "" {
		if boolValue, err := strconv.ParseBool(value); err == nil {
			return boolValue
		}
	}
	return defaultValue
}