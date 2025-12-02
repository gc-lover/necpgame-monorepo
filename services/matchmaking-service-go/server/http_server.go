// Issue: #1579 - ogen router + middleware
package server

import (
	"context"
	"log"
	"net/http"

	"github.com/go-chi/chi/v5"
	"github.com/gc-lover/necpgame-monorepo/services/matchmaking-service-go/pkg/api"
)

// HTTPServer wraps ogen server
type HTTPServer struct {
	addr    string
	service Service
	server  *http.Server
}

// NewHTTPServer создает HTTP server с ogen
func NewHTTPServer(addr string, service Service) *HTTPServer {
	return &HTTPServer{
		addr:    addr,
		service: service,
	}
}

// Start запускает HTTP server
func (s *HTTPServer) Start() error {
	// Create Chi router
	router := chi.NewRouter()

	// Create ogen handlers
	handlers := NewHandlers(s.service)
	
	// Create ogen server (CRITICAL: pass handlers as both Handler and SecurityHandler)
	ogenServer, err := api.NewServer(handlers, handlers)
	if err != nil {
		return err
	}
	
	// Mount ogen server under /api/v1
	router.Mount("/api/v1", ogenServer)
	
	// Health check
	router.Get("/health", healthCheck)
	router.Get("/metrics", metricsHandler)
	
	// Wrap with CORS
	handler := withCORS(router)
	
	s.server = &http.Server{
		Addr:    s.addr,
		Handler: handler,
	}
	
	log.Printf("OK Matchmaking Service (ogen) listening on %s", s.addr)
	return s.server.ListenAndServe()
}

// Shutdown gracefully shuts down server
func (s *HTTPServer) Shutdown(ctx context.Context) error {
	if s.server != nil {
		return s.server.Shutdown(ctx)
	}
	return nil
}

// withCORS adds CORS headers
func withCORS(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization")
		
		if r.Method == http.MethodOptions {
			w.WriteHeader(http.StatusOK)
			return
		}
		
		next.ServeHTTP(w, r)
	})
}

func healthCheck(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"ok"}`))
}

func metricsHandler(w http.ResponseWriter, r *http.Request) {
	// TODO: Prometheus metrics
	w.WriteHeader(http.StatusOK)
}
