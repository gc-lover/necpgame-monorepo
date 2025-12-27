.PHONY: build run test clean docker-build docker-run lint fmt vet mod-tidy help

# Variables
SERVICE_NAME=movement-service-go
DOCKER_IMAGE=necpgame/$(SERVICE_NAME)
DOCKER_TAG?=latest
PORT?=8085

# Build the service
build:
	go build -o bin/$(SERVICE_NAME) .

# Run the service locally
run: build
	./bin/$(SERVICE_NAME)

# Run tests
test:
	go test -v ./...

# Clean build artifacts
clean:
	rm -rf bin/
	go clean

# Build Docker image
docker-build:
	docker build -t $(DOCKER_IMAGE):$(DOCKER_TAG) .

# Run Docker container
docker-run:
	docker run -p $(PORT):$(PORT) \
		-e DATABASE_URL=$$DATABASE_URL \
		-e JWT_SECRET=$$JWT_SECRET \
		-e LOG_LEVEL=info \
		$(DOCKER_IMAGE):$(DOCKER_TAG)

# Run linter
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

# Install dependencies
deps:
	go mod download

# Run all checks
check: fmt vet lint test

# Help
help:
	@echo "Available commands:"
	@echo "  build       - Build the service"
	@echo "  run         - Run the service locally"
	@echo "  test        - Run tests"
	@echo "  clean       - Clean build artifacts"
	@echo "  docker-build - Build Docker image"
	@echo "  docker-run   - Run Docker container"
	@echo "  lint        - Run linter"
	@echo "  fmt         - Format code"
	@echo "  vet         - Vet code"
	@echo "  mod-tidy    - Tidy modules"
	@echo "  deps        - Install dependencies"
	@echo "  check       - Run all checks (fmt, vet, lint, test)"
	@echo "  help        - Show this help"
