@echo off
setlocal
REM Полная автоматизация настройки HUD
REM Выполняет: компиляцию -> проверку -> создание Blueprint -> инструкции

set PROJ_DIR=%~dp0..\..\client\UE5\necpg

echo ========================================
echo Tech Demo HUD - Полная автоматизация
echo ========================================
echo.

REM Шаг 1: Компиляция
echo [1/4] Компиляция проекта...
call "%~dp0build_editor.cmd"
if errorlevel 1 (
    echo ERROR: Компиляция не удалась!
    pause
    exit /b 1
)
echo [OK] Проект скомпилирован
echo.

REM Шаг 2: Проверка готовности
echo [2/4] Проверка готовности...
call "%~dp0setup_hud_after_compile.cmd"
if errorlevel 1 (
    echo ERROR: Проверка не пройдена!
    pause
    exit /b 1
)
echo.

REM Шаг 3: Создание Blueprint (требует открытого редактора)
echo [3/4] Создание Blueprint файлов...
echo.
echo ВАЖНО: Для этого шага нужно:
echo 1. Открыть проект в редакторе (двойной клик на necpg.uproject)
echo 2. Дождаться полной загрузки
echo 3. В консоли редактора (`` ` ``) выполнить:
echo    py Scripts/create_hud_blueprints.py
echo.
echo Или запустить скрипт:
echo    scripts\ue\create_hud_blueprints.cmd
echo.
pause

REM Шаг 4: Инструкции
echo [4/4] Финальная настройка...
echo.
echo После создания Blueprint выполните ручные шаги:
echo.
type "%PROJ_DIR%\Source\necpg\UI\SETUP_ULTRA_QUICK.txt"
echo.
pause

echo ========================================
echo Автоматизация завершена!
echo ========================================
endlocal

