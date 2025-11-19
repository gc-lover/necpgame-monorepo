#!/usr/bin/env bash
set -e
REDIS_CLI=${REDIS_CLI:-redis-cli}
LAST=${1:-$}
echo "Reading allocations stream from id '$LAST' (Ctrl-C to stop)"
while true; do
  $REDIS_CLI XREAD BLOCK 0 STREAMS mm:allocations "$LAST" | awk '{print}'
done


