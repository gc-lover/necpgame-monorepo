# Issue: Update all Makefiles to run tests and benchmarks on build
# Обновляет все Makefile чтобы build запускал тесты и бенчмарки

$ErrorActionPreference = "Continue"

$ServicesDir = "services"
$Updated = 0
$Skipped = 0
$Errors = 0

Write-Host "Updating Makefiles to run tests and benchmarks on build..." -ForegroundColor Cyan
Write-Host ""

$ServiceDirs = Get-ChildItem $ServicesDir -Directory | Where-Object { 
    $_.Name -like "*-go" 
}

foreach ($ServiceDir in $ServiceDirs) {
    $MakefilePath = Join-Path $ServiceDir.FullName "Makefile"
    
    if (-not (Test-Path $MakefilePath)) {
        Write-Host "  [SKIP] $($ServiceDir.Name) - no Makefile" -ForegroundColor Yellow
        $Skipped++
        continue
    }
    
    $Lines = Get-Content $MakefilePath
    $Content = $Lines -join "`n"
    
    # Check if build target exists (check line by line)
    $hasBuild = $false
    $buildLine = $null
    foreach ($line in $Lines) {
        if ($line -match '^build:') {
            $hasBuild = $true
            $buildLine = $line
            break
        }
    }
    
    if (-not $hasBuild) {
        Write-Host "  [SKIP] $($ServiceDir.Name) - no build target" -ForegroundColor Yellow
        $Skipped++
        continue
    }
    
    # Check if already has test/bench in build
    if ($buildLine -match 'test|bench') {
        Write-Host "  [OK] $($ServiceDir.Name) - already has tests/benchmarks" -ForegroundColor Gray
        $Skipped++
        continue
    }
    
    Write-Host "  [UPDATE] $($ServiceDir.Name)" -ForegroundColor Yellow
    
    try {
        # Find build target and update it (Lines already loaded above)
        $NewLines = @()
        $BuildUpdated = $false
        
        for ($i = 0; $i -lt $Lines.Count; $i++) {
            $Line = $Lines[$i]
            
            # Detect build target start
            if ($Line -match '^build:') {
                # Extract dependencies
                $Deps = $Line -replace '^build:\s*', ''
                
                # Add test and bench-quick as dependencies
                if ($Deps) {
                    $NewLines += "build: test bench-quick $Deps"
                } else {
                    $NewLines += "build: test bench-quick"
                }
                $BuildUpdated = $true
                continue
            }
            
            $NewLines += $Line
        }
        
        # Ensure test and bench-quick targets exist
        $NewContent = $NewLines -join "`n"
        
        if ($NewContent -notmatch '^test:') {
            $NewContent += "`n`n.PHONY: test`ntest:`n	@go test -v ./...`n"
        }
        
        if ($NewContent -notmatch 'bench-quick:') {
            $NewContent += "`n`n.PHONY: bench-quick`nbench-quick:`n	@if [ -f `"server/handlers_bench_test.go`" ] || find . -name `"*_bench_test.go`" | grep -q .; then \"
            $NewContent += "		go test -run=^\$\$ -bench=. -benchmem -benchtime=100ms ./server; \"
            $NewContent += "	fi`n"
        }
        
        # Write back
        Set-Content -Path $MakefilePath -Value $NewContent -NoNewline
        $Updated++
        
    } catch {
        Write-Host "    [ERROR] $_" -ForegroundColor Red
        $Errors++
    }
}

Write-Host ""
Write-Host "Update complete!" -ForegroundColor Green
Write-Host "  Updated: $Updated" -ForegroundColor Green
Write-Host "  Skipped: $Skipped" -ForegroundColor Gray
Write-Host "  Errors: $Errors" -ForegroundColor $(if ($Errors -gt 0) { "Red" } else { "Gray" })

