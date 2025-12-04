# Issue: Run all benchmarks and collect results
# Запускает все бенчмарки и сохраняет результаты

$ErrorActionPreference = "Continue"

$ResultsDir = ".benchmarks/results"
$Timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
$OutputFile = Join-Path $ResultsDir "benchmarks_$Timestamp.json"

New-Item -ItemType Directory -Force -Path $ResultsDir | Out-Null

Write-Host "Running benchmarks for all services..." -ForegroundColor Cyan
Write-Host ""

$ServicesFound = 0
$ServicesProcessed = 0
$ServicesSkipped = 0

# Start JSON output
$jsonContent = @{
    timestamp = $Timestamp
    services = @()
} | ConvertTo-Json -Depth 1

# Remove closing brace to append services
$jsonContent = $jsonContent -replace '}$', ''

$ServiceDirs = Get-ChildItem "services" -Directory | Where-Object { 
    $_.Name -like "*-go" 
}

foreach ($ServiceDir in $ServiceDirs) {
    $ServiceName = $ServiceDir.Name
    $ServicePath = $ServiceDir.FullName
    
    Write-Host "  Benchmarking: $ServiceName" -NoNewline
    
    # Check for benchmark files
    $benchFiles = Get-ChildItem -Path $ServicePath -Recurse -Filter "*_bench_test.go" -ErrorAction SilentlyContinue
    if ($benchFiles.Count -eq 0) {
        Write-Host " - No benchmarks found" -ForegroundColor Gray
        $ServicesSkipped++
        continue
    }
    
    $ServicesFound++
    
    Push-Location $ServicePath
    
    try {
        # Try different paths for benchmarks
        $benchOutput = $null
        
        # Try ./server first (most common location)
        if (Test-Path "server") {
            Push-Location "server"
            $benchOutput = go test -run=^$ -bench=. -benchmem -json . 2>&1
            Pop-Location
            
            if ($LASTEXITCODE -eq 0 -and $benchOutput -notmatch "no test files|no packages") {
                # Success, use this output
            } else {
                $benchOutput = $null
            }
        }
        
        # If failed, try root with ./...
        if (-not $benchOutput) {
            $benchOutput = go test -run=^$ -bench=. -benchmem -json ./... 2>&1
        }
        
        if ($LASTEXITCODE -ne 0 -or $benchOutput -match "no test files|no packages") {
            Write-Host " - No benchmark tests" -ForegroundColor Yellow
            $ServicesSkipped++
            Pop-Location
            continue
        }
        
        # Parse JSON output to extract benchmark results
        $benchmarks = @()
        
        # Split output by lines and parse each JSON line
        $outputLines = $benchOutput -split "`n" | Where-Object { $_.Trim() -ne "" }
        
        foreach ($line in $outputLines) {
            try {
                $line = $line.Trim()
                if ($line -match '^\s*\{') {
                    $benchData = $line | ConvertFrom-Json
                    if ($benchData.Action -eq "bench") {
                        $benchmark = @{
                            name = if ($benchData.Package -and $benchData.Test) { "$($benchData.Package)/$($benchData.Test)" } else { $benchData.Test }
                            ns_per_op = if ($benchData.NsPerOp) { [double]$benchData.NsPerOp } else { 0 }
                            allocs_per_op = if ($benchData.AllocsPerOp) { [int]$benchData.AllocsPerOp } else { 0 }
                            bytes_per_op = if ($benchData.BytesPerOp) { [int]$benchData.BytesPerOp } else { 0 }
                        }
                        $benchmarks += $benchmark
                    }
                }
            } catch {
                # Skip invalid JSON lines
                continue
            }
        }
        
        if ($benchmarks.Count -gt 0) {
            $serviceData = @{
                service = $ServiceName
                benchmarks = $benchmarks
            }
            
            $jsonContent += ",`n" + ($serviceData | ConvertTo-Json -Depth 10 -Compress)
            $ServicesProcessed++
            Write-Host " - $($benchmarks.Count) benchmarks" -ForegroundColor Green
        } else {
            Write-Host " - No benchmark results" -ForegroundColor Yellow
            $ServicesSkipped++
        }
        
    } catch {
        Write-Host " - Error: $_" -ForegroundColor Red
        $ServicesSkipped++
    } finally {
        Pop-Location
    }
}

# Close JSON
$jsonContent += "`n}"

# Write to file
Set-Content -Path $OutputFile -Value $jsonContent -NoNewline

Write-Host ""
Write-Host "Benchmark collection complete!" -ForegroundColor Green
Write-Host "  Services found: $ServicesFound" -ForegroundColor Cyan
Write-Host "  Services processed: $ServicesProcessed" -ForegroundColor Green
Write-Host "  Services skipped: $ServicesSkipped" -ForegroundColor Gray
Write-Host "  Results saved to: $OutputFile" -ForegroundColor Cyan

