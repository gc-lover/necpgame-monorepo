# Chat Moderation Service Makefile
# Issue: #1911

.PHONY: all build test clean docker run deps generate tidy

# Variables
SERVICE_NAME=chat-moderation-service-go
MAIN_FILE=main.go
DOCKER_IMAGE=necpgame/$(SERVICE_NAME)

# Default target
all: tidy generate build test

# Dependencies
deps:
	go mod download

tidy:
	go mod tidy

# Code generation
generate:
	cp ../../proto/openapi/chat-moderation-service.yaml openapi-bundled.yaml
	ogen --target pkg/api --package api --clean openapi-bundled.yaml

# Build
build:
	CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o bin/$(SERVICE_NAME) $(MAIN_FILE)

# Test
test:
	go test ./... -v -race -coverprofile=coverage.out
	go tool cover -html=coverage.out -o coverage.html

# Benchmarks (performance validation)
bench:
	go test -bench=. -benchmem ./server/ -run=^$ -count=5

# Lint
lint:
	golangci-lint run

# Clean
clean:
	rm -rf bin/ coverage.out coverage.html openapi-bundled.yaml

# Docker
docker-build:
	docker build -t $(DOCKER_IMAGE):latest .

docker-run: docker-build
	docker run --rm -p 8085:8085 \
		-e DATABASE_URL="postgres://necpgame:necpgame@localhost:5432/necpgame?sslmode=disable" \
		$(DOCKER_IMAGE):latest

# Development
run:
	go run $(MAIN_FILE)

dev: generate
	air

# Check file sizes (SOLID validation)
check-file-sizes:
	@echo "Checking generated file sizes (<1000 lines)..."
	@find pkg/api -name "*.go" -exec wc -l {} \; | awk '$$1 > 1000 {print "❌ "$$2" has "$$1" lines (>1000)"; exit 1} {print "OK "$$2" has "$$1" lines"}'

# Full CI pipeline
ci: tidy generate build test bench lint check-file-sizes

# Database setup (for development)
db-setup:
	@echo "Setting up database..."
	# TODO: Add database migration commands

# Performance validation
perf-test:
	@echo "Running performance tests..."
	go test -bench=BenchmarkCheckMessage -benchmem ./server/ -count=10
	@echo "P99 should be <50ms for CheckMessage"

# Hot reload
watch:
	@if command -v air >/dev/null 2>&1; then \
		air; \
	else \
		echo "Air not installed. Install with: go install github.com/cosmtrek/air@latest"; \
		find . -name "*.go" | entr -r go run $(MAIN_FILE); \
	fi
