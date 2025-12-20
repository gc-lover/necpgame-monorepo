#!/bin/bash

# NECP Game Service Creator Script
# Создание нового микросервиса с шаблоном

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Configuration
TEMPLATE_DIR="scripts/templates"

# Function to create service structure
create_service_structure() {
    local service_name=$1
    local service_description=$2
    local port=$3

    echo -e "${BLUE}🏗️  Creating service structure for $service_name...${NC}"

    # Create directory
    local service_dir="services/${service_name}-go"
    mkdir -p "$service_dir"

    # Create subdirectories
    mkdir -p "$service_dir/server"
    mkdir -p "$service_dir/pkg/api"

    echo -e "${GREEN}OK Service directory created: $service_dir${NC}"
}

# Function to create Dockerfile
create_dockerfile() {
    local service_name=$1
    local port=$2

    cat > "services/${service_name}-go/Dockerfile" << EOF
# Multi-stage build for ${service_name}-service
FROM golang:1.24-alpine AS builder

WORKDIR /app

RUN apk add --no-cache git ca-certificates tzdata

# Copy go modules
COPY go.mod go.sum ./
RUN go mod download
RUN go mod tidy

# Copy source code (with already generated API code)
COPY . .

# Build
RUN CGO_ENABLED=0 GOOS=linux go build -o ${service_name}-service -ldflags="-w -s" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates tzdata

WORKDIR /app

RUN addgroup -g 1000 appuser && \\
    adduser -D -u 1000 -G appuser appuser

COPY --from=builder /app/${service_name}-service /app/${service_name}-service
COPY --from=builder /usr/share/zoneinfo /usr/share/zoneinfo

RUN chown -R appuser:appuser /app

USER appuser

EXPOSE $port/tcp

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \\
  CMD wget -q --spider http://localhost:$port/health || exit 1

ENTRYPOINT ["/app/${service_name}-service"]
EOF

    echo -e "${GREEN}OK Dockerfile created${NC}"
}

