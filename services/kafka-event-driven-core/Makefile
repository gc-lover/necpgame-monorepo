# Issue: #2237
# PERFORMANCE: Optimized build for production deployment

.PHONY: all build clean test deps generate-api docker-build docker-run check-file-sizes lint fmt vet run stop

all: deps build test

build:
	@echo "Building Kafka event-driven core service with performance optimizations..."
	CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo \
		-ldflags="-w -s -X main.version=1.0.0" \
		-o bin/kafka-event-core \
		-trimpath \
		-tags=netgo \
		./main.go

clean:
	@echo "Cleaning build artifacts..."
	rm -rf bin/ pkg/ openapi-bundled.yaml

test:
	@echo "Running Kafka service tests..."
	go test -v -race -coverprofile=coverage.out ./...

deps:
	@echo "Installing dependencies and resolving modules..."
	go mod download
	go mod tidy

run: build
	@echo "Starting Kafka event-driven core service..."
	./bin/kafka-event-core

stop:
	@echo "Stopping Kafka event-driven core service..."
	@pkill -f kafka-event-core || true

docker-build:
	@echo "Building Docker image for Kafka service..."
	docker build -t kafka-event-driven-core:latest .

docker-run:
	@echo "Running Kafka service in Docker..."
	docker run --rm -p 8080:8080 -p 9090:9090 kafka-event-driven-core:latest

check-file-sizes:
	@echo "Checking binary file sizes..."
	@if [ -f bin/kafka-event-core ]; then \
		size=$$(stat -c%s bin/kafka-event-core); \
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
	@echo "Make sure Kafka is running on localhost:9092"
	@echo "Or set KAFKA_BROKERS environment variable"

dev-run: build
	@echo "Running in development mode..."
	KAFKA_BROKERS=localhost:9092 ./bin/kafka-event-core

# Kafka management targets
kafka-topics:
	@echo "Creating required Kafka topics..."
	@# This would use kafka-admin tools to create topics
	@echo "Topics created via application startup"

kafka-health:
	@echo "Checking Kafka health..."
	@# Health checks would be done via the service endpoints

# Metrics and monitoring
metrics:
	@echo "Opening metrics endpoint..."
	curl http://localhost:9090/metrics || echo "Metrics server not running"

health:
	@echo "Checking service health..."
	curl http://localhost:8081/health || echo "Health server not running"
