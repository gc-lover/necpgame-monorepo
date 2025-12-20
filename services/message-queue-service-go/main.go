package main

import (
	"context"
	"net/http"
	"net/http/pprof"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/message-queue-service-go/server"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/sirupsen/logrus"
)

// OPTIMIZATION: Issue #2143 - Memory-aligned struct for message queue service performance
type MessageQueueServiceConfig struct {
	HTTPAddr       string        `json:"http_addr"`       // 16 bytes
	RabbitMQAddr   string        `json:"rabbitmq_addr"`   // 16 bytes
	RedisAddr      string        `json:"redis_addr"`      // 16 bytes
	PprofAddr      string        `json:"pprof_addr"`      // 16 bytes
	HealthAddr     string        `json:"health_addr"`     // 16 bytes
	ReadTimeout    time.Duration `json:"read_timeout"`    // 8 bytes
	WriteTimeout   time.Duration `json:"write_timeout"`   // 8 bytes
	MaxHeaderBytes int           `json:"max_header_bytes"` // 8 bytes
	MaxConnections int           `json:"max_connections"` // 8 bytes
	QueueBufferSize int          `json:"queue_buffer_size"` // 8 bytes
	MessageBatchSize int         `json:"message_batch_size"` // 8 bytes
	ConsumerTimeout time.Duration `json:"consumer_timeout"` // 8 bytes
	HeartbeatInterval time.Duration `json:"heartbeat_interval"` // 8 bytes
}

func main() {
	logger := logrus.New()
	logger.SetFormatter(&logrus.JSONFormatter{
		TimestampFormat: time.RFC3339,
	})
	logger.SetLevel(logrus.InfoLevel)

	config := &MessageQueueServiceConfig{
		HTTPAddr:          getEnv("HTTP_ADDR", ":8080"),
		RabbitMQAddr:      getEnv("RABBITMQ_ADDR", "amqp://guest:guest@localhost:5672/"),
		RedisAddr:         getEnv("REDIS_ADDR", "localhost:6379"),
		PprofAddr:         getEnv("PPROF_ADDR", ":6867"),
		HealthAddr:        getEnv("HEALTH_ADDR", ":8081"),
		ReadTimeout:       30 * time.Second,
		WriteTimeout:      30 * time.Second,
		MaxHeaderBytes:    1 << 20, // 1MB
		MaxConnections:    10000,   // OPTIMIZATION: Support 10k+ concurrent connections
		QueueBufferSize:   1000,    // OPTIMIZATION: Buffer for message queuing
		MessageBatchSize:  100,     // OPTIMIZATION: Batch processing for throughput
		ConsumerTimeout:   30 * time.Second,
		HeartbeatInterval: 10 * time.Second, // OPTIMIZATION: RabbitMQ heartbeat
	}

	// OPTIMIZATION: Issue #2143 - Start pprof server for message queue performance profiling
	go func() {
		logger.WithField("addr", config.PprofAddr).Info("pprof server starting")
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(config.PprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

	// Initialize message queue service with performance optimizations
	srv, err := server.NewMessageQueueServer(config, logger)
	if err != nil {
		logger.WithError(err).Fatal("failed to initialize message queue server")
	}

	// OPTIMIZATION: Issue #2143 - Health check endpoint with queue metrics
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
		logger.WithField("addr", config.HTTPAddr).Info("message queue service starting")
		if err := httpSrv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.WithError(err).Fatal("message queue server failed")
		}
	}()

	// OPTIMIZATION: Issue #2143 - Graceful shutdown with timeout
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("shutting down message queue service...")

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpSrv.Shutdown(ctx); err != nil {
		logger.WithError(err).Error("server forced to shutdown")
	}

	logger.Info("message queue service stopped")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
