#!/usr/bin/env pwsh
# Скрипт для сборки Docker образа конкретного микросервиса
# Использование: .\scripts\docker-build.ps1 -Service auth-service [-Tag "latest"] [-NoCache]

param(
    [Parameter(Mandatory = $true)]
    [string]$Service,
    [string]$Tag = "latest",
    [switch]$NoCache
)

$ErrorActionPreference = "Stop"

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$ProjectRoot = Split-Path -Parent $ScriptDir
Set-Location $ProjectRoot

$serviceNormalized = $Service.Trim().ToLowerInvariant()
if ([string]::IsNullOrWhiteSpace($serviceNormalized)) {
    throw "Имя микросервиса не может быть пустым."
}

Write-Host "🐳 Сборка Docker образа для микросервиса '$serviceNormalized'..." -ForegroundColor Cyan

$candidateDirectories = @(
    Join-Path $ProjectRoot "microservices/$serviceNormalized",
    Join-Path $ProjectRoot "infrastructure/$serviceNormalized"
)

$contextDirectory = $candidateDirectories | Where-Object { Test-Path $_ } | Select-Object -First 1

if (-not $contextDirectory) {
    throw "Директория микросервиса '$serviceNormalized' не найдена. Ожидается путь microservices/$serviceNormalized или infrastructure/$serviceNormalized."
}

$dockerfileCandidates = @(
    Join-Path $contextDirectory "Dockerfile",
    Join-Path $contextDirectory "docker/Dockerfile"
)

$dockerfilePath = $dockerfileCandidates | Where-Object { Test-Path $_ } | Select-Object -First 1

if (-not $dockerfilePath) {
    throw "Dockerfile для микросервиса '$serviceNormalized' не найден. Создайте файл ${contextDirectory}/Dockerfile."
}

# Проверка существования API-SWAGGER (для напоминания о генерации)
if (-not (Test-Path "../API-SWAGGER")) {
    Write-Host "⚠️  Директория API-SWAGGER не найдена. Убедитесь, что спецификации доступны перед сборкой." -ForegroundColor Yellow
}

$imageName = "necpgame-$serviceNormalized"
$fullTag = "${imageName}:${Tag}"

$buildCommand = @("docker", "build")

if ($NoCache) {
    $buildCommand += "--no-cache"
}

$buildCommand += @("-t", $fullTag, "-f", $dockerfilePath, $contextDirectory)

Write-Host "📦 Команда сборки: $($buildCommand -join ' ')" -ForegroundColor Gray

# Выполнение сборки
Write-Host "`n🔨 Начало сборки..." -ForegroundColor Cyan
& $buildCommand[0] $buildCommand[1..($buildCommand.Count - 1)]

if ($LASTEXITCODE -eq 0) {
    Write-Host "`n✅ Образ успешно собран: $fullTag" -ForegroundColor Green
    
    # Показываем информацию об образе
    Write-Host "`n📊 Информация об образе:" -ForegroundColor Cyan
    docker images $imageName --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}\t{{.CreatedAt}}"
    
    Write-Host "`n💡 Для запуска используйте:" -ForegroundColor Yellow
    $defaultPorts = @{
        "auth-service" = 8081
        "character-service" = 8082
        "gameplay-service" = 8083
        "social-service" = 8084
        "economy-service" = 8085
        "world-service" = 8086
        "api-gateway" = 8080
    }
    if ($defaultPorts.ContainsKey($serviceNormalized)) {
        $port = $defaultPorts[$serviceNormalized]
        Write-Host ("   docker run -p {0}:{0} {1}" -f $port, $fullTag) -ForegroundColor White
    } else {
        Write-Host "   docker run $fullTag" -ForegroundColor White
    }
} else {
    Write-Host "`n❌ Ошибка при сборке образа!" -ForegroundColor Red
    exit 1
}