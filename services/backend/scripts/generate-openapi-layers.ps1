param(
    [string]$ApiSpec = "",
    [string]$ApiDirectory = "",
    [switch]$Validate,
    [switch]$DryRun,
    [string]$Layers = "All"
)

$ErrorActionPreference = "Stop"

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$backendRoot = Split-Path -Parent $scriptDir
$repoRoot = Split-Path -Parent $backendRoot
$openApiRoot = Join-Path $repoRoot "services\openapi\api\v1"

function Resolve-PathOrThrow {
    param(
        [string]$Base,
        [string]$PathValue,
        [string]$ParameterName
    )
    if ([string]::IsNullOrWhiteSpace($PathValue)) {
        return ""
    }
    $candidate = if ([System.IO.Path]::IsPathRooted($PathValue)) { $PathValue } else { Join-Path $Base $PathValue }
    if (-not (Test-Path $candidate)) {
        throw "Путь не найден для параметра ${ParameterName}: $candidate"
    }
    return (Resolve-Path $candidate).Path
}

$resolvedSpec = Resolve-PathOrThrow -Base $repoRoot -PathValue $ApiSpec -ParameterName "-ApiSpec"
if ([string]::IsNullOrEmpty($ApiDirectory)) {
    $ApiDirectory = $openApiRoot
}
$resolvedDirectory = Resolve-PathOrThrow -Base $repoRoot -PathValue $ApiDirectory -ParameterName "-ApiDirectory"

if ([string]::IsNullOrEmpty($resolvedSpec) -and [string]::IsNullOrEmpty($resolvedDirectory)) {
    throw "Укажите -ApiSpec или -ApiDirectory."
}

function Collect-ApiFiles {
    param([string]$Spec, [string]$Directory)
    if (-not [string]::IsNullOrWhiteSpace($Spec)) {
        return @($Spec)
    }
    $yaml = Get-ChildItem -Path $Directory -Filter "*.yaml" -File -Recurse
    $yml = Get-ChildItem -Path $Directory -Filter "*.yml" -File -Recurse
    return ($yaml + $yml | Sort-Object FullName | ForEach-Object { $_.FullName })
}

