@echo off
REM Build UE5 Dedicated Server using UAT (Unreal Automation Tool)

setlocal enabledelayedexpansion

set UE_ROOT=C:\Program Files\Epic Games\UE_5.7\Engine
set PROJECT_PATH=C:\NECPGAME\client\UE5\NECPGAME\NECPGAME.uproject
set OUTPUT_DIR=C:\NECPGAME\builds\server

if not exist "%UE_ROOT%" (
    echo Error: Unreal Engine 5.7 not found at %UE_ROOT%
    pause
    exit /b 1
)

if not exist "%PROJECT_PATH%" (
    echo Error: Project file not found at %PROJECT_PATH%
    pause
    exit /b 1
)

echo Building UE5 Dedicated Server using UAT...
echo Project: %PROJECT_PATH%
echo Output: %OUTPUT_DIR%
echo.

REM Create output directory if it doesn't exist
if not exist "%OUTPUT_DIR%" mkdir "%OUTPUT_DIR%"

REM Build server using UAT
"%UE_ROOT%\Build\BatchFiles\RunUAT.bat" BuildCookRun ^
    -project="%PROJECT_PATH%" ^
    -platform=Win64 ^
    -clientconfig=Development ^
    -serverconfig=Development ^
    -server ^
    -build ^
    -skipcook ^
    -stage ^
    -archivedirectory="%OUTPUT_DIR%" ^
    -noclient

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Build successful!
    echo Server executable should be in: %OUTPUT_DIR%
) else (
    echo.
    echo Build failed with error code %ERRORLEVEL%
    pause
    exit /b %ERRORLEVEL%
)

endlocal

