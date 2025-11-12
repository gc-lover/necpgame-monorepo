<#
.SYNOPSIS
  Завершает задачу: переносит карточку в целевую очередь, обновляет Activity Log и архивирует YAML-файл задачи.

.DESCRIPTION
  Скрипт читает YAML-файл задачи из pipeline/tasks/*, находит соответствующую запись в исходной очереди,
  переносит её в целевую очередь, обновляет отметки времени и по умолчанию перемещает YAML задачи в архив
  (`pipeline/tasks/archive/<role>/`). Дополнительно можно запустить синхронизацию через
  `generate-tasks-from-queue.ps1 -ValidateSyncOnly`, чтобы актуализировать `pipeline/tasks/index.yaml`.

.EXAMPLE
  pwsh -File pipeline/scripts/complete-task.ps1 `
       -TaskFile pipeline/tasks/06_backend_implementer/BE-042-api-layer.yaml `
       -TargetQueueFile shared/trackers/queues/backend/completed.yaml `
       -Actor "Backend Implementer"

.PARAMETER TaskFile
  Путь к YAML-файлу задачи, созданному генератором.

.PARAMETER TargetQueueFile
  Очередь, в которую нужно перенести карточку (например, shared/trackers/queues/backend/completed.yaml).

.PARAMETER SourceQueueFile
  Необязательный параметр, если исходная очередь отличается от queue_source, указанного в YAML задачи.

.PARAMETER Actor
  Имя агента для записи в Activity Log. По умолчанию — automation.

.PARAMETER Archive
  Управляет архивацией YAML-файла задачи (в pipeline/tasks/archive/<role>/). Значение по умолчанию — включено.

.PARAMETER ArchiveDirectory
  Каталог, в который будет перенесён файл задачи при архивации (относительно корня репозитория).

.PARAMETER SkipIndexUpdate
  Если указан, не запускает пересборку pipeline/tasks/index.yaml.

.PARAMETER DisableActivityLog
  Отключает автоматическую запись в Activity Log.

.PARAMETER DryRun
  Выводит план действий без изменения файлов.
#>

[CmdletBinding()]
param(
    [Parameter(Mandatory = $true)]
    [string]$TaskFile,

    [Parameter(Mandatory = $true)]
    [string]$TargetQueueFile,

    [string]$SourceQueueFile,

    [string]$Actor = "automation",

    [switch]$Archive = $true,

    [string]$ArchiveDirectory = "pipeline/tasks/archive",

    [switch]$SkipIndexUpdate,

    [switch]$DisableActivityLog,

    [switch]$DryRun,

    [switch]$NoAutoCommit,

    [string]$CommitMessage
)

$scriptName = Split-Path -Leaf $MyInvocation.MyCommand.Path
$repoRoot = $null
$logReference = $null

try {

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

if (-not (Get-Module -ListAvailable -Name powershell-yaml)) {
    throw "Модуль powershell-yaml не найден. Выполни: Install-Module powershell-yaml -Scope CurrentUser"
}

Import-Module -Name powershell-yaml -ErrorAction Stop

$scriptRoot   = Split-Path -Parent $MyInvocation.MyCommand.Path
$automationModulePath = Join-Path -Path $scriptRoot -ChildPath "modules/Pipeline.Automation.psm1"
if (-not (Test-Path -LiteralPath $automationModulePath)) {
    throw "Не найден модуль автоматизации: $automationModulePath"
}
Import-Module -Name $automationModulePath -ErrorAction Stop

$pipelineRoot = Split-Path -Parent $scriptRoot
$repoRoot     = Split-Path -Parent $pipelineRoot

$commitPathSet = New-Object System.Collections.Generic.HashSet[string]([System.StringComparer]::OrdinalIgnoreCase)
function Add-CommitRelativePath {
    param([string]$RelativePath)
    if ([string]::IsNullOrWhiteSpace($RelativePath)) { return }
    $normalized = $RelativePath -replace '\\','/'
    $null = $commitPathSet.Add($normalized)
}

$roleDefinitions = @(
    [ordered]@{ Role = 'refactor-agent'; Actor = 'Refactor Agent'; TasksDirectory = 'pipeline/tasks/01_refactor_agent' },
    [ordered]@{ Role = 'concept-director'; Actor = 'Concept Director'; TasksDirectory = 'pipeline/tasks/02_concept_director' },
    [ordered]@{ Role = 'vision-manager'; Actor = 'Vision Manager'; TasksDirectory = 'pipeline/tasks/03_vision_manager' },
    [ordered]@{ Role = 'api-task-architect'; Actor = 'API Task Architect'; TasksDirectory = 'pipeline/tasks/04_api_task_architect' },
    [ordered]@{ Role = 'openapi-executor'; Actor = 'OpenAPI Executor'; TasksDirectory = 'pipeline/tasks/05_openapi_executor' },
    [ordered]@{ Role = 'backend-implementer'; Actor = 'Backend Implementer'; TasksDirectory = 'pipeline/tasks/06_backend_implementer' },
    [ordered]@{ Role = 'frontend-implementer'; Actor = 'Frontend Implementer'; TasksDirectory = 'pipeline/tasks/07_frontend_implementer' },
    [ordered]@{ Role = 'qa-agent'; Actor = 'QA Agent'; TasksDirectory = 'pipeline/tasks/08_qa_validation' },
    [ordered]@{ Role = 'devops-agent'; Actor = 'DevOps Agent'; TasksDirectory = 'pipeline/tasks/09_devops_release' },
    [ordered]@{ Role = 'analytics-agent'; Actor = 'Analytics Agent'; TasksDirectory = 'pipeline/tasks/10_analytics_agent' },
    [ordered]@{ Role = 'community-agent'; Actor = 'Community & Release Comms Agent'; TasksDirectory = 'pipeline/tasks/11_community_agent' },
    [ordered]@{ Role = 'security-agent'; Actor = 'Security & Compliance Agent'; TasksDirectory = 'pipeline/tasks/12_security_agent' },
    [ordered]@{ Role = 'ux-agent'; Actor = 'UX & UI Agent'; TasksDirectory = 'pipeline/tasks/13_ux_agent' },
    [ordered]@{ Role = 'data-balancing-agent'; Actor = 'Data & Balancing Agent'; TasksDirectory = 'pipeline/tasks/14_data_balancing_agent' }
)

$roleDefinitionMap = @{}
foreach ($definition in $roleDefinitions) {
    $roleDefinitionMap[$definition.Role] = $definition
}

$taskDirectoryRoleMap = @{
    '01_refactor_agent'       = 'refactor-agent'
    '02_concept_director'     = 'concept-director'
    '03_vision_manager'       = 'vision-manager'
    '04_api_task_architect'   = 'api-task-architect'
    '05_openapi_executor'     = 'openapi-executor'
    '06_backend_implementer'  = 'backend-implementer'
    '07_frontend_implementer' = 'frontend-implementer'
    '08_qa_validation'        = 'qa-agent'
    '09_devops_release'       = 'devops-agent'
    '10_analytics_agent'      = 'analytics-agent'
    '11_community_agent'      = 'community-agent'
    '12_security_agent'       = 'security-agent'
    '13_ux_agent'             = 'ux-agent'
    '14_data_balancing_agent' = 'data-balancing-agent'
}

function Resolve-RepoPath {
    param([string]$Path)
    if ([System.IO.Path]::IsPathRooted($Path)) {
        return (Resolve-Path -LiteralPath $Path).Path
    }
    return (Join-Path -Path $repoRoot -ChildPath $Path)
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

function Get-RoleKeyFromTaskPath {
    param([string]$TaskPath)
    $relative = [System.IO.Path]::GetRelativePath($repoRoot, $TaskPath)
    $parts = $relative -split '[\\/]'
    if ($parts.Length -ge 3) {
        $folderName = $parts[2]
        if ($taskDirectoryRoleMap.ContainsKey($folderName)) {
            return $taskDirectoryRoleMap[$folderName]
        }
    }
    return $null
}

function Get-RoleInfo {
    param([string]$RoleKey)
    if ($roleDefinitionMap.ContainsKey($RoleKey)) {
        $info = $roleDefinitionMap[$RoleKey]
        return [ordered]@{
            Role = $info.Role
            Actor = $info.Actor
            TasksDirectory = $info.TasksDirectory
        }
    }
    return [ordered]@{
        Role = $RoleKey
        Actor = $RoleKey
        TasksDirectory = $null
    }
}

function Ensure-Directory {
    param([string]$Path)
    if (-not (Test-Path -LiteralPath $Path)) {
        New-Item -ItemType Directory -Path $Path | Out-Null
    }
}

$taskPath = Resolve-RepoPath -Path $TaskFile
if (-not (Test-Path -LiteralPath $taskPath)) {
    throw "Файл задачи не найден: $TaskFile"
}

$targetQueuePath = Resolve-RepoPath -Path $TargetQueueFile
if (-not (Test-Path -LiteralPath $targetQueuePath)) {
    throw "Целевая очередь не найдена: $TargetQueueFile"
}

$taskRaw = Get-Content -LiteralPath $taskPath -Raw
$taskYaml = ConvertFrom-Yaml -Yaml $taskRaw
if (-not $taskYaml) {
    throw "Не удалось разобрать YAML задачи: $TaskFile"
}

$taskNode = if ($taskYaml -is [System.Collections.IDictionary]) { $taskYaml['task'] } else { $taskYaml.task }
if (-not $taskNode) {
    throw "В файле задачи отсутствует раздел task"
}

$taskId = $taskNode.id
if ([string]::IsNullOrWhiteSpace($taskId)) {
    throw "В файле задачи отсутствует идентификатор"
}

$taskTitle = if ($taskNode.title) { [string]$taskNode.title } else { $taskId }
$taskOwner = $taskNode.owner
$taskQueueSource = $taskNode.queue_source
$taskDirectory = Split-Path -Parent $taskPath

if (-not $SourceQueueFile) {
    if ([string]::IsNullOrWhiteSpace($taskQueueSource)) {
        throw "Не удалось определить исходную очередь. Укажите параметр -SourceQueueFile."
    }
    $SourceQueueFile = $taskQueueSource
}

$sourceQueuePath = Resolve-RepoPath -Path $SourceQueueFile
if (-not (Test-Path -LiteralPath $sourceQueuePath)) {
    throw "Исходная очередь не найдена: $SourceQueueFile"
}

$sourceQueueRaw = Get-Content -LiteralPath $sourceQueuePath -Raw
$sourceQueueObject = ConvertFrom-Yaml -Yaml $sourceQueueRaw
if (-not $sourceQueueObject) {
    throw "Не удалось разобрать исходную очередь: $SourceQueueFile"
}

$targetQueueRaw = Get-Content -LiteralPath $targetQueuePath -Raw
$targetQueueObject = ConvertFrom-Yaml -Yaml $targetQueueRaw
if (-not $targetQueueObject) {
    throw "Не удалось разобрать целевую очередь: $TargetQueueFile"
}

$sourceQueueInfo = Get-QueueObject -QueueFile $SourceQueueFile -RepositoryRoot $repoRoot
$targetQueueInfo = if ($sourceQueuePath -eq $targetQueuePath) {
    $sourceQueueInfo
} else {
    Get-QueueObject -QueueFile $TargetQueueFile -RepositoryRoot $repoRoot
}

$timestamp = (Get-Date).ToString('yyyy-MM-dd HH:mm')

$sourceQueueUpdated = $false
$targetQueueUpdated = $false
$removedItem = $null

foreach ($item in @($sourceQueueInfo.Items | ForEach-Object { $_ })) {
    if ($item.id -eq $taskId) {
        $removedItem = $item
        $null = $sourceQueueInfo.Items.Remove($item)
        $sourceQueueUpdated = $true
        break
    }
}

if (-not $removedItem) {
    $removedItem = [pscustomobject]@{}
}

if (-not $taskOwner -and ($removedItem.PSObject.Properties.Match('owner').Count -gt 0)) {
    $taskOwner = $removedItem.owner
}

$relativeTargetQueue = ([System.IO.Path]::GetRelativePath($repoRoot, $targetQueuePath) -replace '\\','/')

$additionalFields = @{}
foreach ($prop in $removedItem.PSObject.Properties) {
    $name = $prop.Name
    if ($name -in @('id','title','owner','updated','source_document')) { continue }
    $additionalFields[$name] = $prop.Value
}
$additionalFields['completed_at'] = $timestamp
$additionalFields['queue_source'] = $relativeTargetQueue

$sourceDocumentValue = $null
if ($removedItem.PSObject.Properties.Match('source_document').Count -gt 0) {
    $sourceDocumentValue = $removedItem.source_document
}

$validationTargets = New-Object System.Collections.Generic.List[string]
if ($sourceDocumentValue) {
    if ($sourceDocumentValue -is [System.Collections.IEnumerable] -and -not ($sourceDocumentValue -is [string])) {
        foreach ($entry in $sourceDocumentValue) {
            if (-not [string]::IsNullOrWhiteSpace($entry)) {
                $null = $validationTargets.Add([string]$entry)
            }
        }
    } else {
        if (-not [string]::IsNullOrWhiteSpace($sourceDocumentValue)) {
            $null = $validationTargets.Add([string]$sourceDocumentValue)
        }
    }
}

if (-not $DryRun -and $validationTargets.Count -gt 0) {
    $artifactsForValidation = $validationTargets | ForEach-Object { $_ }
    Invoke-ArtifactValidation -RepositoryRoot $repoRoot -Artifacts $artifactsForValidation
}

$queueItemParams = @{
    QueueInfo = $targetQueueInfo
    Id = $taskId
    Title = $taskTitle
    AdditionalFields = $additionalFields
}
if ($taskOwner) { $queueItemParams.Owner = $taskOwner }
if ($sourceDocumentValue) { $queueItemParams.SourceDocument = $sourceDocumentValue }
Set-QueueItem @queueItemParams
$targetQueueUpdated = $true

if ($DryRun) {
    Write-Output "DRY-RUN: удалим $taskId из $SourceQueueFile и добавим в $TargetQueueFile"
} else {
    if ($sourceQueueUpdated) {
        Save-QueueObject -QueueInfo $sourceQueueInfo
        $relativeSourceQueue = ([System.IO.Path]::GetRelativePath($repoRoot, $sourceQueuePath) -replace '\\','/')
        Add-CommitRelativePath -RelativePath $relativeSourceQueue
    }
    if ($targetQueueUpdated -and ($targetQueueInfo -ne $sourceQueueInfo)) {
        Save-QueueObject -QueueInfo $targetQueueInfo
        Add-CommitRelativePath -RelativePath $relativeTargetQueue
    } elseif ($targetQueueUpdated -and ($targetQueueInfo -eq $sourceQueueInfo)) {
        Save-QueueObject -QueueInfo $targetQueueInfo
        Add-CommitRelativePath -RelativePath $relativeTargetQueue
    }
    Write-Output "Обновлены очереди: $SourceQueueFile → $TargetQueueFile"
}

$archivePath = $null
$relativeTaskPath = [System.IO.Path]::GetRelativePath($repoRoot, $taskPath)
$relativeTaskPath = $relativeTaskPath -replace '\\','/'

if ($Archive -and -not $DryRun) {
    $roleKey = Get-RoleKeyFromTaskPath -TaskPath $taskPath
    $roleInfo = Get-RoleInfo -RoleKey $roleKey
    $archiveRoot = Resolve-RepoPath -Path $ArchiveDirectory
    Ensure-Directory -Path $archiveRoot

    $roleArchive = if ($roleInfo.TasksDirectory) {
        $dirName = [System.IO.Path]::GetFileName($roleInfo.TasksDirectory)
        Join-Path -Path $archiveRoot -ChildPath $dirName
    } else {
        Join-Path -Path $archiveRoot -ChildPath 'general'
    }
    Ensure-Directory -Path $roleArchive

    $destinationPath = Join-Path -Path $roleArchive -ChildPath (Split-Path -Leaf $taskPath)
    if (Test-Path -LiteralPath $destinationPath) {
        $suffix = (Get-Date).ToString('yyyyMMddHHmmss')
        $destinationPath = Join-Path -Path $roleArchive -ChildPath ("{0}-{1}{2}" -f [System.IO.Path]::GetFileNameWithoutExtension($taskPath), $suffix, [System.IO.Path]::GetExtension($taskPath))
    }

    # Добавляем отметку о завершении перед перемещением
    $taskNode.completed_at = $timestamp
    $taskNode.queue_source = $relativeTargetQueue
    $taskYaml.task = $taskNode
    Write-YamlFile -Data $taskYaml -Path $taskPath

    Move-Item -LiteralPath $taskPath -Destination $destinationPath
    $archivePath = [System.IO.Path]::GetRelativePath($repoRoot, $destinationPath) -replace '\\','/'
    Write-Output "Задача архивирована: $archivePath"
    Add-CommitRelativePath -RelativePath $archivePath
    $archiveDir = [System.IO.Path]::GetDirectoryName($archivePath)
    if ($archiveDir) { Add-CommitRelativePath -RelativePath ($archiveDir -replace '\\','/') }
    $taskDirForRemoval = [System.IO.Path]::GetDirectoryName($relativeTaskPath)
    if ($taskDirForRemoval) { Add-CommitRelativePath -RelativePath ($taskDirForRemoval -replace '\\','/') }
} elseif (-not $Archive -and -not $DryRun) {
    $taskNode.completed_at = $timestamp
    $taskNode.queue_source = $relativeTargetQueue
    $taskYaml.task = $taskNode
    Write-YamlFile -Data $taskYaml -Path $taskPath
    Write-Output "Задача обновлена (без архивации): $TaskFile"
    Add-CommitRelativePath -RelativePath $relativeTaskPath
} elseif ($DryRun) {
    $archiveStatus = if ($Archive) { 'включено' } else { 'отключено' }
    Write-Output ("DRY-RUN: архивирование {0}" -f $archiveStatus)
}

if (-not $DisableActivityLog -and -not $DryRun) {
    $activityEntry = [ordered]@{
        date = $timestamp
        actor = $Actor
        title = "Завершена задача $taskId"
        queue_from = ([System.IO.Path]::GetRelativePath($repoRoot, $sourceQueuePath) -replace '\\','/')
        queue_to = ([System.IO.Path]::GetRelativePath($repoRoot, $targetQueuePath) -replace '\\','/')
        task = if ($Archive -and $archivePath) { $archivePath } else { $relativeTaskPath }
    }
    if ($Archive -and $archivePath) {
        $activityEntry.archive = $archivePath
    }
    Write-ActivityLogEntry -RepositoryRoot $repoRoot -Entry ([hashtable]$activityEntry)
    Add-CommitRelativePath -RelativePath 'shared/trackers/activity-log.yaml'
}

if (-not $DryRun -and -not $SkipIndexUpdate) {
    $taskDirectoryRelative = ([System.IO.Path]::GetRelativePath($repoRoot, $taskDirectory) -replace '\\','/')
    $targetQueueRelative = ([System.IO.Path]::GetRelativePath($repoRoot, $targetQueuePath) -replace '\\','/')
    & pwsh -NoLogo -File (Join-Path $scriptRoot 'generate-tasks-from-queue.ps1') -QueueFile $targetQueueRelative -TargetDirectory $taskDirectoryRelative -ValidateSyncOnly | Out-Null
    Add-CommitRelativePath -RelativePath 'pipeline/tasks/index.yaml'
    if ($sourceQueuePath -ne $targetQueuePath) {
        $sourceQueueRelative = ([System.IO.Path]::GetRelativePath($repoRoot, $sourceQueuePath) -replace '\\','/')
        & pwsh -NoLogo -File (Join-Path $scriptRoot 'generate-tasks-from-queue.ps1') -QueueFile $sourceQueueRelative -TargetDirectory $taskDirectoryRelative -ValidateSyncOnly | Out-Null
    }
}

if (-not $DryRun -and -not $NoAutoCommit) {
    if ($Archive -and $archivePath) {
        Add-CommitRelativePath -RelativePath $archivePath
    }
    Add-CommitRelativePath -RelativePath $relativeTargetQueue
    if ($sourceQueuePath -ne $targetQueuePath) {
        $sourceRelativeForCommit = ([System.IO.Path]::GetRelativePath($repoRoot, $sourceQueuePath) -replace '\\','/')
        Add-CommitRelativePath -RelativePath $sourceRelativeForCommit
    }
    $taskDirectoryForCommit = [System.IO.Path]::GetDirectoryName($relativeTaskPath)
    if ($taskDirectoryForCommit) {
        Add-CommitRelativePath -RelativePath ($taskDirectoryForCommit -replace '\\','/')
    }
    if ($commitPathSet.Count -gt 0) {
        $pathsToCommit = $commitPathSet | ForEach-Object { $_ }
        $defaultMessage = if ($CommitMessage) { $CommitMessage } else { "chore(tasks): complete {0}" -f $taskId }
        Invoke-AutoCommit -RepositoryRoot $repoRoot -Paths $pathsToCommit -Message $defaultMessage | Out-Null
    }
}

if ($DryRun) {
    Write-Output "DRY-RUN завершён. Ни один файл не был изменён."
}

}
catch {
    $errorRecord = $_
    $message = $errorRecord.Exception.Message
    if ([string]::IsNullOrWhiteSpace($message)) {
        $message = $errorRecord.ToString()
    }

    $context = [ordered]@{}
    if ($errorRecord.Exception) {
        $context.exception_type = $errorRecord.Exception.GetType().FullName
        if ($errorRecord.Exception.InnerException) {
            $context.inner_exception = $errorRecord.Exception.InnerException.Message
        }
    }
    if ($errorRecord.ScriptStackTrace) {
        $context.script_stack = $errorRecord.ScriptStackTrace
    }

    $parameters = [ordered]@{}
    if ($TaskFile) { $parameters.TaskFile = $TaskFile }
    if ($TargetQueueFile) { $parameters.TargetQueueFile = $TargetQueueFile }
    if ($SourceQueueFile) { $parameters.SourceQueueFile = $SourceQueueFile }
    if ($Actor) { $parameters.Actor = $Actor }
    $parameters.Archive = [bool]$Archive
    $parameters.DryRun = [bool]$DryRun
    $parameters.SkipIndexUpdate = [bool]$SkipIndexUpdate
    $parameters.NoAutoCommit = [bool]$NoAutoCommit

    if ($repoRoot -and (Test-Path -LiteralPath $repoRoot)) {
        try {
            if (Get-Command -Name Write-AutomationErrorLog -ErrorAction SilentlyContinue) {
                $logResult = Write-AutomationErrorLog -RepositoryRoot $repoRoot -ScriptName $scriptName -Message $message -Context $context -Parameters $parameters
                if ($logResult -and $logResult.Path) {
                    $logReference = $logResult.Path
                }
            }
        } catch {
            Write-Warning ("Не удалось записать ошибку в лог: {0}" -f $_.Exception.Message)
        }
    }

    Write-Error ("Скрипт {0} завершился с ошибкой: {1}" -f $scriptName, $message)
    if ($logReference) {
        Write-Error ("Подробности: {0}" -f $logReference)
    }
    exit 1
}

