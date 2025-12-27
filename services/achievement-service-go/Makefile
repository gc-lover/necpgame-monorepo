# Achievement Service Go - Enterprise-Grade Build Automation
# Issue: #2253

.PHONY: all build test clean docker-build docker-run generate-api help

# Default target
all: clean build test

# Build the service with optimizations
build:
	@echo "🏗️  Building achievement-service-go with enterprise optimizations..."
	CGO_ENABLED=0 GOOS=linux go build \
		-ldflags="-s -w -X main.version=$$(git rev-parse --short HEAD)" \
		-o bin/achievement-service \
		./main.go
	@echo "✅ Build complete: bin/achievement-service"

# Run tests with coverage
test:
	@echo "🧪 Running achievement service tests..."
	go test -v -race -coverprofile=coverage.out ./...
	go tool cover -html=coverage.out -o coverage.html
	@echo "📊 Coverage report: coverage.html"

# Clean build artifacts
clean:
	@echo "🧹 Cleaning build artifacts..."
	rm -rf bin/ coverage.out coverage.html
	go clean -cache -testcache

# Generate API code from OpenAPI spec (ogen)
generate-api:
	@echo "🔧 Generating API code with ogen optimizations..."
	ogen --target pkg/api --package api --clean openapi-bundled.yaml

# Run the service locally
run:
	@echo "🚀 Starting achievement-service-go locally..."
	go run main.go

# Docker operations
docker-build:
	@echo "🐳 Building achievement service Docker image..."
	docker build -t achievement-service-go:latest .

docker-run:
	@echo "🐳 Running achievement service in Docker..."
	docker run -p 8081:8081 \
		-e DATABASE_URL="postgres://user:password@localhost:5432/achievements" \
		-e JWT_SECRET="your-secret-key" \
		achievement-service-go:latest

# Development helpers
fmt:
	@echo "📝 Formatting Go code..."
	go fmt ./...

vet:
	@echo "🔍 Running go vet..."
	go vet ./...

lint:
	@echo "🧹 Running golint..."
	golint ./...

# Performance profiling
profile:
	@echo "📈 Starting performance profiling..."
	go tool pprof -http=:8082 bin/achievement-service

# Database operations
db-migrate:
	@echo "🗄️  Running database migrations..."
	@echo "TODO: Implement database migration logic"

# Quality gates
quality: fmt vet lint test
	@echo "✅ All quality gates passed!"

# Help
help:
	@echo "Achievement Service Go - Enterprise-Grade Build System"
	@echo ""
	@echo "Available targets:"
	@echo "  all           - Clean, build, and test (default)"
	@echo "  build         - Build the service with optimizations"
	@echo "  test          - Run tests with coverage"
	@echo "  clean         - Clean build artifacts"
	@echo "  generate-api  - Generate API code from OpenAPI spec"
	@echo "  run           - Run the service locally"
	@echo "  docker-build  - Build Docker image"
	@echo "  docker-run    - Run service in Docker"
	@echo "  fmt           - Format Go code"
	@echo "  vet           - Run go vet"
	@echo "  lint          - Run golint"
	@echo "  profile       - Start performance profiling"
	@echo "  quality       - Run all quality checks"
	@echo "  help          - Show this help message"

# Issue: #2253