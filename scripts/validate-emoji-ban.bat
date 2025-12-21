@echo off
REM NECPGAME Emoji and Special Characters Ban Validator (Windows)
REM Checks for forbidden Unicode characters that can break scripts on Windows

setlocal enabledelayedexpansion

REM Colors for Windows CMD (limited support)
REM We'll use plain text since CMD color support is limited

echo 🔍 Checking for forbidden emoji and special characters...

REM Forbidden Unicode characters (basic check for common emoji)
REM This is a simplified version for Windows CMD
set "FORBIDDEN_CHARS=★ ♦ ♠ ♥ ♣ ► ◄ ▲ ▼ ◆ ◇ ✓ ✗ ✖ ✕ ● ○ ■ □ ▶ ◀"

REM Files to exclude from checking
set "EXCLUDED_EXTENSIONS=.png .jpg .jpeg .gif .svg .ico .woff .woff2 .ttf .eot .pdf .zip .tar .gz .7z .rar .mp3 .mp4 .avi .mkv .mov .wmv"
set "EXCLUDED_FILES=.git node_modules .cursor\rules\linter-emoji-ban.mdc"

set "HAS_ERRORS=0"
set "TOTAL_FILES=0"
set "CHECKED_FILES=0"

REM Get files to check from git ls-files or arguments
if "%~1"=="" (
    REM Check all tracked files
    for /f "delims=" %%f in ('git ls-files') do (
        call :check_file "%%f"
    )
) else (
    REM Check specified files
    :check_args
    if "%~1"=="" goto :end_args
    call :check_file "%~1"
    shift
    goto :check_args
    :end_args
)

goto :results

:check_file
set "file=%~1"
set /a TOTAL_FILES+=1

REM Check if file should be excluded
call :should_exclude "%file%"
if !EXCLUDE_RESULT!==1 (
    echo Checking !CHECKED_FILES!/!TOTAL_FILES!: !file! ... SKIPPED
    goto :eof
)

set /a CHECKED_FILES+=1
echo Checking !CHECKED_FILES!/!TOTAL_FILES!: !file! ...

if not exist "%file%" (
    echo   File not found, skipping
    goto :eof
)

REM Basic check for forbidden characters
set "FOUND_ERRORS="
set "LINE_NUM=1"

for /f "delims=" %%l in ('type "%file%" 2^>nul') do (
    set "line=%%l"
    call :check_line "!line!" !LINE_NUM!
    set /a LINE_NUM+=1
)

if defined FOUND_ERRORS (
    echo   ❌ ERRORS FOUND
    echo !FOUND_ERRORS!
    set "HAS_ERRORS=1"
) else (
    echo   OK OK
)

goto :eof

:check_line
setlocal enabledelayedexpansion
set "line=%~1"
set "line_num=%~2"

REM Check for forbidden characters
for %%c in (%FORBIDDEN_CHARS%) do (
    echo !line! | find "%%c" >nul 2>&1
    if !errorlevel!==0 (
        REM Found forbidden character
        for /f "tokens=1,* delims=%%c" %%a in ("!line!") do (
            set "FOUND_ERRORS=!FOUND_ERRORS!Line !line_num!: Found forbidden character '%%c' - !line:~0,50!...\n"
        )
    )
)

REM Basic emoji detection (simplified)
echo !line! | findstr /r "[^\x00-\x7F]" >nul 2>&1
if !errorlevel!==0 (
    REM Contains non-ASCII characters - could be emoji
    echo !line! | findstr /r "[^\x00-\xFF]" >nul 2>&1
    if !errorlevel!==0 (
        REM Contains characters above Latin-1 - likely emoji or special Unicode
        set "FOUND_ERRORS=!FOUND_ERRORS!Line !line_num!: Contains Unicode characters above Latin-1 (possible emoji) - !line:~0,50!...\n"
    )
)

goto :eof

:should_exclude
set "file=%~1"
set "EXCLUDE_RESULT=0"

REM Check excluded files
for %%e in (%EXCLUDED_FILES%) do (
    echo %file% | find "%%e" >nul 2>&1
    if !errorlevel!==0 (
        set "EXCLUDE_RESULT=1"
        goto :eof
    )
)

REM Check excluded extensions
for %%e in (%EXCLUDED_EXTENSIONS%) do (
    echo %file% | find "%%e" >nul 2>&1
    if !errorlevel!==0 (
        set "EXCLUDE_RESULT=1"
        goto :eof
    )
)

goto :eof

:results
echo ==================================================
if !HAS_ERRORS!==1 (
    echo 🚨 CRITICAL: Forbidden emoji/special characters detected!
    echo.
    echo Why this matters:
    echo • Emojis break script execution on Windows
    echo • Special Unicode characters cause encoding issues
    echo • Cross-platform compatibility problems
    echo.
    echo Fix suggestions:
    echo • Replace emoji with ASCII text (:smile: instead of 😀)
    echo • Remove decorative Unicode symbols
    echo • Use plain text for all code comments
    echo.
    echo COMMIT BLOCKED: Fix the issues and try again
    exit /b 1
) else (
    echo OK No forbidden emoji/special characters found
    exit /b 0
)
