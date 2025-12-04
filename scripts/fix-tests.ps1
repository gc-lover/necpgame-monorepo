# Script to fix common test errors
# This script fixes missing imports and common issues

$servicesPath = "services"

# Services that need api import
$servicesNeedingApi = @(
    "battle-pass-service-go",
    "character-engram-compatibility-service-go",
    "character-engram-core-service-go",
    "combat-ai-service-go",
    "combat-combos-service-go",
    "combat-hacking-service-go",
    "combat-implants-core-service-go",
    "combat-implants-stats-service-go",
    "combat-sandevistan-service-go",
    "combat-sessions-service-go",
    "combat-turns-service-go",
    "faction-core-service-go",
    "feedback-service-go",
    "gameplay-progression-core-service-go",
    "gameplay-weapon-special-mechanics-service-go"
)

foreach ($service in $servicesNeedingApi) {
    $benchFile = Join-Path $servicesPath "$service/server/handlers_bench_test.go"
    if (Test-Path $benchFile) {
        $content = Get-Content $benchFile -Raw
        
        # Add api import if missing
        if ($content -notmatch "pkg/api") {
            # Find module name
            $goMod = Get-Content (Join-Path $servicesPath "$service/go.mod") | Select-Object -First 1
            $moduleName = ($goMod -replace "module ", "").Trim()
            
            # Add import after context import
            if ($content -match 'import\s*\(\s*"context"') {
                $content = $content -replace '("context")', "`$1`n`t`"$moduleName/pkg/api`""
                Set-Content -Path $benchFile -Value $content -NoNewline
                Write-Host "Fixed api import for $service"
            }
        }
    }
}

Write-Host "Done fixing imports!"

