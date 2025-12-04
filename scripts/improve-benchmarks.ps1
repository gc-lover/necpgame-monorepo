# Issue: Improve all benchmarks with real handler calls
# Анализирует handlers.go и создает правильные бенчмарки

$ErrorActionPreference = "Continue"
$ServicesDir = "services"
$Fixed = 0
$Errors = 0
$Skipped = 0

Write-Host "Improving benchmarks for all services..." -ForegroundColor Cyan
Write-Host ""

$ServiceDirs = Get-ChildItem $ServicesDir -Directory | Where-Object { 
    $_.Name -like "*-go" -or $_.Name -like "*-service-go" 
}

foreach ($ServiceDir in $ServiceDirs) {
    $ServiceName = $ServiceDir.Name
    $HandlersFile = Join-Path $ServiceDir.FullName "server\handlers.go"
    $BenchFile = Join-Path $ServiceDir.FullName "server\handlers_bench_test.go"
    
    # Пропускаем если нет handlers.go или нет бенчмарка
    if (-not (Test-Path $HandlersFile) -or -not (Test-Path $BenchFile)) {
        $Skipped++
        continue
    }
    
    Write-Host "  Processing: $ServiceName" -ForegroundColor Yellow
    
    try {
        $HandlersContent = Get-Content $HandlersFile -Raw
        
        # Определяем API package
        $ApiPackage = ""
        if ($HandlersContent -match 'api\s+"([^"]+)"') {
            $ApiPackage = $Matches[1]
        } elseif ($HandlersContent -match 'api\s+([^\s]+)') {
            $ApiPackage = $Matches[1]
        }
        
        # Определяем сигнатуру NewHandlers
        $NewHandlersSig = ""
        if ($HandlersContent -match 'func NewHandlers\(([^)]+)\)') {
            $NewHandlersSig = $Matches[1]
        }
        
        # Находим методы handlers
        $HandlerMethods = @()
        if ($HandlersContent -match 'func \(h \*Handlers\) (\w+)\([^)]+\)') {
            $HandlerMethods += $Matches[1]
        }
        $AllMatches = [regex]::Matches($HandlersContent, 'func \(h \*Handlers\) (\w+)\([^)]+\)')
        foreach ($match in $AllMatches) {
            if ($match.Groups[1].Value -notin $HandlerMethods) {
                $HandlerMethods += $match.Groups[1].Value
            }
        }
        
        # Берем первые 3 метода для бенчмарков
        $MethodsToBench = $HandlerMethods | Select-Object -First 3
        
        if ($MethodsToBench.Count -eq 0) {
            Write-Host "    Warning: No handler methods found" -ForegroundColor Yellow
            $Skipped++
            continue
        }
        
        # Генерируем улучшенный бенчмарк
        $BenchContent = "// Issue: Performance benchmarks`n"
        $BenchContent += "package server`n`n"
        $BenchContent += "import (`n"
        $BenchContent += "	`"context`"`n"
        $BenchContent += "	`"testing`"`n`n"
        
        if ($ApiPackage) {
            $BenchContent += "	`"$ApiPackage`"`n"
        }
        $BenchContent += "	`"github.com/google/uuid`"`n"
        $BenchContent += ")`n`n"
        
        # Генерируем бенчмарки для каждого метода
        $BenchIndex = 1
        foreach ($Method in $MethodsToBench) {
            $BenchContent += "// Benchmark$Method benchmarks $Method handler`n"
            $BenchContent += "// Target: <100μs per operation, minimal allocs`n"
            $BenchContent += "func Benchmark$Method(b *testing.B) {`n"
            
            # Определяем инициализацию handlers
            if ($NewHandlersSig -match "logger") {
                $BenchContent += "	logger := GetLogger()`n"
                $BenchContent += "	handlers := NewHandlers(logger)`n"
            } elseif ($NewHandlersSig -match "service") {
                $BenchContent += "	service := NewService(nil)`n"
                $BenchContent += "	handlers := NewHandlers(service)`n"
            } elseif ($NewHandlersSig -match "redis") {
                $BenchContent += "	handlers := NewHandlers(nil)`n"
            } else {
                $BenchContent += "	handlers := NewHandlers()`n"
            }
            
            $BenchContent += "`n"
            $BenchContent += "	ctx := context.Background()`n"
            
            # Определяем параметры метода
            if ($Method -match "Get|List") {
                $BenchContent += "	params := api.${Method}Params{`n"
                if ($Method -match "Get") {
                    $BenchContent += "		ID: uuid.New(),`n"
                }
                $BenchContent += "	}`n`n"
                $BenchContent += "	b.ReportAllocs()`n"
                $BenchContent += "	b.ResetTimer()`n`n"
                $BenchContent += "	for i := 0; i < b.N; i++ {`n"
                $BenchContent += "		_, _ = handlers.$Method(ctx, params)`n"
            } elseif ($Method -match "Create|Update|Post") {
                $BenchContent += "	req := &api.${Method}Request{`n"
                $BenchContent += "		// TODO: Fill request fields`n"
                $BenchContent += "	}`n`n"
                $BenchContent += "	b.ReportAllocs()`n"
                $BenchContent += "	b.ResetTimer()`n`n"
                $BenchContent += "	for i := 0; i < b.N; i++ {`n"
                $BenchContent += "		_, _ = handlers.$Method(ctx, req)`n"
            } else {
                $BenchContent += "	b.ReportAllocs()`n"
                $BenchContent += "	b.ResetTimer()`n`n"
                $BenchContent += "	for i := 0; i < b.N; i++ {`n"
                $BenchContent += "		_, _ = handlers.$Method(ctx)`n"
            }
            
            $BenchContent += "	}`n"
            $BenchContent += "}`n`n"
            $BenchIndex++
        }
        
        # Сохраняем
        $BenchContent | Out-File -FilePath $BenchFile -Encoding UTF8 -NoNewline
        
        Write-Host "    Fixed: $BenchFile" -ForegroundColor Green
        $Fixed++
        
    } catch {
        Write-Host "    Error: $_" -ForegroundColor Red
        $Errors++
    }
}

Write-Host ""
Write-Host "Summary:" -ForegroundColor Cyan
Write-Host "  Fixed: $Fixed" -ForegroundColor Green
Write-Host "  Skipped: $Skipped" -ForegroundColor Yellow
Write-Host "  Errors: $Errors" -ForegroundColor Red

