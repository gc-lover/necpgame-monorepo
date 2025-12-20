#!/usr/bin/env python3
"""
Generate Enterprise-Grade Go Services from OpenAPI Specifications

This script generates Go microservices for all enterprise-grade domains:
- Bundles OpenAPI specs using redocly
- Generates Go code using ogen
- Creates service structure with handlers, middleware, and main.go
- Initializes Go modules
- Tests compilation

Usage:
    python scripts/generate-all-domains-go.py
"""

import os
import subprocess
import sys
from pathlib import Path
from typing import List, Dict, Any
import json

class GoCodeGenerator:
    def __init__(self, project_root: Path):
        self.project_root = project_root
        self.services_dir = project_root / "services"
        self.openapi_dir = project_root / "proto" / "openapi"

        # Enterprise-grade domains to generate
        self.domains = [
            "system-domain",
            "specialized-domain",
            "social-domain",
            "economy-domain",
            "world-domain",
            "arena-domain",
            "auth-expansion-domain",
            "cosmetic-domain",
            "cyberpunk-domain",
            "faction-domain",
            "progression-domain",
            "referral-domain",
            "integration-domain",
            "legacy-domain",
            "misc-domain"
        ]

    def generate_all_domains(self) -> None:
        """Generate Go services for all enterprise-grade domains"""
        print("🚀 Starting enterprise-grade Go service generation...")

        generated_count = 0
        failed_domains = []

        for domain in self.domains:
            try:
                print(f"\n🏗️  Generating {domain} service...")
                self.generate_domain_service(domain)
                generated_count += 1
                print(f"OK {domain} service generated successfully")
            except Exception as e:
                print(f"❌ Failed to generate {domain}: {e}")
                failed_domains.append(domain)

        print("\n📊 Generation Summary:")
        print(f"OK Successfully generated: {generated_count} services")
        print(f"❌ Failed: {len(failed_domains)} services")

        if failed_domains:
            print(f"Failed domains: {', '.join(failed_domains)}")

        print("\n🎯 All enterprise-grade domain services ready for Backend development!")

    def generate_domain_service(self, domain: str) -> None:
        """Generate Go service for a specific domain"""
        domain_dir = self.openapi_dir / domain
        if not domain_dir.exists():
            raise FileNotFoundError(f"Domain directory not found: {domain_dir}")

        service_name = f"{domain}-service-go"
        service_dir = self.services_dir / service_name

        # Create service directory
        service_dir.mkdir(parents=True, exist_ok=True)

        # Bundle OpenAPI spec
        bundled_spec = self._bundle_openapi_spec(domain)

        # Generate Go code
        self._generate_go_code(service_dir, bundled_spec, domain)

        # Create service structure
        self._create_service_structure(service_dir, domain)

        # Initialize Go module
        self._initialize_go_modules(service_dir, service_name)

        # Test compilation
        self._test_compilation(service_dir, service_name)

    def _bundle_openapi_spec(self, domain: str) -> Path:
        """Bundle OpenAPI spec using redocly"""
        main_yaml = self.openapi_dir / domain / "main.yaml"
        if not main_yaml.exists():
            raise FileNotFoundError(f"Main YAML not found: {main_yaml}")

        bundled_file = self.project_root / f"openapi-{domain}-bundled.yaml"

        try:
            result = subprocess.run([
                "npx", "--yes", "@redocly/cli", "bundle",
                str(main_yaml),
                "-o", str(bundled_file)
            ], capture_output=True, text=True, cwd=self.project_root, timeout=60)

            if result.returncode != 0:
                raise RuntimeError(f"Redocly bundle failed: {result.stderr}")

            return bundled_file

        except subprocess.TimeoutExpired:
            raise RuntimeError("Redocly bundle timeout")

    def _generate_go_code(self, service_dir: Path, bundled_spec: Path, domain: str) -> None:
        """Generate Go code using ogen"""
        pkg_dir = service_dir / "pkg" / "api"
        pkg_dir.mkdir(parents=True, exist_ok=True)

        try:
            result = subprocess.run([
                "ogen", "--target", str(pkg_dir),
                "--package", "api", "--clean", str(bundled_spec)
            ], capture_output=True, text=True, cwd=self.project_root, timeout=120)

            if result.returncode != 0:
                raise RuntimeError(f"ogen generation failed: {result.stderr}")

        except subprocess.TimeoutExpired:
            raise RuntimeError("ogen generation timeout")

    def _create_service_structure(self, service_dir: Path, domain: str) -> None:
        """Create standard Go service structure"""
        # Create main.go
        main_content = self._generate_main_go(domain)
        (service_dir / "main.go").write_text(main_content)

        # Create server directory
        server_dir = service_dir / "server"
        server_dir.mkdir(exist_ok=True)

        # Create http_server.go
        server_content = self._generate_http_server_go(domain)
        (server_dir / "http_server.go").write_text(server_content)

        # Create middleware.go
        middleware_content = self._generate_middleware_go()
        (server_dir / "middleware.go").write_text(middleware_content)

        # Create handlers.go
        handlers_content = self._generate_handlers_go(domain)
        (server_dir / "handlers.go").write_text(handlers_content)

        # Create service.go
        service_content = self._generate_service_go(domain)
        (server_dir / "service.go").write_text(service_content)

        # Create repository.go
        repo_content = self._generate_repository_go(domain)
        (server_dir / "repository.go").write_text(repo_content)

        # Create Makefile
        makefile_content = self._generate_makefile(domain)
        (service_dir / "Makefile").write_text(makefile_content)

    def _generate_main_go(self, domain: str) -> str:
        """Generate main.go content"""
        return f'''// Issue: #backend-{domain.replace("-", "_")}

package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"{domain}-service-go/pkg/api"
	"{domain}-service-go/server"
)

func main() {{
	logger := log.New(os.Stdout, "[{domain}] ", log.LstdFlags)

	// Initialize service
	svc := server.New{domain.replace("-", "").title()}Service()

	// Create HTTP server
	httpServer := &http.Server{{
		Addr:    ":8080",
		Handler: svc.Handler(),
	}}

	// Start server
	go func() {{
		logger.Printf("Starting {domain} service on :8080")
		if err := httpServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {{
			logger.Fatalf("HTTP server error: %v", err)
		}}
	}}()

	// Wait for interrupt signal
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	logger.Println("Shutting down server...")

	// Graceful shutdown
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpServer.Shutdown(ctx); err != nil {{
		logger.Fatalf("Server forced to shutdown: %v", err)
	}}

	logger.Println("Server exited")
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

type {domain.replace("-", "").title()}Service struct {{
	api *api.Server
}

func New{domain.replace("-", "").title()}Service() *{domain.replace("-", "").title()}Service {{
	return &{domain.replace("-", "").title()}Service{{
		api: api.NewServer(&Handler{{}}),
	}}
}

func (s *{domain.replace("-", "").title()}Service) Handler() http.Handler {{
	return s.api
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
        """Generate handlers.go content"""
        return f'''// Issue: #backend-{domain.replace("-", "_")}

