# Issue: #1928 - Build and deployment for Meta Mechanics Service
.PHONY: build test clean docker-build docker-run

# Build the service
build:
	go build -o bin/meta-mechanics-service ./main.go

# Run tests
test:
	go test ./...

# Run with race detection
test-race:
	go test -race ./...

# Clean build artifacts
clean:
	rm -rf bin/

# Run the service locally
run:
	go run main.go

# Docker build
docker-build:
	docker build -t necpgame/meta-mechanics-service:latest .

# Docker run
docker-run:
	docker run -p 8170:8170 necpgame/meta-mechanics-service:latest

# Generate mocks (if needed)
mocks:
	@echo "No mocks needed for this service"

# Lint code
lint:
	golangci-lint run

# Format code
fmt:
	go fmt ./...

# Vet code
vet:
	go vet ./...

# All checks
check: fmt vet lint test
