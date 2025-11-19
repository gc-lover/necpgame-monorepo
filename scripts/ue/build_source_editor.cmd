@echo off
setlocal
REM Build UnrealEditor from UE source tree once
REM Usage: build_source_editor.cmd [UE_SRC_DIR]

set UE_SRC=%~1
if "%UE_SRC%"=="" set UE_SRC=C:\UE5-src
set BUILD_BAT=%UE_SRC%\Engine\Build\BatchFiles\Build.bat

if not exist "%BUILD_BAT%" (
  echo Build.bat not found: %BUILD_BAT%
  exit /b 1
)

call "%BUILD_BAT%" UnrealEditor Win64 Development -waitmutex || exit /b %errorlevel%
echo UnrealEditor built.
endlocal


