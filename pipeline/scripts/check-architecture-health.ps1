param(
    [Parameter(Mandatory = $true)]
    [string]$RootPath
)

if (-not (Test-Path $RootPath)) {
    Write-Error "Корневой путь не найден: $RootPath"
    exit 1
}

$requiredDirs = @(
    "pipeline",
    "shared",
    "shared/docs",
    "shared/trackers",
    "services",
    "services/backend",
    "services/frontend",
    "services/openapi"
)

$missing = @()
foreach ($dir in $requiredDirs) {
    if (-not (Test-Path (Join-Path $RootPath $dir))) {
        $missing += $dir
    }
}

if ($missing.Count -gt 0) {
    Write-Error ("Отсутствуют ключевые репозитории: " + ($missing -join ", "))
    exit 1
}

Write-Output "Архитектура проекта на верхнем уровне в порядке."

