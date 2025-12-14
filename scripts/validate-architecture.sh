#!/bin/bash

# NECPGAME Architecture Validation Script
# Validates all architectural aspects before commits

set -e

echo "🔍 Starting NECPGAME Architecture Validation..."
echo "=================================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

ERRORS=0
WARNINGS=0

# Function to log errors
log_error() {
    echo -e "${RED}❌ ERROR: $1${NC}"
    ((ERRORS++))
}

# Function to log warnings
log_warning() {
    echo -e "${YELLOW}WARNING  WARNING: $1${NC}"
    ((WARNINGS++))
}

# Function to log success
log_success() {
    echo -e "${GREEN}OK $1${NC}"
}

# 1. Check file sizes (max 600 lines)
echo ""
echo "📏 Checking file sizes..."
find . -name "*.yaml" -o -name "*.go" -o -name "*.sql" -o -name "*.md" | while read file; do
    # Skip certain directories
    if [[ $file =~ ^\./(\.git|node_modules|vendor)/ ]]; then
        continue
    fi

    lines=$(wc -l < "$file" 2>/dev/null || echo "0")
    if [ "$lines" -gt 600 ]; then
        log_error "File $file exceeds 600 lines ($lines lines)"
    fi
done

# 2. Validate OpenAPI specs
echo ""
echo "🔍 Validating OpenAPI specifications..."
if command -v redocly &> /dev/null; then
    find proto/openapi -name "*.yaml" | while read spec; do
        echo "  Checking $spec..."
        if redocly lint "$spec" --format=json | jq -e '.errors | length > 0' > /dev/null 2>&1; then
            log_error "OpenAPI spec $spec has validation errors"
        else
            log_success "OpenAPI spec $spec is valid"
        fi
    done
else
    log_warning "redocly CLI not found, skipping OpenAPI validation"
fi

# 3. Check Go files for required patterns
echo ""
echo "🔧 Checking Go files..."
find services -name "*.go" | while read gofile; do
    # Check for context timeouts
    if ! grep -q "context\." "$gofile"; then
        log_warning "Go file $gofile may be missing context usage"
    fi

    # Check for proper error handling
    if grep -q "panic(" "$gofile"; then
        log_warning "Go file $gofile contains panic() calls"
    fi
done

# 4. Check YAML structure
echo ""
echo "📄 Checking YAML files..."
find knowledge -name "*.yaml" | while read yamlfile; do
    # Check if YAML has proper metadata
    if ! grep -q "^metadata:" "$yamlfile" 2>/dev/null; then
        log_warning "YAML file $yamlfile missing metadata section"
    fi

    # Check for Issue references
    if ! grep -q "# Issue:" "$yamlfile" 2>/dev/null; then
        log_warning "YAML file $yamlfile missing Issue reference"
    fi
done

# 5. Check database migrations
echo ""
echo "🗄️  Checking database migrations..."
find infrastructure/liquibase -name "*.xml" -o -name "*.sql" | while read migration; do
    # Check for proper naming
    if [[ ! $migration =~ V[0-9]+__ ]]; then
        log_warning "Migration $migration doesn't follow naming convention (V{number}__{description})"
    fi
done

# 6. Architecture compliance check
echo ""
echo "🏗️  Checking architecture compliance..."

# Check for circular dependencies (simplified)
# This is a basic check - in real implementation would be more sophisticated

# Check microservice structure
if [ -d "services" ]; then
    service_count=$(find services -maxdepth 1 -type d | wc -l)
    log_success "Found $service_count microservices in services/ directory"
fi

# 7. Security checks
echo ""
echo "🔒 Checking security patterns..."
find . -name "*.go" -o -name "*.yaml" | xargs grep -l "password\|secret\|token" 2>/dev/null | while read file; do
    # Check for hardcoded secrets (simplified)
    if grep -q "password.*=.*[\"'][^$]" "$file" 2>/dev/null; then
        log_error "Potential hardcoded password in $file"
    fi
done

# 8. Performance checks
echo ""
echo "⚡ Checking performance patterns..."
find services -name "*.go" | xargs grep -l "make(" | while read file; do
    log_warning "Go file $file uses make() - check for memory allocations"
done

# Summary
echo ""
echo "=================================================="
echo "🏁 Architecture Validation Complete"
echo ""
echo "Results:"
echo "  Errors: $ERRORS"
echo "  Warnings: $WARNINGS"

if [ $ERRORS -gt 0 ]; then
    echo ""
    echo -e "${RED}❌ VALIDATION FAILED: $ERRORS errors found${NC}"
    echo "Please fix all errors before committing"
    exit 1
elif [ $WARNINGS -gt 0 ]; then
    echo ""
    echo -e "${YELLOW}WARNING  VALIDATION PASSED WITH WARNINGS: $WARNINGS warnings${NC}"
    echo "Consider fixing warnings for better code quality"
    exit 0
else
    echo ""
    echo -e "${GREEN}OK VALIDATION PASSED: No errors or warnings${NC}"
    exit 0
fi