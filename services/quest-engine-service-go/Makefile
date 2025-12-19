# Issue: #176
.PHONY: all build test clean docker-build docker-run lint fmt vet bench profile

# Variables
SERVICE_NAME=quest-engine-service
DOCKER_IMAGE=necp/$(SERVICE_NAME)
VERSION=$(shell git describe --tags --always --dirty)
COMMIT=$(shell git rev-parse --short HEAD)
BUILD_TIME=$(shell date -u +"%Y-%m-%dT%H:%M:%SZ")

# Go parameters
GOCMD=go
GOBUILD=$(GOCMD) build
GOCLEAN=$(GOCMD) clean
GOTEST=$(GOCMD) test
GOGET=$(GOCMD) get
GOMOD=$(GOCMD) mod
GOFMT=gofmt
GOVET=$(GOCMD) vet

# Build flags for performance optimization
LDFLAGS=-ldflags "-X main.version=$(VERSION) -X main.commit=$(COMMIT) -X main.buildTime=$(BUILD_TIME) -w -s"
GCFLAGS=-gcflags="all=-l -B"

# Directories
BUILD_DIR=./build
DIST_DIR=./dist

all: clean lint test build

# Build the binary with optimizations
build:
	@echo "Building $(SERVICE_NAME)..."
	@mkdir -p $(BUILD_DIR)
	CGO_ENABLED=0 GOOS=linux GOARCH=amd64 $(GOBUILD) $(LDFLAGS) $(GCFLAGS) -o $(BUILD_DIR)/$(SERVICE_NAME) .
	@echo "Build complete: $(BUILD_DIR)/$(SERVICE_NAME)"

# Run tests with coverage
test:
	@echo "Running tests..."
	$(GOTEST) -v -race -coverprofile=coverage.out ./...
	$(GOCMD) tool cover -html=coverage.out -o coverage.html

# Run benchmarks
bench:
	@echo "Running benchmarks..."
	$(GOTEST) -bench=. -benchmem ./...

# Run linting
lint:
	@echo "Running golangci-lint..."
	golangci-lint run

# Format code
fmt:
	@echo "Formatting code..."
	$(GOFMT) -s -w .

# Vet code
vet:
	@echo "Vetting code..."
	$(GOVET) ./...

# Clean build artifacts
clean:
	@echo "Cleaning..."
	$(GOCLEAN)
	rm -rf $(BUILD_DIR) $(DIST_DIR)
	rm -f coverage.out coverage.html

# Generate OpenAPI code
generate-api:
	@echo "Generating OpenAPI code..."
	ogen --target pkg/api --clean openapi-bundled.yaml

# Update dependencies
deps:
	@echo "Updating dependencies..."
	$(GOMOD) tidy
	$(GOMOD) download

# Build Docker image
docker-build:
	@echo "Building Docker image..."
	docker build -t $(DOCKER_IMAGE):$(VERSION) -t $(DOCKER_IMAGE):latest .

# Run Docker container
docker-run:
	@echo "Running Docker container..."
	docker run --rm -p 8300:8300 -p 8301:8301 -p 9092:9092 \
		-e ENVIRONMENT=development \
		-e DATABASE_URL="postgres://user:pass@localhost:5432/necp_game" \
		-e KAFKA_BROKERS="localhost:9092" \
		$(DOCKER_IMAGE):latest

# Development run
dev:
	@echo "Running in development mode..."
	go run $(LDFLAGS) .

# Profile CPU
profile-cpu:
	@echo "Profiling CPU..."
	$(GOTEST) -cpuprofile=cpu.prof -bench=. ./server/
	go tool pprof cpu.prof

# Profile memory
profile-mem:
	@echo "Profiling memory..."
	$(GOTEST) -memprofile=mem.prof -bench=. ./server/
	go tool pprof mem.prof

# Run with race detector
race:
	@echo "Running with race detector..."
	$(GOTEST) -race ./...

# Security scan
security:
	@echo "Running security scan..."
	gosec ./...

# Dependency check
deps-check:
	@echo "Checking dependencies for vulnerabilities..."
	govulncheck ./...

# CI pipeline
ci: clean deps fmt lint vet test build

# Release build
release: clean ci
	@echo "Creating release build..."
	@mkdir -p $(DIST_DIR)
	CGO_ENABLED=0 GOOS=linux GOARCH=amd64 $(GOBUILD) $(LDFLAGS) $(GCFLAGS) -o $(DIST_DIR)/$(SERVICE_NAME)-linux-amd64 .
	CGO_ENABLED=0 GOOS=darwin GOARCH=amd64 $(GOBUILD) $(LDFLAGS) $(GCFLAGS) -o $(DIST_DIR)/$(SERVICE_NAME)-darwin-amd64 .
	CGO_ENABLED=0 GOOS=windows GOARCH=amd64 $(GOBUILD) $(LDFLAGS) $(GCFLAGS) -o $(DIST_DIR)/$(SERVICE_NAME)-windows-amd64.exe .

# Help
help:
	@echo "Available targets:"
	@echo "  all          - Run clean, lint, test, build"
	@echo "  build        - Build the binary with optimizations"
	@echo "  test         - Run tests with coverage"
	@echo "  bench        - Run benchmarks"
	@echo "  lint         - Run golangci-lint"
	@echo "  fmt          - Format code"
	@echo "  vet          - Vet code"
	@echo "  clean        - Clean build artifacts"
	@echo "  generate-api - Generate OpenAPI code"
	@echo "  deps         - Update dependencies"
	@echo "  docker-build - Build Docker image"
	@echo "  docker-run   - Run Docker container"
	@echo "  dev          - Run in development mode"
	@echo "  ci           - Run CI pipeline"
	@echo "  release      - Create release builds"
	@echo "  profile-cpu  - Profile CPU usage"
	@echo "  profile-mem  - Profile memory usage"
	@echo "  race         - Run with race detector"
	@echo "  security     - Run security scan"
	@echo "  deps-check   - Check dependencies for vulnerabilities"




