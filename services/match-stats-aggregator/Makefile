# Issue: #2214
# Makefile for Match Statistics Aggregator Service
# High-performance MMOFPS statistics aggregation system

.PHONY: all build run test clean docker-build docker-run lint format deps

# Go parameters
GOCMD=go
GOBUILD=$(GOCMD) build
GOCLEAN=$(GOCMD) clean
GOTEST=$(GOCMD) test
GOGET=$(GOCMD) get
GOMOD=$(GOCMD) mod
BINARY_NAME=match-stats-aggregator
BINARY_UNIX=$(BINARY_NAME)_unix

# Build parameters
BUILD_DIR=./bin
MAIN_PATH=./main.go

# Docker parameters
DOCKER_IMAGE=necpgame/match-stats-aggregator
DOCKER_TAG=latest

all: clean deps test build

# Build the binary
build:
	@echo "Building $(BINARY_NAME)..."
	@mkdir -p $(BUILD_DIR)
	CGO_ENABLED=0 GOOS=linux GOARCH=amd64 $(GOBUILD) -a -installsuffix cgo -o $(BUILD_DIR)/$(BINARY_NAME) $(MAIN_PATH)
	@echo "Build complete: $(BUILD_DIR)/$(BINARY_NAME)"

# Build for current platform (development)
build-dev:
	@echo "Building $(BINARY_NAME) for development..."
	@mkdir -p $(BUILD_DIR)
	$(GOBUILD) -o $(BUILD_DIR)/$(BINARY_NAME) $(MAIN_PATH)
	@echo "Development build complete: $(BUILD_DIR)/$(BINARY_NAME)"

# Run the service
run: build-dev
	@echo "Starting $(BINARY_NAME)..."
	$(BUILD_DIR)/$(BINARY_NAME) \
		--log-level=debug \
		--workers=2 \
		--buffer-size=1000

# Run with Redis
run-redis: build-dev
	@echo "Starting $(BINARY_NAME) with Redis..."
	$(BUILD_DIR)/$(BINARY_NAME) \
		--redis=localhost:6379 \
		--log-level=info \
		--workers=4 \
		--buffer-size=5000

# Run tests
test:
	@echo "Running tests..."
	$(GOTEST) -v -race -coverprofile=coverage.out ./...
	@echo "Tests complete"

# Run benchmarks
bench:
	@echo "Running benchmarks..."
	$(GOTEST) -bench=. -benchmem -run=^$ ./...
	@echo "Benchmarks complete"

# Clean build artifacts
clean:
	@echo "Cleaning..."
	$(GOCLEAN)
	@rm -rf $(BUILD_DIR)
	@rm -f coverage.out
	@echo "Clean complete"

# Download dependencies
deps:
	@echo "Downloading dependencies..."
	$(GOMOD) download
	$(GOMOD) tidy
	@echo "Dependencies updated"

# Update dependencies
deps-update:
	@echo "Updating dependencies..."
	$(GOGET) -u ./...
	$(GOMOD) tidy
	@echo "Dependencies updated"

# Run linter
lint:
	@echo "Running golangci-lint..."
	golangci-lint run --timeout=5m
	@echo "Lint complete"

# Format code
format:
	@echo "Formatting code..."
	gofmt -s -w .
	goimports -w .
	@echo "Code formatted"

# Docker build
docker-build:
	@echo "Building Docker image..."
	docker build -t $(DOCKER_IMAGE):$(DOCKER_TAG) .
	@echo "Docker image built: $(DOCKER_IMAGE):$(DOCKER_TAG)"

# Docker run
docker-run:
	@echo "Running Docker container..."
	docker run --rm -p 8080:8080 \
		-e REDIS_ADDR=host.docker.internal:6379 \
		$(DOCKER_IMAGE):$(DOCKER_TAG)

# Docker run with local Redis
docker-run-local:
	@echo "Running Docker container with local Redis..."
	docker run --rm -p 8080:8080 \
		--network host \
		$(DOCKER_IMAGE):$(DOCKER_TAG) \
		--redis=localhost:6379

