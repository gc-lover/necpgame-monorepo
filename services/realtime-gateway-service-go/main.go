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

	"github.com/gc-lover/necp-game/services/realtime-gateway-service-go/internal/service"
)

func main() {
	// Initialize logger
	logger, err := zap.NewProduction()
	if err != nil {
		fmt.Fprintf(os.Stderr, "failed to initialize logger: %v\n", err)
		os.Exit(1)
	}
	defer logger.Sync()

	logger.Info("Starting Realtime Gateway Service")

	// Parse environment variables
	addr := os.Getenv("HTTP_ADDR")
	if addr == "" {
		addr = ":8086"
	}

	metricsAddr := os.Getenv("METRICS_ADDR")
	if metricsAddr == "" {
		metricsAddr = ":9090"
	}

	wsAddr := os.Getenv("WS_ADDR")
	if wsAddr == "" {
		wsAddr = ":8087"
	}

	udpAddr := os.Getenv("UDP_ADDR")
	if udpAddr == "" {
		udpAddr = ":7777" // NEW: UDP port for real-time game state
	}

	// Initialize OpenTelemetry metrics
	otel.SetMeterProvider(initMeterProvider())

	// Initialize service
	svc, err := service.NewService(service.Config{
		HTTPAddr:       addr,
		WebSocketAddr:  wsAddr,
		UDPAddr:        udpAddr, // NEW: UDP transport for game state
		DatabaseURL:    os.Getenv("DATABASE_URL"),
		RedisURL:       os.Getenv("REDIS_URL"),
		KafkaBrokers:   os.Getenv("KAFKA_BROKERS"),
		EventStoreURL:  os.Getenv("EVENT_STORE_URL"),
		Logger:         logger,
		Meter:          otel.Meter("realtime-gateway"),
	})
	if err != nil {
		logger.Fatal("failed to initialize service", zap.Error(err))
	}

	// Initialize HTTP server
	httpServer := &http.Server{
		Addr:    addr,
		Handler: svc.HTTPHandler(),
	}

	// Initialize WebSocket server
	wsServer := &http.Server{
		Addr:    wsAddr,
		Handler: svc.WebSocketHandler(),
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
		logger.Info("starting WebSocket server", zap.String("addr", wsAddr))
		if err := wsServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("failed to start WebSocket server", zap.Error(err))
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

	// Shutdown servers
	shutdownServers(ctx, logger, httpServer, "HTTP")
	shutdownServers(ctx, logger, wsServer, "WebSocket")
	shutdownServers(ctx, logger, metricsServer, "metrics")

	logger.Info("servers stopped")
}

func shutdownServers(ctx context.Context, logger *zap.Logger, server *http.Server, name string) {
	if err := server.Shutdown(ctx); err != nil {
		logger.Error(fmt.Sprintf("failed to shutdown %s server", name), zap.Error(err))
	}
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
