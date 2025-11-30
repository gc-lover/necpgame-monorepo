# Issue: Получение результатов CI backend через GitHub CLI
# Скрипт для получения и сохранения результатов CI backend в проект

param(
    [int]$Limit = 10,
    [string]$OutputDir = "reports"
)

$ErrorActionPreference = "Stop"

# Add GitHub CLI to PATH if not already there
$ghPath = "C:\Program Files\GitHub CLI"
if (Test-Path "$ghPath\gh.exe") {
    $env:PATH = "$ghPath;$env:PATH"
}

Write-Host "Getting Backend CI results..." -ForegroundColor Green

# Create output directory
if (-not (Test-Path $OutputDir)) {
    New-Item -ItemType Directory -Path $OutputDir | Out-Null
}

# Get list of recent workflow runs
Write-Host "Getting list of last $Limit workflow runs..." -ForegroundColor Yellow
$runsJson = gh run list --workflow="Backend CI" --limit $Limit --json databaseId,status,conclusion,createdAt,updatedAt,headBranch,headSha,displayTitle,url,event,workflowName
$runs = $runsJson | ConvertFrom-Json

if ($runs.Count -eq 0) {
    Write-Host "No workflow runs found for Backend CI" -ForegroundColor Red
    exit 1
}

Write-Host "Found $($runs.Count) workflow runs" -ForegroundColor Green

# Create structure to store all data
$allResults = @{
    generatedAt = (Get-Date -Format "yyyy-MM-ddTHH:mm:ssZ")
    totalRuns = $runs.Count
    runs = @()
}

# Process each run
foreach ($run in $runs) {
    Write-Host "Processing run #$($run.databaseId) ($($run.headSha.Substring(0, 7)))..." -ForegroundColor Cyan
    
    # Get detailed information about run (including jobs)
    $runDetails = gh run view $run.databaseId --json databaseId,status,conclusion,createdAt,updatedAt,headBranch,headSha,displayTitle,url,event,workflowName,workflowDatabaseId,number,attempt,startedAt,jobs
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Error getting details for run #$($run.databaseId)" -ForegroundColor Red
        continue
    }
    
    $runDetailsObj = $runDetails | ConvertFrom-Json
    
    # Jobs are already included in runDetails
    Write-Host "  Found $($runDetailsObj.jobs.Count) jobs for run #$($run.databaseId)..." -ForegroundColor Gray
    $jobs = if ($runDetailsObj.jobs) { $runDetailsObj.jobs } else { @() }
    
    # Create object with full run information
    $runInfo = @{
        id = $runDetailsObj.databaseId
        status = $runDetailsObj.status
        conclusion = $runDetailsObj.conclusion
        createdAt = $runDetailsObj.createdAt
        updatedAt = $runDetailsObj.updatedAt
        runStartedAt = if ($runDetailsObj.startedAt) { $runDetailsObj.startedAt } else { $runDetailsObj.createdAt }
        runFinishedAt = if ($runDetailsObj.runFinishedAt) { $runDetailsObj.runFinishedAt } else { $runDetailsObj.updatedAt }
        runNumber = $runDetailsObj.number
        runAttempt = $runDetailsObj.attempt
        headBranch = $runDetailsObj.headBranch
        headSha = $runDetailsObj.headSha
        shortSha = $runDetailsObj.headSha.Substring(0, 7)
        displayTitle = $runDetailsObj.displayTitle
        event = $runDetailsObj.event
        workflowName = $runDetailsObj.workflowName
        url = $runDetailsObj.url
        workflowUrl = $runDetailsObj.url
        logsUrl = "$($runDetailsObj.url)/logs"
        jobsUrl = "$($runDetailsObj.url)/jobs"
        actor = @{ login = "unknown"; avatarUrl = "" }
        jobs = $jobs
        jobsSummary = @{
            total = $jobs.Count
            success = ($jobs | Where-Object { $_.conclusion -eq "success" }).Count
            failure = ($jobs | Where-Object { $_.conclusion -eq "failure" }).Count
            cancelled = ($jobs | Where-Object { $_.conclusion -eq "cancelled" }).Count
            in_progress = ($jobs | Where-Object { $_.status -eq "in_progress" }).Count
            queued = ($jobs | Where-Object { $_.status -eq "queued" }).Count
        }
    }
    
    $allResults.runs += $runInfo
}

# Save results to JSON file
$timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
$outputFile = Join-Path $OutputDir "backend-ci-results_$timestamp.json"

Write-Host "Saving results to $outputFile..." -ForegroundColor Green
$allResults | ConvertTo-Json -Depth 10 | Out-File -FilePath $outputFile -Encoding UTF8

# Also create a brief summary in Markdown
$summaryFile = Join-Path $OutputDir "backend-ci-summary_$timestamp.md"
Write-Host "Creating summary in $summaryFile..." -ForegroundColor Green

$summary = @"
# Backend CI Results Summary

**Generated:** $($allResults.generatedAt)  
**Total Runs:** $($allResults.totalRuns)

## Runs Overview

"@

foreach ($run in $allResults.runs) {
    $statusIcon = switch ($run.conclusion) {
        "success" { "OK" }
        "failure" { "❌" }
        "cancelled" { "🚫" }
        default { "⏳" }
    }
    
    $summary += @"

### $statusIcon Run #$($run.runNumber) - $($run.shortSha)

- **Status:** $($run.status) / $($run.conclusion)
- **Branch:** $($run.headBranch)
- **Commit:** [$($run.shortSha)]($($run.url))
- **Title:** $($run.displayTitle)
- **Created:** $($run.createdAt)
- **Finished:** $($run.runFinishedAt)
- **Event:** $($run.event)

**Jobs Summary:**
- Total: $($run.jobsSummary.total)
- OK Success: $($run.jobsSummary.success)
- ❌ Failure: $($run.jobsSummary.failure)
- 🚫 Cancelled: $($run.jobsSummary.cancelled)
- ⏳ In Progress: $($run.jobsSummary.in_progress)
- 📋 Queued: $($run.jobsSummary.queued)

**Jobs:**
"@
    
    foreach ($job in $run.jobs) {
        $jobIcon = switch ($job.conclusion) {
            "success" { "OK" }
            "failure" { "❌" }
            "cancelled" { "🚫" }
            default { "⏳" }
        }
        
        $summary += @"
- $jobIcon **$($job.name)** - $($job.status) / $($job.conclusion)
"@
        
        # Steps are not included to speed up the script
    }
    
    $summary += "`n"
}

$summary | Out-File -FilePath $summaryFile -Encoding UTF8

Write-Host "`nDone! Results saved:" -ForegroundColor Green
Write-Host "  - JSON: $outputFile" -ForegroundColor Cyan
Write-Host "  - Summary: $summaryFile" -ForegroundColor Cyan

