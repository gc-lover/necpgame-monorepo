# NECP Game Services System Check Script
# Проверяет состояние всех сервисов и инфраструктуры

param(
    [switch]$Detailed
)

Write-Host "🔍 NECP Game Services - System Health Check" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan

# Function to check service health
function Check-Service {
    param(
        [string]$ServiceName,
        [int]$Port,
        [string]$Endpoint = "/health"
    )

    try {
        $response = Invoke-WebRequest -Uri "http://localhost:$Port$Endpoint" -TimeoutSec 5 -ErrorAction Stop
        if ($response.StatusCode -eq 200) {
            Write-Host "OK $ServiceName (port $Port)" -ForegroundColor Green
            return $true
        } else {
            Write-Host "❌ $ServiceName (port $Port) - Status: $($response.StatusCode)" -ForegroundColor Red
            return $false
        }
    }
    catch {
        Write-Host "❌ $ServiceName (port $Port) - Error: $($_.Exception.Message)" -ForegroundColor Red
        return $false
    }
}

# Function to check Docker container status
function Check-Container {
    param([string]$ContainerName)

    $status = docker ps --filter "name=$ContainerName" --format "{{.Status}}" 2>$null
    if ($LASTEXITCODE -eq 0 -and $status -like "*Up*") {
        Write-Host "OK $ContainerName`: $status" -ForegroundColor Green
        return $true
    } else {
        Write-Host "❌ $ContainerName`: $status" -ForegroundColor Red
        return $false
    }
}

Write-Host ""
Write-Host "🐳 Docker Infrastructure Status:" -ForegroundColor Yellow
Write-Host "--------------------------------" -ForegroundColor Yellow

# Check infrastructure
$infraHealthy = 0
$infraTotal = 3
if (Check-Container "necpgame-postgres") { $infraHealthy++ }
if (Check-Container "necpgame-redis") { $infraHealthy++ }
if (Check-Container "necpgame-keycloak") { $infraHealthy++ }

Write-Host ""
Write-Host "🎮 Application Services Health:" -ForegroundColor Yellow
Write-Host "-------------------------------" -ForegroundColor Yellow

# Application services array
$services = @(
    @{Name="achievement-service"; Port=8100},
    @{Name="admin-service"; Port=8101},
    @{Name="battle-pass-service"; Port=8102},
    @{Name="character-engram-compatibility-service"; Port=8103},
    @{Name="character-engram-core-service"; Port=8104},
    @{Name="client-service"; Port=8110},
    @{Name="combat-damage-service"; Port=8127},
    @{Name="combat-hacking-service"; Port=8128},
    @{Name="combat-sessions-service"; Port=8117},
    @{Name="cosmetic-service"; Port=8119},
    @{Name="housing-service"; Port=8128},
    @{Name="leaderboard-service"; Port=8130},
    @{Name="progression-experience-service"; Port=8135},
    @{Name="projectile-core-service"; Port=8091},
    @{Name="referral-service"; Port=8097},
    @{Name="reset-service"; Port=8144},
    @{Name="social-player-orders-service"; Port=8097},
    @{Name="stock-analytics-tools-service"; Port=8155},
    @{Name="stock-dividends-service"; Port=8156},
    @{Name="stock-events-service"; Port=8157},
    @{Name="stock-futures-service"; Port=8158},
    @{Name="stock-indices-service"; Port=8159},
    @{Name="stock-margin-service"; Port=8160},
    @{Name="stock-options-service"; Port=8161},
    @{Name="stock-protection-service"; Port=8162},
    @{Name="support-service"; Port=8163}
)

$appHealthy = 0
$appTotal = $services.Count

foreach ($service in $services) {
    if (Check-Service -ServiceName $service.Name -Port $service.Port) {
        $appHealthy++
    }

    if ($Detailed) {
        # Additional checks for detailed mode
        try {
            $metrics = Invoke-WebRequest -Uri "http://localhost:$($service.Port)/metrics" -TimeoutSec 2 -ErrorAction SilentlyContinue
            if ($metrics.StatusCode -eq 200) {
                Write-Host "   📊 Metrics available" -ForegroundColor Gray
            }
        } catch {
            # Metrics not available, skip
        }
    }
}

Write-Host ""
Write-Host "📊 Summary:" -ForegroundColor Yellow
Write-Host "-----------" -ForegroundColor Yellow

Write-Host "Infrastructure: $infraHealthy/$infraTotal healthy ($([math]::Round($infraHealthy/$infraTotal*100,1))%)"
Write-Host "Application: $appHealthy/$appTotal healthy ($([math]::Round($appHealthy/$appTotal*100,1))%)"
Write-Host "Total: $($infraHealthy + $appHealthy)/$($infraTotal + $appTotal) healthy ($([math]::Round(($infraHealthy + $appHealthy)/($infraTotal + $appTotal)*100,1))%)"

if ($infraHealthy -eq $infraTotal -and $appHealthy -eq $appTotal) {
    Write-Host ""
    Write-Host "🎉 All services are healthy!" -ForegroundColor Green
    exit 0
} else {
    Write-Host ""
    Write-Host "WARNING  Some services are unhealthy" -ForegroundColor Red
    exit 1
}
