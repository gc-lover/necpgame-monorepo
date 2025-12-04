# Issue: Verify all benchmarks work
# Проверяет все бенчмарки и создает отчет

$ErrorActionPreference = "Continue"
$ServicesDir = "services"
$Results = @()
$Working = 0
$Failed = 0
$Skipped = 0

Write-Host "Verifying all benchmarks..." -ForegroundColor Cyan
Write-Host ""

$ServiceDirs = Get-ChildItem $ServicesDir -Directory | Where-Object { 
    $_.Name -like "*-go" -or $_.Name -like "*-service-go" 
} | Sort-Object Name

foreach ($ServiceDir in $ServiceDirs) {
    $ServiceName = $ServiceDir.Name
    $BenchFile = Join-Path $ServiceDir.FullName "server\handlers_bench_test.go"
    
    if (-not (Test-Path $BenchFile)) {
        $Skipped++
        continue
    }
    
    Write-Host "  Testing: $ServiceName" -ForegroundColor Yellow -NoNewline
    
    try {
        Push-Location $ServiceDir.FullName
        
        # Проверяем компиляцию и запуск
        $TestOutput = go test -run=^$ -bench=. -benchmem -benchtime=1ms ./server 2>&1
        $ExitCode = $LASTEXITCODE
        
        Pop-Location
        
        if ($ExitCode -eq 0) {
            # Извлекаем результаты бенчмарков
            $BenchResults = $TestOutput | Select-String -Pattern "Benchmark\w+\s+\d+\s+(\d+)\s+ns/op\s+(\d+)\s+B/op\s+(\d+)\s+allocs/op"
            
            $Benchmarks = @()
            foreach ($Match in $BenchResults) {
                $Benchmarks += [PSCustomObject]@{
                    Name = ($Match.Line -split '\s+')[0]
                    NsPerOp = ($Match.Line -split '\s+')[2]
                    BytesPerOp = ($Match.Line -split '\s+')[4]
                    AllocsPerOp = ($Match.Line -split '\s+')[6]
                }
            }
            
            $Results += [PSCustomObject]@{
                Service = $ServiceName
                Status = "OK"
                Benchmarks = $Benchmarks.Count
                Details = $Benchmarks
            }
            
            Write-Host " - OK ($($Benchmarks.Count) benchmarks)" -ForegroundColor Green
            $Working++
        } else {
            $ErrorMsg = ($TestOutput | Select-String -Pattern "error:|undefined:|unknown field" | Select-Object -First 1).Line
            $Results += [PSCustomObject]@{
                Service = $ServiceName
                Status = "FAILED"
                Benchmarks = 0
                Error = $ErrorMsg
            }
            
            Write-Host " - FAILED" -ForegroundColor Red
            $Failed++
        }
    } catch {
        Pop-Location
        $Results += [PSCustomObject]@{
            Service = $ServiceName
            Status = "ERROR"
            Error = $_.Exception.Message
        }
        Write-Host " - ERROR" -ForegroundColor Red
        $Failed++
    }
}

Write-Host ""
Write-Host "Summary:" -ForegroundColor Cyan
Write-Host "  Working: $Working" -ForegroundColor Green
Write-Host "  Failed: $Failed" -ForegroundColor Red
Write-Host "  Skipped: $Skipped" -ForegroundColor Yellow

# Сохраняем отчет
$Report = @"
# Benchmark Verification Report

**Date:** $(Get-Date -Format "yyyy-MM-dd HH:mm:ss")
**Total Services:** $($Working + $Failed + $Skipped)
**Working:** $Working
**Failed:** $Failed
**Skipped:** $Skipped

## Working Benchmarks

"@

$WorkingServices = $Results | Where-Object { $_.Status -eq "OK" }
foreach ($Service in $WorkingServices) {
    $Report += "### $($Service.Service)`n"
    $Report += "- Benchmarks: $($Service.Benchmarks)`n"
    if ($Service.Details) {
        foreach ($Bench in $Service.Details) {
            $Report += "  - $($Bench.Name): $($Bench.NsPerOp) ns/op, $($Bench.BytesPerOp) B/op, $($Bench.AllocsPerOp) allocs/op`n"
        }
    }
    $Report += "`n"
}

$Report += "## Failed Benchmarks`n`n"

$FailedServices = $Results | Where-Object { $_.Status -ne "OK" }
foreach ($Service in $FailedServices) {
    $Report += "### $($Service.Service)`n"
    $Report += "- Status: $($Service.Status)`n"
    if ($Service.Error) {
        $Report += "- Error: $($Service.Error)`n"
    }
    $Report += "`n"
}

$Report | Out-File -FilePath "BENCHMARK-VERIFICATION-REPORT.md" -Encoding UTF8

Write-Host ""
Write-Host "Report saved to: BENCHMARK-VERIFICATION-REPORT.md" -ForegroundColor Cyan

