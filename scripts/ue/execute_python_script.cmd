@echo off
setlocal
REM Запуск Python скрипта в редакторе (требует открытого редактора)

set PROJ_DIR=%~dp0..\..\client\UE5\necpg
set PYTHON_SCRIPT=%PROJ_DIR%\Scripts\create_hud_blueprints.py

echo ========================================
echo Python Script Execution
echo ========================================
echo.
echo INSTRUCTIONS:
echo.
echo Option 1: If editor is already open
echo   1. Press `` ` `` (backtick) to open console
echo   2. Type: py Scripts/create_hud_blueprints.py
echo   3. Press Enter
echo.
echo Option 2: Execute via menu
echo   1. Tools -> Python -> Execute Script
echo   2. Select: Scripts/create_hud_blueprints.py
echo   3. Click Execute
echo.
echo Option 3: Auto-execute (opens editor)
echo   Run: scripts\ue\create_hud_blueprints_auto.cmd
echo.
echo ========================================
echo Script path: %PYTHON_SCRIPT%
echo ========================================
echo.
pause
endlocal

