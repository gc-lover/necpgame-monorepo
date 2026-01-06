# PowerShell script to standardize all OpenAPI services with ETagHeader schema

$services = Get-ChildItem -Path "C:\NECPGAME\proto\openapi" -Directory -Filter "*-service" | Select-Object -ExpandProperty Name

foreach ($service in $services) {
    $mainYamlPath = "C:\NECPGAME\proto\openapi\$service\main.yaml"

    if (Test-Path $mainYamlPath) {
        Write-Host "Processing $service..."

        # Check if ETagHeader schema already exists
        $content = Get-Content $mainYamlPath -Raw
        if ($content -notmatch "ETagHeader:") {
            # Add ETagHeader schema before securitySchemes
            $replacement = "    ETagHeader:`n      type: string`n      description: Entity tag for caching and optimistic locking`n      example: `"$service-123-v1`"`n`n  securitySchemes:"
            $content = $content -replace "(.*)\n  securitySchemes:", $replacement

            # Replace inline ETag definitions with references
            $content = $content -replace "ETag:\s*\n\s*schema:\s*\n\s*type: string\s*\n\s*example:.*", "ETag:`n              schema:`n                `$ref: `"#/components/schemas/ETagHeader`"" -replace "`r`n", "`n"

            Set-Content -Path $mainYamlPath -Value $content
            Write-Host "  Standardized $service"
        } else {
            Write-Host "  Already standardized $service"
        }
    }
}

Write-Host "Standardization complete!"










