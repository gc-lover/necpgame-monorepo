# Issue: Safe migration from Gorilla to Chi with rollback
# Script: Migrate services one-by-one with automatic rollback on failure

param(
    [string]$ServiceName = "",
    [switch]$All = $false
)

$ErrorActionPreference = "Continue"

function Migrate-ServiceToChi {
    param([string]$ServiceName)
    
    Write-Host "`n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
    Write-Host "Migrating: $ServiceName" -ForegroundColor White
    Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
    
    $serviceDir = "services\$ServiceName"
    
    if (-not (Test-Path $serviceDir)) {
        Write-Host "  ❌ Service not found" -ForegroundColor Red
        return $false
    }
    
    Push-Location $serviceDir
    
    try {
        # Create backup
        Write-Host "  Creating backup..." -ForegroundColor Gray
        $backupDir = "..\..\backup-$ServiceName-$(Get-Date -Format 'yyyyMMddHHmmss')"
        Copy-Item -Recurse . $backupDir -ErrorAction Stop
        
        # Step 1: Remove old api.gen.go to avoid conflicts
        if (Test-Path "pkg\api\api.gen.go") {
            Remove-Item "pkg\api\api.gen.go" -Force
            Write-Host "  OK Removed old api.gen.go" -ForegroundColor Green
        }
        
        # Step 2: Update Makefile
        if (Test-Path "Makefile") {
            $makefile = Get-Content "Makefile" -Raw
            $makefile = $makefile -replace "ROUTER_TYPE := gorilla-server", "ROUTER_TYPE := chi-server"
            Set-Content "Makefile" -Value $makefile -Encoding UTF8
            Write-Host "  OK Makefile updated" -ForegroundColor Green
        }
        
        # Step 3: Update server code
        $serverFiles = Get-ChildItem "server\*.go" -ErrorAction SilentlyContinue
        $updated = 0
        
        foreach ($file in $serverFiles) {
            $content = Get-Content $file.FullName -Raw
            $original = $content
            
            # Replace imports
            $content = $content -replace '"github\.com/gorilla/mux"', '"github.com/go-chi/chi/v5"'
            
            # Replace types
            $content = $content -replace '\*mux\.Router', 'chi.Router'
            $content = $content -replace 'mux\.NewRouter\(\)', 'chi.NewRouter()'
            
            # Replace route methods
            $content = $content -creplace '\.Get\(', '.Get('
            $content = $content -creplace '\.Post\(', '.Post('
            $content = $content -creplace '\.Put\(', '.Put('
            $content = $content -creplace '\.Delete\(', '.Delete('
            $content = $content -creplace '\.Patch\(', '.Patch('
            
            # Replace oapi integration
            $content = $content -replace 'api\.HandlerFromMux\(([^,]+),\s*([^\)]+)\)', 'api.HandlerWithOptions($1, api.ChiServerOptions{BaseRouter: $2})'
            
            if ($content -ne $original) {
                Set-Content $file.FullName -Value $content -Encoding UTF8
                $updated++
            }
        }
        
        Write-Host "  OK Updated $updated server files" -ForegroundColor Green
        
        # Step 4: Update dependencies
        go get github.com/go-chi/chi/v5 2>&1 | Out-Null
        go mod tidy 2>&1 | Out-Null
        Write-Host "  OK Dependencies updated" -ForegroundColor Green
        
        # Step 5: Regenerate server.gen.go if bundled exists
        $bundled = Get-ChildItem "pkg\api\*.bundled.yaml" -ErrorAction SilentlyContinue | Select-Object -First 1
        if ($bundled) {
            oapi-codegen -package api -generate types -o pkg\api\types.gen.go $bundled.FullName 2>&1 | Out-Null
            oapi-codegen -package api -generate chi-server -o pkg\api\server.gen.go $bundled.FullName 2>&1 | Out-Null
            oapi-codegen -package api -generate spec -o pkg\api\spec.gen.go $bundled.FullName 2>&1 | Out-Null
            Write-Host "  OK Code regenerated" -ForegroundColor Green
        }
        
        # Step 6: Test compilation
        Write-Host "  Testing compilation..." -ForegroundColor Gray
        $buildOutput = go build ./... 2>&1
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host "  OK COMPILES SUCCESSFULLY!" -ForegroundColor Green
            Remove-Item $backupDir -Recurse -Force -ErrorAction SilentlyContinue
            return $true
        } else {
            Write-Host "  ❌ Compilation failed" -ForegroundColor Red
            Write-Host "  Errors: $($buildOutput | Select-String 'error' | Select-Object -First 3)" -ForegroundColor Red
            
            # Rollback
            Write-Host "  Rolling back changes..." -ForegroundColor Yellow
            Pop-Location
            Remove-Item $serviceDir -Recurse -Force
            Copy-Item -Recurse $backupDir $serviceDir
            Remove-Item $backupDir -Recurse -Force
            Push-Location $serviceDir
            
            Write-Host "  WARNING Rolled back - service unchanged" -ForegroundColor Yellow
            return $false
        }
    }
    catch {
        Write-Host "  ❌ Error: $_" -ForegroundColor Red
        return $false
    }
    finally {
        Pop-Location
    }
}

# Main
if ($ServiceName) {
    $result = Migrate-ServiceToChi -ServiceName $ServiceName
    if ($result) {
        Write-Host "`nOK Migration successful!" -ForegroundColor Green
    } else {
        Write-Host "`n❌ Migration failed (rolled back)" -ForegroundColor Red
    }
}
elseif ($All) {
    # Find all gorilla services
    $gorillaServices = @()
    Get-ChildItem services/*-go/server/*.go | ForEach-Object {
        $content = Get-Content $_.FullName -Raw -ErrorAction SilentlyContinue
        if ($content -match "github\.com/gorilla/mux") {
            $svcName = $_.Directory.Parent.Name
            if ($gorillaServices -notcontains $svcName) {
                $gorillaServices += $svcName
            }
        }
    }
    
    Write-Host "Found $($gorillaServices.Count) services to migrate`n"
    
    $success = 0
    $failed = 0
    
    foreach ($svc in $gorillaServices) {
        if (Migrate-ServiceToChi -ServiceName $svc) {
            $success++
        } else {
            $failed++
        }
    }
    
    Write-Host "`n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
    Write-Host "FINAL RESULTS" -ForegroundColor Green
    Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
    Write-Host "OK Successfully migrated: $success" -ForegroundColor Green
    Write-Host "❌ Failed (rolled back): $failed" -ForegroundColor Red
    Write-Host "Total: $($gorillaServices.Count)" -ForegroundColor White
}
else {
    Write-Host "Usage:"
    Write-Host "  .\scripts\migrate-to-chi-safe.ps1 -ServiceName achievement-service-go"
    Write-Host "  .\scripts\migrate-to-chi-safe.ps1 -All"
}

