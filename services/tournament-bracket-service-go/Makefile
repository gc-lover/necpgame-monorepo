# Tournament Bracket Service Makefile
# Issue: #2210
# Agent: Backend Agent

.PHONY: help build test clean docker-build docker-run lint fmt vet mod-tidy

# Default target
help:
	@echo "Tournament Bracket Service"
	@echo ""
	@echo "Available targets:"
	@echo "  build       - Build the service binary"
	@echo "  test        - Run tests"
	@echo "  clean       - Clean build artifacts"
	@echo "  docker-build - Build Docker image"
	@echo "  docker-run   - Run Docker container"
	@echo "  lint         - Run linter"
	@echo "  fmt          - Format code"
	@echo "  vet          - Run go vet"
	@echo "  mod-tidy     - Tidy go modules"
	@echo "  run          - Run service locally"

# Build the service
build:
	@echo "Building tournament-bracket-service..."
	@go build -ldflags="-w -s" -o bin/tournament-bracket-service ./cmd/server
	@echo "Build complete: bin/tournament-bracket-service"

# Run tests
test:
	@echo "Running tests..."
	@go test -v -race -cover ./...

# Clean build artifacts
clean:
	@echo "Cleaning build artifacts..."
	@rm -rf bin/
	@go clean -cache -testcache -modcache

# Build Docker image
docker-build:
	@echo "Building Docker image..."
	@docker build -t necpgame/tournament-bracket-service:latest .
	@echo "Docker image built: necpgame/tournament-bracket-service:latest"

# Run Docker container
docker-run:
	@echo "Running Docker container..."
	@docker run --rm -p 8080:8080 \
		-e DATABASE_URL="postgres://user:password@localhost:5432/tournament?sslmode=disable" \
		necpgame/tournament-bracket-service:latest

# Run linter
lint:
	@echo "Running linter..."
	@golangci-lint run

# Format code
fmt:
	@echo "Formatting code..."
	@go fmt ./...
	@goimports -w .

# Run go vet
vet:
	@echo "Running go vet..."
	@go vet ./...

# Tidy go modules
mod-tidy:
	@echo "Tidying go modules..."
	@go mod tidy

# Run service locally
run: build
	@echo "Running service locally..."
	@DATABASE_URL="postgres://user:password@localhost:5432/tournament?sslmode=disable" \
	 ./bin/tournament-bracket-service

# Development setup
dev-setup:
	@echo "Setting up development environment..."
	@go mod download
	@go install golang.org/x/tools/cmd/goimports@latest
	@go install github.com/golangci/golangci-lint/cmd/golangci-lint@latest

# Performance testing
perf-test:
	@echo "Running performance tests..."
	@go test -bench=. -benchmem ./...

# Generate mocks (if needed)
mocks:
	@echo "Generating mocks..."
	@go generate ./...

# Database migrations (placeholder)
migrate-up:
	@echo "Running database migrations..."
	@echo "TODO: Implement migration runner"

migrate-down:
	@echo "Rolling back database migrations..."
	@echo "TODO: Implement migration rollback"