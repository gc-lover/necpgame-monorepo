// Issue: #1595
package server

import (
	"context"
	"net/http"

	"github.com/gc-lover/necpgame-monorepo/services/combat-damage-service-go/pkg/api"
)

// HTTPServer represents HTTP server
type HTTPServer struct {
	addr        string
	server      *http.Server
	router      *http.ServeMux
	loadShedder *LoadShedder // Issue: #1588 - Resilience patterns
}

// NewHTTPServer creates new HTTP server
func NewHTTPServer(addr string) *HTTPServer {
	router := http.NewServeMux()

	// Handlers (реализация api.Handler из handlers.go)
	handlers := NewHandlers()

	// Integration with ogen (creates its own Chi router)
	secHandler := &SecurityHandler{}
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		panic(err)
	}

	// Mount ogen server under /api/v1
	var handler http.Handler = ogenServer
	loadShedder := NewLoadShedder(1500) // Max 1500 concurrent (3k RPS service)
	handler = loadSheddingMiddleware(loadShedder)(handler)
	handler = LoggingMiddleware(handler)
	handler = MetricsMiddleware(handler)
	handler = recoverMiddleware(handler)
	router.Handle("/api/v1/", handler)

	// Health check
	router.HandleFunc("/health", healthCheck)
	router.HandleFunc("/metrics", metricsHandler)

	return &HTTPServer{
		addr:        addr,
		router:      router,
		loadShedder: loadShedder,
		server: &http.Server{
			Addr:    addr,
			Handler: router,
		},
	}
}

// Start starts HTTP server
func (s *HTTPServer) Start() error {
	return s.server.ListenAndServe()
}

// Shutdown gracefully shuts down HTTP server
func (s *HTTPServer) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

// Health check handler
func healthCheck(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("OK"))
}

// Metrics handler (stub)
func metricsHandler(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("# HELP combat_damage_service metrics\n"))
}

// loadSheddingMiddleware prevents overload by limiting concurrent requests
// Issue: #1588 - Resilience patterns
func loadSheddingMiddleware(ls *LoadShedder) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			if !ls.Allow() {
				w.WriteHeader(http.StatusServiceUnavailable)
				w.Write([]byte(`{"error":"service overloaded, please try again later"}`))
				return
			}
			defer ls.Done()
			next.ServeHTTP(w, r)
		})
	}
}

func recoverMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		defer func() {
			if rec := recover(); rec != nil {
				http.Error(w, "internal error", http.StatusInternalServerError)
			}
		}()
		next.ServeHTTP(w, r)
	})
}