# Function to create main.go
create_main() {
    local service_name=$1
    local port=$2
    local description=$3

    cat > "services/${service_name}-go/main.go" << EOF
// Issue: #XXXX
package main

import (
	"context"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling endpoints
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/${service_name}-go/server"
	"github.com/sirupsen/logrus"
)

func main() {
	logger := logrus.New()
	logger.SetFormatter(&logrus.JSONFormatter{})
	logger.SetLevel(logrus.InfoLevel)
	logger.Info("${description} Service starting...")

	addr := getEnv("ADDR", "0.0.0.0:$port")

	// Database connection
	dbConnStr := getEnv("DATABASE_URL", "postgres://necpg:necpg@localhost:5432/necpg?sslmode=disable")
	repo, err := server.NewPostgresRepository(dbConnStr)
	if err != nil {
		logger.WithError(err).Fatal("Failed to connect to database")
	}
	defer repo.Close()
	logger.Info("OK Database connection established")

	// Initialize service
	service := server.New${service_name^}Service(repo)
	logger.Info("OK ${service_name^} Service initialized")

	// Create HTTP server
	httpServer := server.NewHTTPServer(addr, logger, service)

	// OPTIMIZATION: Issue #1584 - Start pprof server for profiling
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:$((port + 1000))")
		logger.WithField("addr", pprofAddr).Info("pprof server starting")
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

	// Issue: #1585 - Runtime Goroutine Monitoring
	monitor := server.NewGoroutineMonitor(200, logger) // Max 200 goroutines
	go monitor.Start()
	defer monitor.Stop()
	logger.Info("OK Goroutine monitor started")

	// Graceful shutdown
	sigChan := make(chan os.Signal, 1)
	signal.Notify(sigChan, os.Interrupt, syscall.SIGTERM)

	go func() {
		<-sigChan
		logger.Info("Shutting down server...")

		shutdownCtx, shutdownCancel := context.WithTimeout(context.Background(), 5*time.Second)
		defer shutdownCancel()
		httpServer.Shutdown(shutdownCtx)
	}()

	logger.WithField("addr", addr).Info("HTTP server starting")
	if err := httpServer.Start(); err != nil {
		logger.WithError(err).Fatal("Server error")
	}

	logger.Info("Server stopped")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
EOF

    echo -e "${GREEN}OK main.go created${NC}"
}

# Function to create HTTP server
create_http_server() {
    local service_name=$1

    cat > "services/${service_name}-go/server/http_server.go" << EOF
// Issue: #1595
// OPTIMIZED: No chi dependency, standard http.ServeMux + middleware chain
// PERFORMANCE: OGEN routes (hot path) already maximum speed, removed chi overhead from health/metrics
package server

// HTTP handlers use context.WithTimeout for request timeouts (see handlers.go)

import (
	"context"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/${service_name}-go/pkg/api"
	"github.com/sirupsen/logrus"
)

// HTTPServer represents HTTP server (no chi dependency)
type HTTPServer struct {
	addr   string
	server *http.Server
	logger *logrus.Logger
}

// NewHTTPServer creates new HTTP server WITHOUT chi
// PERFORMANCE: Standard mux for health/metrics, OGEN router for API (already max speed)
func NewHTTPServer(addr string, logger *logrus.Logger, service *${service_name^}Service) *HTTPServer {
	// OGEN server (fast static router - hot path)
	handlers := NewHandlers(service)
	secHandler := &SecurityHandler{}
	ogenServer, err := api.NewServer(handlers, secHandler)
	if err != nil {
		panic(err)
	}

	// Standard mux (for health/metrics - cold path)
	mux := http.NewServeMux()

	// Middleware chain (no duplication, optimized)
	apiHandler := chainMiddleware(ogenServer,
		recoveryMiddleware(logger),  // panic recovery
		requestIDMiddleware,         // request ID
		loggingMiddleware(logger),   // structured logging
		CORSMiddleware(),           // CORS
	)

	// Mount OGEN (hot path - maximum speed, static router)
	mux.Handle("/api/v1/", apiHandler)

	// Health/metrics (cold path - simple mux, no chi overhead)
	mux.HandleFunc("/health", healthCheck)
	mux.Handle("/metrics", promhttp.Handler())

	return &HTTPServer{
		addr: addr,
		server: &http.Server{
			Addr:         addr,
			Handler:      mux,
			ReadTimeout:  30 * time.Second,
			WriteTimeout: 30 * time.Second,
			IdleTimeout:  120 * time.Second,
		},
		logger: logger,
	}
}

// chainMiddleware chains middleware functions (simple and fast)
func chainMiddleware(h http.Handler, mws ...func(http.Handler) http.Handler) http.Handler {
	for i := len(mws) - 1; i >= 0; i-- {
		h = mws[i](h)
	}
	return h
}

// recoveryMiddleware recovers from panics
func recoveryMiddleware(logger *logrus.Logger) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			defer func() {
				if err := recover(); err != nil {
					logger.WithField("error", err).Error("Panic recovered")
					http.Error(w, "Internal Server Error", http.StatusInternalServerError)
				}
			}()
			next.ServeHTTP(w, r)
		})
	}
}

// requestIDMiddleware adds request ID to headers
func requestIDMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		requestID := r.Header.Get("X-Request-ID")
		if requestID == "" {
			requestID = uuid.New().String()
		}
		w.Header().Set("X-Request-ID", requestID)
		next.ServeHTTP(w, r)
	})
}

// loggingMiddleware logs HTTP requests
func loggingMiddleware(logger *logrus.Logger) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()
			next.ServeHTTP(w, r)
			logger.WithFields(logrus.Fields{
				"method":   r.Method,
				"path":     r.URL.Path,
				"duration": time.Since(start),
			}).Info("HTTP request processed")
		})
	}
}

// CORSMiddleware handles CORS
func CORSMiddleware() func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
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
}

// Start starts HTTP server
func (s *HTTPServer) Start() error {
	s.logger.WithField("address", s.addr).Info("Starting HTTP server")

	errChan := make(chan error, 1)
	go func() {
		defer close(errChan)
		if err := s.server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			errChan <- err
		}
	}()

	err := <-errChan
	return err
}

// Shutdown gracefully shuts down HTTP server
func (s *HTTPServer) Shutdown(ctx context.Context) error {
	s.logger.Info("Shutting down HTTP server")
	return s.server.Shutdown(ctx)
}

