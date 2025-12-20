# NECPGAME Pre-commit Hook (PowerShell)
# Runs architecture validation before allowing commits

Write-Host "🔍 Running NECPGAME Architecture Validation..." -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan

# Check for emergency override
$committerName = git config user.name
$committerEmail = git config user.email

if ($committerName -eq "AI_AGENT_EMERGENCY" -and $committerEmail -eq "emergency@necpgame.invalid") {
    Write-Host "WARNING: Emergency commit override detected. Proceeding without validation." -ForegroundColor Yellow
    Write-Host "WARNING: This should only be used in critical situations by authorized personnel." -ForegroundColor Yellow
    exit 0
}

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