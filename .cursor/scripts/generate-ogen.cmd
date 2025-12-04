@echo off
REM Quick ogen generation script
REM Issue: #1595

if "%1"=="" (
    echo Usage: generate-ogen.cmd SERVICE_NAME
    echo Example: generate-ogen.cmd combat-damage-service
    exit /b 1
)

set SERVICE_NAME=%1
set SPEC_PATH=..\..\proto\openapi\%SERVICE_NAME%.yaml

echo.
echo Generating ogen code for %SERVICE_NAME%...
echo.

echo Step 1: Bundling OpenAPI spec...
call npx --yes @redocly/cli bundle %SPEC_PATH% -o openapi-bundled.yaml
if errorlevel 1 (
    echo ERROR: Failed to bundle spec
    exit /b 1
)

echo.
echo Step 2: Removing old generated files...
del /Q pkg\api\*.gen.go 2>NUL

echo.
echo Step 3: Generating with ogen...
C:\Users\zzzle\go\bin\ogen.exe --target pkg/api --package api --clean openapi-bundled.yaml
if errorlevel 1 (
    echo ERROR: ogen generation failed
    exit /b 1
)

echo.
echo Step 4: Updating dependencies...
go mod tidy

echo.
echo Step 5: Building...
go build .
if errorlevel 1 (
    echo.
    echo WARNING: Build failed - may need handler fixes
    echo Check errors above and fix types in server/service.go
    exit /b 1
)

echo.
echo ========================================
echo SUCCESS: %SERVICE_NAME% migrated to ogen!
echo ========================================
echo.

