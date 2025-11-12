<#
.SYNOPSIS
  Преобразует YAML-чеклисты из `pipeline/checklists` в задачи для Cursor (`.cursor/tasks/*.json`).

.DESCRIPTION
  Для каждого чеклиста создаёт JSON-файл вида `{ id, title, sections[], items[] }`, добавляя ссылки на исходный YAML.
  Опционально можно обработать один конкретный файл или все чеклисты сразу. Плейсхолдеры в тексте сохраняются как есть.

.EXAMPLE
  pwsh -File pipeline/scripts/generate-checklist-tasks.ps1 -Checklist pipeline/checklists/idea-to-api.yaml

.EXAMPLE
  pwsh -File pipeline/scripts/generate-checklist-tasks.ps1 -All

.PARAMETER Checklist
  Путь к конкретному чеклисту YAML.

.PARAMETER All
  Генерирует задачи для всех чеклистов в `pipeline/checklists`.

.PARAMETER OutputDirectory
  Каталог для сохранения JSON-файлов (по умолчанию `.cursor/tasks`).

.PARAMETER Force
  Перезаписывать существующие файлы без подтверждения.

.PARAMETER DryRun
  Показывает превью JSON без записи на диск.
#>

[CmdletBinding(DefaultParameterSetName = 'Single')]
param(
    [Parameter(ParameterSetName = 'Single', Mandatory = $true)]
    [string]$Checklist,

    [Parameter(ParameterSetName = 'All', Mandatory = $true)]
    [switch]$All,

    [string]$OutputDirectory = '.cursor/tasks',

    [switch]$Force,

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

function Get-ChecklistValue {
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

function Get-ChecklistFiles {
    if ($All) {
        return Get-ChildItem -Path (Resolve-RepoPath 'pipeline/checklists') -Filter '*.yaml' -File -Recurse | Sort-Object FullName
    }
    $path = Resolve-RepoPath -Path $Checklist
    if (-not (Test-Path -LiteralPath $path)) {
        throw "Чеклист не найден: $Checklist"
    }
    return @(Get-Item -LiteralPath $path)
}

function Convert-Checklist {
    param([System.IO.FileInfo]$File)

    $raw = Get-Content -LiteralPath $File.FullName -Raw
    $yaml = ConvertFrom-Yaml -Yaml $raw
    if (-not $yaml) {
        throw "Не удалось разобрать чеклист: $($File.FullName)"
    }

    $checklist = if ($yaml.checklist) { $yaml.checklist } else { $yaml }
    $id = $checklist.id
    $title = $checklist.title
    $sections = Get-ChecklistValue -Object $checklist -Name 'sections'

    if (-not $id) {
        $id = [System.IO.Path]::GetFileNameWithoutExtension($File.Name)
    }
    if (-not $title) {
        $title = $id
    }

    $result = [ordered]@{
        id = $id
        title = $title
        source = ([System.IO.Path]::GetRelativePath($repoRoot, $File.FullName) -replace '\\','/')
        generated_at = (Get-Date).ToString('yyyy-MM-ddTHH:mm:ssZ')
        sections = @()
    }

    if ($sections) {
        foreach ($section in $sections) {
            $sectionTitle = Get-ChecklistValue -Object $section -Name 'title'
            if (-not $sectionTitle) { $sectionTitle = '' }
            $items = @()
            $sectionItems = Get-ChecklistValue -Object $section -Name 'items'
            if ($sectionItems) {
                foreach ($item in $sectionItems) {
                    $label = Get-ChecklistValue -Object $item -Name 'text'
                    if (-not $label) { $label = '' }
                    $scripts = Get-ChecklistValue -Object $item -Name 'scripts'
                    $scriptsList = New-Object System.Collections.Generic.List[string]
                    if ($scripts) {
                        if ($scripts -is [System.Collections.IEnumerable] -and $scripts -isnot [string]) {
                            foreach ($cmd in $scripts) {
                                if ($cmd) { $scriptsList.Add([string]$cmd) | Out-Null }
                            }
                        } else {
                            $scriptsList.Add([string]$scripts) | Out-Null
                        }
                    }
                    $items += [ordered]@{
                        label = [string]$label
                        scripts = $scriptsList
                        status = 'pending'
                    }
                }
            }
            $result.sections += [ordered]@{
                title = $sectionTitle
                items = $items
            }
        }
    }

    return $result
}

$files = Get-ChecklistFiles
if ($files.Count -eq 0) {
    Write-Output "Чеклисты не найдены"
    exit 0
}

$outputPath = Resolve-RepoPath -Path $OutputDirectory
if (-not $DryRun) {
    if (-not (Test-Path -LiteralPath $outputPath)) {
        New-Item -ItemType Directory -Path $outputPath | Out-Null
    }
}

foreach ($file in $files) {
    $checklistData = Convert-Checklist -File $file
    $json = $checklistData | ConvertTo-Json -Depth 6
    $targetFileName = ($checklistData.id + '.json')
    $targetPath = Join-Path -Path $outputPath -ChildPath $targetFileName

    Write-Output "Готов чеклист: $($checklistData.id) → $targetFileName"
    if ($DryRun) {
        Write-Output $json
        continue
    }

    if ((Test-Path -LiteralPath $targetPath) -and -not $Force) {
        throw "Файл уже существует: $targetPath. Используй -Force для перезаписи."
    }

    [System.IO.File]::WriteAllText($targetPath, $json + [Environment]::NewLine, [System.Text.Encoding]::UTF8)
}
