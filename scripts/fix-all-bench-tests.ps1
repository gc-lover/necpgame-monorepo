# Mass fix for benchmark tests
# Fixes common patterns in handlers_bench_test.go files

$servicesPath = "services"
$fixedCount = 0

Get-ChildItem -Path $servicesPath -Directory | ForEach-Object {
    $service = $_.Name
    $benchFile = Join-Path $servicesPath "$service/server/handlers_bench_test.go"
    
    if (Test-Path $benchFile) {
        $content = Get-Content $benchFile -Raw
        $originalContent = $content
        $needsFix = $false
        
        # Fix 1: Replace Ој with μ in comments
        if ($content -match "Ој") {
            $content = $content -replace "Ој", "μ"
            $needsFix = $true
        }
        
        # Fix 2: Fix NewService(nil) -> NewHandlers() for services without service
        if ($content -match "NewService\(nil\)" -and $content -notmatch "func NewService") {
            # Check if service file exists
            $serviceFile = Get-ChildItem -Path (Join-Path $servicesPath "$service/server") -Filter "*service.go" | Select-Object -First 1
            if (-not $serviceFile) {
                $content = $content -replace "service := NewService\(nil\)\s+handlers := NewHandlers\(service\)", "handlers := NewHandlers()"
                $needsFix = $true
            }
        }
        
        if ($needsFix) {
            Set-Content -Path $benchFile -Value $content -NoNewline
            $fixedCount++
            Write-Host "Fixed $service"
        }
    }
}

Write-Host "`nFixed $fixedCount services"

