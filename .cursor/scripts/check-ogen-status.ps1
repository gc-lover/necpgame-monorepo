# Check ogen migration status for all services
# PowerShell version for Windows

Write-Host "🔍 Checking ogen migration status..." -ForegroundColor Cyan
Write-Host ""

$ServicesDir = "services"
$Total = 0
$OgenCount = 0
$OapiCount = 0
$UnknownCount = 0

$OgenServices = @()
$OapiServices = @()
$UnknownServices = @()

# Get all *-go directories
Get-ChildItem -Path $ServicesDir -Directory -Filter "*-go" | ForEach-Object {
    $service = $_.Name
    $makefile = Join-Path $_.FullName "Makefile"
    
    if (Test-Path $makefile) {
        $Total++
        
        $content = Get-Content $makefile -Raw
        
        if ($content -match "ogen") {
            $OgenCount++
            $OgenServices += $service
        }
        elseif ($content -match "oapi-codegen") {
            $OapiCount++
            $OapiServices += $service
        }
        else {
            $UnknownCount++
            $UnknownServices += $service
        }
    }
}

Write-Host "📊 Migration Statistics:" -ForegroundColor Yellow
Write-Host "========================"
Write-Host ""
Write-Host "Total Services: $Total"
Write-Host "OK Migrated to ogen: $OgenCount ($([math]::Round($OgenCount * 100 / $Total))%)" -ForegroundColor Green
Write-Host "❌ Still on oapi-codegen: $OapiCount ($([math]::Round($OapiCount * 100 / $Total))%)" -ForegroundColor Red
Write-Host "WARNING  Unknown: $UnknownCount" -ForegroundColor Yellow
Write-Host ""

if ($OgenCount -gt 0) {
    Write-Host "OK Migrated Services ($OgenCount):" -ForegroundColor Green
    $OgenServices | Sort-Object | ForEach-Object {
        Write-Host "  - $_" -ForegroundColor Green
    }
    Write-Host ""
}

if ($OapiCount -gt 0) {
    Write-Host "❌ Services Remaining ($OapiCount):" -ForegroundColor Red
    
    # Categorize by priority
    Write-Host ""
    Write-Host "🔴 HIGH PRIORITY (Combat & Movement):" -ForegroundColor Red
    $OapiServices | Where-Object {
        $_ -match "^combat-" -or 
        $_ -match "^movement-" -or 
        $_ -match "^world-" -or 
        $_ -match "^weapon-" -or 
        $_ -match "^projectile-" -or 
        $_ -match "^hacking-" -or 
        $_ -match "^gameplay-weapon-"
    } | Sort-Object | ForEach-Object {
        Write-Host "  - $_" -ForegroundColor Red
    }
    
    Write-Host ""
    Write-Host "🟡 MEDIUM PRIORITY (Quest, Chat, Core):" -ForegroundColor Yellow
    $OapiServices | Where-Object {
        $_ -match "^quest-" -or 
        $_ -match "^chat-" -or 
        $_ -match "^social-" -or 
        $_ -match "^achievement-" -or 
        $_ -match "^leaderboard-" -or 
        $_ -match "^league-" -or 
        $_ -match "^loot-" -or 
        $_ -match "^gameplay-service-" -or 
        $_ -match "^progression-" -or 
        $_ -match "^battle-pass-" -or 
        $_ -match "^seasonal-" -or 
        $_ -match "^companion-" -or 
        $_ -match "^cosmetic-" -or 
        $_ -match "^housing-" -or 
        $_ -match "^mail-" -or 
        $_ -match "^referral-"
    } | Sort-Object | ForEach-Object {
        Write-Host "  - $_" -ForegroundColor Yellow
    }
    
    Write-Host ""
    Write-Host "🟢 LOW PRIORITY (Admin, Economy, Legacy):" -ForegroundColor Green
    $OapiServices | Where-Object {
        $_ -match "^admin-" -or 
        $_ -match "^support-" -or 
        $_ -match "^maintenance-" -or 
        $_ -match "^feedback-" -or 
        $_ -match "^clan-" -or 
        $_ -match "^faction-" -or 
        $_ -match "^reset-" -or 
        $_ -match "^client-" -or 
        $_ -match "^realtime-" -or 
        $_ -match "^ws-" -or 
        $_ -match "^voice-" -or 
        $_ -match "^matchmaking-go$" -or 
        $_ -match "^character-engram-" -or 
        $_ -match "^stock-" -or 
        $_ -match "^economy-" -or 
        $_ -match "^trade-"
    } | Sort-Object | ForEach-Object {
        Write-Host "  - $_" -ForegroundColor Green
    }
    Write-Host ""
}

if ($UnknownCount -gt 0) {
    Write-Host "WARNING  Unknown Services ($UnknownCount):" -ForegroundColor Yellow
    $UnknownServices | Sort-Object | ForEach-Object {
        Write-Host "  - $_" -ForegroundColor Yellow
    }
    Write-Host ""
}

Write-Host "📈 Progress: $OgenCount/$Total ($([math]::Round($OgenCount * 100 / $Total))%)" -ForegroundColor Cyan
Write-Host ""
Write-Host "🎯 Next Steps:" -ForegroundColor Cyan
if ($OapiCount -gt 0) {
    Write-Host "  1. Review High Priority services (combat, movement, world)"
    Write-Host "  2. See .cursor/OGEN_MIGRATION_GUIDE.md for migration steps"
    Write-Host "  3. Track progress in GitHub Issues #1595-#1602"
    Write-Host "  4. Main tracker: Issue #1603"
}
else {
    Write-Host "  🎉 All services migrated to ogen!" -ForegroundColor Green
}
Write-Host ""

Write-Host "💡 Tip: Run 'make generate-api' in each service to regenerate with ogen" -ForegroundColor Cyan
Write-Host ""

