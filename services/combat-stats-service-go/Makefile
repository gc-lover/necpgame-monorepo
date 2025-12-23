# Makefile for combat-stats-service-go

.PHONY: build run test clean deps generate-api fmt lint docker-build docker-run check-file-sizes

# Build the service
build:
	go build -o bin/combat-stats-service-go .

# Run the service
run:
	go run main.go

# Run tests
test:
	go test ./...

# Clean build artifacts
clean:
	rm -rf bin/

# Install dependencies
deps:
	go mod tidy
	go mod download

# Generate API code from OpenAPI spec
generate-api:
	npx --yes @redocly/cli bundle ../../proto/openapi/specialized-domain/combat-stats-service-go/main.yaml -o openapi-bundled.yaml
	ogen --target pkg/api --package api --clean openapi-bundled.yaml

# Format code
fmt:
	go fmt ./...

# Lint code
lint:
	golangci-lint run

# Docker build
docker-build:
	docker build -t combat-stats-service-go .

# Docker run
docker-run:
	docker run -p 8080:8080 combat-stats-service-go

# Check file sizes (should be <1000 lines)
check-file-sizes:
	@find . -name "*.go" -exec wc -l {} + | awk '$$1 > 1000 {print "ERROR: "$$2" has "$$1" lines (>1000)"; exit 1} END {print "All Go files are within size limits"}'
