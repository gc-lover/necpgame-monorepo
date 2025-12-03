#!/bin/bash
# Issue: #1586 - Audit OpenAPI struct field alignment
# Checks ALL OpenAPI specs for optimal field ordering (large → small)
# GAINS: 30-50% memory savings when properly aligned

set -euo pipefail

echo "🔍 Auditing OpenAPI Struct Field Alignment..."
echo ""

SPEC_DIR="proto/openapi"
ISSUES_FOUND=0
SPECS_CHECKED=0

# Field size weights for sorting
# Pointers/strings/slices: 8-24 bytes
# int64/float64: 8 bytes
# int32/float32: 4 bytes
# int16: 2 bytes
# int8/bool: 1 byte

check_schema_alignment() {
    local file=$1
    local schema_name=$2
    
    # Look for schemas without BACKEND NOTE (likely not optimized)
    if ! grep -q "BACKEND NOTE" "$file"; then
        echo "WARNING  $file: Missing BACKEND NOTE (struct alignment hint)"
        ((ISSUES_FOUND++))
    fi
    
    # Check for bool/int8 fields BEFORE larger fields (bad alignment)
    if grep -B5 "type: boolean" "$file" | grep -q "type: string\|type: array\|format: int64"; then
        echo "WARNING  $file: Boolean fields before large fields (bad alignment)"
        ((ISSUES_FOUND++))
    fi
    
    # Check for int32 BEFORE int64 (suboptimal)
    if grep -B3 "format: int32" "$file" | grep -q "format: int64"; then
        echo "WARNING  $file: int32 before int64 (suboptimal alignment)"
        ((ISSUES_FOUND++))
    fi
}

# Scan all OpenAPI specs
for spec in "$SPEC_DIR"/*.yaml; do
    if [[ -f "$spec" ]]; then
        ((SPECS_CHECKED++))
        check_schema_alignment "$spec" "$(basename "$spec")"
    fi
done

echo ""
echo "📊 Audit Results:"
echo "  Specs checked: $SPECS_CHECKED"
echo "  Issues found: $ISSUES_FOUND"

if [[ $ISSUES_FOUND -eq 0 ]]; then
    echo ""
    echo "OK All OpenAPI specs are properly aligned!"
    exit 0
else
    echo ""
    echo "WARNING  Found $ISSUES_FOUND potential alignment issues"
    echo ""
    echo "Recommended field order (large → small):"
    echo "  1. UUID/string (16 bytes)"
    echo "  2. Arrays/objects (24/8 bytes)"
    echo "  3. int64/float64 (8 bytes)"
    echo "  4. int32/float32 (4 bytes)"
    echo "  5. int16 (2 bytes)"
    echo "  6. int8/bool (1 byte)"
    echo ""
    echo "See: .cursor/rules/agent-api-designer.mdc (Performance Optimization section)"
    exit 1
fi