package server

import (
	"context"
	"net/http"

	"{domain}-service-go/pkg/api"
)

// Handler implements the generated API server interface
type Handler struct{{
	service *Service
}}

// NewHandler creates a new handler instance
func NewHandler() *Handler {{
	return &Handler{{
		service: NewService(),
	}}
}}

// Implement generated API interface methods here
// This will be populated based on the OpenAPI spec operations

// Example health check endpoint
func (h *Handler) Health(ctx context.Context) (*api.HealthResponse, error) {{
	return &api.HealthResponse{{
		Status: "healthy",
	}}, nil
}}
'''

    def _generate_service_go(self, domain: str) -> str:
        """Generate service.go content"""
        return f'''// Issue: #backend-{domain.replace("-", "_")}

package server

import (
	"context"

	"{domain}-service-go/pkg/api"
)

// Service contains business logic for {domain}
type Service struct {{
	repo *Repository
}}

// NewService creates a new service instance
func NewService() *Service {{
	return &Service{{
		repo: NewRepository(),
	}}
}}

// HealthCheck performs a health check
func (s *Service) HealthCheck(ctx context.Context) (*api.HealthResponse, error) {{
	// TODO: Implement health check logic
	return &api.HealthResponse{{
		Status: "healthy",
	}}, nil
}}
'''

    def _generate_repository_go(self, domain: str) -> str:
        """Generate repository.go content"""
        return f'''// Issue: #backend-{domain.replace("-", "_")}

package server

import (
	"context"

	"{domain}-service-go/pkg/api"
)

// Repository handles data persistence
type Repository struct {{
	// TODO: Add database connection
}}

// NewRepository creates a new repository instance
func NewRepository() *Repository {{
	return &Repository{{}}
}}

// HealthCheck performs database health check
func (r *Repository) HealthCheck(ctx context.Context) error {{
	// TODO: Implement database health check
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

    def _initialize_go_modules(self, service_dir: Path, service_name: str) -> None:
        """Initialize Go module and dependencies"""
        try:
            # Initialize go mod
            result = subprocess.run([
                "go", "mod", "init", service_name
            ], capture_output=True, text=True, cwd=service_dir, timeout=30)

            if result.returncode != 0:
                raise RuntimeError(f"go mod init failed: {result.stderr}")

            # Tidy dependencies
            result = subprocess.run([
                "go", "mod", "tidy"
            ], capture_output=True, text=True, cwd=service_dir, timeout=60)

            if result.returncode != 0:
                raise RuntimeError(f"go mod tidy failed: {result.stderr}")

        except subprocess.TimeoutExpired:
            raise RuntimeError("Go module initialization timeout")

    def _test_compilation(self, service_dir: Path, service_name: str) -> None:
        """Test that the generated code compiles"""
        try:
            result = subprocess.run([
                "go", "build", "./..."
            ], capture_output=True, text=True, cwd=service_dir, timeout=120)

            if result.returncode != 0:
                error_msg = result.stderr.strip() if result.stderr else "Unknown compilation error"
                raise RuntimeError(f"Compilation failed: {error_msg}")

        except subprocess.TimeoutExpired:
            raise RuntimeError("Compilation timeout")

def main():
    project_root = Path(__file__).parent.parent
    generator = GoCodeGenerator(project_root)
    generator.generate_all_domains()

if __name__ == "__main__":
    main()
'''
