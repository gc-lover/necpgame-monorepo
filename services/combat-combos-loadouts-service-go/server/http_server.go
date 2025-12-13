// Combat Combos Loadouts Service HTTP Server
// Issue: #141890005

package server

import (
	"context"
	"fmt"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/rs/zerolog/log"

	"combat-combos-loadouts-service-go/pkg/api"
)

// Server wraps the HTTP server with dependencies
type Server struct {
	httpServer *http.Server
	router     chi.Router
	config     *Config
	db         *pgxpool.Pool
	handler    *Handler
	service    *Service
	repo       *Repository
}

// NewServer creates a new HTTP server instance
func NewServer(cfg *Config) (*Server, error) {
	// Initialize database connection
	db, err := initDatabase(cfg)
	if err != nil {
		return nil, fmt.Errorf("failed to initialize database: %w", err)
	}

	// Initialize repository
	repo := NewRepository(db)

	// Initialize service
	service := NewService(repo)

	// Initialize handler
	handler := NewHandler(service)

	// Create router
	r := chi.NewRouter()

	// Setup middleware
	setupMiddleware(r, cfg)

	// Setup routes
	setupRoutes(r, handler)

	// Create HTTP server
	httpServer := &http.Server{
		Addr:         cfg.ServerAddr,
		Handler:      r,
		ReadTimeout:  time.Duration(cfg.ReadTimeout) * time.Second,
		WriteTimeout: time.Duration(cfg.WriteTimeout) * time.Second,
		IdleTimeout:  time.Duration(cfg.IdleTimeout) * time.Second,
	}

	server := &Server{
		httpServer: httpServer,
		router:     r,
		config:     cfg,
		db:         db,
		handler:    handler,
		service:    service,
		repo:       repo,
	}

	return server, nil
}

// Start starts the HTTP server
func (s *Server) Start() error {
	log.Info().
		Str("addr", s.config.ServerAddr).
		Msg("Starting Combat Combos Loadouts Service")

	return s.httpServer.ListenAndServe()
}

// Shutdown gracefully shuts down the server
func (s *Server) Shutdown(ctx context.Context) error {
	log.Info().Msg("Shutting down server...")

	// Close database connections
	if s.db != nil {
		s.db.Close()
		log.Info().Msg("Database connections closed")
	}

	return s.httpServer.Shutdown(ctx)
}

// setupMiddleware configures middleware for the router
func setupMiddleware(r chi.Router, cfg *Config) {
	// Request ID
	r.Use(middleware.RequestID)

	// Real IP
	r.Use(middleware.RealIP)

	// Logging
	r.Use(middleware.Logger)

	// Recover from panics
	r.Use(middleware.Recoverer)

	// Timeout
	r.Use(middleware.Timeout(30 * time.Second))

	// CORS
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"},
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300,
	}))

	// Health check
	r.Get("/health", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("OK"))
	})

	// Readiness check
	r.Get("/ready", func(w http.ResponseWriter, r *http.Request) {
		// TODO: Add database connectivity check
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("OK"))
	})
}

// setupRoutes configures API routes
func setupRoutes(r chi.Router, handler *Handler) {
	// Create ogen server
	server, err := api.NewServer(handler, nil)
	if err != nil {
		log.Fatal().Err(err).Msg("Failed to create API server")
	}

	// Mount API routes
	r.Mount("/api/v1", http.StripPrefix("/api/v1", server))
}

// initDatabase initializes the database connection pool
func initDatabase(cfg *Config) (*pgxpool.Pool, error) {
	ctx := context.Background()

	poolConfig, err := pgxpool.ParseConfig(cfg.DatabaseURL)
	if err != nil {
		return nil, fmt.Errorf("failed to parse database URL: %w", err)
	}

	// Configure connection pool
	poolConfig.MaxConns = int32(cfg.MaxOpenConns)
	poolConfig.MinConns = int32(cfg.MaxIdleConns)
	poolConfig.MaxConnLifetime = time.Duration(cfg.ConnMaxLifetime) * time.Second

	pool, err := pgxpool.NewWithConfig(ctx, poolConfig)
	if err != nil {
		return nil, fmt.Errorf("failed to create connection pool: %w", err)
	}

	// Test connection
	if err := pool.Ping(ctx); err != nil {
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	log.Info().Msg("Database connection established")

	return pool, nil
}
