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
    echo "[ERROR] Directory not found: $SERVICE_DIR"
    exit 1
fi

cd "$SERVICE_DIR"

echo ""
echo "[SEARCH] Validating optimizations for: $SERVICE_DIR"
echo "================================================"
echo ""

ERRORS=0
WARNINGS=0

# 1. Struct alignment
echo "[SYMBOL] Checking struct alignment..."
if command -v fieldalignment >/dev/null 2>&1; then
    if fieldalignment ./... 2>&1 | grep -q "struct"; then
        echo "[WARNING]  WARNING: Struct alignment can be improved"
        fieldalignment ./... | head -10
        WARNINGS=$((WARNINGS + 1))
    else
        echo "[OK] Struct alignment: OK"
    fi
else
    echo "[WARNING]  fieldalignment not installed (go install golang.org/x/tools/go/analysis/passes/fieldalignment/cmd/fieldalignment@latest)"
fi

echo ""

# 2. Goroutine leaks
echo "[SEARCH] Checking goroutine leaks..."
if go list -m | grep -q "go.uber.org/goleak"; then
    if go test -v -run TestMain ./... 2>&1 | grep -i "leak"; then
        echo "[SYMBOL] BLOCKER: Goroutine leaks detected!"
        ERRORS=$((ERRORS + 1))
    else
        echo "[OK] No goroutine leaks"
    fi
else
    echo "[WARNING]  WARNING: goleak not in dependencies (recommended: go get go.uber.org/goleak)"
    WARNINGS=$((WARNINGS + 1))
fi

echo ""

# 3. Context timeouts
echo "⏱️  Checking context timeouts..."
TIMEOUT_COUNT=$(grep -r "context.WithTimeout\|context.WithDeadline" server/ 2>/dev/null | wc -l)
if [ "$TIMEOUT_COUNT" -eq 0 ]; then
    echo "[SYMBOL] BLOCKER: No context timeouts found in server/"
    ERRORS=$((ERRORS + 1))
else
    echo "[OK] Context timeouts: $TIMEOUT_COUNT instances"
fi

echo ""

# 4. DB connection pool
echo "[SYMBOL]️  Checking DB connection pool..."
if grep -r "SetMaxOpenConns\|SetMaxIdleConns" server/ >/dev/null 2>&1; then
    echo "[OK] DB connection pool: configured"
else
    echo "[WARNING]  WARNING: DB connection pool not configured"
    WARNINGS=$((WARNINGS + 1))
fi

echo ""

# 5. Structured logging
echo "[NOTE] Checking structured logging..."
if grep -r "fmt.Println\|log.Println" server/ >/dev/null 2>&1; then
    echo "[WARNING]  WARNING: Found fmt.Println/log.Println (use structured logger)"
    grep -r "fmt.Println\|log.Println" server/ | head -5
    WARNINGS=$((WARNINGS + 1))
else
    echo "[OK] Structured logging: OK"
fi

echo ""

# 6. Memory pooling (для game servers)
echo "[SYMBOL]️  Checking memory pooling..."
POOL_COUNT=$(grep -r "sync.Pool" server/ 2>/dev/null | wc -l)
if [ "$POOL_COUNT" -eq 0 ]; then
    echo "[WARNING]  WARNING: No sync.Pool found (recommended for hot path)"
    WARNINGS=$((WARNINGS + 1))
else
    echo "[OK] Memory pooling: $POOL_COUNT pools"
fi

echo ""

# 7. Benchmarks
echo "[SYMBOL] Running benchmarks..."
if go test -bench=. -benchmem ./... 2>&1 | grep -q "Benchmark"; then
    echo "[OK] Benchmarks exist"
    
    # Check allocations
    ALLOC_ISSUES=$(go test -bench=. -benchmem ./... 2>&1 | grep "allocs/op" | awk '{if ($5 > 5) print $0}')
    if [ -n "$ALLOC_ISSUES" ]; then
        echo "[WARNING]  WARNING: High allocations detected:"
        echo "$ALLOC_ISSUES" | head -5
        WARNINGS=$((WARNINGS + 1))
    fi
else
    echo "[WARNING]  WARNING: No benchmarks found"
    WARNINGS=$((WARNINGS + 1))
fi

echo ""

# 8. Profiling endpoints
echo "[SYMBOL] Checking profiling..."
if grep -r "net/http/pprof" . >/dev/null 2>&1; then
    echo "[OK] Profiling: enabled"
else
    echo "[WARNING]  WARNING: pprof not imported"
    WARNINGS=$((WARNINGS + 1))
fi

echo ""

# Summary
echo "================================================"
echo "[SYMBOL] SUMMARY"
echo "================================================"
echo ""
echo "[SYMBOL] BLOCKERS: $ERRORS"
echo "🟡 WARNINGS: $WARNINGS"
echo ""

if [ "$ERRORS" -gt 0 ]; then
    echo "[SYMBOL] VALIDATION FAILED - Fix blockers before handoff"
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
    echo "[OK] VALIDATION PASSED"
    echo ""
    echo "**Status:** Ready for handoff to Network/QA"
    exit 0
fi

