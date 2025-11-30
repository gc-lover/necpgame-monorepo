# Issue: Получение детальной информации об ошибках CI backend
# Скрипт для получения ошибок из failed jobs

param(
    [int]$Limit = 5,
    [string]$OutputDir = "reports"
)

$ErrorActionPreference = "Stop"

# Add GitHub CLI to PATH
$ghPath = "C:\Program Files\GitHub CLI"
if (Test-Path "$ghPath\gh.exe") {
    $env:PATH = "$ghPath;$env:PATH"
}

Write-Host "Getting Backend CI errors..." -ForegroundColor Green

# Create output directory
if (-not (Test-Path $OutputDir)) {
    New-Item -ItemType Directory -Path $OutputDir | Out-Null
}

# Get list of recent workflow runs
Write-Host "Getting last $Limit workflow runs..." -ForegroundColor Yellow
$runsJson = gh run list --workflow="Backend CI" --limit $Limit --json databaseId,status,conclusion,createdAt,updatedAt,headBranch,headSha,displayTitle,url,event,workflowName
$runs = $runsJson | ConvertFrom-Json

if ($runs.Count -eq 0) {
    Write-Host "No workflow runs found" -ForegroundColor Red
    exit 1
}

Write-Host "Found $($runs.Count) runs" -ForegroundColor Green

# Create result structure
$result = @{
    generatedAt = (Get-Date -Format "yyyy-MM-ddTHH:mm:ssZ")
    totalRuns = $runs.Count
    runs = @()
}

# Process each run
foreach ($run in $runs) {
    Write-Host "Processing run #$($run.databaseId) ($($run.headSha.Substring(0, 7)))..." -ForegroundColor Cyan
    
    # Get jobs for this run
    $jobsJson = gh run view $run.databaseId --json jobs
    if ($LASTEXITCODE -ne 0) {
        Write-Host "  Error getting jobs for run #$($run.databaseId)" -ForegroundColor Red
        continue
    }
    
    $runDetails = $jobsJson | ConvertFrom-Json
    $jobs = if ($runDetails.jobs) { $runDetails.jobs } else { @() }
    
    Write-Host "  Found $($jobs.Count) jobs" -ForegroundColor Gray
    
    # Filter failed jobs
    $failedJobs = $jobs | Where-Object { $_.conclusion -eq "failure" }
    Write-Host "  Failed jobs: $($failedJobs.Count)" -ForegroundColor $(if ($failedJobs.Count -gt 0) { "Red" } else { "Green" })
    
    # Get failed logs for this run (more efficient than per-job)
    Write-Host "  Getting failed logs for run..." -ForegroundColor Gray
    $failedLogs = gh run view $run.databaseId --log-failed 2>$null
    $logErrors = @()
    if ($LASTEXITCODE -eq 0 -and $failedLogs) {
        # Extract error patterns from logs
        $logLines = $failedLogs -split "`n"
        $errorPatterns = @(
            "FAIL",
            "Error:",
            "error:",
            "panic:",
            "fatal:",
            "missing go.sum",
            "C\+\+ source files not allowed",
            "not allowed when not using cgo",
            "exit code 1",
            "Process completed with exit code"
        )
        
        foreach ($line in $logLines) {
            foreach ($pattern in $errorPatterns) {
                if ($line -match $pattern) {
                    # Extract service name if present
                    $serviceMatch = if ($line -match "\(([^)]+-go)\)") { $matches[1] } else { "" }
                    $logErrors += @{
                        line = $line.Trim()
                        service = $serviceMatch
                        pattern = $pattern
                    }
                    break
                }
            }
        }
    }
    
    # Get details for each failed job
    $failedJobsDetails = @()
    foreach ($job in $failedJobs) {
        Write-Host "    Getting details for failed job: $($job.name)..." -ForegroundColor DarkYellow
        
        # Get job details with steps
        $jobDetailsJson = gh api "repos/gc-lover/necpgame-monorepo/actions/jobs/$($job.databaseId)"
        if ($LASTEXITCODE -eq 0 -and $jobDetailsJson) {
            $jobDetails = $jobDetailsJson | ConvertFrom-Json
            
            # Find failed steps
            $failedSteps = if ($jobDetails.steps) { 
                $jobDetails.steps | Where-Object { $_.conclusion -eq "failure" } 
            } else { @() }
            
            # Find relevant errors from logs for this job
            $jobServiceName = if ($job.name -match "\(([^)]+)\)") { $matches[1] } else { "" }
            $jobErrors = $logErrors | Where-Object { 
                $_.service -eq $jobServiceName -or 
                $job.name -match [regex]::Escape($_.service)
            } | Select-Object -First 10
            
            $jobError = @{
                id = $job.databaseId
                name = $job.name
                service = $jobServiceName
                status = $job.status
                conclusion = $job.conclusion
                startedAt = $job.startedAt
                completedAt = $job.completedAt
                url = $job.url
                htmlUrl = $job.htmlUrl
                failedSteps = @()
                errorSummary = $jobErrors
            }
            
            # Get error details from failed steps
            foreach ($step in $failedSteps) {
                Write-Host "      Failed step: $($step.name)" -ForegroundColor Red
                
                $stepError = @{
                    name = $step.name
                    status = $step.status
                    conclusion = $step.comclusion
                    number = $step.number
                    startedAt = $step.startedAt
                    completedAt = $step.completedAt
                }
                
                # Set log URL
                $logUrl = "$($job.htmlUrl)/logs#$step.number"
                $stepError.logUrl = $logUrl
                
                # Try to get error summary from step name and job context
                # For test failures, we'll get them from the run log
                if ($step.name -match "test|Test") {
                    $stepError.errorType = "test_failure"
                } elseif ($step.name -match "lint|Lint") {
                    $stepError.errorType = "lint_failure"
                } else {
                    $stepError.errorType = "step_failure"
                }
                
                $jobError.failedSteps += $stepError
            }
            
            $failedJobsDetails += $jobError
        } else {
            # Fallback: just basic job info
            $failedJobsDetails += @{
                id = $job.databaseId
                name = $job.name
                status = $job.status
                conclusion = $job.conclusion
                url = $job.url
                htmlUrl = $job.htmlUrl
                error = "Could not get detailed job information"
            }
        }
    }
    
    # Create run info with errors
    $runInfo = @{
        id = $run.databaseId
        status = $run.status
        conclusion = $run.conclusion
        createdAt = $run.createdAt
        updatedAt = $run.updatedAt
        headBranch = $run.headBranch
        headSha = $run.headSha
        shortSha = $run.headSha.Substring(0, 7)
        displayTitle = $run.displayTitle
        url = $run.url
        event = $run.event
        workflowName = $run.workflowName
        totalJobs = $jobs.Count
        failedJobsCount = $failedJobs.Count
        failedJobs = $failedJobsDetails
    }
    
    $result.runs += $runInfo
}

