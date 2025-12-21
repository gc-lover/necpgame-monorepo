@echo off
REM Git Safety Activation Script for Windows

echo 🛡️ ACTIVATING GIT SAFETY PROTECTION...

REM Set hooks path
git config core.hooksPath .githooks

echo OK Git hooks activated
echo OK Pre-commit protection enabled
echo.
echo Optional: Add to PATH for terminal protection:
echo   set PATH=%%CD%%\.githooks\wrappers;%%PATH%%
echo.
echo 🚨 Dangerous commands will be BLOCKED
pause
