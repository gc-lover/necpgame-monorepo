@echo off
REM Git Safe Wrapper for Windows
REM This script intercepts dangerous git commands and blocks them

REM Dangerous command patterns to block
set "DANGEROUS_COMMANDS[0]=reset --hard"
set "DANGEROUS_COMMANDS[1]=clean -fd"
set "DANGEROUS_COMMANDS[2]=clean -fdx"
set "DANGEROUS_COMMANDS[3]=checkout --force"
set "DANGEROUS_COMMANDS[4]=branch -D"
set "DANGEROUS_COMMANDS[5]=push --force"
set "DANGEROUS_COMMANDS[6]=rebase --abort"
set "DANGEROUS_COMMANDS[7]=stash drop"

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
    echo.
    echo ========================================
    echo 🚨 DANGER: Git command BLOCKED!
    echo ========================================
    echo.
    echo Blocked command: git %*
    echo.
    echo This command can cause IRREVERSIBLE DATA LOSS!
    echo It is FORBIDDEN for agents to prevent project damage.
    echo.
    echo OK SAFE git commands you can use:
    echo   git add ^<files^>           Stage files for commit
    echo   git commit -m "message"   Commit staged changes
    echo   git push                  Push to remote repository
    echo   git pull                  Pull from remote repository
    echo   git checkout ^<branch^>     Switch to branch
    echo   git merge ^<branch^>        Merge branch
    echo   git stash                 Temporarily save changes
    echo   git stash pop             Restore saved changes
    echo.
    echo ❌ FORBIDDEN commands (cause data loss):
    echo   git reset --hard          Lose ALL uncommitted changes
    echo   git clean -fd             Delete untracked files
    echo   git clean -fdx            Delete ALL untracked files
    echo   git checkout --force      Force overwrite local files
    echo   git branch -D             Force delete branch
    echo   git push --force          Force overwrite remote branch
    echo.
    echo If you ABSOLUTELY need to perform dangerous operations:
    echo Contact a HUMAN administrator for approval!
    echo.
    echo ========================================
    exit /b 1
)

REM Safe command, execute normally
git %*
