@echo off
setlocal
REM Скрипт для автоматического создания Blueprint файлов через Python
REM Требует: скомпилированный проект и открытый редактор

set PROJ_DIR=%~dp0..\..\client\UE5\necpg
set PYTHON_SCRIPT=%PROJ_DIR%\Scripts\create_hud_blueprints.py
set EDITOR_EXE=%~1

if "%EDITOR_EXE%"=="" set EDITOR_EXE=C:\Program Files\Epic Games\UE_5.6\Engine\Binaries\Win64\UnrealEditor.exe

if not exist "%PYTHON_SCRIPT%" (
    echo Python script not found: %PYTHON_SCRIPT%
    exit /b 1
)

if not exist "%EDITOR_EXE%" (
    echo UnrealEditor.exe not found: %EDITOR_EXE%
    echo Usage: create_hud_blueprints.cmd [EDITOR_PATH]
    exit /b 1
)

set UPROJECT=%PROJ_DIR%\necpg.uproject

echo ========================================
echo Creating HUD Blueprints via Python...
echo ========================================
echo.
echo This will open Unreal Editor and execute Python script.
echo Make sure project is compiled first!
echo.
pause

echo Opening editor and executing script...
echo.
echo NOTE: Editor will open and execute Python script automatically.
echo Check Output Log in editor for results.
echo.
echo Alternative: Open editor manually, then in console (`` ` ``) type:
echo   py Scripts/create_hud_blueprints.py
echo.
pause

"%EDITOR_EXE%" "%UPROJECT%" -ExecutePythonScript="%PYTHON_SCRIPT%"

echo.
echo ========================================
echo Script execution completed!
echo ========================================
echo.
echo Check Output Log in editor for results.
echo.
pause
endlocal

