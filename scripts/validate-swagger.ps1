param(
    [string]$RepoRoot = (Resolve-Path (Join-Path (Split-Path -Parent $MyInvocation.MyCommand.Path) '..')).Path,
    [string]$TargetRelative = 'API-SWAGGER',
    [string]$ApiSpec = '',
    [string]$ApiDirectory = '',
    [switch]$CheckMetadataOnly,
    [switch]$SkipMetadataCheck,
    [string]$OutputPath = '',
    [int]$ThrottleLimit = [Math]::Max(1, [Environment]::ProcessorCount)
)

if (-not (Get-Command ConvertFrom-Yaml -ErrorAction SilentlyContinue)) {
    Import-Module Microsoft.PowerShell.Utility -ErrorAction SilentlyContinue | Out-Null
}

$MicroserviceRules = [ordered]@{
    'auth-service'      = @{ port = 8081; domain = 'auth';        package = 'com.necpgame.authservice' }
    'character-service' = @{ port = 8082; domain = 'characters'; package = 'com.necpgame.characterservice' }
    'gameplay-service'  = @{ port = 8083; domain = 'gameplay';   package = 'com.necpgame.gameplayservice' }
    'social-service'    = @{ port = 8084; domain = 'social';     package = 'com.necpgame.socialservice' }
    'economy-service'   = @{ port = 8085; domain = 'economy';    package = 'com.necpgame.economyservice' }
    'world-service'     = @{ port = 8086; domain = 'world';      package = 'com.necpgame.worldservice' }
    'narrative-service' = @{ port = 8087; domain = 'narrative';  package = 'com.necpgame.narrativeservice' }
    'admin-service'     = @{ port = 8088; domain = 'admin';      package = 'com.necpgame.adminservice' }
}

function Convert-YamlToObject {
    param(
        [string]$YamlContent,
        [string]$SourcePath
    )

    if (Get-Command ConvertFrom-Yaml -ErrorAction SilentlyContinue) {
        return $YamlContent | ConvertFrom-Yaml -ErrorAction Stop
    }

    $tempFile = [System.IO.Path]::Combine([System.IO.Path]::GetTempPath(), "validate-swagger-" + [System.Guid]::NewGuid().ToString() + ".yaml")
    try {
        Set-Content -Path $tempFile -Value $YamlContent -Encoding UTF8
        $conversionOutput = & npx --yes js-yaml $tempFile 2>&1
        if ($LASTEXITCODE -ne 0) {
            $details = ($conversionOutput | Out-String).Trim()
            throw "Не удалось конвертировать YAML (js-yaml): $details"
        }
        $json = ($conversionOutput -join [Environment]::NewLine)
        return $json | ConvertFrom-Json -Depth 32 -ErrorAction Stop
    } finally {
        Remove-Item -Path $tempFile -ErrorAction SilentlyContinue
    }
}

