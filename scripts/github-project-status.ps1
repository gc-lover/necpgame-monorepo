# PowerShell скрипт для управления статусами задач в GitHub Projects
# Использует GH CLI для работы с Projects API

param(
    [Parameter(Mandatory=$true)]
    [int]$IssueNumber,

    [Parameter(Mandatory=$true)]
    [ValidateSet('Todo', 'In Progress', 'Review', 'Blocked', 'Returned', 'Done')]
    [string]$Status,

    [Parameter(Mandatory=$false)]
    [string]$Owner = 'gc-lover',

    [Parameter(Mandatory=$false)]
    [int]$ProjectNumber = 1
)

Write-Host "Обновление статуса задачи #$IssueNumber на '$Status'..."

# Шаг 1: Проверить существование задачи в проекте
$items = gh project item-list $ProjectNumber --owner $Owner --format json | ConvertFrom-Json
$item = $items.items | Where-Object { $_.content.number -eq $IssueNumber }

if (-not $item) {
    Write-Host "Задача #$IssueNumber не найдена в проекте. Добавляем..."
    gh project item-add $ProjectNumber --owner $Owner --url "https://github.com/$Owner/necpgame-monorepo/issues/$IssueNumber"

    # Подождать немного и проверить снова
    Start-Sleep -Seconds 2
    $items = gh project item-list $ProjectNumber --owner $Owner --format json | ConvertFrom-Json
    $item = $items.items | Where-Object { $_.content.number -eq $IssueNumber }

    if (-not $item) {
        Write-Error "Не удалось добавить задачу #$IssueNumber в проект"
        exit 1
    }
}

# Шаг 2: Получить ID поля Status
$statusFieldId = "PVTSSF_lAHODCWAw84BIyiezg5JYxQ"  # Известный ID поля Status

# Шаг 3: Получить ID опции статуса
$statusOptions = @{
    'Todo' = 'f75ad846'
    'In Progress' = '83d488e7'
    'Review' = '55060662'
    'Blocked' = 'af634d5b'
    'Returned' = 'c01c12e9'
    'Done' = '98236657'
}

$statusOptionId = $statusOptions[$Status]

if (-not $statusOptionId) {
    Write-Error "Неизвестный статус: $Status"
    exit 1
}

# Шаг 4: Обновить статус
Write-Host "Обновление статуса для item $($item.id)..."
gh project item-edit $ProjectNumber --owner $Owner --id $item.id --field-id $statusFieldId --single-select-option-id $statusOptionId

if ($LASTEXITCODE -eq 0) {
    Write-Host "✓ Статус задачи #$IssueNumber успешно обновлен на '$Status'"
} else {
    Write-Error "Не удалось обновить статус задачи #$IssueNumber"
    exit 1
}