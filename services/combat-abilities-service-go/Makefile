# Combat Abilities Service Makefile

.PHONY: build run test clean docker-build docker-run lint

# Build the service
build:
	go build -o bin/combat-abilities-service-go .

# Run the service
run:
	go run .

# Run tests
test:
	go test -v ./...

# Clean build artifacts
clean:
	rm -rf bin/

# Build Docker image
docker-build:
	docker build -t combat-abilities-service-go .

# Run Docker container
docker-run:
	docker run -p 8084:8084 combat-abilities-service-go

# Run linter
lint:
	golangci-lint run

# Format code
fmt:
	go fmt ./...

# Download dependencies
deps:
	go mod download
	go mod tidy

# Generate mocks (if needed)
mocks:
	@echo "Generating mocks..."
	# Add mock generation commands here if needed

# Run with hot reload (requires air)
dev:
	air

# Default target
all: clean deps fmt lint test build
