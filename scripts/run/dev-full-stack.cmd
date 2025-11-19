@echo off
REM Full stack development script - starts everything needed for local testing
REM Starts: Gateway, UE5 Server, and optionally client

setlocal enabledelayedexpansion

echo ========================================
echo NECPGAME Full Stack Development Setup
echo ========================================
echo.

REM Check if Docker is running
docker ps >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo WARNING: Docker is not running!
    echo Starting Docker services requires Docker to be running.
    echo.
)

echo Step 1: Starting Docker services (PostgreSQL, Redis, Keycloak, Gateway)...
echo.
cd /d %~dp0..\..
docker-compose up -d postgres redis keycloak realtime-gateway

echo.
echo Waiting for services to be ready...
timeout /t 5 /nobreak >nul 2>&1

echo.
echo Step 2: Starting UE5 Dedicated Server...
echo.
call %~dp0ue5-server.cmd

echo.
echo ========================================
echo Setup complete!
echo ========================================
echo.
echo Services running:
echo   - PostgreSQL: localhost:5432
echo   - Redis: localhost:6379
echo   - Keycloak: localhost:8080
echo   - Gateway: localhost:18080
echo   - UE5 Server: localhost:7777
echo.
echo To stop everything:
echo   docker-compose down
echo   scripts\run\ue5-server.cmd stop
echo.
echo To check server status:
echo   scripts\run\ue5-server.cmd status
echo.
pause

