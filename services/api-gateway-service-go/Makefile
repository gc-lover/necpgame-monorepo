.PHONY: build run test clean docker-build docker-run deps

# Build the application
build:
	go build -o bin/api-gateway-service ./main.go

# Run the application
run:
	go run ./main.go

# Run tests
test:
	go test ./...

# Clean build artifacts
clean:
	rm -rf bin/
	go clean

# Build Docker image
docker-build:
	docker build -t necpgame/api-gateway-service:latest .

# Run Docker container
docker-run:
	docker run -p 8080:8080 --env-file .env necpgame/api-gateway-service:latest

# Install dependencies
deps:
	go mod download
	go mod tidy

# Run linter
lint:
	golangci-lint run

# Format code
fmt:
	go fmt ./...

# Check file sizes (must be <500 lines per file)
check-file-sizes:
	@echo "Checking file sizes..."
	@find . -name "*.go" -type f -exec wc -l {} \; | awk '$$1 > 500 {print $$2 ": " $$1 " lines (exceeds 500)"; exit 1} END {print "All files within size limits"}'

# Build for production
build-prod:
	CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o bin/api-gateway-service-linux ./main.go

# Run benchmarks
bench:
	go test -bench=. -benchmem ./...

# Run with race detector
race:
	go run -race ./main.go

# Default target
all: deps build test