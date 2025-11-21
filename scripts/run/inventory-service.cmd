@echo off
REM Run Inventory Service (Go)

setlocal enabledelayedexpansion

set SERVICE_DIR=services\inventory-service-go

if not exist "%SERVICE_DIR%" (
    echo Error: Service directory not found: %SERVICE_DIR%
    pause
    exit /b 1
)

cd /d "%SERVICE_DIR%"

set ADDR=0.0.0.0:8085
set METRICS_ADDR=:9090
set DATABASE_URL=postgresql://necpgame:necpgame@localhost:5432/necpgame?sslmode=disable
set REDIS_URL=redis://localhost:6379/0
set LOG_LEVEL=info

echo Starting Inventory Service (Go)...
echo Address: %ADDR%
echo Metrics: %METRICS_ADDR%
echo.

if exist "inventory-service.exe" (
    inventory-service.exe
) else (
    echo Building and running...
    go run .
)

pause
