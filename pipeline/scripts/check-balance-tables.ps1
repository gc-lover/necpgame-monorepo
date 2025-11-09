param(
    [Parameter(Mandatory = $true)]
    [string[]]$Files
)

$failed = $false

foreach ($file in $Files) {
    if (-not (Test-Path $file)) {
        Write-Error "Балансовый файл не найден: $file"
        $failed = $true
        continue
    }

    $ext = [System.IO.Path]::GetExtension($file)
    if ($ext -notin @(".csv", ".yaml", ".yml")) {
        Write-Error "Недопустимое расширение для балансовых данных ($ext): $file"
        $failed = $true
    }
}

if ($failed) {
    exit 1
}

Write-Output "Файлы баланса проверены."

