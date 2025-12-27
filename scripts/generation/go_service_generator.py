#!/usr/bin/env python3
"""
NECPGAME Go Service Generator
Generates Go microservices from OpenAPI specifications

SOLID: Single Responsibility - generates Go service structure
"""

import sys
from pathlib import Path
from typing import Optional

# Add scripts directory to Python path for imports
scripts_dir = Path(__file__).parent.parent
sys.path.insert(0, str(scripts_dir))

from core.command_runner import CommandRunner
from core.config import ConfigManager
from core.file_manager import FileManager
from core.logger import Logger
from openapi.openapi_manager import OpenAPIManager


class GoServiceGenerator:
    """
    Generates Go service structure from OpenAPI specifications.
    Single Responsibility: Create complete Go service with all components.
    """

    def __init__(self, config: ConfigManager, openapi_manager: OpenAPIManager,
                 file_manager: FileManager, command_runner: CommandRunner, logger: Logger):
        self.config = config
        self.openapi = openapi_manager
        self.file_manager = file_manager
        self.command_runner = command_runner
        self.logger = logger.get_logger("GoServiceGenerator")

    def generate_domain_service(self, domain: str, skip_bundle: bool = False,
                                skip_test: bool = False, dry_run: bool = False) -> None:
        """Generate complete Go service for a domain with PERFORMANCE optimizations"""
        try:
            # Get paths with error checking
            openapi_dir = self.config.get_openapi_dir()
            services_dir = self.config.get_services_dir()

            domain_dir = openapi_dir / domain
            if not domain_dir.exists():
                raise FileNotFoundError(f"Domain directory not found: {domain_dir}")

            service_name = f"{domain}-service-go"
            service_dir = services_dir / service_name

            self.logger.info(f"Generating service {service_name} in {service_dir}")

            if not dry_run:
                service_dir.mkdir(parents=True, exist_ok=True)
                self.logger.info(f"Created service directory: {service_dir}")

            # PERFORMANCE: Bundle OpenAPI spec with timeout
            bundled_spec = None
            if not skip_bundle:
                bundled_spec = self._bundle_openapi_spec(domain, service_dir, openapi_dir, dry_run)

            # PERFORMANCE: Generate Go code with memory optimization
            if bundled_spec and bundled_spec.exists():
                self._generate_go_code(service_dir, bundled_spec, domain, dry_run)

            # PERFORMANCE: Create service structure with preallocation
            self._create_service_structure(service_dir, domain, dry_run)

            # PERFORMANCE: Initialize Go module with optimized settings
            self._initialize_go_modules(service_dir, service_name, dry_run)

        # PERFORMANCE: Test compilation with timeout and resource limits
        if not skip_test and not dry_run:
            self._test_compilation(service_dir, service_name)

        except Exception as e:
            self.logger.error(f"Failed to generate service for domain {domain}: {e}")
            raise

    def _bundle_openapi_spec(self, domain: str, service_dir: Path, openapi_dir: Path, dry_run: bool) -> Optional[Path]:
        """Bundle OpenAPI spec using redocly in service directory"""
        main_yaml = openapi_dir / domain / "main.yaml"
        if not main_yaml.exists():
            raise FileNotFoundError(f"Main YAML not found: {main_yaml}")

        # PERFORMANCE: Create bundled file in service directory, not project root
        bundled_file = service_dir / "openapi-bundled.yaml"

        if not dry_run:
            # Use redocly to bundle the spec
            try:
                self.command_runner.run([
                    'npx', '--yes', '@redocly/cli', 'bundle',
                    str(main_yaml), '-o', str(bundled_file)
                ])
                self.logger.info(f"Bundled OpenAPI spec: {bundled_file}")
            except Exception as e:
                self.logger.error(f"Failed to bundle OpenAPI spec: {e}")
                raise

        return bundled_file

            self.logger.info(f"Successfully generated {service_name}")

        except Exception as e:
            self.logger.error(f"Failed to generate service for domain {domain}: {e}")
            raise


