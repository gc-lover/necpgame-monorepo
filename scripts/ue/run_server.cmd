@echo off
setlocal
REM Usage: run_server.cmd C:\NECPGAME\client\UE5\necpg\Binaries\Win64 7777

set BIN_DIR=%~1
set PORT=%2
if "%BIN_DIR%"=="" set BIN_DIR=%~dp0..\..\client\UE5\necpg\Binaries\Win64
if "%PORT%"=="" set PORT=7777

set EXE=%BIN_DIR%\necpgServer.exe
if not exist "%EXE%" (
  echo Server binary not found: %EXE%
  exit /b 1
)
echo Starting necpgServer on port %PORT% ...
"%EXE%" -log -port=%PORT%
endlocal


