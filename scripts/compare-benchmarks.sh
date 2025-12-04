#!/bin/bash
# Сравнивает результаты бенчмарков между двумя запусками

set -e

SERVICE=$1
RESULTS_DIR=".benchmarks/results"

if [ -z "$SERVICE" ]; then
    echo "Usage: $0 <service-name>"
    echo ""
    echo "Example: $0 matchmaking-go"
    echo ""
    echo "Available services:"
    ls -1 "$RESULTS_DIR"/*.json 2>/dev/null | head -5 | xargs -I {} basename {} | sed 's/benchmarks_//' | sed 's/.json//' || echo "  No results found"
    exit 1
fi

LATEST=$(ls -t "$RESULTS_DIR"/*.json 2>/dev/null | head -1)
PREVIOUS=$(ls -t "$RESULTS_DIR"/*.json 2>/dev/null | head -2 | tail -1)

if [ -z "$LATEST" ]; then
    echo "❌ No benchmark results found in $RESULTS_DIR"
    exit 1
fi

if [ -z "$PREVIOUS" ] || [ "$LATEST" = "$PREVIOUS" ]; then
    echo "WARNING  Only one result file found. Need at least 2 for comparison."
    echo "   Latest: $(basename "$LATEST")"
    exit 1
fi

echo "📊 Comparing benchmarks for: $SERVICE"
echo "   Previous: $(basename "$PREVIOUS")"
echo "   Latest:   $(basename "$LATEST")"
echo ""

# Извлекаем данные для сервиса
PREV_DATA=$(jq -r ".services[] | select(.service==\"$SERVICE\")" "$PREVIOUS" 2>/dev/null)
LATEST_DATA=$(jq -r ".services[] | select(.service==\"$SERVICE\")" "$LATEST" 2>/dev/null)

if [ -z "$PREV_DATA" ] || [ "$PREV_DATA" = "null" ]; then
    echo "❌ Service '$SERVICE' not found in previous results"
    exit 1
fi

if [ -z "$LATEST_DATA" ] || [ "$LATEST_DATA" = "null" ]; then
    echo "❌ Service '$SERVICE' not found in latest results"
    exit 1
fi

# Формируем таблицу сравнения
echo "Benchmark Name                    | Previous (ns/op) | Latest (ns/op)  | Change    | Allocs (prev→latest)"
echo "----------------------------------|------------------|-----------------|-----------|-------------------"

echo "$PREV_DATA" | jq -r '.benchmarks[] | "\(.name)|\(.ns_per_op)|\(.allocs_per_op)"' | while IFS='|' read -r name prev_ns prev_allocs; do
    latest=$(echo "$LATEST_DATA" | jq -r ".benchmarks[] | select(.name==\"$name\") | \"\(.ns_per_op)|\(.allocs_per_op)\"")
    
    if [ -z "$latest" ]; then
        continue
    fi
    
    latest_ns=$(echo "$latest" | cut -d'|' -f1)
    latest_allocs=$(echo "$latest" | cut -d'|' -f2)
    
    if [ "$prev_ns" != "0" ] && [ "$prev_ns" != "null" ]; then
        change=$(echo "scale=2; ($latest_ns - $prev_ns) * 100 / $prev_ns" | bc)
        if (( $(echo "$change > 0" | bc -l) )); then
            change_str="+${change}%"
        else
            change_str="${change}%"
        fi
    else
        change_str="N/A"
    fi
    
    printf "%-33s | %16s | %15s | %9s | %s → %s\n" \
        "$name" \
        "$prev_ns" \
        "$latest_ns" \
        "$change_str" \
        "$prev_allocs" \
        "$latest_allocs"
done

