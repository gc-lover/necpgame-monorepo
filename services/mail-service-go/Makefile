# Issue: #1597-#1599
# Makefile for ogen code generation

.PHONY: generate-api clean

SERVICE_NAME := mail-service
SPEC_DIR := ../../proto/openapi
BUNDLED_SPEC := openapi-bundled.yaml
API_DIR := pkg/api

generate-api:
	npx --yes @redocly/cli bundle $(SPEC_DIR)/$(SERVICE_NAME).yaml -o $(BUNDLED_SPEC)
	ogen --target $(API_DIR) --package api --clean $(BUNDLED_SPEC)

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
	@CGO_ENABLED=0 go build -ldflags="-w -s" -o bin/mail-service .
	rm -f $(BUNDLED_SPEC)
	rm -rf $(API_DIR)/oas_*_gen.go


.PHONY: bench bench-json bench-quick

# Run benchmarks (human-readable)
bench:
	go test -run=^ -bench=. -benchmem ./server

# Run benchmarks (JSON output for CI)
bench-json:
	@mkdir -p ../../.benchmarks/results
	go test -run=^ -bench=. -benchmem -json ./server > ../../.benchmarks/results/mail-service_bench.json

# Quick benchmark (short duration)
bench-quick:
	go test -run=^ -bench=. -benchmem -benchtime=100ms ./server
















