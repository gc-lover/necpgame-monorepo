# Combat Damage Service Go Makefile

.PHONY: all build test clean generate deps docker run lint format

# Variables
SERVICE_NAME=combat-damage-service-go
PORT=8083
GO_VERSION=1.21

# Default target
all: deps generate build test

# Dependencies
deps:
	go mod download
	go mod tidy

# Generate API code from OpenAPI spec
generate:
	@echo "Generating OpenAPI code..."
	@if [ ! -f openapi-bundled.yaml ]; then \
		echo "Creating bundled OpenAPI spec..."; \
		npx --yes @redocly/cli bundle ../../../proto/openapi/specialized-domain/combat/combat-damage-service-go/main.yaml -o openapi-bundled.yaml; \
	fi
	@echo "Running ogen code generation..."
	@mkdir -p pkg/api
	@ogen --target pkg/api --package api --clean openapi-bundled.yaml

# Build the service
build:
	@echo "Building $(SERVICE_NAME)..."
	go build -o bin/$(SERVICE_NAME) -ldflags="-s -w" .

# Run tests
test:
	@echo "Running tests..."
	go test ./... -v -race -coverprofile=coverage.out

# Clean build artifacts
clean:
	@echo "Cleaning..."
	rm -rf bin/
	rm -f coverage.out
	rm -f openapi-bundled.yaml

# Run the service locally
run: build
	@echo "Starting $(SERVICE_NAME) on port $(PORT)..."
	./bin/$(SERVICE_NAME)

# Development run with auto-restart
dev:
	@echo "Starting in development mode..."
	air

# Docker build
docker:
	@echo "Building Docker image..."
	docker build -t $(SERVICE_NAME):latest .

# Docker run
docker-run: docker
	@echo "Running in Docker..."
	docker run -p $(PORT):$(PORT) --env-file .env $(SERVICE_NAME):latest

# Lint code
lint:
	@echo "Running golangci-lint..."
	golangci-lint run

# Format code
format:
	@echo "Formatting code..."
	go fmt ./...
	goimports -w .

# Check file sizes (should be <1000 lines)
check-file-sizes:
	@echo "Checking file sizes..."
	@find . -name "*.go" -type f -exec wc -l {} \; | awk '$$1 > 1000 {print "ERROR: " $$2 " has " $$1 " lines (>1000)"; exit 1}'

# Health check
health:
	@curl -s http://localhost:$(PORT)/health | jq .

# Load test
load-test:
	@echo "Running load test..."
	hey -n 1000 -c 10 http://localhost:$(PORT)/health

# Profile CPU
profile-cpu:
	@echo "CPU profiling..."
	go tool pprof http://localhost:$(PORT)/debug/pprof/profile

# Database migration
migrate-up:
	@echo "Running database migrations..."
	migrate -path ../../../infrastructure/liquibase/migrations -database "$(DATABASE_URL)" up

migrate-down:
	@echo "Rolling back database migrations..."
	migrate -path ../../../infrastructure/liquibase/migrations -database "$(DATABASE_URL)" down 1

# Development setup
setup-dev: deps
	@echo "Setting up development environment..."
	go install github.com/cosmtrek/air@latest
	go install github.com/golangci/golangci-lint/cmd/golangci-lint@latest
	go install github.com/rakyll/hey@latest

# CI pipeline
ci: deps generate lint test build check-file-sizes

# Issue: #2251
