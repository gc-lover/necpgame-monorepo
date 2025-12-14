# Pipeline.Automation.psm1 - Укрепленная автоматизация Concept Director
# Реализует механизм взаимного исключения и транзакционный буфер для предотвращения конфликтов

using namespace System.Collections.Generic
using namespace System.IO

# Конфигурация автоматизации
class AutomationConfig {
    [string]$LockFilePath
    [int]$MaxLockRetries
    [int]$LockRetryDelayMs
    [int]$LockTimeoutMs
    [string]$StagingDirectory
    [string]$LogsDirectory
    [hashtable]$TelemetryConfig

    AutomationConfig() {
        $this.LockFilePath = Join-Path $PSScriptRoot "..\automation.lock"
        $this.MaxLockRetries = 10
        $this.LockRetryDelayMs = 1000
        $this.LockTimeoutMs = 30000
        $this.StagingDirectory = Join-Path $PSScriptRoot "..\staging"
        $this.LogsDirectory = Join-Path $PSScriptRoot "..\logs"
        $this.TelemetryConfig = @{
            EnableMetrics = $true
            EnableTracing = $true
            MetricsFile   = Join-Path $PSScriptRoot "..\metrics\automation-metrics.json"
        }
    }
}

# Класс для управления блокировками
class FileLockManager {
    [AutomationConfig]$Config
    [System.IO.FileStream]$LockStream
    [bool]$HasLock
    [DateTime]$LockAcquiredTime

    FileLockManager([AutomationConfig]$config) {
        $this.Config = $config
        $this.HasLock = $false
    }

    [bool] TryAcquireLock() {
        $retryCount = 0
        $lockTimeout = [DateTime]::Now.AddMilliseconds($this.Config.LockTimeoutMs)

        while ($retryCount -lt $this.Config.MaxLockRetries -and [DateTime]::Now -lt $lockTimeout) {
            try {
                # Попытка получить эксклюзивную блокировку файла
                $this.LockStream = [System.IO.File]::Open(
                    $this.Config.LockFilePath,
                    [System.IO.FileMode]::OpenOrCreate,
                    [System.IO.FileAccess]::ReadWrite,
                    [System.IO.FileShare]::None
                )

                $this.HasLock = $true
                $this.LockAcquiredTime = [DateTime]::Now

                Write-AutomationLog "Lock acquired successfully" "INFO"
                return $true
            }
            catch {
                $retryCount++
                $delay = [Math]::Min($this.Config.LockRetryDelayMs * [Math]::Pow(2, $retryCount - 1), 10000)
                Write-AutomationLog "Lock acquisition failed (attempt $retryCount/$($this.Config.MaxLockRetries)), retrying in ${delay}ms" "WARN"
                Start-Sleep -Milliseconds $delay
            }
        }

        Write-AutomationLog "Failed to acquire lock after $($this.Config.MaxLockRetries) attempts" "ERROR"
        return $false
    }

    [void] ReleaseLock() {
        if ($this.HasLock -and $null -ne $this.LockStream) {
            try {
                $lockDuration = [DateTime]::Now - $this.LockAcquiredTime
                $this.LockStream.Close()
                $this.LockStream.Dispose()
                $this.HasLock = $false
                Write-AutomationLog "Lock released after $($lockDuration.TotalSeconds.ToString('F2')) seconds" "INFO"
            }
            catch {
                Write-AutomationLog "Error releasing lock: $($_.Exception.Message)" "ERROR"
            }
        }
    }
}

# Класс для транзакционного буфера очереди
class QueueTransactionBuffer {
    [AutomationConfig]$Config
    [string]$QueueFilePath
    [string]$StagingFilePath
    [object]$StagingData
    [bool]$InTransaction

    QueueTransactionBuffer([AutomationConfig]$config, [string]$queueFilePath) {
        $this.Config = $config
        $this.QueueFilePath = $queueFilePath
        $this.StagingFilePath = Join-Path $this.Config.StagingDirectory "$(Split-Path $queueFilePath -Leaf).staging"
        $this.InTransaction = $false

        # Создать директорию staging если не существует
        if (-not (Test-Path $this.Config.StagingDirectory)) {
            New-Item -ItemType Directory -Path $this.Config.StagingDirectory -Force | Out-Null
        }
    }

    [bool] BeginTransaction() {
        if ($this.InTransaction) {
            Write-AutomationLog "Transaction already in progress" "WARN"
            return $false
        }

        try {
            # Прочитать текущие данные очереди
            if (Test-Path $this.QueueFilePath) {
                $this.StagingData = Get-Content $this.QueueFilePath -Raw | ConvertFrom-Json
            }
            else {
                $this.StagingData = @{ items = @() }
            }

            # Сохранить в staging файл
            $this.StagingData | ConvertTo-Json -Depth 10 | Set-Content $this.StagingFilePath -Encoding UTF8
            $this.InTransaction = $true

            Write-AutomationLog "Transaction started for queue: $(Split-Path $this.QueueFilePath -Leaf)" "INFO"
            return $true
        }
        catch {
            Write-AutomationLog "Failed to begin transaction: $($_.Exception.Message)" "ERROR"
            return $false
        }
    }

