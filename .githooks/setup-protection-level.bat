@echo off
REM Git Protection Level Setup Script for Windows

echo GIT PROTECTION LEVEL SETUP
echo ==========================
echo.
echo Choose protection level:
echo 1. MAXIMUM (Blocks dangerous commands) - For production projects
echo 2. TRAINING (Shows warnings and tips) - For learning environments
echo 3. DISABLED (No protection) - For expert users only
echo.

set /p choice="Enter your choice (1-3): "

if "%choice%"=="1" (
    echo Setting MAXIMUM PROTECTION...
    copy .githooks\pre-commit .git\hooks\pre-commit >nul 2>&1
    copy .githooks\pre-push .git\hooks\pre-push >nul 2>&1
    copy .githooks\commit-msg .git\hooks\commit-msg >nul 2>&1
    echo ✓ MAXIMUM PROTECTION activated - dangerous commands BLOCKED
) else if "%choice%"=="2" (
    echo Setting TRAINING PROTECTION...
    copy .githooks\pre-commit-safety-training .git\hooks\pre-commit >nul 2>&1
    echo ✓ TRAINING PROTECTION activated - educational warnings enabled
) else if "%choice%"=="3" (
    echo DISABLING PROTECTION...
    del .git\hooks\pre-commit .git\hooks\pre-push .git\hooks\commit-msg >nul 2>&1
    echo ✓ PROTECTION DISABLED - use at your own risk!
) else (
    echo Invalid choice. Keeping current protection level.
    goto end
)

echo.
echo Protection level set successfully!
echo Run 'git status' to test the protection.

:end
pause
