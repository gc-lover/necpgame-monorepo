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
    if (Test-Path ".\pom.xml") {
        Write-Output "Запуск OWASP Dependency Check..."
        mvn verify -Psecurity | Write-Output
    }
    if (Test-Path ".\package.json") {
        Write-Output "Запуск npm audit..."
        npm audit --omit=dev | Write-Output
    }
} finally {
    Pop-Location
}

Write-Output "Security scan завершён — проверь вывод выше."

