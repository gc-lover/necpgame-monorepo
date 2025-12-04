#!/bin/bash
# Issue: Run benchmarks after successful build
# Используется в CI/CD для автоматического запуска бенчмарков

set -e

SERVICE_NAME="$1"
QUICK="${2:-false}"

if [ -z "$SERVICE_NAME" ]; then
    echo "Usage: $0 <service-name> [quick]"
    exit 1
fi

SERVICE_DIR="services/$SERVICE_NAME"

if [ ! -d "$SERVICE_DIR" ]; then
    echo "❌ Service not found: $SERVICE_NAME"
    exit 1
fi

BENCH_FILE="$SERVICE_DIR/server/handlers_bench_test.go"

if [ ! -f "$BENCH_FILE" ]; then
    echo "⏭️  No benchmarks for $SERVICE_NAME"
    exit 0
fi

echo "📊 Running benchmarks for $SERVICE_NAME..."

cd "$SERVICE_DIR"

if [ -f "Makefile" ]; then
    if [ "$QUICK" = "true" ]; then
        make bench-quick || exit 1
    else
        make bench || exit 1
    fi
else
    if [ "$QUICK" = "true" ]; then
        go test -run=^$$ -bench=. -benchmem -benchtime=100ms ./server || exit 1
    else
        go test -run=^$$ -bench=. -benchmem ./server || exit 1
    fi
fi

echo "OK Benchmarks passed"

