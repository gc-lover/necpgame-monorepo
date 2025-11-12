<#
.SYNOPSIS
  Проверяет, что изменения в знаниях или задачах сопровождаются обновлением Activity Log.

.DESCRIPTION
  Сравнивает текущую ветку с базовой ревизией и убеждается, что если были изменены файлы
  в `shared/docs/knowledge/`, `pipeline/tasks/` или `shared/trackers/queues/`, то среди
  изменённых файлов присутствует `shared/trackers/activity-log.yaml`.

.EXAMPLE
  pwsh -File pipeline/scripts/check-activity-log.ps1 -BaseRef origin/develop

.PARAMETER BaseRef
  Ревизия или ветка, с которой сравнивать (используется формат `<base>...HEAD`).
  Если параметр не указан, используется `HEAD~1`.
.PARAMETER UseIndex
  Включает проверку по индексированным (staged) файлам через `git diff --cached`.
#>

[CmdletBinding()]
param(
    [string]$BaseRef,
    [switch]$UseIndex
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

$git = Get-Command git -ErrorAction Stop

function Get-ChangedFiles {
    param(
        [string]$Range,
        [switch]$Index
    )
    $diffArgs = @("diff", "--name-only")
    if ($Index) {
        $diffArgs += "--cached"
        if ($Range) {
            $diffArgs += $Range
        }
    } else {
        $diffArgs += $Range
    }

    $psi = New-Object System.Diagnostics.ProcessStartInfo $git.Source, ($diffArgs -join ' ')
    $psi.WorkingDirectory = (Get-Location).Path
    $psi.RedirectStandardOutput = $true
    $psi.RedirectStandardError = $true
    $psi.UseShellExecute = $false
    $process = [System.Diagnostics.Process]::Start($psi)
    $stdout = $process.StandardOutput.ReadToEnd()
    $stderr = $process.StandardError.ReadToEnd()
    $process.WaitForExit()
    if ($process.ExitCode -ne 0) {
        throw "git diff завершился с ошибкой: $stderr"
    }
    return ($stdout -split "`n" | Where-Object { -not [string]::IsNullOrWhiteSpace($_) })
}

$range = $null
if ($BaseRef) {
    $range = "$BaseRef...HEAD"
    try {
        & $git.Source rev-parse $BaseRef | Out-Null
    } catch {
        Write-Warning "Не удалось найти базовую ревизию '$BaseRef'. Использую HEAD~1."
        $range = 'HEAD~1..HEAD'
    }
} elseif (-not $UseIndex) {
    $range = 'HEAD~1..HEAD'
}

$files = Get-ChangedFiles -Range $range -Index:$UseIndex
if (-not $files -or $files.Count -eq 0) {
    Write-Output "Нет изменений для проверки Activity Log."
    exit 0
}

$needsActivityLog = $false
foreach ($file in $files) {
    if ($file -like 'shared/docs/knowledge/*' -or $file -like 'pipeline/tasks/*' -or $file -like 'shared/trackers/queues/*') {
        if ($file -ne 'shared/trackers/activity-log.yaml') {
            $needsActivityLog = $true
            break
        }
    }
}

if (-not $needsActivityLog) {
    Write-Output "Изменения не требуют обновления Activity Log."
    exit 0
}

$hasActivityLogUpdate = $files -contains 'shared/trackers/activity-log.yaml'
if ($hasActivityLogUpdate) {
    Write-Output "Activity Log обновлён."
    exit 0
}

Write-Error "Изменены знания/очереди/задачи, но отсутствует обновление shared/trackers/activity-log.yaml."
exit 1
