# Issue: #2266 - Refactor system-domain AI services
# PERFORMANCE: Optimized build and deployment for enterprise-grade AI companion service

.PHONY: build test clean docker-build docker-run lint bench profile

# PERFORMANCE: Optimized build flags for production
BUILD_FLAGS = -ldflags="-s -w" -trimpath
BINARY_NAME = ai-companion-service

# Build the service with optimizations
build:
	@echo "Building AI Companion service with enterprise-grade optimizations..."
	CGO_ENABLED=0 go build $(BUILD_FLAGS) -o bin/$(BINARY_NAME) ./main.go
	@echo "Build complete: bin/$(BINARY_NAME)"

# Run tests with race detection and coverage
test:
	@echo "Running tests with race detection..."
	go test -race -coverprofile=coverage.out ./...
	go tool cover -html=coverage.out -o coverage.html
	@echo "Tests complete. Coverage report: coverage.html"

# PERFORMANCE: Benchmark critical AI companion operations
bench:
	@echo "Running performance benchmarks..."
	go test -bench=. -benchmem -benchtime=10s ./...
	@echo "Benchmarks complete"

# PERFORMANCE: Profile CPU and memory usage
profile:
	@echo "Starting profiling server on :6060..."
	go tool pprof -http=:6060 http://localhost:8080/debug/pprof/profile

# Clean build artifacts
clean:
	@echo "Cleaning build artifacts..."
	rm -rf bin/ coverage.out coverage.html
	go clean ./...

# PERFORMANCE: Multi-stage Docker build for minimal image size
docker-build:
	@echo "Building optimized Docker image..."
	docker build -t ai-companion-service:latest .
	@echo "Docker image built: ai-companion-service:latest"

# Run in Docker with resource limits
docker-run:
	@echo "Running AI Companion service in Docker..."
	docker run --rm -p 8080:8080 \
		--memory=512m --cpus=1.0 \
		-e GOGC=50 \
		ai-companion-service:latest

# PERFORMANCE: Lint for code quality and optimizations
lint:
	@echo "Running linters and optimization checks..."
	golangci-lint run
	fieldalignment ./...
	go vet ./...
	@echo "Linting complete"

# Development server with hot reload
dev:
	@echo "Starting development server..."
	air

# Production deployment
deploy: build
	@echo "Deploying to production..."
	# Add your deployment commands here

# PERFORMANCE: Load testing
load-test:
	@echo "Running load tests..."
	# Add load testing commands here (e.g., hey, vegeta)

# Generate API documentation
docs:
	@echo "Generating API documentation..."
	# Add documentation generation commands here
