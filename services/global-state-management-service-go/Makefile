# Global State Management Service Makefile
# Enterprise-grade build, test, and deployment automation

.PHONY: all build test clean docker-build docker-run docker-push deploy lint format

# Variables
SERVICE_NAME := global-state-management-service-go
DOCKER_REGISTRY := your-registry.com
DOCKER_TAG := $(shell git rev-parse --short HEAD)
GO_VERSION := 1.21

# Default target
all: clean lint test build

# Build the service
build:
	@echo "Building $(SERVICE_NAME)..."
	CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build \
		-ldflags='-w -s -extldflags "-static"' \
		-a -installsuffix cgo \
		-o bin/$(SERVICE_NAME) \
		.
	@echo "Build complete: bin/$(SERVICE_NAME)"

# Run tests
test:
	@echo "Running tests..."
	go test -v -race -coverprofile=coverage.out ./...
	go tool cover -html=coverage.out -o coverage.html
	@echo "Tests complete. Coverage report: coverage.html"

# Run tests with benchmarks
bench:
	@echo "Running benchmarks..."
	go test -bench=. -benchmem -run=^$ ./...
	@echo "Benchmarks complete"

# Lint the code
lint:
	@echo "Running linter..."
	golangci-lint run --timeout=5m
	@echo "Linting complete"

# Format the code
format:
	@echo "Formatting code..."
	go fmt ./...
	goimports -w .
	@echo "Code formatting complete"

# Clean build artifacts
clean:
	@echo "Cleaning build artifacts..."
	rm -rf bin/
	rm -f coverage.out coverage.html
	go clean
	@echo "Clean complete"

# Build Docker image
docker-build:
	@echo "Building Docker image..."
	docker build -t $(SERVICE_NAME):$(DOCKER_TAG) .
	docker tag $(SERVICE_NAME):$(DOCKER_TAG) $(SERVICE_NAME):latest
	@echo "Docker image built: $(SERVICE_NAME):$(DOCKER_TAG)"

# Run Docker container locally
docker-run:
	@echo "Running Docker container..."
	docker run -p 8080:8080 \
		-e POSTGRES_URL="postgresql://postgres:postgres@localhost:5432/necpgame" \
		-e REDIS_ADDR="localhost:6379" \
		-e KAFKA_BROKERS="localhost:9092" \
		$(SERVICE_NAME):$(DOCKER_TAG)

# Push Docker image to registry
docker-push:
	@echo "Pushing Docker image to registry..."
	docker tag $(SERVICE_NAME):$(DOCKER_TAG) $(DOCKER_REGISTRY)/$(SERVICE_NAME):$(DOCKER_TAG)
	docker push $(DOCKER_REGISTRY)/$(SERVICE_NAME):$(DOCKER_TAG)
	docker tag $(SERVICE_NAME):$(DOCKER_TAG) $(DOCKER_REGISTRY)/$(SERVICE_NAME):latest
	docker push $(DOCKER_REGISTRY)/$(SERVICE_NAME):latest
	@echo "Docker image pushed: $(DOCKER_REGISTRY)/$(SERVICE_NAME):$(DOCKER_TAG)"

# Deploy to Kubernetes
deploy:
	@echo "Deploying to Kubernetes..."
	kubectl apply -f k8s/
	kubectl rollout status deployment/$(SERVICE_NAME)
	@echo "Deployment complete"

# Run locally for development
run:
	@echo "Running service locally..."
	go run main.go

# Generate mocks for testing
mocks:
	@echo "Generating mocks..."
	mockery --all --output ./mocks
	@echo "Mocks generated"

# Run integration tests
integration-test:
	@echo "Running integration tests..."
	go test -tags=integration -v ./...
	@echo "Integration tests complete"

# Run performance tests
perf-test:
	@echo "Running performance tests..."
	go test -bench=. -benchmem -run=^$ -tags=perf ./...
	@echo "Performance tests complete"

# Update dependencies
deps:
	@echo "Updating dependencies..."
	go mod tidy
	go mod verify
	@echo "Dependencies updated"

# Generate API documentation
docs:
	@echo "Generating API documentation..."
	swag init -g main.go
	@echo "API documentation generated"

# Check security vulnerabilities
security:
	@echo "Running security checks..."
	gosec ./...
	trivy filesystem --exit-code 1 --no-progress .
	@echo "Security checks complete"

# Run all quality checks
quality: lint format security test
	@echo "All quality checks passed"

# Development setup
dev-setup:
	@echo "Setting up development environment..."
	go install github.com/golangci/golangci-lint/cmd/golangci-lint@latest
	go install golang.org/x/tools/cmd/goimports@latest
	go install github.com/securecodewarrior/gosec/v2/cmd/gosec@latest
	@echo "Development environment setup complete"

# Help
help:
	@echo "Available targets:"
	@echo "  all             - Run clean, lint, test, build"
	@echo "  build           - Build the service"
	@echo "  test            - Run unit tests"
	@echo "  bench           - Run benchmarks"
	@echo "  lint            - Run linter"
	@echo "  format          - Format code"
	@echo "  clean           - Clean build artifacts"
	@echo "  docker-build    - Build Docker image"
	@echo "  docker-run      - Run Docker container"
	@echo "  docker-push     - Push Docker image"
	@echo "  deploy          - Deploy to Kubernetes"
	@echo "  run             - Run locally for development"
	@echo "  mocks           - Generate mocks"
	@echo "  integration-test- Run integration tests"
	@echo "  perf-test       - Run performance tests"
	@echo "  deps            - Update dependencies"
	@echo "  docs            - Generate API documentation"
	@echo "  security        - Run security checks"
	@echo "  quality         - Run all quality checks"
	@echo "  dev-setup       - Setup development environment"
	@echo "  help            - Show this help"

# Default GOGC setting for better performance
export GOGC=150

