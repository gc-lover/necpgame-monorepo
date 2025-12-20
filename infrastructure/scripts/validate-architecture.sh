#!/bin/bash
# Issue: #1858
# Architecture compliance validation script
# Validates that code follows NECPGAME architectural rules

set -e

echo "🏗️  Starting architecture compliance validation..."

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

# Function to check file size limits
validate_file_sizes() {
    echo "📏 Checking file size limits..."

    # Go files: max 1000 lines
    while IFS= read -r -d '' file; do
        # Skip bundled/generated files
        if [[ $file =~ oas_.*\.go$ ]] || [[ $file =~ /benchmarks/.*_test\.go$ ]] || [[ $file =~ _gen\.go$ ]] || [[ $file =~ \.pb\.go$ ]] || [[ $file =~ docker-compose\.yml$ ]]; then
            continue
        fi

        lines=$(wc -l < "$file")
        if [ "$lines" -gt 1000 ]; then
            log_error "Go file $file exceeds 1000 lines limit ($lines lines)"
        fi
    done < <(find "$REPO_ROOT" -name "*.go" -type f -print0)

    # YAML files in knowledge/canon: max 1000 lines
    while IFS= read -r -d '' file; do
        # Skip bundled/generated files
        if [[ $file =~ bundled\.yaml$ ]] || [[ $file =~ changelog.*\.yaml$ ]] || [[ $file =~ readiness-tracker\.yaml$ ]] || [[ $file =~ docker-compose\.yml$ ]] || [[ $file =~ oas_.*\.go$ ]] || [[ $file =~ _gen\.go$ ]] || [[ $file =~ \.pb\.go$ ]]; then
            continue
        fi

        lines=$(wc -l < "$file")
        if [ "$lines" -gt 1000 ]; then
            log_error "Content YAML file $file exceeds 1000 lines limit ($lines lines)"
        fi
    done < <(find "$REPO_ROOT/knowledge/canon" -name "*.yaml" -type f -print0)

    # SQL migration files: reasonable size check
    while IFS= read -r -d '' file; do
        size=$(stat -f%z "$file" 2>/dev/null || stat -c%s "$file" 2>/dev/null)
        if [ "$size" -gt 1048576 ]; then # 1MB
            log_warning "SQL migration file $file is very large ($size bytes)"
        fi
    done < <(find "$REPO_ROOT/infrastructure/liquibase/migrations" -name "*.sql" -type f -print0)
}

# Function to validate architectural patterns
validate_architecture_patterns() {
    echo "🏛️  Checking architectural patterns..."

    # Check that services don't import each other inappropriately
    # (simplified check - in real implementation would be more sophisticated)

    # Check for direct database access in handlers (should go through repository/service)
    while IFS= read -r -d '' file; do
        if grep -q "pgxpool.Pool" "$file" && grep -q "func.*Handler" "$file"; then
            if ! grep -q "Repository\|Service" "$file"; then
                log_warning "Handler in $file accesses database directly - should use repository/service layer"
            fi
        fi
    done < <(find "$REPO_ROOT/services" -name "*.go" -type f -print0)

    # Check for proper error handling in handlers
    while IFS= read -r -d '' file; do
        if grep -q "func.*Handler" "$file"; then
            if ! grep -q "context\.WithTimeout\|DBTimeout" "$file"; then
                log_warning "Handler in $file missing context timeout validation"
            fi
        fi
    done < <(find "$REPO_ROOT/services" -name "handlers*.go" -type f -print0)
}

# Function to validate naming conventions
validate_naming_conventions() {
    echo "🏷️  Checking naming conventions..."

    # Check Go file naming
    while IFS= read -r -d '' file; do
        filename=$(basename "$file")
        if [[ "$filename" =~ ^[A-Z] ]]; then
            log_error "Go file $file starts with uppercase letter (should be lowercase)"
        fi
    done < <(find "$REPO_ROOT/services" -name "*.go" -type f -print0)

    # Check for proper package naming
    if find "$REPO_ROOT" -name "go.mod" -exec grep -l "module.*[A-Z]" {} \;; then
        log_error "Found go.mod with uppercase in module name"
    fi
}

# Function to validate security patterns
validate_security_patterns() {
    echo "🔒 Checking security patterns..."

    # Check for hardcoded secrets (simplified)
    while IFS= read -r -d '' file; do
        if grep -q -i "password\|secret\|key.*123\|admin.*admin" "$file"; then
            if [[ "$file" != *"test"* ]] && [[ "$file" != *"config"* ]]; then
                log_warning "Potential hardcoded secret found in $file"
            fi
        fi
    done < <(find "$REPO_ROOT/services" -name "*.go" -type f -print0)

    # Check for SQL injection vulnerabilities (simplified)
    while IFS= read -r -d '' file; do
        if grep -q "fmt\.Sprintf.*SELECT\|fmt\.Sprintf.*INSERT\|fmt\.Sprintf.*UPDATE\|fmt\.Sprintf.*DELETE" "$file"; then
            log_error "Potential SQL injection vulnerability in $file (string formatting in SQL)"
        fi
    done < <(find "$REPO_ROOT/services" -name "*.go" -type f -print0)
}

# Function to validate performance patterns
validate_performance_patterns() {
    echo "⚡ Checking performance patterns..."

    # Check for inefficient operations
    while IFS= read -r -d '' file; do
        if grep -q "append.*range\|for.*append" "$file"; then
            log_warning "Potential slice allocation inefficiency in $file"
        fi
    done < <(find "$REPO_ROOT/services" -name "*.go" -type f -print0)

    # Check for missing memory pooling in hot paths
    while IFS= read -r -d '' file; do
        if grep -q "sync\.Pool" "$file"; then
            log_success "Memory pooling found in $file"
        fi
    done < <(find "$REPO_ROOT/services" -name "handlers*.go" -type f -print0)
}

# Function to validate code quality
validate_code_quality() {
    echo "🧹 Checking code quality patterns..."

    # Check for TODO/FIXME comments
    todo_count=$(find "$REPO_ROOT" -name "*.go" -type f -exec grep -l "TODO\|FIXME\|XXX" {} \; | wc -l)
    if [ "$todo_count" -gt 0 ]; then
        log_warning "Found $todo_count files with TODO/FIXME comments"
    fi

    # Check for proper logging
    while IFS= read -r -d '' file; do
        if grep -q "fmt\.Print" "$file" && ! grep -q "logrus\|zap" "$file"; then
            log_warning "Using fmt.Print instead of structured logging in $file"
        fi
    done < <(find "$REPO_ROOT/services" -name "*.go" -type f -print0)
}

# Main validation execution
main() {
    echo "🔍 NECPGAME Architecture Compliance Validator"
    echo "=============================================="

    validate_file_sizes
    validate_architecture_patterns
    validate_naming_conventions
    validate_security_patterns
    validate_performance_patterns
    validate_code_quality

    echo ""
    echo "📊 Validation Summary:"
    echo "  Errors: $ERRORS"
    echo "  Warnings: $WARNINGS"

    if [ "$ERRORS" -gt 0 ]; then
        echo -e "${RED}❌ Architecture validation FAILED with $ERRORS errors${NC}"
        exit 1
    else
        echo -e "${GREEN}OK Architecture validation PASSED${NC}"
        if [ "$WARNINGS" -gt 0 ]; then
            echo -e "${YELLOW}WARNING  $WARNINGS warnings found - consider addressing them${NC}"
        fi
    fi
}

# Run main function
main "$@"