# Issue: Import Grafana dashboard via API
# Импортирует дашборд через Grafana API

$ErrorActionPreference = "Continue"

$DashboardFile = "infrastructure\observability\grafana\dashboards\benchmarks-history.json"
$GrafanaUrl = "http://localhost:3000"
$Username = "admin"
$Password = "admin"

Write-Host "📊 Importing Grafana dashboard..." -ForegroundColor Cyan

# Auth
$auth = [Convert]::ToBase64String([System.Text.Encoding]::ASCII.GetBytes("${Username}:${Password}"))
$headers = @{
    Authorization = "Basic $auth"
    "Content-Type" = "application/json"
}

# Read dashboard
if (-not (Test-Path $DashboardFile)) {
    Write-Host "❌ Dashboard file not found: $DashboardFile" -ForegroundColor Red
    exit 1
}

$dashboardJson = Get-Content $DashboardFile -Raw | ConvertFrom-Json

# Prepare for import
$dashboardJson.dashboard.uid = "benchmarks-history"
$dashboardJson.dashboard.id = $null
$dashboardJson.overwrite = $true

$body = $dashboardJson | ConvertTo-Json -Depth 20

# Import
try {
    $result = Invoke-RestMethod -Uri "$GrafanaUrl/api/dashboards/db" `
        -Method POST `
        -Headers $headers `
        -Body $body `
        -TimeoutSec 10
    
    Write-Host "OK Dashboard imported successfully!" -ForegroundColor Green
    Write-Host "   Title: $($result.dashboard.title)" -ForegroundColor Gray
    Write-Host "   URL: $GrafanaUrl$($result.url)" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Open: $GrafanaUrl/dashboards" -ForegroundColor Yellow
    
} catch {
    Write-Host "❌ Import failed: $_" -ForegroundColor Red
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "   Response: $responseBody" -ForegroundColor Yellow
    }
    exit 1
}

