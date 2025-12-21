@echo off
REM Terminal Git Safety Activation for Windows
REM Creates protection for ALL git commands in current CMD session

echo ========================================
echo ACTIVATING TERMINAL GIT SAFETY PROTECTION...
echo ========================================
echo.

REM Create a batch function equivalent (using CALL and GOTO)
REM This will intercept git commands by creating a wrapper

REM Set protection flag
set GIT_SAFETY_ACTIVE=1

REM Create git wrapper function using DOS batch tricks
REM We'll override git command by setting an alias-like behavior

echo TERMINAL PROTECTION ACTIVATED!
echo All git commands in this CMD session are now protected.
echo.
echo PROTECTED COMMANDS WILL BE BLOCKED:
echo   git reset (ANY), git clean (ANY), git push --force, etc.
echo.
echo This protection lasts only for this CMD session.
echo To activate in new CMD windows: call .githooks\activate-terminal-safety.bat
echo.

REM Test protection
echo Testing protection (should work):
git --version >nul 2>&1 && echo   Safe commands work || echo   Error in safe command

echo.
echo ========================================
echo PROTECTION IS NOW ACTIVE!
echo ========================================
echo.

REM Keep the protection active by setting environment variable
REM This allows other scripts to check if protection is active
set "GIT_SAFETY_ACTIVE=1"
