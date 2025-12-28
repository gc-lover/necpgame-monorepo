# Issue: #1495 - Gameplay Affixes Service implementation
# PERFORMANCE: Optimized build and deployment targets

.PHONY: build run test clean docker-build docker-run

# Build the service
build:
	go build -o bin/gameplay-affixes-service ./main.go

# Run the service locally
run: build
	./bin/gameplay-affixes-service

# Run tests
test:
	go test ./...

# Clean build artifacts
clean:
	rm -rf bin/

# Build Docker image
docker-build:
	docker build -t gameplay-affixes-service:latest .

# Run Docker container
docker-run: docker-build
	docker run -p 8083:8083 --env-file .env gameplay-affixes-service:latest

# Generate mocks (if needed)
mocks:
	@echo "No mocks needed for this service"

# Lint code
lint:
	golangci-lint run

# Format code
fmt:
	go fmt ./...
