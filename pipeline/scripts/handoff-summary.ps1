<#
.SYNOPSIS
  Формирует краткую сводку для передачи задач между агентами.

.DESCRIPTION
  Читает YAML-очередь и подготавливает список карточек с указанием исполнителя,
  статуса и связанных task-файлов. Результат выводится в Markdown (по умолчанию)
  либо YAML и может быть сохранён в файл. При указании `-Actor` добавляет запись
  в Activity Log.

.EXAMPLE
  pwsh -File pipeline/scripts/handoff-summary.ps1 \
       -QueueFile shared/trackers/queues/backend/in-progress.yaml \
       -Role "Backend Implementer" \
       -TasksDirectory pipeline/tasks/06_backend_implementer \
       -Output handoff/backend-to-frontend.md

.PARAMETER QueueFile
  Путь к YAML-очереди.

.PARAMETER Role
  Название роли/агента для заголовка (опционально).

.PARAMETER TasksDirectory
  Каталог с YAML-файлами задач (используется для сопоставления по ID).

.PARAMETER Output
  Путь для сохранения сводки. Если не указан — вывод в STDOUT.

.PARAMETER Format
  Формат вывода: markdown или yaml (по умолчанию markdown).

.PARAMETER Actor
  Имя для записи в Activity Log (при указании добавляется запись).

.PARAMETER DryRun
  Только вычисляет сводку без записи Activity Log.
#>

