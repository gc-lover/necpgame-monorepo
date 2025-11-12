<#
.SYNOPSIS
  Создаёт YAML-документ знания на основе шаблона и обновляет глоссарий.

.DESCRIPTION
  Копирует шаблон `shared/docs/knowledge/templates/knowledge-entry-template.yaml`,
  заполняет ключевые поля (metadata, history, review) и сохраняет файл в указанной
  директории. Идентификатор можно задать вручную или сгенерировать автоматически
  по префиксу с учётом существующих записей в knowledge-glossary.yaml.

.EXAMPLE
  pwsh -File pipeline/scripts/new-knowledge.ps1 \
       -Title "История корпорации Vantor" \
       -DocumentType canon \
       -Category lore \
       -OutputDirectory shared/docs/knowledge/canon/lore \
       -Tags corp,history,lore

.PARAMETER Title
  Название документа (metadata.title).

.PARAMETER DocumentType
  Значение metadata.document_type (canon|mechanics|content|implementation|analysis|archive).

.PARAMETER Category
  Значение metadata.category (например, vision, factions, combat).

.PARAMETER OutputDirectory
  Целевая директория внутри `shared/docs/knowledge`.

.PARAMETER Status
  Значение metadata.status (по умолчанию draft).

.PARAMETER Tags
  Список тегов (metadata.tags).

.PARAMETER Topics
  Список тем (metadata.topics).

.PARAMETER Owners
  Роли владельцев (metadata.owners[].role). Контакты остаются пустыми.

.PARAMETER Id
  Идентификатор знания. Если не указан, генерируется автоматически из Prefix.

.PARAMETER Prefix
  Префикс для автогенерации идентификатора (по умолчанию KNL).

.PARAMETER TemplatePath
  Путь к YAML-шаблону (по умолчанию shared/docs/knowledge/templates/knowledge-entry-template.yaml).

.PARAMETER GlossaryPath
  Путь к knowledge-glossary.yaml (по умолчанию shared/docs/knowledge/knowledge-glossary.yaml).

.PARAMETER Actor
  Имя для записи в Activity Log (опционально).

.PARAMETER DryRun
  Показывает результат без сохранения файлов и обновления глоссария.
#>

