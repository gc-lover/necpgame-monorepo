# Issue: Add build target to all services without it
# Добавляет build target в Makefile для сервисов которые его не имеют

$ErrorActionPreference = "Continue"

$ServicesDir = "services"
$Added = 0
$Skipped = 0
$Errors = 0

Write-Host "Adding build target to Makefiles..."
Write-Host ""

$ServiceDirs = Get-ChildItem $ServicesDir -Directory | Where-Object { 
    $_.Name -like "*-go" 
}

foreach ($ServiceDir in $ServiceDirs) {
    $MakefilePath = Join-Path $ServiceDir.FullName "Makefile"
    
    if (-not (Test-Path $MakefilePath)) {
        $Skipped++
        continue
    }
    
    $Lines = Get-Content $MakefilePath
    $hasBuild = $false
    
    foreach ($line in $Lines) {
        if ($line -match '^build:') {
            $hasBuild = $true
            break
        }
    }
    
    if ($hasBuild) {
        $Skipped++
        continue
    }
    
    Write-Host "  [ADD] $($ServiceDir.Name)" -ForegroundColor Yellow
    
    try {
        # Extract SERVICE_NAME if exists
        $serviceName = $null
        foreach ($line in $Lines) {
            if ($line -match 'SERVICE_NAME\s*:=\s*(.+)') {
                $serviceName = $Matches[1].Trim()
                break
            }
        }
        
        # If no SERVICE_NAME, extract from directory name
        if (-not $serviceName) {
            $serviceName = $ServiceDir.Name -replace '-go$', ''
        }
        
        # Find where to add build target (after clean or at the end)
        $NewLines = @()
        $BuildAdded = $false
        $AfterClean = $false
        
        for ($i = 0; $i -lt $Lines.Count; $i++) {
            $Line = $Lines[$i]
            $NewLines += $Line
            
            # Add build after clean target
            if ($Line -match '^clean:' -and -not $BuildAdded) {
                $AfterClean = $true
                # Find end of clean target
                $j = $i + 1
                while ($j -lt $Lines.Count -and ($Lines[$j] -match '^\t' -or $Lines[$j] -eq '')) {
                    $j++
                }
                # Insert build target
                if ($j -lt $Lines.Count) {
                    $NewLines += ""
                    $NewLines += ".PHONY: test bench-quick build"
                    $NewLines += ""
                    $NewLines += "# Run tests"
                    $NewLines += "test:"
                    $NewLines += "	@go test -v ./..."
                    $NewLines += ""
                    $NewLines += "# Quick benchmark (short duration)"
                    $NewLines += "bench-quick:"
                    $NewLines += "	@if [ -f `"server/handlers_bench_test.go`" ] || find . -name `"*_bench_test.go`" | grep -q .; then \"
                $NewLines += "		go test -run=^\$\$ -bench=. -benchmem -benchtime=100ms ./server; \"
                $NewLines += "	fi"
                    $NewLines += ""
                    $NewLines += "# Build (runs tests and benchmarks first)"
                    $NewLines += "build: test bench-quick"
                    $NewLines += "	@CGO_ENABLED=0 go build -ldflags=`"-w -s`" -o bin/$serviceName ."
                    $BuildAdded = $true
                }
            }
        }
        
        # If not added after clean, add at the end
        if (-not $BuildAdded) {
            $NewLines += ""
            $NewLines += ".PHONY: test bench-quick build"
            $NewLines += ""
            $NewLines += "# Run tests"
            $NewLines += "test:"
            $NewLines += "	@go test -v ./..."
            $NewLines += ""
            $NewLines += "# Quick benchmark (short duration)"
            $NewLines += "bench-quick:"
            $NewLines += "	@if [ -f `"server/handlers_bench_test.go`" ] || find . -name `"*_bench_test.go`" | grep -q .; then \"
            $NewLines += "		go test -run=^\$\$ -bench=. -benchmem -benchtime=100ms ./server; \"
            $NewLines += "	fi"
            $NewLines += ""
            $NewLines += "# Build (runs tests and benchmarks first)"
            $NewLines += "build: test bench-quick"
            $NewLines += "	@CGO_ENABLED=0 go build -ldflags=`"-w -s`" -o bin/$serviceName ."
        }
        
        # Ensure bench-quick exists if not already there
        $hasBenchQuick = $false
        foreach ($line in $NewLines) {
            if ($line -match 'bench-quick:') {
                $hasBenchQuick = $true
                break
            }
        }
        
        # Write back
        Set-Content -Path $MakefilePath -Value ($NewLines -join "`n") -NoNewline
        $Added++
        
    } catch {
        Write-Host "    [ERROR] $_" -ForegroundColor Red
        $Errors++
    }
}

Write-Host ""
Write-Host "Update complete!" -ForegroundColor Green
Write-Host "  Added build target: $Added" -ForegroundColor Green
Write-Host "  Skipped (already has): $Skipped" -ForegroundColor Gray
Write-Host "  Errors: $Errors" -ForegroundColor $(if ($Errors -gt 0) { "Red" } else { "Gray" })

