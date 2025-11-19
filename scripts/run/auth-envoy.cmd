@echo off
setlocal
call %~dp0..\certs\generate-envoy-certs.sh
cd /d %~dp0..\..\infrastructure\docker\auth-envoy
docker compose up -d
endlocal


