# Issue: #44
.PHONY: generate-api bundle-api clean verify-api check-deps

SERVICE_NAME := world-events-scheduler-service
REDOCLY_CLI := npx -y @redocly/cli
OGEN := ogen

SPEC_DIR := ../../proto/openapi
API_DIR := pkg/api
SERVICE_SPEC := $(SPEC_DIR)/$(SERVICE_NAME).yaml
BUNDLED_SPEC := openapi-bundled.yaml

check-deps:
	@command -v node >/dev/null 2>&1 || { echo "вќЊ node not found"; exit 1; }
	@command -v $(OGEN) >/dev/null 2>&1 || { echo "вќЊ ogen not found"; exit 1; }

bundle-api: check-deps
	@$(REDOCLY_CLI) bundle $(SERVICE_SPEC) -o $(BUNDLED_SPEC) || exit 1

generate-api: bundle-api
	@$(OGEN) --target $(API_DIR) --package api --clean $(BUNDLED_SPEC) || exit 1

verify-api: check-deps
	@$(REDOCLY_CLI) lint $(SERVICE_SPEC) || exit 1

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
	@CGO_ENABLED=0 go build -ldflags="-w -s" -o bin/world-events-scheduler-service .
	@rm -f $(BUNDLED_SPEC)


.PHONY: bench bench-json bench-quick

# Run benchmarks (human-readable)
bench:
	go test -run=^ -bench=. -benchmem ./server

# Run benchmarks (JSON output for CI)
bench-json:
	@mkdir -p ../../.benchmarks/results
	go test -run=^ -bench=. -benchmem -json ./server > ../../.benchmarks/results/world-events-scheduler-service_bench.json

# Quick benchmark (short duration)
bench-quick:
	go test -run=^ -bench=. -benchmem -benchtime=100ms ./server