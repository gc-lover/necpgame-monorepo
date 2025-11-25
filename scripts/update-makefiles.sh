#!/bin/bash
# Скрипт для обновления всех Makefile до улучшенной версии

set -e

echo "🔄 Обновление Makefile для всех сервисов..."
echo ""

SERVICES=(
    "battle-pass-service-go:battle-pass-core-service:gorilla-server"
    "inventory-service-go:inventory-service:gorilla-server"
    "housing-service-go:housing-service:gorilla-server"
    "clan-war-service-go:clan-war-service:gorilla-server"
    "movement-service-go:movement-service:gorilla-server"
    "referral-service-go:referral-service:gorilla-server"
    "voice-chat-service-go:voice-chat-service:gorilla-server"
    "achievement-service-go:achievement-core-service:gorilla-server"
    "admin-service-go:admin-service:gorilla-server"
    "character-service-go:character-core-service:gorilla-server"
    "economy-service-go:economy-inventory-core-service:gorilla-server"
    "feedback-service-go:feedback-service:gorilla-server"
    "gameplay-service-go:gameplay-progression-core-service:gorilla-server"
    "leaderboard-service-go:leaderboard-core-service:gorilla-server"
    "social-service-go:social-friends-core-service:gorilla-server"
    "support-service-go:support-tickets-core-service:gorilla-server"
    "world-service-go:world-events-service:gorilla-server"
)

TOTAL=0
UPDATED=0
SKIPPED=0

for service_info in "${SERVICES[@]}"; do
    IFS=':' read -r service_dir service_name router_type <<< "$service_info"
    service_path="services/$service_dir"
    makefile_path="$service_path/Makefile"
    
    TOTAL=$((TOTAL + 1))
    
    if [ ! -f "$makefile_path" ]; then
        echo "WARNING  $service_dir: Makefile not found, skipping"
        SKIPPED=$((SKIPPED + 1))
        continue
    fi
    
    if grep -q "check-deps" "$makefile_path"; then
        echo "OK $service_dir: already has improved Makefile"
        SKIPPED=$((SKIPPED + 1))
    else
        echo "🔄 $service_dir: updating Makefile..."
        
        cat > "$makefile_path" << EOF
.PHONY: generate-api bundle-api clean verify-api check-deps install-deps

SERVICE_NAME := $service_name
OAPI_CODEGEN := oapi-codegen
REDOCLY_CLI := npx -y @redocly/cli
ROUTER_TYPE := $router_type

SPEC_DIR := ../../proto/openapi
API_DIR := pkg/api
SERVICE_SPEC := \$(SPEC_DIR)/\$(SERVICE_NAME).yaml
BUNDLED_SPEC := \$(API_DIR)/\$(SERVICE_NAME).bundled.yaml
OUTPUT_FILE := \$(API_DIR)/api.gen.go

check-deps:
	@echo "Checking dependencies..."
	@command -v \$(OAPI_CODEGEN) >/dev/null 2>&1 || { echo "❌ oapi-codegen not found. Install with: go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest"; exit 1; }
	@command -v node >/dev/null 2>&1 || { echo "❌ node not found. Install Node.js"; exit 1; }
	@command -v npx >/dev/null 2>&1 || { echo "❌ npx not found. Install Node.js"; exit 1; }
	@echo "OK All dependencies are installed"

install-deps:
	@echo "Installing dependencies..."
	@go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest || true
	@echo "OK Dependencies installed"

bundle-api: check-deps
	@echo "Bundling OpenAPI spec: \$(SERVICE_SPEC)"
	@if [ ! -f "\$(SERVICE_SPEC)" ]; then \
		echo "❌ OpenAPI spec not found: \$(SERVICE_SPEC)"; \
		exit 1; \
	fi
	@mkdir -p \$(API_DIR)
	@\$(REDOCLY_CLI) bundle \$(SERVICE_SPEC) -o \$(BUNDLED_SPEC) || { echo "❌ Failed to bundle OpenAPI spec"; exit 1; }
	@echo "OK Bundled spec: \$(BUNDLED_SPEC)"

generate-api: bundle-api
	@echo "Generating Go code from: \$(BUNDLED_SPEC)"
	@if [ ! -f "\$(BUNDLED_SPEC)" ]; then \
		echo "❌ Bundled spec not found: \$(BUNDLED_SPEC)"; \
		exit 1; \
	fi
	@\$(OAPI_CODEGEN) -package api -generate types,\$(ROUTER_TYPE) -o \$(OUTPUT_FILE) \$(BUNDLED_SPEC) || { echo "❌ Failed to generate code"; exit 1; }
	@echo "OK Generated code: \$(OUTPUT_FILE)"

verify-api: check-deps
	@echo "Verifying OpenAPI spec: \$(SERVICE_SPEC)"
	@if [ ! -f "\$(SERVICE_SPEC)" ]; then \
		echo "❌ OpenAPI spec not found: \$(SERVICE_SPEC)"; \
		exit 1; \
	fi
	@\$(REDOCLY_CLI) lint \$(SERVICE_SPEC) || { echo "WARNING  OpenAPI spec validation failed"; exit 1; }
	@echo "OK OpenAPI spec is valid"

clean:
	@echo "Cleaning generated files"
	@rm -f \$(BUNDLED_SPEC) \$(OUTPUT_FILE)
	@echo "OK Cleaned generated files"
EOF
        UPDATED=$((UPDATED + 1))
        echo "   OK Updated"
    fi
    echo ""
done

echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "📊 Results:"
echo "  Total services: $TOTAL"
echo "  OK Already updated: $SKIPPED"
echo "  🔄 Updated: $UPDATED"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

if [ $UPDATED -gt 0 ]; then
    echo "OK Successfully updated $UPDATED Makefile(s)!"
fi

exit 0

