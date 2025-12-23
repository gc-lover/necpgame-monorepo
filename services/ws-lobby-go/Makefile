# Issue: #2218 - Backend: Добавить unit-тесты для ws-lobby-go
.PHONY: all build test clean deps generate-api docker-build docker-run

# Build settings
BINARY_NAME=ws-lobby-go
DOCKER_IMAGE=necpgame-ws-lobby-go

# Go build flags for performance
GOFLAGS=-ldflags="-s -w -X main.version=$(shell git describe --tags --always --dirty)"

all: deps generate-api test build

deps:
	go mod download
	go mod tidy

generate-api:
	@echo "Generating OpenAPI client code..."
	npx --yes @redocly/cli bundle ../../../proto/openapi/social-domain/lobby/ws-lobby-go/main.yaml -o openapi-bundled.yaml
	ogen --target pkg/api --package api --clean openapi-bundled.yaml

build:
	@echo "Building $(BINARY_NAME)..."
	CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build $(GOFLAGS) -o bin/$(BINARY_NAME) .

test:
	@echo "Running tests..."
	go test -v -race -coverprofile=coverage.out ./...
	go tool cover -html=coverage.out -o coverage.html

test-unit:
	@echo "Running unit tests only..."
	go test -v -race -short ./server/...

benchmark:
	@echo "Running benchmarks..."
	go test -bench=. -benchmem ./...

lint:
	@echo "Running linter..."
	golangci-lint run

clean:
	@echo "Cleaning up..."
	rm -rf bin/
	rm -f openapi-bundled.yaml
	rm -f coverage.out coverage.html

docker-build:
	@echo "Building Docker image..."
	docker build -t $(DOCKER_IMAGE):latest .

docker-run:
	@echo "Running Docker container..."
	docker run --rm -p 18081:18081 \
		-e REDIS_URL=redis://host.docker.internal:6379 \
		-e DATABASE_URL=postgres://user:password@host.docker.internal:5432/necpgame?sslmode=disable \
		$(DOCKER_IMAGE):latest

run:
	@echo "Running service locally..."
	go run .

check-file-sizes:
	@echo "Checking generated file sizes..."
	@find pkg/api -name "*.go" -exec wc -l {} \; | awk '$$1 > 1000 {print "ERROR: "$$2" has "$$1" lines (max 1000)"; exit 1} END {print "All generated files within size limits"}'

# Development helpers
dev-deps:
	go install github.com/golangci/golangci-lint/cmd/golangci-lint@latest
	go install google.golang.org/protobuf/cmd/protoc-gen-go@latest
	go install github.com/ogen-go/ogen/cmd/ogen@latest
	npm install -g @redocly/cli

watch:
	@echo "Watching for changes..."
	@if command -v air >/dev/null 2>&1; then \
		air; \
	else \
		echo "Install air for hot reloading: go install github.com/cosmtrek/air@latest"; \
		find . -name "*.go" | entr -r go run .; \
	fi
