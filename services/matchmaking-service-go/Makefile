# Issue: #1579 - ogen migration + performance optimizations
.PHONY: generate-api clean verify-api check-deps check-file-sizes

SERVICE_NAME := matchmaking-service
OGEN := ogen
REDOCLY_CLI := npx -y @redocly/cli

SPEC_DIR := ../../proto/openapi
API_DIR := pkg/api
SERVICE_SPEC := $(SPEC_DIR)/$(SERVICE_NAME).yaml
BUNDLED_SPEC := openapi-bundled.yaml

check-deps:
	@command -v $(OGEN) >/dev/null 2>&1 || { echo "вќЊ ogen not found. Install: go install -v github.com/ogen-go/ogen/cmd/ogen@latest"; exit 1; }
	@command -v node >/dev/null 2>&1 || { echo "вќЊ node not found. Install Node.js"; exit 1; }

verify-api: check-deps
	@echo "рџ”Ќ Validating OpenAPI spec..."
	@$(REDOCLY_CLI) lint $(SERVICE_SPEC) || exit 1
	@echo "вњ… OpenAPI spec is valid!"

generate-api: check-deps verify-api
	@echo "рџ“¦ Bundling OpenAPI spec..."
	@$(REDOCLY_CLI) bundle $(SERVICE_SPEC) -o $(BUNDLED_SPEC) || exit 1
	@echo "рџ”Ё Generating ogen code..."
	@$(OGEN) --target $(API_DIR) --package api --clean $(BUNDLED_SPEC) || exit 1
	@$(MAKE) check-file-sizes
	@echo "вњ… ogen code generated successfully!"

check-file-sizes:
	@echo "рџ”Ќ Checking generated file sizes..."
	@find $(API_DIR) -name "*.go" -type f 2>/dev/null | while read file; do \
		lines=$$(wc -l < "$$file" 2>/dev/null || powershell -Command "(Get-Content '$$file').Count"); \
		if [ $$lines -gt 500 ]; then \
			echo "вљ пёЏ  $$file: $$lines lines (exceeds 500 limit!)"; \
		else \
			echo "вњ… $$file: $$lines lines (OK)"; \
		fi; \
	done || true

clean:
	@echo "рџ§№ Cleaning generated files..."
	@rm -rf $(API_DIR)/* $(BUNDLED_SPEC)
	@echo "вњ… Clean complete!"


.PHONY: bench bench-json bench-quick

# Run benchmarks (human-readable)
bench:
	go test -run=^ -bench=. -benchmem ./server

# Run benchmarks (JSON output for CI)
bench-json:
	@mkdir -p ../../.benchmarks/results
	go test -run=^ -bench=. -benchmem -json ./server > ../../.benchmarks/results/matchmaking-service_bench.json

# Quick benchmark (short duration)
bench-quick:
	go test -run=^ -bench=. -benchmem -benchtime=100ms ./server