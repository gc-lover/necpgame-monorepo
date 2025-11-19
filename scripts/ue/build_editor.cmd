@echo off
setlocal
REM Usage: build_editor.cmd [UPROJECT_DIR] [CONFIG]
REM Uses default UE 5.6 UBT path if not provided via env UBT

set PROJ_DIR=%~1
set CONFIG=%~2
if "%UBT%"=="" set UBT=C:\Program Files\Epic Games\UE_5.6\Engine\Binaries\DotNET\UnrealBuildTool\UnrealBuildTool.exe
if "%PROJ_DIR%"=="" set PROJ_DIR=%~dp0..\..\client\UE5\necpg
if "%CONFIG%"=="" set CONFIG=Development

set PROJ=%PROJ_DIR%\necpg.uproject

if not exist "%UBT%" (
  echo UnrealBuildTool.exe not found: %UBT%
  exit /b 1
)

echo Generating project files...
"%UBT%" -Mode=GenerateProjectFiles -Project="%PROJ%" -Game -Engine
if errorlevel 1 exit /b %errorlevel%

echo Building necpgEditor %CONFIG% Win64 ...
"%UBT%" -Project="%PROJ%" necpgEditor Win64 %CONFIG% -NoHotReloadFromIDE -Quiet
if errorlevel 1 exit /b %errorlevel%

echo Done.
endlocal


