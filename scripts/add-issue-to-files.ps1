# Universal script to add # Issue: #XXX to files
param(
    [Parameter(Mandatory=$true)]
    [string]$IssueNumber,
    
    [Parameter(Mandatory=$true)]
    [string]$Directory,
    
    [string]$Pattern = "*.yaml"
)

$files = Get-ChildItem -Path $Directory -Filter $Pattern -Recurse -ErrorAction SilentlyContinue

if ($null -eq $files -or $files.Count -eq 0) {
    Write-Host "No files found in $Directory"
    exit 1
}

$processed = 0
$skipped = 0

foreach ($file in $files) {
    $content = Get-Content $file.FullName -Raw -ErrorAction SilentlyContinue
    
    if ($null -eq $content) {
        $skipped++
        continue
    }
    
    # Check if file already has this issue
    if ($content -match "^# Issue: #$IssueNumber") {
        $skipped++
        continue
    }
    
    # Remove old issue if exists
    $content = $content -replace "^# Issue: #\d+\s*\r?\n", ""
    
    # Add new issue at the beginning
    $newContent = "# Issue: #$IssueNumber`n" + $content
    try {
        Set-Content -Path $file.FullName -Value $newContent -NoNewline -ErrorAction Stop
        $processed++
        Write-Host "Processed: $($file.Name)"
    } catch {
        Write-Host "Error processing $($file.Name): $_"
        $skipped++
    }
}

Write-Host "`nTotal files: $($files.Count)"
Write-Host "Processed: $processed"
Write-Host "Skipped: $skipped"





