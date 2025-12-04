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
	@command -v node >/dev/null 2>&1 || { echo "❌ node not found"; exit 1; }
	@command -v ogen >/dev/null 2>&1 || { echo "❌ ogen not found"; exit 1; }
	@echo "OK Dependencies installed"

generate-api: check-deps
	@echo "🔧 Generating ogen code for $(SERVICE_NAME)..."
	@$(REDOCLY) bundle $(SPEC_DIR)/$(SERVICE_NAME).yaml -o $(BUNDLED_SPEC)
	@$(OGEN) --target $(API_DIR) --package api --clean $(BUNDLED_SPEC)
	@echo "OK Code generation complete!"

clean:
	@rm -f $(BUNDLED_SPEC)
	@rm -rf $(API_DIR)/oas_*_gen.go

