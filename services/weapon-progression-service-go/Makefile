# Issue: #1595
.PHONY: generate-api bundle-api clean verify-api check-deps install-deps

SERVICE_NAME := weapon-progression-service
OGEN := ogen
REDOCLY_CLI := npx -y @redocly/cli

SPEC_DIR := ../../proto/openapi
API_DIR := pkg/api
SERVICE_SPEC := $(SPEC_DIR)/$(SERVICE_NAME).yaml
BUNDLED_SPEC := openapi-bundled.yaml

check-deps:
	@command -v $(OGEN) >/dev/null 2>&1 || { echo "вќЊ ogen not found"; exit 1; }
	@command -v node >/dev/null 2>&1 || { echo "вќЊ node not found"; exit 1; }

install-deps:
	@go install github.com/ogen-go/ogen/cmd/ogen@latest || true

bundle-api: check-deps
	@echo "Bundling: $(SERVICE_SPEC)"
	@if [ ! -f "$(SERVICE_SPEC)" ]; then echo "вќЊ Spec not found"; exit 1; fi
	@$(REDOCLY_CLI) bundle $(SERVICE_SPEC) -o $(BUNDLED_SPEC) || exit 1
	@echo "вњ… Bundled"

generate-api: bundle-api
	@echo "Generating ogen code..."
	@$(OGEN) --target $(API_DIR) --package api --clean $(BUNDLED_SPEC) || exit 1
	@echo "вњ… Generation complete!"

verify-api: check-deps
	@echo "Verifying: $(SERVICE_SPEC)"
	@if [ ! -f "$(SERVICE_SPEC)" ]; then exit 1; fi
	@$(REDOCLY_CLI) lint $(SERVICE_SPEC) || exit 1
	@echo "вњ… Valid"

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
	@CGO_ENABLED=0 go build -ldflags="-w -s" -o bin/weapon-progression-service .
	@rm -f $(BUNDLED_SPEC)
	@rm -rf $(API_DIR)/oas_*_gen.go


.PHONY: bench bench-json bench-quick

# Run benchmarks (human-readable)
bench:
	go test -run=^ -bench=. -benchmem ./server

# Run benchmarks (JSON output for CI)
bench-json:
	@mkdir -p ../../.benchmarks/results
	go test -run=^ -bench=. -benchmem -json ./server > ../../.benchmarks/results/weapon-progression-service_bench.json

# Quick benchmark (short duration)
bench-quick:
	go test -run=^ -bench=. -benchmem -benchtime=100ms ./server