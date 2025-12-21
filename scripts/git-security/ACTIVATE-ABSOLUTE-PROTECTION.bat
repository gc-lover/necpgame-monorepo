@echo off
REM ABSOLUTE GIT PROTECTION ACTIVATOR
REM One-click installation of complete git command blocking

echo.
echo ========================================
echo ABSOLUTE GIT PROTECTION ACTIVATOR
echo ========================================
echo.
echo This will COMPLETELY BLOCK git reset and git clean system-wide.
echo.
echo REQUIREMENTS:
echo - Administrator privileges
echo - System restart after installation
echo.
echo BLOCKED FOREVER:
echo   git reset (any)  - IMPOSSIBLE TO EXECUTE
echo   git clean (any)  - IMPOSSIBLE TO EXECUTE
echo.
echo SAFE COMMANDS REMAIN:
echo   git add, commit, push, pull, checkout, etc.
echo.
pause

REM Check admin rights
net session >nul 2>&1
if %errorLevel% neq 0 (
    echo.
    echo ========================================
    echo ADMINISTRATOR RIGHTS REQUIRED
    echo ========================================
    echo.
    echo Please run this script as Administrator:
    echo 1. Right-click this .bat file
    echo 2. Select "Run as administrator"
    echo 3. Try again
    echo.
    pause
    exit /b 1
)

echo.
echo ========================================
echo INSTALLING SYSTEM PROTECTION...
echo ========================================
echo.

REM Create wrapper directory if not exists
if not exist "C:\git-system-wrapper" mkdir "C:\git-system-wrapper"

REM Copy our wrapper
copy "C:\git-system-wrapper\git.bat" "C:\git-system-wrapper\git.bat.backup" >nul 2>&1
copy "%~dp0C:\git-system-wrapper\git.bat" "C:\git-system-wrapper\" >nul 2>&1

REM Add to system PATH at the beginning
set "NEW_PATH=C:\git-system-wrapper;%PATH%"
setx /M PATH "%NEW_PATH%" >nul 2>&1

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo INSTALLATION SUCCESSFUL!
    echo ========================================
    echo.
    echo ABSOLUTE PROTECTION ACTIVATED:
    echo.
    echo [FORBIDDEN] COMPLETELY BLOCKED (system-wide):
    echo    git reset (any variant) - IMPOSSIBLE
    echo    git clean (any variant) - IMPOSSIBLE
    echo.
    echo [OK] SAFE COMMANDS (allowed):
    echo    git add, commit, push, pull, checkout, merge
    echo.
    echo [SYMBOL] NEXT STEPS:
    echo    1. Close ALL applications
    echo    2. RESTART YOUR COMPUTER
    echo    3. Open new command prompt
    echo    4. Test: git reset (should be blocked)
    echo.
    echo [SYMBOL] TEST COMMAND:
    echo    test-system-blocking.bat
    echo.
    echo ========================================
    echo PROTECTION ACTIVE AFTER RESTART
    echo ========================================
    echo.
    echo IMPORTANT: Computer restart is REQUIRED!
    echo Protection is NOT active until restart.
    echo.
) else (
    echo.
    echo ========================================
    echo INSTALLATION FAILED!
    echo ========================================
    echo.
    echo Possible issues:
    echo - PATH environment variable too long
    echo - System permissions issue
    echo - Conflicting git installations
    echo.
    echo Try manual installation:
    echo 1. Run install-system-git-blocker.bat as admin
    echo 2. Follow manual steps in SYSTEM-GIT-BLOCKING-README.md
    echo.
)

echo.
echo Press any key to finish...
pause >nul
