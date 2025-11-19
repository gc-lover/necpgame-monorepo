@echo off
REM Stop all development services

echo Stopping all development services...
echo.

echo Stopping UE5 Server...
call %~dp0ue5-server.cmd stop

echo.
echo Stopping Docker services...
cd /d %~dp0..\..
docker-compose down

echo.
echo All services stopped.