    [object] GetNextItem() {
        if (-not $this.InTransaction) {
            Write-AutomationLog "No active transaction" "ERROR"
            return $null
        }

        if ($this.StagingData.items.Count -eq 0) {
            return $null
        }

        # Взять первый элемент (FIFO)
        $item = $this.StagingData.items[0]
        $this.StagingData.items = $this.StagingData.items[1..($this.StagingData.items.Count - 1)]

        Write-AutomationLog "Retrieved item from queue: $($item.id)" "INFO"
        return $item
    }

    [void] MarkItemProcessed([string]$itemId) {
        if (-not $this.InTransaction) {
            Write-AutomationLog "No active transaction" "ERROR"
            return
        }

        # Элемент уже удален из staging data в GetNextItem
        # Сохранить обновленные данные в staging файл
        try {
            $this.StagingData | ConvertTo-Json -Depth 10 | Set-Content $this.StagingFilePath -Encoding UTF8
            Write-AutomationLog "Marked item as processed: $itemId" "INFO"
        }
        catch {
            Write-AutomationLog "Failed to mark item as processed: $($_.Exception.Message)" "ERROR"
        }
    }

    [bool] CommitTransaction() {
        if (-not $this.InTransaction) {
            Write-AutomationLog "No active transaction" "ERROR"
            return $false
        }

        try {
            # Записать staging данные обратно в основную очередь
            $this.StagingData | ConvertTo-Json -Depth 10 | Set-Content $this.QueueFilePath -Encoding UTF8

            # Очистить staging файл
            if (Test-Path $this.StagingFilePath) {
                Remove-Item $this.StagingFilePath -Force
            }

            $this.InTransaction = $false
            Write-AutomationLog "Transaction committed successfully" "INFO"
            return $true
        }
        catch {
            Write-AutomationLog "Failed to commit transaction: $($_.Exception.Message)" "ERROR"
            return $false
        }
    }

    [void] RollbackTransaction() {
        if (-not $this.InTransaction) {
            Write-AutomationLog "No active transaction" "WARN"
            return
        }

        try {
            # Очистить staging файл
            if (Test-Path $this.StagingFilePath) {
                Remove-Item $this.StagingFilePath -Force
            }

            $this.InTransaction = $false
            Write-AutomationLog "Transaction rolled back" "INFO"
        }
        catch {
            Write-AutomationLog "Error during rollback: $($_.Exception.Message)" "ERROR"
        }
    }
}

# Класс для телеметрии и логирования
class AutomationTelemetry {
    [AutomationConfig]$Config
    [hashtable]$Metrics
    [DateTime]$StartTime
    [string]$OperationId

    AutomationTelemetry([AutomationConfig]$config) {
        $this.Config = $config
        $this.Metrics = @{}
        $this.StartTime = [DateTime]::Now
        $this.OperationId = [Guid]::NewGuid().ToString()

        # Создать директорию для метрик если не существует
        $metricsDir = Split-Path $this.Config.TelemetryConfig.MetricsFile -Parent
        if (-not (Test-Path $metricsDir)) {
            New-Item -ItemType Directory -Path $metricsDir -Force | Out-Null
        }
    }

    [void] StartOperation([string]$operationName) {
        $this.Metrics[$operationName] = @{
            StartTime   = [DateTime]::Now
            Status      = "running"
            OperationId = $this.OperationId
        }
        Write-AutomationLog "Started operation: $operationName (ID: $($this.OperationId))" "INFO"
    }

    [void] EndOperation([string]$operationName, [bool]$success, [string]$errorMessage = "") {
        if ($this.Metrics.ContainsKey($operationName)) {
            $operation = $this.Metrics[$operationName]
            $operation.EndTime = [DateTime]::Now
            $operation.Duration = ($operation.EndTime - $operation.StartTime).TotalSeconds
            $operation.Status = if ($success) { "completed" } else { "failed" }
            if ($errorMessage) {
                $operation.ErrorMessage = $errorMessage
            }
        }

        $status = if ($success) { "SUCCESS" } else { "FAILED" }
        Write-AutomationLog "Ended operation: $operationName ($status)" "INFO"
    }

    [void] IncrementCounter([string]$counterName) {
        if (-not $this.Metrics.ContainsKey($counterName)) {
            $this.Metrics[$counterName] = 0
        }
        $this.Metrics[$counterName]++
    }

    [void] RecordMetric([string]$name, [object]$value) {
        $this.Metrics[$name] = $value
    }

