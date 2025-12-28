# Stock Analytics Charts Service Makefile
# Optimized for high-performance financial data processing

.PHONY: all build test clean docker-build docker-run lint fmt deps

# Default target
all: deps fmt lint test build

# Dependencies
deps:
	go mod download
	go mod tidy

# Code formatting
fmt:
	go fmt ./...

# Linting
lint:
	golangci-lint run --timeout=5m

# Testing with coverage optimized for financial calculations
test:
	go test -v -race -coverprofile=coverage.out ./...
	go tool cover -html=coverage.out -o coverage.html

# Build optimized binary for financial data processing
build:
	CGO_ENABLED=0 GOOS=linux go build \
		-a -installsuffix cgo \
		-ldflags '-extldflags "-static" -s -w' \
		-tags netgo \
		-o bin/stock-analytics-charts-service \
		-trimpath \
		.

# Clean build artifacts
clean:
	rm -f bin/stock-analytics-charts-service
	rm -f coverage.out coverage.html

# Docker build with multi-stage optimization
docker-build:
	docker build -t stock-analytics-charts-service:latest .

# Docker run with performance tuning
docker-run:
	docker run --rm -p 8150:8150 \
		-e DATABASE_URL="postgres://user:pass@localhost:5432/stockdb?sslmode=disable" \
		-e JWT_SECRET="your-secret-key" \
		-e LOG_LEVEL="info" \
		--name stock-analytics-charts-service \
		stock-analytics-charts-service:latest

# Development run
dev:
	go run main.go

# Generate API code from OpenAPI spec
generate-api:
	ogen -target pkg/api -package api \
		../../proto/openapi/economy-domain/analytics/stock-analytics-charts-service/main.yaml

# Database migrations (placeholder)
migrate-up:
	@echo "Running database migrations..."
	# liquibase --changeLogFile=../../infrastructure/liquibase/migrations/changelog.xml update

migrate-down:
	@echo "Rolling back database migrations..."
	# liquibase --changeLogFile=../../infrastructure/liquibase/migrations/changelog.xml rollbackCount 1

# Performance profiling
profile:
	go test -bench=. -benchmem -cpuprofile=cpu.prof -memprofile=mem.prof ./...
	go tool pprof cpu.prof

# Security scan
security:
	gosec ./...

# Load testing placeholder
load-test:
	@echo "Running load tests for financial data processing..."
	# ab -n 10000 -c 100 http://localhost:8150/health

# Production deployment check
check-prod:
	@echo "Checking production readiness..."
	go vet ./...
	go test -v ./...
	@echo "✅ Production ready"

# Help
help:
	@echo "Available targets:"
	@echo "  all          - Run full CI pipeline (deps, fmt, lint, test, build)"
	@echo "  deps         - Download and tidy dependencies"
	@echo "  fmt          - Format Go code"
	@echo "  lint         - Run linter"
	@echo "  test         - Run tests with coverage"
	@echo "  build        - Build optimized binary"
	@echo "  clean        - Clean build artifacts"
	@echo "  docker-build - Build Docker image"
	@echo "  docker-run   - Run Docker container"
	@echo "  dev          - Run in development mode"
	@echo "  generate-api - Generate API code from OpenAPI spec"
	@echo "  migrate-up   - Run database migrations"
	@echo "  profile      - Run performance profiling"
	@echo "  security     - Run security scan"
	@echo "  check-prod   - Production readiness check"
	@echo "  help         - Show this help"


