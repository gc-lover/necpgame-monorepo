package main

import (
	"context"
	"fmt"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/go-faster/errors"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"go.opentelemetry.io/otel"
	"go.opentelemetry.io/otel/exporters/prometheus"
	"go.opentelemetry.io/otel/sdk/metric"
	"go.uber.org/zap"

	"github.com/gc-lover/necp-game/services/data-synchronization-service-go/internal/service"
)

func main() {
	// Initialize logger
	logger, err := zap.NewProduction()
	if err != nil {
		fmt.Fprintf(os.Stderr, "failed to initialize logger: %v\n", err)
		os.Exit(1)
	}
	defer logger.Sync()

	logger.Info("Starting Data Synchronization Service")

	// Parse environment variables
	addr := os.Getenv("HTTP_ADDR")
	if addr == "" {
		addr = ":8080"
	}

	metricsAddr := os.Getenv("METRICS_ADDR")
	if metricsAddr == "" {
		metricsAddr = ":9090"
	}

	// Initialize OpenTelemetry metrics
	otel.SetMeterProvider(initMeterProvider())

	// Initialize service
	svc, err := service.NewService(service.Config{
		DatabaseURL:      os.Getenv("DATABASE_URL"),
		RedisURL:         os.Getenv("REDIS_URL"),
		KafkaBrokers:     os.Getenv("KAFKA_BROKERS"),
		EventStoreURL:    os.Getenv("EVENT_STORE_URL"),
		Logger:           logger,
	})
	if err != nil {
		logger.Fatal("failed to initialize service", zap.Error(err))
	}

	// Initialize HTTP server with enterprise-grade timeouts for MMOFPS data sync operations
	httpServer := &http.Server{
		Addr:              addr,
		Handler:           svc.Handler(),
		ReadTimeout:       15 * time.Second, // BACKEND NOTE: Increased for complex sync operations
		WriteTimeout:      15 * time.Second, // BACKEND NOTE: For sync response generation
		IdleTimeout:       120 * time.Second, // BACKEND NOTE: Keep connections alive for sync sessions
		ReadHeaderTimeout: 3 * time.Second, // BACKEND NOTE: Fast header processing for sync requests
		MaxHeaderBytes:    1 << 20, // BACKEND NOTE: 1MB max headers for security
	}

	// Initialize metrics server
	metricsMux := http.NewServeMux()
	metricsMux.Handle("/metrics", promhttp.Handler())
	metricsServer := &http.Server{
		Addr:    metricsAddr,
		Handler: metricsMux,
	}

	// Start servers
	go func() {
		logger.Info("starting HTTP server", zap.String("addr", addr))
		if err := httpServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("failed to start HTTP server", zap.Error(err))
		}
	}()

	go func() {
		logger.Info("starting metrics server", zap.String("addr", metricsAddr))
		if err := metricsServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("failed to start metrics server", zap.Error(err))
		}
	}()

	// Start service components
	if err := svc.Start(context.Background()); err != nil {
		logger.Fatal("failed to start service", zap.Error(err))
	}

	// Wait for shutdown signal
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("shutting down servers...")

	// Graceful shutdown
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	// Stop service components
	if err := svc.Stop(ctx); err != nil {
		logger.Error("failed to stop service", zap.Error(err))
	}

	// Shutdown HTTP server
	if err := httpServer.Shutdown(ctx); err != nil {
		logger.Error("failed to shutdown HTTP server", zap.Error(err))
	}

	// Shutdown metrics server
	if err := metricsServer.Shutdown(ctx); err != nil {
		logger.Error("failed to shutdown metrics server", zap.Error(err))
	}

	logger.Info("servers stopped")
}

func initMeterProvider() *metric.MeterProvider {
	exporter, err := prometheus.New()
	if err != nil {
		panic(err)
	}

	meterProvider, err := metric.NewMeterProvider(
		metric.WithReader(exporter),
	)
	if err != nil {
		panic(err)
	}

	return meterProvider
}
