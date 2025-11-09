param(
    [Parameter(Mandatory = $true)]
    [string]$File
)

if (-not (Test-Path $File)) {
    Write-Error "YAML файл очереди не найден: $File"
    exit 1
}

try {
    $yaml = (Get-Content -LiteralPath $File -Raw) | ConvertFrom-Yaml
} catch {
    Write-Error "Не удалось распарсить YAML: $File"
    exit 1
}

$requiredTop = @("status", "last_updated", "items")
foreach ($key in $requiredTop) {
    if (-not ($yaml.PSObject.Properties.Name -contains $key)) {
        Write-Error "В файле отсутствует обязательное поле '$key': $File"
        exit 1
    }
}

if (-not ($yaml.items -is [System.Collections.IEnumerable])) {
    Write-Error "Поле 'items' должно быть массивом: $File"
    exit 1
}

$ids = @{}
foreach ($item in $yaml.items) {
    if (-not $item.id) {
        Write-Error "Все элементы должны иметь поле 'id': $File"
        exit 1
    }
    if ($ids.ContainsKey($item.id)) {
        Write-Error "Дублирующийся идентификатор '$($item.id)' в $File"
        exit 1
    }
    $ids[$item.id] = $true
}

Write-Output "Очередь $File соответствует структуре."

