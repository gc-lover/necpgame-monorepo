# Simple export script (without errors)

$ResultsFile = Get-ChildItem ".benchmarks\results\*.json" -ErrorAction SilentlyContinue | 
    Sort-Object LastWriteTime -Descending | 
    Select-Object -First 1

if (-not $ResultsFile) {
    Write-Host "No results found" -ForegroundColor Red
    exit 1
}

$Data = Get-Content $ResultsFile.FullName | ConvertFrom-Json
$Metrics = @()

foreach ($Service in $Data.services) {
    foreach ($Benchmark in $Service.benchmarks) {
        $BenchName = $Benchmark.name -replace '.*/', ''
        $Metrics += "benchmark_ns_per_op{service=`"$($Service.service)`",benchmark=`"$BenchName`"} $($Benchmark.ns_per_op)"
        $Metrics += "benchmark_allocs_per_op{service=`"$($Service.service)`",benchmark=`"$BenchName`"} $($Benchmark.allocs_per_op)"
        $Metrics += "benchmark_bytes_per_op{service=`"$($Service.service)`",benchmark=`"$BenchName`"} $($Benchmark.bytes_per_op)"
    }
}

$Metrics | Out-File -FilePath ".benchmarks\metrics.prom" -Encoding UTF8
Write-Host "Exported to .benchmarks\metrics.prom" -ForegroundColor Green

