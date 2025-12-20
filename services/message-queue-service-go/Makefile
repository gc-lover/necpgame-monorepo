# OPTIMIZATION: Issue #2143 - Multi-stage Docker build for message queue security and performance
.PHONY: build test clean docker-build docker-push deploy

# Build configuration
BINARY_NAME=message-queue-service
VERSION?=1.0.0
BUILD_TIME=$(shell date -u +"%Y-%m-%dT%H:%M:%SZ")
GIT_COMMIT=$(shell git rev-parse --short HEAD 2>/dev/null || echo "unknown")

# Go build flags for message queue optimization
GO_LDFLAGS=-ldflags "-X main.version=$(VERSION) -X main.buildTime=$(BUILD_TIME) -X main.gitCommit=$(GIT_COMMIT) -s -w"
GO_BUILD_FLAGS=-trimpath -mod=readonly

# Docker configuration
DOCKER_REGISTRY?=localhost:5000
DOCKER_IMAGE=$(DOCKER_REGISTRY)/necpgame/message-queue-service
DOCKER_TAG?=latest

# Test configuration for message queue
TEST_TIMEOUT=30s
TEST_FLAGS=-v -race -coverprofile=coverage.out -timeout=$(TEST_TIMEOUT)

# Build the binary
build:
	@echo "Building $(BINARY_NAME)..."
	CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build $(GO_BUILD_FLAGS) $(GO_LDFLAGS) -o bin/$(BINARY_NAME) ./main.go
	@echo "Build complete: bin/$(BINARY_NAME)"

# Run tests with coverage
test:
	@echo "Running message queue service tests..."
	go test $(TEST_FLAGS) ./...
	go tool cover -html=coverage.out -o coverage.html
	@echo "Coverage report: coverage.html"

# Run benchmark tests for message queue performance
bench:
	@echo "Running message queue benchmarks..."
	go test -bench=. -benchmem -run=^$$ ./...

# Clean build artifacts
clean:
	@echo "Cleaning..."
	rm -rf bin/
	rm -f coverage.out coverage.html
	docker rmi $(DOCKER_IMAGE):$(DOCKER_TAG) 2>/dev/null || true

# Build Docker image with multi-stage build
docker-build:
	@echo "Building message queue service Docker image..."
	docker build \
		--build-arg VERSION=$(VERSION) \
		--build-arg BUILD_TIME=$(BUILD_TIME) \
		--build-arg GIT_COMMIT=$(GIT_COMMIT) \
		-t $(DOCKER_IMAGE):$(DOCKER_TAG) \
		-t $(DOCKER_IMAGE):$(VERSION) \
		.

# Push Docker image
docker-push:
	@echo "Pushing message queue service Docker image..."
	docker push $(DOCKER_IMAGE):$(DOCKER_TAG)
	docker push $(DOCKER_IMAGE):$(VERSION)

# Deploy to Kubernetes (requires kubectl configured)
deploy:
	@echo "Deploying message queue service to Kubernetes..."
	kubectl apply -f k8s/
	kubectl rollout status deployment/message-queue-service

# Development helpers
run: build
	@echo "Running $(BINARY_NAME)..."
	./bin/$(BINARY_NAME)

dev:
	@echo "Starting message queue service development server..."
	air 2>/dev/null || (echo "Installing air..." && go install github.com/cosmtrek/air@latest && air)

# Linting and formatting
lint:
	@echo "Running linters..."
	golangci-lint run ./... 2>/dev/null || (echo "Installing golangci-lint..." && go install github.com/golangci/golangci-lint/cmd/golangci-lint@latest && golangci-lint run ./... 2>/dev/null || echo "Linting failed")

fmt:
	@echo "Formatting message queue service code..."
	go fmt ./...
	goimports -w . 2>/dev/null || echo "goimports not installed, install with: go install golang.org/x/tools/cmd/goimports@latest"

# Security scanning
security-scan:
	@echo "Running security scan on message queue service..."
	gosec ./... 2>/dev/null || (echo "Installing gosec..." && go install github.com/securecodewarrior/gosec/v2/cmd/gosec@latest && gosec ./... 2>/dev/null || echo "Security scan failed")

# Performance profiling for message queue
profile:
	@echo "Starting message queue performance profiling..."
	go tool pprof -http=:8080 http://localhost:6867/debug/pprof/profile

# All-in-one build pipeline
all: clean lint security-scan test build docker-build

# Help
help:
	@echo "Available targets for message queue service:"
	@echo "  build          - Build the binary"
	@echo "  test           - Run tests with coverage"
	@echo "  bench          - Run benchmark tests"
	@echo "  clean          - Clean build artifacts"
	@echo "  docker-build   - Build Docker image"
	@echo "  docker-push    - Push Docker image"
	@echo "  deploy         - Deploy to Kubernetes"
	@echo "  run            - Run the binary"
	@echo "  dev            - Start development server with hot reload"
	@echo "  lint           - Run linters"
	@echo "  fmt            - Format code"
	@echo "  security-scan  - Run security scanning"
	@echo "  profile        - Start performance profiling"
	@echo "  all            - Run full pipeline"
	@echo "  help           - Show this help"
