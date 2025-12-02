# Issue: SOLID refactoring - migrate all services to split code generation
# Script: Автоматизированный рефакторинг всех сервисов с api.gen.go >500 строк

param(
    [switch]$DryRun = $false
)

$ErrorActionPreference = "Continue"

# Colors
function Write-Info { param([string]$Message); Write-Host "ℹ  $Message" -ForegroundColor Blue }
function Write-Success { param([string]$Message); Write-Host "✅ $Message" -ForegroundColor Green }
function Write-Warning { param([string]$Message); Write-Host "⚠️  $Message" -ForegroundColor Yellow }
function Write-Error { param([string]$Message); Write-Host "❌ $Message" -ForegroundColor Red }

# Определение типа роутера
function Get-RouterType {
    param([string]$ServiceDir)
    
    $files = Get-ChildItem -Path $ServiceDir -Filter "*.go" -Recurse -ErrorAction SilentlyContinue
    
    foreach ($file in $files) {
        $content = Get-Content $file.FullName -Raw -ErrorAction SilentlyContinue
        if ($content -match "github\.com/go-chi/chi") {
            return "chi-server"
        }
        elseif ($content -match "github\.com/gorilla/mux") {
            return "gorilla-server"
        }
    }
    
    return "chi-server"
}

# Обновление Makefile
function Update-ServiceMakefile {
    param(
        [string]$ServiceName,
        [string]$RouterType,
        [string]$ServiceDir
    )
    
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
	@command -v `$(OAPI_CODEGEN) >/dev/null 2>&1 || { echo "❌ oapi-codegen not found"; exit 1; }
	@command -v node >/dev/null 2>&1 || { echo "❌ node not found"; exit 1; }
	@echo "✅ All dependencies installed"

install-deps:
	@go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest || true

bundle-api: check-deps
	@echo "Bundling OpenAPI spec: `$(SERVICE_SPEC)"
	@if [ ! -f "`$(SERVICE_SPEC)" ]; then echo "❌ OpenAPI spec not found"; exit 1; fi
	@mkdir -p `$(API_DIR)
	@`$(REDOCLY_CLI) bundle `$(SERVICE_SPEC) -o `$(BUNDLED_SPEC) || { echo "❌ Failed to bundle"; exit 1; }
	@echo "✅ Bundled: `$(BUNDLED_SPEC)"

generate-types: bundle-api
	@echo "Generating types..."
	@`$(OAPI_CODEGEN) -package api -generate types -o `$(TYPES_FILE) `$(BUNDLED_SPEC) || exit 1
	@wc -l `$(TYPES_FILE) | awk '{print "✅ types.gen.go: " `$`$1 " lines"}'

generate-server: bundle-api
	@echo "Generating server..."
	@`$(OAPI_CODEGEN) -package api -generate `$(ROUTER_TYPE) -o `$(SERVER_FILE) `$(BUNDLED_SPEC) || exit 1
	@wc -l `$(SERVER_FILE) | awk '{print "✅ server.gen.go: " `$`$1 " lines"}'

generate-spec: bundle-api
	@echo "Generating spec..."
	@`$(OAPI_CODEGEN) -package api -generate spec -o `$(SPEC_FILE) `$(BUNDLED_SPEC) || exit 1
	@wc -l `$(SPEC_FILE) | awk '{print "✅ spec.gen.go: " `$`$1 " lines"}'

check-file-sizes:
	@echo ""
	@echo "File size check (max 500 lines):"
	@for file in `$(TYPES_FILE) `$(SERVER_FILE) `$(SPEC_FILE); do \
		if [ -f "`$`$file" ]; then \
			lines=`$`$(wc -l < "`$`$file" | tr -d ' '); \
			if [ `$`$lines -gt 500 ]; then \
				echo "⚠️  `$`$file: `$`$lines lines (EXCEEDS LIMIT)"; \
			else \
				echo "✅ `$`$file: `$`$lines lines (OK)"; \
			fi; \
		fi; \
	done

generate-api: generate-types generate-server generate-spec check-file-sizes
	@echo ""
	@echo "✅ Code generation complete!"

verify-api: check-deps
	@echo "Verifying: `$(SERVICE_SPEC)"
	@if [ ! -f "`$(SERVICE_SPEC)" ]; then echo "❌ Spec not found"; exit 1; fi
	@`$(REDOCLY_CLI) lint `$(SERVICE_SPEC) || exit 1
	@echo "✅ Spec is valid"

clean:
	@rm -f `$(BUNDLED_SPEC) `$(TYPES_FILE) `$(SERVER_FILE) `$(SPEC_FILE)
"@

    Set-Content -Path "$ServiceDir\Makefile" -Value $makefileContent -Encoding UTF8
}

