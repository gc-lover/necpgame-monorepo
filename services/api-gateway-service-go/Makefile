# API Gateway Service Makefile
# Enterprise-grade build, test, and deployment automation

.PHONY: all build test clean docker-build docker-run deps lint format

# Variables
SERVICE_NAME=api-gateway-service-go
BINARY_NAME=api-gateway
DOCKER_IMAGE=necpgame/$(SERVICE_NAME):latest

# Default target
all: deps build

# Dependencies
deps:
	go mod tidy
	go mod download

# Build
build:
	@echo "Building $(SERVICE_NAME)..."
	CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o bin/$(BINARY_NAME) .
	@echo "Build complete: bin/$(BINARY_NAME)"

# Test
test:
	@echo "Running tests..."
	go test -v -race -coverprofile=coverage.out ./...
	go tool cover -html=coverage.out -o coverage.html
	@echo "Tests complete"

# Lint
lint:
	@echo "Running linter..."
	golangci-lint run

# Format
format:
	@echo "Formatting code..."
	go fmt ./...
	goimports -w .

# Clean
clean:
	@echo "Cleaning..."
	rm -rf bin/
	rm -f coverage.out coverage.html
	go clean

# Docker build
docker-build:
	@echo "Building Docker image..."
	docker build -t $(DOCKER_IMAGE) .
	@echo "Docker image built: $(DOCKER_IMAGE)"

# Docker run
docker-run:
	@echo "Running Docker container..."
	docker run -p 8080:8080 --env-file .env $(DOCKER_IMAGE)

# Development run
run:
	@echo "Starting $(SERVICE_NAME)..."
	go run main.go

# Production run
run-prod:
	@echo "Starting $(SERVICE_NAME) in production mode..."
	./bin/$(BINARY_NAME)

# Health check
health-check:
	@echo "Checking service health..."
	curl -f http://localhost:8080/health || echo "Service not healthy"

# Load test
load-test:
	@echo "Running load test..."
	ab -n 1000 -c 10 http://localhost:8080/health

# CI pipeline
ci: deps lint test build docker-build
	@echo "CI pipeline complete"

# Development setup
dev-setup: deps
	@echo "Development environment ready"

# Help
help:
	@echo "Available targets:"
	@echo "  all          - Run deps and build"
	@echo "  deps         - Download dependencies"
	@echo "  build        - Build the service"
	@echo "  test         - Run tests with coverage"
	@echo "  lint         - Run linter"
	@echo "  format       - Format code"
	@echo "  clean        - Clean build artifacts"
	@echo "  docker-build - Build Docker image"
	@echo "  docker-run   - Run Docker container"
	@echo "  run          - Run in development mode"
	@echo "  run-prod     - Run in production mode"
	@echo "  health-check - Check service health"
	@echo "  load-test    - Run load test"
	@echo "  ci           - Run full CI pipeline"
	@echo "  dev-setup    - Setup development environment"
