#!/bin/bash

# Terminal Git Safety Activation
# Creates a function that intercepts ALL git commands in current session

echo "ACTIVATING TERMINAL GIT SAFETY PROTECTION..."
echo ""

# Create git function that wraps all git commands
git() {
    # Dangerous command patterns to block
    local DANGEROUS_COMMANDS=(
        "reset --hard"
        "clean -fd"
        "clean -fdx"
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
            echo "EMERGENCY BLOCK: DANGEROUS GIT COMMAND DETECTED!"
            echo "========================================"
            echo ""
            echo "AGENT ATTEMPTED TO EXECUTE: git $cmd_line"
            echo ""
            echo "BLOCKED: THIS COMMAND WOULD CAUSE IRREVERSIBLE DATA LOSS!"
            echo "BLOCKED FOR PROJECT SAFETY"
            echo ""
            echo "FORBIDDEN COMMANDS (POTENTIAL DATA LOSS):"
            echo "  - git reset --hard    = Lose ALL uncommitted work"
            echo "  - git clean -fd       = Delete untracked files"
            echo "  - git clean -fdx      = Delete ALL untracked files + ignored"
            echo "  - git checkout --force = Force overwrite local files"
            echo "  - git branch -D       = Force delete branch"
            echo "  - git push --force    = Force overwrite remote branch"
            echo "  - git rebase --abort  = Abort rebase (lose progress)"
            echo "  - git stash drop      = Delete stashed changes forever"
            echo ""
            echo "SAFE ALTERNATIVES:"
            echo "  git add <files>       # Add files to staging"
            echo "  git commit -m 'msg'   # Commit staged changes"
            echo "  git push              # Push to remote safely"
            echo "  git pull              # Pull from remote safely"
            echo "  git checkout <branch> # Switch branches safely"
            echo "  git merge <branch>    # Merge branches safely"
            echo "  git stash             # Save work temporarily"
            echo "  git stash pop         # Restore saved work"
            echo ""
            echo "EMERGENCY: Contact HUMAN ADMINISTRATOR for dangerous operations!"
            echo ""
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
