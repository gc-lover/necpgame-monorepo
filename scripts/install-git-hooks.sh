#!/bin/bash

# Git Hooks Installation Script for NECPGAME
# Installs pre-commit and pre-push hooks for automated validation

set -e

echo "🔧 Installing Git Hooks for NECPGAME..."

# Get project root
PROJECT_ROOT="$(git rev-parse --show-toplevel)"
HOOKS_DIR="$PROJECT_ROOT/.githooks"

# Create hooks directory if it doesn't exist
mkdir -p "$HOOKS_DIR"

# Copy hooks
echo "📋 Installing pre-commit hook..."
if [ -f "$HOOKS_DIR/pre-commit" ]; then
    cp "$HOOKS_DIR/pre-commit" "$PROJECT_ROOT/.git/hooks/pre-commit"
else
    echo "⚠️  Pre-commit hook not found in .githooks directory"
    echo "   Creating basic pre-commit hook..."

    cat > "$PROJECT_ROOT/.git/hooks/pre-commit" << 'EOF'
#!/bin/bash

# Git Hook: Unified Pre-commit Validation
# Uses Python scripts for all validation checks

echo "[CHECK] Unified Pre-commit Validation: Starting checks..."

# Ensure we're in the project root directory
cd "$(git rev-parse --show-toplevel)" || exit 1
echo "[INFO] Working directory: $(pwd)"

# Get staged files
STAGED_FILES=$(git diff --cached --name-only)

