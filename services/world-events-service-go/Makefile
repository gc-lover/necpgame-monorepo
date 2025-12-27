# World Events Service Makefile
# Issue: #2224

.PHONY: all build test clean docker-build docker-run generate-api check-file-sizes

# Build the service
build:
	go build -o bin/world-events-service ./main.go

# Run tests
test:
	go test ./...

# Clean build artifacts
clean:
	rm -rf bin/
	go clean

# Run the service
run:
	go run ./main.go

# Build Docker image
docker-build:
	docker build -t world-events-service-go .

# Run Docker container
docker-run:
	docker run -p 8080:8080 world-events-service-go

# Generate API code from OpenAPI spec
generate-api:
	npx @redocly/cli bundle ../../proto/openapi/world-domain/world-events-service-go/main.yaml -o openapi-bundled.yaml
	ogen --target pkg/api --package api --clean openapi-bundled.yaml

# Check file sizes (must be <1000 lines for generated files)
check-file-sizes:
	@echo "Checking file sizes..."
	@find pkg/api -name "*.go" -exec wc -l {} \; | awk '$$1 > 1000 {print "ERROR: " $$2 " has " $$1 " lines (>1000)"; exit 1}'

# Full CI pipeline
ci: generate-api check-file-sizes test build

# Development setup
dev-setup:
	go mod tidy
	go mod download

# Health check
health:
	curl -f http://localhost:8080/health || exit 1
