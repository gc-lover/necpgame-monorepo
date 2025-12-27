# Makefile for analytics-dashboard-service-go

.PHONY: build run test clean deps fmt lint docker-build docker-run setup

# Build the service
build:
	go build -o bin/analytics-dashboard-service .

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

# Format code
fmt:
	go fmt ./...

# Lint code
lint:
	golangci-lint run

# Generate API code (if spec changes)
generate-api:
	npx --yes @redocly/cli bundle ../../proto/openapi/analysis-domain/main.yaml -o openapi-bundled.yaml
	ogen --target pkg/api --package api --clean openapi-bundled.yaml

# Docker build
docker-build:
	docker build -t analytics-dashboard-service .

# Docker run
docker-run:
	docker run -p 8091:8091 analytics-dashboard-service

# Docker compose up
docker-up:
	docker-compose up -d

# Docker compose down
docker-down:
	docker-compose down

# Performance benchmark
bench:
	go test -bench=. -benchmem

# Profile CPU
profile-cpu:
	go tool pprof -http=:8081 cpu.prof

# Profile memory
profile-mem:
	go tool pprof -http=:8082 mem.prof

# Health check
health:
	curl -f http://localhost:8091/health

# All-in-one setup
setup: deps build

# Run with race detector
race:
	go run -race main.go
