# Issue: #1595
# Makefile for ogen code generation (migrated from oapi-codegen)

.PHONY: generate-api clean check-deps

# Service configuration
SERVICE_NAME := gameplay-combat-actions-service
SPEC_DIR := ../../proto/openapi
BUNDLED_SPEC := openapi-bundled.yaml
API_DIR := pkg/api

# Tools
REDOCLY := npx --yes @redocly/cli
OGEN := ogen

check-deps:
	@echo "Checking dependencies..."
	@command -v node >/dev/null 2>&1 || { echo "вќЊ node not found. Install Node.js"; exit 1; }
	@command -v ogen >/dev/null 2>&1 || { echo "вќЊ ogen not found. Run: go install github.com/ogen-go/ogen/cmd/ogen@latest"; exit 1; }
	@echo "вњ… All dependencies installed"

generate-api: check-deps
	@echo "рџ”§ Generating ogen code for $(SERVICE_NAME)..."
	@echo ""
	@echo "Step 1: Bundling OpenAPI spec..."
	@$(REDOCLY) bundle $(SPEC_DIR)/$(SERVICE_NAME).yaml -o $(BUNDLED_SPEC)
	@echo "вњ… Bundled в†’ $(BUNDLED_SPEC)"
	@echo ""
	@echo "Step 2: Generating code with ogen..."
	@$(OGEN) --target $(API_DIR) --package api --clean $(BUNDLED_SPEC)
	@echo "вњ… Generated в†’ $(API_DIR)/"
	@echo ""
	@echo "Step 3: Checking generated files..."
	@ls -lh $(API_DIR)/oas_*_gen.go | awk '{print "  " $$9 " (" $$5 ")"}'
	@echo ""
	@echo "вњ… Code generation complete!"
	@echo ""
	@echo "Next steps:"
	@echo "  1. Update handlers in server/ to implement ogen interfaces"
	@echo "  2. Run: go build ./..."
	@echo "  3. Run: go test ./..."

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
	@CGO_ENABLED=0 go build -ldflags="-w -s" -o bin/gameplay-combat-actions-service .
	@echo "Cleaning generated files..."
	@rm -f $(BUNDLED_SPEC)
	@rm -rf $(API_DIR)/oas_*_gen.go
	@echo "вњ… Cleaned"


.PHONY: bench bench-json bench-quick

# Run benchmarks (human-readable)
bench:
	go test -run=^ -bench=. -benchmem ./server

# Run benchmarks (JSON output for CI)
bench-json:
	@mkdir -p ../../.benchmarks/results
	go test -run=^ -bench=. -benchmem -json ./server > ../../.benchmarks/results/gameplay-combat-actions-service_bench.json

# Quick benchmark (short duration)
bench-quick:
	go test -run=^ -bench=. -benchmem -benchtime=100ms ./server