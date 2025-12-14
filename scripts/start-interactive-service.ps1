# Temporary script to start interactive-objects-service with proper environment

$env:DATABASE_URL = "postgres://postgres:password@localhost:5432/necp_game"
$env:REDIS_URL = "redis://localhost:6379"
$env:PORT = "8083"

cd "$PSScriptRoot\..\services\interactive-objects-service-go"
Write-Host "Starting Interactive Objects Service..."
Write-Host "DATABASE_URL: $env:DATABASE_URL"
Write-Host "REDIS_URL: $env:REDIS_URL"
Write-Host "PORT: $env:PORT"

./interactive-objects-service