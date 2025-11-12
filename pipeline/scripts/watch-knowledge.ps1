<#
.SYNOPSIS
  Сканирует `shared/docs/knowledge` и автоматически формирует карточки для повторного ревью.

.DESCRIPTION
  1. Для каждого YAML-документа проверяется `metadata.last_updated` и `metadata.concept_reviewed_at`.
     Если документ обновлён, но не пересмотрен, создаётся запись в указанной очереди
     (по умолчанию `shared/trackers/queues/vision/review.yaml`).
  2. Опционально обрабатывает `shared/docs/knowledge/knowledge-glossary.yaml` и добавляет
     карточки в соответствующие очереди на основе полей `status` и `risk_level`.

.EXAMPLE
  pwsh -File pipeline/scripts/watch-knowledge.ps1 -ProcessGlossary

.PARAMETER KnowledgeRoot
  Каталог с YAML-знаниями (по умолчанию `shared/docs/knowledge`).

.PARAMETER QueueFile
  Очередь, куда будут добавляться карточки на ревью (`shared/trackers/queues/vision/review.yaml`).

.PARAMETER ProcessGlossary
  Обрабатывать `knowledge-glossary.yaml` и создавать карточки в целевых очередях.

.PARAMETER GlossaryQueueMap
  Хеш-таблица маршрутизации статусов глоссария в очереди (по умолчанию draft/in-review → vision, needs-update/high → refactor).

.PARAMETER DryRun
  Только показывает действия без записи на диск.
#>

