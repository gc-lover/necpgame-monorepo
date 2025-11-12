<#
.SYNOPSIS
  Формирует краткий отчёт об изменениях YAML-документа знаний между коммитами.

.DESCRIPTION
  Загружает текущую версию файла и версию из `git show <since>` (по умолчанию предыдущий коммит),
  сравнивает ключи и выводит summary в формате YAML/Markdown.

.EXAMPLE
  pwsh -File pipeline/scripts/knowledge-diff.ps1 -File shared/docs/knowledge/mechanics/quests/quest-system.yaml

.PARAMETER File
  Относительный путь к документу знаний.

.PARAMETER Since
  Git-ревизия для сравнения (по умолчанию `HEAD~1`).

.PARAMETER Output
  Путь для сохранения отчёта (если не указан — вывод в stdout).
#>

[CmdletBinding()]
param(
    [Parameter(Mandatory = $true)]
    [string]$File,

    [string]$Since = 'HEAD~1',

    [string]$Output
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

function Load-YamlFromString {
    param([string]$Content)
    if ([string]::IsNullOrWhiteSpace($Content)) { return $null }
    try {
        return ConvertFrom-Yaml -Yaml $Content
    } catch {
        Write-Warning "Не удалось разобрать YAML: $($_.Exception.Message)"
        return $null
    }
}

function ConvertTo-YamlSafe {
    param(
        [Parameter(Mandatory = $true)]
        $Data,
        [int]$Depth = 6
    )

    $convertCmd = Get-Command -Name ConvertTo-Yaml -ErrorAction Stop
    if ($convertCmd.Parameters.ContainsKey('Depth')) {
        return ConvertTo-Yaml -Data $Data -Depth $Depth
    }
    return ConvertTo-Yaml -Data $Data
}

function Flatten-Object {
    param(
        $Object,
        [string]$Prefix = ''
    )

    $result = @{}

    if ($Object -is [System.Collections.IDictionary]) {
        foreach ($key in $Object.Keys) {
            $value = $Object[$key]
            $nextPrefix = if ($Prefix) { "$Prefix.$key" } else { [string]$key }
            $result += Flatten-Object -Object $value -Prefix $nextPrefix
        }
    } elseif ($Object -is [System.Collections.IEnumerable] -and $Object -isnot [string]) {
        $index = 0
        foreach ($value in $Object) {
            $nextPrefix = if ($Prefix) { "$Prefix[$index]" } else { "[$index]" }
            $result += Flatten-Object -Object $value -Prefix $nextPrefix
            $index++
        }
    } else {
        $result[$Prefix] = $Object
    }

    return $result
}

function Get-GitContent {
    param(
        [string]$Revision,
        [string]$Path
    )
    $gitArgs = @('show', "${Revision}:${Path}")
    $psi = New-Object System.Diagnostics.ProcessStartInfo 'git', ($gitArgs -join ' ')
    $psi.WorkingDirectory = $repoRoot
    $psi.RedirectStandardOutput = $true
    $psi.RedirectStandardError = $true
    $psi.UseShellExecute = $false
    $process = [System.Diagnostics.Process]::Start($psi)
    $stdout = $process.StandardOutput.ReadToEnd()
    $stderr = $process.StandardError.ReadToEnd()
    $process.WaitForExit()
    if ($process.ExitCode -ne 0) {
        throw "git show ${Revision}:${Path} завершился с ошибкой: $stderr"
    }
    return $stdout
}

$relativePath = $File
$currentPath = Resolve-RepoPath -Path $relativePath
if (-not (Test-Path -LiteralPath $currentPath)) {
    throw "Файл не найден: $File"
}

$currentContent = Get-Content -LiteralPath $currentPath -Raw
$currentYaml = Load-YamlFromString -Content $currentContent
if (-not $currentYaml) {
    throw "Не удалось разобрать текущую версию файла"
}

try {
    $previousContent = Get-GitContent -Revision $Since -Path $relativePath
} catch {
    Write-Warning ("Не удалось получить предыдущую версию ({0}:{1}): {2}" -f $Since, $relativePath, $_.Exception.Message)
    $previousContent = $null
}
$previousYaml = $null
if ($previousContent) {
    $previousYaml = Load-YamlFromString -Content $previousContent
}
if (-not $previousYaml) {
    Write-Warning "Предыдущая версия отсутствует или не разобрана — выводим только текущую структуру"
    $summary = [ordered]@{
        file = $relativePath
        compared_with = $Since
        generated_at = (Get-Date).ToString('yyyy-MM-ddTHH:mm:ssZ')
        note = 'Предыдущая версия отсутствует; сравнение не выполнено.'
    }
    $outputText = if ($Format -eq 'yaml') { ConvertTo-YamlSafe -Data $summary -Depth 4 } else { \"# Knowledge Diff`n- file: $relativePath`n- compared_with: $Since`n- note: предыдущая версия отсутствует\" }
    if ($Output) {
        $outputPath = Resolve-RepoPath -Path $Output
        Set-Content -LiteralPath $outputPath -Value $outputText -Encoding UTF8
        Write-Output \"Diff сохранён: $Output\"
    } else {
        Write-Output $outputText
    }
    exit 0
}

$currentFlat = Flatten-Object -Object $currentYaml
$previousFlat = Flatten-Object -Object $previousYaml

$added = @()
$removed = @()
$updated = @()

foreach ($key in $currentFlat.Keys) {
    if (-not $previousFlat.ContainsKey($key)) {
        $added += [ordered]@{ key = $key; value = $currentFlat[$key] }
    } elseif ($previousFlat[$key] -ne $currentFlat[$key]) {
        $updated += [ordered]@{ key = $key; old = $previousFlat[$key]; new = $currentFlat[$key] }
    }
}

foreach ($key in $previousFlat.Keys) {
    if (-not $currentFlat.ContainsKey($key)) {
        $removed += [ordered]@{ key = $key; value = $previousFlat[$key] }
    }
}

$summary = [ordered]@{
    file = $relativePath
    compared_with = $Since
    generated_at = (Get-Date).ToString('yyyy-MM-ddTHH:mm:ssZ')
    stats = [ordered]@{
        added = $added.Count
        removed = $removed.Count
        updated = $updated.Count
    }
    changes = [ordered]@{
        added = $added
        removed = $removed
        updated = $updated
    }
}

$outputText = ConvertTo-YamlSafe -Data $summary -Depth 6

if ($Output) {
    $outputPath = Resolve-RepoPath -Path $Output
    Set-Content -LiteralPath $outputPath -Value $outputText -Encoding UTF8
    Write-Output "Diff сохранён: $Output"
} else {
    Write-Output $outputText
}
