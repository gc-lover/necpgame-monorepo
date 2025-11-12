<#
.SYNOPSIS
  Генерирует файлы задач из очередей shared/trackers/queues/*.

.DESCRIPTION
  Для каждой карточки в очереди создаёт YAML-файл в pipeline/tasks/<stage>/.
  Если -Id не задан, но указан -Prefix, скрипт вычисляет следующий номер (<PREFIX>-000, <PREFIX>-001, …),
  добавляет slug на основе title и обновляет очередь (если не указан -NoQueueUpdate).
  После генерации автоматически обновляет индекс задач (pipeline/tasks/index.yaml) и при необходимости
  проверяет синхронизацию очередей и task-файлов (параметры -ValidateSync или -ValidateSyncOnly).

.EXAMPLE
  pwsh -File pipeline/scripts/generate-tasks-from-queue.ps1 -QueueFile shared/trackers/queues/backend/not-started.yaml `
       -TargetDirectory pipeline/tasks/06_backend_implementer -Prefix BE

.EXAMPLE
  pwsh -File pipeline/scripts/generate-tasks-from-queue.ps1 -QueueFile shared/trackers/queues/api/queued.yaml `
       -TargetDirectory pipeline/tasks/04_api_task_architect -Prefix API -TemplateFile pipeline/templates/api-task-template.yaml

.EXAMPLE
  pwsh -File pipeline/scripts/generate-tasks-from-queue.ps1 -QueueFile shared/trackers/queues/refactor/queued.yaml `
       -TargetDirectory pipeline/tasks/01_refactor_agent -ValidateSyncOnly

  Проверка синхронизации между очередью и задачами без генерации новых файлов.
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

    [switch]$DisableActivityLog,

    [switch]$SkipIndexUpdate,

    [switch]$ValidateSync,

    [switch]$ValidateSyncOnly
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

    $scriptRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
    $automationModulePath = Join-Path -Path $scriptRoot -ChildPath "modules/Pipeline.Automation.psm1"
    if (-not (Test-Path -LiteralPath $automationModulePath)) {
        throw "Не найден модуль автоматизации: $automationModulePath"
    }
    Import-Module -Name $automationModulePath -ErrorAction Stop

    $pipelineRoot = Split-Path -Parent $scriptRoot
    $repoRoot = Split-Path -Parent $pipelineRoot

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

    $queueRoleRouting = @{
        concept  = @{ default = 'concept-director' }
        vision   = @{ default = 'vision-manager' }
        api      = @{
            queued        = 'api-task-architect'
            'in-progress' = 'openapi-executor'
            review        = 'openapi-executor'
            completed     = 'openapi-executor'
        }
        backend  = @{ default = 'backend-implementer' }
        frontend = @{ default = 'frontend-implementer' }
        qa       = @{ default = 'qa-agent' }
        release  = @{ default = 'devops-agent' }
        refactor = @{ default = 'refactor-agent' }
    }

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
        }
        else {
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

    function Get-RoleKeyFromActor {
        param([string]$ActorName)

        if ([string]::IsNullOrWhiteSpace($ActorName)) {
            return $null
        }

        $normalized = ($ActorName -replace '\s+', '-').ToLowerInvariant()
        $normalized = $normalized -replace '[^a-z0-9\-]', ''
        if ($roleDefinitionMap.ContainsKey($normalized)) {
            return $normalized
        }

        switch ($normalized) {
            'backend-agent' { return 'backend-implementer' }
            'frontend-agent' { return 'frontend-implementer' }
            'qa' { return 'qa-agent' }
            default { return $normalized }
        }
    }

    function Get-RoleInfo {
        param(
            [string]$RoleKey,
            [string]$ActorHint
        )

        if ($roleDefinitionMap.ContainsKey($RoleKey)) {
            $original = $roleDefinitionMap[$RoleKey]
            return [ordered]@{
                Role           = $original.Role
                Actor          = $original.Actor
                TasksDirectory = $original.TasksDirectory
            }
        }

        return [ordered]@{
            Role           = $RoleKey
            Actor          = if ($ActorHint) { $ActorHint } else { $RoleKey }
            TasksDirectory = $null
        }
    }

    function Get-TasksDirectoryForRole {
        param([string]$RoleKey)

        $info = Get-RoleInfo -RoleKey $RoleKey -ActorHint $null
        if ($info.TasksDirectory) {
            return Resolve-RepoPath -Path $info.TasksDirectory
        }
        return $null
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

    function Resolve-RoleFromQueue {
        param(
            [string]$Segment,
            [string]$Status
        )

        if ([string]::IsNullOrWhiteSpace($Segment)) { return $null }
        $segmentKey = $Segment.ToLowerInvariant()
        if (-not $queueRoleRouting.ContainsKey($segmentKey)) { return $null }

        $statusKey = $Status
        if (-not [string]::IsNullOrWhiteSpace($statusKey)) {
            $statusKey = ($statusKey -replace '_', '-').ToLowerInvariant()
        }

        $routes = $queueRoleRouting[$segmentKey]
        if ($statusKey -and $routes.ContainsKey($statusKey)) {
            return $routes[$statusKey]
        }
        if ($routes.ContainsKey('default')) {
            return $routes['default']
        }
        return $null
    }

    function Load-TaskFiles {
        $tasksRoot = Resolve-RepoPath -Path 'pipeline/tasks'
        $result = @{}

        if (-not (Test-Path -LiteralPath $tasksRoot)) {
            return $result
        }

        $taskFiles = Get-ChildItem -Path $tasksRoot -Recurse -Filter '*.yaml' -File -ErrorAction SilentlyContinue
        foreach ($taskFile in $taskFiles) {
            try {
                $raw = Get-Content -LiteralPath $taskFile.FullName -Raw
                $taskYaml = ConvertFrom-Yaml -Yaml $raw
            }
            catch {
                Write-Warning "Не удалось обработать файл задачи $($taskFile.FullName): $($_.Exception.Message)"
                continue
            }

            if (-not $taskYaml) { continue }

            $taskNode = Get-ItemProperty -Item $taskYaml -Name 'task'
            if (-not $taskNode) { continue }

            $taskId = Get-ItemProperty -Item $taskNode -Name 'id'
            if ([string]::IsNullOrWhiteSpace($taskId)) { continue }

            $relativePath = [System.IO.Path]::GetRelativePath($repoRoot, $taskFile.FullName)
            $relativePath = $relativePath -replace '\\', '/'
            $roleKey = Get-RoleKeyFromTaskPath -TaskPath $taskFile.FullName
            $taskTitle = Get-ItemProperty -Item $taskNode -Name 'title'
            $queueSourceValue = Get-ItemProperty -Item $taskNode -Name 'queue_source'
            $createdAtValue = Get-ItemProperty -Item $taskNode -Name 'created_at'

            $entry = [ordered]@{
                id           = $taskId
                title        = $taskTitle
                file         = $relativePath
                queue_source = $queueSourceValue
                role         = $roleKey
                created_at   = $createdAtValue
                processed    = $false
            }

            $result[$taskId] = $entry
        }

        return $result
    }

    function Rebuild-TasksIndex {
        param([switch]$Quiet)

        $taskFiles = Load-TaskFiles
        $queuesRoot = Resolve-RepoPath -Path 'shared/trackers/queues'
        $indexRoles = @{}

        if (Test-Path -LiteralPath $queuesRoot) {
            $queueFiles = Get-ChildItem -Path $queuesRoot -Recurse -Filter '*.yaml' -File -ErrorAction SilentlyContinue
            foreach ($queueFile in $queueFiles) {
                $segment = Split-Path -Path $queueFile.DirectoryName -Leaf
                $statusName = [System.IO.Path]::GetFileNameWithoutExtension($queueFile.Name)
                $roleKey = Resolve-RoleFromQueue -Segment $segment -Status $statusName
                if (-not $roleKey) { continue }

                $roleInfo = Get-RoleInfo -RoleKey $roleKey -ActorHint $null
                if (-not $indexRoles.ContainsKey($roleKey)) {
                    $indexRoles[$roleKey] = [ordered]@{
                        role            = $roleKey
                        actor           = $roleInfo.Actor
                        tasks_directory = $roleInfo.TasksDirectory
                        summary         = [ordered]@{
                            total     = 0
                            by_status = [ordered]@{}
                        }
                        queues          = @()
                        orphan_tasks    = @()
                    }
                }

                $relativeQueue = [System.IO.Path]::GetRelativePath($repoRoot, $queueFile.FullName)
                $relativeQueue = $relativeQueue -replace '\\', '/'

                try {
                    $queueRaw = Get-Content -LiteralPath $queueFile.FullName -Raw
                    $queueYaml = if ([string]::IsNullOrWhiteSpace($queueRaw)) { $null } else { ConvertFrom-Yaml -Yaml $queueRaw }
                }
                catch {
                    Write-Warning ("Failed to read queue {0}: {1}" -f $relativeQueue, $_.Exception.Message)
                    continue
                }

                $items = @()
                if ($queueYaml) {
                    $itemsValue = Get-ItemProperty -Item $queueYaml -Name 'items'
                    if ($itemsValue) {
                        $items = @($itemsValue)
                    }
                }

                $lastUpdatedValue = $null
                if ($queueYaml) {
                    $lastUpdatedValue = Get-ItemProperty -Item $queueYaml -Name 'last_updated'
                }

                $queueEntry = [ordered]@{
                    queue        = $relativeQueue
                    status       = ($statusName -replace '-', '_')
                    last_updated = $lastUpdatedValue
                    items        = @()
                }

                foreach ($item in $items) {
                    $itemId = [string](Get-ItemProperty -Item $item -Name 'id')
                    $itemTitle = [string](Get-ItemProperty -Item $item -Name 'title')
                    $taskEntry = $null

                    if ($itemId -and $taskFiles.ContainsKey($itemId)) {
                        $taskEntry = $taskFiles[$itemId]
                        $taskEntry.processed = $true
                    }

                    $taskFilePath = if ($taskEntry) { $taskEntry.file } else { $null }
                    $queueMatch = $false
                    if ($taskEntry -and $taskEntry.queue_source) {
                        $queueMatch = ($taskEntry.queue_source -eq $relativeQueue)
                    }

                    $queueEntry.items += [ordered]@{
                        id                   = $itemId
                        title                = $itemTitle
                        task_file            = if ($taskFilePath) { $taskFilePath -replace '\\', '/' } else { $null }
                        exists_in_repo       = [bool]$taskEntry
                        queue_source_matches = $queueMatch
                    }

                    $indexRoles[$roleKey].summary.total++
                    $statusKey = $queueEntry.status
                    if (-not $indexRoles[$roleKey].summary.by_status.Contains($statusKey)) {
                        $indexRoles[$roleKey].summary.by_status[$statusKey] = 0
                    }
                    $indexRoles[$roleKey].summary.by_status[$statusKey]++
                }

                $indexRoles[$roleKey].queues += $queueEntry
            }
        }

        foreach ($taskEntry in $taskFiles.Values) {
            if ($taskEntry.processed) { continue }

            $roleKey = $taskEntry.role
            if (-not $roleKey -and $taskEntry.queue_source) {
                $queueParts = $taskEntry.queue_source -split '[\\/]'
                $segmentFromQueue = $null
                $indexOfQueues = [Array]::IndexOf($queueParts, 'queues')
                if ($indexOfQueues -ge 0 -and $queueParts.Length -gt ($indexOfQueues + 1)) {
                    $segmentFromQueue = $queueParts[$indexOfQueues + 1]
                }
                $statusFromQueue = [System.IO.Path]::GetFileNameWithoutExtension($queueParts[-1])
                if ($segmentFromQueue) {
                    $derivedRole = Resolve-RoleFromQueue -Segment $segmentFromQueue -Status $statusFromQueue
                    if ($derivedRole) {
                        $roleKey = $derivedRole
                    }
                }
            }

            if (-not $roleKey) { $roleKey = 'unmapped' }

            $roleInfo = Get-RoleInfo -RoleKey $roleKey -ActorHint $taskEntry.role
            if (-not $indexRoles.ContainsKey($roleKey)) {
                $indexRoles[$roleKey] = [ordered]@{
                    role            = $roleKey
                    actor           = $roleInfo.Actor
                    tasks_directory = $roleInfo.TasksDirectory
                    summary         = [ordered]@{
                        total     = 0
                        by_status = [ordered]@{}
                    }
                    queues          = @()
                    orphan_tasks    = @()
                }
            }

            $indexRoles[$roleKey].orphan_tasks += [ordered]@{
                id           = $taskEntry.id
                title        = $taskEntry.title
                task_file    = $taskEntry.file
                queue_source = $taskEntry.queue_source
            }
        }

        $timestamp = (Get-Date).ToString('yyyy-MM-ddTHH:mm:ssZ')
        $indexObject = [ordered]@{
            tasks_index = [ordered]@{
                generated_at = $timestamp
                roles        = @()
            }
        }

        foreach ($roleKey in ($indexRoles.Keys | Sort-Object)) {
            $roleEntry = $indexRoles[$roleKey]
            $indexObject.tasks_index.roles += $roleEntry
        }

        $indexPath = Resolve-RepoPath -Path 'pipeline/tasks/index.yaml'
        $yaml = ConvertTo-YamlSafe -Data $indexObject
        $header = "# Индекс задач`n"
        try {
            Set-Content -LiteralPath $indexPath -Value ($header + $yaml) -Encoding UTF8
        }
        catch {
            [System.IO.File]::WriteAllText($indexPath, $header + $yaml, [System.Text.Encoding]::UTF8)
        }

        if (-not $Quiet) {
            Write-Output "Обновлён индекс задач: pipeline/tasks/index.yaml"
        }
    }

    function Invoke-QueueSyncValidation {
        param(
            [string]$QueueFile,
            $QueueInfo,
            [string]$TargetDirectory,
            [string]$Actor
        )

        $relativeQueuePath = [System.IO.Path]::GetRelativePath($repoRoot, (Resolve-RepoPath -Path $QueueFile))
        $items = @($QueueInfo.Items | ForEach-Object { $_ })

        $missing = New-Object System.Collections.Generic.List[object]
        $orphan = New-Object System.Collections.Generic.List[object]

        foreach ($item in $items) {
            $itemId = [string](Get-ItemProperty -Item $item -Name 'id')
            if ([string]::IsNullOrWhiteSpace($itemId)) { continue }

            $matchingFiles = @()
            if (Test-Path -LiteralPath $TargetDirectory) {
                $matchingFiles = @(Get-ChildItem -Path $TargetDirectory -Filter "$itemId*.yaml" -File -ErrorAction SilentlyContinue)
            }

            if ($matchingFiles.Count -eq 0) {
                $missing.Add([ordered]@{
                        id    = $itemId
                        title = [string](Get-ItemProperty -Item $item -Name 'title')
                        queue = $relativeQueuePath
                    })
            }
        }

        if (Test-Path -LiteralPath $TargetDirectory) {
            $taskFiles = @(Get-ChildItem -Path $TargetDirectory -Filter '*.yaml' -File -ErrorAction SilentlyContinue)
            foreach ($taskFile in $taskFiles) {
                try {
                    $taskRaw = Get-Content -LiteralPath $taskFile.FullName -Raw
                    $taskYaml = ConvertFrom-Yaml -Yaml $taskRaw
                }
                catch {
                    Write-Warning "Не удалось прочитать файл задачи $($taskFile.FullName) во время валидации: $($_.Exception.Message)"
                    continue
                }

                if (-not $taskYaml) { continue }
                $taskNode = Get-ItemProperty -Item $taskYaml -Name 'task'
                if (-not $taskNode) { continue }
                $taskId = Get-ItemProperty -Item $taskNode -Name 'id'
                $queueSource = Get-ItemProperty -Item $taskNode -Name 'queue_source'
                if (-not $taskId) { continue }

                $existsInQueue = $false
                foreach ($item in $items) {
                    if ((Get-ItemProperty -Item $item -Name 'id') -eq $taskId) {
                        $existsInQueue = $true
                        break
                    }
                }

                if (-not $existsInQueue -and ($queueSource -eq $relativeQueuePath -or -not $queueSource)) {
                    $orphan.Add([ordered]@{
                            id           = $taskId
                            task_file    = [System.IO.Path]::GetRelativePath($repoRoot, $taskFile.FullName)
                            queue_source = $queueSource
                        })
                }
            }
        }

        if ($missing.Count -eq 0 -and $orphan.Count -eq 0) {
            Write-Output "Синхронизация очереди $relativeQueuePath в порядке."
            return $true
        }

        Write-Warning "Обнаружены несоответствия между очередью и задачами ($relativeQueuePath)."
        if ($missing.Count -gt 0) {
            Write-Warning "Отсутствуют файлы задач для следующих элементов очереди:"
            $missing | ForEach-Object {
                Write-Warning ("  - {0} ({1})" -f $_.id, $_.title)
            }
        }

        if ($orphan.Count -gt 0) {
            Write-Warning "Найдены задачи без записей в очереди:"
            $orphan | ForEach-Object {
                Write-Warning ("  - {0} ({1})" -f $_.id, $_.task_file)
            }
        }

        return $false
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

    $queueInfo = Get-QueueObject -QueueFile $QueueFile -RepositoryRoot $repoRoot
    $relativeQueuePath = [System.IO.Path]::GetRelativePath($repoRoot, $queueInfo.ResolvedPath)
    $relativeQueuePath = $relativeQueuePath -replace '\\', '/'

    $allQueueItems = @($queueInfo.Items | ForEach-Object { $_ })
    $items = if ([string]::IsNullOrWhiteSpace($Id)) { $allQueueItems } else { @($allQueueItems | Where-Object { $_.id -eq $Id }) }

    if ($ValidateSyncOnly) {
        $validationResult = Invoke-QueueSyncValidation -QueueFile $QueueFile -QueueInfo $queueInfo -TargetDirectory $targetPath -Actor $Actor
        if (-not $SkipIndexUpdate) {
            Rebuild-TasksIndex -Quiet
        }
        if ($validationResult) {
            exit 0
        }
        else {
            exit 1
        }
    }

    if (-not $items -or $items.Count -eq 0) {
        if ($Id) {
            throw "В очереди $relativeQueuePath не найдено элементов (Id='$Id')."
        }
        throw "В очереди $relativeQueuePath отсутствуют элементы для обработки."
    }

    $timestamp = (Get-Date).ToString('yyyy-MM-ddTHH:mm:ssZ')
    $generated = @()
    $queueUpdated = $false
    $reservedIds = New-Object 'System.Collections.Generic.HashSet[string]'
    $activityEntries = New-Object System.Collections.Generic.List[object]

    foreach ($item in $items) {
        $taskId = [string](Get-ItemProperty -Item $item -Name 'id')
        $title = [string](Get-ItemProperty -Item $item -Name 'title')
        $ownerValue = Get-ItemProperty -Item $item -Name 'owner'
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
        }
        else {
            $payload = ConvertTo-Hashtable -InputObject $item
            $payload.PSObject.Properties.Remove('id') | Out-Null
            $payload.PSObject.Properties.Remove('title') | Out-Null

            $taskObject = [ordered]@{
                task     = [ordered]@{
                    id           = $taskId
                    title        = $title
                    queue_source = $relativeQueuePath
                    created_at   = $timestamp
                }
                metadata = [ordered]@{
                    generated_by = "pipeline/scripts/generate-tasks-from-queue.ps1"
                    generated_at = $timestamp
                }
                payload  = $payload
            }

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
        Set-ItemProperty -Item $item -Name 'title' -Value $title
        if ($ownerValue) {
            Set-ItemProperty -Item $item -Name 'owner' -Value $ownerValue
        }
        $nowQueueTimestamp = (Get-Date -Format 'yyyy-MM-dd HH:mm')
        Set-ItemProperty -Item $item -Name 'updated' -Value $nowQueueTimestamp
        $queueUpdated = $true

        $generated += $relativeTaskPath
        if ($assignedNewId) {
            Write-Output "Создан файл задачи: $relativeTaskPath (новый ID $taskId)"
        }
        else {
            Write-Output "Создан файл задачи: $relativeTaskPath"
        }

        $activityEntry = [ordered]@{
            date  = (Get-Date -Format 'yyyy-MM-dd HH:mm')
            actor = $Actor
            title = "Создана задача $taskId"
            queue = $relativeQueuePath
            task  = $relativeTaskPath
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
        Save-QueueObject -QueueInfo $queueInfo
    }

    if (-not $DryRun -and -not $DisableActivityLog) {
        foreach ($entry in $activityEntries) {
            Write-ActivityLogEntry -RepositoryRoot $repoRoot -Entry ([hashtable]$entry)
        }
    }

    if (-not $DryRun -and $generated.Count -eq 0) {
        Write-Warning "Ни один файл не был создан."
    }

    if (-not $DryRun -and -not $SkipIndexUpdate) {
        Rebuild-TasksIndex -Quiet
    }

    if ($ValidateSync) {
        $validationResult = Invoke-QueueSyncValidation -QueueFile $QueueFile -QueueInfo $queueInfo -TargetDirectory $targetPath -Actor $Actor
        if (-not $validationResult) {
            Write-Warning "Синхронизация очереди требует внимания. Запустите скрипт с параметром -ValidateSyncOnly для подробностей."
        }
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
    if ($QueueFile) { $parameters.QueueFile = $QueueFile }
    if ($TargetDirectory) { $parameters.TargetDirectory = $TargetDirectory }
    if ($TemplateFile) { $parameters.TemplateFile = $TemplateFile }
    if ($Prefix) { $parameters.Prefix = $Prefix }
    if ($Actor) { $parameters.Actor = $Actor }
    $parameters.DryRun = [bool]$DryRun
    $parameters.ValidateSync = [bool]$ValidateSync
    $parameters.ValidateSyncOnly = [bool]$ValidateSyncOnly

    if ($repoRoot -and (Test-Path -LiteralPath $repoRoot)) {
        try {
            if (Get-Command -Name Write-AutomationErrorLog -ErrorAction SilentlyContinue) {
                $logResult = Write-AutomationErrorLog -RepositoryRoot $repoRoot -ScriptName $scriptName -Message $message -Context $context -Parameters $parameters
                if ($logResult -and $logResult.Path) {
                    $logReference = $logResult.Path
                }
            }
        }
        catch {
            Write-Warning ("Не удалось записать ошибку в лог: {0}" -f $_.Exception.Message)
        }
    }

    Write-Error ("Скрипт {0} завершился с ошибкой: {1}" -f $scriptName, $message)
    if ($logReference) {
        Write-Error ("Подробности: {0}" -f $logReference)
    }
    exit 1
}

