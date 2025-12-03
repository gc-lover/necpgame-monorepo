// Issue: #MOVEMENT_OPTIMIZATION
// Movement Service - UDP + Protobuf Entry Point
// Performance: Real-time movement with <20ms latency, >1000 updates/sec
package main

import (
	"context"
	"log"
	"os"
	"os/signal"
	"syscall"

	"github.com/gc-lover/necpgame-monorepo/services/movement-service-go/server"
)

func main() {
	// Configuration
	udpAddr := getEnv("UDP_ADDR", ":9001")
	httpAddr := getEnv("HTTP_ADDR", ":8091") // For health/metrics only

	log.Println("🚀 Starting Movement Service (UDP + Protobuf)...")
	log.Printf("📡 UDP Address: %s (real-time movement)", udpAddr)
	log.Printf("🌐 HTTP Address: %s (health/metrics only)", httpAddr)

	// Initialize movement service
	movementService := server.NewMovementService()
	log.Println("OK Movement service initialized")

	// Create UDP server
	udpServer, err := server.NewUDPServer(udpAddr, movementService)
	if err != nil {
		log.Fatalf("Failed to create UDP server: %v", err)
	}
	log.Println("OK UDP server created")

	// Context for graceful shutdown
	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()

	// Start UDP server
	go func() {
		log.Println("🎮 Movement Service running...")
		log.Println("📊 Performance targets:")
		log.Println("   - Tick rate: 128 Hz (adaptive)")
		log.Println("   - Latency: <20ms P99")
		log.Println("   - Throughput: >1000 updates/sec")
		log.Println("   - Bandwidth: ~10 KB/s per player")
		
		if err := udpServer.Start(ctx); err != nil {
			log.Fatalf("UDP server failed: %v", err)
		}
	}()

	// Optional: Start HTTP server for health/metrics
	// (Keep REST API for admin/metrics, use UDP for game state)
	go func() {
		// TODO: Start minimal HTTP server for /health, /metrics
	}()

	// Graceful shutdown
	stop := make(chan os.Signal, 1)
	signal.Notify(stop, os.Interrupt, syscall.SIGTERM)

	<-stop
	log.Println("WARNING  Shutting down gracefully...")

	cancel()
	udpServer.Close()

	log.Println("OK Movement Service stopped")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

