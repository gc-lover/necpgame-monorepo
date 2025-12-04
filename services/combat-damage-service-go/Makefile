# Issue: #1595
# Makefile for ogen code generation (migrated from oapi-codegen)

.PHONY: generate-api clean check-deps

SERVICE_NAME := combat-damage-service
SPEC_DIR := ../../proto/openapi
BUNDLED_SPEC := openapi-bundled.yaml
API_DIR := pkg/api

REDOCLY := npx --yes @redocly/cli
OGEN := ogen

check-deps:
	@echo "Checking dependencies..."
	@command -v node >/dev/null 2>&1 || { echo "вќЊ node not found"; exit 1; }
	@command -v ogen >/dev/null 2>&1 || { echo "вќЊ ogen not found"; exit 1; }
	@echo "вњ… Dependencies installed"

generate-api: check-deps
	@echo "рџ”§ Generating ogen code for $(SERVICE_NAME)..."
	@$(REDOCLY) bundle $(SPEC_DIR)/$(SERVICE_NAME).yaml -o $(BUNDLED_SPEC)
	@$(OGEN) --target $(API_DIR) --package api --clean $(BUNDLED_SPEC)
	@echo "вњ… Code generation complete!"

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
	@CGO_ENABLED=0 go build -ldflags="-w -s" -o bin/combat-damage-service .
	@rm -f $(BUNDLED_SPEC)
	@rm -rf $(API_DIR)/oas_*_gen.go


.PHONY: bench bench-json bench-quick

# Run benchmarks (human-readable)
bench:
	go test -run=^ -bench=. -benchmem ./server

# Run benchmarks (JSON output for CI)
bench-json:
	@mkdir -p ../../.benchmarks/results
	go test -run=^ -bench=. -benchmem -json ./server > ../../.benchmarks/results/combat-damage-service_bench.json

# Quick benchmark (short duration)
bench-quick:
	go test -run=^ -bench=. -benchmem -benchtime=100ms ./server