[CmdletBinding()]
param(
    [Parameter(Mandatory = $true)]
    [string]$Title,

    [Parameter(Mandatory = $true)]
    [ValidateSet('canon','mechanics','content','implementation','analysis','archive')]
    [string]$DocumentType,

    [Parameter(Mandatory = $true)]
    [string]$OutputDirectory,

    [string]$Category = 'vision',

    [string]$Status = 'draft',

    [string[]]$Tags = @(),

    [string[]]$Topics = @(),

    [string[]]$Owners = @('vision_manager'),

    [string]$Id,

    [string]$Prefix = 'KNL',

    [string]$TemplatePath = 'shared/docs/knowledge/templates/knowledge-entry-template.yaml',

    [string]$GlossaryPath = 'shared/docs/knowledge/knowledge-glossary.yaml',

    [string]$Actor,

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

$TemplatePath = Resolve-RepoPath -Path $TemplatePath
if (-not (Test-Path -LiteralPath $TemplatePath)) {
    throw "Не найден шаблон: $TemplatePath"
}

$GlossaryPath = Resolve-RepoPath -Path $GlossaryPath
if (-not (Test-Path -LiteralPath $GlossaryPath)) {
    throw "Не найден knowledge-glossary.yaml: $GlossaryPath"
}

$OutputDirectory = Resolve-RepoPath -Path $OutputDirectory
if (-not (Test-Path -LiteralPath $OutputDirectory)) {
    New-Item -ItemType Directory -Path $OutputDirectory -Force | Out-Null
}

$translitMap = @{
    'а'='a';'б'='b';'в'='v';'г'='g';'д'='d';
    'е'='e';'ё'='e';'ж'='zh';'з'='z';'и'='i';
    'й'='y';'к'='k';'л'='l';'м'='m';'н'='n';
    'о'='o';'п'='p';'р'='r';'с'='s';'т'='t';
    'у'='u';'ф'='f';'х'='h';'ц'='c';'ч'='ch';
    'ш'='sh';'щ'='sch';'ъ'='';'ы'='y';'ь'='';
    'э'='e';'ю'='yu';'я'='ya'
}

function Get-Slug {
    param([string]$Value)
    if ([string]::IsNullOrWhiteSpace($Value)) { return '' }
    $lower = $Value.ToLowerInvariant()
    $builder = New-Object System.Text.StringBuilder
    foreach ($ch in $lower.ToCharArray()) {
        $char = [string]$ch
        if ($translitMap.ContainsKey($char)) {
            $builder.Append($translitMap[$char]) | Out-Null
        } elseif ($char -match '[a-z0-9]') {
            $builder.Append($char) | Out-Null
        } else {
            $builder.Append('-') | Out-Null
        }
    }
    $slug = $builder.ToString()
    $slug = $slug -replace '-{2,}','-'
    $slug = $slug.Trim('-')
    if ($slug.Length -gt 80) {
        $slug = $slug.Substring(0,80).Trim('-')
    }
    return $slug
}

function ConvertTo-YamlSafe {
    param(
        [Parameter(Mandatory = $true)]
        $Data,
        [int]$Depth = 12
    )

    $convertCmd = Get-Command -Name ConvertTo-Yaml -ErrorAction Stop
    if ($convertCmd.Parameters.ContainsKey('Depth')) {
        return ConvertTo-Yaml -Data $Data -Depth $Depth
    }
    return ConvertTo-Yaml -Data $Data
}

function Load-YamlFile {
    param([string]$Path)
    $raw = Get-Content -LiteralPath $Path -Raw
    if ([string]::IsNullOrWhiteSpace($raw)) { return $null }
    return ConvertFrom-Yaml -Yaml $raw
}

$glossary = Load-YamlFile -Path $GlossaryPath
if (-not $glossary) {
    throw "Не удалось прочитать knowledge-glossary.yaml"
}

if (-not $Id) {
    $existingIds = @()
    if ($glossary.documents) {
        foreach ($doc in $glossary.documents) {
            if ($doc.id -and $doc.id -like "$Prefix-*") {
                $existingIds += $doc.id
            }
        }
    }
    $max = -1
    foreach ($existing in $existingIds) {
        if ($existing -match "^$Prefix-(\d{3})") {
            $num = [int]$Matches[1]
            if ($num -gt $max) { $max = $num }
        }
    }
    $Id = "{0}-{1}" -f $Prefix, ($max + 1).ToString('000')
}

$slug = Get-Slug -Value $Title
$targetFile = Join-Path -Path $OutputDirectory -ChildPath ($slug ? "$slug.yaml" : "$Id.yaml")
$relativePath = [System.IO.Path]::GetRelativePath($repoRoot, $targetFile) -replace '\\','/'

$template = Load-YamlFile -Path $TemplatePath
if (-not $template) {
    throw "Не удалось загрузить шаблон знания"
}

$now = (Get-Date)
$timestampIso = $now.ToString('yyyy-MM-ddTHH:mm:ssZ')
$historyDate = $now.ToString('yyyy-MM-dd')

$metadata = $template.metadata
if (-not $metadata) { $metadata = [ordered]@{} }
$metadata.id = $Id
$metadata.title = $Title
$metadata.document_type = $DocumentType
$metadata.category = $Category
$metadata.status = $Status
$metadata.version = '0.1.0'
$metadata.last_updated = $timestampIso
$metadata.concept_approved = $false
$metadata.concept_reviewed_at = ''
$metadata.tags = @($Tags)
$metadata.topics = @($Topics)
$metadata.related_systems = @()
$metadata.related_documents = @()
$metadata.visibility = 'internal'
if (-not $metadata.audience) { $metadata.audience = @('concept') }

$ownerObjects = @()
foreach ($owner in $Owners) {
    $ownerObjects += [ordered]@{ role = $owner; contact = '' }
}
$metadata.owners = $ownerObjects

$review = $template.review
if (-not $review) { $review = [ordered]@{} }
$review.chain = @([ordered]@{ role = 'vision_manager'; reviewer = ''; reviewed_at = ''; status = 'pending' })
$review.next_actions = @()

$history = @([ordered]@{
        version = '0.1.0'
        date = $historyDate
        author = if ($Actor) { $Actor } else { 'automation' }
        changes = 'Создан документ.'
    })

$validation = $template.validation
if (-not $validation) { $validation = [ordered]@{} }
$validation.schema_version = '1.0'
$validation.checksum = ''

$template.metadata = $metadata
$template.review = $review
$template.history = $history
$template.validation = $validation

if ($DryRun) {
    $yaml = ConvertTo-YamlSafe -Data $template
    Write-Output "DRY-RUN: создали бы $relativePath"
    Write-Output $yaml
    exit 0
}

$yamlContent = ConvertTo-YamlSafe -Data $template
Set-Content -LiteralPath $targetFile -Value $yamlContent -Encoding UTF8
Write-Output "Создан документ знания: $relativePath"

# Обновление глоссария
if (-not $glossary.documents) { $glossary.documents = @() }
$glossary.documents += [ordered]@{
    id = $Id
    file = $relativePath
    title = $Title
    status = $Status
    tags = @($Tags)
    summary = ''
    risk_level = 'medium'
}

$glossaryYaml = ConvertTo-YamlSafe -Data $glossary
Set-Content -LiteralPath $GlossaryPath -Value $glossaryYaml -Encoding UTF8
Write-Output "Обновлён knowledge-glossary.yaml"

if ($Actor) {
    $activityPath = Resolve-RepoPath -Path 'shared/trackers/activity-log.yaml'
    $activityRaw = if (Test-Path $activityPath) { Get-Content -LiteralPath $activityPath -Raw } else { '' }
    $activity = if ($activityRaw) { ConvertFrom-Yaml -Yaml $activityRaw } else { [ordered]@{ entries = @() } }
    if (-not $activity.entries) { $activity.entries = @() }
    $activity.entries += [ordered]@{
        date = (Get-Date -Format 'yyyy-MM-dd HH:mm')
        actor = $Actor
        title = "Создан документ знания $Id"
        document = $relativePath
    }
    $activityYaml = ConvertTo-YamlSafe -Data $activity
    $header = "# Журнал активностей`n"
    Set-Content -LiteralPath $activityPath -Value ($header + $activityYaml) -Encoding UTF8
    Write-Output "Activity Log обновлён."
}
