@echo off
REM Запуск UE5 Editor в режиме Dedicated Server для тестирования

setlocal enabledelayedexpansion

set UE_ROOT=C:\Program Files\Epic Games\UE_5.7\Engine
set PROJECT_PATH=%~dp0NECPGAME.uproject
set MAP=/Game/ShooterMaps/Maps/L_Expanse.L_Expanse

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

echo Starting UE5 Editor in Dedicated Server mode...
echo Project: %PROJECT_PATH%
echo Map: %MAP%
echo.

REM Запуск редактора с параметрами для Dedicated Server
"%UE_ROOT%\Binaries\Win64\UnrealEditor.exe" "%PROJECT_PATH%" "%MAP%?listen -server -game -log" -WebSocketGateway=127.0.0.1:18080

endlocal

