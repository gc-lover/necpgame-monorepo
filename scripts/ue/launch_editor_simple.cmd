@echo off
setlocal
REM Простой запуск редактора БЕЗ автоматического выполнения скриптов

set "SCRIPT_DIR=%~dp0"
cd /d "%SCRIPT_DIR%..\.."
set PROJ_DIR=%CD%\client\UE5\necpg
set EDITOR_EXE=C:\Program Files\Epic Games\UE_5.6\Engine\Binaries\Win64\UnrealEditor.exe
set UPROJECT=%PROJ_DIR%\necpg.uproject

if not exist "%EDITOR_EXE%" (
    echo ERROR: UnrealEditor.exe not found: %EDITOR_EXE%
    exit /b 1
)

if not exist "%UPROJECT%" (
    echo ERROR: Project not found: %UPROJECT%
    exit /b 1
)

echo ========================================
echo Launching Unreal Editor (Simple)
echo ========================================
echo.
echo Project: %UPROJECT%
echo.
echo Editor will open normally without auto-executing scripts.
echo.
echo ========================================

start "" "%EDITOR_EXE%" "%UPROJECT%"

echo.
echo Editor launched!
echo.
pause
endlocal

