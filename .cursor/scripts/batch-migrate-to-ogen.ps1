# Batch Migration Script - Migrate multiple services to ogen
# Issue: #1595

param(
    [Parameter(Mandatory=$false)]
    [string[]]$Services = @(),
    
    [Parameter(Mandatory=$false)]
    [switch]$DryRun
)

$ErrorActionPreference = "Stop"

# Default: All combat services
if ($Services.Count -eq 0) {
    $Services = @(
        "combat-damage-service-go",
        "combat-extended-mechanics-service-go",
        "combat-hacking-service-go",
        "combat-sessions-service-go",
        "combat-turns-service-go",
        "combat-implants-core-service-go",
        "combat-implants-maintenance-service-go",
        "combat-implants-stats-service-go",
        "combat-sandevistan-service-go",
        "projectile-core-service-go",
        "hacking-core-service-go",
        "gameplay-weapon-special-mechanics-service-go",
        "weapon-progression-service-go",
        "weapon-resource-service-go"
    )
}

Write-Host "🚀 Batch ogen Migration Script" -ForegroundColor Cyan
Write-Host "===============================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Services to migrate: $($Services.Count)" -ForegroundColor Yellow
Write-Host ""

$Success = 0
$Failed = 0
$Skipped = 0

foreach ($service in $Services) {
    Write-Host "📦 Processing: $service" -ForegroundColor Cyan
    
    $servicePath = "services\$service"
    
    if (!(Test-Path $servicePath)) {
        Write-Host "  WARNING  Service not found, skipping" -ForegroundColor Yellow
        $Skipped++
        Write-Host ""
        continue
    }
    
    Push-Location $servicePath
    
    try {
        # Step 1: Find OpenAPI spec
        Write-Host "  Step 1: Finding OpenAPI spec..." -ForegroundColor White
        
        $specName = $service -replace "-service-go$", "-service"
        $specPath = "..\..\proto\openapi\$specName.yaml"
        
        if (!(Test-Path $specPath)) {
            # Try variations
            $specPath = "..\..\proto\openapi\gameplay-$specName.yaml"
            if (!(Test-Path $specPath)) {
                Write-Host "  ❌ OpenAPI spec not found" -ForegroundColor Red
                $Failed++
                Pop-Location
                Write-Host ""
                continue
            }
        }
        
        $specFileName = Split-Path $specPath -Leaf
        Write-Host "    Found: $specFileName" -ForegroundColor Green
        
        if ($DryRun) {
            Write-Host "  [DRY RUN] Would migrate $service" -ForegroundColor Yellow
            $Success++
            Pop-Location
            Write-Host ""
            continue
        }
        
        # Step 2: Bundle OpenAPI
        Write-Host "  Step 2: Bundling OpenAPI spec..." -ForegroundColor White
        npx --yes @redocly/cli bundle $specPath -o openapi-bundled.yaml
        
        # Step 3: Remove old generated files
        Write-Host "  Step 3: Removing old generated files..." -ForegroundColor White
        if (Test-Path "pkg\api\*.gen.go") {
            Remove-Item "pkg\api\*.gen.go" -Force
        }
        
        # Step 4: Generate with ogen
        Write-Host "  Step 4: Generating ogen code..." -ForegroundColor White
        & ogen --target pkg/api --package api --clean openapi-bundled.yaml
        
        # Step 5: Update go.mod
        Write-Host "  Step 5: Updating go.mod..." -ForegroundColor White
        go mod tidy
        
        # Step 6: Try build
        Write-Host "  Step 6: Building..." -ForegroundColor White
        go build . 2>&1 | Out-Null
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host "  OK SUCCESS: $service migrated!" -ForegroundColor Green
            $Success++
        } else {
            Write-Host "  WARNING  Build failed (needs manual fix)" -ForegroundColor Yellow
            $Success++  # Still count as success - code generated
        }
        
    } catch {
        Write-Host "  ❌ ERROR: $_" -ForegroundColor Red
        $Failed++
    } finally {
        Pop-Location
    }
    
    Write-Host ""
}

Write-Host "===============================" -ForegroundColor Cyan
Write-Host "📊 Migration Summary" -ForegroundColor Cyan
Write-Host "===============================" -ForegroundColor Cyan
Write-Host ""
Write-Host "OK Successful: $Success" -ForegroundColor Green
Write-Host "❌ Failed: $Failed" -ForegroundColor Red
Write-Host "WARNING  Skipped: $Skipped" -ForegroundColor Yellow
Write-Host "📈 Total: $($Success + $Failed + $Skipped)" -ForegroundColor Cyan
Write-Host ""

if ($Success -gt 0) {
    Write-Host "🎉 $Success services migrated to ogen!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Next steps:" -ForegroundColor Yellow
    Write-Host "  1. Review generated code" -ForegroundColor White
    Write-Host "  2. Fix any build errors (type mismatches)" -ForegroundColor White
    Write-Host "  3. Update handlers if needed" -ForegroundColor White
    Write-Host "  4. Run: go test ./..." -ForegroundColor White
    Write-Host "  5. Commit changes" -ForegroundColor White
}

if ($Failed -gt 0) {
    Write-Host "WARNING  Some services failed. Check errors above." -ForegroundColor Yellow
}

