#!/bin/bash
# Issue: Benchmark history viewer
# Просмотр истории бенчмарков с сравнением

set -e

RESULTS_DIR=".benchmarks/results"

if [ ! -d "$RESULTS_DIR" ]; then
    echo "❌ No benchmark results found in $RESULTS_DIR"
    echo "   Run: ./scripts/run-all-benchmarks.sh"
    exit 1
fi

echo "📊 Benchmark History Viewer"
echo "=========================="
echo ""

# Показываем все доступные результаты
echo "Available benchmark runs:"
echo ""

FILES=($(ls -t "$RESULTS_DIR"/*.json 2>/dev/null | head -10))

if [ ${#FILES[@]} -eq 0 ]; then
    echo "  No results found"
    exit 1
fi

for i in "${!FILES[@]}"; do
    FILE="${FILES[$i]}"
    BASENAME=$(basename "$FILE" .json)
    TIMESTAMP=$(echo "$BASENAME" | sed 's/benchmarks_//')
    DATE=$(echo "$TIMESTAMP" | cut -d'_' -f1)
    TIME=$(echo "$TIMESTAMP" | cut -d'_' -f2)
    
    echo "  [$((i+1))] $DATE $TIME"
done

echo ""
read -p "Select run number (1-${#FILES[@]}) or 'compare' for comparison: " choice

if [ "$choice" = "compare" ]; then
    if [ ${#FILES[@]} -lt 2 ]; then
        echo "❌ Need at least 2 runs for comparison"
        exit 1
    fi
    
    LATEST="${FILES[0]}"
    PREVIOUS="${FILES[1]}"
    
    echo ""
    echo "📊 Comparing:"
    echo "   Latest:   $(basename "$LATEST")"
    echo "   Previous: $(basename "$PREVIOUS")"
    echo ""
    
    # Извлекаем и сравниваем данные
    jq -r '.services[] | "\(.service):" as $svc | .benchmarks[] | "\($svc)/\(.name): \(.ns_per_op) ns/op, \(.allocs_per_op) allocs/op"' \
        "$LATEST" "$PREVIOUS" | \
        sort | uniq | column -t
    
else
    SELECTED=$((choice-1))
    if [ $SELECTED -lt 0 ] || [ $SELECTED -ge ${#FILES[@]} ]; then
        echo "❌ Invalid selection"
        exit 1
    fi
    
    FILE="${FILES[$SELECTED]}"
    
    echo ""
    echo "📊 Benchmark Results: $(basename "$FILE")"
    echo "=========================================="
    echo ""
    
    # Показываем результаты
    jq -r '.services[] | "\(.service):\n" + (.benchmarks[] | "  - \(.name): \(.ns_per_op) ns/op, \(.allocs_per_op) allocs/op, \(.bytes_per_op) bytes/op\n")' "$FILE"
fi

