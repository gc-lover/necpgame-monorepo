.PHONY: generate-api bundle-api clean verify-api check-deps install-deps check-file-sizes build test benchmark

# Issue: #150 - Admin Service ogen migration
SERVICE_NAME := admin-dashboard-service
OGEN := ogen
REDOCLY_CLI := npx -y @redocly/cli

SPEC_DIR := ../../proto/openapi
API_DIR := pkg/api
SERVICE_SPEC := $(SPEC_DIR)/$(SERVICE_NAME).yaml
BUNDLED_SPEC := openapi-bundled.yaml

check-deps:
	@echo "Checking dependencies..."
	@command -v $(OGEN) >/dev/null 2>&1 || { echo "❌ ogen not found. Installing..."; $(MAKE) install-deps; }
	@command -v node >/dev/null 2>&1 || { echo "❌ node not found"; exit 1; }
	@echo "✅ Dependencies installed"

install-deps:
	@echo "Installing ogen..."
	@go install github.com/ogen-go/ogen/cmd/ogen@latest
	@echo "✅ ogen installed"

bundle-api: check-deps
	@echo "Bundling: $(SERVICE_SPEC)"
	@if [ ! -f "$(SERVICE_SPEC)" ]; then echo "❌ Spec not found: $(SERVICE_SPEC)"; exit 1; fi
	@$(REDOCLY_CLI) bundle $(SERVICE_SPEC) -o $(BUNDLED_SPEC) || exit 1
	@echo "✅ Bundled to $(BUNDLED_SPEC)"

generate-api: bundle-api
	@echo "Generating ogen code..."
	@mkdir -p $(API_DIR)
	@$(OGEN) --target $(API_DIR) --package api --clean $(BUNDLED_SPEC) || exit 1
	@echo "✅ ogen generation complete!"
	@$(MAKE) check-file-sizes

check-file-sizes:
	@echo ""
	@echo "File size check (max 500 lines per file):"
	@find $(API_DIR) -name "*.go" -type f | while read file; do \
		lines=$$(wc -l < "$$file" | tr -d ' '); \
		if [ $$lines -gt 500 ]; then \
			echo "⚠️  $$file: $$lines lines (EXCEEDS)"; \
		else \
			echo "✅ $$file: $$lines lines"; \
		fi; \
	done
	@echo ""

verify-api: check-deps
	@echo "Verifying: $(SERVICE_SPEC)"
	@if [ ! -f "$(SERVICE_SPEC)" ]; then echo "❌ Spec not found"; exit 1; fi
	@$(REDOCLY_CLI) lint $(SERVICE_SPEC) || exit 1
	@echo "✅ Valid OpenAPI spec"

clean:
	@rm -rf $(API_DIR)/oas_*_gen.go
	@rm -f $(BUNDLED_SPEC)
	@echo "✅ Cleaned generated files"

build: generate-api
	@go build -o admin-service .

test:
	@go test -v ./...

benchmark:
	@go test -bench=. -benchmem -run=^$$ ./server
