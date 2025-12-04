# Issue: Export benchmark results to Prometheus
# Читает JSON результаты и отправляет в Prometheus через Pushgateway или файл

param(
    [string]$ResultsFile = "",
    [string]$PushgatewayUrl = "http://localhost:9091",
    [switch]$UseFile = $false,
    [string]$MetricsFile = ".benchmarks/metrics.prom"
)

$ErrorActionPreference = "Continue"

# Если файл не указан, берем последний
if ([string]::IsNullOrEmpty($ResultsFile)) {
    $Latest = Get-ChildItem ".benchmarks\results\*.json" -ErrorAction SilentlyContinue | 
        Sort-Object LastWriteTime -Descending | 
        Select-Object -First 1
    
    if (-not $Latest) {
        Write-Host "❌ No benchmark results found" -ForegroundColor Red
        Write-Host "   Run: .\scripts\run-all-benchmarks.sh" -ForegroundColor Yellow
        exit 1
    }
    
    $ResultsFile = $Latest.FullName
    Write-Host "📊 Using latest results: $($Latest.Name)" -ForegroundColor Cyan
}

if (-not (Test-Path $ResultsFile)) {
    Write-Host "❌ File not found: $ResultsFile" -ForegroundColor Red
    exit 1
}

Write-Host "📊 Exporting benchmarks to Prometheus..." -ForegroundColor Cyan
Write-Host "   Source: $ResultsFile" -ForegroundColor Gray

# Читаем JSON
$Data = Get-Content $ResultsFile | ConvertFrom-Json

# Генерируем Prometheus метрики
$Metrics = @()

# Парсим timestamp (формат: YYYYMMDD_HHMMSS)
$TimestampStr = $Data.timestamp
$Year = $TimestampStr.Substring(0, 4)
$Month = $TimestampStr.Substring(4, 2)
$Day = $TimestampStr.Substring(6, 2)
$Hour = $TimestampStr.Substring(9, 2)
$Minute = $TimestampStr.Substring(11, 2)
$Second = $TimestampStr.Substring(13, 2)

$DateTimeStr = "${Year}-${Month}-${Day} ${Hour}:${Minute}:${Second}"
$DateTime = [DateTime]::ParseExact($DateTimeStr, "yyyy-MM-dd HH:mm:ss", $null)
$Timestamp = [DateTimeOffset]::new($DateTime).ToUnixTimeSeconds()

foreach ($Service in $Data.services) {
    foreach ($Benchmark in $Service.benchmarks) {
        # Извлекаем имя бенчмарка
        $BenchName = $Benchmark.name -replace '.*/', ''
        
        # Метрика: ns/op
        $Metrics += "# HELP benchmark_ns_per_op Benchmark nanoseconds per operation"
        $Metrics += "# TYPE benchmark_ns_per_op gauge"
        $Metrics += "benchmark_ns_per_op{service=`"$($Service.service)`",benchmark=`"$BenchName`"} $($Benchmark.ns_per_op) $Timestamp"
        
        # Метрика: allocs/op
        $Metrics += "# HELP benchmark_allocs_per_op Benchmark allocations per operation"
        $Metrics += "# TYPE benchmark_allocs_per_op gauge"
        $Metrics += "benchmark_allocs_per_op{service=`"$($Service.service)`",benchmark=`"$BenchName`"} $($Benchmark.allocs_per_op) $Timestamp"
        
        # Метрика: bytes/op
        $Metrics += "# HELP benchmark_bytes_per_op Benchmark bytes per operation"
        $Metrics += "# TYPE benchmark_bytes_per_op gauge"
        $Metrics += "benchmark_bytes_per_op{service=`"$($Service.service)`",benchmark=`"$BenchName`"} $($Benchmark.bytes_per_op) $Timestamp"
    }
}

# Сохраняем или отправляем
if ($UseFile) {
    $Metrics | Out-File -FilePath $MetricsFile -Encoding UTF8
    Write-Host "OK Metrics saved to: $MetricsFile" -ForegroundColor Green
    Write-Host "   Configure Prometheus to scrape this file" -ForegroundColor Yellow
} else {
    # Отправляем в Pushgateway
    $Body = $Metrics -join "`n"
    
    try {
        $Response = Invoke-WebRequest -Uri "$PushgatewayUrl/metrics/job/benchmarks" `
            -Method POST `
            -Body $Body `
            -ContentType "text/plain" `
            -ErrorAction Stop
        
        Write-Host "OK Metrics pushed to Pushgateway: $PushgatewayUrl" -ForegroundColor Green
    } catch {
        Write-Host "❌ Failed to push to Pushgateway: $_" -ForegroundColor Red
        Write-Host "   Saving to file instead..." -ForegroundColor Yellow
        
        $Metrics | Out-File -FilePath $MetricsFile -Encoding UTF8
        Write-Host "OK Metrics saved to: $MetricsFile" -ForegroundColor Green
    }
}

Write-Host ""
Write-Host "📊 Summary:" -ForegroundColor Cyan
Write-Host "   Services: $($Data.services.Count)" -ForegroundColor Gray
$TotalBenchmarks = ($Data.services | ForEach-Object { $_.benchmarks.Count } | Measure-Object -Sum).Sum
Write-Host "   Benchmarks: $TotalBenchmarks" -ForegroundColor Gray
