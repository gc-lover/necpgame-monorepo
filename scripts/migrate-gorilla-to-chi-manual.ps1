# Issue: Manual migration from Gorilla to Chi router
# Script: Systematically migrate all services from gorilla/mux to go-chi/chi

$ErrorActionPreference = "Continue"

Write-Host "`n🔄 Manual Migration: Gorilla -> Chi" -ForegroundColor Cyan
Write-Host "===================================`n"

# Find all services with gorilla in server code
$gorillaServices = @()
Get-ChildItem services/*-go/server/*.go -ErrorAction SilentlyContinue | ForEach-Object {
    $content = Get-Content $_.FullName -Raw -ErrorAction SilentlyContinue
    if ($content -match "github\.com/gorilla/mux") {
        $serviceName = $_.Directory.Parent.Name
        if ($gorillaServices -notcontains $serviceName) {
            $gorillaServices += $serviceName
        }
    }
}

Write-Host "Found $($gorillaServices.Count) services with gorilla/mux`n" -ForegroundColor Yellow

$migrated = 0
$failed = 0

foreach ($svc in $gorillaServices) {
    Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
    Write-Host "Migrating: $svc" -ForegroundColor White
    Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
    
    $serviceDir = "services\$svc"
    $serverFiles = Get-ChildItem "$serviceDir\server\*.go" -ErrorAction SilentlyContinue
    
    if ($serverFiles.Count -eq 0) {
        Write-Host "  WARNING No server files found" -ForegroundColor Yellow
        continue
    }
    
    $filesUpdated = 0
    
    foreach ($file in $serverFiles) {
        $content = Get-Content $file.FullName -Raw
        $originalContent = $content
        
        # Step 1: Replace imports
        $content = $content -replace '"github\.com/gorilla/mux"', '"github.com/go-chi/chi/v5"'
        
        # Step 2: Replace mux references with chi
        $content = $content -replace '\*mux\.Router', 'chi.Router'
        $content = $content -replace 'mux\.NewRouter\(\)', 'chi.NewRouter()'
        
        # Step 3: Replace PathPrefix with chi Route
        # Pattern: router.PathPrefix("/api").Subrouter()
        # Becomes: router (chi doesn't need this pattern)
        $content = $content -replace '(\w+)\.PathPrefix\("[^"]+"\)\.Subrouter\(\)', '$1'
        
        # Step 4: Replace HandleFunc().Methods() with chi methods
        # GET -> router.Get()
        $content = $content -creplace '\.HandleFunc\(([^,]+),\s*([^\)]+)\)\.Methods\("GET"\)', '.Get($1, $2)'
        # POST -> router.Post()
        $content = $content -creplace '\.HandleFunc\(([^,]+),\s*([^\)]+)\)\.Methods\("POST"\)', '.Post($1, $2)'
        # PUT -> router.Put()
        $content = $content -creplace '\.HandleFunc\(([^,]+),\s*([^\)]+)\)\.Methods\("PUT"\)', '.Put($1, $2)'
        # DELETE -> router.Delete()
        $content = $content -creplace '\.HandleFunc\(([^,]+),\s*([^\)]+)\)\.Methods\("DELETE"\)', '.Delete($1, $2)'
        # PATCH -> router.Patch()
        $content = $content -creplace '\.HandleFunc\(([^,]+),\s*([^\)]+)\)\.Methods\("PATCH"\)', '.Patch($1, $2)'
        
        # Step 5: Replace HandlerFromMux with HandlerWithOptions
        $content = $content -replace 'api\.HandlerFromMux\(([^,]+),\s*([^\)]+)\)', 'api.HandlerWithOptions($1, api.ChiServerOptions{BaseRouter: $2})'
        
        if ($content -ne $originalContent) {
            Set-Content -Path $file.FullName -Value $content -Encoding UTF8
            Write-Host "  OK Updated: $($file.Name)" -ForegroundColor Green
            $filesUpdated++
        }
    }
    
    if ($filesUpdated -gt 0) {
        Write-Host "  Files updated: $filesUpdated" -ForegroundColor Cyan
        
        # Update Makefile
        $makefilePath = "$serviceDir\Makefile"
        if (Test-Path $makefilePath) {
            $makefileContent = Get-Content $makefilePath -Raw
            $makefileContent = $makefileContent -replace "ROUTER_TYPE := gorilla-server", "ROUTER_TYPE := chi-server"
            Set-Content -Path $makefilePath -Value $makefileContent -Encoding UTF8
            Write-Host "  OK Makefile updated to chi-server" -ForegroundColor Green
        }
        
        # Update go.mod
        Push-Location $serviceDir
        go get github.com/go-chi/chi/v5 2>&1 | Out-Null
        go mod tidy 2>&1 | Out-Null
        Pop-Location
        Write-Host "  OK Dependencies updated" -ForegroundColor Green
        
        # Regenerate server.gen.go if bundled spec exists
        $bundled = Get-ChildItem "$serviceDir\pkg\api\*.bundled.yaml" -ErrorAction SilentlyContinue | Select-Object -First 1
        if ($bundled) {
            Push-Location $serviceDir
            oapi-codegen -package api -generate chi-server -o pkg\api\server.gen.go $bundled.FullName 2>&1 | Out-Null
            Pop-Location
            Write-Host "  OK server.gen.go regenerated with chi" -ForegroundColor Green
        }
        
        # Test compilation
        Push-Location $serviceDir
        $buildResult = go build ./... 2>&1
        Pop-Location
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host "  OK Compiles successfully!" -ForegroundColor Green
            $migrated++
        } else {
            Write-Host "  ❌ Compilation failed" -ForegroundColor Red
            Write-Host "  Error: $($buildResult | Select-String 'error' | Select-Object -First 2)" -ForegroundColor Red
            $failed++
        }
    } else {
        Write-Host "  ℹ️  No changes needed" -ForegroundColor Gray
    }
    
    Write-Host ""
}

Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
Write-Host "MIGRATION SUMMARY" -ForegroundColor Green
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
Write-Host "Total services found: $($gorillaServices.Count)"
Write-Host "OK Successfully migrated: $migrated" -ForegroundColor Green
if ($failed -gt 0) {
    Write-Host "❌ Failed: $failed" -ForegroundColor Red
}
Write-Host ""

