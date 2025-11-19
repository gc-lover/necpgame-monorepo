@echo off
REM Script to start/stop UE5 Dedicated Server

setlocal enabledelayedexpansion

REM Get project root (go up from scripts/run to project root)
cd /d %~dp0..\..
set PROJECT_ROOT=%CD%
set SERVER_EXE=%PROJECT_ROOT%\client\UE5\NECPGAME\Binaries\Win64\NECPGAMEServer.exe
set MAP=/Game/ShooterMaps/Maps/L_Expanse.L_Expanse
set PORT=7777
set GATEWAY_ADDRESS=127.0.0.1
set GATEWAY_PORT=18080
set MAX_PLAYERS=64
set PID_FILE=%~dp0ue5-server.pid

if "%1"=="stop" goto :stop
if "%1"=="status" goto :status

REM Check if server is already running
if exist "%PID_FILE%" (
    for /f %%i in (%PID_FILE%) do (
        tasklist /FI "PID eq %%i" 2>NUL | find /I /N "NECPGAMEServer.exe">NUL
        if "!ERRORLEVEL!"=="0" (
            echo UE5 Server is already running (PID: %%i)
            echo Use: %~nx0 stop
            exit /b 1
        )
    )
)

if not exist "%SERVER_EXE%" (
    echo Error: Server executable not found at %SERVER_EXE%
    echo.
    echo The server needs to be built first.
    echo.
    echo To build the server:
    echo   1. Open UE5 Editor
    echo   2. Or run BuildServer.bat from client\UE5\NECPGAME folder
    echo.
    echo For development, you can use PIE Play In Editor:
    echo   - Open UE5 Editor
    echo   - Click Play button
    echo   - Select "Number of Players" to test multiplayer
    echo.
    pause
    exit /b 1
)

echo Starting UE5 Dedicated Server...
echo Map: %MAP%
echo Port: %PORT%
echo Gateway: %GATEWAY_ADDRESS%:%GATEWAY_PORT%
echo Max Players: %MAX_PLAYERS%
echo.

REM Start server in background
start /B "" "%SERVER_EXE%" %MAP%?listen -server -port=%PORT% -WebSocketGateway=%GATEWAY_ADDRESS%:%GATEWAY_PORT% -MaxPlayers=%MAX_PLAYERS% -log

REM Wait a bit and get PID
timeout /t 2 /nobreak >nul 2>&1
for /f "tokens=2" %%i in ('tasklist /FI "IMAGENAME eq NECPGAMEServer.exe" /FO CSV ^| find "NECPGAMEServer.exe"') do (
    set PID=%%i
    set PID=!PID:"=!
    echo !PID! > "%PID_FILE%"
    echo Server started with PID: !PID!
    echo PID saved to: %PID_FILE%
    echo.
    echo To stop server: %~nx0 stop
    exit /b 0
)

echo Warning: Could not detect server PID
echo Server may have failed to start. Check logs.

exit /b 0

:stop
if not exist "%PID_FILE%" (
    echo Server PID file not found. Server may not be running.
    exit /b 1
)

for /f %%i in (%PID_FILE%) do (
    echo Stopping UE5 Server (PID: %%i)...
    taskkill /PID %%i /F >nul 2>&1
    if !ERRORLEVEL! EQU 0 (
        echo Server stopped successfully
        del "%PID_FILE%"
    ) else (
        echo Failed to stop server. Process may have already exited.
        del "%PID_FILE%"
    )
)
exit /b 0

:status
if not exist "%PID_FILE%" (
    echo Server is not running (no PID file)
    exit /b 1
)

for /f %%i in (%PID_FILE%) do (
    tasklist /FI "PID eq %%i" 2>NUL | find /I /N "NECPGAMEServer.exe">NUL
    if "!ERRORLEVEL!"=="0" (
        echo Server is running (PID: %%i)
    ) else (
        echo Server PID file exists but process is not running
        del "%PID_FILE%"
    )
)
exit /b 0

