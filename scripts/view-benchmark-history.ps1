# Issue: Benchmark history viewer
# Просмотр истории бенчмарков с сравнением (PowerShell)

$ResultsDir = ".benchmarks/results"

if (-not (Test-Path $ResultsDir)) {
    Write-Host "❌ No benchmark results found in $ResultsDir" -ForegroundColor Red
    Write-Host "   Run: .\scripts\run-all-benchmarks.sh" -ForegroundColor Yellow
    exit 1
}

Write-Host "📊 Benchmark History Viewer" -ForegroundColor Cyan
Write-Host "==========================" -ForegroundColor Cyan
Write-Host ""

# Показываем все доступные результаты
Write-Host "Available benchmark runs:" -ForegroundColor Yellow
Write-Host ""

$Files = Get-ChildItem "$ResultsDir\*.json" | Sort-Object LastWriteTime -Descending | Select-Object -First 10

if ($Files.Count -eq 0) {
    Write-Host "  No results found" -ForegroundColor Red
    exit 1
}

for ($i = 0; $i -lt $Files.Count; $i++) {
    $File = $Files[$i]
    $Basename = [System.IO.Path]::GetFileNameWithoutExtension($File.Name)
    $Timestamp = $Basename -replace 'benchmarks_', ''
    $Parts = $Timestamp -split '_'
    $Date = $Parts[0]
    $Time = $Parts[1]
    
    Write-Host "  [$($i+1)] $Date $Time" -ForegroundColor Gray
}

Write-Host ""
$Choice = Read-Host "Select run number (1-$($Files.Count)) or 'compare' for comparison"

if ($Choice -eq "compare") {
    if ($Files.Count -lt 2) {
        Write-Host "❌ Need at least 2 runs for comparison" -ForegroundColor Red
        exit 1
    }
    
    $Latest = $Files[0]
    $Previous = $Files[1]
    
    Write-Host ""
    Write-Host "📊 Comparing:" -ForegroundColor Cyan
    Write-Host "   Latest:   $($Latest.Name)" -ForegroundColor Green
    Write-Host "   Previous: $($Previous.Name)" -ForegroundColor Yellow
    Write-Host ""
    
    # Извлекаем и сравниваем данные
    $LatestData = Get-Content $Latest.FullName | ConvertFrom-Json
    $PreviousData = Get-Content $Previous.FullName | ConvertFrom-Json
    
    Write-Host "Service/Benchmark                    | Latest (ns/op) | Previous (ns/op) | Change" -ForegroundColor Cyan
    Write-Host "-------------------------------------|----------------|------------------|--------" -ForegroundColor Gray
    
    foreach ($Service in $LatestData.services) {
        foreach ($Benchmark in $Service.benchmarks) {
            $PrevBenchmark = ($PreviousData.services | Where-Object { $_.service -eq $Service.service }).benchmarks | 
                            Where-Object { $_.name -eq $Benchmark.name }
            
            if ($PrevBenchmark) {
                $Change = $Benchmark.ns_per_op - $PrevBenchmark.ns_per_op
                $ChangePercent = if ($PrevBenchmark.ns_per_op -ne 0) { 
                    [math]::Round(($Change / $PrevBenchmark.ns_per_op) * 100, 2) 
                } else { 0 }
                
                $ChangeStr = if ($ChangePercent -gt 0) { 
                    "+$ChangePercent%" -ForegroundColor Red 
                } elseif ($ChangePercent -lt 0) { 
                    "$ChangePercent%" -ForegroundColor Green 
                } else { 
                    "0%" -ForegroundColor Gray 
                }
                
                Write-Host ("{0,-35} | {1,14} | {2,16} | {3}" -f `
                    "$($Service.service)/$($Benchmark.name)", `
                    $Benchmark.ns_per_op, `
                    $PrevBenchmark.ns_per_op, `
                    $ChangeStr)
            }
        }
    }
} else {
    $Selected = [int]$Choice - 1
    if ($Selected -lt 0 -or $Selected -ge $Files.Count) {
        Write-Host "❌ Invalid selection" -ForegroundColor Red
        exit 1
    }
    
    $File = $Files[$Selected]
    
    Write-Host ""
    Write-Host "📊 Benchmark Results: $($File.Name)" -ForegroundColor Cyan
    Write-Host "==========================================" -ForegroundColor Cyan
    Write-Host ""
    
    # Показываем результаты
    $Data = Get-Content $File.FullName | ConvertFrom-Json
    
    foreach ($Service in $Data.services) {
        Write-Host "$($Service.service):" -ForegroundColor Yellow
        foreach ($Benchmark in $Service.benchmarks) {
            Write-Host "  - $($Benchmark.name): $($Benchmark.ns_per_op) ns/op, $($Benchmark.allocs_per_op) allocs/op, $($Benchmark.bytes_per_op) bytes/op" -ForegroundColor Gray
        }
        Write-Host ""
    }
}

