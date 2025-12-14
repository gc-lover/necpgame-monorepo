#!/bin/bash
# Issue: #1858
# OpenAPI specification validation script
# Validates OpenAPI specs using redocly and custom rules

set -e

echo "📋 Starting OpenAPI specification validation..."

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(dirname "$SCRIPT_DIR")"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

ERRORS=0
WARNINGS=0

log_error() {
    echo -e "${RED}❌ ERROR: $1${NC}" >&2
    ((ERRORS++))
}

log_warning() {
    echo -e "${YELLOW}WARNING  WARNING: $1${NC}"
    ((WARNINGS++))
}

log_success() {
    echo -e "${GREEN}OK $1${NC}"
}

# Function to validate OpenAPI specs with redocly
validate_with_redocly() {
    echo "🔍 Validating with redocly..."

    # Find all OpenAPI spec files
    find "$REPO_ROOT" -name "openapi*.yaml" -o -name "*spec*.yaml" -o -name "*api*.yaml" | while read -r spec_file; do
        echo "  Checking $spec_file..."

        # Check if redocly is available
        if command -v redocly &> /dev/null; then
            if redocly lint "$spec_file" 2>/dev/null; then
                log_success "Redocly validation passed for $(basename "$spec_file")"
            else
                log_error "Redocly validation failed for $(basename "$spec_file")"
            fi
        else
            log_warning "redocly not found, skipping validation for $(basename "$spec_file")"
        fi
    done
}

# Function to validate OpenAPI structure and content
validate_openapi_structure() {
    echo "🏗️  Validating OpenAPI structure and content..."

    find "$REPO_ROOT" -name "openapi*.yaml" -o -name "*spec*.yaml" -o -name "*api*.yaml" | while read -r spec_file; do
        echo "  Analyzing $(basename "$spec_file")..."

        # Check if file is valid YAML
        if ! python3 -c "import yaml; yaml.safe_load(open('$spec_file'))" 2>/dev/null; then
            log_error "Invalid YAML syntax in $spec_file"
            continue
        fi

        # Check for required OpenAPI fields
        if ! grep -q "openapi:" "$spec_file"; then
            log_error "Missing openapi version in $spec_file"
        fi

        if ! grep -q "info:" "$spec_file"; then
            log_error "Missing info section in $spec_file"
        fi

        if ! grep -q "paths:" "$spec_file"; then
            log_error "Missing paths section in $spec_file"
        fi

        # Check for NECPGAME-specific patterns
        if ! grep -q "schemas:" "$spec_file"; then
            log_warning "No schemas section found in $spec_file (recommended for struct alignment)"
        fi

        # Check for rate limiting definitions
        if ! grep -q "RateLimit\|rate.limit" "$spec_file"; then
            log_warning "No rate limiting defined in $spec_file"
        fi

        # Check for error responses
        if ! grep -q "400\|401\|403\|404\|500" "$spec_file"; then
            log_warning "Limited error response definitions in $spec_file"
        fi

        log_success "Structure validation passed for $(basename "$spec_file")"
    done
}

# Function to validate schema alignment (memory optimization)
validate_schema_alignment() {
    echo "📐 Validating schema alignment for memory optimization..."

    find "$REPO_ROOT" -name "openapi*.yaml" -o -name "*spec*.yaml" -o -name "*api*.yaml" | while read -r spec_file; do
        echo "  Checking schema alignment in $(basename "$spec_file")..."

        # Check for struct alignment patterns (large fields first)
        # This is a simplified check - in practice would need more sophisticated analysis

        if grep -q "type.*string" "$spec_file" && grep -q "type.*integer" "$spec_file"; then
            # Check if large fields come before small ones (simplified)
            if grep -A 10 -B 2 "properties:" "$spec_file" | grep -q "type.*string.*type.*integer"; then
                log_success "Schema alignment looks good in $(basename "$spec_file")"
            else
                log_warning "Consider optimizing field order for memory alignment in $(basename "$spec_file")"
            fi
        fi
    done
}

# Function to validate API consistency
validate_api_consistency() {
    echo "🔗 Validating API consistency across services..."

    # Check for consistent error response formats
    error_formats=$(find "$REPO_ROOT" -name "openapi*.yaml" -o -name "*spec*.yaml" -o -name "*api*.yaml" -exec grep -l "responses:" {} \; | wc -l)
    total_specs=$(find "$REPO_ROOT" -name "openapi*.yaml" -o -name "*spec*.yaml" -o -name "*api*.yaml" | wc -l)

    if [ "$error_formats" -ne "$total_specs" ]; then
        log_warning "Not all API specs define error responses ($error_formats/$total_specs)"
    fi

    # Check for consistent pagination patterns
    pagination_count=$(find "$REPO_ROOT" -name "openapi*.yaml" -o -name "*spec*.yaml" -o -name "*api*.yaml" -exec grep -l "page\|limit\|offset" {} \; | wc -l)

    if [ "$pagination_count" -gt 0 ]; then
        log_success "Found pagination patterns in $pagination_count specs"
    else
        log_warning "No pagination patterns found in API specs"
    fi
}

# Function to generate validation report
generate_report() {
    echo "📊 Generating validation report..."

    REPORT_FILE="$REPO_ROOT/infrastructure/reports/openapi-validation-$(date +%Y%m%d-%H%M%S).txt"

    {
        echo "NECPGAME OpenAPI Validation Report"
        echo "=================================="
        echo "Generated: $(date)"
        echo ""
        echo "Summary:"
        echo "  Errors: $ERRORS"
        echo "  Warnings: $WARNINGS"
        echo ""
        echo "Checked files:"
        find "$REPO_ROOT" -name "openapi*.yaml" -o -name "*spec*.yaml" -o -name "*api*.yaml" | while read -r file; do
            echo "  - $(basename "$file")"
        done
    } > "$REPORT_FILE"

    log_success "Report generated: $REPORT_FILE"
}

# Main validation execution
main() {
    echo "🔍 NECPGAME OpenAPI Specification Validator"
    echo "==========================================="

    # Create reports directory
    mkdir -p "$REPO_ROOT/infrastructure/reports"

    validate_with_redocly
    validate_openapi_structure
    validate_schema_alignment
    validate_api_consistency

    generate_report

    echo ""
    echo "📊 Validation Summary:"
    echo "  Errors: $ERRORS"
    echo "  Warnings: $WARNINGS"

    if [ "$ERRORS" -gt 0 ]; then
        echo -e "${RED}❌ OpenAPI validation FAILED with $ERRORS errors${NC}"
        exit 1
    else
        echo -e "${GREEN}OK OpenAPI validation PASSED${NC}"
        if [ "$WARNINGS" -gt 0 ]; then
            echo -e "${YELLOW}WARNING  $WARNINGS warnings found - consider addressing them${NC}"
        fi
    fi
}

# Run main function
main "$@"