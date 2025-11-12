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

function Get-TopKeyNames {
    param($Object)
    if ($Object -is [System.Collections.IDictionary]) {
        return $Object.Keys
    }
    return $Object.PSObject.Properties.Name
}

function Get-Value {
    param($Object, [string]$Name)
    if ($Object -is [System.Collections.IDictionary]) {
        return $Object[$Name]
    }
    return $Object.$Name
}

$requiredTop = @("status", "last_updated", "items")
$topKeys = Get-TopKeyNames -Object $yaml
foreach ($key in $requiredTop) {
    if (-not ($topKeys -contains $key)) {
        Write-Error "В файле отсутствует обязательное поле '$key': $File"
        exit 1
    }
}

$items = Get-Value -Object $yaml -Name "items"
if (-not ($items -is [System.Collections.IEnumerable])) {
    Write-Error "Поле 'items' должно быть массивом: $File"
    exit 1
}

$ids = @{}
foreach ($item in $items) {
    $itemId = if ($item -is [System.Collections.IDictionary]) { $item["id"] } else { $item.id }
    if (-not $itemId) {
        Write-Error "Все элементы должны иметь поле 'id': $File"
        exit 1
    }
    if ($ids.ContainsKey($itemId)) {
        Write-Error "Дублирующийся идентификатор '$itemId' в $File"
        exit 1
    }
    $ids[$itemId] = $true
}

Write-Output "Очередь $File соответствует структуре."

