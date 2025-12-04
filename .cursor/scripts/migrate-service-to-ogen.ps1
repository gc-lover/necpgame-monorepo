# ogen Migration Script
# Migrates a single service from oapi-codegen to ogen

param(
    [Parameter(Mandatory=$true)]
    [string]$ServiceName,
    
    [Parameter(Mandatory=$false)]
    [switch]$DryRun
)

$ErrorActionPreference = "Stop"

$ServicePath = "services\$ServiceName"
$ReferenceService = "services\combat-combos-service-ogen-go"

Write-Host "`n🚀 ogen Migration Script" -ForegroundColor Cyan
Write-Host "Service: $ServiceName" -ForegroundColor Yellow
if ($DryRun) {
    Write-Host "Mode: DRY RUN (no changes)" -ForegroundColor Yellow
}
Write-Host ""

# Validate service exists
if (-not (Test-Path $ServicePath)) {
    Write-Host "❌ Service not found: $ServicePath" -ForegroundColor Red
    exit 1
}

# Check if already migrated
if (Test-Path "$ServicePath\MIGRATION_SUMMARY.md") {
    Write-Host "✅ Service already migrated!" -ForegroundColor Green
    exit 0
}

# Validate reference service
if (-not (Test-Path $ReferenceService)) {
    Write-Host "❌ Reference service not found: $ReferenceService" -ForegroundColor Red
    exit 1
}

Write-Host "📋 Pre-flight checks..." -ForegroundColor Cyan

# Check dependencies
Write-Host "  Checking ogen..."
$ogen = Get-Command ogen -ErrorAction SilentlyContinue
if (-not $ogen) {
    Write-Host "❌ ogen not found. Install: go install github.com/ogen-go/ogen/cmd/ogen@latest" -ForegroundColor Red
    exit 1
}
Write-Host "  ✅ ogen: $($ogen.Version)" -ForegroundColor Green

# Check node
Write-Host "  Checking node..."
$node = Get-Command node -ErrorAction SilentlyContinue
if (-not $node) {
    Write-Host "❌ node not found" -ForegroundColor Red
    exit 1
}
Write-Host "  ✅ node installed" -ForegroundColor Green

Write-Host "`n📦 Step 1: Backup current service" -ForegroundColor Cyan
if (-not $DryRun) {
    $BackupPath = "$ServicePath-backup-$(Get-Date -Format 'yyyyMMdd-HHmmss')"
    Copy-Item -Path $ServicePath -Destination $BackupPath -Recurse
    Write-Host "  ✅ Backup: $BackupPath" -ForegroundColor Green
} else {
    Write-Host "  [DRY RUN] Would create backup" -ForegroundColor Yellow
}

Write-Host "`n📝 Step 2: Update Makefile" -ForegroundColor Cyan
$MakefilePath = "$ServicePath\Makefile"
if (Test-Path $MakefilePath) {
    if (-not $DryRun) {
        # Copy reference Makefile structure
        $ReferenceMakefile = Get-Content "$ReferenceService\Makefile" -Raw
        
        # Extract SERVICE_NAME from current Makefile
        $CurrentMakefile = Get-Content $MakefilePath -Raw
        if ($CurrentMakefile -match 'SERVICE_NAME\s*[:=]\s*([a-z-]+)') {
            $ExtractedServiceName = $Matches[1]
        } else {
            $ExtractedServiceName = $ServiceName -replace '-service-go$', '-service'
        }
        
        # Replace in reference Makefile
        $NewMakefile = $ReferenceMakefile -replace 'combat-combos-service', $ExtractedServiceName
        
        # Write new Makefile
        Set-Content -Path $MakefilePath -Value $NewMakefile
        Write-Host "  ✅ Makefile updated for ogen" -ForegroundColor Green
    } else {
        Write-Host "  [DRY RUN] Would update Makefile" -ForegroundColor Yellow
    }
} else {
    Write-Host "  ⚠️  Makefile not found" -ForegroundColor Yellow
}

Write-Host "`n🔧 Step 3: Generate ogen code" -ForegroundColor Cyan
if (-not $DryRun) {
    Push-Location $ServicePath
    
    Write-Host "  Running: make generate-api" -ForegroundColor Gray
    $GenerateOutput = & make generate-api 2>&1
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "  ✅ Code generated successfully" -ForegroundColor Green
        
        # Count generated files
        $GenFiles = Get-ChildItem "pkg\api\oas_*_gen.go" -ErrorAction SilentlyContinue
        Write-Host "  📦 Generated: $($GenFiles.Count) files" -ForegroundColor Cyan
    } else {
        Write-Host "  ❌ Generation failed" -ForegroundColor Red
        Write-Host $GenerateOutput
        Pop-Location
        exit 1
    }
    
    Pop-Location
} else {
    Write-Host "  [DRY RUN] Would run: make generate-api" -ForegroundColor Yellow
}

Write-Host "`n✅ Migration structure complete!" -ForegroundColor Green
Write-Host ""
Write-Host "📋 Next steps (MANUAL):" -ForegroundColor Cyan
Write-Host "  1. Update handlers in server\ to implement ogen interfaces" -ForegroundColor Yellow
Write-Host "  2. Update server\http_server.go for ogen server setup" -ForegroundColor Yellow
Write-Host "  3. Ensure server\security.go implements SecurityHandler" -ForegroundColor Yellow
Write-Host "  4. Run: cd $ServicePath && go build ./..." -ForegroundColor Yellow
Write-Host "  5. Run: go test ./..." -ForegroundColor Yellow
Write-Host "  6. Run: go test -bench=. -benchmem ./server" -ForegroundColor Yellow
Write-Host ""
Write-Host "📚 Reference:" -ForegroundColor Cyan
Write-Host "  services\combat-combos-service-ogen-go\" -ForegroundColor Gray
Write-Host "  .cursor\ogen\02-MIGRATION-STEPS.md" -ForegroundColor Gray
Write-Host ""


