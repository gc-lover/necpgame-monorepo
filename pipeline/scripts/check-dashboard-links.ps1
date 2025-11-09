param(
    [Parameter(Mandatory = $true)]
    [string]$ReportFile
)

if (-not (Test-Path $ReportFile)) {
    Write-Error "Файл отчёта не найден: $ReportFile"
    exit 1
}

$content = Get-Content -LiteralPath $ReportFile -Raw
if ($content -notmatch "https://") {
    Write-Error "В отчёте отсутствуют ссылки на dashboards/метрики."
    exit 1
}

Write-Output "Отчёт содержит ссылки на dashboards."

