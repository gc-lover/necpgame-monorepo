# Makefile for jackie-welles-service-go
.PHONY: build run test clean deps generate-api fmt lint docker-build docker-run

# Build the service
build:
	go build -o bin/jackie-welles-service-go .

# Run the service
run:
	go run main.go

# Run tests
test:
	go test ./...

# Clean build artifacts
clean:
	rm -rf bin/

# Install dependencies
deps:
	go mod tidy
	go mod download

# Generate API code (if spec changes)
generate-api:
	npx --yes @redocly/cli bundle ../../proto/openapi/specialized-domain/npc/jackie-wells-service.yaml -o openapi-bundled.yaml
	ogen --target pkg/api --package api --clean openapi-bundled.yaml

# Format code
fmt:
	go fmt ./...

# Lint code
lint:
	golangci-lint run

# Docker build
docker-build:
	docker build -t jackie-welles-service-go .

# Docker run
docker-run:
	docker run -p 8089:8089 jackie-welles-service-go

# Development run with hot reload
dev:
	air

# Run integration tests
test-integration:
	go test -tags=integration ./...

# Run benchmarks
bench:
	go test -bench=. ./...

# Generate mocks for testing
mocks:
	mockery --all --output ./mocks

# Run security scan
security:
	gosec ./...

# Run performance tests
perf:
	go test -bench=. -benchmem ./...

# Generate coverage report
coverage:
	go test -coverprofile=coverage.out ./...
	go tool cover -html=coverage.out -o coverage.html

# Run all quality checks
quality: fmt lint security test coverage