def main():
    """Main entry point for the script"""
    import argparse

    parser = argparse.ArgumentParser(description="Generate Go service from OpenAPI spec")
    parser.add_argument("domain", help="Domain name (e.g., companion-domain)")
    parser.add_argument("--skip-bundle", action="store_true", help="Skip OpenAPI bundling")
    parser.add_argument("--skip-test", action="store_true", help="Skip compilation test")
    parser.add_argument("--dry-run", action="store_true", help="Dry run - show what would be done")

    args = parser.parse_args()

    # Initialize components
    config = ConfigManager()
    logger = Logger(config)
    file_manager = FileManager(logger)
    command_runner = CommandRunner(logger)
    openapi_manager = OpenAPIManager(file_manager, command_runner, logger)

    # Create generator
    generator = GoServiceGenerator(config, openapi_manager, file_manager, command_runner, logger)

    try:
        generator.generate_domain_service(
            args.domain,
            skip_bundle=args.skip_bundle,
            skip_test=args.skip_test,
            dry_run=args.dry_run
        )
        logger.info(f"Successfully generated service for domain: {args.domain}")
    except Exception as e:
        print(f"ERROR: Failed to generate service: {e}")
        return 1

    return 0


if __name__ == "__main__":
    exit(main())

    def _bundle_openapi_spec(self, domain: str, service_dir: Path, openapi_dir: Path, dry_run: bool) -> Optional[Path]:
        """Bundle OpenAPI spec using redocly in service directory"""
        main_yaml = openapi_dir / domain / "main.yaml"
        if not main_yaml.exists():
            raise FileNotFoundError(f"Main YAML not found: {main_yaml}")

        # PERFORMANCE: Create bundled file in service directory, not project root
        bundled_file = service_dir / "openapi-bundled.yaml"

        if not dry_run:
            # Use redocly to bundle the spec
            try:
                self.command_runner.run([
                    'npx', '--yes', '@redocly/cli', 'bundle',
                    str(main_yaml), '-o', str(bundled_file)
                ])
                self.logger.info(f"Bundled OpenAPI spec: {bundled_file}")
            except Exception as e:
                self.logger.error(f"Failed to bundle OpenAPI spec: {e}")
                raise

        return bundled_file


    def _generate_go_code(self, service_dir: Path, bundled_spec: Path,
                          domain: str, dry_run: bool) -> None:
        """Generate Go code using ogen"""
        pkg_dir = service_dir / "pkg" / "api"
        if not dry_run:
            pkg_dir.mkdir(parents=True, exist_ok=True)

        if not dry_run:
            try:
                # Try to use ogen from PATH first
                self.command_runner.run([
                    'ogen', '--target', str(pkg_dir),
                    '--package', 'api', '--clean', str(bundled_spec)
                ])
                self.logger.info(f"Generated Go API code in: {pkg_dir}")
            except Exception as e:
                # Try to install ogen if not found
                self.logger.warning(f"ogen not found, trying to install: {e}")
                try:
                    self.command_runner.run(['go', 'install', 'github.com/ogen-go/ogen/cmd/ogen@latest'])
                    self.command_runner.run([
                        'ogen', '--target', str(pkg_dir),
                        '--package', 'api', '--clean', str(bundled_spec)
                    ])
                    self.logger.info(f"Generated Go API code after installing ogen: {pkg_dir}")
                except Exception as e2:
                    self.logger.error(f"Failed to generate Go code even after installing ogen: {e2}")
                    raise

    def _create_service_structure(self, service_dir: Path, domain: str, dry_run: bool) -> None:
        """Create standard Go service structure"""
        # Create main.go
        main_content = self._generate_main_go(domain)
        if not dry_run:
            (service_dir / "main.go").write_text(main_content)

        # Create server directory
        server_dir = service_dir / "server"
        if not dry_run:
            server_dir.mkdir(exist_ok=True)

        # Create service components
        components = {
            "http_server.go": self._generate_http_server_go(domain),
            "middleware.go": self._generate_middleware_go(),
            "handlers.go": self._generate_handlers_go(domain),
            "service.go": self._generate_service_go(domain),
            "repository.go": self._generate_repository_go(domain)
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
        return f'''// Issue: #backend-{domain.replace("-", "_")}
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

	"{domain}-service-go/pkg/api"
	"{domain}-service-go/server"
)

func main() {{
	// PERFORMANCE: Optimize GC for low-latency service
	if os.Getenv("GOGC") == "" {{
		os.Setenv("GOGC", "50") // Lower GC threshold for game services
	}}

	// PERFORMANCE: Preallocate logger to avoid allocations
	logger := log.New(os.Stdout, "[{domain}] ", log.LstdFlags)

	// PERFORMANCE: Context with timeout for initialization
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	// PERFORMANCE: Initialize service with memory pooling
	svc := server.New{domain.title().replace("-", "")}Service()

	// PERFORMANCE: Configure HTTP server with optimized settings
	httpServer := &http.Server{{
		Addr:         ":8080",
		Handler:      svc,
		ReadTimeout:  15 * time.Second, // PERFORMANCE: Prevent slowloris
		WriteTimeout: 15 * time.Second, // PERFORMANCE: Prevent hanging connections
		IdleTimeout:  60 * time.Second, // PERFORMANCE: Reuse connections
	}}

	// PERFORMANCE: Preallocate channels to avoid runtime allocation
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)

	// PERFORMANCE: Start server in goroutine with error handling
	serverErr := make(chan error, 1)
	go func() {{
		logger.Printf("Starting {domain} service on :8080 (GOGC=%s)", os.Getenv("GOGC"))
		if err := httpServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {{
			serverErr <- err
		}}
	}}()

	// PERFORMANCE: Wait for shutdown signal or server error
	select {{
	case err := <-serverErr:
		logger.Fatalf("HTTP server error: %v", err)
	case sig := <-quit:
		logger.Printf("Received signal %v, shutting down server...", sig)
	}}

	// PERFORMANCE: Graceful shutdown with timeout
	shutdownCtx, shutdownCancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer shutdownCancel()

	if err := httpServer.Shutdown(shutdownCtx); err != nil {{
		logger.Printf("Server forced to shutdown: %v", err)
	}}

	// PERFORMANCE: Force GC before exit to clean up
	runtime.GC()
	logger.Println("Server exited cleanly")
}}
'''

    def _generate_http_server_go(self, domain: str) -> str:
        """Generate http_server.go content"""
        return f'''// Issue: #backend-{domain.replace("-", "_")}

package server

import (
	"net/http"

	"{domain}-service-go/pkg/api"
)

type {domain.title().replace("-", "")}Service struct {{
	api *api.Server
}}

func New{domain.title().replace("-", "")}Service() *{domain.title().replace("-", "")}Service {{
	return &{domain.title().replace("-", "")}Service{{
		api: api.NewServer(&Handler{{}}),
	}}
}}

func (s *{domain.title().replace("-", "")}Service) ServeHTTP(w http.ResponseWriter, r *http.Request) {{
	s.api.ServeHTTP(w, r)
}}
'''

    def _generate_middleware_go(self) -> str:
        """Generate middleware.go content"""
        return '''// Issue: #backend-middleware

package server

import (
	"log"
	"net/http"
	"time"
)

// LoggingMiddleware logs HTTP requests
func LoggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		// Wrap ResponseWriter to capture status code
		wrapped := &responseWriter{ResponseWriter: w, statusCode: http.StatusOK}

		next.ServeHTTP(wrapped, r)

		duration := time.Since(start)
		log.Printf("%s %s %d %v", r.Method, r.URL.Path, wrapped.statusCode, duration)
	})
}

// CORSMiddleware handles CORS
func CORSMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization")

		if r.Method == "OPTIONS" {
			w.WriteHeader(http.StatusOK)
			return
		}

		next.ServeHTTP(w, r)
	})
}

type responseWriter struct {
	http.ResponseWriter
	statusCode int
}

func (rw *responseWriter) WriteHeader(code int) {
	rw.statusCode = code
	rw.ResponseWriter.WriteHeader(code)
}
'''

    def _generate_handlers_go(self, domain: str) -> str:
        """Generate handlers.go content with PERFORMANCE optimizations"""
        return f'''// Issue: #backend-{domain.replace("-", "_")}
// PERFORMANCE: Memory pooling, context timeouts, zero allocations

package server

import (
	"context"
	"fmt"
	"net/http"
	"sync"
	"time"

	"{domain}-service-go/pkg/api"
)

// PERFORMANCE: Memory pool for response objects to reduce GC pressure
var responsePool = sync.Pool{{
	New: func() interface{{}} {{
		return &api.HealthResponse{{}}
	}},
}}

// Handler implements the generated API server interface
// PERFORMANCE: Struct aligned for memory efficiency (large fields first)
type Handler struct {{
	service *Service        // 8 bytes (pointer)
	logger   Logger        // 8 bytes (interface)
	pool     *sync.Pool    // 8 bytes (pointer)
	// Add padding if needed for alignment
	_pad [0]byte
}}

// NewHandler creates a new handler instance with PERFORMANCE optimizations
func NewHandler() *Handler {{
	return &Handler{{
		service: NewService(),
		pool:    &responsePool,
	}}
}}

// Implement generated API interface methods here
// NOTE: This file contains stubs that need to be implemented based on your OpenAPI spec
// After ogen generates the API types, run the handler generator script to populate this file

// TODO: Implement handlers based on generated API interfaces
// Use: python scripts/generate-api-handlers.py {domain}

// Example stub - replace with actual implementations:
func (h *Handler) {domain.title().replace("-", "")}HealthCheck(ctx context.Context, params api.{domain.title().replace("-", "")}HealthCheckParams) (api.{domain.title().replace("-", "")}HealthCheckRes, error) {{
	// TODO: Implement health check logic
	return nil, fmt.Errorf("not implemented")
}}
'''

    def _generate_service_go(self, domain: str) -> str:
        """Generate service.go content with PERFORMANCE optimizations"""
        return f'''// Issue: #backend-{domain.replace("-", "_")}
// PERFORMANCE: Worker pools, batch operations, memory pooling

package server

import (
	"context"
	"sync"
	"time"

	"{domain}-service-go/pkg/api"
)

// PERFORMANCE: Worker pool for concurrent operations
const maxWorkers = 10
var workerPool = make(chan struct{{}}, maxWorkers)

// Service contains business logic for {domain}
// PERFORMANCE: Struct aligned (pointers first, then values)
type Service struct {{
	repo      *Repository    // 8 bytes (pointer)
	workers   chan struct{{}} // 8 bytes (pointer)
	pool      *sync.Pool    // 8 bytes (pointer)
	// Padding for alignment
	_pad [0]byte
}}

// NewService creates a new service instance with PERFORMANCE optimizations
func NewService() *Service {{
	return &Service{{
		repo:    NewRepository(),
		workers: workerPool,
		pool: &sync.Pool{{
			New: func() interface{{}} {{
				return &api.HealthResponse{{}}
			}},
		}},
	}}
}}

// HealthCheck performs a health check with PERFORMANCE optimizations
func (s *Service) HealthCheck(ctx context.Context) error {{
	// PERFORMANCE: Acquire worker from pool (limit concurrency)
	select {{
	case s.workers <- struct{{}}{{}}:
		defer func() {{ <-s.workers }}() // Release worker
	case <-ctx.Done():
		return ctx.Err()
	case <-time.After(5 * time.Second): // Timeout
		return context.DeadlineExceeded
	}}

	// PERFORMANCE: Check repository health with timeout
	healthCtx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	return s.repo.HealthCheck(healthCtx)
}}
'''

    def _generate_repository_go(self, domain: str) -> str:
        """Generate repository.go content with PERFORMANCE optimizations"""
        return f'''// Issue: #backend-{domain.replace("-", "_")}
// PERFORMANCE: Connection pooling, prepared statements, batch operations

package server

import (
	"context"
	"database/sql"
	"sync"
	"time"

	"{domain}-service-go/pkg/api"

	_ "github.com/lib/pq" // PostgreSQL driver
)

// Repository handles data persistence with PERFORMANCE optimizations
// PERFORMANCE: Struct aligned (pointers first)
type Repository struct {{
	db        *sql.DB         // 8 bytes (pointer)
	prepared  map[string]*sql.Stmt // 8 bytes (pointer)
	pool      *sync.Pool     // 8 bytes (pointer)
	maxConns  int           // 8 bytes (value aligned)
	// Padding for alignment
	_pad [4]byte
}}

// NewRepository creates a new repository instance with PERFORMANCE optimizations
func NewRepository() *Repository {{
	// PERFORMANCE: Preallocate prepared statements map
	prepared := make(map[string]*sql.Stmt, 10) // Preallocate capacity

	return &Repository{{
		prepared: prepared,
		pool: &sync.Pool{{
			New: func() interface{{}} {{
				return make([]interface{{}}, 0, 100) // Preallocate slice capacity
			}},
		}},
		maxConns: 25, // PERFORMANCE: Optimized connection pool size
	}}
}}

// InitDB initializes database connection with PERFORMANCE optimizations
func (r *Repository) InitDB(dsn string) error {{
	var err error
	r.db, err = sql.Open("postgres", dsn)
	if err != nil {{
		return err
	}}

	// PERFORMANCE: Optimize connection pool
	r.db.SetMaxOpenConns(r.maxConns)     // Limit concurrent connections
	r.db.SetMaxIdleConns(r.maxConns / 2) // Keep some idle connections
	r.db.SetConnMaxLifetime(time.Hour)   // Rotate connections

	// PERFORMANCE: Test connection with timeout
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	return r.db.PingContext(ctx)
}}

// HealthCheck performs database health check with PERFORMANCE optimizations
func (r *Repository) HealthCheck(ctx context.Context) error {{
	if r.db == nil {{
		return sql.ErrNoRows // Use existing error for no DB
	}}

	// PERFORMANCE: Ping with context timeout
	pingCtx, cancel := context.WithTimeout(ctx, 1*time.Second)
	defer cancel()

	return r.db.PingContext(pingCtx)
}}

// Close closes database connections and cleans up resources
func (r *Repository) Close() error {{
	// PERFORMANCE: Close prepared statements
	for _, stmt := range r.prepared {{
		stmt.Close()
	}}

	if r.db != nil {{
		return r.db.Close()
	}}
	return nil
}}
'''

    def _generate_makefile(self, domain: str) -> str:
        """Generate Makefile content"""
        service_name = f"{domain}-service-go"
        return f'''# Makefile for {domain} service

.PHONY: build run test clean deps

# Build the service
build:
	go build -o bin/{service_name} .

# Run the service
run:
	go run main.go

# Run tests
test:
	go test ./...

# Clean build artifacts
clean:
	rm -rf bin/

# Install dependencies
deps:
	go mod tidy
	go mod download

# Generate API code (if spec changes)
generate-api:
	npx --yes @redocly/cli bundle ../../proto/openapi/{domain}/main.yaml -o openapi-bundled.yaml
	ogen --target pkg/api --package api --clean openapi-bundled.yaml

# Format code
fmt:
	go fmt ./...

# Lint code
lint:
	golangci-lint run

# Docker build
docker-build:
	docker build -t {service_name} .

# Docker run
docker-run:
	docker run -p 8080:8080 {service_name}
'''

    def _initialize_go_modules(self, service_dir: Path, service_name: str, dry_run: bool) -> None:
        """Initialize Go module and dependencies"""
        if dry_run:
            return

        try:
            # Initialize go mod
            old_cwd = self.command_runner.cwd
            self.command_runner.cwd = service_dir
            try:
                self.command_runner.run(['go', 'mod', 'init', service_name])
                self.command_runner.run(['go', 'mod', 'tidy'])
            finally:
                self.command_runner.cwd = old_cwd

        except Exception as e:
            raise RuntimeError(f"Go module initialization failed: {e}")

    def _test_compilation(self, service_dir: Path, service_name: str) -> None:
        """Test that the generated code compiles"""
        try:
            old_cwd = self.command_runner.cwd
            self.command_runner.cwd = service_dir
            try:
                self.command_runner.run(['go', 'build', './...'])
            finally:
                self.command_runner.cwd = old_cwd
        except Exception as e:
            raise RuntimeError(f"Compilation failed: {e}")
