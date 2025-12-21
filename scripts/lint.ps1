# ESLint runner script for Windows
# Runs ESLint from the linting directory

param(
    [Parameter(ValueFromRemainingArguments = $true)]
    [string[]]$Args
)

$lintingDir = Join-Path $PSScriptRoot "linting"

if (!(Test-Path $lintingDir)) {
    Write-Error "Error: linting directory not found"
    exit 1
}

Set-Location $lintingDir

if (!(Test-Path "package.json")) {
    Write-Error "Error: package.json not found in linting directory"
    exit 1
}

# Install dependencies if node_modules doesn't exist
if (!(Test-Path "node_modules")) {
    Write-Host "Installing ESLint dependencies..."
    npm install
}

# Run ESLint
Write-Host "Running ESLint..."
npm run lint @Args
