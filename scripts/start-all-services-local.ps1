# Start all 17 services locally for testing

$services = @(
    @{Name="maintenance-service-go"; Port=8125}
    @{Name="stock-dividends-service-go"; Port=8146}
    @{Name="stock-events-service-go"; Port=8147}
    @{Name="stock-indices-service-go"; Port=8149}
    @{Name="stock-protection-service-go"; Port=8152}
    @{Name="battle-pass-service-go"; Port=8102}
    @{Name="leaderboard-service-go"; Port=8124}
    @{Name="combat-sessions-service-go"; Port=8158}
    @{Name="gameplay-service-go"; Port=8120}
    @{Name="league-service-go"; Port=8157}
    @{Name="social-player-orders-service-go"; Port=8156}
    @{Name="housing-service-go"; Port=8122}
    @{Name="companion-service-go"; Port=8116}
    @{Name="world-service-go"; Port=8155}
    @{Name="referral-service-go"; Port=8134}
    @{Name="social-service-go"; Port=8143}
    @{Name="cosmetic-service-go"; Port=8117}
)

Write-Host "`n🚀 Starting all 17 services locally...`n" -ForegroundColor Cyan

$jobs = @()

foreach ($svc in $services) {
    $svcName = $svc.Name
    $port = $svc.Port
    
    Write-Host "Starting $svcName on :$port..." -ForegroundColor White
    
    $job = Start-Job -ScriptBlock {
        param($svcPath, $port)
        Set-Location $svcPath
        $env:ADDR = "0.0.0.0:$port"
        go run . 2>&1
    } -ArgumentList "C:\NECPGAME\services\$svcName", $port -Name $svcName
    
    $jobs += @{Job=$job; Name=$svcName; Port=$port}
    
    Start-Sleep -Milliseconds 500
}

Write-Host "`n⏳ Waiting for services to start (10 seconds)...`n" -ForegroundColor Yellow
Start-Sleep -Seconds 10

Write-Host "🔍 Checking health endpoints...`n" -ForegroundColor Cyan

$healthyCount = 0

foreach ($svc in $services) {
    $port = $svc.Port
    $name = $svc.Name
    
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:$port/health" -Method GET -TimeoutSec 2 -ErrorAction Stop
        if ($response.StatusCode -eq 200) {
            Write-Host "OK $name (:$port) - healthy" -ForegroundColor Green
            $healthyCount++
        }
        else {
            Write-Host "WARNING  $name (:$port) - returned $($response.StatusCode)" -ForegroundColor Yellow
        }
    }
    catch {
        Write-Host "❌ $name (:$port) - not responding" -ForegroundColor Red
    }
}

Write-Host "`n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
Write-Host "HEALTHY: $healthyCount / $($services.Count)" -ForegroundColor $(if ($healthyCount -eq $services.Count) {"Green"} else {"Yellow"})
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━`n" -ForegroundColor Cyan

if ($healthyCount -eq $services.Count) {
    Write-Host "🎉 All services are healthy!`n" -ForegroundColor Green
}

Write-Host "Jobs running. Press Enter to stop all services..." -ForegroundColor Yellow
Read-Host

Write-Host "`n🛑 Stopping all services...`n" -ForegroundColor Yellow

foreach ($jobInfo in $jobs) {
    Stop-Job $jobInfo.Job -ErrorAction SilentlyContinue
    Remove-Job $jobInfo.Job -Force -ErrorAction SilentlyContinue
    Write-Host "Stopped: $($jobInfo.Name)" -ForegroundColor Gray
}

Write-Host "`nOK All services stopped`n" -ForegroundColor Green

