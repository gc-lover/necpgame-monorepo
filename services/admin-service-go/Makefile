# Issue: Implement admin-service-go based on OpenAPI specification
.PHONY: all build test clean deps generate-api docker-build docker-run

# Build settings
BINARY_NAME=admin-service-go
DOCKER_IMAGE=necpgame-admin-service-go
SERVICE_PORT=18085

# Go build flags for performance optimization
# -ldflags for stripping debug info and reducing binary size
# -trimpath to remove file system paths from the binary
# CGO_ENABLED=0 for static linking and better performance
GOFLAGS=-ldflags="-s -w -X main.version=$(shell git describe --tags --always --dirty) -trimpath"
GOBUILD=CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build $(GOFLAGS)

all: deps generate-api test build

deps:
	go mod download
	go mod tidy

generate-api:
	@echo "Generating OpenAPI client code..."
	npx --yes @redocly/cli bundle ../../../proto/openapi/specialized-domain/admin-service-go/main.yaml -o openapi-bundled.yaml
	ogen --target pkg/api --package api --clean openapi-bundled.yaml

build:
	@echo "Building $(BINARY_NAME)..."
	$(GOBUILD) -o bin/$(BINARY_NAME) .
	@echo "Build complete. Binary size:"
	@ls -lh bin/$(BINARY_NAME)

test:
	@echo "Running tests..."
	go test -v -race -coverprofile=coverage.out ./...
	go tool cover -html=coverage.out -o coverage.html
	@echo "Test coverage report generated: coverage.html"

clean:
	@echo "Cleaning up..."
	rm -rf bin/
	rm -f openapi-bundled.yaml
	rm -f coverage.out coverage.html

docker-build:
	@echo "Building Docker image..."
	docker build -t $(DOCKER_IMAGE):latest .
	docker tag $(DOCKER_IMAGE):latest $(DOCKER_IMAGE):$(shell git rev-parse --short HEAD)

docker-run:
	@echo "Running Docker container..."
	docker run -p $(SERVICE_PORT):$(SERVICE_PORT) \
		-e PORT=$(SERVICE_PORT) \
		-e DATABASE_URL="postgres://user:password@host.docker.internal:5432/necpgame?sslmode=disable" \
		-e REDIS_URL="redis://host.docker.internal:6379" \
		-e JWT_SECRET="your-admin-jwt-secret" \
		$(DOCKER_IMAGE):latest

# Development helpers
dev:
	@echo "Starting development server..."
	go run . --port=$(SERVICE_PORT)

lint:
	@echo "Running golangci-lint..."
	golangci-lint run

fmt:
	@echo "Formatting code..."
	go fmt ./...

vet:
	@echo "Running go vet..."
	go vet ./...

# Security scanning
security-scan:
	@echo "Running security scan..."
	gosec ./...

# Performance profiling
profile:
	@echo "Running performance profiling..."
	go test -bench=. -benchmem ./...
	go test -cpuprofile=cpu.prof -memprofile=mem.prof ./...
	@echo "Profiles generated: cpu.prof, mem.prof"

# Database migration helpers
migrate-up:
	@echo "Running database migrations..."
	# Add migration commands here when database schema is ready

migrate-down:
	@echo "Rolling back database migrations..."
	# Add migration rollback commands here

# Deployment helpers
deploy-staging:
	@echo "Deploying to staging..."
	# Add staging deployment commands

deploy-prod:
	@echo "Deploying to production..."
	# Add production deployment commands

# Monitoring
health-check:
	@echo "Running health check..."
	curl -f http://localhost:$(SERVICE_PORT)/health || echo "Health check failed"

# Cleanup
deep-clean: clean
	@echo "Deep cleaning..."
	rm -rf vendor/
	go clean -cache
	go clean -modcache
