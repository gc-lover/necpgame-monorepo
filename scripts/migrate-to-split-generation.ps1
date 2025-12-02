# Issue: Migrate services to split code generation for SOLID compliance
# Script: Автоматически мигрирует сервисы на раздельную генерацию (types.gen.go, server.gen.go, spec.gen.go)

param(
    [string]$ServiceName = ""
)

$ErrorActionPreference = "Stop"

# Colors for output
function Write-Info {
    param([string]$Message)
    Write-Host "ℹ  $Message" -ForegroundColor Blue
}

function Write-Success {
    param([string]$Message)
    Write-Host "OK $Message" -ForegroundColor Green
}

function Write-Warning {
    param([string]$Message)
    Write-Host "WARNING  $Message" -ForegroundColor Yellow
}

function Write-Error {
    param([string]$Message)
    Write-Host "❌ $Message" -ForegroundColor Red
}

# Проверка зависимостей
function Test-Dependencies {
    Write-Info "Checking dependencies..."
    
    $depsOk = $true
    
    if (-not (Get-Command oapi-codegen -ErrorAction SilentlyContinue)) {
        Write-Error "oapi-codegen not found. Install: go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest"
        $depsOk = $false
    }
    
    if (-not (Get-Command node -ErrorAction SilentlyContinue)) {
        Write-Error "node not found. Install Node.js"
        $depsOk = $false
    }
    
    if (-not $depsOk) {
        exit 1
    }
    
    Write-Success "All dependencies are installed"
}

# Определение типа роутера
function Get-RouterType {
    param([string]$ServiceDir)
    
    $files = Get-ChildItem -Path $ServiceDir -Filter "*.go" -Recurse -ErrorAction SilentlyContinue
    
    foreach ($file in $files) {
        $content = Get-Content $file.FullName -Raw
        if ($content -match "github\.com/go-chi/chi") {
            return "chi-server"
        }
        elseif ($content -match "github\.com/gorilla/mux") {
            return "gorilla-server"
        }
    }
    
    return "chi-server"  # Default
}

