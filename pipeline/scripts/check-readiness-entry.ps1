param(
    [Parameter(Mandatory = $true)]
    [string]$TrackerFile,
    [Parameter(Mandatory = $true)]
    [string]$DocumentPath
)

if (-not (Test-Path $TrackerFile)) {
    Write-Error "Файл трекера не найден: $TrackerFile"
    exit 1
}

$tracker = Get-Content -LiteralPath $TrackerFile -Raw
if (-not ($tracker -match [Regex]::Escape($DocumentPath))) {
    Write-Error "В readiness-tracker отсутствует запись для документа: $DocumentPath"
    exit 1
}

if (-not ($tracker -match "status:\s*ready")) {
    Write-Error "Документ не имеет статуса ready в трекере."
    exit 1
}

Write-Output "Документ $DocumentPath отмечен как готовый в трекере."

