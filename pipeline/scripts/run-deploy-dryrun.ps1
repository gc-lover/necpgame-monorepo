param(
    [Parameter(Mandatory = $true)]
    [string]$EnvDirectory
)

if (-not (Test-Path $EnvDirectory)) {
    Write-Error "Каталог окружения не найден: $EnvDirectory"
    exit 1
}

Write-Output "Проверка docker-compose..."
if (Test-Path (Join-Path $EnvDirectory "docker-compose.yml")) {
    docker compose -f (Join-Path $EnvDirectory "docker-compose.yml") config | Write-Output
}

Write-Output "Проверка Helm charts..."
$charts = Get-ChildItem -Path $EnvDirectory -Directory -Filter "chart*"
foreach ($chart in $charts) {
    helm lint $chart.FullName | Write-Output
}

Write-Output "Dry run завершён. Проанализируй вывод на предмет ошибок."

