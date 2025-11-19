@echo off
setlocal
REM Повторный запуск создания Blueprint'ов (когда редактор уже открыт)

set "SCRIPT_DIR=%~dp0"
cd /d "%SCRIPT_DIR%..\.."
set PROJ_DIR=%CD%\client\UE5\necpg
set PYTHON_SCRIPT=%PROJ_DIR%\Scripts\create_hud_blueprints_simple.py
set EDITOR_EXE=C:\Program Files\Epic Games\UE_5.6\Engine\Binaries\Win64\UnrealEditor.exe
set UPROJECT=%PROJ_DIR%\necpg.uproject

echo ========================================
echo Retry Blueprint Creation
echo ========================================
echo.
echo This will execute Python script in already running editor.
echo.
echo If editor is open, you can also:
echo 1. Press `` ` `` to open console
echo 2. Type: py Scripts/create_hud_blueprints_simple.py
echo 3. Press Enter
echo.
echo Or execute via command line:
echo.

"%EDITOR_EXE%" "%UPROJECT%" -ExecutePythonScript="%PYTHON_SCRIPT%"

echo.
echo Check Output Log in editor for results.
echo.
pause
endlocal

