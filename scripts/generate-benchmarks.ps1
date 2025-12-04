# Issue: Generate benchmarks for all services
# Автоматическая генерация бенчмарков для всех сервисов

$ErrorActionPreference = "Continue"
$ServicesDir = "services"

Write-Host "Finding services without benchmarks..." -ForegroundColor Cyan
Write-Host ""

$ServiceDirs = Get-ChildItem $ServicesDir -Directory | Where-Object { 
    $_.Name -like "*-go" -or $_.Name -like "*-service-go" 
}

$Created = 0
$Skipped = 0
$Errors = 0

foreach ($ServiceDir in $ServiceDirs) {
    $ServiceName = $ServiceDir.Name
    $BenchFile = Join-Path $ServiceDir.FullName "server\handlers_bench_test.go"
    
    # Пропускаем если уже есть бенчмарки
    if (Test-Path $BenchFile) {
        Write-Host "  Skipping $ServiceName - already has benchmarks" -ForegroundColor Gray
        $Skipped++
        continue
    }
    
    # Проверяем наличие handlers.go
    $HandlersFile = Join-Path $ServiceDir.FullName "server\handlers.go"
    if (-not (Test-Path $HandlersFile)) {
        Write-Host "  Warning: $ServiceName - no handlers.go found" -ForegroundColor Yellow
        $Skipped++
        continue
    }
    
    Write-Host "  Creating benchmarks for: $ServiceName" -ForegroundColor Yellow
    
    try {
        # Генерируем бенчмарк файл
        $BenchContent = @"
// Issue: Performance benchmarks
// Auto-generated benchmark file
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
)

// BenchmarkHandler benchmarks handler performance
// Target: <100μs per operation, minimal allocs
func BenchmarkHandler(b *testing.B) {
	// Setup - adjust based on service structure
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		// TODO: Add actual handler call based on service API
		// Example:
		// _, _ = handlers.Get(ctx, api.GetParams{ID: uuid.New()})
		_ = handlers
		_ = ctx
	}
}

"@
        
        # Создаем директорию если нужно
        $ServerDir = Join-Path $ServiceDir.FullName "server"
        if (-not (Test-Path $ServerDir)) {
            New-Item -ItemType Directory -Path $ServerDir -Force | Out-Null
        }
        
        # Сохраняем файл
        $BenchContent | Out-File -FilePath $BenchFile -Encoding UTF8 -NoNewline
        
        Write-Host "    Created: $BenchFile" -ForegroundColor Green
        $Created++
        
    } catch {
        Write-Host "    Error: $_" -ForegroundColor Red
        $Errors++
    }
}

Write-Host ""
Write-Host "Summary:" -ForegroundColor Cyan
Write-Host "  Created: $Created" -ForegroundColor Green
Write-Host "  Skipped: $Skipped" -ForegroundColor Yellow
Write-Host "  Errors: $Errors" -ForegroundColor Red
Write-Host ""
Write-Host "NOTE: Generated benchmarks need manual adjustment:" -ForegroundColor Yellow
Write-Host "  1. Add correct API imports" -ForegroundColor White
Write-Host "  2. Add actual handler calls" -ForegroundColor White
Write-Host "  3. Adjust service initialization" -ForegroundColor White
Write-Host "  4. Add proper request/response types" -ForegroundColor White
