# Analyze services for oapi-codegen migration
# Checks: oapi-codegen.yaml presence, HandlerFromMux usage, OpenAPI specs availability

$services = @()
$baseDir = "C:\NECPGAME"
$servicesDir = Join-Path $baseDir "services"
$openapiDir = Join-Path $baseDir "proto\openapi"

# Get all services
$serviceDirs = Get-ChildItem -Path $servicesDir -Directory | Where-Object { $_.Name -match "-service-go$|^-go$" }

foreach ($serviceDir in $serviceDirs) {
    $serviceName = $serviceDir.Name
    $servicePath = $serviceDir.FullName
    
    $hasOapiCodegen = Test-Path (Join-Path $servicePath "oapi-codegen.yaml")
    $hasHandlerFromMux = $false
    $hasOpenAPISpec = $false
    $openAPISpecs = @()
    
    # Check HandlerFromMux usage
    $goFiles = Get-ChildItem -Path $servicePath -Recurse -Filter "*.go" -ErrorAction SilentlyContinue
    foreach ($file in $goFiles) {
        $content = Get-Content $file.FullName -Raw -ErrorAction SilentlyContinue
        if ($content -match "HandlerFromMux|HandlerWithOptions") {
            $hasHandlerFromMux = $true
            break
        }
    }
    
    # Find OpenAPI specs for this service
    $baseServiceName = $serviceName -replace "-service-go$", "" -replace "-go$", ""
    
    $specFiles = Get-ChildItem -Path $openapiDir -Filter "*.yaml" -ErrorAction SilentlyContinue | 
        Where-Object { $_.Name -match "^$baseServiceName" -or $_.Name -match "-$baseServiceName-" }
    
    if ($specFiles.Count -gt 0) {
        $hasOpenAPISpec = $true
        $openAPISpecs = $specFiles | ForEach-Object { $_.Name }
    }
    
    $services += [PSCustomObject]@{
        ServiceName = $serviceName
        HasOapiCodegen = $hasOapiCodegen
        UsesHandlerFromMux = $hasHandlerFromMux
        HasOpenAPISpec = $hasOpenAPISpec
        OpenAPISpecs = ($openAPISpecs -join ", ")
        NeedsMigration = ($hasOapiCodegen -and -not $hasHandlerFromMux -and $hasOpenAPISpec)
        NeedsSpec = (-not $hasOpenAPISpec)
    }
}

# Output results
Write-Host "`n=== Services requiring oapi-codegen migration ===" -ForegroundColor Yellow
$services | Where-Object { $_.NeedsMigration } | Format-Table -AutoSize

Write-Host "`n=== Services without OpenAPI specs ===" -ForegroundColor Red
$services | Where-Object { $_.NeedsSpec } | Format-Table -AutoSize

Write-Host "`n=== All services summary ===" -ForegroundColor Cyan
$services | Format-Table -AutoSize

# Save to JSON
$services | ConvertTo-Json -Depth 3 | Out-File (Join-Path $baseDir "services-analysis.json") -Encoding UTF8
Write-Host "`nResults saved to services-analysis.json" -ForegroundColor Green
