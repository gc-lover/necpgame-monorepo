# Start all 17 services in Docker and test health

$services = @(
    @{Name="maintenance-service"; Port=8125},
    @{Name="stock-dividends-service"; Port=8146},
    @{Name="stock-events-service"; Port=8147},
    @{Name="stock-indices-service"; Port=8149},
    @{Name="stock-protection-service"; Port=8152},
    @{Name="battle-pass-service"; Port=8102},
    @{Name="leaderboard-service"; Port=8124},
    @{Name="combat-sessions-service"; Port=8158},
    @{Name="gameplay-service"; Port=8120},
    @{Name="league-service"; Port=8157},
    @{Name="social-player-orders-service"; Port=8156},
    @{Name="housing-service"; Port=8122},
    @{Name="companion-service"; Port=8116},
    @{Name="world-service"; Port=8155},
    @{Name="referral-service"; Port=8134},
    @{Name="social-service"; Port=8143},
    @{Name="cosmetic-service"; Port=8117}
)

Write-Host "`n🐳 Starting all 17 services in Docker...`n" -ForegroundColor Cyan

$containers = @()

foreach ($svc in $services) {
    $name = $svc.Name
    $port = $svc.Port
    
    Write-Host "Starting $name..." -NoNewline
    
    $containerId = docker run -d --name $name -p "${port}:${port}" -e ADDR="0.0.0.0:${port}" "necpgame/${name}:latest" 2>&1
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host " OK" -ForegroundColor Green
        $containers += @{Name=$name; Port=$port; Id=$containerId}
    }
    else {
        Write-Host " ❌" -ForegroundColor Red
    }
}

Write-Host "`n⏳ Waiting for services to start (10 seconds)...`n" -ForegroundColor Yellow
Start-Sleep -Seconds 10

Write-Host "🔍 Checking health endpoints...`n" -ForegroundColor Cyan

$healthyCount = 0

foreach ($c in $containers) {
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:$($c.Port)/health" -Method GET -TimeoutSec 3 -ErrorAction Stop
        if ($response.StatusCode -eq 200) {
            Write-Host "OK $($c.Name) (:$($c.Port))" -ForegroundColor Green
            $healthyCount++
        }
    }
    catch {
        Write-Host "❌ $($c.Name) (:$($c.Port)) - not responding" -ForegroundColor Red
    }
}

Write-Host "`n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
Write-Host "HEALTHY: $healthyCount / $($containers.Count)" -ForegroundColor $(if ($healthyCount -eq $containers.Count) {"Green"} else {"Yellow"})
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━`n" -ForegroundColor Cyan

if ($healthyCount -eq $containers.Count) {
    Write-Host "🎉 All Docker services are healthy!`n" -ForegroundColor Green
}

Write-Host "Press Enter to stop all containers..." -ForegroundColor Yellow
Read-Host

Write-Host "`n🛑 Stopping and removing containers...`n" -ForegroundColor Yellow

foreach ($c in $containers) {
    docker stop $c.Name 2>&1 | Out-Null
    docker rm $c.Name 2>&1 | Out-Null
    Write-Host "Removed: $($c.Name)" -ForegroundColor Gray
}

Write-Host "`nOK All containers stopped and removed`n" -ForegroundColor Green

