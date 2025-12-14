# NECPGAME Pre-commit Hook (PowerShell)
# Runs architecture validation before allowing commits

Write-Host "🔍 Running NECPGAME Architecture Validation..." -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan

# Check if validation script exists
if (-not (Test-Path "scripts/validate-architecture.sh")) {
    Write-Host "WARNING  Validation script not found, skipping validation" -ForegroundColor Yellow
    exit 0
}

# Run validation (assuming bash is available via WSL or Git Bash)
try {
    & bash scripts/validate-architecture.sh
    $exitCode = $LASTEXITCODE
} catch {
    Write-Host "WARNING  Could not run bash validation script" -ForegroundColor Yellow
    exit 0
}

# Check exit code
if ($exitCode -ne 0) {
    Write-Host ""
    Write-Host "❌ Commit blocked due to validation errors" -ForegroundColor Red
    Write-Host "Please fix the issues and try again" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "OK All validations passed - proceeding with commit" -ForegroundColor Green
exit 0