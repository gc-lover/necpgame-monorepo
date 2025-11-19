@echo off
setlocal
REM Автоматическая версия без пауз для создания Blueprint файлов

set PROJ_DIR=%~dp0..\..\client\UE5\necpg
set PYTHON_SCRIPT=%PROJ_DIR%\Scripts\create_hud_blueprints.py
set EDITOR_EXE=C:\Program Files\Epic Games\UE_5.6\Engine\Binaries\Win64\UnrealEditor.exe

if not exist "%PYTHON_SCRIPT%" (
    echo ERROR: Python script not found: %PYTHON_SCRIPT%
    exit /b 1
)

if not exist "%EDITOR_EXE%" (
    echo ERROR: UnrealEditor.exe not found: %EDITOR_EXE%
    echo Please provide editor path as parameter or set default path
    exit /b 1
)

set UPROJECT=%PROJ_DIR%\necpg.uproject

echo ========================================
echo Creating HUD Blueprints via Python...
echo ========================================
echo.
echo Opening editor and executing Python script...
echo This may take a moment...
echo.

"%EDITOR_EXE%" "%UPROJECT%" -ExecutePythonScript="%PYTHON_SCRIPT%" -unattended -nologtimes -NoSplash -NullRHI

echo.
echo ========================================
echo Script execution completed!
echo ========================================
echo.
echo Check Output Log in editor or Saved\Logs\necpg.log for results.
echo.

endlocal

