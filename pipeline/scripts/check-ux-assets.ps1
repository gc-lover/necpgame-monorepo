param(
    [Parameter(Mandatory = $true)]
    [string]$GuideFile
)

if (-not (Test-Path $GuideFile)) {
    Write-Error "UX документ не найден: $GuideFile"
    exit 1
}

$content = Get-Content -LiteralPath $GuideFile -Raw

if ($content -notmatch "figma\.com") {
    Write-Error "В UX документе отсутствуют ссылки на макеты (ожидается хотя бы одна ссылка Figma)."
    exit 1
}

Write-Output "UX документ содержит ссылки на макеты."

