# Quick migration script for one service
# Issue: #1595
# Usage: .\migrate-one-service.ps1 -ServiceName "combat-damage-service-go"

param(
    [Parameter(Mandatory=$true)]
    [string]$ServiceName
)

Write-Host "🚀 Migrating $ServiceName to ogen..." -ForegroundColor Cyan

$ServicePath = "services\$ServiceName"
$TemplatePath = "services\combat-actions-service-go"

# Step 1: Update Makefile
Write-Host "1️⃣ Updating Makefile..." -ForegroundColor Yellow
$makefileTemplate = @"
# Issue: #1595
# Makefile for ogen code generation

.PHONY: generate-api clean

SERVICE_NAME := $($ServiceName -replace '-service-go$', '-service')
SPEC_DIR := ../../proto/openapi
BUNDLED_SPEC := openapi-bundled.yaml
API_DIR := pkg/api

generate-api:
	npx --yes @redocly/cli bundle `$(SPEC_DIR)/`$(SERVICE_NAME).yaml -o `$(BUNDLED_SPEC)
	C:\Users\zzzle\go\bin\ogen.exe --target `$(API_DIR) --package api --clean `$(BUNDLED_SPEC)
	@echo "OK Generated!"

clean:
	del /Q `$(BUNDLED_SPEC) 2>NUL
	del /Q `$(API_DIR)\oas_*_gen.go 2>NUL
"@

Set-Content -Path "$ServicePath\Makefile" -Value $makefileTemplate

# Step 2: Copy server structure if missing
if (!(Test-Path "$ServicePath\server")) {
    Write-Host "2️⃣ Creating server structure..." -ForegroundColor Yellow
    Copy-Item -Path "$TemplatePath\server" -Destination "$ServicePath\server" -Recurse -Force
    
    # Update imports
    (Get-Content "$ServicePath\server\*.go") | 
        ForEach-Object { $_ -replace "combat-actions-service-go", $ServiceName } |
        Set-Content "$ServicePath\server\*.go"
}

# Step 3: Copy main.go if missing
if (!(Test-Path "$ServicePath\main.go")) {
    Write-Host "3️⃣ Creating main.go..." -ForegroundColor Yellow
    Copy-Item -Path "$TemplatePath\main.go" -Destination "$ServicePath\main.go"
    
    (Get-Content "$ServicePath\main.go") |
        ForEach-Object { $_ -replace "combat-actions-service-go", $ServiceName } |
        ForEach-Object { $_ -replace ":8084", ":8090" } |
        ForEach-Object { $_ -replace "Combat Actions", "Combat Service" } |
        Set-Content "$ServicePath\main.go"
}

# Step 4: Copy go.mod if missing  
if (!(Test-Path "$ServicePath\go.mod")) {
    Write-Host "4️⃣ Creating go.mod..." -ForegroundColor Yellow
    Copy-Item -Path "$TemplatePath\go.mod" -Destination "$ServicePath\go.mod"
    
    (Get-Content "$ServicePath\go.mod") |
        ForEach-Object { $_ -replace "combat-actions-service-go", $ServiceName } |
        Set-Content "$ServicePath\go.mod"
}

Write-Host ""
Write-Host "OK Template files ready!" -ForegroundColor Green
Write-Host ""
Write-Host "Next: Run these commands manually:" -ForegroundColor Yellow
Write-Host "  cd $ServicePath" -ForegroundColor White
Write-Host "  npx --yes @redocly/cli bundle ../../proto/openapi/{spec}.yaml -o openapi-bundled.yaml" -ForegroundColor White
Write-Host "  C:\Users\zzzle\go\bin\ogen.exe --target pkg/api --package api --clean openapi-bundled.yaml" -ForegroundColor White
Write-Host "  go mod tidy" -ForegroundColor White
Write-Host "  go build ." -ForegroundColor White

