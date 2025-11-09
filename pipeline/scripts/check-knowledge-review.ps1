param(
    [string]$KnowledgeRoot = "shared/docs/knowledge"
)

$ErrorActionPreference = "Stop"

$convertFromYaml = Get-Command -Name ConvertFrom-Yaml -ErrorAction SilentlyContinue
if (-not $convertFromYaml) {
    throw "Команда ConvertFrom-Yaml недоступна. Установи PowerShell 7+ или модуль powershell-yaml."
}

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$repoRoot = Split-Path -Parent $scriptDir
$knowledgePath = if ([System.IO.Path]::IsPathRooted($KnowledgeRoot)) { $KnowledgeRoot } else { Join-Path $repoRoot $KnowledgeRoot }

if (-not (Test-Path $knowledgePath)) {
    throw "Каталог знаний не найден: $knowledgePath"
}

$violations = @()
$files = Get-ChildItem -Path $knowledgePath -Filter "*.yaml" -File -Recurse
foreach ($file in $files) {
    $content = Get-Content -LiteralPath $file.FullName -Raw
    if ([string]::IsNullOrWhiteSpace($content)) { continue }
    try {
        $doc = ConvertFrom-Yaml -Yaml $content
    } catch {
        continue
    }
    if (-not $doc.metadata) { continue }
    $lastUpdated = $doc.metadata.last_updated
    $reviewed = $doc.metadata.concept_reviewed_at
    if ([string]::IsNullOrWhiteSpace($lastUpdated) -or [string]::IsNullOrWhiteSpace($reviewed)) { continue }

    $parsedUpdated = [datetime]::MinValue
    $parsedReviewed = [datetime]::MinValue
    if (-not [datetime]::TryParse($lastUpdated, [ref]$parsedUpdated)) { continue }
    if (-not [datetime]::TryParse($reviewed, [ref]$parsedReviewed)) { continue }

    if ($parsedUpdated -gt $parsedReviewed) {
        $relative = [System.IO.Path]::GetRelativePath($repoRoot, $file.FullName)
        $violations += "Документ '$relative' обновлён $lastUpdated, но concept_reviewed_at = $reviewed. Требуется пересмотр и обновление очереди."
    }
}

if ($violations.Count -gt 0) {
    $violations | ForEach-Object { Write-Error $_ }
    exit 1
}

Write-Output "Все документы прошли проверку last_updated vs concept_reviewed_at."


