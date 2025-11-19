#!/usr/bin/env bash
set -e
bash "$(dirname "$0")/../certs/generate-envoy-certs.sh"
cd "$(dirname "$0")/../../infrastructure/docker/auth-envoy"
docker compose up -d


