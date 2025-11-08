param(
    [string]$RepoRoot = (Resolve-Path (Join-Path (Split-Path -Parent $MyInvocation.MyCommand.Path) '..')).Path,
    [string]$TargetRelative = 'API-SWAGGER',
    [string]$OutputPath = (Join-Path (Resolve-Path (Join-Path (Split-Path -Parent $MyInvocation.MyCommand.Path) '..')).Path 'swagger-validation.json'),
    [int]$ThrottleLimit = [Math]::Max(1, [Environment]::ProcessorCount)
)

function Test-OpenApiBackendMetadata {
    param([string]$Content)

    $lines = $Content -split "(\r\n|\n|\r)"
    $stack = New-Object System.Collections.Generic.List[pscustomobject]
    $errors = New-Object System.Collections.Generic.List[string]

    $hasInfo = $false
    $hasMicroserviceSection = $false
    $microserviceName = $null
    $hasServers = $false
    $hasProductionServer = $false

    foreach ($rawLine in $lines) {
        if ($null -eq $rawLine) { continue }
        $trimmedLine = $rawLine.Trim()
        if ($trimmedLine.Length -eq 0) { continue }
        if ($trimmedLine.StartsWith('#')) { continue }

        $indent = $rawLine.Length - $rawLine.TrimStart().Length

        while ($stack.Count -gt 0 -and $stack[$stack.Count - 1].Indent -ge $indent) {
            $stack.RemoveAt($stack.Count - 1)
        }

        $lineBody = $trimmedLine
        if ($lineBody.StartsWith('- ')) {
            $lineBody = $lineBody.Substring(2).TrimStart()
            $indent += 2
        }

        if ($lineBody -notmatch '^(?<key>[A-Za-z0-9_\-]+):\s*(?<value>.*)$') {
            continue
        }

        $key = $matches['key']
        $value = $matches['value'].Trim()

        if ($key -eq 'info') {
            $hasInfo = $true
        }

        $insideInfo = $false
        $insideMicroservice = $false
        $insideServers = $false

        foreach ($frame in $stack) {
            if ($frame.Key -eq 'info') {
                $insideInfo = $true
            }
            if ($insideInfo -and $frame.Key -eq 'x-microservice') {
                $insideMicroservice = $true
            }
            if ($frame.Key -eq 'servers') {
                $insideServers = $true
            }
        }

        if ($key -eq 'x-microservice' -and $insideInfo) {
            $hasMicroserviceSection = $true
        }

        if ($key -eq 'servers') {
            $hasServers = $true
        }

        if ($key -eq 'name' -and $insideMicroservice) {
            if ($value.Length -gt 0) {
                $microserviceName = $value.Trim("'`"")
            }
        }

        if ($key -eq 'url' -and $insideServers) {
            if ($value.Length -gt 0) {
                $urlValue = $value.Trim("'`"")
                if ($urlValue.ToLowerInvariant() -eq 'https://api.necp.game/v1') {
                    $hasProductionServer = $true
                }
            }
        }

        $stack.Add([pscustomobject]@{
            Key = $key
            Indent = $indent
        })
    }

    if (-not $hasInfo) {
        $errors.Add('Отсутствует секция info.')
    }

    if (-not $hasMicroserviceSection) {
        $errors.Add('Отсутствует секция info.x-microservice.')
    }

    if ([string]::IsNullOrWhiteSpace($microserviceName)) {
        $errors.Add('Отсутствует обязательное поле info.x-microservice.name.')
    }

    if (-not $hasServers) {
        $errors.Add('Отсутствует секция servers.')
    } elseif (-not $hasProductionServer) {
        $errors.Add('Сервер https://api.necp.game/v1 не указан в секции servers.')
    }

    return [pscustomobject]@{
        Errors = $errors
    }
}

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
$files = $files | Where-Object {
    $normalized = $_.FullName.Replace('\', '/').ToLowerInvariant()
    return ($normalized -notlike "*/api/shared/*") -and ($normalized -notlike "*/api/v1/shared/*")
}
$errors = [System.Collections.Concurrent.ConcurrentBag[object]]::new()
$metadataValidator = ${function:Test-OpenApiBackendMetadata}

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

$files | ForEach-Object {
    $filePath = $_.FullName
    $bag = $errors
    $root = $repoPath
    $validatorFunc = $metadataValidator
    $yamlErrors = @()
    $skipCliValidation = $false
    $rawYaml = $null

    try {
        $rawYaml = Get-Content -Path $filePath -Raw
    } catch {
        $yamlErrors += "Ошибка чтения YAML: $($_.Exception.Message)"
        $skipCliValidation = $true
    }

    if ($null -ne $rawYaml) {
        $analysis = & $validatorFunc $rawYaml
        if ($analysis.Errors.Count -gt 0) {
            $yamlErrors += [string[]]$analysis.Errors
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
}

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

