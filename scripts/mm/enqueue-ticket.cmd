@echo off
set MODE=%MODE%
if "%MODE%"=="" set MODE=pve8
set TTL=%TTL%
if "%TTL%"=="" set TTL=60

for /f "tokens=2 delims={}" %%G in ('powershell -NoProfile -Command "[guid]::NewGuid().ToString()"') do set UUID=%%G
set ID=t-%UUID%

redis-cli HSET mm:ticket:%ID% mode %MODE% created_ms 0 >nul
redis-cli EXPIRE mm:ticket:%ID% %TTL% >nul
redis-cli LPUSH mm:queue:%MODE% %ID% >nul

echo Enqueued ticket %ID% to mm:queue:%MODE% (ttl=%TTL%s)


