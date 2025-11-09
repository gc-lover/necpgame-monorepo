#!/usr/bin/env pwsh
param(
    [string]$ApiSpec = "",
    [string]$ApiDirectory = "",
    [switch]$Validate,
    [switch]$DryRun,
    [string]$Layers = "All"
)

$ErrorActionPreference = "Stop"

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$ProjectRoot = Split-Path -Parent $ScriptDir
Set-Location $ProjectRoot

function Resolve-PathOrThrow {
    param([string]$PathValue, [string]$ParameterName)
    if ([string]::IsNullOrEmpty($PathValue)) {
        return ""
    }
    if (-not (Test-Path $PathValue)) {
        throw "Путь не найден для ${ParameterName}: $PathValue"
    }
    return (Resolve-Path $PathValue).Path
}

$resolvedSpec = Resolve-PathOrThrow -PathValue $ApiSpec -ParameterName "-ApiSpec"
$resolvedDirectory = Resolve-PathOrThrow -PathValue $ApiDirectory -ParameterName "-ApiDirectory"

if ([string]::IsNullOrEmpty($resolvedSpec) -and [string]::IsNullOrEmpty($resolvedDirectory)) {
    throw "Укажите -ApiSpec или -ApiDirectory."
}

function Collect-ApiFiles {
    param([string]$Spec, [string]$Directory)
    if (-not [string]::IsNullOrEmpty($Spec)) {
        return @($Spec)
    }
    $yaml = Get-ChildItem -Path $Directory -Filter "*.yaml" -File -Recurse
    $yml = Get-ChildItem -Path $Directory -Filter "*.yml" -File -Recurse
    return ($yaml + $yml | Sort-Object FullName | ForEach-Object { $_.FullName })
}

