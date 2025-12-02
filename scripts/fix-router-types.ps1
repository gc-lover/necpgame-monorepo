# Issue: Fix incorrect router types in Makefiles
# Script: Auto-detect actual router and update Makefile

$ErrorActionPreference = "Continue"

$services = Get-ChildItem services/*-go -Directory | Where-Object {
    Test-Path "$($_.FullName)\Makefile"
}

Write-Host "🔧 Fixing router types for $($services.Count) services..." -ForegroundColor Cyan
Write-Host ""

$fixed = 0

foreach ($svc in $services) {
    $serviceName = $svc.Name
    $makefilePath = "$($svc.FullName)\Makefile"
    
    # Detect actual router
    $usesChi = $false
    $usesGorilla = $false
    
    $files = Get-ChildItem "$($svc.FullName)\server\*.go" -ErrorAction SilentlyContinue
    foreach ($file in $files) {
        $content = Get-Content $file.FullName -Raw -ErrorAction SilentlyContinue
        if ($content -match "github\.com/go-chi/chi") {
            $usesChi = $true
        }
        if ($content -match "github\.com/gorilla/mux") {
            $usesGorilla = $true
        }
    }
    
    # Determine correct router
    $correctRouter = if ($usesChi) { "chi-server" } elseif ($usesGorilla) { "gorilla-server" } else { "chi-server" }
    
    # Check Makefile
    $makefileContent = Get-Content $makefilePath -Raw
    if ($makefileContent -match "ROUTER_TYPE := (.+)") {
        $currentRouter = $matches[1].Trim()
        
        if ($currentRouter -ne $correctRouter) {
            Write-Host "$serviceName : $currentRouter -> $correctRouter" -ForegroundColor Yellow
            $makefileContent = $makefileContent -replace "ROUTER_TYPE := $currentRouter", "ROUTER_TYPE := $correctRouter"
            Set-Content -Path $makefilePath -Value $makefileContent -Encoding UTF8
            $fixed++
        }
    }
}

Write-Host ""
Write-Host "OK Fixed: $fixed Makefiles" -ForegroundColor Green

