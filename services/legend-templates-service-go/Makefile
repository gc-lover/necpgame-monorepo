# Issue: #2241
# Legend Templates Service Makefile

.PHONY: build run test clean docker-build docker-run generate-api lint

# Build the service
build:
	go build -o bin/legend-templates-service ./main.go

# Run the service
run:
	go run ./main.go

# Run tests
test:
	go test -v -race -cover ./...

# Clean build artifacts
clean:
	rm -rf bin/
	go clean

# Run linter
lint:
	golangci-lint run

# Generate API code from OpenAPI spec
generate-api:
	# BACKEND NOTE: Regenerate ogen code when OpenAPI spec changes
	npx --yes @redocly/cli bundle ../../../proto/openapi/social-domain/legend-templates/legend-templates-service-go/main.yaml -o ../../../legend-templates-bundled.yaml
	ogen --target pkg/api --package api --clean ../../../legend-templates-bundled.yaml

# Docker build
docker-build:
	docker build -t necpgame/legend-templates-service:latest .

# Docker run
docker-run:
	docker run -p 8086:8086 --env-file .env necpgame/legend-templates-service:latest

# Development with hot reload
dev:
	air

# Format code
fmt:
	go fmt ./...

# Vet code
vet:
	go vet ./...

# Run benchmarks
bench:
	go test -bench=. -benchmem ./...

# Check memory leaks
memcheck:
	go test -race -run=TestMemoryLeak ./...

# Generate mocks for testing
mocks:
	mockgen -source=server/service.go -destination=mocks/service_mock.go -package=mocks
	mockgen -source=server/repository.go -destination=mocks/repository_mock.go -package=mocks

# Database migration (requires liquibase)
db-migrate:
	liquibase --changeLogFile=../../../infrastructure/liquibase/changelog.xml --url=jdbc:postgresql://localhost:5432/legend_templates_db --username=legend_user --password=legend_pass update

# Health check
health:
	curl -f http://localhost:8086/health || exit 1

# Load test with hey
load-test:
	hey -n 1000 -c 10 http://localhost:8086/health

# Security scan
security-scan:
	gosec ./...

# Dependency check
deps-check:
	go mod tidy
	go mod verify

# All checks (CI pipeline)
ci: fmt vet lint test security-scan deps-check

# Performance profiling
profile:
	go test -cpuprofile=cpu.prof -memprofile=mem.prof -bench=. ./...
	go tool pprof cpu.prof
	go tool pprof mem.prof

# Generate coverage report
coverage:
	go test -coverprofile=coverage.out ./...
	go tool cover -html=coverage.out -o coverage.html

# Install development dependencies
install-dev-deps:
	go install github.com/cosmtrek/air@latest
	go install github.com/golangci/golangci-lint/cmd/golangci-lint@latest
	go install github.com/securecodewarrior/gosec/v2/cmd/gosec@latest
	go install github.com/golang/mock/mockgen@latest

# Show help
help:
	@echo "Available commands:"
	@echo "  build         - Build the service"
	@echo "  run           - Run the service"
	@echo "  test          - Run tests"
	@echo "  clean         - Clean build artifacts"
	@echo "  lint          - Run linter"
	@echo "  generate-api  - Regenerate API code from OpenAPI spec"
	@echo "  docker-build  - Build Docker image"
	@echo "  docker-run    - Run Docker container"
	@echo "  dev           - Run with hot reload (requires air)"
	@echo "  fmt           - Format code"
	@echo "  vet           - Vet code"
	@echo "  bench         - Run benchmarks"
	@echo "  memcheck      - Check for memory leaks"
	@echo "  mocks         - Generate mocks for testing"
	@echo "  db-migrate    - Run database migrations"
	@echo "  health        - Check service health"
	@echo "  load-test     - Run load test"
	@echo "  security-scan - Run security scan"
	@echo "  deps-check    - Check dependencies"
	@echo "  ci            - Run all CI checks"
	@echo "  profile       - Generate performance profiles"
	@echo "  coverage      - Generate coverage report"
	@echo "  install-dev-deps - Install development dependencies"
	@echo "  help          - Show this help"
