param(
    [switch]$SkipOpenApi,
    [switch]$SkipQueues
)

$ErrorActionPreference = "Stop"

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$repoRoot = Split-Path -Parent $scriptDir

Write-Output "== Проверка архитектуры =="
pwsh -File (Join-Path $scriptDir "check-architecture-health.ps1") -RootPath $repoRoot | Write-Output

Write-Output "== Проверка Markdown в knowledge =="
pwsh -File (Join-Path $scriptDir "check-knowledge-markdown.ps1") | Write-Output

Write-Output "== Проверка review метаданных =="
pwsh -File (Join-Path $scriptDir "check-knowledge-review.ps1") | Write-Output

if (-not $SkipQueues) {
    Write-Output "== Проверка очередей =="
    $queueFiles = Get-ChildItem -Path (Join-Path $repoRoot "shared/trackers/queues") -Filter "*.yaml" -File -Recurse
    foreach ($file in $queueFiles) {
        pwsh -File (Join-Path $scriptDir "check-queue-yaml.ps1") -File $file.FullName | Write-Output
    }
}

if (-not $SkipOpenApi) {
    Write-Output "== Валидация OpenAPI =="
    pwsh -File (Join-Path $scriptDir "validate-swagger.ps1") -ApiDirectory (Join-Path $repoRoot "services/openapi/api/v1") | Write-Output
}

Write-Output "== Проверка Activity Log для индексированных изменений =="
pwsh -File (Join-Path $scriptDir "check-activity-log.ps1") -UseIndex | Write-Output

Write-Output "Pre-commit проверки завершены."


