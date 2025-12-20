#!/bin/bash
# Issue: #1858
# File structure and size validation script
# Validates file sizes, structure, and naming conventions

set -e

echo "📁 Starting file structure validation..."

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

# Function to validate Go file sizes and structure
validate_go_files() {
    echo "🐹 Validating Go files..."

    # Check file sizes (1000 lines max)
    while IFS= read -r -d '' file; do
        # Skip bundled/generated files
        if [[ "$file" =~ oas_.*\.go$ ]] || [[ "$file" =~ _gen\.go$ ]] || [[ "$file" =~ \.pb\.go$ ]]; then
            continue
        fi

        lines=$(wc -l < "$file")
        if [ "$lines" -gt 1000 ]; then
            log_error "Go file $file exceeds 1000 lines limit ($lines lines)"
        elif [ "$lines" -gt 350 ]; then
            log_warning "Go file $file is large ($lines lines, recommended <350)"
        fi
    done < <(find "$REPO_ROOT/services" -name "*.go" -type f -print0)

    # Check for proper package declarations
    while IFS= read -r -d '' file; do
        if ! head -5 "$file" | grep -q "^package "; then
            log_error "Go file $file missing package declaration"
        fi
    done < <(find "$REPO_ROOT/services" -name "*.go" -type f -print0)

    # Check for imports formatting
    while IFS= read -r -d '' file; do
        if grep -q "^import (" "$file"; then
            # Check if imports are properly grouped
            if ! grep -A 50 "^import (" "$file" | grep -q "^)" | head -1; then
                log_warning "Go file $file has improperly formatted imports"
            fi
        fi
    done < <(find "$REPO_ROOT/services" -name "*.go" -type f -print0)
}

# Function to validate YAML file sizes and structure
validate_yaml_files() {
    echo "📄 Validating YAML files..."

    # Check knowledge/canon YAML files (1000 lines max)
    while IFS= read -r -d '' file; do
        # Skip bundled/generated files
        if [[ "$file" =~ bundled\.yaml$ ]] || [[ "$file" =~ changelog.*\.yaml$ ]] || [[ "$file" =~ readiness-tracker\.yaml$ ]] || [[ "$file" =~ docker-compose\.yml$ ]] || [[ "$file" =~ oas_.*\.go$ ]] || [[ "$file" =~ _gen\.go$ ]] || [[ "$file" =~ \.pb\.go$ ]]; then
            continue
        fi

        lines=$(wc -l < "$file")
        if [ "$lines" -gt 1000 ]; then
            log_error "Content YAML file $file exceeds 1000 lines limit ($lines lines)"
        elif [ "$lines" -gt 400 ]; then
            log_warning "Content YAML file $file is large ($lines lines)"
        fi

        # Validate YAML syntax
        if ! python3 -c "import yaml; yaml.safe_load(open('$file'))" 2>/dev/null; then
            log_error "Invalid YAML syntax in $file"
        fi

        # Check for required metadata
        if ! grep -q "metadata:" "$file"; then
            log_warning "YAML file $file missing metadata section"
        fi

        if ! grep -q "id:" "$file"; then
            log_warning "YAML file $file missing id field"
        fi
    done < <(find "$REPO_ROOT/knowledge/canon" -name "*.yaml" -type f -print0)

    # Check OpenAPI YAML files
    while IFS= read -r -d '' file; do
        # Skip bundled/generated files
        if [[ "$file" =~ bundled\.yaml$ ]] || [[ "$file" =~ oas_.*\.go$ ]] || [[ "$file" =~ /benchmarks/.*_test\.go$ ]] || [[ "$file" =~ changelog.*\.yaml$ ]] || [[ "$file" =~ readiness-tracker\.yaml$ ]] || [[ "$file" =~ docker-compose\.yml$ ]] || [[ "$file" =~ _gen\.go$ ]] || [[ "$file" =~ \.pb\.go$ ]]; then
            continue
        fi

        lines=$(wc -l < "$file")
        if [ "$lines" -gt 1000 ]; then
            log_error "OpenAPI YAML file $file exceeds 1000 lines limit ($lines lines)"
        fi
    done < <(find "$REPO_ROOT" -name "openapi*.yaml" -o -name "*spec*.yaml" -o -name "*api*.yaml" -type f -print0)
}

