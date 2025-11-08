param(
    [string]$RepoRoot = (Resolve-Path (Join-Path (Split-Path -Parent $MyInvocation.MyCommand.Path) '..')).Path,
    [string]$TargetRelative = 'API-SWAGGER',
    [string]$OutputPath = (Join-Path (Resolve-Path (Join-Path (Split-Path -Parent $MyInvocation.MyCommand.Path) '..')).Path 'swagger-validation.json'),
    [int]$ThrottleLimit = [Math]::Max(1, [Environment]::ProcessorCount)
)

$repoPath = (Resolve-Path $RepoRoot).Path
$targetPath = Join-Path $repoPath $TargetRelative

if (-not (Test-Path $targetPath)) {
    throw "Path not found: $targetPath"
}

$files = Get-ChildItem -Path $targetPath -Recurse -Filter '*.yaml' -File
$apiRoot = Join-Path $targetPath 'api'
$files = $files | Where-Object {
    $_.FullName.StartsWith($apiRoot, [System.StringComparison]::OrdinalIgnoreCase)
}
$files = $files | Where-Object {
    Select-String -Path $_.FullName -Pattern '^\s*openapi:' -Quiet
}
$errors = [System.Collections.Concurrent.ConcurrentBag[object]]::new()
$convertCmd = Get-Command -Name ConvertFrom-Yaml -ErrorAction SilentlyContinue

if (-not $files) {
    $emptyResult = [PSCustomObject]@{
        generatedAt = (Get-Date -AsUTC)
        root = $repoPath
        target = $targetPath
        throttleLimit = $ThrottleLimit
        files = @()
    }
    $emptyJson = $emptyResult | ConvertTo-Json -Depth 5
    Set-Content -Path $OutputPath -Value $emptyJson -Encoding UTF8
    Write-Output $OutputPath
    exit 0
}

$files | ForEach-Object -Parallel {
    $filePath = $_.FullName
    $bag = $using:errors
    $root = $using:repoPath
    $parsed = $null
    $yamlErrors = @()
    $skipCliValidation = $false

    if ($convertCmd) {
        try {
            $rawYaml = Get-Content -Path $filePath -Raw
            $parsed = $rawYaml | ConvertFrom-Yaml -ErrorAction Stop
        } catch {
            $yamlErrors += "Ошибка чтения YAML: $($_.Exception.Message)"
            $skipCliValidation = $true
        }
    }

    if ($convertCmd -and -not $skipCliValidation -and $null -eq $parsed) {
        $yamlErrors += "Не удалось разобрать YAML из файла."
        $skipCliValidation = $true
    }

    if ($null -ne $parsed) {
        if ($null -eq $parsed.info) {
            $yamlErrors += "Отсутствует секция info."
        } else {
            $microservice = $parsed.info.'x-microservice'
            if ($null -eq $microservice) {
                $yamlErrors += "Отсутствует секция info.x-microservice."
            } else {
                $microserviceName = $microservice.name
                if ([string]::IsNullOrWhiteSpace($microserviceName)) {
                    $yamlErrors += "Отсутствует обязательное поле info.x-microservice.name."
                }
            }
        }

        $servers = @($parsed.servers)
        if ($servers.Count -eq 0) {
            $yamlErrors += "Отсутствует секция servers."
        } else {
            $hasProductionServer = $false
            foreach ($server in $servers) {
                $serverUrl = $server.url
                if (-not [string]::IsNullOrWhiteSpace($serverUrl) -and $serverUrl.Trim().ToLowerInvariant() -eq 'https://api.necp.game/v1') {
                    $hasProductionServer = $true
                    break
                }
            }
            if (-not $hasProductionServer) {
                $yamlErrors += "Сервер https://api.necp.game/v1 не указан в секции servers."
            }
        }
    }

    if ($yamlErrors.Count -gt 0) {
        $bag.Add([PSCustomObject]@{
            file    = $filePath
            message = ($yamlErrors -join [Environment]::NewLine)
        })
    }

    if ($skipCliValidation) {
        return
    }

    Push-Location $root
    try {
        $commandOutput = & npx swagger-cli validate $filePath 2>&1
        $exitCode = $LASTEXITCODE
    } finally {
        Pop-Location
    }
    if ($exitCode -ne 0) {
        $bag.Add([PSCustomObject]@{
            file = $filePath
            message = ($commandOutput | Out-String).Trim()
        })
    }
} -ThrottleLimit $ThrottleLimit

$failed = @($errors | Sort-Object file)
$errorCount = $failed.Count

$report = [PSCustomObject]@{
    generatedAt = (Get-Date -AsUTC)
    root = $repoPath
    target = $targetPath
    throttleLimit = $ThrottleLimit
    error = $errorCount
    files = $failed
}

$json = $report | ConvertTo-Json -Depth 5
Set-Content -Path $OutputPath -Value $json -Encoding UTF8
Write-Output $OutputPath

if ($errorCount -eq 0) { exit 0 } else { exit 1 }