# Обновление .gitignore
function Update-ServiceGitIgnore {
    param([string]$ServiceDir)
    
    $gitignorePath = "$ServiceDir\.gitignore"
    
    if (-not (Test-Path $gitignorePath)) {
        $content = @"
*.bundled.yaml
*.merged.yaml
pkg/api/types.gen.go
pkg/api/server.gen.go
pkg/api/spec.gen.go
pkg/api/api.gen.go
*.exe
*.test
*.out
coverage/
vendor/
.idea/
.vscode/
"@
        Set-Content -Path $gitignorePath -Value $content -Encoding UTF8
    }
    else {
        $content = Get-Content $gitignorePath -Raw
        if (-not ($content -match "types\.gen\.go")) {
            Add-Content -Path $gitignorePath -Value "`npkg/api/types.gen.go`npkg/api/server.gen.go`npkg/api/spec.gen.go"
        }
    }
}

# Генерация кода для сервиса
function Invoke-CodeGeneration {
    param([string]$ServiceDir, [string]$ServiceName)
    
    Write-Info "Generating code for $ServiceName..."
    
    try {
        # Bundle
        $specPath = "..\..\proto\openapi\$ServiceName.yaml"
        $bundledPath = "pkg\api\$ServiceName.bundled.yaml"
        
        if (-not (Test-Path $specPath)) {
            Write-Warning "OpenAPI spec not found: $specPath"
            return $false
        }
        
        npx -y @redocly/cli bundle $specPath -o $bundledPath 2>&1 | Out-Null
        
        if (-not (Test-Path $bundledPath)) {
            Write-Error "Failed to bundle spec"
            return $false
        }
        
        # Generate types
        oapi-codegen -package api -generate types -o pkg\api\types.gen.go $bundledPath
        
        # Generate server
        $routerType = Get-RouterType $ServiceDir
        oapi-codegen -package api -generate $routerType -o pkg\api\server.gen.go $bundledPath
        
        # Generate spec
        oapi-codegen -package api -generate spec -o pkg\api\spec.gen.go $bundledPath
        
        # Check sizes
        $typesLines = (Get-Content pkg\api\types.gen.go).Count
        $serverLines = (Get-Content pkg\api\server.gen.go).Count
        $specLines = (Get-Content pkg\api\spec.gen.go).Count
        
        Write-Host "  types.gen.go: $typesLines lines" -ForegroundColor $(if ($typesLines -le 500) { "Green" } else { "Yellow" })
        Write-Host "  server.gen.go: $serverLines lines" -ForegroundColor $(if ($serverLines -le 500) { "Green" } else { "Yellow" })
        Write-Host "  spec.gen.go: $specLines lines" -ForegroundColor $(if ($specLines -le 500) { "Green" } else { "Yellow" })
        
        if ($typesLines -le 500 -and $serverLines -le 500 -and $specLines -le 500) {
            Write-Success "All files <500 lines!"
            return $true
        }
        else {
            Write-Warning "Some files exceed 500 lines - OpenAPI spec needs splitting"
            return $false
        }
    }
    catch {
        Write-Error "Code generation failed: $_"
        return $false
    }
}

