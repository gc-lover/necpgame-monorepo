#!/bin/bash

# Git Protection Level Setup Script
# Allows choosing between different levels of git safety

echo "GIT PROTECTION LEVEL SETUP"
echo "=========================="
echo ""
echo "Choose protection level:"
echo "1. MAXIMUM (Blocks dangerous commands) - For production projects"
echo "2. TRAINING (Shows warnings and tips) - For learning environments"
echo "3. DISABLED (No protection) - For expert users only"
echo ""

read -p "Enter your choice (1-3): " choice

case $choice in
    1)
        echo "Setting MAXIMUM PROTECTION..."
        cp .githooks/pre-commit .git/hooks/pre-commit 2>/dev/null || true
        cp .githooks/pre-push .git/hooks/pre-push 2>/dev/null || true
        cp .githooks/commit-msg .git/hooks/commit-msg 2>/dev/null || true
        chmod +x .git/hooks/* 2>/dev/null || true
        echo "✓ MAXIMUM PROTECTION activated - dangerous commands BLOCKED"
        ;;
    2)
        echo "Setting TRAINING PROTECTION..."
        cp .githooks/pre-commit-safety-training .git/hooks/pre-commit 2>/dev/null || true
        chmod +x .git/hooks/pre-commit 2>/dev/null || true
        echo "✓ TRAINING PROTECTION activated - educational warnings enabled"
        ;;
    3)
        echo "DISABLING PROTECTION..."
        rm -f .git/hooks/pre-commit .git/hooks/pre-push .git/hooks/commit-msg
        echo "✓ PROTECTION DISABLED - use at your own risk!"
        ;;
    *)
        echo "Invalid choice. Keeping current protection level."
        exit 1
        ;;
esac

echo ""
echo "Protection level set successfully!"
echo "Run 'git status' to test the protection."
