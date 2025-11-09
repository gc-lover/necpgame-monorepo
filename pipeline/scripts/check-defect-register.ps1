param(
    [Parameter(Mandatory = $true)]
    [string]$File
)

if (-not (Test-Path $File)) {
    Write-Error "Файл дефектов не найден: $File"
    exit 1
}

$content = Get-Content -LiteralPath $File -Raw

$requiredKeys = @("defect_id", "severity", "status", "owner")
foreach ($key in $requiredKeys) {
    if (-not ($content -match "$key\s*:\s*")) {
        Write-Error "В реестре отсутствует ключ: $key"
        exit 1
    }
}

Write-Output "Реестр дефектов содержит обязательные поля."

