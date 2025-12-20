# Issue: #158
.PHONY: help build test clean docker-build docker-run lint vet fmt mod-tidy run dev deps

help: ## Show this help message
	@echo 'Usage: make [target]'
	@echo ''
	@echo 'Targets:'
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "  %-15s %s\n", $$1, $$2}' $(MAKEFILE_LIST)

build: ## Build the application
	go build -o bin/combat-combos-service ./main.go

test: ## Run tests
	go test -v -race -coverprofile=coverage.out ./...

clean: ## Clean build artifacts
	rm -rf bin/
	rm -f coverage.out

docker-build: ## Build Docker image
	docker build -t combat-combos-service-go:latest .

docker-run: ## Run Docker container
	docker run -p 8084:8084 --env-file .env combat-combos-service-go:latest

lint: ## Run linter
	golangci-lint run

vet: ## Run go vet
	go vet ./...

fmt: ## Format code
	go fmt ./...

mod-tidy: ## Tidy modules
	go mod tidy

run: ## Run the application
	go run main.go

dev: ## Run in development mode with hot reload
	air

deps: ## Download dependencies
	go mod download