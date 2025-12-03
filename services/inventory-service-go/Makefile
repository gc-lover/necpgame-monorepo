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
	@command -v $(OGEN) >/dev/null 2>&1 || { echo "❌ ogen not found. Install: go install -v github.com/ogen-go/ogen/cmd/ogen@latest"; exit 1; }
	@command -v node >/dev/null 2>&1 || { echo "❌ node not found. Install Node.js"; exit 1; }

verify-api: check-deps
	@echo "🔍 Validating OpenAPI spec..."
	@$(REDOCLY_CLI) lint $(SERVICE_SPEC) || exit 1
	@echo "✅ OpenAPI spec is valid!"

generate-api: check-deps verify-api
	@echo "📦 Bundling OpenAPI spec..."
	@$(REDOCLY_CLI) bundle $(SERVICE_SPEC) -o $(BUNDLED_SPEC) || exit 1
	@echo "🔨 Generating ogen code..."
	@$(OGEN) --target $(API_DIR) --package api --clean $(BUNDLED_SPEC) || exit 1
	@echo "✅ ogen code generated successfully!"

clean:
	@echo "🧹 Cleaning generated files..."
	@rm -rf $(API_DIR)/* $(BUNDLED_SPEC)
	@echo "✅ Clean complete!"

