# Скрипт для генерации TypeScript клиента из API Swagger спецификаций
# Использование: .\scripts\generate-api.ps1 <путь-к-api-swagger-file> <output-dir>

param(
    [Parameter(Mandatory=$true)]
    [string]$SwaggerFile,
    
    [Parameter(Mandatory=$true)]
    [string]$OutputDir
)

# Проверка аргументов
if (-not $SwaggerFile -or -not $OutputDir) {
    Write-Host "Ошибка: Недостаточно аргументов" -ForegroundColor Red
    Write-Host "Использование: .\scripts\generate-api.ps1 <путь-к-api-swagger-file> <output-dir>"
    Write-Host "Пример: .\scripts\generate-api.ps1 ..\openapi\api\v1\gameplay\social\personal-npc-tool.yaml src\api\generated\personal-npc-tool"
    exit 1
}

# Проверка существования файла
if (-not (Test-Path $SwaggerFile)) {
    Write-Host "Ошибка: Файл $SwaggerFile не найден" -ForegroundColor Red
    exit 1
}

# Проверка установки OpenAPI Generator
try {
    $null = Get-Command openapi-generator-cli -ErrorAction Stop
} catch {
    Write-Host "OpenAPI Generator не установлен. Устанавливаю..." -ForegroundColor Yellow
    npm install -g @openapitools/openapi-generator-cli
}

Write-Host "Генерация TypeScript клиента..." -ForegroundColor Green
Write-Host "  Файл: $SwaggerFile"
Write-Host "  Выходная директория: $OutputDir"
Write-Host ""

# Создание директории, если не существует
if (-not (Test-Path $OutputDir)) {
    New-Item -ItemType Directory -Path $OutputDir -Force | Out-Null
}

# Генерация кода
$result = openapi-generator-cli generate `
  -i $SwaggerFile `
  -g typescript-axios `
  -o $OutputDir `
  --additional-properties=supportsES6=true,withInterfaces=true,npmName=@necpgame/api-client,typescriptThreePlus=true

if ($LASTEXITCODE -eq 0) {
    Write-Host "✓ Генерация завершена успешно!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Следующие шаги:"
    Write-Host "  1. Проверьте сгенерированный клиент в $OutputDir"
    Write-Host "  2. Настройте базовый URL API в configuration.ts"
    Write-Host "  3. Создайте компоненты в src/components/"
    Write-Host "  4. Создайте хуки в src/hooks/"
    Write-Host "  5. Создайте сервисы в src/services/"
} else {
    Write-Host "✗ Ошибка при генерации кода" -ForegroundColor Red
    exit 1
}



























