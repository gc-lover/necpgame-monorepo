# Apply split code generation to all 16 services with OpenAPI specs

$services = @(
    @{Name="battle-pass-service-go"; Spec="battle-pass-service.yaml"}
    @{Name="leaderboard-service-go"; Spec="leaderboard-service.yaml"}
    @{Name="maintenance-service-go"; Spec="maintenance-service.yaml"}
    @{Name="stock-dividends-service-go"; Spec="stock-dividends-service.yaml"}
    @{Name="stock-events-service-go"; Spec="stock-events-service.yaml"}
    @{Name="stock-indices-service-go"; Spec="stock-indices-service.yaml"}
    @{Name="stock-protection-service-go"; Spec="stock-protection-service.yaml"}
    @{Name="combat-sessions-service-go"; Spec="combat-sessions-service.yaml"}
    @{Name="gameplay-service-go"; Spec="gameplay-service.yaml"}
    @{Name="league-service-go"; Spec="league-service.yaml"}
    @{Name="social-player-orders-service-go"; Spec="social-player-orders-service.yaml"}
    @{Name="housing-service-go"; Spec="housing-service.yaml"}
    @{Name="companion-service-go"; Spec="companion-service.yaml"}
    @{Name="world-service-go"; Spec="world-service.yaml"}
    @{Name="referral-service-go"; Spec="referral-service.yaml"}
    @{Name="social-service-go"; Spec="social-service.yaml"}
    @{Name="cosmetic-service-go"; Spec="cosmetic-service.yaml"}
)

$successCount = 0
$failCount = 0
$results = @()

Write-Host "`n🔧 Applying split generation to $($services.Count) services...`n" -ForegroundColor Cyan

foreach ($svc in $services) {
    $svcName = $svc.Name
    $specName = $svc.Spec
    $svcPath = "services\$svcName"
    
    if (-not (Test-Path $svcPath)) {
        Write-Host "⏭️  Skipping $svcName (not found)" -ForegroundColor Gray
        continue
    }
    
    Write-Host "$svcName..." -NoNewline
    
    try {
        cd $svcPath
        
        # Bundle spec
        $bundleResult = npx -y @redocly/cli bundle "../../proto/openapi/$specName" -o "pkg/api/$specName.bundled.yaml" 2>&1
        if ($LASTEXITCODE -ne 0) {
            throw "Bundle failed"
        }
        
        # Generate split files
        oapi-codegen -package api -generate types -o pkg/api/types.gen.go "pkg/api/$specName.bundled.yaml" 2>&1 | Out-Null
        oapi-codegen -package api -generate chi-server -o pkg/api/server.gen.go "pkg/api/$specName.bundled.yaml" 2>&1 | Out-Null
        oapi-codegen -package api -generate spec -o pkg/api/spec.gen.go "pkg/api/$specName.bundled.yaml" 2>&1 | Out-Null
        
        # Check file sizes
        $typesLines = (Get-Content pkg/api/types.gen.go).Count
        $serverLines = (Get-Content pkg/api/server.gen.go).Count
        $specLines = (Get-Content pkg/api/spec.gen.go).Count
        
        $maxLines = [Math]::Max([Math]::Max($typesLines, $serverLines), $specLines)
        
        # Remove old api.gen.go
        if (Test-Path pkg/api/api.gen.go) {
            Remove-Item pkg/api/api.gen.go -Force
        }
        
        # Update Makefile to chi-server
        (Get-Content Makefile) -replace "ROUTER_TYPE := gorilla-server", "ROUTER_TYPE := chi-server" | Set-Content Makefile
        
        # Add chi dependency
        go get github.com/go-chi/chi/v5 2>&1 | Out-Null
        go mod tidy 2>&1 | Out-Null
        
        # Test compilation
        $null = go build ./... 2>&1
        $compiles = $LASTEXITCODE -eq 0
        
        cd ..\..
        
        if ($maxLines -le 500 -and $compiles) {
            Write-Host " OK (types=$typesLines | server=$serverLines | spec=$specLines)" -ForegroundColor Green
            $successCount++
            $results += [PSCustomObject]@{Service=$svcName; Status="OK OK"; MaxLines=$maxLines; Compiles=$true}
        }
        elseif ($maxLines -le 500) {
            Write-Host " WARNING  Generated but needs handler update (max=$maxLines)" -ForegroundColor Yellow
            $results += [PSCustomObject]@{Service=$svcName; Status="WARNING Handlers"; MaxLines=$maxLines; Compiles=$false}
        }
        else {
            Write-Host " ❌ File too large (max=$maxLines)" -ForegroundColor Red
            $failCount++
            $results += [PSCustomObject]@{Service=$svcName; Status="❌ Too large"; MaxLines=$maxLines; Compiles=$false}
        }
    }
    catch {
        Write-Host " ❌ Error: $($_.Exception.Message)" -ForegroundColor Red
        $failCount++
        $results += [PSCustomObject]@{Service=$svcName; Status="❌ Error"; MaxLines=0; Compiles=$false}
        cd ..\..
    }
}

Write-Host "`n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
Write-Host "SUMMARY" -ForegroundColor Cyan
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
Write-Host "OK Success: $successCount services" -ForegroundColor Green
Write-Host "WARNING  Needs handlers: $(($results | Where-Object {$_.Status -eq 'WARNING Handlers'}).Count) services" -ForegroundColor Yellow
Write-Host "❌ Failed: $failCount services" -ForegroundColor Red

Write-Host "`nDetailed results:" -ForegroundColor White
$results | Format-Table -AutoSize