# Миграция одного сервиса
function Invoke-ServiceRefactoring {
    param([string]$ServiceName)
    
    Write-Host "`n================================================" -ForegroundColor Cyan
    Write-Host "Refactoring: $ServiceName" -ForegroundColor Cyan
    Write-Host "================================================" -ForegroundColor Cyan
    
    $serviceDir = "services\$ServiceName-go"
    
    if (-not (Test-Path $serviceDir)) {
        Write-Error "Service directory not found: $serviceDir"
        return
    }
    
    Push-Location $serviceDir
    
    try {
        # Проверка текущего api.gen.go
        if (Test-Path "pkg\api\api.gen.go") {
            $oldLines = (Get-Content pkg\api\api.gen.go).Count
            Write-Info "Current api.gen.go: $oldLines lines"
        }
        
        # Определение роутера
        $routerType = Get-RouterType $serviceDir
        Write-Info "Router type: $routerType"
        
        # Обновление Makefile
        if (-not $DryRun) {
            Write-Info "Updating Makefile..."
            Update-ServiceMakefile -ServiceName $ServiceName -RouterType $routerType -ServiceDir $serviceDir
            Write-Success "Makefile updated"
        }
        
        # Обновление .gitignore
        if (-not $DryRun) {
            Write-Info "Updating .gitignore..."
            Update-ServiceGitIgnore -ServiceDir $serviceDir
            Write-Success ".gitignore updated"
        }
        
        # Генерация кода
        if (-not $DryRun) {
            $success = Invoke-CodeGeneration -ServiceDir $serviceDir -ServiceName $ServiceName
            
            if ($success) {
                Write-Success "Service $ServiceName refactored successfully!"
            }
            else {
                Write-Warning "Service $ServiceName needs OpenAPI spec splitting"
            }
        }
        else {
            Write-Info "[DRY RUN] Would generate code for $ServiceName"
        }
    }
    finally {
        Pop-Location
    }
}

# Main
Write-Host ""
Write-Info "🚀 SOLID Code Generation Refactoring"
Write-Info "====================================="
Write-Host ""

if ($DryRun) {
    Write-Warning "DRY RUN MODE - no changes will be made"
    Write-Host ""
}

# Список сервисов с OpenAPI spec И api.gen.go >500 строк
$services = @(
    "character-engram-compatibility-service",
    "character-engram-core-service",
    "combat-damage-service",
    "combat-hacking-service",
    "combat-implants-core-service",
    "combat-sandevistan-service",
    "hacking-core-service",
    "quest-core-service",
    "quest-skill-checks-conditions-service",
    "quest-state-dialogue-service",
    "seasonal-challenges-service",
    "social-chat-channels-service",
    "social-chat-history-service",
    "social-chat-messages-service",
    "stock-analytics-charts-service",
    "stock-analytics-tools-service",
    "stock-margin-service",
    "stock-options-service"
)

Write-Info "Services to refactor: $($services.Count)"
Write-Host ""

$successCount = 0
$failureCount = 0
$results = @()

foreach ($service in $services) {
    try {
        Invoke-ServiceRefactoring -ServiceName $service
        $successCount++
        $results += [PSCustomObject]@{Service=$service; Status="✅ Success"}
    }
    catch {
        $failureCount++
        $errorMsg = $_.Exception.Message
        $results += [PSCustomObject]@{Service=$service; Status="❌ Failed: $errorMsg"}
        Write-Error "Failed to refactor ${service}: $errorMsg"
    }
}

Write-Host "`n================================================" -ForegroundColor Cyan
Write-Host "REFACTORING SUMMARY" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""
$results | Format-Table -AutoSize
Write-Host ""
Write-Success "Completed: $successCount/$($services.Count) services"
if ($failureCount -gt 0) {
    Write-Warning "Failed: $failureCount services"
}
Write-Host ""

