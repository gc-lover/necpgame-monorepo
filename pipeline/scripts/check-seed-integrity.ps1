param(
    [Parameter(Mandatory = $true)]
    [string]$SeedDirectory
)

if (-not (Test-Path $SeedDirectory)) {
    Write-Error "Каталог seed данных не найден: $SeedDirectory"
    exit 1
}

$files = Get-ChildItem -Path $SeedDirectory -File -Include *.sql,*.yaml,*.yml
if ($files.Count -eq 0) {
    Write-Error "В каталоге отсутствуют seed файлы (*.sql, *.yaml)."
    exit 1
}

foreach ($file in $files) {
    if ((Get-Content -LiteralPath $file.FullName).Count -eq 0) {
        Write-Error "Seed файл пуст: $($file.FullName)"
        exit 1
    }
}

Write-Output "Seed файлы найдены и не пусты."