# Save JSON
$timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
$jsonFile = Join-Path $OutputDir "backend-ci-errors_$timestamp.json"
$result | ConvertTo-Json -Depth 10 | Set-Content -Path $jsonFile -Encoding UTF8

# Create Markdown report
$mdFile = Join-Path $OutputDir "backend-ci-errors_$timestamp.md"
$mdContent = @"
# Backend CI Errors Report

**Generated:** $($result.generatedAt)  
**Total Runs Analyzed:** $($result.totalRuns)

"@

foreach ($run in $result.runs) {
    if ($run.failedJobsCount -eq 0) {
        continue
    }
    
    $statusIcon = switch ($run.conclusion) {
        "failure" { "❌" }
        "cancelled" { "🚫" }
        default { "⏳" }
    }
    
    $mdContent += @"

## $statusIcon Run #$($run.id) - $($run.shortSha)

- **Status:** $($run.status) / $($run.conclusion)
- **Branch:** $($run.headBranch)
- **Commit:** [$($run.shortSha)]($($run.url))
- **Title:** $($run.displayTitle)
- **Created:** $($run.createdAt)
- **Total Jobs:** $($run.totalJobs)
- **Failed Jobs:** $($run.failedJobsCount)

### Failed Jobs

"@
    
    foreach ($job in $run.failedJobs) {
        $mdContent += @"
#### ❌ $($job.name)

- **Job ID:** $($job.id)
- **Status:** $($job.status) / $($job.conclusion)
- **URL:** [$($job.name)]($($job.htmlUrl))

"@
        
        if ($job.failedSteps -and $job.failedSteps.Count -gt 0) {
            $mdContent += "**Failed Steps:**`n`n"
            foreach ($step in $job.failedSteps) {
                $mdContent += @"
- **$($step.name)** (Step #$($step.number))
  - Status: $($step.status) / $($step.conclusion)
  - Log: [$($step.name)]($($step.logUrl))
"@
                
                if ($step.errorLines -and $step.errorLines.Count -gt 0) {
                    $mdContent += "`n  **Error Lines:**`n  ```\n"
                    foreach ($line in $step.errorLines) {
                        $mdContent += "  $line`n"
                    }
                    $mdContent += "  ```\n"
                }
            }
            
            if ($job.errorSummary -and $job.errorSummary.Count -gt 0) {
                $mdContent += "`n  **Error Summary from Logs:**`n  ```\n"
                foreach ($error in $job.errorSummary) {
                    $mdContent += "  [$($error.pattern)] $($error.line)`n"
                }
                $mdContent += "  ```\n"
                
                $mdContent += "`n"
            }
        } else {
            $mdContent += "No detailed step information available.`n"
        }
        
        $mdContent += "`n"
    }
}

$mdContent += @"

---
*Report generated automatically from GitHub Actions CI results*
"@

$mdContent | Set-Content -Path $mdFile -Encoding UTF8

Write-Host "`nDone! Results saved:" -ForegroundColor Green
Write-Host "  - JSON: $jsonFile" -ForegroundColor Cyan
Write-Host "  - Markdown: $mdFile" -ForegroundColor Cyan

