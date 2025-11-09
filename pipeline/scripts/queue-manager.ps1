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

try {
    Import-Module -Name powershell-yaml -ErrorAction Stop
} catch {
}

$convertFromYaml = Get-Command -Name ConvertFrom-Yaml -ErrorAction SilentlyContinue
$convertToYaml = Get-Command -Name ConvertTo-Yaml -ErrorAction SilentlyContinue
if (-not $convertFromYaml -or -not $convertToYaml) {
    throw "Команды ConvertFrom-Yaml/ConvertTo-Yaml недоступны. Установи PowerShell 7+ или модуль powershell-yaml."
}

function Test-QueueFile {
    param([string]$Path)
    if (-not (Test-Path $Path)) {
        throw "Файл очереди не найден: $Path"
    }
}

function Get-QueueContent {
    param([string]$Path)
    Test-QueueFile -Path $Path
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

function Set-QueueContent {
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
        $queue = Get-QueueContent -Path $SourceFile
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
        $queue = Get-QueueContent -Path $SourceFile
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
        Set-QueueContent -Path $SourceFile -Queue $queue
    }
    "remove" {
        if (-not $SourceFile) { throw "Для remove укажи -SourceFile." }
        if (-not $Id) { throw "Для remove укажи -Id." }
        $queue = Get-QueueContent -Path $SourceFile
        $initial = $queue.items.Count
        $queue.items = @($queue.items | Where-Object { $_.id -ne $Id })
        if ($queue.items.Count -eq $initial) {
            throw "В очереди $SourceFile не найден элемент с id '$Id'."
        }
        Set-QueueContent -Path $SourceFile -Queue $queue
    }
    "move" {
        if (-not $SourceFile) { throw "Для move укажи -SourceFile." }
        if (-not $TargetFile) { throw "Для move укажи -TargetFile." }
        if (-not $Id) { throw "Для move укажи -Id." }

        $sourceQueue = Get-QueueContent -Path $SourceFile
        $item = $sourceQueue.items | Where-Object { $_.id -eq $Id }
        if (-not $item) {
            throw "В очереди $SourceFile не найден элемент с id '$Id'."
        }
        $sourceQueue.items = @($sourceQueue.items | Where-Object { $_.id -ne $Id })
        Set-QueueContent -Path $SourceFile -Queue $sourceQueue

        $targetQueue = Get-QueueContent -Path $TargetFile
        if ($targetQueue.items | Where-Object { $_.id -eq $Id }) {
            throw "Элемент с id '$Id' уже существует в целевой очереди $TargetFile."
        }
        $item.updated = (Get-Date -Format "yyyy-MM-dd HH:mm")
        $targetQueue.items += $item
        Set-QueueContent -Path $TargetFile -Queue $targetQueue
    }
}


