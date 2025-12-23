# Issue: #140889798
# PERFORMANCE: Optimized build for production deployment

.PHONY: all build clean test deps generate-api docker-build docker-run check-file-sizes

all: deps generate-api build test

generate-api:
	@echo "Generating optimized API code for session management service..."
	npx --yes @redocly/cli bundle ../../../proto/openapi/system-domain/session-management-service-go/main.yaml -o openapi-bundled.yaml
	ogen --target pkg/api --package api --clean openapi-bundled.yaml

build:
	@echo "Building session management service with performance optimizations..."
	CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo \
		-ldflags="-w -s -X main.version=1.0.0" \
		-o bin/session-service \
		-trimpath \
		-tags=netgo \
		./main.go

clean:
	@echo "Cleaning build artifacts..."
	rm -rf bin/ pkg/ openapi-bundled.yaml

test:
	@echo "Running session service tests..."
	go test -v -race -coverprofile=coverage.out ./...

deps:
	@echo "Installing dependencies and resolving modules..."
	go mod download
	go mod tidy

docker-build:
	@echo "Building Docker image for session service..."
	docker build -t session-management-service-go:latest .

docker-run:
	@echo "Running session service in Docker..."
	docker run --rm -p 8080:8080 session-management-service-go:latest

check-file-sizes:
	@echo "Checking binary file sizes..."
	@if [ -f bin/session-service ]; then \
		size=$$(stat -c%s bin/session-service); \
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
