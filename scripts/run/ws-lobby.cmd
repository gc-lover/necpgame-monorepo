@echo off
cd /d %~dp0\..\..\services\ws-lobby-go
if exist ws-lobby.exe (
    ws-lobby.exe
) else (
    go run .
)

