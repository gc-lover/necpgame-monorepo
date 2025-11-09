param(
    [Parameter(Mandatory = $true)]
    [string]$ProjectRoot
)

if (-not (Test-Path $ProjectRoot)) {
    Write-Error "Каталог не найден: $ProjectRoot"
    exit 1
}

$violations = @()

$manualFetch = Get-ChildItem -Path $ProjectRoot -Recurse -Include *.ts,*.tsx |
    Where-Object { (Get-Content -LiteralPath $_.FullName) -match "import\s+axios" -or (Get-Content -LiteralPath $_.FullName) -match "fetch\(" }
foreach ($file in $manualFetch) {
    $violations += "Найден прямой сетевой вызов (используй сгенерированные клиенты): $($file.FullName)"
}

$generated = Join-Path $ProjectRoot "src\api\generated"
if (-not (Test-Path $generated)) {
    $violations += "Отсутствует каталог сгенерированных клиентов: $generated"
}

if ($violations.Count -gt 0) {
    $violations | ForEach-Object { Write-Error $_ }
    exit 1
}

Write-Output "Фронтенд соответствует базовым правилам генерации клиентов."

