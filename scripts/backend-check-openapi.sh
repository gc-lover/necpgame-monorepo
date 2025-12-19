#!/bin/bash
# Backend OpenAPI Validation Script
# Issue: #1878

set -e

SPEC_PATH=${1:-"proto/openapi"}

echo "🔍 Validating OpenAPI specifications..."
echo "=========================================="
echo ""

ERRORS=0
WARNINGS=0

# Find all OpenAPI files
find_openapi_files() {
    if [ -f "$SPEC_PATH" ]; then
        echo "$SPEC_PATH"
    elif [ -d "$SPEC_PATH" ]; then
        find "$SPEC_PATH" -name "*.yaml" -o -name "*.yml" | sort
    else
        echo "❌ ERROR: Path not found: $SPEC_PATH"
        exit 1
    fi
}

# Validate single OpenAPI file
validate_openapi_file() {
    local file="$1"
    echo "📋 Validating: $file"

    # Check file size
    local lines=$(wc -l < "$file")
    if [ "$lines" -gt 500 ]; then
        echo "WARNING  WARNING: Spec exceeds 500 lines ($lines lines)"
        echo "   Consider splitting into modules"
        WARNINGS=$((WARNINGS + 1))
    fi

    # Spectral validation
    if command -v spectral >/dev/null 2>&1; then
        echo "  🔍 Running Spectral validation..."
        if ! spectral lint "$file" --ruleset .spectral.yaml 2>/dev/null; then
            echo "❌ ERROR: Spectral validation failed"
            spectral lint "$file" --ruleset .spectral.yaml || true
            ERRORS=$((ERRORS + 1))
            return 1
        fi
    else
        echo "WARNING  WARNING: Spectral not installed, skipping validation"
        WARNINGS=$((WARNINGS + 1))
    fi

    # ogen compatibility check
    if command -v ogen >/dev/null 2>&1; then
        echo "  🔧 Checking ogen compatibility..."
        if ! ogen validate "$file" 2>/dev/null; then
            echo "❌ ERROR: ogen validation failed"
            ogen validate "$file" || true
            ERRORS=$((ERRORS + 1))
            return 1
        fi
    else
        echo "WARNING  WARNING: ogen not installed, skipping compatibility check"
        WARNINGS=$((WARNINGS + 1))
    fi

    echo "OK $file validation passed"
    echo ""
}

# Main validation loop
for file in $(find_openapi_files); do
    if ! validate_openapi_file "$file"; then
        continue
    fi
done

# Summary
echo "=========================================="
echo "📊 VALIDATION SUMMARY"
echo "=========================================="
echo ""
echo "❌ Errors: $ERRORS"
echo "WARNING  Warnings: $WARNINGS"
echo ""

if [ "$ERRORS" -gt 0 ]; then
    echo "❌ VALIDATION FAILED"
    echo ""
    echo "Fix errors before proceeding with backend development:"
    echo "- Install spectral: npm install -g @stoplight/spectral-cli"
    echo "- Install ogen: go install github.com/ogen-go/ogen/cmd/ogen@latest"
    echo "- Fix OpenAPI spec issues"
    echo ""
    exit 1
elif [ "$WARNINGS" -gt 0 ]; then
    echo "WARNING  VALIDATION PASSED with warnings"
    echo ""
    echo "Consider addressing warnings for better code generation"
    exit 0
else
    echo "OK VALIDATION PASSED"
    echo ""
    echo "OpenAPI specs ready for backend development!"
    exit 0
fi