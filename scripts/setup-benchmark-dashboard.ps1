# Issue: Complete setup for benchmark dashboard
# Полная настройка дашборда бенчмарков

$ErrorActionPreference = "Continue"

Write-Host "🚀 Setting up Benchmark Dashboard..." -ForegroundColor Cyan
Write-Host ""

# 1. Create directories
Write-Host "1. Creating directories..." -ForegroundColor Yellow
New-Item -ItemType Directory -Force -Path ".benchmarks\results" | Out-Null
Write-Host "   OK .benchmarks\results created" -ForegroundColor Green

# 2. Create sample data
Write-Host ""
Write-Host "2. Creating sample benchmark data..." -ForegroundColor Yellow
$SampleJson = @{
    timestamp = (Get-Date -Format "yyyyMMdd_HHmmss")
    services = @(
        @{
            service = "loot-service-go"
            benchmarks = @(
                @{
                    name = "server/BenchmarkGetPlayerLootHistory"
                    ns_per_op = 200.2
                    allocs_per_op = 5
                    bytes_per_op = 320
                }
            )
        },
        @{
            service = "quest-core-service-go"
            benchmarks = @(
                @{
                    name = "server/BenchmarkGetQuest"
                    ns_per_op = 254.5
                    allocs_per_op = 5
                    bytes_per_op = 320
                }
            )
        }
    )
}

$SampleFile = ".benchmarks\results\benchmarks_$($SampleJson.timestamp).json"
$SampleJson | ConvertTo-Json -Depth 10 | Out-File -FilePath $SampleFile -Encoding UTF8
Write-Host "   OK Created: $SampleFile" -ForegroundColor Green

# 3. Export to Prometheus
Write-Host ""
Write-Host "3. Exporting to Prometheus format..." -ForegroundColor Yellow
& "$PSScriptRoot\export-benchmarks-to-prometheus.ps1" -UseFile -ResultsFile $SampleFile
if ($LASTEXITCODE -eq 0) {
    Write-Host "   OK Metrics exported" -ForegroundColor Green
} else {
    Write-Host "   ❌ Export failed" -ForegroundColor Red
    exit 1
}

# 4. Check HTTP server
Write-Host ""
Write-Host "4. Checking HTTP server..." -ForegroundColor Yellow
try {
    $Response = Invoke-WebRequest -Uri "http://localhost:9099/metrics" -TimeoutSec 2 -ErrorAction Stop
    Write-Host "   OK HTTP server is running" -ForegroundColor Green
} catch {
    Write-Host "   WARNING  HTTP server not running" -ForegroundColor Yellow
    Write-Host "      Start it: .\scripts\benchmark-metrics-server.ps1" -ForegroundColor Gray
}

# 5. Summary
Write-Host ""
Write-Host "OK Setup complete!" -ForegroundColor Green
Write-Host ""
Write-Host "Next steps:" -ForegroundColor Cyan
Write-Host "  1. Start HTTP server (if not running):" -ForegroundColor Yellow
Write-Host "     .\scripts\benchmark-metrics-server.ps1" -ForegroundColor White
Write-Host ""
Write-Host "  2. Restart Prometheus:" -ForegroundColor Yellow
Write-Host "     docker-compose restart prometheus" -ForegroundColor White
Write-Host ""
Write-Host "  3. Check Prometheus:" -ForegroundColor Yellow
Write-Host "     http://localhost:9090/graph?g0.expr=benchmark_ns_per_op" -ForegroundColor White
Write-Host ""
Write-Host "  4. Open Grafana:" -ForegroundColor Yellow
Write-Host "     http://localhost:3000 → Dashboards → Benchmarks History" -ForegroundColor White

