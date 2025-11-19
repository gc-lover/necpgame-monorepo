@echo off
setlocal
REM Usage: run_listen_server.cmd C:\NECPGAME\client\UE5\necpg C:\\"Program Files\\Epic Games\\UE_5.6\\Engine\\Binaries\\Win64\\UnrealEditor.exe" 7777

set PROJ_DIR=%~1
set EDITOR_EXE=%~2
set PORT=%3
if "%PROJ_DIR%"=="" set PROJ_DIR=%~dp0..\..\client\UE5\necpg
if "%EDITOR_EXE%"=="" set EDITOR_EXE=C:\Program Files\Epic Games\UE_5.6\Engine\Binaries\Win64\UnrealEditor.exe
if "%PORT%"=="" set PORT=7777

set UPROJECT=%PROJ_DIR%\necpg.uproject
set MAP=/Engine/Maps/Minimal_Default

if not exist "%EDITOR_EXE%" (
  echo UnrealEditor.exe not found: %EDITOR_EXE%
  exit /b 1
)

echo Starting Listen Server via UnrealEditor on port %PORT% ...
"%EDITOR_EXE%" "%UPROJECT%" "%MAP%" -server -log -port=%PORT% -nosteam
endlocal


