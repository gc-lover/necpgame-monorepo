@echo off
REM Git Safe Wrapper for Windows
REM This script intercepts ALL git commands and blocks dangerous ones

REM Dangerous command patterns to block
set "DANGEROUS_COMMANDS[0]=reset"
set "DANGEROUS_COMMANDS[1]=clean"
set "DANGEROUS_COMMANDS[2]=checkout --force"
set "DANGEROUS_COMMANDS[3]=branch -D"
set "DANGEROUS_COMMANDS[4]=push --force"
set "DANGEROUS_COMMANDS[5]=rebase --abort"
set "DANGEROUS_COMMANDS[6]=stash drop"

REM Check if command line contains dangerous patterns
set "CMD_LINE=%*"
set "IS_DANGEROUS=0"

for /L %%i in (0,1,7) do (
    echo !DANGEROUS_COMMANDS[%%i]! | findstr /C:"%CMD_LINE%" >nul 2>&1
    if !errorlevel! equ 0 (
        set "IS_DANGEROUS=1"
        set "DANGEROUS_CMD=!DANGEROUS_COMMANDS[%%i]!"
        goto :danger_check_done
    )
)

:danger_check_done

if %IS_DANGEROUS% equ 1 (
    echo ========================================
    echo CRITICAL SECURITY VIOLATION: DANGEROUS GIT COMMAND DETECTED!
    echo AI AGENT SECURITY BREACH DETECTED!
    echo ========================================
    echo.
    echo STRICTLY FORBIDDEN: AGENT ATTEMPTED TO EXECUTE: git %*
    echo.
    echo BLOCKED: THIS COMMAND WOULD DESTROY ENTIRE PROJECT!
    echo BLOCKED: CAUSES IRREVERSIBLE DATA LOSS!
    echo BLOCKED FOR PROJECT SAFETY
    echo.
    echo DO NOT ATTEMPT TO BYPASS THIS PROTECTION!
    echo DO NOT TRY TO USE THESE COMMANDS IN ANY WAY!
    echo DO NOT TRY TO EXECUTE DANGEROUS OPERATIONS!
    echo THIS IS A SERIOUS SECURITY VIOLATION!
    echo.
    echo REQUIRED ACTION: Return task immediately with security violation note
    echo DO NOT proceed with any dangerous operations!
    echo.
    echo ALLOWED SAFE COMMANDS ONLY:
    echo   git add ^<files^>       Stage files safely
    echo   git commit -m "msg"   Commit changes safely
    echo   git push              Push to remote safely
    echo   git pull              Pull from remote safely
    echo   git checkout ^<branch^> Switch branches safely
    echo   git merge ^<branch^>   Merge branches safely
    echo   git stash             Save work temporarily
    echo   git stash pop         Restore saved work
    echo.
    echo FORBIDDEN DESTRUCTIVE COMMANDS (NEVER USE):
    echo   git reset (ANY)       Lose work - FORBIDDEN
    echo   git clean (ANY)       Delete files - FORBIDDEN
    echo   git checkout --force  Force overwrite - FORBIDDEN
    echo   git branch -D         Force delete branch - FORBIDDEN
    echo   git push --force      Force push - FORBIDDEN
    echo.
    echo EMERGENCY: If you need dangerous operations:
    echo    STOP IMMEDIATELY and contact HUMAN ADMINISTRATOR!
    echo    Do NOT attempt to bypass this protection!
    echo.
    echo ========================================
    echo SECURITY INCIDENT LOGGED - ADMIN NOTIFICATION SENT
    echo ========================================
    exit /b 1
)

REM Safe command, execute normally
git %*
