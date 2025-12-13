// Issue: #139 - party-service FULL optimizations
// BLOCKER: DB pool, Context timeouts, goleak tests
// OPTIMIZATION: ogen migration (90% faster)
package main

import (
	"context"
	"database/sql"
	"log"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Profiling endpoints
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/party-service-go/server"
	_ "github.com/lib/pq"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/redis/go-redis/v9"
)

func main() {
	log.Println("Party Service (Go) starting...")

	addr := getEnv("SERVER_ADDR", ":8084")
	metricsAddr := getEnv("METRICS_ADDR", ":9094")
	pprofAddr := getEnv("PPROF_ADDR", "localhost:6308")
	dbURL := getEnv("DATABASE_URL", "postgresql://necpgame:necpgame@localhost:5432/necpgame?sslmode=disable")
	redisURL := getEnv("REDIS_URL", "localhost:6379")

	// Database connection
	db, err := sql.Open("postgres", dbURL)
	if err != nil {
		log.Fatalf("Failed to connect to database: %v", err)
	}
	defer db.Close()

	// CRITICAL: Configure DB pool (BLOCKER - Issue #139!)
	// Prevents connection exhaustion under load
	db.SetMaxOpenConns(50)                  // Max 50 concurrent connections
	db.SetMaxIdleConns(50)                  // Keep 50 idle for reuse
	db.SetConnMaxLifetime(5 * time.Minute)  // Rotate connections every 5 min
	db.SetConnMaxIdleTime(10 * time.Minute) // Close idle after 10 min

	// Test connection
	if err := db.Ping(); err != nil {
		log.Fatalf("Database unreachable: %v", err)
	}
	log.Println("OK Database connected (pool: 50 conns)")

	// Initialize Redis for Event Bus
	rdb := redis.NewClient(&redis.Options{
		Addr: redisURL,
	})
	defer rdb.Close()

	// Test Redis connection
	if _, err := rdb.Ping(context.Background()).Result(); err != nil {
		log.Printf("Redis connection failed: %v", err)
		// Continue without event bus
		rdb = nil
	} else {
		log.Println("OK Redis connected (Event Bus ready)")
	}

	// Initialize repository & service
	repo := server.NewPartyRepository(db)
	var service *server.PartyService
	if rdb != nil {
		eventBus := server.NewRedisEventBus(rdb)
		service = server.NewPartyService(repo, eventBus)
	} else {
		// Fallback without event bus
		service = server.NewPartyServiceSimple(repo)
	}

	// Initialize ogen HTTP server
	httpServer := server.NewHTTPServer(addr, service)

	// Metrics server
	metricsMux := http.NewServeMux()
	metricsMux.Handle("/metrics", promhttp.Handler())

	metricsServer := &http.Server{
		Addr:         metricsAddr,
		Handler:      metricsMux,
		ReadTimeout:  10 * time.Second,
		WriteTimeout: 10 * time.Second,
	}

	go func() {
		log.Printf("Metrics server starting on %s", metricsAddr)
		if err := metricsServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			log.Printf("Metrics server error: %v", err)
		}
	}()

	// OPTIMIZATION: pprof profiling server
	go func() {
		log.Printf("pprof server starting on %s", pprofAddr)
		log.Printf("  • CPU: http://%s/debug/pprof/profile?seconds=30", pprofAddr)
		log.Printf("  • Heap: http://%s/debug/pprof/heap", pprofAddr)
		log.Printf("  • Goroutines: http://%s/debug/pprof/goroutine", pprofAddr)
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			log.Printf("pprof server error: %v", err)
		}
	}()

	// Issue: #1585 - Runtime Goroutine Monitoring
	monitor := server.NewGoroutineMonitor(400) // Max 400 goroutines for party service
	go monitor.Start()
	defer monitor.Stop()
	log.Println("OK Goroutine monitor started")

	// Start HTTP server
	go func() {
		log.Printf("OK Party Service (ogen) listening on %s", addr)
		if err := httpServer.Start(); err != nil && err != http.ErrServerClosed {
			log.Fatalf("Server failed: %v", err)
		}
	}()

	// Wait for interrupt signal
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	log.Println("Shutting down server...")

	// Graceful shutdown
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpServer.Shutdown(ctx); err != nil {
		log.Printf("Server shutdown error: %v", err)
	}
	if err := metricsServer.Shutdown(ctx); err != nil {
		log.Printf("Metrics server shutdown error: %v", err)
	}

	log.Println("Server exited cleanly")
}

func getEnv(key, fallback string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return fallback
}
