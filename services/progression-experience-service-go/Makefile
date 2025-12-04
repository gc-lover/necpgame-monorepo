# Issue: #1597-#1599
# Makefile for ogen code generation

.PHONY: generate-api clean

SERVICE_NAME := progression-experience-service
SPEC_DIR := ../../proto/openapi
BUNDLED_SPEC := openapi-bundled.yaml
API_DIR := pkg/api

generate-api:
	npx --yes @redocly/cli bundle $(SPEC_DIR)/$(SERVICE_NAME).yaml -o $(BUNDLED_SPEC)
	ogen --target $(API_DIR) --package api --clean $(BUNDLED_SPEC)

clean:
	rm -f $(BUNDLED_SPEC)
	rm -rf $(API_DIR)/oas_*_gen.go


.PHONY: bench bench-json bench-quick

# Run benchmarks (human-readable)
bench:
	go test -run=^ -bench=. -benchmem ./server

# Run benchmarks (JSON output for CI)
bench-json:
	@mkdir -p ../../.benchmarks/results
	go test -run=^ -bench=. -benchmem -json ./server > ../../.benchmarks/results/progression-experience-service_bench.json

# Quick benchmark (short duration)
bench-quick:
	go test -run=^ -bench=. -benchmem -benchtime=100ms ./server