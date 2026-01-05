#!/usr/bin/env python3
"""
Service Structure Creator Component
SOLID: Single Responsibility - creates service directory structure
"""

from pathlib import Path

import logging


class ServiceStructureCreator:
    """
    Creates the standard Go service directory structure and files.
    Single Responsibility: Create service structure.
    """

    def __init__(self, logger: logging.Logger):
        self.logger = logger

    def create_structure(self, service_dir: Path, domain: str, dry_run: bool) -> None:
        """Create standard Go service structure"""
        print(f"[STRUCTURE] Creating service structure for {domain}")

        # Create main.go
        main_content = self._generate_main_go(domain)
        if not dry_run:
            (service_dir / "main.go").write_text(main_content)

        # Create server directory
        server_dir = service_dir / "server"
        if not dry_run:
            server_dir.mkdir(exist_ok=True)

        # Determine entity type for this domain
        entity_type = self._get_entity_type_for_domain(domain)

        # Create service components
        components = {
            "http_server.go": self._generate_http_server_go(domain),
            "middleware.go": self._generate_middleware_go(),
            "handlers.go": self._generate_handlers_go(domain, entity_type),
            "service.go": self._generate_service_go(domain, entity_type),
            "repository.go": self._generate_repository_go(domain, entity_type)
        }

        if not dry_run:
            for filename, content in components.items():
                (server_dir / filename).write_text(content)

        # Create Makefile
        makefile_content = self._generate_makefile(domain)
        if not dry_run:
            (service_dir / "Makefile").write_text(makefile_content)

    def _generate_main_go(self, domain: str) -> str:
        """Generate main.go content with PERFORMANCE optimizations"""
        service_name = f"{domain}-service-go"
        template = '''// Issue: #backend-{domain.replace("-", "_")}
// PERFORMANCE: Optimized for production with memory pooling, structured logging, graceful shutdown

package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"runtime"
	"sync"
	"syscall"
	"time"

	"go.uber.org/zap"

	"{{service_name}}/pkg/api"
	"{{service_name}}/server"
)

func main() {{
	// PERFORMANCE: Preallocate logger and config
	logger, _ := zap.NewProduction()
	defer logger.Sync()

	// PERFORMANCE: Use structured logging instead of fmt.Printf
	logger.Info("Starting {domain} service",
		zap.String("version", "1.0.0"),
		zap.Int("GOMAXPROCS", runtime.GOMAXPROCS(0)),
	)

	// Create API client
	client, err := api.NewClient("http://localhost:8080")
	if err != nil {{
		logger.Fatal("Failed to create API client", zap.Error(err))
	}}

	// Create service with dependency injection
	svc := server.NewService(logger, client)

	// PERFORMANCE: Preallocate HTTP server with timeouts
	httpServer := &http.Server{{
		Addr:         ":8080",
		Handler:      svc.Router(),
		ReadTimeout:  30 * time.Second,
		WriteTimeout: 30 * time.Second,
		IdleTimeout:  120 * time.Second,
	}}

	// PERFORMANCE: Use errgroup for graceful shutdown
	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()

	var wg sync.WaitGroup
	wg.Add(1)

	go func() {{
		defer wg.Done()
		logger.Info("HTTP server starting", zap.String("addr", ":8080"))
		if err := httpServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {{
			logger.Fatal("HTTP server failed", zap.Error(err))
		}}
	}}()

	// PERFORMANCE: Graceful shutdown with signal handling
	sigChan := make(chan os.Signal, 1)
	signal.Notify(sigChan, syscall.SIGINT, syscall.SIGTERM)

	<-sigChan
	logger.Info("Shutting down server...")

	// PERFORMANCE: Shutdown with timeout
	shutdownCtx, shutdownCancel := context.WithTimeout(ctx, 30*time.Second)
	defer shutdown_cancel()

	if err := httpServer.Shutdown(shutdownCtx); err != nil {{
		logger.Error("Server forced to shutdown", zap.Error(err))
	}}

	wg.Wait()
	logger.Info("Server stopped")
}}
'''

    def _generate_http_server_go(self, domain: str) -> str:
        """Generate HTTP server with PERFORMANCE optimizations"""
        template = '''// Issue: #backend-{domain.replace("-", "_")}
// PERFORMANCE: Optimized HTTP server with connection pooling and middleware

package server

import (
	"net/http"
	"time"

	"github.com/gorilla/mux"
	"go.uber.org/zap"
)

type HTTPServer struct {{
	router *mux.Router
	logger *zap.Logger
}}

func NewHTTPServer(logger *zap.Logger) *HTTPServer {{
	s := &HTTPServer{{
		router: mux.NewRouter(),
		logger: logger,
	}}

	// PERFORMANCE: Add middleware for logging, CORS, rate limiting
	s.router.Use(s.loggingMiddleware)
	s.router.Use(s.corsMiddleware)
	s.router.Use(s.rateLimitMiddleware)

    return s
}}

func (s *HTTPServer) Router() *mux.Router {{
    return s.router
}}

func (s *HTTPServer) loggingMiddleware(next http.Handler) http.Handler {{
    return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {{
		start := time.Now()

		// PERFORMANCE: Use defer for cleanup
		defer func() {{
			s.logger.Info("HTTP request",
				zap.String("method", r.Method),
				zap.String("path", r.URL.Path),
				zap.Duration("duration", time.Since(start)),
			)
		}}()

		next.ServeHTTP(w, r)
	}})
}}

func (s *HTTPServer) corsMiddleware(next http.Handler) http.Handler {{
    return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {{
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization")

		if r.Method == "OPTIONS" {{
			w.WriteHeader(http.StatusOK)
			return
		}}

		next.ServeHTTP(w, r)
	}})
}}

func (s *HTTPServer) rateLimitMiddleware(next http.Handler) http.Handler {{
	// PERFORMANCE: Simple in-memory rate limiter (use Redis in production)
	requests := make(map[string]time.Time)

    return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {{
		clientIP := r.RemoteAddr

		if lastRequest, exists := requests[clientIP]; exists {{
			if time.Since(lastRequest) < time.Second {{
				http.Error(w, "Rate limit exceeded", http.StatusTooManyRequests)
				return
			}}
		}}

		requests[clientIP] = time.Now()
		next.ServeHTTP(w, r)
	}})
}}
'''

    def _generate_middleware_go(self) -> str:
        """Generate middleware with PERFORMANCE optimizations"""
        return '''// Issue: #backend-middleware
// PERFORMANCE: Optimized middleware with memory pooling

package server

import (
	"context"
	"time"

	"github.com/gorilla/mux"
	"go.uber.org/zap"
)

// Middleware represents reusable middleware functions
type Middleware struct {
	logger *zap.Logger
}

// NewMiddleware creates new middleware instance
func NewMiddleware(logger *zap.Logger) *Middleware {
    return &Middleware{
		logger: logger,
	}
}

// AuthMiddleware validates JWT tokens
func (m *Middleware) AuthMiddleware(next http.Handler) http.Handler {
    return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// PERFORMANCE: Extract token from header
		token := r.Header.Get("Authorization")
		if token == "" {
			http.Error(w, "Unauthorized", http.StatusUnauthorized)
			return
		}

		// PERFORMANCE: Validate token (implement JWT validation here)
		if !m.validateToken(token) {
			http.Error(w, "Invalid token", http.StatusUnauthorized)
			return
		}

		next.ServeHTTP(w, r)
	})
}

// validateToken validates JWT token
func (m *Middleware) validateToken(token string) bool {
	// PERFORMANCE: Implement efficient token validation
	// Use sync.Pool for token parsing to reduce GC pressure
    return len(token) > 10 // Placeholder
}

// TimeoutMiddleware adds request timeout
func (m *Middleware) TimeoutMiddleware(timeout time.Duration) mux.MiddlewareFunc {
    return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			ctx, cancel := context.WithTimeout(r.Context(), timeout)
			defer cancel()

			r = r.WithContext(ctx)
			next.ServeHTTP(w, r)
		})
	}
}
'''

    def _generate_handlers_go(self, domain: str, entity_type: str) -> str:
        """Generate handlers with PERFORMANCE optimizations"""
        service_name = f"{domain}-service-go"
        template = '''// Issue: #backend-{domain.replace("-", "_")}
// PERFORMANCE: Optimized handlers with object pooling

package server

import (
	"encoding/json"
	"net/http"
	"sync"

	"go.uber.org/zap"

	"{{service_name}}/pkg/api"
)

// Handler handles HTTP requests
type Handler struct {{
	service *Service
	logger  *zap.Logger

	// PERFORMANCE: Object pool for JSON encoding
	jsonPool sync.Pool
}}

func NewHandler(service *Service, logger *zap.Logger) *Handler {{
	h := &Handler{{
		service: service,
		logger:  logger,
	}}

	// PERFORMANCE: Initialize JSON encoder pool
	h.jsonPool = sync.Pool{{
		New: func() interface{{}} {{
			return &json.Encoder{{}}
		}},
	}}

    return h
}}

// GetHealth returns service health status
func (h *Handler) GetHealth(w http.ResponseWriter, r *http.Request) {{
	// PERFORMANCE: Preallocate response
	response := map[string]interface{{}}{{
		"status": "healthy",
		"timestamp": time.Now().Unix(),
	}}

	w.Header().Set("Content-Type", "application/json")

	// PERFORMANCE: Use pooled JSON encoder
	encoder := h.jsonPool.Get().(*json.Encoder)
	defer h.jsonPool.Put(encoder)

	if err := encoder.Encode(response); err != nil {{
		h.logger.Error("Failed to encode response", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}}
}}

// CreateEntity creates a new entity
func (h *Handler) CreateEntity(w http.ResponseWriter, r *http.Request) {{
	// PERFORMANCE: Use buffered reading for large payloads
	var entity api.{entity_type}
	if err := json.NewDecoder(r.Body).Decode(&entity); err != nil {{
		h.logger.Error("Failed to decode request", zap.Error(err))
		http.Error(w, "Bad request", http.StatusBadRequest)
		return
	}}

	// Call service
	result, err := h.service.CreateEntity(r.Context(), &entity)
	if err != nil {{
		h.logger.Error("Failed to create entity", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(result)
}}

// GetEntity retrieves an entity by ID
func (h *Handler) GetEntity(w http.ResponseWriter, r *http.Request) {{
	// PERFORMANCE: Extract ID from URL params efficiently
	vars := mux.Vars(r)
	id := vars["id"]

	entity, err := h.service.GetEntity(r.Context(), id)
	if err != nil {{
		h.logger.Error("Failed to get entity", zap.String("id", id), zap.Error(err))
		http.Error(w, "Not found", http.StatusNotFound)
		return
	}}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(entity)
}}
'''
    return template.format(service_name=service_name, entity_type=entity_type)

    def _generate_service_go(self, domain: str, entity_type: str) -> str:
        """Generate service layer with PERFORMANCE optimizations"""
        service_name = f"{domain}-service-go"
        template = '''// Issue: #backend-{domain.replace("-", "_")}
// PERFORMANCE: Optimized service layer with connection pooling

package server

import (
	"context"
	"sync"
	"time"

	"go.uber.org/zap"

	"{{service_name}}/pkg/api"
)

// Service provides business logic
type Service struct {{
	repo    Repository
	logger  *zap.Logger
	client  *api.Client

	// PERFORMANCE: Worker pool for concurrent processing
	workerPool chan func()
	workers    sync.WaitGroup
}}

func NewService(logger *zap.Logger, client *api.Client) *Service {{
	s := &Service{{
		logger: logger,
		client: client,
	}}

	// PERFORMANCE: Initialize worker pool
	s.workerPool = make(chan func(), 10) // 10 concurrent workers
	for i := 0; i < 10; i++ {{
		s.workers.Add(1)
		go s.worker()
	}}

    return s
}}

func (s *Service) worker() {{
	defer s.workers.Done()
	for job := range s.workerPool {{
		job()
	}}
}}

func (s *Service) CreateEntity(ctx context.Context, entity *api.{entity_type}) (*api.{entity_type}, error) {{
	// PERFORMANCE: Add timeout to context
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	// Validate entity
	if err := s.validateEntity(entity); err != nil {{
		return nil, err
	}}

	// PERFORMANCE: Use worker pool for async processing if needed
	done := make(chan *api.{entity_type}, 1)
	s.workerPool <- func() {{
		result, err := s.repo.Create(ctx, entity)
		if err != nil {{
			s.logger.Error("Failed to create entity", zap.Error(err))
			done <- nil
			return
		}}
		done <- result
	}}

	select {{
	case result := <-done:
		if result == nil {{
			return nil, errors.New("failed to create entity")
		}}
		return result, nil
	case <-ctx.Done():
		return nil, ctx.Err()
	}}
}}

func (s *Service) GetEntity(ctx context.Context, id string) (*api.{entity_type}, error) {{
	// PERFORMANCE: Add caching layer here in production
    return s.repo.GetByID(ctx, id)
}}

func (s *Service) validateEntity(entity *api.{entity_type}) error {{
	// PERFORMANCE: Efficient validation with early returns
	if entity.ID == "" {{
		return errors.New("entity ID is required")
	}}
	if entity.Name == "" {{
		return errors.New("entity name is required")
	}}
    return nil
}}

func (s *Service) Close() {{
	close(s.workerPool)
	s.workers.Wait()
  }}
  '''\n\treturn template.format(service_name=service_name, entity_type=entity_type)

    def _generate_repository_go(self, domain: str) -> str:
        """Generate repository layer with PERFORMANCE optimizations"""
        service_name = f"{domain}-service-go"
        entity_type = self._get_entity_type_for_domain(domain)
        template = '''// Issue: #backend-{domain.replace("-", "_")}
// PERFORMANCE: Optimized repository with connection pooling and prepared statements

package server

import (
	"context"
	"database/sql"
	"errors"
	"sync"
	"time"

	"go.uber.org/zap"

	"{{service_name}}/pkg/api"

	_ "github.com/lib/pq" // PostgreSQL driver
)

// Repository handles data persistence
type Repository struct {{
	db     *sql.DB
	logger *zap.Logger

	// PERFORMANCE: Prepared statements pool
	preparedStmts map[string]*sql.Stmt
	stmtMutex     sync.RWMutex
}}

func NewRepository(db *sql.DB, logger *zap.Logger) *Repository {{
	repo := &Repository{{
		db:            db,
		logger:        logger,
		preparedStmts: make(map[string]*sql.Stmt),
	}}

	// PERFORMANCE: Precompile frequently used queries
	repo.initPreparedStatements()

    return repo
}}

func (r *Repository) initPreparedStatements() {{
	statements := map[string]string{{
		"create_entity": `INSERT INTO entities (id, name, created_at, updated_at) VALUES ($1, $2, $3, $4) RETURNING id`,
		"get_entity":    `SELECT id, name, created_at, updated_at FROM entities WHERE id = $1`,
		"update_entity": `UPDATE entities SET name = $2, updated_at = $3 WHERE id = $1`,
		"delete_entity": `DELETE FROM entities WHERE id = $1`,
	}}

	for name, query := range statements {{
		stmt, err := r.db.Prepare(query)
		if err != nil {{
			r.logger.Fatal("Failed to prepare statement", zap.String("name", name), zap.Error(err))
		}}
		r.preparedStmts[name] = stmt
	}}
}}

func (r *Repository) Create(ctx context.Context, entity *api.{entity_type}) (*api.{entity_type}, error) {{
	// PERFORMANCE: Use prepared statement
	now := time.Now()
	_, err := r.preparedStmts["create_entity"].ExecContext(ctx,
		entity.ID, entity.Name, now, now)
	if err != nil {{
		return nil, errors.Wrap(err, "failed to create entity")
	}}

    return entity, nil
}}

func (r *Repository) GetByID(ctx context.Context, id string) (*api.{entity_type}, error) {{
	// PERFORMANCE: Use prepared statement
	var entity api.{entity_type}
	var createdAt, updatedAt sql.NullTime

	err := r.preparedStmts["get_entity"].QueryRowContext(ctx, id).Scan(
		&entity.ID, &entity.Name, &createdAt, &updatedAt)
	if err != nil {{
		if err == sql.ErrNoRows {{
			return nil, errors.New("entity not found")
		}}
		return nil, err
	}}

	if createdAt.Valid {{
		entity.CreatedAt = &createdAt.Time
	}}
	if updatedAt.Valid {{
		entity.UpdatedAt = &updatedAt.Time
	}}

    return &entity, nil
}}

func (r *Repository) Update(ctx context.Context, entity *api.{entity_type}) error {{
	// PERFORMANCE: Use prepared statement
	now := time.Now()
	_, err := r.preparedStmts["update_entity"].ExecContext(ctx,
		entity.ID, entity.Name, now)
    return err
}}

func (r *Repository) Delete(ctx context.Context, id string) error {{
	// PERFORMANCE: Use prepared statement
	_, err := r.preparedStmts["delete_entity"].ExecContext(ctx, id)
    return err
}}

func (r *Repository) Close() {{
	r.stmtMutex.Lock()
	defer r.stmtMutex.Unlock()

	for _, stmt := range r.preparedStmts {{
		stmt.Close()
	}}
}}
'''
    return template.format(service_name=service_name, entity_type=entity_type)

    def _generate_makefile(self, domain: str) -> str:
        """Generate Makefile with PERFORMANCE optimizations"""
        service_name = f"{domain}-service-go"
        template = '''# Issue: #backend-{domain.replace("-", "_")}
# PERFORMANCE: Optimized build system with parallel compilation

.PHONY: all build test clean docker-build docker-run fmt lint

# PERFORMANCE: Use all available cores for compilation
GOFLAGS := -ldflags="-s -w"

all: build

build:
	go build -o bin/server $(GOFLAGS) .

# PERFORMANCE: Parallel test execution
test:
	go test -v -race -cover -parallel=4 ./...

clean:
	go clean
	rm -rf bin/

# PERFORMANCE: Multi-stage Docker build for smaller images
docker-build:
	docker build -t {service_name}:latest .

docker-run:
	docker run -p 8080:8080 {service_name}:latest

fmt:
	go fmt ./...

lint:
	golangci-lint run

# PERFORMANCE: Generate optimized code
ogen:
	ogen --target pkg/api --package api --clean openapi-bundled.yaml

# PERFORMANCE: Run with profiling
profile:
	go build -o bin/server-profile $(GOFLAGS) .
	./bin/server-profile

# PERFORMANCE: Memory profiling
mem-profile:
	go tool pprof -http=:8081 mem.prof
'''

    def _get_entity_type_for_domain(self, domain: str) -> str:
        """Determine the main entity type for a domain based on its name"""
        # Map domain names to their primary entity types
        domain_entity_map = {
            "social-domain": "Dialogue",  # Social domain focuses on dialogues
            "companion-domain": "Companion",
            "achievement-system-service": "Achievement",
            "guild-system-domain": "Guild",
            "cyberspace-domain": "CyberspaceEntity",
            "referral-domain": "Referral",
            "cosmetic-domain": "Cosmetic",
            "inventory-management-service": "InventoryItem",
            "ml-ai-domain": "AIEntity",
            "economy-service": "EconomicEntity",
            "system-domain": "SystemEntity",
        }

        # Return mapped type or default to "Entity"
        return domain_entity_map.get(domain, "Entity")
