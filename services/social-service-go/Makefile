.PHONY: generate-api bundle-api clean verify-api check-deps install-deps generate-types generate-server generate-spec check-file-sizes generate-friends-api verify-friends-api generate-groups-api verify-groups-api

SERVICE_NAME := social-service
OAPI_CODEGEN := oapi-codegen
REDOCLY_CLI := npx -y @redocly/cli
ROUTER_TYPE := chi-server

SPEC_DIR := ../../proto/openapi
API_DIR := pkg/api
SERVICE_SPEC := $(SPEC_DIR)/$(SERVICE_NAME).yaml
BUNDLED_SPEC := $(API_DIR)/$(SERVICE_NAME).bundled.yaml

# Split output files (SOLID compliance)
TYPES_FILE := $(API_DIR)/types.gen.go
SERVER_FILE := $(API_DIR)/server.gen.go
SPEC_FILE := $(API_DIR)/spec.gen.go

# Friends API specs
FRIENDS_CORE_SPEC := social-friends-core-service
FRIENDS_REQUESTS_SPEC := social-friends-requests-service
FRIENDS_BLOCKS_SPEC := social-friends-blocks-service
FRIENDS_STATUS_SPEC := social-friends-status-service

FRIENDS_CORE_SPEC_FILE := $(SPEC_DIR)/$(FRIENDS_CORE_SPEC).yaml
FRIENDS_REQUESTS_SPEC_FILE := $(SPEC_DIR)/$(FRIENDS_REQUESTS_SPEC).yaml
FRIENDS_BLOCKS_SPEC_FILE := $(SPEC_DIR)/$(FRIENDS_BLOCKS_SPEC).yaml
FRIENDS_STATUS_SPEC_FILE := $(SPEC_DIR)/$(FRIENDS_STATUS_SPEC).yaml

FRIENDS_BUNDLED := $(API_DIR)/friends/$(FRIENDS_CORE_SPEC).bundled.yaml
FRIENDS_OUTPUT := $(API_DIR)/friends/api.gen.go

# Groups API specs
GROUPS_CORE_SPEC := social-group-core-service

GROUPS_CORE_SPEC_FILE := $(SPEC_DIR)/$(GROUPS_CORE_SPEC).yaml

GROUPS_BUNDLED := $(API_DIR)/groups/$(GROUPS_CORE_SPEC).bundled.yaml
GROUPS_OUTPUT := $(API_DIR)/groups/api.gen.go

check-deps:
	@echo "Checking dependencies..."
	@command -v $(OAPI_CODEGEN) >/dev/null 2>&1 || { echo "❌ oapi-codegen not found"; exit 1; }
	@command -v node >/dev/null 2>&1 || { echo "❌ node not found"; exit 1; }
	@echo "OK Dependencies installed"

install-deps:
	@go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest || true

bundle-api: check-deps
	@echo "Bundling: $(SERVICE_SPEC)"
	@if [ ! -f "$(SERVICE_SPEC)" ]; then echo "❌ Spec not found"; exit 1; fi
	@mkdir -p $(API_DIR)
	@$(REDOCLY_CLI) bundle $(SERVICE_SPEC) -o $(BUNDLED_SPEC) || exit 1
	@echo "OK Bundled"

generate-types: bundle-api
	@echo "Generating types..."
	@$(OAPI_CODEGEN) -package api -generate types -o $(TYPES_FILE) $(BUNDLED_SPEC) || exit 1
	@wc -l $(TYPES_FILE) | awk '{print "OK types.gen.go: " $$1 " lines"}'

generate-server: bundle-api
	@echo "Generating server..."
	@$(OAPI_CODEGEN) -package api -generate $(ROUTER_TYPE) -o $(SERVER_FILE) $(BUNDLED_SPEC) || exit 1
	@wc -l $(SERVER_FILE) | awk '{print "OK server.gen.go: " $$1 " lines"}'

generate-spec: bundle-api
	@echo "Generating spec..."
	@$(OAPI_CODEGEN) -package api -generate spec -o $(SPEC_FILE) $(BUNDLED_SPEC) || exit 1
	@wc -l $(SPEC_FILE) | awk '{print "OK spec.gen.go: " $$1 " lines"}'

