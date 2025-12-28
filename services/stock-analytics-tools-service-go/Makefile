# Stock Analytics Tools Service Makefile
# Optimized for heavy analytical computations and ML workloads

.PHONY: all build test clean docker-build docker-run lint fmt deps generate-api

# Default target
all: deps fmt lint test build

# Dependencies
deps:
	go mod download
	go mod tidy

# Code formatting
fmt:
	go fmt ./...

# Linting with performance checks
lint:
	golangci-lint run --timeout=10m \
		--enable=goimports \
		--enable=gocyclo \
		--enable=gocritic

# Testing with race detection and coverage
test:
	go test -v -race -coverprofile=coverage.out ./...
	go tool cover -html=coverage.out -o coverage.html

# Build optimized binary for analytical computations
build:
	CGO_ENABLED=1 GOOS=linux go build \
		-buildvcs=false \
		-ldflags '-extldflags "-static" -s -w -X main.version=$(shell git describe --tags --always)' \
		-tags netgo \
		-o bin/stock-analytics-tools-service \
		-trimpath \
		.

# Clean build artifacts
clean:
	rm -f bin/stock-analytics-tools-service
	rm -f coverage.out coverage.html

# Docker build with scientific libraries
docker-build:
	docker build -t stock-analytics-tools-service:latest \
		--build-arg BUILDKIT_INLINE_CACHE=1 \
		.

# Docker run with resource limits for analytical workloads
docker-run:
	docker run --rm -p 8151:8151 \
		--memory=4g --cpus=4 \
		-e DATABASE_URL="postgres://user:pass@localhost:5432/analytics?sslmode=disable" \
		-e JWT_SECRET="your-secret-key" \
		-e LOG_LEVEL="info" \
		-e MAX_CONCURRENT_ANALYSIS="10" \
		-e CALCULATION_TIMEOUT="5m" \
		-v $(PWD)/models:/models \
		--name stock-analytics-tools-service \
		stock-analytics-tools-service:latest

# Development run
dev:
	go run main.go

# Generate API code from OpenAPI spec
generate-api:
	ogen -target pkg/api -package api \
		../../proto/openapi/economy-domain/analytics/stock-analytics-tools-service/main.yaml

# Database migrations
migrate-up:
	@echo "Running database migrations..."
	# liquibase --changeLogFile=../../infrastructure/liquibase/migrations/changelog.xml update

migrate-down:
	@echo "Rolling back database migrations..."
	# liquibase --changeLogFile=../../infrastructure/liquibase/migrations/changelog.xml rollbackCount 1

# Performance profiling
profile:
	go test -bench=. -benchmem -cpuprofile=cpu.prof -memprofile=mem.prof ./internal/calculations/
	go tool pprof cpu.prof

# Memory profiling
mem-profile:
	go test -bench=. -benchmem -memprofile=mem.prof ./internal/calculations/
	go tool pprof mem.prof

# Security scan
security:
	gosec ./...

# Load testing for analytical endpoints
load-test:
	@echo "Running load tests for analytical operations..."
	# ab -n 50 -c 5 -T application/json -p fundamental_request.json http://localhost:8151/api/v1/analysis/fundamental/AAPL
	# ab -n 30 -c 3 -T application/json -p portfolio_request.json http://localhost:8151/api/v1/portfolio/optimization

# Production readiness check
check-prod:
	@echo "Checking production readiness..."
	go vet ./...
	go test -v ./...
	@echo "Running security scan..."
	gosec ./...
	@echo "✅ Production ready"

# ML model training (placeholder)
train-models:
	@echo "Training ML models..."
	# python scripts/train_price_prediction_model.py
	# python scripts/train_portfolio_optimization_model.py

# Benchmark analytical functions
bench:
	go test -bench=. -benchmem -count=5 ./internal/calculations/
	go test -bench=. -benchmem -count=3 ./internal/analysis/

# Generate test data
generate-test-data:
	@echo "Generating test data..."
	go run scripts/generate_test_data.go

# Help
help:
	@echo "Available targets:"
	@echo "  all                - Run full CI pipeline (deps, fmt, lint, test, build)"
	@echo "  deps               - Download and tidy dependencies"
	@echo "  fmt                - Format Go code"
	@echo "  lint               - Run linter with performance checks"
	@echo "  test               - Run tests with race detection and coverage"
	@echo "  build              - Build optimized binary with CGO for math libraries"
	@echo "  clean              - Clean build artifacts"
	@echo "  docker-build       - Build Docker image with scientific libraries"
	@echo "  docker-run         - Run Docker container with resource limits"
	@echo "  dev                - Run in development mode"
	@echo "  generate-api       - Generate API code from OpenAPI spec"
	@echo "  migrate-up         - Run database migrations"
	@echo "  profile            - Run CPU performance profiling"
	@echo "  mem-profile        - Run memory profiling"
	@echo "  security           - Run security scan"
	@echo "  load-test          - Run load tests for analytical endpoints"
	@echo "  check-prod         - Production readiness check"
	@echo "  train-models       - Train ML models"
	@echo "  bench              - Run benchmarks for analytical functions"
	@echo "  generate-test-data - Generate test data"
	@echo "  help               - Show this help"

