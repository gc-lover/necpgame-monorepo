# Issue: Fix Grafana dashboard datasource UID
# Получает реальный UID Prometheus и обновляет дашборд

$ErrorActionPreference = "Continue"

$GrafanaUrl = "http://localhost:3000"
$Username = "admin"
$Password = "admin"
$DashboardFile = "infrastructure\observability\grafana\dashboards\benchmarks-history.json"

Write-Host "Fixing dashboard datasource..." -ForegroundColor Cyan

# Get Prometheus datasource UID
$auth = [Convert]::ToBase64String([System.Text.Encoding]::ASCII.GetBytes("${Username}:${Password}"))
$headers = @{Authorization="Basic $auth"}

try {
    $ds = Invoke-RestMethod -Uri "$GrafanaUrl/api/datasources" -Headers $headers -TimeoutSec 5
    $promDS = $ds | Where-Object { $_.type -eq "prometheus" }
    
    if (-not $promDS) {
        Write-Host "Prometheus datasource not found!" -ForegroundColor Red
        exit 1
    }
    
    $promUID = $promDS.uid
    Write-Host "Found Prometheus datasource UID: $promUID" -ForegroundColor Green
    
    # Read dashboard
    $json = Get-Content $DashboardFile -Raw | ConvertFrom-Json
    
    # Update all panels with correct datasource UID
    foreach ($panel in $json.dashboard.panels) {
        if ($panel.targets) {
            foreach ($target in $panel.targets) {
                if (-not $target.datasource) {
                    $target | Add-Member -MemberType NoteProperty -Name "datasource" -Value @{type="prometheus"; uid=$promUID} -Force
                } else {
                    $target.datasource.uid = $promUID
                    $target.datasource.type = "prometheus"
                }
            }
        }
    }
    
    # Save updated dashboard
    $json | ConvertTo-Json -Depth 20 | Out-File -FilePath $DashboardFile -Encoding UTF8
    Write-Host "Dashboard file updated with UID: $promUID" -ForegroundColor Green
    
    # Re-import via API
    $importHeaders = @{Authorization="Basic $auth"; "Content-Type"="application/json"}
    $dashboardObj = @{
        dashboard = $json.dashboard
        overwrite = $true
    }
    $body = $dashboardObj | ConvertTo-Json -Depth 20
    
    $result = Invoke-RestMethod -Uri "$GrafanaUrl/api/dashboards/db" -Method POST -Headers $importHeaders -Body $body -TimeoutSec 10
    
    Write-Host "Dashboard re-imported successfully!" -ForegroundColor Green
    Write-Host "URL: $GrafanaUrl$($result.url)" -ForegroundColor Cyan
    
} catch {
    Write-Host "Error: $_" -ForegroundColor Red
    exit 1
}

