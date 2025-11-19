#!/bin/bash
cd "$(dirname "$0")/../../services/matchmaking-go"
if [ -f "./matchmaking" ]; then
    ./matchmaking
else
    go run .
fi

