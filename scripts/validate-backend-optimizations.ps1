# Validate backend optimizations before handoff
# Usage: .\scripts\validate-backend-optimizations.ps1 -ServiceDir services\companion-service-go

param(
    [Parameter(Mandatory=$true)]
    [string]$ServiceDir
)

if (-not (Test-Path $ServiceDir)) {
    Write-Host "❌ Directory not found: $ServiceDir" -ForegroundColor Red
    exit 1
}

Push-Location $ServiceDir

Write-Host ""
Write-Host "🔍 Validating optimizations for: $ServiceDir" -ForegroundColor Cyan
Write-Host "================================================"
Write-Host ""

$Errors = 0
$Warnings = 0

# 1. Struct alignment
Write-Host "📐 Checking struct alignment..." -ForegroundColor Yellow
if (Get-Command fieldalignment -ErrorAction SilentlyContinue) {
    $alignOutput = fieldalignment ./... 2>&1 | Out-String
    if ($alignOutput -match "struct") {
        Write-Host "⚠️  WARNING: Struct alignment can be improved" -ForegroundColor Yellow
        Write-Host ($alignOutput | Select-String -Pattern "struct" | Select-Object -First 10)
        $Warnings++
    } else {
        Write-Host "✅ Struct alignment: OK" -ForegroundColor Green
    }
} else {
    Write-Host "⚠️  fieldalignment not installed" -ForegroundColor Yellow
    Write-Host "   Install: go install golang.org/x/tools/go/analysis/passes/fieldalignment/cmd/fieldalignment@latest"
}

Write-Host ""

# 2. Goroutine leaks
Write-Host "🔍 Checking goroutine leaks..." -ForegroundColor Yellow
$goModContent = Get-Content go.mod -Raw -ErrorAction SilentlyContinue
if ($goModContent -match "go.uber.org/goleak") {
    $testOutput = go test -v -run TestMain ./... 2>&1 | Out-String
    if ($testOutput -match "leak") {
        Write-Host "🔴 BLOCKER: Goroutine leaks detected!" -ForegroundColor Red
        $Errors++
    } else {
        Write-Host "✅ No goroutine leaks" -ForegroundColor Green
    }
} else {
    Write-Host "⚠️  WARNING: goleak not in dependencies" -ForegroundColor Yellow
    Write-Host "   Recommended: go get go.uber.org/goleak"
    $Warnings++
}

Write-Host ""

# 3. Context timeouts
Write-Host "⏱️  Checking context timeouts..." -ForegroundColor Yellow
$timeoutCount = (Select-String -Path "server\*.go" -Pattern "context\.WithTimeout|context\.WithDeadline" -ErrorAction SilentlyContinue).Count
if ($timeoutCount -eq 0) {
    Write-Host "🔴 BLOCKER: No context timeouts found in server/" -ForegroundColor Red
    $Errors++
} else {
    Write-Host "✅ Context timeouts: $timeoutCount instances" -ForegroundColor Green
}

Write-Host ""

# 4. DB connection pool
Write-Host "🗄️  Checking DB connection pool..." -ForegroundColor Yellow
$poolConfig = Select-String -Path "server\*.go" -Pattern "SetMaxOpenConns|SetMaxIdleConns" -ErrorAction SilentlyContinue
if ($poolConfig) {
    Write-Host "✅ DB connection pool: configured" -ForegroundColor Green
} else {
    Write-Host "⚠️  WARNING: DB connection pool not configured" -ForegroundColor Yellow
    $Warnings++
}

Write-Host ""

# 5. Structured logging
Write-Host "📝 Checking structured logging..." -ForegroundColor Yellow
$badLogging = Select-String -Path "server\*.go" -Pattern "fmt\.Println|log\.Println" -ErrorAction SilentlyContinue
if ($badLogging) {
    Write-Host "⚠️  WARNING: Found fmt.Println/log.Println (use structured logger)" -ForegroundColor Yellow
    $badLogging | Select-Object -First 5 | ForEach-Object { Write-Host "   $_" }
    $Warnings++
} else {
    Write-Host "✅ Structured logging: OK" -ForegroundColor Green
}

Write-Host ""

# 6. Memory pooling
Write-Host "♻️  Checking memory pooling..." -ForegroundColor Yellow
$poolCount = (Select-String -Path "server\*.go" -Pattern "sync\.Pool" -ErrorAction SilentlyContinue).Count
if ($poolCount -eq 0) {
    Write-Host "⚠️  WARNING: No sync.Pool found (recommended for hot path)" -ForegroundColor Yellow
    $Warnings++
} else {
    Write-Host "✅ Memory pooling: $poolCount pools" -ForegroundColor Green
}

Write-Host ""

# 7. Benchmarks
Write-Host "🏃 Running benchmarks..." -ForegroundColor Yellow
$benchOutput = go test -bench=. -benchmem ./... 2>&1 | Out-String
if ($benchOutput -match "Benchmark") {
    Write-Host "✅ Benchmarks exist" -ForegroundColor Green
    
    # Check allocations
    $allocIssues = $benchOutput | Select-String -Pattern "(\d+)\s+allocs/op" | Where-Object {
        $_ -match "(\d+)\s+allocs/op" -and [int]$matches[1] -gt 5
    }
    if ($allocIssues) {
        Write-Host "⚠️  WARNING: High allocations detected:" -ForegroundColor Yellow
        $allocIssues | Select-Object -First 5 | ForEach-Object { Write-Host "   $_" }
        $Warnings++
    }
} else {
    Write-Host "⚠️  WARNING: No benchmarks found" -ForegroundColor Yellow
    $Warnings++
}

Write-Host ""

# 8. Profiling endpoints
Write-Host "📊 Checking profiling..." -ForegroundColor Yellow
$pprofImport = Select-String -Path "*.go" -Pattern "net/http/pprof" -ErrorAction SilentlyContinue
if ($pprofImport) {
    Write-Host "✅ Profiling: enabled" -ForegroundColor Green
} else {
    Write-Host "⚠️  WARNING: pprof not imported" -ForegroundColor Yellow
    $Warnings++
}

Write-Host ""

# Summary
Write-Host "================================================"
Write-Host "📊 SUMMARY" -ForegroundColor Cyan
Write-Host "================================================"
Write-Host ""
Write-Host "🔴 BLOCKERS: $Errors" -ForegroundColor $(if ($Errors -gt 0) { "Red" } else { "Green" })
Write-Host "🟡 WARNINGS: $Warnings" -ForegroundColor $(if ($Warnings -gt 0) { "Yellow" } else { "Green" })
Write-Host ""

Pop-Location

if ($Errors -gt 0) {
    Write-Host "🔴 VALIDATION FAILED - Fix blockers before handoff" -ForegroundColor Red
    Write-Host ""
    Write-Host "**Action:** Keep status 'Backend - In Progress'" -ForegroundColor Yellow
    exit 1
} elseif ($Warnings -gt 3) {
    Write-Host "🟡 VALIDATION PASSED with warnings" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "**Recommendation:** Fix warnings for better performance"
    Write-Host "**Status:** Can handoff, but consider improvements"
    exit 0
} else {
    Write-Host "✅ VALIDATION PASSED" -ForegroundColor Green
    Write-Host ""
    Write-Host "**Status:** Ready for handoff to Network/QA"
    exit 0
}

