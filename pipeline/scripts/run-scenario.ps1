<#
.SYNOPSIS
  Исполняет сценарий агента из файла `pipeline/agents/<role>.yaml`.

.DESCRIPTION
  Находит раздел `workflow` в YAML, выбирает указанный шаг (или все шаги по порядку) и
  последовательно выполняет команды из блока `scripts`. Опционально подставляет значения
  в плейсхолдеры `<placeholder>` из переданного hashtable. Опциональные фрагменты,
  заключённые в квадратные скобки, удаляются перед выполнением.

.EXAMPLE
  pwsh -File pipeline/scripts/run-scenario.ps1 -Role openapi-executor -Step claim-task

.EXAMPLE
  pwsh -File pipeline/scripts/run-scenario.ps1 -Role backend-implementer -Variables @{ "task_file" = "pipeline/tasks/06_backend_implementer/BE-042.yaml" }

.PARAMETER Role
  Идентификатор агента (например, `openapi-executor`, `backend-implementer`).

.PARAMETER Step
  Опциональный идентификатор шага (`claim-task`, `finalize`). Если не указан, выполняются все шаги.

.PARAMETER Variables
  Hashtable со значениями для плейсхолдеров `<placeholder>`.

.PARAMETER DryRun
  Показывает команды, но не исполняет их.

.PARAMETER NoPrompt
  Не запрашивает подтверждения перед выполнением шагов.

.PARAMETER OutputTranscript
  Путь к файлу для логирования результатов (необязательный).
#>

