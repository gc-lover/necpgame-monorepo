#!/usr/bin/env bash
set -e

OUT_DIR="$(cd "$(dirname "$0")/../../infrastructure/envoy/certs" && pwd)"
mkdir -p "$OUT_DIR"

CN=${CN:-"localhost"}
DAYS=${DAYS:-365}

openssl req -x509 -nodes -newkey rsa:2048 \
  -keyout "$OUT_DIR/server.key" \
  -out "$OUT_DIR/server.crt" \
  -subj "/CN=$CN" \
  -days "$DAYS"

echo "Created:"
echo "  $OUT_DIR/server.crt"
echo "  $OUT_DIR/server.key"


