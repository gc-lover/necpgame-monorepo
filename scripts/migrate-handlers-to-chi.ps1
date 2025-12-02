# Migrate handlers from Gorilla to Chi API for all services

$services = @(
    "battle-pass-service-go", "leaderboard-service-go", "maintenance-service-go",
    "stock-dividends-service-go", "stock-events-service-go", "stock-indices-service-go", 
    "stock-protection-service-go", "combat-sessions-service-go", "gameplay-service-go",
    "league-service-go", "social-player-orders-service-go", "housing-service-go",
    "companion-service-go", "world-service-go", "referral-service-go", "cosmetic-service-go"
)

$successCount = 0
$results = @()

Write-Host "`n🔧 Migrating handlers to Chi API for $($services.Count) services...`n" -ForegroundColor Cyan

foreach ($svc in $services) {
    $svcPath = "services\$svc"
    
    if (-not (Test-Path $svcPath)) {
        continue
    }
    
    Write-Host "$svc..." -NoNewline
    
    try {
        cd $svcPath
        
        # Check if http_server.go exists
        if (-not (Test-Path "server/http_server.go")) {
            Write-Host " ⏭️  No http_server.go" -ForegroundColor Gray
            cd ..\..
            continue
        }
        
        # Read http_server.go
        $content = Get-Content "server/http_server.go" -Raw
        
        # Check if already uses Chi
        if ($content -match 'github.com/go-chi/chi/v5') {
            # Update to use generated ServerInterface
            if ($content -notmatch 'api\.HandlerWithOptions') {
                # Replace old manual routing with generated handler
                $content = $content -replace 'router\.HandleFunc\([^\)]+\)\.Methods\([^\)]+\)', '// Replaced with generated Chi handlers'
                $content = $content -replace 'apiRouter\.HandleFunc\([^\)]+\)\.Methods\([^\)]+\)', '// Replaced with generated Chi handlers'
                
                # Add generated handler integration
                if ($content -match 'func NewHTTPServer') {
                    $content = $content -replace '(router := chi\.NewRouter\(\)[^\n]*)', "`$1`n`n`t// Generated API handlers`n`thandlers := NewHandlers(service)`n`tapi.HandlerWithOptions(handlers, api.ChiServerOptions{BaseRouter: router})"
                }
                
                Set-Content "server/http_server.go" $content
            }
        }
        else {
            # Migrate from Gorilla to Chi
            $content = $content -replace 'github\.com/gorilla/mux', 'github.com/go-chi/chi/v5'
            $content = $content -replace 'mux\.NewRouter', 'chi.NewRouter'
            $content = $content -replace '\*mux\.Router', 'chi.Router'
            $content = $content -replace '\.Methods\("([^"]+)"\)', ''
            $content = $content -replace 'HandleFunc', 'Get'
            
            Set-Content "server/http_server.go" $content
        }
        
        # Test compilation
        go mod tidy 2>&1 | Out-Null
        $null = go build ./... 2>&1
        
        cd ..\..
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host " OK" -ForegroundColor Green
            $successCount++
            $results += [PSCustomObject]@{Service=$svc; Status="OK OK"}
        }
        else {
            Write-Host " WARNING  Needs manual fix" -ForegroundColor Yellow
            $results += [PSCustomObject]@{Service=$svc; Status="WARNING Manual"}
        }
    }
    catch {
        Write-Host " ❌ Error" -ForegroundColor Red
        $results += [PSCustomObject]@{Service=$svc; Status="❌ Error"}
        cd ..\..
    }
}

Write-Host "`n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
Write-Host "SUMMARY" -ForegroundColor Cyan
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
Write-Host "OK Success: $successCount services" -ForegroundColor Green
Write-Host "WARNING  Needs manual: $(($results | Where-Object {$_.Status -eq 'WARNING Manual'}).Count) services" -ForegroundColor Yellow

Write-Host "`nResults:" -ForegroundColor White
$results | Format-Table -AutoSize