function Test-OpenApiBackendMetadata {
    param(
        [string]$Content,
        [string]$RelativePath
    )

    $errors = New-Object System.Collections.Generic.List[string]

    try {
        $document = Convert-YamlToObject -YamlContent $Content -SourcePath $RelativePath
    } catch {
        $errors.Add("Ошибка чтения YAML: $($_.Exception.Message)")
        return [pscustomobject]@{ Errors = $errors }
    }

    $lines = $Content -split "(\r\n|\n|\r)"
    $stack = New-Object System.Collections.Generic.List[pscustomobject]

    $hasInfo = $false
    $hasMicroserviceSection = $false
    $microserviceName = $null
    $hasServers = $false
    $hasProductionServer = $false
    $hasLocalGateway = $false

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
                if ($urlValue.ToLowerInvariant() -eq 'http://localhost:8080/api/v1') {
                    $hasLocalGateway = $true
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

    # Детальная проверка микросервисной метадаты и путей
    $infoSection = $document.info
    $microserviceMeta = $null
    if ($infoSection -and $infoSection.PSObject.Properties.Name -contains 'x-microservice') {
        $microserviceMeta = $infoSection.'x-microservice'
    }

    if ($microserviceMeta) {
        $metaName = $microserviceMeta.name
        $metaPort = $microserviceMeta.port
        $metaDomain = $microserviceMeta.domain
        $metaBasePath = $microserviceMeta.'base-path'
        if (-not $metaName) {
            $errors.Add('info.x-microservice.name не заполнен.')
        } elseif (-not $MicroserviceRules.Contains($metaName)) {
            $errors.Add("info.x-microservice.name '$metaName' не входит в список допустимых значений.")
        } else {
            $expected = $MicroserviceRules[$metaName]
            if ($null -ne $metaPort -and [int]$metaPort -ne [int]$expected.port) {
                $errors.Add("info.x-microservice.port ($metaPort) не соответствует ожидаемому порту $($expected.port) для $metaName.")
            }
            if ($metaDomain -and $metaDomain -ne $expected.domain) {
                $errors.Add("info.x-microservice.domain '$metaDomain' не соответствует ожидаемому домену '$($expected.domain)'.")
            } elseif (-not $metaDomain) {
                $errors.Add('info.x-microservice.domain не заполнен.')
            }

            if ($metaBasePath) {
                $expectedBasePrefix = "/api/v1/$($expected.domain)"
                if ($metaBasePath -notlike "$expectedBasePrefix*") {
                    $errors.Add("info.x-microservice.base-path '$metaBasePath' должен начинаться с '$expectedBasePrefix'.")
                }
            } else {
                $errors.Add('info.x-microservice.base-path не заполнен.')
            }
            if ($microserviceMeta.PSObject.Properties.Name -contains 'package') {
                $metaPackage = [string]$microserviceMeta.package
                if ([string]::IsNullOrWhiteSpace($metaPackage)) {
                    $errors.Add('info.x-microservice.package не заполнен.')
                } elseif (-not [string]::Equals($metaPackage, $expected.package, [System.StringComparison]::OrdinalIgnoreCase)) {
                    $errors.Add("info.x-microservice.package '$metaPackage' не соответствует ожидаемому значению '$($expected.package)'.")
                }
            } else {
                $errors.Add('info.x-microservice.package не заполнен.')
            }

            if ($RelativePath) {
                $relativeNorm = $RelativePath.Replace('\', '/')
                $relativeNorm = $relativeNorm.TrimStart('/')
                $expectedPathPrefix = "api/v1/$($expected.domain)"
                if ($relativeNorm -and -not $relativeNorm.StartsWith($expectedPathPrefix, $true, [System.Globalization.CultureInfo]::InvariantCulture)) {
                    $errors.Add("Файл расположен в '$relativeNorm', но ожидается каталог '$expectedPathPrefix'.")
                }
            }
        }
    }

    if (-not $hasLocalGateway) {
        $errors.Add('Секция servers должна включать http://localhost:8080/api/v1 (development gateway).')
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

if ([string]::IsNullOrEmpty($OutputPath)) {
    $OutputPath = Join-Path $repoPath 'swagger-validation.json'
}

$skipMetadataCheck = $SkipMetadataCheck.IsPresent
$metadataOnly = $CheckMetadataOnly.IsPresent

function Resolve-FullPath {
    param(
        [string]$Path,
        [string]$RepoPath
    )

    if ([string]::IsNullOrWhiteSpace($Path)) {
        return $null
    }

    if (Test-Path $Path) {
        return (Resolve-Path $Path).Path
    }

    $candidate = Join-Path $RepoPath $Path
    if (Test-Path $candidate) {
        return (Resolve-Path $candidate).Path
    }

    throw "Path not found: $Path"
}

$resolvedApiSpec = $null
if (-not [string]::IsNullOrWhiteSpace($ApiSpec)) {
    $resolvedApiSpec = Resolve-FullPath -Path $ApiSpec -RepoPath $repoPath
}

$resolvedApiDirectory = $null
if (-not [string]::IsNullOrWhiteSpace($ApiDirectory)) {
    $resolvedApiDirectory = Resolve-FullPath -Path $ApiDirectory -RepoPath $repoPath
    if (-not (Test-Path $resolvedApiDirectory -PathType Container)) {
        throw "Directory not found: $ApiDirectory"
    }
}

if ($resolvedApiSpec -and $resolvedApiDirectory) {
    throw "Укажите только -ApiSpec или -ApiDirectory, но не оба сразу."
}

function Get-OpenApiFiles {
    param(
        [string[]]$Candidates,
        [string]$ApiRoot
    )

    $valid = @()

    foreach ($candidate in $Candidates) {
        if (-not (Test-Path $candidate -PathType Leaf)) {
            continue
        }

        if ($ApiRoot -and (-not $candidate.StartsWith($ApiRoot, [System.StringComparison]::OrdinalIgnoreCase))) {
            continue
        }

        $normalized = $candidate.Replace('\', '/').ToLowerInvariant()
        if ($normalized -like '*/api/shared/*' -or $normalized -like '*/api/v1/shared/*') {
            continue
        }

        if (-not (Select-String -Path $candidate -Pattern '^\s*openapi:' -Quiet)) {
            continue
        }

        if ($normalized.EndsWith('.yml') -or $normalized.EndsWith('.yaml')) {
            $valid += $candidate
        }
    }

    return $valid
}

$initialFiles = @()

if ($resolvedApiSpec) {
    if (-not $resolvedApiSpec.StartsWith($targetPath, [System.StringComparison]::OrdinalIgnoreCase)) {
        throw "Файл должен находиться внутри '$TargetRelative'."
    }
    $initialFiles = @($resolvedApiSpec)
} elseif ($resolvedApiDirectory) {
    if (-not $resolvedApiDirectory.StartsWith($targetPath, [System.StringComparison]::OrdinalIgnoreCase)) {
        throw "Директория должна находиться внутри '$TargetRelative'."
    }
    $initialFiles = Get-ChildItem -Path $resolvedApiDirectory -Recurse -Include *.yaml, *.yml -File | Select-Object -ExpandProperty FullName
} else {
    $initialFiles = Get-ChildItem -Path $targetPath -Recurse -Include *.yaml, *.yml -File | Select-Object -ExpandProperty FullName
}

$apiRoot = if ($resolvedApiDirectory) {
    $resolvedApiDirectory
} else {
    Join-Path $targetPath 'api'
}

$files = Get-OpenApiFiles -Candidates $initialFiles -ApiRoot $apiRoot
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
    $filePath = $_
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

    if ($null -ne $rawYaml -and -not $skipMetadataCheck) {
        $relativeToTarget = if ($filePath.StartsWith($targetPath, [System.StringComparison]::OrdinalIgnoreCase)) {
            $filePath.Substring($targetPath.Length).TrimStart('\','/')
        } else {
            [IO.Path]::GetRelativePath($repoPath, $filePath).TrimStart('\','/')
        }
        $analysis = & $validatorFunc $rawYaml $relativeToTarget
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

    if ($skipCliValidation -or $metadataOnly) {
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

