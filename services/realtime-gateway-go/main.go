// Issue: #1580
// Real-time Gateway Service - UDP + WebSocket hybrid for MMOFPS game state
// Performance: UDP for game state (50-60% latency reduction), WebSocket for lobby/chat

package main

import (
	"context"
	"flag"
	"log"
	"net/http"
	"os"
	"os/signal"
	"sync"
	"syscall"

	"realtime-gateway-go/server"

	"github.com/prometheus/client_golang/prometheus/promhttp"
	"go.uber.org/zap"
)

var (
	httpAddr  = flag.String("http", ":8080", "HTTP server address")
	udpAddr   = flag.String("udp", ":18080", "UDP server address")
	redisAddr = flag.String("redis", "localhost:6379", "Redis server address")
)

func main() {
	flag.Parse()

	// Initialize structured logging
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to create logger: %v", err)
	}
	defer logger.Sync()

	logger.Info("Starting realtime-gateway-go service",
		zap.String("http_addr", *httpAddr),
		zap.String("udp_addr", *udpAddr),
		zap.String("redis_addr", *redisAddr))

	// Initialize spatial grid for interest management
	spatialGrid := server.NewSpatialGrid(50.0, logger) // 50m cell size

	// Initialize UDP server for game state
	udpServer, err := server.NewUDPServer(*udpAddr, spatialGrid, logger)
	if err != nil {
		logger.Fatal("Failed to create UDP server", zap.Error(err))
	}

	// Initialize WebSocket server for lobby/chat
	wsServer, err := server.NewWebSocketServer(*httpAddr, logger)
	if err != nil {
		logger.Fatal("Failed to create WebSocket server", zap.Error(err))
	}

	// Start UDP server
	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()

	var wg sync.WaitGroup

	// Start UDP server
	wg.Add(1)
	go func() {
		defer wg.Done()
		logger.Info("Starting UDP server", zap.String("addr", *udpAddr))
		if err := udpServer.Start(ctx); err != nil {
			logger.Error("UDP server failed", zap.Error(err))
		}
	}()

	// Start WebSocket server
	wg.Add(1)
	go func() {
		defer wg.Done()
		logger.Info("Starting WebSocket server", zap.String("addr", *httpAddr))

		// Add metrics endpoint
		http.Handle("/metrics", promhttp.Handler())
		http.Handle("/health", http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			w.WriteHeader(http.StatusOK)
			w.Write([]byte("OK"))
		}))

		if err := wsServer.Start(ctx); err != nil {
			logger.Error("WebSocket server failed", zap.Error(err))
		}
	}()

	// Graceful shutdown
	sigChan := make(chan os.Signal, 1)
	signal.Notify(sigChan, syscall.SIGINT, syscall.SIGTERM)

	logger.Info("Service started successfully")
	logger.Info("UDP game state server listening", zap.String("addr", *udpAddr))
	logger.Info("WebSocket lobby server listening", zap.String("addr", *httpAddr))

	<-sigChan
	logger.Info("Received shutdown signal, shutting down gracefully...")

	cancel()
	wg.Wait()

	logger.Info("Service shutdown complete")
}
