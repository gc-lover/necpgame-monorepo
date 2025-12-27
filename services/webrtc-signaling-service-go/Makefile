.PHONY: build run test clean docker-build docker-run lint fmt vet mod-tidy

# Variables
APP_NAME=webrtc-signaling-service
DOCKER_IMAGE=necpgame/$(APP_NAME):latest

# Build the application
build:
	go build -o bin/$(APP_NAME) .

# Run the application
run:
	go run .

# Run tests
test:
	go test -v ./...

# Clean build artifacts
clean:
	go clean
	rm -rf bin/

# Format code
fmt:
	go fmt ./...

# Vet code
vet:
	go vet ./...

# Tidy modules
mod-tidy:
	go mod tidy

# Lint code (requires golangci-lint)
lint:
	golangci-lint run

# Build Docker image
docker-build:
	docker build -t $(DOCKER_IMAGE) .

# Run Docker container
docker-run:
	docker run -p 8080:8080 \
		-e DATABASE_URL="postgres://user:password@localhost:5432/webrtc_signaling?sslmode=disable" \
		-e REDIS_URL="redis://localhost:6379" \
		-e JWT_SECRET="your-secret-key" \
		$(DOCKER_IMAGE)

# Development setup
dev-setup: mod-tidy fmt vet

# Production build
prod-build: mod-tidy fmt vet test build docker-build

# Health check
health-check:
	curl -f http://localhost:8080/health || exit 1

# Load test (requires hey or similar)
load-test:
	hey -n 1000 -c 10 http://localhost:8080/health

# All quality checks
quality: fmt vet lint test
