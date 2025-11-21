# Apply database migrations

Write-Host "Applying database migrations..." -ForegroundColor Cyan

$migrations = @(
    "infrastructure/liquibase/migrations/V1_6__inventory_tables.sql",
    "infrastructure/liquibase/migrations/V1_7__inventory_seed_data.sql",
    "infrastructure/liquibase/migrations/V1_8__character_positions.sql"
)

foreach ($migration in $migrations) {
    Write-Host "Applying $migration..." -ForegroundColor Yellow
    Get-Content $migration | docker exec -i necpgame-postgres-1 psql -U postgres -d necpgame 2>&1 | Out-Null
    if ($LASTEXITCODE -eq 0) {
        Write-Host "  ✓ Success" -ForegroundColor Green
    } else {
        Write-Host "  ✗ Failed" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "Checking tables..." -ForegroundColor Cyan
docker exec necpgame-postgres-1 psql -U postgres -d necpgame -c "\dt mvp_core.*" 2>&1

