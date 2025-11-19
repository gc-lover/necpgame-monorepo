@echo off
setlocal
REM Build necpgServer directly from source engine using UBT
REM Usage: build_server_from_source.cmd [UE_SRC_DIR] [PROJECT_DIR] [CONFIG]

set UE_SRC=%~1
if "%UE_SRC%"=="" set UE_SRC=C:\UE5-src
set PROJECT_DIR=%~2
if "%PROJECT_DIR%"=="" set PROJECT_DIR=C:\NECPGAME\client\UE5\necpg
set CONFIG=%~3
if "%CONFIG%"=="" set CONFIG=Development

set UBT=%UE_SRC%\Engine\Binaries\DotNET\UnrealBuildTool\UnrealBuildTool.exe

if not exist "%UBT%" (
  echo UnrealBuildTool not found: %UBT%
  echo Run build_source_editor.cmd first to build the engine
  exit /b 1
)

if not exist "%PROJECT_DIR%\necpg.uproject" (
  echo Project not found: %PROJECT_DIR%\necpg.uproject
  exit /b 1
)

echo Building necpgServer %CONFIG% Win64 from source engine...
"%UBT%" -Project="%PROJECT_DIR%\necpg.uproject" necpgServer Win64 %CONFIG% -engine -waitmutex || exit /b %errorlevel%

echo Server built: %PROJECT_DIR%\Binaries\Win64\necpgServer.exe
endlocal




