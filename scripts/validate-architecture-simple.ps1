# Simple Architecture Validation Script for NECPGAME
# PowerShell script for basic architecture compliance checks

param(
    [Parameter(Mandatory=$false)]
    [string]$Path = ".",

    [Parameter(Mandatory=$false)]
    [switch]$Verbose,

    [Parameter(Mandatory=$false)]
    [int]$MaxLines = 1500
)

Write-Host "🔍 NECPGAME Simple Architecture Validation" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# Configuration
$EXCLUDED_PATTERNS = @(
    "*.gen.go",
    "*ogen-generated*",
    "*bundled-openapi*",
    "*.md",
    "*.txt",
    "docs/*",
    "infrastructure/liquibase/migrations/*",
    ".git/*",
    "node_modules/*"
)

$FORBIDDEN_EXTENSIONS = @('.exe', '.dll', '.so', '.dylib')

$errors = 0
$warnings = 0

function Write-VerboseMessage {
    param([string]$Message)
    if ($Verbose) {
        Write-Host "  $Message" -ForegroundColor Gray
    }
}

function Write-Success {
    param([string]$Message)
    Write-Host "✅ $Message" -ForegroundColor Green
}

function Write-Warning {
    param([string]$Message)
    Write-Host "⚠️  $Message" -ForegroundColor Yellow
    $script:warnings++
}

function Write-Error {
    param([string]$Message)
    Write-Host "❌ $Message" -ForegroundColor Red
    $script:errors++
}

# Check if path exists
if (-not (Test-Path $Path)) {
    Write-Error "Path does not exist: $Path"
    exit 1
}

Write-Host "📂 Scanning directory: $Path" -ForegroundColor Blue
Write-Host "📏 Max file size: $MaxLines lines" -ForegroundColor Blue
Write-Host ""

# Get all files recursively
$allFiles = Get-ChildItem -Path $Path -Recurse -File

Write-VerboseMessage "Found $($allFiles.Count) files total"

# Filter out excluded files
$filesToCheck = @()
foreach ($file in $allFiles) {
    $relativePath = $file.FullName.Replace((Get-Location).Path + "\", "").Replace((Get-Location).Path, "")
    $shouldExclude = $false

    foreach ($pattern in $EXCLUDED_PATTERNS) {
        if ($relativePath -like $pattern) {
            $shouldExclude = $true
            break
        }
    }

    if (-not $shouldExclude) {
        $filesToCheck += $file
    }
}

Write-Host "📋 Checking $($filesToCheck.Count) files (excluded patterns applied)" -ForegroundColor Blue
Write-Host ""

# Check 1: File size limits
Write-Host "📏 Checking file sizes..." -ForegroundColor Yellow

foreach ($file in $filesToCheck) {
    try {
        $lineCount = (Get-Content $file.FullName -ErrorAction Stop | Measure-Object -Line).Lines

        if ($lineCount -gt $MaxLines) {
            Write-Warning "File exceeds size limit: $($file.Name) ($lineCount lines > $MaxLines)"
        } else {
            Write-VerboseMessage "OK: $($file.Name) ($lineCount lines)"
        }
    } catch {
        Write-VerboseMessage "Skipped: $($file.Name) (binary or unreadable)"
    }
}

Write-Success "File size check completed"

# Check 2: Forbidden file extensions
Write-Host "🚫 Checking forbidden file extensions..." -ForegroundColor Yellow

foreach ($file in $filesToCheck) {
    $extension = $file.Extension.ToLower()
    if ($FORBIDDEN_EXTENSIONS -contains $extension) {
        Write-Error "Forbidden file extension: $($file.Name) ($extension)"
    }
}

Write-Success "Forbidden extensions check completed"

# Check 3: Basic structural validation
Write-Host "🏗️  Checking basic structure..." -ForegroundColor Yellow

# Check for required directories
$requiredDirs = @(
    "services",
    "proto",
    "infrastructure",
    "scripts",
    "knowledge"
)

foreach ($dir in $requiredDirs) {
    if (-not (Test-Path $dir)) {
        Write-Warning "Missing recommended directory: $dir"
    }
}

# Check for critical files
$criticalFiles = @(
    "docker-compose.yml",
    "README.md"
)

foreach ($file in $criticalFiles) {
    if (-not (Test-Path $file)) {
        Write-Warning "Missing critical file: $file"
    }
}

Write-Success "Basic structure check completed"

# Check 4: Cross-platform compatibility
Write-Host "🌐 Checking cross-platform compatibility..." -ForegroundColor Yellow

$unixPathIssues = 0
$encodingIssues = 0

foreach ($file in $filesToCheck) {
    # Check for Windows-specific paths in scripts
    if ($file.Extension -in @('.sh', '.bash', '.ps1', '.py')) {
        try {
            $content = Get-Content $file.FullName -Raw -ErrorAction Stop
            if ($content -match "C:\\" -or $content -match "\\\\") {
                Write-Warning "Potential Windows path in script: $($file.Name)"
            }
        } catch {
            # Skip binary files
        }
    }
}

if ($unixPathIssues -eq 0) {
    Write-Success "Cross-platform compatibility check completed"
}

# Summary
Write-Host ""
Write-Host "📊 Validation Summary" -ForegroundColor Cyan
Write-Host "===================" -ForegroundColor Cyan

if ($errors -eq 0 -and $warnings -eq 0) {
    Write-Success "All checks passed! ($($filesToCheck.Count) files checked)"
    exit 0
} else {
    Write-Host "Errors: $errors" -ForegroundColor Red
    Write-Host "Warnings: $warnings" -ForegroundColor Yellow

    if ($errors -gt 0) {
        Write-Host ""
        Write-Error "Validation failed! Fix errors before proceeding."
        exit 1
    } else {
        Write-Host ""
        Write-Warning "Validation completed with warnings. Review and fix if needed."
        exit 0
    }
}