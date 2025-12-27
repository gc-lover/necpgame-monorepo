# Stock Protection Service Makefile

# Variables
SERVICE_NAME=stock-protection-service-go
DOCKER_IMAGE=$(SERVICE_NAME):latest
PORT=8152

.PHONY: build run test clean docker-build docker-run docker-stop logs health-check

# Build the Go application
build:
	@echo "Building $(SERVICE_NAME)..."
	go build -o bin/$(SERVICE_NAME) .

# Run the application locally
run: build
	@echo "Running $(SERVICE_NAME) on port $(PORT)..."
	./bin/$(SERVICE_NAME)

# Run tests
test:
	@echo "Running tests..."
	go test -v ./...

# Clean build artifacts
clean:
	@echo "Cleaning build artifacts..."
	rm -rf bin/
	go clean

# Build Docker image
docker-build:
	@echo "Building Docker image $(DOCKER_IMAGE)..."
	docker build -t $(DOCKER_IMAGE) .

# Run Docker container
docker-run: docker-build
	@echo "Running Docker container on port $(PORT)..."
	docker run -d \
		--name $(SERVICE_NAME) \
		-p $(PORT):$(PORT) \
		-e DATABASE_URL="postgresql://user:password@localhost:5432/stockdb" \
		-e JWT_SECRET="your-jwt-secret-key" \
		$(DOCKER_IMAGE)

# Stop Docker container
docker-stop:
	@echo "Stopping Docker container..."
	docker stop $(SERVICE_NAME) || true
	docker rm $(SERVICE_NAME) || true

# Show logs
logs:
	docker logs -f $(SERVICE_NAME)

# Health check
health-check:
	@echo "Checking health..."
	curl -f http://localhost:$(PORT)/health || echo "Service is not healthy"

# Development setup
dev-setup:
	@echo "Setting up development environment..."
	go mod tidy
	go mod download

# Lint code
lint:
	@echo "Linting code..."
	golangci-lint run

# Format code
fmt:
	@echo "Formatting code..."
	go fmt ./...

# Security scan
security:
	@echo "Running security scan..."
	gosec ./...

# Performance test
bench:
	@echo "Running benchmarks..."
	go test -bench=. -benchmem ./...

# All-in-one development command
dev: clean dev-setup fmt lint test build
	@echo "Development build complete"

# Production build
prod: clean test security docker-build
	@echo "Production build complete"

# Help
help:
	@echo "Available commands:"
	@echo "  build        - Build the Go application"
	@echo "  run          - Run the application locally"
	@echo "  test         - Run tests"
	@echo "  clean        - Clean build artifacts"
	@echo "  docker-build - Build Docker image"
	@echo "  docker-run   - Run Docker container"
	@echo "  docker-stop  - Stop Docker container"
	@echo "  logs         - Show container logs"
	@echo "  health-check - Check service health"
	@echo "  dev-setup    - Setup development environment"
	@echo "  lint         - Lint code"
	@echo "  fmt          - Format code"
	@echo "  security     - Run security scan"
	@echo "  bench        - Run benchmarks"
	@echo "  dev          - All-in-one development build"
	@echo "  prod         - Production build"
	@echo "  help         - Show this help"


