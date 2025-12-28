.PHONY: build run test clean docker-build docker-run deps lint

# Build the application
build:
	go build -o notification-service .

# Run the application
run:
	go run .

# Run tests
test:
	go test -v ./...

# Clean build artifacts
clean:
	rm -f notification-service
	go clean

# Install dependencies
deps:
	go mod download
	go mod tidy

# Run linter
lint:
	golangci-lint run

# Build Docker image
docker-build:
	docker build -t notification-system-service-go .

# Run Docker container
docker-run:
	docker run -p 8086:8086 --env-file .env notification-system-service-go

# Development setup
dev-setup: deps
	go install github.com/golangci/golangci-lint/cmd/golangci-lint@latest

# Format code
fmt:
	go fmt ./...

# Run with race detector
race:
	go run -race .

# Build for production
build-prod:
	CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o notification-service .

# Run integration tests
test-integration:
	go test -v -tags=integration ./...
