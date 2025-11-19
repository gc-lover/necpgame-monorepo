@echo off
setlocal
REM Скрипт для проверки готовности к настройке HUD после компиляции
REM Запускать после успешной компиляции проекта

echo ========================================
echo Tech Demo HUD - Проверка готовности
echo ========================================
echo.

set PROJ_DIR=%~dp0..\..\client\UE5\necpg
set UI_DIR=%PROJ_DIR%\Source\necpg\UI

echo Проверка C++ классов...
if exist "%UI_DIR%\TechDemoHUD.h" (
    echo [OK] TechDemoHUD.h найден
) else (
    echo [ERROR] TechDemoHUD.h не найден!
    exit /b 1
)

if exist "%UI_DIR%\TechDemoHUDWidget.h" (
    echo [OK] TechDemoHUDWidget.h найден
) else (
    echo [ERROR] TechDemoHUDWidget.h не найден!
    exit /b 1
)

if exist "%UI_DIR%\TechDemoPlayerController.h" (
    echo [OK] TechDemoPlayerController.h найден
) else (
    echo [ERROR] TechDemoPlayerController.h не найден!
    exit /b 1
)

echo.
echo Проверка скомпилированных файлов...
set BIN_DIR=%PROJ_DIR%\Binaries\Win64
if exist "%BIN_DIR%\UnrealEditor-necpg.dll" (
    echo [OK] Проект скомпилирован
) else (
    echo [WARNING] DLL не найден - возможно нужна компиляция
)

echo.
echo ========================================
echo Готово к настройке в редакторе!
echo ========================================
echo.
echo Следующие шаги:
echo 1. Откройте necpg.uproject в редакторе
echo 2. Следуйте инструкции в:
echo    %UI_DIR%\SETUP_QUICK.md
echo.
echo Или используйте детальную инструкцию:
echo    %UI_DIR%\TESTING.md
echo.
pause
endlocal

