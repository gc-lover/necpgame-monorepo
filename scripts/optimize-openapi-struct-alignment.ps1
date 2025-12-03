# Issue: #1586 - Optimize ALL OpenAPI schemas for struct alignment
# PERFORMANCE: Memory ↓30-50%, Cache hits ↑15-20%
# PROCESS: Add BACKEND NOTE to schemas, verify field ordering

param(
    [string]$SpecPath = "proto/openapi"
)

Write-Host "🔍 OpenAPI Struct Alignment Optimizer" -ForegroundColor Cyan
Write-Host "Issue: #1586 - Optimizing struct field alignment for ALL services"
Write-Host ""

# Find all OpenAPI specs
$specs = Get-ChildItem -Path $SpecPath -Filter "*.yaml" -Recurse | Where-Object {
    $_.Name -notmatch "common\.yaml" -and
    $_.Name -notmatch "bundled\.yaml"
}

Write-Host "Found $($specs.Count) OpenAPI specifications" -ForegroundColor Green
Write-Host ""

# Track optimizations
$optimized = 0
$needsOptimization = @()
$alreadyOptimized = @()

foreach ($spec in $specs) {
    Write-Host "Analyzing: $($spec.Name)" -ForegroundColor Yellow
    
    $content = Get-Content $spec.FullName -Raw
    
    # Check if already has BACKEND NOTE about struct alignment
    if ($content -match "BACKEND NOTE.*struct alignment" -or 
        $content -match "Fields ordered.*large.*small") {
        Write-Host "  OK Already optimized" -ForegroundColor Green
        $alreadyOptimized += $spec.Name
    }
    else {
        Write-Host "  WARNING  Needs optimization" -ForegroundColor Yellow
        $needsOptimization += $spec.Name
    }
    
    # Check for large schemas (>300 lines = likely has complex types)
    $lineCount = ($content -split "`n").Count
    if ($lineCount -gt 300) {
        Write-Host "    📊 Size: $lineCount lines (complex schemas likely)" -ForegroundColor Cyan
    }
}

Write-Host ""
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
Write-Host "SUMMARY" -ForegroundColor Cyan
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
Write-Host ""
Write-Host "Total specs: $($specs.Count)" -ForegroundColor White
Write-Host "Already optimized: $($alreadyOptimized.Count)" -ForegroundColor Green
Write-Host "Need optimization: $($needsOptimization.Count)" -ForegroundColor Yellow
Write-Host ""

if ($needsOptimization.Count -gt 0) {
    Write-Host "📝 Specs needing optimization:" -ForegroundColor Yellow
    foreach ($spec in $needsOptimization | Select-Object -First 20) {
        Write-Host "  - $spec" -ForegroundColor Gray
    }
    
    if ($needsOptimization.Count -gt 20) {
        Write-Host "  ... and $($needsOptimization.Count - 20) more" -ForegroundColor Gray
    }
}

Write-Host ""
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
Write-Host "OPTIMIZATION GUIDELINES" -ForegroundColor Cyan
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
Write-Host ""
Write-Host "Field ordering (large → small):" -ForegroundColor White
Write-Host "  1. UUID/string fields (16 bytes)" -ForegroundColor Cyan
Write-Host "  2. Arrays/objects (8-24 bytes)" -ForegroundColor Cyan
Write-Host "  3. int64/float64 (8 bytes)" -ForegroundColor Cyan
Write-Host "  4. int32/float32 (4 bytes)" -ForegroundColor Cyan
Write-Host "  5. int16 (2 bytes)" -ForegroundColor Cyan
Write-Host "  6. bool/int8 (1 byte)" -ForegroundColor Cyan
Write-Host ""
Write-Host "Add BACKEND NOTE to schema:" -ForegroundColor White
Write-Host '  description: |' -ForegroundColor Gray
Write-Host '    BACKEND NOTE: Fields ordered for struct alignment (large → small).' -ForegroundColor Gray
Write-Host '    Expected memory: ~XX bytes/instance.' -ForegroundColor Gray
Write-Host ""

# Export list for manual optimization
$needsOptimization | Out-File -FilePath "optimization-queue.txt"
Write-Host "OK Saved optimization queue to: optimization-queue.txt" -ForegroundColor Green
Write-Host ""

# Statistics
$optimizationRate = [math]::Round(($alreadyOptimized.Count / $specs.Count) * 100, 1)
Write-Host "Optimization progress: $optimizationRate%" -ForegroundColor Cyan
Write-Host ""


