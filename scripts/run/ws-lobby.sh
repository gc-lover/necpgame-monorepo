#!/bin/bash
cd "$(dirname "$0")/../../services/ws-lobby-go"
if [ -f "./ws-lobby" ]; then
    ./ws-lobby
else
    go run .
fi

