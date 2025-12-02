// Issue: #1560

package server

import (
	"context"
	"database/sql"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	_ "github.com/lib/pq"
	"github.com/prometheus/client_golang/prometheus/promhttp"

	"github.com/gc-lover/necpgame-monorepo/services/projectile-core-service-go/pkg/api"
)

// HTTPServer represents the HTTP server
type HTTPServer struct {
	addr    string
	router  chi.Router
	server  *http.Server
	db      *sql.DB
	service *ProjectileService
}

// NewHTTPServer creates a new HTTP server
func NewHTTPServer(addr, dbConnStr string) (*HTTPServer, error) {
	// Connect to database
	db, err := sql.Open("postgres", dbConnStr)
	if err != nil {
		return nil, err
	}

	if err := db.Ping(); err != nil {
		return nil, err
	}

	// Initialize components
	repo := NewProjectileRepository(db)
	service := NewProjectileService(repo)
	handlers := NewProjectileHandlers(service)

	// Setup router
	router := chi.NewRouter()

	// Register middlewares
	router.Use(middleware.RequestID)
	router.Use(middleware.RealIP)
	router.Use(middleware.Logger)
	router.Use(middleware.Recoverer)
	router.Use(middleware.Timeout(60 * time.Second))
	router.Use(LoggingMiddleware())
	router.Use(MetricsMiddleware())

	// Register API handlers
	api.HandlerWithOptions(handlers, api.ChiServerOptions{
		BaseURL:    "/api/v1",
		BaseRouter: router,
	})

	// Health check
	router.Get("/health", healthCheck)

	// Metrics
	router.Handle("/metrics", promhttp.Handler())

	return &HTTPServer{
		addr:    addr,
		router:  router,
		db:      db,
		service: service,
	}, nil
}

// Start starts the HTTP server
func (s *HTTPServer) Start() error {
	s.server = &http.Server{
		Addr:    s.addr,
		Handler: s.router,
	}
	return s.server.ListenAndServe()
}

// Shutdown gracefully shuts down the server
func (s *HTTPServer) Shutdown(ctx context.Context) error {
	if s.db != nil {
		s.db.Close()
	}
	if s.server != nil {
		return s.server.Shutdown(ctx)
	}
	return nil
}

func healthCheck(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"healthy"}`))
}

