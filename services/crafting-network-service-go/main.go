//go:align 64
// Issue: #2286

package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"runtime/debug"
	"strconv"
	"strings"
	"syscall"
	"time"

	"necpgame/services/crafting-network-service-go/server"
)

func main() {
	// PERFORMANCE: Optimize GC for MMOFPS crafting network
	if gcPercent := os.Getenv("GOGC"); gcPercent == "" {
		debug.SetGCPercent(50) // Reduce GC pressure for high-frequency crafting updates
	}

	// PERFORMANCE: Pre-allocate worker pools for concurrent crafting sessions
	const maxCraftingWorkers = 200
	craftingWorkerPool := make(chan struct{}, maxCraftingWorkers)

	// Network ports configuration
	wsPort := getEnvInt("WEBSOCKET_PORT", 8081)
	udpPort := getEnvInt("UDP_PORT", 8082)

	logger := log.New(os.Stdout, "[crafting-network] ", log.LstdFlags|log.Lmicroseconds)

	// Initialize server with enterprise-grade optimizations
	srv := server.NewCraftingNetworkServer(&server.Config{
		MaxWorkers:     maxCraftingWorkers,
		WorkerPool:     craftingWorkerPool,
		CacheTTL:       10 * time.Minute,
		ReadTimeout:    15 * time.Second,
		WriteTimeout:   15 * time.Second,
		IdleTimeout:    60 * time.Second,
		MaxHeaderBytes: 1 << 16,
		WebSocketPort:  wsPort,
		UDPPort:        udpPort,
	})

	// PERFORMANCE: Configure HTTP server for low latency real-time crafting
	// Create mux to handle WebSocket routes before ogen server
	mux := http.NewServeMux()

	// WebSocket handlers for real-time crafting
	mux.HandleFunc("/ws/crafting/", func(w http.ResponseWriter, r *http.Request) {
		// Extract session_id from path: /ws/crafting/{session_id}
		path := strings.TrimPrefix(r.URL.Path, "/ws/crafting/")
		sessionID := strings.TrimSuffix(path, "/")

		if sessionID == "" {
			http.Error(w, "session_id is required", http.StatusBadRequest)
			return
		}

		playerID := r.URL.Query().Get("player_id")
		if playerID == "" {
			http.Error(w, "player_id is required", http.StatusBadRequest)
			return
		}

		// Create WebSocket connection using server's WebSocket manager
		srv.GetWebSocketManager().HandleCraftingSessionWebSocket(w, r, sessionID, playerID)
	})

	mux.HandleFunc("/ws/queue/", func(w http.ResponseWriter, r *http.Request) {
		// Extract player_id from path: /ws/queue/{player_id}
		path := strings.TrimPrefix(r.URL.Path, "/ws/queue/")
		playerID := strings.TrimSuffix(path, "/")

		if playerID == "" {
			http.Error(w, "player_id is required", http.StatusBadRequest)
			return
		}

		// Create WebSocket connection using server's WebSocket manager
		srv.GetWebSocketManager().HandleCraftingQueueWebSocket(w, r, playerID)
	})

	// Handle UDP endpoints
	mux.HandleFunc("/udp/sessions/", func(w http.ResponseWriter, r *http.Request) {
		if r.Method != http.MethodPost {
			http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
			return
		}

		path := strings.TrimPrefix(r.URL.Path, "/udp/sessions/")
		parts := strings.Split(path, "/")

		if len(parts) < 2 {
			http.Error(w, "Invalid path", http.StatusBadRequest)
			return
		}

		sessionID := parts[0]
		action := parts[1]

		switch action {
		case "connect":
			srv.GetUDPManager().HandleUDPConnect(w, r, sessionID)
		case "progress":
			srv.GetUDPManager().HandleUDPProgress(w, r, sessionID)
		case "materials":
			srv.GetUDPManager().HandleUDPMaterials(w, r, sessionID)
		default:
			http.Error(w, "Unknown action", http.StatusNotFound)
		}
	})

	// Default handler for all other routes goes to ogen server
	mux.Handle("/", srv.Handler())

	httpSrv := &http.Server{
		Addr:           getEnv("SERVER_ADDR", ":8080"),
		Handler:        mux,
		ReadTimeout:    srv.Config().ReadTimeout,
		WriteTimeout:   srv.Config().WriteTimeout,
		IdleTimeout:    srv.Config().IdleTimeout,
		MaxHeaderBytes: srv.Config().MaxHeaderBytes,
	}

	// Graceful shutdown handling
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)

	// Start integrated HTTP/WebSocket/UDP server in background
	go func() {
		logger.Printf("Starting Crafting Network Service on %s (GOGC=%d, Workers=%d)",
			httpSrv.Addr, debug.SetGCPercent(-1), maxCraftingWorkers)
		logger.Printf("WebSocket endpoints: /ws/crafting/*, /ws/queue/*")
		logger.Printf("UDP endpoints: /udp/sessions/* (simulation), UDP port: %d", srv.Config().UDPPort)

		if err := httpSrv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatalf("Server error: %v", err)
		}
	}()

	// Start UDP manager for high-frequency crafting updates (real UDP)
	go func() {
		logger.Printf("Starting Crafting Network UDP Service on port %d", srv.Config().UDPPort)
		if err := srv.StartUDPManager(context.Background()); err != nil {
			logger.Fatalf("UDP manager error: %v", err)
		}
	}()

	// Wait for shutdown signal
	<-quit
	logger.Println("Shutting down Crafting Network Service...")

	// PERFORMANCE: Graceful shutdown with timeout for active crafting sessions
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpSrv.Shutdown(ctx); err != nil {
		logger.Printf("Server forced to shutdown: %v", err)
	}

	logger.Println("Crafting Network Service exited")
}

// getEnv gets environment variable with fallback
func getEnv(key, fallback string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return fallback
}

// getEnvInt gets environment variable as int with fallback
func getEnvInt(key string, fallback int) int {
	if value := os.Getenv(key); value != "" {
		if intVal, err := strconv.Atoi(value); err == nil {
			return intVal
		}
	}
	return fallback
}