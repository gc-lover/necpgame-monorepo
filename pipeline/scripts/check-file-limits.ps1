param(
    [Parameter(Mandatory = $true, ValueFromPipeline = $true)]
    [string[]]$Path
)

$hasViolation = $false

foreach ($item in $Path) {
    if (-not (Test-Path $item)) {
        Write-Error "Файл не найден: $item"
        $hasViolation = $true
        continue
    }

    $lineCount = (Get-Content -LiteralPath $item).Count
    if ($lineCount -gt 500) {
        Write-Error "Нарушение лимита: $item содержит $lineCount строк (>500). Разбей документ на части `_0001`, `_0002`, ... и обнови ссылки."
        $hasViolation = $true
    } else {
        Write-Output "OK [$lineCount строк]: $item"
    }
}

if ($hasViolation) {
    exit 1
}

