# Issue: Simple HTTP server for benchmark metrics
# Отдает метрики из файла через HTTP для Prometheus

param(
    [string]$MetricsFile = ".benchmarks/metrics.prom",
    [int]$Port = 9099
)

$ErrorActionPreference = "Continue"

Write-Host "📊 Benchmark Metrics Server" -ForegroundColor Cyan
Write-Host "   File: $MetricsFile" -ForegroundColor Gray
Write-Host "   Port: $Port" -ForegroundColor Gray
Write-Host "   URL: http://localhost:$Port/metrics" -ForegroundColor Gray
Write-Host ""

if (-not (Test-Path $MetricsFile)) {
    Write-Host "WARNING  Metrics file not found: $MetricsFile" -ForegroundColor Yellow
    Write-Host "   Run: .\scripts\export-benchmarks-to-prometheus.ps1 -UseFile" -ForegroundColor Yellow
}

# Простой HTTP сервер
$Listener = New-Object System.Net.HttpListener
$Listener.Prefixes.Add("http://localhost:$Port/")
$Listener.Start()

Write-Host "OK Server started. Press Ctrl+C to stop." -ForegroundColor Green
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
    Write-Host "`nOK Server stopped" -ForegroundColor Green
}

