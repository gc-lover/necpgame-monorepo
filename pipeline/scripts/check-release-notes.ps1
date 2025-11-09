param(
    [Parameter(Mandatory = $true)]
    [string]$File
)

if (-not (Test-Path $File)) {
    Write-Error "Файл release notes не найден: $File"
    exit 1
}

$content = Get-Content -LiteralPath $File -Raw

$required = @("version", "date", "summary", "highlights")
foreach ($key in $required) {
    if (-not ($content -match "$key\s*:\s*")) {
        Write-Error "В release notes отсутствует ключ: $key"
        exit 1
    }
}

Write-Output "Release notes заполнен корректно: $File"

