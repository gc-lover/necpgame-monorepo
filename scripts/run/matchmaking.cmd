@echo off
cd /d %~dp0\..\..\services\matchmaking-go
if exist matchmaking.exe (
    matchmaking.exe
) else (
    go run .
)

