param(
    [Parameter(Mandatory = $true)]
    [string]$File
)

if (-not (Test-Path $File)) {
    Write-Error "Файл задания не найден: $File"
    exit 1
}

$content = Get-Content -LiteralPath $File -Raw

$checks = @(
    @{
        Key = "Task ID"
        Patterns = @("task_id\s*:", "\*\*Task ID:\*\*")
    },
    @{
        Key = "Microservice"
        Patterns = @("microservice[^\n]*name\s*:", "\*\*Microservice:\*\*")
    },
    @{
        Key = "Base path"
        Patterns = @("base_path\s*:", "\*\*base-path:\*\*")
    },
    @{
        Key = "API directory"
        Patterns = @("api\s*directory\s*:", "\*\*API directory:\*\*")
    },
    @{
        Key = "Endpoints table"
        Patterns = @("\| Method \| Path \|", "##\s*Endpoints")
    },
    @{
        Key = "Acceptance criteria"
        Patterns = @("Acceptance Criteria")
    }
)

$failed = @()
foreach ($check in $checks) {
    $matched = $false
    foreach ($pattern in $check.Patterns) {
        if ($content -match $pattern) {
            $matched = $true
            break
        }
    }
    if (-not $matched) {
        $failed += $check.Key
    }
}

if ($failed.Count -gt 0) {
    Write-Error ("Отсутствуют обязательные секции: " + ($failed -join ", "))
    exit 1
}

Write-Output "API task выглядит корректно: $File"

