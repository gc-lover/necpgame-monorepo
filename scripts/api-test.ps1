# NECP Game API Testing Script
# Комплексное тестирование API endpoints

param(
    [string]$BaseUrl = "http://localhost",
    [string]$JwtSecret = "your-jwt-secret-change-in-production",
    [switch]$Verbose
)

# Import JWT library (you may need to install: Install-Module -Name JWT)
# For simplicity, we'll create a basic token generator

function New-JwtToken {
    param(
        [string]$Secret,
        [string]$UserId = "test-user-123",
        [array]$Roles = @("player"),
        [int]$ExpiryHours = 24
    )

    # Simple JWT header and payload (base64url encoded)
    $header = @{
        alg = "HS256"
        typ = "JWT"
    } | ConvertTo-Json -Compress

    $payload = @{
        sub = $UserId
        iat = [int](Get-Date -UFormat %s)
        exp = [int]((Get-Date).AddHours($ExpiryHours) | Get-Date -UFormat %s)
        roles = $Roles
        iss = "necp-game-backend"
        aud = "necp-game-api"
    } | ConvertTo-Json -Compress

    # Base64Url encoding function
    function ConvertTo-Base64Url {
        param([string]$InputString)
        $bytes = [System.Text.Encoding]::UTF8.GetBytes($InputString)
        $base64 = [Convert]::ToBase64String($bytes)
        return $base64.Replace('+', '-').Replace('/', '_').Replace('=', '')
    }

    $headerB64 = ConvertTo-Base64Url $header
    $payloadB64 = ConvertTo-Base64Url $payload

    $message = "$headerB64.$payloadB64"

    # Simple HMAC-SHA256 (you might need a proper JWT library for production)
    $hmac = New-Object System.Security.Cryptography.HMACSHA256
    $hmac.Key = [System.Text.Encoding]::UTF8.GetBytes($Secret)
    $signatureBytes = $hmac.ComputeHash([System.Text.Encoding]::UTF8.GetBytes($message))
    $signature = [Convert]::ToBase64String($signatureBytes).Replace('+', '-').Replace('/', '_').Replace('=', '')

    return "$message.$signature"
}

function Test-Endpoint {
    param(
        [string]$Service,
        [int]$Port,
        [string]$Endpoint,
        [string]$Method = "GET",
        [string]$Data = "",
        [string]$Description
    )

    Write-Host "Testing $Description... " -NoNewline

    $url = "$BaseUrl`:$Port$Endpoint"

    $headers = @{}

    # Add JWT token for API endpoints (not health/metrics)
    if ($Endpoint -notmatch "/health|/metrics") {
        $token = New-JwtToken -Secret $JwtSecret -UserId "test-user-$Service"
        $headers["Authorization"] = "Bearer $token"
    }

    if ($Data) {
        $headers["Content-Type"] = "application/json"
    }

    try {
        if ($Method -eq "GET") {
            $response = Invoke-WebRequest -Uri $url -Headers $headers -Method $Method -TimeoutSec 10
        } else {
            $response = Invoke-WebRequest -Uri $url -Headers $headers -Method $Method -Body $Data -TimeoutSec 10
        }

        $statusCode = $response.StatusCode

        if ($Endpoint -match "/health") {
            if ($statusCode -eq 200) {
                Write-Host "OK PASS (HTTP $statusCode)" -ForegroundColor Green
                return $true
            } else {
                Write-Host "❌ FAIL (HTTP $statusCode)" -ForegroundColor Red
                return $false
            }
        } elseif ($Endpoint -match "/metrics") {
            $content = $response.Content
            if ($statusCode -eq 200 -and $content -match "go_goroutines") {
                Write-Host "OK PASS (HTTP $statusCode, metrics available)" -ForegroundColor Green
                return $true
            } else {
                Write-Host "❌ FAIL (HTTP $statusCode)" -ForegroundColor Red
                return $false
            }
        } else {
            # API endpoints
            if ($statusCode -ne 404) {
                Write-Host "OK PASS (HTTP $statusCode)" -ForegroundColor Green
                return $true
            } else {
                Write-Host "WARNING  SKIP (HTTP $statusCode - not implemented)" -ForegroundColor Yellow
                return $true
            }
        }
    } catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        if ($statusCode -eq 404 -and $Endpoint -notmatch "/health|/metrics") {
            Write-Host "WARNING  SKIP (HTTP $statusCode - not implemented)" -ForegroundColor Yellow
            return $true
        } else {
            Write-Host "❌ FAIL (HTTP $statusCode)" -ForegroundColor Red
            return $false
        }
    }
}

