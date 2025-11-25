@echo off
setlocal enabledelayedexpansion

echo.
echo Installing Git hooks...
echo.

cd /d "%~dp0.."

if not exist ".githooks" (
    echo Error: .githooks directory not found
    exit /b 1
)

git config core.hooksPath .githooks

if %errorlevel% neq 0 (
    echo Error: Failed to configure Git hooks
    exit /b 1
)

echo Git hooks installed successfully!
echo.
echo Hooks installed:
for %%f in (.githooks\*) do (
    echo   - %%~nxf
)
echo.
echo To uninstall: git config --unset core.hooksPath
echo.

pause

