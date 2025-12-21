#!/bin/bash
# NECPGAME Script Type Enforcement Validator
# Blocks forbidden script types, allowing only Python

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Forbidden script extensions (only Python allowed)
FORBIDDEN_EXTENSIONS=(
    ".sh"
    ".ps1"
    ".bat"
    ".cmd"
    ".pl"
    ".rb"
    ".js"
    ".php"
    ".lua"
)

# Allowed directories for non-Python scripts (system infrastructure)
ALLOWED_SYSTEM_DIRS=(
    ".githooks/"
    "infrastructure/"
    "scripts/git-security/"
    "scripts/linting/"
)

# Specific allowed files (legacy scripts that are hard to migrate)
ALLOWED_FILES=(
    "scripts/certs/generate-envoy-certs.sh"
    "scripts/deploy/check-status.sh"
    "scripts/deploy/deploy-all.sh"
    "scripts/deploy/deploy-observability.sh"
    "scripts/deploy/rollback-service.sh"
    "scripts/local/check-local-infrastructure.sh"
    "scripts/db/apply-all-migrations.sh"
    "scripts/validate-backend-optimizations.sh"
    "scripts/validate-emoji-ban.sh"
    "scripts/generate-content-migrations.sh"
    "scripts/lint.sh"
    "scripts/db/apply-all-migrations.ps1"
    "scripts/db/apply-migrations-direct.ps1"
    "scripts/db/generate-content-changelog.ps1"
    "scripts/generate-content-migrations.ps1"
    "scripts/lint.ps1"
    "scripts/testing/run-findlimit-with-results.ps1"
    "scripts/testing/run-findlimit.ps1"
    "scripts/testing/run-loadtest.ps1"
    "scripts/validate-emoji-ban.bat"
)

# Function to check if file is in allowed system directory or specific allowed file
is_system_file() {
    local file="$1"

    # Check allowed directories
    for allowed_dir in "${ALLOWED_SYSTEM_DIRS[@]}"; do
        if [[ "$file" == ${allowed_dir}* ]]; then
            return 0
        fi
    done

    # Check specific allowed files
    for allowed_file in "${ALLOWED_FILES[@]}"; do
        if [[ "$file" == "$allowed_file" ]]; then
            return 0
        fi
    done

    return 1
}

# Function to validate a single file
validate_file() {
    local file="$1"

    # Skip if file doesn't exist
    if [ ! -f "$file" ]; then
        return 0
    fi

    # Skip system files
    if is_system_file "$file"; then
        return 0
    fi

    # Skip if not in scripts/ directory (only enforce in scripts/)
    if [[ "$file" != scripts/* ]]; then
        return 0
    fi

    # Check if file has forbidden extension
    local filename=$(basename "$file")
    local extension="${filename##*.}"

    # If it's a script without extension, check if it's executable
    if [[ "$filename" != *.* ]]; then
        if [ -x "$file" ]; then
            echo -e "${RED}[FORBIDDEN] Executable script without extension: $file${NC}"
            echo "All scripts must have .py extension (Python only)"
            return 1
        fi
        return 0
    fi

    # Check forbidden extensions
    for forbidden_ext in "${FORBIDDEN_EXTENSIONS[@]}"; do
        if [[ ".$extension" == "$forbidden_ext" ]]; then
            echo -e "${RED}[FORBIDDEN] Forbidden script type detected: $file${NC}"
            echo "Extension: $forbidden_ext"
            return 1
        fi
    done

    return 0
}

# Main validation function
main() {
    echo -e "${BLUE}[CHECK] Validating script types (Python only policy)...${NC}"

    local files_to_check=("$@")
    local has_errors=0

    if [ ${#files_to_check[@]} -eq 0 ]; then
        echo -e "${GREEN}[INFO] No files to check${NC}"
        exit 0
    fi

    for file in "${files_to_check[@]}"; do
        if ! validate_file "$file"; then
            has_errors=1
        fi
    done

    echo -e "${BLUE}==================================================${NC}"

    if [ $has_errors -eq 1 ]; then
        echo -e "${RED}[CRITICAL] FORBIDDEN SCRIPT TYPES DETECTED!${NC}"
        echo ""
        echo -e "${YELLOW}SCRIPT LANGUAGE ENFORCEMENT POLICY:${NC}"
        echo "• OK ALLOWED: .py (Python scripts only)"
        echo "• ❌ FORBIDDEN: .sh, .ps1, .bat, .cmd, .pl, .rb, .js, etc."
        echo ""
        echo -e "${YELLOW}WHY THIS POLICY EXISTS:${NC}"
        echo "• Python is cross-platform (works on Windows/Linux/macOS)"
        echo "• Better error handling and debugging"
        echo "• Easier testing and maintenance"
        echo "• Single language reduces complexity"
        echo ""
        echo -e "${YELLOW}HOW TO FIX:${NC}"
        echo "1. Convert shell scripts to Python equivalents"
        echo "2. Use 'python scripts/framework.py' for new scripts"
        echo "3. Remove old shell scripts after conversion"
        echo ""
        echo -e "${YELLOW}ALLOWED EXCEPTIONS (system infrastructure only):${NC}"
        echo "• .githooks/*.sh - Git hooks"
        echo "• infrastructure/**/*.sh - Infrastructure automation"
        echo "• scripts/git-security/*.bat - Git security tools"
        echo "• scripts/linting/* - Build tools"
        echo "• Specific legacy scripts (see ALLOWED_FILES in validate-script-types.sh)"
        echo ""
        echo -e "${RED}COMMIT BLOCKED - CONVERT SCRIPTS TO PYTHON FIRST${NC}"
        exit 1
    else
        echo -e "${GREEN}[SUCCESS] All scripts are Python or allowed system scripts${NC}"
        exit 0
    fi
}

# Run main function with all arguments
main "$@"
