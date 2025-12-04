// Issue: #150, #1584 - Matchmaking Service Main Entry Point (ogen-based)
package main

import (
	"context"
	"log"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling endpoints
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/matchmaking-go/server"
	"github.com/grafana/pyroscope-go" // Issue: #1611 - Continuous Profiling
)

func main() {
	// Issue: #1611 - Continuous Profiling (Pyroscope)
	pyroscope.Start(pyroscope.Config{
		ApplicationName: "necpgame.matchmaking",
		ServerAddress:   getEnv("PYROSCOPE_SERVER", "http://pyroscope:4040"),
		ProfileTypes: []pyroscope.ProfileType{
			pyroscope.ProfileCPU,
			pyroscope.ProfileAllocObjects,
			pyroscope.ProfileAllocSpace,
			pyroscope.ProfileInuseObjects,
			pyroscope.ProfileInuseSpace,
		},
		Tags: map[string]string{
			"environment": getEnv("ENV", "development"),
			"version":     getEnv("VERSION", "unknown"),
		},
		UploadRate: 10 * time.Second, // 10 seconds
	})
	log.Println("OK Pyroscope continuous profiling started")

	// OPTIMIZATION: Issue #1584 - Start pprof server for profiling
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6498")
		log.Printf("Starting pprof server on %s", pprofAddr)
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			log.Printf("pprof server error: %v", err)
		}
	}()

	// Configuration from environment
	addr := getEnv("SERVER_ADDR", ":8090")
	dbConnStr := getEnv("DATABASE_URL", "postgres://necpgame:necpgame@localhost:5432/necpgame?sslmode=disable")
	redisAddr := getEnv("REDIS_ADDR", "localhost:6379")

	log.Println("Starting Matchmaking Service...")
	log.Printf("Server Address: %s", addr)
	log.Printf("Database: %s", dbConnStr)
	log.Printf("Redis: %s", redisAddr)

	// Initialize repository (DB pool: 25-50 connections)
	repo, err := server.NewRepository(dbConnStr)
	if err != nil {
		log.Fatalf("Failed to initialize repository: %v", err)
	}
	defer repo.Close()
	log.Println("OK Database connected")

	// Initialize cache manager (Redis)
	cache := server.NewCacheManager(redisAddr)
	defer cache.Close()
	log.Println("OK Redis connected")

	// Initialize service (with memory pooling, skill buckets)
	service := server.NewService(repo, cache)
	log.Println("OK Service initialized")

	// OPTIMIZATION: Issue #1585 - Runtime goroutine leak monitoring
	goroutineMonitor := server.NewGoroutineMonitor(500) // Max 500 goroutines for matchmaking service
	go goroutineMonitor.Start()
	defer goroutineMonitor.Stop()

	// Create HTTP server (ogen-based)
	httpServer := server.NewHTTPServer(addr, service)
	log.Println("OK HTTP server created")

	// Graceful shutdown
	stop := make(chan os.Signal, 1)
	signal.Notify(stop, os.Interrupt, syscall.SIGTERM)

	// Start server
	go func() {
		log.Printf("🚀 Matchmaking Service listening on %s", addr)
		log.Println("📊 Endpoints:")
		log.Println("  - POST   /api/v1/matchmaking/queue")
		log.Println("  - GET    /api/v1/matchmaking/queue/{queueId}")
		log.Println("  - DELETE /api/v1/matchmaking/queue/{queueId}")
		log.Println("  - GET    /api/v1/matchmaking/rating/{player_id}")
		log.Println("  - GET    /api/v1/matchmaking/leaderboard/{activityType}")
		log.Println("  - GET    /health")
		log.Println("  - GET    /metrics")

		if err := httpServer.Start(); err != nil {
			log.Fatalf("Server failed: %v", err)
		}
	}()

	// Wait for shutdown signal
	<-stop
	log.Println("WARNING  Shutting down gracefully...")

	// Shutdown timeout
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpServer.Shutdown(ctx); err != nil {
		log.Printf("❌ Shutdown error: %v", err)
	}

	log.Println("OK Server stopped")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
