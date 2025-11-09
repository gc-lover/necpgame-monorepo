param(
    [Parameter(Mandatory = $true)]
    [string]$ApiRoot
)

if (-not (Test-Path $ApiRoot)) {
    Write-Error "Каталог API не найден: $ApiRoot"
    exit 1
}

$violations = @()
$allowed = @("auth", "characters", "gameplay", "social", "economy", "world", "narrative", "admin", "shared")

Get-ChildItem -Path $ApiRoot -Directory | ForEach-Object {
    if ($allowed -notcontains $_.Name) {
        $violations += "Недопустимый каталог внутри api/v1: $($_.FullName)"
    }
}

if ($violations.Count -gt 0) {
    $violations | ForEach-Object { Write-Error $_ }
    exit 1
}

Write-Output "Директория OpenAPI соответствует архитектурным правилам."