$ApiFiles = Collect-ApiFiles -Spec $resolvedSpec -Directory $resolvedDirectory
$ApiFiles = $ApiFiles | Where-Object {
    $_.Replace('\', '/').ToLowerInvariant() -notlike "*/api/v1/shared/*"
}
if ($ApiFiles.Count -eq 0) {
    throw "OpenAPI файлы не найдены."
}

$Microservices = @{
    "auth-service" = @{
        package = "com.necpgame.authservice"
        sourceDir = Join-Path $ProjectRoot "microservices/auth-service/src/main/java"
    }
    "character-service" = @{
        package = "com.necpgame.characterservice"
        sourceDir = Join-Path $ProjectRoot "microservices/character-service/src/main/java"
    }
    "gameplay-service" = @{
        package = "com.necpgame.gameplayservice"
        sourceDir = Join-Path $ProjectRoot "microservices/gameplay-service/src/main/java"
    }
    "social-service" = @{
        package = "com.necpgame.socialservice"
        sourceDir = Join-Path $ProjectRoot "microservices/social-service/src/main/java"
    }
    "economy-service" = @{
        package = "com.necpgame.economyservice"
        sourceDir = Join-Path $ProjectRoot "microservices/economy-service/src/main/java"
    }
    "world-service" = @{
        package = "com.necpgame.worldservice"
        sourceDir = Join-Path $ProjectRoot "microservices/world-service/src/main/java"
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
    $serverUrls = New-Object System.Collections.Generic.List[string]

    $infoIndent = -1
    $xMicroserviceIndent = -1
    $serversIndent = -1

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

        if ($trimmed -match "^\s*servers\s*:") {
            $serversIndent = $indent
            continue
        }

        if ($serversIndent -ge 0 -and $indent -le $serversIndent -and $trimmed -ne "") {
            $serversIndent = -1
        }

        if ($serversIndent -ge 0 -and $trimmed -match "^\s*-\s*url\s*:\s*(.+)$") {
            $url = $matches[1].Trim().Trim('"',"'")
            if (-not [string]::IsNullOrWhiteSpace($url)) {
                $serverUrls.Add($url)
            }
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

    return @{
        metadata = $metadata
        servers = $serverUrls
    }
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
    $servers = $null

    $convertCmd = Get-Command -Name ConvertFrom-Yaml -ErrorAction SilentlyContinue
    if ($convertCmd) {
        try {
            $document = ConvertFrom-Yaml -Yaml $rawContent
        } catch {
            $document = $null
        }

        if ($null -ne $document) {
            $metadata = @{
                name = ""
                package = ""
            }
            if ($null -ne $document.info -and $document.info.PSObject.Properties.Name -contains "x-microservice") {
                $xMicroservice = $document.info."x-microservice"
                if ($xMicroservice.PSObject.Properties.Name -contains "name") {
                    $metadata.name = [string]$xMicroservice.name
                }
                if ($xMicroservice.PSObject.Properties.Name -contains "package") {
                    $metadata.package = [string]$xMicroservice.package
                }
            }

            $servers = New-Object System.Collections.Generic.List[string]
            if ($null -ne $document.servers) {
                foreach ($server in @($document.servers)) {
                    if ($null -ne $server -and $server.PSObject.Properties.Name -contains "url" -and -not [string]::IsNullOrWhiteSpace($server.url)) {
                        $servers.Add([string]$server.url)
                    }
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
        }
    }

    if ($null -eq $metadata) {
        $lines = Get-Content -Path $FilePath
        $parsed = ParseOpenApiMetadataFromLines -Lines $lines -FilePath $FilePath
        $metadata = $parsed.metadata
        $servers = $parsed.servers
    }

    $requiredServerUrl = "https://api.necp.game/v1"
    if (-not $servers.Contains($requiredServerUrl)) {
        throw "В файле $FilePath отсутствует production URL сервера '$requiredServerUrl' в разделе servers."
    }

    return @{ metadata = $metadata }
}

function Ensure-DirectoryExists {
    param([string]$PathValue)
    if (-not (Test-Path $PathValue)) {
        New-Item -ItemType Directory -Path $PathValue -Force | Out-Null
    }
}

function Run-Generator {
    param(
        [string]$InputFile,
        [string]$OutputDir,
        [string]$ApiPackage,
        [string]$ModelPackage,
        [string]$InvokerPackage,
        [string]$TemplateDir = "",
        [string]$AdditionalProperties = "",
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
        "--invoker-package", $InvokerPackage
    )

    if (-not [string]::IsNullOrEmpty($TemplateDir)) {
        $arguments += @("-t", $TemplateDir)
    }

    if ($ExtraArgs.Count -gt 0) {
        $arguments += $ExtraArgs
    }

    if (-not [string]::IsNullOrEmpty($AdditionalProperties)) {
        $arguments += @("-p", $AdditionalProperties)
    }

    Write-Host "    npx openapi-generator-cli $($arguments -join ' ')"
    $generatorOutput = & npx --yes @openapitools/openapi-generator-cli @arguments 2>&1
    
    # Показываем последние строки вывода для диагностики
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
    $sourceCom = Join-Path $SourceRoot "com"
    if (-not (Test-Path $sourceCom)) {
        Write-Warning "Директория $sourceCom не найдена после генерации."
        Write-Host "  Содержимое ${SourceRoot}:" -ForegroundColor Yellow
        if (Test-Path $SourceRoot) {
            Get-ChildItem -Path $SourceRoot -Recurse | Select-Object -First 5 | ForEach-Object {
                Write-Host "    - $($_.FullName)"
            }
        }
        throw "Не найдена директория $sourceCom после генерации."
    }
    Ensure-DirectoryExists -PathValue $TargetRoot
    
    $targetCom = Join-Path $TargetRoot "com"
    Write-Host "    Копирование из $sourceCom в $targetCom"
    
    # Копируем содержимое директории com/* в TargetRoot/com/
    # Robocopy копирует СОДЕРЖИМОЕ первой директории во вторую
    $robocopyArgs = @($sourceCom, $targetCom, "/E", "/IS", "/IT", "/NJH", "/NJS", "/NFL", "/NDL")
    $robocopyOutput = robocopy @robocopyArgs 2>&1
    
    # Robocopy exit codes: 0=ничего не скопировано, 1=файлы скопированы, 2+=частичные проблемы, 8+=ошибки
    if ($LASTEXITCODE -ge 8) {
        Write-Host "    Robocopy output: $robocopyOutput" -ForegroundColor Red
        throw "Ошибка копирования файлов из $sourceCom в $targetCom (robocopy code $LASTEXITCODE)."
    }
    
    # Проверяем, что файлы действительно скопировались
    if (Test-Path $targetCom) {
        $copiedFiles = (Get-ChildItem -Path $targetCom -Recurse -File | Measure-Object).Count
        Write-Host "    ✓ Файлы скопированы: $copiedFiles файлов"
    } else {
        throw "Копирование не создало директорию com в $TargetRoot"
    }
}

if ($Validate) {
    $validationScript = Join-Path (Split-Path $ProjectRoot -Parent) "pipeline/scripts/validate-swagger.ps1"
    if (-not (Test-Path $validationScript)) {
        throw "Скрипт валидации не найден: $validationScript"
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

Write-Host "Всего файлов для генерации: $($ApiFiles.Count)"

$Tasks = foreach ($file in $ApiFiles) {
    $context = Read-OpenApiContext -FilePath $file
    [ordered]@{
        FilePath = $file
        Metadata = $context.metadata
    }
}

$TempRoot = Join-Path $ProjectRoot "target/openapi-microservices"
Ensure-DirectoryExists -PathValue $TempRoot

$Failures = @()

foreach ($task in $Tasks) {
    $filePath = $task.FilePath
    $metadata = $task.Metadata
    $relative = [IO.Path]::GetRelativePath($ProjectRoot, $filePath)
    Write-Host "`n═══════════════════════════════════════════════════════════════"
    Write-Host "Файл: $relative"
    Write-Host "Микросервис: $($metadata.name)"

    $javaPackageRoot = $metadata.package
    $apiPackage = "$javaPackageRoot.api"
    $modelPackage = "$javaPackageRoot.model"
    $servicePackage = "$javaPackageRoot.service"
    $invokerPackage = "$javaPackageRoot.invoker"

    if (-not $Microservices.ContainsKey($metadata.name)) {
        $defaultSourceDir = Join-Path $ProjectRoot ("microservices/$($metadata.name)/src/main/java")
        $Microservices[$metadata.name] = @{
            package = $metadata.package
            sourceDir = $defaultSourceDir
        }
    } else {
        if (-not [string]::IsNullOrWhiteSpace($metadata.package) -and $metadata.package -ne $Microservices[$metadata.name].package) {
            $Microservices[$metadata.name].package = $metadata.package
        }
        if ([string]::IsNullOrWhiteSpace($metadata.package)) {
            $metadata.package = $Microservices[$metadata.name].package
            $javaPackageRoot = $metadata.package
            $apiPackage = "$javaPackageRoot.api"
            $modelPackage = "$javaPackageRoot.model"
            $servicePackage = "$javaPackageRoot.service"
            $invokerPackage = "$javaPackageRoot.invoker"
        }
    }

    $destinationRoot = $Microservices[$metadata.name].sourceDir

    Ensure-DirectoryExists -PathValue $destinationRoot

    $fileTempRoot = Join-Path $TempRoot ([Guid]::NewGuid().ToString())
    if (-not $DryRun) {
        try {
            if ($Layers -eq "All" -or $Layers -match "DTOs") {
                Write-Host "  → Генерация DTO и API интерфейсов"
                Run-Generator -InputFile $filePath `
                    -OutputDir (Join-Path $fileTempRoot "contracts") `
                    -ApiPackage $apiPackage `
                    -ModelPackage $modelPackage `
                    -InvokerPackage $invokerPackage `
                    -AdditionalProperties "interfaceOnly=true,delegatePattern=false,useSpringBoot3=true,useJakartaEe=true,useBeanValidation=true,hideGenerationTimestamp=true,sourceFolder=."

                Copy-GeneratedContent -SourceRoot (Join-Path $fileTempRoot "contracts") -TargetRoot $destinationRoot
            }

            if ($Layers -eq "All" -or $Layers -match "Services") {
                Write-Host "  → Генерация Service интерфейсов"
                Run-Generator -InputFile $filePath `
                    -OutputDir (Join-Path $fileTempRoot "services") `
                    -ApiPackage $servicePackage `
                    -ModelPackage $modelPackage `
                    -InvokerPackage $invokerPackage `
                    -TemplateDir (Join-Path $ProjectRoot "templates") `
                    -AdditionalProperties "interfaceOnly=true,generateApis=true,generateModels=false,useSpringBoot3=true,useJakartaEe=true,hideGenerationTimestamp=true,sourceFolder=." `
                    -ExtraArgs @("--api-name-suffix", "Service")

                Copy-GeneratedContent -SourceRoot (Join-Path $fileTempRoot "services") -TargetRoot $destinationRoot
            }
        } catch {
            $Failures += [ordered]@{
                file = $relative
                error = $_.Exception.Message
            }
            Write-Host "✗ Ошибка: $($_.Exception.Message)" -ForegroundColor Red
        } finally {
            # Проверяем содержимое перед удалением для диагностики
            if (Test-Path $fileTempRoot) {
                $fileCount = (Get-ChildItem -Path $fileTempRoot -Recurse -File | Measure-Object).Count
                Write-Host "    Временная директория содержит $fileCount файлов" -ForegroundColor Cyan
                if ($fileCount -eq 0) {
                    Write-Warning "Генератор не создал файлов! Проверьте параметры генерации."
                }
                Remove-Item -Path $fileTempRoot -Recurse -Force -ErrorAction SilentlyContinue
            }
        }
    } else {
        Write-Host "  (DryRun) Пропуск генерации для $relative"
    }
}

Write-Host "`n═══════════════════════════════════════════════════════════════"
Write-Host "Генерация завершена."

if ($Failures.Count -gt 0) {
    Write-Host "Ошибки:" -ForegroundColor Red
    foreach ($failure in $Failures) {
        Write-Host "  - $($failure.file): $($failure.error)" -ForegroundColor Red
    }
    exit 1
}

exit 0

