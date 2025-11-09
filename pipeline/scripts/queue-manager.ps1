param(
    [Parameter(Mandatory = $true)]
    [ValidateSet("add", "move", "remove", "list")]
    [string]$Command,

    [string]$SourceFile,
    [string]$TargetFile,

    [string]$Id,
    [string]$Title,
    [string]$Owner,
    [string]$ApiSpec,
    [string]$Notes
)

$ErrorActionPreference = "Stop"

$yamlModuleLoaded = $false
try {
    Import-Module -Name powershell-yaml -ErrorAction Stop
    $yamlModuleLoaded = $true
} catch {
    $yamlModuleLoaded = $false
}

$convertFromYaml = Get-Command -Name ConvertFrom-Yaml -ErrorAction SilentlyContinue
$convertToYaml = Get-Command -Name ConvertTo-Yaml -ErrorAction SilentlyContinue
if (-not $convertFromYaml -or -not $convertToYaml) {
    throw "Команды ConvertFrom-Yaml/ConvertTo-Yaml недоступны. Установи PowerShell 7+ или модуль powershell-yaml."
}

function Ensure-FileExists {
    param([string]$Path)
    if (-not (Test-Path $Path)) {
        throw "Файл очереди не найден: $Path"
    }
}

function Load-Queue {
    param([string]$Path)
    Ensure-FileExists -Path $Path
    $content = Get-Content -LiteralPath $Path -Raw
    if ([string]::IsNullOrWhiteSpace($content)) {
        throw "Файл очереди пуст: $Path"
    }
    $queue = $content | ConvertFrom-Yaml
    if ($null -eq $queue) {
        throw "Не удалось распарсить очередь: $Path"
    }
    if (-not $queue.items) {
        $queue.items = @()
    }
    return $queue
}

function Save-Queue {
    param([string]$Path, $Queue)
    $Queue.last_updated = (Get-Date -Format "yyyy-MM-dd HH:mm")
    $params = @{
        Data = $Queue
    }
    if ($convertToYaml.Parameters.ContainsKey('Depth')) {
        $params['Depth'] = 10
    }
    $yaml = ConvertTo-Yaml @params
    Set-Content -LiteralPath $Path -Value $yaml -Encoding UTF8
    Write-Output "Обновлён файл очереди: $Path"
}

switch ($Command.ToLowerInvariant()) {
    "list" {
        if (-not $SourceFile) {
            throw "Для list укажи -SourceFile."
        }
        $queue = Load-Queue -Path $SourceFile
        Write-Output "Статус: $($queue.status)"
        foreach ($item in $queue.items) {
            $line = "$($item.id) — $($item.title)"
            if ($item.owner) { $line += " (owner: $($item.owner))" }
            Write-Output $line
        }
    }
    "add" {
        if (-not $SourceFile) { throw "Для add укажи -SourceFile." }
        if (-not $Id) { throw "Для add укажи -Id." }
        if (-not $Title) { throw "Для add укажи -Title." }
        $queue = Load-Queue -Path $SourceFile
        if ($queue.items | Where-Object { $_.id -eq $Id }) {
            throw "Запись с id '$Id' уже существует в $SourceFile."
        }
        $newItem = [ordered]@{
            id      = $Id
            title   = $Title
        }
        if ($ApiSpec) { $newItem.api_spec = $ApiSpec }
        if ($Owner) { $newItem.owner = $Owner }
        $newItem.updated = (Get-Date -Format "yyyy-MM-dd HH:mm")
        if ($Notes) { $newItem.notes = $Notes }
        $queue.items += $newItem
        Save-Queue -Path $SourceFile -Queue $queue
    }
    "remove" {
        if (-not $SourceFile) { throw "Для remove укажи -SourceFile." }
        if (-not $Id) { throw "Для remove укажи -Id." }
        $queue = Load-Queue -Path $SourceFile
        $initial = $queue.items.Count
        $queue.items = @($queue.items | Where-Object { $_.id -ne $Id })
        if ($queue.items.Count -eq $initial) {
            throw "В очереди $SourceFile не найден элемент с id '$Id'."
        }
        Save-Queue -Path $SourceFile -Queue $queue
    }
    "move" {
        if (-not $SourceFile) { throw "Для move укажи -SourceFile." }
        if (-not $TargetFile) { throw "Для move укажи -TargetFile." }
        if (-not $Id) { throw "Для move укажи -Id." }

        $sourceQueue = Load-Queue -Path $SourceFile
        $item = $sourceQueue.items | Where-Object { $_.id -eq $Id }
        if (-not $item) {
            throw "В очереди $SourceFile не найден элемент с id '$Id'."
        }
        $sourceQueue.items = @($sourceQueue.items | Where-Object { $_.id -ne $Id })
        Save-Queue -Path $SourceFile -Queue $sourceQueue

        $targetQueue = Load-Queue -Path $TargetFile
        if ($targetQueue.items | Where-Object { $_.id -eq $Id }) {
            throw "Элемент с id '$Id' уже существует в целевой очереди $TargetFile."
        }
        $item.updated = (Get-Date -Format "yyyy-MM-dd HH:mm")
        $targetQueue.items += $item
        Save-Queue -Path $TargetFile -Queue $targetQueue
    }
}


