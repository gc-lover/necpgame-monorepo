// Issue: #1579 - ogen router + middleware
package server

import (
	"context"
	"log"
	"net/http"

	"github.com/gc-lover/necpgame-monorepo/services/matchmaking-service-go/pkg/api"
)

// HTTPServer wraps ogen server
type HTTPServer struct {
	addr        string
	service     Service
	server      *http.Server
	loadShedder *LoadShedder // Issue: #1588 - Resilience patterns
}

// NewHTTPServer создает HTTP server с ogen
func NewHTTPServer(addr string, service Service) *HTTPServer {
	return &HTTPServer{
		addr:        addr,
		service:     service,
		loadShedder: NewLoadShedder(3000), // Max 3000 concurrent (5k RPS service)
	}
}

// Start запускает HTTP server
func (s *HTTPServer) Start() error {
	// Create ogen handlers
	handlers := NewHandlers(s.service)
	
	// Create ogen server (CRITICAL: pass handlers as both Handler and SecurityHandler)
	ogenServer, err := api.NewServer(handlers, handlers)
	if err != nil {
		return err
	}
	
	mux := http.NewServeMux()
	var handler http.Handler = ogenServer
	handler = s.loadSheddingMiddleware(handler)
	handler = withCORS(handler)
	mux.Handle("/api/v1", handler)
	mux.HandleFunc("/health", healthCheck)
	mux.HandleFunc("/metrics", metricsHandler)
	
	s.server = &http.Server{
		Addr:    s.addr,
		Handler: mux,
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

// loadSheddingMiddleware prevents overload by limiting concurrent requests
// Issue: #1588 - Resilience patterns
func (s *HTTPServer) loadSheddingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if !s.loadShedder.Allow() {
			w.WriteHeader(http.StatusServiceUnavailable)
			w.Write([]byte(`{"error":"service overloaded, please try again later"}`))
			return
		}
		defer s.loadShedder.Done()
		next.ServeHTTP(w, r)
	})
}
