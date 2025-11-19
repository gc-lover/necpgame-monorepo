@echo off
echo Building MsQuic test client...
echo.

REM Check if MSVC is available
where cl >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo Error: MSVC compiler (cl.exe) not found in PATH
    echo Please run this from "Developer Command Prompt for VS" or set up MSVC environment
    pause
    exit /b 1
)

REM Compile with MsQuic
cl.exe /EHsc /I"C:\Program Files\Epic Games\UE_5.7\Engine\Source\ThirdParty\MsQuic\v220\win64\include" test_client_cpp.cpp /link /LIBPATH:"C:\Program Files\Epic Games\UE_5.7\Engine\Source\ThirdParty\MsQuic\v220\win64\lib" msquic.lib ws2_32.lib /OUT:test_client_cpp.exe

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Build succeeded!
    echo Running test client...
    echo.
    test_client_cpp.exe
) else (
    echo.
    echo Build failed!
)

pause

