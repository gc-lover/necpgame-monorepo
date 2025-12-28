# Issue: #2213 - Tournament Spectator Mode Implementation
# Makefile for Tournament Spectator Service - Enterprise-grade build automation

.PHONY: all build test clean docker-build docker-run lint fmt vet mod-tidy help

# Variables
SERVICE_NAME=tournament-spectator-service-go
BINARY_NAME=tournament-spectator-service
VERSION ?= $(shell git describe --tags --always --dirty)
BUILD_TIME=$(shell date -u +"%Y-%m-%dT%H:%M:%SZ")
COMMIT_HASH=$(shell git rev-parse --short HEAD)
GO_VERSION=$(shell go version | cut -d ' ' -f 3)

# Build flags
LDFLAGS=-ldflags "-s -w -X main.Version=$(VERSION) -X main.BuildTime=$(BUILD_TIME) -X main.CommitHash=$(COMMIT_HASH)"

# Default target
all: clean mod-tidy fmt vet test build

# Build the binary
build:
	@echo "Building $(SERVICE_NAME)..."
	@CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build $(LDFLAGS) -o bin/$(BINARY_NAME) main.go
	@echo "Binary built: bin/$(BINARY_NAME)"

# Build for development
build-dev:
	@echo "Building $(SERVICE_NAME) for development..."
	@go build $(LDFLAGS) -o bin/$(BINARY_NAME)-dev main.go
	@echo "Development binary built: bin/$(BINARY_NAME)-dev"

# Run tests
test:
	@echo "Running tests..."
	@go test -v -race -coverprofile=coverage.out ./...
	@go tool cover -html=coverage.out -o coverage.html
	@echo "Coverage report generated: coverage.html"

# Run tests with short flag
test-short:
	@echo "Running short tests..."
	@go test -short -v ./...

# Clean build artifacts
clean:
	@echo "Cleaning build artifacts..."
	@rm -rf bin/
	@rm -f coverage.out coverage.html
	@go clean

# Format code
fmt:
	@echo "Formatting code..."
	@go fmt ./...

# Vet code
vet:
	@echo "Vetting code..."
	@go vet ./...

# Run linter
lint:
	@echo "Running linter..."
	@golangci-lint run

# Tidy modules
mod-tidy:
	@echo "Tidying modules..."
	@go mod tidy
	@go mod verify

# Download dependencies
mod-download:
	@echo "Downloading dependencies..."
	@go mod download

# Build Docker image
docker-build:
	@echo "Building Docker image..."
	@docker build -t $(SERVICE_NAME):$(VERSION) .
	@docker tag $(SERVICE_NAME):$(VERSION) $(SERVICE_NAME):latest

# Run Docker container
docker-run:
	@echo "Running Docker container..."
	@docker run -p 8090:8090 --env-file .env $(SERVICE_NAME):latest

# Run in development mode
dev:
	@echo "Running in development mode..."
	@air

# Generate mocks for testing
mocks:
	@echo "Generating mocks..."
	@mockery --all --output ./mocks

# Run integration tests
integration-test:
	@echo "Running integration tests..."
	@go test -tags=integration -v ./tests/...

# Run performance tests
perf-test:
	@echo "Running performance tests..."
	@go test -bench=. -benchmem ./...

# Create release archive
release: clean build
	@echo "Creating release archive..."
	@mkdir -p release
	@tar -czf release/$(SERVICE_NAME)-$(VERSION).tar.gz bin/$(BINARY_NAME)
	@sha256sum release/$(SERVICE_NAME)-$(VERSION).tar.gz > release/$(SERVICE_NAME)-$(VERSION).tar.gz.sha256
	@echo "Release archive created: release/$(SERVICE_NAME)-$(VERSION).tar.gz"

# Install development tools
install-tools:
	@echo "Installing development tools..."
	@go install github.com/cosmtrek/air@latest
	@go install github.com/golangci/golangci-lint/cmd/golangci-lint@latest
	@go install github.com/vektra/mockery/v2@latest

# Show version information
version:
	@echo "$(SERVICE_NAME) version information:"
	@echo "  Version: $(VERSION)"
	@echo "  Build Time: $(BUILD_TIME)"
	@echo "  Commit: $(COMMIT_HASH)"
	@echo "  Go Version: $(GO_VERSION)"

# Show help
help:
	@echo "$(SERVICE_NAME) - Tournament Spectator Service"
	@echo ""
	@echo "Available targets:"
	@echo "  all           - Run clean, mod-tidy, fmt, vet, test, and build"
	@echo "  build         - Build the binary for production"
	@echo "  build-dev     - Build the binary for development"
	@echo "  test          - Run all tests with coverage"
	@echo "  test-short    - Run short tests"
	@echo "  clean         - Clean build artifacts"
	@echo "  fmt           - Format code"
	@echo "  vet           - Vet code"
	@echo "  lint          - Run linter"
	@echo "  mod-tidy      - Tidy and verify modules"
	@echo "  mod-download  - Download dependencies"
	@echo "  docker-build  - Build Docker image"
	@echo "  docker-run    - Run Docker container"
	@echo "  dev           - Run in development mode with air"
	@echo "  mocks         - Generate mocks for testing"
	@echo "  integration-test - Run integration tests"
	@echo "  perf-test     - Run performance tests"
	@echo "  release       - Create release archive"
	@echo "  install-tools - Install development tools"
	@echo "  version       - Show version information"
	@echo "  help          - Show this help message"
	@echo ""

# Default target
.DEFAULT_GOAL := help
