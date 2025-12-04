# Issue: Create sample benchmark results for testing dashboard
# Создает тестовые результаты бенчмарков для проверки дашборда

$ErrorActionPreference = "Continue"

$ResultsDir = ".benchmarks/results"
$Timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
$OutputFile = Join-Path $ResultsDir "benchmarks_$Timestamp.json"

New-Item -ItemType Directory -Force -Path $ResultsDir | Out-Null

Write-Host "📊 Creating sample benchmark results..." -ForegroundColor Cyan

# Создаем тестовые данные
$SampleData = @{
    timestamp = $Timestamp
    services = @(
        @{
            service = "loot-service-go"
            benchmarks = @(
                @{
                    name = "server/BenchmarkGetPlayerLootHistory"
                    ns_per_op = 207.0
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
        },
        @{
            service = "social-reputation-core-service-go"
            benchmarks = @(
                @{
                    name = "server/BenchmarkGetReputation"
                    ns_per_op = 180.0
                    allocs_per_op = 3
                    bytes_per_op = 256
                }
            )
        }
    )
}

$SampleData | ConvertTo-Json -Depth 10 | Out-File -FilePath $OutputFile -Encoding UTF8

Write-Host "✅ Created: $OutputFile" -ForegroundColor Green
Write-Host ""
Write-Host "Next steps:" -ForegroundColor Yellow
Write-Host "  1. Export to Prometheus: .\scripts\export-benchmarks-to-prometheus.ps1 -UseFile" -ForegroundColor Gray
Write-Host "  2. Start HTTP server: .\scripts\benchmark-metrics-server.ps1" -ForegroundColor Gray
Write-Host "  3. Check Prometheus: http://localhost:9090/graph?g0.expr=benchmark_ns_per_op" -ForegroundColor Gray

