# Build Docker images for all 17 services

$services = @(
    "maintenance-service-go", "stock-dividends-service-go", "stock-events-service-go",
    "stock-indices-service-go", "stock-protection-service-go", "battle-pass-service-go",
    "leaderboard-service-go", "combat-sessions-service-go", "gameplay-service-go",
    "league-service-go", "social-player-orders-service-go", "housing-service-go",
    "companion-service-go", "world-service-go", "referral-service-go",
    "social-service-go", "cosmetic-service-go"
)

$successCount = 0
$failCount = 0
$results = @()

Write-Host "`n🐳 Building Docker images for $($services.Count) services...`n" -ForegroundColor Cyan

foreach ($svc in $services) {
    $svcPath = "services\$svc"
    $imageName = ($svc -replace '-go$', '').ToLower()
    
    Write-Host "$svc..." -NoNewline
    
    try {
        # Build Docker image
        $buildOutput = docker build -t "necpgame/$imageName:latest" $svcPath 2>&1
        
        if ($LASTEXITCODE -eq 0) {
            # Get image size
            $size = docker images "necpgame/$imageName:latest" --format "{{.Size}}" 2>$null
            Write-Host " OK ($size)" -ForegroundColor Green
            $successCount++
            $results += [PSCustomObject]@{Service=$svc; Status="OK OK"; Size=$size}
        }
        else {
            Write-Host " ❌ Build failed" -ForegroundColor Red
            $failCount++
            $results += [PSCustomObject]@{Service=$svc; Status="❌ Failed"; Size="N/A"}
        }
    }
    catch {
        Write-Host " ❌ Error: $($_.Exception.Message)" -ForegroundColor Red
        $failCount++
        $results += [PSCustomObject]@{Service=$svc; Status="❌ Error"; Size="N/A"}
    }
}

Write-Host "`n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
Write-Host "SUMMARY" -ForegroundColor Cyan
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
Write-Host "OK Success: $successCount images" -ForegroundColor Green
Write-Host "❌ Failed: $failCount images" -ForegroundColor Red

Write-Host "`nResults:" -ForegroundColor White
$results | Format-Table -AutoSize

if ($successCount -eq $services.Count) {
    Write-Host "`n🎉 All Docker images built successfully!`n" -ForegroundColor Green
}

