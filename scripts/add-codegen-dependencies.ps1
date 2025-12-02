# Issue: Add missing dependencies for generated code
# Script: Add oapi-codegen dependencies to all services with split generation

$ErrorActionPreference = "Continue"

$services = Get-ChildItem services/*-go -Directory | Where-Object { 
    Test-Path "$($_.FullName)\pkg\api\types.gen.go" 
}

Write-Host "🔧 Adding dependencies to $($services.Count) services..." -ForegroundColor Cyan
Write-Host ""

$updated = 0

foreach ($svc in $services) {
    $serviceName = $svc.Name
    Write-Host "Updating: $serviceName" -ForegroundColor White
    
    Push-Location $svc.FullName
    
    try {
        # Add missing dependencies
        go get github.com/getkin/kin-openapi/openapi3 2>&1 | Out-Null
        go mod tidy 2>&1 | Out-Null
        
        Write-Host "  OK Dependencies added" -ForegroundColor Green
        $updated++
    }
    catch {
        Write-Host "  ❌ Failed: $_" -ForegroundColor Red
    }
    finally {
        Pop-Location
    }
}

Write-Host ""
Write-Host "OK Updated: $updated services" -ForegroundColor Green

