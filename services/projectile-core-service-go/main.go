// Issue: #1560, #1584

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

	"github.com/gc-lover/necpgame-monorepo/services/projectile-core-service-go/server"
)

func main() {
	// Configuration
	addr := getEnv("SERVER_ADDR", ":8091")
	_ = getEnv("DATABASE_URL", "postgres://necpg:necpg@localhost:5432/necpg?sslmode=disable") // TODO: Use DB connection

	// Initialize server
	repo := server.NewProjectileRepository(nil) // TODO: Add DB connection
	service := server.NewProjectileService(repo)
	srv := server.NewHTTPServer(addr, service)

	// OPTIMIZATION: Issue #1584 - Start pprof server for profiling
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6116")
		log.Printf("pprof server starting on %s", pprofAddr)
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			log.Printf("pprof server error: %v", err)
		}
	}()

	// Start server
	go func() {
		log.Printf("🚀 Projectile Core Service starting on %s", addr)
		if err := srv.Start(); err != nil && err != http.ErrServerClosed {
			log.Fatalf("Server failed: %v", err)
		}
	}()

	// Graceful shutdown
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	log.Println("🛑 Shutting down server...")

	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		log.Fatalf("Server forced to shutdown: %v", err)
	}

	log.Println("OK Server stopped gracefully")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}









