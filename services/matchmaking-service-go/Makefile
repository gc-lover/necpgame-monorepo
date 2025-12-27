# Makefile for matchmaking-service-go

.PHONY: build run test clean docker-build docker-run lint fmt vet mod-tidy

# Build the service
build:
	go build -o bin/matchmaking-service .

# Run the service locally
run: build
	./bin/matchmaking-service

# Run tests with coverage
test:
	go test -v -race -coverprofile=coverage.out ./...
	go tool cover -html=coverage.out -o coverage.html

# Clean build artifacts
clean:
	rm -rf bin/ coverage.out coverage.html

# Build Docker image
docker-build:
	docker build -t matchmaking-service-go:latest .

# Run Docker container
docker-run:
	docker run -p 8080:8080 --env-file .env matchmaking-service-go:latest

# Lint code
lint:
	golangci-lint run

# Format code
fmt:
	go fmt ./...

# Vet code
vet:
	go vet ./...

# Tidy modules
mod-tidy:
	go mod tidy

# Run all checks
check: fmt vet lint test

# Generate mocks (if needed)
mocks:
	@echo "No mocks needed for this service"

# Performance profiling
profile:
	go test -bench=. -benchmem -cpuprofile=cpu.prof -memprofile=mem.prof ./...

# Show help
help:
	@echo "Available targets:"
	@echo "  build        - Build the service"
	@echo "  run          - Run the service locally"
	@echo "  test         - Run tests with coverage"
	@echo "  clean        - Clean build artifacts"
	@echo "  docker-build - Build Docker image"
	@echo "  docker-run   - Run Docker container"
	@echo "  lint         - Lint code"
	@echo "  fmt          - Format code"
	@echo "  vet          - Vet code"
	@echo "  mod-tidy     - Tidy modules"
	@echo "  check        - Run all checks"
	@echo "  profile      - Run performance profiling"
