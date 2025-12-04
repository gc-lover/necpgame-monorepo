# Batch create ogen handlers for all migrated services
# Issue: #1595-#1602

$services = @(
    "achievement-service-go", "battle-pass-service-go", "character-engram-compatibility-service-go",
    "character-engram-core-service-go", "character-engram-cyberpsychosis-service-go",
    "character-engram-historical-service-go", "character-engram-security-service-go",
    "chat-service-go", "combat-damage-service-go", "combat-extended-mechanics-service-go",
    "combat-hacking-service-go", "combat-implants-core-service-go",
    "combat-implants-maintenance-service-go", "combat-implants-stats-service-go",
    "combat-sandevistan-service-go", "combat-sessions-service-go", "combat-turns-service-go",
    "companion-service-go", "cosmetic-service-go", "gameplay-progression-core-service-go",
    "gameplay-service-go", "hacking-core-service-go", "housing-service-go",
    "leaderboard-service-go", "league-service-go", "loot-service-go", "mail-service-go",
    "maintenance-service-go", "movement-service-go", "progression-experience-service-go",
    "progression-paragon-service-go", "projectile-core-service-go", "quest-core-service-go",
    "quest-rewards-events-service-go", "quest-skill-checks-conditions-service-go",
    "quest-state-dialogue-service-go", "referral-service-go", "reset-service-go",
    "seasonal-challenges-service-go", "social-chat-channels-service-go",
    "social-chat-commands-service-go", "social-chat-format-service-go",
    "social-chat-history-service-go", "social-chat-messages-service-go",
    "social-chat-moderation-service-go", "social-player-orders-service-go",
    "social-reputation-core-service-go", "stock-analytics-charts-service-go",
    "stock-analytics-tools-service-go", "stock-dividends-service-go",
    "stock-events-service-go", "stock-futures-service-go", "stock-indices-service-go",
    "stock-margin-service-go", "stock-options-service-go", "stock-protection-service-go",
    "trade-service-go", "weapon-resource-service-go", "world-events-analytics-service-go",
    "world-service-go"
)

$success = 0
$skipped = 0

Write-Host "🚀 Batch Creating ogen Handlers" -ForegroundColor Cyan
Write-Host "Total: $($services.Count) services" -ForegroundColor Yellow
Write-Host ""

foreach ($service in $services) {
    if (Test-Path "services\$service\server\handlers.go") {
        Write-Host "⏭️  $service (already has handlers)" -ForegroundColor Gray
        $skipped++
        continue
    }
    
    if (!(Test-Path "services\$service\pkg\api\oas_server_gen.go")) {
        Write-Host "⏭️  $service (no ogen code)" -ForegroundColor Yellow
        $skipped++
        continue
    }
    
    Write-Host "📦 $service" -ForegroundColor Cyan
    
    try {
        .\.cursor\scripts\create-ogen-handlers.ps1 -ServiceName $service 2>&1 | Out-Null
        Write-Host "  OK" -ForegroundColor Green
        $success++
    } catch {
        Write-Host "  ❌ Error" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "OK Created: $success" -ForegroundColor Green
Write-Host "⏭️  Skipped: $skipped" -ForegroundColor Yellow

