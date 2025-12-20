.PHONY: generate-api bundle-api clean verify-api check-deps install-deps check-file-sizes build test benchmark

# Issue: #150 - Economy Service oapi-codegen
SERVICE_NAME := economy-core-service
OAPI_CODEGEN := oapi-codegen
REDOCLY_CLI := npx -y @redocly/cli

SPEC_DIR := ../../proto/openapi
API_DIR := pkg/api
SERVICE_SPEC := $(SPEC_DIR)/$(SERVICE_NAME).yaml
BUNDLED_SPEC := openapi-bundled.yaml

check-deps:
	@echo "Checking dependencies..."
	@command -v $(OAPI_CODEGEN) >/dev/null 2>&1 || { echo "❌ oapi-codegen not found. Installing..."; $(MAKE) install-deps; }
	@command -v node >/dev/null 2>&1 || { echo "❌ node not found"; exit 1; }
	@echo "OK Dependencies installed"

install-deps:
	@echo "Installing oapi-codegen..."
	@go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest
	@echo "OK oapi-codegen installed"

bundle-api: check-deps
	@echo "Bundling: $(SERVICE_SPEC)"
	@if [ ! -f "$(SERVICE_SPEC)" ]; then echo "❌ Spec not found: $(SERVICE_SPEC)"; exit 1; fi
	@$(REDOCLY_CLI) bundle $(SERVICE_SPEC) -o $(BUNDLED_SPEC) || exit 1
	@echo "OK Bundled to $(BUNDLED_SPEC)"

generate-api: bundle-api
	@echo "Generating oapi-codegen code..."
	@mkdir -p $(API_DIR)
	@$(OAPI_CODEGEN) -generate types,client,server -package api $(BUNDLED_SPEC) > $(API_DIR)/api.gen.go || exit 1
	@echo "OK oapi-codegen generation complete!"
	@$(MAKE) check-file-sizes

check-file-sizes:
	@echo ""
	@echo "File size check (max 1000 lines per file):"
	@find $(API_DIR) -name "*.go" -type f | while read file; do \
		lines=$$(wc -l < "$$file" | tr -d ' '); \
		if [ $$lines -gt 1000 ]; then \
			echo "WARNING  $$file: $$lines lines (EXCEEDS)"; \
		else \
			echo "OK $$file: $$lines lines"; \
		fi; \
	done
	@echo ""

verify-api: check-deps
	@echo "Verifying: $(SERVICE_SPEC)"
	@if [ ! -f "$(SERVICE_SPEC)" ]; then echo "❌ Spec not found"; exit 1; fi
	@$(REDOCLY_CLI) lint $(SERVICE_SPEC) || exit 1
	@echo "OK Valid OpenAPI spec"

clean:
	@rm -rf $(API_DIR)/oas_*_gen.go
	@rm -f $(BUNDLED_SPEC)
	@echo "OK Cleaned generated files"

build: test bench-quick generate-api
	@go build -o economy-service .

test:
	@go test -v ./...

benchmark:
	@go test -bench=. -benchmem -run=^$$ ./server

.PHONY: test
test:
	@go test -v ./...


.PHONY: bench-quick
bench-quick:
	@if [ -f "server/handlers_bench_test.go" ] || find . -name "*_bench_test.go" | grep -q .; then \		go test -run=^\$\$ -bench=. -benchmem -benchtime=100ms ./server; \	fi
