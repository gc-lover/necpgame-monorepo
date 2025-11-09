# Скрипт для генерации TypeScript клиента и React Query хуков из OpenAPI спецификаций
# Использует Orval для автоматической генерации type-safe API клиента
# Использование: .\scripts\generate-api-orval.ps1

Write-Host "╔═══════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║       NECPGAME - API Code Generation (Orval)             ║" -ForegroundColor Cyan
Write-Host "╚═══════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

# Проверка наличия node_modules
if (-not (Test-Path "node_modules")) {
    Write-Host "WARNING  node_modules не найден. Устанавливаю зависимости..." -ForegroundColor Yellow
    npm install
    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ Ошибка при установке зависимостей" -ForegroundColor Red
        exit 1
    }
    Write-Host ""
}

# Проверка наличия OpenAPI файлов
$apiSwaggerPath = "../openapi/api/v1"
if (-not (Test-Path $apiSwaggerPath)) {
    Write-Host "❌ Директория services/openapi/api/v1 не найдена: $apiSwaggerPath" -ForegroundColor Red
    Write-Host "   Убедитесь, что вы запускаете скрипт из директории services/frontend" -ForegroundColor Yellow
    exit 1
}

Write-Host "📋 Найденные OpenAPI спецификации:" -ForegroundColor Green
Get-ChildItem -Path $apiSwaggerPath -Recurse -Filter "*.yaml" -File | ForEach-Object {
    Write-Host "   • $($_.FullName.Replace((Get-Location).Path + '\', ''))" -ForegroundColor Gray
}
Write-Host ""

# Очистка старых сгенерированных файлов
Write-Host "🧹 Очистка старых сгенерированных файлов..." -ForegroundColor Yellow
$generatedPath = "src/api/generated"
if (Test-Path $generatedPath) {
    Remove-Item -Path $generatedPath -Recurse -Force
    Write-Host "   ✓ Удалено: $generatedPath" -ForegroundColor Gray
}
Write-Host ""

# Генерация кода с помощью Orval
Write-Host "🚀 Запуск генерации кода..." -ForegroundColor Green
Write-Host ""

npm run generate:api

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "╔═══════════════════════════════════════════════════════════╗" -ForegroundColor Green
    Write-Host "║           ✓ Генерация завершена успешно!                ║" -ForegroundColor Green
    Write-Host "╚═══════════════════════════════════════════════════════════╝" -ForegroundColor Green
    Write-Host ""
    Write-Host "📦 Сгенерированные файлы находятся в:" -ForegroundColor Cyan
    Write-Host "   • src/api/generated/" -ForegroundColor White
    Write-Host ""
    Write-Host "🎯 Что дальше?" -ForegroundColor Cyan
    Write-Host "   1. Проверьте сгенерированный код в src/api/generated/" -ForegroundColor White
    Write-Host "   2. Импортируйте хуки в компоненты:" -ForegroundColor White
    Write-Host "      import { useLogin, useRegister } from '@/api/generated/auth/auth'" -ForegroundColor Gray
    Write-Host "   3. Используйте в компонентах React Query хуки:" -ForegroundColor White
    Write-Host "      const { mutate: login } = useLogin()" -ForegroundColor Gray
    Write-Host "   4. Настройте QueryClientProvider в main.tsx" -ForegroundColor White
    Write-Host ""
    Write-Host "📚 Документация:" -ForegroundColor Cyan
    Write-Host "   • Orval: https://orval.dev/" -ForegroundColor White
    Write-Host "   • React Query: https://tanstack.com/query/latest" -ForegroundColor White
    Write-Host ""
} else {
    Write-Host ""
    Write-Host "╔═══════════════════════════════════════════════════════════╗" -ForegroundColor Red
    Write-Host "║             ✗ Ошибка при генерации кода                 ║" -ForegroundColor Red
    Write-Host "╚═══════════════════════════════════════════════════════════╝" -ForegroundColor Red
    Write-Host ""
    Write-Host "🔍 Возможные причины:" -ForegroundColor Yellow
    Write-Host "   1. Проверьте синтаксис OpenAPI спецификаций" -ForegroundColor White
    Write-Host "   2. Убедитесь, что все $ref ссылки корректны" -ForegroundColor White
    Write-Host "   3. Проверьте путь к файлам в orval.config.ts" -ForegroundColor White
    Write-Host "   4. Проверьте логи выше для деталей ошибки" -ForegroundColor White
    Write-Host ""
    exit 1
}
























