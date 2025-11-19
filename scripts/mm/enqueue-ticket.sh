#!/usr/bin/env bash
set -e

MODE=${MODE:-pve8}
REDIS_CLI=${REDIS_CLI:-redis-cli}
TTL=${TTL:-60}

ID="t-$(uuidgen 2>/dev/null || cat /proc/sys/kernel/random/uuid)"

$REDIS_CLI HSET "mm:ticket:$ID" mode "$MODE" created_ms "$(date +%s%3N)" >/dev/null
$REDIS_CLI EXPIRE "mm:ticket:$ID" "$TTL" >/dev/null
$REDIS_CLI LPUSH "mm:queue:$MODE" "$ID" >/dev/null

echo "Enqueued ticket $ID to mm:queue:$MODE (ttl=${TTL}s)"


