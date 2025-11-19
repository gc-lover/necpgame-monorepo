@echo off
setlocal
REM Проверка статуса Blueprint файлов

set PROJ_DIR=%~dp0..\..\client\UE5\necpg
set CONTENT_DIR=%PROJ_DIR%\Content

echo ========================================
echo Blueprint Files Status Check
echo ========================================
echo.

echo Checking for Blueprint files...
echo.

if exist "%CONTENT_DIR%\UI\WBP_TechDemoHUD.uasset" (
    echo [OK] WBP_TechDemoHUD.uasset exists
) else (
    echo [MISSING] WBP_TechDemoHUD.uasset
)

if exist "%CONTENT_DIR%\Blueprints\BP_TechDemoHUD.uasset" (
    echo [OK] BP_TechDemoHUD.uasset exists
) else (
    echo [MISSING] BP_TechDemoHUD.uasset
)

if exist "%CONTENT_DIR%\Blueprints\BP_TechDemoGameMode.uasset" (
    echo [OK] BP_TechDemoGameMode.uasset exists
) else (
    echo [MISSING] BP_TechDemoGameMode.uasset
)

echo.
echo ========================================
echo To create Blueprints:
echo 1. Open editor
echo 2. Press `` ` `` to open console
echo 3. Type: py Scripts/create_hud_blueprints_simple.py
echo ========================================
echo.
pause
endlocal

