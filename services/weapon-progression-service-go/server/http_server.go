// Issue: #1574
package server

import (
	"context"
	"database/sql"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/gc-lover/necpgame-monorepo/services/weapon-progression-service-go/pkg/api"
	"github.com/prometheus/client_golang/prometheus/promhttp"
)

// HTTPServer represents HTTP server
type HTTPServer struct {
	addr    string
	router  chi.Router
	service *Service
	server  *http.Server
}

// NewHTTPServer creates HTTP server with DI
func NewHTTPServer(addr string, db *sql.DB) *HTTPServer {
	router := chi.NewRouter()

	// Create dependencies
	repo := NewRepository(db)
	service := NewService(repo)
	handlers := NewHandlers(service)

	// Apply middleware
	router.Use(middleware.Logger)
	router.Use(middleware.Recoverer)
	router.Use(middleware.RequestID)
	router.Use(LoggingMiddleware)
	router.Use(RecoveryMiddleware)
	router.Use(CORSMiddleware)

	// Integration with ogen
	secHandler := &SecurityHandler{}
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		panic(err)
	}

	// Mount ogen server under /api/v1
	router.Mount("/api/v1", ogenServer)

	// Health and metrics
	router.Get("/health", healthCheck)
	router.Handle("/metrics", promhttp.Handler())

	return &HTTPServer{
		addr:    addr,
		router:  router,
		service: service,
		server: &http.Server{
			Addr:         addr,
			Handler:      router,
			ReadTimeout:  15 * time.Second,
			WriteTimeout: 15 * time.Second,
		},
	}
}

// Start starts the HTTP server
func (s *HTTPServer) Start() error {
	return s.server.ListenAndServe()
}

// Shutdown gracefully shuts down the server
func (s *HTTPServer) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

func healthCheck(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("OK"))
}
