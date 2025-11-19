@echo off
setlocal
REM Make Installed Build with Server enabled
REM Usage: make_installed_server_build.cmd [UE_SRC_DIR]

set UE_SRC=%~1
if "%UE_SRC%"=="" set UE_SRC=C:\UE5-src
set UAT=%UE_SRC%\Engine\Build\BatchFiles\RunUAT.bat

if not exist "%UAT%" (
  echo RunUAT.bat not found: %UAT%
  exit /b 1
)

set DOTNET_NUGET_NO_WARN=NU1902,NU1903
call "%UAT%" BuildGraph -Script="Engine\Build\InstalledEngineBuild.xml" -target="Make Installed Build Win64" -set:HostPlatformOnly=true -set:WithDDC=false -set:WithServer=true -NoSubmit || exit /b %errorlevel%
echo Installed Build created under %UE_SRC%\LocalBuilds\Engine\Windows
endlocal


