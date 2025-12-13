// Issue: #140875766
.PHONY: all clean deps generate build test run docker-build docker-run

SERVICE_NAME=sandevistan-service
OPENAPI_SPEC=../../proto/openapi/$(SERVICE_NAME).yaml

# Colors for output
RED=\033[0;31m
GREEN=\033[0;32m
YELLOW=\033[1;33m
NC=\033[0m # No Color

all: deps generate build test

deps:
	@echo "$(GREEN)Installing dependencies...$(NC)"
	go mod tidy
	go mod download

generate:
	@echo "$(GREEN)Generating API code with ogen...$(NC)"
	# Bundle OpenAPI spec first
	npx --yes @redocly/cli bundle $(OPENAPI_SPEC) -o openapi-bundled.yaml
	# Generate Go code
	ogen --target pkg/api --package api --clean openapi-bundled.yaml
	@echo "$(GREEN)API code generated successfully$(NC)"

build:
	@echo "$(GREEN)Building service...$(NC)"
	go build -o bin/$(SERVICE_NAME) .
	@echo "$(GREEN)Build completed$(NC)"

test:
	@echo "$(GREEN)Running tests...$(NC)"
	go test -v -race -coverprofile=coverage.out ./...
	go tool cover -html=coverage.out -o coverage.html
	@echo "$(GREEN)Tests completed$(NC)"

run: build
	@echo "$(GREEN)Starting service...$(NC)"
	./bin/$(SERVICE_NAME)

clean:
	@echo "$(YELLOW)Cleaning up...$(NC)"
	rm -rf bin/
	rm -rf pkg/api/
	rm -f openapi-bundled.yaml
	rm -f coverage.out coverage.html
	go clean

docker-build:
	@echo "$(GREEN)Building Docker image...$(NC)"
	docker build -t $(SERVICE_NAME):latest .

docker-run: docker-build
	@echo "$(GREEN)Running in Docker...$(NC)"
	docker run -p 8084:8084 $(SERVICE_NAME):latest

# Development helpers
dev: generate build
	@echo "$(GREEN)Development build ready$(NC)"

lint:
	@echo "$(GREEN)Running linters...$(NC)"
	golangci-lint run

fmt:
	@echo "$(GREEN)Formatting code...$(NC)"
	go fmt ./...

vet:
	@echo "$(GREEN)Running go vet...$(NC)"
	go vet ./...

check: fmt vet lint test
	@echo "$(GREEN)All checks passed$(NC)"