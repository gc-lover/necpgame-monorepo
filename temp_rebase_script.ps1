@'
param($filePath)
$content = Get-Content $filePath
$content = $content -replace '^pick 678351835a', 'drop 678351835a'
Set-Content $filePath $content
'@