// Issue: #1595
package server

import (
	"context"
	"net/http"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/gc-lover/necpgame-monorepo/services/combat-damage-service-go/pkg/api"
)

// HTTPServer represents HTTP server
type HTTPServer struct {
	addr        string
	server      *http.Server
	router      chi.Router
	loadShedder *LoadShedder // Issue: #1588 - Resilience patterns
}

// NewHTTPServer creates new HTTP server
func NewHTTPServer(addr string) *HTTPServer {
	router := chi.NewRouter()

	// Built-in middleware
	router.Use(middleware.Logger)
	router.Use(middleware.Recoverer)
	router.Use(middleware.RequestID)

	// Issue: #1588 - Load shedding middleware (prevent overload)
	loadShedder := NewLoadShedder(1500) // Max 1500 concurrent (3k RPS service)
	router.Use(loadSheddingMiddleware(loadShedder))

	// Custom middleware
	router.Use(LoggingMiddleware)
	router.Use(MetricsMiddleware)

	// Handlers (реализация api.Handler из handlers.go)
	handlers := NewHandlers()

	// Integration with ogen (creates its own Chi router)
	secHandler := &SecurityHandler{}
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		panic(err)
	}

	// Mount ogen server under /api/v1
	router.Mount("/api/v1", ogenServer)

	// Health check
	router.Get("/health", healthCheck)
	router.Get("/metrics", metricsHandler)

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
