@echo off
set ID=%1
if "%ID%"=="" set ID=$
echo Reading allocations from id "%ID%" (Ctrl-C to stop)
:loop
redis-cli XREAD BLOCK 0 STREAMS mm:allocations %ID%
goto loop


