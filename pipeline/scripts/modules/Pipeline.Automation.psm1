function Resolve-RepoPath {
    param(
        [Parameter(Mandatory = $true)]
        [string]$Path,
        [Parameter(Mandatory = $true)]
        [string]$RepositoryRoot
    )

    if ([System.IO.Path]::IsPathRooted($Path)) {
        return (Resolve-Path -LiteralPath $Path).Path
    }

    return (Join-Path -Path $RepositoryRoot -ChildPath $Path)
}

function Get-RelativeRepoPath {
    param(
        [Parameter(Mandatory = $true)]
        [string]$RepositoryRoot,
        [Parameter(Mandatory = $true)]
        [string]$Path
    )

    $absolutePath = Resolve-RepoPath -Path $Path -RepositoryRoot $RepositoryRoot
    $relative = [System.IO.Path]::GetRelativePath($RepositoryRoot, $absolutePath)
    return ($relative -replace '\\','/')
}

function Invoke-AutoCommit {
    param(
        [Parameter(Mandatory = $true)]
        [string]$RepositoryRoot,
        [Parameter(Mandatory = $true)]
        [string[]]$Paths,
        [string]$Message,
        [switch]$AllowEmpty
    )

    $normalized = @()
    foreach ($path in $Paths) {
        if ([string]::IsNullOrWhiteSpace($path)) { continue }
        $normalized += ($path -replace '\\','/')
    }
    $unique = $normalized | Select-Object -Unique
    if ($unique.Count -eq 0) {
        return $false
    }

    foreach ($relative in $unique) {
        $addArgs = @('-C', $RepositoryRoot, 'add', '-A', '--', $relative)
        git @addArgs | Out-Null
        if ($LASTEXITCODE -ne 0) {
            throw "Не удалось подготовить файл к коммиту: $relative"
        }
    }

    git -C $RepositoryRoot diff --cached --quiet
    $diffExit = $LASTEXITCODE
    if ($diffExit -eq 0 -and -not $AllowEmpty) {
        Write-Verbose "Изменения для коммита отсутствуют."
        return $false
    }

    if ([string]::IsNullOrWhiteSpace($Message)) {
        $Message = "chore: automated update"
    }

    git -C $RepositoryRoot commit -m $Message | Out-Null
    if ($LASTEXITCODE -ne 0) {
        throw "git commit завершился с ошибкой."
    }
    Write-Output "Создан коммит: $Message"
    return $true
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

function ConvertFrom-YamlSafe {
    param(
        [Parameter(Mandatory = $true)]
        [string]$Yaml
    )

    if (-not (Get-Command -Name ConvertFrom-Yaml -ErrorAction SilentlyContinue)) {
        throw "Команда ConvertFrom-Yaml недоступна. Установи PowerShell 7+ или модуль powershell-yaml."
    }
    return ConvertFrom-Yaml -Yaml $Yaml
}

function Get-YamlFile {
    param(
        [Parameter(Mandatory = $true)]
        [string]$Path
    )

    if (-not (Test-Path -LiteralPath $Path)) {
        throw "Файл не найден: $Path"
    }

    $raw = Get-Content -LiteralPath $Path -Raw
    if ([string]::IsNullOrWhiteSpace($raw)) {
        return $null
    }
    return ConvertFrom-YamlSafe -Yaml $raw
}

function Write-YamlFile {
    param(
        [Parameter(Mandatory = $true)]
        $Data,
        [Parameter(Mandatory = $true)]
        [string]$Path
    )

    $yaml = ConvertTo-YamlSafe -Data $Data
    $directory = Split-Path -Parent $Path
    if ($directory -and -not (Test-Path -LiteralPath $directory)) {
        New-Item -ItemType Directory -Path $directory | Out-Null
    }

    $encoding = New-Object System.Text.UTF8Encoding($false)
    $fileStream = [System.IO.File]::Open($Path, [System.IO.FileMode]::Create, [System.IO.FileAccess]::Write, [System.IO.FileShare]::ReadWrite)
    try {
        $writer = New-Object System.IO.StreamWriter($fileStream, $encoding)
        try {
            if ($yaml -and $yaml.EndsWith("`n")) {
                $writer.Write($yaml)
            } else {
                $writer.WriteLine($yaml)
            }
        } finally {
            $writer.Dispose()
        }
    } finally {
        $fileStream.Dispose()
    }
}

function Write-AutomationErrorLog {
    param(
        [Parameter(Mandatory = $true)]
        [string]$RepositoryRoot,
        [Parameter(Mandatory = $true)]
        [string]$ScriptName,
        [Parameter(Mandatory = $true)]
        [string]$Message,
        [hashtable]$Context,
        [hashtable]$Parameters
    )

    $logDirectory = Join-Path -Path $RepositoryRoot -ChildPath 'shared/trackers/logs'
    if (-not (Test-Path -LiteralPath $logDirectory)) {
        New-Item -ItemType Directory -Path $logDirectory | Out-Null
    }

    $logPath = Join-Path -Path $logDirectory -ChildPath 'automation-errors.yaml'
    $logData = $null
    if (Test-Path -LiteralPath $logPath) {
        $raw = Get-Content -LiteralPath $logPath -Raw
        if (-not [string]::IsNullOrWhiteSpace($raw)) {
            try {
                $logData = ConvertFrom-YamlSafe -Yaml $raw
            } catch {
                $logData = $null
            }
        }
    }
    if (-not $logData) {
        $logData = [ordered]@{ entries = @() }
    }
    if (-not $logData.entries) {
        $logData.entries = @()
    }

    $entry = [ordered]@{
        timestamp = (Get-Date -Format 'yyyy-MM-dd HH:mm:ss')
        script    = $ScriptName
        message   = $Message
    }
    if ($Parameters -and $Parameters.Count -gt 0) {
        $entry.parameters = $Parameters
    }
    if ($Context -and $Context.Count -gt 0) {
        $entry.context = $Context
    }

    $logData.entries += $entry

    $maxAttempts = 3
    for ($attempt = 1; $attempt -le $maxAttempts; $attempt++) {
        try {
            Write-YamlFile -Data $logData -Path $logPath
            break
        } catch [System.IO.IOException] {
            if ($attempt -eq $maxAttempts) {
                throw
            }
            Start-Sleep -Milliseconds 200
        }
    }

    return [pscustomobject]@{
        Path = Get-RelativeRepoPath -RepositoryRoot $RepositoryRoot -Path $logPath
        Entry = $entry
    }
}

function Invoke-ArtifactValidation {
    param(
        [Parameter(Mandatory = $true)]
        [string]$RepositoryRoot,
        [Parameter(Mandatory = $true)]
        [string[]]$Artifacts
    )

    $scriptDirectory = Join-Path -Path $RepositoryRoot -ChildPath 'pipeline/scripts'
    $unique = New-Object System.Collections.Generic.HashSet[string]([System.StringComparer]::OrdinalIgnoreCase)

    foreach ($artifact in $Artifacts) {
        if ([string]::IsNullOrWhiteSpace($artifact)) { continue }
        $relative = Get-RelativeRepoPath -RepositoryRoot $RepositoryRoot -Path $artifact
        if (-not $unique.Add($relative)) { continue }
        $resolved = Resolve-RepoPath -Path $relative -RepositoryRoot $RepositoryRoot
        if (-not (Test-Path -LiteralPath $resolved)) {
            throw "Артефакт не найден: $relative"
        }

        if ($relative -like 'shared/docs/knowledge/*') {
            $validator = Join-Path -Path $scriptDirectory -ChildPath 'check-knowledge-schema.ps1'
            if (-not (Test-Path -LiteralPath $validator)) {
                throw "Не найден валидатор знаний: $validator"
            }
            try {
                & pwsh -NoLogo -File $validator -Path $relative -NoAutoCommit -DisableActivityLog | Out-Null
            } catch {
                $message = $_.Exception.Message
                if ([string]::IsNullOrWhiteSpace($message) -and $_.Exception.InnerException) {
                    $message = $_.Exception.InnerException.Message
                }
                throw "Валидация знания завершилась ошибкой для ${relative}: $message"
            }
        }
    }
}

function Resolve-QueueReference {
    param(
        [Parameter(Mandatory = $true)]
        [string]$Reference
    )

    if ([string]::IsNullOrWhiteSpace($Reference)) {
        throw "Очередь не указана."
    }

    $parts = $Reference.Split('#', 2, [System.StringSplitOptions]::None)
    $path = $parts[0]
    $id = $null
    if ($parts.Count -gt 1 -and -not [string]::IsNullOrWhiteSpace($parts[1])) {
        $id = $parts[1]
    }

    return [pscustomobject]@{
        Path = $path
        Id   = $id
    }
}

function Get-QueueObject {
    param(
        [Parameter(Mandatory = $true)]
        [string]$QueueFile,
        [Parameter(Mandatory = $true)]
        [string]$RepositoryRoot
    )

    $resolvedPath = Resolve-RepoPath -Path $QueueFile -RepositoryRoot $RepositoryRoot
    if (-not (Test-Path -LiteralPath $resolvedPath)) {
        throw "Файл очереди не найден: $QueueFile"
    }

    $raw = Get-Content -LiteralPath $resolvedPath -Raw
    $queueYaml = if ([string]::IsNullOrWhiteSpace($raw)) { $null } else { ConvertFrom-YamlSafe -Yaml $raw }
    if ($queueYaml -and $queueYaml.PSObject.Properties.Name -contains 'queue') {
        $queueYaml = $queueYaml.queue
    }
    if (-not $queueYaml) {
        $queueYaml = [ordered]@{}
    }

    $statusValue = $null
    if ($queueYaml -is [System.Collections.IDictionary]) {
        if ($queueYaml.Contains('status')) { $statusValue = $queueYaml['status'] }
    } else {
        $statusValue = $queueYaml.status
    }
    $status = if ($statusValue) { [string]$statusValue } else { 'queued' }

    $itemsSource = @()
    if ($queueYaml -is [System.Collections.IDictionary]) {
        if ($queueYaml.Contains('items')) { $itemsSource = @($queueYaml['items']) }
    } elseif ($queueYaml.PSObject.Properties['items']) {
        $itemsSource = @($queueYaml.items)
    }

    $itemList = New-Object System.Collections.ArrayList
    foreach ($item in $itemsSource) {
        $orderedItem = [ordered]@{}
        if ($item) {
            if ($item -is [System.Collections.IDictionary]) {
                foreach ($key in $item.Keys) { $orderedItem[$key] = $item[$key] }
            } else {
                foreach ($prop in $item.PSObject.Properties) { $orderedItem[$prop.Name] = $prop.Value }
            }
        }
        $null = $itemList.Add([pscustomobject]$orderedItem)
    }

    $metadata = [ordered]@{}
    if ($queueYaml -is [System.Collections.IDictionary]) {
        foreach ($key in $queueYaml.Keys) {
            if (@('status','last_updated','items') -contains $key) { continue }
            $metadata[$key] = $queueYaml[$key]
        }
    } else {
        foreach ($prop in $queueYaml.PSObject.Properties) {
            if (@('status','last_updated','items') -contains $prop.Name) { continue }
            $metadata[$prop.Name] = $prop.Value
        }
    }

    return [pscustomobject]@{
        ResolvedPath = $resolvedPath
        Status       = $status
        Items        = $itemList
        Metadata     = $metadata
    }
}

function Save-QueueObject {
    param(
        [Parameter(Mandatory = $true)]
        $QueueInfo
    )

    $timestamp = (Get-Date -Format 'yyyy-MM-dd HH:mm')
    $queueData = [ordered]@{
        status       = if ($QueueInfo.Status) { $QueueInfo.Status } else { 'queued' }
        last_updated = $timestamp
        items        = @($QueueInfo.Items)
    }

    foreach ($key in $QueueInfo.Metadata.Keys) {
        $queueData[$key] = $QueueInfo.Metadata[$key]
    }

    Write-YamlFile -Data $queueData -Path $QueueInfo.ResolvedPath
    Write-Output "Обновлён файл очереди: $($QueueInfo.ResolvedPath)"
}

function Set-QueueItem {
    param(
        [Parameter(Mandatory = $true)]
        $QueueInfo,
        [Parameter(Mandatory = $true)]
        [string]$Id,
        [Parameter(Mandatory = $true)]
        [string]$Title,
        [string]$Owner,
        [string]$SourceDocument,
        [hashtable]$AdditionalFields
    )

    $items = $QueueInfo.Items
    if (-not ($items -is [System.Collections.IList])) {
        $list = New-Object System.Collections.ArrayList
        foreach ($item in @($items)) {
            $null = $list.Add($item)
        }
        $QueueInfo.Items = $list
        $items = $QueueInfo.Items
    }

    $existing = @($items | Where-Object { $_.id -eq $Id })
    $timestamp = (Get-Date -Format 'yyyy-MM-dd HH:mm')

    if ($existing.Count -gt 0) {
        $entry = $existing[0]
        $entry | Add-Member -NotePropertyName title -NotePropertyValue $Title -Force
        if ($Owner) { $entry | Add-Member -NotePropertyName owner -NotePropertyValue $Owner -Force }
        if ($SourceDocument) { $entry | Add-Member -NotePropertyName source_document -NotePropertyValue $SourceDocument -Force }
        $entry | Add-Member -NotePropertyName updated -NotePropertyValue $timestamp -Force
        if ($AdditionalFields) {
            foreach ($key in $AdditionalFields.Keys) {
                $entry | Add-Member -NotePropertyName $key -NotePropertyValue $AdditionalFields[$key] -Force
            }
        }
    } else {
        $orderedNewItem = [ordered]@{
            id      = $Id
            title   = $Title
            updated = $timestamp
        }
        if ($Owner) { $orderedNewItem.owner = $Owner }
        if ($SourceDocument) { $orderedNewItem.source_document = $SourceDocument }
        if ($AdditionalFields) {
            foreach ($key in $AdditionalFields.Keys) {
                $orderedNewItem[$key] = $AdditionalFields[$key]
            }
        }
        $null = $items.Add([pscustomobject]$orderedNewItem)
    }
}

function Write-ActivityLogEntry {
    param(
        [Parameter(Mandatory = $true)]
        [string]$RepositoryRoot,
        [Parameter(Mandatory = $true)]
        [hashtable]$Entry
    )

    $logPath = Join-Path -Path $RepositoryRoot -ChildPath 'shared/trackers/activity-log.yaml'
    $logObject = $null

    if (Test-Path -LiteralPath $logPath) {
        $raw = Get-Content -LiteralPath $logPath -Raw
        if (-not [string]::IsNullOrWhiteSpace($raw)) {
            try {
                $logObject = ConvertFrom-YamlSafe -Yaml $raw
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
    }

    $Entry['date'] = (Get-Date -Format 'yyyy-MM-dd HH:mm')
    $logObject.entries += $Entry

    $header = "# Журнал активностей`n"

    $maxAttempts = 3
    for ($attempt = 1; $attempt -le $maxAttempts; $attempt++) {
        try {
            $yamlBody = ConvertTo-YamlSafe -Data $logObject
            $content = $header + $yamlBody
            $encoding = New-Object System.Text.UTF8Encoding($false)
            $fileStream = [System.IO.File]::Open($logPath, [System.IO.FileMode]::Create, [System.IO.FileAccess]::Write, [System.IO.FileShare]::ReadWrite)
            try {
                $writer = New-Object System.IO.StreamWriter($fileStream, $encoding)
                try {
                    $writer.Write($content)
                } finally {
                    $writer.Dispose()
                }
            } finally {
                $fileStream.Dispose()
            }
            break
        } catch [System.IO.IOException] {
            if ($attempt -eq $maxAttempts) {
                throw
            }
            Start-Sleep -Milliseconds 200
        }
    }
    Write-Output "Обновлён Activity Log: $logPath"
}

Export-ModuleMember -Function Resolve-RepoPath, ConvertTo-YamlSafe, ConvertFrom-YamlSafe, `
    Get-YamlFile, Write-YamlFile, Resolve-QueueReference, Get-QueueObject, Save-QueueObject, `
    Set-QueueItem, Write-ActivityLogEntry, Get-RelativeRepoPath, Invoke-AutoCommit, Invoke-ArtifactValidation, `
    Write-AutomationErrorLog

