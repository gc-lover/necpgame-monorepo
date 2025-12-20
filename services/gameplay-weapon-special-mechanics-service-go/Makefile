# Issue: #1595 - ogen migration
.PHONY: generate-api bundle-api clean verify-api check-deps install-deps check-file-sizes

SERVICE_NAME := gameplay-weapon-special-mechanics-core-service
OGEN := ogen
REDOCLY_CLI := npx -y @redocly/cli

SPEC_DIR := ../../proto/openapi
API_DIR := pkg/api
SERVICE_SPEC := $(SPEC_DIR)/$(SERVICE_NAME).yaml
BUNDLED_SPEC := openapi-bundled.yaml

check-deps:
	@echo "Checking dependencies..."
	@command -v $(OGEN) >/dev/null 2>&1 || { echo "вќЊ ogen not found. Installing..."; $(MAKE) install-deps; }
	@command -v node >/dev/null 2>&1 || { echo "вќЊ node not found"; exit 1; }
	@echo "вњ… Dependencies installed"

install-deps:
	@echo "Installing ogen..."
	@go install github.com/ogen-go/ogen/cmd/ogen@latest
	@echo "вњ… ogen installed"

bundle-api: check-deps
	@echo "Bundling: $(SERVICE_SPEC)"
	@if [ ! -f "$(SERVICE_SPEC)" ]; then echo "вќЊ Spec not found: $(SERVICE_SPEC)"; exit 1; fi
	@$(REDOCLY_CLI) bundle $(SERVICE_SPEC) -o $(BUNDLED_SPEC) || exit 1
	@echo "вњ… Bundled to $(BUNDLED_SPEC)"

generate-api: bundle-api
	@echo "Generating ogen code..."
	@mkdir -p $(API_DIR)
	@$(OGEN) --target $(API_DIR) --package api --clean $(BUNDLED_SPEC) || exit 1
	@echo "вњ… ogen generation complete!"
	@$(MAKE) check-file-sizes

check-file-sizes:
	@echo ""
	@echo "File size check (max 1000 lines per file):"
	@find $(API_DIR) -name "*.go" -type f 2>/dev/null | while read file; do \
		lines=$$(wc -l < "$$file" 2>/dev/null | tr -d ' '); \
		if [ $$lines -gt 1000 ]; then \
			echo "вљ пёЏ  $$file: $$lines lines (EXCEEDS)"; \
		else \
			echo "вњ… $$file: $$lines lines"; \
		fi; \
	done || true
	@echo ""

verify-api: check-deps
	@echo "Verifying: $(SERVICE_SPEC)"
	@if [ ! -f "$(SERVICE_SPEC)" ]; then echo "вќЊ Spec not found"; exit 1; fi
	@$(REDOCLY_CLI) lint $(SERVICE_SPEC) || exit 1
	@echo "вњ… Valid OpenAPI spec"

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
	@CGO_ENABLED=0 go build -ldflags="-w -s" -o bin/gameplay-weapon-special-mechanics-core-service .
	@rm -rf $(API_DIR)/oas_*_gen.go
	@rm -f $(BUNDLED_SPEC)
	@echo "вњ… Cleaned generated files"


.PHONY: bench bench-json bench-quick

# Run benchmarks (human-readable)
bench:
	go test -run=^ -bench=. -benchmem ./server

# Run benchmarks (JSON output for CI)
bench-json:
	@mkdir -p ../../.benchmarks/results
	go test -run=^ -bench=. -benchmem -json ./server > ../../.benchmarks/results/gameplay-weapon-special-mechanics-core-service_bench.json

# Quick benchmark (short duration)
bench-quick:
	go test -run=^ -bench=. -benchmem -benchtime=100ms ./server