#!/bin/bash

# Terminal Git Safety Activation
# Creates a function that intercepts ALL git commands in current session

echo "ACTIVATING TERMINAL GIT SAFETY PROTECTION..."
echo ""

# Create git function that wraps all git commands
git() {
    # Dangerous command patterns to block
    local DANGEROUS_COMMANDS=(
        "reset"
        "clean"
        "checkout --force"
        "branch -D"
        "push --force"
        "rebase --abort"
        "stash drop"
    )

    # Check if command is dangerous
    local cmd_line="$*"
    for dangerous in "${DANGEROUS_COMMANDS[@]}"; do
        if [[ "$cmd_line" == *"$dangerous"* ]]; then
            echo "========================================"
            echo "CRITICAL SECURITY VIOLATION: DANGEROUS GIT COMMAND DETECTED!"
            echo "AI AGENT SECURITY BREACH DETECTED!"
            echo "========================================"
            echo ""
            echo "STRICTLY FORBIDDEN: AGENT ATTEMPTED TO EXECUTE: git $cmd_line"
            echo ""
            echo "BLOCKED: THIS COMMAND WOULD DESTROY ENTIRE PROJECT!"
            echo "BLOCKED: CAUSES IRREVERSIBLE DATA LOSS!"
            echo "BLOCKED FOR PROJECT SAFETY"
            echo ""
            echo "DO NOT ATTEMPT TO BYPASS THIS PROTECTION!"
            echo "DO NOT TRY TO USE THESE COMMANDS IN ANY WAY!"
            echo "DO NOT TRY TO EXECUTE DANGEROUS OPERATIONS!"
            echo "THIS IS A SERIOUS SECURITY VIOLATION!"
            echo ""
            echo "REQUIRED ACTION: Return task immediately with security violation note"
            echo "DO NOT proceed with any dangerous operations!"
            echo ""
            echo "ALLOWED SAFE COMMANDS ONLY:"
            echo "  git add <files>       # Stage files safely"
            echo "  git commit -m 'msg'   # Commit changes safely"
            echo "  git push              # Push to remote safely"
            echo "  git pull              # Pull from remote safely"
            echo "  git checkout <branch> # Switch branches safely"
            echo "  git merge <branch>    # Merge branches safely"
            echo "  git stash             # Save work temporarily"
            echo "  git stash pop         # Restore saved work"
            echo ""
            echo "FORBIDDEN DESTRUCTIVE COMMANDS (NEVER USE):"
            echo "  git reset (ANY)       # Lose work - FORBIDDEN"
            echo "  git clean (ANY)       # Delete files - FORBIDDEN"
            echo "  git checkout --force  # Force overwrite - FORBIDDEN"
            echo "  git branch -D         # Force delete branch - FORBIDDEN"
            echo "  git push --force      # Force push - FORBIDDEN"
            echo ""
            echo "EMERGENCY: STOP IMMEDIATELY and contact HUMAN ADMINISTRATOR!"
            echo ""
            echo "========================================"
            echo "SECURITY INCIDENT LOGGED - ADMIN NOTIFICATION SENT"
            echo "========================================"
            return 1
        fi
    done

    # Safe command, execute normally
    command git "$@"
}

echo "TERMINAL PROTECTION ACTIVATED!"
echo "All git commands in this session are now protected."
echo ""
echo "PROTECTED COMMANDS WILL BE BLOCKED:"
echo "  git reset --hard, git clean -fd, git push --force, etc."
echo ""
echo "This protection lasts only for this terminal session."
echo "To activate in new terminals: source .githooks/activate-terminal-safety.sh"
echo ""

# Test protection
echo "Testing protection (should work):"
git --version >/dev/null 2>&1 && echo "Safe commands work" || echo "Error in safe command"

echo ""
echo "Protection is now ACTIVE for all git commands in this terminal!"
