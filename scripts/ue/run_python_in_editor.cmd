@echo off
setlocal
REM Запуск Python скрипта в уже открытом редакторе
REM Использование: открыть редактор, затем запустить этот скрипт

set PROJ_DIR=%~dp0..\..\client\UE5\necpg
set PYTHON_SCRIPT=%PROJ_DIR%\Scripts\create_hud_blueprints.py

echo ========================================
echo Запуск Python скрипта в редакторе
echo ========================================
echo.
echo ИНСТРУКЦИЯ:
echo 1. Убедитесь что Unreal Editor открыт с проектом necpg
echo 2. Нажмите `` ` `` (обратная кавычка) для открытия консоли
echo 3. Введите команду:
echo.
echo    py Scripts/create_hud_blueprints.py
echo.
echo 4. Нажмите Enter
echo.
echo Или используйте меню:
echo Tools -> Python -> Execute Script -> выберите create_hud_blueprints.py
echo.
echo ========================================
echo Путь к скрипту:
echo %PYTHON_SCRIPT%
echo ========================================
echo.
pause
endlocal

