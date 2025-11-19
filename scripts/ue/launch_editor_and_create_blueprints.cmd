@echo off
setlocal
REM Запуск редактора и автоматическое создание Blueprint'ов

set "SCRIPT_DIR=%~dp0"
cd /d "%SCRIPT_DIR%..\.."
set PROJ_DIR=%CD%\client\UE5\necpg
set PYTHON_SCRIPT=%PROJ_DIR%\Scripts\create_hud_blueprints_simple.py
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

if not exist "%PYTHON_SCRIPT%" (
    echo ERROR: Python script not found: %PYTHON_SCRIPT%
    exit /b 1
)

echo ========================================
echo Launching Unreal Editor...
echo ========================================
echo.
echo Project: %UPROJECT%
echo Python Script: %PYTHON_SCRIPT%
echo.
echo NOTE: Editor will open and execute Python script.
echo Wait for project to fully load before script runs.
echo.
echo ========================================

start "" "%EDITOR_EXE%" "%UPROJECT%" -ExecutePythonScript="%PYTHON_SCRIPT%"

echo.
echo Editor launched! Check Output Log for results.
echo.
pause
endlocal

