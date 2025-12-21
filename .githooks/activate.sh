#!/bin/bash
# Git Safety Activation Script

echo "🛡️ ACTIVATING GIT SAFETY PROTECTION..."

# Set hooks path
git config core.hooksPath .githooks

# Make hooks executable
chmod +x .githooks/pre-commit .githooks/wrappers/*.sh .githooks/wrappers/*.bat 2>/dev/null || true

echo "OK Git hooks activated"
echo "OK Pre-commit protection enabled"
echo ""
echo "Optional: Add to PATH for terminal protection:"
echo "  export PATH=\"\$(pwd)/.githooks/wrappers:\$PATH\""
echo ""
echo "🚨 Dangerous commands will be BLOCKED"
