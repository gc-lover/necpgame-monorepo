param(
    [Parameter(Mandatory = $true)]
    [string]$ProjectRoot
)

if (-not (Test-Path $ProjectRoot)) {
    Write-Error "Каталог не найден: $ProjectRoot"
    exit 1
}

Push-Location $ProjectRoot
try {
    Write-Output "Запуск unit тестов..."
    if (Test-Path ".\mvnw" -or Test-Path ".\pom.xml") {
        mvn test | Write-Output
    }
    if (Test-Path ".\package.json") {
        npm run test -- --run | Write-Output
    }
} finally {
    Pop-Location
}

Write-Output "QA suite выполнен (проверь логи на наличие ошибок)."