// Health check handler
func healthCheck(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(\`{"status":"healthy"}\`))
}
EOF

    echo -e "${GREEN}OK http_server.go created${NC}"
}

# Function to create basic handlers
create_handlers() {
    local service_name=$1

    cat > "services/${service_name}-go/server/handlers.go" << EOF
// Issue: #1595
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/${service_name}-go/pkg/api"
)

const DBTimeout = 50 * time.Millisecond

type Handlers struct {
	service *${service_name^}Service
}

func NewHandlers(service *${service_name^}Service) *Handlers {
	return &Handlers{service: service}
}

// Health handler (basic implementation)
func (h *Handlers) Health(ctx context.Context) error {
	return nil
}

// Add your API handlers here
// Example:
// func (h *Handlers) Get${service_name^}(ctx context.Context, params api.Get${service_name^}Params) (api.Get${service_name^}Res, error) {
//     ctx, cancel := context.WithTimeout(ctx, DBTimeout)
//     defer cancel()
//
//     result, err := h.service.Get${service_name^}(ctx, params)
//     if err != nil {
//         return &api.Get${service_name^}InternalServerError{}, err
//     }
//
//     return result, nil
// }
EOF

    echo -e "${GREEN}OK handlers.go created${NC}"
}

# Function to create service file
create_service() {
    local service_name=$1

    cat > "services/${service_name}-go/server/service.go" << EOF
// Issue: #XXXX
package server

import (
	"context"
	"database/sql"
	"time"

	"github.com/sirupsen/logrus"
)

const DBTimeout = 50 * time.Millisecond

// ${service_name^}Service represents the service
type ${service_name^}Service struct {
	db     *sql.DB
	logger *logrus.Logger
}

// New${service_name^}Service creates new service
func New${service_name^}Service(db *sql.DB) *${service_name^}Service {
	return &${service_name^}Service{
		db:     db,
		logger: logrus.New(),
	}
}

// Add your business logic methods here
// Example:
// func (s *${service_name^}Service) Get${service_name^}(ctx context.Context, params interface{}) (interface{}, error) {
//     ctx, cancel := context.WithTimeout(ctx, DBTimeout)
//     defer cancel()
//
//     // Implement your business logic here
//     return result, nil
// }
EOF

    echo -e "${GREEN}OK service.go created${NC}"
}

# Function to create repository
create_repository() {
    local service_name=$1

    cat > "services/${service_name}-go/server/repository.go" << EOF
// Issue: #XXXX
package server

import (
	"database/sql"
	"time"

	_ "github.com/lib/pq"
)

const DBTimeout = 5 * time.Second

// Repository represents data access layer
type Repository struct {
	db *sql.DB
}

// NewPostgresRepository creates new PostgreSQL repository
func NewPostgresRepository(connStr string) (*Repository, error) {
	db, err := sql.Open("postgres", connStr)
	if err != nil {
		return nil, err
	}

	// Test connection
	ctx, cancel := context.WithTimeout(context.Background(), DBTimeout)
	defer cancel()

	if err := db.PingContext(ctx); err != nil {
		return nil, err
	}

	return &Repository{db: db}, nil
}

// Close closes database connection
func (r *Repository) Close() error {
	return r.db.Close()
}

// Add your data access methods here
// Example:
// func (r *Repository) Get${service_name^}(ctx context.Context, id string) (interface{}, error) {
//     ctx, cancel := context.WithTimeout(ctx, DBTimeout)
//     defer cancel()
//
//     query := \`SELECT * FROM ${service_name} WHERE id = \$1\`
//     // Execute query and return result
// }
EOF

    echo -e "${GREEN}OK repository.go created${NC}"
}

# Function to create Makefile
create_makefile() {
    local service_name=$1

    cat > "services/${service_name}-go/Makefile" << EOF
# Issue: #1595
# Makefile for ogen code generation

.PHONY: generate-api clean

SERVICE_NAME := ${service_name}
SPEC_DIR := ../../proto/openapi
BUNDLED_SPEC := openapi-bundled.yaml
API_DIR := pkg/api

generate-api:
	npx --yes @redocly/cli bundle \$(SPEC_DIR)/\$(SERVICE_NAME).yaml -o \$(BUNDLED_SPEC)
	ogen --target \$(API_DIR) --package api --clean \$(BUNDLED_SPEC)
	@echo "🎉 Generated!"

clean:
	rm -f \$(BUNDLED_SPEC)
	rm -rf \$(API_DIR)/oas_*_gen.go

.PHONY: test bench-quick build

# Run tests
test:
	@go test -v ./...

# Quick benchmark (short duration)
bench-quick:
	@if [ -f "server/handlers_bench_test.go" ] || find . -name "*_bench_test.go" | grep -q .; then \\
		go test -run=^\$\$ -bench=. -benchmem -benchtime=100ms ./server; \\
	fi

# Build (runs tests and benchmarks first)
build: test bench-quick
	@CGO_ENABLED=0 go build -ldflags="-w -s" -o bin/${service_name}-service .

.PHONY: bench bench-json bench-quick

# Run benchmarks (human-readable)
bench:
	go test -run=^ -bench=. -benchmem ./server

# Run benchmarks (JSON output for CI)
bench-json:
	@mkdir -p ../../.benchmarks/results
	go test -run=^ -bench=. -benchmem -json ./server > ../../.benchmarks/results/${service_name}_bench.json
EOF

    echo -e "${GREEN}OK Makefile created${NC}"
}

# Function to create go.mod
create_go_mod() {
    local service_name=$1

    cat > "services/${service_name}-go/go.mod" << EOF
module github.com/necpgame/necpgame-monorepo/services/${service_name}-go

go 1.24

require (
	github.com/go-chi/chi/v5 v5.0.10
	github.com/golang-jwt/jwt/v5 v5.2.0
	github.com/google/uuid v1.6.0
	github.com/lib/pq v1.10.9
	github.com/ogen-go/ogen v1.4.0
	github.com/prometheus/client_golang v1.18.0
	github.com/sirupsen/logrus v1.9.3
)
EOF

    echo -e "${GREEN}OK go.mod created${NC}"
}

# Function to update docker-compose.yml
update_docker_compose() {
    local service_name=$1
    local port=$2
    local description=$3

    echo -e "${BLUE}🔧 Updating docker-compose.yml...${NC}"

    # This is a simple append - in real scenario, you might want more sophisticated logic
    cat >> docker-compose.yml << EOF

  ${service_name}:
    build:
      context: ./services/${service_name}-go
      dockerfile: Dockerfile
    depends_on:
      - postgres
    ports:
      - "${port}:${port}"
    environment:
      - ADDR=0.0.0.0:${port}
      - DATABASE_URL=postgres://necpg:necpg@postgres:5432/necpg?sslmode=disable
      - JWT_SECRET=your-jwt-secret-change-in-production
    networks:
      - necpgame-network
EOF

    echo -e "${GREEN}OK docker-compose.yml updated${NC}"
}

# Main function
main() {
    if [ $# -lt 3 ]; then
        echo "Usage: $0 <service-name> <description> <port>"
        echo "Example: $0 my-service 'My awesome service' 8123"
        exit 1
    fi

    local service_name=$1
    local description=$2
    local port=$3

    echo -e "${BLUE}🚀 Creating new NECP Game service: $service_name${NC}"
    echo "Description: $description"
    echo "Port: $port"
    echo ""

    # Create service structure
    create_service_structure "$service_name" "$description" "$port"

    # Create core files
    create_go_mod "$service_name"
    create_main "$service_name" "$port" "$description"
    create_http_server "$service_name"
    create_handlers "$service_name"
    create_service "$service_name"
    create_repository "$service_name"
    create_makefile "$service_name"
    create_dockerfile "$service_name" "$port"

    # Update configuration
    update_docker_compose "$service_name" "$port" "$description"

    echo ""
    echo -e "${GREEN}🎉 Service '$service_name' created successfully!${NC}"
    echo ""
    echo "Next steps:"
    echo "1. Create OpenAPI specification: proto/openapi/${service_name}.yaml"
    echo "2. Run 'cd services/${service_name}-go && make generate-api'"
    echo "3. Implement business logic in server/service.go"
    echo "4. Test with './scripts/system-check.sh'"
    echo "5. Deploy with 'docker-compose up -d ${service_name}'"
}

main "$@"
