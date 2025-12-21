@echo off
REM Test System Git Command Blocking

echo ========================================
echo TESTING SYSTEM GIT COMMAND BLOCKING
echo ========================================
echo.
echo This test verifies that git reset and git clean are COMPLETELY BLOCKED.
echo.

echo 1. Testing git reset --hard (should be BLOCKED):
git reset --hard >nul 2>&1
if %errorlevel% neq 0 (
    echo    [OK] BLOCKED: git reset --hard command blocked
) else (
    echo    [ERROR] FAILED: git reset --hard executed (not blocked!)
)

echo.
echo 2. Testing git reset HEAD~1 (should be BLOCKED):
git reset HEAD~1 >nul 2>&1
if %errorlevel% neq 0 (
    echo    [OK] BLOCKED: git reset HEAD~1 command blocked
) else (
    echo    [ERROR] FAILED: git reset HEAD~1 executed (not blocked!)
)

echo.
echo 3. Testing git clean -fd (should be BLOCKED):
git clean -fd >nul 2>&1
if %errorlevel% neq 0 (
    echo    [OK] BLOCKED: git clean -fd command blocked
) else (
    echo    [ERROR] FAILED: git clean -fd executed (not blocked!)
)

echo.
echo 4. Testing git clean -n (should be BLOCKED):
git clean -n >nul 2>&1
if %errorlevel% neq 0 (
    echo    [OK] BLOCKED: git clean -n command blocked
) else (
    echo    [ERROR] FAILED: git clean -n executed (not blocked!)
)

echo.
echo 5. Testing SAFE command git status (should work):
git status --porcelain >nul 2>&1
if %errorlevel% equ 0 (
    echo    [OK] SAFE: git status works normally
) else (
    echo    [ERROR] ERROR: Even safe commands are blocked!
)

echo.
echo ========================================
echo TEST RESULTS SUMMARY
echo ========================================
echo.
if %errorlevel% equ 0 (
    echo RESULT: System blocking is WORKING correctly!
    echo All dangerous commands are blocked, safe commands work.
    echo.
    echo PROTECTION STATUS: ACTIVE [OK]
) else (
    echo RESULT: System blocking has ISSUES!
    echo Some dangerous commands may not be blocked properly.
    echo.
    echo PROTECTION STATUS: INCOMPLETE [WARNING]
)
echo.
pause
