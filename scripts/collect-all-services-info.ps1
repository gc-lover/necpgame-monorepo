# Issue: Collect all services info and test results
# Собирает информацию по всем сервисам и результатам тестов

$ErrorActionPreference = "Continue"
$OutputFile = "services-info-report.json"
$Report = @{
    timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
    services = @()
    summary = @{
        total_services = 0
        services_with_tests = 0
        services_with_benchmarks = 0
        total_test_files = 0
        total_benchmark_files = 0
    }
}

Write-Host "🔍 Collecting information about all services..." -ForegroundColor Cyan
Write-Host ""

$ServiceDirs = Get-ChildItem services -Directory | Where-Object { $_.Name -like "*-go" -or $_.Name -like "*-service-go" }

$Report.summary.total_services = $ServiceDirs.Count

foreach ($ServiceDir in $ServiceDirs) {
    $ServiceName = $ServiceDir.Name
    Write-Host "  📦 $ServiceName" -ForegroundColor Yellow
    
    $ServiceInfo = @{
        name = $ServiceName
        path = $ServiceDir.FullName
        has_tests = $false
        has_benchmarks = $false
        test_files = @()
        benchmark_files = @()
        has_dockerfile = $false
        has_makefile = $false
        has_main_go = $false
        go_mod_exists = $false
    }
    
    # Проверяем наличие файлов
    $ServiceInfo.has_dockerfile = Test-Path "$($ServiceDir.FullName)\Dockerfile"
    $ServiceInfo.has_makefile = Test-Path "$($ServiceDir.FullName)\Makefile"
    $ServiceInfo.has_main_go = Test-Path "$($ServiceDir.FullName)\main.go"
    $ServiceInfo.go_mod_exists = Test-Path "$($ServiceDir.FullName)\go.mod"
    
    # Ищем тесты
    $TestFiles = Get-ChildItem -Path $ServiceDir.FullName -Recurse -Filter "*_test.go" -ErrorAction SilentlyContinue | 
                 Where-Object { $_.Name -notlike "*_bench_test.go" }
    
    if ($TestFiles) {
        $ServiceInfo.has_tests = $true
        $ServiceInfo.test_files = $TestFiles | ForEach-Object { $_.FullName.Replace($ServiceDir.FullName, "").TrimStart('\') }
        $Report.summary.services_with_tests++
        $Report.summary.total_test_files += $TestFiles.Count
    }
    
    # Ищем бенчмарки
    $BenchFiles = Get-ChildItem -Path $ServiceDir.FullName -Recurse -Filter "*_bench_test.go" -ErrorAction SilentlyContinue
    
    if ($BenchFiles) {
        $ServiceInfo.has_benchmarks = $true
        $ServiceInfo.benchmark_files = $BenchFiles | ForEach-Object { $_.FullName.Replace($ServiceDir.FullName, "").TrimStart('\') }
        $Report.summary.services_with_benchmarks++
        $Report.summary.total_benchmark_files += $BenchFiles.Count
    }
    
    $Report.services += $ServiceInfo
}

Write-Host ""
Write-Host "📊 Summary:" -ForegroundColor Cyan
Write-Host "  Total services: $($Report.summary.total_services)" -ForegroundColor Green
Write-Host "  Services with tests: $($Report.summary.services_with_tests)" -ForegroundColor Green
Write-Host "  Services with benchmarks: $($Report.summary.services_with_benchmarks)" -ForegroundColor Green
Write-Host "  Total test files: $($Report.summary.total_test_files)" -ForegroundColor Green
Write-Host "  Total benchmark files: $($Report.summary.total_benchmark_files)" -ForegroundColor Green
Write-Host ""

# Сохраняем отчет
$Report | ConvertTo-Json -Depth 10 | Out-File -FilePath $OutputFile -Encoding UTF8

Write-Host "OK Report saved to: $OutputFile" -ForegroundColor Green
Write-Host ""

# Показываем детали по сервисам с тестами
Write-Host "📋 Services with tests:" -ForegroundColor Cyan
$Report.services | Where-Object { $_.has_tests } | ForEach-Object {
    Write-Host "  OK $($_.name) - $($_.test_files.Count) test file(s)" -ForegroundColor Green
}

Write-Host ""
Write-Host "📋 Services with benchmarks:" -ForegroundColor Cyan
$Report.services | Where-Object { $_.has_benchmarks } | ForEach-Object {
    Write-Host "  ⚡ $($_.name) - $($_.benchmark_files.Count) benchmark file(s)" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "📋 Services without tests:" -ForegroundColor Cyan
$Report.services | Where-Object { $_.has_tests -eq $false -and $_.has_benchmarks -eq $false } | ForEach-Object {
    Write-Host "  WARNING  $($_.name)" -ForegroundColor Gray
}
