#!/bin/bash

# NECPGAME Performance Monitor Entrypoint
# Issue: Docker optimization for monitoring service

set -e

echo "[INFO] Starting NECPGAME Performance Monitor"

# Wait for database to be ready
echo "[INFO] Waiting for database..."
while ! nc -z postgres 5432; do
  sleep 1
done
echo "[OK] Database is ready"

# Run the monitoring script
echo "[INFO] Starting performance monitoring..."
exec "$@"
