@echo off
setlocal
REM Rebuild UE5 Game with Unity Build optimization
REM Usage: rebuild_game_unity.cmd [UE_ROOT] [PROJECT_PATH] [CONFIG]

set UE_ROOT=%~1
if "%UE_ROOT%"=="" set UE_ROOT=C:\Program Files\Epic Games\UE_5.7\Engine

set PROJECT_PATH=%~2
if "%PROJECT_PATH%"=="" set PROJECT_PATH=C:\NECPGAME\client\UE5\NECPGAME\NECPGAME.uproject

set CONFIG=%~3
if "%CONFIG%"=="" set CONFIG=Development

set BUILD_BAT=%UE_ROOT%\Build\BatchFiles\Build.bat

if not exist "%BUILD_BAT%" (
  echo Error: Build.bat not found at %BUILD_ROOT%
  echo Please specify correct UE_ROOT path
  exit /b 1
)

if not exist "%PROJECT_PATH%" (
  echo Error: Project not found at %PROJECT_PATH%
  exit /b 1
)

echo ========================================
echo Rebuilding UE5 Game with Unity Build
echo ========================================
echo UE Root: %UE_ROOT%
echo Project: %PROJECT_PATH%
echo Config: %CONFIG%
echo ========================================
echo.

echo Building LyraGame %CONFIG% Win64...
"%BUILD_BAT%" LyraGame Win64 %CONFIG% "%PROJECT_PATH%" -waitmutex

if %errorlevel% equ 0 (
  echo.
  echo ========================================
  echo Build completed successfully!
  echo ========================================
) else (
  echo.
  echo ========================================
  echo Build failed with error code: %errorlevel%
  echo ========================================
  exit /b %errorlevel%
)

endlocal