    [void] FlushMetrics() {
        if (-not $this.Config.TelemetryConfig.EnableMetrics) {
            return
        }

        try {
            $metricsData = @{
                Timestamp     = [DateTime]::Now.ToString("o")
                OperationId   = $this.OperationId
                TotalDuration = ([DateTime]::Now - $this.StartTime).TotalSeconds
                Metrics       = $this.Metrics
            }

            $metricsData | ConvertTo-Json -Depth 10 | Set-Content $this.Config.TelemetryConfig.MetricsFile -Encoding UTF8
            Write-AutomationLog "Metrics flushed to file" "INFO"
        }
        catch {
            Write-AutomationLog "Failed to flush metrics: $($_.Exception.Message)" "ERROR"
        }
    }
}

# Глобальные переменные модуля
$script:AutomationConfig = [AutomationConfig]::new()
$script:Telemetry = [AutomationTelemetry]::new($script:AutomationConfig)

# Функции для работы с автоматизацией

function Write-AutomationLog {
    param(
        [string]$Message,
        [string]$Level = "INFO",
        [string]$Component = "Automation"
    )

    $timestamp = [DateTime]::Now.ToString("yyyy-MM-dd HH:mm:ss")
    $logEntry = "[$timestamp] [$Level] [$Component] $Message"

    # Создать директорию логов если не существует
    if (-not (Test-Path $script:AutomationConfig.LogsDirectory)) {
        New-Item -ItemType Directory -Path $script:AutomationConfig.LogsDirectory -Force | Out-Null
    }

    $logFile = Join-Path $script:AutomationConfig.LogsDirectory "automation-$(Get-Date -Format 'yyyy-MM-dd').log"
    $logEntry | Out-File -FilePath $logFile -Append -Encoding UTF8

    # Также вывод в консоль для отладки
    Write-Host $logEntry
}

function New-AutomationLock {
    return [FileLockManager]::new($script:AutomationConfig)
}

function New-QueueTransactionBuffer {
    param([string]$QueueFilePath)
    return [QueueTransactionBuffer]::new($script:AutomationConfig, $QueueFilePath)
}

function Start-AutomationOperation {
    param([string]$OperationName)
    $script:Telemetry.StartOperation($OperationName)
}

function Stop-AutomationOperation {
    param(
        [string]$OperationName,
        [bool]$Success = $true,
        [string]$ErrorMessage = ""
    )
    $script:Telemetry.EndOperation($OperationName, $Success, $ErrorMessage)
}

function Write-AutomationMetric {
    param([string]$Name, [object]$Value)
    $script:Telemetry.RecordMetric($Name, $Value)
}

function Invoke-WithLock {
    param(
        [scriptblock]$ScriptBlock,
        [string]$OperationName = "GenericOperation"
    )

    $lockManager = New-AutomationLock
    $success = $false
    $errorMessage = ""

    try {
        Start-AutomationOperation $OperationName

        if (-not $lockManager.TryAcquireLock()) {
            throw "Failed to acquire lock for operation: $OperationName"
        }

        Write-AutomationLog "Executing operation with lock: $OperationName" "INFO"

        # Выполнить скриптблок
        & $ScriptBlock

        $success = $true
        Write-AutomationLog "Operation completed successfully: $OperationName" "INFO"
    }
    catch {
        $errorMessage = $_.Exception.Message
        Write-AutomationLog "Operation failed: $OperationName - $errorMessage" "ERROR"
        throw
    }
    finally {
        $lockManager.ReleaseLock()
        Stop-AutomationOperation $OperationName $success $errorMessage
        $script:Telemetry.FlushMetrics()
    }
}

function Invoke-QueueTransaction {
    param(
        [string]$QueueFilePath,
        [scriptblock]$ScriptBlock,
        [string]$OperationName = "QueueTransaction"
    )

    $buffer = New-QueueTransactionBuffer $QueueFilePath
    $success = $false

    try {
        Start-AutomationOperation $OperationName

        if (-not $buffer.BeginTransaction()) {
            throw "Failed to begin transaction for queue: $QueueFilePath"
        }

        Write-AutomationLog "Executing queue transaction: $OperationName" "INFO"

        # Выполнить скриптблок с буфером
        & $ScriptBlock $buffer

        if (-not $buffer.CommitTransaction()) {
            throw "Failed to commit transaction for queue: $QueueFilePath"
        }

        $success = $true
        Write-AutomationLog "Queue transaction completed successfully: $OperationName" "INFO"
    }
    catch {
        $buffer.RollbackTransaction()
        Write-AutomationLog "Queue transaction failed: $OperationName - $($_.Exception.Message)" "ERROR"
        throw
    }
    finally {
        Stop-AutomationOperation $OperationName $success
        $script:Telemetry.FlushMetrics()
    }
}

# Экспорт функций модуля
Export-ModuleMember -Function @(
    'Write-AutomationLog',
    'New-AutomationLock',
    'New-QueueTransactionBuffer',
    'Start-AutomationOperation',
    'Stop-AutomationOperation',
    'Write-AutomationMetric',
    'Invoke-WithLock',
    'Invoke-QueueTransaction'
)

# Инициализация модуля
Write-AutomationLog "Pipeline.Automation module loaded successfully" "INFO"