# Issue: #1601 - ogen migration
.PHONY: generate-api bundle-api clean verify-api check-deps install-deps check-file-sizes

SERVICE_NAME := stock-integration-economy-service
OGEN := ogen
REDOCLY_CLI := npx -y @redocly/cli

SPEC_DIR := ../../proto/openapi
API_DIR := pkg/api
SERVICE_SPEC := $(SPEC_DIR)/$(SERVICE_NAME).yaml
BUNDLED_SPEC := openapi-bundled.yaml

check-deps:
	@echo "Checking dependencies..."
	@command -v $(OGEN) >/dev/null 2>&1 || { echo "❌ ogen not found. Install: go install github.com/ogen-go/ogen/cmd/ogen@latest"; exit 1; }
	@command -v node >/dev/null 2>&1 || { echo "❌ node not found"; exit 1; }
	@echo "OK Dependencies installed"

install-deps:
	@go install github.com/ogen-go/ogen/cmd/ogen@latest || true

bundle-api: check-deps
	@echo "Bundling: $(SERVICE_SPEC)"
	@if [ ! -f "$(SERVICE_SPEC)" ]; then echo "❌ Spec not found"; exit 1; fi
	@mkdir -p $(API_DIR)
	@$(REDOCLY_CLI) bundle $(SERVICE_SPEC) -o $(BUNDLED_SPEC) || exit 1
	@echo "OK Bundled"

generate-api: bundle-api
	@echo "Generating ogen code..."
	@$(OGEN) --target $(API_DIR) --package api --clean $(BUNDLED_SPEC) || exit 1
	@echo "OK ogen generation complete!"
	@$(MAKE) check-file-sizes

check-file-sizes:
	@echo ""
	@echo "File size check (max 500 lines per file):"
	@find $(API_DIR) -name "*.go" -type f 2>/dev/null | while read file; do \
		lines=$$(wc -l < "$$file" 2>/dev/null | tr -d ' '); \
		if [ $$lines -gt 500 ]; then \
			echo "WARNING  $$file: $$lines lines (EXCEEDS 500)"; \
		else \
			echo "OK $$file: $$lines lines"; \
		fi; \
	done || true

verify-api: check-deps
	@echo "Verifying: $(SERVICE_SPEC)"
	@if [ ! -f "$(SERVICE_SPEC)" ]; then echo "❌ Spec not found"; exit 1; fi
	@$(REDOCLY_CLI) lint $(SERVICE_SPEC) || exit 1
	@echo "OK OpenAPI spec is valid"

clean:

.PHONY: test bench-quick build

# Run tests
test:
	@go test -v ./...

# Quick benchmark (short duration)
bench-quick:
	@if [ -f "server/handlers_bench_test.go" ] || find . -name "*_bench_test.go" | grep -q .; then \
		go test -run=^\$\$ -bench=. -benchmem -benchtime=100ms ./server; \
	fi

# Build (runs tests and benchmarks first)
build: test bench-quick
	@CGO_ENABLED=0 go build -ldflags="-w -s" -o bin/stock-integration-service .
	@rm -f $(BUNDLED_SPEC) $(TYPES_FILE) $(SERVER_FILE) $(SPEC_FILE)


.PHONY: bench bench-json bench-quick

# Run benchmarks (human-readable)
bench:
	go test -run=^ -bench=. -benchmem ./server

# Run benchmarks (JSON output for CI)
bench-json:
	@mkdir -p ../../.benchmarks/results
	go test -run=^ -bench=. -benchmem -json ./server > ../../.benchmarks/results/stock-integration-service_bench.json

# Quick benchmark (short duration)
bench-quick:
	go test -run=^ -bench=. -benchmem -benchtime=100ms ./server