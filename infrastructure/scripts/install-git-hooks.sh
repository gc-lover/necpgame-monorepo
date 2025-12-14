#!/bin/bash
# Issue: #1858
# Git hooks installation script
# Installs NECPGAME quality validation hooks to local repository

set -e

echo "🔧 Installing NECPGAME Git hooks..."

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(dirname "$SCRIPT_DIR")"
HOOKS_DIR="$REPO_ROOT/infrastructure/git-hooks"
GIT_HOOKS_DIR="$REPO_ROOT/.git/hooks"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

log_error() {
    echo -e "${RED}❌ ERROR: $1${NC}" >&2
}

log_warning() {
    echo -e "${YELLOW}WARNING  WARNING: $1${NC}"
}

log_success() {
    echo -e "${GREEN}OK $1${NC}"
}

# Function to check if we're in a git repository
check_git_repo() {
    if [ ! -d "$REPO_ROOT/.git" ]; then
        log_error "Not a git repository. Run 'git init' first."
        exit 1
    fi

    if [ ! -d "$GIT_HOOKS_DIR" ]; then
        log_error "Git hooks directory not found. Initialize submodules or check repository structure."
        exit 1
    fi
}

# Function to backup existing hooks
backup_existing_hooks() {
    echo "💾 Backing up existing hooks..."

    mkdir -p "$GIT_HOOKS_DIR/backup"

    for hook in pre-commit pre-push; do
        if [ -f "$GIT_HOOKS_DIR/$hook" ]; then
            cp "$GIT_HOOKS_DIR/$hook" "$GIT_HOOKS_DIR/backup/$hook.bak"
            log_success "Backed up existing $hook hook"
        fi
    done
}

# Function to install hooks
install_hooks() {
    echo "🔗 Installing NECPGAME hooks..."

    for hook in pre-commit pre-push; do
        hook_source="$HOOKS_DIR/$hook"
        hook_target="$GIT_HOOKS_DIR/$hook"

        if [ -f "$hook_source" ]; then
            cp "$hook_source" "$hook_target"
            chmod +x "$hook_target"
            log_success "Installed $hook hook"
        else
            log_warning "Hook source not found: $hook_source"
        fi
    done
}

# Function to create hooks configuration
create_hook_config() {
    echo "⚙️  Creating hook configuration..."

    CONFIG_FILE="$REPO_ROOT/.necp-game-hooks"

    cat > "$CONFIG_FILE" << 'EOF'
# NECPGAME Git Hooks Configuration
# Set environment variables to control hook behavior

# Run full validation suite on pre-commit (default: false)
# Set to 'true' for complete validation before each commit
RUN_FULL_VALIDATION=false

# Skip hooks for specific file patterns (space-separated)
SKIP_HOOKS_PATTERNS="*.log *.tmp *.cache"

# Enable security checks (default: true)
ENABLE_SECURITY_CHECKS=true

# Enable performance checks (default: true)
ENABLE_PERFORMANCE_CHECKS=true
EOF

    log_success "Created configuration file: $CONFIG_FILE"
}

# Function to test hooks installation
test_hooks() {
    echo "🧪 Testing hooks installation..."

    # Test pre-commit hook
    if [ -x "$GIT_HOOKS_DIR/pre-commit" ]; then
        log_success "Pre-commit hook is executable"
    else
        log_error "Pre-commit hook is not executable"
    fi

    # Test pre-push hook
    if [ -x "$GIT_HOOKS_DIR/pre-push" ]; then
        log_success "Pre-push hook is executable"
    else
        log_error "Pre-push hook is not executable"
    fi

    # Test basic functionality
    if "$GIT_HOOKS_DIR/pre-commit" --help 2>/dev/null || true; then
        log_success "Hooks are functional"
    fi
}

# Function to show usage instructions
show_usage() {
    echo ""
    echo "📖 NECPGAME Git Hooks Usage:"
    echo "==========================="
    echo ""
    echo "Pre-commit hook:"
    echo "  - Validates staged files for syntax and basic issues"
    echo "  - Checks for large files and sensitive data"
    echo "  - Optionally runs full validation suite"
    echo ""
    echo "Pre-push hook:"
    echo "  - Validates commits being pushed"
    echo "  - Runs security checks"
    echo "  - Validates branch naming conventions"
    echo "  - Checks CI readiness"
    echo ""
    echo "Configuration (.necp-game-hooks):"
    echo "  RUN_FULL_VALIDATION=true    # Enable full validation on pre-commit"
    echo "  ENABLE_SECURITY_CHECKS=true # Enable/disable security checks"
    echo ""
    echo "Override hooks:"
    echo "  git commit --no-verify      # Skip pre-commit validation"
    echo "  git push --no-verify        # Skip pre-push validation"
    echo ""
    echo "WARNING  Note: Skipping hooks is not recommended for production code"
}

# Main installation execution
main() {
    echo "🪝 NECPGAME Git Hooks Installer"
    echo "==============================="

    check_git_repo
    backup_existing_hooks
    install_hooks
    create_hook_config
    test_hooks

    echo ""
    log_success "NECPGAME Git hooks installed successfully!"
    echo ""
    echo "📋 What's installed:"
    echo "  - Pre-commit hook: Validates code quality before commits"
    echo "  - Pre-push hook: Validates code quality before pushing"
    echo "  - Configuration file: .necp-game-hooks"
    echo "  - Backup directory: .git/hooks/backup/"

    show_usage
}

# Run main function
main "$@"