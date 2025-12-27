# Player Orders World Impact Service Makefile
# Issue: #140894810

.PHONY: build run test clean docker-build docker-run lint

# Build the service
build:
	go build -o bin/player-orders-service main.go

# Run the service
run:
	go run main.go

# Run tests
test:
	go test ./...

# Clean build artifacts
clean:
	rm -rf bin/

# Run linting
lint:
	golangci-lint run

# Build Docker image
docker-build:
	docker build -t necpgame/player-orders-service-go .

# Run Docker container
docker-run:
	docker run -p 8080:8080 necpgame/player-orders-service-go

# Generate mocks (if needed)
mocks:
	go generate ./...

# Install dependencies
deps:
	go mod download
	go mod tidy

# Development setup
dev-setup: deps
	go install github.com/golangci/golangci-lint/cmd/golangci-lint@latest

# Format code
fmt:
	go fmt ./...

# Run with hot reload (requires air or similar)
dev:
	air


