param(
    [Parameter(Mandatory = $true)]
    [string]$PackFile
)

if (-not (Test-Path $PackFile)) {
    Write-Error "Файл коммуникационного пакета не найден: $PackFile"
    exit 1
}

$content = Get-Content -LiteralPath $PackFile -Raw

$sections = @("audience", "channels", "messages", "schedule")
foreach ($section in $sections) {
    if (-not ($content -match "$section\s*:\s*")) {
        Write-Error "Коммуникационный пакет не содержит секцию: $section"
        exit 1
    }
}

Write-Output "Коммуникационный пакет заполнен корректно."

