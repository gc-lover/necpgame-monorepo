# Anti-Cheat Behavior Analytics Service Makefile
# Issue: #2212
# Enterprise-grade build and deployment automation

.PHONY: all build run test clean docker-build docker-run deps lint format

# Default target
all: deps build test

# Build the service
build:
	@echo "Building Anti-Cheat Behavior Analytics Service..."
	@go build -o bin/anti-cheat-service ./main.go
	@echo "Build complete: bin/anti-cheat-service"

# Run the service
run: build
	@echo "Starting Anti-Cheat Behavior Analytics Service..."
	@./bin/anti-cheat-service

# Run with custom config
run-config: build
	@echo "Starting Anti-Cheat Behavior Analytics Service with custom config..."
	@./bin/anti-cheat-service -config=config.yaml

# Run tests
test:
	@echo "Running tests..."
	@go test -v ./...

# Run tests with coverage
test-coverage:
	@echo "Running tests with coverage..."
	@go test -v -coverprofile=coverage.out ./...
	@go tool cover -html=coverage.out -o coverage.html
	@echo "Coverage report: coverage.html"

# Clean build artifacts
clean:
	@echo "Cleaning build artifacts..."
	@rm -rf bin/
	@rm -f coverage.out coverage.html
	@go clean

# Install dependencies
deps:
	@echo "Installing dependencies..."
	@go mod download
	@go mod tidy

# Update dependencies
deps-update:
	@echo "Updating dependencies..."
	@go get -u ./...
	@go mod tidy

# Run linter
lint:
	@echo "Running linter..."
	@golangci-lint run

# Format code
format:
	@echo "Formatting code..."
	@go fmt ./...
	@gofmt -s -w .

# Build Docker image
docker-build:
	@echo "Building Docker image..."
	@docker build -t necpgame/anti-cheat-behavior-analytics:latest .

# Run Docker container
docker-run:
	@echo "Running Docker container..."
	@docker run -p 8080:8080 --env-file .env necpgame/anti-cheat-behavior-analytics:latest

# Development setup
dev-setup: deps
	@echo "Setting up development environment..."
	@go install github.com/golangci/golangci-lint/cmd/golangci-lint@latest
	@echo "Development environment ready"

# Health check
health-check:
	@echo "Checking service health..."
	@curl -f http://localhost:8080/health || echo "Service not healthy"

# Generate API documentation
docs:
	@echo "Generating API documentation..."
	@swag init -g main.go -o docs/

# Database migrations (placeholder)
migrate-up:
	@echo "Running database migrations..."
	# Add your migration tool here (e.g., migrate, liquibase)

migrate-down:
	@echo "Rolling back database migrations..."
	# Add your migration tool here

# Load test
load-test:
	@echo "Running load tests..."
	# Add your load testing tool here (e.g., k6, artillery)

# Security scan
security-scan:
	@echo "Running security scan..."
	@gosec ./...

# Performance profiling
profile:
	@echo "Running performance profiling..."
	@go test -bench=. -benchmem ./...

# Cross-platform build
build-linux:
	@echo "Building for Linux..."
	@CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -o bin/anti-cheat-service-linux ./main.go

build-windows:
	@echo "Building for Windows..."
	@CGO_ENABLED=0 GOOS=windows GOARCH=amd64 go build -o bin/anti-cheat-service.exe ./main.go

build-darwin:
	@echo "Building for macOS..."
	@CGO_ENABLED=0 GOOS=darwin GOARCH=amd64 go build -o bin/anti-cheat-service-darwin ./main.go

# CI/CD targets
ci: deps lint test build

cd: docker-build
	@echo "Deploying to production..."
	# Add your deployment commands here

# Help
help:
	@echo "Anti-Cheat Behavior Analytics Service Makefile"
	@echo ""
	@echo "Available targets:"
	@echo "  all              - Run deps, build, test"
	@echo "  build            - Build the service"
	@echo "  run              - Run the service"
	@echo "  test             - Run tests"
	@echo "  clean            - Clean build artifacts"
	@echo "  deps             - Install dependencies"
	@echo "  lint             - Run linter"
	@echo "  format           - Format code"
	@echo "  docker-build     - Build Docker image"
	@echo "  docker-run       - Run Docker container"
	@echo "  health-check     - Check service health"
	@echo "  ci               - CI pipeline (deps, lint, test, build)"
	@echo "  help             - Show this help"
