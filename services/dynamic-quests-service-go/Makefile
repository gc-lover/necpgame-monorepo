# Dynamic Quests Service Makefile
# Issue: #2244
# Agent: Backend

.PHONY: build run test clean docker-build docker-run deps lint format

# Build the service binary
build:
	@echo "Building Dynamic Quests Service..."
	CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o bin/dynamic-quests-service main.go

# Run the service locally
run:
	@echo "Running Dynamic Quests Service..."
	go run main.go

# Run tests with coverage
test:
	@echo "Running tests..."
	go test -v -race -coverprofile=coverage.out ./...
	go tool cover -html=coverage.out -o coverage.html

# Clean build artifacts
clean:
	@echo "Cleaning build artifacts..."
	rm -rf bin/
	rm -f coverage.out coverage.html

# Build Docker image
docker-build:
	@echo "Building Docker image..."
	docker build -t necpgame/dynamic-quests-service:latest .

# Run in Docker container
docker-run:
	@echo "Running in Docker..."
	docker run --rm -p 8080:8080 \
		-e DB_HOST=localhost \
		-e DB_PORT=5432 \
		-e DB_USER=postgres \
		-e DB_PASSWORD=password \
		-e DB_NAME=necpgame \
		necpgame/dynamic-quests-service:latest

# Install dependencies
deps:
	@echo "Installing dependencies..."
	go mod download
	go mod tidy

# Run linter
lint:
	@echo "Running linter..."
	golangci-lint run

# Format code
format:
	@echo "Formatting code..."
	go fmt ./...

# Run health checks
health-check:
	@echo "Running health checks..."
	curl -f http://localhost:8080/health || echo "Health check failed"

# Database migration (placeholder)
migrate:
	@echo "Running database migrations..."
	@echo "Migration scripts should be run separately"

# Development setup
dev-setup: deps
	@echo "Development environment setup complete"

# Production build
prod-build: clean deps lint test build
	@echo "Production build complete"

# All-in-one development command
dev: dev-setup run

# Help
help:
	@echo "Available commands:"
	@echo "  build         - Build the service binary"
	@echo "  run           - Run the service locally"
	@echo "  test          - Run tests with coverage"
	@echo "  clean         - Clean build artifacts"
	@echo "  docker-build  - Build Docker image"
	@echo "  docker-run    - Run in Docker container"
	@echo "  deps          - Install dependencies"
	@echo "  lint          - Run linter"
	@echo "  format        - Format code"
	@echo "  health-check  - Run health checks"
	@echo "  migrate       - Run database migrations"
	@echo "  dev-setup     - Setup development environment"
	@echo "  prod-build    - Production build"
	@echo "  dev           - All-in-one development command"
	@echo "  help          - Show this help"

