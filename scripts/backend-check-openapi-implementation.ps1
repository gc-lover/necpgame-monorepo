# Backend Check OpenAPI Implementation - Validation Command
# Issue: #146050248

param(
    [string]$ProtoDir = "$PSScriptRoot/../proto/openapi"
)

# Colors
$RED = [char]27 + '[0;31m'
$GREEN = [char]27 + '[0;32m'
$YELLOW = [char]27 + '[1;33m'
$BLUE = [char]27 + '[0;34m'
$NC = [char]27 + '[0m' # No Color

# Check Python
if (-not (Get-Command python3 -ErrorAction SilentlyContinue)) {
    Write-Host "$RED`Error: python3 is required$NC"
    exit 1
}

# Check spectral
if (-not (Get-Command spectral -ErrorAction SilentlyContinue)) {
    Write-Host "$YELLOW`Warning: spectral not found, skipping spectral validation$NC"
    $SPECTRAL_AVAILABLE = $false
}
else {
    $SPECTRAL_AVAILABLE = $true
}

# Check ogen
if (-not (Get-Command ogen -ErrorAction SilentlyContinue)) {
    Write-Host "$YELLOW`Warning: ogen not found, skipping ogen validation$NC"
    $OGEN_AVAILABLE = $false
}
else {
    $OGEN_AVAILABLE = $true
}

Write-Host "$BLUE`🔍 Backend OpenAPI Implementation Validator$NC"
Write-Host "=============================================="
Write-Host ""

$ERRORS = 0
$WARNINGS = 0
$SERVICES_CHECKED = 0

# Find all OpenAPI specs
function Find-OpenAPISpecs {
    Get-ChildItem -Path $ProtoDir -Recurse -Include "*.yaml", "*.yml" |
    Where-Object { $_.FullName -notmatch "notification-service/main\.yaml" } |
    Sort-Object FullName
}

# Validate OpenAPI spec
function Validate-OpenAPISpec {
    param([string]$SpecFile)

    $serviceName = [System.IO.Path]::GetFileNameWithoutExtension($SpecFile)
    Write-Host "$BLUE`📋 Validating: $serviceName$NC"
    $script:SERVICES_CHECKED++

    # Check file size (<600 lines)
    $lines = (Get-Content $SpecFile | Measure-Object -Line).Lines
    if ($lines -gt 600) {
        Write-Host "  $RED❌ File size: $lines lines (exceeds 600 limit)$NC"
        $script:ERRORS++
        return $false
    }

    # Spectral linting
    if ($SPECTRAL_AVAILABLE) {
        Write-Host "  🔍 Running Spectral validation..."
        try {
            $result = & spectral lint $SpecFile --ruleset .spectral.yaml 2>$null
            if ($LASTEXITCODE -ne 0) {
                Write-Host "  $RED❌ Spectral validation failed$NC"
                $script:ERRORS++
                return $false
            }
        }
        catch {
            Write-Host "  $RED❌ Spectral validation failed$NC"
            $script:ERRORS++
            return $false
        }
    }
    else {
        Write-Host "  $YELLOWWARNING  Spectral not available, skipping$NC"
        $script:WARNINGS++
    }

    # ogen compatibility check
    if ($OGEN_AVAILABLE) {
        Write-Host "  🔧 Checking ogen compatibility..."
        try {
            $result = & ogen validate $SpecFile 2>$null
            if ($LASTEXITCODE -ne 0) {
                Write-Host "  $RED❌ ogen validation failed$NC"
                $script:ERRORS++
                return $false
            }
        }
        catch {
            Write-Host "  $RED❌ ogen validation failed$NC"
            $script:ERRORS++
            return $false
        }
    }
    else {
        Write-Host "  $YELLOWWARNING  ogen not available, skipping$NC"
        $script:WARNINGS++
    }

    # Check if corresponding service exists
    $serviceDir = "$PSScriptRoot/../services/${serviceName}-go"
    if (-not (Test-Path $serviceDir)) {
        Write-Host "  $YELLOWWARNING  Warning: Service directory not found: $serviceDir$NC"
        $script:WARNINGS++
    }
    else {
        # Check if ogen generated files exist
        $ogenServerFile = "$serviceDir/pkg/api/oas_server_gen.go"
        if (-not (Test-Path $ogenServerFile)) {
            Write-Host "  $RED❌ ogen generated files not found$NC"
            $script:ERRORS++
            return $false
        }

        # Check if handlers implement the interface
        $handlersFile = "$serviceDir/server/handlers.go"
        if (Test-Path $handlersFile) {
            $content = Get-Content $handlersFile -Raw
            if ($content -notmatch "CreateNotification") {
                Write-Host "  $YELLOWWARNING  Warning: Handler interface may not be fully implemented$NC"
                $script:WARNINGS++
            }
        }

        # Check for performance optimizations
        $serviceFile = "$serviceDir/server/service.go"
        if (Test-Path $serviceFile) {
            $content = Get-Content $serviceFile -Raw

            if ($content -notmatch "context\.WithTimeout") {
                Write-Host "  $YELLOWWARNING  Warning: Context timeouts not found$NC"
                $script:WARNINGS++
            }

            if ($content -notmatch "sync\.Pool") {
                Write-Host "  $YELLOWWARNING  Warning: Memory pooling not found$NC"
                $script:WARNINGS++
            }
        }
    }

    Write-Host "  $GREENOK $serviceName validation passed$NC"
    Write-Host ""
    return $true
}

# Main validation loop
Write-Host "$BLUE🔍 Scanning OpenAPI specifications...$NC"
Write-Host ""

$specs = Find-OpenAPISpecs
foreach ($spec in $specs) {
    Validate-OpenAPISpec -SpecFile $spec.FullName
}

# Summary
Write-Host "=============================================="
Write-Host "$BLUE📊 VALIDATION SUMMARY$NC"
Write-Host "=============================================="
Write-Host ""
Write-Host "Services checked: $SERVICES_CHECKED"
Write-Host "$RED`Errors: $ERRORS$NC"
Write-Host "$YELLOW`Warnings: $WARNINGS$NC"
Write-Host ""

if ($ERRORS -gt 0) {
    Write-Host "$RED❌ VALIDATION FAILED$NC"
    Write-Host ""
    Write-Host "Fix the following issues:"
    Write-Host "- Ensure OpenAPI specs are valid and <600 lines"
    Write-Host "- Run 'ogen generate' for all services"
    Write-Host "- Implement Handler interfaces completely"
    Write-Host "- Add performance optimizations (context timeouts, memory pooling)"
    Write-Host ""
    exit 1
}
elseif ($WARNINGS -gt 0) {
    Write-Host "$YELLOWWARNING  VALIDATION PASSED with warnings$NC"
    Write-Host ""
    Write-Host "Consider addressing warnings for better implementation quality"
    exit 0
}
else {
    Write-Host "$GREENOK ALL VALIDATIONS PASSED$NC"
    Write-Host ""
    Write-Host "All OpenAPI implementations are ready for production!"
    exit 0
}