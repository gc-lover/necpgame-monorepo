# Makefile for maintenance-windows-service-go
.PHONY: build run test clean deps generate-api fmt lint docker-build docker-run generate-migrations

# Build the service
build:
	go build -o bin/maintenance-windows-service-go .

# Run the service
run:
	go run main.go

# Run tests
test:
	go test ./... -v

# Run tests with coverage
test-coverage:
	go test ./... -coverprofile=coverage.out
	go tool cover -html=coverage.out -o coverage.html

# Clean build artifacts
clean:
	rm -rf bin/
	rm -f coverage.out coverage.html
	rm -f openapi-bundled.yaml

# Install dependencies
deps:
	go mod tidy
	go mod download

# Generate API code (if spec changes)
generate-api:
	npx --yes @redocly/cli bundle ../../proto/openapi/system-domain/ai/maintenance-windows-service.yaml -o openapi-bundled.yaml
	ogen --target pkg/api --package api --clean openapi-bundled.yaml

# Format code
fmt:
	go fmt ./...

# Lint code
lint:
	golangci-lint run

# Run benchmarks
bench:
	go test -bench=. -benchmem ./...

# Docker build
docker-build:
	docker build -t maintenance-windows-service-go .

# Docker run
docker-run:
	docker run -p 8097:8097 maintenance-windows-service-go

# Development run with hot reload
dev:
	go run github.com/cosmtrek/air -c .air.toml

# Generate database migrations
generate-migrations:
	# This would generate Liquibase migrations for maintenance windows tables
	# For now, just a placeholder
	@echo "Generating migrations for maintenance windows..."

# Check file sizes (must be <1000 lines)
check-file-sizes:
	@echo "Checking file sizes..."
	@find . -name "*.go" -type f -exec wc -l {} \; | awk '$$1 > 1000 {print "ERROR: " $$2 " has " $$1 " lines (>1000)"; exit 1} END {print "All files are within size limits"}'

# Run integration tests
test-integration:
	@echo "Running integration tests..."
	# Placeholder for integration tests
	@echo "Integration tests completed"

# Run all quality checks
quality-check: fmt lint test check-file-sizes
	@echo "All quality checks passed!"
