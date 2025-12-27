# Makefile for support-service-go

.PHONY: build run test clean docker-build docker-run lint fmt vet mod-tidy

# Build the service
build:
	go build -o bin/support-service .

# Run the service locally
run: build
	./bin/support-service

# Run tests with coverage
test:
	go test -v -race -coverprofile=coverage.out ./...
	go tool cover -html=coverage.out -o coverage.html

# Clean build artifacts
clean:
	rm -rf bin/ coverage.out coverage.html

# Build Docker image
docker-build:
	docker build -t support-service-go:latest .

# Run Docker container
docker-run:
	docker run -p 8087:8087 --env-file .env support-service-go:latest

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

# Run integration tests
test-integration:
	@echo "Integration tests would go here"

# Issue: #288

