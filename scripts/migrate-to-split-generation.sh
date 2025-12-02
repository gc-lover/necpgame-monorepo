#!/usr/bin/env bash
# Issue: Migrate services to split code generation for SOLID compliance
# Script: Автоматически мигрирует сервисы на раздельную генерацию (types.gen.go, server.gen.go, spec.gen.go)

set -euo pipefail

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Функция вывода сообщений
log_info() {
    echo -e "${BLUE}ℹ ${NC}$1"
}

log_success() {
    echo -e "${GREEN}✅${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}⚠️ ${NC}$1"
}

log_error() {
    echo -e "${RED}❌${NC} $1"
}

# Проверка зависимостей
check_dependencies() {
    log_info "Checking dependencies..."
    
    local deps_ok=true
    
    if ! command -v oapi-codegen &> /dev/null; then
        log_error "oapi-codegen not found. Install: go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest"
        deps_ok=false
    fi
    
    if ! command -v node &> /dev/null; then
        log_error "node not found. Install Node.js"
        deps_ok=false
    fi
    
    if [ "$deps_ok" = false ]; then
        exit 1
    fi
    
    log_success "All dependencies are installed"
}

# Определение типа роутера
detect_router_type() {
    local service_dir="$1"
    
    if grep -rq "github.com/go-chi/chi" "$service_dir"/*.go "$service_dir"/server/*.go 2>/dev/null; then
        echo "chi-server"
    elif grep -rq "github.com/gorilla/mux" "$service_dir"/*.go "$service_dir"/server/*.go 2>/dev/null; then
        echo "gorilla-server"
    else
        echo "chi-server"  # Default
    fi
}

# Создание обновленного Makefile с раздельной генерацией
create_makefile() {
    local service_name="$1"
    local router_type="$2"
    local service_dir="$3"
    
    log_info "Creating Makefile with split generation..."
    
    cat > "$service_dir/Makefile" << 'EOF'
.PHONY: generate-api bundle-api clean verify-api check-deps install-deps generate-types generate-server generate-spec check-file-sizes

SERVICE_NAME := SERVICE_NAME_PLACEHOLDER
OAPI_CODEGEN := oapi-codegen
REDOCLY_CLI := npx -y @redocly/cli
ROUTER_TYPE := ROUTER_TYPE_PLACEHOLDER

SPEC_DIR := ../../proto/openapi
API_DIR := pkg/api
SERVICE_SPEC := $(SPEC_DIR)/$(SERVICE_NAME).yaml
BUNDLED_SPEC := $(API_DIR)/$(SERVICE_NAME).bundled.yaml

# Split output files (SOLID compliance)
TYPES_FILE := $(API_DIR)/types.gen.go
SERVER_FILE := $(API_DIR)/server.gen.go
SPEC_FILE := $(API_DIR)/spec.gen.go

check-deps:
	@echo "Checking dependencies..."
	@command -v $(OAPI_CODEGEN) >/dev/null 2>&1 || { echo "❌ oapi-codegen not found. Install: go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest"; exit 1; }
	@command -v node >/dev/null 2>&1 || { echo "❌ node not found. Install Node.js"; exit 1; }
	@echo "✅ All dependencies are installed"

install-deps:
	@echo "Installing dependencies..."
	@go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest || true
	@echo "✅ Dependencies installed"

bundle-api: check-deps
	@echo "Bundling OpenAPI spec: $(SERVICE_SPEC)"
	@if [ ! -f "$(SERVICE_SPEC)" ]; then \
		echo "❌ OpenAPI spec not found: $(SERVICE_SPEC)"; \
		exit 1; \
	fi
	@mkdir -p $(API_DIR)
	@$(REDOCLY_CLI) bundle $(SERVICE_SPEC) -o $(BUNDLED_SPEC) || { echo "❌ Failed to bundle"; exit 1; }
	@echo "✅ Bundled spec: $(BUNDLED_SPEC)"

# Generate types separately (models only)
generate-types: bundle-api
	@echo "Generating types from: $(BUNDLED_SPEC)"
	@$(OAPI_CODEGEN) -package api -generate types -o $(TYPES_FILE) $(BUNDLED_SPEC) || { echo "❌ Failed to generate types"; exit 1; }
	@wc -l $(TYPES_FILE) | awk '{print "✅ Generated types: $(TYPES_FILE) (" $$1 " lines)"}'

# Generate server interface separately
generate-server: bundle-api
	@echo "Generating server interface from: $(BUNDLED_SPEC)"
	@$(OAPI_CODEGEN) -package api -generate $(ROUTER_TYPE) -o $(SERVER_FILE) $(BUNDLED_SPEC) || { echo "❌ Failed to generate server"; exit 1; }
	@wc -l $(SERVER_FILE) | awk '{print "✅ Generated server: $(SERVER_FILE) (" $$1 " lines)"}'

# Generate spec embedding
generate-spec: bundle-api
	@echo "Generating spec embedding from: $(BUNDLED_SPEC)"
	@$(OAPI_CODEGEN) -package api -generate spec -o $(SPEC_FILE) $(BUNDLED_SPEC) || { echo "❌ Failed to generate spec"; exit 1; }
	@wc -l $(SPEC_FILE) | awk '{print "✅ Generated spec: $(SPEC_FILE) (" $$1 " lines)"}'

# Check file sizes (500 line limit)
check-file-sizes:
	@echo ""
	@echo "Checking file sizes (max 500 lines)..."
	@for file in $(TYPES_FILE) $(SERVER_FILE) $(SPEC_FILE); do \
		if [ -f "$$file" ]; then \
			lines=$$(wc -l < "$$file" | tr -d ' '); \
			if [ $$lines -gt 500 ]; then \
				echo "⚠️  WARNING: $$file has $$lines lines (exceeds 500 line limit)"; \
			else \
				echo "✅ $$file: $$lines lines (OK)"; \
			fi; \
		fi; \
	done

# Generate all files
generate-api: generate-types generate-server generate-spec check-file-sizes
	@echo ""
	@echo "✅ Code generation complete!"
	@echo "Files generated:"
	@ls -lh $(API_DIR)/*.gen.go 2>/dev/null || true

verify-api: check-deps
	@echo "Verifying OpenAPI spec: $(SERVICE_SPEC)"
	@$(REDOCLY_CLI) lint $(SERVICE_SPEC) || { echo "❌ Spec validation failed"; exit 1; }
	@echo "✅ Spec is valid"

clean:
	@echo "Cleaning generated files"
	@rm -f $(BUNDLED_SPEC) $(TYPES_FILE) $(SERVER_FILE) $(SPEC_FILE)
	@echo "✅ Cleaned"
EOF

    # Замена плейсхолдеров
    sed -i "s/SERVICE_NAME_PLACEHOLDER/$service_name/" "$service_dir/Makefile"
    sed -i "s/ROUTER_TYPE_PLACEHOLDER/$router_type/" "$service_dir/Makefile"
    
    log_success "Makefile created"
}

# Обновление .gitignore
update_gitignore() {
    local service_dir="$1"
    
    log_info "Updating .gitignore..."
    
    # Создаем .gitignore если его нет
    if [ ! -f "$service_dir/.gitignore" ]; then
        cat > "$service_dir/.gitignore" << 'EOF'
# Generated OpenAPI bundled files (DO NOT commit)
*.bundled.yaml
*.merged.yaml

# Generated API code (multiple files for SOLID compliance)
pkg/api/types.gen.go
pkg/api/server.gen.go
pkg/api/spec.gen.go

# Legacy single file (if exists)
pkg/api/api.gen.go

# Binaries
*.exe
*.exe~
*.dll
*.so
*.dylib

# Test binary
*.test

# Output of the go coverage tool
*.out
coverage/

# Dependency directories
vendor/

# IDE
.idea/
.vscode/
*.swp
*.swo
*~
EOF
        log_success ".gitignore created"
    else
        # Добавляем новые строки если их нет
        if ! grep -q "types.gen.go" "$service_dir/.gitignore"; then
            echo "" >> "$service_dir/.gitignore"
            echo "# Generated API code (split generation for SOLID compliance)" >> "$service_dir/.gitignore"
            echo "pkg/api/types.gen.go" >> "$service_dir/.gitignore"
            echo "pkg/api/server.gen.go" >> "$service_dir/.gitignore"
            echo "pkg/api/spec.gen.go" >> "$service_dir/.gitignore"
            log_success ".gitignore updated"
        else
            log_info ".gitignore already up to date"
        fi
    fi
}

# Удаление старого oapi-codegen.yaml
cleanup_old_config() {
    local service_dir="$1"
    
    if [ -f "$service_dir/oapi-codegen.yaml" ]; then
        log_info "Removing old oapi-codegen.yaml (not needed with Makefile-based generation)..."
        rm -f "$service_dir/oapi-codegen.yaml"
        log_success "oapi-codegen.yaml removed"
    fi
}

# Миграция одного сервиса
migrate_service() {
    local service_name="$1"
    local service_dir="services/${service_name}-go"
    
    log_info "=================================================="
    log_info "Migrating service: $service_name"
    log_info "=================================================="
    
    # Проверка существования директории
    if [ ! -d "$service_dir" ]; then
        log_error "Service directory not found: $service_dir"
        return 1
    fi
    
    # Определение типа роутера
    local router_type
    router_type=$(detect_router_type "$service_dir")
    log_info "Detected router type: $router_type"
    
    # Создание Makefile
    create_makefile "$service_name" "$router_type" "$service_dir"
    
    # Обновление .gitignore
    update_gitignore "$service_dir"
    
    # Удаление старого конфига
    cleanup_old_config "$service_dir"
    
    # Генерация кода
    log_info "Generating code..."
    cd "$service_dir"
    
    if make generate-api 2>&1 | tee /tmp/codegen.log; then
        log_success "Code generation completed"
        
        # Удаление старого api.gen.go если существует
        if [ -f "pkg/api/api.gen.go" ]; then
            log_warning "Old api.gen.go found. Consider removing it after verifying new generation works."
            log_info "To remove: rm pkg/api/api.gen.go"
        fi
        
        # Проверка размеров файлов
        make check-file-sizes
    else
        log_error "Code generation failed. Check OpenAPI spec and dependencies."
        cat /tmp/codegen.log
        cd - > /dev/null
        return 1
    fi
    
    cd - > /dev/null
    
    log_success "Service $service_name migrated successfully!"
    echo ""
}

# Основная функция
main() {
    echo ""
    log_info "🚀 Migration to Split Code Generation"
    log_info "======================================"
    echo ""
    
    # Проверка зависимостей
    check_dependencies
    echo ""
    
    # Если указан конкретный сервис
    if [ $# -gt 0 ]; then
        local service_name="${1%-go}"  # Убираем -go если есть
        migrate_service "$service_name"
    else
        # Миграция всех проблемных сервисов
        log_info "Migrating all services with api.gen.go >500 lines..."
        echo ""
        
        local services=(
            "voice-chat-service"
            "housing-service"
            "clan-war-service"
            "companion-service"
            "cosmetic-service"
            "referral-service"
            "world-service"
            "maintenance-service"
        )
        
        for service in "${services[@]}"; do
            migrate_service "$service" || log_warning "Failed to migrate $service, continuing..."
            echo ""
        done
        
        log_success "Migration complete!"
    fi
}

# Запуск скрипта
main "$@"

