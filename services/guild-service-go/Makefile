# Guild Service Makefile

.PHONY: all build run test clean docker-build docker-run lint fmt vet mod-tidy

# Variables
SERVICE_NAME=guild-service-go
BINARY_NAME=guild-service
DOCKER_IMAGE=necpgame/$(SERVICE_NAME):latest
MAIN_FILE=main.go

# Go parameters
GOCMD=go
GOBUILD=$(GOCMD) build
GOCLEAN=$(GOCMD) clean
GOTEST=$(GOCMD) test
GOGET=$(GOCMD) get
GOMOD=$(GOCMD) mod
GOFMT=gofmt
GOVET=$(GOCMD) vet

# Build the project
all: clean fmt vet test build

# Build binary
build:
	@echo "Building $(BINARY_NAME)..."
	CGO_ENABLED=0 GOOS=linux GOARCH=amd64 $(GOBUILD) -a -installsuffix cgo -o bin/$(BINARY_NAME) $(MAIN_FILE)

# Run the service
run:
	@echo "Running $(SERVICE_NAME)..."
	$(GOCMD) run $(MAIN_FILE)

# Run tests
test:
	@echo "Running tests..."
	$(GOTEST) -v -race -coverprofile=coverage.out ./...

# Run tests with benchmarking
bench:
	@echo "Running benchmarks..."
	$(GOTEST) -bench=. -benchmem ./...

# Clean build artifacts
clean:
	@echo "Cleaning..."
	$(GOCLEAN)
	rm -rf bin/
	rm -f coverage.out

# Run linter
lint:
	@echo "Running golangci-lint..."
	golangci-lint run

# Format code
fmt:
	@echo "Formatting code..."
	$(GOFMT) -w .

# Run go vet
vet:
	@echo "Running go vet..."
	$(GOVET) ./...

# Tidy modules
mod-tidy:
	@echo "Tidying modules..."
	$(GOMOD) tidy

# Update dependencies
mod-update:
	@echo "Updating dependencies..."
	$(GOMOD) download

# Docker build
docker-build:
	@echo "Building Docker image..."
	docker build -t $(DOCKER_IMAGE) .

# Docker run
docker-run:
	@echo "Running Docker container..."
	docker run --rm -p 8080:8080 $(DOCKER_IMAGE)

# Docker compose up
compose-up:
	@echo "Starting services with docker-compose..."
	docker-compose up -d

# Docker compose down
compose-down:
	@echo "Stopping services with docker-compose..."
	docker-compose down

# Show help
help:
	@echo "Available targets:"
	@echo "  all          - Clean, format, vet, test and build"
	@echo "  build        - Build the binary"
	@echo "  run          - Run the service"
	@echo "  test         - Run tests"
	@echo "  bench        - Run benchmarks"
	@echo "  clean        - Clean build artifacts"
	@echo "  lint         - Run golangci-lint"
	@echo "  fmt          - Format code"
	@echo "  vet          - Run go vet"
	@echo "  mod-tidy     - Tidy modules"
	@echo "  mod-update   - Update dependencies"
	@echo "  docker-build - Build Docker image"
	@echo "  docker-run   - Run Docker container"
	@echo "  compose-up   - Start with docker-compose"
	@echo "  compose-down - Stop with docker-compose"
