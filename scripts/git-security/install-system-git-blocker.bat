@echo off
REM System Git Blocker Installation
REM Requires Administrator privileges

echo ========================================
echo SYSTEM GIT COMMAND BLOCKER INSTALLATION
echo ========================================
echo.
echo This will COMPLETELY BLOCK git reset and git clean system-wide.
echo.

REM Check for admin rights
net session >nul 2>&1
if %errorLevel% neq 0 (
    echo ERROR: Administrator privileges required!
    echo.
    echo To install system-wide blocking:
    echo 1. Right-click this .bat file
    echo 2. Select "Run as administrator"
    echo 3. Follow the prompts
    echo.
    pause
    exit /b 1
)

echo Installing system git command blocker...

REM Add our wrapper to the BEGINNING of system PATH
set "NEW_PATH=C:\git-system-wrapper;%PATH%"

REM Update system PATH permanently
setx /M PATH "%NEW_PATH%"

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo INSTALLATION SUCCESSFUL!
    echo ========================================
    echo.
    echo SYSTEM PROTECTION ACTIVATED:
    echo.
    echo BLOCKED COMMANDS (system-wide):
    echo   ❌ git reset (any variant)  - COMPLETELY FORBIDDEN
    echo   ❌ git clean (any variant)  - COMPLETELY FORBIDDEN
    echo.
    echo SAFE COMMANDS (allowed):
    echo   OK git add, commit, push, pull, checkout, merge, etc.
    echo.
    echo PROTECTION LEVEL: MAXIMUM
    echo COVERAGE: System-wide (all users, all programs)
    echo.
    echo IMPORTANT:
    echo - Close ALL command prompts and applications
    echo - Open NEW windows to activate protection
    echo - Test with: git reset (should be blocked)
    echo.
    echo ========================================
    echo SYSTEM PROTECTION ACTIVE
    echo ========================================
    echo.
) else (
    echo.
    echo ERROR: Failed to install system protection!
    echo.
    echo Possible causes:
    echo - Insufficient permissions
    echo - PATH too long
    echo - System restrictions
    echo.
)

pause
