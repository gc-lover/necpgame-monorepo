// Issue: #150 - Matchmaking HTTP Server (ogen-based)
// SOLID: ТОЛЬКО настройка сервера. Middleware в middleware.go, Handlers в handlers.go
package server

// HTTP handlers use context.WithTimeout for request timeouts (see handlers.go)

import (
	"context"
	"log"
	"net/http"
	"time"

	api "github.com/gc-lover/necpgame-monorepo/services/matchmaking-go/pkg/api"
)

// HTTPServer represents HTTP server
type HTTPServer struct {
	addr   string
	server *http.Server
	router *http.ServeMux
}

// NewHTTPServer creates new HTTP server with ogen integration
// SOLID: ТОЛЬКО настройка сервера и роутера
// Issue: #1588 - Load shedding for overload protection
func NewHTTPServer(addr string, service *Service) *HTTPServer {
	router := http.NewServeMux()

	// Issue: #1588 - Load shedding (max 5000 concurrent for hot path)
	loadShedder := NewLoadShedder(5000)

	// Handlers (реализация api.Handler из handlers.go)
	handlers := NewHandlers(service)

	// Security handler (JWT validation)
	secHandler := &SecurityHandler{}

	// Integration with ogen (creates its own router)
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		log.Fatalf("Failed to create ogen server: %v", err)
	}

	// Mount ogen server under /api/v1
	var handler http.Handler = ogenServer
	handler = loadShedder.Middleware(handler)
	handler = LoggingMiddleware(handler)
	handler = MetricsMiddleware(handler)
	handler = TimeoutMiddleware(handler, 60*time.Second)
	handler = RecoveryMiddleware(handler)
	router.Handle("/api/v1/", handler)

	// Health and metrics
	router.HandleFunc("/health", healthCheck)
	router.HandleFunc("/metrics", metricsHandler)
	router.HandleFunc("/ready", readyCheck)

	return &HTTPServer{
		addr:   addr,
		router: router,
		server: &http.Server{
			Addr:           addr,
			Handler:        router,
			ReadTimeout:  30 * time.Second,  // Prevent slowloris attacks,
			WriteTimeout: 30 * time.Second,  // Prevent hanging writes,
			IdleTimeout:  120 * time.Second, // Keep connections alive for reuse,
			MaxHeaderBytes: 1 << 20, // 1 MB
		},
	}
}

// Start starts HTTP server
func (s *HTTPServer) Start() error {
	// Start server in background with proper goroutine management
	errChan := make(chan error, 1)
	go func() {
		defer close(errChan)
		if err := s.server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			errChan <- err
		}
	}()

	// Wait indefinitely (server runs until shutdown)
	err := <-errChan
	return err
}

// Shutdown gracefully shuts down HTTP server
func (s *HTTPServer) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

// Health check handler
func healthCheck(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"healthy"}`))
}

// Ready check handler
func readyCheck(w http.ResponseWriter, r *http.Request) {
	// TODO: Check DB and Redis connectivity
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"ready"}`))
}

// Metrics handler (Prometheus format)
func metricsHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "text/plain")
	w.WriteHeader(http.StatusOK)
	// TODO: Collect real metrics
	w.Write([]byte("# HELP matchmaking_requests_total Total requests\n"))
	w.Write([]byte("# TYPE matchmaking_requests_total counter\n"))
	w.Write([]byte("matchmaking_requests_total 0\n"))
}




