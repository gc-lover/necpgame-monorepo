# Issue: #1604
# Script to add context timeouts to handlers that don't have them
# Usage: .\scripts\add-context-timeouts.ps1

$ErrorActionPreference = "Stop"

# Find all handlers.go files
$handlersFiles = Get-ChildItem -Path services -Recurse -Filter "handlers.go" | Where-Object {
    $content = Get-Content $_.FullName -Raw
    # Check if file has handlers but no DBTimeout constant
    ($content -match "func.*\(.*http\.ResponseWriter.*http\.Request" -or 
     $content -match "func.*\(.*context\.Context") -and
    -not ($content -match "DBTimeout|context\.WithTimeout")
}

Write-Host "Found $($handlersFiles.Count) handlers files without context timeouts"

foreach ($file in $handlersFiles) {
    Write-Host "Processing: $($file.FullName)"
    
    $content = Get-Content $file.FullName -Raw
    $lines = Get-Content $file.FullName
    
    # Check if context is imported
    $hasContextImport = $content -match "import\s*\([^)]*`"context`""
    $hasTimeImport = $content -match "import\s*\([^)]*`"time`""
    
    # Add imports if needed
    $modified = $false
    $newLines = @()
    $inImports = $false
    $importsEnd = -1
    
    for ($i = 0; $i -lt $lines.Count; $i++) {
        $line = $lines[$i]
        
        if ($line -match "^import\s*\(") {
            $inImports = $true
            $newLines += $line
            continue
        }
        
        if ($inImports) {
            if ($line -match "^\)") {
                $importsEnd = $i
                # Add missing imports before closing )
                if (-not $hasContextImport) {
                    $newLines += '	"context"'
                }
                if (-not $hasTimeImport) {
                    $newLines += '	"time"'
                }
                $newLines += $line
                $inImports = $false
                $modified = $true
                continue
            }
            $newLines += $line
            continue
        }
        
        # Add DBTimeout constant after package declaration
        if ($line -match "^package\s+\w+") {
            $newLines += $line
            # Check if Issue comment exists
            $nextLine = if ($i + 1 -lt $lines.Count) { $lines[$i + 1] } else { "" }
            if (-not ($nextLine -match "Issue:")) {
                $newLines += ""
                $newLines += "// Issue: #1604"
            }
            continue
        }
        
        # Add DBTimeout constant if not exists
        if ($line -match "^const\s*\(" -and -not ($content -match "DBTimeout")) {
            $newLines += $line
            # Check if DBTimeout is in this const block
            $constBlock = ""
            $j = $i
            while ($j -lt $lines.Count -and -not ($lines[$j] -match "^\)")) {
                $constBlock += $lines[$j]
                $j++
            }
            if (-not ($constBlock -match "DBTimeout")) {
                # Add DBTimeout before closing )
                $newLines += "	DBTimeout = 50 * time.Millisecond"
            }
            continue
        }
        
        # Add context timeout to handler functions
        if ($line -match "func.*\(.*http\.ResponseWriter.*http\.Request") {
            $newLines += $line
            # Get function body
            $funcBody = ""
            $braceCount = 0
            $j = $i + 1
            while ($j -lt $lines.Count) {
                $funcLine = $lines[$j]
                if ($funcLine -match "\{") { $braceCount++ }
                if ($funcLine -match "\}") { $braceCount-- }
                $funcBody += $funcLine
                if ($braceCount -eq 0 -and $funcLine -match "\}") { break }
                $j++
            }
            
            # Check if context timeout already exists in function
            if (-not ($funcBody -match "context\.WithTimeout")) {
                # Find first line of function body (after {)
                $bodyStart = $i + 1
                while ($bodyStart -lt $lines.Count -and $lines[$bodyStart].Trim() -eq "") {
                    $bodyStart++
                }
                # Insert context timeout after opening brace
                $newLines += "	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)"
                $newLines += "	defer cancel()"
                $newLines += "	_ = ctx // Will be used when DB operations are implemented"
                $modified = $true
            }
            continue
        }
        
        $newLines += $line
    }
    
    if ($modified) {
        # Backup original file
        Copy-Item $file.FullName "$($file.FullName).backup"
        # Write modified content
        $newLines | Set-Content $file.FullName
        Write-Host "  ✓ Modified: $($file.FullName)"
    } else {
        Write-Host "  - Skipped (already has timeouts or no handlers found): $($file.FullName)"
    }
}

Write-Host "Done!"