check-file-sizes:
	@echo ""
	@echo "File size check (max 1000 lines):"
	@for file in $(TYPES_FILE) $(SERVER_FILE) $(SPEC_FILE); do \
		if [ -f "$$file" ]; then \
			lines=$$(wc -l < "$$file" | tr -d ' '); \
			if [ $$lines -gt 1000 ]; then \
				echo "WARNING  $$file: $$lines lines (EXCEEDS)"; \
			else \
				echo "OK $$file: $$lines lines"; \
			fi; \
		fi; \
	done

bundle-friends-api: check-deps
	@echo "Bundling Friends OpenAPI specs..."
	@mkdir -p $(API_DIR)/friends
	@$(REDOCLY_CLI) bundle $(FRIENDS_CORE_SPEC_FILE) -o $(FRIENDS_BUNDLED) || exit 1
	@echo "OK Bundled Friends spec: $(FRIENDS_BUNDLED)"

generate-friends-api: bundle-friends-api
	@echo "Generating Friends Go code from: $(FRIENDS_BUNDLED)"
	@if [ ! -f "$(FRIENDS_BUNDLED)" ]; then \
		echo "❌ Bundled spec not found: $(FRIENDS_BUNDLED)"; \
		exit 1; \
	fi
	@$(OAPI_CODEGEN) -package friends -generate types,$(ROUTER_TYPE) -o $(FRIENDS_OUTPUT) $(FRIENDS_BUNDLED) || exit 1
	@echo "OK Generated Friends code: $(FRIENDS_OUTPUT)"

verify-friends-api: check-deps
	@echo "Verifying Friends OpenAPI specs..."
	@if [ ! -f "$(FRIENDS_CORE_SPEC_FILE)" ]; then \
		echo "❌ OpenAPI spec not found: $(FRIENDS_CORE_SPEC_FILE)"; \
		exit 1; \
	fi
	@$(REDOCLY_CLI) lint $(FRIENDS_CORE_SPEC_FILE) $(FRIENDS_REQUESTS_SPEC_FILE) $(FRIENDS_BLOCKS_SPEC_FILE) $(FRIENDS_STATUS_SPEC_FILE) || exit 1
	@echo "OK Friends specs are valid"

bundle-groups-api: check-deps
	@echo "Bundling Groups OpenAPI specs..."
	@mkdir -p $(API_DIR)/groups
	@$(REDOCLY_CLI) bundle $(GROUPS_CORE_SPEC_FILE) -o $(GROUPS_BUNDLED) || exit 1
	@echo "OK Bundled Groups spec: $(GROUPS_BUNDLED)"

generate-groups-api: bundle-groups-api
	@echo "Generating Groups Go code from: $(GROUPS_BUNDLED)"
	@if [ ! -f "$(GROUPS_BUNDLED)" ]; then \
		echo "❌ Bundled spec not found: $(GROUPS_BUNDLED)"; \
		exit 1; \
	fi
	@$(OAPI_CODEGEN) -package groups -generate types,$(ROUTER_TYPE) -o $(GROUPS_OUTPUT) $(GROUPS_BUNDLED) || exit 1
	@echo "OK Generated Groups code: $(GROUPS_OUTPUT)"

verify-groups-api: check-deps
	@echo "Verifying Groups OpenAPI specs..."
	@if [ ! -f "$(GROUPS_CORE_SPEC_FILE)" ]; then \
		echo "❌ OpenAPI spec not found: $(GROUPS_CORE_SPEC_FILE)"; \
		exit 1; \
	fi
	@$(REDOCLY_CLI) lint $(GROUPS_CORE_SPEC_FILE) || exit 1
	@echo "OK Groups specs are valid"

generate-api: generate-types generate-server generate-spec generate-friends-api generate-groups-api check-file-sizes
	@echo ""
	@echo "OK Generation complete!"

verify-api: check-deps verify-friends-api verify-groups-api
	@echo "Verifying: $(SERVICE_SPEC)"
	@if [ ! -f "$(SERVICE_SPEC)" ]; then exit 1; fi
	@$(REDOCLY_CLI) lint $(SERVICE_SPEC) || exit 1
	@echo "OK Valid"

clean:
	@rm -f $(BUNDLED_SPEC) $(TYPES_FILE) $(SERVER_FILE) $(SPEC_FILE) $(FRIENDS_BUNDLED) $(FRIENDS_OUTPUT) $(GROUPS_BUNDLED) $(GROUPS_OUTPUT)

