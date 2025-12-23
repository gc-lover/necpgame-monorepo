# Issue: #2217
# PERFORMANCE: Optimized build for event sourcing aggregates

.PHONY: all build clean test deps run stop docker-build docker-run check-file-sizes lint fmt vet

all: deps build test

build:
	@echo "Building Event Sourcing Aggregates service with performance optimizations..."
	CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo \
		-ldflags="-w -s -X main.version=1.0.0" \
		-o bin/event-sourcing-aggregates \
		-trimpath \
		-tags=netgo \
		./main.go

clean:
	@echo "Cleaning build artifacts..."
	rm -rf bin/ pkg/

test:
	@echo "Running Event Sourcing tests..."
	go test -v -race -coverprofile=coverage.out ./...

deps:
	@echo "Installing dependencies and resolving modules..."
	go mod download
	go mod tidy

run: build
	@echo "Starting Event Sourcing Aggregates service..."
	./bin/event-sourcing-aggregates

stop:
	@echo "Stopping Event Sourcing Aggregates service..."
	@pkill -f event-sourcing-aggregates || true

docker-build:
	@echo "Building Docker image for Event Sourcing service..."
	docker build -t event-sourcing-aggregates:latest .

docker-run:
	@echo "Running Event Sourcing service in Docker..."
	docker run --rm -p 8082:8082 event-sourcing-aggregates:latest

check-file-sizes:
	@echo "Checking binary file sizes..."
	@if [ -f bin/event-sourcing-aggregates ]; then \
		size=$$(stat -c%s bin/event-sourcing-aggregates); \
		echo "Binary size: $$size bytes ($$(($$size/1024)) KB)"; \
		if [ $$size -gt 10485760 ]; then \
			echo "WARNING: Binary size >10MB!"; \
			exit 1; \
		fi; \
	else \
		echo "Binary not found, run 'make build' first"; \
	fi

lint:
	@echo "Running linter..."
	golangci-lint run

fmt:
	@echo "Formatting code..."
	go fmt ./...

vet:
	@echo "Running go vet..."
	go vet ./...

# Development targets
dev-setup:
	@echo "Setting up development environment..."
	@echo "Make sure PostgreSQL is running on localhost:5432"
	@echo "Create database: CREATE DATABASE necpgame_event_sourcing;"

dev-run: build
	@echo "Running in development mode..."
	DATABASE_URL="postgres://user:password@localhost:5432/necpgame_event_sourcing?sslmode=disable" \
	./bin/event-sourcing-aggregates

# Database targets
db-migrate:
	@echo "Running database migrations..."
	# Add migration commands here

db-seed:
	@echo "Seeding database with test data..."
	# Add seeding commands here

# Event store targets
event-store-init:
	@echo "Initializing event store..."
	# Commands to initialize event store

event-store-migrate:
	@echo "Migrating event store schema..."
	# Schema migration commands

# Performance testing
perf-test:
	@echo "Running performance tests..."
	go test -bench=. -benchmem ./...

load-test:
	@echo "Running load tests..."
	# Load testing commands

# Monitoring
health:
	@echo "Checking service health..."
	curl http://localhost:8082/health || echo "Health server not running"

metrics:
	@echo "Service metrics endpoint..."
	curl http://localhost:8082/metrics || echo "Metrics not available"
