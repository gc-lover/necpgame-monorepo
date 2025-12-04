# Issue: Simple HTTP server for benchmark metrics
# Отдает метрики из файла через HTTP для Prometheus

param(
    [string]$MetricsFile = ".benchmarks/metrics.prom",
    [int]$Port = 9100
)

$ErrorActionPreference = "Continue"

Write-Host "Benchmark Metrics Server" -ForegroundColor Cyan
Write-Host "   File: $MetricsFile" -ForegroundColor Gray
Write-Host "   Port: $Port" -ForegroundColor Gray
Write-Host "   URL: http://localhost:$Port/metrics" -ForegroundColor Gray
Write-Host ""

if (-not (Test-Path $MetricsFile)) {
    Write-Host "Metrics file not found: $MetricsFile" -ForegroundColor Yellow
    Write-Host "   Run: .\scripts\export-benchmarks-to-prometheus.ps1 -UseFile" -ForegroundColor Yellow
}

# Простой HTTP сервер
# Проверяем не занят ли порт
$existing = Get-NetTCPConnection -LocalPort $Port -ErrorAction SilentlyContinue
if ($existing) {
    Write-Host "Port $Port is already in use. Stopping existing process..." -ForegroundColor Yellow
    $processId = $existing | Select-Object -ExpandProperty OwningProcess -Unique
    Stop-Process -Id $processId -Force -ErrorAction SilentlyContinue
    Start-Sleep -Seconds 2
}

$Listener = New-Object System.Net.HttpListener

# Try to listen on all interfaces first (requires admin or URL reservation)
# If that fails, fallback to localhost (Docker can still access via host.docker.internal)
$bindAll = $false
try {
    # Check if URL is already reserved
    $urlCheck = netsh http show urlacl | Select-String "http://\*:$Port/"
    if ($urlCheck) {
        Write-Host "URL reservation found, binding to all interfaces..." -ForegroundColor Gray
        $Listener.Prefixes.Add("http://*:$Port/")
        $bindAll = $true
    } else {
        throw "URL not reserved"
    }
} catch {
    # Fallback to localhost - Docker can access via host.docker.internal
    Write-Host "Binding to localhost (Docker will use host.docker.internal)" -ForegroundColor Gray
    $Listener.Prefixes.Add("http://localhost:$Port/")
    Write-Host "Note: For better Docker access, run as Administrator:" -ForegroundColor Yellow
    Write-Host "  netsh http add urlacl url=http://*:$Port/ user=Everyone" -ForegroundColor White
}

try {
    $Listener.Start()
} catch {
    Write-Host "Failed to start listener: $_" -ForegroundColor Red
    Write-Host "Port $Port may be in use. Try a different port or stop the conflicting process." -ForegroundColor Yellow
    exit 1
}

Write-Host "Server started. Press Ctrl+C to stop." -ForegroundColor Green
Write-Host ""

try {
    while ($Listener.IsListening) {
        $Context = $Listener.GetContext()
        $Request = $Context.Request
        $Response = $Context.Response
        
        if ($Request.Url.PathAndQuery -eq "/metrics") {
            if (Test-Path $MetricsFile) {
                $Content = Get-Content $MetricsFile -Raw -Encoding UTF8
                $Buffer = [System.Text.Encoding]::UTF8.GetBytes($Content)
                
                $Response.ContentType = "text/plain; version=0.0.4"
                $Response.ContentLength64 = $Buffer.Length
                $Response.StatusCode = 200
                $Response.OutputStream.Write($Buffer, 0, $Buffer.Length)
            } else {
                $Content = "# No metrics available yet. Run export script first."
                $Buffer = [System.Text.Encoding]::UTF8.GetBytes($Content)
                
                $Response.ContentType = "text/plain"
                $Response.ContentLength64 = $Buffer.Length
                $Response.StatusCode = 200
                $Response.OutputStream.Write($Buffer, 0, $Buffer.Length)
            }
        } else {
            $Content = "Benchmark Metrics Server`n`nAvailable endpoints:`n  /metrics - Prometheus metrics"
            $Buffer = [System.Text.Encoding]::UTF8.GetBytes($Content)
            
            $Response.ContentType = "text/plain"
            $Response.ContentLength64 = $Buffer.Length
            $Response.StatusCode = 200
            $Response.OutputStream.Write($Buffer, 0, $Buffer.Length)
        }
        
        $Response.Close()
    }
} finally {
    $Listener.Stop()
    Write-Host ""
    Write-Host "Server stopped" -ForegroundColor Green
}
