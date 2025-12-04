#!/bin/bash
# Issue: Benchmark dashboard
# Запускает все бенчмарки и сохраняет результаты

set -e

RESULTS_DIR=".benchmarks/results"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
OUTPUT_FILE="${RESULTS_DIR}/benchmarks_${TIMESTAMP}.json"

mkdir -p "$RESULTS_DIR"

echo "🚀 Running benchmarks for all services..."

# Массив результатов
echo '{"timestamp":"'$TIMESTAMP'","services":[' > "$OUTPUT_FILE"

FIRST=true
SERVICES_FOUND=0

for service_dir in services/*-go; do
    if [ ! -d "$service_dir" ]; then
        continue
    fi
    
    service_name=$(basename "$service_dir")
    echo "  📊 Benchmarking: $service_name"
    
    cd "$service_dir"
    
    # Проверяем наличие бенчмарков
    if ! find . -name "*_bench_test.go" -o -name "*bench_test.go" 2>/dev/null | grep -q .; then
        echo "    ⚠️  No benchmarks found"
        cd - > /dev/null
        continue
    fi
    
    SERVICES_FOUND=$((SERVICES_FOUND + 1))
    
    # Запускаем бенчмарки с JSON output
    BENCH_OUTPUT=$(go test -run=^$$ -bench=. -benchmem -json ./server 2>&1 || echo "")
    
    if [ -z "$BENCH_OUTPUT" ] || echo "$BENCH_OUTPUT" | grep -q "no test files"; then
        echo "    ⚠️  No benchmark tests found"
        cd - > /dev/null
        continue
    fi
    
    if [ "$FIRST" = false ]; then
        echo "," >> "$OUTPUT_FILE"
    fi
    FIRST=false
    
    # Форматируем результат
    echo -n "{\"service\":\"$service_name\",\"benchmarks\":[" >> "$OUTPUT_FILE"
    
    # Парсим JSON output от go test
    BENCH_COUNT=0
    echo "$BENCH_OUTPUT" | while IFS= read -r line; do
        if echo "$line" | jq -e '.Action=="bench"' > /dev/null 2>&1; then
            if [ $BENCH_COUNT -gt 0 ]; then
                echo -n "," >> "$OUTPUT_FILE"
            fi
            BENCH_COUNT=$((BENCH_COUNT + 1))
            
            NAME=$(echo "$line" | jq -r '.Package + "/" + .Test')
            NS_PER_OP=$(echo "$line" | jq -r '.NsPerOp // 0')
            ALLOCS=$(echo "$line" | jq -r '.AllocsPerOp // 0')
            BYTES=$(echo "$line" | jq -r '.BytesPerOp // 0')
            
            echo -n "{\"name\":\"$NAME\",\"ns_per_op\":$NS_PER_OP,\"allocs_per_op\":$ALLOCS,\"bytes_per_op\":$BYTES}" >> "$OUTPUT_FILE"
        fi
    done
    
    echo "]}" >> "$OUTPUT_FILE"
    
    cd - > /dev/null
done

echo "]}" >> "$OUTPUT_FILE"

if [ $SERVICES_FOUND -eq 0 ]; then
    echo "⚠️  No services with benchmarks found"
    exit 1
fi

echo "✅ Benchmarks complete: $OUTPUT_FILE"
echo "   Services benchmarked: $SERVICES_FOUND"

