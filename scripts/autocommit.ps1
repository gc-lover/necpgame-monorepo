# Auto commit helper for agents
param(
    [string]$CommitMessage = ""
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$OutputEncoding = [System.Text.Encoding]::UTF8

$repoRoot = git rev-parse --show-toplevel 2>$null
if (-not $repoRoot) {
    Write-Host "Error: git repository not found" -ForegroundColor Red
    exit 1
}

Set-Location $repoRoot

$status = git status --porcelain
if (-not $status) {
    Write-Host "No changes to commit" -ForegroundColor Yellow
    exit 0
}

Write-Host "Staging changes..." -ForegroundColor Cyan
git add -A

if ([string]::IsNullOrWhiteSpace($CommitMessage)) {
    $changedFiles = git diff --cached --name-only
    if ($changedFiles) {
        $fileCount = ($changedFiles | Measure-Object).Count
        $CommitMessage = "Auto commit (${fileCount} files)"
    } else {
        $CommitMessage = "Auto commit"
    }
}

Write-Host "Committing: $CommitMessage" -ForegroundColor Cyan
$commitResult = git commit -m "$CommitMessage" 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "Commit failed: $commitResult" -ForegroundColor Red
    exit 1
}
Write-Host "Commit created" -ForegroundColor Green

$currentBranch = git rev-parse --abbrev-ref HEAD 2>$null
if (-not $currentBranch) {
    Write-Host "Warning: unable to detect branch, using 'main'" -ForegroundColor Yellow
    $currentBranch = "main"
}

Write-Host "Pushing to origin/$currentBranch..." -ForegroundColor Cyan
$pushResult = git push origin "$currentBranch" 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "Warning: push failed: $pushResult" -ForegroundColor Yellow
    Write-Host "Changes were committed locally only" -ForegroundColor Yellow
} else {
    Write-Host "Push completed" -ForegroundColor Green
}

exit 0
