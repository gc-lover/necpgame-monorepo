# Issue: ogen migration
.PHONY: generate-api bundle-api clean verify-api check-deps install-deps check-file-sizes

SERVICE_NAME := support-tickets-core-service
OGEN := ogen
REDOCLY_CLI := npx -y @redocly/cli

SPEC_DIR := ../../proto/openapi
API_DIR := pkg/api
SERVICE_SPEC := $(SPEC_DIR)/$(SERVICE_NAME).yaml
BUNDLED_SPEC := openapi-bundled.yaml

check-deps:
	@echo "Checking dependencies..."
	@command -v $(OGEN) >/dev/null 2>&1 || { echo "вќЊ ogen not found. Install: go install github.com/ogen-go/ogen/cmd/ogen@latest"; exit 1; }
	@command -v node >/dev/null 2>&1 || { echo "вќЊ node not found"; exit 1; }
	@echo "вњ… Dependencies installed"

install-deps:
	@go install github.com/ogen-go/ogen/cmd/ogen@latest || true

bundle-api: check-deps
	@echo "Bundling: $(SERVICE_SPEC)"
	@if [ ! -f "$(SERVICE_SPEC)" ]; then echo "вќЊ Spec not found"; exit 1; fi
	@mkdir -p $(API_DIR)
	@$(REDOCLY_CLI) bundle $(SERVICE_SPEC) -o $(BUNDLED_SPEC) || exit 1
	@echo "вњ… Bundled"

generate-api: bundle-api
	@echo "Generating ogen code..."
	@$(OGEN) --target $(API_DIR) --package api --clean $(BUNDLED_SPEC) || exit 1
	@echo "вњ… ogen generation complete!"
	@$(MAKE) check-file-sizes

check-file-sizes:
	@echo ""
	@echo "File size check (max 1000 lines per file):"
	@find $(API_DIR) -name "*.go" -type f 2>/dev/null | while read file; do \
		lines=$$(wc -l < "$$file" 2>/dev/null | tr -d ' '); \
		if [ $$lines -gt 1000 ]; then \
			echo "вљ пёЏ  $$file: $$lines lines (EXCEEDS 1000)"; \
		else \
			echo "вњ… $$file: $$lines lines"; \
		fi; \
	done || true

verify-api: check-deps
	@echo "Verifying: $(SERVICE_SPEC)"
	@if [ ! -f "$(SERVICE_SPEC)" ]; then echo "вќЊ Spec not found"; exit 1; fi
	@$(REDOCLY_CLI) lint $(SERVICE_SPEC) || exit 1
	@echo "вњ… OpenAPI spec is valid"

clean:

.PHONY: test test-docker test-docker-up test-docker-down bench-quick build

# Run tests (automatically uses Docker if available, otherwise tries local DB/Redis)
test:
	@if command -v docker-compose >/dev/null 2>&1 && [ -f "docker-compose.test.yml" ]; then \
		echo "🐳 Docker detected, using test containers..."; \
		$(MAKE) test-docker; \
	else \
		echo "WARNING  Docker not available, running tests with local DB/Redis (may skip if unavailable)..."; \
		go test -v -timeout 30s ./...; \
	fi

# Run tests with Docker (automatically starts/stops containers)
test-docker: test-docker-up
	@echo "Waiting for services to be ready..."
	@sleep 3
	@echo "Running tests with Docker services..."
	@TEST_DATABASE_URL="postgresql://necpgame:necpgame@localhost:5433/necpgame?sslmode=disable" \
	 REDIS_ADDR="localhost:6380" \
	 go test -v -timeout 30s ./... || EXIT_CODE=$$?; \
	$(MAKE) test-docker-down; \
	exit $${EXIT_CODE:-0}

# Start Docker services for testing
test-docker-up:
	@echo "Starting test Docker services..."
	@docker-compose -f docker-compose.test.yml up -d
	@echo "Waiting for services to be healthy (max 30s)..."
	@for i in 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15; do \
		docker-compose -f docker-compose.test.yml exec -T postgres-test pg_isready -U necpgame >/dev/null 2>&1 && \
		docker-compose -f docker-compose.test.yml exec -T redis-test redis-cli PING >/dev/null 2>&1 && \
		echo "✓ Test services ready" && exit 0; \
		sleep 2; \
	done; \
	echo "⚠ Services may not be fully ready, but continuing..."

# Stop Docker services for testing
test-docker-down:
	@echo "Stopping test Docker services..."
	@docker-compose -f docker-compose.test.yml down -v
	@echo "✓ Test services stopped"

# Quick benchmark (short duration)
bench-quick:
	@if [ -f "server/handlers_bench_test.go" ] || find . -name "*_bench_test.go" | grep -q .; then \
		go test -run=^\$\$ -bench=. -benchmem -benchtime=100ms ./server; \
	fi

# Build (runs tests and benchmarks first)
build: test bench-quick
	@CGO_ENABLED=0 go build -ldflags="-w -s" -o bin/support-tickets-core-service .
	@rm -rf $(API_DIR) $(BUNDLED_SPEC)
	@echo "вњ… Cleaned generated files"


.PHONY: bench bench-json bench-quick

# Run benchmarks (human-readable)
bench:
	go test -run=^ -bench=. -benchmem ./server

# Run benchmarks (JSON output for CI)
bench-json:
	@mkdir -p ../../.benchmarks/results
	go test -run=^ -bench=. -benchmem -json ./server > ../../.benchmarks/results/support-tickets-core-service_bench.json

# Quick benchmark (short duration)
bench-quick:
	go test -run=^ -bench=. -benchmem -benchtime=100ms ./server