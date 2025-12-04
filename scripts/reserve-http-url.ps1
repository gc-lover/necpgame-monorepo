# Reserve URL for HTTP server (run as Administrator)
netsh http add urlacl url=http://*:9100/ user=Everyone
Write-Host "URL reserved. Restart HTTP server now." -ForegroundColor Green
