# Integration Domain Service Makefile
# Enterprise-grade build, test, and deployment automation

.PHONY: help build test clean docker-build docker-run deps lint format ci

# Default target
help:
	@echo "Available targets:"
	@echo "  build       - Build the service binary"
	@echo "  test        - Run tests"
	@echo "  clean       - Clean build artifacts"
	@echo "  docker-build- Build Docker image"
	@echo "  docker-run  - Run Docker container"
	@echo "  deps        - Download dependencies"
	@echo "  lint        - Run linter"
	@echo "  format      - Format code"
	@echo "  ci          - Run CI pipeline"

# Build the service
build:
	@echo "Building Integration Domain Service..."
	CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -a -installsuffix cgo -o bin/integration-domain-service .
	@echo "Build complete: bin/integration-domain-service"

# Run tests
test:
	@echo "Running tests..."
	go test -v -race -coverprofile=coverage.out ./...
	go tool cover -html=coverage.out -o coverage.html
	@echo "Tests complete. Coverage report: coverage.html"

# Clean build artifacts
clean:
	@echo "Cleaning build artifacts..."
	rm -f bin/integration-domain-service
	rm -f coverage.out coverage.html
	go clean -cache -testcache -modcache

# Download dependencies
deps:
	@echo "Downloading dependencies..."
	go mod download
	go mod tidy

# Run linter
lint:
	@echo "Running linter..."
	golangci-lint run --timeout=5m

# Format code
format:
	@echo "Formatting code..."
	go fmt ./...
	goimports -w .

# Build Docker image
docker-build:
	@echo "Building Docker image..."
	docker build -t necpgame/integration-domain-service:latest .
	docker tag necpgame/integration-domain-service:latest necpgame/integration-domain-service:$(shell date +%Y%m%d-%H%M%S)

# Run Docker container
docker-run:
	@echo "Running Docker container..."
	docker run -p 8080:8080 \
		-e DATABASE_URL="postgres://user:pass@localhost/integration?sslmode=disable" \
		-e LOG_LEVEL="info" \
		--name integration-domain-service \
		necpgame/integration-domain-service:latest

# Run with Redis
docker-run-redis:
	@echo "Running Docker container with Redis..."
	docker run -p 8080:8080 \
		-e DATABASE_URL="postgres://user:pass@localhost/integration?sslmode=disable" \
		-e REDIS_URL="redis://localhost:6379" \
		-e LOG_LEVEL="debug" \
		--name integration-domain-service \
		necpgame/integration-domain-service:latest

# Stop Docker container
docker-stop:
	@echo "Stopping Docker container..."
	docker stop integration-domain-service || true
	docker rm integration-domain-service || true

# Run locally for development
run:
	@echo "Running service locally..."
	go run main.go

# Run with debug logging
run-debug:
	@echo "Running service with debug logging..."
	LOG_LEVEL=debug go run main.go

# Generate mocks for testing
mocks:
	@echo "Generating mocks..."
	mockery --all --output ./mocks

# Run integration tests
test-integration:
	@echo "Running integration tests..."
	go test -v -tags=integration ./...

# Run performance tests
bench:
	@echo "Running benchmarks..."
	go test -bench=. -benchmem ./...

# CI pipeline
ci: deps lint test build
	@echo "CI pipeline complete"

# Development setup
dev-setup: deps
	@echo "Development environment setup complete"
	@echo "Run 'make run' to start the service"

# Production build
prod-build:
	@echo "Building for production..."
	CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -a -installsuffix cgo -ldflags="-w -s" -o bin/integration-domain-service .
	@echo "Production build complete"

# Security scan
security-scan:
	@echo "Running security scan..."
	gosec ./...

# Vulnerability check
vuln-check:
	@echo "Checking for vulnerabilities..."
	govulncheck ./...

# Update dependencies
update-deps:
	@echo "Updating dependencies..."
	go get -u ./...
	go mod tidy

# Show version
version:
	@echo "Integration Domain Service v1.0.0"


