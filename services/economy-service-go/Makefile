# Economy Service Go Makefile

.PHONY: all build test clean generate deps docker run lint format

# Variables
SERVICE_NAME=economy-service-go
PORT=8082
GO_VERSION=1.23

# Default target
all: deps generate build test

# Dependencies
deps:
	go mod download
	go mod tidy

# Generate API code from OpenAPI spec
generate:
	@echo "Generating OpenAPI code..."
	@if [ ! -f openapi-bundled.yaml ]; then \
		echo "Creating bundled OpenAPI spec..."; \
		npx --yes @redocly/cli bundle ../../../proto/openapi/economy-service/main.yaml -o openapi-bundled.yaml; \
	fi
	@echo "Running ogen code generation..."
	@mkdir -p pkg/api
	@ogen --target pkg/api --package api --clean openapi-bundled.yaml

# Build the service
build:
	go build -o bin/$(SERVICE_NAME) ./main.go

# Run tests
test:
	go test -v ./...

# Clean build artifacts
clean:
	rm -rf bin/ openapi-bundled.yaml pkg/api/

# Docker targets
docker: build
	docker build -t $(SERVICE_NAME):latest .

docker-run: docker
	docker run -p $(PORT):$(PORT) --env-file .env $(SERVICE_NAME):latest

# Development
run:
	go run ./main.go

# Linting and formatting
lint:
	golangci-lint run

format:
	gofmt -w .




