# Issue: Test build process with tests and benchmarks
# Проверяет что build запускает тесты и бенчмарки

$ErrorActionPreference = "Continue"

$ServicesDir = "services"
$Tested = 0
$Passed = 0
$Failed = 0

Write-Host "Testing build process (tests + benchmarks) for services..."
Write-Host ""

$ServiceDirs = Get-ChildItem $ServicesDir -Directory | Where-Object { 
    $_.Name -like "*-go" 
} | Select-Object -First 5

foreach ($ServiceDir in $ServiceDirs) {
    $ServiceName = $ServiceDir.Name
    $MakefilePath = Join-Path $ServiceDir.FullName "Makefile"
    
    if (-not (Test-Path $MakefilePath)) {
        Write-Host "  [SKIP] $ServiceName - no Makefile" -ForegroundColor Gray
        continue
    }
    
    $Content = Get-Content $MakefilePath -Raw
    
    if ($Content -notmatch '^build:') {
        Write-Host "  [SKIP] $ServiceName - no build target" -ForegroundColor Gray
        continue
    }
    
    Write-Host "  [TEST] $ServiceName" -ForegroundColor Cyan
    
    Push-Location $ServiceDir.FullName
    
    try {
        # Test 1: Check if test target works
        Write-Host "    Running tests..." -NoNewline
        $testOutput = go test ./... 2>&1
        if ($LASTEXITCODE -eq 0) {
            Write-Host " [OK]" -ForegroundColor Green
        } else {
            Write-Host " [FAIL]" -ForegroundColor Red
            $Failed++
            Pop-Location
            continue
        }
        
        # Test 2: Check if benchmarks work (if benchmark file exists)
        $benchFile = Join-Path "server" "handlers_bench_test.go"
        $hasBench = Test-Path $benchFile
        
        if (-not $hasBench) {
            $hasBench = (Get-ChildItem -Recurse -Filter "*_bench_test.go" -ErrorAction SilentlyContinue).Count -gt 0
        }
        
        if ($hasBench) {
            Write-Host "    Running benchmarks..." -NoNewline
            $benchOutput = go test -run=^$ -bench=. -benchmem -benchtime=100ms ./server 2>&1
            if ($LASTEXITCODE -eq 0 -or $benchOutput -match "PASS") {
                Write-Host " [OK]" -ForegroundColor Green
            } else {
                Write-Host " [WARN]" -ForegroundColor Yellow
            }
        } else {
            Write-Host "    No benchmarks found [SKIP]" -ForegroundColor Gray
        }
        
        # Test 3: Check if build target includes test/bench
        if ($Content -match 'build:.*test|build:.*bench') {
            Write-Host "    Build includes tests/benchmarks [OK]" -ForegroundColor Green
        } else {
            Write-Host "    Build does NOT include tests/benchmarks [WARN]" -ForegroundColor Yellow
        }
        
        $Passed++
        
    } catch {
        Write-Host "    [ERROR] $_" -ForegroundColor Red
        $Failed++
    } finally {
        Pop-Location
    }
    
    $Tested++
    Write-Host ""
}

Write-Host "Test Summary:"
Write-Host "  Tested: $Tested" -ForegroundColor Cyan
Write-Host "  Passed: $Passed" -ForegroundColor Green
Write-Host "  Failed: $Failed" -ForegroundColor $(if ($Failed -gt 0) { "Red" } else { "Gray" })

