param(
    [string]$KnowledgeRoot = "shared/docs/knowledge",
    [string]$SchemaFile = "shared/docs/knowledge/templates/knowledge-schema.yaml"
)
$ErrorActionPreference = "Stop"
function Resolve-AbsolutePath {
    param([string]$BasePath,[string]$RelativePath)
    if ([System.IO.Path]::IsPathRooted($RelativePath)) {
        return (Resolve-Path $RelativePath).Path
    }
    return (Resolve-Path (Join-Path $BasePath $RelativePath)).Path
}
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$repoRoot = Split-Path -Parent $scriptDir
$knowledgePath = Resolve-AbsolutePath -BasePath $repoRoot -RelativePath $KnowledgeRoot
$schemaPath = Resolve-AbsolutePath -BasePath $repoRoot -RelativePath $SchemaFile
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
$targetDirectories = @("canon","mechanics","content","implementation-guides","analysis","archives")
$knowledgeFiles = @()
foreach ($dir in $targetDirectories) {
    $fullDir = Join-Path $knowledgePath $dir
    if (Test-Path $fullDir) {
        $knowledgeFiles += Get-ChildItem -Path $fullDir -Filter "*.yaml" -File -Recurse
    }
}
$violations = @()
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
            foreach ($path in $typeRules.required_fields) {
                $result = Test-YamlPath -Object $document -Segments ($path -split "\.")
                if (-not $result.Success) {
                    $violations += "Файл '$relative' не удовлетворяет типовым требованиям: отсутствует '$path'."
                }
            }
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
}
if ($violations.Count -gt 0) {
    Write-Host "Обнаружены нарушения структуры знаний:" -ForegroundColor Red
    foreach ($issue in $violations) {
        Write-Host " - $issue" -ForegroundColor Red
    }
    exit 1
}
Write-Host "Структура знаний соответствует схеме." -ForegroundColor Green

