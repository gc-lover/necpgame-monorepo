#!/usr/bin/env python3
"""
Create template files for enhanced service generator
"""

import os
from pathlib import Path

def create_template_dir():
    """Create templates directory"""
    templates_dir = Path(__file__).parent / "templates"
    templates_dir.mkdir(exist_ok=True)
    return templates_dir

def create_main_template(templates_dir):
    """Create main.go template"""
    template = '''// Issue: #backend-{domain}
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
	svc := server.New{domain.title()}Service()

	// PERFORMANCE: Configure HTTP server with optimized settings
	httpServer := &http.Server{{
		Addr:         ":8080",
		Handler:      svc.Handler(),
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
		logger.Printf("Starting {domain} service on :8080 (GOGC=%s, Estimated QPS: {estimated_qps})", os.Getenv("GOGC"))
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
    (templates_dir / "main.go.template").write_text(template)

def create_middleware_template(templates_dir):
    """Create middleware.go template"""
    template = '''package server

import (
	"log"
	"net/http"
	"strings"
	"time"
	{"github.com/golang-jwt/jwt/v4"" if needs_auth else ""}
	{"golang.org/x/time/rate"" if needs_rate_limiting else ""}
)

// LoggingMiddleware logs HTTP requests with performance metrics
func LoggingMiddleware(next http.Handler) http.Handler {{
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {{
		start := time.Now()

		// Wrap ResponseWriter to capture status code
		wrapped := &responseWriter{{ResponseWriter: w, statusCode: http.StatusOK}}

		next.ServeHTTP(wrapped, r)

		duration := time.Since(start)
		log.Printf("%s %s %d %v", r.Method, r.URL.Path, wrapped.statusCode, duration)
	}})
}}

// MetricsMiddleware collects HTTP metrics
func MetricsMiddleware(next http.Handler) http.Handler {{
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {{
		start := time.Now()

		wrapped := &responseWriter{{ResponseWriter: w, statusCode: http.StatusOK}}
		next.ServeHTTP(wrapped, r)

		duration := time.Since(start)
		// TODO: Send metrics to monitoring system
		_ = duration // Prevent unused variable warning
	}})
}}

type responseWriter struct {{
	http.ResponseWriter
	statusCode int
}}

func (rw *responseWriter) WriteHeader(code int) {{
	rw.statusCode = code
	rw.ResponseWriter.WriteHeader(code)
}}
{"""
// AuthMiddleware validates JWT tokens
func AuthMiddleware(next http.Handler) http.Handler {{
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {{
		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {{
			http.Error(w, "Missing authorization header", http.StatusUnauthorized)
			return
		}}

		// TODO: Implement JWT validation
		// For now, just check if header exists
		if !strings.HasPrefix(authHeader, "Bearer ") {{
			http.Error(w, "Invalid authorization header", http.StatusUnauthorized)
			return
		}}

		next.ServeHTTP(w, r)
	}})
}}
""" if needs_auth else ""}{"""
// RateLimitMiddleware implements rate limiting
func RateLimitMiddleware(next http.Handler) http.Handler {{
	// TODO: Implement proper rate limiting with Redis
	// For now, simple in-memory rate limiter
	limiter := rate.NewLimiter(rate.Limit(100), 100) // 100 requests per second

	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {{
		if !limiter.Allow() {{
			http.Error(w, "Rate limit exceeded", http.StatusTooManyRequests)
			return
		}}

		next.ServeHTTP(w, r)
	}})
}}
""" if needs_rate_limiting else ""}{"""
// CORSMiddleware handles CORS
func CORSMiddleware(next http.Handler) http.Handler {{
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
""" if needs_cors else ""}
'''
    (templates_dir / "middleware.go.template").write_text(template)

def create_makefile_template(templates_dir):
    """Create Makefile template"""
    template = '''# Makefile for {domain} service

.PHONY: build run test clean deps docker-build docker-run k8s-deploy

# Build the service
build:
	go build -o bin/{service_name} .

# Run the service
run:
	go run main.go

# Run tests
test:
	go test ./...

# Run integration tests
test-integration:
	go test -tags=integration ./tests/

# Clean build artifacts
clean:
	rm -rf bin/

# Install dependencies
deps:
	go mod tidy
	go mod download

# Format code
fmt:
	go fmt ./...

# Lint code
lint:
	golangci-lint run

# Generate API code (if spec changes)
generate-api:
	npx --yes @redocly/cli bundle ../../proto/openapi/{domain}/main.yaml -o openapi-bundled.yaml
	ogen --target pkg/api --package api --clean openapi-bundled.yaml

# Docker build
docker-build:
	docker build -t {service_name} .

# Docker run
docker-run:
	docker run -p 8080:8080 {service_name}

# Docker compose up
docker-up:
	docker-compose up -d

# Docker compose down
docker-down:
	docker-compose down

# Kubernetes deploy
k8s-deploy:
	kubectl apply -f k8s/

# Performance benchmark
bench:
	go test -bench=. -benchmem

# Profile CPU
profile-cpu:
	go tool pprof -http=:8081 cpu.prof

# Profile memory
profile-mem:
	go tool pprof -http=:8082 mem.prof

# Health check
health:
	curl -f http://localhost:8080/health

# All-in-one setup
setup: deps generate-api build docker-build
'''
    (templates_dir / "Makefile.template").write_text(template)

def create_dockerfile_template(templates_dir):
    """Create Dockerfile template"""
    template = '''# Dockerfile for {domain} service
FROM {base_image} AS builder

# Install build dependencies
RUN apk add --no-cache git ca-certificates

# Set working directory
WORKDIR /app

# Copy go mod files
COPY go.mod go.sum ./
RUN go mod download

# Copy source code
COPY . .

# Build optimized binary
RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o {service_name} .

# Final stage
FROM alpine:latest

# Install ca-certificates for HTTPS
RUN apk --no-cache add ca-certificates

# Create non-root user
RUN adduser -D -s /bin/sh appuser

WORKDIR /root/

# Copy binary from builder
COPY --from=builder /app/{service_name} .

# Change ownership
RUN chown appuser {service_name}

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \\
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/health || exit 1

# Run the binary
CMD ["./{service_name}"]
'''
    (templates_dir / "Dockerfile.template").write_text(template)

def main():
    """Create all template files"""
    templates_dir = create_template_dir()
    print(f"Creating templates in: {templates_dir}")

    create_main_template(templates_dir)
    create_middleware_template(templates_dir)
    create_makefile_template(templates_dir)
    create_dockerfile_template(templates_dir)

    print("[OK] All templates created successfully!")

if __name__ == "__main__":
    main()