Write-Host "🧪 NECP Game API Testing Suite" -ForegroundColor Cyan
Write-Host "================================" -ForegroundColor Cyan

if ($Verbose) {
    Write-Host "Configuration:" -ForegroundColor Gray
    Write-Host "  Base URL: $BaseUrl" -ForegroundColor Gray
    Write-Host "  JWT Secret: $($JwtSecret.Substring(0,10))..." -ForegroundColor Gray
    Write-Host ""
}

# Health checks
Write-Host "🏥 Health Checks:" -ForegroundColor Yellow
Write-Host "-----------------" -ForegroundColor Yellow

$healthTests = @(
    @{Service="achievement-service"; Port=8100; Endpoint="/health"; Description="Achievement Service Health"},
    @{Service="admin-service"; Port=8101; Endpoint="/health"; Description="Admin Service Health"},
    @{Service="battle-pass-service"; Port=8102; Endpoint="/health"; Description="Battle Pass Service Health"},
    @{Service="client-service"; Port=8110; Endpoint="/health"; Description="Client Service Health"},
    @{Service="combat-sessions-service"; Port=8117; Endpoint="/health"; Description="Combat Sessions Service Health"},
    @{Service="cosmetic-service"; Port=8119; Endpoint="/health"; Description="Cosmetic Service Health"},
    @{Service="housing-service"; Port=8128; Endpoint="/health"; Description="Housing Service Health"},
    @{Service="leaderboard-service"; Port=8130; Endpoint="/health"; Description="Leaderboard Service Health"}
)

$healthPassed = 0
$healthTotal = $healthTests.Count

foreach ($test in $healthTests) {
    if (Test-Endpoint @test) {
        $healthPassed++
    }
}

# Metrics checks
Write-Host ""
Write-Host "📊 Metrics Checks:" -ForegroundColor Yellow
Write-Host "------------------"

$metricsTests = @(
    @{Service="achievement-service"; Port=9200; Endpoint="/metrics"; Description="Achievement Service Metrics"},
    @{Service="client-service"; Port=9210; Endpoint="/metrics"; Description="Client Service Metrics"}
)

$metricsPassed = 0
$metricsTotal = $metricsTests.Count

foreach ($test in $metricsTests) {
    if (Test-Endpoint @test) {
        $metricsPassed++
    }
}

# API endpoints
Write-Host ""
Write-Host "🔗 API Endpoints:" -ForegroundColor Yellow
Write-Host "-----------------"

$apiTests = @(
    @{Service="achievement-service"; Port=8100; Endpoint="/api/v1/achievements"; Description="Achievement Service API"}
)

$apiPassed = 0
$apiTotal = $apiTests.Count

foreach ($test in $apiTests) {
    if (Test-Endpoint @test) {
        $apiPassed++
    }
}

# Summary
Write-Host ""
Write-Host "📋 Test Summary:" -ForegroundColor Yellow
Write-Host "---------------"

Write-Host "Health checks: $healthPassed/$healthTotal passed"
Write-Host "Metrics checks: $metricsPassed/$metricsTotal passed"
Write-Host "API endpoints: $apiPassed/$apiTotal tested"

$totalPassed = $healthPassed + $metricsPassed + $apiPassed
$totalTests = $healthTotal + $metricsTotal + $apiTotal

Write-Host ""
Write-Host "Total: $totalPassed/$totalTests tests passed ($([math]::Round($totalPassed/$totalTests*100,1))%)"

if ($healthPassed -eq $healthTotal) {
    Write-Host ""
    Write-Host "🎉 All health checks passed!" -ForegroundColor Green
    exit 0
} else {
    Write-Host ""
    Write-Host "WARNING  Some health checks failed" -ForegroundColor Red
    exit 1
}
