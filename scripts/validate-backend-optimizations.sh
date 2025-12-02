#!/bin/bash
# Validate backend optimizations before handoff
# Usage: ./scripts/validate-backend-optimizations.sh services/companion-service-go

set -e

SERVICE_DIR=$1

if [ -z "$SERVICE_DIR" ]; then
    echo "Usage: $0 <service-directory>"
    echo "Example: $0 services/companion-service-go"
    exit 1
fi

if [ ! -d "$SERVICE_DIR" ]; then
    echo "❌ Directory not found: $SERVICE_DIR"
    exit 1
fi

cd "$SERVICE_DIR"

echo ""
echo "🔍 Validating optimizations for: $SERVICE_DIR"
echo "================================================"
echo ""

ERRORS=0
WARNINGS=0

# 1. Struct alignment
echo "📐 Checking struct alignment..."
if command -v fieldalignment >/dev/null 2>&1; then
    if fieldalignment ./... 2>&1 | grep -q "struct"; then
        echo "⚠️  WARNING: Struct alignment can be improved"
        fieldalignment ./... | head -10
        WARNINGS=$((WARNINGS + 1))
    else
        echo "✅ Struct alignment: OK"
    fi
else
    echo "⚠️  fieldalignment not installed (go install golang.org/x/tools/go/analysis/passes/fieldalignment/cmd/fieldalignment@latest)"
fi

echo ""

# 2. Goroutine leaks
echo "🔍 Checking goroutine leaks..."
if go list -m | grep -q "go.uber.org/goleak"; then
    if go test -v -run TestMain ./... 2>&1 | grep -i "leak"; then
        echo "🔴 BLOCKER: Goroutine leaks detected!"
        ERRORS=$((ERRORS + 1))
    else
        echo "✅ No goroutine leaks"
    fi
else
    echo "⚠️  WARNING: goleak not in dependencies (recommended: go get go.uber.org/goleak)"
    WARNINGS=$((WARNINGS + 1))
fi

echo ""

# 3. Context timeouts
echo "⏱️  Checking context timeouts..."
TIMEOUT_COUNT=$(grep -r "context.WithTimeout\|context.WithDeadline" server/ 2>/dev/null | wc -l)
if [ "$TIMEOUT_COUNT" -eq 0 ]; then
    echo "🔴 BLOCKER: No context timeouts found in server/"
    ERRORS=$((ERRORS + 1))
else
    echo "✅ Context timeouts: $TIMEOUT_COUNT instances"
fi

echo ""

# 4. DB connection pool
echo "🗄️  Checking DB connection pool..."
if grep -r "SetMaxOpenConns\|SetMaxIdleConns" server/ >/dev/null 2>&1; then
    echo "✅ DB connection pool: configured"
else
    echo "⚠️  WARNING: DB connection pool not configured"
    WARNINGS=$((WARNINGS + 1))
fi

echo ""

# 5. Structured logging
echo "📝 Checking structured logging..."
if grep -r "fmt.Println\|log.Println" server/ >/dev/null 2>&1; then
    echo "⚠️  WARNING: Found fmt.Println/log.Println (use structured logger)"
    grep -r "fmt.Println\|log.Println" server/ | head -5
    WARNINGS=$((WARNINGS + 1))
else
    echo "✅ Structured logging: OK"
fi

echo ""

# 6. Memory pooling (для game servers)
echo "♻️  Checking memory pooling..."
POOL_COUNT=$(grep -r "sync.Pool" server/ 2>/dev/null | wc -l)
if [ "$POOL_COUNT" -eq 0 ]; then
    echo "⚠️  WARNING: No sync.Pool found (recommended for hot path)"
    WARNINGS=$((WARNINGS + 1))
else
    echo "✅ Memory pooling: $POOL_COUNT pools"
fi

echo ""

# 7. Benchmarks
echo "🏃 Running benchmarks..."
if go test -bench=. -benchmem ./... 2>&1 | grep -q "Benchmark"; then
    echo "✅ Benchmarks exist"
    
    # Check allocations
    ALLOC_ISSUES=$(go test -bench=. -benchmem ./... 2>&1 | grep "allocs/op" | awk '{if ($5 > 5) print $0}')
    if [ -n "$ALLOC_ISSUES" ]; then
        echo "⚠️  WARNING: High allocations detected:"
        echo "$ALLOC_ISSUES" | head -5
        WARNINGS=$((WARNINGS + 1))
    fi
else
    echo "⚠️  WARNING: No benchmarks found"
    WARNINGS=$((WARNINGS + 1))
fi

echo ""

# 8. Profiling endpoints
echo "📊 Checking profiling..."
if grep -r "net/http/pprof" . >/dev/null 2>&1; then
    echo "✅ Profiling: enabled"
else
    echo "⚠️  WARNING: pprof not imported"
    WARNINGS=$((WARNINGS + 1))
fi

echo ""

# Summary
echo "================================================"
echo "📊 SUMMARY"
echo "================================================"
echo ""
echo "🔴 BLOCKERS: $ERRORS"
echo "🟡 WARNINGS: $WARNINGS"
echo ""

if [ "$ERRORS" -gt 0 ]; then
    echo "🔴 VALIDATION FAILED - Fix blockers before handoff"
    echo ""
    echo "**Action:** Keep status 'Backend - In Progress'"
    exit 1
elif [ "$WARNINGS" -gt 3 ]; then
    echo "🟡 VALIDATION PASSED with warnings"
    echo ""
    echo "**Recommendation:** Fix warnings for better performance"
    echo "**Status:** Can handoff, but consider improvements"
    exit 0
else
    echo "✅ VALIDATION PASSED"
    echo ""
    echo "**Status:** Ready for handoff to Network/QA"
    exit 0
fi