[CmdletBinding()]
param(
    [Parameter(Mandatory = $true)]
    [string]$QueueFile,

    [string]$Role = '',

    [string]$TasksDirectory,

    [string]$Output,

    [ValidateSet('markdown','yaml')]
    [string]$Format = 'markdown',

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

function ConvertTo-YamlSafe {
    param([Parameter(Mandatory = $true)]$Data, [int]$Depth = 8)
    $convertCmd = Get-Command -Name ConvertTo-Yaml -ErrorAction Stop
    if ($convertCmd.Parameters.ContainsKey('Depth')) {
        return ConvertTo-Yaml -Data $Data -Depth $Depth
    }
    return ConvertTo-Yaml -Data $Data
}

function Get-YamlValue {
    param($Object, [string]$Name)
    if (-not $Object) { return $null }
    if ($Object -is [System.Collections.IDictionary]) {
        if ($Object.Contains($Name)) { return $Object[$Name] }
        return $null
    }
    try {
        return $Object.$Name
    } catch {
        return $null
    }
}

$QueueFile = Resolve-RepoPath -Path $QueueFile
if (-not (Test-Path -LiteralPath $QueueFile)) {
    throw "Очередь не найдена: $QueueFile"
}

$queueRaw = Get-Content -LiteralPath $QueueFile -Raw
$queueYaml = if ([string]::IsNullOrWhiteSpace($queueRaw)) { $null } else { ConvertFrom-Yaml -Yaml $queueRaw }
if (-not $queueYaml) {
    throw "Очередь пуста или невалидна: $QueueFile"
}

$tasksMap = @{}
if ($TasksDirectory) {
    $TasksDirectory = Resolve-RepoPath -Path $TasksDirectory
    if (Test-Path -LiteralPath $TasksDirectory) {
        $taskFiles = Get-ChildItem -Path $TasksDirectory -Recurse -Filter '*.yaml' -File
        foreach ($taskFile in $taskFiles) {
            try {
                $taskRaw = Get-Content -LiteralPath $taskFile.FullName -Raw
                $taskYaml = ConvertFrom-Yaml -Yaml $taskRaw
                if ($taskYaml.task.id) {
                    $tasksMap[$taskYaml.task.id] = [System.IO.Path]::GetRelativePath($repoRoot, $taskFile.FullName) -replace '\\','/'
                }
            } catch {
                Write-Warning "Не удалось прочитать задачу: $($taskFile.FullName)"
            }
        }
    }
}

$items = @($queueYaml.items)
$summaryItems = @()
foreach ($item in $items) {
    $entry = [ordered]@{
        id = Get-YamlValue -Object $item -Name 'id'
        title = Get-YamlValue -Object $item -Name 'title'
        owner = Get-YamlValue -Object $item -Name 'owner'
        notes = Get-YamlValue -Object $item -Name 'description'
        task_file = $null
    }
    $entryId = $entry['id']
    if (-not $entryId) {
        $entryId = '(no-id)'
        $entry['id'] = $entryId
    }
    if ($entryId -and $tasksMap.ContainsKey($entryId)) {
        $entry['task_file'] = $tasksMap[$entryId]
    }
    $queueSource = Get-YamlValue -Object $item -Name 'queue_source'
    $statusValue = Get-YamlValue -Object $item -Name 'status'
    if ($queueSource) { $entry['queue_source'] = $queueSource }
    if ($statusValue) { $entry['status'] = $statusValue }
    $summaryItems += $entry
}

$relativeQueue = [System.IO.Path]::GetRelativePath($repoRoot, $QueueFile) -replace '\\','/'
$summary = [ordered]@{
    role = $Role
    queue = $relativeQueue
    generated_at = (Get-Date).ToString('yyyy-MM-ddTHH:mm:ssZ')
    total = $summaryItems.Count
    items = $summaryItems
}

$outputText = ''
if ($Format -eq 'yaml') {
    $outputText = ConvertTo-YamlSafe -Data $summary
} else {
    $sb = New-Object System.Text.StringBuilder
    $heading = if ($Role) { "## Handoff: $Role" } else { '## Handoff Summary' }
    $null = $sb.AppendLine($heading)
    $null = $sb.AppendLine([string]::Format(' - Queue: `{0}`', $relativeQueue))
    $null = $sb.AppendLine([string]::Format(' - Generated: {0}', $summary.generated_at))
    $null = $sb.AppendLine([string]::Format(' - Total items: {0}', $summary.total))
    $null = $sb.AppendLine()
    foreach ($entry in $summaryItems) {
        $null = $sb.AppendLine([string]::Format(' - **{0}** — {1}', $entry['id'], $entry['title']))
        if ($entry['owner'])        { $null = $sb.AppendLine([string]::Format('   - Owner: {0}', $entry['owner'])) }
        if ($entry['status'])       { $null = $sb.AppendLine([string]::Format('   - Status: {0}', $entry['status'])) }
        if ($entry['queue_source']) { $null = $sb.AppendLine([string]::Format('   - Queue source: `{0}`', $entry['queue_source'])) }
        if ($entry['task_file'])    { $null = $sb.AppendLine([string]::Format('   - Task file: `{0}`', $entry['task_file'])) }
        if ($entry['notes'])        { $null = $sb.AppendLine([string]::Format('   - Notes: {0}', $entry['notes'])) }
        $null = $sb.AppendLine()
    }
    $outputText = $sb.ToString()
}

if ($Output) {
    $outputPath = Resolve-RepoPath -Path $Output
    $outputDir = Split-Path -Parent $outputPath
    if (-not (Test-Path -LiteralPath $outputDir)) {
        New-Item -ItemType Directory -Path $outputDir -Force | Out-Null
    }
    Set-Content -LiteralPath $outputPath -Value $outputText -Encoding UTF8
    Write-Output "Сводка сохранена: $([System.IO.Path]::GetRelativePath($repoRoot, $outputPath))"
} else {
    Write-Output $outputText
}

if ($Actor -and -not $DryRun) {
    $activityPath = Resolve-RepoPath -Path 'shared/trackers/activity-log.yaml'
    $activityRaw = if (Test-Path $activityPath) { Get-Content -LiteralPath $activityPath -Raw } else { '' }
    $activity = if ($activityRaw) { ConvertFrom-Yaml -Yaml $activityRaw } else { [ordered]@{ entries = @() } }
    if (-not $activity.entries) { $activity.entries = @() }
    $activity.entries += [ordered]@{
        date = (Get-Date -Format 'yyyy-MM-dd HH:mm')
        actor = $Actor
        title = "Handoff подготовлен ($relativeQueue)"
        queue = $relativeQueue
        items = $summary.total
        output = if ($Output) { [System.IO.Path]::GetRelativePath($repoRoot, (Resolve-RepoPath -Path $Output)) -replace '\\','/' } else { null }
    }
    $activityYaml = ConvertTo-YamlSafe -Data $activity
    $header = "# Журнал активностей`n"
    Set-Content -LiteralPath $activityPath -Value ($header + $activityYaml) -Encoding UTF8
    Write-Output "Activity Log обновлён."
}
