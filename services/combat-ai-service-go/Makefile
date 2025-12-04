# Issue: #1595
# Makefile for ogen code generation (migrated from oapi-codegen)

.PHONY: generate-api clean check-deps

# Service configuration
SERVICE_NAME := combat-ai-enemies-profiles-service
SPEC_DIR := ../../proto/openapi
BUNDLED_SPEC := openapi-bundled.yaml
API_DIR := pkg/api

# Tools
REDOCLY := npx --yes @redocly/cli
OGEN := ogen

check-deps:
	@echo "Checking dependencies..."
	@command -v node >/dev/null 2>&1 || { echo "❌ node not found. Install Node.js"; exit 1; }
	@command -v ogen >/dev/null 2>&1 || { echo "❌ ogen not found. Run: go install github.com/ogen-go/ogen/cmd/ogen@latest"; exit 1; }
	@echo "OK All dependencies installed"

generate-api: check-deps
	@echo "🔧 Generating ogen code for $(SERVICE_NAME)..."
	@echo ""
	@echo "Step 1: Bundling OpenAPI spec..."
	@$(REDOCLY) bundle $(SPEC_DIR)/$(SERVICE_NAME).yaml -o $(BUNDLED_SPEC)
	@echo "OK Bundled → $(BUNDLED_SPEC)"
	@echo ""
	@echo "Step 2: Generating code with ogen..."
	@$(OGEN) --target $(API_DIR) --package api --clean $(BUNDLED_SPEC)
	@echo "OK Generated → $(API_DIR)/"
	@echo ""
	@echo "Step 3: Checking generated files..."
	@ls -lh $(API_DIR)/oas_*_gen.go | awk '{print "  " $$9 " (" $$5 ")"}'
	@echo ""
	@echo "OK Code generation complete!"
	@echo ""
	@echo "Next steps:"
	@echo "  1. Update handlers in server/ to implement ogen interfaces"
	@echo "  2. Run: go build ./..."
	@echo "  3. Run: go test ./..."

clean:
	@echo "Cleaning generated files..."
	@rm -f $(BUNDLED_SPEC)
	@rm -rf $(API_DIR)/oas_*_gen.go
	@echo "OK Cleaned"
