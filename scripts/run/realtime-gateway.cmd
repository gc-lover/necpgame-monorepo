@echo off
setlocal
cd /d %~dp0..\..\services\realtime-gateway-go
if exist realtime-gateway.exe (
  realtime-gateway.exe
) else (
  go run .
)
endlocal


