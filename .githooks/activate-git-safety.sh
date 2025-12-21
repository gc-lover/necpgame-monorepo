#!/bin/bash

# Git Safety Activation Script
# This script activates git command interception for the current session

echo "ACTIVATING GIT SAFETY SYSTEM..."
echo ""

# Add wrappers to PATH (prepend to override system git)
export PATH="$(pwd)/.githooks/wrappers:$PATH"

# Verify activation
echo "Git safety activated for this session"
echo "PATH updated: $PATH"
echo ""

# Test the protection
echo "Testing protection..."
echo "   (This should work: git --version)"
git --version >/dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "   Safe commands work"
else
    echo "   Safe commands blocked - check PATH"
fi

echo ""
echo "IMPORTANT:"
echo "   This protection is active ONLY in this terminal session."
echo "   To activate in new terminals, run: source .githooks/activate-git-safety.sh"
echo ""
echo "PROTECTION ACTIVE: Dangerous git commands will be BLOCKED!"
echo "   git reset --hard, git clean -fd, git push --force, etc. = BLOCKED"
echo ""
echo "Use 'git safe <command>' as alternative if needed"
echo ""
