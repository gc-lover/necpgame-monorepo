<#
.SYNOPSIS
  Генерирует файлы задач из очередей shared/trackers/queues/*.

.DESCRIPTION
  Для каждой карточки в очереди создаёт YAML-файл в pipeline/tasks/<stage>/.
  Если -Id не задан, но указан -Prefix, скрипт вычисляет следующий номер (<PREFIX>-000, <PREFIX>-001, …),
  добавляет slug на основе title и обновляет очередь (если не указан -NoQueueUpdate).

.EXAMPLE
  pwsh -File pipeline/scripts/generate-tasks-from-queue.ps1 -QueueFile shared/trackers/queues/backend/not-started.yaml `
       -TargetDirectory pipeline/tasks/06_backend_implementer -Prefix BE

.EXAMPLE
  pwsh -File pipeline/scripts/generate-tasks-from-queue.ps1 -QueueFile shared/trackers/queues/api/queued.yaml `
       -TargetDirectory pipeline/tasks/04_api_task_architect -Prefix API -TemplateFile pipeline/templates/api-task-template.yaml
#>

[CmdletBinding()]
param(
    [Parameter(Mandatory = $true)]
    [string]$QueueFile,

    [Parameter(Mandatory = $true)]
    [string]$TargetDirectory,

    [string]$TemplateFile,

    [string]$Id,

    [string]$Prefix,

    [string]$Actor = "automation",

    [switch]$Force,

    [switch]$DryRun,

    [switch]$NoQueueUpdate,

    [switch]$OverrideExistingId,

    [switch]$DisableActivityLog
)

Set-StrictMode -Version Latest

$ErrorActionPreference = "Stop"

if (-not (Get-Module -ListAvailable -Name powershell-yaml)) {
    throw "Модуль powershell-yaml не найден. Выполни: Install-Module powershell-yaml -Scope CurrentUser"
}

Import-Module -Name powershell-yaml -ErrorAction Stop

$scriptRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$pipelineRoot = Split-Path -Parent $scriptRoot
$repoRoot = Split-Path -Parent $pipelineRoot

$translitMap = @{
    'а' = 'a'; 'б' = 'b'; 'в' = 'v'; 'г' = 'g'; 'д' = 'd';
    'е' = 'e'; 'ё' = 'e'; 'ж' = 'zh'; 'з' = 'z'; 'и' = 'i';
    'й' = 'y'; 'к' = 'k'; 'л' = 'l'; 'м' = 'm'; 'н' = 'n';
    'о' = 'o'; 'п' = 'p'; 'р' = 'r'; 'с' = 's'; 'т' = 't';
    'у' = 'u'; 'ф' = 'f'; 'х' = 'h'; 'ц' = 'c'; 'ч' = 'ch';
    'ш' = 'sh'; 'щ' = 'sch'; 'ъ' = ''; 'ы' = 'y'; 'ь' = '';
    'э' = 'e'; 'ю' = 'yu'; 'я' = 'ya'
}

function Resolve-RepoPath {
    param([string]$Path)
    if ([System.IO.Path]::IsPathRooted($Path)) {
        return (Resolve-Path -LiteralPath $Path).Path
    }
    return (Join-Path -Path $repoRoot -ChildPath $Path)
}

function ConvertTo-Hashtable {
    param($InputObject)
    $json = $InputObject | ConvertTo-Json -Depth 12
    return ($json | ConvertFrom-Json -Depth 12)
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

function Get-QueueItems {
    param(
        $QueueObject,
        [string]$FilterId
    )

    if (-not $QueueObject -or -not $QueueObject.items) {
        return @()
    }

    $items = @($QueueObject.items)
    if ([string]::IsNullOrWhiteSpace($FilterId)) {
        return $items
    }

    return @($items | Where-Object { $_.id -eq $FilterId })
}

function Get-ItemProperty {
    param(
        $Item,
        [string]$Name
    )

    if ($Item -is [System.Collections.IDictionary]) {
        return $Item[$Name]
    }

    $prop = $Item.PSObject.Properties.Match($Name)
    if ($prop.Count -gt 0) {
        return $prop.Value
    }

    return $null
}

function Set-ItemProperty {
    param(
        $Item,
        [string]$Name,
        $Value
    )

    if ($Item -is [System.Collections.IDictionary]) {
        $Item[$Name] = $Value
        return
    }

    $prop = $Item.PSObject.Properties.Match($Name)
    if ($prop.Count -gt 0) {
        $Item.$Name = $Value
    } else {
        $Item | Add-Member -NotePropertyName $Name -NotePropertyValue $Value
    }
}

function Get-Slug {
    param([string]$Title)

    if ([string]::IsNullOrWhiteSpace($Title)) {
        return ''
    }

    $lower = $Title.ToLowerInvariant()
    $builder = New-Object System.Text.StringBuilder

    foreach ($ch in $lower.ToCharArray()) {
        $charStr = [string]$ch
        if ($translitMap.ContainsKey($charStr)) {
            $builder.Append($translitMap[$charStr]) | Out-Null
            continue
        }
        if ($charStr -match '[a-z0-9]') {
            $builder.Append($charStr) | Out-Null
            continue
        }
        $builder.Append('-') | Out-Null
    }

    $slug = $builder.ToString()
    $slug = $slug -replace '-{2,}', '-'
    $slug = $slug.Trim('-')
    if ($slug.Length -gt 60) {
        $slug = $slug.Substring(0, 60).Trim('-')
    }
    return $slug
}

function Get-FileName {
    param(
        [string]$TaskId,
        [string]$Slug
    )

    $safeId = $TaskId -replace '[\\/:*?"<>|]', '-'
    if ([string]::IsNullOrWhiteSpace($Slug)) {
        return "$safeId.yaml"
    }
    $safeSlug = $Slug -replace '[\\/:*?"<>|]', '-'
    return "{0}-{1}.yaml" -f $safeId, $safeSlug
}

function Get-NextId {
    param(
        [string]$Prefix,
        [string]$TargetPath,
        [System.Collections.Generic.HashSet[string]]$Reserved
    )

    $escapedPrefix = [Regex]::Escape($Prefix)
    $pattern = "^{0}-(\d{{3}})(?:-.+)?$" -f $escapedPrefix
    $max = -1

    $existing = Get-ChildItem -Path $TargetPath -Filter "$Prefix-*.yaml" -File -ErrorAction SilentlyContinue
    foreach ($file in $existing) {
        $name = [System.IO.Path]::GetFileNameWithoutExtension($file.Name)
        if ($name -match $pattern) {
            $num = [int]$Matches[1]
            if ($num -gt $max) { $max = $num }
        }
    }

    if ($Reserved) {
        foreach ($reservedId in $Reserved) {
            if ($reservedId -match $pattern) {
                $num = [int]$Matches[1]
                if ($num -gt $max) { $max = $num }
            }
        }
    }

    $next = $max + 1
    if ($next -gt 999) {
        throw "Превышен лимит 999 задач для префикса $Prefix."
    }

    return '{0}-{1}' -f $Prefix, $next.ToString('000')
}

function Replace-Placeholder {
    param(
        [string]$Text,
        [string]$Placeholder,
        [string]$Value
    )

    $pattern = [Regex]::Escape('{{' + $Placeholder + '}}')
    return [Regex]::Replace($Text, $pattern, { param($m) $Value })
}

function Update-ActivityLog {
    param(
        [System.Collections.IEnumerable]$Entries
    )

    if (-not $Entries) { return }
    $entriesList = @()
    foreach ($entry in $Entries) { $entriesList += $entry }
    if ($entriesList.Count -eq 0) { return }

    $activityPath = Resolve-RepoPath -Path 'shared/trackers/activity-log.yaml'
    $logObject = $null

    if (Test-Path -LiteralPath $activityPath) {
        $raw = Get-Content -LiteralPath $activityPath -Raw
        if (-not [string]::IsNullOrWhiteSpace($raw)) {
            try {
                $logObject = ConvertFrom-Yaml -Yaml $raw
            } catch {
                $logObject = $null
            }
        }
    }

    if (-not $logObject) {
        $logObject = [ordered]@{ entries = @() }
    }

    if (-not $logObject.entries) {
        $logObject.entries = @()
    } elseif ($logObject.entries -isnot [System.Collections.IList]) {
        $logObject.entries = @($logObject.entries)
    }

    foreach ($entry in $entriesList) {
        $logObject.entries += $entry
    }

    $yaml = ConvertTo-YamlSafe -Data $logObject
    $header = "# Журнал активностей`n"
    try {
        Set-Content -LiteralPath $activityPath -Value ($header + $yaml) -Encoding UTF8
    } catch {
        [System.IO.File]::WriteAllText($activityPath, $header + $yaml, [System.Text.Encoding]::UTF8)
    }
    Write-Output "Обновлён Activity Log: shared/trackers/activity-log.yaml"
}

$queuePath = Resolve-RepoPath -Path $QueueFile
if (-not (Test-Path -LiteralPath $queuePath)) {
    throw "Файл очереди не найден: $queuePath"
}

$targetPath = Resolve-RepoPath -Path $TargetDirectory
if (-not (Test-Path -LiteralPath $targetPath)) {
    New-Item -ItemType Directory -Path $targetPath | Out-Null
}

$templateText = $null
$templatePath = $null
if ($TemplateFile) {
    $templatePath = Resolve-RepoPath -Path $TemplateFile
    if (-not (Test-Path -LiteralPath $templatePath)) {
        throw "Файл шаблона не найден: $templatePath"
    }
    $templateText = Get-Content -LiteralPath $templatePath -Raw
}

$queueRaw = Get-Content -LiteralPath $queuePath -Raw
$queueObject = ConvertFrom-Yaml -Yaml $queueRaw
$relativeQueuePath = [System.IO.Path]::GetRelativePath($repoRoot, (Resolve-Path -LiteralPath $queuePath))
$items = Get-QueueItems -QueueObject $queueObject -FilterId $Id

if (-not $items -or $items.Count -eq 0) {
    throw "В очереди $relativeQueuePath не найдено элементов (Id='$Id')."
}

$timestamp = (Get-Date).ToString('yyyy-MM-ddTHH:mm:ssZ')
$generated = @()
$queueUpdated = $false
$reservedIds = New-Object 'System.Collections.Generic.HashSet[string]'
$activityEntries = New-Object System.Collections.Generic.List[object]

foreach ($item in $items) {
    $taskId = [string](Get-ItemProperty -Item $item -Name 'id')
    $title = [string](Get-ItemProperty -Item $item -Name 'title')
    $assignedNewId = $false

    if ([string]::IsNullOrWhiteSpace($taskId) -or $OverrideExistingId) {
        if ([string]::IsNullOrWhiteSpace($Prefix)) {
            throw "Для автогенерации идентификатора укажи -Prefix (например, -Prefix VSN)."
        }
        $taskId = Get-NextId -Prefix $Prefix -TargetPath $targetPath -Reserved $reservedIds
        Set-ItemProperty -Item $item -Name 'id' -Value $taskId
        $assignedNewId = $true
        $queueUpdated = $true
        $null = $reservedIds.Add($taskId)
    }
    elseif ($Prefix) {
        $null = $reservedIds.Add($taskId)
    }

    if ([string]::IsNullOrWhiteSpace($title)) {
        $title = $taskId
    }

    $slug = Get-Slug -Title $title
    Write-Verbose ("Slug for {0}: {1}" -f $taskId, $slug)
    $fileName = Get-FileName -TaskId $taskId -Slug $slug
    $taskPath = Join-Path -Path $targetPath -ChildPath $fileName
    $relativeTaskPath = [System.IO.Path]::GetRelativePath($repoRoot, $taskPath)

    if ((Test-Path -LiteralPath $taskPath) -and (-not $Force)) {
        Write-Warning "Файл $relativeTaskPath уже существует. Используй -Force для перезаписи."
        continue
    }

    if ($templateText) {
        $content = $templateText
        $content = Replace-Placeholder -Text $content -Placeholder 'ID' -Value $taskId
        $content = Replace-Placeholder -Text $content -Placeholder 'TITLE' -Value $title
        $content = Replace-Placeholder -Text $content -Placeholder 'QUEUE' -Value $relativeQueuePath
        $content = Replace-Placeholder -Text $content -Placeholder 'GENERATED_AT' -Value $timestamp
    } else {
        $payload = ConvertTo-Hashtable -InputObject $item
        $payload.PSObject.Properties.Remove('id') | Out-Null
        $payload.PSObject.Properties.Remove('title') | Out-Null

        $taskObject = [ordered]@{
            task = [ordered]@{
                id = $taskId
                title = $title
                queue_source = $relativeQueuePath
                created_at = $timestamp
            }
            metadata = [ordered]@{
                generated_by = "pipeline/scripts/generate-tasks-from-queue.ps1"
                generated_at = $timestamp
            }
            payload = $payload
        }

        $ownerValue = Get-ItemProperty -Item $item -Name 'owner'
        if ($ownerValue) {
            $taskObject.task.owner = $ownerValue
        }
        $apiSpecValue = Get-ItemProperty -Item $item -Name 'api_spec'
        if ($apiSpecValue) {
            $taskObject.task.api_spec = $apiSpecValue
        }

        $content = ConvertTo-YamlSafe -Data $taskObject
    }

    if ($DryRun) {
        Write-Output "DRY-RUN: $relativeTaskPath"
        Write-Output $content
        if ($assignedNewId) {
            Write-Output "DRY-RUN: сгенерирован ID $taskId для элемента без идентификатора"
        }
        continue
    }

    Set-Content -LiteralPath $taskPath -Value $content -Encoding UTF8
    $generated += $relativeTaskPath
    if ($assignedNewId) {
        Write-Output "Создан файл задачи: $relativeTaskPath (новый ID $taskId)"
    } else {
        Write-Output "Создан файл задачи: $relativeTaskPath"
    }

    $activityEntry = [ordered]@{
        date = (Get-Date -Format 'yyyy-MM-dd HH:mm')
        actor = $Actor
        title = "Создана задача $taskId"
        queue = $relativeQueuePath
        task = $relativeTaskPath
    }
    if ($TemplateFile) {
        $activityEntry.template = [System.IO.Path]::GetRelativePath($repoRoot, $templatePath)
    }
    if ($assignedNewId) {
        $activityEntry.generated_id = $true
    }
    $null = $activityEntries.Add($activityEntry)
}

if (-not $DryRun -and $queueUpdated -and -not $NoQueueUpdate) {
    $queueObject.last_updated = (Get-Date -Format 'yyyy-MM-dd HH:mm')
    $updatedQueueYaml = ConvertTo-YamlSafe -Data $queueObject
    try {
        Set-Content -LiteralPath $queuePath -Value $updatedQueueYaml -Encoding UTF8
    } catch {
        [System.IO.File]::WriteAllText($queuePath, $updatedQueueYaml, [System.Text.Encoding]::UTF8)
    }
    Write-Output "Обновлена очередь: $relativeQueuePath"
}

if (-not $DryRun -and -not $DisableActivityLog) {
    Update-ActivityLog -Entries $activityEntries
}

if (-not $DryRun -and $generated.Count -eq 0) {
    Write-Warning "Ни один файл не был создан."
}
