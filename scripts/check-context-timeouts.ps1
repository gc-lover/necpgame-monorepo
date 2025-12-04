# Issue: #1604 - Check Context Timeouts
# Проверяет наличие context timeouts в Go сервисах

param(
    [string]$ServicePath = "services"
)

Write-Host "Checking context timeouts in Go services..." -ForegroundColor Cyan

$servicesWithTimeouts = @()
$servicesWithoutTimeouts = @()
$totalHandlers = 0
$handlersWithTimeouts = 0

Get-ChildItem -Path $ServicePath -Directory | ForEach-Object {
    $serviceDir = $_.FullName
    $serviceName = $_.Name
    
    # Ищем handlers.go файлы
    $handlersFiles = Get-ChildItem -Path $serviceDir -Recurse -Filter "handlers.go" -ErrorAction SilentlyContinue
    
    if ($handlersFiles.Count -eq 0) {
        return
    }
    
    $hasTimeouts = $false
    $hasHandlers = $false
    
    foreach ($file in $handlersFiles) {
        $content = Get-Content $file.FullName -Raw
        
        # Проверяем наличие context.WithTimeout
        if ($content -match "context\.WithTimeout") {
            $hasTimeouts = $true
        }
        
        # Проверяем наличие handler функций с context
        if ($content -match "func.*\(.*context\.Context") {
            $hasHandlers = $true
            $totalHandlers++
            
            # Проверяем, есть ли timeout в этой функции
            $lines = Get-Content $file.FullName
            $inHandler = $false
            $handlerHasTimeout = $false
            
            foreach ($line in $lines) {
                if ($line -match "func.*\(.*context\.Context") {
                    $inHandler = $true
                    $handlerHasTimeout = $false
                }
                
                if ($inHandler -and $line -match "context\.WithTimeout") {
                    $handlerHasTimeout = $true
                    $handlersWithTimeouts++
                }
                
                if ($inHandler -and $line -match "^\s*}\s*$" -and $line -notmatch "defer") {
                    $inHandler = $false
                    if (-not $handlerHasTimeout) {
                        Write-Host "  WARNING  $($file.Name): Handler without timeout" -ForegroundColor Yellow
                    }
                }
            }
        }
    }
    
    if ($hasTimeouts) {
        $servicesWithTimeouts += $serviceName
    } elseif ($hasHandlers) {
        $servicesWithoutTimeouts += $serviceName
        Write-Host "❌ $serviceName - NO timeouts" -ForegroundColor Red
    }
}

Write-Host "`n=== Summary ===" -ForegroundColor Cyan
Write-Host "Services WITH timeouts: $($servicesWithTimeouts.Count)" -ForegroundColor Green
Write-Host "Services WITHOUT timeouts: $($servicesWithoutTimeouts.Count)" -ForegroundColor Red
Write-Host "Total handlers: $totalHandlers" -ForegroundColor Cyan
Write-Host "Handlers with timeouts: $handlersWithTimeouts" -ForegroundColor Green

if ($servicesWithoutTimeouts.Count -gt 0) {
    Write-Host "`nServices needing timeouts:" -ForegroundColor Yellow
    $servicesWithoutTimeouts | ForEach-Object {
        Write-Host "  - $_" -ForegroundColor Yellow
    }
}

