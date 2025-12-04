# Issue: #1581 - ogen migration + performance optimizations
.PHONY: generate-api clean verify-api check-deps check-file-sizes

SERVICE_NAME := inventory-service
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
	@echo "вњ… ogen code generated successfully!"

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
	@CGO_ENABLED=0 go build -ldflags="-w -s" -o bin/inventory-service .
	@echo "рџ§№ Cleaning generated files..."
	@rm -rf $(API_DIR)/* $(BUNDLED_SPEC)
	@rm -f default.pgo
	@echo "вњ… Clean complete!"

# Issue: #1610 - PGO (Profile-Guided Optimization)
# Generate PGO profile from tests
pgo-profile:
	@echo "рџ“Љ Generating PGO profile..."
	@go test -cpuprofile=default.pgo ./... || echo "вљ пёЏ  Tests failed, but profile generated"
	@echo "вњ… PGO profile generated: default.pgo"

# Build with PGO (requires default.pgo)
build-pgo: generate-api
	@if [ ! -f "default.pgo" ]; then \
		echo "вљ пёЏ  PGO profile not found. Generating..."; \
		$(MAKE) pgo-profile; \
	fi
	@echo "рџ”Ё Building with PGO..."
	@go build -pgo=default.pgo -o inventory-service .
	@echo "вњ… Built with PGO optimization (+2-14% performance)"


.PHONY: bench bench-json bench-quick

# Run benchmarks (human-readable)
bench:
	go test -run=^ -bench=. -benchmem ./server

# Run benchmarks (JSON output for CI)
bench-json:
	@mkdir -p ../../.benchmarks/results
	go test -run=^ -bench=. -benchmem -json ./server > ../../.benchmarks/results/inventory-service_bench.json

# Quick benchmark (short duration)
bench-quick:
	go test -run=^ -bench=. -benchmem -benchtime=100ms ./server