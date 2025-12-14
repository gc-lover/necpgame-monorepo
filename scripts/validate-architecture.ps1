# NECPGAME Architecture Validation Script (PowerShell)
# Validates all architectural aspects

param(
    [string]$Check = "all"
)

Write-Host "🔍 Starting NECPGAME Architecture Validation..." -ForegroundColor Cyan
Write-Host ("=" * 50) -ForegroundColor Cyan

$errors = 0
$warnings = 0

function Log-Error {
    param([string]$message)
    Write-Host "❌ ERROR: $message" -ForegroundColor Red
    $script:errors++
}

function Log-Warning {
    param([string]$message)
    Write-Host "WARNING  WARNING: $message" -ForegroundColor Yellow
    $script:warnings++
}

function Log-Success {
    param([string]$message)
    Write-Host "OK $message" -ForegroundColor Green
}

# 1. Check file sizes (max 600 lines)
if ($Check -eq "all" -or $Check -eq "file-sizes") {
    Write-Host ""
    Write-Host "📏 Checking file sizes..."

    $filesToCheck = Get-ChildItem -Recurse -Include "*.yaml", "*.go", "*.sql", "*.md" |
        Where-Object { $_.FullName -notmatch '\\(\.git|node_modules|vendor)\\' }

    foreach ($file in $filesToCheck) {
        try {
            $lines = (Get-Content $file.FullName | Measure-Object -Line).Lines
            if ($lines -gt 600) {
                Log-Error "File $($file.Name) exceeds 600 lines ($lines lines)"
            }
        } catch {
            Log-Warning "Could not read file $($file.Name)"
        }
    }
}

# 2. Check for basic structure
if ($Check -eq "all" -or $Check -eq "structure") {
    Write-Host ""
    Write-Host "🏗️  Checking project structure..."

    $requiredDirs = @("proto/openapi", "services", "knowledge", "infrastructure")
    foreach ($dir in $requiredDirs) {
        if (Test-Path $dir) {
            Log-Success "Directory $dir exists"
        } else {
            Log-Error "Required directory $dir missing"
        }
    }
}

# 3. Check YAML files for metadata
if ($Check -eq "all" -or $Check -eq "yaml") {
    Write-Host ""
    Write-Host "📄 Checking YAML files..."

    $yamlFiles = Get-ChildItem -Recurse -Include "*.yaml" |
        Where-Object { $_.FullName -match '\\knowledge\\' }

    foreach ($file in $yamlFiles) {
        $content = Get-Content $file.FullName -Raw

        if (-not ($content -match "^metadata:")) {
            Log-Warning "YAML file $($file.Name) missing metadata section"
        }

        if (-not ($content -match "# Issue:")) {
            Log-Warning "YAML file $($file.Name) missing Issue reference"
        }
    }
}

# 4. Security checks
if ($Check -eq "all" -or $Check -eq "security") {
    Write-Host ""
    Write-Host "🔒 Checking security patterns..."

    $codeFiles = Get-ChildItem -Recurse -Include "*.go", "*.yaml" |
        Where-Object { $_.FullName -notmatch '\\(\.git|node_modules|vendor)\\' }

    foreach ($file in $codeFiles) {
        $content = Get-Content $file.FullName -Raw

        # Check for potential hardcoded secrets
        if ($content -match 'password.*=.*["''][^$]') {
            Log-Error "Potential hardcoded password in $($file.Name)"
        }
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
    Write-Host ""
    Write-Host "❌ VALIDATION FAILED: $errors errors found" -ForegroundColor Red
    Write-Host "Please fix all errors before committing" -ForegroundColor Red
    exit 1
} elseif ($warnings -gt 0) {
    Write-Host ""
    Write-Host "WARNING  VALIDATION PASSED WITH WARNINGS: $warnings warnings" -ForegroundColor Yellow
    Write-Host "Consider fixing warnings for better code quality" -ForegroundColor Yellow
    exit 0
} else {
    Write-Host ""
    Write-Host "OK VALIDATION PASSED: No errors or warnings" -ForegroundColor Green
    exit 0
}