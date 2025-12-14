# Issue: #1860
# Test CI/CD setup script functionality

Write-Host "Testing CI/CD setup functionality..." -ForegroundColor Green

# Check if quality-gates.yml exists
$qualityGatesPath = ".github/workflows/quality-gates.yml"
if (Test-Path $qualityGatesPath) {
    Write-Host "✓ CI/CD workflow file exists: $qualityGatesPath" -ForegroundColor Green

    # Check content
    $content = Get-Content $qualityGatesPath -Raw
    if ($content -match "architecture-validator") {
        Write-Host "✓ Workflow includes architecture validation" -ForegroundColor Green
    } else {
        Write-Host "✗ Workflow missing architecture validation" -ForegroundColor Red
    }

    if ($content -match "openapi-validator") {
        Write-Host "✓ Workflow includes OpenAPI validation" -ForegroundColor Green
    } else {
        Write-Host "✗ Workflow missing OpenAPI validation" -ForegroundColor Red
    }

    if ($content -match "file-structure-validator") {
        Write-Host "✓ Workflow includes file structure validation" -ForegroundColor Green
    } else {
        Write-Host "✗ Workflow missing file structure validation" -ForegroundColor Red
    }
}
} else {
    Write-Host "✗ CI/CD workflow file not found" -ForegroundColor Red
}

# Check if dependabot config exists
$dependabotPath = ".github/dependabot.yml"
if (Test-Path $dependabotPath) {
    Write-Host "✓ Dependabot configuration exists" -ForegroundColor Green
} else {
    Write-Host "✗ Dependabot configuration missing" -ForegroundColor Red
}

# Check if golangci-lint config exists
$golangciPath = ".golangci.yml"
if (Test-Path $golangciPath) {
    Write-Host "✓ GolangCI-Lint configuration exists" -ForegroundColor Green
} else {
    Write-Host "✗ GolangCI-Lint configuration missing" -ForegroundColor Red
}

# Test that scripts are executable (conceptually)
$scripts = @(
    "scripts/architecture-validator.py",
    "scripts/file-structure-validator.py",
    "scripts/openapi-validator.py"
)

foreach ($script in $scripts) {
    if (Test-Path $script) {
        Write-Host "✓ Script exists: $script" -ForegroundColor Green
    } else {
        Write-Host "✗ Script missing: $script" -ForegroundColor Red
    }
}

Write-Host "`nCI/CD setup test completed!" -ForegroundColor Blue