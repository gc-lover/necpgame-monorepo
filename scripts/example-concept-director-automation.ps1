# Example: Улучшенная автоматизация Concept Director
# Демонстрирует использование Pipeline.Automation.psm1

param(
    [string]$QueueFile = "shared/trackers/queues/concept/queued.yaml",
    [string]$ActivityLogFile = "shared/trackers/activity-log.yaml",
    [switch]$DryRun
)

# Импорт модуля автоматизации
Import-Module "$PSScriptRoot/modules/Pipeline.Automation.psm1"

function Process-ConceptQueue {
    param([string]$QueuePath, [string]$ActivityLogPath)

    # Использовать Invoke-WithLock для защиты от одновременного доступа
    Invoke-WithLock -OperationName "ProcessConceptQueue" -ScriptBlock {
        Write-AutomationLog "Processing Concept Director queue: $QueuePath" "INFO"

        # Использовать транзакционный буфер для безопасной работы с очередью
        Invoke-QueueTransaction -QueueFilePath $QueuePath -OperationName "ProcessQueueItems" -ScriptBlock {
            param($buffer)

            $processedItems = 0
            $item = $null

            # Обработать все доступные элементы очереди
            while ($null -ne ($item = $buffer.GetNextItem())) {
                try {
                    Write-AutomationLog "Processing item: $($item.id)" "INFO"

                    # Имитация обработки элемента
                    if (-not $DryRun) {
                        # Здесь будет логика обработки элемента очереди
                        # Например: генерация task, валидация документа, etc.

                        Start-Sleep -Milliseconds 500 # Имитация работы
                    }

                    # Отметить элемент как обработанный
                    $buffer.MarkItemProcessed($item.id)

                    $processedItems++
                    Write-AutomationMetric "ProcessedItems" $processedItems

                }
                catch {
                    Write-AutomationLog "Failed to process item $($item.id): $($_.Exception.Message)" "ERROR"
                    # В случае ошибки элемент останется в очереди для повторной обработки
                    break
                }
            }

            Write-AutomationLog "Processed $processedItems items from queue" "INFO"
        }
    }
}

function Update-ActivityLog {
    param([string]$ActivityLogPath, [hashtable]$ActivityData)

    # Защитить обновление activity log блокировкой
    Invoke-WithLock -OperationName "UpdateActivityLog" -ScriptBlock {
        Write-AutomationLog "Updating activity log: $ActivityLogPath" "INFO"

        try {
            # Прочитать текущий activity log
            $activityLog = @{}
            if (Test-Path $ActivityLogPath) {
                $activityLog = Get-Content $ActivityLogPath -Raw | ConvertFrom-Yaml
                if ($null -eq $activityLog) { $activityLog = @{} }
            }

            # Обновить данные
            foreach ($key in $ActivityData.Keys) {
                $activityLog[$key] = $ActivityData[$key]
            }

            # Добавить метку времени
            $activityLog["last_updated"] = [DateTime]::Now.ToString("o")
            $activityLog["automation_version"] = "2.0-hardened"

            # Записать обновленный activity log
            $activityLog | ConvertTo-Yaml | Set-Content $ActivityLogPath -Encoding UTF8

            Write-AutomationLog "Activity log updated successfully" "INFO"
        }
        catch {
            Write-AutomationLog "Failed to update activity log: $($_.Exception.Message)" "ERROR"
            throw
        }
    }
}

# Основная логика скрипта
try {
    Write-AutomationLog "Starting Concept Director automation (Hardened Version)" "INFO"

    # Метрики выполнения
    Write-AutomationMetric "ScriptStartTime" ([DateTime]::Now)
    Write-AutomationMetric "DryRun" $DryRun.ToString()

    # 1. Обработать очередь концептов
    if (Test-Path $QueueFile) {
        Process-ConceptQueue -QueuePath $QueueFile -ActivityLogPath $ActivityLogFile
    }
    else {
        Write-AutomationLog "Queue file not found: $QueueFile" "WARN"
    }

    # 2. Обновить activity log
    $activityUpdate = @{
        "last_automation_run" = [DateTime]::Now.ToString("o")
        "processed_items"     = (Get-AutomationMetric "ProcessedItems" 0)
        "automation_status"   = "completed"
    }
    Update-ActivityLog -ActivityLogPath $ActivityLogFile -ActivityData $activityUpdate

    Write-AutomationLog "Concept Director automation completed successfully" "INFO"

}
catch {
    Write-AutomationLog "Critical error in Concept Director automation: $($_.Exception.Message)" "ERROR"
    Write-AutomationLog "Stack trace: $($_.ScriptStackTrace)" "ERROR"

    # Обновить статус в activity log даже при ошибке
    try {
        $errorUpdate = @{
            "last_error"        = [DateTime]::Now.ToString("o")
            "error_message"     = $_.Exception.Message
            "automation_status" = "failed"
        }
        Update-ActivityLog -ActivityLogPath $ActivityLogFile -ActivityData $errorUpdate
    }
    catch {
        Write-AutomationLog "Failed to update error status in activity log" "ERROR"
    }

    # Завершить с ошибкой
    exit 1
}

# Финализация метрик
Write-AutomationMetric "ScriptEndTime" ([DateTime]::Now)
Write-AutomationMetric "TotalDuration" ((Get-AutomationMetric "ScriptEndTime" ([DateTime]::Now)) - (Get-AutomationMetric "ScriptStartTime" ([DateTime]::Now))).TotalSeconds

Write-AutomationLog "Automation script finished" "INFO"