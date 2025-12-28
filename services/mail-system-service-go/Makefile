.PHONY: build run test clean docker-build docker-run deps lint

# Build the application
build:
	go build -o mail-service .

# Run the application
run:
	go run .

# Run tests
test:
	go test -v ./...

# Clean build artifacts
clean:
	rm -f mail-service
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
	docker build -t mail-system-service-go .

# Run Docker container
docker-run:
	docker run -p 8084:8084 --env-file .env mail-system-service-go

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
	CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o mail-service .


