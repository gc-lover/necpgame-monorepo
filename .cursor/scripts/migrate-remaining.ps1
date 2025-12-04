# Migrate remaining Low Priority services
# Issues: #1600, #1601, #1602

$env:Path = "C:\Program Files\Go\bin;C:\Users\zzzle\go\bin;C:\Program Files\Git\cmd;C:\Program Files\nodejs;" + $env:Path

$services = @(
    # Character Engram (#1600)
    "character-engram-compatibility-service-go",
    "character-engram-core-service-go",
    "character-engram-cyberpsychosis-service-go",
    "character-engram-historical-service-go",
    "character-engram-security-service-go",
    
    # Stock/Economy (#1601)
    "stock-analytics-charts-service-go",
    "stock-analytics-tools-service-go",
    "stock-dividends-service-go",
    "stock-events-service-go",
    "stock-futures-service-go",
    "stock-indices-service-go",
    "stock-integration-service-go",
    "stock-margin-service-go",
    "stock-options-service-go",
    "stock-protection-service-go",
    "economy-service-go",
    "trade-service-go",
    
    # Admin & Support (#1602)
    "admin-service-go",
    "support-service-go",
    "maintenance-service-go",
    "feedback-service-go",
    "clan-war-service-go",
    "faction-core-service-go",
    "reset-service-go",
    "client-service-go",
    "realtime-gateway-go",
    "ws-lobby-go",
    "voice-chat-service-go"
)

$success = 0
$total = $services.Count

Write-Host "🚀 Migrating Low Priority Services" -ForegroundColor Cyan
Write-Host "Total: $total services" -ForegroundColor Yellow
Write-Host ""

foreach ($service in $services) {
    $current = $success + 1
    Write-Host "[$current/$total] $service" -ForegroundColor Cyan
    
    Push-Location "services\$service" -ErrorAction SilentlyContinue
    
    if (!$?) {
        Write-Host "  WARNING  Not found" -ForegroundColor Yellow
        Pop-Location -ErrorAction SilentlyContinue
        continue
    }
    
    try {
        $specName = $service -replace "-service-go$", "-service"
        $specPath = "..\..\proto\openapi\$specName.yaml"
        
        if (!(Test-Path $specPath)) {
            $specPath = "..\..\proto\openapi\gameplay-$specName.yaml"
        }
        
        if (!(Test-Path $specPath)) {
            Write-Host "  WARNING  Spec not found" -ForegroundColor Yellow
            Pop-Location
            continue
        }
        
        # Generate
        npx --yes @redocly/cli bundle $specPath -o openapi-bundled.yaml 2>&1 | Out-Null
        Remove-Item pkg\api\*.gen.go -Force -ErrorAction SilentlyContinue
        ogen --target pkg/api --package api --clean openapi-bundled.yaml 2>&1 | Out-Null
        
        if (Test-Path "pkg\api\oas_server_gen.go") {
            $actualSpec = (Split-Path $specPath -Leaf) -replace ".yaml$", ""
            
            $makefile = @"
# Issue: #1600-#1602
# Makefile for ogen code generation

.PHONY: generate-api clean

SERVICE_NAME := $actualSpec
SPEC_DIR := ../../proto/openapi
BUNDLED_SPEC := openapi-bundled.yaml
API_DIR := pkg/api

generate-api:
	npx --yes @redocly/cli bundle `$(SPEC_DIR)/`$(SERVICE_NAME).yaml -o `$(BUNDLED_SPEC)
	ogen --target `$(API_DIR) --package api --clean `$(BUNDLED_SPEC)

clean:
	rm -f `$(BUNDLED_SPEC)
	rm -rf `$(API_DIR)/oas_*_gen.go
"@
            Set-Content -Path "Makefile" -Value $makefile
            Write-Host "  OK" -ForegroundColor Green
            $success++
        } else {
            Write-Host "  ❌ Gen failed" -ForegroundColor Red
        }
        
    } catch {
        Write-Host "  ❌ Error" -ForegroundColor Red
    } finally {
        Pop-Location
    }
}

Write-Host ""
Write-Host "OK Success: $success/$total" -ForegroundColor Green

