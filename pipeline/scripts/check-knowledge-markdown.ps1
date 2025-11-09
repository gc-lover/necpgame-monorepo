param(
    [string]$KnowledgeRoot = "shared/docs/knowledge",
    [switch]$FailOnMarkdown
)

$ErrorActionPreference = "Stop"

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$repoRoot = Split-Path -Parent $scriptDir
$knowledgePath = if ([System.IO.Path]::IsPathRooted($KnowledgeRoot)) { $KnowledgeRoot } else { Join-Path $repoRoot $KnowledgeRoot }

if (-not (Test-Path $knowledgePath)) {
    throw "Каталог знаний не найден: $knowledgePath"
}

$markdownFiles = Get-ChildItem -Path $knowledgePath -Recurse -Include *.md -File
if ($markdownFiles.Count -eq 0) {
    Write-Output "Markdown-файлы в knowledge не найдены."
    exit 0
}

Write-Output "Обнаружены Markdown-файлы в knowledge (рекомендуется перенести в YAML):"
foreach ($file in $markdownFiles) {
    $relative = [System.IO.Path]::GetRelativePath($repoRoot, $file.FullName)
    Write-Output " - $relative"
}

if ($FailOnMarkdown) {
    throw "Найдены Markdown-файлы в knowledge. Перенеси их в YAML или исключи явно."
}