# Function to validate SQL file sizes and structure
validate_sql_files() {
    echo "🗄️  Validating SQL files..."

    # Check migration file sizes
    while IFS= read -r -d '' file; do
        size=$(stat -f%z "$file" 2>/dev/null || stat -c%s "$file" 2>/dev/null)
        if [ "$size" -gt 2097152 ]; then # 2MB
            log_error "SQL migration file $file is too large ($size bytes > 2MB)"
        elif [ "$size" -gt 1048576 ]; then # 1MB
            log_warning "SQL migration file $file is large ($size bytes > 1MB)"
        fi

        # Check for proper Issue comments
        if ! grep -q "^-- Issue:" "$file"; then
            log_warning "SQL file $file missing Issue comment"
        fi
    done < <(find "$REPO_ROOT/infrastructure/liquibase/migrations" -name "*.sql" -type f -print0)

    # Check for proper SQL formatting (basic)
    while IFS= read -r -d '' file; do
        # Check for missing semicolons (simplified)
        if grep -q "INSERT\|UPDATE\|DELETE\|CREATE\|ALTER\|DROP" "$file"; then
            last_line=$(tail -1 "$file" | tr -d '[:space:]')
            if [[ "$last_line" != *";" ]]; then
                log_warning "SQL file $file may be missing semicolon on last statement"
            fi
        fi
    done < <(find "$REPO_ROOT/infrastructure/liquibase/migrations" -name "*.sql" -type f -print0)
}

# Function to validate directory structure
validate_directory_structure() {
    echo "📂 Validating directory structure..."

    # Check for required directories
    required_dirs=(
        "services"
        "infrastructure/liquibase/migrations"
        "knowledge/canon"
        "infrastructure/scripts"
    )

    for dir in "${required_dirs[@]}"; do
        if [ ! -d "$REPO_ROOT/$dir" ]; then
            log_error "Required directory missing: $dir"
        else
            log_success "Directory exists: $dir"
        fi
    done

    # Check for proper service structure
    while IFS= read -r service_dir; do
        service_name=$(basename "$service_dir")

        # Check for required service files
        if [ ! -f "$service_dir/go.mod" ]; then
            log_warning "Service $service_name missing go.mod"
        fi

        if [ ! -f "$service_dir/main.go" ]; then
            log_warning "Service $service_name missing main.go"
        fi

        if [ ! -d "$service_dir/pkg/api" ]; then
            log_warning "Service $service_name missing pkg/api directory"
        fi
    done < <(find "$REPO_ROOT/services" -maxdepth 1 -type d | tail -n +2)
}

# Function to validate file naming conventions
validate_naming_conventions() {
    echo "🏷️  Validating naming conventions..."

    # Check Go file naming (snake_case)
    while IFS= read -r -d '' file; do
        filename=$(basename "$file")
        if [[ "$filename" =~ [A-Z] ]]; then
            log_error "Go file $filename contains uppercase letters (use snake_case)"
        fi
    done < <(find "$REPO_ROOT/services" -name "*.go" -type f -print0)

    # Check YAML file naming
    while IFS= read -r -d '' file; do
        filename=$(basename "$file")
        if [[ "$filename" =~ [A-Z] ]] && [[ "$filename" != *"API"* ]]; then
            log_warning "YAML file $filename contains uppercase letters"
        fi
    done < <(find "$REPO_ROOT" -name "*.yaml" -type f -print0)

    # Check script naming
    while IFS= read -r -d '' file; do
        filename=$(basename "$file")
        if [[ "$filename" =~ [A-Z] ]]; then
            log_error "Script file $filename contains uppercase letters (use kebab-case)"
        fi
    done < <(find "$REPO_ROOT/infrastructure/scripts" -name "*.sh" -type f -print0)
}

# Function to generate validation report
generate_report() {
    echo "📊 Generating validation report..."

    REPORT_FILE="$REPO_ROOT/infrastructure/reports/file-structure-validation-$(date +%Y%m%d-%H%M%S).txt"

    {
        echo "NECPGAME File Structure Validation Report"
        echo "========================================="
        echo "Generated: $(date)"
        echo ""
        echo "Summary:"
        echo "  Errors: $ERRORS"
        echo "  Warnings: $WARNINGS"
        echo ""
        echo "Validated categories:"
        echo "  - Go files (size, structure, naming)"
        echo "  - YAML files (size, syntax, metadata)"
        echo "  - SQL files (size, formatting)"
        echo "  - Directory structure"
        echo "  - Naming conventions"
    } > "$REPORT_FILE"

    log_success "Report generated: $REPORT_FILE"
}

# Main validation execution
main() {
    echo "🔍 NECPGAME File Structure Validator"
    echo "==================================="

    # Create reports directory
    mkdir -p "$REPO_ROOT/infrastructure/reports"

    validate_go_files
    validate_yaml_files
    validate_sql_files
    validate_directory_structure
    validate_naming_conventions

    generate_report

    echo ""
    echo "📊 Validation Summary:"
    echo "  Errors: $ERRORS"
    echo "  Warnings: $WARNINGS"

    if [ "$ERRORS" -gt 0 ]; then
        echo -e "${RED}❌ File structure validation FAILED with $ERRORS errors${NC}"
        exit 1
    else
        echo -e "${GREEN}OK File structure validation PASSED${NC}"
        if [ "$WARNINGS" -gt 0 ]; then
            echo -e "${YELLOW}WARNING  $WARNINGS warnings found - consider addressing them${NC}"
        fi
    fi
}

# Run main function
main "$@"