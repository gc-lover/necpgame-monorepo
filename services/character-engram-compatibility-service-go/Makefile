# Character Engram Compatibility Service Go - Build and test automation

.PHONY: all build test clean docker-build docker-run generate lint

# Default target
all: build test

# Build the service
build:
	go build -o bin/character-engram-compatibility-service-go ./main.go

# Run tests with coverage
test:
	go test -v -race -coverprofile=coverage.out ./...
	go tool cover -html=coverage.out -o coverage.html

# Run tests with leak detection
test-leak:
	go test -v -tags leak ./...

# Generate OpenAPI code
generate:
	ogen --target pkg/api --package api --clean openapi-bundled.yaml

# Run linter
lint:
	golangci-lint run ./...

# Clean build artifacts
clean:
	rm -rf bin/
	rm -f coverage.out coverage.html
	rm -f openapi-bundled.yaml

# Create bundled OpenAPI spec
bundle-openapi:
	npx @redocly/cli bundle ../../../proto/openapi/system-domain/ai/security-domain/character/character-engram-compatibility-service.yaml -o openapi-bundled.yaml

# Docker build
docker-build:
	docker build -t character-engram-compatibility-service-go:latest .

# Docker run
docker-run:
	docker run -p 8087:8087 character-engram-compatibility-service-go:latest

# Development run
run:
	go run main.go

# Install dependencies
deps:
	go mod download
	go mod tidy

# Format code
fmt:
	go fmt ./...

# Vet code
vet:
	go vet ./...
