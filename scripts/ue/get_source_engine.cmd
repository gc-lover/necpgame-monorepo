@echo off
setlocal
REM Clones UE source (requires EpicGames GitHub access), runs Setup and GenerateProjectFiles
REM Usage: get_source_engine.cmd [UE_SRC_DIR] [BRANCH]

set UE_SRC=%~1
set BRANCH=%~2
if "%UE_SRC%"=="" set UE_SRC=C:\UE5-src
if "%BRANCH%"=="" set BRANCH=5.6

if exist "%UE_SRC%\\Engine\\Build\\BatchFiles\\Build.bat" (
  echo UE source already present: %UE_SRC%
  goto :setup
)

where git >nul 2>nul || (echo Git not found. Install Git first. & exit /b 1)
git lfs install

echo Cloning UnrealEngine %BRANCH% to %UE_SRC% ...
git clone --depth 1 -b %BRANCH% https://github.com/EpicGames/UnrealEngine.git "%UE_SRC%"
if errorlevel 1 (
  echo Branch %BRANCH% not found or access issue. Trying default branch and tag checkout...
  git clone --depth 1 https://github.com/EpicGames/UnrealEngine.git "%UE_SRC%"
  if errorlevel 1 (
    echo Clone failed. Ensure your GitHub account has access to EpicGames/UnrealEngine.
    exit /b 1
  )
  pushd "%UE_SRC%"
  git fetch --tags --quiet
  git checkout 5.6.1 2>nul || git checkout 5.6 2>nul || (echo Could not checkout 5.6/5.6.1, staying on default && popd)
  if "%CD%"=="%UE_SRC%" popd
)

:setup
pushd "%UE_SRC%"
call Setup.bat || (echo Setup failed & popd & exit /b 1)
call GenerateProjectFiles.bat || (echo GPF failed & popd & exit /b 1)
popd
echo UE source ready at %UE_SRC%
endlocal


