param(
    [string]$HooksPath
)

$ErrorActionPreference = "Stop"

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$repoRoot = Split-Path -Parent $scriptDir

if (-not $HooksPath) {
    $HooksPath = Join-Path $repoRoot ".git/hooks"
}

if (-not (Test-Path $HooksPath)) {
    throw "Каталог git hooks не найден: $HooksPath"
}

$shellHookPath = Join-Path $HooksPath "pre-commit"
$psHookPath = Join-Path $HooksPath "pre-commit.ps1"
$relativeScript = "pipeline/scripts/run-precommit.ps1"

$shellHook = @"
#!/bin/sh
SCRIPT_DIR="\$(cd "\$(dirname "\$0")" && pwd)"
ROOT="\$(cd "\$SCRIPT_DIR/.." && pwd)"
pwsh -NoLogo -NoProfile -File "\$ROOT/$relativeScript"
exit \$?
"@

$psHook = @"
param()
\$scriptDir = Split-Path -Parent \$MyInvocation.MyCommand.Path
\$root = Split-Path -Parent \$scriptDir
pwsh -NoLogo -NoProfile -File (Join-Path \$root '$relativeScript')
if (\$LASTEXITCODE -ne 0) { exit \$LASTEXITCODE }
"@

Set-Content -LiteralPath $shellHookPath -Value $shellHook -Encoding UTF8 -NoNewline
Set-Content -LiteralPath $psHookPath -Value $psHook -Encoding UTF8

try {
    & git update-index --chmod=+x -- $shellHookPath | Out-Null
} catch {
    Write-Warning "Не удалось выставить +x через git update-index. Установи вручную: chmod +x $shellHookPath"
}

Write-Output "Pre-commit hook установлен. Перед первым коммитом убедись, что файл исполняемый (chmod +x .git/hooks/pre-commit на Linux/macOS)."


