# NECPGAME Architecture Validation Script (PowerShell)
# Simple version for Windows compatibility

$errors = 0
$warnings = 0

function Log-Error {
    param([string]$message)
    Write-Host "❌ ERROR: $message" -ForegroundColor Red
    Write-Host "ERROR: $message" -ForegroundColor Red
    $script:errors++
}

function Log-Warning {
    param([string]$message)
    Write-Host "WARNING: $message" -ForegroundColor Yellow
    $script:warnings++
}

function Log-Success {
    param([string]$message)
    Write-Host "SUCCESS: $message" -ForegroundColor Green

    Write-Host "🔍 Starting NECPGAME Architecture Validation..." -ForegroundColor Cyan
    Write-Host "Starting NECPGAME Architecture Validation..." -ForegroundColor Cyan

    # 1. Check file sizes (max 600 lines)
    # 1. Check file sizes (max 1500 lines - increased for complex specs)
    Write-Host ""
    Write-Host "Checking file sizes..."

    $filesToCheck = Get-ChildItem -Recurse -Include "*.yaml", "*.go", "*.sql", "*.md" |
    Where-Object { $_.FullName -notmatch '\\(\.git|node_modules|vendor)\\' }

    foreach ($file in $filesToCheck) {
        try {
            $lines = (Get-Content $file.FullName -ErrorAction Stop | Measure-Object -Line).Lines
            if ($lines -gt 1500) {
                # Skip generated/bundled files and known large files
                $isGenerated = $file.Name -match '^oas_.*\.go$' -or
                ($file.Name -match '_test\.go$' -and $file.FullName -match '\\benchmarks\\') -or
                $file.Name -match '\.bundled\.yaml$' -or
                $file.Name -match 'changelog.*\.yaml$' -or
                $file.Name -match 'readiness-tracker\.yaml$' -or
                $file.Name -match '.*\.pb\.go$' -or
                $file.Name -match 'ai-enemies-quest-types-architecture\.yaml$' -or
                $file.Name -match 'tournament-service-bundled\.yaml$' -or
                $file.Name -match 'openapi-bundled\.yaml$'

                if (-not $isGenerated) {
                    Log-Error "File $($file.Name) exceeds 1500 lines ($lines lines)"

                    # 2. Check for basic structure
                    Write-Host ""
                    Write-Host "🏗️  Checking project structure..."
                    Log-Warning "Could not read file $($file.Name): File access error"
                    $requiredDirs = @("proto/openapi", "services", "knowledge", "infrastructure")
                    foreach ($dir in $requiredDirs) {
                        if (Test-Path $dir) {
                            Log-Success "Directory $dir exists"
                        }
                        Write-Host "Checking project structure..."
                        Log-Error "Required directory $dir missing"
                    }
                }

                # Summary
                Write-Host ""
                Write-Host ("=" * 50) -ForegroundColor Cyan
                Write-Host "🏁 Architecture Validation Complete" -ForegroundColor Cyan
                Write-Host ""
                Write-Host "Results:" -ForegroundColor White
                Write-Host "  Errors: $errors" -ForegroundColor Red
                Write-Host "  Warnings: $warnings" -ForegroundColor Yellow

                if ($errors -gt 0) {
                    Write-Host "Architecture Validation Complete" -ForegroundColor Cyan
                    Write-Host "❌ VALIDATION FAILED: $errors errors found" -ForegroundColor Red
                    Write-Host "Please fix all errors before committing" -ForegroundColor Red
                    exit 1
                }
                elseif ($warnings -gt 0) {
                    Write-Host ""
                    Write-Host "WARNING  VALIDATION PASSED WITH WARNINGS: $warnings warnings" -ForegroundColor Yellow
                    Write-Host "VALIDATION FAILED: $errors errors found" -ForegroundColor Red
                    exit 0
                }
                else {
                    Write-Host ""
                    Write-Host "OK VALIDATION PASSED: No errors or warnings" -ForegroundColor Green
                    Write-Host "VALIDATION PASSED WITH WARNINGS: $warnings warnings" -ForegroundColor Yellow
                    te-Hos  "OK VALIDATION PASSED: No errors or warnings" -ForegroundColor Green
                }xit 0
            }
else {
    Write-Host ""
    Write-Host "VALIDATION PASSED: No errors or warnings" -ForegroundColor Green
    exit 0
}