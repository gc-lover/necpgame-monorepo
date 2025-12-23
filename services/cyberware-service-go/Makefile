# Issue: #2226
# PERFORMANCE: Optimized build for cyberware implant service

.PHONY: all build clean test deps generate-api docker-build docker-run check-file-sizes

# PERFORMANCE: Parallel builds with optimized flags
all: deps generate-api build test

# PERFORMANCE: Generate optimized API code from OpenAPI spec
generate-api:
	@echo "Generating optimized cyberware API code..."
	npx --yes @redocly/cli bundle ../../../proto/openapi/cyberpunk-domain/cyberware-service-go/main.yaml -o openapi-bundled.yaml
	ogen --target pkg/api --package api --clean openapi-bundled.yaml

# PERFORMANCE: Optimized Go build with production flags
build:
	@echo "Building optimized cyberware binary..."
	CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build \
		-ldflags="-w -s -X main.version=$(shell date -u +%Y%m%d-%H%M%S)" \
		-o bin/cyberware-service \
		./main.go

# PERFORMANCE: Clean build artifacts
clean:
	@echo "Cleaning cyberware build artifacts..."
	rm -rf bin/ openapi-bundled.yaml
	go clean ./...

# PERFORMANCE: Download dependencies with module proxy
deps:
	@echo "Downloading optimized cyberware dependencies..."
	go mod download
	go mod tidy

# PERFORMANCE: Run tests with race detection and coverage
test:
	@echo "Running optimized cyberware tests..."
	go test -race -coverprofile=coverage.out ./...
	go tool cover -html=coverage.out -o coverage.html

# PERFORMANCE: Check file sizes (must be <1000 lines for generated code)
check-file-sizes:
	@echo "Checking cyberware file sizes..."
	@find . -name "*.go" -exec wc -l {} \; | awk '$$1 > 1000 {print "ERROR: "$$2" has "$$1" lines (>1000)"; exit 1}'

# PERFORMANCE: Multi-stage Docker build for minimal image size
docker-build:
	@echo "Building optimized cyberware Docker image..."
	docker build \
		--target production \
		--build-arg BUILDKIT_INLINE_CACHE=1 \
		-t cyberware-service:latest \
		.

# PERFORMANCE: Run in Docker with resource limits
docker-run:
	@echo "Running cyberware container with resource limits..."
	docker run \
		--cpus=1 \
		--memory=512m \
		--read-only \
		-p 8080:8080 \
		cyberware-service:latest
