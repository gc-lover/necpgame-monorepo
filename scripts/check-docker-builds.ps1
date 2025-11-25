# Скрипт для проверки Docker сборки всех Go сервисов

$services = @(
    "achievement-service-go",
    "admin-service-go",
    "battle-pass-service-go",
    "character-service-go",
    "clan-war-service-go",
    "companion-service-go",
    "economy-service-go",
    "feedback-service-go",
    "gameplay-service-go",
    "housing-service-go",
    "inventory-service-go",
    "leaderboard-service-go",
    "maintenance-service-go",
    "matchmaking-go",
    "movement-service-go",
    "realtime-gateway-go",
    "referral-service-go",
    "reset-service-go",
    "social-service-go",
    "support-service-go",
    "voice-chat-service-go",
    "world-service-go",
    "ws-lobby-go"
)

$results = @{}
$totalServices = $services.Count
$currentService = 0

Write-Host "🐳 Проверка Docker сборки всех Go сервисов..." -ForegroundColor Cyan
Write-Host "Всего сервисов: $totalServices`n" -ForegroundColor Yellow

foreach ($service in $services) {
    $currentService++
    Write-Host "[$currentService/$totalServices] Проверка $service..." -ForegroundColor Yellow
    
    $servicePath = "services/$service"
    $dockerfilePath = "$servicePath/Dockerfile"
    
    if (-not (Test-Path $dockerfilePath)) {
        Write-Host "  ❌ Dockerfile не найден" -ForegroundColor Red
        $results[$service] = "NO_DOCKERFILE"
        continue
    }
    
    # Проверка go build
    Write-Host "  📦 Go build..." -NoNewline
    Push-Location $servicePath
    $goBuildResult = & go build -o "$service-test.exe" . 2>&1
    Pop-Location
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host " ✅" -ForegroundColor Green
        Remove-Item -Path "$servicePath/$service-test.exe" -ErrorAction SilentlyContinue
        $results[$service] = "OK"
    } else {
        Write-Host " ❌" -ForegroundColor Red
        Write-Host "    Error: $goBuildResult" -ForegroundColor DarkRed
        $results[$service] = "BUILD_FAILED"
    }
}

Write-Host "`n📊 РЕЗУЛЬТАТЫ:" -ForegroundColor Cyan
Write-Host "=" * 60

$ok = ($results.Values | Where-Object { $_ -eq "OK" }).Count
$failed = ($results.Values | Where-Object { $_ -eq "BUILD_FAILED" }).Count
$noDockerfile = ($results.Values | Where-Object { $_ -eq "NO_DOCKERFILE" }).Count

Write-Host "✅ Успешно собрались: $ok" -ForegroundColor Green
Write-Host "❌ Ошибки сборки: $failed" -ForegroundColor Red
Write-Host "⚠️  Нет Dockerfile: $noDockerfile" -ForegroundColor Yellow

Write-Host "`nДетальный отчёт:" -ForegroundColor Cyan
foreach ($service in $services) {
    $status = $results[$service]
    $icon = switch ($status) {
        "OK" { "✅" }
        "BUILD_FAILED" { "❌" }
        "NO_DOCKERFILE" { "⚠️" }
        default { "❓" }
    }
    Write-Host "  $icon $service : $status"
}