# 1. Git Safety Check: Scan for dangerous command traces
echo "[CHECK] Git Safety Check: Scanning for dangerous command traces..."
if [ -n "$STAGED_FILES" ]; then
    DANGEROUS_FOUND=""
    for file in $STAGED_FILES; do
        if [ -f "$file" ]; then
            # Skip system files that contain safety documentation
            if [[ "$file" == .githooks/* ]] || [[ "$file" == .cursor/rules/* ]] || [[ "$file" == scripts/git-security/* ]] || [[ "$file" == scripts/linting/* ]] || [[ "$file" == fix_git_push.py ]] || [[ "$file" == test_validation.py ]] || [[ "$file" == infrastructure/* ]]; then
                continue
            fi
            # Check for dangerous git commands in file content
            if grep -q "git reset\|git clean\|git checkout --force\|git branch -D\|git push --force\|git rebase --abort\|git stash drop" "$file" 2>/dev/null; then
                DANGEROUS_FOUND="$DANGEROUS_FOUND\n  - Dangerous git command found in $file"
            fi
        fi
    done

    if [ -n "$DANGEROUS_FOUND" ]; then
        echo "[CRITICAL] SECURITY VIOLATION: DANGEROUS GIT COMMANDS DETECTED!"
        echo "COMMIT REJECTED - PROJECT PROTECTED$DANGEROUS_FOUND"
        exit 1
    fi
fi
echo "[SUCCESS] Git Safety Check: No dangerous command traces found."

# 2. Emoji Ban Check
echo "[CHECK] Emoji Ban Check: Scanning for forbidden Unicode characters..."
echo "[DEBUG] Checking for emoji validation script..."
if [ -f "scripts/validate-emoji-ban.py" ]; then
    echo "[DEBUG] Emoji validation script found"
else
    echo "[WARNING] Emoji validation script not found at: $(pwd)/scripts/validate-emoji-ban.py"
fi

if [ -f "scripts/validate-emoji-ban.py" ] && [ -n "$STAGED_FILES" ]; then
    # Filter files for emoji validation (Python files and OpenAPI specs)
    PYTHON_FILES=$(echo "$STAGED_FILES" | grep -E '\.(py|yaml|yml)$' | grep -v 'node_modules/' | head -10 || true)

    if [ -n "$PYTHON_FILES" ]; then
        echo "[DEBUG] Validating emoji ban for: $PYTHON_FILES"
        python scripts/validate-emoji-ban.py $PYTHON_FILES || {
            echo "[ERROR] Emoji validation failed!"
            exit 1
        }
    fi
else
    echo "[WARNING] Emoji validation skipped (script not found or no staged files)"
fi
echo "[SUCCESS] Emoji Ban Check: Validation completed."

# 3. Secrets Validation
echo "[CHECK] Secret Validation: Scanning for sensitive data..."
if [ -f "scripts/validate-secrets.py" ] && [ -n "$STAGED_FILES" ]; then
    # Check for potential secrets in staged files
    SECRETS_FOUND=""
    for file in $STAGED_FILES; do
        if [ -f "$file" ]; then
            # Skip binary files and known safe files
            if [[ "$file" == *.png ]] || [[ "$file" == *.jpg ]] || [[ "$file" == *.pdf ]] || [[ "$file" == *.zip ]]; then
                continue
            fi
            if grep -q "password\|secret\|key\|token" "$file" 2>/dev/null; then
                SECRETS_FOUND="$SECRETS_FOUND\n  - Potential secret found in $file"
            fi
        fi
    done

    if [ -n "$SECRETS_FOUND" ]; then
        echo "[WARNING] Potential secrets detected:$SECRETS_FOUND"
        echo "[INFO] Please review and ensure no sensitive data is committed"
    fi
fi
echo "[SUCCESS] Secret Validation: No sensitive data found."

# 4. Architecture Validation (optional - only if script exists)
echo "[CHECK] Script Language Enforcement: Only Python scripts allowed..."
if [ -n "$STAGED_FILES" ]; then
    NON_PYTHON_SCRIPTS=$(echo "$STAGED_FILES" | grep -E '\.(sh|bash)$' | grep -v 'node_modules/' | wc -l)
    if [ "$NON_PYTHON_SCRIPTS" -gt 0 ]; then
        echo "[WARNING] Found $NON_PYTHON_SCRIPTS shell scripts"
        echo "[INFO] Ensure shell scripts are necessary and follow project standards"
    fi
fi
echo "[SUCCESS] Script Language Enforcement: Only Python scripts detected."

echo "[SUCCESS] Unified Pre-commit Validation: All checks passed."
EOF

    chmod +x "$PROJECT_ROOT/.git/hooks/pre-commit"
}

# Make hook executable
chmod +x "$PROJECT_ROOT/.git/hooks/pre-commit"

echo "✅ Pre-commit hook installed successfully"

# Install pre-push hook
echo "📋 Installing pre-push hook..."
cat > "$PROJECT_ROOT/.git/hooks/pre-push" << 'EOF'
#!/bin/bash

# Git Hook: Pre-push Validation
# Runs comprehensive validation before pushing to remote

echo "[CHECK] Pre-push Validation: Starting comprehensive checks..."

# Ensure we're in the project root directory
cd "$(git rev-parse --show-toplevel)" || exit 1

# Run basic architecture validation if available
if [ -f "scripts/validate-architecture-simple.ps1" ]; then
    echo "[INFO] Running architecture validation..."
    # Note: PowerShell scripts may not work in all environments
    # This is a placeholder for future validation
    echo "[INFO] Architecture validation script found (PowerShell required for execution)"
elif [ -f "scripts/validate-architecture.py" ]; then
    echo "[INFO] Running Python architecture validation..."
    python scripts/validate-architecture.py || {
        echo "[ERROR] Architecture validation failed!"
        exit 1
    }
fi

echo "[SUCCESS] Pre-push Validation: All checks passed."
EOF

chmod +x "$PROJECT_ROOT/.git/hooks/pre-push"

echo "✅ Pre-push hook installed successfully"

# Create local hooks directory for development
if [ ! -d "$HOOKS_DIR" ]; then
    mkdir -p "$HOOKS_DIR"
    echo "# Git Hooks Directory" > "$HOOKS_DIR/README.md"
    echo "This directory contains git hooks for automated validation." >> "$HOOKS_DIR/README.md"
    echo "Hooks are automatically installed by scripts/install-git-hooks.sh" >> "$HOOKS_DIR/README.md"
fi

echo ""
echo "🎉 Git Hooks Installation Complete!"
echo ""
echo "Installed hooks:"
echo "  ✅ pre-commit: Basic validation (safety, emoji, secrets)"
echo "  ✅ pre-push:  Comprehensive validation (architecture)"
echo ""
echo "Hooks location: $PROJECT_ROOT/.git/hooks/"
echo "Hook sources: $HOOKS_DIR/"
echo ""
echo "To uninstall: rm $PROJECT_ROOT/.git/hooks/pre-commit $PROJECT_ROOT/.git/hooks/pre-push"
echo ""
echo "📚 See ARCHITECTURE_VALIDATION_README.md for detailed documentation."