#!/bin/bash

# Git Safe Wrapper
# This script intercepts dangerous git commands and blocks them

# Dangerous command patterns to block
DANGEROUS_COMMANDS=(
    "reset --hard"
    "clean -fd"
    "clean -fdx"
    "checkout --force"
    "branch -D"
    "push --force"
    "rebase --abort"
    "stash drop"
)

# Function to check if command is dangerous
is_dangerous_command() {
    local cmd="$1"
    for dangerous in "${DANGEROUS_COMMANDS[@]}"; do
        if [[ "$cmd" == *"$dangerous"* ]]; then
            return 0  # true - is dangerous
        fi
    done
    return 1  # false - safe
}

# Function to show safe commands
show_safe_commands() {
    echo "🚨 DANGER: This git command is BLOCKED for safety reasons!"
    echo ""
    echo "Blocked command: git $@"
    echo ""
    echo "This command can cause irreversible data loss and is forbidden for agents."
    echo ""
    echo "OK SAFE git commands you can use:"
    echo "  git add <files>           # Stage files for commit"
    echo "  git commit -m 'message'   # Commit staged changes"
    echo "  git push                  # Push commits to remote"
    echo "  git pull                  # Pull changes from remote"
    echo "  git checkout <branch>     # Switch to branch"
    echo "  git merge <branch>        # Merge branch"
    echo "  git stash                 # Stash changes"
    echo "  git stash pop             # Restore stashed changes"
    echo ""
    echo "❌ FORBIDDEN commands (cause data loss):"
    echo "  git reset --hard          # Lose all changes"
    echo "  git clean -fd             # Delete untracked files"
    echo "  git clean -fdx            # Delete ALL untracked files"
    echo "  git checkout --force      # Force checkout (overwrite)"
    echo "  git branch -D             # Force delete branch"
    echo "  git push --force          # Force push (overwrite remote)"
    echo ""
    echo "If you need to perform dangerous operations, ask a human administrator."
}

# Main logic
if [ $# -eq 0 ]; then
    # No arguments, just run git
    exec git
fi

# Check if this is a dangerous command
command_line="$*"
if is_dangerous_command "$command_line"; then
    show_safe_commands "$@"
    exit 1
fi

# Safe command, execute normally
exec git "$@"
