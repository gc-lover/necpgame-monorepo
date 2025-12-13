# Combat Combos Loadouts Service Makefile
# Issue: #141890005

SERVICE_NAME=combat-combos-loadouts-service-go
OPENAPI_SPEC=../../proto/openapi/combat-combos-loadouts-service.yaml

.PHONY: all build clean test lint generate deps run help

# Default target
all: deps generate build test

# Dependencies
deps:
	go mod tidy
	go mod download

# Generate ogen code from OpenAPI spec
generate:
	@echo "Generating ogen code from $(OPENAPI_SPEC)..."
	@redocly bundle $(OPENAPI_SPEC) -o openapi-bundled.yaml
	@ogen --target pkg/api --package api --clean openapi-bundled.yaml

# Build the service
build: generate
	@echo "Building $(SERVICE_NAME)..."
	@go build -o bin/$(SERVICE_NAME) ./main.go

# Run tests
test:
	@echo "Running tests..."
	@go test -v -race -coverprofile=coverage.out ./...
	@go tool cover -html=coverage.out -o coverage.html

# Run benchmarks
bench:
	@echo "Running benchmarks..."
	@go test -bench=. -benchmem -count=3 ./...

# Run linter
lint:
	@echo "Running linter..."
	@golangci-lint run --timeout=5m

# Clean generated files
clean:
	@echo "Cleaning generated files..."
	@rm -rf pkg/api openapi-bundled.yaml bin/ coverage.out coverage.html

# Run the service
run: build
	@echo "Starting $(SERVICE_NAME)..."
	@./bin/$(SERVICE_NAME)

# Development run with hot reload
dev:
	@echo "Starting in development mode..."
	@air

# Check file sizes (should be <500 lines each)
check-file-sizes:
	@echo "Checking file sizes..."
	@find . -name "*.go" -not -path "./pkg/api/*" -exec wc -l {} \; | awk '$$1 > 500 {print "ERROR: " $$2 " has " $$1 " lines (>500)"; exit 1}'

# Health check
health:
	@echo "Service health check..."
	@curl -f http://localhost:8083/health || echo "Service not healthy"

# Docker build
docker-build:
	@echo "Building Docker image..."
	@docker build -t $(SERVICE_NAME):latest .

# Docker run
docker-run:
	@echo "Running Docker container..."
	@docker run -p 8083:8083 $(SERVICE_NAME):latest

# Database migrations
migrate-up:
	@echo "Running database migrations..."
	@migrate -path ./migrations -database "postgres://user:password@localhost/dbname?sslmode=disable" up

migrate-down:
	@echo "Rolling back database migrations..."
	@migrate -path ./migrations -database "postgres://user:password@localhost/dbname?sslmode=disable" down 1

# Performance validation
validate-performance:
	@echo "Validating performance optimizations..."
	@go test -bench=BenchmarkHandlers -benchmem -count=5 ./server/
	@echo "Performance validation complete"

# Help
help:
	@echo "Available targets:"
	@echo "  all              - Run full build pipeline (deps, generate, build, test)"
	@echo "  deps             - Download and tidy dependencies"
	@echo "  generate         - Generate ogen code from OpenAPI spec"
	@echo "  build            - Build the service binary"
	@echo "  test             - Run unit tests with coverage"
	@echo "  bench            - Run benchmark tests"
	@echo "  lint             - Run golangci-lint"
	@echo "  clean            - Remove generated files and binaries"
	@echo "  run              - Build and run the service"
	@echo "  dev              - Run in development mode with hot reload"
	@echo "  check-file-sizes - Verify all source files are <500 lines"
	@echo "  health           - Check service health"
	@echo "  docker-build     - Build Docker image"
	@echo "  docker-run       - Run Docker container"
	@echo "  migrate-up       - Run database migrations"
	@echo "  migrate-down     - Rollback database migrations"
	@echo "  validate-performance - Run performance validation tests"
	@echo "  help             - Show this help message"

# Issue: #141890005
# Backend implementation for Combat Combos Loadouts Service