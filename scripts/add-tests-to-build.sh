#!/bin/bash
# Issue: Add tests and benchmarks to build process
# Обновляет все Makefile чтобы build запускал тесты и бенчмарки

set -e

SERVICES_DIR="services"
UPDATED=0
SKIPPED=0
ERRORS=0

echo "Updating Makefiles to run tests and benchmarks on build..."
echo ""

for service_dir in "$SERVICES_DIR"/*-go; do
    if [ ! -d "$service_dir" ]; then
        continue
    fi
    
    service_name=$(basename "$service_dir")
    makefile="$service_dir/Makefile"
    
    if [ ! -f "$makefile" ]; then
        echo "  [SKIP] $service_name - no Makefile"
        SKIPPED=$((SKIPPED + 1))
        continue
    fi
    
    # Check if build target exists
    if ! grep -q "^build:" "$makefile"; then
        echo "  [SKIP] $service_name - no build target"
        SKIPPED=$((SKIPPED + 1))
        continue
    fi
    
    # Check if already has test/bench in build
    if grep -q "^build:.*test\|^build:.*bench" "$makefile"; then
        echo "  [OK] $service_name - already has tests/benchmarks"
        SKIPPED=$((SKIPPED + 1))
        continue
    fi
    
    echo "  [UPDATE] $service_name"
    
    # Update build target to include test and bench-quick
    if grep -q "^build:" "$makefile"; then
        # Get current build dependencies
        build_line=$(grep "^build:" "$makefile" | head -1)
        deps=$(echo "$build_line" | sed 's/^build://' | sed 's/^[[:space:]]*//')
        
        # Add test and bench-quick as dependencies
        if [ -n "$deps" ]; then
            new_build="build: test bench-quick $deps"
        else
            new_build="build: test bench-quick"
        fi
        
        # Replace build line
        sed -i.bak "s|^build:.*|$new_build|" "$makefile"
        rm -f "$makefile.bak"
        
        # Ensure test target exists
        if ! grep -q "^test:" "$makefile"; then
            echo "" >> "$makefile"
            echo ".PHONY: test" >> "$makefile"
            echo "test:" >> "$makefile"
            echo "	@go test -v ./..." >> "$makefile"
        fi
        
        # Ensure bench-quick target exists
        if ! grep -q "bench-quick:" "$makefile"; then
            echo "" >> "$makefile"
            echo ".PHONY: bench-quick" >> "$makefile"
            echo "bench-quick:" >> "$makefile"
            echo "	@if [ -f \"server/handlers_bench_test.go\" ] || find . -name \"*_bench_test.go\" | grep -q .; then \\" >> "$makefile"
            echo "		go test -run=^\$\$ -bench=. -benchmem -benchtime=100ms ./server; \\" >> "$makefile"
            echo "	fi" >> "$makefile"
        fi
        
        UPDATED=$((UPDATED + 1))
    else
        echo "    [ERROR] Could not update build target"
        ERRORS=$((ERRORS + 1))
    fi
done

echo ""
echo "Update complete!"
echo "  Updated: $UPDATED"
echo "  Skipped: $SKIPPED"
echo "  Errors: $ERRORS"