# Health check
health:
	@echo "Checking service health..."
	curl -f http://localhost:8080/health || echo "Service not healthy"

# Load test (requires hey or similar tool)
load-test:
	@echo "Running load test..."
	hey -n 1000 -c 10 http://localhost:8080/api/v1/match-stats/system/stats

# Production build (optimized)
build-prod:
	@echo "Building optimized production binary..."
	@mkdir -p $(BUILD_DIR)
	CGO_ENABLED=0 GOOS=linux GOARCH=amd64 $(GOBUILD) \
		-a -installsuffix cgo \
		-ldflags="-w -s -extldflags '-static'" \
		-o $(BUILD_DIR)/$(BINARY_NAME) $(MAIN_PATH)
	@echo "Production build complete: $(BUILD_DIR)/$(BINARY_NAME)"
	@ls -lh $(BUILD_DIR)/$(BINARY_NAME)

# Cross-platform builds
build-linux:
	@echo "Building for Linux..."
	@mkdir -p $(BUILD_DIR)
	CGO_ENABLED=0 GOOS=linux GOARCH=amd64 $(GOBUILD) -o $(BUILD_DIR)/$(BINARY_NAME)-linux $(MAIN_PATH)

build-windows:
	@echo "Building for Windows..."
	@mkdir -p $(BUILD_DIR)
	CGO_ENABLED=0 GOOS=windows GOARCH=amd64 $(GOBUILD) -o $(BUILD_DIR)/$(BINARY_NAME)-windows.exe $(MAIN_PATH)

build-darwin:
	@echo "Building for macOS..."
	@mkdir -p $(BUILD_DIR)
	CGO_ENABLED=0 GOOS=darwin GOARCH=amd64 $(GOBUILD) -o $(BUILD_DIR)/$(BINARY_NAME)-darwin $(MAIN_PATH)

# All platforms
build-all: build-linux build-windows build-darwin
	@echo "Built binaries for all platforms in $(BUILD_DIR)/"

# Development setup
dev-setup: deps
	@echo "Setting up development environment..."
	@echo "Install golangci-lint: https://golangci-lint.run/usage/install/"
	@echo "Install hey (load testing): go install github.com/rakyll/hey@latest"
	@echo "Install goimports: go install golang.org/x/tools/cmd/goimports@latest"

# CI/CD pipeline simulation
ci: deps lint test build-prod
	@echo "CI/CD pipeline simulation complete"

# Performance profiling
profile:
	@echo "Starting service with profiling..."
	$(BUILD_DIR)/$(BINARY_NAME) --log-level=debug &
	@echo "Service started with PID: $$!"
	@echo "Run: go tool pprof http://localhost:8080/debug/pprof/profile"
	@echo "Press Ctrl+C to stop"
	@trap 'kill $$!' INT; sleep infinity

# Help
help:
	@echo "Available targets:"
	@echo "  build         - Build optimized Linux binary"
	@echo "  build-dev     - Build for current platform (development)"
	@echo "  build-prod    - Build optimized production binary"
	@echo "  build-all     - Build for Linux, Windows, and macOS"
	@echo "  run           - Build and run service (development)"
	@echo "  run-redis     - Build and run with Redis support"
	@echo "  test          - Run tests with coverage"
	@echo "  bench         - Run benchmarks"
	@echo "  clean         - Clean build artifacts"
	@echo "  deps          - Download and tidy dependencies"
	@echo "  deps-update   - Update all dependencies"
	@echo "  lint          - Run golangci-lint"
	@echo "  format        - Format code with gofmt and goimports"
	@echo "  docker-build  - Build Docker image"
	@echo "  docker-run    - Run Docker container"
	@echo "  health        - Check service health"
	@echo "  load-test     - Run load test (requires hey)"
	@echo "  profile       - Start service with profiling enabled"
	@echo "  ci            - Simulate CI/CD pipeline"
	@echo "  dev-setup     - Setup development environment"
	@echo "  help          - Show this help message"
