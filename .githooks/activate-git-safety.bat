@echo off
REM Git Safety Activation Script for Windows
REM This script activates git command interception for the current session

echo ========================================
echo ACTIVATING GIT SAFETY SYSTEM...
echo ========================================
echo.

REM Add wrappers to PATH (prepend to override system git)
set "PATH=%~dp0wrappers;%PATH%"

REM Verify activation
echo Git safety activated for this CMD session
echo PATH updated
echo.

REM Test the protection
echo Testing protection...
echo    (This should work: git --version)
git --version >nul 2>&1
if %errorlevel% equ 0 (
    echo    Safe commands work
) else (
    echo    Safe commands blocked - check PATH setup
)

echo.
echo IMPORTANT:
echo    This protection is active ONLY in this CMD session.
echo    To activate in new CMD windows, run: .githooks\activate-git-safety.bat
echo.
echo PROTECTION ACTIVE: Dangerous git commands will be BLOCKED!
echo    git reset --hard, git clean -fd, git push --force, etc. = BLOCKED
echo.
echo Use 'git safe ^<command^>' as alternative if needed
echo.
echo ========================================
pause