// Issue: #1598, #1584
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

	"github.com/gc-lover/necpgame-monorepo/services/chat-service-go/server"
)

func main() {
	addr := getEnv("SERVER_ADDR", ":8200")
	dbConnStr := getEnv("DATABASE_URL", "postgres://necpgame:necpgame@localhost:5432/necpgame?sslmode=disable")

	repo, err := server.NewRepository(dbConnStr)
	if err != nil {
		log.Fatalf("Failed to initialize repository: %v", err)
	}
	defer repo.Close()

	service := server.NewService(repo)

	// Create configuration for JWT
	config := server.NewConfig()

	httpServer := server.NewHTTPServer(addr, service, config, log.Default())

	// OPTIMIZATION: Issue #1584 - Start pprof server for profiling
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6947")
		log.Printf("Starting pprof server on %s", pprofAddr)
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			log.Printf("pprof server error: %v", err)
		}
	}()

	// Issue: #1585 - Runtime Goroutine Monitoring
	monitor := server.NewGoroutineMonitor(200) // Max 200 goroutines for chat service
	go monitor.Start()
	defer monitor.Stop()
	log.Printf("OK Goroutine monitor started")

	stop := make(chan os.Signal, 1)
	signal.Notify(stop, os.Interrupt, syscall.SIGTERM)

	go func() {
		log.Printf("Starting Chat Service on %s", addr)
		if err := httpServer.Start(); err != nil {
			log.Fatalf("Server failed: %v", err)
		}
	}()

	<-stop
	log.Println("Shutting down gracefully...")

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpServer.Shutdown(ctx); err != nil {
		log.Printf("Shutdown error: %v", err)
	}

	log.Println("Server stopped")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