[CmdletBinding()]
param(
    [string]$KnowledgeRoot = 'shared/docs/knowledge',

    [string]$QueueFile = 'shared/trackers/queues/vision/review.yaml',

    [switch]$ProcessGlossary,

    [hashtable]$GlossaryQueueMap,

    [switch]$DryRun
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

if (-not (Get-Module -ListAvailable -Name powershell-yaml)) {
    throw "Модуль powershell-yaml не найден. Выполни: Install-Module powershell-yaml -Scope CurrentUser"
}

Import-Module -Name powershell-yaml -ErrorAction Stop

$scriptRoot   = Split-Path -Parent $MyInvocation.MyCommand.Path
$pipelineRoot = Split-Path -Parent $scriptRoot
$repoRoot     = Split-Path -Parent $pipelineRoot

function Resolve-RepoPath {
    param([string]$Path)
    if ([System.IO.Path]::IsPathRooted($Path)) {
        return (Resolve-Path -LiteralPath $Path).Path
    }
    return (Join-Path -Path $repoRoot -ChildPath $Path)
}

function Get-RelativePath {
    param([string]$Path)
    return ([System.IO.Path]::GetRelativePath($repoRoot, $Path) -replace '\\','/')
}

function Get-YamlValue {
    param(
        $Object,
        [string]$Name
    )

    if (-not $Object) { return $null }
    if ($Object -is [System.Collections.IDictionary]) {
        return $Object[$Name]
    }
    return $Object.$Name
}

function Load-YamlFile {
    param([string]$Path)
    if (-not (Test-Path -LiteralPath $Path)) {
        return $null
    }
    $raw = Get-Content -LiteralPath $Path -Raw
    if ([string]::IsNullOrWhiteSpace($raw)) { return $null }
    try {
        return ConvertFrom-Yaml -Yaml $raw
    } catch {
        Write-Warning "Не удалось разобрать YAML: $Path ($($_.Exception.Message))"
        return $null
    }
}

function ConvertTo-YamlSafe {
    param(
        [Parameter(Mandatory = $true)]
        $Data,
        [int]$Depth = 8
    )

    $convertCmd = Get-Command -Name ConvertTo-Yaml -ErrorAction Stop
    if ($convertCmd.Parameters.ContainsKey('Depth')) {
        return ConvertTo-Yaml -Data $Data -Depth $Depth
    }
    return ConvertTo-Yaml -Data $Data
}

function Save-YamlFile {
    param(
        [string]$Path,
        $Object
    )
    $yaml = ConvertTo-YamlSafe -Data $Object -Depth 8
    Set-Content -LiteralPath $Path -Value $yaml -Encoding UTF8
}

function Evaluate-KnowledgeDocuments {
    param(
        [string]$RootPath,
        [string]$QueuePath,
        [switch]$Preview
    )

    $knowledgePath = Resolve-RepoPath -Path $RootPath
    $queueFullPath = Resolve-RepoPath -Path $QueuePath

    $excludePatterns = @(
        'templates/',
        'analysis/tasks/',
        'knowledge-glossary.yaml',
        'structure-guidelines.yaml',
        'changelog.yaml',
        'index.yaml'
    )

    $files = Get-ChildItem -Path $knowledgePath -Filter '*.yaml' -File -Recurse | Where-Object {
        $rel = Get-RelativePath -Path $_.FullName
        foreach ($pattern in $excludePatterns) {
            if ($rel -like "*${pattern}*") { return $false }
        }
        return $true
    }

    if ($files.Count -eq 0) {
        Write-Output "Документы знаний не найдены"
        return
    }

    $queueObject = Load-YamlFile -Path $queueFullPath
    if (-not $queueObject) {
        $queueObject = [ordered]@{ status = 'review'; last_updated = ''; items = @() }
    }
    if (-not $queueObject.items) {
        $queueObject.items = @()
    }

    $existingDocuments = @{}
    foreach ($item in $queueObject.items) {
        if ($item -is [System.Collections.IDictionary] -and $item.Contains('document')) {
            $existingDocuments[$item['document']] = $true
        }
    }

    $timestamp = (Get-Date).ToString('yyyy-MM-dd HH:mm')
    $added = @()

    foreach ($file in $files) {
        $doc = Load-YamlFile -Path $file.FullName
        if (-not $doc) { continue }
        $metadata = Get-YamlValue -Object $doc -Name 'metadata'
        if (-not $metadata) { continue }

        $lastUpdated = Get-YamlValue -Object $metadata -Name 'last_updated'
        $conceptReviewed = Get-YamlValue -Object $metadata -Name 'concept_reviewed_at'
        $title = Get-YamlValue -Object $metadata -Name 'title'

        if (-not $lastUpdated) { continue }
        try { $lastUpdatedDt = [DateTime]::Parse($lastUpdated) } catch { continue }
        $conceptReviewedDt = $null
        if ($conceptReviewed) {
            try { $conceptReviewedDt = [DateTime]::Parse($conceptReviewed) } catch { $conceptReviewedDt = $null }
        }

        $needsReview = $false
        if (-not $conceptReviewedDt) {
            $needsReview = $true
        } elseif ($lastUpdatedDt -gt $conceptReviewedDt) {
            $needsReview = $true
        }

        if (-not $needsReview) { continue }

        $relativeDoc = Get-RelativePath -Path $file.FullName
        if ($existingDocuments.ContainsKey($relativeDoc)) { continue }

        $item = [ordered]@{
            title = if ($title) { $title } else { $relativeDoc }
            document = $relativeDoc
            owner = 'Readiness Reviewer'
            last_updated = $lastUpdated
            reason = 'last_updated>concept_reviewed_at'
        }

        Write-Output "⏱ Требуется ревью: $relativeDoc"
        $added += $item
        if (-not $Preview) {
            $queueObject.items += $item
        }
    }

    if (-not $Preview -and $added.Count -gt 0) {
        $queueObject.last_updated = $timestamp
        Save-YamlFile -Path $queueFullPath -Object $queueObject
        Write-Output "Обновлена очередь: $QueuePath ($($added.Count) новых записей)"
    }
}

function Process-Glossary {
    param(
        [hashtable]$Routing,
        [switch]$Preview
    )

    $glossaryPath = Resolve-RepoPath -Path 'shared/docs/knowledge/knowledge-glossary.yaml'
    $glossary = Load-YamlFile -Path $glossaryPath
    if (-not $glossary) {
        Write-Output "Файл knowledge-glossary.yaml не найден или пуст"
        return
    }

    $documents = Get-YamlValue -Object $glossary -Name 'documents'
    if (-not $documents) { return }

    $defaultRouting = @{
        'draft'        = 'shared/trackers/queues/vision/queued.yaml'
        'in-review'    = 'shared/trackers/queues/vision/review.yaml'
        'needs-update' = 'shared/trackers/queues/refactor/queued.yaml'
        'high-risk'    = 'shared/trackers/queues/refactor/queued.yaml'
    }

    if ($Routing) {
        foreach ($key in $Routing.Keys) {
            $defaultRouting[$key] = $Routing[$key]
        }
    }

    $glossaryTimestamp = (Get-Date).ToString('yyyy-MM-dd HH:mm')

    foreach ($doc in $documents) {
        $status = Get-YamlValue -Object $doc -Name 'status'
        $risk = Get-YamlValue -Object $doc -Name 'risk_level'
        $relativeFile = Get-YamlValue -Object $doc -Name 'file'
        if (-not $relativeFile) { continue }

        $targetKey = $null
        if ($status -and $defaultRouting.ContainsKey($status)) {
            $targetKey = $status
        } elseif ($risk -eq 'high' -and $defaultRouting.ContainsKey('high-risk')) {
            $targetKey = 'high-risk'
        }

        if (-not $targetKey) { continue }
        $queuePath = Resolve-RepoPath -Path $defaultRouting[$targetKey]
        $queueObject = Load-YamlFile -Path $queuePath
        if (-not $queueObject) {
            $queueObject = [ordered]@{ status = 'queued'; last_updated = ''; items = @() }
        }
        if (-not $queueObject.items) { $queueObject.items = @() }

        $existing = $queueObject.items | Where-Object {
            ($_ -is [System.Collections.IDictionary]) -and $_.Contains('document') -and ($_.document -eq $relativeFile)
        }
        if ($existing) { continue }

        $title = Get-YamlValue -Object $doc -Name 'title'
        $entry = [ordered]@{
            title = if ($title) { $title } else { $relativeFile }
            document = $relativeFile
            owner = switch ($targetKey) {
                'needs-update' { 'Refactor Agent' }
                'high-risk'    { 'Refactor Agent' }
                'in-review'    { 'Readiness Reviewer' }
                default        { 'Vision Manager' }
            }
            status = $status
            risk_level = $risk
            reason = "glossary:$targetKey"
        }

        Write-Output "📌 Добавление карточки из глоссария: $relativeFile → $($defaultRouting[$targetKey])"
        if (-not $Preview) {
            $queueObject.items += $entry
            $queueObject.last_updated = $glossaryTimestamp
            Save-YamlFile -Path $queuePath -Object $queueObject
        }
    }
}

Evaluate-KnowledgeDocuments -RootPath $KnowledgeRoot -QueuePath $QueueFile -Preview:$DryRun

if ($ProcessGlossary) {
    Process-Glossary -Routing $GlossaryQueueMap -Preview:$DryRun
}
