param(
    [Parameter(Mandatory = $true)]
    [string]$File
)

if (-not (Test-Path $File)) {
    Write-Error "Security review не найден: $File"
    exit 1
}

$content = Get-Content -LiteralPath $File -Raw

$keys = @("review_id", "Threat Model", "Findings", "Controls", "Tests", "Recommendations", "Approval")
foreach ($key in $keys) {
    if (-not ($content -match $key)) {
        Write-Error "Отсутствует секция: $key"
        exit 1
    }
}

Write-Output "Security review заполнен корректно."

