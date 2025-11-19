#!/usr/bin/env bash
set -e
cd "$(dirname "$0")/../../services/realtime-gateway-go"
if [ -f "./realtime-gateway" ]; then
  ./realtime-gateway
else
  go run .
fi


