#!/bin/bash
# NECPGAME Git Hooks Installation Script
# Installs pre-commit and pre-push hooks for architecture validation
# Issue: #1866

set -e

HOOKS_DIR=".git/hooks"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

echo "🔧 Installing NECPGAME Git Hooks..."
echo "=================================="

# Create hooks directory if it doesn't exist
mkdir -p "$HOOKS_DIR"

# Install pre-commit hook
echo "📝 Installing pre-commit hook..."
cat > "$HOOKS_DIR/pre-commit" << 'EOF'
#!/bin/bash
# NECPGAME Pre-commit Hook
# Validates architecture before allowing commits
# Issue: #1866

echo "🔍 Running NECPGAME Architecture Validation..."

# Check if PowerShell is available (Windows)
if command -v pwsh >/dev/null 2>&1; then
    # Use PowerShell on Windows
    pwsh -ExecutionPolicy Bypass -File "scripts/validate-architecture-simple.ps1"
    exit $?
elif command -v powershell >/dev/null 2>&1; then
    # Use Windows PowerShell
    powershell -ExecutionPolicy Bypass -File "scripts/validate-architecture-simple.ps1"
    exit $?
else
    # Fallback to bash validation (Linux/Mac)
    echo "WARNING  PowerShell not found, running basic checks..."

    # Basic file size check
    find . -name "*.go" -o -name "*.yaml" -o -name "*.sql" -o -name "*.md" | grep -v -E '\.git|node_modules|vendor' | while read file; do
        lines=$(wc -l < "$file" 2>/dev/null || echo "0")
        if [ "$lines" -gt 1000 ]; then
            # Skip generated files
            if ! echo "$file" | grep -q -E '^.*oas_.*\.go$|\.bundled\.yaml$|changelog.*\.yaml$|readiness-tracker\.yaml$'; then
                echo "❌ ERROR: File $file exceeds 1000 lines ($lines lines)"
                exit 1
            fi
        fi
    done

    # Check required directories
    required_dirs=("proto/openapi" "services" "knowledge" "infrastructure")
    for dir in "${required_dirs[@]}"; do
        if [ ! -d "$dir" ]; then
            echo "❌ ERROR: Required directory $dir missing"
            exit 1
        fi
    done

    echo "OK Basic validation passed"
fi
EOF

chmod +x "$HOOKS_DIR/pre-commit"

# Install pre-push hook
echo "📤 Installing pre-push hook..."
cat > "$HOOKS_DIR/pre-push" << 'EOF'
#!/bin/bash
# NECPGAME Pre-push Hook
# Runs full validation before pushing to remote
# Issue: #1866

echo "🔍 Running NECPGAME Pre-push Validation..."

# Check if PowerShell is available
if command -v pwsh >/dev/null 2>&1; then
    pwsh -ExecutionPolicy Bypass -File "scripts/validate-architecture.ps1" -Check "all"
    exit $?
elif command -v powershell >/dev/null 2>&1; then
    powershell -ExecutionPolicy Bypass -File "scripts/validate-architecture.ps1" -Check "all"
    exit $?
else
    echo "WARNING  PowerShell not found, skipping advanced validation"
    exit 0
fi
EOF

chmod +x "$HOOKS_DIR/pre-push"

echo ""
echo "OK Git hooks installed successfully!"
echo ""
echo "📋 Installed hooks:"
echo "  • pre-commit: Validates architecture before commits"
echo "  • pre-push: Runs full validation before pushing"
echo ""
echo "🎯 Next steps:"
echo "  1. Test hooks with: git commit -m 'test'"
echo "  2. View hook logs in terminal"
echo "  3. Hooks will prevent commits/pushes with validation errors"
echo ""
echo "📖 For manual validation run:"
echo "  PowerShell: ./scripts/validate-architecture-simple.ps1"
echo "  Advanced:   ./scripts/validate-architecture.ps1"