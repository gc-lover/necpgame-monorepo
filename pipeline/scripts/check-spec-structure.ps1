param(
    [Parameter(Mandatory = $true)]
    [string]$Spec
)

if (-not (Test-Path $Spec)) {
    Write-Error "Файл спецификации не найден: $Spec"
    exit 1
}

$raw = Get-Content -LiteralPath $Spec -Raw

$requiredPatterns = @(
    @{ Name = "info.x-microservice"; Pattern = "info:\s*.*?x-microservice\s*:" },
    @{ Name = "servers production"; Pattern = "https://api\.necp\.game/v1" },
    @{ Name = "components reference"; Pattern = "\$ref:.*shared/common" }
)

$missing = @()
foreach ($item in $requiredPatterns) {
    if (-not ($raw -match $item.Pattern)) {
        $missing += $item.Name
    }
}

if ($missing.Count -gt 0) {
    Write-Error ("Нарушение структуры OpenAPI: отсутствуют " + ($missing -join ", "))
    exit 1
}

Write-Output "Структура OpenAPI в порядке: $Spec"

