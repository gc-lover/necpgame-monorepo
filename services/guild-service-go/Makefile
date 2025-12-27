# Guild Service Makefile
# Issue: #2247

.PHONY: all build test clean docker-build docker-run generate-api check-file-sizes

# Build the service
build:
	go build -o bin/guild-service ./main.go

# Run tests
test:
	go test ./...

# Clean build artifacts
clean:
	rm -rf bin/
	go clean

# Run the service
run:
	go run ./main.go

# Build Docker image
docker-build:
	docker build -t guild-service-go .

# Run Docker container
docker-run:
	docker run -p 8080:8080 guild-service-go

# Generate API code from OpenAPI spec
generate-api:
	npx @redocly/cli bundle ../../proto/openapi/social-domain/guild-service-go/main.yaml -o openapi-bundled.yaml
	ogen --target pkg/api --package api --clean openapi-bundled.yaml

# Check file sizes (must be <1000 lines for generated files)
check-file-sizes:
	@echo "Checking file sizes..."
	@find pkg/api -name "*.go" -exec wc -l {} \;

# Run with hot reload
dev:
	air

# Format code
fmt:
	go fmt ./...

# Lint code
lint:
	golangci-lint run

# Run benchmarks
bench:
	go test -bench=. -benchmem ./...

# Check coverage
coverage:
	go test -coverprofile=coverage.out ./...
	go tool cover -html=coverage.out -o coverage.html

# Security scan
security:
	gosec ./...

# Performance profiling
profile:
	go test -cpuprofile=cpu.prof -memprofile=mem.prof -bench=. ./...
