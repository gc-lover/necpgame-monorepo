@echo off
setlocal enabledelayedexpansion

cd /d "%~dp0.."

set "MAX_LINES=600"
set "REPORT_FILE=file-size-report.md"
set "FAILED_COUNT=0"
set "PASSED_COUNT=0"
set "TOTAL_COUNT=0"
set "EXCLUDED_COUNT=0"

echo.
echo 🔍 Checking ALL files in repository...
echo Max lines per file: %MAX_LINES%
echo.
echo This will take several minutes for large repositories...
echo.

set "EXTENSIONS=.md .mdx .yaml .yml .json .proto .go .cpp .h .hpp .java .kt .ts .tsx .js .jsx .py .rs .sql .sh .bat .ps1"

set "EXCLUDE_PATTERNS=.gen.go .pb.go vendor node_modules target dist build client\UE5 .bundled.yaml coverage .idea .vscode __pycache__ .mvn shared\trackers\logs .uasset .umap .upk .uexp .ubulk .ufont .png .jpg .jpeg .wav .mp3 .ogg .fbx"

echo # File Size Check Report > "%REPORT_FILE%"
echo. >> "%REPORT_FILE%"
echo Generated: %date% %time% >> "%REPORT_FILE%"
echo Max lines per file: %MAX_LINES% >> "%REPORT_FILE%"
echo. >> "%REPORT_FILE%"
echo ## Files Exceeding Limit >> "%REPORT_FILE%"
echo. >> "%REPORT_FILE%"

for /r %%f in (*) do (
    set "file=%%f"
    set "skip=0"
    
    for %%p in (%EXCLUDE_PATTERNS%) do (
        echo !file! | findstr /i "%%p" >nul 2>&1
        if !errorlevel! equ 0 set "skip=1"
    )
    
    if !skip! equ 0 (
        set "ext=%%~xf"
        set "check=0"
        
        for %%e in (%EXTENSIONS%) do (
            if /i "!ext!"=="%%e" set "check=1"
        )
        
        if !check! equ 1 (
            set /a TOTAL_COUNT+=1
            
            for /f %%c in ('find /c /v "" ^< "%%f" 2^>nul') do set "lines=%%c"
            
            if !lines! gtr %MAX_LINES% (
                set /a FAILED_COUNT+=1
                echo ❌ %%f : !lines! lines
                echo - **%%f** : !lines! lines >> "%REPORT_FILE%"
            ) else (
                set /a PASSED_COUNT+=1
                if !TOTAL_COUNT! lss 20 echo OK %%f : !lines! lines
            )
        )
    )
)

echo. >> "%REPORT_FILE%"
echo ## Summary >> "%REPORT_FILE%"
echo. >> "%REPORT_FILE%"
echo - Total checked: %TOTAL_COUNT% files >> "%REPORT_FILE%"
echo - Passed: %PASSED_COUNT% files >> "%REPORT_FILE%"
echo - Failed: %FAILED_COUNT% files >> "%REPORT_FILE%"
echo. >> "%REPORT_FILE%"

echo.
echo ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
echo 📊 Summary:
echo ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
echo Total checked: %TOTAL_COUNT% files
echo Passed: %PASSED_COUNT% files
echo Failed: %FAILED_COUNT% files
echo ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
echo.
echo 📝 Report generated: %REPORT_FILE%
echo.

if %FAILED_COUNT% gtr 0 (
    echo ❌ Found %FAILED_COUNT% files exceeding %MAX_LINES% lines!
    echo Please check the report for details.
    exit /b 1
) else (
    echo OK All files pass the size check!
    exit /b 0
)

pause

