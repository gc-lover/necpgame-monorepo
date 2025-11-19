@echo off
setlocal
REM Usage: build_server.cmd "C:\Program Files\Epic Games\UE_5.6\Engine\Binaries\DotNET\UnrealBuildTool\UnrealBuildTool.exe" C:\NECPGAME\client\UE5\necpg

set UBT=%~1
set PROJ_DIR=%~2
set CONFIG=%3
if "%UBT%"=="" (
  set UBT=C:\Program Files\Epic Games\UE_5.6\Engine\Binaries\DotNET\UnrealBuildTool\UnrealBuildTool.exe
  for /f "delims=" %%A in ('dir /s /b "C:\UE5-src\LocalBuilds\Engine\Windows\Engine\Binaries\DotNET\UnrealBuildTool\UnrealBuildTool.exe" 2^>nul') do set UBT=%%A
)
if "%PROJ_DIR%"=="" set PROJ_DIR=%~dp0..\..\client\UE5\necpg
if "%CONFIG%"=="" set CONFIG=Development

set PROJ=%PROJ_DIR%\necpg.uproject

if not exist "%UBT%" (
  echo UnrealBuildTool.exe not found: %UBT%
  echo Provide the correct path as the first argument.
  exit /b 1
)

echo Building necpgServer %CONFIG% Win64 ...
"%UBT%" -Project="%PROJ%" necpgServer Win64 %CONFIG% -NoHotReloadFromIDE -Quiet
if errorlevel 1 exit /b %errorlevel%

echo Done.
endlocal


