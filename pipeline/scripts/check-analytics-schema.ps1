param(
    [Parameter(Mandatory = $true)]
    [string]$SchemaFile
)

if (-not (Test-Path $SchemaFile)) {
    Write-Error "Файл схемы аналитики не найден: $SchemaFile"
    exit 1
}

$content = Get-Content -LiteralPath $SchemaFile -Raw

$requiredFields = @("event_name", "payload", "context", "owner")
foreach ($field in $requiredFields) {
    if (-not ($content -match "$field\s*:\s*")) {
        Write-Error "Схема аналитики не содержит поле: $field"
        exit 1
    }
}

Write-Output "Схема аналитики заполнена корректно."

