param(
    [string]$KnowledgeRoot = "shared/docs/knowledge",
    [string]$SchemaFile = "shared/docs/knowledge/templates/knowledge-schema.yaml",
    [string]$Path,
    [switch]$NoAutoCommit,
    [string]$CommitMessage,
    [switch]$DisableActivityLog
)
$scriptName = Split-Path -Leaf $MyInvocation.MyCommand.Path
$repoRoot = $null
$logReference = $null

try {
    $ErrorActionPreference = "Stop"
    function Resolve-AbsolutePath {
        param([string]$BasePath,[string]$RelativePath)
        if ([System.IO.Path]::IsPathRooted($RelativePath)) {
            return (Resolve-Path $RelativePath).Path
        }
        return (Resolve-Path (Join-Path $BasePath $RelativePath)).Path
    }
    $scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
    $repoRoot = (Resolve-Path (Join-Path $scriptDir "..\..")).Path
    $knowledgePath = Resolve-AbsolutePath -BasePath $repoRoot -RelativePath $KnowledgeRoot
    $schemaPath = Resolve-AbsolutePath -BasePath $repoRoot -RelativePath $SchemaFile
    $modulePath = Join-Path -Path $scriptDir -ChildPath "modules/Pipeline.Automation.psm1"
    if (-not (Test-Path -LiteralPath $modulePath)) {
        throw "Не найден модуль автоматизации: $modulePath"
    }
    Import-Module -Name $modulePath -ErrorAction Stop
    if (-not (Test-Path $schemaPath)) {
        throw "Не найден файл схемы: $schemaPath"
    }
    $schemaRaw = Get-Content -Path $schemaPath -Raw
    if ([string]::IsNullOrWhiteSpace($schemaRaw)) {
        throw "Файл схемы пуст: $schemaPath"
    }
    $convertCommand = Get-Command -Name ConvertFrom-Yaml -ErrorAction SilentlyContinue
    if (-not $convertCommand) {
        throw "Команда ConvertFrom-Yaml недоступна."
    }
    $schema = ConvertFrom-Yaml -Yaml $schemaRaw
function Get-MemberValue {
    param($Object,[string]$PropertyName)
    if ($null -eq $Object) {
        return $null
    }
    if ($Object -is [System.Collections.IDictionary]) {
        if ($Object.Contains($PropertyName)) {
            return $Object[$PropertyName]
        }
        return $null
    }
    $property = $Object.PSObject.Properties[$PropertyName]
    if ($null -ne $property) {
        return $property.Value
    }
    return $null
}
function Select-Segments {
    param([string[]]$Segments)
    if ($Segments.Count -gt 1) {
        return $Segments[1..($Segments.Count - 1)]
    }
    return @()
}
function Test-YamlPath {
    param($Object,[string[]]$Segments)
    if ($Segments.Count -eq 0) {
        return [PSCustomObject]@{Success = $true; Value = $Object}
    }
    $segment = $Segments[0]
    $isArray = $segment.EndsWith("[]")
    $name = $segment
    if ($isArray) {
        $name = $segment.Substring(0,$segment.Length - 2)
    }
    $child = Get-MemberValue -Object $Object -PropertyName $name
    if ($null -eq $child) {
        return [PSCustomObject]@{Success = $false; Value = $null}
    }
    if ($isArray) {
        $collection = @()
        if ($child -is [System.Collections.IEnumerable] -and -not ($child -is [string])) {
            $collection = @($child)
        } else {
            return [PSCustomObject]@{Success = $false; Value = $null}
        }
        if ($collection.Count -eq 0) {
            return [PSCustomObject]@{Success = $false; Value = $null}
        }
        foreach ($item in $collection) {
            $result = Test-YamlPath -Object $item -Segments (Select-Segments -Segments $Segments)
            if (-not $result.Success) {
                return $result
            }
        }
        return [PSCustomObject]@{Success = $true; Value = $child}
    }
    $remaining = Select-Segments -Segments $Segments
    if ($remaining.Count -eq 0) {
        if ($child -is [string]) {
            if ([string]::IsNullOrWhiteSpace($child)) {
                return [PSCustomObject]@{Success = $false; Value = $null}
            }
        } elseif ($child -is [System.Collections.IEnumerable] -and -not ($child -is [string])) {
            if (@($child).Count -eq 0) {
                return [PSCustomObject]@{Success = $false; Value = $null}
            }
        } elseif ($null -eq $child) {
            return [PSCustomObject]@{Success = $false; Value = $null}
        }
        return [PSCustomObject]@{Success = $true; Value = $child}
    }
    return Test-YamlPath -Object $child -Segments $remaining
}
function Get-RawValue {
    param($Object,[string[]]$Segments)
    $current = $Object
    foreach ($segment in $Segments) {
        if ($segment.EndsWith("[]")) {
            $name = $segment.Substring(0,$segment.Length - 2)
            $current = Get-MemberValue -Object $current -PropertyName $name
            return $current
        }
        $current = Get-MemberValue -Object $current -PropertyName $segment
        if ($null -eq $current) {
            return $null
        }
    }
    return $current
}
$targetDirectories = @("canon","mechanics","content","implementation","analysis","archives")
$knowledgeFiles = @()
$commitPathSet = New-Object System.Collections.Generic.HashSet[string]([System.StringComparer]::OrdinalIgnoreCase)
$commitIds = New-Object System.Collections.Generic.HashSet[string]([System.StringComparer]::OrdinalIgnoreCase)
function Add-CommitPath {
    param([string]$ItemPath)
    if ([string]::IsNullOrWhiteSpace($ItemPath)) { return }
    if (-not (Test-Path -LiteralPath $ItemPath)) { return }
    $relative = [System.IO.Path]::GetRelativePath($repoRoot, (Resolve-Path -LiteralPath $ItemPath)) -replace '\\','/'
    $null = $commitPathSet.Add($relative)
}
if ($Path) {
    $targetFile = Resolve-AbsolutePath -BasePath $repoRoot -RelativePath $Path
    if (-not (Test-Path $targetFile)) {
        throw "Не найден указанный файл: $Path"
    }
    if ([System.IO.Path]::GetExtension($targetFile) -ne ".yaml") {
        throw "Поддерживаются только YAML-документы знаний."
    }
    $knowledgeFiles = @(Get-Item -Path $targetFile)
    Add-CommitPath -ItemPath $targetFile
} else {
    foreach ($dir in $targetDirectories) {
        $fullDir = Join-Path $knowledgePath $dir
        if (Test-Path $fullDir) {
            $knowledgeFiles += Get-ChildItem -Path $fullDir -Filter "*.yaml" -File -Recurse
        }
    }
}
$violations = @()
$queueUpdates = New-Object System.Collections.Generic.List[object]
foreach ($file in $knowledgeFiles) {
    $relative = [System.IO.Path]::GetRelativePath($knowledgePath,$file.FullName)
    $yamlRaw = Get-Content -Path $file.FullName -Raw
    if ([string]::IsNullOrWhiteSpace($yamlRaw)) {
        $violations += "Файл '$relative' пуст."
        continue
    }
    try {
        $document = ConvertFrom-Yaml -Yaml $yamlRaw
    } catch {
        $violations += "Файл '$relative' содержит синтаксическую ошибку: $($_.Exception.Message)"
        continue
    }
    if ($null -eq $document) {
        $violations += "Файл '$relative' не удалось разобрать."
        continue
    }
    foreach ($requiredPath in @($schema.required_fields)) {
        $result = Test-YamlPath -Object $document -Segments ($requiredPath -split "\.")
        if (-not $result.Success) {
            $violations += "Файл '$relative' отсутствует поле '$requiredPath'."
        }
    }
    if ($schema.validation_rules.document_type.allowed) {
        $docType = Get-RawValue -Object $document -Segments @("metadata","document_type")
        if ([string]::IsNullOrWhiteSpace($docType) -or ($schema.validation_rules.document_type.allowed -notcontains $docType)) {
            $violations += "Файл '$relative' значение metadata.document_type не входит в список допустимых."
        }
    }
    if ($schema.validation_rules.status.allowed) {
        $statusValue = Get-RawValue -Object $document -Segments @("metadata","status")
        if ([string]::IsNullOrWhiteSpace($statusValue) -or ($schema.validation_rules.status.allowed -notcontains $statusValue)) {
            $violations += "Файл '$relative' значение metadata.status не входит в список допустимых."
        }
    }
    $owners = Get-RawValue -Object $document -Segments @("metadata","owners")
    if ($owners -is [System.Collections.IEnumerable] -and -not ($owners -is [string])) {
        foreach ($owner in $owners) {
            $ownerRole = Get-MemberValue -Object $owner -PropertyName "role"
            $ownerContact = Get-MemberValue -Object $owner -PropertyName "contact"
            if ([string]::IsNullOrWhiteSpace($ownerRole) -or [string]::IsNullOrWhiteSpace($ownerContact)) {
                $violations += "Файл '$relative' содержит владельца без role или contact."
                break
            }
        }
    }
    $lastUpdated = Get-RawValue -Object $document -Segments @("metadata","last_updated")
    if (-not [string]::IsNullOrWhiteSpace($lastUpdated)) {
        $parsed = [datetime]::MinValue
        if (-not [datetime]::TryParse($lastUpdated,[ref]$parsed)) {
            $violations += "Файл '$relative' имеет некорректное значение metadata.last_updated."
        }
    }
    $reviewedValue = Get-RawValue -Object $document -Segments @("metadata","concept_reviewed_at")
    if (-not [string]::IsNullOrWhiteSpace($reviewedValue)) {
        $parsedReview = [datetime]::MinValue
        if (-not [datetime]::TryParse($reviewedValue,[ref]$parsedReview)) {
            $violations += "Файл '$relative' имеет некорректное значение metadata.concept_reviewed_at."
        }
    }
    $docType = Get-RawValue -Object $document -Segments @("metadata","document_type")
    if (-not [string]::IsNullOrWhiteSpace($docType)) {
        $typeRules = $schema.type_specific_requirements.$docType
        if ($null -ne $typeRules -and $typeRules.required_fields) {
            foreach ($typeRequiredField in $typeRules.required_fields) {
                $result = Test-YamlPath -Object $document -Segments ($typeRequiredField -split "\.")
                if (-not $result.Success) {
                    $violations += "Файл '$relative' не удовлетворяет типовым требованиям: отсутствует '$typeRequiredField'."
                }
            }
        }
    }
    if ($docType -eq "implementation" -or $docType -eq "content" -or $docType -eq "mechanics") {
        $queueReference = Get-RawValue -Object $document -Segments @("implementation","queue_reference")
        if ($queueReference -isnot [System.Collections.IEnumerable]) {
            $violations += "Файл '$relative' имеет неверный формат implementation.queue_reference, ожидается массив путей."
        } elseif (@($queueReference).Count -eq 0) {
            $violations += "Файл '$relative' содержит пустой implementation.queue_reference."
        } else {
            $index = 0
            foreach ($referenceEntry in @($queueReference)) {
                if ([string]::IsNullOrWhiteSpace([string]$referenceEntry)) {
                    $violations += "Файл '$relative' содержит пустое значение implementation.queue_reference[$index]."
                } elseif (Get-RawValue -Object $document -Segments @("implementation","needs_task")) {
                    $queueUpdates.Add([ordered]@{
                        RelativePath = $relative
                        Reference    = [string]$referenceEntry
                        Document     = $document
                    })
                }
                $index++
            }
        }
        $needsTask = Get-RawValue -Object $document -Segments @("implementation","needs_task")
        if ($needsTask -eq $null) {
            $violations += "Файл '$relative' отсутствует поле implementation.needs_task."
        }
    }
    $historyEntries = Get-RawValue -Object $document -Segments @("history")
    if ($historyEntries -is [System.Collections.IEnumerable] -and -not ($historyEntries -is [string])) {
        foreach ($entry in $historyEntries) {
            $fields = @("version","date","author","changes")
            foreach ($field in $fields) {
                $value = Get-MemberValue -Object $entry -PropertyName $field
                if ([string]::IsNullOrWhiteSpace($value)) {
                    $violations += "Файл '$relative' содержит запись history без поля '$field'."
                    break
                }
            }
        }
    }
    $docId = Get-RawValue -Object $document -Segments @("metadata","id")
    if (-not [string]::IsNullOrWhiteSpace($docId)) {
        $null = $commitIds.Add($docId)
    }
}
if ($violations.Count -gt 0) {
    Write-Host "Обнаружены нарушения структуры знаний:" -ForegroundColor Red
    foreach ($issue in $violations) {
        Write-Host " - $issue" -ForegroundColor Red
    }
    exit 1
}

Write-Host "Структура знаний соответствует схеме." -ForegroundColor Green

if ($queueUpdates.Count -gt 0) {
    foreach ($update in $queueUpdates) {
        $reference = Resolve-QueueReference -Reference $update.Reference
        $queueInfo = Get-QueueObject -QueueFile $reference.Path -RepositoryRoot $repoRoot
        $document = $update.Document

        $docId = Get-RawValue -Object $document -Segments @("metadata","id")
        $docTitle = Get-RawValue -Object $document -Segments @("metadata","title")

        $ownerCandidate = $document.metadata.owners | Select-Object -First 1
        $ownerValue = $null
        if ($ownerCandidate) {
            $contactValue = [string](Get-MemberValue -Object $ownerCandidate -PropertyName 'contact')
            $roleValue = [string](Get-MemberValue -Object $ownerCandidate -PropertyName 'role')
            if (-not [string]::IsNullOrWhiteSpace($contactValue)) {
                $ownerValue = $contactValue
            } elseif (-not [string]::IsNullOrWhiteSpace($roleValue)) {
                $ownerValue = $roleValue
            }
        }

        $queueId = if ($reference.Id) { $reference.Id } else { $docId }
        if ([string]::IsNullOrWhiteSpace($queueId)) {
            throw "Не удалось определить идентификатор для очереди '$($reference.Path)' из документа '$($update.RelativePath)'. Добавь '#<id>' к ссылке или укажи metadata.id."
        }

        $sourceDocument = Join-Path -Path $KnowledgeRoot -ChildPath $update.RelativePath
        $sourceDocument = $sourceDocument -replace '\\','/'

        $extraFields = @{}
        if ($document.summary) {
            $extraFields.context = "concept"
        }

        Set-QueueItem -QueueInfo $queueInfo -Id $queueId -Title $docTitle -Owner $ownerValue -SourceDocument $sourceDocument -AdditionalFields $extraFields
        Save-QueueObject -QueueInfo $queueInfo
        Add-CommitPath -ItemPath $queueInfo.ResolvedPath

        if (-not $DisableActivityLog) {
            Write-ActivityLogEntry -RepositoryRoot $repoRoot -Entry @{
                actor    = "concept-automation"
                title    = "Обновлена очередь $($reference.Path)"
                queue    = $reference.Path
                document = $sourceDocument
                item_id  = $queueId
            }
            Add-CommitPath -ItemPath (Join-Path $repoRoot 'shared/trackers/activity-log.yaml')
        }
    }
}

$shouldAutoCommit = ($Path -and -not $NoAutoCommit)
if ($shouldAutoCommit -and $commitPathSet.Count -gt 0) {
    $pathsForCommit = $commitPathSet | ForEach-Object { $_ }
    $defaultMessage = if ($commitIds.Count -gt 0) {
        $idsForMessage = $commitIds | ForEach-Object { $_ }
        "chore(knowledge): validate {0}" -f ($idsForMessage -join ', ')
    } else {
        "chore(knowledge): validate entry"
    }
    $messageToUse = if ($CommitMessage) { $CommitMessage } else { $defaultMessage }
    Invoke-AutoCommit -RepositoryRoot $repoRoot -Paths $pathsForCommit -Message $messageToUse | Out-Null
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
    if ($Path) { $parameters.Path = $Path }
    if ($KnowledgeRoot) { $parameters.KnowledgeRoot = $KnowledgeRoot }
    if ($SchemaFile) { $parameters.SchemaFile = $SchemaFile }
    $parameters.DisableActivityLog = [bool]$DisableActivityLog

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


