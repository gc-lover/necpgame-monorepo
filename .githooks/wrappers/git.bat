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
    echo EMERGENCY BLOCK: DANGEROUS GIT COMMAND DETECTED!
    echo ========================================
    echo.
    echo WARNING: AGENT ATTEMPTED TO EXECUTE: git %*
    echo.
    echo BLOCKED: THIS COMMAND WOULD CAUSE IRREVERSIBLE DATA LOSS!
    echo BLOCKED FOR PROJECT SAFETY
    echo.
    echo FORBIDDEN COMMANDS (POTENTIAL DATA LOSS):
    echo   - git reset --hard    = Lose ALL uncommitted work
    echo   - git clean -fd       = Delete untracked files
    echo   - git clean -fdx      = Delete ALL untracked files + ignored
    echo   - git checkout --force = Force overwrite local files
    echo   - git branch -D       = Force delete branch
    echo   - git push --force    = Force overwrite remote branch
    echo   - git rebase --abort  = Abort rebase (lose progress)
    echo   - git stash drop      = Delete stashed changes forever
    echo.
    echo SAFE ALTERNATIVES:
    echo   git add ^<files^>       Add files to staging
    echo   git commit -m "msg"   Commit staged changes
    echo   git push              Push to remote safely
    echo   git pull              Pull from remote safely
    echo   git checkout ^<branch^> Switch branches safely
    echo   git merge ^<branch^>   Merge branches safely
    echo   git stash             Save work temporarily
    echo   git stash pop         Restore saved work
    echo.
    echo EMERGENCY: If you need dangerous operations:
    echo    Contact HUMAN ADMINISTRATOR immediately!
    echo    Do NOT attempt to bypass this protection!
    echo.
    echo ========================================
    echo SECURITY INCIDENT LOGGED
    echo ========================================
    exit /b 1
)

REM Safe command, execute normally
git %*
