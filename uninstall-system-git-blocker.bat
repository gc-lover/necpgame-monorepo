@echo off
REM System Git Blocker Uninstallation
REM Removes system-wide git command blocking

echo ========================================
echo SYSTEM GIT BLOCKER UNINSTALLATION
echo ========================================
echo.
echo This will REMOVE system-wide git command blocking.
echo Git reset and git clean will work normally again.
echo.

REM Check for admin rights
net session >nul 2>&1
if %errorLevel% neq 0 (
    echo ERROR: Administrator privileges required!
    echo.
    echo To uninstall system protection:
    echo 1. Right-click this .bat file
    echo 2. Select "Run as administrator"
    echo 3. Follow the prompts
    echo.
    pause
    exit /b 1
)

echo Removing system git command blocker...

REM Get current system PATH
for /f "tokens=2*" %%A in ('reg query "HKLM\SYSTEM\CurrentControlSet\Control\Session Manager\Environment" /v Path 2^>nul') do set "CURRENT_PATH=%%B"

REM Remove our path from PATH
set "NEW_PATH=%CURRENT_PATH:C:\git-system-wrapper;=%"

REM Update system PATH
setx /M PATH "%NEW_PATH%"

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo UNINSTALLATION SUCCESSFUL!
    echo ========================================
    echo.
    echo SYSTEM PROTECTION REMOVED:
    echo.
    echo COMMANDS NOW ALLOWED:
    echo   WARNING  git reset (any variant)  - NO LONGER BLOCKED
    echo   WARNING  git clean (any variant)  - NO LONGER BLOCKED
    echo.
    echo WARNING: Use these commands CAREFULLY!
    echo They can destroy your work permanently.
    echo.
    echo IMPORTANT:
    echo - Close ALL command prompts and applications
    echo - Open NEW windows for changes to take effect
    echo - Test with: git reset (should work now)
    echo.
    echo ========================================
    echo SYSTEM PROTECTION REMOVED
    echo ========================================
    echo.
) else (
    echo.
    echo ERROR: Failed to remove system protection!
    echo.
    echo You may need to manually edit system PATH.
    echo Remove: C:\git-system-wrapper;
    echo.
)

pause