[CmdletBinding()]
param(
    [Parameter(Mandatory = $true)]
    [string]$Role,

    [string]$Step,

    [hashtable]$Variables,

    [switch]$DryRun,

    [switch]$NoPrompt,

    [string]$OutputTranscript
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

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

function Normalize-RoleKey {
    param([string]$Value)
    if ([string]::IsNullOrWhiteSpace($Value)) { return $null }
    $normalized = $Value.Trim().ToLowerInvariant()
    $normalized = $normalized -replace '\s+', '-'
    $normalized
}

$roleKey = Normalize-RoleKey -Value $Role
if (-not $roleKey) {
    throw "Не удалось определить role key для значения: $Role"
}

$agentFile = Resolve-RepoPath -Path (Join-Path 'pipeline/agents' ($roleKey + '.yaml'))
if (-not (Test-Path -LiteralPath $agentFile)) {
    throw "Файл агента не найден: $agentFile"
}

$agentRaw = Get-Content -LiteralPath $agentFile -Raw
$agentYaml = ConvertFrom-Yaml -Yaml $agentRaw
if (-not $agentYaml) {
    throw "Не удалось разобрать YAML агента: $agentFile"
}

$workflow = $agentYaml.workflow
if (-not $workflow) {
    throw "В файле агента $roleKey отсутствует раздел workflow"
}

$selectedSteps = @()
foreach ($entry in $workflow) {
    $stepId = if ($entry -is [System.Collections.IDictionary]) { $entry['step'] } else { $entry.step }
    if ([string]::IsNullOrWhiteSpace($stepId)) { continue }
    if ($Step) {
        if ((Normalize-RoleKey -Value $stepId) -eq (Normalize-RoleKey -Value $Step)) {
            $selectedSteps = @($entry)
            break
        }
    } else {
        $selectedSteps += $entry
    }
}

if ($selectedSteps.Count -eq 0) {
    if ($Step) {
        throw "В сценарии $roleKey не найден шаг '$Step'"
    }
    throw "У агента $roleKey отсутствуют шаги workflow"
}

$transcriptWriter = $null
if ($OutputTranscript) {
    $transcriptPath = Resolve-RepoPath -Path $OutputTranscript
    $transcriptWriter = New-Object System.IO.StreamWriter($transcriptPath, $true, [System.Text.Encoding]::UTF8)
    $transcriptWriter.WriteLine("# run-scenario.ps1 :: {0:yyyy-MM-dd HH:mm:ss}" -f (Get-Date))
    $transcriptWriter.WriteLine("role: {0}" -f $roleKey)
    if ($Step) { $transcriptWriter.WriteLine("step: {0}" -f $Step) }
}

function Write-ScenarioLog {
    param([string]$Message)
    Write-Output $Message
    if ($transcriptWriter) {
        $transcriptWriter.WriteLine($Message)
        $transcriptWriter.Flush()
    }
}

function Expand-Command {
    param(
        [string]$Command,
        [hashtable]$Variables
    )

    if ([string]::IsNullOrWhiteSpace($Command)) { return $null }

    # Удаляем optional-сегменты в квадратных скобках
    $result = $Command
    $result = [Regex]::Replace($result, '\[[^\[\]]*\]', '').Trim()

    if (-not $Variables) { $Variables = @{} }

    $placeholders = [Regex]::Matches($result, '<([a-zA-Z0-9_-]+)>')
    foreach ($match in $placeholders) {
        $name = $match.Groups[1].Value
        if ($Variables.ContainsKey($name)) {
            $value = $Variables[$name]
            $result = $result.Replace($match.Value, $value)
        } else {
            return @{ MissingPlaceholder = $name }
        }
    }

    return @{ Command = $result }
}

$overallStatus = $true
$stepResults = @()

foreach ($entry in $selectedSteps) {
    $stepId = if ($entry -is [System.Collections.IDictionary]) { $entry['step'] } else { $entry.step }
    $description = if ($entry -is [System.Collections.IDictionary]) { $entry['description'] } else { $entry.description }
    $scripts = if ($entry -is [System.Collections.IDictionary]) { $entry['scripts'] } else { $entry.scripts }

    Write-ScenarioLog "=== Шаг: $stepId ==="
    if ($description) {
        Write-ScenarioLog $description
    }

    if (-not $scripts) {
        Write-ScenarioLog "Нет команд для выполнения"
        $stepResults += [ordered]@{ step = $stepId; status = 'skipped'; reason = 'no scripts' }
        continue
    }

    if (-not $NoPrompt -and -not $DryRun) {
        $response = Read-Host "Выполнить шаг '$stepId'? [Y/n]"
        if ($response -and $response.ToLowerInvariant().StartsWith('n')) {
            Write-ScenarioLog "Шаг пропущен по запросу пользователя"
            $stepResults += [ordered]@{ step = $stepId; status = 'skipped'; reason = 'user skipped' }
            continue
        }
    }

    $stepSuccess = $true
    $commandIndex = 0
    foreach ($rawCommand in $scripts) {
        $commandIndex++
        $expanded = Expand-Command -Command $rawCommand -Variables $Variables
        if ($expanded -and $expanded.ContainsKey('MissingPlaceholder')) {
            $missing = $expanded.MissingPlaceholder
            Write-ScenarioLog "[WARN] Пропуск команды: отсутствует значение для плейсхолдера <$missing>"
            $stepSuccess = $false
            $overallStatus = $false
            continue
        }
        if (-not $expanded -or -not $expanded.Command) {
            continue
        }

        $commandText = $expanded.Command.Trim()
        Write-ScenarioLog ("[{0}/{1}] {2}" -f $commandIndex, $scripts.Count, $commandText)

        if ($DryRun) { continue }

        try {
            Invoke-Expression $commandText
            Write-ScenarioLog "  ✔ Выполнено"
        } catch {
            Write-ScenarioLog "  ✖ Ошибка: $($_.Exception.Message)"
            $stepSuccess = $false
            $overallStatus = $false
        }
    }

    $stepResults += [ordered]@{ step = $stepId; status = ($stepSuccess ? 'ok' : 'failed') }
}

if ($transcriptWriter) {
    $transcriptWriter.WriteLine("status: {0}" -f ($overallStatus ? 'success' : 'failed'))
    $transcriptWriter.Dispose()
}

if (-not $overallStatus) {
    throw "Сценарий завершён с ошибками. См. вывод выше."
}
