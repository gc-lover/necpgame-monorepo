<#
.SYNOPSIS
  Формирует агрегированный отчёт по очередям, задачам и статусам агентов.

.DESCRIPTION
  Собирает данные из `shared/trackers/queues/**/*.yaml` и (опционально) из
  `pipeline/tasks/**` и сохраняет сводку в YAML-файл (`shared/trackers/status-dashboard.yaml`
  по умолчанию). Отчёт отображает количество карточек по очередям и сгруппированно по ролям.

.EXAMPLE
  pwsh -File pipeline/scripts/status-dashboard.ps1

.EXAMPLE
  pwsh -File pipeline/scripts/status-dashboard.ps1 -IncludeTasks -Output shared/trackers/status-dashboard.yaml

.PARAMETER Output
  Путь к результирующему YAML (относительно корня репозитория).

.PARAMETER IncludeTasks
  Включает подсчёт файлов задач в `pipeline/tasks/**`.

.PARAMETER MaxItemsPerQueue
  Количество элементов, сохраняемых в выборке `items_sample` (по умолчанию 5).

.PARAMETER DryRun
  Выводит YAML в STDOUT без записи на диск.
#>

[CmdletBinding()]
param(
    [string]$Output = 'shared/trackers/status-dashboard.yaml',

    [switch]$IncludeTasks,

    [int]$MaxItemsPerQueue = 5,

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
    param([Parameter(Mandatory = $true)]$Data, [int]$Depth = 10)
    $convertCmd = Get-Command -Name ConvertTo-Yaml -ErrorAction Stop
    if ($convertCmd.Parameters.ContainsKey('Depth')) {
        return ConvertTo-Yaml -Data $Data -Depth $Depth
    }
    return ConvertTo-Yaml -Data $Data
}

function Load-YamlFile {
    param([string]$Path)
    try {
        $raw = Get-Content -LiteralPath $Path -Raw
        if ([string]::IsNullOrWhiteSpace($raw)) { return $null }
        return ConvertFrom-Yaml -Yaml $raw
    } catch {
        Write-Warning "Не удалось прочитать YAML: $Path ($($_.Exception.Message))"
        return $null
    }
}

function Get-YamlValue {
    param($Object, [string]$Name)
    if (-not $Object) { return $null }
    if ($Object -is [System.Collections.IDictionary]) {
        return $Object[$Name]
    }
    return $Object.$Name
}

$queuesRoot = Resolve-RepoPath -Path 'shared/trackers/queues'
if (-not (Test-Path -LiteralPath $queuesRoot)) {
    throw "Каталог очередей не найден: $queuesRoot"
}

$queueFiles = Get-ChildItem -Path $queuesRoot -Recurse -Filter '*.yaml' -File |
    Where-Object { $_.Name -ne 'README.md' }

$queueEntries = @()
$segmentSummary = @{}

foreach ($queueFile in $queueFiles) {
    $queueYaml = Load-YamlFile -Path $queueFile.FullName
    if (-not $queueYaml) { continue }

    $relativePath = [System.IO.Path]::GetRelativePath($repoRoot, $queueFile.FullName) -replace '\\','/'
    $itemsValue = Get-YamlValue -Object $queueYaml -Name 'items'
    $items = @()
    if ($itemsValue) {
        if ($itemsValue -is [System.Collections.IEnumerable] -and $itemsValue -isnot [string]) {
            foreach ($entry in $itemsValue) { $items += $entry }
        } else {
            $items += $itemsValue
        }
    }
    $count = $items.Length
    $statusValue = Get-YamlValue -Object $queueYaml -Name 'status'
    $lastUpdated = Get-YamlValue -Object $queueYaml -Name 'last_updated'

    $sample = @()
    if ($items -and $items.Count -gt 0) {
        $take = [Math]::Min($MaxItemsPerQueue, $items.Count)
        for ($i = 0; $i -lt $take; $i++) {
            $item = $items[$i]
            $sample += [ordered]@{
                id = Get-YamlValue -Object $item -Name 'id'
                title = Get-YamlValue -Object $item -Name 'title'
                owner = Get-YamlValue -Object $item -Name 'owner'
            }
        }
    }

    $queueEntries += [ordered]@{
        queue = $relativePath
        status = $statusValue
        last_updated = $lastUpdated
        count = $count
        items_sample = $sample
    }

    $segment = Split-Path -Path ([System.IO.Path]::GetRelativePath($queuesRoot, $queueFile.DirectoryName)) -Leaf
    if (-not $segment) {
        $segment = Split-Path -Path $relativePath -Leaf
    }
    if ([string]::IsNullOrWhiteSpace($segment)) { $segment = 'misc' }

    if (-not $segmentSummary.ContainsKey($segment)) {
        $segmentSummary[$segment] = [ordered]@{
            segment = $segment
            total = 0
            queues = @()
        }
    }
    $segmentSummary[$segment].total += $count
    $segmentSummary[$segment].queues += [ordered]@{
        queue = $relativePath
        count = $count
    }
}

$taskSummary = $null
if ($IncludeTasks) {
    $tasksRoot = Resolve-RepoPath -Path 'pipeline/tasks'
    if (Test-Path -LiteralPath $tasksRoot) {
        $taskSummary = @{}
        $taskFiles = Get-ChildItem -Path $tasksRoot -Recurse -Filter '*.yaml' -File
        foreach ($taskFile in $taskFiles) {
            $relative = [System.IO.Path]::GetRelativePath($tasksRoot, $taskFile.DirectoryName)
            if (-not $taskSummary.ContainsKey($relative)) {
                $taskSummary[$relative] = 0
            }
            $taskSummary[$relative]++
        }
    }
}

$dashboard = [ordered]@{
    generated_at = (Get-Date).ToString('yyyy-MM-ddTHH:mm:ssZ')
    queues = $queueEntries | Sort-Object queue
    segments = @()
}

foreach ($segmentKey in ($segmentSummary.Keys | Sort-Object)) {
    $dashboard.segments += $segmentSummary[$segmentKey]
}

if ($taskSummary) {
    $taskEntries = @()
    foreach ($key in ($taskSummary.Keys | Sort-Object)) {
        $taskEntries += [ordered]@{
            path = $key
            count = $taskSummary[$key]
        }
    }
    $dashboard.tasks = $taskEntries
}

$yamlOutput = ConvertTo-YamlSafe -Data $dashboard
if ($DryRun) {
    Write-Output $yamlOutput
    exit 0
}

$Output = Resolve-RepoPath -Path $Output
Set-Content -LiteralPath $Output -Value $yamlOutput -Encoding UTF8
Write-Output "Обновлён статус-дэшборд: $([System.IO.Path]::GetRelativePath($repoRoot, $Output))"