$apiFiles = Collect-ApiFiles -Spec $resolvedSpec -Directory $resolvedDirectory
$apiFiles = $apiFiles | Where-Object { $_.Replace('\', '/').ToLowerInvariant() -notlike "*/shared/*" }
if ($apiFiles.Count -eq 0) {
    throw "OpenAPI файлы не найдены."
}

function Ensure-DirectoryExists {
    param([string]$PathValue)
    if (-not (Test-Path $PathValue)) {
        New-Item -ItemType Directory -Path $PathValue -Force | Out-Null
    }
}

function Get-LineIndent {
    param([string]$Line)
    return ([regex]::Match($Line, "^\s*")).Value.Length
}

function ParseOpenApiMetadataFromLines {
    param([string[]]$Lines, [string]$FilePath)

    $metadata = @{
        name = ""
        package = ""
    }
    $infoIndent = -1
    $xMicroserviceIndent = -1

    for ($i = 0; $i -lt $Lines.Length; $i++) {
        $line = $Lines[$i]
        $trimmed = $line.Trim()
        $indent = Get-LineIndent -Line $line

        if ($trimmed -match "^\s*info\s*:") {
            $infoIndent = $indent
            $xMicroserviceIndent = -1
            continue
        }

        if ($infoIndent -ge 0 -and $indent -le $infoIndent -and $trimmed -ne "") {
            $infoIndent = -1
            $xMicroserviceIndent = -1
        }

        if ($infoIndent -ge 0 -and $trimmed -match "^\s*x-microservice\s*:") {
            $xMicroserviceIndent = $indent
            continue
        }

        if ($xMicroserviceIndent -ge 0 -and $indent -le $xMicroserviceIndent -and $trimmed -ne "") {
            $xMicroserviceIndent = -1
        }

        if ($xMicroserviceIndent -ge 0 -and $indent -gt $xMicroserviceIndent) {
            if ($trimmed -match "^\s*name\s*:\s*(.+)$" -and [string]::IsNullOrWhiteSpace($metadata.name)) {
                $metadata.name = $matches[1].Trim('"', "'"," ")
            }
            if ($trimmed -match "^\s*package\s*:\s*(.+)$" -and [string]::IsNullOrWhiteSpace($metadata.package)) {
                $metadata.package = $matches[1].Trim('"', "'"," ")
            }
            continue
        }
    }

    if ([string]::IsNullOrWhiteSpace($metadata.name)) {
        throw "В файле $FilePath отсутствует info.x-microservice.name."
    }

    if ([string]::IsNullOrWhiteSpace($metadata.package)) {
        $suffix = ($metadata.name -replace "[^A-Za-z0-9]", "")
        if ([string]::IsNullOrWhiteSpace($suffix)) {
            $suffix = "microservice"
        }
        $metadata.package = "com.necpgame.$suffix"
    }

    return $metadata
}

function Read-OpenApiContext {
    param([string]$FilePath)

    if (-not (Test-Path $FilePath)) {
        throw "OpenAPI файл не найден: $FilePath"
    }

    $rawContent = Get-Content -Path $FilePath -Raw
    if ([string]::IsNullOrWhiteSpace($rawContent)) {
        throw "OpenAPI файл пуст: $FilePath"
    }

    $metadata = $null

    $convertCmd = Get-Command -Name ConvertFrom-Yaml -ErrorAction SilentlyContinue
    if ($convertCmd) {
        try {
            $document = ConvertFrom-Yaml -Yaml $rawContent
        } catch {
            $document = $null
        }

        if ($null -ne $document) {
            if ($null -ne $document.info -and $document.info.PSObject.Properties.Name -contains "x-microservice") {
                $xMicroservice = $document.info."x-microservice"
                $metadata = @{
                    name = ""
                    package = ""
                }
                if ($xMicroservice.PSObject.Properties.Name -contains "name") {
                    $metadata.name = [string]$xMicroservice.name
                }
                if ($xMicroservice.PSObject.Properties.Name -contains "package") {
                    $metadata.package = [string]$xMicroservice.package
                }
            }
        }
    }

    if ($null -eq $metadata) {
        $lines = Get-Content -Path $FilePath
        $metadata = ParseOpenApiMetadataFromLines -Lines $lines -FilePath $FilePath
    }

    return @{ metadata = $metadata }
}

function Resolve-TargetDirectory {
    param([string]$ServiceName)
    $candidates = @(
        Join-Path $backendRoot "microservices\$ServiceName\src\main\java",
        Join-Path $backendRoot "microservices\$ServiceName\src",
        Join-Path $backendRoot "src\main\java"
    )
    foreach ($candidate in $candidates) {
        if (Test-Path $candidate) {
            return $candidate
        }
    }
    return ""
}

function Run-Generator {
    param(
        [string]$InputFile,
        [string]$OutputDir,
        [string]$ApiPackage,
        [string]$ModelPackage,
        [string]$InvokerPackage,
        [string[]]$ExtraArgs = @()
    )

    Ensure-DirectoryExists -PathValue $OutputDir

    $arguments = @(
        "generate",
        "-i", $InputFile,
        "-g", "spring",
        "-o", $OutputDir,
        "--api-package", $ApiPackage,
        "--model-package", $ModelPackage,
        "--invoker-package", $InvokerPackage,
        "--additional-properties", "dateLibrary=java8,useSpringBoot3=true"
    )

    if ($ExtraArgs.Count -gt 0) {
        $arguments += $ExtraArgs
    }

    Write-Host "    npx openapi-generator-cli $($arguments -join ' ')"
    $generatorOutput = & npx --yes @openapitools/openapi-generator-cli @arguments 2>&1

    if ($generatorOutput -and $generatorOutput.Count -gt 0) {
        $lastLines = $generatorOutput | Select-Object -Last 3
        Write-Host "    Последние строки вывода генератора:" -ForegroundColor Cyan
        $lastLines | ForEach-Object { Write-Host "      $_" -ForegroundColor Cyan }
    }

    if ($LASTEXITCODE -ne 0) {
        $errorMessage = "Генерация завершилась с ошибкой для файла $InputFile"
        if ($generatorOutput) {
            $errorMessage += "`n$($generatorOutput -join "`n")"
        }
        throw $errorMessage
    }
}

function Copy-GeneratedContent {
    param([string]$SourceRoot, [string]$TargetRoot)
    $sourceCom = Join-Path $SourceRoot "src\main\java"
    if (-not (Test-Path $sourceCom)) {
        $sourceCom = Join-Path $SourceRoot "com"
    }
    if (-not (Test-Path $sourceCom)) {
        Write-Warning "Директория с сгенерированным кодом не найдена: $SourceRoot"
        return
    }
    Ensure-DirectoryExists -PathValue $TargetRoot
    Write-Host "    Копирование из $sourceCom в $TargetRoot"
    robocopy $sourceCom $TargetRoot /E /IS /IT /NJH /NJS /NFL /NDL | Out-Null
    if ($LASTEXITCODE -ge 8) {
        throw "Ошибка копирования файлов (robocopy code $LASTEXITCODE)."
    }
}

Push-Location $backendRoot
try {
    if ($Validate) {
    $validationCandidates = @(
        Join-Path $repoRoot "services\openapi\scripts\validate-openapi.ps1"
    )
    $validationScript = $validationCandidates | Where-Object { Test-Path $_ } | Select-Object -First 1
    if (-not $validationScript) {
        throw "Скрипт валидации OpenAPI не найден."
    }
    $validationArgs = @("-NoProfile", "-File", $validationScript)
    if (-not [string]::IsNullOrEmpty($resolvedSpec)) {
        $validationArgs += @("-ApiSpec", $resolvedSpec)
    } else {
        $validationArgs += @("-ApiDirectory", $resolvedDirectory)
    }
    Write-Host "Запуск валидации OpenAPI..."
    pwsh @validationArgs
    if ($LASTEXITCODE -ne 0) {
        throw "Валидация OpenAPI завершилась с ошибками."
    }
}

Write-Host "Всего файлов для генерации: $($apiFiles.Count)"

$tasks = foreach ($file in $apiFiles) {
    $context = Read-OpenApiContext -FilePath $file
    [ordered]@{
        FilePath = $file
        Metadata = $context.metadata
    }
}

$tempRoot = Join-Path $backendRoot "target\generated-openapi"
Ensure-DirectoryExists -PathValue $tempRoot
$failures = @()

foreach ($task in $tasks) {
    $filePath = $task.FilePath
    $metadata = $task.Metadata
    $relative = [System.IO.Path]::GetRelativePath($repoRoot, $filePath)
    Write-Host "`n═══════════════════════════════════════════════════════════════"
    Write-Host "Файл: $relative"
    Write-Host "Микросервис: $($metadata.name)"

    $javaPackageRoot = $metadata.package
    $apiPackage = "$javaPackageRoot.api"
    $modelPackage = "$javaPackageRoot.model"
    $invokerPackage = "$javaPackageRoot.invoker"

    $serviceOutputDir = Join-Path $tempRoot $metadata.name
    if (Test-Path $serviceOutputDir) {
        Remove-Item -Path $serviceOutputDir -Recurse -Force
    }
    Ensure-DirectoryExists -PathValue $serviceOutputDir

    try {
        Run-Generator -InputFile $filePath -OutputDir $serviceOutputDir -ApiPackage $apiPackage -ModelPackage $modelPackage -InvokerPackage $invokerPackage
        $targetDir = Resolve-TargetDirectory -ServiceName $metadata.name
        if ([string]::IsNullOrWhiteSpace($targetDir)) {
            Write-Warning "Целевая директория для сервиса $($metadata.name) не найдена. Код оставлен в $serviceOutputDir."
        } elseif (-not $DryRun) {
            Copy-GeneratedContent -SourceRoot $serviceOutputDir -TargetRoot $targetDir
        } else {
            Write-Host "    DryRun: пропускаю копирование в $targetDir" -ForegroundColor Yellow
        }
    } catch {
        $failures += $_.Exception.Message
        Write-Host "    ✗ Ошибка: $($_.Exception.Message)" -ForegroundColor Red
    }
}

if ($failures.Count -gt 0) {
    Write-Host "`nОшибки генерации:" -ForegroundColor Red
    $failures | ForEach-Object { Write-Host " - $_" -ForegroundColor Red }
    exit 1
}

Write-Host "`nВсе спецификации обработаны успешно." -ForegroundColor Green
}
finally {
    Pop-Location
}

