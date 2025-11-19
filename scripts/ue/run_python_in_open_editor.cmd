@echo off
setlocal
REM Инструкция для запуска Python скрипта в открытом редакторе

echo ========================================
echo Запуск Python скрипта в редакторе
echo ========================================
echo.
echo ИНСТРУКЦИЯ:
echo.
echo 1. Откройте Unreal Editor с проектом necpg
echo 2. Дождитесь полной загрузки проекта
echo 3. Нажмите `` ` `` (обратная кавычка) для открытия консоли
echo 4. Введите команду:
echo.
echo    py Scripts/create_hud_blueprints_simple.py
echo.
echo 5. Нажмите Enter
echo.
echo Или через меню:
echo Tools -> Python -> Execute Script
echo Выберите: Scripts/create_hud_blueprints_simple.py
echo.
echo ========================================
echo Скрипт создаст:
echo - WBP_TechDemoHUD (Widget Blueprint)
echo - BP_TechDemoHUD (HUD Blueprint)
echo - BP_TechDemoGameMode (GameMode Blueprint)
echo ========================================
echo.
pause
endlocal