# Создание обновленного Makefile с раздельной генерацией
function New-Makefile {
    param(
        [string]$ServiceName,
        [string]$RouterType,
        [string]$ServiceDir
    )
    
    Write-Info "Creating Makefile with split generation..."
    
    $makefileContent = @"
.PHONY: generate-api bundle-api clean verify-api check-deps install-deps generate-types generate-server generate-spec check-file-sizes

SERVICE_NAME := $ServiceName
OAPI_CODEGEN := oapi-codegen
REDOCLY_CLI := npx -y @redocly/cli
ROUTER_TYPE := $RouterType

SPEC_DIR := ../../proto/openapi
API_DIR := pkg/api
SERVICE_SPEC := `$(SPEC_DIR)/`$(SERVICE_NAME).yaml
BUNDLED_SPEC := `$(API_DIR)/`$(SERVICE_NAME).bundled.yaml

# Split output files (SOLID compliance)
TYPES_FILE := `$(API_DIR)/types.gen.go
SERVER_FILE := `$(API_DIR)/server.gen.go
SPEC_FILE := `$(API_DIR)/spec.gen.go

check-deps:
	@echo "Checking dependencies..."
	@command -v `$(OAPI_CODEGEN) >/dev/null 2>&1 || { echo "❌ oapi-codegen not found. Install: go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest"; exit 1; }
	@command -v node >/dev/null 2>&1 || { echo "❌ node not found. Install Node.js"; exit 1; }
	@echo "OK All dependencies are installed"

install-deps:
	@echo "Installing dependencies..."
	@go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest || true
	@echo "OK Dependencies installed"

bundle-api: check-deps
	@echo "Bundling OpenAPI spec: `$(SERVICE_SPEC)"
	@if [ ! -f "`$(SERVICE_SPEC)" ]; then \
		echo "❌ OpenAPI spec not found: `$(SERVICE_SPEC)"; \
		exit 1; \
	fi
	@mkdir -p `$(API_DIR)
	@`$(REDOCLY_CLI) bundle `$(SERVICE_SPEC) -o `$(BUNDLED_SPEC) || { echo "❌ Failed to bundle"; exit 1; }
	@echo "OK Bundled spec: `$(BUNDLED_SPEC)"

# Generate types separately (models only)
generate-types: bundle-api
	@echo "Generating types from: `$(BUNDLED_SPEC)"
	@`$(OAPI_CODEGEN) -package api -generate types -o `$(TYPES_FILE) `$(BUNDLED_SPEC) || { echo "❌ Failed to generate types"; exit 1; }
	@wc -l `$(TYPES_FILE) | awk '{print "OK Generated types: `$(TYPES_FILE) (" `$`$1 " lines)"}'

# Generate server interface separately
generate-server: bundle-api
	@echo "Generating server interface from: `$(BUNDLED_SPEC)"
	@`$(OAPI_CODEGEN) -package api -generate `$(ROUTER_TYPE) -o `$(SERVER_FILE) `$(BUNDLED_SPEC) || { echo "❌ Failed to generate server"; exit 1; }
	@wc -l `$(SERVER_FILE) | awk '{print "OK Generated server: `$(SERVER_FILE) (" `$`$1 " lines)"}'

# Generate spec embedding
generate-spec: bundle-api
	@echo "Generating spec embedding from: `$(BUNDLED_SPEC)"
	@`$(OAPI_CODEGEN) -package api -generate spec -o `$(SPEC_FILE) `$(BUNDLED_SPEC) || { echo "❌ Failed to generate spec"; exit 1; }
	@wc -l `$(SPEC_FILE) | awk '{print "OK Generated spec: `$(SPEC_FILE) (" `$`$1 " lines)"}'

# Check file sizes (500 line limit)
check-file-sizes:
	@echo ""
	@echo "Checking file sizes (max 500 lines)..."
	@for file in `$(TYPES_FILE) `$(SERVER_FILE) `$(SPEC_FILE); do \
		if [ -f "`$`$file" ]; then \
			lines=`$`$(wc -l < "`$`$file" | tr -d ' '); \
			if [ `$`$lines -gt 500 ]; then \
				echo "WARNING  WARNING: `$`$file has `$`$lines lines (exceeds 500 line limit)"; \
			else \
				echo "OK `$`$file: `$`$lines lines (OK)"; \
			fi; \
		fi; \
	done

# Generate all files
generate-api: generate-types generate-server generate-spec check-file-sizes
	@echo ""
	@echo "OK Code generation complete!"
	@echo "Files generated:"
	@ls -lh `$(API_DIR)/*.gen.go 2>/dev/null || true

verify-api: check-deps
	@echo "Verifying OpenAPI spec: `$(SERVICE_SPEC)"
	@`$(REDOCLY_CLI) lint `$(SERVICE_SPEC) || { echo "❌ Spec validation failed"; exit 1; }
	@echo "OK Spec is valid"

clean:
	@echo "Cleaning generated files"
	@rm -f `$(BUNDLED_SPEC) `$(TYPES_FILE) `$(SERVER_FILE) `$(SPEC_FILE)
	@echo "OK Cleaned"
"@

    Set-Content -Path "$ServiceDir\Makefile" -Value $makefileContent -Encoding UTF8
    Write-Success "Makefile created"
}

# Обновление .gitignore
function Update-GitIgnore {
    param([string]$ServiceDir)
    
    Write-Info "Updating .gitignore..."
    
    $gitignorePath = "$ServiceDir\.gitignore"
    
    if (-not (Test-Path $gitignorePath)) {
        $gitignoreContent = @"
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
"@
        Set-Content -Path $gitignorePath -Value $gitignoreContent -Encoding UTF8
        Write-Success ".gitignore created"
    }
    else {
        $content = Get-Content $gitignorePath -Raw
        if (-not ($content -match "types\.gen\.go")) {
            Add-Content -Path $gitignorePath -Value "`n# Generated API code (split generation for SOLID compliance)"
            Add-Content -Path $gitignorePath -Value "pkg/api/types.gen.go"
            Add-Content -Path $gitignorePath -Value "pkg/api/server.gen.go"
            Add-Content -Path $gitignorePath -Value "pkg/api/spec.gen.go"
            Write-Success ".gitignore updated"
        }
        else {
            Write-Info ".gitignore already up to date"
        }
    }
}

# Удаление старого oapi-codegen.yaml
function Remove-OldConfig {
    param([string]$ServiceDir)
    
    $configPath = "$ServiceDir\oapi-codegen.yaml"
    if (Test-Path $configPath) {
        Write-Info "Removing old oapi-codegen.yaml (not needed with Makefile-based generation)..."
        Remove-Item $configPath -Force
        Write-Success "oapi-codegen.yaml removed"
    }
}

# Миграция одного сервиса
function Invoke-ServiceMigration {
    param([string]$ServiceName)
    
    Write-Info "=================================================="
    Write-Info "Migrating service: $ServiceName"
    Write-Info "=================================================="
    
    $serviceDir = "services\$ServiceName-go"
    
    if (-not (Test-Path $serviceDir)) {
        Write-Error "Service directory not found: $serviceDir"
        return
    }
    
    # Определение типа роутера
    $routerType = Get-RouterType $serviceDir
    Write-Info "Detected router type: $routerType"
    
    # Создание Makefile
    New-Makefile -ServiceName $ServiceName -RouterType $routerType -ServiceDir $serviceDir
    
    # Обновление .gitignore
    Update-GitIgnore -ServiceDir $serviceDir
    
    # Удаление старого конфига
    Remove-OldConfig -ServiceDir $serviceDir
    
    # Генерация кода
    Write-Info "Generating code..."
    Push-Location $serviceDir
    
    try {
        & make generate-api 2>&1
        Write-Success "Code generation completed"
        
        # Удаление старого api.gen.go если существует
        if (Test-Path "pkg\api\api.gen.go") {
            Write-Warning "Old api.gen.go found. Consider removing it after verifying new generation works."
            Write-Info "To remove: Remove-Item pkg\api\api.gen.go"
        }
        
        # Проверка размеров файлов
        & make check-file-sizes
    }
    catch {
        Write-Error "Code generation failed. Check OpenAPI spec and dependencies."
        Write-Host $_.Exception.Message
    }
    finally {
        Pop-Location
    }
    
    Write-Success "Service $ServiceName migrated successfully!"
    Write-Host ""
}

# Основная функция
function Main {
    Write-Host ""
    Write-Info "🚀 Migration to Split Code Generation"
    Write-Info "======================================"
    Write-Host ""
    
    # Проверка зависимостей
    Test-Dependencies
    Write-Host ""
    
    if ($ServiceName) {
        # Убираем -go если есть
        $ServiceName = $ServiceName -replace '-go$', ''
        Invoke-ServiceMigration -ServiceName $ServiceName
    }
    else {
        # Миграция всех проблемных сервисов
        Write-Info "Migrating all services with api.gen.go >500 lines..."
        Write-Host ""
        
        $services = @(
            "voice-chat-service",
            "housing-service",
            "clan-war-service",
            "companion-service",
            "cosmetic-service",
            "referral-service",
            "world-service",
            "maintenance-service"
        )
        
        foreach ($service in $services) {
            try {
                Invoke-ServiceMigration -ServiceName $service
            }
            catch {
                Write-Warning "Failed to migrate $service, continuing..."
            }
            Write-Host ""
        }
        
        Write-Success "Migration complete!"
    }
}

# Запуск скрипта
Main

