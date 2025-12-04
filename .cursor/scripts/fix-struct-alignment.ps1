# Issue: #1606
# Автоматическое исправление struct alignment через fieldalignment (PowerShell)

Write-Host "🔧 Fixing struct alignment..." -ForegroundColor Cyan

# Найти все .go файлы, исключая generated
$files = Get-ChildItem -Path services -Filter "*.go" -Recurse | 
    Where-Object { 
        $_.FullName -notmatch "\\pkg\\api\\" -and
        $_.Name -notmatch "_gen\.go$" -and
        $_.Name -notmatch "_test\.go$"
    }

foreach ($file in $files) {
    Write-Host "Processing: $($file.FullName)" -ForegroundColor Gray
    fieldalignment -fix $file.FullName
}

Write-Host "OK Struct alignment fixed" -ForegroundColor Green
